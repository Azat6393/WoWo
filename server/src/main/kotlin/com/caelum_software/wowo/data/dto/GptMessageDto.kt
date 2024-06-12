package com.caelum_software.wowo.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GptMessageDto(
    @SerialName("role")
    val role: String,
    @SerialName("content")
    val content: String
)
