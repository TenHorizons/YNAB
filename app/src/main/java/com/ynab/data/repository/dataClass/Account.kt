package com.ynab.data.repository.dataClass

data class Account(
    var accountId: Int,
    var accountName: String,
    var uiPosition: Int,
    var budgetId: Int
)
