package com.ynab.ui.addTransaction

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Green
import androidx.compose.ui.graphics.Color.Companion.Red
import androidx.compose.ui.graphics.Color.Companion.Transparent
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ynab.data.repository.dataClass.Account
import com.ynab.ui.shared.CurrencyAmountInputVisualTransformation
import com.ynab.ui.shared.LIGHT_GREEN
import java.time.LocalDate
import java.time.ZoneId
import java.util.Date

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTransaction(
    modifier: Modifier,
    vm: AddTransactionViewModel = hiltViewModel(),
    bottomNavBar: @Composable () -> Unit,
) {
    val accounts by vm.accounts.collectAsStateWithLifecycle(initialValue = listOf())
    val uiState by vm.uiState.collectAsStateWithLifecycle()

    val keyboardController = LocalSoftwareKeyboardController.current

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Add Transaction",
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            )
        },
        bottomBar = bottomNavBar
    ) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {
            AmountRow(
                uiState = uiState,
                onSwitchGreenChanged = vm::onIsSwitchGreenChanged,
                onAmountChanged = vm::onAmountChanged
            )
            Card(modifier = Modifier.padding(8.dp)) {
                TransactionInfo(
                    uiState = uiState,
                    accounts = accounts,
                    onAccountSelected = vm::onAccountChange,
                    onDateSelected = vm::onDateSelected
                )
            }
            Card(modifier = Modifier.padding(8.dp)) {
                Memo(
                    uiState.memoText,
                    onTextChanged = vm::onMemoChange
                )
            }
            Row(
                Modifier.padding(end = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.End
            ) {
                Spacer(modifier = Modifier.weight(1f))
                if (uiState.isAddInProgress) {
                    CircularProgressIndicator()
                } else {
                    Button(
                        onClick = {
                            keyboardController?.hide()
                            vm.onAddTransaction()
                        }
                    ) {
                        Text(text = "Add Transaction")
                    }
                }
            }
            if (uiState.isError || uiState.isAddSuccess) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Card(
                        colors =
                        CardDefaults.cardColors(
                            containerColor =
                            if (uiState.isError) colorScheme.errorContainer
                            else LIGHT_GREEN
                        )
                    ) {
                        Row(
                            modifier = Modifier.padding(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text =
                                if (uiState.isError) uiState.errorMessage
                                else "Transaction Added!"
                            )
                        }
                    }
                }

            }
        }
    }
}

@Composable
fun AmountRow(
    uiState: AddTransactionState,
    onSwitchGreenChanged: (Boolean) -> Unit,
    onAmountChanged: (String) -> Unit
) {
    Card(
        modifier = Modifier.padding(8.dp),
        colors = CardDefaults.cardColors(
            containerColor =
            if (uiState.isSwitchGreen) LIGHT_GREEN
            else colorScheme.errorContainer
        )
    ) {
        Row(
            modifier = Modifier.padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Switch(
                checked = uiState.isSwitchGreen,
                onCheckedChange = onSwitchGreenChanged,
                colors = SwitchDefaults.colors(
                    checkedTrackColor = Green,
                    uncheckedTrackColor = Red
                )
            )
            Spacer(modifier = Modifier.weight(1f))
            TextField(
                modifier = Modifier.wrapContentWidth(),
                value = uiState.displayedAmount,
                singleLine = true,
                onValueChange = onAmountChanged,
                visualTransformation =
                CurrencyAmountInputVisualTransformation(isPositiveValue = uiState.isSwitchGreen),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
                textStyle = LocalTextStyle.current.copy(
                    fontSize = 32.sp,
                    textAlign = TextAlign.End,
                    shadow = Shadow(Color.Black, blurRadius = 0.3f)
                ),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Transparent,
                    unfocusedContainerColor = Transparent,
                    disabledContainerColor = Transparent,
                    errorContainerColor = Transparent,
                    focusedIndicatorColor = Transparent,
                    unfocusedIndicatorColor = Transparent,
                    disabledIndicatorColor = Transparent,
                    errorIndicatorColor = Transparent
                ),
                placeholder = { Text("Amount") }
            )
        }
    }
}

