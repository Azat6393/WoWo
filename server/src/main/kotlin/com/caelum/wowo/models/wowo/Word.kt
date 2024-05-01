package com.caelum.wowo.models.wowo

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Word(
    @SerialName("uuid") val uuid: String,
    @SerialName("word") val word: String,
    @SerialName("category") val category: String,
    @SerialName("language") val language: String,
    @Serializable
    @SerialName("game_condition") val gameCondition: GameCondition,
)
