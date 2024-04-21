package com.caelum.wowo.models.body

import kotlinx.serialization.Serializable

@Serializable
data class QuestionBody(
    val word: String,
    val question: String
)
