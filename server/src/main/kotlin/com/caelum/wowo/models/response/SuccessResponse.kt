package com.caelum.wowo.models.response

import kotlinx.serialization.Serializable

@Serializable
data class SuccessResponse<T>(
    var data: T,
    var code: Int,
    var message: String
)