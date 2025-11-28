package com.ynab.data.repository

import com.ynab.data.dataSource.BudgetItemDataSource
import com.ynab.data.repository.dataClass.BudgetItem
import kotlinx.coroutines.flow.Flow
import java.time.YearMonth
import javax.inject.Inject

class BudgetItemRepositoryImpl @Inject constructor(
    private val budgetItemDataSource: BudgetItemDataSource
): BudgetItemRepository {
    /**Add new budget items for a single category and YearMonth.*/
    override fun addBudgetItems(categoryId: Int, budgetItemNames: List<String>, yearMonth: YearMonth) =
        budgetItemDataSource.addAll(categoryId, budgetItemNames, yearMonth)

    /**Get budget item ID based on name and category ID.*/
    override fun getBudgetItemId(budgetItemName: String, categoryId: Int): Int =
        budgetItemDataSource.getBudgetItemId(budgetItemName, categoryId)

    /**Get budget items for a list of categories.*/
    override fun getBudgetItems(categoryIds: List<Int>): Flow<List<BudgetItem>> =
        budgetItemDataSource.getBudgetItems(categoryIds)

    /**Get budget item IDs for a list of categories.*/
    override fun getBudgetItemIds(categoryIds: List<Int>): Flow<List<Int>> =
        budgetItemDataSource.getBudgetItemIds(categoryIds)

    /**Save all data in repository on logout or onDestroy*/
    override fun saveAllData() {
        TODO("Not yet implemented")
    }
}