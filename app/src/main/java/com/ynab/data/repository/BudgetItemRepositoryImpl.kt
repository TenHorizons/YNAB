package com.ynab.data.repository

import com.ynab.data.dataSource.BudgetItemDataSource
import java.time.YearMonth
import javax.inject.Inject

class BudgetItemRepositoryImpl @Inject constructor(
    private val budgetItemDataSource: BudgetItemDataSource
): BudgetItemRepository {
    /**Add new budget items for a single category and YearMonth.*/
    override fun addAll(categoryId: Int, budgetItemNames: List<String>, yearMonth: YearMonth) =
        budgetItemDataSource.addAll(categoryId, budgetItemNames, yearMonth)

    /**Save all data in repository on logout or onDestroy*/
    override fun saveAllData() {
        TODO("Not yet implemented")
    }
}