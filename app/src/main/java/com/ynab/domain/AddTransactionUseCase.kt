package com.ynab.domain

import com.ynab.data.repository.dataClass.Account
import kotlinx.coroutines.flow.Flow

interface AddTransactionUseCase {
    val accounts: Flow<List<Account>>
}