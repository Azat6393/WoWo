package com.caelum.wowo.di

import com.caelum.wowo.repository.WordRepository
import com.caelum.wowo.mongodb.MongoDb
import com.caelum.wowo.mongodb.MongoDbConstants
import com.caelum.wowo.repository.CategoryRepository
import org.koin.dsl.module

val appModule = module {
    single<MongoDb> { MongoDb(MongoDbConstants.DATABASE_WOWO) }
    single<WordRepository> { WordRepository(get()) }
    single<CategoryRepository> { CategoryRepository(get()) }
}