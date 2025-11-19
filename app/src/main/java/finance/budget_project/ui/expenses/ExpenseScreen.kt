package finance.budget_project.ui.expenses

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import finance.budget_project.components.WelcomeHeader
import finance.budget_project.model.Expense
import finance.budget_project.model.ExpenseCategory
import kotlinx.coroutines.launch
import java.util.Locale

@Composable
fun ExpenseScreen(
    expenseViewModel: ExpenseViewModel = viewModel()
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Spacer(modifier = Modifier.height(20.dp))

        // --- HEADER ---
        WelcomeHeader()

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFF5F5F5))
                .padding(16.dp)
        ) {
            Text(
                text = "Overview",
                color = Color.Black,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 10.dp)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // --- BUDGET BOX ---
                InfoBox(
                    title = "Budget",
                    amount = expenseViewModel.budget.value,
                    textButton = "edit",
                    allowToAdd = true,
                    onAddClick = { expenseViewModel.showEditDialog = true },
                    modifier = Modifier.weight(1f)
                )

                if (expenseViewModel.showEditDialog) {
                    EditBudgetDialog(
                        expenseViewModel = expenseViewModel,
                        onDismiss = { expenseViewModel.showEditDialog = false }
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

                // --- TOTAL EXPENSE BOX ---
                InfoBox(
                    title = "Expenses",
                    amount = expenseViewModel.expenseTotal,
                    allowToAdd = false,
                    onAddClick = { /* TODO */ },
                    modifier = Modifier.weight(1f)
                )


                Spacer(modifier = Modifier.width(8.dp))

                // --- REST BOX ---
                InfoBox(
                    title = "Rest",
                    amount = expenseViewModel.restAvailable,
                    allowToAdd = false,
                    onAddClick = { /* TODO */ },
                    modifier = Modifier.weight(1f)
                )



            }
            Spacer(modifier = Modifier.height(20.dp))

            // 👉 Barre de progression du budget
            BudgetProgressBar(
                budget = expenseViewModel.budget.value,
                spent = expenseViewModel.expenseTotal,
                expenseViewModel = expenseViewModel
            )
        }


        // --- BOUTON POUR AJOUTER UNE DÉPENSE ---
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.Start
        ) {
            OutlinedButton(
                onClick = { expenseViewModel.showDialog = true },
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.outlinedButtonColors(containerColor = Color.White),
                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = null, tint = Color.Black)
                Spacer(Modifier.width(4.dp))
                Text("Add Expenses", color = Color.Black)
            }
        }


        // --- LISTE DES DÉPENSES ---
        LazyColumn(Modifier.fillMaxWidth().padding(16.dp)) {
            items(expenseViewModel.expenses) { expense ->
                ExpenseItem(expense, expenseViewModel = expenseViewModel)
            }
        }

        if (expenseViewModel.showDialog) {
            AddExpenseDialog(
                onDismiss = { expenseViewModel.showDialog = false },
                expenseViewModel = expenseViewModel,
                // onAddExpense = { expenseViewModel.addExpense(it) }
            )
        }
    }
}


