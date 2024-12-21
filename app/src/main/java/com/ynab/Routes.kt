package com.ynab

import androidx.compose.ui.graphics.vector.ImageVector
import kotlinx.serialization.Serializable

@Serializable
data object Login

@Serializable
data object Register

@Serializable
data class Splash(val isNewUser: Boolean)

@Serializable
data object Budget

@Serializable
data class BudgetItem(val budgetItemId: Int)

@Serializable
data class Goal(val goalId: Int)

@Serializable
data object AddGoal

@Serializable
data object AddTransaction

@Serializable
data object Accounts

@Serializable
data class Transactions(val isAllTransactions: Boolean, val accountId: Int)

@Serializable
data class Transaction(val transactionId: Int)

@Serializable
data object AddAccount

@Serializable
data object Settings

data class TopLevelRoute<T: Any>(val name: String, val route: T, val icon: ImageVector)