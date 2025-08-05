/*
 * Copyright (C) 2023 Cobblemon Contributors
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package com.cobblemon.mod.common.net.messages.client.pokedex

import com.cobblemon.mod.common.api.net.NetworkPacket
import com.cobblemon.mod.common.api.spawning.detail.ClientsidePokedexSpawnDetailInfo
import com.cobblemon.mod.common.util.cobblemonResource
import com.cobblemon.mod.common.util.readString
import com.cobblemon.mod.common.util.writeString
import net.minecraft.network.RegistryFriendlyByteBuf

/**
 * Send info about a pokemon's spawns after the client requests it, for displaying in the Locations pokedex tab
 *
 * Handled by [com.cobblemon.mod.common.client.net.pokedex.ClientsidePokedexSpawnInfoManager]
 */
class PokedexSpawnInfoPacket(
    val species: String,
    val info: List<ClientsidePokedexSpawnDetailInfo>
): NetworkPacket<PokedexSpawnInfoPacket> {
    override val id = ID

    override fun encode(buffer: RegistryFriendlyByteBuf) {
        buffer.writeString(species)
        buffer.writeCollection<ClientsidePokedexSpawnDetailInfo>(info) { buf, detail ->
            detail.encode(buf)
        }
    }

    companion object {
        fun decode(buffer: RegistryFriendlyByteBuf) = PokedexSpawnInfoPacket(
            buffer.readString(),
            mutableListOf<ClientsidePokedexSpawnDetailInfo>()
                .apply {
                    buffer.readList { ClientsidePokedexSpawnDetailInfo.decode(it) }.forEach {add(it)}
                }
        )
        val ID = cobblemonResource("pokedex_spawn_info")
    }
}