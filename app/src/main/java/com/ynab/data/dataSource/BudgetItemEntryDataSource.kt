package com.ynab.data.dataSource

import java.time.YearMonth
import javax.inject.Inject

class BudgetItemEntryDataSource @Inject constructor(
    private val localBudgetItemEntryDataSource: LocalBudgetItemEntryDataSource
) {
    fun addBudgetItemEntry(budgetItemId: Int, yearMonth: YearMonth) =
        localBudgetItemEntryDataSource.addBudgetItemEntry(budgetItemId, yearMonth)

}
