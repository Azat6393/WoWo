package com.caelum.wowo.models.wowo

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserStatistics(
    @SerialName("user_id")
    val userId: String,
    @SerialName("easy")
    val easy: Statistics = Statistics(),
    @SerialName("medium")
    val medium: Statistics = Statistics(),
    @SerialName("hard")
    val hard: Statistics = Statistics()
)

@Serializable
data class Statistics(
    @SerialName("played")
    val played: Int = 0,
    @SerialName("wins")
    val wins: Int = 0,
    @SerialName("loses")
    val loses: Int = 0,
    @SerialName("max_streak")
    val maxStreak: Int = 0,
    @SerialName("current_streak")
    val currentStreak: Int = 0,
    @SerialName("totalQuestion")
    val totalQuestion: Int = 0,
    @SerialName("totalAttempt")
    val totalAttempt: Int = 0
)
