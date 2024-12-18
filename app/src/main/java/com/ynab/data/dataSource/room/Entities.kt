package com.ynab.data.dataSource.room

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import java.math.BigDecimal

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

class Converter {
    @TypeConverter
    fun fromBigDecimal(value: BigDecimal): String = value.toPlainString()

    @TypeConverter
    fun toBigDecimal(value: String): BigDecimal = value.toBigDecimal()
}