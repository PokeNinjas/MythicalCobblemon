package com.cobblemon.mod.common.api.events.entity

import com.cobblemon.mod.common.entity.pokemon.PokemonEntity
import net.minecraft.entity.ai.goal.GoalSelector

data class PokemonEntityGoalsEvent(
    val entity: PokemonEntity,
    val goalSelector: GoalSelector
) {
}