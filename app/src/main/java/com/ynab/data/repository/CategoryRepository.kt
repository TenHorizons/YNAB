package com.ynab.data.repository

interface CategoryRepository: Repository {
    /**Add new categories for a single budget.*/
    fun addCategories(categoryNames: Set<String>, budgetId: Int)
    /**Get category ID from category name and budget ID.*/
    fun getCategoryId(categoryName: String, budgetId: Int): Int
}