package com.cobblemon.mod.common.net.serverhandling.pokedex

import com.cobblemon.mod.common.api.net.ServerNetworkPacketHandler
import com.cobblemon.mod.common.api.spawning.CobblemonSpawnPools
import com.cobblemon.mod.common.api.spawning.detail.ClientsidePokedexSpawnDetailInfo
import com.cobblemon.mod.common.net.messages.client.pokedex.PokedexSpawnInfoPacket
import com.cobblemon.mod.common.net.messages.server.pokedex.PokedexRequestSpawnInfoPacket
import net.minecraft.server.MinecraftServer
import net.minecraft.server.level.ServerPlayer

object RequestSpawnInfoHandler : ServerNetworkPacketHandler<PokedexRequestSpawnInfoPacket> {
    override fun handle(
        packet: PokedexRequestSpawnInfoPacket,
        server: MinecraftServer,
        player: ServerPlayer
    ) {
        PokedexSpawnInfoPacket(packet.species, CobblemonSpawnPools.WORLD_SPAWN_POOL.pokemonSpawnsMapForPokedex[packet.species]?.map { ClientsidePokedexSpawnDetailInfo.from(it) } ?: emptyList()).sendToPlayer(player)
    }
}