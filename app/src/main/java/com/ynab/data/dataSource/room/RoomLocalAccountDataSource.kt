package com.ynab.data.dataSource.room

import android.database.sqlite.SQLiteConstraintException
import android.util.Log
import com.ynab.TAG_PREFIX
import com.ynab.data.dataSource.LocalAccountDataSource
import com.ynab.data.repository.dataClass.Account
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.math.BigDecimal
import javax.inject.Inject

private const val TAG = "${TAG_PREFIX}RoomLocalAccountDataSource"

class RoomLocalAccountDataSource @Inject constructor(
    db: RoomDatabase
) : LocalAccountDataSource {
    private val accountDao = db.accountDao()

    //Convert Room Account to Ui Account to maintain clean separation of UI and data layers.
    override fun getAccountsByBudgetId(budgetId: Int): Flow<List<Account>> =
        accountDao.getAccountsByBudgetId(budgetId).map { accountList ->
            accountList.map { roomAccount -> roomAccount.toUiAccount() }
        }

    override fun isAccountNameExist(accountName: String, budgetId: Int): Boolean =
        accountDao.isAccountNameExist(accountName, budgetId)

    override fun addAccount(
        accountName: String,
        accountBalance: BigDecimal,
        budgetId: Int
    ): Boolean {
        try {
            val newAccount = Account(
                budgetId = budgetId,
                accountName = accountName,
                uiPosition = accountDao.getNumberOfAccountsByBudgetId(budgetId),
                balance = accountBalance
            )
            accountDao.insert(newAccount)
            return true
        } catch (e: SQLiteConstraintException) {
            Log.d(
                TAG,
                "addAccount threw SQLiteConstraintException, likely due to account already exist."
            )
            return false
        } catch (e: Exception) {
            Log.e(TAG, "Unknown error at addAccount: ${e.stackTraceToString()}")
            return false
        }
    }

    override fun updateAccount(accountToEdit: Account, newAccountName: String): Boolean {
        try {
            return accountDao.update(
                accountToEdit.copy(accountName = newAccountName).toRoomAccount()
            ) == 1
        } catch (e: SQLiteConstraintException) {
            Log.d(TAG, "updateAccount threw SQLiteConstraintException.")
            return false
        } catch (e: Exception) {
            Log.e(TAG, "Unknown error at updateAccount: ${e.stackTraceToString()}")
            return false
        }
    }

    override fun deleteAccount(account: Account) =
        accountDao.delete(account.toRoomAccount())

    override fun getAccountById(accountId: Int): Account? {
        try {
            return accountDao.getAccountById(accountId)?.toUiAccount()
        } catch (e: SQLiteConstraintException) {
            Log.d(TAG, "updateAccount threw SQLiteConstraintException.")
            return null
        } catch (e: Exception) {
            Log.e(TAG, "Unknown error at updateAccount: ${e.stackTraceToString()}")
            return null
        }
    }

    private fun com.ynab.data.dataSource.room.Account.toUiAccount(): Account =
        Account(
            accountId = accountId,
            budgetId = budgetId,
            accountName = accountName,
            uiPosition = uiPosition,
            balance = balance
        )

    private fun Account.toRoomAccount(): com.ynab.data.dataSource.room.Account =
        com.ynab.data.dataSource.room.Account(
            accountId = accountId,
            budgetId = budgetId,
            accountName = accountName,
            uiPosition = uiPosition,
            balance = balance
        )
}