package data.remote

import data.remote.body.InputWordBody
import data.remote.body.QuestionBody
import data.remote.body.ResultGameBody
import data.remote.response.ErrorResponse
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
import io.ktor.http.HttpStatusCode

class WoWoApi(private val client: HttpClient) {
    companion object {
        private const val BASE_URL = "https://wowo-421813.ew.r.appspot.com"
        private const val GAME = "${BASE_URL}/api/v1/game/"
        private const val USERS = "${BASE_URL}/api/v1/user/"
    }

    suspend fun getUser(userId: String): SuccessResponse<User> {
        val response = client.get("$USERS$userId")
        return if (response.status.value == HttpStatusCode.OK.value) {
            response.body()
        } else {
            val error = response.body<ErrorResponse>()
            throw Exception(error.message)
        }
    }

    suspend fun updateUser(user: User): SuccessResponse<User> {
        val response = client.post(USERS) {
            setBody(user)
        }
        return if (response.status.value == HttpStatusCode.OK.value) {
            response.body()
        } else {
            val error = response.body<ErrorResponse>()
            throw Exception(error.message)
        }
    }

    suspend fun getUserStatistics(userId: String): SuccessResponse<UserStatistics> {
        val response = client.get("${USERS}statistics/$userId")
        return if (response.status.value == HttpStatusCode.OK.value) {
            response.body()
        } else {
            val error = response.body<ErrorResponse>()
            throw Exception(error.message)
        }
    }

    suspend fun gameResult(resultGameBody: ResultGameBody): SuccessResponse<Boolean> {
        val response = client.post("${GAME}result") {
            setBody(resultGameBody)
        }
        return if (response.status.value == HttpStatusCode.OK.value) {
            response.body()
        } else {
            val error = response.body<ErrorResponse>()
            throw Exception(error.message)
        }
    }

    suspend fun getWord(
        category: String,
        language: String,
        difficulty: String,
    ): SuccessResponse<Word> {
        val response = client.get("${GAME}word/$category/$language/$difficulty")
        return if (response.status.value == HttpStatusCode.OK.value) {
            response.body()
        } else {
            val error = response.body<ErrorResponse>()
            throw Exception(error.message)
        }
    }

    suspend fun inputWord(inputWordBody: InputWordBody): SuccessResponse<InputResult> {
        val response = client.post("${GAME}input") {
            setBody(inputWordBody)
        }
        return if (response.status.value == HttpStatusCode.OK.value) {
            response.body()
        } else {
            val error = response.body<ErrorResponse>()
            throw Exception(error.message)
        }
    }

    suspend fun sendQuestion(questionBody: QuestionBody): SuccessResponse<QuestionResult> {
        val response = client.post("${GAME}question") {
            setBody(questionBody)
        }
        return if (response.status.value == HttpStatusCode.OK.value) {
            response.body()
        } else {
            val error = response.body<ErrorResponse>()
            throw Exception(error.message)
        }
    }

    suspend fun sendQuestionForEasyMode(questionBody: QuestionBody): SuccessResponse<QuestionEasyModeResult> {
        val response = client.post("${GAME}question-easy") { setBody(questionBody) }
        return if (response.status.value == HttpStatusCode.OK.value) {
            response.body()
        } else {
            val error = response.body<ErrorResponse>()
            throw Exception(error.message)
        }
    }

    suspend fun getCategories(language: String): SuccessResponse<List<Category>> {
        val response = client.get("${GAME}categories/$language")
        return if (response.status.value == HttpStatusCode.OK.value) {
            response.body()
        } else {
            val error = response.body<ErrorResponse>()
            throw Exception(error.message)
        }
    }
}