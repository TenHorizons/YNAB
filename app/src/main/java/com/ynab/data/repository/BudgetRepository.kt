package com.ynab.data.repository

interface BudgetRepository {
    /**
     * Creates a new budget for the user and returns the budgetId.
     * Returns -1 if creation fails.
     */
    suspend fun createBudget(username: String, budgetName: String): Int
}