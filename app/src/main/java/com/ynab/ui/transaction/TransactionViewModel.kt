package com.ynab.ui.transaction

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ynab.data.repository.AccountRepository
import com.ynab.data.repository.BudgetItemRepository
import com.ynab.data.repository.CategoryRepository
import com.ynab.data.repository.TransactionRepository
import com.ynab.data.repository.UserRepository
import com.ynab.data.repository.dataClass.BudgetItem
import com.ynab.data.repository.dataClass.Transaction
import com.ynab.ui.shared.currencyStringToBigDecimal
import com.ynab.ui.shared.toDisplayedString
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDate

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel(assistedFactory = TransactionViewModel.TransactionViewModelFactory::class)
class TransactionViewModel @AssistedInject constructor(
    @Assisted private val transactionId: Int,
    private val accountRepository: AccountRepository,
    private val userRepository: UserRepository,
    private val categoryRepository: CategoryRepository,
    private val budgetItemRepository: BudgetItemRepository,
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
            val budgetItems: Flow<List<BudgetItem>> =
                userRepository.getUserLastBudgetId().flatMapLatest { budgetId ->
                    categoryRepository.getCategories(budgetId).flatMapLatest { categories ->
                        budgetItemRepository.getBudgetItems(categories.map { it.categoryId })
                    }
                }
            withContext(Dispatchers.Main) {
                if (transaction != null)
                    _uiState.update {
                        it.copy(
                            accounts = accounts,
                            budgetItems = budgetItems,
                            isSwitchGreen = transaction.amount.signum() == 1,
                            displayedAmount = transaction.amount.toDisplayedString(),
                            selectedBudgetItemId = transaction.budgetItemId,
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

    fun onBudgetItemSelected(selectedBudgetItemId: Int) =
        _uiState.update { it.copy(selectedBudgetItemId = selectedBudgetItemId) }

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
                    budgetItemId = uiState.value.selectedBudgetItemId,
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