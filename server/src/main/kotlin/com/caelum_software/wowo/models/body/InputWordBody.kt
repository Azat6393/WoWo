package com.caelum_software.wowo.models.body

import com.caelum_software.wowo.models.wowo.GameCondition
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class InputWordBody(
    @SerialName("actual_word") val actualWord: String,
    @SerialName("entered_word") val enteredWord: String,
    @SerialName("user_id") val userId: String,
    @SerialName("difficulty_level") val difficultyLevel: Int,
    @SerialName("game_condition") val gameCondition: GameCondition
)
