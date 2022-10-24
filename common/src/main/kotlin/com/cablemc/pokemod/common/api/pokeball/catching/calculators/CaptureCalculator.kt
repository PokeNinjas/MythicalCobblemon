/*
 * Copyright (C) 2022 Pokemod Cobbled Contributors
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package com.cablemc.pokemod.common.api.pokeball.catching.calculators

import com.cablemc.pokemod.common.api.pokeball.catching.CaptureContext
import com.cablemc.pokemod.common.pokeball.PokeBall
import com.cablemc.pokemod.common.pokemon.Pokemon
import net.minecraft.entity.LivingEntity

/**
 * Used to process Pokémon captures.
 * This interface is here with the intention that several capture calculators can be created,
 * i.e. supporting an earlier generation capture system.
 *
 * @author landonjw
 * @since November 30, 2021
 */
interface CaptureCalculator {

    fun processCapture(thrower: LivingEntity, pokeBall: PokeBall, target: Pokemon, host: Pokemon?) : CaptureContext
}