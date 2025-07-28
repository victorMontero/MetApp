package com.example.metapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import coil.compose.AsyncImage
import com.example.metapp.domain.models.ArtObject
import com.example.metapp.ui.search.SearchViewModel
import com.example.metapp.ui.theme.MetAppTheme
import com.example.metapp.utils.UiState
import javax.inject.Inject

class MainActivity : ComponentActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel: SearchViewModel by viewModels { viewModelFactory }

    override fun onCreate(savedInstanceState: Bundle?) {
        (application as MetApplication).applicationComponent.inject(this)
        super.onCreate(savedInstanceState)
        setContent {
            MetAppTheme {
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    val uiState by viewModel.uiState.collectAsState()
                    ArtObjectScreen(uiState)
                }
            }
        }
    }
}

@Composable
fun ArtObjectScreen(uiState: UiState) {
    when (uiState) {
        is UiState.Loading -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }
        is UiState.Success -> {
            if (uiState.data.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Nenhuma obra de arte encontrada.")
                }
            } else {
                ArtObjectList(artObjects = uiState.data)
            }
        }
        is UiState.Error -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(text = "Erro: ${uiState.message}")
            }
        }
        is UiState.Idle -> {
            // Pode mostrar uma tela inicial ou não fazer nada
        }
    }
}

@Composable
fun ArtObjectList(artObjects: List<ArtObject>, modifier: Modifier = Modifier) {
    LazyColumn(
        modifier = modifier.padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(artObjects) { artObject ->
            ArtObjectItem(artObject = artObject)
        }
    }
}

@Composable
fun ArtObjectItem(artObject: ArtObject, modifier: Modifier = Modifier) {
    Card(modifier = modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            AsyncImage(
                model = artObject.primaryImageSmall,
                contentDescription = artObject.title,
                modifier = Modifier
                    .height(180.dp)
                    .fillMaxWidth(),
                contentScale = ContentScale.Crop,
                placeholder = // Você pode adicionar um placeholder aqui
                    null,
                error = // E uma imagem de erro
                    null
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = artObject.title ?: "Título desconhecido",
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = artObject.artistDisplayName ?: "Artista desconhecido",
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}