package com.ynab.data.repository.dataClass

import java.math.BigDecimal
import java.time.LocalDate

/** 
 * Attach a Goal to a Budget Item. Each Budget Item is limited to 1 Goal.
 * @param goalId: Identifier
 * @param budgetItemId: Budget Item Goal is attached to.
 * @param name: Name of goal.
 * @param notes: any notes e.g. why this goal is important, why this amount, etc.
 * @param goalType: type of Goal. Effects how the Goal is calculated and displayed in the UI.
 * 
 * In the background, Goal reads the Savings in a Budget Item to indicate if the savings
 * are enough to achieve goal. Savings = Expenses assigned to budget item - Income assigned to Budget Item.
 */
data class Goal(
    val goalId: Int,
    val budgetItemId: Int,
    val name: String,
    val notes: String,
    val goalType: GoalType
)

/** 
 * Base class for different goal types. Helps identify which goal type is assigned to budget item.
 */
sealed class GoalType {
    abstract val amount: BigDecimal
}

/** 
 * Goal for target savings on monthly basis.
 * e.g. I need RM800 monthly for groceries, so I set monthly goal type to remind myself 
 * that I need RM800 in this category every month.
 */
data class MonthlyGoal(
    override val amount: BigDecimal
) : GoalType()

/** 
 * Goal for a large eventual spending.
 * e.g. I will refresh my phone in 3 years.
 * I start saving for it 1 Aug 2025.
 * I plan to save RM3000 by target end date 1 Aug 2028.
 * I use this goal to plan how much I need to save per month.
 */
data class TargetGoal(
    override val amount: BigDecimal,
    val targetStartDate: LocalDate,
    val targetEndDate: LocalDate
) : GoalType()

/** 
 * Goal for target savings without any plan for spending.
 * e.g. I want to save RM30,000 in case I get laid off so I can last until I get a new one.
 * I use this goal to remind myself not to dip into these savings, or replenish quickly if I dip into it.
 */
data class SavingsGoal(
    override val amount: BigDecimal,
    val targetDate: LocalDate? = null // Optional target date
) : GoalType()
