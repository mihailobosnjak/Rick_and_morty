package com.example.rickandmorty.navigation

/**
 * Centralizovane rute za Jetpack Navigation.
 * Sve destinacije i argumenti na jednom mestu – lakše održavanje i manje grešaka.
 */
object Routes {

    /** Splash ekran (početna destinacija). */
    const val SPLASH = "splash"

    /** Lista likova. */
    const val CHARACTER_LIST = "character_list"

    /** Detalj lika – zahteva argument [CHARACTER_ID]. */
    const val CHARACTER_DETAIL = "character_detail"

    /** Ime argumenta za ID lika na ekranu detalja. */
    const val CHARACTER_ID = "characterId"

    /**
     * Gradi putanju za ekran detalja sa datim ID-jem.
     * Primer: detailRoute(1) -> "character_detail/1"
     */
    fun characterDetailRoute(characterId: Int): String =
        "$CHARACTER_DETAIL/$characterId"
}
