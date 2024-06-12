package di

import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import presentation.game.GameViewModel
import presentation.profile.ProfileViewModel

actual val viewModelModule: Module
    get() = module {
        singleOf(::GameViewModel)
        singleOf(::ProfileViewModel)
    }