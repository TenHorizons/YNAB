package com.ynab.data.dataSource

import com.ynab.data.repository.dataClass.Account
import kotlinx.coroutines.flow.Flow

interface LocalAccountDataSource {
    fun getAccountsByBudgetId(budgetId: Int): Flow<List<Account>>

}