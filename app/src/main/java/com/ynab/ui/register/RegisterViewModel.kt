package com.ynab.ui.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ynab.domain.BasicAuthUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject


@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val basicAuthUseCase: BasicAuthUseCase
): ViewModel() {
    private val _uiState = MutableStateFlow(RegisterState())
    val uiState: StateFlow<RegisterState> = _uiState.asStateFlow()

    fun onUsernameChange(username: String) =
        _uiState.update { it.copy(username = username) }
    fun onPasswordChange(password: String) =
        _uiState.update { it.copy(password = password) }
    fun onConfirmPasswordChange(password: String) =
        _uiState.update { it.copy(confirmPassword = password) }
    fun onRegisterClick(onRegisterSuccess: () -> Unit) {
        //Check passwords match
        if (uiState.value.password != uiState.value.confirmPassword) {
            _uiState.update {
                it.copy(
                    isRegisterInProgress = false,
                    isRegisterError = true,
                    errorMessage = "Password does not match."
                )
            }
            return
        }

        //Show loading circle.
        _uiState.update { it.copy(isRegisterInProgress = true) }

        viewModelScope.launch(context = Dispatchers.IO) {
            //Check if username already exist.
            if(basicAuthUseCase.isUsernameExist(uiState.value.username)) {
                withContext(Dispatchers.Main){
                    _uiState.update { it.copy(
                        isRegisterInProgress = false,
                        isRegisterError = true,
                        errorMessage = "Username already exists."
                    ) }
                }
                return@launch
            }

            //Register user
            val isUserRegistered =
                basicAuthUseCase.registerUser(uiState.value.username,uiState.value.password)
            //set user session
            if(isUserRegistered) {
                basicAuthUseCase.setSessionUsername(uiState.value.username)
                onRegisterSuccess()
            }
            else _uiState.update { it.copy(
                isRegisterInProgress = false,
                isRegisterError = true,
                errorMessage = "Unknown error occurred when registering user."
            ) }
        }
    }
    fun hideAfterDelay(duration: Long){
        viewModelScope.launch(context = Dispatchers.Default){
            delay(duration)
            _uiState.update { it.copy(
                isRegisterInProgress = false,
                isRegisterError = false,
                errorMessage = ""
            ) }
        }
    }
}