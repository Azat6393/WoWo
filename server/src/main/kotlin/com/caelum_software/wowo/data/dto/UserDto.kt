package com.caelum_software.wowo.data.dto

import org.bson.codecs.pojo.annotations.BsonId
import org.bson.codecs.pojo.annotations.BsonProperty
import org.bson.types.ObjectId
import java.time.LocalDateTime

data class UserDto(
    @BsonId
    val id: ObjectId,
    @BsonProperty("uuid")
    val uuid: String,
    @BsonProperty("nickname")
    val nickname: String,
    @BsonProperty("total_score")
    val totalScore: Int,
    @BsonProperty("email")
    val email: String? = null,
    @BsonProperty("created_date")
    val createdDate: LocalDateTime
)
