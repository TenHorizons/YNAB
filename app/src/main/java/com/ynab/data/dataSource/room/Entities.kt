package com.ynab.data.dataSource.room

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import java.math.BigDecimal
import java.time.LocalDate
import java.time.YearMonth

@Entity(indices = [Index(value = ["username"], unique = true)])
data class User(
    @PrimaryKey(autoGenerate = true) val userId: Int = 0,
    var username: String,
    var password: String,
    var lastBudgetId: Int = 0
)

@Entity(indices = [Index(value = ["budgetId", "accountName"], unique = true)])
data class Account(
    @PrimaryKey(autoGenerate = true) val accountId: Int = 0,
    val budgetId: Int,
    var accountName: String,
    var uiPosition: Int
)

@Entity(
    indices = [
        Index(value = ["accountId", "date"]),
        Index(value = ["budgetItemId", "date"])
    ]
)
data class Transaction(
    @PrimaryKey(autoGenerate = true) val transactionId: Int = 0,
    var accountId: Int,
    var budgetItemId: Int,
    var amount: BigDecimal,
    var date: LocalDate,
    var memo: String
)

@Entity(indices = [Index(value = ["username", "budgetName"], unique = true)])
data class Budget(
    @PrimaryKey(autoGenerate = true) val budgetId: Int = 0,
    val userId: Int,
    var budgetName: String,
    var uiPosition: Int
)

@Entity(indices = [Index(value = ["budgetId", "categoryName"], unique = true)])
data class Category(
    @PrimaryKey(autoGenerate = true) val categoryId: Int = 0,
    val budgetId: Int,
    var categoryName: String,
    var categoryUiPosition: Int
)

@Entity(indices = [Index(value = ["categoryId", "budgetItemName"], unique = true)])
data class BudgetItem(
    @PrimaryKey(autoGenerate = true) val budgetItemId: Int = 0,
    var categoryId: Int,
    var budgetItemName: String,
    var itemUiPosition: Int
)

@Entity(indices = [Index(value = ["budgetItemId","yearMonth"], unique = true)])
data class BudgetItemEntry(
    @PrimaryKey(autoGenerate = true) val budgetItemEntryId: Int = 0,
    var budgetItemId: Int,
    var yearMonth: YearMonth,
    var assigned: BigDecimal,
    var rolloverBalance: BigDecimal
)

class Converter {
    @TypeConverter
    fun fromBigDecimal(value: BigDecimal): String = value.toPlainString()

    @TypeConverter
    fun toBigDecimal(value: String): BigDecimal = value.toBigDecimal()

    @TypeConverter
    fun fromLocalDate(value: LocalDate): String = value.toString()

    @TypeConverter
    fun toLocalDate(value: String): LocalDate = LocalDate.parse(value)

    @TypeConverter
    fun fromYearMonth(value: YearMonth): String = value.toString()

    @TypeConverter
    fun toYearMonth(value: String): YearMonth {
        val parts = value.split("-")
        return YearMonth.of(parts[0].toInt(), parts[1].toInt())
    }
}