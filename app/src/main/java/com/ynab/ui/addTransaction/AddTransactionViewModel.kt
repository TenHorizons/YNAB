package com.ynab.ui.addTransaction

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ynab.TAG_PREFIX
import com.ynab.domain.AddTransactionUseCase
import com.ynab.ui.shared.currencyStringToBigDecimal
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDate
import javax.inject.Inject

private const val TAG = "${TAG_PREFIX}AddTransactionViewModel"

@HiltViewModel
class AddTransactionViewModel @Inject constructor(
    private val addTransactionUseCase: AddTransactionUseCase
): ViewModel() {
    private val _uiState = MutableStateFlow(AddTransactionState())

    val uiState: StateFlow<AddTransactionState> = _uiState
    val accounts = addTransactionUseCase.accounts

    fun onIsSwitchGreenChanged(value: Boolean) =
        _uiState.update { it.copy(isSwitchGreen = value) }

    fun onAmountChanged(value: String) {
        if(value.toIntOrNull() == null) return
        else
            _uiState.update { it.copy(displayedAmount = value) }
    }

    fun onAccountChange(value: Int) =
        _uiState.update { it.copy(selectedAccountId = value) }

    fun onDateSelected(value: LocalDate?) =
        _uiState.update { it.copy(selectedDate = value) }

    fun onMemoChange(value: String) =
        _uiState.update { it.copy(memoText = value) }

    fun onAddTransaction() {
        val errorMessage = when {
            uiState.value.selectedAccountId == null ->
                "Please select an account."
            uiState.value.displayedAmount.toIntOrNull() == null ->
                "Invalid transaction amount."
            uiState.value.selectedDate == null ->
                "Please select a date."
            else -> ""
        }
        if(errorMessage != "") {
            _uiState.update { it.copy(isError = true, errorMessage = errorMessage) }
            return
        }

        val amount =
            if (uiState.value.isSwitchGreen) uiState.value.displayedAmount.currencyStringToBigDecimal()
        else
            uiState.value.displayedAmount.currencyStringToBigDecimal().negate()

        _uiState.update { it.copy(
            isError = false,
            errorMessage = "",
            isAddInProgress = true
        )}

        viewModelScope.launch (Dispatchers.IO) {
            val isAddSuccess = addTransactionUseCase.addTransaction(
                amount = amount,
                accountId = uiState.value.selectedAccountId!!,
                budgetItemId = uiState.value.selectedBudgetItemId,
                date = uiState.value.selectedDate!!,
                memo = uiState.value.memoText
            )

            withContext(Dispatchers.Main) {
                if (isAddSuccess)
                    _uiState.update {
                        it.copy(isAddInProgress = false, isAddSuccess = true) }
                else
                    _uiState.update {
                        it.copy(
                            isAddInProgress = false,
                            isError = true,
                            errorMessage = "Unknown error occurred when adding transaction."
                        ) }
            }
        }
    }
}
