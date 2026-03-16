package com.example.rick_and_morty

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.rick_and_morty.ui.characterdetail.CharacterDetailRoute
import com.example.rick_and_morty.ui.characterdetail.CharacterDetailViewModel
import com.example.rick_and_morty.ui.characterlist.CharacterListRoute
import com.example.rick_and_morty.ui.theme.Rick_and_mortyTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Rick_and_mortyTheme {
                val navController = rememberNavController()
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    NavHost(
                        navController = navController,
                        startDestination = "list",
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        composable("list") {
                            CharacterListRoute(
                                modifier = Modifier.fillMaxSize(),
                                onCharacterClick = { character ->
                                    navController.navigate("detail/${character.id}")
                                }
                            )
                        }
                        composable(
                            route = "detail/{characterId}",
                            arguments = listOf(
                                navArgument("characterId") { type = NavType.IntType }
                            )
                        ) { backStackEntry ->
                            val characterId = backStackEntry.arguments?.getInt("characterId")
                                ?: return@composable
                            val viewModel = CharacterDetailViewModel.default(characterId)

                            CharacterDetailRoute(
                                viewModel = viewModel,
                                onBackClick = { navController.popBackStack() }
                            )
                        }
                    }
                }
            }
        }
    }
}