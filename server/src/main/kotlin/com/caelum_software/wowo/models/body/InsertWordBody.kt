package com.caelum_software.wowo.models.body

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class InsertWordBody(
    @SerialName("words") val words: List<String>,
    @SerialName("category") val category: String,
    @SerialName("language") val language: String
)
