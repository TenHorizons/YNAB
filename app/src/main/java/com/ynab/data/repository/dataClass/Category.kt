package com.ynab.data.repository.dataClass

data class Category(
    val categoryId: Int,
    val budgetId: Int,
    var categoryName: String,
    var categoryUiPosition: Int
)
