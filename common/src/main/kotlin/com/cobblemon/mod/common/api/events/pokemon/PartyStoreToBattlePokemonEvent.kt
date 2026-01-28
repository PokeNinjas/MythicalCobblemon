package com.cobblemon.mod.common.api.events.pokemon

import com.cobblemon.mod.common.api.storage.party.PartyStore
import com.cobblemon.mod.common.battles.pokemon.BattlePokemon

class PartyStoreToBattlePokemonEvent(val partyStore: PartyStore, val team: MutableList<BattlePokemon>) {
}