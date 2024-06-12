package di

import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.Module
import org.koin.dsl.module
import presentation.game.GameViewModel
import presentation.profile.ProfileViewModel

actual val viewModelModule: Module
    get() = module {
        viewModelOf(::GameViewModel)
        viewModelOf(::ProfileViewModel)
    }