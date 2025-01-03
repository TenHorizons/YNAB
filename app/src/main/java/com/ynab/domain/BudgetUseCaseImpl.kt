package com.ynab.domain

import com.ynab.data.repository.BudgetItemEntryRepository
import com.ynab.data.repository.BudgetItemRepository
import com.ynab.data.repository.CategoryRepository
import com.ynab.data.repository.TransactionRepository
import com.ynab.data.repository.UserRepository
import com.ynab.data.repository.dataClass.BudgetItem
import com.ynab.data.repository.dataClass.BudgetItemEntry
import com.ynab.data.repository.dataClass.Category
import com.ynab.data.repository.dataClass.Transaction
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import java.math.BigDecimal
import java.time.YearMonth
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BudgetUseCaseImpl @Inject constructor(
    private val categoryRepository: CategoryRepository,
    private val budgetItemRepository: BudgetItemRepository,
    private val budgetItemEntryRepository: BudgetItemEntryRepository,
    transactionRepository: TransactionRepository,
    userRepository: UserRepository
) : BudgetUseCase {
    override var lastSelectedYearMonth: YearMonth = YearMonth.now()

    @OptIn(ExperimentalCoroutinesApi::class)
    private val categories = userRepository.getUserLastBudgetId().flatMapLatest { budgetId ->
        categoryRepository.getCategories(budgetId)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private val budgetItems = categories.flatMapLatest { categoryList ->
        budgetItemRepository.getBudgetItems(categoryList.map { it.categoryId })
    }

    private val budgetItemIds = budgetItems.map { list ->
        list.map { it.budgetItemId }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private val budgetItemEntries = budgetItemIds.flatMapLatest { budgetItemIdList ->
        getBudgetItemEntries(budgetItemIdList, lastSelectedYearMonth)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private val previousMonthEntries =
        budgetItemIds.flatMapLatest { budgetItemIdList ->
            getBudgetItemEntries(
                budgetItemIdList,
                lastSelectedYearMonth.minusMonths(1)
            )
        }

    private val transactions: Flow<List<Transaction>> =
        transactionRepository.getTransactions(lastSelectedYearMonth)

    /** Get Categories of the currently selected Budget.*/
    override fun getBudgetCategories(): Flow<List<Category>> = categories

    /** Get Budget Items for a list of Categories.*/
    override fun getBudgetItems(): Flow<List<BudgetItem>> = budgetItems

    /** Get Budget Item Entries for a YearMonth of a list of Budget Items.*/
    override fun getThisYearMonthBudgetItemEntries(): Flow<List<BudgetItemEntry>> =
        budgetItemEntries

    private fun getBudgetItemEntries(
        budgetItemIds: List<Int>,
        yearMonth: YearMonth
    ): Flow<List<BudgetItemEntry>> =
        budgetItemEntryRepository.getBudgetItemEntries(budgetItemIds, yearMonth)

    /** Get Available for a YearMonth.
     * Available (YearMonth) =
     *      Sum of previous YearMonth's BudgetItem.rolloverBalance +
     *      Sum of this YearMonth's BudgetItem.assigned +
     *      Sum of this YearMonth's Transaction.amount*/
    @OptIn(ExperimentalCoroutinesApi::class)
    override fun getYearMonthAvailable(): Flow<BigDecimal> =
        previousMonthEntries.flatMapLatest { previousEntriesList ->
            budgetItemEntries.flatMapLatest { currentEntriesList ->
                transactions.map { transactionsList ->
                    val sumPreviousRolloverBalance = previousEntriesList.sumOf { it.rolloverBalance }
                    val sumAssigned = currentEntriesList.sumOf { it.assigned }
                    val sumTransactions = transactionsList.sumOf { it.amount }
                    sumPreviousRolloverBalance.minus(sumAssigned).plus(sumTransactions)
                }
            }
        }

    /** Get Available for a Budget Item.
     * Available (Budget Item) =
     *      Previous YearMonth's BudgetItem.rolloverBalance +
     *      This YearMonth's BudgetItem.assigned +
     *      This YearMonth's Transaction.amount where BudgetItem.budgetItemId = Transaction.budgetItemId*/
    @OptIn(ExperimentalCoroutinesApi::class)
    override fun getBudgetItemEntryAvailable(): Flow<Map<Int, BigDecimal>> =
        budgetItemEntries.flatMapLatest { currentList ->
            previousMonthEntries.flatMapLatest { previousList ->
                transactions.map { transactionList ->
                    val current = currentList.sortedBy { it.budgetItemId }
                    val previous = previousList.sortedBy { it.budgetItemId }
                    current.zip(previous) { cur, prev ->
                        val transactionSum = transactionList
                            .filter { it.budgetItemId == cur.budgetItemId }
                            .sumOf { it.amount }
                        cur.budgetItemEntryId to (prev.rolloverBalance + transactionSum + cur.assigned)
                    }.toMap()
                }
            }
        }

    /** Get Available for a Category.
     * Available (Category) =
     *      Sum of previous YearMonth's BudgetItem.rolloverBalance where BudgetItem.categoryId = Category.categoryId +
     *      Sum of this YearMonth's BudgetItem.assigned where BudgetItem.categoryId = Category.categoryId +
     *      Sum of this YearMonth's Transaction.amount where Transaction.budgetItemId = BudgetItem.budgetItemId AND BudgetItem.budgetItemId = Category.categoryId*/
    @OptIn(ExperimentalCoroutinesApi::class)
    override fun getCategoryAvailable(): Flow<Map<Int, BigDecimal>> =
        budgetItems.flatMapLatest { budgetItemList ->
            budgetItemEntries.flatMapLatest { currentList ->
                previousMonthEntries.flatMapLatest { previousList ->
                    transactions.map { transactionList ->
                        val categoryGroup = budgetItemList.groupBy { it.categoryId }
                        categoryGroup.mapValues { (_, budgetItems) ->
                            budgetItems.sumOf { budgetItem ->
                                val current = currentList
                                    .filter { it.budgetItemId == budgetItem.budgetItemId }
                                    .sortedBy { it.budgetItemEntryId }

                                val previous = previousList
                                    .filter { it.budgetItemId == budgetItem.budgetItemId }
                                    .sortedBy { it.budgetItemEntryId }

                                current.zip(previous).sumOf { (cur, prev) ->
                                    val transactionSum = transactionList
                                        .filter { it.budgetItemId == cur.budgetItemId }
                                        .sumOf { it.amount }
                                    prev.rolloverBalance + transactionSum + cur.assigned
                                }
                            }
                        }
                    }
                }
            }
        }

    /** Update the assigned value of a Budget Item.*/
    override fun updateAssigned(budgetItemEntry: BudgetItemEntry, newValue: BigDecimal) =
        budgetItemEntryRepository.updateAssigned(budgetItemEntry, newValue)
}