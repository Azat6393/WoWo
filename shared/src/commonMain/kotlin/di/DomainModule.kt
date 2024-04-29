package di

import data.remote.WoWoApi
import data.remote.ktorHttpClient
import data.repository.GameRepositoryImpl
import data.repository.UserRepositoryImpl
import domain.repository.GameRepository
import domain.repository.UserRepository
import io.ktor.client.HttpClient
import org.koin.dsl.module

val domainModule = module {
    single<HttpClient> { ktorHttpClient }
    single<WoWoApi> { WoWoApi(get()) }

    // Repository
    single<UserRepository> { UserRepositoryImpl(get()) }
    single<GameRepository> { GameRepositoryImpl(get()) }
}

