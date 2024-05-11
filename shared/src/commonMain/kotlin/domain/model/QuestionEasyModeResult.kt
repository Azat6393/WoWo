package domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class QuestionEasyModeResult(
    @SerialName("answer") val answer: String,
)
