package com.ynab.data.dataSource.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface UserDao {
    /**Gets the password of the user by their username.*/
    @Query("select password from user where username = :username")
    suspend fun getPassword(username: String): String?

    @Query("select exists (select username from user where username = :username)")
    suspend fun isUsernameExist(username: String): Boolean

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(user: User): Long //OnConflictStrategy.ABORT: SQLiteConstraintException of username exists

    @Update
    suspend fun update(user: User): Int //returns number of rows updated

    @Delete
    suspend fun delete(user: User): Int //returns number of rows deleted
}