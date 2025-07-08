/*
 * Copyright (C) 2023 Cobblemon Contributors
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package com.cobblemon.mod.common.client.net.storage.pc

import com.cobblemon.mod.common.api.net.ClientNetworkPacketHandler
import com.cobblemon.mod.common.client.CobblemonClient
import com.cobblemon.mod.common.net.messages.client.storage.pc.SetLockedPCSlotsPacket
import net.minecraft.client.Minecraft

object SetLockedPCSlotsHandler : ClientNetworkPacketHandler<SetLockedPCSlotsPacket> {
    override fun handle(packet: SetLockedPCSlotsPacket, client: Minecraft) {
        CobblemonClient.storage.setLockedPCSlots(packet.storeID, packet.lockedPositions)
    }
}