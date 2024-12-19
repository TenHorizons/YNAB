package com.ynab.data.repository.dataClass

import java.math.BigDecimal
import java.time.LocalDate

data class Transaction(
    val transactionId: Int,
    var accountId: Int,
    var budgetItemId: Int,
    var date: LocalDate,
    var amount: BigDecimal,
    var memo: String
)
