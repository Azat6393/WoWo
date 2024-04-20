package com.caelum.wowo.models.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

// lettersCondition -> Key is index of letter, Value is condition of letter
// 0 -> letter is not in the word in any spot.
// 1 -> letter is in the word but in the wrong spot.
// 2 -> letter is in the word and in the correct spot.

@Serializable
data class InputResponse(
    @SerialName("is_correct") val isCorrect: Boolean,
    @SerialName("letters_condition") val lettersCondition: Map<Int, Int>,
    @SerialName("earned_score") val earnedScore: Int? = null
)