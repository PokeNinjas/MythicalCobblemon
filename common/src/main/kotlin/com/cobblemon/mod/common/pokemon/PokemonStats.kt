/*
 * Copyright (C) 2022 Cobblemon Contributors
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package com.cobblemon.mod.common.pokemon

import com.cobblemon.mod.common.api.pokemon.stats.Stat
import com.cobblemon.mod.common.api.pokemon.stats.Stats
import com.cobblemon.mod.common.api.reactive.SimpleObservable
import com.google.gson.JsonObject
import net.minecraft.nbt.NbtCompound
import net.minecraft.network.PacketByteBuf

/**
 * Holds a mapping from a Stat to value that should be reducible to a short for NBT and net.
 */
abstract class PokemonStats : Iterable<Map.Entry<Stat, Int>> {
    abstract val acceptableRange: IntRange
    abstract val defaultValue: Int
    override fun iterator() = stats.entries.iterator()

    /** Emits any stat change. */
    val observable = SimpleObservable<PokemonStats>()

    private val stats = mutableMapOf<Stat, Int>()
    private var emit = true

    fun doWithoutEmitting(action: () -> Unit) {
        emit = false
        action()
        emit = true
    }

    fun doThenEmit(action: () -> Unit) {
        doWithoutEmitting(action)
        update()
    }

    fun update() {
        if (emit) {
            observable.emit(this)
        }
    }

    operator fun get(key: Stat) = stats[key]
    open operator fun set(key: Stat, value: Int) {
        if (this.canSet(key, value)) {
            stats[key] = value
            update()
        }
    }

    protected open fun canSet(stat: Stat, value: Int) = value in acceptableRange

    fun saveToNBT(nbt: NbtCompound): NbtCompound {
        stats.entries.forEach { (stat, value) -> nbt.putShort(stat.id, value.toShort()) }
        return nbt
    }

    fun loadFromNBT(nbt: NbtCompound): PokemonStats {
        stats.clear()
        nbt.keys.forEach { statId ->
            val stat = Stats.getStat(statId)
            this[stat] = nbt.getShort(statId).toInt()
        }
        return this
    }

    fun saveToJSON(json: JsonObject): JsonObject {
        stats.entries.forEach { (stat, value) -> json.addProperty(stat.id, value) }
        return json
    }

    fun loadFromJSON(json: JsonObject): PokemonStats {
        stats.clear()
        json.entrySet().forEach { (key, element) ->
            val stat = Stats.getStat(key)
            this[stat] = element.asInt
        }
        return this
    }

    fun saveToBuffer(buffer: PacketByteBuf) {
        buffer.writeByte(stats.size)
        for ((stat, value) in stats) {
            buffer.writeString(stat.id)
            buffer.writeShort(value)
        }
    }

    fun loadFromBuffer(buffer: PacketByteBuf) {
        stats.clear()
        repeat(times = buffer.readUnsignedByte().toInt()) {
            val stat = Stats.getStat(buffer.readString())
            val value = buffer.readUnsignedShort()
            stats[stat] = value
        }
    }

    fun getOrDefault(stat: Stat) = this[stat] ?: this.defaultValue
}