package com.ynab.data.repository

import android.util.Log
import com.ynab.TAG_PREFIX
import com.ynab.data.dataSource.room.Budget
import com.ynab.data.dataSource.room.RoomDatabase
import javax.inject.Inject

private const val TAG = "${TAG_PREFIX}BudgetRepository"

class BudgetRepositoryImpl @Inject constructor(
    private val db: RoomDatabase
): BudgetRepository {
    private val budgetDao = db.budgetDao()
    private val userDao = db.userDao()
    
    override suspend fun createBudget(username: String, budgetName: String): Int {
        return try {
            // Get userId from username
            val userId = userDao.getUserIdByUsername(username) ?: run {
                Log.e(TAG, "User not found: $username")
                return -1
            }
            
            // Get the count of existing budgets to determine position
            val existingBudgets = budgetDao.getBudgetsByUserId(userId)
            val position = existingBudgets.size
            
            // Create the budget entity
            val budget = Budget(
                userId = userId,
                budgetName = budgetName,
                uiPosition = position
            )
            
            // Insert and get the generated budgetId
            val budgetId = budgetDao.insert(budget)
            
            Log.d(TAG, "Created budget '$budgetName' with ID: $budgetId for user: $username")
            budgetId.toInt()
        } catch (e: Exception) {
            Log.e(TAG, "Error creating budget: ${e.stackTraceToString()}")
            -1
        }
    }
}