package com.caelum.wowo.models.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

// 1 -> "yes"
// 2 -> "no"
// 3 -> "invalid"

@Serializable
data class QuestionResponse(
    @SerialName("answer") val answer: Int,
)
