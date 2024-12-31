package com.ynab.data.repository

import com.ynab.data.dataSource.CategoryDataSource
import javax.inject.Inject

class CategoryRepositoryImpl @Inject constructor(
    private val categoryDataSource: CategoryDataSource
): CategoryRepository {
    /**Add new categories for a single budget.*/
    override fun addCategories(categoryNames: Set<String>, budgetId: Int) =
        categoryDataSource.addCategories(categoryNames,budgetId)

    /**Get category ID from category name and budget ID.*/
    override fun getCategoryId(categoryName: String, budgetId: Int): Int =
        categoryDataSource.getCategoryId(categoryName,budgetId)

    /**Save all data in repository on logout or onDestroy*/
    override fun saveAllData() {
        TODO("Not yet implemented")
    }
}