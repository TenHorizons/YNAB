package com.ynab.data.dataSource

import java.math.BigDecimal
import java.time.LocalDate

interface LocalTransactionDataSource {
    fun addTransaction(
        amount: BigDecimal,
        accountId: Int,
        budgetItemId: Int,
        date: LocalDate,
        memo: String
    ): Boolean

}
