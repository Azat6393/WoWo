package com.caelum.wowo.service

import com.caelum.wowo.models.toUser
import com.caelum.wowo.models.toUserStatistics
import com.caelum.wowo.models.wowo.User
import com.caelum.wowo.models.wowo.UserStatistics
import com.caelum.wowo.repository.StatisticsRepository
import com.caelum.wowo.repository.UserRepository
import com.caelum.wowo.utils.GUEST_USER
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class UserService(
    private val userRepository: UserRepository,
    private val statisticsRepository: StatisticsRepository,
) {

    fun getUserStatistics(userId: String): Flow<UserStatistics> = flow {
        val result = statisticsRepository.getUserStatistics(userId)
        result.fold(
            onSuccess = { statisitics ->
                emit(statisitics.toUserStatistics())
            },
            onFailure = { exception: Throwable ->
                throw exception
            }
        )
    }

    fun getUser(userId: String): Flow<User> = flow {
        val result = userRepository.getUser(userId)
        result.fold(
            onSuccess = { userDto ->
                if (userId == GUEST_USER) {
                    emit(
                        User(
                            uuid = userId,
                            nickname = "Guest",
                            total_score = 0,
                            email = null
                        )
                    )
                } else emit(userDto.toUser())
            },
            onFailure = { exception: Throwable ->
                throw exception
            }
        )
    }

    fun updateUser(user: User): Flow<User> = flow {
        val result = userRepository.updateUser(user)
        result.fold(
            onSuccess = { user ->
                emit(user)
            },
            onFailure = { exception: Throwable ->
                throw exception
            }
        )
    }
}