package com.ynab.data.repository

import java.time.YearMonth

interface BudgetItemEntryRepository {
    /** Add budget item entry.*/
    fun addBudgetItemEntry(budgetItemId: Int, yearMonth: YearMonth)

}
