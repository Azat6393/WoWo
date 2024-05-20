package com.caelum.wowo.repository

import com.caelum.wowo.data.dto.UserDto
import com.caelum.wowo.data.dto.UserStatisticsDto
import com.caelum.wowo.data.dto.WordDto
import com.caelum.wowo.data.mongodb.MongoDbConstants
import com.caelum.wowo.models.wowo.User
import com.caelum.wowo.utils.exception.UnknownException
import com.mongodb.client.model.Filters
import com.mongodb.client.model.Filters.eq
import com.mongodb.client.model.UpdateOptions
import com.mongodb.client.model.Updates
import com.mongodb.kotlin.client.coroutine.MongoDatabase
import kotlinx.coroutines.flow.firstOrNull
import org.bson.types.ObjectId
import java.time.LocalDateTime
import java.util.UUID

class UserRepository(private val database: MongoDatabase) {

    suspend fun getUser(userId: String): Result<UserDto> {
        return try {
            val collection =
                database.getCollection<WordDto>(collectionName = MongoDbConstants.COLLECTION_USERS)

            val queryParam = Filters
                .and(listOf(eq(UserDto::uuid.name, userId)))

            val resultFlow = collection.find<UserDto>(queryParam).limit(1)
                .firstOrNull()

            if (resultFlow == null) {
                val result = createUser(userId)
                if (result.isSuccess) Result.success(result.getOrNull()!!)
                else Result.failure(result.exceptionOrNull()!!)
            } else {
                Result.success(resultFlow)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun updateUser(user: User): Result<User> {
        return try {
            val collection =
                database.getCollection<WordDto>(collectionName = MongoDbConstants.COLLECTION_USERS)

            val queryParam = Filters
                .and(listOf(eq(User::uuid.name, user.uuid)))

            val updates = Updates.combine(
                Updates.set(User::nickname.name, user.nickname),
                Updates.set(User::email.name, user.email),
                Updates.set(User::total_score.name, user.total_score)
            )
            val options = UpdateOptions().upsert(false)

            collection.updateMany(queryParam, updates, options)
            Result.success(user)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    private suspend fun createUser(userId: String): Result<UserDto?> {
        return try {
            val collection =
                database.getCollection<UserDto>(collectionName = MongoDbConstants.COLLECTION_USERS)
            val item = UserDto(
                id = ObjectId(),
                uuid = userId,
                nickname = UUID.randomUUID().toString(),
                totalScore = 0,
                email = null,
                createdDate = LocalDateTime.now()
            )
            val result = collection.insertOne(item)

            if (result.insertedId == null) Result.failure(UnknownException("Cannot create user!"))
            else Result.success(item)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun increaseScore(score: Int, userId: String) {
        try {
            val collection =
                database.getCollection<UserDto>(collectionName = MongoDbConstants.COLLECTION_USERS)

            val queryParams = Filters.and(
                listOf(eq(UserDto::uuid.name, userId))
            )
            val updateParams = Updates.inc("total_score", score)
            collection.updateOne(filter = queryParams, update = updateParams)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    suspend fun decreaseScore(score: Int, userId: String) {
        try {
            val collection =
                database.getCollection<UserDto>(collectionName = MongoDbConstants.COLLECTION_USERS)

            val queryParams = Filters.and(
                listOf(eq(UserDto::uuid.name, userId))
            )
            val updateParams = Updates.inc("total_score", -score)
            collection.updateOne(filter = queryParams, update = updateParams)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}