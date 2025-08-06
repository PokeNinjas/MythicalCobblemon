/*
 * Copyright (C) 2023 Cobblemon Contributors
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package com.cobblemon.mod.common.client.gui.pokedex.widgets

import com.cobblemon.mod.common.api.pokemon.moves.Learnset
import com.cobblemon.mod.common.api.text.bold
import com.cobblemon.mod.common.client.CobblemonResources
import com.cobblemon.mod.common.client.gui.pokedex.ScaledButton
import com.cobblemon.mod.common.client.render.drawScaledText
import com.cobblemon.mod.common.util.cobblemonResource
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.network.chat.Component

class MovesWidget(x: Int, y: Int): InfoTextScrollWidget(pX = x, pY = y) {
    companion object {
        private val arrowLeft = cobblemonResource("textures/gui/pokedex/info_arrow_left.png")
        private val arrowRight = cobblemonResource("textures/gui/pokedex/info_arrow_right.png")
    }

    val leftButton: ScaledButton = ScaledButton(
        pX + 2.5F,
        pY - 8F,
        7,
        10,
        arrowLeft,
        clickAction = { switchCategory(false) }
    )

    val rightButton: ScaledButton = ScaledButton(
        pX + 133F,
        pY - 8F,
        7,
        10,
        arrowRight,
        clickAction = { switchCategory(true) }
    )

    var learnset: Learnset? = null
    var category: MoveCategory = MoveCategory.LEVEL_UP

    override fun renderWidget(context: GuiGraphics, mouseX: Int, mouseY: Int, delta: Float) {
        drawScaledText(
            context = context,
            font = CobblemonResources.DEFAULT_LARGE,
            text = Component.literal("Moves: ${category.title}").bold(),
            x = pX + 9,
            y = pY - 10,
            shadow = true
        )

        super.renderWidget(context, mouseX, mouseY, delta)
    }

    private fun switchCategory(forwards: Boolean, recursionCount: Int = 0) {
        val newIndex = this.category.ordinal + (if (forwards) 1 else -1)
        category = if (newIndex < 0) MoveCategory.entries.last() else if (newIndex > MoveCategory.entries.lastIndex) MoveCategory.entries.first() else MoveCategory.entries[newIndex]
        if (recursionCount < 10) { // To handle case of all categories being empty
            learnset?.let {
                if (isThisCategoryEmpty(it)) {
                    switchCategory(forwards, recursionCount + 1) // Skip over empty categories
                    return
                }
            }
        }
        refreshMoves()
    }

    fun refreshMoves() {
        this.setText(learnset?.let { getTextFromLearnset(it) } ?: listOf("Unknown"))
    }

    fun isThisCategoryEmpty(learnset: Learnset) : Boolean {
        if (category == MoveCategory.LEVEL_UP) {
            for ((_, moves) in learnset.levelUpMoves) {
                if (moves.isNotEmpty()) return false
            }
            return true
        } else {
            val moves = when (category) {
                MoveCategory.EGG -> learnset.eggMoves
                MoveCategory.TM -> learnset.tmMoves
                MoveCategory.TUTOR -> learnset.tutorMoves
                MoveCategory.EVOLUTIONS -> learnset.evolutionMoves
                MoveCategory.FORM_CHANGE -> learnset.formChangeMoves
                else -> return true
            }
            return moves.isEmpty()
        }
    }

    fun getTextFromLearnset(learnset: Learnset) : List<String> {
        val toReturn = mutableListOf<String>()
        if (category == MoveCategory.LEVEL_UP) {
            for ((level, moves) in learnset.levelUpMoves) {
                moves.forEach { toReturn.add("$level - ${it.displayName.string}") }
            }
        } else {
            val moves = when (category) {
                MoveCategory.EGG -> learnset.eggMoves
                MoveCategory.TM -> learnset.tmMoves
                MoveCategory.TUTOR -> learnset.tutorMoves
                MoveCategory.EVOLUTIONS -> learnset.evolutionMoves
                MoveCategory.FORM_CHANGE -> learnset.formChangeMoves
                else -> return emptyList<String>()
            }
            moves.forEach { toReturn.add(it.displayName.string) }
        }
        return toReturn
    }

    enum class MoveCategory(val title: String) {
        LEVEL_UP("Level Up"),
        EGG("Egg"),
        TM("TM"),
        TUTOR("Tutor"),
        EVOLUTIONS("Evolutions"),
        FORM_CHANGE("Form Change")
    }
}