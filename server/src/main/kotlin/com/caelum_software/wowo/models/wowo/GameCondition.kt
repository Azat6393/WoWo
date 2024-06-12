package com.caelum_software.wowo.models.wowo

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GameCondition(
    @SerialName("question") val question: Int,
    @SerialName("seconds") val seconds: Int,
    @SerialName("attempts") val attempts: Int
)
