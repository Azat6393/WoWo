package com.caelum.wowo.utils

import com.caelum.wowo.models.response.InputResponse

fun calculateScore(seconds: Int, attempts: Int, questions: Int): Int {
    return 100
}

fun changeLetterCondition(enteredWord: String, actualWord: String): Map<Int, Int> {
    val inputResult = mutableMapOf<Int, Int>()
    enteredWord.forEachIndexed { index, c ->
        inputResult[index] = when {
            enteredWord[index] == actualWord[index] -> 2
            actualWord.contains(c) -> 1
            else -> 0
        }
    }
    return inputResult
}