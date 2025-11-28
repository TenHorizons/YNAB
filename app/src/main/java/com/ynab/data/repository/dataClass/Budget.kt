package com.ynab.data.repository.dataClass

data class Budget(
    val budgetId: Int,
    val userId: Int,
    var budgetName: String,
    var uiPosition: Int
)
