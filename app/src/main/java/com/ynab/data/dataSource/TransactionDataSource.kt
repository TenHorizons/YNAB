package com.ynab.data.dataSource

import java.math.BigDecimal
import java.time.LocalDate
import javax.inject.Inject

class TransactionDataSource @Inject constructor(
    private val localTransactionDataSource: LocalTransactionDataSource
) {
    fun addTransaction(
        amount: BigDecimal,
        accountId: Int,
        budgetItemId: Int,
        date: LocalDate,
        memo: String
    ): Boolean =
        localTransactionDataSource.addTransaction(
            amount = amount,
            accountId = accountId,
            budgetItemId = budgetItemId,
            date = date,
            memo = memo
        )

}
