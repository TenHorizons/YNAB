package com.ynab.domain

import com.ynab.data.repository.UserRepository
import javax.inject.Inject

class BasicAuthUseCaseImpl @Inject constructor(
    private val userRepository: UserRepository
): BasicAuthUseCase {

    override suspend fun verifyUser(username: String, password: String): Boolean =
        userRepository.getUserPassword(username) == password

    override suspend fun isUsernameExist(username: String): Boolean =
        userRepository.isUsernameExist(username)

    override suspend fun registerUser(username: String, password: String): Boolean =
        userRepository.addUser(username,password)

}