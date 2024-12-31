package com.ynab.data.repository

import java.time.YearMonth

interface BudgetItemRepository: Repository {
    /**Add new budget items for a single category and YearMonth.*/
    fun addBudgetItems(categoryId: Int, budgetItemNames: List<String>, yearMonth: YearMonth): Exception?
    /**Get budget item ID based on name and category ID.*/
    fun getBudgetItemId(budgetItemName: String, categoryId: Int): Int
}