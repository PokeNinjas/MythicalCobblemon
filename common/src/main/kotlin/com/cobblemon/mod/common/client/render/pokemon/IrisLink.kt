/*
 * Copyright (C) 2023 Cobblemon Contributors
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package com.cobblemon.mod.common.client.render.pokemon

object IrisLink {
    private val method = runCatching {
        val cls = Class.forName("net.irisshaders.iris.uniforms.CobblemonBridge")
        cls.getMethod("setEffectForEntity", Int::class.javaPrimitiveType)
    }.getOrNull()

    fun setEffectForEntity(effectId: Int) {
        try { method?.invoke(null, effectId) } catch (_: Throwable) {}
    }
}