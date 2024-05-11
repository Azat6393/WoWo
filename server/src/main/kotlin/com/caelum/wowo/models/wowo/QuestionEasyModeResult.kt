package com.caelum.wowo.models.wowo

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class QuestionEasyModeResult(
    @SerialName("answer") val answer: String,
)
