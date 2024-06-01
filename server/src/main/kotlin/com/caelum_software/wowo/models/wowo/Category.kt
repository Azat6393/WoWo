package com.caelum_software.wowo.models.wowo

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Category(
    @SerialName("uuid") val uuid: String,
    @SerialName("name") val name: String,
    @SerialName("language") val language: String
)
