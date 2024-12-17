package com.ynab.domain

import javax.inject.Inject

class FakeBasicAuthUseCase @Inject constructor(): BasicAuthUseCase {
    override suspend fun verifyUser(username: String, password: String): Boolean =
        username == "Alice" && password == "Alice123"

    override suspend fun isUsernameExist(username: String): Boolean =
        username == "Alice"

    override suspend fun registerUser(username: String, password: String): Boolean =
        username != "Alice"

    override fun setSessionUsername(username: String): Boolean {
        TODO("Not yet implemented")
    }
}