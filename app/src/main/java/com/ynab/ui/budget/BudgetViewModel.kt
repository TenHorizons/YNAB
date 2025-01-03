package com.ynab.ui.budget

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ynab.data.repository.dataClass.BudgetItem
import com.ynab.data.repository.dataClass.BudgetItemEntry
import com.ynab.data.repository.dataClass.Category
import com.ynab.domain.BudgetUseCase
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
import java.math.BigDecimal
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class BudgetViewModel @Inject constructor(
    private val budgetUseCase: BudgetUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(BudgetState())
    val uiState: StateFlow<BudgetState> = _uiState

    init {
        viewModelScope.launch(Dispatchers.IO) {
            val lastSelectedYearMonth = budgetUseCase.lastSelectedYearMonth
            val categories: Flow<List<Category>> =
                budgetUseCase.getBudgetCategories()
            val budgetItems: Flow<List<BudgetItem>> = categories.flatMapLatest { categoryList ->
                budgetUseCase.getBudgetItems(categoryList.map { it.categoryId })
            }
            val budgetItemEntries: Flow<List<BudgetItemEntry>> = budgetItems.flatMapLatest { budgetItemList ->
                budgetUseCase.getThisYearMonthBudgetItemEntries(budgetItemList.map { it.budgetItemId })
            }
            val yearMonthAvailable: Flow<BigDecimal> =
                budgetUseCase.getYearMonthAvailable()
            val categoryAvailable: Flow<Map<Int, BigDecimal>> =
                budgetUseCase.getCategoryAvailable()
            val budgetItemEntryAvailable: Flow<Map<Int, BigDecimal>> =
                budgetUseCase.getBudgetItemEntryAvailable()

            withContext(Dispatchers.Main) {
                _uiState.update { it.copy(
                    lastSelectedYearMonth = lastSelectedYearMonth,
                    categories = categories,
                    budgetItems = budgetItems,
                    budgetItemEntries = budgetItemEntries,
                    yearMonthAvailable = yearMonthAvailable,
                    categoryAvailable = categoryAvailable,
                    budgetItemEntryAvailable = budgetItemEntryAvailable
                ) }
            }
        }
    }

    fun onAssignedChange(budgetItemEntry: BudgetItemEntry, newValue: String) {
        val value = newValue.toBigDecimalOrNull()

        if (value == null) throw IllegalArgumentException("Value $newValue is not a valid BigDecimal.")
        else viewModelScope.launch(Dispatchers.IO) {
            budgetUseCase.updateAssigned(budgetItemEntry, value.divide(BigDecimal(100)))
        }
    }
}