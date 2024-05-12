package data.repository

import data.remote.WoWoApi
import domain.model.User
import domain.model.UserStatistics
import domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class UserRepositoryImpl(private val woWoApi: WoWoApi) : UserRepository {

    override fun getUser(userId: String): Flow<Result<User>> = flow {
        try {
            val response = woWoApi.getUser(userId)
            emit(Result.success(response.data))
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }

    override fun getUserStatistics(userId: String): Flow<Result<UserStatistics>> = flow {
        try {
            val response = woWoApi.getUserStatistics(userId)
            emit(Result.success(response.data))
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }
}