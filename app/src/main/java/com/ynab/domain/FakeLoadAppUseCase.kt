package com.ynab.domain

import com.ynab.data.repository.dataClass.TutorialCard
import javax.inject.Inject

class FakeLoadAppUseCase @Inject constructor(): LoadAppUseCase {

    override suspend fun generateUserData(isNewUser: Boolean): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun getTutorialCards(): List<TutorialCard> {
        TODO("Not yet implemented")
    }
}