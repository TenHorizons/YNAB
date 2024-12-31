package com.ynab.data.repository

import java.time.YearMonth

interface BudgetItemRepository: Repository {
    /**Add new budget items for a single category and YearMonth.*/
    fun addAll(categoryId: Int, budgetItemNames: List<String>, yearMonth: YearMonth): Exception?
}