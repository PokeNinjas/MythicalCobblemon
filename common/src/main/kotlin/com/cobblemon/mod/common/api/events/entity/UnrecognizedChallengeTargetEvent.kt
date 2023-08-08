package com.cobblemon.mod.common.api.events.entity

import com.cobblemon.mod.common.api.events.Cancelable
import net.minecraft.entity.Entity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.server.network.ServerPlayerEntity
import java.util.UUID

/**
 * Event fired when a player attempts to challenge an entity to a battle, but the entity is not another pokemon or player.
 * This is used to allow mods to add support for other entities to be challenged to battles.
 * @param targetedEntity The entity that was targeted for a battle.
 * @param player The player that attempted to challenge the entity.
 * @param leadingPokemon The UUID of the pokemon that the player was leading with.
 */
class UnrecognizedChallengeTargetEvent(val targetedEntity: Entity, val player: ServerPlayerEntity, val leadingPokemon: UUID) : Cancelable()