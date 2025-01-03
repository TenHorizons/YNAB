package com.ynab.data.dataSource.room

import android.database.sqlite.SQLiteConstraintException
import android.util.Log
import com.ynab.TAG_PREFIX
import com.ynab.data.dataSource.LocalUserDataSource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

private const val TAG = "${TAG_PREFIX}RoomLocalUserDataSource"

@Singleton
class RoomLocalUserDataSource @Inject constructor(
    db: RoomDatabase
): LocalUserDataSource {
    private val userDao = db.userDao()
    override suspend fun getPassword(username: String): String? =
        userDao.getPassword(username)

    override suspend fun isUsernameExist(username: String): Boolean =
        userDao.isUsernameExist(username)

    override suspend fun addUser(username: String, password: String): Boolean {
        try {
            userDao.insert(User(username = username, password = password))
            return true
        } catch (e: SQLiteConstraintException) {
            Log.d(TAG,"addUser threw SQLiteConstraintException, likely due to username already exist.")
            return false
        } catch (e: Exception){
            Log.d(TAG,"Unknown error at addUser: ${e.stackTraceToString()}")
            return false
        }
    }

    override suspend fun deleteUser(username: String): Boolean =
        userDao.deleteUser(username) > 0

    override fun getUserLastBudgetId(username: String): Flow<Int> =
        userDao.getUserLastBudgetId(username)
}