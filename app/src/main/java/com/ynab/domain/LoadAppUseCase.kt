package com.ynab.domain

import com.ynab.data.repository.dataClass.TutorialCard

interface LoadAppUseCase {
    suspend fun generateUserData(isNewUser: Boolean): Boolean
    suspend fun getTutorialCards(): List<TutorialCard>
}