package com.ynab.data.dataSource.room

import android.database.sqlite.SQLiteConstraintException
import android.util.Log
import com.ynab.TAG_PREFIX
import com.ynab.data.dataSource.LocalBudgetItemDataSource
import java.math.BigDecimal
import java.time.YearMonth
import javax.inject.Inject

private const val TAG = "${TAG_PREFIX}RoomLocalBudgetItemDataSource"

class RoomLocalBudgetItemDataSource @Inject constructor(
    db: RoomDatabase
): LocalBudgetItemDataSource {
    private val budgetItemDao = db.budgetItemDao()
    /**Add new budget items for a single category and YearMonth.*/
    override fun addBudgetItems(
        categoryId: Int,
        budgetItemNames: List<String>,
        yearMonth: YearMonth
    ): Exception? {
        val budgetItems = budgetItemNames.map { name ->
            BudgetItem(
                categoryId = categoryId,
                budgetItemName = name,
                itemUiPosition = budgetItemNames.indexOf(name),
                yearMonth = yearMonth,
                assigned = BigDecimal.ZERO,
                rolloverBalance = BigDecimal.ZERO
            )
        }
        try {
            budgetItemDao.insert(budgetItems)
            return null
        } catch (e: SQLiteConstraintException) {
            Log.d(
                TAG,
                "addBudgetItems threw SQLiteConstraintException, likely due to budget item/s already exist."
            )
            return e
        } catch (e: Exception) {
            Log.d(TAG, "Unknown error at addBudgetItems: ${e.stackTraceToString()}")
            return e
        }
    }
}