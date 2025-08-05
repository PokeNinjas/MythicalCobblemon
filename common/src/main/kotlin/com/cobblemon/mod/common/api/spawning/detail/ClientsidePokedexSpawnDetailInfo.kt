package com.cobblemon.mod.common.api.spawning.detail

import com.cobblemon.mod.common.api.pokemon.PokemonProperties
import com.cobblemon.mod.common.api.pokemon.evolution.ClientsidePokedexEvolutionInfo.Companion.getStringForRegistryLikeCondition
import com.cobblemon.mod.common.api.pokemon.evolution.ClientsidePokedexEvolutionInfo.Companion.nameFromResourceLocation
import com.cobblemon.mod.common.api.spawning.TimeRange
import com.cobblemon.mod.common.api.spawning.condition.MoonPhase
import com.cobblemon.mod.common.api.spawning.condition.SpawningCondition
import com.cobblemon.mod.common.util.fromJson
import com.cobblemon.mod.common.util.readString
import com.cobblemon.mod.common.util.writeString
import com.google.gson.Gson
import net.minecraft.network.FriendlyByteBuf
import java.util.Locale
import kotlin.math.roundToInt

data class ClientsidePokedexSpawnDetailInfo(
    var name: String,
    var pokemonProperties: PokemonProperties,
    var levelRange: IntRange?,
    var contextName: String,
    var bucketName: String,
    var conditionsString: String
) {
    fun encode(buffer: FriendlyByteBuf) {
        buffer.writeString(name)
        buffer.writeString(pokemonProperties.saveToJSON(includeAspects = true).toString())
        buffer.writeNullable(levelRange) {buf, range ->
            buffer.writeInt(range.first)
            buffer.writeInt(range.last)
        }
        buffer.writeString(contextName)
        buffer.writeString(bucketName)
        buffer.writeString(conditionsString)
    }

    companion object {
        fun decode(buffer: FriendlyByteBuf) : ClientsidePokedexSpawnDetailInfo {
            val name = buffer.readString()
            val propertiesJson = buffer.readString()
            val pokemonProperties = PokemonProperties().loadFromJSON(Gson().fromJson(propertiesJson), aspectsIncluded = true)
            val levelRange = buffer.readNullable { buffer.readInt()..buffer.readInt() }
            val contextName = buffer.readString()
            val bucketName = buffer.readString()
            val conditionsString = buffer.readString()
            return ClientsidePokedexSpawnDetailInfo(name, pokemonProperties, levelRange, contextName, bucketName, conditionsString)
        }

        fun from(detail: PokemonSpawnDetail) : ClientsidePokedexSpawnDetailInfo {
            val name = detail.displayName ?: displayNameFromId(detail.id)
            var contextName = detail.context.name.split(" ").map { it.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() } }.joinToString(" ")
            var bucketName = detail.bucket.name.split("-").map { it.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() } }.joinToString("-")

            var conditionsString = StringBuilder()
            detail.conditions.forEach { handleCondition(it, conditionsString, false) }
            detail.anticonditions.forEach { handleCondition(it, conditionsString, true) }

            return ClientsidePokedexSpawnDetailInfo(name, detail.pokemon, detail.levelRange, contextName, bucketName, conditionsString.removeSuffix("\n").removeSuffix("</st>").toString())
        }

        private fun handleCondition(
            condition: SpawningCondition<*>,
            conditionsString: java.lang.StringBuilder,
            anti: Boolean
        ) {
            val p = if (anti) "<st>" else ""
            val s = if (anti) "</st>" else ""
            condition.dimensions?.let {
                conditionsString.append("${p}In Dimension: ")
                it.forEach { conditionsString.append(nameFromResourceLocation(it));conditionsString.append(", ") }
                conditionsString.deleteCharAt(conditionsString.lastIndex)
                conditionsString.deleteCharAt(conditionsString.lastIndex)
                conditionsString.append("${s}\n")
            }

            condition.biomes?.let {
                conditionsString.append("${p}In Biome: ")
                it.forEach { conditionsString.append(getStringForRegistryLikeCondition(it));conditionsString.append(", ") }
                conditionsString.deleteCharAt(conditionsString.lastIndex)
                conditionsString.deleteCharAt(conditionsString.lastIndex)
                conditionsString.append("${s}\n")
            }

            condition.moonPhase?.let {
                conditionsString.append("${p}Moon: ")
                it.ranges.forEach { it.forEach { conditionsString.append(MoonPhase.entries[it].name.lowercase().split("_").map { it.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() } }.joinToString(" ")); conditionsString.append(", ") }}
                conditionsString.deleteCharAt(conditionsString.lastIndex)
                conditionsString.deleteCharAt(conditionsString.lastIndex)
                conditionsString.append("${s}\n")
            }

            condition.canSeeSky?.let { conditionsString.append("${p}Can See Sky: ${if (it) "Yes" else "No"}${s}\n") }
            condition.minX?.let { conditionsString.append("${p}Min X: ${it.roundToInt()}${s}\n") }
            condition.minY?.let { conditionsString.append("${p}Min Y: ${it.roundToInt()}${s}\n") }
            condition.minZ?.let { conditionsString.append("${p}Min Z: ${it.roundToInt()}${s}\n") }
            condition.maxX?.let { conditionsString.append("${p}Max X: ${it.roundToInt()}${s}\n") }
            condition.maxY?.let { conditionsString.append("${p}Max Y: ${it.roundToInt()}${s}\n") }
            condition.maxZ?.let { conditionsString.append("${p}Max Z: ${it.roundToInt()}${s}\n") }
            condition.minLight?.let { conditionsString.append("${p}Min Light: $it${s}\n") }
            condition.maxLight?.let { conditionsString.append("${p}Max Light: $it${s}\n") }
            condition.minSkyLight?.let { conditionsString.append("${p}Min Sky Light: $it${s}\n") }
            condition.maxSkyLight?.let { conditionsString.append("${p}Max Sky Light: $it${s}\n") }
            condition.isRaining?.let { conditionsString.append("${p}Raining: ${if (it) "Yes" else "No"}${s}\n") }
            condition.isThundering?.let { conditionsString.append("${p}Thundering: ${if (it) "Yes" else "No"}${s}\n") }
            condition.isSlimeChunk?.let { conditionsString.append("${p}Slime Chunk: ${if (it) "Yes" else "No"}${s}\n") }
            condition.timeRange?.let { conditionsString.append("${p}Time: ${TimeRange.timeRangeNames[it.ranges.first().toString()] ?: "${it.ranges}"}${s}\n")
            }

            condition.structures?.let {
                conditionsString.append("${p}In Structure: ")
                it.forEach {
                    it.ifLeft { conditionsString.append(nameFromResourceLocation(it)) }
                    it.ifRight { conditionsString.append(nameFromResourceLocation(it.location)) }
                    conditionsString.append(", ")
                }
                conditionsString.deleteCharAt(conditionsString.lastIndex)
                conditionsString.deleteCharAt(conditionsString.lastIndex)
                conditionsString.append("${s}\n")
            }
        }

        fun displayNameFromId(id: String) : String {
            val split = id.split("-")
            return if (split.size == 1) id // Not sure what's going on here, fall back to ID
            else if (split.size == 2) {
                "Normal ${split[1]}"
            } else {
                split.drop(1).map { it.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() } }.joinToString(" ")
            }
        }
    }
}