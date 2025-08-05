/*
 * Copyright (C) 2023 Cobblemon Contributors
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package com.cobblemon.mod.common.net.messages.server.pokedex

import com.cobblemon.mod.common.api.net.NetworkPacket
import com.cobblemon.mod.common.util.cobblemonResource
import com.cobblemon.mod.common.util.readString
import com.cobblemon.mod.common.util.writeString
import net.minecraft.network.RegistryFriendlyByteBuf

class PokedexRequestSpawnInfoPacket(val species: String) : NetworkPacket<PokedexRequestSpawnInfoPacket> {
    override val id = ID

    override fun encode(buffer: RegistryFriendlyByteBuf) {
        buffer.writeString(species)
    }

    companion object {
        val ID = cobblemonResource("pokedex_request_spawn_info")

        fun decode(buffer: RegistryFriendlyByteBuf): PokedexRequestSpawnInfoPacket {
            return PokedexRequestSpawnInfoPacket(buffer.readString())
        }
    }
}
