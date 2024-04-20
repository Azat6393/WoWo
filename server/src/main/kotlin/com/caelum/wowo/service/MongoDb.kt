package com.caelum.wowo.service

import com.mongodb.MongoException
import com.mongodb.kotlin.client.coroutine.MongoClient
import com.mongodb.kotlin.client.coroutine.MongoDatabase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.bson.BsonInt64
import org.bson.Document

typealias IsSuccess = Boolean

class MongoDb(
    private val databaseName: String,
    private val connectionEnvVariable: String = "MONGODB_URI",
) {

    suspend fun setupConnection(): MongoDatabase? {
        val connectString = if (System.getenv(connectionEnvVariable) != null) {
            System.getenv(connectionEnvVariable)
        } else {
            "mongodb+srv://caelum6393:jXCYy57ouXcLRp26@wowo.3ehxnev.mongodb.net/?retryWrites=true&w=majority&appName=WoWo"
        }

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

