package com.ynab.ui.addAccount

import androidx.lifecycle.ViewModel
import com.ynab.TAG_PREFIX
import com.ynab.data.repository.AccountRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

private const val TAG = "${TAG_PREFIX}AddAccountViewModel"

@HiltViewModel
class AddAccountViewModel @Inject constructor(
    accountRepository: AccountRepository
): ViewModel() {
}