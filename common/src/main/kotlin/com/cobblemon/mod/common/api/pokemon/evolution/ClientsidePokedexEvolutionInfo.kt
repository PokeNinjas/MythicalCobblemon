package com.cobblemon.mod.common.api.pokemon.evolution

import com.cobblemon.mod.common.api.conditional.RegistryLikeCondition
import com.cobblemon.mod.common.api.conditional.RegistryLikeIdentifierCondition
import com.cobblemon.mod.common.api.conditional.RegistryLikeTagCondition
import com.cobblemon.mod.common.api.pokemon.PokemonProperties
import com.cobblemon.mod.common.api.pokemon.evolution.requirement.EvolutionRequirement
import com.cobblemon.mod.common.api.spawning.TimeRange
import com.cobblemon.mod.common.api.spawning.condition.MoonPhase
import com.cobblemon.mod.common.pokemon.evolution.requirements.*
import com.cobblemon.mod.common.pokemon.evolution.variants.ItemInteractionEvolution
import com.cobblemon.mod.common.pokemon.evolution.variants.LevelUpEvolution
import com.cobblemon.mod.common.pokemon.evolution.variants.TradeEvolution
import com.cobblemon.mod.common.util.fromJson
import com.cobblemon.mod.common.util.readString
import com.cobblemon.mod.common.util.writeString
import com.google.gson.Gson
import net.minecraft.network.FriendlyByteBuf
import net.minecraft.resources.ResourceLocation
import java.util.Locale

