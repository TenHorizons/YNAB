package com.ynab.data.repository

interface UserRepository: Repository {
    suspend fun getUserPassword(username: String): String
    suspend fun isUsernameExist(username: String): Boolean
    /**returns false if fail due to username exist, else returns true or throws exception.*/
    suspend fun addUser(username: String, password: String): Boolean
    fun setSessionUsername(username: String): Boolean
}