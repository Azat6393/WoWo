package data.repository

import data.remote.WoWoApi
import domain.model.User
import domain.model.UserStatistics
import domain.repository.UserRepository
import io.ktor.client.call.NoTransformationFoundException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class UserRepositoryImpl(private val woWoApi: WoWoApi) : UserRepository {

    override fun getUser(userId: String): Flow<Result<User>> = flow {
        try {
            val response = woWoApi.getUser(userId)
            response.data?.let {
                emit(Result.success(it))
            }
        } catch (e: NoTransformationFoundException) {
            emit(Result.failure(Exception("Something went wrong")))
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }

    override fun getUserStatistics(userId: String): Flow<Result<UserStatistics>> = flow {
        try {
            val response = woWoApi.getUserStatistics(userId)
            response.data?.let {
                emit(Result.success(it))
            }
        } catch (e: NoTransformationFoundException) {
            emit(Result.failure(Exception("Something went wrong")))
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }

    override fun updateUser(user: User): Flow<Result<User>> = flow {
        try {
            val response = woWoApi.updateUser(user)
            response.data?.let {
                emit(Result.success(it))
            }
        } catch (e: NoTransformationFoundException) {
            emit(Result.failure(Exception("Something went wrong")))
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }
}