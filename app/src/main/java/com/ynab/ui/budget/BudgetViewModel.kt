package com.ynab.ui.budget

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ynab.data.repository.dataClass.BudgetItem
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
import java.time.YearMonth
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
            val categories: Flow<List<Category>> =
                budgetUseCase.getBudgetCategories()
            val budgetItems: Flow<List<BudgetItem>> = categories.flatMapLatest { categoryList ->
                //hardcode to YearMonth.now() before enhance to support different months.
                budgetUseCase.getBudgetItems(categoryList.map { it.categoryId }, YearMonth.now())
            }

            withContext(Dispatchers.Main) {
                _uiState.update { it.copy(
                    categories = categories,
                    budgetItems = budgetItems
                ) }
            }
        }
    }

    fun getAvailable(): Flow<BigDecimal> =
        budgetUseCase.getAvailable(YearMonth.now()) //hardcode to YearMonth.now() before enhance to support different months.

    fun getTotalAvailable(category: Category): Flow<BigDecimal> =
        budgetUseCase.getAvailable(category)

    fun getAvailable(budgetItem: BudgetItem): Flow<BigDecimal> =
        budgetUseCase.getAvailable(budgetItem)

    fun onAssignedChange(budgetItem: BudgetItem, newValue: String) {
//        val value = newValue.toBigDecimalOrNull()
//
//        if (value == null) return
//        else viewModelScope.launch(Dispatchers.IO) {
//            budgetUseCase.updateAssigned(budgetItem, value.divide(BigDecimal(100)))
//        }
    }
}