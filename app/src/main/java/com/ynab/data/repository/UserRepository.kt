package com.ynab.data.repository

import kotlinx.coroutines.flow.Flow

interface UserRepository: Repository {
    suspend fun getUserPassword(username: String): String
    suspend fun isUsernameExist(username: String): Boolean
    /**returns false if fail due to username exist, else returns true or throws exception.*/
    suspend fun addUser(username: String, password: String): Boolean
    fun setSessionUsername(username: String): Boolean
    fun getSessionUsername(): String
    fun getUserLastBudgetId(): Flow<Int>
    suspend fun deleteUser(): Boolean
}