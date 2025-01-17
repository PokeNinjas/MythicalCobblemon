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
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.MutableComponent
import net.minecraft.world.entity.Entity

/**
 * CUSTOM: MythicalNetwork - For MythicalRadars/MythicalCobbled
 *
 * Event fired when a capture is attempted
 *
 * @param
 */
data class PokeballCaptureConditionsEvent(
    val pokemonEntity: PokemonEntity,
    val captureState: EmptyPokeBallEntity.CaptureState,
    val pokeBallEntity: EmptyPokeBallEntity,
    val owner: Entity?
) : Cancelable() {
    private var failMessage: MutableComponent? = null

    fun setFailMessage(message: MutableComponent) {
        failMessage = message
    }

    fun getFailMessage(): MutableComponent? {
        return failMessage
    }

    fun getFailMessageOrDefault(): MutableComponent {
        return failMessage ?: Component.literal("You can't catch this pokemon!") as MutableComponent
    }
}
