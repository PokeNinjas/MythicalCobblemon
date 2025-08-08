/*
 * Copyright (C) 2023 Cobblemon Contributors
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package com.cobblemon.mod.common.net.messages.client.pokedex

import com.cobblemon.mod.common.api.net.NetworkPacket
import com.cobblemon.mod.common.config.research_tasks.ResearchTaskConfig
import com.cobblemon.mod.common.util.cobblemonResource
import com.cobblemon.mod.common.util.readNullable
import com.cobblemon.mod.common.util.readString
import com.cobblemon.mod.common.util.writeCollection
import com.cobblemon.mod.common.util.writeString
import net.minecraft.network.RegistryFriendlyByteBuf

/**
 * Handled by [com.cobblemon.mod.common.pokedex.research_tasks.ClientsideResearchTasksManager]
 */
class ResearchTasksInfoPacket(
    val species: String,
    val progress: Map<String, Int>,
    val tasks: List<ResearchTaskConfig>
): NetworkPacket<ResearchTasksInfoPacket> {
    override val id = ID

    override fun encode(buffer: RegistryFriendlyByteBuf) {
        buffer.writeString(species)
        buffer.writeCollection<Map.Entry<String, Int>>(progress.entries) { buf, entry ->
            buf.writeString(entry.key)
            buf.writeInt(entry.value)
        }
        buffer.writeCollection<ResearchTaskConfig>(tasks) {buf, task ->
            buf.writeString(task.task)
            buf.writeNullable(task.target) { b, s -> b.writeString(s) }
            buf.writeInt(task.amount)
        }
    }

    companion object {
        fun decode(buffer: RegistryFriendlyByteBuf) = ResearchTasksInfoPacket(
            buffer.readString(),
            hashMapOf<String, Int>()
                .apply {
                    buffer.readList { Pair(it.readString(), it.readInt()) }.forEach {put(it.first, it.second)}
                },
            mutableListOf<ResearchTaskConfig>()
                .apply {
                    buffer.readList { ResearchTaskConfig(it.readString(), it.readNullable { it.readString() }, it.readInt()) }.forEach {add(it)}
                },
        )
        val ID = cobblemonResource("research_tasks_info")
    }
}