package com.caelum_software.wowo.models.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ErrorResponse(
    @SerialName("message") var message: String,
    @SerialName("code") var code: Int,
)