package com.ynab.ui.accounts

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ynab.TAG_PREFIX
import com.ynab.data.repository.AccountRepository
import com.ynab.data.repository.TransactionRepository
import com.ynab.data.repository.dataClass.Account
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

private const val TAG = "${TAG_PREFIX}AccountsViewModel"

@HiltViewModel
class AccountsViewModel @Inject constructor(
    private val accountRepository: AccountRepository,
    private val transactionRepository: TransactionRepository
) : ViewModel() {

    val accounts: Flow<List<DisplayedAccount>> = accountRepository.accounts.map { accountList ->
        accountList.map { account ->
            val balance =
                transactionRepository.getTransactionsByAccountId(account.accountId).map { transactionList ->
                    transactionList.map { it.amount }.reduce { acc, value -> acc.plus(value) }
                }
            DisplayedAccount(
                accountId = account.accountId,
                accountName = account.accountName,
                uiPosition = account.uiPosition,
                budgetId = account.budgetId,
                balance = balance)
        }
    }

    private val _uiState = MutableStateFlow(AccountsState())
    val uiState: StateFlow<AccountsState> = _uiState

    fun onOpenDialog(account: DisplayedAccount) =
        _uiState.update {
            it.copy(
                isDialogOpen = true,
                accountToEdit = account,
                editAccountText = account.accountName,
                isEditInProgress = false
            )
        }

    fun onDismissDialog() =
        _uiState.update {
            it.copy(
                isDialogOpen = false,
                accountToEdit = null,
                editAccountText = "",
                isEditInProgress = false
            )
        }

    fun onEditAccountTextChange(value: String) =
        _uiState.update { it.copy(editAccountText = value) }

    fun editAccountName() {
        viewModelScope.launch(Dispatchers.IO) {
            val isAccountExist =
                accountRepository.isAccountNameExist(uiState.value.editAccountText)
            if (isAccountExist)
                withContext(Dispatchers.Main) {
                    _uiState.update {
                        it.copy(
                            isEditInProgress = false,
                            isEditError = true,
                            errorMessage = "Account Name already exists."
                        )
                    }
                }
            else {
                val isEditSuccess =
                    accountRepository.updateAccountName(
                        uiState.value.accountToEdit!!.toAccount(),
                        uiState.value.editAccountText
                    )
                if (isEditSuccess)
                    withContext(Dispatchers.Main) {
                        _uiState.update {
                            it.copy(
                                isEditInProgress = false,
                                isDialogOpen = false,
                                accountToEdit = null,
                                editAccountText = ""
                            )
                        }
                    }
                else
                    withContext(Dispatchers.Main) {
                        _uiState.update {
                            it.copy(
                                isEditInProgress = false,
                                isEditError = true,
                                errorMessage = "Unknown error when updating account name."
                            )
                        }
                    }
            }
        }
    }

    fun deleteAccount(displayedAccount: DisplayedAccount) {
        viewModelScope.launch(Dispatchers.IO) {
            accountRepository.deleteAccount(displayedAccount.toAccount())
        }
    }

    private fun DisplayedAccount.toAccount(): Account = Account(
        accountId = accountId,
        accountName = accountName,
        uiPosition = uiPosition,
        budgetId = budgetId
    )
}