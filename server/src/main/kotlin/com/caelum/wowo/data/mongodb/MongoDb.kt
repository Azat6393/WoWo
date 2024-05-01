package com.caelum.wowo.data.mongodb

import com.mongodb.MongoException
import com.mongodb.kotlin.client.coroutine.MongoClient
import com.mongodb.kotlin.client.coroutine.MongoDatabase
import org.bson.BsonInt64
import org.bson.Document

typealias IsSuccess = Boolean

class MongoDb(
    private val databaseName: String,
    private val connectionEnvVariable: String = MongoDbConstants.MONGO_DB_URI,
) {

    suspend fun setupConnection(): MongoDatabase? {

        val client =
            MongoClient.create(
                connectionString = System.getenv(connectionEnvVariable) ?: ""
            )
        val database = client.getDatabase(databaseName = databaseName)

        return try {
            val command = Document("ping", BsonInt64(1))
            database.runCommand(command)
            println("Pinged your deployment. You successfully connected to MongoDB!")
            database
        } catch (me: MongoException) {
            System.err.println("MongoDB Setup error: $me")
            null
        }
    }
}

