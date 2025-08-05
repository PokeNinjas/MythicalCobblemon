package com.cobblemon.mod.common.client.net.pokedex

import com.cobblemon.mod.common.api.net.ClientNetworkPacketHandler
import com.cobblemon.mod.common.client.gui.pokedex.widgets.PokemonInfoWidget
import com.cobblemon.mod.common.net.messages.client.pokedex.PokedexTextureBlacklistPacket
import net.minecraft.client.Minecraft

object PokedexTextureBlacklistHandler : ClientNetworkPacketHandler<PokedexTextureBlacklistPacket> {
    override fun handle(packet: PokedexTextureBlacklistPacket, client: Minecraft) {
        PokemonInfoWidget.textureBlacklist.addAll(packet.blacklist)
    }
}