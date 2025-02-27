/*
 * Copyright (C) 2023 Cobblemon Contributors
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package com.cobblemon.mod.common.client.render.models.blockbench.pokemon.gen2

import com.cobblemon.mod.common.client.render.models.blockbench.PosableState
import com.cobblemon.mod.common.client.render.models.blockbench.createTransformation
import com.cobblemon.mod.common.client.render.models.blockbench.frame.HeadedFrame
import com.cobblemon.mod.common.client.render.models.blockbench.pokemon.CryProvider
import com.cobblemon.mod.common.client.render.models.blockbench.pokemon.PokemonPosableModel
import com.cobblemon.mod.common.client.render.models.blockbench.pose.ModelPartTransformation
import com.cobblemon.mod.common.client.render.models.blockbench.pose.Pose
import com.cobblemon.mod.common.entity.PoseType
import com.cobblemon.mod.common.entity.PoseType.Companion.MOVING_POSES
import com.cobblemon.mod.common.entity.PoseType.Companion.STATIONARY_POSES
import com.cobblemon.mod.common.entity.PoseType.Companion.UI_POSES
import net.minecraft.client.model.geom.ModelPart
import net.minecraft.world.phys.Vec3

class WooperModel(root: ModelPart) : PokemonPosableModel(root), HeadedFrame {
    override val rootPart = root.registerChildWithAllChildren("wooper")
    override val head = getPart("head")

    override var portraitScale = 2.0F
    override var portraitTranslation = Vec3(-0.15, -0.65, 0.0)

    override var profileScale = 0.9F
    override var profileTranslation = Vec3(0.0, 0.4, 0.0)

    lateinit var shoulderLeft: Pose
    lateinit var shoulderRight: Pose
    lateinit var standing: Pose
    lateinit var walk: Pose
    lateinit var sleep: Pose
    lateinit var float: Pose
    lateinit var swim: Pose

    override val cryAnimation = CryProvider { bedrockStateful("wooper", "cry") }

    val shoulderOffset = 2.5
    override fun registerPoses() {
        val blink = quirk { bedrockStateful("wooper", "blink") }

        sleep = registerPose(
            poseName = "sleep",
            poseType = PoseType.SLEEP,
            animations = arrayOf(bedrock("wooper", "sleep"))
        )

        standing = registerPose(
            poseName = "standing",
            poseTypes = STATIONARY_POSES + UI_POSES - PoseType.FLOAT,
            quirks = arrayOf(blink),
            animations = arrayOf(
                singleBoneLook(),
                bedrock("wooper", "ground_idle")
            )
        )

        walk = registerPose(
            poseName = "walk",
            poseTypes = MOVING_POSES - PoseType.SWIM,
            quirks = arrayOf(blink),
            animations = arrayOf(
                singleBoneLook(),
                bedrock("wooper", "ground_walk")
            )
        )

        float = registerPose(
            poseType = PoseType.FLOAT,
            quirks = arrayOf(blink),
            animations = arrayOf(
                singleBoneLook(),
                bedrock("wooper", "water_idle")
            )
        )

        swim = registerPose(
            poseType = PoseType.SWIM,
            quirks = arrayOf(blink),
            animations = arrayOf(
                singleBoneLook(),
                bedrock("wooper", "water_swim")
            )
        )

        shoulderLeft = registerPose(
            poseType = PoseType.SHOULDER_LEFT,
            quirks = arrayOf(blink),
            animations = arrayOf(
                singleBoneLook(),
                bedrock("wooper", "ground_idle")
            ),
            transformedParts = arrayOf(
                rootPart.createTransformation().addPosition(ModelPartTransformation.X_AXIS, shoulderOffset)
            )
        )

        shoulderRight = registerPose(
            poseType = PoseType.SHOULDER_RIGHT,
            quirks = arrayOf(blink),
            animations = arrayOf(
                singleBoneLook(),
                bedrock("wooper", "ground_idle")
            ),
            transformedParts = arrayOf(
                rootPart.createTransformation().addPosition(ModelPartTransformation.X_AXIS, -shoulderOffset)
            )
        )
    }

    override fun getFaintAnimation(state: PosableState) = if (state.isNotPosedIn(shoulderRight, shoulderLeft)) bedrockStateful("wooper", "faint") else null
}