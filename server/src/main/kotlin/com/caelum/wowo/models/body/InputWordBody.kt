package com.caelum.wowo.models.body

data class InputWordBody(
    val actualWord: String,
    val enteredWord: String,
    val userId: String,
    val difficultyLevel: Int,
    val question: Int,
    val seconds: Int,
    val attempts: Int
)
