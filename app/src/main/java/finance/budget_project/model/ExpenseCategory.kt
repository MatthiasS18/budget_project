package finance.budget_project.model

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.Fastfood
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material.icons.filled.Wifi
import androidx.compose.ui.graphics.Color

enum class ExpenseCategory(
    val displayName: String,
    val icon: androidx.compose.ui.graphics.vector.ImageVector,
    val color: Color
) {
    FOOD("Food", Icons.Default.Fastfood, Color(0xFFFFA726)),
    TRANSPORT("Transport", Icons.Default.DirectionsCar, Color(0xFF66BB6A)),
    RENT("Rent", Icons.Default.Home, Color(0xFF42A5F5)),
    ELECTRICITY("Electricity", Icons.Default.Bolt, Color(0xFFFFD54F)),
    WATER("Water", Icons.Default.WaterDrop, Color(0xFF29B6F6)),
    INTERNET("Internet", Icons.Default.Wifi, Color(0xFF7E57C2)),
    ENTERTAINMENT("Leisure", Icons.Default.Movie, Color(0xFFEC407A))
}