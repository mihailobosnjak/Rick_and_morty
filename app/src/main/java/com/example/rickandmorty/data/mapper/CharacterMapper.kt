package com.example.rickandmorty.data.mapper

import com.example.rickandmorty.data.remote.dto.CharacterDto
import com.example.rickandmorty.domain.model.Character
import com.example.rickandmorty.domain.model.CharacterDetail

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

