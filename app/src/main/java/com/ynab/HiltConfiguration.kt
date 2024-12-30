package com.ynab

import android.content.Context
import androidx.room.Room
import com.ynab.data.dataSource.LocalAccountDataSource
import com.ynab.data.dataSource.LocalTransactionDataSource
import com.ynab.data.dataSource.LocalUserDataSource
import com.ynab.data.dataSource.room.RoomLocalAccountDataSource
import com.ynab.data.dataSource.room.RoomDatabase
import com.ynab.data.dataSource.room.RoomLocalTransactionDataSource
import com.ynab.data.dataSource.room.RoomLocalUserDataSource
import com.ynab.data.repository.AccountRepository
import com.ynab.data.repository.AccountRepositoryImpl
import com.ynab.data.repository.TransactionRepository
import com.ynab.data.repository.TransactionRepositoryImpl
import com.ynab.data.repository.UserRepository
import com.ynab.data.repository.UserRepositoryImpl
import com.ynab.domain.AddTransactionUseCase
import com.ynab.domain.AddTransactionUseCaseImpl
import com.ynab.domain.BasicAuthUseCase
import com.ynab.domain.BasicAuthUseCaseImpl
import com.ynab.domain.BudgetUseCase
import com.ynab.domain.FakeBudgetUseCaseImpl
import com.ynab.domain.LoadAppUseCase
import com.ynab.domain.LoadAppUseCaseImpl
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
    abstract fun bindLoadAppUseCase(impl: LoadAppUseCaseImpl): LoadAppUseCase

    @Binds
    abstract fun bindAccountRepository(impl: AccountRepositoryImpl): AccountRepository

    @Binds
    abstract fun bindLocalAccountDataSource(impl: RoomLocalAccountDataSource): LocalAccountDataSource

    @Binds
    abstract fun bindAddTransactionUseCase(impl: AddTransactionUseCaseImpl): AddTransactionUseCase

    @Binds
    abstract fun bindTransactionRepository(impl: TransactionRepositoryImpl): TransactionRepository

    @Binds
    abstract fun bindLocalTransactionDataSource(impl: RoomLocalTransactionDataSource): LocalTransactionDataSource

    @Binds
    abstract fun bindBudgetUseCase(impl: FakeBudgetUseCaseImpl): BudgetUseCase
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
        ).fallbackToDestructiveMigration().build()
    }
}