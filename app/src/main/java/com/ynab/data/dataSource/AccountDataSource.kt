package com.ynab.data.dataSource

import com.ynab.data.repository.dataClass.Account
import kotlinx.coroutines.flow.Flow
import java.math.BigDecimal
import javax.inject.Inject

/** To handle remote and local account data source*/
class AccountDataSource @Inject constructor(
    private val localAccountDataSource: LocalAccountDataSource
) {
    fun getAccountsByBudgetId(budgetId: Int): Flow<List<Account>> =
        localAccountDataSource.getAccountsByBudgetId(budgetId)

    fun isAccountNameExist(accountName: String, budgetId: Int): Boolean =
        localAccountDataSource.isAccountNameExist(accountName, budgetId)

    fun addAccount(accountName: String, accountBalance: BigDecimal, budgetId: Int): Boolean =
        localAccountDataSource.addAccount(accountName, accountBalance, budgetId)
}