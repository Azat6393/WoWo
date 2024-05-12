package com.caelum.wowo.di

import com.caelum.wowo.data.gpt.ChatGptApi
import com.caelum.wowo.data.ktorHttpClient
import com.caelum.wowo.data.mongodb.MongoDbConstants.DATABASE_WOWO
import com.caelum.wowo.data.mongodb.MongoDbConstants.MONGO_DB_URI
import com.caelum.wowo.repository.CategoryRepository
import com.caelum.wowo.repository.GptRepository
import com.caelum.wowo.repository.StatisticsRepository
import com.caelum.wowo.repository.UserRepository
import com.caelum.wowo.repository.WordRepository
import com.caelum.wowo.service.AdminService
import com.caelum.wowo.service.GameService
import com.caelum.wowo.service.UserService
import com.caelum.wowo.utils.Keys.MONGODB_URI
import com.mongodb.kotlin.client.coroutine.MongoClient
import io.ktor.client.HttpClient
import org.koin.dsl.module

val appModule = module {
    single {
        MongoClient.create(
            System.getenv(MONGO_DB_URI) ?: MONGODB_URI
        )
    }
    single { get<MongoClient>().getDatabase(DATABASE_WOWO) }

    single<HttpClient> { ktorHttpClient }
    single<ChatGptApi> { ChatGptApi(get()) }

    // Repository
    single<WordRepository> { WordRepository(get()) }
    single<CategoryRepository> { CategoryRepository(get()) }
    single<UserRepository> { UserRepository(get()) }
    single<GptRepository> { GptRepository(get()) }
    single<StatisticsRepository> { StatisticsRepository(get()) }

    // Service
    single<GameService> { GameService(get(), get(), get(), get(), get()) }
    single<AdminService> { AdminService(get(), get()) }
    single<UserService> { UserService(get(), get()) }
}