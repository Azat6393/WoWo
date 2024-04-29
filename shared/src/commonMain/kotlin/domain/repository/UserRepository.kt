package domain.repository

import domain.model.User
import kotlinx.coroutines.flow.Flow

interface UserRepository {

    suspend fun getUser(userId: String): Flow<Result<User>>
}