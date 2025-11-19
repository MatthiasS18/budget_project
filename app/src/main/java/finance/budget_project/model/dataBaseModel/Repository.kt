package finance.budget_project.model.dataBaseModel


import android.content.Context
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateListOf
import finance.budget_project.database.budgetDatabase.BudgetEntity
import finance.budget_project.database.expenseDatabase.ExpenseDatabase
import finance.budget_project.database.expenseDatabase.ExpenseEntity
import finance.budget_project.model.Expense


object Repository {

    private val expensesList = mutableStateListOf<Expense>()
    val expenses: List<Expense> get() = expensesList

    var budget = mutableDoubleStateOf(0.0)

    val expenseTotal: Double get() = expensesList.sumOf { it.amount.toDouble() }
    val restAvailable: Double get() = budget.value - expenseTotal

    private var database: ExpenseDatabase? = null





    /**
     * Initializes the database if it is not already initialized.
     *
     * @param context The context to use for initializing the database.
     */
    fun initDataBase(context: Context) {
        if (database == null) {
            database = ExpenseDatabase.getInstance(context)
        }
    }


    /**
     * Retrieves all expense items from the database.
     *
     * @return A list of LessonItem objects.
     */
    suspend fun getAllExpensesFromDatabase(): List<ExpenseEntity> {
        database?.let { theDatabase ->
            return theDatabase.theDAO().getAllExpenses()
        }
        return listOf()
    }



    /**
     * Inserts a new expense into the database.
     *
     * @param expense The ExpenseEntity object to insert.
     */
    suspend fun insertExpenseInDatabase(expense: ExpenseEntity) {
        database?.theDAO()?.insertExpense(expense)
    }

    /**
     * Deletes a specific expense from the database.
     *
     * @param expenseId The ExpenseEntity object to delete.
     */
    private suspend fun deleteExpense(expenseId: Int) {
        database?.theDAO()?.delete(expenseId)
    }

    /**
     * Checks if an expense is present in the database.
     *
     * @param expenseId The ID of the expense to check.
     * @return True if the expense is present, false otherwise.
     */
    suspend fun isExpenseInDatabase(expenseId: Int): Boolean {
        return database?.theDAO()?.getExpenseById(expenseId) != null
    }

    /**
     * Deletes all expenses from the database.
     */
    suspend fun deleteAllExpenses() {
        database?.theDAO()?.deleteAll()
    }

    /**
     * Removes an expense from the database if it exists, and updates the repository list.
     *
     * @param expenseId The ID of the expense to remove.
     */
    suspend fun removeExpenseInDb(expenseId: Int) {
        if (isExpenseInDatabase(expenseId)) {
            deleteExpense(expenseId = expenseId)
            clearExpenses()
            expenses
        }
    }



    fun addExpense(expense: Expense): Boolean {
        return if (expenseTotal + expense.amount <= budget.value) {
            expensesList.add(expense)
            true
        } else false
    }

    fun updateBudget(newBudget: Double) {
        budget.value = newBudget
    }

    fun clearExpenses() {
        expensesList.clear()
    }

    fun setExpenses(list: List<Expense>) {
        expensesList.clear()
        expensesList.addAll(list)
    }

    suspend fun getBudgetFromDatabase(): Double {
        return database?.theDAO()?.getBudget()?.amount ?: 0.0
    }

    suspend fun loadBudget() {
        val saved = database?.theDAO()?.getBudget()?.amount ?: 0.0
        budget.value = saved
    }


    suspend fun saveBudgetToDatabase(amount: Double) {
        budget.value = amount
        database?.theDAO()?.saveBudget(BudgetEntity(amount = amount))
    }

    suspend fun getTotalSpentFromDatabase(): Double {
        return database?.theDAO()?.getTotalSpent() ?: 0.0
    }


}

