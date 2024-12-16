package com.ynab.data.dataSource.room

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(indices = [Index(value = ["username"], unique = true)])
data class User(
    @PrimaryKey(autoGenerate = true) val userId: Int = 0,
    var username: String,
    var password: String,
    var lastBudgetId: Int = 0
)