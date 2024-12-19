package com.ynab.data.dataSource.room

import android.database.sqlite.SQLiteConstraintException
import android.util.Log
import com.ynab.TAG_PREFIX
import com.ynab.data.dataSource.LocalTransactionDataSource
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
            Log.d(
                TAG,
                "addTransaction threw SQLiteConstraintException."
            )
            return false
        } catch (e: Exception) {
            Log.e(TAG, "Unknown error at addTransaction: ${e.stackTraceToString()}")
            return false
        }
    }
}