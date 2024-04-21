package com.caelum.wowo.data.dto

import org.bson.codecs.pojo.annotations.BsonId
import org.bson.codecs.pojo.annotations.BsonProperty
import org.bson.types.ObjectId

data class CategoryDto(
    @BsonId
    val id: ObjectId,
    @BsonProperty("uuid")
    val uuid: String,
    @BsonProperty("name")
    val name: String,
    @BsonProperty("language")
    val language: String
)
