/*
 * Copyright (C) 2023 Cobblemon Contributors
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package com.cobblemon.mod.common.config.research_tasks

import com.cobblemon.mod.common.config.Category
import com.cobblemon.mod.common.config.NodeCategory
import com.google.gson.GsonBuilder

class ResearchTasksConfig {
    companion object {
        val GSON = GsonBuilder()
            .disableHtmlEscaping()
            .setPrettyPrinting()
            .registerTypeAdapter(ResearchTaskConfig::class.java, CompactResearchTasksSerializer())
            .create()
    }

    @NodeCategory(Category.Pokedex)
    var taskExamplesForReference = listOf(
        ResearchTaskConfig("catch"),
        ResearchTaskConfig("catch", amountNeeded=20),
        ResearchTaskConfig("evolve"),
        ResearchTaskConfig("evolve", amountNeeded=20),
        ResearchTaskConfig("defeat"),
        ResearchTaskConfig("defeat", amountNeeded=20),
        ResearchTaskConfig("fish"),
        ResearchTaskConfig("fish", amountNeeded=20),
        ResearchTaskConfig("shear"),
        ResearchTaskConfig("shear", amountNeeded=20),
        ResearchTaskConfig("hatch"),
        ResearchTaskConfig("hatch", amountNeeded=20),
        ResearchTaskConfig("milk"),
        ResearchTaskConfig("milk", amountNeeded=20),
        ResearchTaskConfig("evolve_into", "pikachu"),
        ResearchTaskConfig("evolve_into", "pikachu", 20),
        ResearchTaskConfig("use_move", "doubleteam"),
        ResearchTaskConfig("use_move", "doubleteam", 20),
        ResearchTaskConfig("catch_with", "cobblemon:master_ball"),
        ResearchTaskConfig("catch_with", "cobblemon:master_ball", 20),
        ResearchTaskConfig("catch_form", "galar"),
        ResearchTaskConfig("catch_form", "galar", 20),
        ResearchTaskConfig("catch_status", "sleep"),
        ResearchTaskConfig("catch_status", "sleep", 20),
        ResearchTaskConfig("catch_status", "burn"),
        ResearchTaskConfig("catch_status", "frozen"),
        ResearchTaskConfig("catch_status", "paralysis"),
        ResearchTaskConfig("catch_status", "poisonbadly"),
        ResearchTaskConfig("catch_status", "poison"),
        ResearchTaskConfig("catch_time", "night"),
        ResearchTaskConfig("catch_time", "night", 20),
        ResearchTaskConfig("catch_time", "day"),
        ResearchTaskConfig("catch_time", "noon"),
        ResearchTaskConfig("catch_time", "midnight"),
        ResearchTaskConfig("catch_time", "dawn"),
        ResearchTaskConfig("catch_time", "dusk"),
        ResearchTaskConfig("catch_time", "twilight"),
        ResearchTaskConfig("catch_time", "morning"),
        ResearchTaskConfig("catch_time", "afternoon"),
        ResearchTaskConfig("catch_time", "predawn"),
        ResearchTaskConfig("catch_time", "evening"),
        ResearchTaskConfig("catch_gender", "male"),
        ResearchTaskConfig("catch_gender", "female", 20),
    )

    @NodeCategory(Category.Pokedex)
    var tasks = mapOf(
        "bulbasaur" to listOf(
            ResearchTaskConfig("catch", amountNeeded=20),
            ResearchTaskConfig("evolve"),
            ResearchTaskConfig("defeat", amountNeeded=10),
            ResearchTaskConfig("fish"),
            ResearchTaskConfig("shear", amountNeeded=5),
            ResearchTaskConfig("hatch"),
            ResearchTaskConfig("evolve_into", "ivysaur"),
            ResearchTaskConfig("use_move", "tackle", 15),
            ResearchTaskConfig("use_move", "growl", 10)),
        "ivysaur" to listOf(
            ResearchTaskConfig("catch", amountNeeded=20),
            ResearchTaskConfig("evolve"),
            ResearchTaskConfig("defeat", amountNeeded=10),
            ResearchTaskConfig("fish"),
            ResearchTaskConfig("shear", amountNeeded=5),
            ResearchTaskConfig("hatch"),
            ResearchTaskConfig("evolve_into", "venusaur"),
            ResearchTaskConfig("use_move", "tackle", 15),
            ResearchTaskConfig("use_move", "growl", 10)),
    )
}