package com.example.rickandmorty.ui.splash

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.rickandmorty.R
import com.example.rickandmorty.ui.characterlist.CharacterListUiState
import com.example.rickandmorty.ui.characterlist.CharacterListViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

private const val SPLASH_MIN_DURATION_MS = 5000L
private const val LOGO_ANIMATION_DURATION_MS = 1400

@Composable
fun SplashRoute(
    viewModel: CharacterListViewModel,
    modifier: Modifier = Modifier,
    onNavigateToHome: () -> Unit
) {
    val alpha = androidx.compose.runtime.remember { Animatable(0f) }
    val offsetY = androidx.compose.runtime.remember { Animatable(48f) }

    LaunchedEffect(Unit) {
        launch {
            alpha.animateTo(
                targetValue = 1f,
                animationSpec = tween(
                    durationMillis = LOGO_ANIMATION_DURATION_MS,
                    easing = FastOutSlowInEasing
                )
            )
        }
        launch {
            offsetY.animateTo(
                targetValue = 0f,
                animationSpec = tween(
                    durationMillis = LOGO_ANIMATION_DURATION_MS,
                    easing = FastOutSlowInEasing
                )
            )
        }
    }

    LaunchedEffect(Unit) {
        // Čekamo minimum 5 sekundi, ali ostajemo na splash-u dok se podaci ne učitaju.
        val minTimePassed = launch { delay(SPLASH_MIN_DURATION_MS) }
        val loadingFinished = launch {
            viewModel.uiState.filter { state: CharacterListUiState -> !state.isLoading }.first()
        }
        minTimePassed.join()
        loadingFinished.join()
        onNavigateToHome()
    }

    val gradient = Brush.verticalGradient(
        colors = listOf(
            MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.35f),
            MaterialTheme.colorScheme.surface,
            MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.2f)
        )
    )

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(gradient),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .graphicsLayer {
                    translationY = offsetY.value
                    this.alpha = alpha.value
                }
        ) {
            Image(
                painter = painterResource(R.drawable.ic_launcher_foreground),
                contentDescription = null,
                modifier = Modifier.size(120.dp)
            )
            Spacer(modifier = Modifier.height(20.dp))
            Text(
                text = stringResource(R.string.splash_title),
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = stringResource(R.string.splash_loading_hint),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
                modifier = Modifier.alpha(0.85f)
            )
        }
    }
}
