package com.ynab.data.repository

import com.ynab.data.repository.dataClass.BudgetItem
import kotlinx.coroutines.flow.Flow
import java.time.YearMonth

interface BudgetItemRepository: Repository {
    /**Add new budget items for a single category and YearMonth.*/
    fun addBudgetItems(categoryId: Int, budgetItemNames: List<String>, yearMonth: YearMonth): Exception?
    /**Get budget item ID based on name and category ID.*/
    fun getBudgetItemId(budgetItemName: String, categoryId: Int): Int
    /**Get budget items for a list of categories.*/
    fun getBudgetItems(categoryIds: List<Int>): Flow<List<BudgetItem>>
    /**Get budget item IDs for a list of categories.*/
    fun getBudgetItemIds(categoryIds: List<Int>): Flow<List<Int>>
}