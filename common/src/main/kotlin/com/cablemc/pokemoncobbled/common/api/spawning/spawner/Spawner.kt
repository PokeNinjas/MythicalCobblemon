package com.cablemc.pokemoncobbled.common.api.spawning.spawner

import com.cablemc.pokemoncobbled.common.api.spawning.context.SpawningContext
import com.cablemc.pokemoncobbled.common.api.spawning.detail.SpawnPool
import com.cablemc.pokemoncobbled.common.api.spawning.influence.SpawningInfluence
import com.cablemc.pokemoncobbled.common.api.spawning.selection.SpawningSelector
import net.minecraft.world.entity.Entity
import java.util.concurrent.Executors

/**
 * Interface representing something that performs the action of spawning. Various functions
 * exist to streamline the process of using the Best Spawner API.
 *
 * @author Hiroku
 * @since January 24th, 2022
 */
interface Spawner {
    companion object {
        var worker = Executors.newSingleThreadExecutor { r -> Thread(r, "Spawning Worker") }
    }

    val name: String
    val influences: MutableList<SpawningInfluence>
    fun getSpawningSelector(): SpawningSelector
    fun setSpawningSelector(selector: SpawningSelector)
    fun getSpawnPool(): SpawnPool
    fun setSpawnPool(spawnPool: SpawnPool)

    fun modifySpawn(entity: Entity) {}
    fun afterSpawn(entity: Entity) {}
    fun canSpawn(): Boolean
    fun getMatchingSpawns(ctx: SpawningContext) = getSpawnPool().retrieve(ctx).filter { it.isSatisfiedBy(ctx) }

    fun copyInfluences() = influences.toMutableList()
}