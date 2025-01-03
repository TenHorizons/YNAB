package com.ynab.domain

import android.util.Log
import com.ynab.TAG_PREFIX
import com.ynab.data.repository.BudgetItemEntryRepository
import com.ynab.data.repository.BudgetItemRepository
import com.ynab.data.repository.CategoryRepository
import com.ynab.data.repository.UserRepository
import com.ynab.data.repository.dataClass.TutorialCard
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import java.time.YearMonth
import javax.inject.Inject

private const val TAG = "${TAG_PREFIX}LoadAppUseCaseImpl"

class LoadAppUseCaseImpl @Inject constructor(
    private val categoryRepository: CategoryRepository,
    private val budgetItemRepository: BudgetItemRepository,
    private val budgetItemEntryRepository: BudgetItemEntryRepository,
    private val useRepository: UserRepository
) : LoadAppUseCase {

    override suspend fun generateUserData(isNewUser: Boolean): Boolean {
        try {
            if (isNewUser)
                generateNewUserCategoriesAndBudgetItems()
            return generateAbsentCurrentAndPreviousBudgetItemEntries()
        } catch (e: Exception) {
            Log.d(TAG, "An unknown error occurred at generateNewUserData: \n$e")
            return false
        }
    }

    override suspend fun getTutorialCards(): List<TutorialCard> {
        TODO("Not yet implemented")
    }

    private suspend fun generateAbsentCurrentAndPreviousBudgetItemEntries(): Boolean {
        try {
            val categories = useRepository.getUserLastBudgetId().first().let { budgetId ->
                categoryRepository.getCategories(budgetId).first()
            }
            Log.d(TAG, "categories: $categories")
            val budgetItemIds =
                budgetItemRepository.getBudgetItems(categories.map { it.categoryId })
                    .map { list ->
                        list.map { it.budgetItemId }
                    }.first()
            Log.d(TAG,"budgetItemIds: $budgetItemIds")
            val current = YearMonth.now()
            val previous = YearMonth.now().minusMonths(1)

            budgetItemIds.filter { budgetItemId ->
                !budgetItemEntryRepository.isBudgetItemEntryExist(budgetItemId, current)
            }.let {
                budgetItemEntryRepository.addBudgetItemEntries(it, current)
            }
            budgetItemIds.filter { budgetItemId ->
                !budgetItemEntryRepository.isBudgetItemEntryExist(budgetItemId, previous)
            }.let {
                budgetItemEntryRepository.addBudgetItemEntries(it, previous)
            }
            return true
        } catch (e: Exception) {
            Log.d(
                TAG,
                "Unknown error occurred at generateAbsentCurrentAndPreviousBudgetItemEntries: \n $e"
            )
            return false
        }
    }

    private fun generateNewUserCategoriesAndBudgetItems() {
        categoryRepository.addCategories(categoryNames = categoryToItemNamesMap.keys, budgetId = 0)
        categoryToItemNamesMap.map { (categoryName, budgetItemNames) ->
            val categoryId: Int =
                categoryRepository.getCategoryId(categoryName = categoryName, budgetId = 0)
            budgetItemRepository.addBudgetItems(
                categoryId = categoryId,
                budgetItemNames = budgetItemNames,
                yearMonth = YearMonth.now()
            )
            budgetItemNames.map { budgetItemName ->
                val budgetItemId: Int = budgetItemRepository.getBudgetItemId(
                    budgetItemName = budgetItemName,
                    categoryId = categoryId
                )
                budgetItemEntryRepository.addBudgetItemEntry(
                    budgetItemId = budgetItemId,
                    yearMonth = YearMonth.now()
                )
            }
        }
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