package com.cobblemon.mod.common.pokedex.research_tasks

import com.cobblemon.mod.common.api.pokemon.PokemonSpecies
import com.cobblemon.mod.common.util.lang
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.Component.translatable
import net.minecraft.network.chat.MutableComponent
import net.minecraft.resources.ResourceLocation
import java.util.Locale

object ResearchTasks {
    fun fromFullIdentifier(fullIdentifier: String): ResearchTask {
        return when (fullIdentifier) {
            "catch" -> CatchResearchTask()
            "evolve" -> EvolveResearchTask()
            "defeat" -> DefeatResearchTask()
            "fish" -> FishResearchTask()
            "shear" -> ShearResearchTask()
            "hatch" -> HatchResearchTask()
            "milk" -> MilkResearchTask()
            "revive" -> ReviveResearchTask()
            "mega_evolve" -> MegaEvolveResearchTask()
            "raid_defeat" -> RaidDefeatResearchTask()
            else -> {
                val split = fullIdentifier.split("!")
                if (split.size != 2) throw IllegalArgumentException("Invalid research task identifier $fullIdentifier!")
                when (split[0]) {
                    "use_move" -> UseMoveResearchTask(split[1])
                    "evolve_into" -> EvolveIntoResearchTask(split[1])
                    "catch_with" -> CatchWithBallResearchTask(split[1])
                    "catch_form" -> CatchFormResearchTask(split[1])
                    "catch_status" -> CatchWithStatusResearchTask(split[1])
                    "catch_time" -> CatchAtTimeResearchTask(split[1])
                    "catch_gender" -> CatchGenderResearchTask(split[1])
                    else -> throw IllegalArgumentException("Invalid research task identifier $fullIdentifier!")
                }
            }
        }
    }
}

open class ResearchTask(val identifier: String, protected val target: String?) {
    open fun getDisplayName(): MutableComponent = throw NotImplementedError()
    fun getFullIdentifier() = target?.let {"$identifier!${it.lowercase()}"} ?: identifier
}

abstract class SimpleResearchTask(identifier: String, val displayName: String) : ResearchTask(identifier, null) {
    override fun getDisplayName(): MutableComponent {
        return Component.literal(displayName)
    }
}

class CatchResearchTask : SimpleResearchTask("catch", "Catch")
class EvolveResearchTask : SimpleResearchTask("evolve", "Evolve")
class DefeatResearchTask : SimpleResearchTask("defeat", "Defeat")
class FishResearchTask : SimpleResearchTask("fish", "Fish")
class ShearResearchTask : SimpleResearchTask("shear", "Shear")
class HatchResearchTask : SimpleResearchTask("hatch", "Hatch")
class MilkResearchTask : SimpleResearchTask("milk", "Milk")
class ReviveResearchTask : SimpleResearchTask("revive", "Revive From Fossil")
class MegaEvolveResearchTask : SimpleResearchTask("mega_evolve", "Mega Evolve")
class RaidDefeatResearchTask : SimpleResearchTask("raid_defeat", "Defeat in Raid Battle")

class UseMoveResearchTask(move: String) : ResearchTask("use_move", move) {
    override fun getDisplayName(): MutableComponent {
        return Component.literal("Use Move ").append(lang("move.${target?.lowercase()}"))
    }
}

class EvolveIntoResearchTask(pokemon: String) : ResearchTask("evolve_into", pokemon) {
    override fun getDisplayName(): MutableComponent {
        return Component.literal("Evolve Into ").append(PokemonSpecies.getByName(target?.lowercase()!!)?.translatedName ?: Component.literal("Invalid Pokemon Configured"))
    }
}

class CatchWithBallResearchTask(ball: String) : ResearchTask("catch_with", ball) {
    override fun getDisplayName(): MutableComponent {
        return Component.literal("Catch With ").append(
            ResourceLocation.tryParse(target?.lowercase()!!)?.let {translatable("item.${it.namespace}.${it.path}")} ?: Component.literal("Invalid PokeBall Configured"))
    }
}

class CatchFormResearchTask(form: String) : ResearchTask("catch_form", form) {
    override fun getDisplayName(): MutableComponent {
        return Component.literal("Catch Form ${target!!.lowercase().split("_").map { it.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() } }.joinToString(" ")}")
    }
}

class CatchWithStatusResearchTask(status: String) : ResearchTask("catch_status", status) {
    override fun getDisplayName(): MutableComponent {
        return Component.literal("Catch With Status: ${target!!.lowercase().replaceFirstChar {it.titlecase(Locale.getDefault())}}")
    }
}

class CatchAtTimeResearchTask(time: String) : ResearchTask("catch_time", time) {
    override fun getDisplayName(): MutableComponent {
        return Component.literal("Catch At ${target!!.lowercase().replaceFirstChar {it.titlecase(Locale.getDefault())}}")
    }
}

class CatchGenderResearchTask(gender: String) : ResearchTask("catch_gender", gender) {
    override fun getDisplayName(): MutableComponent {
        return Component.literal("Catch Gender: ${target!!.lowercase().replaceFirstChar {it.titlecase(Locale.getDefault())}}")
    }
}