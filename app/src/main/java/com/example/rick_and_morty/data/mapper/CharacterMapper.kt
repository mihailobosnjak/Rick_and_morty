package com.example.rick_and_morty.data.mapper

import com.example.rick_and_morty.data.remote.dto.CharacterDto
import com.example.rick_and_morty.domain.model.Character
import com.example.rick_and_morty.domain.model.CharacterDetail

fun CharacterDto.toDomain(): Character =
    Character(
        id = id,
        name = name,
        imageUrl = image
    )

fun CharacterDto.toDetail(): CharacterDetail =
    CharacterDetail(
        id = id,
        name = name,
        imageUrl = image,
        status = status ?: "Unknown",
        species = species ?: "Unknown",
        gender = gender ?: "Unknown"
    )

