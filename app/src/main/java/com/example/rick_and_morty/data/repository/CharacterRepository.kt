package com.example.rick_and_morty.data.repository

import com.example.rick_and_morty.data.mapper.toDetail
import com.example.rick_and_morty.data.mapper.toDomain
import com.example.rick_and_morty.data.remote.RemoteClient
import com.example.rick_and_morty.domain.model.Character
import com.example.rick_and_morty.domain.model.CharacterDetail

interface CharacterRepository {
    suspend fun getCharacters(): Result<List<Character>>
    suspend fun getCharacterById(id: Int): Result<CharacterDetail>
}

class CharacterRepositoryImpl : CharacterRepository {

    private val api = RemoteClient.api

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

object CharacterRepositoryProvider {
    val repository: CharacterRepository = CharacterRepositoryImpl()
}

