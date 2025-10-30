/*
 * Copyright (C) 2023 Cobblemon Contributors
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package com.cobblemon.mod.common.client.render.pokemon

import net.minecraft.client.renderer.RenderStateShard

class EffectUniformState(
    name: String,
    setup: Runnable,
    clear: Runnable
) : RenderStateShard.TexturingStateShard(name, setup, clear)