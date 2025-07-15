package com.example.metapp.ui.search

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.metapp.data.ArtRepository
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

class SearchViewModel @Inject constructor(
    private val repository: ArtRepository
) : ViewModel() {

}