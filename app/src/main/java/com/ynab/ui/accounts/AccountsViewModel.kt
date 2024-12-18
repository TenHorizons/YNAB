package com.ynab.ui.accounts

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ynab.TAG_PREFIX
import com.ynab.data.repository.AccountRepository
import com.ynab.data.repository.dataClass.Account
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

private const val TAG = "${TAG_PREFIX}AccountsViewModel"

@HiltViewModel
class AccountsViewModel @Inject constructor(
    private val accountRepository: AccountRepository
): ViewModel() {
    val accounts = accountRepository.accounts

    private val _uiState = MutableStateFlow(AccountsState())
    val uiState: StateFlow<AccountsState> = _uiState

    fun onOpenDialog(account: Account) =
        _uiState.update { it.copy(
            isDialogOpen = true,
            accountToEdit = account,
            editAccountText = account.accountName,
            isEditInProgress = false
        ) }

    fun onDismissDialog() =
        _uiState.update { it.copy(
            isDialogOpen = false,
            accountToEdit = null,
            editAccountText = "",
            isEditInProgress = false
        ) }

    fun onEditAccountTextChange(value: String) =
        _uiState.update { it.copy(editAccountText = value) }

    fun onEditAccountNameClick() {

        viewModelScope.launch(Dispatchers.IO) {
            val isAccountExist =
                accountRepository.isAccountNameExist(uiState.value.editAccountText)
            if (isAccountExist)
                withContext(Dispatchers.Main) {
                    _uiState.update { it.copy(
                        isEditInProgress = false,
                        isEditError = true,
                        errorMessage = "Account Name already exists."
                    ) }
                }
            else {
                val isEditSuccess =
                    accountRepository.updateAccountName(
                        uiState.value.accountToEdit!!,
                        uiState.value.editAccountText)
                if (isEditSuccess)
                    withContext(Dispatchers.Main) {
                        _uiState.update { it.copy(
                            isEditInProgress = false,
                            isDialogOpen = false,
                            accountToEdit = null,
                            editAccountText = ""
                        ) }
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
}