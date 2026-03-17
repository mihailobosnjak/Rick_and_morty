package com.example.rickandmorty.ui.splash

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.rickandmorty.R
import kotlinx.coroutines.delay

private const val SPLASH_DELAY_MS = 1500L

@Composable
fun SplashRoute(
    modifier: Modifier = Modifier,
    onNavigateToHome: () -> Unit
) {
    LaunchedEffect(Unit) {
        delay(SPLASH_DELAY_MS)
        onNavigateToHome()
    }

    Surface(modifier = modifier.fillMaxSize()) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = stringResource(R.string.splash_title),
                style = MaterialTheme.typography.headlineLarge
            )
        }
    }
}
