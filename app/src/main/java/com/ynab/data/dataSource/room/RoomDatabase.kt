package com.ynab.data.dataSource.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(
    entities = [
        User::class,
        Account::class,
        Transaction::class,
        Budget::class,
        BudgetItem::class,
        Category::class,
        BudgetItemEntry::class
    ],
    version = 7
)
@TypeConverters(Converter::class)
abstract class RoomDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun accountDao(): AccountDao
    abstract fun transactionDao(): TransactionDao
    abstract fun budgetDao(): BudgetDao
    abstract fun budgetItemDao(): BudgetItemDao
    abstract fun categoryDao(): CategoryDao
    abstract fun budgetItemEntryDao(): BudgetItemEntryDao
}