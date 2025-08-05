package com.cobblemon.mod.common.client.net.pokedex

import com.cobblemon.mod.common.api.net.ClientNetworkPacketHandler
import com.cobblemon.mod.common.api.spawning.detail.ClientsidePokedexSpawnDetailInfo
import com.cobblemon.mod.common.client.gui.pokedex.PokedexGUI
import com.cobblemon.mod.common.client.gui.pokedex.widgets.LocationsScrollingWidget
import com.cobblemon.mod.common.net.messages.client.pokedex.PokedexSpawnInfoPacket
import net.minecraft.client.Minecraft

object ClientsidePokedexSpawnInfoManager : ClientNetworkPacketHandler<PokedexSpawnInfoPacket> {
    val map: MutableMap<String, List<ClientsidePokedexSpawnDetailInfo>> = hashMapOf() // Species string -> spawn detail list

    override fun handle(packet: PokedexSpawnInfoPacket, client: Minecraft) {
        map[packet.species] = packet.info

        if (client.screen is PokedexGUI) {
            val tabInfoElement = (client.screen as PokedexGUI).tabInfoElement
            if (tabInfoElement is LocationsScrollingWidget) {
                tabInfoElement.locations = packet.info
                tabInfoElement.setEntries()
            }
        }
    }
}