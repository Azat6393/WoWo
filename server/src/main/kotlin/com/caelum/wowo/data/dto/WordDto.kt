package com.caelum.wowo.data.dto

import org.bson.codecs.pojo.annotations.BsonId
import org.bson.codecs.pojo.annotations.BsonProperty
import org.bson.types.ObjectId
import java.time.LocalDateTime

data class WordDto(
    @BsonId
    val id: ObjectId,
    @BsonProperty("uuid")
    val uuid: String,
    @BsonProperty("word")
    val word: String,
    @BsonProperty("category")
    val category: String,
    @BsonProperty("language")
    val language: String,
    @BsonProperty("created_date")
    val createdData: LocalDateTime
)
