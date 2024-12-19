package com.ynab.data.repository

import java.math.BigDecimal
import java.time.LocalDate

interface TransactionRepository {
    fun addTransaction(
        amount: BigDecimal,
        accountId: Int,
        budgetItemId: Int,
        date: LocalDate,
        memo: String
    ): Boolean

}
