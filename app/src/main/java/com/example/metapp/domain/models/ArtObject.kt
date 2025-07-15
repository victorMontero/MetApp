package com.example.metapp.domain.models

data class ArtObject(
    val objectID: Int,
    val primaryImage: String?,
    val primaryImageSmall: String?,
    val constituents: List<Constituent>?,
    val department: String?,
    val objectDate: String?,
    val artistDisplayName: String?,
    val artistDisplayBio : String?,
    val title: String?,
    val medium: String?
)
