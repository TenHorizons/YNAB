package com.ynab.data.dataSource

import com.ynab.data.repository.dataClass.Account
import kotlinx.coroutines.flow.Flow
import java.math.BigDecimal

interface LocalAccountDataSource {
    fun getAccountsByBudgetId(budgetId: Int): Flow<List<Account>>
    fun isAccountNameExist(accountName: String, budgetId: Int): Boolean
    fun addAccount(accountName: String, accountBalance: BigDecimal, budgetId: Int): Boolean

}