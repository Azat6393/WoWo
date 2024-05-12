package com.caelum.wowo.data.dto

import org.bson.codecs.pojo.annotations.BsonId
import org.bson.codecs.pojo.annotations.BsonProperty
import org.bson.types.ObjectId

data class UserStatisticsDto(
    @BsonId
    val id: ObjectId,
    @BsonProperty("user_id")
    val user_id: String,
    @BsonProperty("easy")
    val easy: StatisticsDto = StatisticsDto(),
    @BsonProperty("medium")
    val medium: StatisticsDto = StatisticsDto(),
    @BsonProperty("hard")
    val hard: StatisticsDto = StatisticsDto()
)

data class StatisticsDto(
    @BsonProperty("played")
    val played: Int = 0,
    @BsonProperty("wins")
    val wins: Int = 0,
    @BsonProperty("loses")
    val loses: Int = 0,
    @BsonProperty("max_streak")
    val maxStreak: Int = 0,
    @BsonProperty("current_streak")
    val currentStreak: Int = 0,
    @BsonProperty("totalQuestion")
    val totalQuestion: Int = 0,
    @BsonProperty("totalAttempt")
    val totalAttempt: Int = 0
)
