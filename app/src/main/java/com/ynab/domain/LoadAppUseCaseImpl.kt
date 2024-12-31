package com.ynab.domain

import android.util.Log
import com.ynab.TAG_PREFIX
import com.ynab.data.repository.BudgetItemEntryRepository
import com.ynab.data.repository.BudgetItemRepository
import com.ynab.data.repository.CategoryRepository
import com.ynab.data.repository.dataClass.TutorialCard
import java.time.YearMonth
import javax.inject.Inject

private const val TAG = "${TAG_PREFIX}LoadAppUseCaseImpl"

class LoadAppUseCaseImpl @Inject constructor(
    private val categoryRepository: CategoryRepository,
    private val budgetItemRepository: BudgetItemRepository,
    private val budgetItemEntryRepository: BudgetItemEntryRepository
): LoadAppUseCase {

    override suspend fun generateNewUserData(): Exception? {
        try {
            categoryRepository.addCategories(categoryNames = categoryToItemNamesMap.keys, budgetId = 0)
            categoryToItemNamesMap.map { (categoryName, budgetItemNames) ->
                val categoryId: Int = categoryRepository.getCategoryId(categoryName = categoryName, budgetId = 0)
                budgetItemRepository.addBudgetItems(
                    categoryId = categoryId,
                    budgetItemNames = budgetItemNames,
                    yearMonth = YearMonth.now()
                )
                budgetItemNames.map { budgetItemName ->
                    val budgetItemId: Int = budgetItemRepository.getBudgetItemId(budgetItemName = budgetItemName, categoryId = categoryId)
                    budgetItemEntryRepository.addBudgetItemEntry(
                        budgetItemId = budgetItemId,
                        yearMonth = YearMonth.now()
                    )
                }
            }
            return null
        } catch (e: Exception) {
            Log.d(TAG, "An unknown error occurred at generateNewUserData: ${e.stackTrace}")
            return e
        }
    }

    override suspend fun getTutorialCards(): List<TutorialCard> {
        TODO("Not yet implemented")
    }

    private val categoryToItemNamesMap = mapOf(
        "Immediate Obligations" to listOf(
            "Groceries",
            "Internet",
            "Electric",
            "Water",
            "Rent/Mortgage",
            "Monthly Software Subscriptions",
            "Interest & Fees"
        ),
        "True Expenses" to listOf(
            "Emergency Fund",
            "Auto Maintenance",
            "Home Maintenance",
            "Renter's/Home Insurance",
            "Medical",
            "Clothing",
            "Gifts",
            "Computer Replacement",
            "Annual Software Subscriptions",
            "Stuff I Forgot to Budget For"
        ),
        "Debt Payments" to listOf(
            "Student Loan", "Auto Loan"
        ),
        "Quality of Life Goals" to listOf(
            "Investments", "Vacation",
            "Fitness", "Education"
        ),
        "Just for Fun" to listOf(
            "Dining Out", "Gaming",
            "Music", "Fun Money"
        )
    )
}