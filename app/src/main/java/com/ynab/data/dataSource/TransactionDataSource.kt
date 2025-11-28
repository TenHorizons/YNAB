package com.ynab.data.dataSource

import com.ynab.data.repository.dataClass.BudgetItemEntry
import com.ynab.data.repository.dataClass.Transaction
import kotlinx.coroutines.flow.Flow
import java.math.BigDecimal
import java.time.LocalDate
import java.time.YearMonth
import javax.inject.Inject

class TransactionDataSource @Inject constructor(
    private val localTransactionDataSource: LocalTransactionDataSource
) {
    fun addTransaction(
        amount: BigDecimal,
        accountId: Int,
        budgetItemId: Int,
        date: LocalDate,
        memo: String
    ): Boolean =
        localTransactionDataSource.addTransaction(
            amount = amount,
            accountId = accountId,
            budgetItemId = budgetItemId,
            date = date,
            memo = memo
        )

    fun getTransactionsByAccountId(accountId: Int): Flow<List<Transaction>> =
        localTransactionDataSource.getTransactionsByAccountId(accountId)

    fun getTransactionsByAccountIdList(accountIdList: List<Int>): Flow<List<Transaction>> =
        localTransactionDataSource.getTransactionsByAccountIdList(accountIdList)

    fun getTransaction(transactionId: Int): Transaction? =
        localTransactionDataSource.getTransaction(transactionId)

    fun updateTransaction(transaction: Transaction): Boolean =
        localTransactionDataSource.updateTransaction(transaction)

    fun deleteTransaction(transactionId: Int): Boolean =
        localTransactionDataSource.deleteTransaction(transactionId)

    fun getTransactions(yearMonth: YearMonth): Flow<List<Transaction>> =
        localTransactionDataSource.getTransactions(yearMonth)

    fun getTransactions(budgetItemEntry: BudgetItemEntry): Flow<List<Transaction>> =
        localTransactionDataSource.getTransactions(budgetItemEntry)
}
