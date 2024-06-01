package com.caelum_software.wowo.data.gpt

import com.caelum_software.wowo.data.dto.GptMessageDto
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GptResponse(
    @SerialName("choices") val choices: List<Choice>,
    @SerialName("created") val created: Int,
    @SerialName("id") val id: String,
    @SerialName("model") val model: String,
    @SerialName("object") val gptObject: String,
    @SerialName("usage") val usage: Usage,
    @SerialName("system_fingerprint") val systemFingerprint: String
)

@Serializable
data class Choice(
    @SerialName("finish_reason") val finishReason: String,
    @SerialName("index") val index: Int,
    @SerialName("message") val message: GptMessageDto,
)

@Serializable
data class Usage(
    @SerialName("completion_tokens") val completionTokens: Int,
    @SerialName("prompt_tokens") val promptTokens: Int,
    @SerialName("total_tokens") val totalTokens: Int,
)