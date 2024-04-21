package com.caelum.wowo.models.body

import kotlinx.serialization.Serializable

@Serializable
data class InsertCategoryBody(
    val category: String,
    val language: String
)
