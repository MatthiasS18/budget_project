package finance.budget_project.database.expenseDatabase

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import finance.budget_project.database.budgetDatabase.BudgetEntity

@Dao
interface ExpenseDAO {

    /**
     * Inserts a expense item into the database.
     * If there is a conflict (e.g., an item with the same ID already exists), the insert is ignored.
     *
     * @param expense The expense item to insert.
     */
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertExpense(expense: ExpenseEntity)

    /**
     * Retrieves all expense items from the database.
     *
     * @return A list of all expense items.
     */
    @Query("SELECT * FROM expenses")
    suspend fun getAllExpenses(): List<ExpenseEntity>

    /**
     * Deletes a expense item from the database based on its content text.
     *
     * @param idExpense The content text of the expense item to delete.
     */
    @Query("DELETE FROM expenses WHERE id = :idExpense")
    suspend fun delete(idExpense: Int)

    /**
     * Retrieves a expense item from the database based on its content text.
     *
     * @param idExpense The content text of the expense item to retrieve.
     * @return The expense item with the specified content text, or null if no such item exists.
     */
    @Query("SELECT * FROM expenses WHERE id = :idExpense LIMIT 1")
    suspend fun getExpenseById(idExpense: Int): ExpenseEntity?

    /**
     * Deletes all expense items from the database.
     */
    @Query("DELETE FROM expenses")
    suspend fun deleteAll()


    // ---------- BUDGET ----------
    @Query("SELECT * FROM budget WHERE id = 0")
    suspend fun getBudget(): BudgetEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveBudget(budget: BudgetEntity)

    @Query("SELECT SUM(amount) FROM expenses")
    suspend fun getTotalSpent(): Double?
}