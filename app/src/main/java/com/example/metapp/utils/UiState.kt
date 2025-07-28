package com.example.metapp.utils

import com.example.metapp.domain.models.ArtObject

sealed class UiState {
    object Idle: UiState()
    object Loading: UiState()
    data class Success(val data: List<ArtObject>) : UiState()
    data class Error(val message: String) : UiState()
}