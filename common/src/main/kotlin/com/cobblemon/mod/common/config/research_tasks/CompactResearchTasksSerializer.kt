/*
 * Copyright (C) 2023 Cobblemon Contributors
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package com.cobblemon.mod.common.config.research_tasks

import com.google.gson.Gson
import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter

class CompactResearchTasksSerializer: TypeAdapter<ResearchTaskConfig>() {
    companion object {
        val compactGson = Gson()
    }

    override fun write(
        out: JsonWriter?,
        value: ResearchTaskConfig?
    ) {
        // serialize Detail to a single-line JSON string…
        val singleLine = compactGson.toJson(value)

        // …and emit it as “raw” JSON into the main stream
        out?.jsonValue(singleLine)
    }

    override fun read(`in`: JsonReader?): ResearchTaskConfig? {
        // fallback to default deserialization
        return compactGson.fromJson(`in`, ResearchTaskConfig::class.java)
    }
}
