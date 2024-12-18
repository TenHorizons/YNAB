package com.ynab.ui.accounts

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import com.ynab.ui.shared.displayTwoDecimal
import com.ynab.ui.shared.isLessThanZero

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Accounts(
    modifier: Modifier = Modifier,
    vm: AccountsViewModel = hiltViewModel(),
    bottomNavBar: @Composable () -> Unit,
    onAccountClicked: (Account) -> Unit = {},
    onAllTransactionsClicked: () -> Unit = {},
    onAddAccountClicked: () -> Unit = {}
) {
    val accounts by vm.accounts.collectAsStateWithLifecycle(initialValue = listOf())

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = {
                Text(
                    "Accounts",
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                ) },
                actions = { TextButton(onClick = onAddAccountClicked) { Text("Add Account") } }
            )
        },
        bottomBar = bottomNavBar
    ) { innerPadding ->
        if (accounts.isEmpty())
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
                    text = "No Accounts!\nAdd Account Now!",
                    style = typography.headlineMedium,
                    textAlign = TextAlign.Center
                )
            }
        else
            LazyColumn(
                modifier = modifier.padding(innerPadding),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                item {
                    AllTransactions(onClick = onAllTransactionsClicked)
                }
                items(accounts) {
                    Account(
                        account = it,
                        onClick = { onAccountClicked(it) }
                    )
                }
            }
    }
}

@Composable
fun AllTransactions(
    onClick: () -> Unit
) {
    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        elevation = CardDefaults.elevatedCardElevation(2.dp)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("All Transactions")
            Spacer(Modifier.weight(1f))
            Icon(
                modifier = Modifier.size(24.dp),
                imageVector = Icons.Default.ChevronRight,
                contentDescription = "See transactions for all accounts."
            )
        }
    }
}

@Composable
fun Account(
    account: Account,
    onClick: () -> Unit
) {
    Card(
        elevation = CardDefaults.elevatedCardElevation(2.dp),
        modifier = Modifier.clickable(onClick = onClick)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = account.accountName)
            Spacer(Modifier.weight(1f))
            Column {
                Text("Balance:")
                Text(
                    text =
                    (if(account.balance.isLessThanZero())"-RM"
                    else "RM") +
                            "${account.balance.displayTwoDecimal().abs()}")
            }
            Spacer(Modifier.padding(horizontal = 8.dp))
            Icon(
                modifier = Modifier.size(24.dp),
                imageVector = Icons.Default.ChevronRight,
                contentDescription = "See transactions for ${account.accountName} account."
            )
        }
    }
}