package com.ynab.data.dataSource

import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class FakeLocalUserDataSource @Inject constructor(): LocalUserDataSource {
    override suspend fun getPassword(username: String): String? =
        if(username == "Alice") "Alice123" else ""

    override suspend fun isUsernameExist(username: String): Boolean =
        username == "Alice"

    override suspend fun addUser(username: String, password: String): Boolean =
        username != "Alice"

    override suspend fun deleteUser(username: String): Boolean {
        TODO("Not yet implemented")
    }

    override fun getUserLastBudgetId(username: String): Flow<Int> {
        TODO("Not yet implemented")
    }

}