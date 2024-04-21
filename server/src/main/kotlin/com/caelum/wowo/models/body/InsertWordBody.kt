package com.caelum.wowo.models.body

import kotlinx.serialization.Serializable

@Serializable
data class InsertWordBody(
    val word: String,
    val category: String,
    val language: String
)
