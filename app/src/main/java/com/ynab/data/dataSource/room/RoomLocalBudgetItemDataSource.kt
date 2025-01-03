package com.ynab.data.dataSource.room

import android.database.sqlite.SQLiteConstraintException
import android.util.Log
import com.ynab.TAG_PREFIX
import com.ynab.data.dataSource.LocalBudgetItemDataSource
import kotlinx.coroutines.flow.Flow
import java.time.YearMonth
import javax.inject.Inject
import com.ynab.data.repository.dataClass.BudgetItem
import kotlinx.coroutines.flow.map

private const val TAG = "${TAG_PREFIX}RoomLocalBudgetItemDataSource"

class RoomLocalBudgetItemDataSource @Inject constructor(
    db: RoomDatabase
): LocalBudgetItemDataSource {
    private val budgetItemDao = db.budgetItemDao()
    /**Add new budget items for a single category and YearMonth.*/
    override fun addBudgetItems(
        categoryId: Int,
        budgetItemNames: List<String>,
        yearMonth: YearMonth
    ): Exception? {
        val budgetItems = budgetItemNames.map { name ->
            BudgetItem(
                categoryId = categoryId,
                budgetItemName = name,
                itemUiPosition = budgetItemNames.indexOf(name)
            )
        }
        try {
            budgetItemDao.insert(budgetItems)
            return null
        } catch (e: SQLiteConstraintException) {
            Log.d(
                TAG,
                "addBudgetItems threw SQLiteConstraintException, likely due to budget item/s already exist."
            )
            return e
        } catch (e: Exception) {
            Log.d(TAG, "Unknown error at addBudgetItems: ${e.stackTraceToString()}")
            return e
        }
    }

    /**Get budget item ID based on name and category ID.*/
    override fun getBudgetItemId(budgetItemName: String, categoryId: Int): Int =
        budgetItemDao.getBudgetItemId(budgetItemName, categoryId)

    override fun getBudgetItems(categoryIds: List<Int>): Flow<List<BudgetItem>> =
        budgetItemDao.getBudgetItemsByCategoryIds(categoryIds)
            .map { budgetItemList ->
                budgetItemList.map { budgetItem ->
                    budgetItem.toUiBudgetItem() } }

    override fun getBudgetItemIds(categoryIds: List<Int>): Flow<List<Int>> =
        budgetItemDao.getBudgetItemsIdsByCategoryIds(categoryIds)

    private fun com.ynab.data.dataSource.room.BudgetItem.toUiBudgetItem(): BudgetItem =
        BudgetItem(
            budgetItemId = budgetItemId,
            categoryId = categoryId,
            budgetItemName = budgetItemName,
            itemUiPosition = itemUiPosition
        )

    private fun BudgetItem.toRoomBudgetItem(): com.ynab.data.dataSource.room.BudgetItem =
        com.ynab.data.dataSource.room.BudgetItem(
            budgetItemId = budgetItemId,
            categoryId = categoryId,
            budgetItemName = budgetItemName,
            itemUiPosition = itemUiPosition
        )
}