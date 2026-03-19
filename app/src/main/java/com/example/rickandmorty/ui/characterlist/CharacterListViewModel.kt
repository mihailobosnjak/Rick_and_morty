package com.example.rickandmorty.ui.characterlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rickandmorty.data.repository.CharacterRepository
import com.example.rickandmorty.domain.model.Character
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class CharacterListUiState(
    val isLoading: Boolean = false,
    val isLoadingMore: Boolean = false,
    val characters: List<Character> = emptyList(),
    val hasNextPage: Boolean = true,
    val errorMessage: String? = null
)

@HiltViewModel
class CharacterListViewModel @Inject constructor(
    private val repository: CharacterRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(CharacterListUiState(isLoading = true))
    val uiState: StateFlow<CharacterListUiState> = _uiState.asStateFlow()

    private var currentPage = 0
    /** Buffer: preostalih do 10 likova sa poslednje učitane stranice (API vraća 20), prikazuju se po 10 pri skrolu. */
    private var pendingBuffer: List<Character> = emptyList()
    private var hasNextPageFromApi = true

    init {
        loadCharacters()
    }

    /** Prvo učitavanje: stranica 1 = 20 elemenata. Retry takođe. */
    fun loadCharacters() {
        currentPage = 0
        pendingBuffer = emptyList()
        hasNextPageFromApi = true
        _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)

        viewModelScope.launch {
            val result = repository.getCharactersPage(1)
            currentPage = 1
            _uiState.value = result.fold(
                onSuccess = { pageResult ->
                    hasNextPageFromApi = pageResult.hasNextPage
                    _uiState.value.copy(
                        isLoading = false,
                        characters = pageResult.characters,
                        hasNextPage = pageResult.hasNextPage || pendingBuffer.isNotEmpty()
                    )
                },
                onFailure = { error ->
                    _uiState.value.copy(
                        isLoading = false,
                        characters = emptyList(),
                        errorMessage = error.localizedMessage
                    )
                }
            )
        }
    }

    /** Po 10 pri skrolu: prvo iz buffera, pa sledeća stranica ako treba. */
    fun loadMoreCharacters() {
        val state = _uiState.value
        if (state.isLoadingMore || !state.hasNextPage || state.characters.isEmpty()) return

        if (pendingBuffer.size >= 10) {
            val toAdd = pendingBuffer.take(10)
            pendingBuffer = pendingBuffer.drop(10)
            _uiState.value = state.copy(
                characters = state.characters + toAdd,
                hasNextPage = pendingBuffer.isNotEmpty() || hasNextPageFromApi
            )
            return
        }

        if (pendingBuffer.isNotEmpty()) {
            _uiState.value = state.copy(characters = state.characters + pendingBuffer)
            pendingBuffer = emptyList()
        }

        _uiState.value = _uiState.value.copy(isLoadingMore = true)
        val nextPage = currentPage + 1

        viewModelScope.launch {
            val result = repository.getCharactersPage(nextPage)
            if (result.isSuccess) {
                val pageResult = result.getOrNull()!!
                currentPage = nextPage
                hasNextPageFromApi = pageResult.hasNextPage
                val fromPage = pageResult.characters
                val toShow = fromPage.take(10)
                pendingBuffer = fromPage.drop(10)
                _uiState.value = _uiState.value.copy(
                    isLoadingMore = false,
                    characters = _uiState.value.characters + toShow,
                    hasNextPage = pendingBuffer.isNotEmpty() || hasNextPageFromApi
                )
            } else {
                _uiState.value = _uiState.value.copy(
                    isLoadingMore = false,
                    errorMessage = result.exceptionOrNull()?.localizedMessage
                )
            }
        }
    }
}

