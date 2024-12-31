package com.ynab.data.dataSource.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(
    entities = [
        User::class,
        Account::class,
        Transaction::class,
        BudgetItem::class,
        Category::class
    ],
    version = 5
)
@TypeConverters(Converter::class)
abstract class RoomDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun accountDao(): AccountDao
    abstract fun transactionDao(): TransactionDao
    abstract fun budgetItemDao(): BudgetItemDao
    abstract fun categoryDao(): CategoryDao
}