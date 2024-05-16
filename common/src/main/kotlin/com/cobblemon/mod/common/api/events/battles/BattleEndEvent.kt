/*
 * Copyright (C) 2023 Cobblemon Contributors
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package com.cobblemon.mod.common.api.events.battles

import com.cobblemon.mod.common.api.battles.model.actor.BattleActor

/**
 * CUSTOM: MythicalNetwork - For MythicalNPCs
 *
 * Event fired for each participant of a battle when it's ended for easy detection.
 *
 * @param actor the actor that was in a battle
 */
class BattleEndEvent(val actor: BattleActor)