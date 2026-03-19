package com.example.rickandmorty.data.repository

import com.example.rickandmorty.data.mapper.toDetail
import com.example.rickandmorty.data.mapper.toDomain
import com.example.rickandmorty.data.remote.RickAndMortyApi
import com.example.rickandmorty.domain.model.Character
import com.example.rickandmorty.domain.model.CharacterDetail

/** Rezultat stranice: lista likova i da li postoji sledeća stranica. */
data class CharacterPageResult(
    val characters: List<Character>,
    val hasNextPage: Boolean
)

interface CharacterRepository {
    /** Učitava jednu stranicu likova (API vraća 20 po stranici). Stranica 1 = prvih 20. */
    suspend fun getCharactersPage(page: Int): Result<CharacterPageResult>
    suspend fun getCharacterById(id: Int): Result<CharacterDetail>
}

class CharacterRepositoryImpl(
    private val api: RickAndMortyApi
) : CharacterRepository {

    override suspend fun getCharactersPage(page: Int): Result<CharacterPageResult> {
        return try {
            val response = api.getCharacters(page)
            val characters = response.results.map { it.toDomain() }
            val hasNextPage = response.info.next != null
            Result.success(CharacterPageResult(characters, hasNextPage))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getCharacterById(id: Int): Result<CharacterDetail> {
        return try {
            val dto = api.getCharacter(id)
            Result.success(dto.toDetail())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}

