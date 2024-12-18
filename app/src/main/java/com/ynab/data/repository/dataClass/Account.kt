package com.ynab.data.repository.dataClass

import java.math.BigDecimal

data class Account(
    var accountId: Int,
    var accountName: String,
    var uiPosition: Int,
    var balance: BigDecimal,
    var budgetId: Int
)
