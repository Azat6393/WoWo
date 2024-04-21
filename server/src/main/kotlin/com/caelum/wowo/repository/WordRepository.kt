package com.caelum.wowo.repository

import com.caelum.wowo.mongodb.IsSuccess
import com.caelum.wowo.mongodb.MongoDb
import com.caelum.wowo.mongodb.MongoDbConstants.COLLECTION_WORDS
import com.caelum.wowo.mongodb.dto.WordDto
import com.caelum.wowo.utils.exception.NotFoundException
import com.caelum.wowo.utils.exception.UnknownException
import com.mongodb.client.model.Aggregates
import com.mongodb.client.model.Filters
import com.mongodb.client.model.Filters.eq
import com.mongodb.kotlin.client.coroutine.MongoDatabase
import kotlinx.coroutines.flow.firstOrNull
import org.bson.types.ObjectId
import java.time.LocalDateTime
import java.util.UUID

class WordRepository(private val mongoDb: MongoDb) {

    private var database: MongoDatabase? = null

    suspend fun addWord(word: String, category: String, language: String): Result<IsSuccess> {
        return try {
            if (database == null) {
                database = mongoDb.setupConnection()
            }

            val collection = database!!.getCollection<WordDto>(collectionName = COLLECTION_WORDS)
            val item = WordDto(
                id = ObjectId(),
                uuid = UUID.randomUUID().toString(),
                word = word,
                category = category,
                language = language,
                createdData = LocalDateTime.now()
            )
            val result = collection.insertOne(item)

            if (result.insertedId == null) Result.failure(UnknownException("Something went wrong!"))
            else Result.success(true)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getRandomWord(language: String, category: String): Result<WordDto> {
        return try {
            if (database == null) {
                database = mongoDb.setupConnection()
            }

            val collection = database!!.getCollection<WordDto>(collectionName = COLLECTION_WORDS)
            val queryParams = Filters
                .and(
                    listOf(
                        eq(WordDto::language.name, language), eq(WordDto::category.name, category)
                    )
                )

            val resultFlow = collection.aggregate<WordDto>(
                listOf(
                    Aggregates.match(queryParams),
                    Aggregates.sample(1)
                )
            ).firstOrNull()

            if (resultFlow == null) Result.failure(NotFoundException())
            else Result.success(resultFlow)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}