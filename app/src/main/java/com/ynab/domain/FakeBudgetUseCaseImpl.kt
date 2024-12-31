package com.ynab.domain

import com.ynab.data.repository.dataClass.BudgetItem
import com.ynab.data.repository.dataClass.BudgetItemEntry
import com.ynab.data.repository.dataClass.Category
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.math.BigDecimal
import java.time.YearMonth
import javax.inject.Inject

class FakeBudgetUseCaseImpl @Inject constructor(): BudgetUseCase {

    private val categoryNames = listOf(
        "Immediate Obligations",
        "True Expenses",
        "Debt Payments",
        "Quality of Life Goals",
        "Just for Fun"
    )

    private val budgetItemNames = listOf(
        "Groceries&Laundry", "Internet",
        "Electric", "Water",
        "Rent/Mortgage", "Monthly Software Subscriptions", "Interest & Fees",

        "Emergency Fund", "Auto Maintenance",
        "Home Maintenance", "Renter's/Home Insurance",
        "Medical", "Clothing",
        "Gifts", "Computer Replacement",
        "Annual Software Subscriptions",
        "Stuff I Forgot to Budget For",

        "Student Loan", "Auto Loan",

        "Investments", "Vacation",
        "Fitness", "Education",

        "Dining Out", "Gaming",
        "Music", "Fun Money"
    )

    private val categoryToItemNamesMap = mapOf(
        categoryNames[0] to listOf(
            budgetItemNames[0],
            budgetItemNames[1],
            budgetItemNames[2],
            budgetItemNames[3],
            budgetItemNames[4],
            budgetItemNames[5],
            budgetItemNames[6]
        ),
        categoryNames[1] to listOf(
            budgetItemNames[7],
            budgetItemNames[8],
            budgetItemNames[9],
            budgetItemNames[10],
            budgetItemNames[11],
            budgetItemNames[12],
            budgetItemNames[13],
            budgetItemNames[14],
            budgetItemNames[15],
            budgetItemNames[16]
        ),
        categoryNames[2] to listOf(
            budgetItemNames[17],
            budgetItemNames[18]
        ),
        categoryNames[3] to listOf(
            budgetItemNames[19],
            budgetItemNames[20],
            budgetItemNames[21],
            budgetItemNames[22]
        ),
        categoryNames[4] to listOf(
            budgetItemNames[23],
            budgetItemNames[24],
            budgetItemNames[25],
            budgetItemNames[26]
        )
    )

    override fun getBudgetCategories(): Flow<List<Category>> = flow {
        while (true) {
            emit(categoryNames.map { categoryName ->
                Category(categoryNames.indexOf(categoryName),0,categoryName,0)
            })
            delay(5000)
        }
    }

    override fun getBudgetItems(
        categoryIds: List<Int>
    ): Flow<List<BudgetItem>> = flow {
        while (true) {
            emit(budgetItemNames.map { budgetItemName ->
                val budgetItemIndex: Int = budgetItemNames.indexOf(budgetItemName)
                val categoryIndex: Int = categoryNames.indexOf(
                    categoryToItemNamesMap.keys.first { categoryToItemNamesMap[it]!!.contains(budgetItemName) }
                )
                val budgetItemPosition: Int = categoryToItemNamesMap[categoryNames[categoryIndex]]!!.indexOf(budgetItemName)
                BudgetItem(
                    budgetItemId = budgetItemIndex,
                    categoryId = categoryIndex,
                    budgetItemName = budgetItemName,
                    itemUiPosition = budgetItemPosition
                )
            })
            delay(5000)
        }
    }

    override fun getBudgetItemEntries(
        map: List<Int>,
        yearMonth: YearMonth
    ): Flow<List<BudgetItemEntry>> = flow {
        while (true) {
            emit(budgetItemNames.map { budgetItemName ->
                val budgetItemIndex: Int = budgetItemNames.indexOf(budgetItemName)
                BudgetItemEntry(
                    budgetItemEntryId = budgetItemIndex,
                    budgetItemId = budgetItemIndex,
                    yearMonth = YearMonth.now(),
                    assigned = BigDecimal.ZERO,
                    rolloverBalance = BigDecimal.ZERO
                )
            })
            delay(5000)
        }
    }

    override fun getAvailable(yearMonth: YearMonth): Flow<BigDecimal> = flow {
        while (true) {
            emit(BigDecimal.ZERO)
            delay(5000)
        }
    }

    override fun getAvailable(budgetItem: BudgetItem): Flow<BigDecimal> = flow {
        while (true) {
            emit(BigDecimal.ZERO)
            delay(5000)
        }
    }

    override fun getAvailable(category: Category): Flow<BigDecimal> = flow {
        while (true) {
            emit(BigDecimal.ZERO)
            delay(5000)
        }
    }

    override fun updateAssigned(budgetItem: BudgetItem, newValue: BigDecimal) {
        TODO("Not yet implemented")
    }
}