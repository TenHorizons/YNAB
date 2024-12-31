package com.ynab.data.dataSource.room

import android.database.sqlite.SQLiteConstraintException
import android.util.Log
import com.ynab.TAG_PREFIX
import com.ynab.data.dataSource.LocalCategoryDataSource
import javax.inject.Inject

private const val TAG = "${TAG_PREFIX}RoomLocalCategoryDataSource"

class RoomLocalCategoryDataSource @Inject constructor(
    db: RoomDatabase
): LocalCategoryDataSource {
    private val categoryDao = db.categoryDao()

    /**Add new categories for a single budget.*/
    override fun addCategories(categoryNames: Set<String>, budgetId: Int) {
        val categories = categoryNames.map { categoryName ->
            Category(
                budgetId = budgetId,
                categoryName = categoryName,
                categoryUiPosition = categoryNames.indexOf(categoryName)
            )
        }
        try {
            categoryDao.insert(categories)
        } catch (e: SQLiteConstraintException) {
            Log.d(
                TAG,
                "addCategories threw SQLiteConstraintException, likely due to category/ies already exist."
            )
        } catch (e: Exception) {
            Log.d(TAG, "Unknown error at addCategories: ${e.stackTraceToString()}")
        }
    }

    /**Get category ID from category name and budget ID.*/
    override fun getCategoryId(categoryName: String, budgetId: Int): Int {
        try {
            return categoryDao.getCategoryId(categoryName, budgetId)
        } catch (e: SQLiteConstraintException) {
            Log.d(
                TAG,
                "addCategories threw SQLiteConstraintException, likely due to category(s) already exist."
            )
            throw e
        } catch (e: Exception) {
            Log.d(TAG, "Unknown error at addCategories: ${e.stackTraceToString()}")
            throw e
        }
    }
}