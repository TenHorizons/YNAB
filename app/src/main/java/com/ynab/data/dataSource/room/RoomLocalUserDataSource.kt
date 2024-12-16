package com.ynab.data.dataSource.room

import android.database.sqlite.SQLiteConstraintException
import android.util.Log
import com.ynab.TAG_PREFIX
import com.ynab.data.dataSource.LocalUserDataSource
import javax.inject.Inject

private const val TAG = "${TAG_PREFIX}RoomLocalUserDataSource"

class RoomLocalUserDataSource @Inject constructor(
    private val db: RoomDatabase
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
            Log.e(TAG,"Unknown error at addUser: ${e.stackTraceToString()}")
            return false
        }
    }
}