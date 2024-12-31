package com.ynab.data.dataSource

import java.time.YearMonth

interface LocalBudgetItemEntryDataSource {
    /** Add budget item entry.*/
    fun addBudgetItemEntry(budgetItemId: Int, yearMonth: YearMonth)

}
