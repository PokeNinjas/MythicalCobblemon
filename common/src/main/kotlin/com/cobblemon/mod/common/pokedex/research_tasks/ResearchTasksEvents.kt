package com.cobblemon.mod.common.pokedex.research_tasks

import com.cobblemon.mod.common.Cobblemon
import com.cobblemon.mod.common.api.battles.model.actor.BattleActor
import com.cobblemon.mod.common.api.events.CobblemonEvents
import com.cobblemon.mod.common.api.spawning.TimeRange
import com.cobblemon.mod.common.battles.ActiveBattlePokemon
import com.cobblemon.mod.common.battles.actor.PlayerBattleActor
import com.cobblemon.mod.common.entity.pokemon.PokemonEntity
import com.cobblemon.mod.common.pokemon.Pokemon
import net.minecraft.server.level.ServerPlayer

object ResearchTasksEvents {
    fun init() {
        CobblemonEvents.POKEMON_CAPTURED.subscribe { event ->
            val player = event.player
            val data = ComponentRegistry.RESEARCH_TASKS_DATA.get(player)

            val pokemon = event.pokemon.species.resourceIdentifier.path
            data.incrementProgress(pokemon, CatchResearchTask())
            data.incrementProgress(pokemon, CatchWithBallResearchTask(event.pokeBallEntity.pokeBall.name.toString()))
            data.incrementProgress(pokemon, CatchFormResearchTask(event.pokemon.form.name))
            event.pokemon.status?.status?.name?.path?.let { data.incrementProgress(pokemon, CatchWithStatusResearchTask(it)) }
            data.incrementProgress(pokemon, CatchGenderResearchTask(event.pokemon.gender.name))

            val time = (event.player.level().dayTime % 24000).toInt()
            TimeRange.timeRanges.forEach {
                if (it.value.contains(time)) {
                    data.incrementProgress(pokemon, CatchAtTimeResearchTask(it.key))
                }
            }
        }

        CobblemonEvents.HATCH_EGG_POST.subscribe { event ->
            val player = event.player
            val data = ComponentRegistry.RESEARCH_TASKS_DATA.get(player)

            event.egg.species?.let {data.incrementProgress(it, HatchResearchTask()) }
        }

        CobblemonEvents.EVOLUTION_ACCEPTED.subscribe { event ->
            event.pokemon.getOwnerPlayer()?.let { player ->
                val data = ComponentRegistry.RESEARCH_TASKS_DATA.get(player)

                data.incrementProgress(event.pokemon.species.resourceIdentifier.path, EvolveResearchTask())
                event.evolution.result.species?.let { data.incrementProgress(event.pokemon.species.resourceIdentifier.path, EvolveIntoResearchTask(it)) }
            }
        }

        CobblemonEvents.BOBBER_SPAWN_POKEMON_POST.subscribe { event ->
            event.bobber.playerOwner?.let {
                val data = ComponentRegistry.RESEARCH_TASKS_DATA.get(it)
                data.incrementProgress(event.pokemon.pokemon.species.resourceIdentifier.path, FishResearchTask())
            }
        }


        // Golden pokeball affecting shiny chance
        CobblemonEvents.SHINY_CHANCE_CALCULATION.subscribe { event ->
            event.addModificationFunction { baseChance, player, pokemon ->
                if (player != null) {
                    val data = ComponentRegistry.RESEARCH_TASKS_DATA.get(player)
                    val species = pokemon.species.resourceIdentifier.path
                    if (data.speciesWithAllTasksCompleted.contains(species)) {
                        return@addModificationFunction Cobblemon.researchTasksConfig.goldenPokeballShinyRates[species] ?: Cobblemon.researchTasksConfig.goldenPokeballShinyRates["default"] ?: baseChance
                    }
                }
                return@addModificationFunction baseChance
            }
        }

        CobblemonEvents.FOSSIL_REVIVED.subscribe { event ->
            event.player?.let {
                val data = ComponentRegistry.RESEARCH_TASKS_DATA.get(it)
                data.incrementProgress(event.pokemon.species.resourceIdentifier.path, ReviveResearchTask())
            }
        }

        CobblemonEvents.MEGA_EVOLUTION.subscribe { event ->
            if (event.pokemon.actor is PlayerBattleActor) {
                (event.pokemon.actor as PlayerBattleActor).entity?.let {
                    val data = ComponentRegistry.RESEARCH_TASKS_DATA.get(it)
                    data.incrementProgress(event.pokemon.originalPokemon.species.resourceIdentifier.path, MegaEvolveResearchTask())
                }
            }
        }
    }

    // Called from MythicalRaids
    fun raidDefeated(player: ServerPlayer, pokemon: Pokemon) {
        val data = ComponentRegistry.RESEARCH_TASKS_DATA.get(player)
        data.incrementProgress(pokemon.species.resourceIdentifier.path, RaidDefeatResearchTask())
    }

    fun moveUsed(actor: BattleActor, pokemon: ActiveBattlePokemon, move: String) {
        if (actor is PlayerBattleActor) {
            actor.entity?.let {player ->
                pokemon.battlePokemon?.originalPokemon?.species?.resourceIdentifier?.path?.let {
                    val data = ComponentRegistry.RESEARCH_TASKS_DATA.get(player)
                    data.incrementProgress(it, UseMoveResearchTask(move))
                }
            }
        }
    }

    fun pokemonSheared(player: ServerPlayer, pokemon: PokemonEntity) {
        val data = ComponentRegistry.RESEARCH_TASKS_DATA.get(player)
        data.incrementProgress(pokemon.pokemon.species.resourceIdentifier.path, ShearResearchTask())
    }

    fun pokemonMilked(player: ServerPlayer, pokemon: PokemonEntity) {
        val data = ComponentRegistry.RESEARCH_TASKS_DATA.get(player)
        data.incrementProgress(pokemon.pokemon.species.resourceIdentifier.path, MilkResearchTask())
    }

    fun pokemonDefeated(winnerBattleActor: BattleActor, looser: Pokemon) {
        if (winnerBattleActor is PlayerBattleActor) {
            winnerBattleActor.entity?.let {
                val data = ComponentRegistry.RESEARCH_TASKS_DATA.get(it)
                data.incrementProgress(looser.species.resourceIdentifier.path, DefeatResearchTask())
            }
        }
    }
}