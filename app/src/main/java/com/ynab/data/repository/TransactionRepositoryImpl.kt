package com.ynab.data.repository

import android.util.Log
import com.ynab.TAG_PREFIX
import com.ynab.data.dataSource.TransactionDataSource
import com.ynab.data.repository.dataClass.BudgetItemEntry
import com.ynab.data.repository.dataClass.Transaction
import kotlinx.coroutines.flow.Flow
import java.math.BigDecimal
import java.time.LocalDate
import java.time.YearMonth
import javax.inject.Inject

private const val TAG = "${TAG_PREFIX}TransactionRepositoryImpl"

class TransactionRepositoryImpl @Inject constructor(
    private val transactionDs: TransactionDataSource
): TransactionRepository {

    override fun addTransaction(
        amount: BigDecimal,
        accountId: Int,
        budgetItemId: Int,
        date: LocalDate,
        memo: String
    ): Boolean =
        try{
            transactionDs.addTransaction(
                amount = amount,
                accountId = accountId,
                budgetItemId = budgetItemId,
                date = date,
                memo = memo
            )
        }catch (e: Exception) {
            Log.d(TAG, "An unknown error occurred at addTransaction: ${e.stackTrace}")
            throw e
        }

    override fun getTransactionsByAccountId(accountId: Int): Flow<List<Transaction>> =
        try{
            transactionDs.getTransactionsByAccountId(accountId)
        }catch (e: Exception) {
            Log.d(TAG, "An unknown error occurred at getTransactionsByAccountId: ${e.stackTrace}")
            throw e
        }

    override fun getTransactionsByAccountIdList(accountIdList: List<Int>): Flow<List<Transaction>> =
        try{
            transactionDs.getTransactionsByAccountIdList(accountIdList)
        }catch (e: Exception) {
            Log.d(TAG, "An unknown error occurred at getTransactionsByAccountIdList: ${e.stackTrace}")
            throw e
        }

    override fun getTransaction(transactionId: Int): Transaction? =
        try{
            transactionDs.getTransaction(transactionId)
        }catch (e: Exception) {
            Log.d(TAG, "An unknown error occurred at getTransaction: ${e.stackTrace}")
            throw e
        }

    override fun updateTransaction(transaction: Transaction): Boolean =
        try{
            transactionDs.updateTransaction(transaction)
        }catch (e: Exception) {
            Log.d(TAG, "An unknown error occurred at updateTransaction: ${e.stackTrace}")
            throw e
        }

    override fun deleteTransaction(transactionId: Int): Boolean =
        try{
            transactionDs.deleteTransaction(transactionId)
        }catch (e: Exception) {
            Log.d(TAG, "An unknown error occurred at deleteTransaction: ${e.stackTrace}")
            throw e
        }

    override fun getTransactions(yearMonth: YearMonth): Flow<List<Transaction>> =
        try{
            transactionDs.getTransactions(yearMonth)
        }catch (e: Exception) {
            Log.d(TAG, "An unknown error occurred at getTransactions: ${e.stackTrace}")
            throw e
        }

    override fun getTransactions(budgetItemEntry: BudgetItemEntry): Flow<List<Transaction>> =
        try{
            transactionDs.getTransactions(budgetItemEntry)
        }catch (e: Exception) {
            Log.d(TAG, "An unknown error occurred at getTransactions: ${e.stackTrace}")
            throw e
        }
}