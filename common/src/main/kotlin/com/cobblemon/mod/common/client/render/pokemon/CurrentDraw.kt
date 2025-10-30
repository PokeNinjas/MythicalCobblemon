/*
 * Copyright (C) 2023 Cobblemon Contributors
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package com.cobblemon.mod.common.client.render.pokemon

object CurrentDraw {
    private val ENTITY_ID = ThreadLocal.withInitial { -1 }
    fun setEntityId(id: Int) { ENTITY_ID.set(id) }
    fun getEntityId(): Int = ENTITY_ID.get()
}