package com.caelum.wowo.service

import com.caelum.wowo.models.toUser
import com.caelum.wowo.models.wowo.User
import com.caelum.wowo.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class UserService(
    private val userRepository: UserRepository,
) {

    fun getUser(userId: String): Flow<User> = flow {
        val result = userRepository.getUser(userId)
        result.fold(
            onSuccess = { userDto ->
                emit(userDto.toUser())
            },
            onFailure = { exception: Throwable ->
                throw exception
            }
        )
    }
}