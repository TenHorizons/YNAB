package com.ynab.domain

import com.ynab.data.repository.dataClass.BudgetItem
import com.ynab.data.repository.dataClass.Category
import kotlinx.coroutines.flow.Flow
import java.math.BigDecimal
import java.time.YearMonth
import javax.inject.Inject

class BudgetUseCaseImpl @Inject constructor(

): BudgetUseCase {
    /** Get Categories of the currently selected Budget.*/
    override fun getBudgetCategories(): Flow<List<Category>> {
        TODO("Not yet implemented")
    }

    /** Get Budget Items for a YearMonth of a list of Categories.*/
    override fun getBudgetItems(
        categoryIds: List<Int>,
        yearMonth: YearMonth
    ): Flow<List<BudgetItem>> {
        TODO("Not yet implemented")
    }

    /** Get Available for a YearMonth.
     * Available (YearMonth) =
     *      Sum of previous YearMonth's BudgetItem.rolloverBalance +
     *      Sum of this YearMonth's BudgetItem.assigned +
     *      Sum of this YearMonth's Transaction.amount*/
    override fun getAvailable(yearMonth: YearMonth): Flow<BigDecimal> {
        TODO("Not yet implemented")
    }

    /** Get Available for a Budget Item.
     * Available (Budget Item =
     *      Previous YearMonth's BudgetItem.rolloverBalance +
     *      This YearMonth's BudgetItem.assigned +
     *      This YearMonth's Transaction.amount where BudgetItem.budgetItemId = Transaction.budgetItemId*/
    override fun getAvailable(budgetItem: BudgetItem): Flow<BigDecimal> {
        TODO("Not yet implemented")
    }

    /** Get Available for a Category.
     * Available (Category) =
     *      Sum of previous YearMonth's BudgetItem.rolloverBalance where BudgetItem.categoryId = Category.categoryId +
     *      Sum of this YearMonth's BudgetItem.assigned where BudgetItem.categoryId = Category.categoryId +
     *      Sum of this YearMonth's Transaction.amount where Transaction.budgetItemId = BudgetItem.budgetItemId AND BudgetItem.budgetItemId = Category.categoryId*/
    override fun getAvailable(category: Category): Flow<BigDecimal> {
        TODO("Not yet implemented")
    }

    /** Update the assigned value of a Budget Item.*/
    override fun updateAssigned(budgetItem: BudgetItem, newValue: BigDecimal) {
        TODO("Not yet implemented")
    }
}