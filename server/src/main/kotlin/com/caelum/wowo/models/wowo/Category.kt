package com.caelum.wowo.models.wowo

import kotlinx.serialization.Serializable

@Serializable
data class Category(
    val uuid: String,
    val name: String,
    val language: String
)
