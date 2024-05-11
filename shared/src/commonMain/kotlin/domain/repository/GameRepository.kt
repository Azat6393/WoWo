package domain.repository

import data.remote.body.InputWordBody
import data.remote.body.QuestionBody
import domain.model.Category
import domain.model.InputResult
import domain.model.QuestionEasyModeResult
import domain.model.QuestionResult
import domain.model.Word
import kotlinx.coroutines.flow.Flow

interface GameRepository {

    fun getWord(category: String, language: String, difficulty: Int): Flow<Result<Word>>

    fun inputWord(inputWordBody: InputWordBody): Flow<Result<InputResult>>

    fun askQuestion(questionBody: QuestionBody): Flow<Result<QuestionResult>>

    fun askQuestionForEasyMode(questionBody: QuestionBody): Flow<Result<QuestionEasyModeResult>>

    fun getCategories(language: String): Flow<Result<List<Category>>>
}