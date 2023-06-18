package com.cobblemon.mod.common.api.events.entity

import com.cobblemon.mod.common.api.events.Cancelable
import com.cobblemon.mod.common.api.spawning.context.SpawningContext
import net.minecraft.entity.Entity

class SpawnEvent<T : Entity>(val entity: T, val ctx: SpawningContext) : Cancelable() {
}