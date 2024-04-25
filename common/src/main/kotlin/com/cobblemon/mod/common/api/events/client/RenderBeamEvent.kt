/*
 * Copyright (C) 2023 Cobblemon Contributors
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package com.cobblemon.mod.common.api.events.client

import com.cobblemon.mod.common.entity.pokemon.PokemonEntity
import net.minecraft.client.render.VertexConsumerProvider
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.entity.Entity
import net.minecraft.util.math.Vec3d
import org.joml.Vector4f

class RenderBeamEvent(matrixStack: MatrixStack, val partialTicks: Float, entity: PokemonEntity, val beamTarget: Entity, buffer: VertexConsumerProvider, val sourcePosition: Vec3d)