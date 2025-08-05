/*
 * Copyright (C) 2023 Cobblemon Contributors
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package com.cobblemon.mod.common.client.gui.pokedex.widgets

import com.cobblemon.mod.common.api.text.bold
import com.cobblemon.mod.common.client.CobblemonResources
import com.cobblemon.mod.common.client.gui.ScrollingWidget
import com.cobblemon.mod.common.client.gui.pokedex.PokedexGUIConstants
import com.cobblemon.mod.common.client.render.drawScaledText
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.network.chat.Component
import net.minecraft.util.FastColor
import net.minecraft.util.Mth

class ResearchTasksScrollingWidget(val pX: Int, val pY: Int): ScrollingWidget<ResearchTasksScrollingWidget.ResearchTaskWidgetEntry>(
    width = 131,
    height = 40,
    left = pX,
    top = pY + 10,
    slotHeight = 27
) {
    override fun getX() = pX
    override fun getY() = pY

//    fun setEntries() {
//        if (entriesAdded) return
//        entriesAdded = true
//        locations.forEach {
//            addEntry(ResearchTaskWidgetEntry(this, it))
//        }
//    }

    override fun getScrollbarPosition(): Int {
        return left + width - scrollBarWidth + 6
    }

    override fun getBottom(): Int {
        return this.y + this.height - 1
    }

    override fun renderScrollbar(context: GuiGraphics, mouseX: Int, mouseY: Int, delta: Float) {
        val xLeft = this.scrollbarPosition
        val xRight = xLeft + 3
        val yStart = y + 1

        val barHeight = this.bottom - yStart

        var yBottom = ((barHeight * barHeight).toFloat() / this.maxPosition.toFloat()).toInt()
        yBottom = Mth.clamp(yBottom, 32, barHeight - 8)
        var yTop = scrollAmount.toInt() * (barHeight - yBottom) / this.maxScroll + yStart
        if (yTop < yStart) yTop = yStart

        context.fill(xLeft + 1, yStart, xRight - 1, this.bottom, FastColor.ARGB32.color(255, 126, 231, 229)) // track
        context.fill(xLeft, yTop, xRight, yTop + yBottom, FastColor.ARGB32.color(255, 58, 150, 182)) // bar
    }

    override fun renderWidget(context: GuiGraphics, mouseX: Int, mouseY: Int, delta: Float) {
        drawScaledText(
            context = context,
            font = CobblemonResources.DEFAULT_LARGE,
            text = Component.literal("Research Tasks").bold(),
            x = pX,
            y = pY - 10,
            shadow = true
        )

//        if (locations.isEmpty()) {
            drawScaledText(
                context = context,
                text = Component.literal("Coming Soon!"),
                x = pX + (width / 2) - 7,
                y = pY + (height / 2) - 3,
                shadow = false,
                colour = 0x606B6E,
                scale = PokedexGUIConstants.SCALE,
                centered = true
            )
//        } else {
//            super.renderWidget(context, mouseX, mouseY, delta)
//        }
    }

    override fun renderItem(
        context: GuiGraphics,
        mouseX: Int,
        mouseY: Int,
        delta: Float,
        index: Int,
        x: Int,
        y: Int,
        entryWidth: Int,
        entryHeight: Int
    ) {
        val entry =  this.getEntry(index)
        entry.render(
            context, index, y + 2, x, entryWidth, entryHeight, mouseX, mouseY,
            hovered == entry, delta
        )
    }

    override fun getEntry(index: Int): ResearchTaskWidgetEntry {
        return children()[index] as ResearchTaskWidgetEntry
    }

    class ResearchTaskWidgetEntry(): Slot<ResearchTaskWidgetEntry>() {

        override fun render(
            context: GuiGraphics,
            index: Int,
            y: Int,
            x: Int,
            entryWidth: Int,
            entryHeight: Int,
            mouseX: Int,
            mouseY: Int,
            hovered: Boolean,
            tickDelta: Float
        ) {
//            if (species == null) return
//
//            val matrices = context.pose()
//
//            blitk(
//                matrixStack = matrices,
//                texture = evolutionSlot,
//                x = x, y = y,
//                width = 131,
//                height = 25
//            )
//
//            drawScaledText(context, CobblemonResources.DEFAULT_LARGE, Component.literal(location.name).bold(), x+4, y+1, shadow=false)
//
//            val levelX = x + 68
//
//            location.levelRange?.let {
//                drawScaledText(context, null, Component.literal("Lvl. ${it.first}-${it.last}"), levelX, y + 3, PokedexGUIConstants.SCALE, colour = 16777215, shadow = false)
//            }
//
//            drawScaledText(context, null, Component.literal("${location.contextName}, ${location.bucketName}"), x+4, y + 12, PokedexGUIConstants.SCALE, colour = 16777215, shadow=false)
//            drawScaledText(context, null, Component.literal("Hover for details!"), x+4, y + 12 + 6, PokedexGUIConstants.SCALE, colour = 16777215, shadow=false)
//
//            if (mouseX in x..(x+131) && mouseY in y..(y+25)) {
//                val lines = location.conditionsString.replace("</st>", "").split("\n")
//                val component = Component.literal("Conditions\n").bold().underline().withColor(0xF7B27C)
//                for (line in lines) {
//                    if ("<st>" in line) {
//                        component.append(Component.literal(line.replace("<st>", "")).notBold().notUnderline().strikethrough().withColor(0xFA6969))
//                    } else if ("biome" in line.lowercase()) {
//                        component.append(Component.literal(line).notBold().notUnderline().notStrikethrough().withColor(0x8DFCCE))
//                    } else {
//                        component.append(Component.literal(line).notBold().notUnderline().notStrikethrough().withColor(0xFCEB8D))
//                    }
//                }
//
//                parent.tooltip = Tooltip.create(component)
//            }
//
//            matrices.pushPose()
//            matrices.translate((x + 12) + 104.0, y - 3.0, 0.0)
//            matrices.scale(2.5F, 2.5F, 1.0F)
//            val quaternion = Quaternionf().fromEulerXYZDegrees(Vector3f(13.0F, 35.0F, 0.0F))
//            val state = FloatingState()
//            renderablePokemon?.let { drawProfilePokemon(it, matrices, quaternion, state = state, partialTicks=tickDelta, scale=6.0F) }
//            matrices.popPose()
        }

        override fun getNarration(): Component {
            return Component.literal("No Narration Available")
        }

    }



}