package com.example.metapp.data

import com.example.metapp.domain.models.ArtObject
import com.example.metapp.domain.models.SearchResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @GET("public/collection/v1/search")
    suspend fun searchObjects(
        @Query("q") query: String? = null,
        @Query("departmentId") departmentId: Int? = null,
        @Query("isOnView") isOnView: Boolean? = null,
        @Query("isHighlight") isHighlight: Boolean? = null,
        @Query("dateBegin") dateBegin: Int? = null,
        @Query("dateEnd") dateEnd: Int? = null,
        @Query("hasImages") hasImages: Boolean? = null
    ): SearchResponse

    @GET("public/collection/v1/objects/{objectId}")
    suspend fun getObjectDetails(@Path("objectId") objectId: Int): ArtObject
}