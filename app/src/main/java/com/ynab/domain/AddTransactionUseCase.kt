package com.ynab.domain

import com.ynab.data.repository.dataClass.Account
import com.ynab.data.repository.dataClass.BudgetItem
import kotlinx.coroutines.flow.Flow
import java.math.BigDecimal
import java.time.LocalDate

interface AddTransactionUseCase {
    val accounts: Flow<List<Account>>
    val budgetItems: Flow<List<BudgetItem>>

    fun addTransaction(
        amount: BigDecimal,
        accountId: Int,
        budgetItemId: Int,
        date: LocalDate,
        memo: String
    ): Boolean
}