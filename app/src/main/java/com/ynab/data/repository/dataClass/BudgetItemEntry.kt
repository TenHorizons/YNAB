package com.ynab.data.repository.dataClass

import java.math.BigDecimal
import java.time.YearMonth

data class BudgetItemEntry(
    val budgetItemEntryId: Int,
    var budgetItemId: Int,
    var yearMonth: YearMonth,
    var assigned: BigDecimal,
    var rolloverBalance: BigDecimal
)
