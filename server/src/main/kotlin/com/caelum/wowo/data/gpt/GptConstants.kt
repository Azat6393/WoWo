package com.caelum.wowo.data.gpt


object GptConstants {
    const val OPENAI_AUTHORIZATION = "OpenAI_Authorization"
    const val OPENAI_ORGANIZATION = "OpenAI_Organization"
    const val OPENAI_PROJECT = "OpenAI_Project"

    const val GPT_MODEL = "gpt-4-turbo-2024-04-09"
    const val GPT_TEMPERATURE = 0.2f
    const val GPT_MAX_TOKEN = 400
    const val BASE_URL = "https://api.openai.com/v1/chat/completions"

    const val SYSTEM = "system"
    const val USER = "user"
}