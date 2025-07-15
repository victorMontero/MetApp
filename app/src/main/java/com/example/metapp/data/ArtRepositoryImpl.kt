package com.example.metapp.data

import com.example.metapp.domain.models.ArtObject
import com.example.metapp.utils.Result

class ArtRepositoryImpl(private val service: ApiService): ArtRepository {

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
}