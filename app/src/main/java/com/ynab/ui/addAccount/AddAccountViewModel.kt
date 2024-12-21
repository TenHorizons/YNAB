package com.ynab.ui.addAccount

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ynab.TAG_PREFIX
import com.ynab.data.repository.AccountRepository
import com.ynab.data.repository.TransactionRepository
import com.ynab.ui.shared.currencyStringToBigDecimal
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDate
import javax.inject.Inject

private const val TAG = "${TAG_PREFIX}AddAccountViewModel"

@HiltViewModel
class AddAccountViewModel @Inject constructor(
    private val accountRepository: AccountRepository,
    private val transactionRepository: TransactionRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(AddAccountState())
    val uiState: StateFlow<AddAccountState> = _uiState

    fun onAccountNameChange(newName: String) =
        _uiState.update { it.copy(accountName = newName) }

    fun onBalanceChange(newValue: String) {
        if (newValue.toIntOrNull() == null) return
        val balance = if (newValue.startsWith("0")) "" else newValue
        _uiState.update { it.copy(displayedAccountBalance = balance) }
    }

    fun onAddAccountClick(onAddSuccess: () -> Unit) {
        if (uiState.value.accountName.isBlank()) {
            _uiState.update {
                it.copy(
                    isAddInProgress = false,
                    isAddError = true,
                    errorMessage = "Account name cannot be blank."
                )
            }
            return
        }
        //Show loading circle.
        _uiState.update { it.copy(isAddInProgress = true) }

        viewModelScope.launch(context = Dispatchers.IO) {
            //check if account already exists
            if (accountRepository.isAccountNameExist(uiState.value.accountName)) {
                withContext(Dispatchers.Main) {
                    _uiState.update {
                        it.copy(
                            isAddInProgress = false,
                            isAddError = true,
                            errorMessage = "Account name already exists."
                        )
                    }
                }
                return@launch
            }

            //add account
            val addedAccountId = accountRepository.addAccount(
                accountName = uiState.value.accountName
            )
            if (addedAccountId == null)
                withContext(Dispatchers.Main) {
                    _uiState.update {
                        it.copy(
                            isAddInProgress = false,
                            isAddError = true,
                            errorMessage = "Unknown error occurred when adding account."
                        )
                    }
                }

            val initialBalance = uiState.value.displayedAccountBalance.currencyStringToBigDecimal()

            val isAddInitialTransactionSuccess =
                if (initialBalance.signum() == 0) true
                else {

                    transactionRepository.addTransaction(
                        amount = initialBalance,
                        accountId = addedAccountId!!.toInt(),
                        budgetItemId = 0,
                        date = LocalDate.now(),
                        memo = "Initial balance."
                    )
                }

            withContext(Dispatchers.Main) {
                if (isAddInitialTransactionSuccess) {
                    _uiState.update {
                        it.copy(
                            isAddInProgress = false,
                            isAddSuccess = true,
                            isAddError = false,
                            errorMessage = ""
                        )
                    }
                    //delay 4 seconds to display add success
                    delay(4000)
                    onAddSuccess()
                } else
                    _uiState.update {
                        it.copy(
                            isAddInProgress = false,
                            isAddError = true,
                            errorMessage = "Account added. Unknown error occurred when adding transaction for initial balance."
                        )
                    }
            }
        }
    }

    fun hideAfterDelay(duration: Long) {
        viewModelScope.launch(context = Dispatchers.Default) {
            delay(duration)
            _uiState.update {
                it.copy(
                    isAddInProgress = false,
                    isAddError = false,
                    errorMessage = ""
                )
            }
        }
    }
}