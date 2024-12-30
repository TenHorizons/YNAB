package com.ynab.ui.budget

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.LocalOverscrollConfiguration
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ynab.data.repository.dataClass.BudgetItem
import com.ynab.data.repository.dataClass.Category
import com.ynab.ui.shared.LIGHT_GREEN
import com.ynab.ui.shared.LIGHT_RED
import com.ynab.ui.shared.isLessThanZero
import com.ynab.ui.shared.toCurrencyString
import com.ynab.ui.shared.toDisplayedString
import kotlinx.coroutines.flow.Flow
import java.math.BigDecimal

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Budget(
    modifier: Modifier,
    vm: BudgetViewModel = hiltViewModel(),
    bottomNavBar: @Composable () -> Unit,
    onBudgetItemClicked: (Int) -> Unit
) {
    val uiState by vm.uiState.collectAsStateWithLifecycle()
    val categories by uiState.categories.collectAsStateWithLifecycle(initialValue = listOf())
    val budgetItems by uiState.budgetItems.collectAsStateWithLifecycle(initialValue = listOf())

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "BudgetTop Bar, to enhance.",
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                actions = {
                    IconButton(onClick = { }) {
                        Icon(
                            Icons.Default.Edit,
                            contentDescription = null
                        )
                    }
                }
            )
        },
        bottomBar = bottomNavBar
    ) { innerPadding ->
        LazyColumn(
            modifier = modifier.padding(innerPadding),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            item {
                Available(availableAmount = vm.getAvailable())
            }
            items(categories) { category ->
                val categoryBudgetItems = budgetItems.filter { budgetItem ->
                    budgetItem.categoryId == category.categoryId
                }
                Category(
                    category = category,
                    totalAssigned = budgetItems.sumOf { it.assigned },
                    totalAvailable = vm.getTotalAvailable(category),
                    budgetItems = categoryBudgetItems,
                    budgetItemComposable = { budgetItem ->
                        BudgetItem(
                            budgetItem = budgetItem,
                            available = vm.getAvailable(budgetItem),
                            onBudgetItemClicked = { onBudgetItemClicked(budgetItem.budgetItemId) },
                            onAssignedChange = { newValue ->
                                vm.onAssignedChange(budgetItem, newValue)
                            }
                        )
                        if (categoryBudgetItems.last() != budgetItem) HorizontalDivider()
                    }
                )
            }
        }
    }
}

@Composable
fun Available(
    availableAmount: Flow<BigDecimal>
) {
    val available by availableAmount.collectAsStateWithLifecycle(initialValue = BigDecimal.ZERO)
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor =
            if (available.isLessThanZero()) LIGHT_RED
            else LIGHT_GREEN,
        )
    ) {
        Column(modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Start
            ) {
                Text(
                    text = "Available to Budget: ",
                    fontSize = typography.bodyMedium.fontSize,
                    textAlign = TextAlign.Start,
                    maxLines = 1
                )
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                Text(
                    modifier = Modifier.horizontalScroll(rememberScrollState(0)),
                    fontSize = typography.displayMedium.fontSize,
                    textAlign = TextAlign.End,
                    maxLines = 1,
                    color =
                    if (available.isLessThanZero()) colorScheme.onErrorContainer
                    else colorScheme.onTertiaryContainer,
                    text = available.toCurrencyString()
                )
            }
        }
    }
}

