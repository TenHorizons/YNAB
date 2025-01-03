package com.ynab.ui.splash

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ynab.TAG_PREFIX
import com.ynab.domain.LoadAppUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

private const val TAG = "${TAG_PREFIX}SplashScreenViewModel"

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val loadAppUseCase: LoadAppUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(SplashViewModelState())
    val uiState: StateFlow<SplashViewModelState> = _uiState.asStateFlow()

    fun onAppStart(isNewUser: Boolean, onStartupComplete: () -> Unit) {
        val start = System.currentTimeMillis()
        Log.d(TAG, "load start: $start")
        _uiState.value = uiState.value.copy(
            showError = false,
            errorMessage = ""
        )

        viewModelScope.launch(context = Dispatchers.IO) {
            val success = loadAppUseCase.generateUserData(isNewUser)

            withContext(Dispatchers.Main) {
                if (success) {
                    val end = System.currentTimeMillis()
                    Log.d(TAG, "load end: $end")
                    Log.d(TAG, "load time: ${end - start}")
                    onStartupComplete()
                } else
                    _uiState.value = uiState.value.copy(
                        showError = true,
                        errorMessage = "Failed to generate new user data. See logs for error details."
                    )
            }
        }
    }
}
