package com.ynab.data.dataSource

import com.ynab.data.dataSource.LocalUserDataSource
import javax.inject.Inject

class UserDataSource @Inject constructor(
    private val localUserDataSource: LocalUserDataSource
) {

    suspend fun getPassword(username: String): String? = localUserDataSource.getPassword(username)
    suspend fun isUsernameExist(username: String): Boolean = localUserDataSource.isUsernameExist(username)
    suspend fun addUser(username: String, password: String): Boolean = localUserDataSource.addUser(username, password)
    suspend fun deleteUser(username: String): Boolean = localUserDataSource.deleteUser(username)
}