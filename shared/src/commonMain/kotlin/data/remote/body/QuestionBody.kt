package data.remote.body

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class QuestionBody(
    @SerialName("word") val word: String,
    @SerialName("question") val question: String,
    @SerialName("language") val language: String
)
