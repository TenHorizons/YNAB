package com.ynab.data.dataSource

import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Class to manage interaction between remote and local data sources. (WIP)*/
class UserDataSource @Inject constructor(
    private val localUserDataSource: LocalUserDataSource
) {

    suspend fun getPassword(username: String): String? =
        localUserDataSource.getPassword(username)
    suspend fun isUsernameExist(username: String): Boolean =
        localUserDataSource.isUsernameExist(username)
    suspend fun addUser(username: String, password: String): Boolean =
        localUserDataSource.addUser(username, password)
    suspend fun deleteUser(username: String): Boolean =
        localUserDataSource.deleteUser(username)
    fun getUserLastBudgetId(username: String): Flow<Int> =
        localUserDataSource.getUserLastBudgetId(username)
}