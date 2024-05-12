package com.caelum.wowo.models.body

import com.caelum.wowo.models.wowo.GameCondition
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ResultGameBody(
    @SerialName("win") val win: Boolean,
    @SerialName("actual_word") val actualWord: String,
    @SerialName("user_id") val userId: String,
    @SerialName("difficulty_level") val difficultyLevel: Int,
    @SerialName("game_condition") val gameCondition: GameCondition
)
