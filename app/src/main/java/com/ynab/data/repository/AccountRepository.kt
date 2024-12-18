package com.ynab.data.repository

import com.ynab.data.repository.dataClass.Account
import kotlinx.coroutines.flow.Flow
import java.math.BigDecimal

interface AccountRepository: Repository {
    val accounts: Flow<List<Account>>

    suspend fun isAccountNameExist(accountName: String): Boolean
    suspend fun addAccount(accountName: String, accountBalance: BigDecimal): Boolean
    suspend fun updateAccountName(accountToEdit: Account, newAccountName: String): Boolean
}