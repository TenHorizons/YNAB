package com.ynab.ui.transactions

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ynab.data.repository.dataClass.Account
import com.ynab.data.repository.dataClass.BudgetItem
import com.ynab.data.repository.dataClass.Transaction
import com.ynab.ui.shared.UNASSIGNED_TRANSACTION
import com.ynab.ui.shared.toCurrencyString


const val NO_ACC_ERR = "NO_ACCOUNT_ERR"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Transactions(
    modifier: Modifier = Modifier,
    isAllTransactions: Boolean,
    accountId: Int = -1,
    vm: TransactionsViewModel =
        hiltViewModel<TransactionsViewModel, TransactionsViewModel.TransactionsViewModelFactory> { factory ->
            factory.create(isAllTransactions, accountId)
        },
    onBackPressed: () -> Unit = {},
    onTransactionClick: (Int) -> Unit,
) {
    val uiState by vm.uiState.collectAsStateWithLifecycle()
    val transactions by uiState.transactions.collectAsStateWithLifecycle(initialValue = emptyList())
    val accounts by uiState.accountList.collectAsStateWithLifecycle(initialValue = emptyList())
    val budgetItems by uiState.budgetItems.collectAsStateWithLifecycle(initialValue = listOf())

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = {
                    val prefix =
                        if (isAllTransactions) "All"
                        else uiState.account?.accountName ?: NO_ACC_ERR
                    Text(
                        "$prefix Transactions",
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackPressed) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        if (transactions.isEmpty())
            Column(
                modifier = modifier.padding(innerPadding),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.fillMaxHeight(0.3F))
                Text(
                    modifier = Modifier
                        .padding(12.dp)
                        .fillMaxSize(),
                    text = "No Transactions!\nAdd Transactions Now!",
                    style = typography.headlineMedium,
                    textAlign = TextAlign.Center
                )
            }
        else
            LazyColumn(
                modifier = modifier.padding(innerPadding),
                verticalArrangement = Arrangement.spacedBy(2.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                item {
                    Text(
                        text = "Tap a transaction to see transaction details.",
                        color = TextFieldDefaults.colors().disabledTextColor
                    )
                }
                items(transactions.sortedByDescending { it.date }) {
                    Transaction(
                        uiState = uiState,
                        accounts = accounts,
                        budgetItems = budgetItems,
                        transaction = it,
                        onTransactionClick = { onTransactionClick(it.transactionId) }
                    )
                }
            }
    }
}

@Composable
fun Transaction(
    uiState: TransactionsState,
    accounts: List<Account>,
    budgetItems: List<BudgetItem>,
    transaction: Transaction,
    onTransactionClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onTransactionClick() }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp, 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text =
                    if (uiState.isAllTransactions)
                        accounts.first { it.accountId == transaction.accountId }.accountName
                    else
                        uiState.account?.accountName ?: NO_ACC_ERR,
                    fontSize = typography.bodySmall.fontSize
                )
                Spacer(modifier = Modifier.weight(0.5f))
                Text(
                    text =
                    if (transaction.budgetItemId == UNASSIGNED_TRANSACTION)
                        "Unassigned"
                    else
                        budgetItems.firstOrNull {
                            it.budgetItemId == transaction.budgetItemId
                        }?.budgetItemName ?: "Budget_Item_Not_Found",
                    fontSize = typography.bodySmall.fontSize
                )
                Spacer(modifier = Modifier.weight(0.5f))
                Text(
                    text = transaction.date.toString(),
                    fontSize = typography.bodyLarge.fontSize
                )
            }
            Spacer(modifier = Modifier.weight(0.09f))
            Text(
                modifier = Modifier.weight(0.9f),
                text = transaction.memo,
                fontSize = typography.bodySmall.fontSize,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.weight(0.01f))
            Text(
                text = transaction.amount.toCurrencyString(),
                fontSize = typography.titleLarge.fontSize
            )
        }
        HorizontalDivider()
    }
}