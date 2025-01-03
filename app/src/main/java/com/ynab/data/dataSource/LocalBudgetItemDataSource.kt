package com.ynab.data.dataSource

import com.ynab.data.repository.dataClass.BudgetItem
import kotlinx.coroutines.flow.Flow
import java.time.YearMonth

interface LocalBudgetItemDataSource {
    /**Add new budget items for a single category and YearMonth.*/
    fun addBudgetItems(categoryId: Int, budgetItemNames: List<String>, yearMonth: YearMonth): Exception?
    /**Get budget item ID based on name and category ID.*/
    fun getBudgetItemId(budgetItemName: String, categoryId: Int): Int
    fun getBudgetItems(categoryIds: List<Int>): Flow<List<BudgetItem>>
    fun getBudgetItemIds(categoryIds: List<Int>): Flow<List<Int>>

}
