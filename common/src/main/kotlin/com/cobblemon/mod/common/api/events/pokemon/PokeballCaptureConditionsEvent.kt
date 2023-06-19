package com.cobblemon.mod.common.api.events.pokemon

import com.cobblemon.mod.common.api.events.Cancelable
import com.cobblemon.mod.common.entity.pokeball.EmptyPokeBallEntity
import com.cobblemon.mod.common.entity.pokemon.PokemonEntity
import net.minecraft.text.MutableText
import net.minecraft.text.Text

data class PokeballCaptureConditionsEvent(
    val pokemonEntity: PokemonEntity,
    val captureState: EmptyPokeBallEntity.CaptureState,
    val pokeBallEntity: EmptyPokeBallEntity
) : Cancelable() {
    private var failMessage: MutableText? = null

    fun setFailMessage(message: MutableText) {
        failMessage = message
    }

    fun getFailMessage(): MutableText? {
        return failMessage
    }

    fun getFailMessageOrDefault(): MutableText {
        return failMessage ?: Text.of("You can't catch this pokemon!") as MutableText
    }
}