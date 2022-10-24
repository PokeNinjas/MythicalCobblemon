/*
 * Copyright (C) 2022 Pokemod Cobbled Contributors
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package com.cablemc.pokemod.common.api.events.pokemon.evolution

import com.cablemc.pokemod.common.api.pokemon.evolution.Evolution
import com.cablemc.pokemod.common.pokemon.Pokemon

/**
 * Fired after an evolution finishes.
 *
 * @param pokemon The [Pokemon] resulting from the evolution.
 * @param evolution The [Evolution] that was used.
 *
 * @author Licious
 * @since October 2nd, 2022
 */
data class EvolutionCompleteEvent(
    val pokemon: Pokemon,
    val evolution: Evolution
)