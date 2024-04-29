package data.repository

import data.remote.WoWoApi
import domain.model.User
import domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class UserRepositoryImpl(private val woWoApi: WoWoApi) : UserRepository {

    override suspend fun getUser(userId: String): Flow<Result<User>> = flow {
        try {
            val response = woWoApi.getUser(userId)
            emit(Result.success(response.data))
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }
}