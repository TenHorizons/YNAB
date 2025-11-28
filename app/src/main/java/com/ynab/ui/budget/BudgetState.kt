package com.ynab.ui.budget

import com.ynab.data.repository.dataClass.BudgetItem
import com.ynab.data.repository.dataClass.BudgetItemEntry
import com.ynab.data.repository.dataClass.Category
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import java.math.BigDecimal
import java.time.YearMonth

data class BudgetState(
    val lastSelectedYearMonth: YearMonth = YearMonth.now(),
    val categories: Flow<List<Category>> = emptyFlow(),
    val budgetItems: Flow<List<BudgetItem>> = emptyFlow(),
    val budgetItemEntries: Flow<List<BudgetItemEntry>> = emptyFlow(),
    val yearMonthAvailable: Flow<BigDecimal> = emptyFlow(),
    val categoryAvailable: Flow<Map<Int, BigDecimal>> = emptyFlow(),
    val budgetItemEntryAvailable: Flow<Map<Int, BigDecimal>> = emptyFlow()
)
