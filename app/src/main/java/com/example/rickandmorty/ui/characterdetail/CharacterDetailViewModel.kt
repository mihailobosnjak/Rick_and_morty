package com.example.rickandmorty.ui.characterdetail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rickandmorty.data.repository.CharacterRepository
import com.example.rickandmorty.domain.model.CharacterDetail
import com.example.rickandmorty.navigation.Routes
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class CharacterDetailUiState(
    val isLoading: Boolean = false,
    val character: CharacterDetail? = null,
    val errorMessage: String? = null
)

@HiltViewModel
class CharacterDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val repository: CharacterRepository
) : ViewModel() {

    private val characterId: Int = checkNotNull(savedStateHandle[Routes.CHARACTER_ID]) {
        "characterId is required for CharacterDetailViewModel"
    }

    private val _uiState = MutableStateFlow(CharacterDetailUiState(isLoading = true))
    val uiState: StateFlow<CharacterDetailUiState> = _uiState.asStateFlow()

    init {
        loadCharacter()
    }

    fun loadCharacter() {
        _uiState.value = CharacterDetailUiState(isLoading = true)

        viewModelScope.launch {
            val result = repository.getCharacterById(characterId)
            _uiState.value = result.fold(
                onSuccess = { detail ->
                    CharacterDetailUiState(
                        isLoading = false,
                        character = detail,
                        errorMessage = null
                    )
                },
                onFailure = { error ->
                    CharacterDetailUiState(
                        isLoading = false,
                        character = null,
                        errorMessage = error.localizedMessage
                    )
                }
            )
        }
    }
}
