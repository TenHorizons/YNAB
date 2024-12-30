package com.ynab.ui.budget

import com.ynab.data.repository.dataClass.Category
import com.ynab.data.repository.dataClass.BudgetItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flowOf
import java.time.YearMonth

data class BudgetState(
    val lastSelectedYearMonth: Flow<YearMonth> = flowOf(YearMonth.now()),
    val categories: Flow<List<Category>> = emptyFlow(),
    val budgetItems: Flow<List<BudgetItem>> = emptyFlow()
)
