/*
 * Copyright (C) 2022 Cobblemon Contributors
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package com.cobblemon.mod.common.command

import com.cobblemon.mod.common.api.permission.CobblemonPermissions
import com.cobblemon.mod.common.api.permission.PermissionLevel
import com.cobblemon.mod.common.api.pokemon.PokemonProperties
import com.cobblemon.mod.common.command.argument.PartySlotArgumentType
import com.cobblemon.mod.common.util.commandLang
import com.cobblemon.mod.common.util.permission
import com.cobblemon.mod.common.util.permissionLevel
import com.cobblemon.mod.common.util.player
import com.mojang.brigadier.Command
import com.mojang.brigadier.CommandDispatcher
import com.mojang.brigadier.arguments.StringArgumentType
import com.mojang.brigadier.builder.ArgumentBuilder
import com.mojang.brigadier.context.CommandContext
import net.minecraft.command.argument.EntityArgumentType
import net.minecraft.server.command.CommandManager
import net.minecraft.server.command.ServerCommandSource
import net.minecraft.server.network.ServerPlayerEntity

object PokemonEditCommand {

    private const val NAME = "pokemonedit"
    private const val PLAYER = "player"
    private const val SLOT = "slot"
    private const val PROPERTIES = "properties"

    fun register(dispatcher : CommandDispatcher<ServerCommandSource>) {
        val command = CommandManager.literal(NAME)
            .permission(CobblemonPermissions.POKEMON_EDIT_SELF)
            .permissionLevel(PermissionLevel.CHEAT_COMMANDS_AND_COMMAND_BLOCKS)
            .then(CommandManager.argument(PLAYER, EntityArgumentType.player())
                .permission(CobblemonPermissions.POKEMON_EDIT_OTHER)
                .permissionLevel(PermissionLevel.MULTIPLAYER_MANAGEMENT)
                .then(createCommonArguments { it.player() })
            )
            .then(createCommonArguments { it.source.playerOrThrow })
        dispatcher.register(command)
    }

    private fun createCommonArguments(playerResolver: (CommandContext<ServerCommandSource>) -> ServerPlayerEntity): ArgumentBuilder<ServerCommandSource, *> {
        return CommandManager.argument(SLOT, PartySlotArgumentType.partySlot())
            .then(CommandManager.argument(PROPERTIES, StringArgumentType.greedyString())
                .executes { execute(it, playerResolver.invoke(it)) }
            )
    }

    private fun execute(context: CommandContext<ServerCommandSource>, player: ServerPlayerEntity) : Int {
        val pokemon = PartySlotArgumentType.getPokemon(context, SLOT)
        // They may change the species, think it makes sense to say the existing thing was edited, or maybe it doesn't & I'm a derp
        val oldName = pokemon.species.translatedName
        val properties = PokemonProperties.parse(StringArgumentType.getString(context, PROPERTIES))
        properties.apply(pokemon)
        context.source.sendFeedback(commandLang(NAME, oldName, player.name), true)
        return Command.SINGLE_SUCCESS
    }

}