package com.ynab.domain

import android.content.Context
import android.util.Log
import com.ynab.TAG_PREFIX
import org.json.JSONObject
import javax.inject.Inject

private const val TAG = "${TAG_PREFIX}DefaultBudgetStructure"

data class BudgetCategory(
    val name: String,
    val items: List<String>
)

data class DefaultBudgetStructure(
    val categories: List<BudgetCategory>
)

class DefaultBudgetStructureLoader @Inject constructor(
    private val context: Context
) {
    /**
     * Loads the default budget structure from assets/default_budget_structure.json
     * Returns a map of category names to budget item names for backward compatibility
     */
    fun loadDefaultStructure(): Map<String, List<String>> {
        return try {
            val jsonString = context.assets.open("default_budget_structure.json")
                .bufferedReader()
                .use { it.readText() }
            
            val jsonObject = JSONObject(jsonString)
            val categoriesArray = jsonObject.getJSONArray("categories")
            
            val categoryMap = mutableMapOf<String, List<String>>()
            
            for (i in 0 until categoriesArray.length()) {
                val categoryObj = categoriesArray.getJSONObject(i)
                val categoryName = categoryObj.getString("name")
                val itemsArray = categoryObj.getJSONArray("items")
                
                val items = mutableListOf<String>()
                for (j in 0 until itemsArray.length()) {
                    items.add(itemsArray.getString(j))
                }
                
                categoryMap[categoryName] = items
            }
            
            Log.d(TAG, "Successfully loaded default budget structure with ${categoryMap.size} categories")
            categoryMap
        } catch (e: Exception) {
            Log.e(TAG, "Error loading default budget structure: ${e.stackTraceToString()}")
            // Return fallback hardcoded structure if file loading fails
            getFallbackStructure()
        }
    }
    
    /**
     * Fallback structure in case JSON file cannot be loaded
     */
    private fun getFallbackStructure(): Map<String, List<String>> {
        Log.w(TAG, "Using fallback budget structure")
        return mapOf(
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
}
