package com.caelum.wowo.repository

import com.caelum.wowo.mongodb.IsSuccess
import com.caelum.wowo.mongodb.MongoDb
import com.caelum.wowo.mongodb.MongoDbConstants
import com.caelum.wowo.mongodb.dto.CategoryDto
import com.caelum.wowo.utils.exception.UnknownException
import com.mongodb.client.model.Aggregates
import com.mongodb.client.model.Filters
import com.mongodb.kotlin.client.coroutine.MongoDatabase
import org.bson.types.ObjectId
import java.util.UUID

class CategoryRepository(private val mongoDb: MongoDb) {

    private var database: MongoDatabase? = null

    suspend fun addCategory(category: String, language: String): Result<IsSuccess> {
        return try {
            if (database == null) {
                database = mongoDb.setupConnection()
            }

            val collection =
                database!!.getCollection<CategoryDto>(collectionName = MongoDbConstants.COLLECTION_CATEGORIES)
            val item = CategoryDto(
                id = ObjectId(),
                uuid = UUID.randomUUID().toString(),
                name = category,
                language = language
            )
            val result = collection.insertOne(item)
            if (result.insertedId == null)  Result.failure(UnknownException("Something went wrong!"))
            else Result.success(true)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getCategories(language: String): Result<List<CategoryDto>> {
        return try {
            if (database == null) {
                database = mongoDb.setupConnection()
            }
            val categories = mutableListOf<CategoryDto>()
            val collection =
                database!!.getCollection<CategoryDto>(collectionName = MongoDbConstants.COLLECTION_CATEGORIES)
            val queryParams = Filters
                .and(
                    listOf(
                        Filters.eq(CategoryDto::language.name, language),
                    )
                )
            collection.aggregate<CategoryDto>(
                listOf(
                    Aggregates.match(queryParams)
                )
            ).collect(categories::add)

            Result.success(categories)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}