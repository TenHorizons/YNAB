package com.ynab.data.dataSource.room

import com.ynab.data.dataSource.LocalAccountDataSource
import com.ynab.data.repository.dataClass.Account
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class RoomLocalAccountDataSource @Inject constructor(
    db: RoomDatabase
): LocalAccountDataSource {
    private val accountDao = db.accountDao()
    override fun getAccountsByBudgetId(budgetId: Int): Flow<List<Account>> =
        accountDao.getAccountsByBudgetId(budgetId)
}