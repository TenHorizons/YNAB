package com.ynab.domain

import com.ynab.data.repository.FakeUserRepository
import com.ynab.data.repository.UserRepository
import kotlinx.coroutines.runBlocking
import org.junit.Test

class BasicAuthUseCaseTest {
    private val userRepository: UserRepository = FakeUserRepository()
    private val basicAuthUseCase = BasicAuthUseCaseImpl(userRepository)

    @Test
    fun verifyUser_UsernameExistAndPasswordCorrect_VerifyUserSuccess() =
        runBlocking {
            assert(basicAuthUseCase.verifyUser("Alice", "Alice123"))
        }

    @Test
    fun verifyUser_UsernameExistAndPasswordIncorrect_VerifyUserFail() =
        runBlocking {
            assert(!basicAuthUseCase.verifyUser("Alice", "Alice1234"))
        }

    @Test
    fun verifyUser_UsernameNotExist_VerifyUserFail() =
        runBlocking {
            assert(!basicAuthUseCase.verifyUser("Bob", "Bob123"))
        }

    @Test
    fun isUsernameExist_UsernameNotExist_IsUsernameExistFail() =
        runBlocking {
            assert(!basicAuthUseCase.isUsernameExist("Bob"))
        }

    @Test
    fun isUsernameExist_UsernameExist_IsUsernameExistSuccess() =
        runBlocking {
            assert(basicAuthUseCase.isUsernameExist("Alice"))
        }

    @Test
    fun registerUser_UsernameNotExist_RegisterUserSuccess() =
        runBlocking {
            assert(basicAuthUseCase.registerUser("Bob", "Bob123"))

        }

    @Test
    fun registerUser_UsernameExist_RegisterUserFail() =
        runBlocking {
            assert(!basicAuthUseCase.registerUser("Alice", "Alice123"))
        }
}