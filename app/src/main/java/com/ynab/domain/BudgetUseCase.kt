package com.ynab.domain

import com.ynab.data.repository.dataClass.BudgetItem
import com.ynab.data.repository.dataClass.BudgetItemEntry
import com.ynab.data.repository.dataClass.Category
import kotlinx.coroutines.flow.Flow
import java.math.BigDecimal
import java.time.YearMonth

interface BudgetUseCase {
    /** Get Categories of the currently selected Budget.*/
    fun getBudgetCategories(): Flow<List<Category>>
    /** Get Budget Items for a YearMonth of a list of Categories.*/
    fun getBudgetItems(categoryIds: List<Int>): Flow<List<BudgetItem>>
    /** Get Budget Item Entries for a YearMonth of a list of Budget Items.*/
    fun getBudgetItemEntries(map: List<Int>, yearMonth: YearMonth): Flow<List<BudgetItemEntry>>
    /** Get Available for a YearMonth.
     * Available (YearMonth) =
     *      Sum of previous YearMonth's BudgetItem.rolloverBalance +
     *      Sum of this YearMonth's BudgetItem.assigned +
     *      Sum of this YearMonth's Transaction.amount*/
    fun getAvailable(yearMonth: YearMonth): Flow<BigDecimal>
    /** Get Available for a Budget Item.
     * Available (Budget Item =
     *      Previous YearMonth's BudgetItem.rolloverBalance +
     *      This YearMonth's BudgetItem.assigned +
     *      This YearMonth's Transaction.amount where BudgetItem.budgetItemId = Transaction.budgetItemId*/
    fun getAvailable(budgetItem: BudgetItem): Flow<BigDecimal>
    /** Get Available for a Category.
     * Available (Category) =
     *      Sum of previous YearMonth's BudgetItem.rolloverBalance where BudgetItem.categoryId = Category.categoryId +
     *      Sum of this YearMonth's BudgetItem.assigned where BudgetItem.categoryId = Category.categoryId +
     *      Sum of this YearMonth's Transaction.amount where Transaction.budgetItemId = BudgetItem.budgetItemId AND BudgetItem.budgetItemId = Category.categoryId*/
    fun getAvailable(category: Category): Flow<BigDecimal>
    /** Update the assigned value of a Budget Item.*/
    fun updateAssigned(budgetItem: BudgetItem, newValue: BigDecimal)
}