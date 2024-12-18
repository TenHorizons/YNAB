package com.ynab.ui.splash

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ynab.TAG_PREFIX
import com.ynab.domain.LoadAppUseCase
import com.ynab.ui.register.RegisterState
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
            val error =
                if (isNewUser) loadAppUseCase.generateNewUserData()
                else loadAppUseCase.loadUserData()

            withContext(Dispatchers.Main) {
                if (error == null) {
                    val end = System.currentTimeMillis()
                    Log.d(TAG, "load end: $end")
                    Log.d(TAG, "load time: ${end - start}")
                    onStartupComplete()
                } else {
                    val action = if (isNewUser) "generate" else "load"
                    _uiState.value = uiState.value.copy(
                        showError = true,
                        errorMessage = "failed to $action user data. Error: \n${error.message}"
                    )
                }
            }
        }
    }
}
