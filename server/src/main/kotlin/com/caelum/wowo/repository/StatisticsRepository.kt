package com.caelum.wowo.repository

import com.caelum.wowo.data.dto.StatisticsDto
import com.caelum.wowo.data.dto.UserStatisticsDto
import com.caelum.wowo.data.mongodb.MongoDbConstants
import com.caelum.wowo.models.wowo.GameCondition
import com.caelum.wowo.utils.exception.UnknownException
import com.mongodb.client.model.Filters
import com.mongodb.client.model.Filters.eq
import com.mongodb.client.model.UpdateOptions
import com.mongodb.client.model.Updates
import com.mongodb.kotlin.client.coroutine.MongoDatabase
import kotlinx.coroutines.flow.firstOrNull
import org.bson.types.ObjectId

class StatisticsRepository(private val database: MongoDatabase) {

    suspend fun updateUserStatistics(
        userId: String,
        isWin: Boolean,
        gameCondition: GameCondition,
        difficulty: Int,
    ) {
        try {
            val result = getUserStatistics(userId)
            result.fold(
                onSuccess = { userStatistics ->
                    val newStatistics =
                        newUserStatistics(difficulty, userStatistics, isWin, gameCondition)

                    val collection =
                        database.getCollection<UserStatisticsDto>(collectionName = MongoDbConstants.COLLECTION_STATISTICS)

                    val queryParam = Filters
                        .and(listOf(eq(UserStatisticsDto::user_id.name, userId)))

                    val updates = Updates.combine(
                        Updates.set(UserStatisticsDto::easy.name, newStatistics.easy),
                        Updates.set(UserStatisticsDto::medium.name, newStatistics.medium),
                        Updates.set(UserStatisticsDto::hard.name, newStatistics.hard)
                    )
                    val options = UpdateOptions().upsert(false)

                    collection.updateMany(queryParam, updates, options)
                },
                onFailure = { throw it }
            )
        } catch (e: Exception) {
            System.err.println("Unable to update due to an error: ${e.message}")
        }
    }

    suspend fun getUserStatistics(userId: String): Result<UserStatisticsDto> {
        return try {
            val collection =
                database.getCollection<UserStatisticsDto>(collectionName = MongoDbConstants.COLLECTION_STATISTICS)

            val queryParam = Filters
                .and(listOf(eq(UserStatisticsDto::user_id.name, userId)))

            val resultFlow = collection.find<UserStatisticsDto>(queryParam).limit(1)
                .firstOrNull()

            if (resultFlow == null) {
                val result = createUserStatistics(userId)
                if (result.isSuccess) Result.success(result.getOrNull()!!)
                else Result.failure(result.exceptionOrNull()!!)
            } else {
                Result.success(resultFlow)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    private suspend fun createUserStatistics(userId: String): Result<UserStatisticsDto> {
        return try {
            val collection =
                database.getCollection<UserStatisticsDto>(collectionName = MongoDbConstants.COLLECTION_STATISTICS)
            val item = UserStatisticsDto(
                id = ObjectId(),
                user_id = userId
            )
            val result = collection.insertOne(item)

            if (result.insertedId == null) Result.failure(UnknownException("Cannot create user statistics!"))
            else Result.success(item)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    private fun newUserStatistics(
        difficulty: Int,
        userStatistics: UserStatisticsDto,
        isWin: Boolean,
        gameCondition: GameCondition,
    ) = when (difficulty) {
        1 -> userStatistics.copy(
            easy = updateStatistics(
                currentStatistics = userStatistics.easy,
                isWin = isWin,
                gameCondition = gameCondition
            )
        )

        2 -> userStatistics.copy(
            medium = updateStatistics(
                currentStatistics = userStatistics.medium,
                isWin = isWin,
                gameCondition = gameCondition
            )
        )

        else -> userStatistics.copy(
            hard = updateStatistics(
                currentStatistics = userStatistics.hard,
                isWin = isWin,
                gameCondition = gameCondition
            )
        )
    }

    private fun updateStatistics(
        currentStatistics: StatisticsDto,
        isWin: Boolean,
        gameCondition: GameCondition,
    ): StatisticsDto {
        return StatisticsDto(
            played = currentStatistics.played + 1,
            wins = currentStatistics.wins + if (isWin) 1 else 0,
            loses = currentStatistics.loses + if (!isWin) 1 else 0,
            currentStreak = if (isWin) currentStatistics.currentStreak + 1 else 0,
            totalQuestion = currentStatistics.totalQuestion + gameCondition.question,
            totalAttempt = currentStatistics.totalAttempt + gameCondition.attempts,
            maxStreak = calculateMaxStreak(
                currentSteak = currentStatistics.currentStreak,
                maxStreak = currentStatistics.maxStreak
            )
        )
    }

    private fun calculateMaxStreak(currentSteak: Int, maxStreak: Int): Int {
        return if (currentSteak > maxStreak) currentSteak else maxStreak
    }
}