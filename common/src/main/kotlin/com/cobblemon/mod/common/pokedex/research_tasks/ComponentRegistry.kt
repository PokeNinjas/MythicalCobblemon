/*
 * Copyright (C) 2023 Cobblemon Contributors
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package com.cobblemon.mod.common.pokedex.research_tasks

import com.cobblemon.mod.common.Cobblemon
import net.minecraft.resources.ResourceLocation
import org.ladysnake.cca.api.v3.component.ComponentKey
import org.ladysnake.cca.api.v3.component.ComponentRegistryV3
import org.ladysnake.cca.api.v3.entity.EntityComponentFactoryRegistry
import org.ladysnake.cca.api.v3.entity.EntityComponentInitializer
import org.ladysnake.cca.api.v3.entity.RespawnCopyStrategy

object ComponentRegistry: EntityComponentInitializer {
    val RESEARCH_TASKS_DATA: ComponentKey<PlayerResearchTasksData> = ComponentRegistryV3.INSTANCE.getOrCreate(ResourceLocation.fromNamespaceAndPath(Cobblemon.MODID, "research_tasks_data"), PlayerResearchTasksData::class.java)

    override fun registerEntityComponentFactories(registry: EntityComponentFactoryRegistry) {
        registry.registerForPlayers(RESEARCH_TASKS_DATA, { c ->  PlayerResearchTasksData(c) }, RespawnCopyStrategy.ALWAYS_COPY)
    }
}
