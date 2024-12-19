package com.ynab.data.dataSource.room

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import java.math.BigDecimal
import java.time.LocalDate

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
    var uiPosition: Int,
    var balance: BigDecimal
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

class Converter {
    @TypeConverter
    fun fromBigDecimal(value: BigDecimal): String = value.toPlainString()

    @TypeConverter
    fun toBigDecimal(value: String): BigDecimal = value.toBigDecimal()

    @TypeConverter
    fun toLocalDate(value: LocalDate): String = value.toString()

    @TypeConverter
    fun toLocalDate(value: String): LocalDate = LocalDate.parse(value)
}