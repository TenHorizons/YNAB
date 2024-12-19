package com.ynab.domain

import com.ynab.data.repository.dataClass.Account
import kotlinx.coroutines.flow.Flow
import java.math.BigDecimal
import java.time.LocalDate

interface AddTransactionUseCase {
    val accounts: Flow<List<Account>>

    fun addTransaction(
        amount: BigDecimal,
        accountId: Int,
        budgetItemId: Int,
        date: LocalDate,
        memo: String
    ): Boolean
}