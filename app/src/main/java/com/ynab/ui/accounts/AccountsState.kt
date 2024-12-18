package com.ynab.ui.accounts

import com.ynab.data.repository.dataClass.Account

data class AccountsState(
    val isDialogOpen: Boolean = false,
    val accountToEdit: Account? = null,
    val editAccountText: String = "",
    val isEditInProgress: Boolean = false,
    val isEditError: Boolean = false,
    val errorMessage: String = ""
)
