/*
 * Copyright (C) 2023 Cobblemon Contributors
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package com.cobblemon.mod.common.battles

import com.cobblemon.mod.common.Cobblemon.LOGGER
import com.cobblemon.mod.common.api.battles.interpreter.*
import com.cobblemon.mod.common.api.battles.model.PokemonBattle
import com.cobblemon.mod.common.api.battles.model.actor.BattleActor
import com.cobblemon.mod.common.api.battles.model.actor.EntityBackedBattleActor
import com.cobblemon.mod.common.api.text.red
import com.cobblemon.mod.common.api.text.yellow
import com.cobblemon.mod.common.battles.dispatch.InstructionSet
import com.cobblemon.mod.common.battles.dispatch.InterpreterInstruction
import com.cobblemon.mod.common.battles.interpreter.ContextManager
import com.cobblemon.mod.common.battles.interpreter.instructions.*
import com.cobblemon.mod.common.battles.pokemon.BattlePokemon
import com.cobblemon.mod.common.util.battleLang
import com.cobblemon.mod.common.util.runOnServer
import net.minecraft.world.phys.Vec3
import java.util.UUID
import kotlin.collections.Iterator
import kotlin.collections.filter
import kotlin.collections.forEach
import kotlin.collections.listOf
import kotlin.collections.map
import kotlin.collections.mutableListOf
import kotlin.collections.mutableMapOf
import kotlin.collections.set
import kotlin.collections.toMutableList
import kotlin.collections.toTypedArray

@Suppress("KotlinPlaceholderCountMatchesArgumentCount", "UNUSED_PARAMETER")
object ShowdownInterpreter {
    // Stores a reference to the previous ability, activate, or move message in a battle so a minor action can refer back to it (Battle UUID :  BattleMessage)
    val lastCauser = mutableMapOf<UUID, BattleMessage>()

    private val updateInstructionParser = mutableMapOf<String, (PokemonBattle, InstructionSet, BattleMessage, Iterator<BattleMessage>) -> InterpreterInstruction>()
    private val splitInstructionParser = mutableMapOf<String, (PokemonBattle, BattleActor, InstructionSet, BattleMessage, BattleMessage, Iterator<BattleMessage>) -> InterpreterInstruction>()
    private val sideInstructionParser = mutableMapOf<String, (PokemonBattle, BattleActor, InstructionSet, BattleMessage) -> InterpreterInstruction>()

    init {
        updateInstructionParser["split"] = { battle, instructionSet, message, messages ->
            val privateMessage = messages.next()
            val publicMessage = messages.next()
            val targetActor = battle.getActor(message.argumentAt(0)!!)!!
            val type = publicMessage.rawMessage.split("|")[1]
            splitInstructionParser[type]?.invoke(battle, targetActor, instructionSet, publicMessage, privateMessage, messages) ?: IgnoredInstruction()
        }

        listOf(
            "player", "teamsize", "gametype", "gen", "tier", "rated", "clearpoke", "poke", "teampreview", "start", "rule", "t:", "", "capture", "-anim"
        ).forEach { updateInstructionParser[it] = { _, _, _, _ -> IgnoredInstruction() } }

        updateInstructionParser["-ability"]              = { _, instructionSet, message, _ -> AbilityInstruction(instructionSet, message) }
        updateInstructionParser["-activate"]             = { _, instructionSet, message, _ -> ActivateInstruction(instructionSet, message) }
        updateInstructionParser["bagitem"]               = { _, _, message, _ -> BagItemInstruction(message) }
        updateInstructionParser["-boost"]                = { battle, _, message, _ -> BoostInstruction(battle, message, true) }
        updateInstructionParser["-block"]                = { _, _, message, _ -> BlockInstruction(message) }
        updateInstructionParser["cant"]                  = { _, _, message, _ -> CantInstruction(message) }
        updateInstructionParser["-clearallboost"]        = { _, _, message, _ -> ClearAllBoostInstruction(message) }
        updateInstructionParser["-clearnegativeboost"]   = { _, _, message, _ -> ClearNegativeBoostInstruction(message) }
        updateInstructionParser["-clearboost"]           = {  _, _, message, _ -> ClearBoostInstruction(message) }
        updateInstructionParser["-copyboost"]            = { _, _, message, _ -> CopyBoostInstruction(message) }
        updateInstructionParser["-crit"]                 = { _, instructionSet, message, _ -> CritInstruction(message, instructionSet) }
        updateInstructionParser["-curestatus"]           = { _, _, message, _ -> CureStatusInstruction(message) }
        updateInstructionParser["detailschange"]         = { _, _, message, _ -> FormeChangeInstruction(message) }
        updateInstructionParser["-endability"]           = { _, _, message, _ -> EndAbilityInstruction(message) }
        updateInstructionParser["-end"]                  = { _, _, message, _ -> EndInstruction(message) }
        updateInstructionParser["-enditem"]              = { _, _, message, _ -> EndItemInstruction(message) }
        updateInstructionParser["-fail"]                 = { _, _, message, _ -> FailInstruction(message) }
        updateInstructionParser["faint"]                 = { battle, _, message, _ -> FaintInstruction(battle, message) }
        updateInstructionParser["-fieldactivate"]        = { _, _, message, _ -> FieldActivateInstruction(message) }
        updateInstructionParser["-fieldend"]             = { _, _, message, _ -> FieldEndInstruction(message) }
        updateInstructionParser["-fieldstart"]           = { _, _, message, _ -> FieldStartInstruction(message) }
        updateInstructionParser["-formechange"]          = { _, _, message, _ -> FormeChangeInstruction(message) }
        updateInstructionParser["-hitcount"]             = { _, _, message, _ -> HitCountInstruction(message) }
        updateInstructionParser["-immune"]               = { _, _, message, _ -> ImmuneInstruction(message) }
        updateInstructionParser["-invertboost"]          = { _, _, message, _ -> InvertBoostInstruction(message) }
        updateInstructionParser["-item"]                 = { _, _, message, _ -> ItemInstruction(message) }
        updateInstructionParser["-mega"]                 = { _, _, message, _ -> MegaInstruction(message) }
        updateInstructionParser["-miss"]                 = { battle, _, message, _ -> MissInstruction(battle, message) }
        updateInstructionParser["move"]                  = { _, instructionSet, message, _ -> MoveInstruction(instructionSet, message) }
        updateInstructionParser["-nothing"]              = { _, _, _, _ -> NothingInstruction() }
        updateInstructionParser["pp_update"]             = { _, _, message, _ -> PpUpdateInstruction(message) }
        updateInstructionParser["-prepare"]              = { _, _, message, _ -> PrepareInstruction(message) }
        updateInstructionParser["-mustrecharge"]         = { _, _, message, _ -> RechargeInstruction(message) }
        updateInstructionParser["replace"]               = { _, _, message, _ -> ReplaceInstruction(message) }
        updateInstructionParser["-resisted"]             = { _, instructionSet, message, _ -> ResistedInstruction(message, instructionSet) }
        updateInstructionParser["-setboost"]             = { _, _, message, _ -> SetBoostInstruction(message) }
        updateInstructionParser["-sideend"]              = { _, _, message, _ -> SideEndInstruction(message) }
        updateInstructionParser["-sidestart"]            = { _, _, message, _ -> SideStartInstruction(message) }
        updateInstructionParser["-singlemove"]           = { _, _, message, _ -> SingleMoveInstruction(message) }
        updateInstructionParser["-singleturn"]           = { _, _, message, _ -> SingleTurnInstruction(message) }
        updateInstructionParser["start"]                 = { _, instructionSet, message, _ -> InitializeInstruction(instructionSet, message) }
        updateInstructionParser["-start"]                = { _, _, message, _ -> StartInstruction(message) }
        updateInstructionParser["-status"]               = { _, _, message, _ -> StatusInstruction(message) }
        updateInstructionParser["-supereffective"]       = { _, instructionSet, message, _ -> SuperEffectiveInstruction(message, instructionSet) }
        updateInstructionParser["-swapboost"]            = { _, _, message, _ -> SwapBoostInstruction(message) }
        updateInstructionParser["-swapsideconditions"]   = { _, _, message, _ -> SwapSideConditionsInstruction(message) }
        updateInstructionParser["-terastallize"]         = { _, _, message, _ -> TerastallizeInstruction(message) }
        updateInstructionParser["-transform"]            = { battle, _, message, _ -> TransformInstruction(battle, message) }
        updateInstructionParser["turn"]                  = { _, _, message, _ -> TurnInstruction(message) }
        updateInstructionParser["-unboost"]              = { battle, _, message, _ -> BoostInstruction(battle, message, false) }
        updateInstructionParser["upkeep"]                = { _, _, _, _ -> UpkeepInstruction() }
        updateInstructionParser["-weather"]              = { _, _, message, _ -> WeatherInstruction(message) }
        updateInstructionParser["win"]                   = { _, _, message, _ -> WinInstruction(message) }
        updateInstructionParser["-zbroken"]              = { _, _, message, _ -> ZBrokenInstruction(message) }
        updateInstructionParser["-zpower"]               = { _, _, message, _ -> ZPowerInstruction(message) }
        updateInstructionParser["swap"]                  = { _, instructionSet, message, _ -> SwapInstruction(message, instructionSet) }
        updateInstructionParser["-center"]               = { _, _, message, _ -> CenterInstruction(message) }

        sideInstructionParser["error"]                   = { _, targetActor, _, message -> ErrorInstruction(targetActor, message) }
        sideInstructionParser["request"]                 = { _, targetActor, _, message -> RequestInstruction(targetActor, message) }

        splitInstructionParser["-damage"]                = { _, targetActor, instructionSet, publicMessage, privateMessage, _ ->
                                                                DamageInstruction(instructionSet, targetActor, publicMessage, privateMessage)
                                                           }
        splitInstructionParser["drag"]                   = { _, targetActor, instructionSet, publicMessage, privateMessage, _ ->
                                                                DragInstruction(instructionSet, targetActor, publicMessage, privateMessage)
                                                           }
        splitInstructionParser["-heal"]                  = { _, targetActor, _, publicMessage, privateMessage, _ ->
                                                                HealInstruction(targetActor, publicMessage, privateMessage)
                                                           }
        splitInstructionParser["-sethp"]                 = { _, targetActor, _, publicMessage, privateMessage, _ ->
                                                                SetHpInstruction(targetActor, publicMessage, privateMessage)
                                                           }
        splitInstructionParser["switch"]                 = { _, targetActor, instructionSet, publicMessage, privateMessage, _ ->
                                                                SwitchInstruction(instructionSet, targetActor, publicMessage, privateMessage)
                                                           }


        // Note '-cureteam' is a legacy thing that is only used in generation 2 and 4 mods for heal bell and aromatherapy respectively as such we can just ignore that
    }

    /**
     *
     * Figures out the sendout position for a pokemon in a battle
     * 
     *
     */
     fun getSendoutPosition(battle: PokemonBattle, activePokemon: ActiveBattlePokemon, battleActor: BattleActor): Vec3? {
        val pnx = activePokemon.getPNX()
        val actorEntityPosList = battleActor.getSide().actors.mapNotNull { if (it is EntityBackedBattleActor<*>) it.initialPos else null }
        val actorEntityPos = if (actorEntityPosList.size == 1)
            actorEntityPosList[0]
        else if (actorEntityPosList.size > 1)
            actorEntityPosList.fold(Vec3(0.0, 0.0, 0.0)) { acc, vec3 -> acc.add(vec3.scale(1.0 / actorEntityPosList.size)) }
        else
            null
        val opposingActorEntityList = battleActor.getSide().getOppositeSide().actors.mapNotNull { if (it is EntityBackedBattleActor<*>) it.initialPos else null }
        val opposingEntityPos = if (opposingActorEntityList.size == 1)
            opposingActorEntityList[0]
        else if (opposingActorEntityList.size > 1) {
            // If multiple actors per side, avg their position
            opposingActorEntityList.fold(Vec3(0.0, 0.0, 0.0)) { acc, vec3 -> acc.add(vec3.scale(1.0 / opposingActorEntityList.size)) }
        }
        else null
        
        var actorOffset = actorEntityPos?.let { opposingEntityPos?.subtract(it) }
        var result = actorEntityPos
        if (actorOffset != null) {
            var widthSum = 4.0 // Leave a constant to allow double/triples alignment to be consistent
            if (battle.format.battleType.pokemonPerSide == 1) {
                activePokemon.battlePokemon?.let { battlePokemon ->
                    // sum of the hitbox widths of both pokemon
                    val opposingActivePokemon = (activePokemon.getOppositeOpponent() as ActiveBattlePokemon)
                    val pokemonWidth = battlePokemon.originalPokemon.form.hitbox.width * battlePokemon.originalPokemon.form.baseScale
                    val opposingPokemonWidth = opposingActivePokemon.battlePokemon?.let {
                        it.originalPokemon.form.hitbox.width * it.originalPokemon.form.baseScale
                    } ?: pokemonWidth
                    widthSum = (pokemonWidth + opposingPokemonWidth) / 2.0 // Only care about the front half of each hitbox
                }
            }

            val minDistance = 4.0 + widthSum
            val actorDistance = actorOffset.length()

            if (actorDistance < minDistance) {
                val temp = actorOffset.scale(minDistance / actorDistance) ?: actorOffset
                result = actorEntityPos?.subtract(temp.subtract(actorOffset))
                actorOffset = temp
            }
            var vector = Vec3(actorOffset.x, 0.0, actorOffset.z).normalize()
            vector = vector.cross(Vec3(0.0, 1.0, 0.0))

            if (battle.format.battleType.pokemonPerSide == 1) { // Singles
                result = result?.add(actorOffset.scale(if (battle.isPvW) 0.4 else 0.3))
                activePokemon.battlePokemon?.let { battlePokemon ->
                    val hitbox = battlePokemon.originalPokemon.form.hitbox
                    val scale = battlePokemon.originalPokemon.form.baseScale
                    activePokemon.getAdjacentOpponents()
                    result = result?.add(vector.scale(-0.3 - hitbox.width * scale ))
                }
            } else if (battle.format.battleType.pokemonPerSide == 2) { // Doubles/Multi
                if (battle.actors.first() !== battle.actors.last()) {
                    val offsetB = if (pnx[2] == 'a') vector.scale(-1.0) else vector
                    result = result?.add(actorOffset.scale(0.33))?.add(offsetB.scale(2.5))
                }
            } else if (battle.format.battleType.pokemonPerSide == 3) { // Triples
                if (battle.actors.first() !== battle.actors.last()) {
                    result = when (pnx[2]) {
                        'a' -> result?.add(actorOffset.scale(0.15))?.add(vector.scale(-3.5))
                        'b' -> result?.add(actorOffset.scale(0.3))
                        'c' -> result?.add(actorOffset.scale(0.15))?.add(vector.scale(3.5))
                        else -> result
                    }
                }
            }
        }
        return result
    }



    fun interpretMessage(battleId: UUID, message: String) {
        // Check key map and use function if matching
        if (message.startsWith("{\"winner\":\"")) {
            // The post-win message is something we don't care about just yet. It's basically a summary of what happened in the battle.
            // Check /docs/example-post-win-message.json for its format.
            return
        }

        val battle = BattleRegistry.getBattle(battleId)

        if (battle == null) {
            LOGGER.info("No battle could be found with the id: $battleId")
            return
        }

        runOnServer {
            battle.showdownMessages.add(message)
            interpret(battle, message)
        }
    }

    fun interpret(battle: PokemonBattle, rawMessage: String) {
        battle.log()
        battle.log(rawMessage)
        battle.log()
        val instructionSet = InstructionSet()
        val battleMessages = mutableListOf<BattleMessage>()


        try {
            val lines = rawMessage.split("\n").toMutableList()
            if (lines[0] == "update") {
                lines.removeAt(0)
                lines.forEach { battleMessages.add(BattleMessage(it)) }

                val iterator = battleMessages.iterator()
                while (iterator.hasNext()) {
                    val message = iterator.next()
                    val id = message.id.replace("|", "")
                    val instruction = updateInstructionParser[id]?.invoke(battle, instructionSet, message, iterator) ?: UnknownInstruction(message)
                    instructionSet.instructions.add(instruction)
                }
            }
            else if (lines[0] == "sideupdate") {
                val showdownId = lines[1]
                val targetActor = battle.getActor(showdownId)
                val message = BattleMessage(lines[2])
                val id = message.id.replace("|", "")

                if (targetActor == null) {
                    battle.log("No actor could be found with the showdown id: $showdownId")
                    return
                }

                val instruction = sideInstructionParser[id]?.invoke(battle, targetActor, instructionSet, message) ?: UnknownInstruction(message)
                instructionSet.instructions.add(instruction)
            }
            instructionSet.execute(battle)
        }
        catch (e: InvalidInstructionException) {
            e.message?.let {
                battle.broadcastChatMessage(it.red())
                LOGGER.error(it)
            }

        }
        catch (e: Exception) {
            battle.broadcastChatMessage("A fatal error occurred. Please report to developers.".red())
            LOGGER.error("Caught exception interpreting battle instructions.", e)
        }
    }

    fun broadcastOptionalAbility(battle: PokemonBattle, effect: Effect?, pokemon: BattlePokemon) {
        if (effect == null) return
        broadcastAbility(battle, effect, pokemon)
    }

    // Broadcasts a generic lang to notify players of ability activations (effects are broadcasted separately)
    fun broadcastAbility(battle: PokemonBattle, effect: Effect, pokemon: BattlePokemon) {
        if (effect.type != Effect.Type.ABILITY) return
        battle.dispatchWaiting(0.5F) {
            val lang = battleLang("ability.generic", pokemon.getName(), effect.typelessData).yellow()
            battle.broadcastChatMessage(lang)
        }
    }

    fun getContextFromFaint(pokemon: BattlePokemon, battle: PokemonBattle): BattleContext {
        val cause = battle.minorBattleActions[pokemon.uuid] ?: lastCauser[battle.battleId] ?: return MissingContext()
        val side = pokemon.actor.getSide()

        return when (cause.id) {
            "-damage", "move" -> {
                // damage from abilities
                cause.effect("of")?.let {
                    val effectID = cause.effect()?.id ?: it.id
                    val originPnx = cause.optionalArgument("of")!!.substringBefore(':')
                    val uuid = cause.optionalArgument("of")!!.substringAfter(':').trim()
                    val origin = battle.getBattlePokemon(originPnx, uuid)
                    BasicContext(effectID, battle.turn, BattleContext.Type.FAINT, origin)
                } ?:
                // damage from weather, statuses, entry hazards
                cause.effect()?.let { effect ->
                    val damagingContexts = BattleContext.Type.values().filter { it.damaging }
                    val contextBuckets = damagingContexts.map { pokemon.contextManager.get(it) ?: side.contextManager.get(it)
                        ?: battle.contextManager.get(it) }
                    ContextManager.scoop(effect.id, *contextBuckets.toTypedArray())
                } ?:
                // damage from moves and suicide
                lastCauser[battle.battleId]?.let {
                    val move = it.effectAt(1)!!.id
                    val origin = it.battlePokemon(0, battle)
                    BasicContext(move, battle.turn, BattleContext.Type.FAINT, origin)
                } ?:
                MissingContext()
            }
            // perish song
            "-start" -> {
                cause.effectAt(1)?.let {
                    val effectID = if (it.id.contains("perish")) "perishsong" else it.id
                    ContextManager.scoop(effectID, pokemon.contextManager.get(BattleContext.Type.VOLATILE))
                } ?:
                MissingContext()
            }
            // destiny bond
            "-activate" -> {
                cause.effectAt(1)?.let {
                    val origin = cause.battlePokemon(0, battle)
                    BasicContext(it.id, battle.turn, BattleContext.Type.FAINT, origin)
                } ?:
                MissingContext()
            }
            else -> MissingContext()
        }
    }

    fun getContextFromAction(message: BattleMessage, type: BattleContext.Type, battle: PokemonBattle): BattleContext {
        // |-action|POKEMON|EFFECT|[from]EFFECT|[of]POKEMON or |-action|EFFECT|[from]EFFECT|[of]POKEMON
        return message.actorAndActivePokemonFromOptional(battle)?.let {
            // ex: |-item|p2a: ###|Black Sludge|[from] ability: Pickpocket|[of] p1a: ###
            val effectID = message.effectAt(1)?.id ?: message.effectAt(0)?.id ?: return@let MissingContext()
            BasicContext(effectID, battle.turn, type, it.second.battlePokemon)
        } ?:
        // |-action|POKEMON|EFFECT| (caused by a move or another action)
        message.actorAndActivePokemon(0, battle)?.let {
            // ex: |-status|p2a: ###|par -> |move|p1a: ###|Glare|p2a: ###
            // ex: |-unboost|p1a: ###|atk|1 -> |-ability|p2a: ###|Intimidate|boost
            val effectID = message.effectAt(1)?.id ?: return@let MissingContext()
            val origin = lastCauser[battle.battleId]?.battlePokemon(0, battle) ?: return@let MissingContext()
            BasicContext(effectID, battle.turn, type, origin)
        } ?:
        // |-action|EFFECT
        lastCauser[battle.battleId]?.let {
            // ex: |-sidestart|p2: ###|move: Toxic Spikes -> |-activate|p1a: ###|ability: Toxic Debris
            // ex: |-weather|Sandstorm -> |move|p1a: ###|Sandstorm|p1a: ###
            val effectID = message.effectAt(1)?.id ?: message.effectAt(0)?.id ?: return@let MissingContext()
            BasicContext(effectID, battle.turn, type, it.battlePokemon(0, battle))
        } ?:
        MissingContext()
    }
}
