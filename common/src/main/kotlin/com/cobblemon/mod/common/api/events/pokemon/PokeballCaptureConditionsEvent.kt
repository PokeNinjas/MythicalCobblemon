/*
 * Copyright (C) 2023 Cobblemon Contributors
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package com.cobblemon.mod.common.api.events.pokemon

import com.cobblemon.mod.common.api.events.Cancelable
import com.cobblemon.mod.common.entity.pokeball.EmptyPokeBallEntity
import com.cobblemon.mod.common.entity.pokemon.PokemonEntity
import net.minecraft.text.MutableText
import net.minecraft.text.Text

/**
 * CUSTOM: MythicalNetwork - For MythicalRadars/MythicalCobbled
 *
 * Event fired when a
 *
 * @param
 */
data class PokeballCaptureConditionsEvent(
    val pokemonEntity: PokemonEntity,
    val captureState: EmptyPokeBallEntity.CaptureState,
    val pokeBallEntity: EmptyPokeBallEntity
) : Cancelable() {
    private var failMessage: MutableText? = null

    fun setFailMessage(message: MutableText) {
        failMessage = message
    }

    fun getFailMessage(): MutableText? {
        return failMessage
    }

    fun getFailMessageOrDefault(): MutableText {
        return failMessage ?: Text.of("You can't catch this pokemon!") as MutableText
    }
}