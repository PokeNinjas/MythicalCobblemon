package com.cobblemon.mod.common.entity.pokemon.ai.goals

import com.cobblemon.mod.common.CobblemonEntities
import com.cobblemon.mod.common.entity.pokemon.PokemonEntity
import net.minecraft.entity.EntityPose
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.ai.goal.MeleeAttackGoal

class PokemonMeleeAttackGoal(val pokemonEntity: PokemonEntity) : MeleeAttackGoal(pokemonEntity, 1.0, true) {
    private var lastUpdateTime: Long = 0

    fun canMove() = pokemonEntity.behaviour.moving.walk.canWalk || pokemonEntity.behaviour.moving.fly.canFly// TODO probably depends on whether we're underwater or not

    override fun getSquaredMaxAttackDistance(entity: LivingEntity?): Double {
        val dist = pokemonEntity.getDimensions(EntityPose.STANDING).width - 0.1f
        return (dist * 2.0 * dist * 2.0 + entity!!.getDimensions(EntityPose.STANDING).width).toDouble()
    }

    override fun shouldContinue(): Boolean {
        return super.shouldContinue() && canMove() && !pokemonEntity.pokemon.aspects.contains("alpha_defeated") && !pokemonEntity.isBusy
    }

    override fun canStart(): Boolean {
        return canStartCheck() && canMove() && !pokemonEntity.pokemon.aspects.contains("alpha_defeated") && !pokemonEntity.isBusy
    }

    fun canStartCheck() : Boolean {
        val updateTime = this.mob.world.time
        if(updateTime - this.lastUpdateTime < 20L) {
            return false
        } else {
            this.lastUpdateTime = updateTime
            val target: LivingEntity = this.mob.target ?: return false
            if(!target.isAlive) {
                return false
            } else {
                return this.getSquaredMaxAttackDistance(target) >= this.mob.squaredDistanceTo(target.x, target.y, target.z)
            }
        }
    }
}