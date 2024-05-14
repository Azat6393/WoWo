package com.caelum.wowo.models

import com.caelum.wowo.data.dto.CategoryDto
import com.caelum.wowo.data.dto.StatisticsDto
import com.caelum.wowo.data.dto.UserDto
import com.caelum.wowo.data.dto.UserStatisticsDto
import com.caelum.wowo.data.dto.WordDto
import com.caelum.wowo.models.wowo.Category
import com.caelum.wowo.models.wowo.Statistics
import com.caelum.wowo.models.wowo.User
import com.caelum.wowo.models.wowo.UserStatistics
import com.caelum.wowo.models.wowo.Word
import com.caelum.wowo.utils.getGameCondition

fun UserDto.toUser(): User {
    return User(
        uuid = uuid,
        nickname = nickname,
        total_score = totalScore,
        email = email
    )
}

fun CategoryDto.toCategory(): Category {
    return Category(
        uuid = uuid,
        name = name,
        language = language
    )
}

fun WordDto.toWord(difficulty: Int): Word {
    return Word(
        uuid = uuid,
        category = category,
        word = word,
        language = language,
        gameCondition = getGameCondition(difficulty)
    )
}

fun UserStatisticsDto.toUserStatistics(): UserStatistics {
    return UserStatistics(
        userId = user_id,
        easy = easy.toStatistics(),
        medium = medium.toStatistics(),
        hard = hard.toStatistics()
    )
}

fun StatisticsDto.toStatistics(): Statistics {
    return Statistics(
        played = played,
        wins = wins,
        loses = loses,
        maxStreak = maxStreak,
        currentStreak = currentStreak,
        totalAttempt = totalAttempt,
        totalQuestion = totalQuestion
    )
}