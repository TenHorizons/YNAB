package com.ynab.data.dataSource

import java.time.YearMonth
import javax.inject.Inject

class BudgetItemDataSource @Inject constructor(
    private val localBudgetItemDataSource: LocalBudgetItemDataSource
) {
    /**Add new budget items for a single category and YearMonth.*/
    fun addAll(categoryId: Int, budgetItemNames: List<String>, yearMonth: YearMonth): Exception? =
        localBudgetItemDataSource.addBudgetItems(categoryId, budgetItemNames, yearMonth)

    /**Get budget item ID based on name and category ID.*/
    fun getBudgetItemId(budgetItemName: String, categoryId: Int): Int =
        localBudgetItemDataSource.getBudgetItemId(budgetItemName, categoryId)
}
