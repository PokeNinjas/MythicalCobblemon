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
