package domain.repository

import domain.model.User
import domain.model.UserStatistics
import kotlinx.coroutines.flow.Flow

interface UserRepository {

    fun getUserStatistics(userId: String): Flow<Result<UserStatistics>>

    fun getUser(userId: String): Flow<Result<User>>

    fun updateUser(user: User): Flow<Result<User>>
}