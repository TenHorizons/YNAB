package com.ynab.domain

interface BasicAuthUseCase {
    suspend fun verifyUser(username: String, password: String): Boolean
    suspend fun isUsernameExist(username: String): Boolean
    suspend fun registerUser(username: String,password: String): Boolean
}