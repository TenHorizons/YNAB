package com.ynab.data.dataSource

import java.time.YearMonth
import javax.inject.Inject

class BudgetItemDataSource @Inject constructor(
    private val localBudgetItemDataSource: LocalBudgetItemDataSource
) {
    /**Add new budget items for a single category and YearMonth.*/
    fun addAll(categoryId: Int, budgetItemNames: List<String>, yearMonth: YearMonth): Exception? =
        localBudgetItemDataSource.addBudgetItems(categoryId, budgetItemNames, yearMonth)
}
