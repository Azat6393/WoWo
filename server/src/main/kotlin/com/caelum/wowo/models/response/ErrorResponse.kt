package com.caelum.wowo.models.response

import kotlinx.serialization.Serializable

@Serializable
data class ErrorResponse(
    var message: String,
    var code: Int
)