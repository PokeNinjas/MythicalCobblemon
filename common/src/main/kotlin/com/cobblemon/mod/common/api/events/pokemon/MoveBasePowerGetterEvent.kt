package com.cobblemon.mod.common.api.events.pokemon

import com.cobblemon.mod.common.api.pokemon.stats.Stat
import com.cobblemon.mod.common.pokemon.Pokemon
import kotlin.math.max

// CUSTOM: MythicalNetwork event
class MoveBasePowerGetterEvent(val pokemon: Pokemon, val stat: Stat, val initialValue: Int) {
    val modifiers = mutableListOf<(Int) -> (Int)>()

    fun calculateFinalValue(): Int {
        var value = this.initialValue
        modifiers.forEach {
            value = it.invoke(value)
        }

        return value
    }
}