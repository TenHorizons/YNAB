package com.ynab.domain

class BasicAuthUseCaseImpl (
//    private val userRepository: UserRepository
): BasicAuthUseCase {

//    override suspend fun verifyUser(username: String, password: String): Boolean {
//        val retrievedPassword = userRepository.getUserPassword(username)
//        return when {
//            retrievedPassword.isEmpty() -> false
//            retrievedPassword == password -> true
//            else -> false
//        }
//    }
//
//    override suspend fun isUsernameExist(username: String): Boolean =
//        userRepository.isUsernameExist(username)
//
//    override suspend fun registerUser(username: String, password: String): Boolean {
//        return userRepository.addUser(username,password)
//    }

    override suspend fun verifyUser(username: String, password: String): Boolean {
        val retrievedPassword = "Alice123"
        return when {
            retrievedPassword.isEmpty() -> false
            retrievedPassword == password -> true
            else -> false
        }
    }

    override suspend fun isUsernameExist(username: String): Boolean =
        username == "Alice"

    override suspend fun registerUser(username: String, password: String): Boolean =
        username != "Alice"
}