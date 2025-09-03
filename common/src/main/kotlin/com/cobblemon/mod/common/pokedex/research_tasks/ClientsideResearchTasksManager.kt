/*
 * Copyright (C) 2023 Cobblemon Contributors
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package com.cobblemon.mod.common.pokedex.research_tasks

import com.cobblemon.mod.common.api.net.ClientNetworkPacketHandler
import com.cobblemon.mod.common.client.gui.pokedex.PokedexGUI
import com.cobblemon.mod.common.client.gui.pokedex.widgets.ResearchTasksScrollingWidget
import com.cobblemon.mod.common.config.research_tasks.ResearchTaskConfig
import com.cobblemon.mod.common.net.messages.client.pokedex.ResearchTasksAllCompletedPacket
import com.cobblemon.mod.common.net.messages.client.pokedex.ResearchTasksInfoPacket
import net.minecraft.client.Minecraft
import kotlin.collections.associateWith

object ClientsideResearchTasksManager : ClientNetworkPacketHandler<ResearchTasksInfoPacket> {
    val progress: MutableMap<String, Map<String, Int>> = hashMapOf() // Species string -> map of research task full identifier -> progress
    val tasks: MutableMap<String, List<ResearchTaskConfig>> = hashMapOf() // Species string -> list of tasks

    override fun handle(packet: ResearchTasksInfoPacket, client: Minecraft) {
        progress[packet.species] = packet.progress
        tasks[packet.species] = packet.tasks

        if (client.screen is PokedexGUI) {
            val tabInfoElement = (client.screen as PokedexGUI).tabInfoElement
            if (tabInfoElement is ResearchTasksScrollingWidget) {
                val map = (packet.tasks).associateWith { packet.progress[ResearchTask(it.task, it.target).getFullIdentifier()] ?: 0 }
                tabInfoElement.tasksAndProgress = map
                tabInfoElement.setEntries()
            }
        }
    }

    fun clearStoredData() {
        progress.clear()
    }
}

object ClientsideResearchTasksAllCompletedManager : ClientNetworkPacketHandler<ResearchTasksAllCompletedPacket> {
    val speciesWithAllTasksCompleted: MutableSet<String> = hashSetOf()

    override fun handle(packet: ResearchTasksAllCompletedPacket, client: Minecraft) {
        speciesWithAllTasksCompleted.clear()
        speciesWithAllTasksCompleted.addAll(packet.speciesWithAllTasksCompleted)
    }
}