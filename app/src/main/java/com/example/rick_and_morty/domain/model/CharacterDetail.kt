package com.example.rick_and_morty.domain.model

/**
 * Domen model za ekran detalja lika.
 * Sadrži sva polja potrebna za prikaz: velika slika, ime, status, vrsta, pol.
 */
data class CharacterDetail(
    val id: Int,
    val name: String,
    val imageUrl: String,
    val status: String,
    val species: String,
    val gender: String
)
