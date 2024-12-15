package com.ynab

import kotlinx.serialization.Serializable

@Serializable
data object Login

@Serializable
data object Register

@Serializable
data object Splash

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
data class Account(val accountId: Int)

@Serializable
data class Transaction(val transactionId: Int)

@Serializable
data object Settings