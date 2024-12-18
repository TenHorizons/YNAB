package com.ynab.data.repository

import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class FakeUserRepository @Inject constructor(): UserRepository {
    override suspend fun getUserPassword(username: String): String =
        if(username == "Alice") "Alice123" else ""

    override suspend fun isUsernameExist(username: String): Boolean =
        username == "Alice"

    override suspend fun addUser(username: String, password: String): Boolean =
        username != "Alice"

    override fun setSessionUsername(username: String): Boolean {
        TODO("Not yet implemented")
    }

    override fun getSessionUsername(): String {
        TODO("Not yet implemented")
    }

    override fun getUserLastBudgetId(): Flow<Int> {
        TODO("Not yet implemented")
    }

    override suspend fun deleteUser(): Boolean {
        TODO("Not yet implemented")
    }

    override fun setSelectedBudgetId(budgetId: Int) {
        TODO("Not yet implemented")
    }

    override fun saveAllData() =
        TODO("Not yet implemented")
}