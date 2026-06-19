package ai.kwotly.companion.di

import ai.kwotly.companion.data.repository.AuthRepository
import ai.kwotly.companion.data.repository.AuthRepositoryImpl
import ai.kwotly.companion.data.repository.QuotesRepository
import ai.kwotly.companion.data.repository.QuotesRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindAuthRepository(impl: AuthRepositoryImpl): AuthRepository

    @Binds
    @Singleton
    abstract fun bindQuotesRepository(impl: QuotesRepositoryImpl): QuotesRepository
}