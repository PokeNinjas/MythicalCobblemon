/*
 * Copyright (C) 2023 Cobblemon Contributors
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package com.cobblemon.mod.common.pokeball.catching.calculators

import com.cobblemon.mod.common.api.pokeball.catching.CaptureContext
import com.cobblemon.mod.common.api.pokeball.catching.calculators.CaptureCalculator
import com.cobblemon.mod.common.api.pokeball.catching.calculators.CriticalCaptureProvider
import com.cobblemon.mod.common.entity.pokeball.EmptyPokeBallEntity
import com.cobblemon.mod.common.entity.pokemon.PokemonEntity
import com.cobblemon.mod.common.pokemon.status.statuses.persistent.BurnStatus
import com.cobblemon.mod.common.pokemon.status.statuses.persistent.FrozenStatus
import com.cobblemon.mod.common.pokemon.status.statuses.persistent.ParalysisStatus
import com.cobblemon.mod.common.pokemon.status.statuses.persistent.PoisonBadlyStatus
import com.cobblemon.mod.common.pokemon.status.statuses.persistent.PoisonStatus
import com.cobblemon.mod.common.pokemon.status.statuses.persistent.SleepStatus
import kotlin.math.max
import kotlin.math.pow
import kotlin.math.roundToInt
import kotlin.random.Random
import net.minecraft.world.entity.LivingEntity
import net.minecraft.server.level.ServerPlayer

/**
 * An implementation of the capture calculator used in the generation 8 games.
 * For more information see the [Bulbapedia](https://bulbapedia.bulbagarden.net/wiki/Catch_rate#Capture_method_.28Generation_VIII.29) page.
 *
 * @author Licious
 * @since January 29th, 2022
 */
object Gen8CaptureCalculator : CaptureCalculator, CriticalCaptureProvider {

    override fun id(): String = "generation_8"

    override fun processCapture(thrower: LivingEntity, pokeBallEntity: EmptyPokeBallEntity, target: PokemonEntity): CaptureContext {
        val pokeBall = pokeBallEntity.pokeBall
        val pokemon = target.pokemon
        if (pokeBall.catchRateModifier.isGuaranteed()) {
            return CaptureContext.successful()
        }
        val catchRate = getCatchRate(thrower, pokeBallEntity, target, pokemon.form.catchRate.toFloat())
        val validModifier = pokeBall.catchRateModifier.isValid(thrower, pokemon)
        val bonusStatus = when (pokemon.status?.status) {
            is SleepStatus, is FrozenStatus -> 2.5F
            is ParalysisStatus, is BurnStatus, is PoisonStatus, is PoisonBadlyStatus -> 1.5F
            else -> 1F
        }
        val bonusLevel = if (pokemon.level < 20) max((30 - pokemon.level) / 10, 1) else 1
        // ToDo implement difficulty when we have dynamax raids
        val ballBonus = if (validModifier) pokeBall.catchRateModifier.value(thrower, pokemon) else 1F
        val modifiedCatchRate = (pokeBall.catchRateModifier.behavior(thrower, pokemon).mutator((3F * pokemon.maxHealth - 2F * pokemon.currentHealth) * catchRate, ballBonus) / (3F * pokemon.maxHealth)) * bonusStatus * bonusLevel
        val critical = if (thrower is ServerPlayer) this.shouldHaveCriticalCapture(thrower, modifiedCatchRate) else false
        val shakeProbability = (65536F / (255F / modifiedCatchRate).pow(0.1875F)).roundToInt()
        var shakes = 0
        repeat(4) {
            val n = Random.nextInt(65537)
            if (n < shakeProbability) {
                shakes++
            }
            if (it == 0 && critical) {
                return CaptureContext(numberOfShakes = 1, isSuccessfulCapture = shakes == 1, isCriticalCapture = true)
            }
        }
        return CaptureContext(numberOfShakes = shakes, isSuccessfulCapture = shakes == 4, isCriticalCapture = false)
    }

}