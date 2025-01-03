package com.ynab.domain

import com.ynab.data.repository.AccountRepository
import com.ynab.data.repository.BudgetItemRepository
import com.ynab.data.repository.CategoryRepository
import com.ynab.data.repository.TransactionRepository
import com.ynab.data.repository.UserRepository
import com.ynab.data.repository.dataClass.Account
import com.ynab.data.repository.dataClass.BudgetItem
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import java.math.BigDecimal
import java.time.LocalDate
import javax.inject.Inject

class AddTransactionUseCaseImpl @Inject constructor(
    accountRepository: AccountRepository,
    userRepository: UserRepository,
    categoryRepository: CategoryRepository,
    budgetItemRepository: BudgetItemRepository,
    private val transactionRepository: TransactionRepository
): AddTransactionUseCase {
    override val accounts: Flow<List<Account>> = accountRepository.accounts
    @OptIn(ExperimentalCoroutinesApi::class)
    override val budgetItems: Flow<List<BudgetItem>> =
        userRepository.getUserLastBudgetId().flatMapLatest { budgetId ->
            categoryRepository.getCategories(budgetId).flatMapLatest { categories ->
                budgetItemRepository.getBudgetItems(categories.map { it.categoryId })
            }
        }

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