@Composable
fun Category(
    category: Category,
    totalAssigned: BigDecimal,
    totalAvailable: Flow<BigDecimal>,
    budgetItems: List<BudgetItem>,
    budgetItemComposable: @Composable (BudgetItem) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    val available by totalAvailable.collectAsStateWithLifecycle(initialValue = BigDecimal.ZERO)

    Card(
        modifier = Modifier.padding(4.dp)
    ) {
        Column(
            modifier = Modifier.animateContentSize(
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioNoBouncy,
                    stiffness = Spring.StiffnessMedium
                )
            )
        ) {
            Row(
                Modifier
                    .padding(horizontal = 8.dp, vertical = 4.dp)
                    .clickable { expanded = !expanded },
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = category.categoryName,
                    fontSize = typography.bodyMedium.fontSize,
                    modifier = Modifier.weight(0.4f)
                )
                Column(
                    modifier = Modifier.weight(0.3f),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Assigned",
                        fontSize = typography.bodyMedium.fontSize
                    )
                    Text(
                        text = totalAssigned.toCurrencyString(),
                        textAlign = TextAlign.End,
                        fontSize = typography.bodyMedium.fontSize
                    )
                }
                Column(
                    modifier = Modifier.weight(0.28f),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Available",
                        fontSize = typography.bodyMedium.fontSize
                    )
                    Text(
                        text = available.toCurrencyString(),
                        textAlign = TextAlign.End,
                        fontSize = typography.bodyMedium.fontSize
                    )
                }
                Icon(
                    modifier = Modifier.weight(0.02f),
                    imageVector =
                    if (expanded) Icons.Default.ExpandLess
                    else Icons.Default.ExpandMore,
                    contentDescription = null
                )
            }
            if (expanded) {
                budgetItems.forEach { budgetItem ->
                    budgetItemComposable(budgetItem)
                }
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun BudgetItem(
    budgetItem: BudgetItem,
    available: Flow<BigDecimal>,
    onBudgetItemClicked: () -> Unit,
    onAssignedChange: (String) -> Unit
) {
    val prefix =
        if (budgetItem.assigned.isLessThanZero()) "-"
        else ""
    val displayedAssignedAmount = prefix + budgetItem.assigned.toDisplayedString()
    val budgetItemAvailable by available.collectAsStateWithLifecycle(initialValue = BigDecimal.ZERO)
    Card {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
        ) {
            CompositionLocalProvider(
                LocalOverscrollConfiguration provides null
            ) {
                Text(
                    text = budgetItem.budgetItemName,
                    fontSize = typography.bodyMedium.fontSize,
                    modifier = Modifier
                        .weight(0.4f)
                        .clickable(onClick = onBudgetItemClicked)
                        .horizontalScroll(rememberScrollState(0)),
                    maxLines = 1
                )
            }
            CustomAssignTextField(
                modifier = Modifier.weight(0.3f),
                value = displayedAssignedAmount,
                onValueChange = onAssignedChange
            )
            //Keeping non-custom text field in case custom not working well
/*            TextField(
                modifier = Modifier.weight(0.25f),
                textStyle = TextStyle.Default.copy(
                    fontSize = typography.bodyMedium.fontSize,
                    textAlign = TextAlign.Center
                ),
                value = displayedAssignedAmount,
                singleLine = true,
                onValueChange = onAssignedChange,
                visualTransformation = BudgetItemInputVisualTransformation(),
                colors = TextFieldDefaults.colors(
                    focusedIndicatorColor = Transparent,
                    unfocusedIndicatorColor = Transparent,
                    disabledIndicatorColor = Transparent,
                ),
                keyboardOptions =
                KeyboardOptions(keyboardType = KeyboardType.NumberPassword)
            )*/
            Column(
                modifier = Modifier.weight(0.3f),
                horizontalAlignment = Alignment.CenterHorizontally
            ){
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor =
                        when {
                            budgetItemAvailable.isLessThanZero() -> LIGHT_RED
                            //TODO add orange if goal not met
                            else -> LIGHT_GREEN
                        }
                    ),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(8.dp,2.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = budgetItemAvailable.toCurrencyString(),
                            fontSize = typography.bodyMedium.fontSize,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }
    }
}

/**Default Text Field too tall because of decoration box. Create custom to make text field with less height.*/
@Composable
private fun CustomAssignTextField(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit
) {
    BasicTextField(
        modifier = modifier,
        value = value,
        onValueChange = onValueChange,
        singleLine = true,
        visualTransformation = BudgetItemInputVisualTransformation(),
        textStyle = TextStyle.Default.copy(
            fontSize = typography.bodyMedium.fontSize,
            textAlign = TextAlign.Center
        ),
        decorationBox = { innerTextField ->
            Row(
                modifier.padding(6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(Modifier.weight(1f)) {
                    innerTextField()
                }
            }
        }
    )
}