package com.ynab.data.dataSource

import kotlinx.coroutines.flow.Flow

interface LocalUserDataSource {

    /**Returns user password as String if found. Else returns empty string.*/
    suspend fun getPassword(username: String): String?

    /**Checks if username exists, returns false if no.*/
    suspend fun isUsernameExist(username: String): Boolean

    /**Adds a single user. Returns false if failed to add.
     * Failure to add is usually due to constraint conflict, such as username already exist*/
    suspend fun addUser(username: String, password: String): Boolean
    suspend fun deleteUser(username: String): Boolean
    fun getUserLastBudgetId(username: String): Flow<Int>
}