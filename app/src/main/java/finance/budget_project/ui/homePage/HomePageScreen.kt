package finance.budget_project.ui.homePage

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import finance.budget_project.components.WelcomeHeader
import finance.budget_project.model.Expense
import finance.budget_project.model.dataBaseModel.Repository
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun HomePageScreen(
    homePageViewModel: HomePageViewModel = viewModel()
) {
    // --- Liste unique des dépenses ---


    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Spacer(modifier = Modifier.height(20.dp))

        // --- HEADER ---
        WelcomeHeader()

        // --- GRAPH ---
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.40f),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            ExpensePieChart(expenses = Repository.expenses)
        }

        // --- LISTE LIÉE ---
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(horizontal = 12.dp)
                .background(Color.White)
        ) {
            Text(
                text = "Expenses",
                color = Color.Black,
                fontSize = 20.sp,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            if (Repository.expenses.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No expenses yet",
                        color = Color.Gray,
                        fontSize = 18.sp
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.White)
                ) {
                    items(Repository.expenses.size) { index ->
                        val expense = Repository.expenses[index]
                        ExpenseCard(expense)
                    }
                }
            }
        }
    }
}

@Composable
fun ExpenseCard(expense: Expense) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp, horizontal = 20.dp)
            .shadow(8.dp, RoundedCornerShape(20.dp))
            .background(Color.White)
            .border(2.dp, Color(242, 242, 242), shape = RoundedCornerShape(20.dp))
            .padding(horizontal = 16.dp, vertical = 32.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = expense.category.icon,
            contentDescription = expense.category.name,
            tint = expense.category.color.copy(alpha = 0.8f),
            modifier = Modifier
                .size(50.dp)
                .background(
                    color = expense.category.color.copy(alpha = 0.15f),
                    shape = RoundedCornerShape(10.dp),

                )
                .padding(6.dp)
        )


        Text(
            text = expense.category.name,
            color = Color.Black,
            modifier = Modifier.padding(start = 40.dp)
        )
        Spacer(modifier = Modifier.weight(1f))
        Text(
            text = "${expense.amount}€",
            color = expense.category.color,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun ExpensePieChart(expenses: List<Expense>) {

    Canvas(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 30.dp, vertical = 58.dp)
    ) {
        val radius = (size.minDimension / 2) * 1.2f
        val center = Offset(size.width / 2, size.height / 2)
        val total = expenses.sumOf { it.amount.toDouble() }

        if (total == 0.0) {
            // --- Aucun expense : afficher un cercle gris clair ---
            drawCircle(
                color = Color(0xFFE0E0E0),
                radius = radius,
                center = center
            )
            drawCircle(
                color = Color.White,
                radius = radius * 0.77f,
                center = center
            )
            drawContext.canvas.nativeCanvas.apply {
                drawText(
                    "No data",
                    center.x,
                    center.y + 12f,
                    android.graphics.Paint().apply {
                        color = android.graphics.Color.GRAY
                        textSize = 40f
                        textAlign = android.graphics.Paint.Align.CENTER
                        isAntiAlias = true
                    }
                )
            }
        } else {
            // --- Données disponibles : dessiner le graphique ---
            var startAngle = -90f

            for (expense in expenses) {
                val sweepAngle = (expense.amount / total) * 360f
                val arcSize = Size(radius * 2, radius * 2)

                drawArc(
                    color = expense.category.color,
                    startAngle = startAngle,
                    sweepAngle = sweepAngle.toFloat(),
                    useCenter = true,
                    topLeft = Offset(center.x - radius, center.y - radius),
                    size = arcSize
                )

                val middleAngle = startAngle + sweepAngle / 2
                val angleRad = Math.toRadians(middleAngle)
                val circleCenter = Offset(
                    x = center.x + radius * cos(angleRad).toFloat(),
                    y = center.y + radius * sin(angleRad).toFloat()
                )

                drawCircle(
                    color = expense.category.color.copy(alpha = 0.3f),
                    radius = 25f,
                    center = circleCenter
                )

                drawCircle(
                    color = Color.White,
                    radius = 15f,
                    center = circleCenter
                )

                drawCircle(
                    color = expense.category.color,
                    radius = 13f,
                    center = circleCenter
                )

                val textDistance = 50f
                val iconSize = 40f
                val textCenter = Offset(
                    x = circleCenter.x + textDistance * cos(angleRad).toFloat(),
                    y = circleCenter.y + textDistance * sin(angleRad).toFloat()
                )
                val percent = (expense.amount / total * 100).toInt()
                val textAlign = when {
                    middleAngle in -90f..90f -> android.graphics.Paint.Align.LEFT
                    else -> android.graphics.Paint.Align.RIGHT
                }
                drawContext.canvas.nativeCanvas.apply {
                    val textX = textCenter.x + if (textAlign == android.graphics.Paint.Align.LEFT) iconSize / 2 + 8f else -iconSize / 2 - 8f
                    drawText(
                        "${percent}%",
                        textX,
                        textCenter.y,
                        android.graphics.Paint().apply {
                            color = android.graphics.Color.GRAY
                            textSize = 36f
                            isAntiAlias = true
                            this.textAlign = textAlign
                        }
                    )
                }

                startAngle += sweepAngle.toFloat()
            }

            drawCircle(
                color = Color.White,
                radius = radius * 0.77f,
                center = center
            )
        }
    }

}



