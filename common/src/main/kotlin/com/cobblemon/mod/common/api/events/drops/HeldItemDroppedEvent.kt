/*
 * Copyright (C) 2023 Cobblemon Contributors
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package com.cobblemon.mod.common.api.events.drops

import com.cobblemon.mod.common.api.events.Cancelable
import com.cobblemon.mod.common.entity.pokemon.PokemonEntity
import net.minecraft.world.item.ItemStack

/**
 * MYTHICALNETWORK - CUSTOM EVENT
 * Cancelable event posted when a Pokemon drops its held item
 */
class HeldItemDroppedEvent(
    val entity: PokemonEntity?,
    val itemStack: ItemStack
) : Cancelable()
