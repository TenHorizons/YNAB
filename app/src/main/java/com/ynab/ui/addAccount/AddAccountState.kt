package com.ynab.ui.addAccount

data class AddAccountState(
    val accountName:String="",
    val displayedAccountBalance: String = "",
    val isAddInProgress: Boolean = false,
    val isAddSuccess: Boolean = false,
    val isAddError: Boolean = false,
    val errorMessage: String = ""
)
