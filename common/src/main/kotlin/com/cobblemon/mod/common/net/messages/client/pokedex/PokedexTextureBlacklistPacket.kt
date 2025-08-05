/*
 * Copyright (C) 2023 Cobblemon Contributors
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package com.cobblemon.mod.common.net.messages.client.pokedex

import com.cobblemon.mod.common.api.net.NetworkPacket
import com.cobblemon.mod.common.util.cobblemonResource
import com.cobblemon.mod.common.util.readString
import com.cobblemon.mod.common.util.writeString
import net.minecraft.network.RegistryFriendlyByteBuf

/**
 * Handled by [com.cobblemon.mod.common.client.net.pokedex.PokedexTextureBlacklistHandler]
 */
class PokedexTextureBlacklistPacket(
    val blacklist: List<String>,
): NetworkPacket<PokedexTextureBlacklistPacket> {
    override val id = ID

    override fun encode(buffer: RegistryFriendlyByteBuf) {
        buffer.writeCollection<String>(blacklist) { buf, str ->
            buf.writeString(str)
        }
    }

    companion object {
        fun decode(buffer: RegistryFriendlyByteBuf) = PokedexTextureBlacklistPacket(
            mutableListOf<String>()
                .apply {
                    buffer.readList { it.readString() }.forEach {add(it)}
                }
        )
        val ID = cobblemonResource("pokedex_texture_blacklist")
    }
}