@Composable
fun TransactionInfo(
    uiState: AddTransactionState,
    accounts: List<Account>,
    onAccountSelected: (Int) -> Unit,
    onDateSelected: (LocalDate?) -> Unit
) {

    Column {
        TransactionInfoItemWithMenu(
            title = "Account",
            selectedOption =
            if (uiState.selectedAccountId == null) "No Accounts Selected"
            else accounts.first {
                it.accountId == uiState.selectedAccountId
            }.accountName,
            onSelectedChange = { selectedAccountName ->
                onAccountSelected(
                    accounts.first {
                        it.accountName == selectedAccountName
                    }
                        .accountId)
            },
            options = accounts.map { it.accountName }
        )
        HorizontalDivider()
        Row(
            Modifier.padding(start = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "Date", textAlign = TextAlign.Start)
            Spacer(Modifier.weight(1f))
            AddTransactionDatePicker(
                uiState = uiState,
                onDateSelected = onDateSelected
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TransactionInfoItemWithMenu(
    title: String,
    selectedOption: String,
    onSelectedChange: (String) -> Unit,
    options: List<String>
) {
    var expanded by remember { mutableStateOf(false) }
    Row(
        Modifier.padding(start = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(title, textAlign = TextAlign.Start)
        Spacer(modifier = Modifier.weight(1f))
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded }
        ) {
            TextField(
                modifier = Modifier
                    .menuAnchor()
                    .widthIn(100.dp),
                readOnly = true,
                value = selectedOption,
                onValueChange = {/*no action here, since read only.*/ },
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(
                        expanded = expanded
                    )
                },
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Transparent,
                    unfocusedContainerColor = Transparent,
                    disabledContainerColor = Transparent,
                    errorContainerColor = Transparent,
                    focusedIndicatorColor = Transparent,
                    unfocusedIndicatorColor = Transparent,
                    disabledIndicatorColor = Transparent,
                    errorIndicatorColor = Transparent
                ),
                textStyle = LocalTextStyle.current.copy(textAlign = TextAlign.End)
            )
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                options.forEach { option ->
                    DropdownMenuItem(
                        text = { Text(text = option) },
                        onClick = {
                            onSelectedChange(option)
                            expanded = false
                        },
                        contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding
                    )
                }
            }
        }
    }
}

@Composable
fun Memo(memoText: String, onTextChanged: (String) -> Unit) {
    Column(
        horizontalAlignment = Alignment.Start,
        modifier = Modifier.padding(8.dp)
    ) {
        Text("Memo", Modifier.padding(start = 14.dp))
        TextField(
            value = memoText,
            onValueChange = onTextChanged,
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.3f),
            label = { Text("Write notes here...") }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTransactionDatePicker(
    uiState: AddTransactionState,
    onDateSelected: (LocalDate?) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState()
    fun getSelectedDate(): LocalDate? =
        if (datePickerState.selectedDateMillis == null) null
        else
            Date(datePickerState.selectedDateMillis!!)
                .toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate()

    TextField(
        modifier = Modifier
            .wrapContentWidth()
            .clickable { expanded = true },
        /*need enabled = false for clickable() to work:
         https://stackoverflow.com/questions/67902919/jetpack-compose-textfield-clickable-does-not-work*/
        enabled = false,
        readOnly = true,
        value =
        if (uiState.selectedDate == null) "No date selected"
        else uiState.selectedDate.toString(),
        onValueChange = {/*no action here, since read only.*/ },
        trailingIcon = {
            ExposedDropdownMenuDefaults.TrailingIcon(
                expanded = expanded
            )
        },
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Transparent,
            unfocusedContainerColor = Transparent,
            disabledContainerColor = Transparent,
            errorContainerColor = Transparent,
            focusedIndicatorColor = Transparent,
            unfocusedIndicatorColor = Transparent,
            disabledIndicatorColor = Transparent,
            errorIndicatorColor = Transparent,
            //since text field disabled, change disabled color as focused
            disabledTextColor = TextFieldDefaults.colors().focusedTextColor,
            disabledTrailingIconColor = TextFieldDefaults.colors().focusedTrailingIconColor
        ),
        textStyle = LocalTextStyle.current.copy(textAlign = TextAlign.End)
    )
    if (expanded)
        DatePickerDialog(
            onDismissRequest = { expanded = false },
            confirmButton = {
                TextButton(onClick = {
                    onDateSelected(getSelectedDate())
                    expanded = false
                }) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(onClick = { expanded = false }) {
                    Text("Cancel")
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
}