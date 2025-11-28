package com.ynab.data.dataSource.room

import android.database.sqlite.SQLiteConstraintException
import android.util.Log
import com.ynab.TAG_PREFIX
import com.ynab.data.dataSource.LocalUserDataSource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

private const val TAG = "${TAG_PREFIX}RoomLocalUserDataSource"

@Singleton
class RoomLocalUserDataSource @Inject constructor(
    db: RoomDatabase
): LocalUserDataSource {
    private val userDao = db.userDao()
    private val budgetDao = db.budgetDao()
    private val accountDao = db.accountDao()
    private val transactionDao = db.transactionDao()
    private val categoryDao = db.categoryDao()
    private val budgetItemDao = db.budgetItemDao()
    private val budgetItemEntryDao = db.budgetItemEntryDao()
    
    override suspend fun getPassword(username: String): String? =
        userDao.getPassword(username)

    override suspend fun isUsernameExist(username: String): Boolean =
        userDao.isUsernameExist(username)

    override suspend fun addUser(username: String, password: String): Boolean {
        try {
            userDao.insert(User(username = username, password = password))
            return true
        } catch (e: SQLiteConstraintException) {
            Log.d(TAG,"addUser threw SQLiteConstraintException, likely due to username already exist.")
            return false
        } catch (e: Exception){
            Log.d(TAG,"Unknown error at addUser: ${e.stackTraceToString()}")
            return false
        }
    }

    override suspend fun deleteUser(username: String): Boolean {
        try {
            // Get userId by username
            val userId = userDao.getUserIdByUsername(username) ?: run {
                Log.w(TAG, "User not found: $username")
                return false
            }
            
            // Get all budgets for this user
            val budgets = budgetDao.getBudgetsByUserId(userId)
            
            // For each budget, cascade delete all related data
            budgets.forEach { budget ->
                val budgetId = budget.budgetId
                
                // 1. Get all account IDs for this budget
                val accountIds = accountDao.getAccountIdsByBudgetId(budgetId)
                
                // 2. Delete all transactions for these accounts
                if (accountIds.isNotEmpty()) {
                    transactionDao.deleteTransactionsByAccountIds(accountIds)
                    Log.d(TAG, "Deleted transactions for ${accountIds.size} accounts")
                }
                
                // 3. Delete all accounts for this budget
                accountDao.deleteAccountsByBudgetId(budgetId)
                Log.d(TAG, "Deleted accounts for budget $budgetId")
                
                // 4. Get all category IDs for this budget
                val categoryIds = categoryDao.getCategoryIdsByBudgetId(budgetId)
                
                // 5. Get all budget item IDs for these categories
                if (categoryIds.isNotEmpty()) {
                    val budgetItemIds = budgetItemDao.getBudgetItemIdsByCategoryIds(categoryIds)
                    
                    // 6. Delete all budget item entries for these budget items
                    if (budgetItemIds.isNotEmpty()) {
                        budgetItemEntryDao.deleteBudgetItemEntriesByBudgetItemIds(budgetItemIds)
                        Log.d(TAG, "Deleted budget item entries for ${budgetItemIds.size} budget items")
                    }
                    
                    // 7. Delete all budget items for these categories
                    budgetItemDao.deleteBudgetItemsByCategoryIds(categoryIds)
                    Log.d(TAG, "Deleted budget items for ${categoryIds.size} categories")
                }
                
                // 8. Delete all categories for this budget
                categoryDao.deleteCategoriesByBudgetId(budgetId)
                Log.d(TAG, "Deleted categories for budget $budgetId")
            }
            
            // 9. Delete all budgets for this user
            budgetDao.deleteBudgetsByUserId(userId)
            Log.d(TAG, "Deleted ${budgets.size} budgets for user $username")
            
            // 10. Finally, delete the user
            val deleted = userDao.deleteUser(username) > 0
            if (deleted) {
                Log.d(TAG, "Successfully deleted user $username and all associated data")
            }
            return deleted
        } catch (e: Exception) {
            Log.e(TAG, "Error deleting user and associated data: ${e.stackTraceToString()}")
            return false
        }
    }

    override fun getUserLastBudgetId(username: String): Flow<Int> =
        userDao.getUserLastBudgetId(username)

    override suspend fun updateLastBudgetId(username: String, budgetId: Int): Boolean {
        return try {
            val userId = userDao.getUserIdByUsername(username) ?: return false
            userDao.updateLastBudgetId(userId, budgetId)
            Log.d(TAG, "Updated lastBudgetId to $budgetId for user $username")
            true
        } catch (e: Exception) {
            Log.e(TAG, "Error updating lastBudgetId: ${e.stackTraceToString()}")
            false
        }
    }
}