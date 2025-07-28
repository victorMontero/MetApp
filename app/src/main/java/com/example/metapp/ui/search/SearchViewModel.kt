package com.example.metapp.ui.search

import android.R.attr.data
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.metapp.data.ArtRepository
import com.example.metapp.utils.Result
import com.example.metapp.utils.UiState
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

class SearchViewModel @Inject constructor(
    private val repository: ArtRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow<UiState>(UiState.Idle)
    val uiState = _uiState.asStateFlow()

    init {
        // Inicia a busca mocada assim que o ViewModel é criado
        searchObjects("sunflower")
    }

    fun searchObjects(query: String){
        viewModelScope.launch {
            _uiState.update { UiState.Loading } // Inicia com o estado de Loading
            try {
                when(val result = repository.searchObjects(query)){ // Use a query recebida
                    is Result.Success -> {
                        val objectsIds = result.data.take(20) // Limita a 20 para o MVP
                        if (objectsIds.isEmpty()){
                            _uiState.update { UiState.Success(emptyList()) }
                        } else {
                            val artObjects = coroutineScope {
                                objectsIds.map { id ->
                                    async {
                                        try {
                                            repository.getObjectDetails(id)
                                        } catch (e: Exception) {
                                            null // Retorna nulo se um item falhar
                                        }
                                    }
                                }
                            }.mapNotNull { it.await() } // Aguarda todos e filtra os nulos
                            _uiState.update { UiState.Success(artObjects) }
                        }
                    }
                    is Result.Error -> {
                        _uiState.update { UiState.Error(result.message ?: "Erro ao buscar ids") }
                    }
                    is Result.Loading -> {}
                }
            } catch (e: Exception){
                _uiState.update { UiState.Error(e.message ?: "Erro inesperado") }
            }
        }
    }
}