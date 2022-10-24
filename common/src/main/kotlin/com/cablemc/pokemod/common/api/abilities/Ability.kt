/*
 * Copyright (C) 2022 Pokemod Cobbled Contributors
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package com.cablemc.pokemod.common.api.abilities

import com.cablemc.pokemod.common.util.DataKeys
import com.google.gson.JsonObject
import net.minecraft.nbt.NbtCompound
import net.minecraft.text.Text

/**
 * Representing an Ability with all its attributes
 *
 * Can be extended to allow for custom attributes (be sure to overwrite the load and save methods)
 *
 * @author Qu
 * @since January 9th, 2022
 */
open class Ability internal constructor(var template: AbilityTemplate) {

    val name: String
        get() = template.name

    val displayName: Text
        get() = template.displayName

    val description: Text
        get() = template.description

    open fun saveToNBT(nbt: NbtCompound): NbtCompound {
        nbt.putString(DataKeys.POKEMON_ABILITY_NAME, name)
        return nbt
    }

    open fun saveToJSON(json: JsonObject): JsonObject {
        json.addProperty(DataKeys.POKEMON_ABILITY_NAME, name)
        return json
    }

    open fun loadFromNBT(nbt: NbtCompound) =  this
    open fun loadFromJSON(json: JsonObject) = this
}