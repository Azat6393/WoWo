package com.caelum.wowo.models.body

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class InputWordBody(
    @SerialName("actual_word") val actualWord: String,
    @SerialName("entered_word") val enteredWord: String,
    @SerialName("user_id") val userId: String,
    @SerialName("difficulty_level") val difficultyLevel: Int,
    val question: Int,
    val seconds: Int,
    val attempts: Int
)
