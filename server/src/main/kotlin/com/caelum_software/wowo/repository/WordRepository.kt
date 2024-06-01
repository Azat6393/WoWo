package com.caelum_software.wowo.repository

import com.caelum_software.wowo.data.mongodb.IsSuccess
import com.caelum_software.wowo.data.mongodb.MongoDbConstants.COLLECTION_WORDS
import com.caelum_software.wowo.data.dto.WordDto
import com.caelum_software.wowo.utils.exception.NotFoundException
import com.caelum_software.wowo.utils.exception.UnknownException
import com.mongodb.client.model.Aggregates
import com.mongodb.client.model.Filters
import com.mongodb.client.model.Filters.eq
import com.mongodb.kotlin.client.coroutine.MongoDatabase
import kotlinx.coroutines.flow.firstOrNull
import org.bson.types.ObjectId
import java.time.LocalDateTime
import java.util.UUID

class WordRepository(private val mongoDatabase: MongoDatabase) {

    suspend fun addWord(
        words: List<String>,
        category: String,
        language: String,
    ): IsSuccess {
        return try {
            val collection =
                mongoDatabase.getCollection<WordDto>(collectionName = "$COLLECTION_WORDS-$language")
            val items = words.map { word ->
                WordDto(
                    id = ObjectId(),
                    uuid = UUID.randomUUID().toString(),
                    word = word,
                    category = category,
                    language = language,
                    createdData = LocalDateTime.now()
                )
            }

            val result = collection.insertMany(items)
            if (result.insertedIds.isEmpty()) throw UnknownException("Something went wrong!")
            else true
        } catch (e: Exception) {
            throw e
        }
    }

    suspend fun getRandomWord(language: String, category: String): Result<WordDto> {
        return try {
            val collection = mongoDatabase.getCollection<WordDto>(collectionName = "$COLLECTION_WORDS-$language")
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
            throw e
        }
    }
}