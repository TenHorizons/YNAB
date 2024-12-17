package com.ynab.ui.accounts

import androidx.lifecycle.ViewModel
import com.ynab.TAG_PREFIX
import com.ynab.data.repository.AccountRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

private const val TAG = "${TAG_PREFIX}AccountsViewModel"

@HiltViewModel
class AccountsViewModel @Inject constructor(
    private val accountRepository: AccountRepository
): ViewModel() {
    val accounts = accountRepository.accounts
}