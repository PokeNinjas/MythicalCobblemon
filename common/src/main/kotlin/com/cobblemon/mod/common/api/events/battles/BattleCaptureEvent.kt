/*
 * Copyright (C) 2023 Cobblemon Contributors
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package com.cobblemon.mod.common.api.events.battles

import com.cobblemon.mod.common.api.battles.model.PokemonBattle
import com.cobblemon.mod.common.battles.ActiveBattlePokemon
import com.cobblemon.mod.common.entity.pokeball.EmptyPokeBallEntity

data class BattleCaptureEvent(
    val battle: PokemonBattle,
    val targetPokemon: ActiveBattlePokemon,
    val pokeBallEntity: EmptyPokeBallEntity,
    val captureState: BattleCaptureState
)

enum class BattleCaptureState {
    SHAKE,
    CAPTURE,
    BREAK_FREE
}
