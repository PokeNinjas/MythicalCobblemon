/*
 * Copyright (C) 2022 Pokemod Cobbled Contributors
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package com.cablemc.pokemod.common.pokemon.feature

/**
 * A feature that keeps track of battle damage.
 * They reset when a Pokémon is healed or faints.
 *
 * @author Licious
 * @since October 2nd, 2022
 */
class DamageTakenFeature : ResettableAmountFeature() {

    override fun createInstance(value: Int) = DamageTakenFeature().apply { currentValue = value }

    override val name: String = ID

    companion object {
        const val ID = "damage_taken"
    }

}