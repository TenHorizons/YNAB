package com.ynab.data.repository

import com.ynab.data.dataSource.FakeLocalUserDataSource
import com.ynab.data.dataSource.LocalUserDataSource
import com.ynab.data.dataSource.UserDataSource
import kotlinx.coroutines.runBlocking
import org.junit.Test

class UserRepositoryTest {
    private val localUserDs: LocalUserDataSource = FakeLocalUserDataSource()
    private val userDs = UserDataSource(localUserDs)
    private val userRepository = UserRepositoryImpl(userDs)

    @Test
    fun getUserPassword_UsernameExist_ReturnNonEmptyString() =
        runBlocking {
            assert(userRepository.getUserPassword("Alice") == "Alice123")
        }

    @Test
    fun getUserPassword_UsernameNotExist_ReturnEmptyString() =
        runBlocking {
            assert(userRepository.getUserPassword("Bob") == "")
        }

    @Test
    fun isUsernameExist_UsernameNotExist_ReturnFalse() =
        runBlocking {
            assert(!userRepository.isUsernameExist("Bob"))
        }

    @Test
    fun isUsernameExist_UsernameExist_ReturnTrue() =
        runBlocking {
            assert(userRepository.isUsernameExist("Alice"))
        }

    @Test
    fun addUser_UsernameNotExist_addUserSuccess() =
        runBlocking {
            assert(userRepository.addUser("Bob", "Bob123"))

        }

    @Test
    fun addUser_UsernameExist_addUserFail() =
        runBlocking {
            assert(!userRepository.addUser("Alice", "Alice123"))
        }
}