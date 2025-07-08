/*
 * Copyright (C) 2023 Cobblemon Contributors
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package com.cobblemon.mod.common.pokemon

enum class Growth(
    val displayName: String,
    val scale: Double
) {
    MICROSCOPIC("Microscopic", 0.33),
    PYGMY("Pygmy", 0.5),
    RUNT("Runt", 0.75),
    SMALL("Small", 0.9),
    ORDINARY("Ordinary", 1.0),
    HUGE("Huge", 1.1),
    GIANT("Giant", 1.25),
    ENORMOUS("Enormous", 1.35),
    GINORMOUS("Ginormous", 1.5);

    fun applyToPokemon(pokemon: Pokemon) {
        pokemon.updatePersistentData {
            putString("MythicalCobblemonSize", name)
        }
        pokemon.scaleModifier = this.scale.toFloat()
    }

    companion object {
        fun getFromPokemon(pokemon: Pokemon): Growth {
            val growthName = pokemon.persistentData.getString("MythicalCobblemonSize")
            return entries.find { it.name == growthName } ?: ORDINARY
        }

        fun getRandomSpawnGrowth(): Growth {
            return when ((1..100).random()) {
                in 1..5 -> PYGMY
                in 6..15 -> RUNT
                in 16..35 -> SMALL
                in 36..65 -> ORDINARY
                in 66..85 -> HUGE
                in 86..95 -> GIANT
                else -> ENORMOUS
            }
        }
    }
}