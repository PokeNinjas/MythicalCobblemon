/*
 * Copyright (C) 2023 Cobblemon Contributors
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package com.cobblemon.mod.common.item.berry

import com.cobblemon.mod.common.CobblemonItemComponents
import com.cobblemon.mod.common.block.BerryBlock
import net.minecraft.network.chat.Component
import net.minecraft.world.item.ItemNameBlockItem
import net.minecraft.world.item.ItemStack

open class BerryItem(private val berryBlock: BerryBlock) : ItemNameBlockItem(berryBlock, Properties()) {

    fun berry() = this.berryBlock.berry()

    override fun getName(stack: ItemStack): Component? {
        val base = this.getDescriptionId(stack)
        val suffix = when(stack.get(CobblemonItemComponents.BERRY_QUALITY)) {
            BerryQuality.SILVER -> ".silver"
            BerryQuality.GOLD -> ".gold"
            BerryQuality.RAINBOW -> ".rainbow"
            else -> ""
        }
        return Component.translatable("$base$suffix")
    }

}