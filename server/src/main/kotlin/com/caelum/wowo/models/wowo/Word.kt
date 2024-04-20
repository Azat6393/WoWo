package com.caelum.wowo.models.wowo

import kotlinx.serialization.Serializable

@Serializable
data class Word(
    val uuid: String,
    val word: String,
    val category: String,
    val language: String,
    val createdData: String
)
