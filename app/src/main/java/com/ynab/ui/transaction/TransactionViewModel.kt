package com.ynab.ui.transaction

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ynab.data.repository.AccountRepository
import com.ynab.data.repository.TransactionRepository
import com.ynab.data.repository.dataClass.Transaction
import com.ynab.ui.shared.currencyStringToBigDecimal
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.math.BigDecimal
import java.time.LocalDate

@HiltViewModel(assistedFactory = TransactionViewModel.TransactionViewModelFactory::class)
class TransactionViewModel @AssistedInject constructor(
    @Assisted private val transactionId: Int,
    private val accountRepository: AccountRepository,
    private val transactionRepository: TransactionRepository
) : ViewModel() {

    @AssistedFactory
    interface TransactionViewModelFactory {
        fun create(transactionId: Int): TransactionViewModel
    }

    private val _uiState = MutableStateFlow(TransactionState())
    val uiState: StateFlow<TransactionState> = _uiState

    init {
        viewModelScope.launch(Dispatchers.IO) {
            val accounts = accountRepository.accounts
            val transaction = transactionRepository.getTransaction(transactionId)
            withContext(Dispatchers.Main) {
                if (transaction != null)
                    _uiState.update {
                        it.copy(
                            accounts = accounts,
                            isSwitchGreen = transaction.amount.signum() == 1,
                            //Multiply by 100 to remove decimals. e.g. 20.20 to 2020.
                            //abs() and setScale(0) so it doesn't result as -2020.00.
                            //This is for text field visual transformation.
                            displayedAmount =
                            transaction.amount
                                .abs()
                                .multiply(BigDecimal("100"))
                                .setScale(0)
                                .toPlainString(),
                            selectedAccountId = transaction.accountId,
                            selectedDate = transaction.date,
                            displayedMemo = transaction.memo
                        )
                    }
                else
                    _uiState.update {
                        it.copy(
                            isError = true,
                            errorMessage = "Transaction not found.")
                    }
            }
        }
    }

    fun onIsSwitchGreenChanged(value: Boolean) =
        _uiState.update { it.copy(isSwitchGreen = value) }

    fun onAmountChanged(value: String) {
        if (value.toIntOrNull() == null) return
        else _uiState.update { it.copy(displayedAmount = value) }
    }

    fun onAccountChange(value: Int) =
        _uiState.update { it.copy(selectedAccountId = value) }

    fun onDateSelected(value: LocalDate?) =
        _uiState.update { it.copy(selectedDate = value) }

    fun onMemoChange(value: String) =
        _uiState.update { it.copy(displayedMemo = value) }

    fun onUpdateTransaction() {
        val errorMessage = when {
            uiState.value.selectedAccountId == null ->
                "Please select an account."

            uiState.value.displayedAmount.toIntOrNull() == null ->
                "Invalid transaction amount."

            uiState.value.selectedDate == null ->
                "Please select a date."

            else -> ""
        }
        if (errorMessage != "") {
            _uiState.update { it.copy(isError = true, errorMessage = errorMessage) }
            return
        }

        val amount =
            if (uiState.value.isSwitchGreen)
                uiState.value.displayedAmount.currencyStringToBigDecimal()
            else
                uiState.value.displayedAmount.currencyStringToBigDecimal().negate()

        _uiState.update {
            it.copy(
                isError = false,
                errorMessage = "",
                isUpdateInProgress = true
            )
        }

        viewModelScope.launch(Dispatchers.IO) {
            val isUpdateSuccess = transactionRepository.updateTransaction(
                Transaction(
                    transactionId = transactionId,
                    accountId = uiState.value.selectedAccountId!!,
                    budgetItemId = 0,
                    date = uiState.value.selectedDate!!,
                    amount = amount,
                    memo = uiState.value.displayedMemo
                )
            )

            withContext(Dispatchers.Main) {
                if (isUpdateSuccess)
                    _uiState.update {
                        it.copy(isUpdateInProgress = false, isUpdateSuccess = true)
                    }
                else
                    _uiState.update {
                        it.copy(
                            isUpdateInProgress = false,
                            isError = true,
                            errorMessage = "Unknown error occurred when updating transaction."
                        )
                    }
            }
        }
    }

    fun deleteTransaction(onDeleteSuccess: () -> Unit) {
        _uiState.update {
            it.copy(
                isError = false,
                errorMessage = "",
                isUpdateInProgress = true
            )
        }
        viewModelScope.launch(Dispatchers.IO) {
            val isDeleteSuccess = transactionRepository.deleteTransaction(transactionId)
            withContext(Dispatchers.Main) {
                if (isDeleteSuccess)
                    onDeleteSuccess()
                else
                    _uiState.update {
                        it.copy(
                            isUpdateInProgress = false,
                            isError = true,
                            errorMessage = "Unknown error occurred when deleting transaction."
                        )
                    }
            }
        }
    }
}