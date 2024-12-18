package com.ynab.data.repository

import com.ynab.data.dataSource.AccountDataSource
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import javax.inject.Inject

class AccountRepositoryImpl @Inject constructor(
    private val accountDs: AccountDataSource,
    userRepository: UserRepository
) : AccountRepository {

    @OptIn(ExperimentalCoroutinesApi::class)
    override val accounts =
        userRepository.getUserLastBudgetId().flatMapLatest { budgetId ->
            accountDs.getAccountsByBudgetId(budgetId).distinctUntilChanged()
        }

    override fun saveAllData() {
        TODO("Not yet implemented")
    }
}