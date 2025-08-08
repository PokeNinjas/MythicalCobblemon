package com.cobblemon.mod.common.config.research_tasks

data class ResearchTaskConfig(val task: String, val target: String? = null, private val amountNeeded: Int? = null) {
    val amount
        get() = amountNeeded ?: 1
}
