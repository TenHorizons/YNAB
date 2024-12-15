package com.ynab

import com.ynab.domain.BasicAuthUseCase
import com.ynab.domain.BasicAuthUseCaseImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class HiltBindings {
    @Binds
    abstract fun bindBasicAuthUseCase(impl: BasicAuthUseCaseImpl): BasicAuthUseCase
}