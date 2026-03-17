package com.example.rickandmorty.data.remote.dto

data class CharactersResponse(
    val info: InfoDto,
    val results: List<CharacterDto>
)

data class InfoDto(
    val count: Int,
    val pages: Int,
    val next: String?,
    val prev: String?
)

data class CharacterDto(
    val id: Int,
    val name: String,
    val status: String? = null,
    val species: String? = null,
    val gender: String? = null,
    val image: String
)
