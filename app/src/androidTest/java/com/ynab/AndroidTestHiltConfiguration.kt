package com.ynab

import com.ynab.domain.BasicAuthUseCase
import com.ynab.domain.FakeBasicAuthUseCase
import dagger.Binds
import dagger.Module
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import javax.inject.Singleton

//@Module
//@TestInstallIn(
//    components = [SingletonComponent::class],
//    replaces = [HiltBindings::class]
//)
//abstract class AndroidTestHiltBindings {
//
//    @Singleton
//    @Binds
//    abstract fun bindBasicAuthUseCase(
//        impl: FakeBasicAuthUseCase
//    ): BasicAuthUseCase
//
//}