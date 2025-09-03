/*
 * Copyright (C) 2023 Cobblemon Contributors
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

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