@Composable
fun ExpenseItem(expense: Expense, expenseViewModel: ExpenseViewModel) {

    val amountText = expenseViewModel.amount.trim()
    val amountValue = if (amountText.isNotEmpty()) amountText.toDouble() else 0.0
    val lineColor = if (amountValue >= 0.0) Color(0xFFF44336) else Color(0xFF4CAF50)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
            .background(Color.White, shape = RoundedCornerShape(20.dp))
            .drawBehind {
                val shadowWidth = 15.dp.toPx()
                val gradient = Brush.horizontalGradient(
                    colors = listOf(lineColor, Color.Transparent),
                    startX = 0f,
                    endX = shadowWidth
                )
                drawRect(
                    brush = gradient,
                    size = Size(shadowWidth, size.height)
                )
            }
            .padding(vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {


        Spacer(modifier = Modifier.width(16.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 12.dp, end = 20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(50.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(expense.category.color.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = expense.category.icon,
                    contentDescription = expense.category.name,
                    tint = expense.category.color,
                    modifier = Modifier.size(28.dp)
                )
            }

            Spacer(Modifier.width(16.dp))

            Text(
                text = expense.name,
                fontSize = 18.sp,
                color = Color.Black,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.weight(1f)
            )

            Text(
                text = "- ${expense.amount}€",
                fontSize = 18.sp,
                color = Color.Red,
                fontWeight = FontWeight.Bold
            )
        }
    }
}


@Composable
fun InfoBox(
    modifier: Modifier = Modifier,
    title: String,
    amount: Double,
    textButton: String = "",
    allowToAdd : Boolean = false,
    onAddClick: () -> Unit,

) {
    Column(
        modifier = modifier
            .shadow(4.dp, RoundedCornerShape(16.dp))
            .background(Color.White, RoundedCornerShape(16.dp))
            .border(1.dp, Color(0xFFE0E0E0), RoundedCornerShape(16.dp))
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = title, fontSize = 14.sp, color = Color.Gray)

        Text(
            text = buildAnnotatedString {
                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                    append(String.format(Locale.US, "%.2f", amount))
                }
                withStyle(style = SpanStyle(fontWeight = FontWeight.Normal)) {
                    append(" EUR")
                }
            },
            color = Color.Black,
            fontSize = 13.sp
        )

        if (allowToAdd) {
            OutlinedButton(
                onClick = onAddClick,
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.outlinedButtonColors(containerColor = Color.White),
                contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                modifier = Modifier.padding(top = 8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = textButton,
                    tint = Color.Black,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(text = textButton, color = Color.Black, fontSize = 14.sp)
            }
        }else{
            Spacer(modifier = Modifier.height(12.dp))

        }
    }
}




@Composable
fun BudgetProgressBar(
    budget: Double,
    spent: Double,
    expenseViewModel: ExpenseViewModel,
    modifier: Modifier = Modifier
) {

    Column(
        modifier = modifier.padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Label
        Text(
            text = "Budget usage",
            color = Color.Black,
            fontSize = 18.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        // Barre de progression stylée
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(22.dp)
                .clip(RoundedCornerShape(50))
                .background(Color(0xFFE0E0E0)) // gris clair (fond)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(expenseViewModel.progress)
                    .fillMaxHeight()
                    .clip(RoundedCornerShape(50))
                    .background(
                        brush = Brush.horizontalGradient(
                            colors = listOf(
                                Color(0xFF7115E7), // violet
                                Color(0xFFB388FF)  // violet clair
                            )
                        )
                    )
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = if (budget > 0) {
                "${String.format(Locale.US,"%.2f", spent)}€ / ${String.format(Locale.US,"%.2f", budget)}€ (${String.format(Locale.US,"%.0f", expenseViewModel.progress * 100)}%)"
            } else {
                "0.00€ / 0.00€ (0%)"
            },
            color = if (expenseViewModel.progress < 0.7f) Color.Black else Color(0xFF8131E4),
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun AddExpenseDialog(
    expenseViewModel: ExpenseViewModel,
    onDismiss: () -> Unit,

    ) {
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            Button(onClick = {
                val amountValue = expenseViewModel.amount.toFloatOrNull() ?: 0f
                val newExpense = Expense(
                    name = expenseViewModel.name,
                    amount = amountValue,
                    category = expenseViewModel.selectedCategory
                )
                coroutineScope.launch {
                    val added = expenseViewModel.addExpense(newExpense)
                    if (added) {
                        onDismiss()
                    } else {
                        Toast.makeText(context, "Budget exceeded!", Toast.LENGTH_SHORT).show()
                    }
                }
            }) {
                Text("Add")
            }
        },
        dismissButton = {
            OutlinedButton(onClick = onDismiss) {
                Text("Cancel")
            }
        },
        title = { Text("Add Expense") },
        text = {
            Column {
                OutlinedTextField(
                    value = expenseViewModel.name,
                    onValueChange = { expenseViewModel.name = it },
                    label = { Text("Expense name") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = expenseViewModel.amount,
                    onValueChange = { input ->
                        val regex = Regex("^\\d*\\.?\\d{0,2}$")
                        if (input.isEmpty() || regex.matches(input)) {
                            expenseViewModel.amount = input
                        }
                    },
                    label = { Text("Amount (€)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text("Choose category", fontWeight = FontWeight.Bold)

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    ExpenseCategory.entries.forEach { category ->
                        IconButton(onClick = {
                            expenseViewModel.selectedCategory = category

                        }) {
                            Icon(
                                imageVector = category.icon,
                                contentDescription = category.displayName,
                                tint = category.color
                            )
                        }
                    }
                }

            }
        }
    )
}



@Composable
fun EditBudgetDialog(
    expenseViewModel: ExpenseViewModel,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            Button(onClick = {
                val newBudgetText = expenseViewModel.newBudgetText
                val newBudget = newBudgetText.toDoubleOrNull()

                if (newBudget != null && newBudget >= expenseViewModel.expenseTotal) {
                    coroutineScope.launch {
                        expenseViewModel.updateBudget(newBudget)
                        onDismiss()
                    }
                } else {
                    Toast.makeText(
                        context,
                        "Budget must be ≥ total expenses!",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }) {
                Text("Save")
            }
        },
        dismissButton = {
            OutlinedButton(onClick = onDismiss) {
                Text("Cancel")
            }
        },
        title = { Text("Edit Budget") },
        text = {
            Column {
                OutlinedTextField(
                    value = expenseViewModel.newBudgetText,
                    onValueChange = { input ->
                        val regex = Regex("^\\d*\\.?\\d{0,2}$")
                        if (input.isEmpty() || regex.matches(input)) {
                            expenseViewModel.newBudgetText = input
                        }
                    },
                    label = { Text("Budget (€)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    )
}


