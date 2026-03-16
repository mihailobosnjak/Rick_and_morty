package com.example.rick_and_morty.ui.characterdetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rick_and_morty.data.repository.CharacterRepository
import com.example.rick_and_morty.data.repository.CharacterRepositoryProvider
import com.example.rick_and_morty.domain.model.CharacterDetail
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class CharacterDetailUiState(
    val isLoading: Boolean = false,
    val character: CharacterDetail? = null,
    val errorMessage: String? = null
)

class CharacterDetailViewModel(
    private val characterId: Int,
    private val repository: CharacterRepository
) : ViewModel() {

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
                        errorMessage = error.localizedMessage ?: "Unknown error"
                    )
                }
            )
        }
    }

    companion object {
        fun default(characterId: Int): CharacterDetailViewModel =
            CharacterDetailViewModel(characterId, CharacterRepositoryProvider.repository)
    }
}
