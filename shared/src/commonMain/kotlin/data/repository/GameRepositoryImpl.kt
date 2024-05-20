package data.repository

import data.remote.WoWoApi
import data.remote.body.InputWordBody
import data.remote.body.QuestionBody
import data.remote.body.ResultGameBody
import domain.model.Category
import domain.model.InputResult
import domain.model.QuestionEasyModeResult
import domain.model.QuestionResult
import domain.model.Word
import domain.repository.GameRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GameRepositoryImpl(private val woWoApi: WoWoApi) : GameRepository {

    override fun getWord(
        category: String,
        language: String,
        difficulty: Int,
    ): Flow<Result<Word>> = flow {
        try {
            val response = woWoApi.getWord(category, language, difficulty.toString())
            response.data?.let { emit(Result.success(it)) }
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }

    override fun getCategories(language: String): Flow<Result<List<Category>>> = flow {
        try {
            val response = woWoApi.getCategories(language)
            response.data?.let { emit(Result.success(it)) }
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }

    override fun inputWord(inputWordBody: InputWordBody): Flow<Result<InputResult>> = flow {
        try {
            val response = woWoApi.inputWord(inputWordBody)
            response.data?.let { emit(Result.success(it)) }
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }

    override suspend fun gameResult(resultGameBody: ResultGameBody) {
        try {
            woWoApi.gameResult(resultGameBody)
        } catch (e: Exception) {
            println("error: ${e.message}")
        }
    }

    override fun askQuestion(questionBody: QuestionBody): Flow<Result<QuestionResult>> =
        flow {
            try {
                val response = woWoApi.sendQuestion(questionBody)
                response.data?.let { emit(Result.success(it)) }
            } catch (e: Exception) {
                emit(Result.failure(e))
            }
        }

    override fun askQuestionForEasyMode(questionBody: QuestionBody): Flow<Result<QuestionEasyModeResult>> =
        flow {
            try {
                val response = woWoApi.sendQuestionForEasyMode(questionBody)
                response.data?.let { emit(Result.success(it)) }
            } catch (e: Exception) {
                emit(Result.failure(e))
            }
        }
}