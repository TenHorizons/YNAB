package com.ynab.data.repository

import com.ynab.data.repository.dataClass.Account
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.math.BigDecimal
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
                        balance = 8000.toBigDecimal(),
                        budgetId = 0
                    ),
                    Account(
                        accountId = 1,
                        accountName = "StashAway",
                        uiPosition = 1,
                        balance = 60000.toBigDecimal(),
                        budgetId = 0
                    ),
                    Account(
                        accountId = 2,
                        accountName = "IBKR",
                        uiPosition = 2,
                        balance = 100000.toBigDecimal(),
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

    override suspend fun addAccount(accountName: String, accountBalance: BigDecimal): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun updateAccountName(accountToEdit: Account, newAccountName: String): Boolean {
        TODO("Not yet implemented")
    }

    override fun deleteAccount(account: Account) {
        TODO("Not yet implemented")
    }

    override fun saveAllData() {
        TODO("Not yet implemented")
    }
}