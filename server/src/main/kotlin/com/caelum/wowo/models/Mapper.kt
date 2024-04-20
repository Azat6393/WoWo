package com.caelum.wowo.models

import com.caelum.wowo.models.wowo.Category
import com.caelum.wowo.models.wowo.User
import com.caelum.wowo.models.wowo.Word
import com.caelum.wowo.mongodb.dto.CategoryDto
import com.caelum.wowo.mongodb.dto.UserDto
import com.caelum.wowo.mongodb.dto.WordDto

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

fun WordDto.toWord(): Word {
    return Word(
        uuid = uuid,
        category = category,
        word = word,
        language = language,
        createdData = createdData.toString()
    )
}