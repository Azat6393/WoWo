package com.caelum.wowo.models.wowo

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class User(
    val uuid: String,
    val nickname: String,
    @SerialName("total_score") val totalScore: Int,
    val email: String? = null,
)
