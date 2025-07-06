/*
 * Copyright (C) 2023 Cobblemon Contributors
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package com.cobblemon.mod.common.net.messages.client.storage.pc

import com.cobblemon.mod.common.api.net.NetworkPacket
import com.cobblemon.mod.common.api.net.UnsplittablePacket
import com.cobblemon.mod.common.api.storage.pc.PCPosition
import com.cobblemon.mod.common.util.cobblemonResource
import com.cobblemon.mod.common.util.readPCPosition
import com.cobblemon.mod.common.util.writePCPosition
import java.util.UUID
import net.minecraft.network.RegistryFriendlyByteBuf

/**
 * Sets which PC slots are locked out (due to balloon expeditions)
 *
 * Handled by [com.cobblemon.mod.common.client.net.storage.pc.SetLockedPCSlotsHandler].
 *
 * @author repeater64
 * @since July 5th, 2025
 */
class SetLockedPCSlotsPacket internal constructor(val storeID: UUID, val lockedPositions: List<PCPosition>) : NetworkPacket<SetLockedPCSlotsPacket>, UnsplittablePacket {

    override val id = ID

    override fun encode(buffer: RegistryFriendlyByteBuf) {
        buffer.writeUUID(this.storeID)
        buffer.writeInt(lockedPositions.size)
        for (pos in lockedPositions) {
            buffer.writePCPosition(pos)
        }
    }

    companion object {
        fun create(storeID: UUID, lockedPositions: List<PCPosition>) : SetLockedPCSlotsPacket = SetLockedPCSlotsPacket(storeID, lockedPositions)

        val ID = cobblemonResource("set_locked_pc_slots")
        fun decode(buffer: RegistryFriendlyByteBuf): SetLockedPCSlotsPacket {
            val uuid = buffer.readUUID()
            val numPositions = buffer.readInt()
            val lockedPositions = mutableListOf<PCPosition>()
            for (i in 0 until numPositions) {
                lockedPositions.add(buffer.readPCPosition())
            }
            return SetLockedPCSlotsPacket(
                storeID = uuid,
                lockedPositions = lockedPositions
            )
        }
    }
}