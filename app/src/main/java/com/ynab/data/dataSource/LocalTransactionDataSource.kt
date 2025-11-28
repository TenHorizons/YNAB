package com.ynab.data.dataSource

import com.ynab.data.repository.dataClass.BudgetItemEntry
import com.ynab.data.repository.dataClass.Transaction
import kotlinx.coroutines.flow.Flow
import java.math.BigDecimal
import java.time.LocalDate
import java.time.YearMonth

interface LocalTransactionDataSource {
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
    fun getTransactions(yearMonth: YearMonth): Flow<List<Transaction>>
    fun getTransactions(budgetItemEntry: BudgetItemEntry): Flow<List<Transaction>>

}
