package com.ynab.ui.transactions

import com.ynab.data.repository.dataClass.Account
import com.ynab.data.repository.dataClass.Transaction
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

data class TransactionsState(
    val isAllTransactions: Boolean = true,
    val account: Account? = null,
    val accountList: Flow<List<Account>> = emptyFlow(),
    val transactions: Flow<List<Transaction>> = emptyFlow()
)
