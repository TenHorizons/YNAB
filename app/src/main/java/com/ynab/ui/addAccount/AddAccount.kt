package com.ynab.ui.addAccount

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Transparent
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ynab.ui.shared.CurrencyAmountInputVisualTransformation
import com.ynab.ui.shared.LIGHT_GREEN
import com.ynab.ui.shared.displayTwoDecimal

@Composable
fun AddAccount(
    modifier: Modifier = Modifier,
    vm: AddAccountViewModel = hiltViewModel(),
    onAddAccountComplete: () -> Unit = {}
) {
    val uiState by vm.uiState.collectAsStateWithLifecycle()

    Column(
        modifier = modifier.padding(12.dp),
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            text = "Account Name:",
            fontSize = MaterialTheme.typography.titleLarge.fontSize
        )
        TextField(
            value = uiState.accountName,
            onValueChange = vm::onAccountNameChange,
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = Transparent,
                unfocusedIndicatorColor = Transparent,
                disabledIndicatorColor = Transparent,
            )
        )
        Spacer(Modifier.height(8.dp))
        Text(
            text = "Account Balance:",
            fontSize = MaterialTheme.typography.titleLarge.fontSize
        )
        TextField(
            value = uiState.displayedAccountBalance,
            singleLine = true,
            onValueChange = vm::onBalanceChange,
            modifier = Modifier.fillMaxWidth(),
            visualTransformation =
            CurrencyAmountInputVisualTransformation(isPositiveValue = true),
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = Transparent,
                unfocusedIndicatorColor = Transparent,
                disabledIndicatorColor = Transparent,
            ),
            keyboardOptions =
            KeyboardOptions(keyboardType = KeyboardType.NumberPassword)
        )
        Spacer(Modifier.height(8.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            Button(
                onClick = { vm.onAddAccountClick(onAddAccountComplete) },
                enabled = !uiState.isAddSuccess
            ) {
                if (uiState.isAddInProgress) {
                    CircularProgressIndicator(color = MaterialTheme.colorScheme.background)
                } else {
                    Text("Add Account")
                }
            }
        }
        Spacer(Modifier.height(8.dp))
        if (uiState.isAddError || uiState.isAddSuccess) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Card(
                    colors =
                    CardDefaults.cardColors(
                        containerColor =
                        if (uiState.isAddError) MaterialTheme.colorScheme.errorContainer
                        else LIGHT_GREEN
                    )
                ) {
                    Row(
                        modifier = Modifier.padding(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text =
                            if (uiState.isAddError) "Error! Error Message:\n${uiState.errorMessage}"
                            else "Account Added!"
                        )
                    }
                }
            }

        }
    }
}