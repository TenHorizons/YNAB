package com.ynab.domain

import com.ynab.data.repository.AccountRepository
import com.ynab.data.repository.UserRepository
import com.ynab.data.repository.dataClass.Account
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class AddTransactionUseCaseImpl @Inject constructor(
    private val userRepository: UserRepository,
    private val accountRepository: AccountRepository
): AddTransactionUseCase {
    override val accounts: Flow<List<Account>> = accountRepository.accounts
}