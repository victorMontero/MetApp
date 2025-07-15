package com.example.metapp.domain.models

data class SearchResponse(
    val total: Int,
    val objectIDs: List<Int>?
)
