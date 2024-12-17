package com.ynab

import android.content.Context
import androidx.room.Room
import com.ynab.data.dataSource.LocalUserDataSource
import com.ynab.data.dataSource.room.RoomDatabase
import com.ynab.data.dataSource.room.RoomLocalUserDataSource
import com.ynab.data.repository.UserRepository
import com.ynab.data.repository.UserRepositoryImpl
import com.ynab.domain.BasicAuthUseCase
import com.ynab.domain.BasicAuthUseCaseImpl
import com.ynab.domain.FakeLoadAppUseCase
import com.ynab.domain.LoadAppUseCase
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class HiltBindings {
    @Binds
    abstract fun bindBasicAuthUseCase(impl: BasicAuthUseCaseImpl): BasicAuthUseCase
    @Binds
    @Singleton
    abstract fun bindUserRepository(impl: UserRepositoryImpl): UserRepository
    @Binds
    @Singleton
    abstract fun bindLocalUserDataSource(impl: RoomLocalUserDataSource): LocalUserDataSource
    @Binds
    abstract fun bindLoadAppUseCase(impl: FakeLoadAppUseCase): LoadAppUseCase
}

@Module
@InstallIn(SingletonComponent::class)
object RoomModule {
    @Provides
    @Singleton
    fun provideRoomDatabase(@ApplicationContext app: Context): RoomDatabase {
        return Room.databaseBuilder(
            context = app,
            klass = RoomDatabase::class.java,
            name = "ynab_database"
        ).build()
    }
}