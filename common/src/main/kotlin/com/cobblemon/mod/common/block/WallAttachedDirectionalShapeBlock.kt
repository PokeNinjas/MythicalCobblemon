package com.cobblemon.mod.common.block

import com.cobblemon.mod.common.util.rotateShape
import com.mojang.serialization.Codec
import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.world.level.BlockGetter
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.phys.shapes.CollisionContext
import net.minecraft.world.phys.shapes.Shapes
import net.minecraft.world.phys.shapes.VoxelShape

class WallAttachedDirectionalShapeBlock(settings: Properties, val width: Int = 16, val height: Int = 16, val depth: Int = 16): WallAttachedDirectionalBlock(settings) {
    companion object {
        /**
         * Calculates a [VoxelShape] based on a center point of (8, 8, 16).
         *
         * The resulting shape is centered X horizontally and Y vertically
         * expanding inwards/North along the Z-axis from the South face (16).
         *
         * @param width The width of the shape from 0-16, along the X-axis.
         * @param height The height of the shape from 0-16 along the Y-axis.
         * @param depth The depth of the shape from 0-16 along the Z-axis, extending inwards from 16.
         * @return A VoxelShape.
         */
        fun convertToShape(sizeX: Int, sizeY: Int, sizeZ: Int): VoxelShape {
            val clampedX = sizeX.coerceIn(0, 16)
            val clampedY = sizeY.coerceIn(0, 16)
            val clampedZ = sizeZ.coerceIn(0, 16)

            val fromX = 8.0 - (clampedX / 2)
            val toX = 8.0 + (clampedX / 2)

            val fromY = 8.0 - (clampedY / 2)
            val toY = 8.0 + (clampedY / 2)

            val fromZ = 16.0 - clampedZ
            val toZ = 16.0

            val minX = fromX / 16.0
            val minY = fromY / 16.0
            val minZ = fromZ / 16.0
            val maxX = toX / 16.0
            val maxY = toY / 16.0
            val maxZ = toZ / 16.0

            return Shapes.box(minX, minY, minZ, maxX, maxY, maxZ)
        }
    }

    override fun codec(): MapCodec<WallAttachedDirectionalShapeBlock> = RecordCodecBuilder.mapCodec { instance ->
        instance.group(
            propertiesCodec(),
            Codec.INT.fieldOf("width").forGetter { it.width },
            Codec.INT.fieldOf("height").forGetter { it.height },
            Codec.INT.fieldOf("depth").forGetter { it.depth }
        ).apply(instance, { properties, width, height, depth ->
            WallAttachedDirectionalShapeBlock(properties, width, height, depth)
        })
    }

    override fun getShape(state: BlockState, level: BlockGetter, pos: BlockPos, context: CollisionContext): VoxelShape {
        val direction = state.getValue(FACING)
        return rotateShape(Direction.NORTH, direction, convertToShape(width, height, depth))
    }
}
