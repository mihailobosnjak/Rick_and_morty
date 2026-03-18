package com.example.rickandmorty.data.repository

import com.example.rickandmorty.data.mapper.toDetail
import com.example.rickandmorty.data.mapper.toDomain
import com.example.rickandmorty.data.remote.RickAndMortyApi
import com.example.rickandmorty.domain.model.Character
import com.example.rickandmorty.domain.model.CharacterDetail

interface CharacterRepository {
    suspend fun getCharacters(): Result<List<Character>>
    suspend fun getCharacterById(id: Int): Result<CharacterDetail>
}

class CharacterRepositoryImpl(
    private val api: RickAndMortyApi
) : CharacterRepository {

    override suspend fun getCharacters(): Result<List<Character>> {
        return try {
            val response = api.getCharacters()
            val characters = response.results.map { it.toDomain() }
            Result.success(characters)
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

