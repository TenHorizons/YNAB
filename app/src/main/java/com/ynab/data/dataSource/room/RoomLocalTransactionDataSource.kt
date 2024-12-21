package com.ynab.data.dataSource.room

import android.database.sqlite.SQLiteConstraintException
import android.util.Log
import com.ynab.TAG_PREFIX
import com.ynab.data.dataSource.LocalTransactionDataSource
import com.ynab.data.repository.dataClass.Transaction
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.map
import java.math.BigDecimal
import java.time.LocalDate
import javax.inject.Inject

private const val TAG = "${TAG_PREFIX}RoomLocalTransactionDataSource"

class RoomLocalTransactionDataSource @Inject constructor(
    db: RoomDatabase
): LocalTransactionDataSource {
    private val transactionDao = db.transactionDao()

    override fun addTransaction(
        amount: BigDecimal,
        accountId: Int,
        budgetItemId: Int,
        date: LocalDate,
        memo: String
    ): Boolean {
        try {
            val newTransaction = Transaction(
                accountId = accountId,
                budgetItemId = budgetItemId,
                date = date,
                amount = amount,
                memo = memo
            )
            transactionDao.insert(newTransaction)
            return true
        } catch (e: SQLiteConstraintException) {
            Log.d(TAG, "addTransaction threw SQLiteConstraintException.")
            return false
        } catch (e: Exception) {
            Log.d(TAG, "Unknown error at addTransaction: ${e.stackTraceToString()}")
            return false
        }
    }

    override fun getTransactionsByAccountId(accountId: Int): Flow<List<Transaction>> {
        try {
            return transactionDao.getTransactionsByAccountId(accountId).map { transactions ->
                transactions.map { roomTransaction -> roomTransaction.toUiTransaction() }
            }
        } catch (e: SQLiteConstraintException) {
            Log.d(TAG, "updateAccount threw SQLiteConstraintException.")
            return emptyFlow()
        } catch (e: Exception) {
            Log.d(TAG, "Unknown error at updateAccount: ${e.stackTraceToString()}")
            return emptyFlow()
        }
    }

    override fun getTransactionsByAccountIdList(accountIdList: List<Int>): Flow<List<Transaction>> {
        try {
            return transactionDao.getTransactionsByAccountIdList(accountIdList).map { transactions ->
                transactions.map { roomTransaction -> roomTransaction.toUiTransaction() }
            }
        } catch (e: SQLiteConstraintException) {
            Log.d(TAG, "updateAccount threw SQLiteConstraintException.")
            return emptyFlow()
        } catch (e: Exception) {
            Log.d(TAG, "Unknown error at updateAccount: ${e.stackTraceToString()}")
            return emptyFlow()
        }
    }

    private fun com.ynab.data.dataSource.room.Transaction.toUiTransaction(): Transaction =
        Transaction(
            transactionId = transactionId,
            accountId = accountId,
            budgetItemId = budgetItemId,
            date = date,
            amount = amount,
            memo = memo
        )

    private fun Transaction.toRoomTransaction(): com.ynab.data.dataSource.room.Transaction =
        com.ynab.data.dataSource.room.Transaction(
            transactionId = transactionId,
            accountId = accountId,
            budgetItemId = budgetItemId,
            date = date,
            amount = amount,
            memo = memo
        )
}