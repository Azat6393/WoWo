package com.caelum.wowo.data.gpt

import com.caelum.wowo.data.dto.GptMessageDto
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ChatRequestBody(
    @SerialName("model")
    val model: String,
    @SerialName("temperature")
    val temperature: Float,
    @SerialName("max_tokens")
    val maxTokens: Int,
    @SerialName("messages")
    val messages: List<GptMessageDto>
)
