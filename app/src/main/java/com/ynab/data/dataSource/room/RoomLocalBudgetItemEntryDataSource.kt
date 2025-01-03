package com.ynab.data.dataSource.room

import android.database.sqlite.SQLiteConstraintException
import android.util.Log
import com.ynab.TAG_PREFIX
import com.ynab.data.dataSource.LocalBudgetItemEntryDataSource
import com.ynab.data.repository.dataClass.BudgetItemEntry
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.math.BigDecimal
import java.time.YearMonth
import javax.inject.Inject

private const val TAG = "${TAG_PREFIX}RoomLocalBudgetItemEntryDataSource"

class RoomLocalBudgetItemEntryDataSource @Inject constructor(
    db: RoomDatabase
) : LocalBudgetItemEntryDataSource {
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
                TAG,
                "addBudgetItemEntry threw SQLiteConstraintException, likely due to entry already exist."
            )
            throw e
        } catch (e: Exception) {
            Log.d(TAG, "Unknown error at addBudgetItemEntry: ${e.stackTraceToString()}")
            throw e
        }

    }

    override fun addBudgetItemEntries(budgetItemIds: List<Int>, yearMonth: YearMonth) =
        budgetItemEntryDao.insert(budgetItemIds.map { BudgetItemEntry(
            budgetItemId = it,
            yearMonth = yearMonth,
            assigned = BigDecimal.ZERO,
            rolloverBalance = BigDecimal.ZERO
        ) })

    override fun getBudgetItemEntries(
        budgetItemIds: List<Int>,
        yearMonth: YearMonth
    ): Flow<List<BudgetItemEntry>> =
        try {
            budgetItemEntryDao.getBudgetItemEntriesByBudgetIdsAndYearMonth(budgetItemIds, yearMonth)
                .map { entryList ->
                    entryList.map { entry -> entry.toUiBudgetItemEntry() }
                }
        } catch (e: SQLiteConstraintException) {
            Log.d(
                TAG, "getBudgetItemEntries threw SQLiteConstraintException."
            )
            throw e
        } catch (e: Exception) {
            Log.d(TAG, "Unknown error at getBudgetItemEntries: ${e.stackTraceToString()}")
            throw e
        }

    override fun getBudgetItemEntry(
        budgetItemId: Int,
        yearMonth: YearMonth
    ): Flow<BudgetItemEntry> =
        try {
            budgetItemEntryDao.getBudgetItemEntryByBudgetItemIdAndYearMonth(budgetItemId, yearMonth)
                .map { entry -> entry.toUiBudgetItemEntry() }
        } catch (e: SQLiteConstraintException) {
            Log.d(
                TAG, "getBudgetItemEntry threw SQLiteConstraintException."
            )
            throw e
        } catch (e: Exception) {
            Log.d(TAG, "Unknown error at getBudgetItemEntry: ${e.stackTraceToString()}")
            throw e
        }

    override fun getBudgetItemEntry(budgetItemEntryId: Int): Flow<BudgetItemEntry> =
        try {
            budgetItemEntryDao.getBudgetItemEntryByBudgetItemEntryId(budgetItemEntryId)
                .map { entry -> entry.toUiBudgetItemEntry() }
        } catch (e: SQLiteConstraintException) {
            Log.d(
                TAG,
                "addBudgetItemEntry threw SQLiteConstraintException, likely due to entry already exist."
            )
            throw e
        } catch (e: Exception) {
            Log.d(TAG, "Unknown error at addBudgetItemEntry: ${e.stackTraceToString()}")
            throw e
        }

    override fun updateAssigned(
        budgetItemEntry: BudgetItemEntry,
        newValue: BigDecimal
    ) =
        try {
            val newBudgetItemEntry =
                budgetItemEntry.copy(assigned = newValue).toRoomBudgetItemEntry()
            budgetItemEntryDao.update(newBudgetItemEntry)
        } catch (e: SQLiteConstraintException) {
            Log.d(
                TAG,
                "addBudgetItemEntry threw SQLiteConstraintException, likely due to entry already exist."
            )
            throw e
        } catch (e: Exception) {
            Log.d(TAG, "Unknown error at addBudgetItemEntry: ${e.stackTraceToString()}")
            throw e
        }

    override fun addBudgetItemEntriesWhenNotExist(budgetItemEntries: List<BudgetItemEntry>) =
        budgetItemEntryDao.insert(budgetItemEntries.map { it.toRoomBudgetItemEntry() })

    override fun isBudgetItemEntryExist(budgetItemId: Int, yearMonth: YearMonth): Boolean =
        budgetItemEntryDao.isBudgetItemEntryExist(budgetItemId, yearMonth)

    private fun com.ynab.data.dataSource.room.BudgetItemEntry.toUiBudgetItemEntry(): BudgetItemEntry =
        BudgetItemEntry(
            budgetItemEntryId = budgetItemEntryId,
            budgetItemId = budgetItemId,
            yearMonth = yearMonth,
            assigned = assigned,
            rolloverBalance = rolloverBalance
        )

    private fun BudgetItemEntry.toRoomBudgetItemEntry(): com.ynab.data.dataSource.room.BudgetItemEntry =
        com.ynab.data.dataSource.room.BudgetItemEntry(
            budgetItemEntryId = budgetItemEntryId,
            budgetItemId = budgetItemId,
            yearMonth = yearMonth,
            assigned = assigned,
            rolloverBalance = rolloverBalance
        )
}