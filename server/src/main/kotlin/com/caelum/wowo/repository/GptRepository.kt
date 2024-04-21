package com.caelum.wowo.repository

import com.caelum.wowo.data.dto.GptMessageDto
import com.caelum.wowo.data.gpt.ChatGptApi
import com.caelum.wowo.data.gpt.ChatRequestBody
import com.caelum.wowo.data.gpt.GptConstants
import com.caelum.wowo.utils.exception.UnknownException

class GptRepository(private val chatGptApi: ChatGptApi) {

    suspend fun sendMessage(
        word: String,
        question: String,
        language: String,
    ): Result<GptMessageDto> {
        return try {
            val requestBody = ChatRequestBody(
                model = GptConstants.GPT_MODEL,
                temperature = GptConstants.GPT_TEMPERATURE,
                maxTokens = GptConstants.GPT_MAX_TOKEN,
                messages = listOf(
                    GptMessageDto(
                        role = GptConstants.SYSTEM,
                        content = getSystemMessage(word, language)
                    ),
                    GptMessageDto(
                        role = GptConstants.USER,
                        content = question
                    )
                )
            )
            val result = chatGptApi.sendMessage(requestBody)
            val message = result.choices.getOrNull(0)?.message

            if (message == null) Result.failure(UnknownException("Something went wrong!"))
            else Result.success(message)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }


    private fun getSystemMessage(word: String, language: String): String {
        return " You are a game assistant.\n" +
                "- Gameplay: \"You are trying to find a secret word. While trying to find this word, you collect clues with questions that you can answer 'yes' or 'no' to.\" \n" +
                "- Your role: \"Answer only 'yes' or 'no' to the question depending on the word, and answer 'invalid' to questions where the answer is neither 'yes' nor 'no'.\"\n" +
                "- Secret Word: \"$word\"\n" +
                "- Language of the question: \"$language\"\n" +
                "- Language of the answer: \"eng \""
    }
}