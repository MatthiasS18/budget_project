package finance.budget_project.ui.expenses

import android.util.Log
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.Fastfood
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import finance.budget_project.R
import finance.budget_project.model.Expense
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import finance.budget_project.model.ExpenseCategory

@Composable
fun ExpenseScreen(
    expenseViewModel: ExpenseViewModel = ExpenseViewModel()
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Spacer(modifier = Modifier.height(20.dp))

        // --- HEADER ---
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(bottomStart = 15.dp, bottomEnd = 15.dp))
                .background(Color(129, 49, 228))
                .padding(15.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(
                    painter = painterResource(id = R.drawable.example_face),
                    contentDescription = stringResource(R.string.example_image_photo),
                    modifier = Modifier
                        .size(80.dp)
                        .clip(CircleShape)
                        .border(2.dp, Color.Transparent, CircleShape),
                    contentScale = ContentScale.Crop
                )

                Spacer(modifier = Modifier.size(12.dp))

                Column {
                    Text(
                        text = "Welcome",
                        style = typography.bodyMedium,
                        color = Color.White,
                        fontSize = 25.sp
                    )
                    Text(
                        text = "Cartman 👋",
                        style = typography.titleMedium,
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }



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
                    amount = expenseViewModel.budget,
                    textButton= "edit",
                    allowToAdd = true,
                    onAddClick = { /* TODO */ },
                    modifier = Modifier.weight(1f)
                )

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
                budget = expenseViewModel.budget,
                spent = expenseViewModel.expenseTotal
            )
        }


        // --- BOUTON POUR AJOUTER UNE DÉPENSE ---
        OutlinedButton(
            onClick = { expenseViewModel.showDialog = true },
            shape = RoundedCornerShape(10.dp),
            colors = ButtonDefaults.outlinedButtonColors(containerColor = Color.White),
            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
        ) {
            Icon(Icons.Default.Add, contentDescription = null, tint = Color.Black)
            Spacer(Modifier.width(4.dp))
            Text("Add Expense", color = Color.Black)
        }

        // --- LISTE DES DÉPENSES ---
        LazyColumn(Modifier.fillMaxWidth().padding(16.dp)) {
            items(expenseViewModel.expenses) { expense ->
                ExpenseItem(expense)
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
fun ExpenseItem(expense: Expense) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
            .shadow(4.dp, RoundedCornerShape(20.dp))
            .background(Color.White)
            .padding(horizontal = 20.dp, vertical = 16.dp),
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
            text = expense.category.name,
            fontSize = 18.sp,
            color = Color.Black,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.weight(1f)
        )

        Text(
            text = "${expense.amount}€",
            fontSize = 18.sp,
            color = expense.category.color,
            fontWeight = FontWeight.Bold
        )
    }
}


@Composable
fun InfoBox(
    title: String,
    amount: Double,
    textButton: String = "",
    allowToAdd : Boolean = false,
    onAddClick: () -> Unit,
    modifier: Modifier = Modifier
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
                    append("$amount")
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
    modifier: Modifier = Modifier
) {
    val progress = (spent.toFloat() / budget.toFloat()).coerceIn(0f, 1f)
    val percentText = "${(progress * 100).toInt()}%"

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
                    .fillMaxWidth(progress)
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

        // Texte sous la barre
        Text(
            text = "$spent€ / $budget€ ($percentText)",
            color = if (progress < 0.7f) Color.Black else Color(0xFF8131E4),
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

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            Button(onClick = {
                val amountValue = expenseViewModel.amount.toFloatOrNull() ?: 0f
                if (expenseViewModel.name.isNotBlank() && amountValue > 0f) {
                    expenseViewModel.addExpense(
                        Expense(
                            name = expenseViewModel.name,
                            amount = amountValue,
                            category =  expenseViewModel.selectedCategory

                        )
                    )
                    onDismiss()
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
                    onValueChange = { expenseViewModel.amount = it },
                    label = { Text("Amount (€)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text("Choose category", fontWeight = FontWeight.Bold)

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    IconButton(onClick = {
                        expenseViewModel.selectedIcon = Icons.Default.Fastfood
                        expenseViewModel.selectedColor = Color(0xFFFFA726)
                    }) {
                        Icon(
                            Icons.Default.Fastfood,
                            contentDescription = null,
                            tint = Color(0xFFFFA726)
                        )
                    }
                    IconButton(onClick = {
                        expenseViewModel.selectedIcon = Icons.Default.Home
                        expenseViewModel.selectedColor = Color(0xFF42A5F5)
                    }) {
                        Icon(
                            Icons.Default.Home,
                            contentDescription = null,
                            tint = Color(0xFF42A5F5)
                        )
                    }
                    IconButton(onClick = {
                        expenseViewModel.selectedIcon = Icons.Default.WaterDrop
                        expenseViewModel.selectedColor = Color(0xFF03A9F4)
                    }) {
                        Icon(
                            Icons.Default.WaterDrop,
                            contentDescription = null,
                            tint = Color(0xFF03A9F4)
                        )
                    }
                    IconButton(onClick = {
                        expenseViewModel.selectedIcon = Icons.Default.DirectionsCar
                        expenseViewModel.selectedColor = Color(0xFF4CAF50)
                    }) {
                        Icon(
                            Icons.Default.DirectionsCar,
                            contentDescription = null,
                            tint = Color(0xFF4CAF50)
                        )
                    }
                }
            }
        }
    )
}

