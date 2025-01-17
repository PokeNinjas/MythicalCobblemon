/*
 * Copyright (C) 2023 Cobblemon Contributors
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package com.cobblemon.mod.common.api.events.entity

import com.cobblemon.mod.common.api.events.Cancelable
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.entity.Entity
import java.util.*

/**
 * CUSTOM: MythcialNetwork - For MythicalNPCs
 *
 * Event fired when a player attempts to challenge an entity to a battle, but the entity is not another pokemon or player.
 * This is used to allow mods to add support for other entities to be challenged to battles.
 * @param targetedEntity The entity that was targeted for a battle.
 * @param player The player that attempted to challenge the entity.
 * @param leadingPokemon The UUID of the pokemon that the player was leading with.
 */
class UnrecognizedChallengeTargetEvent(val targetedEntity: Entity, val player: ServerPlayer, val leadingPokemon: UUID) : Cancelable()
