package com.ynab.ui.addTransaction

import com.ynab.ui.shared.UNASSIGNED_TRANSACTION
import java.time.LocalDate

data class AddTransactionState(
    val isSwitchGreen:Boolean = true,
    val displayedAmount: String = "",
    val selectedAccountId: Int? = null,
    val selectedBudgetItemId: Int = UNASSIGNED_TRANSACTION,
    val selectedDate:LocalDate? = LocalDate.now(),
    val memoText: String = "",
    val isError: Boolean = false,
    val errorMessage: String = "",
    val isAddInProgress: Boolean = false,
    val isAddSuccess: Boolean = false
)