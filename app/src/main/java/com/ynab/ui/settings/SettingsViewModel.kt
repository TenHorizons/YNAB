package com.ynab.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ynab.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val userRepository: UserRepository
): ViewModel() {
    fun deleteAccount(onDeleteComplete: () -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            //delete user data before delete user (probably move this to work manager)
            if(userRepository.deleteUser()) withContext(Dispatchers.Main) {
                onDeleteComplete()
            }
        }

    }
}