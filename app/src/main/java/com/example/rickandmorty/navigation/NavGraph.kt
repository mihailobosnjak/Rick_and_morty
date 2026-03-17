package com.example.rickandmorty.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.rickandmorty.ui.characterdetail.CharacterDetailRoute
import com.example.rickandmorty.ui.characterdetail.CharacterDetailViewModel
import com.example.rickandmorty.ui.characterlist.CharacterListRoute
import com.example.rickandmorty.ui.characterlist.CharacterListViewModel
import com.example.rickandmorty.ui.splash.SplashRoute

/**
 * Glavni navigacioni graf aplikacije (Jetpack Navigation Compose).
 * NavHost registruje sve ekrane i mapira rute na Composable sadržaj.
 */
@Composable
fun AppNavGraph(
    modifier: Modifier = Modifier
) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Routes.SPLASH,
        modifier = modifier
    ) {
        composable(Routes.SPLASH) {
            SplashRoute(
                modifier = Modifier.fillMaxSize(),
                onNavigateToHome = {
                    navController.navigate(Routes.CHARACTER_LIST) {
                        popUpTo(Routes.SPLASH) { inclusive = true }
                    }
                }
            )
        }

        composable(Routes.CHARACTER_LIST) {
            val viewModel: CharacterListViewModel = hiltViewModel()
            CharacterListRoute(
                viewModel = viewModel,
                modifier = Modifier.fillMaxSize(),
                onCharacterClick = { character ->
                    navController.navigate(Routes.characterDetailRoute(character.id))
                }
            )
        }

        composable(
            route = "${Routes.CHARACTER_DETAIL}/{${Routes.CHARACTER_ID}}",
            arguments = listOf(
                navArgument(Routes.CHARACTER_ID) { type = NavType.IntType }
            )
        ) { backStackEntry ->
            val viewModel: CharacterDetailViewModel = hiltViewModel(backStackEntry)
            CharacterDetailRoute(
                viewModel = viewModel,
                onBackClick = { navController.popBackStack() }
            )
        }
    }
}
