package com.ynab.data.dataSource

import com.ynab.data.repository.dataClass.Account
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class AccountDataSource @Inject constructor(
    private val localAccountDataSource: LocalAccountDataSource
) {
    fun getAccountsByBudgetId(budgetId: Int): Flow<List<Account>> =
        localAccountDataSource.getAccountsByBudgetId(budgetId)
}