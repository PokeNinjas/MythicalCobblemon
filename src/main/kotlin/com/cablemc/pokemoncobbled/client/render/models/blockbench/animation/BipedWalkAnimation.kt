package com.cablemc.pokemoncobbled.client.render.models.blockbench.animation

import com.cablemc.pokemoncobbled.client.render.models.blockbench.PoseableEntityModel
import com.cablemc.pokemoncobbled.client.render.models.blockbench.frame.BipedFrame
import net.minecraft.util.Mth
import net.minecraft.world.entity.Entity

/**
 * A biped animation that will have zero-rotations on all legs at
 * stateless and otherwise does simple predictable walking like Minecraft
 * quadrupeds.
 *
 * @author Deltric
 * @since December 21st, 2021
 */
class BipedWalkAnimation<T : Entity>(
    frame: BipedFrame,
    /** The multiplier to apply to the cosine movement of the legs. The smaller this value, the quicker the legs move. */
    val periodMultiplier: Float = 0.6662F,
    /** The multiplier to apply to the stride of the entity. The larger this is, the further the legs move. */
    val amplitudeMultiplier: Float = 1.4F
) : StatelessAnimation<T, BipedFrame>(frame) {
    override val targetFrame: Class<BipedFrame> = BipedFrame::class.java
    override fun setAngles(entity: T?, model: PoseableEntityModel<T>, limbSwing: Float, limbSwingAmount: Float, ageInTicks: Float, headYaw: Float, headPitch: Float) {
        frame.rightLeg.xRot += Mth.cos(limbSwing * periodMultiplier + Math.PI.toFloat()) * limbSwingAmount * amplitudeMultiplier
        frame.leftLeg.xRot += Mth.cos(limbSwing * periodMultiplier) * limbSwingAmount * amplitudeMultiplier
    }
}