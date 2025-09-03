/*
 * Copyright (C) 2023 Cobblemon Contributors
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package com.cobblemon.mod.common.pokedex.research_tasks

import com.cobblemon.mod.common.Cobblemon
import net.minecraft.core.HolderLookup
import net.minecraft.nbt.CompoundTag
import net.minecraft.world.entity.player.Player
import org.ladysnake.cca.api.v3.component.ComponentV3
import org.ladysnake.cca.api.v3.entity.RespawnableComponent

class PlayerResearchTasksData(
    private val progress: MutableMap<String, MutableMap<String, Int>>, // Map of pokemon species -> map of research task full identifier -> number of times completed that task
    val speciesWithAllTasksCompleted: MutableSet<String>
) : ComponentV3, RespawnableComponent<PlayerResearchTasksData> {
    private lateinit var provider: Player

    constructor(provider: Player) : this(hashMapOf<String, MutableMap<String, Int>>(), hashSetOf()) {
        this.provider = provider
    }

    fun getProgress(pokemon: String, task: ResearchTask) : Int {
        return progress[pokemon]?.get(task.getFullIdentifier()) ?: 0
    }

    fun incrementProgress(pokemon: String, task: ResearchTask) {
        progress.computeIfAbsent(pokemon) { _ -> hashMapOf<String, Int>()}.merge(task.getFullIdentifier(), 1, Integer::sum)
        checkIfAllTasksCompleted(pokemon)
    }

    fun getAllProgress(pokemon: String) : Map<String, Int> {
        return progress[pokemon] ?: emptyMap()
    }

    private fun checkIfAllTasksCompleted(pokemon: String) {
        val tasks = Cobblemon.researchTasksConfig.tasks[pokemon] ?: return // Doesn't count if there are no tasks set up
        val allCompleted = tasks.all { task -> getProgress(pokemon, ResearchTask(task.task, task.target)) >= task.amount }
        if (allCompleted) {
            speciesWithAllTasksCompleted.add(pokemon)
        }
    }

    override fun readFromNbt(
        tag: CompoundTag,
        registryLookup: HolderLookup.Provider
    ) {
        val progressTag = tag.getCompound("progress")
        for (pokemon in progressTag.allKeys) {
            val pokemonTag = progressTag.getCompound(pokemon)
            val map = hashMapOf<String, Int>()
            for (taskID in pokemonTag.allKeys) {
                map[taskID] = pokemonTag.getInt(taskID)
            }
            progress[pokemon] = map
            checkIfAllTasksCompleted(pokemon)
        }
    }

    override fun writeToNbt(
        tag: CompoundTag,
        registryLookup: HolderLookup.Provider
    ) {
        val progressTag = CompoundTag()
        for ((pokemon, map) in progress) {
            val pokemonTag = CompoundTag()
            for ((taskID, num) in map) {
                pokemonTag.putInt(taskID, num)
            }
            progressTag.put(pokemon, pokemonTag)
        }
        tag.put("progress", progressTag)
    }
}