package com.ynab.domain

import com.ynab.data.repository.dataClass.TutorialCard

interface LoadAppUseCase {
    suspend fun generateNewUserData(): Exception?
    suspend fun getTutorialCards(): List<TutorialCard>
}