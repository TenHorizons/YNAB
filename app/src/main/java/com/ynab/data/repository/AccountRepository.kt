package com.ynab.data.repository

import com.ynab.data.repository.dataClass.Account
import kotlinx.coroutines.flow.Flow

interface AccountRepository {
    val accounts: Flow<List<Account>>
}