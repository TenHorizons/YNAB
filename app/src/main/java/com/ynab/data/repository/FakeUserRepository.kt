package com.ynab.data.repository

import javax.inject.Inject

class FakeUserRepository @Inject constructor(): UserRepository {
    override suspend fun getUserPassword(username: String): String =
        if(username == "Alice") "Alice123" else ""

    override suspend fun isUsernameExist(username: String): Boolean =
        username == "Alice"

    override suspend fun addUser(username: String, password: String): Boolean =
        username != "Alice"

    override fun saveAllData() =
        TODO("Not yet implemented")
}