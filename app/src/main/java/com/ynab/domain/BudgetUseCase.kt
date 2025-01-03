package com.ynab.domain

import com.ynab.data.repository.dataClass.BudgetItem
import com.ynab.data.repository.dataClass.BudgetItemEntry
import com.ynab.data.repository.dataClass.Category
import kotlinx.coroutines.flow.Flow
import java.math.BigDecimal
import java.time.YearMonth

interface BudgetUseCase {
    var lastSelectedYearMonth: YearMonth

    /** Get Categories of the currently selected Budget.*/
    fun getBudgetCategories(): Flow<List<Category>>

    /** Get Budget Items for a list of Categories.*/
    fun getBudgetItems(categoryIds: List<Int>): Flow<List<BudgetItem>>

    /** Get Budget Item Entries for the selected YearMonth of a list of Budget Items.*/
    fun getThisYearMonthBudgetItemEntries(budgetItemIds: List<Int>): Flow<List<BudgetItemEntry>>

    /** Get Available for the selected YearMonth.
     * Available (YearMonth) =
     *      Sum of previous YearMonth's BudgetItem.rolloverBalance +
     *      Sum of this YearMonth's BudgetItem.assigned +
     *      Sum of this YearMonth's Transaction.amount*/
    fun getYearMonthAvailable(): Flow<BigDecimal>

    /** Get Available for a Budget Item.
     * Available (Budget Item) =
     *      Previous YearMonth's BudgetItem.rolloverBalance +
     *      This YearMonth's BudgetItem.assigned +
     *      This YearMonth's Transaction.amount where BudgetItem.budgetItemId = Transaction.budgetItemId*/
    fun getBudgetItemEntryAvailable(): Flow<Map<Int, BigDecimal>>

    /** Get Available for a Category.
     * Available (Category) =
     *      Sum of previous YearMonth's BudgetItem.rolloverBalance where BudgetItem.categoryId = Category.categoryId +
     *      Sum of this YearMonth's BudgetItem.assigned where BudgetItem.categoryId = Category.categoryId +
     *      Sum of this YearMonth's Transaction.amount where Transaction.budgetItemId = BudgetItem.budgetItemId AND BudgetItem.budgetItemId = Category.categoryId*/
    fun getCategoryAvailable(): Flow<Map<Int, BigDecimal>>

    /** Update the assigned value of a Budget Item.*/
    fun updateAssigned(budgetItemEntry: BudgetItemEntry, newValue: BigDecimal)
}