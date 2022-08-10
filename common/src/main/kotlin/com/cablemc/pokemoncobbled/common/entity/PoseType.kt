package com.cablemc.pokemoncobbled.common.entity

/**
 * The type of a pose. Used for normalizing pose swapping for all models.
 *
 * @author Hiroku
 * @since December 5th, 2021
 */
enum class PoseType {
    STAND,
    WALK,
    SLEEP,
    HOVER,
    FLY,
    FLOAT,
    SWIM,
    /** A pose for rendering statelessly on the left shoulder. Stateless animations are given the player head yaw, pitch, and ageInTicks. */
    SHOULDER_LEFT,
    /** A pose for rendering statelessly on the right shoulder. Stateless animations are given the player head yaw, pitch, and ageInTicks. */
    SHOULDER_RIGHT,
    /** A pose for rendering in the SummaryUI */
    PROFILE,
    /** A pose for rendering in the party overlay and in minor spaces like the battle tiles. */
    PORTRAIT,
    /** A simple type for non-living entities or errant cases. */
    NONE
}