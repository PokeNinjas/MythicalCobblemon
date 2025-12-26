package com.cobblemon.mod.common.item.components

import com.mojang.serialization.Codec
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.StreamCodec

enum class BerryQuality {
    NORMAL,
    SILVER,
    GOLD,
    RAINBOW;

    companion object {
        val CODEC: Codec<BerryQuality> = Codec.STRING.xmap(
            { name ->
                valueOf(name)
            },
            { quality ->
                quality.name
            }
        )
        val PACKET_CODEC: StreamCodec<RegistryFriendlyByteBuf, BerryQuality> = StreamCodec.of(
            { buf, quality -> buf.writeByte(quality.ordinal) },
            { buf -> entries[buf.readByte().toInt()] }
        )
    }

}
