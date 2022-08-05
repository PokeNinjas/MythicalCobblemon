package com.cablemc.pokemoncobbled.common.pokemon.evolution.requirements

import com.cablemc.pokemoncobbled.common.pokemon.Pokemon
import com.cablemc.pokemoncobbled.common.pokemon.evolution.requirements.template.EntityQueryRequirement
import net.minecraft.entity.LivingEntity
import net.minecraft.util.Identifier
import net.minecraft.world.World

/**
 * A [EntityQueryRequirement] for when a [Pokemon] is expected to be in a [World].
 *
 * @property identifier The [Identifier] of the [World] the queried entity is expected to be in.
 * @author Licious
 * @since March 21st, 2022
 */
class LevelRequirement(val identifier: Identifier) : EntityQueryRequirement() {

    override fun check(pokemon: Pokemon, queriedEntity: LivingEntity): Boolean {
        return queriedEntity.world.registryKey.value == this.identifier
    }

    companion object {

        internal const val ADAPTER_VARIANT = "world"

    }

}