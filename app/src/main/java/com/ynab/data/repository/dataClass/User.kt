package com.ynab.data.repository.dataClass

data class User(
    val userId: Int,
    var username: String,
    var password: String,
    var lastBudgetId: Int
)
