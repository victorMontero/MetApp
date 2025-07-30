package com.example.metapp.ui.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.metapp.data.ArtRepository
import com.example.metapp.utils.Result
import com.example.metapp.utils.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val repository: ArtRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow<UiState>(UiState.Idle)
    val uiState = _uiState.asStateFlow()

    init {
        getEuropeanPaintingsGallery()
    }

    fun searchObjects(query: String) {
        viewModelScope.launch {
            _uiState.update { UiState.Loading }
            try {
                when (val result = repository.searchObjects(query)) {
                    is Result.Success -> {
                        val objectsIds = result.data
                        if (objectsIds.isEmpty()) {
                            _uiState.update { UiState.Success(emptyList()) }
                        } else {
                            val artObjects = coroutineScope {
                                objectsIds.map { id ->
                                    async {
                                        try {
                                            repository.getObjectDetails(id)
                                        } catch (e: Exception) {
                                            null
                                        }
                                    }
                                }
                            }.mapNotNull { it.await() }
                            _uiState.update { UiState.Success(artObjects) }
                        }
                    }

                    is Result.Error -> {
                        _uiState.update { UiState.Error(result.message ?: "Erro ao buscar ids") }
                    }

                    is Result.Loading -> {}
                }
            } catch (e: Exception) {
                _uiState.update { UiState.Error(e.message ?: "Erro inesperado") }
            }
        }
    }

    fun getEuropeanPaintingsGallery() {
        viewModelScope.launch {
            _uiState.update { UiState.Loading }
            try {
                when (val result = repository.getEuropeanPaintingsGallery()) {
                    is Result.Success -> {
                        val artObjects = result.data

                        _uiState.update { UiState.Success(artObjects) }
                    }

                    is Result.Error -> _uiState.update {
                        UiState.Error(
                            result.message ?: "Erro ao buscar artes"
                        )
                    }

                    is Result.Loading -> {}
                }
            } catch (e: Exception) {
                _uiState.update { UiState.Error(e.message ?: "Erro inesperado") }
            }
        }
    }
}