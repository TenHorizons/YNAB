package com.ynab.ui.login

data class LoginState(
    val username: String = "",
    val password: String = "",
    val isLoginInProgress: Boolean = false,
    val isLoginError: Boolean = false,
    val errorMessage: String = ""
)