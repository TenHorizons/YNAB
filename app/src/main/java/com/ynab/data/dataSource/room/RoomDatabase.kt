package com.ynab.data.dataSource.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(
    entities = [
        User::class,
        Account::class
    ],
    version = 2
)
@TypeConverters(Converter::class)
abstract class RoomDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun accountDao(): AccountDao
}