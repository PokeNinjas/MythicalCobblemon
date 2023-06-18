package com.cobblemon.mod.common.entity.pokemon.ai.goals

import com.cobblemon.mod.common.entity.pokemon.PokemonBehaviourFlag
import com.cobblemon.mod.common.entity.pokemon.PokemonEntity
import com.cobblemon.mod.common.util.isHeadLookingAt
import com.cobblemon.mod.common.util.isLookingAt
import com.cobblemon.mod.common.util.math.toRGB
import com.cobblemon.mod.common.util.sendParticlesServer
import net.minecraft.entity.EntityPose
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.ai.goal.Goal
import net.minecraft.entity.projectile.SmallFireballEntity
import net.minecraft.particle.DustParticleEffect
import net.minecraft.particle.ParticleTypes
import net.minecraft.sound.SoundEvents
import net.minecraft.util.math.MathHelper
import net.minecraft.util.math.Vec3d
import net.minecraft.util.math.Vec3f
import net.minecraft.world.World
import kotlin.math.cos
import kotlin.math.sin

class PokemonBreathAttackGoal(val mob: PokemonEntity, val speed: Double, val attackInterval: Int, val maxRange: Float, val breathDuration: Int, val breathCooldown: Int) :
    Goal() {
    private var target: LivingEntity? = null
    private var targetNotVisibleTicks: Int = 0
    private var cooldown: Int = -1
    private var squaredMaxRange: Float = maxRange * maxRange
    private var left: Boolean = false
    private var backwards: Boolean = false
    private var isBreathing: Boolean = false
    private var breathTicks: Int = 0
    private var attackCooldownTicks: Int = 0
    override fun canStart(): Boolean {
        val livingEntity: LivingEntity = this.mob.target ?: return false
        if (livingEntity.isAlive && canMove() && this.mob.canTarget(livingEntity)) {
            this.target = livingEntity
            return true
        }
        return false
    }

    fun canMove() =
        mob.behaviour.moving.walk.canWalk || mob.behaviour.moving.fly.canFly// TODO probably depends on whether we're underwater or not

    override fun shouldContinue(): Boolean {
        return (canStart() || this.target?.isAlive ?: false && !this.mob.navigation.isIdle) && canMove() && !mob.pokemon.aspects.contains(
            "alpha_defeated"
        ) && !mob.isBusy
    }

    fun isTargetTooClose(): Boolean {
        return getAttackDistance(this.target!!) < 4.0
    }

    fun getAttackDistance(entity: LivingEntity): Double {
        val dist = mob.getDimensions(EntityPose.STANDING).width - 0.1f
        return (dist * 2.0 * dist * 2.0 + target!!.getDimensions(EntityPose.STANDING).width).toDouble()
    }

    override fun stop() {
        this.target = null
        this.targetNotVisibleTicks = 0
        this.cooldown = -1
    }

    override fun shouldRunEveryTick(): Boolean {
    return !this.mob.pokemon.aspects.contains("alpha_defeated")
    }

    private fun getSquaredMaxAttackDistance(entity: LivingEntity?): Double {
        val dist = mob.getDimensions(EntityPose.STANDING).width - 0.1f
        return (dist * 2.0 * dist * 2.0 + entity!!.getDimensions(EntityPose.STANDING).width).toDouble()
    }

    override fun tick() {
        if(this.mob.pokemon.aspects.contains("alpha_defeated")) return
        this.cooldown--
        this.attackCooldownTicks--
        val target: LivingEntity = this.target ?: return
        val canSee: Boolean = this.mob.canSee(target)
        if (canSee) {
            this.targetNotVisibleTicks = 0
        } else {
            this.targetNotVisibleTicks++
        }

        val distance: Double = this.mob.squaredDistanceTo(target)
        if (distance < getSquaredMaxAttackDistance(target) && !isBreathing) {
            if (this.mob.random.nextFloat() < 0.25 && canMove()) {
                var toTakeoff: Int = if(this.mob.random.nextFloat() < 0.25) {
                    this.mob.addVelocity(0.0, 1.0, 0.0)
                    this.mob.setBehaviourFlag(PokemonBehaviourFlag.FLYING, true)
                    this.mob.random.nextInt(10)
                } else { 0 }
                this.mob.moveControl.moveTo(target.x, target.y + toTakeoff , target.z, this.speed)
            } else if (canSee && canMove() && this.attackCooldownTicks <= 0) {
                this.mob.moveControl.moveTo(target.x, target.y, target.z, this.speed)
                this.mob.tryAttack(target)
                // offset the particle by half a block towards the target
                val offset = Vec3d(target.x - this.mob.x, target.y - this.mob.y, target.z - this.mob.z).normalize().multiply(1.5)
                this.mob.world.sendParticlesServer(ParticleTypes.SWEEP_ATTACK, offset, 1, Vec3d.ZERO, 0.0)
                this.mob.playSound(SoundEvents.ENTITY_PLAYER_ATTACK_SWEEP, 1.0f, 1.0f)
                this.attackCooldownTicks = this.attackInterval
            }
        }  else if (distance < this.squaredMaxRange && canSee && this.cooldown <= 0) {
            if(this.mob.random.nextFloat() < 0.3) {
                this.left = !this.left
            }
            if(this.mob.random.nextFloat() < 0.3) {
                this.backwards = !this.backwards
            }
            if(!isBreathing && this.mob.random.nextFloat() < 0.25f){
                isBreathing = true
                breathTicks = breathDuration
                this.mob.playSound(SoundEvents.ENTITY_ENDER_DRAGON_GROWL, 1.0f, 1.0f)
            } else if (!isBreathing && this.mob.random.nextFloat() > 0.25 && this.mob.random.nextFloat() < 0.5){
                val xVel: Double = this.mob.random.nextTriangular(target.x - this.mob.x, 2.297)
                val zVel: Double = this.mob.random.nextTriangular(target.z - this.mob.z, 2.297)
                val yVel: Double = target.getBodyY(0.5) - this.mob.getBodyY(0.5)
                val smallFireballEntity: SmallFireballEntity = SmallFireballEntity(this.mob.world, this.mob, xVel, yVel, zVel)
                smallFireballEntity.setPos(this.mob.x, this.mob.y + this.mob.standingEyeHeight.toDouble(), this.mob.z)
                this.mob.world.spawnEntity(smallFireballEntity)
                this.mob.playSound(SoundEvents.ENTITY_BLAZE_SHOOT, 1.0f, 1.0f)
                this.cooldown = this.mob.random.nextBetween(this.breathCooldown / 2, this.breathCooldown)
            }
        } else if (this.targetNotVisibleTicks < 5) {
            this.mob.moveControl.moveTo(target.x, target.y, target.z, this.speed)
        }
        if (isBreathing){
            breathTicks--
            this.mob.lookControl.lookAt(target, 30.0f, 30.0f)
            for (i in 0 until 10) {
                spawnParticles()
            }
            if(breathTicks == 0){
                isBreathing = false
            }
            if(!isBreathing && breathTicks == 0){
                this.cooldown = this.mob.random.nextBetween(this.breathCooldown / 2, this.breathCooldown)
            }
            if(distance < this.squaredMaxRange) {
                if (canSee && this.mob.isHeadLookingAt(target, maxRange)) {
                    this.mob.lookControl.lookAt(target, 30.0f, 30.0f)
                    this.mob.tryAttack(target)
                }
            }
        } else {
            this.mob.moveControl.strafeTo(if (this.backwards) -0.5f else 0.5f, if (this.left) -0.5f else 0.5f)
            this.mob.lookControl.lookAt(target, 30.0f, 30.0f)
        }
    }

    private fun spawnParticles() {
        // spawn particles in a straight line from the mob to the target
        val level: World = this.mob.world
        val startPos: Vec3d = this.mob.pos.add(0.0, this.mob.standingEyeHeight.toDouble(), 0.0)
        val direction: Vec3d = Vec3d.fromPolar(0.0F, this.mob.headYaw)
        val endPos: Vec3d = startPos.add(direction.multiply(this.maxRange.toDouble()))
        val distance: Double = startPos.distanceTo(endPos).coerceAtMost(this.maxRange.toDouble())
        val step: Double = 0.5
        val steps: Int = (distance / step).toInt()
        val color = mob.pokemon.primaryType.hue.toRGB()
        for (i in 0 until steps) {
            if(this.mob.random.nextFloat() < 0.75) continue
            val pos = startPos.add(direction.multiply(step * i))
            val particle: DustParticleEffect = DustParticleEffect(
                Vec3f(
                    color.first.toFloat(), color.second.toFloat(),
                    color.third.toFloat()
                ), 1.0f
            )
            level.sendParticlesServer(
                ParticleTypes.FLAME,
                pos,
                1,
                Vec3d((mob.random.nextDouble() - 0.5)*i/10.0, (mob.random.nextDouble() - 0.5)*i/10.0, (mob.random.nextDouble() - 0.5)*i/10.0),
                0.025
            )
        }
    }
}