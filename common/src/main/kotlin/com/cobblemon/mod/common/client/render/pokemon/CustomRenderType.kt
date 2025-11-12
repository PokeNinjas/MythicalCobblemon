/*
 * Copyright (C) 2023 Cobblemon Contributors
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package com.cobblemon.mod.common.client.render.pokemon

import com.cobblemon.mod.common.client.render.models.blockbench.repository.PokemonModelRepository
import com.cobblemon.mod.common.client.render.models.blockbench.repository.RenderContext
import com.mojang.blaze3d.systems.RenderSystem
import com.mojang.blaze3d.vertex.DefaultVertexFormat
import com.mojang.blaze3d.vertex.VertexFormat
import net.minecraft.client.renderer.GameRenderer
import net.minecraft.client.renderer.RenderStateShard
import net.minecraft.client.renderer.RenderType
import net.minecraft.resources.ResourceLocation
import java.util.concurrent.ConcurrentHashMap
import java.util.function.Supplier

object CustomRenderType {
    private val cutout = ConcurrentHashMap<Pair<ResourceLocation, Int>, RenderType>()
    private val translucentCull = ConcurrentHashMap<Pair<ResourceLocation, Int>, RenderType>()

    private fun effectUniformState(effectId: Int): RenderStateShard.TexturingStateShard =
        EffectUniformState(
            "cobb_effect_u_$effectId",
            Runnable {
                val s = RenderSystem.getShader() ?: return@Runnable
                s.safeGetUniform("cobblemon_effectType")?.set(effectId)
                IrisLink.setEffectForEntity(effectId)
            },
            Runnable {
                val s = RenderSystem.getShader() ?: return@Runnable
                s.safeGetUniform("cobblemon_effectType")?.set(0)
                IrisLink.setEffectForEntity(0)
            }
        )

    fun cutout(tex: ResourceLocation, effectId: Int): RenderType =
        cutout.computeIfAbsent(tex to effectId) {
            val shaderState = RenderStateShard.ShaderStateShard(Supplier {
                val s = GameRenderer.getRendertypeEntityCutoutShader()
                val u = s?.safeGetUniform("cobblemon_effectType")
                u?.set(effectId)
                s
            })
            val state = RenderType.CompositeState.builder()
                .setShaderState(shaderState)
                .setTextureState(RenderStateShard.TextureStateShard(tex, false, false))
                .setTransparencyState(RenderStateShard.NO_TRANSPARENCY)
                .setCullState(RenderStateShard.CULL)
                .setLightmapState(RenderStateShard.LIGHTMAP)
                .setOverlayState(RenderStateShard.OVERLAY)
                .setTexturingState(effectUniformState(effectId))
                .createCompositeState(true)

            RenderType.create(
                "cobb_entity_cutout_effect_$effectId",
                DefaultVertexFormat.NEW_ENTITY, VertexFormat.Mode.QUADS,
                256, true, false, state
            )
        }

    fun translucentCull(tex: ResourceLocation, effectId: Int): RenderType =
        translucentCull.computeIfAbsent(tex to effectId) {
            val shaderState = RenderStateShard.ShaderStateShard(Supplier {
                val s = GameRenderer.getRendertypeEntityCutoutShader()
                val u = s?.safeGetUniform("cobblemon_effectType")
                u?.set(effectId)
                s
            })

            val state = RenderType.CompositeState.builder()
                .setShaderState(shaderState)
                .setTextureState(RenderStateShard.TextureStateShard(tex, false, false))
                .setTransparencyState(RenderStateShard.TRANSLUCENT_TRANSPARENCY)
                .setCullState(RenderStateShard.CULL)
                .setLightmapState(RenderStateShard.LIGHTMAP)
                .setOverlayState(RenderStateShard.OVERLAY)
                .setTexturingState(effectUniformState(effectId))
                .createCompositeState(true)

            RenderType.create(
                "cobb_entity_translucent_cull_effect_$effectId",
                DefaultVertexFormat.NEW_ENTITY, VertexFormat.Mode.QUADS,
                256, true, true, state
            )
        }

    fun getEffectId(ctx: RenderContext): Int {
        val state = ctx.request(RenderContext.POSABLE_STATE)
        val species = ctx.request<ResourceLocation>(RenderContext.SPECIES)
        val resolver = species?.let { PokemonModelRepository.variations[it] }
        val name = if (state != null) resolver?.getEffect(state) else null
        return when (name?.lowercase()) {
            "radiant" -> 1
            "magma" -> 2
            "glitch" -> 3
            "galaxy" -> 4
            "matrix" -> 5
            "fireworks" -> 6
            "holographic" -> 7
            else -> 0
        }
    }
}