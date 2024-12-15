package com.ynab.ui.login

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
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val basicAuthUseCase: BasicAuthUseCase
): ViewModel() {
//object LoginViewModel: ViewModel() {

    private val _uiState = MutableStateFlow(LoginState())
    val uiState: StateFlow<LoginState> = _uiState.asStateFlow()

    fun onUsernameChange(username: String) =
        _uiState.update { it.copy(username = username) }
    fun onPasswordChange(password: String) =
        _uiState.update { it.copy(password = password) }
    fun onLoginClick(onLoginSuccess: () -> Unit) {
        //Show loading circle.
        _uiState.update { it.copy(isLoginInProgress = true) }

        viewModelScope.launch(context = Dispatchers.IO) {
            //Login user
            val isUserValid =
                basicAuthUseCase.verifyUser(uiState.value.username,uiState.value.password)
            if(isUserValid) onLoginSuccess()
            else _uiState.update { it.copy(
                isLoginInProgress = false,
                isLoginError = true,
                errorMessage = "Invalid username or password."
            ) }
        }
    }
    fun hideAfterDelay(duration: Long){
        viewModelScope.launch(context = Dispatchers.Default){
            delay(duration)
            _uiState.update { it.copy(
                isLoginInProgress = false,
                isLoginError = false,
                errorMessage = ""
            ) }
        }
    }
}