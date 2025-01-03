package com.ynab.data.dataSource

import com.ynab.data.repository.dataClass.Account
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/** To handle remote and local account data source*/
class AccountDataSource @Inject constructor(
    private val localAccountDataSource: LocalAccountDataSource
) {
    fun getAccountsByBudgetId(budgetId: Int): Flow<List<Account>> =
        localAccountDataSource.getAccountsByBudgetId(budgetId)

    fun isAccountNameExist(accountName: String, budgetId: Int): Boolean =
        localAccountDataSource.isAccountNameExist(accountName, budgetId)

    fun addAccount(accountName: String, budgetId: Int): Long? =
        localAccountDataSource.addAccount(accountName, budgetId)

    fun updateAccount(accountToEdit: Account, newAccountName: String): Boolean =
        localAccountDataSource.updateAccount(accountToEdit, newAccountName)

    fun deleteAccount(account: Account) =
        localAccountDataSource.deleteAccount(account)

    fun getAccountById(accountId: Int): Account? =
        localAccountDataSource.getAccountById(accountId)
}