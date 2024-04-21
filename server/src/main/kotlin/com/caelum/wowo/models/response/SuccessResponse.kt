package com.caelum.wowo.models.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SuccessResponse<T>(
    @SerialName("data") var data: T,
    @SerialName("code") var code: Int,
    @SerialName("message") var message: String
)