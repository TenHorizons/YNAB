package com.ynab.data.repository

import android.util.Log
import com.ynab.TAG_PREFIX
import com.ynab.data.dataSource.UserDataSource
import javax.inject.Inject

private const val TAG = "${TAG_PREFIX}UserRepository"

class UserRepositoryImpl @Inject constructor(
    private val userDs: UserDataSource
): UserRepository {
    private var sessionUsername: String = ""

    override suspend fun getUserPassword(username: String): String =
        userDs.getPassword(username) ?: ""
    override suspend fun isUsernameExist(username: String): Boolean =
        userDs.isUsernameExist(username)
    /**returns false if fail due to username exist, else returns true or throws exception.*/
    override suspend fun addUser(username: String, password: String): Boolean {
        if(userDs.isUsernameExist(username)) return false
        try {
            userDs.addUser(username, password)
            return true
        }catch (e: Exception) {
            Log.e(TAG, "An unknown error occurred at addUser: ${e.stackTrace}")
            throw e
        }
    }

    override fun setSessionUsername(username: String): Boolean {
        sessionUsername = username
        return true
    }

    override fun saveAllData() {
        TODO("Not yet implemented")
    }
}