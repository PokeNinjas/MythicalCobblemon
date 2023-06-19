/*
 * Copyright (C) 2023 Cobblemon Contributors
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package com.cobblemon.mod.common.api.events.entity

import com.cobblemon.mod.common.api.events.Cancelable
import com.cobblemon.mod.common.api.spawning.context.SpawningContext
import net.minecraft.entity.Entity

class SpawnEvent<T : Entity>(val entity: T, val ctx: SpawningContext) : Cancelable() {
}