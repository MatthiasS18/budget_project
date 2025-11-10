package finance.budget_project.ui.homePage

import android.util.Log
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.dropShadow
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.shadow.Shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import finance.budget_project.R
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun HomePageScreen(
    homePageViewModel: HomePageViewModel = HomePageViewModel()
) {
    Column(
        modifier = Modifier
            .fillMaxSize().background(Color.White)
    ) {
        Spacer(modifier = Modifier.height(20.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(bottomStart = 15.dp, bottomEnd = 15.dp))
                .background(Color(129, 49, 228))
                .padding(20.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
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

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.42f),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        )
        {
            ExpensePieChart()
        }

        // --- TITRE + LISTE DÉFILABLE ---
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(horizontal = 12.dp, vertical = 6.dp).background(Color.White)
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
                items(30) { index ->
                    ExpenseCard(
                        title = "Expense #$index",
                        amount = "$${(10..300).random()}"
                    )
                }
            }
        }
    }
}

@Composable
fun ExpenseCard(title: String, amount: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp, horizontal = 20.dp)
            .shadow(8.dp, RoundedCornerShape(20.dp))
            .background(Color.White)
            .border(2.dp, Color(242,242,242), shape = RoundedCornerShape(20.dp))
            .padding(horizontal = 16.dp, vertical = 32.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            color = Color.Black,
            modifier = Modifier.padding(start = 80.dp)
        )
        Spacer(modifier = Modifier.weight(1f))
        Text(
            text = amount,
            color = Color(87, 26, 162)
        )
    }
}



@Composable
fun ExpensePieChart() {
    val expenses = listOf(
        Pair("Food", 30f ),
        Pair("Transport", 20f),
        Pair("Rent", 50f)
    )
    val colors = listOf(Color.Red, Color.Green, Color.Blue)
    val total = 100f

    Canvas(modifier = Modifier.fillMaxSize().padding(50.dp)) {
        val radius = (size.minDimension / 2) * 0.9f
        val center = Offset(size.width / 2, size.height / 2)
        var startAngle = -90f

        for ((i, expense) in expenses.withIndex()) {
            val amount = expense.second
            val sweepAngle = (amount / total) * 360f
            val arcSize = Size(radius * 2, radius * 2)

            // Dessiner l'arc
            drawArc(
                color = colors[i % colors.size],
                startAngle = startAngle,
                sweepAngle = sweepAngle,
                useCenter = true,
                topLeft = Offset(center.x - radius, center.y - radius),
                size = arcSize
            )

            // Petit cercle au bord extérieur
            val middleAngle = startAngle + sweepAngle / 2
            val angleRad = Math.toRadians(middleAngle.toDouble())
            val circleRadius = radius
            val circleCenter = Offset(
                x = center.x + circleRadius * cos(angleRad).toFloat(),
                y = center.y + circleRadius * sin(angleRad).toFloat()
            )


            drawCircle(
                color = colors[i % colors.size].copy(alpha = 0.3f),
                radius = 20f,
                center = circleCenter
            )


            drawCircle(
                color = Color.White,
                radius = 10f,
                center = circleCenter
            )


            drawCircle(
                color = colors[i % colors.size],
                radius = 8f,
                center = circleCenter
            )

            // Dessiner le texte à côté du cercle
            val textOffset = Offset(
                x = circleCenter.x + 50f,
                y = circleCenter.y + 50f   // léger ajustement vertical
            )
            drawContext.canvas.nativeCanvas.apply {
                drawText(
                    "${expense.second}%",
                    textOffset.x,
                    textOffset.y,
                    android.graphics.Paint().apply {
                        color = android.graphics.Color.BLACK
                        textSize = 32f
                        isAntiAlias = true
                    }
                )
            }



            startAngle += sweepAngle
        }

        // Donut
        drawCircle(
            color = Color.White,
            radius = radius * 0.8f,
            center = center
        )
    }
}

