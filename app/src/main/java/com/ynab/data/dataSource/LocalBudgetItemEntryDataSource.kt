package com.ynab.data.dataSource

import com.ynab.data.repository.dataClass.BudgetItemEntry
import kotlinx.coroutines.flow.Flow
import java.math.BigDecimal
import java.time.YearMonth

interface LocalBudgetItemEntryDataSource {
    /** Add budget item entry.*/
    fun addBudgetItemEntry(budgetItemId: Int, yearMonth: YearMonth)
    fun getBudgetItemEntries(
        budgetItemIds: List<Int>,
        yearMonth: YearMonth
    ): Flow<List<BudgetItemEntry>>

    fun getBudgetItemEntry(
        budgetItemId: Int,
        yearMonth: YearMonth
    ): Flow<BudgetItemEntry>

    fun getBudgetItemEntry(budgetItemEntryId: Int): Flow<BudgetItemEntry>
    fun updateAssigned(budgetItemEntry: BudgetItemEntry, newValue: BigDecimal)
    fun addBudgetItemEntriesWhenNotExist(budgetItemEntries: List<BudgetItemEntry>)
    fun isBudgetItemEntryExist(budgetItemId: Int, yearMonth: YearMonth): Boolean
    fun addBudgetItemEntries(budgetItemIds: List<Int>, yearMonth: YearMonth)

}
