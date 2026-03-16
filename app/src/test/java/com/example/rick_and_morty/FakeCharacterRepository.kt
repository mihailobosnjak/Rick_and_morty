package com.example.rick_and_morty

import com.example.rick_and_morty.data.repository.CharacterRepository
import com.example.rick_and_morty.domain.model.Character
import com.example.rick_and_morty.domain.model.CharacterDetail

class FakeCharacterRepository : CharacterRepository {
    var charactersResult: Result<List<Character>> = Result.success(emptyList())
    var characterDetailResult: Result<CharacterDetail> =
        Result.failure(IllegalStateException("No detail configured"))

    override suspend fun getCharacters(): Result<List<Character>> = charactersResult

    override suspend fun getCharacterById(id: Int): Result<CharacterDetail> = characterDetailResult
}


