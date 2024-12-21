package com.ynab.data.repository

import com.ynab.data.repository.dataClass.Account
import kotlinx.coroutines.flow.Flow

interface AccountRepository: Repository {
    val accounts: Flow<List<Account>>

    suspend fun isAccountNameExist(accountName: String): Boolean
    suspend fun addAccount(accountName: String): Long?
    suspend fun updateAccountName(accountToEdit: Account, newAccountName: String): Boolean
    fun deleteAccount(account: Account)
    fun getAccount(accountId: Int): Account?
}