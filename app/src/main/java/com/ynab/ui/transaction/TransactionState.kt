package com.ynab.ui.transaction

import com.ynab.data.repository.dataClass.Account
import com.ynab.data.repository.dataClass.BudgetItem
import com.ynab.ui.shared.UNASSIGNED_TRANSACTION
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import java.time.LocalDate

data class TransactionState(
    val accounts: Flow<List<Account>> = emptyFlow(),
    val budgetItems: Flow<List<BudgetItem>> = emptyFlow(),
    val selectedBudgetItemId: Int = UNASSIGNED_TRANSACTION,
    val isSwitchGreen: Boolean = true,
    val displayedAmount: String = "",
    val selectedAccountId: Int? = null,
    val selectedDate: LocalDate? = LocalDate.now(),
    val displayedMemo: String = "",
    val isUpdateInProgress: Boolean = false,
    val isUpdateSuccess: Boolean = false,
    val isError: Boolean = false,
    val errorMessage: String = ""
)
