package com.ynab.data.repository.dataClass

data class BudgetItem(
    val budgetItemId: Int,
    var categoryId: Int,
    var budgetItemName: String,
    var itemUiPosition: Int
)
