package com.ynab.data.repository

import com.ynab.data.repository.dataClass.Transaction
import kotlinx.coroutines.flow.Flow
import java.math.BigDecimal
import java.time.LocalDate

interface TransactionRepository {
    fun addTransaction(
        amount: BigDecimal,
        accountId: Int,
        budgetItemId: Int,
        date: LocalDate,
        memo: String
    ): Boolean

    fun getTransactionsByAccountId(accountId: Int): Flow<List<Transaction>>
    fun getTransactionsByAccountIdList(accountIdList: List<Int>): Flow<List<Transaction>>
    fun getTransaction(transactionId: Int): Transaction?
    fun updateTransaction(transaction: Transaction): Boolean
    fun deleteTransaction(transactionId: Int): Boolean
}
