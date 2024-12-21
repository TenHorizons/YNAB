package com.ynab.ui.transactions

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ynab.data.repository.AccountRepository
import com.ynab.data.repository.TransactionRepository
import com.ynab.data.repository.dataClass.Account
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel(assistedFactory = TransactionsViewModel.TransactionsViewModelFactory::class)
class TransactionsViewModel @AssistedInject constructor(
    @Assisted private val isAllTransactions: Boolean,
    @Assisted private val accountId: Int,
    private val transactionRepository: TransactionRepository,
    private val accountRepository: AccountRepository
) : ViewModel() {

    @AssistedFactory
    interface TransactionsViewModelFactory {
        fun create(isAllTransactions: Boolean, accountId: Int): TransactionsViewModel
    }

    private val _uiState = MutableStateFlow(TransactionsState())
    val uiState: StateFlow<TransactionsState> = _uiState

    init {
        viewModelScope.launch(Dispatchers.IO) {
            val account =
                if (accountId < 0) null
                else accountRepository.getAccount(accountId)
            val accountList =
                if (isAllTransactions) accountRepository.accounts
                else emptyFlow()
            val transactions =
                when {
                    accountList != emptyFlow<List<Account>>() ->
                        accountList.flatMapLatest { accList ->
                            transactionRepository.getTransactionsByAccountIdList(
                                accList.map { it.accountId })
                        }

                    accountId < 0 -> emptyFlow()
                    else -> transactionRepository.getTransactionsByAccountId(accountId)
                }
            withContext(Dispatchers.Main) {
                _uiState.update {
                    it.copy(
                        isAllTransactions = isAllTransactions,
                        account = account,
                        accountList = accountList,
                        transactions = transactions
                    )
                }
            }
        }
    }
}