package com.caelum.wowo.models.wowo

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class User(
    @SerialName("uuid") val uuid: String,
    @SerialName("nickname") val nickname: String,
    @SerialName("total_score") val totalScore: Int,
    @SerialName("email") val email: String? = null,
)
