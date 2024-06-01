package com.caelum_software.wowo.service

import com.caelum_software.wowo.data.mongodb.IsSuccess
import com.caelum_software.wowo.repository.CategoryRepository
import com.caelum_software.wowo.repository.WordRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class AdminService(
    private val wordRepository: WordRepository,
    private val categoryRepository: CategoryRepository,
) {
    suspend fun insertWord(words: List<String>, language: String, category: String): IsSuccess {
        return wordRepository.addWord(words, category, language)
    }

    fun insertCategory(category: String, language: String): Flow<IsSuccess> = flow {
        val result = categoryRepository.addCategory(category, language)
        result.fold(
            onSuccess = { isSuccess ->
                emit(isSuccess)
            },
            onFailure = { exception: Throwable ->
                throw exception
            }
        )
    }
}