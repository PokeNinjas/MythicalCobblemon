/*
 * Copyright (C) 2023 Cobblemon Contributors
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package com.cobblemon.mod.common.client.gui.pokedex.widgets

import com.cobblemon.mod.common.api.gui.blitk
import com.cobblemon.mod.common.api.text.bold
import com.cobblemon.mod.common.client.CobblemonResources
import com.cobblemon.mod.common.client.gui.ScrollingWidget
import com.cobblemon.mod.common.client.gui.pokedex.PokedexGUIConstants
import com.cobblemon.mod.common.client.render.drawScaledText
import com.cobblemon.mod.common.client.render.drawScaledTextJustifiedRight
import com.cobblemon.mod.common.config.research_tasks.ResearchTaskConfig
import com.cobblemon.mod.common.pokedex.research_tasks.ResearchTask
import com.cobblemon.mod.common.pokedex.research_tasks.ResearchTasks
import com.cobblemon.mod.common.util.cobblemonResource
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.network.chat.Component
import net.minecraft.util.FastColor
import net.minecraft.util.Mth

class ResearchTasksScrollingWidget(val pX: Int, val pY: Int): ScrollingWidget<ResearchTasksScrollingWidget.ResearchTaskWidgetEntry>(
    width = PokedexGUIConstants.HALF_OVERLAY_WIDTH - 2,
    height = 42,
    left = pX,
    top = pY + 10,
    slotHeight = 10
) {

    companion object {
        private val done = cobblemonResource("textures/gui/pokedex/research_task_done.png")
        private val not_done = cobblemonResource("textures/gui/pokedex/research_task_not_done.png")
    }

    var tasksAndProgress: Map<ResearchTaskConfig, Int> = emptyMap()
    var completedAll: Boolean = false

    override fun getX() = pX
    override fun getY() = pY

    fun setEntries() {
        clearEntries()
        tasksAndProgress.forEach {
            addEntry(ResearchTaskWidgetEntry(it.key, it.value))
        }
        addEntry(ResearchTaskWidgetEntry(completedAll))
    }

    override fun getScrollbarPosition(): Int {
        return left + width - scrollBarWidth - 7
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

        if (tasksAndProgress.isEmpty()) {
            drawScaledText(
                context = context,
                text = Component.literal("No research tasks available yet!"),
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

    override fun getEntry(index: Int): ResearchTaskWidgetEntry {
        return children()[index] as ResearchTaskWidgetEntry
    }

    class ResearchTaskWidgetEntry(val task: ResearchTaskConfig, val progress: Int): Slot<ResearchTaskWidgetEntry>() {
        var infoOneAtBottom = false
        var allCompleted = false

        constructor(allCompleted: Boolean) : this(ResearchTaskConfig("none"), 0) {
            this.infoOneAtBottom = true
            this.allCompleted = allCompleted
        }

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
            if (infoOneAtBottom) {

                val text = Component.literal(if (allCompleted) "Shiny chance boosted for natural spawns!" else "Complete all tasks for boosted shiny chance!")

                drawScaledText(
                    context = context,
                    text = text,
                    x = x + 5,
                    y = y + 7,
                    colour = 0x606B6E,
                    scale = PokedexGUIConstants.SCALE
                )
                return
            }

            val completed = progress >= task.amount

            context.blit(if (completed) done else not_done, x, y+5, 0, 0.0f, 0.0f, 8, 8, 8, 8)

            val taskText = ResearchTasks.fromFullIdentifier(ResearchTask(task.task, task.target).getFullIdentifier()).getDisplayName().bold()

            drawScaledText(
                context = context,
                text = taskText,
                x = x + 10,
                y = y + 7,
                colour = 0x606B6E,
                scale = PokedexGUIConstants.SCALE
            )

            if (task.amount != 1) {
                val progressText = Component.literal("${progress.coerceAtMost(task.amount)}/${task.amount}")

                drawScaledTextJustifiedRight(
                    context = context,
                    text = progressText,
                    x = x + 120,
                    y = y + 7,
                    colour = 0x606B6E,
                    scale = PokedexGUIConstants.SCALE
                )
            }
        }

        override fun getNarration(): Component {
            return Component.literal("No narration available")
        }

    }

}