package com.caelum.wowo.service

import com.caelum.wowo.mongodb.IsSuccess
import com.caelum.wowo.repository.CategoryRepository
import com.caelum.wowo.repository.WordRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class AdminService(
    private val wordRepository: WordRepository,
    private val categoryRepository: CategoryRepository,
) {
    fun insertWord(word: String, language: String, category: String): Flow<IsSuccess> = flow {
        val result = wordRepository.addWord(word, category, language)
        result.fold(
            onSuccess = { isSuccess ->
                emit(isSuccess)
            },
            onFailure = { exception: Throwable ->
                throw exception
            }
        )
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