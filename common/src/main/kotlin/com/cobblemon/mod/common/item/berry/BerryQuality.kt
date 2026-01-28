package com.cobblemon.mod.common.item.berry

import com.cobblemon.mod.common.CobblemonItemComponents
import com.mojang.serialization.Codec
import net.minecraft.core.component.DataComponents
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.StreamCodec
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Rarity

enum class BerryQuality {
    NORMAL,
    SILVER,
    GOLD,
    RAINBOW;

    fun set(stack: ItemStack) {
        if (this == BerryQuality.NORMAL) {
            stack.remove(CobblemonItemComponents.BERRY_QUALITY)
            stack.remove(DataComponents.RARITY)
        } else {
            stack.set(CobblemonItemComponents.BERRY_QUALITY, this)
            stack.set(DataComponents.RARITY, getRarity(this))
        }
    }

    private fun getRarity(quality: BerryQuality): Rarity {
        return when (quality) {
            NORMAL -> Rarity.COMMON
            SILVER -> Rarity.UNCOMMON
            GOLD -> Rarity.RARE
            RAINBOW -> Rarity.EPIC
        }
    }

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
