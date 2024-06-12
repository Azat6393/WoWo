package com.caelum_software.wowo.models.body

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class QuestionBody(
    @SerialName("word") val word: String,
    @SerialName("category") val category: String,
    @SerialName("question") val question: String,
    @SerialName("language") val language: String
)
