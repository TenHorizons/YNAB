package com.ynab.ui.accounts

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ynab.ui.shared.displayTwoDecimal
import com.ynab.ui.shared.isLessThanZero
import java.math.BigDecimal

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Accounts(
    modifier: Modifier = Modifier,
    vm: AccountsViewModel = hiltViewModel(),
    bottomNavBar: @Composable () -> Unit,
    onAccountClicked: (DisplayedAccount) -> Unit = {},
    onAllTransactionsClicked: () -> Unit = {},
    onAddAccountClicked: () -> Unit = {}
) {
    val accounts by vm.accounts.collectAsStateWithLifecycle(initialValue = listOf())
    val uiState by vm.uiState.collectAsStateWithLifecycle()

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Accounts",
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                actions = { TextButton(onClick = onAddAccountClicked) { Text("Add Account") } }
            )
        },
        bottomBar = bottomNavBar
    ) { innerPadding ->
        if (uiState.isDialogOpen)
            EditAccountNameDialog(
                uiState = uiState,
                onDismissDialog = vm::onDismissDialog,
                onEditAccountTextChange = vm::onEditAccountTextChange,
                onEditAccountNameClick = vm::editAccountName
            )
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
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                item {
                    AllTransactions(onClick = onAllTransactionsClicked)
                }
                item {
                    Text(
                        text = "Long press accounts for more options.",
                        color = TextFieldDefaults.colors().disabledTextColor
                    )
                }
                items(accounts.sortedBy { it.uiPosition }) {
                    Account(
                        account = it,
                        onAccountClick = { onAccountClicked(it) },
                        onEditAccountNameClick = { vm.onOpenDialog(it) },
                        onDeleteAccountClick = { vm.deleteAccount(it) }
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

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun Account(
    account: DisplayedAccount,
    onAccountClick: () -> Unit,
    onEditAccountNameClick: () -> Unit,
    onDeleteAccountClick: () -> Unit,
) {
    val haptics = LocalHapticFeedback.current
    val balance by account.balance.collectAsStateWithLifecycle(initialValue = BigDecimal.ZERO)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .wrapContentSize(Alignment.BottomCenter)
    ) {
        var menuExpanded by remember { mutableStateOf(false) }
        Card(
            elevation = CardDefaults.elevatedCardElevation(2.dp),
            modifier = Modifier.combinedClickable(
                onClick = onAccountClick,
                onLongClick = {
                    haptics.performHapticFeedback(HapticFeedbackType.LongPress)
                    menuExpanded = true
                }
            )
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = account.accountName)
                Spacer(Modifier.weight(1f))
                Column {
                    Text("Balance:")
                    val prefix =
                        if (balance.isLessThanZero()) "-RM"
                        else "RM"
                    Text(
                        text = prefix + balance.displayTwoDecimal().abs()
                    )
                }
                Spacer(Modifier.padding(horizontal = 8.dp))
                Icon(
                    modifier = Modifier.size(24.dp),
                    imageVector = Icons.Default.ChevronRight,
                    contentDescription = "See transactions for ${account.accountName} account."
                )
            }
        }
        DropdownMenu(
            expanded = menuExpanded,
            onDismissRequest = { menuExpanded = false }
        ) {
            DropdownMenuItem(
                text = { Text("Edit account name") },
                onClick = {
                    onEditAccountNameClick()
                    menuExpanded = false
                },
                leadingIcon = { Icon(Icons.Default.Edit, contentDescription = null) }
            )
            DropdownMenuItem(
                text = { Text("Delete account") },
                onClick = {
                    onDeleteAccountClick()
                    menuExpanded = false
                },
                leadingIcon = { Icon(Icons.Default.Delete, contentDescription = null) }
            )
        }
    }
}

@Composable
fun EditAccountNameDialog(
    uiState: AccountsState,
    onDismissDialog: () -> Unit,
    onEditAccountTextChange: (String) -> Unit,
    onEditAccountNameClick: () -> Unit
) {
    Dialog(onDismissRequest = onDismissDialog) {
        Card(
            modifier = Modifier.padding(16.dp),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Edit Account Name",
                    style = typography.titleLarge,
                    modifier = Modifier.padding(16.dp)
                )
                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    singleLine = true,
                    value = uiState.editAccountText,
                    onValueChange = onEditAccountTextChange,
                    placeholder = { Text("New Account Name") }
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    Button(
                        onClick = onEditAccountNameClick,
                        enabled = !uiState.isEditInProgress
                    ) {
                        if (uiState.isEditInProgress) {
                            CircularProgressIndicator()
                        } else {
                            Text("Edit Account Name")
                        }
                    }
                }
                if (uiState.isEditError) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Card(
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.errorContainer
                            )
                        ) {
                            Row(
                                modifier = Modifier.padding(8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(uiState.errorMessage)
                            }
                        }
                    }
                }
            }
        }
    }
}