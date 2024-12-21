package com.ynab.data.repository

import android.util.Log
import com.ynab.TAG_PREFIX
import com.ynab.data.dataSource.TransactionDataSource
import com.ynab.data.repository.dataClass.Transaction
import kotlinx.coroutines.flow.Flow
import java.math.BigDecimal
import java.time.LocalDate
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
    ): Boolean {
        try{
            return transactionDs.addTransaction(
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
    }

    override fun getTransactionsByAccountId(accountId: Int): Flow<List<Transaction>> {
        try{
            return transactionDs.getTransactionsByAccountId(accountId)
        }catch (e: Exception) {
            Log.d(TAG, "An unknown error occurred at getTransactionsByAccountId: ${e.stackTrace}")
            throw e
        }
    }

    override fun getTransactionsByAccountIdList(accountIdList: List<Int>): Flow<List<Transaction>> {
        try{
            return transactionDs.getTransactionsByAccountIdList(accountIdList)
        }catch (e: Exception) {
            Log.d(TAG, "An unknown error occurred at getTransactionsByAccountId: ${e.stackTrace}")
            throw e
        }
    }
}