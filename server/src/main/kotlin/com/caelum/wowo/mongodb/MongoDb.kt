package com.caelum.wowo.mongodb

import com.mongodb.MongoException
import com.mongodb.kotlin.client.coroutine.MongoClient
import com.mongodb.kotlin.client.coroutine.MongoDatabase
import org.bson.BsonInt64
import org.bson.Document

typealias IsSuccess = Boolean

class MongoDb(
    private val databaseName: String,
    private val connectionEnvVariable: String = "MONGODB_URI",
) {

    suspend fun setupConnection(): MongoDatabase? {
        val connectString = System.getenv(connectionEnvVariable)

        val client = MongoClient.create(connectionString = connectString)
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

