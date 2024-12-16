package com.ynab.ui.register

data class RegisterState(
    val username: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val isRegisterInProgress: Boolean = false,
    val isRegisterError: Boolean = false,
    val errorMessage: String = ""
)