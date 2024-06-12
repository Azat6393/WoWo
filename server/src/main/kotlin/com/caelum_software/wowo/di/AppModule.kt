package com.caelum_software.wowo.di

import com.caelum_software.wowo.data.gpt.ChatGptApi
import com.caelum_software.wowo.data.ktorHttpClient
import com.caelum_software.wowo.data.mongodb.MongoDbConstants.DATABASE_WOWO
import com.caelum_software.wowo.data.mongodb.MongoDbConstants.MONGO_DB_URI
import com.caelum_software.wowo.repository.CategoryRepository
import com.caelum_software.wowo.repository.GptRepository
import com.caelum_software.wowo.repository.StatisticsRepository
import com.caelum_software.wowo.repository.UserRepository
import com.caelum_software.wowo.repository.WordRepository
import com.caelum_software.wowo.service.AdminService
import com.caelum_software.wowo.service.GameService
import com.caelum_software.wowo.service.UserService
import com.caelum_software.wowo.utils.Keys.MONGODB_URI
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