package com.ynab.data.repository

import com.ynab.data.dataSource.BudgetItemEntryDataSource
import java.time.YearMonth
import javax.inject.Inject

class BudgetItemEntryRepositoryImpl @Inject constructor(
    private val budgetItemEntryDataSource: BudgetItemEntryDataSource
): BudgetItemEntryRepository {
    /** Add budget item entry.*/
    override fun addBudgetItemEntry(budgetItemId: Int, yearMonth: YearMonth) =
        budgetItemEntryDataSource.addBudgetItemEntry(budgetItemId, yearMonth)
}