/*
 * Copyright (C) 2023 Cobblemon Contributors
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package com.cobblemon.mod.common.client.gui.pokedex.widgets

import com.cobblemon.mod.common.api.gui.blitk
import com.cobblemon.mod.common.api.pokemon.PokemonSpecies
import com.cobblemon.mod.common.api.pokemon.evolution.ClientsidePokedexEvolutionInfo
import com.cobblemon.mod.common.api.text.bold
import com.cobblemon.mod.common.api.text.font
import com.cobblemon.mod.common.api.text.red
import com.cobblemon.mod.common.api.text.strikethrough
import com.cobblemon.mod.common.client.CobblemonResources
import com.cobblemon.mod.common.client.gui.ScrollingWidget
import com.cobblemon.mod.common.client.gui.TypeIcon
import com.cobblemon.mod.common.client.gui.drawProfilePokemon
import com.cobblemon.mod.common.client.gui.pokedex.PokedexGUIConstants
import com.cobblemon.mod.common.client.render.drawScaledText
import com.cobblemon.mod.common.client.render.models.blockbench.FloatingState
import com.cobblemon.mod.common.util.cobblemonResource
import com.cobblemon.mod.common.util.math.fromEulerXYZDegrees
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.Style
import net.minecraft.util.FastColor
import net.minecraft.util.Mth
import org.joml.Quaternionf
import org.joml.Vector3f

class EvolutionsScrollingWidget(val pX: Int, val pY: Int): ScrollingWidget<EvolutionsScrollingWidget.EvolutionWidgetEntry>(
    width = 131,
    height = 40,
    left = pX,
    top = pY + 10,
    slotHeight = 27
) {

    companion object {
        private val evolutionSlot = cobblemonResource("textures/gui/pokedex/evolution_slot.png")
    }

    var evolutions: List<ClientsidePokedexEvolutionInfo> = emptyList()

    override fun getX() = pX
    override fun getY() = pY

    fun setEntries() {
        evolutions.forEach {
            addEntry(EvolutionWidgetEntry(it))
        }
    }

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
            text = Component.literal("Evolutions").bold(),
            x = pX,
            y = pY - 10,
            shadow = true
        )

        if (evolutions.isEmpty()) {
            drawScaledText(
                context = context,
                text = Component.literal("No evolutions!"),
                x = pX + (width / 2) - 7,
                y = pY + (height / 2) - 3,
                shadow = false,
                colour = 0x606B6E,
                scale = PokedexGUIConstants.SCALE,
                centered = true
            )
        } else {
            super.renderWidget(context, mouseX, mouseY, delta)
        }
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

    override fun getEntry(index: Int): EvolutionWidgetEntry {
        return children()[index] as EvolutionWidgetEntry
    }

    class EvolutionWidgetEntry(val evolution: ClientsidePokedexEvolutionInfo): Slot<EvolutionWidgetEntry>() {
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
            val matrices = context.pose()

            blitk(
                matrixStack = matrices,
                texture = evolutionSlot,
                x = x, y = y,
                width = 131,
                height = 25
            )

            val species = evolution.result.species?.let {  PokemonSpecies.getByName(it) }
            if (species == null) return

            val form = evolution.result.form?.let { form -> species.forms.find { it.name == form } } ?: species.standardForm

            drawScaledText(context, CobblemonResources.DEFAULT_LARGE, species.translatedName.bold(), x+4, y+1, shadow=false)

            val fontWidth = Minecraft.getInstance().font.width(species.translatedName.bold().font(CobblemonResources.DEFAULT_LARGE))
            val typeIconX = x + 12 + fontWidth + (if (form.secondaryType != null) 10 else 0)
            val variantX = if (typeIconX >= x + 67) typeIconX + 4 else x + 68

            drawScaledText(context, null, Component.literal(evolution.type), variantX, y+3, PokedexGUIConstants.SCALE, colour = 16777215, shadow=false)

            val requirementsString = this.evolution.requirementsString.replace("<st>", "\u200B").replace("</st>", "\u200B") // Replace strikethrough tags with zero width space
            val lines = Minecraft.getInstance().font.splitter.splitLines(requirementsString, 180, Style.EMPTY)

            var strikethrough = false
            for ((index, line) in lines.withIndex()) {
                val split = line.string.split("\u200B")
                val component = Component.literal("")
                for (segment in split) {
                    if (strikethrough) component.append(Component.literal(segment).strikethrough().red()) else component.append(Component.literal(segment))
                    strikethrough = !strikethrough
                }
                strikethrough = !strikethrough

                drawScaledText(context, null, component, x+4, y + 12 + (index*6), PokedexGUIConstants.SCALE, colour = 16777215, shadow=false)
            }

            (TypeIcon(x + 10 + fontWidth, y+1, form.primaryType, form.secondaryType, true, true, 9.5F, 0F)).render(context)

            matrices.pushPose()
            matrices.translate((x + 12) + 104.0, y - 3.0, 0.0)
            matrices.scale(2.5F, 2.5F, 1.0F)
            val quaternion = Quaternionf().fromEulerXYZDegrees(Vector3f(13.0F, 35.0F, 0.0F))
            val state = FloatingState()
            state.currentAspects = evolution.result.aspects
            drawProfilePokemon(species.resourceIdentifier, matrices, quaternion, state = state, partialTicks=tickDelta, scale=6.0F)
            matrices.popPose()
        }

        override fun getNarration(): Component {
            return Component.literal("No Narration Available")
        }

    }

}