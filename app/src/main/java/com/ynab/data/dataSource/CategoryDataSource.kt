package com.ynab.data.dataSource

import javax.inject.Inject

class CategoryDataSource @Inject constructor(
    private val localCategoryDataSource: LocalCategoryDataSource
) {
    /**Add new categories for a single budget.*/
    fun addCategories(categoryNames: Set<String>, budgetId: Int) =
        localCategoryDataSource.addCategories(categoryNames,budgetId)

    /**Get category ID from category name and budget ID.*/
    fun getCategoryId(categoryName: String, budgetId: Int): Int =
        localCategoryDataSource.getCategoryId(categoryName,budgetId)

}
