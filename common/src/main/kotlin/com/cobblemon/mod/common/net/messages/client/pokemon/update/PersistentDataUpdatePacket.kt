/*
 * Copyright (C) 2023 Cobblemon Contributors
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package com.cobblemon.mod.common.net.messages.client.pokemon.update

import com.cobblemon.mod.common.pokemon.Pokemon
import com.cobblemon.mod.common.util.cobblemonResource
import net.minecraft.nbt.CompoundTag
import net.minecraft.network.RegistryFriendlyByteBuf

class PersistentDataUpdatePacket(pokemon: () -> Pokemon, value: CompoundTag): SingleUpdatePacket<CompoundTag, PersistentDataUpdatePacket>(pokemon, value) {
    override val id = ID
    override fun encodeValue(buffer: RegistryFriendlyByteBuf) {
        buffer.writeNbt(this.value)
    }

    override fun set(pokemon: Pokemon, value: CompoundTag) {
        pokemon.persistentData = value
        println("Changing persistent data for ${pokemon.species.name} to $value")
    }

    companion object {
        val ID = cobblemonResource("persistent_data_update")
        fun decode(buffer: RegistryFriendlyByteBuf): PersistentDataUpdatePacket {
            val pokemon = decodePokemon(buffer)
            val persistentData = buffer.readNbt() ?: CompoundTag()
            return PersistentDataUpdatePacket(pokemon, persistentData)
        }
    }
}