package com.ynab.data.repository.dataClass

import java.math.BigDecimal
import java.time.YearMonth

data class BudgetItem(
    val budgetItemId: Int,
    var categoryId: Int,
    var budgetItemName: String,
    var itemUiPosition: Int,
    var yearMonth: YearMonth,
    var assigned: BigDecimal,
    var rolloverBalance: BigDecimal
)
