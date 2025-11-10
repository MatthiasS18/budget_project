package finance.budget_project.ui.homePage

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import finance.budget_project.R
import finance.budget_project.model.Expense
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun HomePageScreen(
    homePageViewModel: HomePageViewModel = HomePageViewModel()
) {
    // --- Liste unique des dépenses ---
    val expenses = listOf(
        Expense("Food", 30f, Icons.Default.Restaurant, Color(0xFFFF9800)),
        Expense("Transport", 20f, Icons.Default.DirectionsCar, Color(0xFF4CAF50)),
        Expense("Rent", 50f, Icons.Default.Home, Color(0xFF2196F3)),
        Expense("Water", 10f, Icons.Default.WaterDrop, Color(0xFF03A9F4))
    )

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
                .padding(20.dp),
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

        // --- GRAPH ---
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.40f),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            ExpensePieChart(expenses = expenses)
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

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White)
            ) {
                items(expenses.size) { index ->
                    val expense = expenses[index]
                    ExpenseCard(expense)
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
            imageVector = expense.icon,
            contentDescription = expense.name,
            tint = expense.color.copy(alpha = 0.8f), // couleur plus douce, effet ombre
            modifier = Modifier
                .size(50.dp)
                .background(
                    color = expense.color.copy(alpha = 0.15f),
                    shape = RoundedCornerShape(10.dp),

                )
                .padding(6.dp)
        )


        Text(
            text = expense.name,
            color = Color.Black,
            modifier = Modifier.padding(start = 40.dp)
        )
        Spacer(modifier = Modifier.weight(1f))
        Text(
            text = "${expense.amount}€",
            color = expense.color,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun ExpensePieChart(expenses: List<Expense>) {
    val total = expenses.sumOf { it.amount.toDouble() }.toFloat()

    Canvas(modifier = Modifier.fillMaxSize().padding(50.dp)) {
        val radius = (size.minDimension / 2) * 0.9f
        val center = Offset(size.width / 2, size.height / 2)
        var startAngle = -90f

        for (expense in expenses) {
            val sweepAngle = (expense.amount / total) * 360f
            val arcSize = Size(radius * 2, radius * 2)

            drawArc(
                color = expense.color,
                startAngle = startAngle,
                sweepAngle = sweepAngle,
                useCenter = true,
                topLeft = Offset(center.x - radius, center.y - radius),
                size = arcSize
            )

            val middleAngle = startAngle + sweepAngle / 2
            val angleRad = Math.toRadians(middleAngle.toDouble())
            val circleRadius = radius
            val circleCenter = Offset(
                x = center.x + circleRadius * cos(angleRad).toFloat(),
                y = center.y + circleRadius * sin(angleRad).toFloat()
            )

            drawCircle(
                color = expense.color.copy(alpha = 0.3f),
                radius = 25f,
                center = circleCenter
            )

            drawCircle(
                color = Color.White,
                radius = 15f,
                center = circleCenter
            )

            drawCircle(
                color = expense.color,
                radius = 13f,
                center = circleCenter
            )

            val textDistance = 50f
            val iconSize = 40f
            val textCenter = Offset(
                x = circleCenter.x + textDistance * cos(angleRad).toFloat(),
                y = circleCenter.y + textDistance * sin(angleRad).toFloat()
            )
            val total = expenses.sumOf { it.amount.toDouble() }
            val percent = (expense.amount / total * 100).toInt()
            val textAlign = when {
                middleAngle in -90f..90f -> android.graphics.Paint.Align.LEFT
                else -> android.graphics.Paint.Align.RIGHT
            }
            drawContext.canvas.nativeCanvas.apply {

                // texte
                val textX = textCenter.x + if (textAlign == android.graphics.Paint.Align.LEFT) iconSize/2 + 8f else -iconSize/2 - 8f
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


            startAngle += sweepAngle
        }

        drawCircle(
            color = Color.White,
            radius = radius * 0.77f,
            center = center
        )
    }
}



