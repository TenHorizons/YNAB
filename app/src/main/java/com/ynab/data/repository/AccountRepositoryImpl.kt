package com.ynab.data.repository

import android.util.Log
import com.ynab.TAG_PREFIX
import com.ynab.data.dataSource.AccountDataSource
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import java.math.BigDecimal
import javax.inject.Inject

private const val TAG = "${TAG_PREFIX}AccountRepositoryImpl"

class AccountRepositoryImpl @Inject constructor(
    private val accountDs: AccountDataSource,
    private val userRepository: UserRepository
) : AccountRepository {

    @OptIn(ExperimentalCoroutinesApi::class)
    override val accounts =
        userRepository.getUserLastBudgetId().flatMapLatest { budgetId ->
            accountDs.getAccountsByBudgetId(budgetId).distinctUntilChanged()
        }

    override suspend fun isAccountNameExist(accountName: String): Boolean =
        accountDs.isAccountNameExist(accountName, userRepository.getUserLastBudgetId().first())

    override suspend fun addAccount(accountName: String, accountBalance: BigDecimal): Boolean {
        if(isAccountNameExist(accountName)) return false
        try{
            return accountDs.addAccount(accountName, accountBalance, userRepository.getUserLastBudgetId().first())
        }catch (e: Exception) {
            Log.e(TAG, "An unknown error occurred at addAccount: ${e.stackTrace}")
            throw e
        }
    }

    override fun saveAllData() {
        TODO("Not yet implemented")
    }
}