package com.ynab.ui.accounts

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import java.math.BigDecimal

data class AccountsState(
    val displayedAccounts: Flow<List<DisplayedAccount>> = emptyFlow(),
    val isDialogOpen: Boolean = false,
    val accountToEdit: DisplayedAccount? = null,
    val editAccountText: String = "",
    val isEditInProgress: Boolean = false,
    val isEditError: Boolean = false,
    val errorMessage: String = ""
)

data class DisplayedAccount(
    var accountId: Int,
    var accountName: String,
    var uiPosition: Int,
    var budgetId: Int,
    var balance: Flow<BigDecimal>
)
