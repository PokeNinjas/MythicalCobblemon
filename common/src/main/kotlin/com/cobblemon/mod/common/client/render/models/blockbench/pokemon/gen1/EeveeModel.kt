/*
 * Copyright (C) 2023 Cobblemon Contributors
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package com.cobblemon.mod.common.client.render.models.blockbench.pokemon.gen1

import com.cobblemon.mod.common.client.render.models.blockbench.createTransformation
import com.cobblemon.mod.common.client.render.models.blockbench.frame.HeadedFrame
import com.cobblemon.mod.common.client.render.models.blockbench.frame.QuadrupedFrame
import com.cobblemon.mod.common.client.render.models.blockbench.pokemon.CryProvider
import com.cobblemon.mod.common.client.render.models.blockbench.pokemon.PokemonPosableModel
import com.cobblemon.mod.common.client.render.models.blockbench.pose.ModelPartTransformation.Companion.X_AXIS
import com.cobblemon.mod.common.client.render.models.blockbench.pose.Pose
import com.cobblemon.mod.common.entity.PoseType
import com.cobblemon.mod.common.entity.PoseType.Companion.MOVING_POSES
import com.cobblemon.mod.common.entity.PoseType.Companion.STATIONARY_POSES
import com.cobblemon.mod.common.entity.PoseType.Companion.UI_POSES
import net.minecraft.client.model.geom.ModelPart
import net.minecraft.world.phys.Vec3

class EeveeModel(root: ModelPart) : PokemonPosableModel(root), HeadedFrame, QuadrupedFrame {
    override val rootPart = root.registerChildWithAllChildren("eevee")
    val body = getPart("body")
    override val head = getPart("head")
    override val hindRightLeg = getPart("leg_back_right")
    override val hindLeftLeg = getPart("leg_back_left")
    override val foreRightLeg = getPart("leg_front_right")
    override val foreLeftLeg= getPart("leg_front_left")

    override var portraitScale = 2.2F
    override var portraitTranslation = Vec3(-0.4, -0.54, 0.0)

    override var profileScale = 0.8F
    override var profileTranslation = Vec3(0.0, 0.55, 0.0)

    lateinit var stand: Pose
    lateinit var walk: Pose
    lateinit var shoulderLeft: Pose
    lateinit var shoulderRight: Pose

    override val cryAnimation = CryProvider { bedrockStateful("eevee", "cry") }

    val shoulderOffset = 4

    override fun registerPoses() {
        val blink = quirk { bedrockStateful("eevee", "blink") }
        stand = registerPose(
            poseName = "standing",
            poseTypes = STATIONARY_POSES + UI_POSES,
            transformTicks = 10,
            quirks = arrayOf(blink),
            animations = arrayOf(
                singleBoneLook(),
                bedrock("eevee", "ground_idle")
            )
        )

        walk = registerPose(
            poseName = "walk",
            poseTypes = MOVING_POSES,
            transformTicks = 10,
            quirks = arrayOf(blink),
            animations = arrayOf(
                singleBoneLook(),
                bedrock("eevee", "ground_walk")
            )
        )

        shoulderLeft = registerPose(
            poseType = PoseType.SHOULDER_LEFT,
            quirks = arrayOf(blink),
            animations = arrayOf(
                singleBoneLook(),
                bedrock("eevee", "ground_idle")
            ),
            transformedParts = arrayOf(
                rootPart.createTransformation().addPosition(X_AXIS, shoulderOffset)
            )
        )

        shoulderRight = registerPose(
            poseType = PoseType.SHOULDER_RIGHT,
            quirks = arrayOf(blink),
            animations = arrayOf(
                singleBoneLook(),
                bedrock("eevee", "ground_idle")
            ),
            transformedParts = arrayOf(
                rootPart.createTransformation().addPosition(X_AXIS, -shoulderOffset)
            )
        )

    }
}