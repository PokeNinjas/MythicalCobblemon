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

class RequestResearchTasksInfoPacket(val species: String) : NetworkPacket<RequestResearchTasksInfoPacket> {
    override val id = ID

    override fun encode(buffer: RegistryFriendlyByteBuf) {
        buffer.writeString(species)
    }

    companion object {
        val ID = cobblemonResource("request_research_tasks_info")

        fun decode(buffer: RegistryFriendlyByteBuf): RequestResearchTasksInfoPacket {
            return RequestResearchTasksInfoPacket(buffer.readString())
        }
    }
}
