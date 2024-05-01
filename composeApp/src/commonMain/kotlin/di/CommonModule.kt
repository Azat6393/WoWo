package di

import org.koin.dsl.module
import presentation.game.GameViewModel

val commonModule = module {
    factory { GameViewModel(get()) }
}