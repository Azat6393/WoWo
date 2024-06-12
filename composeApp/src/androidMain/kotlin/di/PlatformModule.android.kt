package di

import org.koin.android.ext.koin.androidApplication
import org.koin.core.module.Module
import org.koin.dsl.module
import utils.UniqueIdGenerator

actual val platformModule: Module
    get() = module {
        single<UniqueIdGenerator> { UniqueIdGenerator(androidApplication()) }
    }