package com.ynab.data.dataSource.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    /**Gets the password of the user by their username.*/
    @Query("select password from user where username = :username")
    suspend fun getPassword(username: String): String?

    @Query("select exists (select username from user where username = :username)")
    suspend fun isUsernameExist(username: String): Boolean

    @Query("delete from user where username = :username")
    suspend fun deleteUser(username: String): Int

    @Query("select lastBudgetId from user where username = :username")
    fun getUserLastBudgetId(username: String): Flow<Int>

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(user: User): Long //OnConflictStrategy.ABORT: SQLiteConstraintException of username exists, returns the id of the added record

    @Update
    suspend fun update(user: User): Int //returns number of rows updated

    @Delete
    suspend fun delete(user: User): Int //returns number of rows deleted
}

@Dao
interface AccountDao {
    @Query("select * from account where budgetId = :budgetId")
    fun getAccountsByBudgetId(budgetId: Int): Flow<List<Account>>
    @Query("select exists (select accountName from account where budgetId = :budgetId and accountName = :accountName)")
    fun isAccountNameExist(accountName: String, budgetId: Int): Boolean
    @Query("select count(*) from account where budgetId = :budgetId")
    fun getNumberOfAccountsByBudgetId(budgetId: Int): Int
    @Query("select * from account where accountId = :accountId")
    fun getAccountById(accountId: Int): Account?

    @Insert(onConflict = OnConflictStrategy.ABORT)
    fun insert(account: Account): Long // returns the id of the added record

    @Update
    fun update(account: Account): Int //returns number of rows updated

    @Delete
    fun delete(account: Account)
}

@Dao
interface TransactionDao {
    @Query("select * from `transaction` where accountId = :accountId")
    fun getTransactionsByAccountId(accountId: Int): Flow<List<Transaction>>
    @Query("select * from `transaction` where accountId in (:accountIdList)")
    fun getTransactionsByAccountIdList(accountIdList: List<Int>): Flow<List<Transaction>>

    @Insert
    fun insert(transaction:Transaction): Long // returns the id of the added record
}