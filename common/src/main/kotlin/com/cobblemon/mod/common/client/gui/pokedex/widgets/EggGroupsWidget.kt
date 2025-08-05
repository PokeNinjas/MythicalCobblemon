/*
 * Copyright (C) 2023 Cobblemon Contributors
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package com.cobblemon.mod.common.client.gui.pokedex.widgets

import com.cobblemon.mod.common.api.pokemon.egg.EggGroup
import com.cobblemon.mod.common.api.text.bold
import com.cobblemon.mod.common.client.CobblemonResources
import com.cobblemon.mod.common.client.render.drawScaledText
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.network.chat.Component

class EggGroupsWidget(x: Int, y: Int): InfoTextScrollWidget(pX = x, pY = y) {

    var eggGroups: Set<EggGroup>? = null

    override fun renderWidget(context: GuiGraphics, mouseX: Int, mouseY: Int, delta: Float) {
        drawScaledText(
            context = context,
            font = CobblemonResources.DEFAULT_LARGE,
            text = Component.literal("Egg Groups").bold(),
            x = pX + 9,
            y = pY - 10,
            shadow = true
        )

        super.renderWidget(context, mouseX, mouseY, delta)
    }

    fun refresh() {
        this.setText(eggGroups?.let { it.map { it.showdownID } } ?: listOf("Unknown"))
    }
}