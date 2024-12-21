package com.ynab.data.repository

import com.ynab.data.repository.dataClass.Account
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class FakeAccountRepository @Inject constructor(): AccountRepository {
    override val accounts: Flow<List<Account>> = flow {
        while (true) {
            emit(
                listOf(
                    Account(
                        accountId = 0,
                        accountName = "Maybank",
                        uiPosition = 0,
                        budgetId = 0
                    ),
                    Account(
                        accountId = 1,
                        accountName = "StashAway",
                        uiPosition = 1,
                        budgetId = 0
                    ),
                    Account(
                        accountId = 2,
                        accountName = "IBKR",
                        uiPosition = 2,
                        budgetId = 0
                    )
                )
            )
            delay(5000)
        }
    }

    override suspend fun isAccountNameExist(accountName: String): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun addAccount(accountName: String): Long? {
        TODO("Not yet implemented")
    }

    override suspend fun updateAccountName(accountToEdit: Account, newAccountName: String): Boolean {
        TODO("Not yet implemented")
    }

    override fun deleteAccount(account: Account) {
        TODO("Not yet implemented")
    }

    override fun getAccount(accountId: Int): Account? {
        TODO("Not yet implemented")
    }

    override fun saveAllData() {
        TODO("Not yet implemented")
    }
}