data class ClientsidePokedexEvolutionInfo(
    val type: String,
    val result: PokemonProperties,
    val requirementsString: String
) {

    fun encode(buffer: FriendlyByteBuf) {
        buffer.writeString(type)
        buffer.writeString(result.saveToJSON(includeAspects = true).toString())
        buffer.writeString(requirementsString)
    }

    companion object {
        fun decode(buffer: FriendlyByteBuf) : ClientsidePokedexEvolutionInfo {
            val type = buffer.readString()
            val resultJson = buffer.readString()
            val requirementsString = buffer.readString()

            val result = PokemonProperties().loadFromJSON(Gson().fromJson(resultJson), aspectsIncluded = true)

            return ClientsidePokedexEvolutionInfo(type, result, requirementsString)
        }

        fun from(evolution: Evolution) : ClientsidePokedexEvolutionInfo {
            val type = if (evolution is TradeEvolution) "Trade" else if (evolution is LevelUpEvolution) "Level Up" else if (evolution is ItemInteractionEvolution) "Item Interact" else "Unknown Type"
            val result = evolution.result
            val requirementsString = StringBuilder()

            if (evolution is ItemInteractionEvolution) {
                requirementsString.append("Item: ${getStringForRegistryLikeCondition(evolution.requiredContext.item)}, ")
            }

            for (requirement in evolution.requirements) {
                if (requirement is BiomeRequirement && requirement.biomeAnticondition != null && getStringForRegistryLikeCondition(requirement.biomeAnticondition).lowercase().contains("anti")) continue
                requirementsString.append(getEvolutionRequirementString(requirement))
            }

            return ClientsidePokedexEvolutionInfo(type, result, requirementsString.removeSuffix(", ").toString())
        }

        fun getEvolutionRequirementString(requirement: EvolutionRequirement) : String {
            return when (requirement) {
                is AnyRequirement -> StringBuilder("Any of: ").apply { requirement.possibilities.forEach {append("${getEvolutionRequirementString(it)}, ")} }.toString()
                is AreaRequirement -> "In specific coords, "
                is AttackDefenceRatioRequirement -> when (requirement.ratio) {
                    AttackDefenceRatioRequirement.AttackDefenceRatio.ATTACK_HIGHER -> "Attack > Defence, "
                    AttackDefenceRatioRequirement.AttackDefenceRatio.DEFENCE_HIGHER -> "Defence > Attack, "
                    AttackDefenceRatioRequirement.AttackDefenceRatio.EQUAL -> "Attack = Defence, "
                }
                is BattleCriticalHitsRequirement -> "${requirement.amount} crits in a battle, "
                is BiomeRequirement -> if (requirement.biomeCondition != null) "In biome: ${getStringForRegistryLikeCondition(requirement.biomeCondition)}, " else if (requirement.biomeAnticondition != null) "<st>In biome: ${getStringForRegistryLikeCondition(requirement.biomeAnticondition)}</st>, " else "Unknown Requirement, "
                is BlocksTraveledRequirement -> "Travelled ${requirement.amount} blocks, "
                is DamageTakenRequirement -> "Taken ${requirement.amount} damage, "
                is DefeatRequirement -> "Defeat ${requirement.amount} ${requirement.target.species}, "
                is FriendshipRequirement -> "${requirement.amount} Friendship, "
                is HeldItemRequirement -> "Held Item: ${getStringForRegistryLikeCondition(requirement.itemCondition.item)}, "
                is LevelRequirement -> "Level: ${requirement.minLevel}, "
                is MoonPhaseRequirement -> "Moon Phase: ${when (requirement.moonPhase) {MoonPhase.FULL_MOON -> "Full Moon"; MoonPhase.WANING_GIBBOUS -> "Waning Gibbous"; MoonPhase.THIRD_QUARTER -> "Third Quarter"; MoonPhase.WANING_CRESCENT -> "Waning Crescent"; MoonPhase.NEW_MOON -> "New Moon"; MoonPhase.WAXING_CRESCENT -> "Waxing Crescent"; MoonPhase.FIRST_QUARTER -> "First Quarter"; MoonPhase.WAXING_GIBBOUS -> "Waxing Gibbous" }}, "
                is MoveSetRequirement -> "Has Move: ${requirement.move.name}, "
                is MoveTypeRequirement -> "Has Move Type: ${requirement.type.name}, "
                is PartyMemberRequirement -> if (requirement.contains) "Party Contains: ${requirement.target.species}, " else "<st>Party Contains: ${requirement.target.species}</st>, "
                is PlayerHasAdvancementRequirement -> "Advancement: ${nameFromResourceLocation(requirement.requiredAdvancement)}, "
                is PokemonPropertiesRequirement -> "Properties: ${requirement.target.asString()}, "
                is PropertyRangeRequirement -> "${requirement.range.start} <= ${requirement.feature} <= ${requirement.range.endInclusive}, "
                is RecoilRequirement -> "Recoil without fainting: ${requirement.amount}, "
                is StatCompareRequirement -> "${requirement.highStat} > ${requirement.lowStat}, "
                is StatEqualRequirement -> "${requirement.statOne} = ${requirement.statTwo}, "
                is StructureRequirement -> if (requirement.structureCondition != null) "In structure: ${getStringForRegistryLikeCondition(requirement.structureCondition)}, " else if (requirement.structureAnticondition != null) "<st>In structure: ${getStringForRegistryLikeCondition(requirement.structureAnticondition)}</st>, " else "Unknown Requirement, "
                is TimeRangeRequirement -> "Time: ${TimeRange.timeRangeNames[requirement.range.ranges.first().toString()] ?: "${requirement.range.ranges}"}, "
                is UseMoveRequirement -> "Used ${requirement.move.name} ${requirement.amount}x, "
                is WeatherRequirement -> if (requirement.isThundering == true) "Thundering, " else if (requirement.isRaining == true) "Raining, " else "Not Raining, "
                is WorldRequirement -> "Dimension: ${nameFromResourceLocation(requirement.identifier)}, "
                else -> "${requirement.javaClass.name}, "
            }
        }

        fun nameFromResourceLocation(rl: ResourceLocation?): String {
            return if (rl == null) {
                "Unknown"
            } else {
                rl.path.split("/").last().split("_").map { it.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() } }.joinToString(" ")
            }
        }

        fun <T> getStringForRegistryLikeCondition(condition: RegistryLikeCondition<T>) : String {
            return if (condition is RegistryLikeIdentifierCondition) {
                nameFromResourceLocation(condition.identifier)
            } else if (condition is RegistryLikeTagCondition) {
                nameFromResourceLocation(condition.tag.location)
            } else {
                "Unknown"
            }
        }
    }
}