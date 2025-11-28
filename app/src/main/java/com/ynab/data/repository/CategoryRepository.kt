package com.ynab.data.repository

import com.ynab.data.repository.dataClass.Category
import kotlinx.coroutines.flow.Flow

interface CategoryRepository: Repository {
    /**Add new categories for a single budget.*/
    fun addCategories(categoryNames: Set<String>, budgetId: Int)
    /**Get category ID from category name and budget ID.*/
    fun getCategoryId(categoryName: String, budgetId: Int): Int
    /**Get categories for a selected budget.*/
    fun getCategories(budgetId: Int): Flow<List<Category>>
}