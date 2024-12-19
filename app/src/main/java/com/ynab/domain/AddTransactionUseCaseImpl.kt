package com.ynab.domain

import com.ynab.data.repository.AccountRepository
import com.ynab.data.repository.TransactionRepository
import com.ynab.data.repository.dataClass.Account
import kotlinx.coroutines.flow.Flow
import java.math.BigDecimal
import java.time.LocalDate
import javax.inject.Inject

class AddTransactionUseCaseImpl @Inject constructor(
    accountRepository: AccountRepository,
    private val transactionRepository: TransactionRepository
): AddTransactionUseCase {
    override val accounts: Flow<List<Account>> = accountRepository.accounts

    override fun addTransaction(
        amount: BigDecimal,
        accountId: Int,
        budgetItemId: Int,
        date: LocalDate,
        memo: String
    ): Boolean =
        transactionRepository.addTransaction(
            amount = amount,
            accountId = accountId,
            budgetItemId = budgetItemId,
            date = date,
            memo = memo)
}