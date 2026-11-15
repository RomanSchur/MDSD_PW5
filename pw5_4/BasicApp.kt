package com.example.pw5_4.composables

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlin.math.cos
import kotlin.math.sin
import kotlin.random.Random
@Composable
fun ReactivePowerSensorGauge(
    currentValue: Double,
    minValue: Double = 0.0,
    maxValue: Double = 250.0,
    modifier: Modifier = Modifier
) {
    val percentage = ((currentValue - minValue) / (maxValue - minValue)).coerceIn(0.0, 1.0).toFloat()
    Box(
        modifier = modifier.size(200.dp),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val strokeWidth = 16.dp.toPx()
            val arcColor = if (currentValue > 170.0) Color(0xFFEF4444) else Color(0xFF38BDF8)
            drawArc(
                color = Color(0xFF334155),
                startAngle = 135f,
                sweepAngle = 270f,
                useCenter = false,
                style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
            )
            drawArc(
                color = arcColor,
                startAngle = 135f,
                sweepAngle = 270f * percentage,
                useCenter = false,
                style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
            )
        }
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "ДАТЧИК Q",
                style = MaterialTheme.typography.labelSmall,
                color = Color(0xFF94A3B8)
            )
            Text(
                text = "${"%.1f".format(currentValue)}",
                style = MaterialTheme.typography.headlineMedium,
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "кВАр",
                style = MaterialTheme.typography.bodySmall,
                color = Color(0xFF38BDF8)
            )
        }
    }
}
@Composable
fun BasicApp() {
    var reactivePower by remember { mutableStateOf(145.2) }
    var sensorStatus by remember { mutableStateOf("Нормальний режим роботи") }
    var isSensorActive by remember { mutableStateOf(true) }
    LaunchedEffect(isSensorActive) {
        while (isSensorActive) {
            delay(1500)
            val delta = Random.nextDouble(-12.0, 12.0)
            reactivePower = (reactivePower + delta).coerceIn(20.0, 240.0)

            sensorStatus = if (reactivePower > 170.0) {
                "Увага: Критичне навантаження мережі!"
            } else {
                "Датчик справний, передача даних активна"
            }
        }
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(Color(0xFF0F172A), Color(0xFF1E293B))
                )
            )
            .systemBarsPadding()
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF334155)),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Електричні мережі з компенсацією реактивної потужності",
                        style = MaterialTheme.typography.titleSmall,
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF1E293B)),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .padding(24.dp)
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "ДАТЧИК РЕАКТИВНОЇ ПОТУЖНОСТІ",
                        style = MaterialTheme.typography.labelSmall,
                        color = Color(0xFF94A3B8),
                        letterSpacing = 1.1.sp
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                    ReactivePowerSensorGauge(currentValue = reactivePower)
                    Spacer(modifier = Modifier.height(20.dp))
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        modifier = Modifier
                            .clip(RoundedCornerShape(50))
                            .background(if (reactivePower > 170.0) Color(0xFF7F1D1D) else Color(0xFF065F46))
                            .padding(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .clip(RoundedCornerShape(50))
                                .background(if (reactivePower > 170.0) Color(0xFFF87171) else Color(0xFF34D399))
                        )
                        Text(
                            text = sensorStatus,
                            style = MaterialTheme.typography.bodySmall,
                            color = if (reactivePower > 170.0) Color(0xFFFEE2E2) else Color(0xFFD1FAE5)
                        )
                    }
                    Spacer(modifier = Modifier.height(24.dp))
                    Button(
                        onClick = { isSensorActive = !isSensorActive },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (isSensorActive) Color(0xFFEF4444) else Color(0xFF0284C7)
                        ),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = if (isSensorActive) "Вимкнути опитування датчика" else "Увімкнути опитування датчика",
                            style = MaterialTheme.typography.titleSmall,
                            color = Color.White,
                            modifier = Modifier.padding(vertical = 6.dp)
                        )
                    }
                }
            }
        }
    }
}