package com.caelum.wowo.repository

import com.caelum.wowo.service.IsSuccess
import com.caelum.wowo.service.MongoDb
import com.caelum.wowo.service.MongoDbConstants.COLLECTION_WORDS
import com.caelum.wowo.service.dto.WordDto
import com.caelum.wowo.utils.NullDatabaseException
import com.mongodb.client.model.Aggregates
import com.mongodb.client.model.Filters
import com.mongodb.client.model.Filters.eq
import com.mongodb.kotlin.client.coroutine.MongoDatabase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import org.bson.Document
import org.bson.types.ObjectId
import java.time.LocalDateTime
import java.util.UUID

class WordRepository(private val mongoDb: MongoDb, ) {

    private var database: MongoDatabase? = null

    suspend fun addWord(word: String, category: String, language: String): Result<IsSuccess> {
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

        return if (result.insertedId == null) Result.success(false)
        else Result.success(true)
    }

    suspend fun getRandomWord(
        language: String,
        category: String,
    ): Flow<Result<WordDto>> = flow {
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
        )
        resultFlow.collect { word ->
            emit(Result.success(word))
        }
    }
}