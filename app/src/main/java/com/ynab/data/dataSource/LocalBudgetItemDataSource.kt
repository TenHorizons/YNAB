package com.ynab.data.dataSource

import java.time.YearMonth

interface LocalBudgetItemDataSource {
    /**Add new budget items for a single category and YearMonth.*/
    fun addBudgetItems(categoryId: Int, budgetItemNames: List<String>, yearMonth: YearMonth): Exception?

}
