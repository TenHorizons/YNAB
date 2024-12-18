package com.ynab.domain

import com.ynab.data.repository.UserRepository
import com.ynab.data.repository.dataClass.TutorialCard
import javax.inject.Inject

class LoadAppUseCaseImpl @Inject constructor(
    private val userRepository: UserRepository
): LoadAppUseCase {
    override suspend fun loadUserData(): Exception? {
        return null
    }

    override suspend fun generateNewUserData(): Exception? {
        return null
    }

    override suspend fun getTutorialCards(): List<TutorialCard> {
        TODO("Not yet implemented")
    }
}