package com.cablemc.pokemoncobbled.common.command

import com.cablemc.pokemoncobbled.common.PokemonCobbled.config
import com.cablemc.pokemoncobbled.common.api.spawning.CobbledWorldSpawnerManager
import com.cablemc.pokemoncobbled.common.api.spawning.SpawnCause
import com.cablemc.pokemoncobbled.common.api.spawning.spawner.SpawningArea
import com.cablemc.pokemoncobbled.common.api.text.green
import com.cablemc.pokemoncobbled.common.api.text.lightPurple
import com.cablemc.pokemoncobbled.common.api.text.plus
import com.cablemc.pokemoncobbled.common.api.text.red
import com.cablemc.pokemoncobbled.common.api.text.yellow
import com.cablemc.pokemoncobbled.common.command.argument.SpawnBucketArgumentType
import com.cablemc.pokemoncobbled.common.util.sendServerMessage
import com.mojang.brigadier.Command
import com.mojang.brigadier.CommandDispatcher
import com.mojang.brigadier.context.CommandContext
import java.text.DecimalFormat
import net.minecraft.server.command.CommandManager
import net.minecraft.server.command.ServerCommandSource
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.text.MutableText
import net.minecraft.util.math.MathHelper

object CheckSpawnsCommand {
    const val PURPLE_THRESHOLD = 0.01F
    const val RED_THRESHOLD = 0.1F
    const val YELLOW_THRESHOLD = 5F
    val df = DecimalFormat("#.##")

    fun register(dispatcher : CommandDispatcher<ServerCommandSource>) {
        val command = dispatcher.register(CommandManager.literal("checkspawn")
            .requires { it.hasPermissionLevel(4) }
            .then(
                CommandManager.argument("bucket", SpawnBucketArgumentType.spawnBucket())
                    .requires { it != it.server}
                    .executes { execute(it, it.source.player) }
            ))
        dispatcher.register(CommandManager.literal("pokegive").redirect(command))
    }

    private fun execute(context: CommandContext<ServerCommandSource>, player: ServerPlayerEntity) : Int {
        val spawner = CobbledWorldSpawnerManager.spawnersForPlayers[player.uuid] ?: return Command.SINGLE_SUCCESS
        val bucket = SpawnBucketArgumentType.getSpawnBucket(context, "bucket")
        val cause = SpawnCause(spawner, bucket, player)

        val slice = spawner.prospector.prospect(
            spawner = spawner,
            area = SpawningArea(
                cause = cause,
                world = player.world,
                baseX = MathHelper.ceil(player.x - config.worldSliceDiameter / 2F),
                baseY = MathHelper.ceil(player.y - config.worldSliceHeight / 2F),
                baseZ = MathHelper.ceil(player.z - config.worldSliceDiameter / 2F),
                length = config.worldSliceDiameter,
                height = config.worldSliceHeight,
                width = config.worldSliceDiameter
            )
        )

        val contexts = spawner.resolver.resolve(spawner, spawner.contextCalculators, slice)

        val spawnProbabilities = spawner.getSpawningSelector().getProbabilities(spawner, contexts)

        val spawnNames = mutableMapOf<String, MutableText>()
        val namedProbabilities = mutableMapOf<MutableText, Float>()

        spawnProbabilities.entries.forEach {
            val nameText = it.key.getName()
            val nameString = nameText.asString()
            if (!spawnNames.containsKey(nameString)) {
                spawnNames[nameString] = it.key.getName()
            }

            val standardizedNameText = spawnNames[nameString]!!
            namedProbabilities[standardizedNameText] = (namedProbabilities[standardizedNameText] ?: 0F) + it.value
        }

        val sortedEntries = namedProbabilities.entries.sortedByDescending { it.value }
        sortedEntries.forEach { (name, percentage) ->
            player.sendServerMessage(applyColour(name + ": " + df.format(percentage), percentage))
        }

        return Command.SINGLE_SUCCESS
    }

    fun applyColour(name: MutableText, percentage: Float): MutableText {
        return if (percentage < PURPLE_THRESHOLD) {
            name.lightPurple()
        } else if (percentage < RED_THRESHOLD) {
            name.red()
        } else if (percentage < YELLOW_THRESHOLD) {
            name.yellow()
        } else {
            name.green()
        }
    }
}