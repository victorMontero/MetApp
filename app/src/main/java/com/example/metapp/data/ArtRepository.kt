package com.example.metapp.data

import com.example.metapp.domain.models.ArtObject
import com.example.metapp.utils.Result

interface ArtRepository {

    suspend fun searchObjects(query: String): Result<List<Int>>

    suspend fun getObjectDetails(objectId: Int): ArtObject

    suspend fun getEuropeanPaintingsGallery(): Result<List<ArtObject>>

}