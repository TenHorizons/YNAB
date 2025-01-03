package com.ynab.data.dataSource.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate
import java.time.YearMonth

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
    @Query("select * from `transaction` where transactionId = :transactionId")
    fun getTransactionById(transactionId: Int): Transaction?
    @Query("select * from `transaction` where date between :startDate and :endDate")
    fun getTransactionsByLocalDateRange(startDate: LocalDate, endDate: LocalDate): Flow<List<Transaction>>
    @Query("select * from `transaction` where date between :startDate and :endDate and budgetItemId = :budgetItemId")
    fun getTransactionsByLocalDateRangeAndBudgetItemId(startDate: LocalDate, endDate: LocalDate, budgetItemId: Int): Flow<List<Transaction>>

    @Insert
    fun insert(transaction:Transaction): Long // returns the id of the added record
    @Update
    fun update(transaction: Transaction): Int //returns number of rows updated
    @Delete
    fun delete(transaction: Transaction)
}

@Dao
interface BudgetItemDao {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    fun insert(budgetItems: List<BudgetItem>)
    @Query("select budgetItemId from budgetItem where budgetItemName = :budgetItemName and categoryId = :categoryId")
    fun getBudgetItemId(budgetItemName: String, categoryId: Int): Int
    @Query("select * from budgetItem where categoryId in (:categoryIds)")
    fun getBudgetItemsByCategoryIds(categoryIds: List<Int>): Flow<List<BudgetItem>>
    @Query("select budgetItemId from budgetItem where categoryId in (:categoryIds)")
    fun getBudgetItemsIdsByCategoryIds(categoryIds: List<Int>): Flow<List<Int>>
}

@Dao
interface CategoryDao {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    fun insert(budgetItems: List<Category>)
    @Query("select categoryId from category where categoryName = :categoryName and budgetId = :budgetId")
    fun getCategoryId(categoryName: String, budgetId: Int): Int
    @Query("select * from category where budgetId = :budgetId")
    fun getCategoriesByBudgetId(budgetId: Int): Flow<List<Category>>
}

@Dao
interface BudgetItemEntryDao {
    @Query("select * from budgetItemEntry where budgetItemId in (:budgetItemIds) and yearMonth = :yearMonth")
    fun getBudgetItemEntriesByBudgetIdsAndYearMonth(budgetItemIds: List<Int>, yearMonth: YearMonth): Flow<List<BudgetItemEntry>>
    @Query("select exists (select * from budgetItemEntry where budgetItemId = :budgetItemId and yearMonth = :yearMonth)")
    fun isBudgetItemEntryExist(budgetItemId: Int, yearMonth: YearMonth): Boolean
    @Query("select * from budgetItemEntry where budgetItemId = :budgetItemId and yearMonth = :yearMonth")
    fun getBudgetItemEntryByBudgetItemIdAndYearMonth(budgetItemId: Int, yearMonth: YearMonth): Flow<BudgetItemEntry>
    @Query("select * from budgetItemEntry where budgetItemEntryId = :budgetItemEntryId")
    fun getBudgetItemEntryByBudgetItemEntryId(budgetItemEntryId: Int): Flow<BudgetItemEntry>

    @Insert
    fun insert(budgetItemEntry: BudgetItemEntry)
    @Insert
    fun insert(budgetItemEntries: List<BudgetItemEntry>)
    @Update
    fun update(budgetItemEntry: BudgetItemEntry)
}