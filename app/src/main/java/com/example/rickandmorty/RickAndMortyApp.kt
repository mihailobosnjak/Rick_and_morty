package com.example.rickandmorty

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.rickandmorty.navigation.AppNavGraph
import com.example.rickandmorty.ui.theme.AppTheme

@Composable
fun RickAndMortyApp() {
    AppTheme {
        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
            AppNavGraph(modifier = Modifier.padding(innerPadding).fillMaxSize())
        }
    }
}
