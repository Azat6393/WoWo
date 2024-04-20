package com.caelum.wowo.models.response

data class ErrorResponse(
    var message: String,
    var code: Int
)