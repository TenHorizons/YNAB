package com.ynab.data.repository

import android.util.Log
import com.ynab.TAG_PREFIX
import com.ynab.data.dataSource.UserDataSource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

private const val TAG = "${TAG_PREFIX}UserRepository"

@Singleton
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
            return userDs.addUser(username, password)
        }catch (e: Exception) {
            Log.e(TAG, "An unknown error occurred at addUser: ${e.stackTrace}")
            throw e
        }
    }

    override fun setSessionUsername(username: String): Boolean {
        sessionUsername = username
        return true
    }

    override fun getSessionUsername(): String = sessionUsername

    override fun getUserLastBudgetId(): Flow<Int> =
        userDs.getUserLastBudgetId(sessionUsername)

    override suspend fun deleteUser(): Boolean {
        if(userDs.deleteUser(sessionUsername)) return true
        Log.d(TAG, "Failed to delete user")
        return false
    }

    override fun saveAllData() {
        TODO("Not yet implemented")
    }
}