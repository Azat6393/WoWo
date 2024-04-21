package com.caelum.wowo.data.gpt

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.headers
import io.ktor.client.request.post
import io.ktor.client.request.setBody

class ChatGptApi(private val client: HttpClient) {

    suspend fun sendMessage(requestBody: ChatRequestBody): GptResponse {
        val authorization = System.getenv(GptConstants.OPENAI_AUTHORIZATION)
        val openAIOrganization = System.getenv(GptConstants.OPENAI_ORGANIZATION)
        val penAIProject = System.getenv(GptConstants.OPENAI_PROJECT)

        val httpResponse = client.post(GptConstants.BASE_URL) {
            headers {
                append("Authorization", authorization)
                append("OpenAI-Organization", openAIOrganization)
                append("OpenAI-Project", penAIProject)
            }
            setBody(requestBody)
        }
        return httpResponse.body<GptResponse>()
    }
}