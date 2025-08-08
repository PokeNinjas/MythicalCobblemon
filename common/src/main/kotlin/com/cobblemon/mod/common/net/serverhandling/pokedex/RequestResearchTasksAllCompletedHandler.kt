package com.cobblemon.mod.common.net.serverhandling.pokedex

import com.cobblemon.mod.common.api.net.ServerNetworkPacketHandler
import com.cobblemon.mod.common.net.messages.client.pokedex.ResearchTasksAllCompletedPacket
import com.cobblemon.mod.common.net.messages.server.pokedex.RequestResearchTasksAllCompletedPacket
import com.cobblemon.mod.common.pokedex.research_tasks.ComponentRegistry
import net.minecraft.server.MinecraftServer
import net.minecraft.server.level.ServerPlayer

object RequestResearchTasksAllCompletedHandler : ServerNetworkPacketHandler<RequestResearchTasksAllCompletedPacket> {
    override fun handle(
        packet: RequestResearchTasksAllCompletedPacket,
        server: MinecraftServer,
        player: ServerPlayer
    ) {
        val set = ComponentRegistry.RESEARCH_TASKS_DATA.get(player).speciesWithAllTasksCompleted
        ResearchTasksAllCompletedPacket(set.toList()).sendToPlayer(player)
    }
}