package com.cobblemon.mod.common.api.events.client

import com.cobblemon.mod.common.entity.pokemon.PokemonEntity
import net.minecraft.client.render.VertexConsumerProvider
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.entity.Entity
import net.minecraft.util.math.Vec3d
import net.minecraft.util.math.Vector4f

class RenderBeamEvent(matrixStack: MatrixStack, partialTicks: Float, entity: PokemonEntity, beamTarget: Entity, colour: Vector4f, buffer: VertexConsumerProvider, val sourcePosition: Vec3d)