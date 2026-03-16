package com.example.rick_and_morty.ui.characterlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rick_and_morty.data.repository.CharacterRepository
import com.example.rick_and_morty.data.repository.CharacterRepositoryProvider
import com.example.rick_and_morty.domain.model.Character
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class CharacterListUiState(
    val isLoading: Boolean = false,
    val characters: List<Character> = emptyList(),
    val errorMessage: String? = null
)

class CharacterListViewModel(
    private val repository: CharacterRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(CharacterListUiState(isLoading = true))
    val uiState: StateFlow<CharacterListUiState> = _uiState.asStateFlow()

    init {
        loadCharacters()
    }

    fun loadCharacters() {
        _uiState.value = CharacterListUiState(isLoading = true)

        viewModelScope.launch {
            val result = repository.getCharacters()
            _uiState.value = result.fold(
                onSuccess = { characters ->
                    CharacterListUiState(
                        isLoading = false,
                        characters = characters,
                        errorMessage = null
                    )
                },
                onFailure = { error ->
                    CharacterListUiState(
                        isLoading = false,
                        characters = emptyList(),
                        errorMessage = error.localizedMessage ?: "Unknown error"
                    )
                }
            )
        }
    }

    companion object {
        fun default(): CharacterListViewModel =
            CharacterListViewModel(CharacterRepositoryProvider.repository)
    }
}

