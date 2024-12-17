package com.ynab.data.dataSource.room

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class UserDaoTest {

    private lateinit var database: RoomDatabase
    private lateinit var userDao: UserDao

    @Before
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        database = Room.inMemoryDatabaseBuilder(context, RoomDatabase::class.java).build()

        userDao = database.userDao()
    }

    @After
    @Throws(IOException::class)
    fun close() {
        database.close()
    }

    @Test
    @Throws(Exception::class)
    fun insertUser() {
        runTest {
            val user = User( username = "Ben", password = "Ben123")
            userDao.insert(user)

            assert(userDao.isUsernameExist(user.username))
            assert(userDao.getPassword(user.username) == user.password)
        }
    }

    @Test
    @Throws(Exception::class)
    fun deleteUser() {
        runTest {
            val user = User( username = "Ben", password = "Ben123")
            userDao.insert(user)

            assert(userDao.isUsernameExist(user.username))
            assert(userDao.getPassword(user.username) == user.password)
            assert(userDao.deleteUser(user.username) == 1)
        }
    }
}