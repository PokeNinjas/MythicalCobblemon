package com.cablemc.pokemoncobbled.common.net.serverhandling.storage

import com.cablemc.pokemoncobbled.common.CobbledNetwork.NetworkContext
import com.cablemc.pokemoncobbled.common.PokemonCobbled
import com.cablemc.pokemoncobbled.common.api.storage.pc.link.PCLinkManager
import com.cablemc.pokemoncobbled.common.net.messages.client.storage.pc.ClosePCPacket
import com.cablemc.pokemoncobbled.common.net.messages.server.storage.SwapPCPartyPokemonPacket
import com.cablemc.pokemoncobbled.common.net.serverhandling.ServerPacketHandler
import net.minecraft.server.network.ServerPlayerEntity

object SwapPCPartyPokemonHandler : ServerPacketHandler<SwapPCPartyPokemonPacket> {
    override fun invokeOnServer(packet: SwapPCPartyPokemonPacket, ctx: NetworkContext, player: ServerPlayerEntity) {
        val party = PokemonCobbled.storage.getParty(player)
        val pc = PCLinkManager.getPC(player) ?: return run { ClosePCPacket().sendToPlayer(player) }

        val partyPokemon = party[packet.partyPosition] ?: return
        val pcPokemon = pc[packet.pcPosition] ?: return

        if (partyPokemon.uuid != packet.partyPokemonID || pcPokemon.uuid != packet.pcPokemonID) {
            return
        }

        party[packet.partyPosition] = pcPokemon
        pc[packet.pcPosition] = partyPokemon
    }
}