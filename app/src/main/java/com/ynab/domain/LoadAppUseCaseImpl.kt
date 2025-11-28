package com.ynab.domain

import android.util.Log
import com.ynab.TAG_PREFIX
import com.ynab.data.repository.BudgetItemEntryRepository
import com.ynab.data.repository.BudgetItemRepository
import com.ynab.data.repository.BudgetRepository
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
    private val userRepository: UserRepository,
    private val budgetRepository: BudgetRepository,
    private val defaultBudgetStructureLoader: DefaultBudgetStructureLoader
) : LoadAppUseCase {

    override suspend fun generateUserData(isNewUser: Boolean): Boolean {
        try {
            if (isNewUser) {
                // Create default budget first, then categories and items
                val budgetId = createDefaultBudget()
                if (budgetId == -1) {
                    Log.e(TAG, "Failed to create default budget")
                    return false
                }
                generateNewUserCategoriesAndBudgetItems(budgetId)
            }
            return generateAbsentCurrentAndPreviousBudgetItemEntries()
        } catch (e: Exception) {
            Log.e(TAG, "An unknown error occurred at generateNewUserData: \n$e")
            return false
        }
    }

    override suspend fun getTutorialCards(): List<TutorialCard> {
        TODO("Not yet implemented")
    }

    private suspend fun generateAbsentCurrentAndPreviousBudgetItemEntries(): Boolean {
        try {
            val categories = userRepository.getUserLastBudgetId().first().let { budgetId ->
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

    /**
     * Creates a default budget for the current user and returns the budgetId.
     * Returns -1 if creation fails.
     */
    private suspend fun createDefaultBudget(): Int {
        return try {
            val username = userRepository.getSessionUsername()
            val budgetId = budgetRepository.createBudget(username, "My Budget")
            
            // Update user's lastBudgetId
            userRepository.updateLastBudgetId(budgetId)
            
            Log.d(TAG, "Created default budget with ID: $budgetId for user: $username")
            budgetId
        } catch (e: Exception) {
            Log.e(TAG, "Error creating default budget: ${e.stackTraceToString()}")
            -1
        }
    }

    private fun generateNewUserCategoriesAndBudgetItems(budgetId: Int) {
        // Load default budget structure from JSON file
        val categoryToItemNamesMap = defaultBudgetStructureLoader.loadDefaultStructure()
        
        categoryRepository.addCategories(categoryNames = categoryToItemNamesMap.keys, budgetId = budgetId)
        categoryToItemNamesMap.map { (categoryName, budgetItemNames) ->
            val categoryId: Int =
                categoryRepository.getCategoryId(categoryName = categoryName, budgetId = budgetId)
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
}