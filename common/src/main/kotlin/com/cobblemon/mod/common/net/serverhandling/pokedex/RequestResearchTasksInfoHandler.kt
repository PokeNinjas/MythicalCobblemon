package com.cobblemon.mod.common.net.serverhandling.pokedex

import com.cobblemon.mod.common.Cobblemon
import com.cobblemon.mod.common.api.net.ServerNetworkPacketHandler
import com.cobblemon.mod.common.net.messages.client.pokedex.ResearchTasksInfoPacket
import com.cobblemon.mod.common.net.messages.server.pokedex.RequestResearchTasksInfoPacket
import com.cobblemon.mod.common.pokedex.research_tasks.ComponentRegistry
import net.minecraft.server.MinecraftServer
import net.minecraft.server.level.ServerPlayer

object RequestResearchTasksInfoHandler : ServerNetworkPacketHandler<RequestResearchTasksInfoPacket> {
    override fun handle(
        packet: RequestResearchTasksInfoPacket,
        server: MinecraftServer,
        player: ServerPlayer
    ) {
        val progress = ComponentRegistry.RESEARCH_TASKS_DATA.get(player).getAllProgress(packet.species)
        val tasks = Cobblemon.researchTasksConfig.tasks[packet.species] ?: emptyList()
        ResearchTasksInfoPacket(packet.species, progress, tasks).sendToPlayer(player)
    }
}