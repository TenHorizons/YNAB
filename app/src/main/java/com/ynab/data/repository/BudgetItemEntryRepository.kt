package com.ynab.data.repository

import com.ynab.data.repository.dataClass.BudgetItemEntry
import kotlinx.coroutines.flow.Flow
import java.math.BigDecimal
import java.time.YearMonth

interface BudgetItemEntryRepository {
    /** Add budget item entry.*/
    fun addBudgetItemEntry(budgetItemId: Int, yearMonth: YearMonth)
    fun addBudgetItemEntries(budgetItemIds: List<Int>, yearMonth: YearMonth)
    fun getBudgetItemEntries(
        budgetItemIds: List<Int>,
        yearMonth: YearMonth
    ): Flow<List<BudgetItemEntry>>

    /** Get budget item entry.*/
    fun getBudgetItemEntry(budgetItemId: Int, yearMonth: YearMonth): Flow<BudgetItemEntry>

    /** Get budget item entry.*/
    fun getBudgetItemEntry(budgetItemEntryId: Int): Flow<BudgetItemEntry>

    /** Update Budget Item Entry Assigned.*/
    fun updateAssigned(budgetItemEntry: BudgetItemEntry, newValue: BigDecimal)
    fun isBudgetItemEntryExist(budgetItemId: Int, yearMonth: YearMonth): Boolean

}
