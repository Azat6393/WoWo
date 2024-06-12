package di

import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import utils.UniqueIdGenerator

actual val platformModule: Module
    get() = module {
        singleOf(::UniqueIdGenerator)
    }