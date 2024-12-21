package com.ynab.data.dataSource

import com.ynab.data.repository.dataClass.Account
import kotlinx.coroutines.flow.Flow

interface LocalAccountDataSource {
    fun getAccountsByBudgetId(budgetId: Int): Flow<List<Account>>
    fun isAccountNameExist(accountName: String, budgetId: Int): Boolean
    fun addAccount(accountName: String, budgetId: Int): Long?
    fun updateAccount(accountToEdit: Account, newAccountName: String): Boolean
    fun deleteAccount(account: Account)
    fun getAccountById(accountId: Int): Account?

}