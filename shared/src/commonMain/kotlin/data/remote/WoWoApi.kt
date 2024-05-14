package data.remote

import data.remote.body.InputWordBody
import data.remote.body.QuestionBody
import data.remote.body.ResultGameBody
import data.remote.response.SuccessResponse
import domain.model.Category
import domain.model.InputResult
import domain.model.QuestionEasyModeResult
import domain.model.QuestionResult
import domain.model.User
import domain.model.UserStatistics
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

    suspend fun updateUser(user: User): SuccessResponse<User> {
        return client.post(USERS) {
            setBody(user)
        }.body()
    }

    suspend fun getUserStatistics(userId: String): SuccessResponse<UserStatistics> {
        return client.get("${USERS}statistics/$userId").body()
    }

    suspend fun gameResult(resultGameBody: ResultGameBody): SuccessResponse<Boolean> {
        return client.post("${GAME}result") {
            setBody(resultGameBody)
        }.body()
    }

    suspend fun getWord(
        category: String,
        language: String,
        difficulty: String,
    ): SuccessResponse<Word> {
        return client.get("${GAME}word/$category/$language/$difficulty").body()
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

    suspend fun sendQuestionForEasyMode(questionBody: QuestionBody): SuccessResponse<QuestionEasyModeResult> {
        return client.post("${GAME}question-easy") {
            setBody(questionBody)
        }.body()
    }

    suspend fun getCategories(language: String): SuccessResponse<List<Category>> {
        return client.get("${GAME}categories/$language").body()
    }
}