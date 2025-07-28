package com.example.metapp.data

import com.example.metapp.domain.models.ArtObject
import com.example.metapp.domain.models.SearchResponse
import com.example.metapp.utils.Result
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import javax.inject.Inject // <-- Importe o Inject
import javax.inject.Singleton // <-- Importe o Singleton

@Singleton // Adicione o escopo, já que o ApplicationComponent é Singleton
class ArtRepositoryImpl @Inject constructor(private val service: ApiService): ArtRepository {

    override suspend fun searchObjects(query: String): Result<List<Int>> {
        return try {
            val response = service.searchObjects(query)
            val objectIDs = response.objectIDs
            if (objectIDs != null) {
                Result.Success(objectIDs)
            } else {
                Result.Success(emptyList())
            }
        } catch (e: Exception) {
            Result.Error(e.message, e)
        }
    }

    override suspend fun getObjectDetails(objectId: Int): ArtObject {
        return service.getObjectDetails(objectId)
    }

    override suspend fun getEuropeanPaintingsGallery(): Result<List<ArtObject>> {
        return fetchCuratedGallery(limit = 12) {
            service.searchObjects(departmentId = 11, isOnView = true)
        }
    }

    private suspend fun fetchCuratedGallery(
        limit: Int = 20,
        searchAction: suspend () -> SearchResponse
    ): Result<List<ArtObject>> {
        return try {
            // Etapa 1: Executar a ação de busca para obter os IDs
            val searchResponse = searchAction()
            val objectIDs = searchResponse.objectIDs ?: return Result.Success(emptyList())
            val idsToFetch = objectIDs.take(limit)

            // Etapa 2: Buscar os detalhes em paralelo usando corrotinas
            val artObjects = coroutineScope {
                idsToFetch.map { id ->
                    async {
                        try {
                            service.getObjectDetails(id)
                        } catch (e: Exception) {
                            // Se um único objeto falhar, retornamos null para ele
                            // e o filtramos depois, não quebrando a lista inteira.
                            null
                        }
                    }
                }.mapNotNull { it.await() } // awaitAll() e depois filterNotNull()
            }
            Result.Success(artObjects)

        } catch (e: Exception) {
            // Se a busca inicial de IDs falhar (ex: sem internet), capturamos aqui.
            Result.Error(e.message, e)
        }
    }
}