package com.ynab.data.repository

import com.ynab.data.dataSource.BudgetItemEntryDataSource
import com.ynab.data.repository.dataClass.BudgetItemEntry
import kotlinx.coroutines.flow.Flow
import java.math.BigDecimal
import java.time.YearMonth
import javax.inject.Inject

class BudgetItemEntryRepositoryImpl @Inject constructor(
    private val budgetItemEntryDataSource: BudgetItemEntryDataSource
): BudgetItemEntryRepository {
    /** Add budget item entry.*/
    override fun addBudgetItemEntry(budgetItemId: Int, yearMonth: YearMonth) =
        budgetItemEntryDataSource.addBudgetItemEntry(budgetItemId, yearMonth)

    override fun addBudgetItemEntries(budgetItemIds: List<Int>, yearMonth: YearMonth) =
        budgetItemEntryDataSource.addBudgetItemEntries(budgetItemIds, yearMonth)

    override fun getBudgetItemEntries(
        budgetItemIds: List<Int>,
        yearMonth: YearMonth
    ): Flow<List<BudgetItemEntry>> =
        budgetItemEntryDataSource.getBudgetItemEntries(budgetItemIds, yearMonth)

    /** Get budget item entry.*/
    override fun getBudgetItemEntry(
        budgetItemId: Int,
        yearMonth: YearMonth
    ): Flow<BudgetItemEntry> =
        budgetItemEntryDataSource.getBudgetItemEntry(budgetItemId, yearMonth)

    /** Get budget item entry.*/
    override fun getBudgetItemEntry(budgetItemEntryId: Int): Flow<BudgetItemEntry> =
        budgetItemEntryDataSource.getBudgetItemEntry(budgetItemEntryId)

    /** Update Budget Item Entry Assigned.*/
    override fun updateAssigned(budgetItemEntry: BudgetItemEntry, newValue: BigDecimal) =
        budgetItemEntryDataSource.updateAssigned(budgetItemEntry, newValue)

    override fun isBudgetItemEntryExist(budgetItemId: Int, yearMonth: YearMonth): Boolean =
        budgetItemEntryDataSource.isBudgetItemEntryExist(budgetItemId, yearMonth)
}