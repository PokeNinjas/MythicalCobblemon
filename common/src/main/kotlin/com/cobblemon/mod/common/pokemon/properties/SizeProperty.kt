/*
 * Copyright (C) 2023 Cobblemon Contributors
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package com.cobblemon.mod.common.pokemon.properties

import com.cobblemon.mod.common.api.properties.CustomPokemonProperty
import com.cobblemon.mod.common.api.properties.CustomPokemonPropertyType
import com.cobblemon.mod.common.entity.pokemon.PokemonEntity
import com.cobblemon.mod.common.pokemon.Growth
import com.cobblemon.mod.common.pokemon.Pokemon

class SizeProperty(private val growth: Growth) : CustomPokemonProperty {
    override fun asString(): String = "size=${growth.name.lowercase()}"

    override fun apply(pokemon: Pokemon) {
        growth.applyToPokemon(pokemon)
    }
    override fun apply(pokemonEntity: PokemonEntity) = apply(pokemonEntity.pokemon)

    override fun matches(pokemon: Pokemon): Boolean =
        Growth.getFromPokemon(pokemon) == growth

    companion object : CustomPokemonPropertyType<SizeProperty> {
        override val keys = listOf("size", "growth")
        override val needsKey = true

        override fun fromString(value: String?): SizeProperty? {
            val v = value?.trim() ?: return null
            val g = Growth.entries.firstOrNull {
                it.name.equals(v, true) || it.displayName.equals(v, true)
            } ?: return null
            return SizeProperty(g)
        }

        override fun examples(): Collection<String> =
            Growth.entries.map { it.name.lowercase() }
    }
}