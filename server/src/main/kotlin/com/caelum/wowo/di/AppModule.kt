package com.caelum.wowo.di

import com.caelum.wowo.repository.WordRepository
import com.caelum.wowo.service.MongoDb
import com.caelum.wowo.service.MongoDbConstants
import org.koin.dsl.module

val appModule = module {
    single<MongoDb> { MongoDb(MongoDbConstants.DATABASE_WOWO) }
    single<WordRepository> { WordRepository(get()) }
}