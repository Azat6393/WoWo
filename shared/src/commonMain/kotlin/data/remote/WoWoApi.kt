package data.remote

import data.remote.body.InputWordBody
import data.remote.body.QuestionBody
import domain.model.InputResult
import data.remote.response.SuccessResponse
import domain.model.Category
import domain.model.QuestionResult
import domain.model.User
import domain.model.Word
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody

class WoWoApi(private val client: HttpClient) {
    companion object {
        private const val BASE_URL = "https://wowo-421813.ew.r.appspot.com"
        private const val GAME = "${BASE_URL}/api/v1/game/"
        private const val USERS = "${BASE_URL}/api/v1/user/"
    }

    suspend fun getUser(userId: String): SuccessResponse<User> {
        return client.get("$USERS$userId").body()
    }

    suspend fun getWord(
        category: String,
        language: String,
        difficulty: String,
    ): SuccessResponse<Word> {
        return client.get("$GAME$category/$language/$difficulty").body()
    }

    suspend fun inputWord(inputWordBody: InputWordBody): SuccessResponse<InputResult> {
        return client.post("${GAME}input") {
            setBody(inputWordBody)
        }.body()
    }

    suspend fun sendQuestion(questionBody: QuestionBody): SuccessResponse<QuestionResult> {
        return client.post("${GAME}question") {
            setBody(questionBody)
        }.body()
    }

    suspend fun getCategories(language: String): SuccessResponse<List<Category>> {
        return client.get("${GAME}categories/$language").body()
    }
}