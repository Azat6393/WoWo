package com.caelum.wowo.models

import com.caelum.wowo.data.dto.CategoryDto
import com.caelum.wowo.data.dto.UserDto
import com.caelum.wowo.data.dto.WordDto
import com.caelum.wowo.models.wowo.Category
import com.caelum.wowo.models.wowo.User
import com.caelum.wowo.models.wowo.Word
import com.caelum.wowo.utils.getGameCondition

fun UserDto.toUser(): User {
    return User(
        uuid = uuid,
        nickname = nickname,
        totalScore = totalScore,
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