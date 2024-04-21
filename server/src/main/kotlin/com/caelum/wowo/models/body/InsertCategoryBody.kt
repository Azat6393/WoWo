package com.caelum.wowo.models.body

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class InsertCategoryBody(
    @SerialName("category") val category: String,
    @SerialName("language") val language: String
)
