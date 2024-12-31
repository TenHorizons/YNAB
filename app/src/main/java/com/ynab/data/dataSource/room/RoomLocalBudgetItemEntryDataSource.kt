package com.ynab.data.dataSource.room

import android.database.sqlite.SQLiteConstraintException
import android.util.Log
import com.ynab.TAG_PREFIX
import com.ynab.data.dataSource.LocalBudgetItemEntryDataSource
import java.math.BigDecimal
import java.time.YearMonth
import javax.inject.Inject

private const val TAG = "${TAG_PREFIX}RoomLocalBudgetItemEntryDataSource"

class RoomLocalBudgetItemEntryDataSource @Inject constructor(
    db: RoomDatabase
): LocalBudgetItemEntryDataSource {
    private val budgetItemEntryDao = db.budgetItemEntryDao()
    /** Add budget item entry.*/
    override fun addBudgetItemEntry(budgetItemId: Int, yearMonth: YearMonth) {
        val budgetItemEntry = BudgetItemEntry(
            budgetItemId = budgetItemId,
            yearMonth = yearMonth,
            assigned = BigDecimal.ZERO,
            rolloverBalance = BigDecimal.ZERO
        )
        try {
            budgetItemEntryDao.insert(budgetItemEntry)
        } catch (e: SQLiteConstraintException) {
            Log.d(
                TAG, "addBudgetItemEntry threw SQLiteConstraintException, likely due to entry already exist."
            )
            throw e
        } catch (e: Exception) {
            Log.d(TAG, "Unknown error at addBudgetItemEntry: ${e.stackTraceToString()}")
            throw e
        }

    }
}