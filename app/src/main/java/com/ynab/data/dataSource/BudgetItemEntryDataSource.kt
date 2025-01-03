package com.ynab.data.dataSource

import com.ynab.data.repository.dataClass.BudgetItemEntry
import kotlinx.coroutines.flow.Flow
import java.math.BigDecimal
import java.time.YearMonth
import javax.inject.Inject

class BudgetItemEntryDataSource @Inject constructor(
    private val localBudgetItemEntryDataSource: LocalBudgetItemEntryDataSource
) {
    fun addBudgetItemEntry(budgetItemId: Int, yearMonth: YearMonth) =
        localBudgetItemEntryDataSource.addBudgetItemEntry(budgetItemId, yearMonth)

    fun getBudgetItemEntries(
        budgetItemIds: List<Int>,
        yearMonth: YearMonth
    ): Flow<List<BudgetItemEntry>> =
        localBudgetItemEntryDataSource.getBudgetItemEntries(
            budgetItemIds,
            yearMonth
        )

    fun getBudgetItemEntry(
        budgetItemId: Int,
        yearMonth: YearMonth
    ): Flow<BudgetItemEntry> =
        localBudgetItemEntryDataSource.getBudgetItemEntry(
            budgetItemId,
            yearMonth
        )

    fun getBudgetItemEntry(budgetItemEntryId: Int): Flow<BudgetItemEntry> =
        localBudgetItemEntryDataSource.getBudgetItemEntry(budgetItemEntryId)

    fun updateAssigned(budgetItemEntry: BudgetItemEntry, newValue: BigDecimal) =
        localBudgetItemEntryDataSource.updateAssigned(budgetItemEntry, newValue)

    fun isBudgetItemEntryExist(budgetItemId: Int, yearMonth: YearMonth): Boolean =
        localBudgetItemEntryDataSource.isBudgetItemEntryExist(budgetItemId, yearMonth)

    fun addBudgetItemEntries(budgetItemIds: List<Int>, yearMonth: YearMonth) =
        localBudgetItemEntryDataSource.addBudgetItemEntries(budgetItemIds, yearMonth)

}
