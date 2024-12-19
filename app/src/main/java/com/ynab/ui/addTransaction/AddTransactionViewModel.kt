package com.ynab.ui.addTransaction

import android.util.Log
import androidx.lifecycle.ViewModel
import com.ynab.TAG_PREFIX
import com.ynab.domain.AddTransactionUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import java.time.LocalDate
import javax.inject.Inject

private const val TAG = "${TAG_PREFIX}AddTransactionViewModel"

@HiltViewModel
class AddTransactionViewModel @Inject constructor(
    private val addTransactionUseCaseImpl: AddTransactionUseCase
): ViewModel() {
    private val _uiState = MutableStateFlow(AddTransactionState())

    val uiState: StateFlow<AddTransactionState> = _uiState
    val accounts = addTransactionUseCaseImpl.accounts

    fun onIsSwitchGreenChanged(value: Boolean) =
        _uiState.update { it.copy(isSwitchGreen = value) }

    fun onAmountChanged(value: String) {
        if(value.toIntOrNull() == null) return
        else
            _uiState.update { it.copy(displayedAmount = value) }
    }

    fun onAccountChange(value: Int) =
        _uiState.update { it.copy(selectedAccountId = value) }

    fun onDateSelected(value: LocalDate) =
        _uiState.update { it.copy(selectedDate = value) }

    fun onMemoChange(value: String) =
        _uiState.update { it.copy(memoText = value) }

    fun onAddTransaction() {
        Log.d(TAG, "Add Transaction button clicked. uiState:\n${uiState.value}")
    }
}
