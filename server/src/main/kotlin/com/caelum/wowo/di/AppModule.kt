package com.caelum.wowo.di

import com.caelum.wowo.repository.WordRepository
import com.caelum.wowo.mongodb.MongoDb
import com.caelum.wowo.mongodb.MongoDbConstants
import com.caelum.wowo.repository.CategoryRepository
import com.caelum.wowo.repository.UserRepository
import com.caelum.wowo.service.AdminService
import com.caelum.wowo.service.GameService
import com.caelum.wowo.service.UserService
import org.koin.dsl.module

val appModule = module {
    single<MongoDb> { MongoDb(MongoDbConstants.DATABASE_WOWO) }

    // Repository
    single<WordRepository> { WordRepository(get()) }
    single<CategoryRepository> { CategoryRepository(get()) }
    single<UserRepository> { UserRepository(get()) }

    // Service
    single<GameService> { GameService(get(), get()) }
    single<AdminService> { AdminService(get(), get()) }
    single<UserService> { UserService(get()) }
}