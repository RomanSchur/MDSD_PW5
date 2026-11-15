package com.example.pw5_3.composables

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
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlin.random.Random
@Composable
fun BasicApp() {
    var reactivePower by remember { mutableStateOf(145.2) }
    var powerHistory by remember { mutableStateOf(listOf(140.0, 142.5, 145.0, 143.2, 145.2)) }
    var compensationStatus by remember { mutableStateOf("Оптимальна компенсація") }
    var isMonitoringActive by remember { mutableStateOf(true) }
    LaunchedEffect(isMonitoringActive) {
        while (isMonitoringActive) {
            delay(1500)
            val delta = Random.nextDouble(-10.0, 10.0)
            reactivePower = (reactivePower + delta).coerceIn(70.0, 230.0)
            powerHistory = (powerHistory + reactivePower).takeLast(10)
            compensationStatus = if (reactivePower > 170.0) {
                "Увага: Перевищення реактивної потужності!"
            } else {
                "Система стабільна, компенсатор активний"
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
                        .padding(20.dp)
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "МОНІТОРИНГ ПОТУЖНОСТІ В РЕАЛЬНОМУ ЧАСІ",
                        style = MaterialTheme.typography.labelSmall,
                        color = Color(0xFF94A3B8),
                        letterSpacing = 1.2.sp
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "${"%.1f".format(reactivePower)} кВАр",
                        style = MaterialTheme.typography.displayMedium,
                        color = Color(0xFF38BDF8),
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(120.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color(0xFF0F172A))
                            .padding(12.dp)
                    ) {
                        val lineColor = Color(0xFF38BDF8)
                        val gridColor = Color(0xFF334155)
                        Canvas(modifier = Modifier.fillMaxSize()) {
                            val width = size.width
                            val height = size.height
                            val maxVal = 250.0
                            val minVal = 50.0
                            val range = maxVal - minVal
                            for (i in 1..3) {
                                val y = height * (i / 4f)
                                drawLine(
                                    color = gridColor,
                                    start = androidx.compose.ui.geometry.Offset(0f, y),
                                    end = androidx.compose.ui.geometry.Offset(width, y),
                                    strokeWidth = 1.dp.toPx()
                                )
                            }
                            if (powerHistory.size > 1) {
                                val stepX = width / (powerHistory.size - 1)
                                val path = Path()
                                powerHistory.forEachIndexed { index, value ->
                                    val x = index * stepX
                                    val normalizedY = height - ((value - minVal) / range * height).toFloat()
                                    if (index == 0) {
                                        path.moveTo(x, normalizedY)
                                    } else {
                                        path.lineTo(x, normalizedY)
                                    }
                                }
                                drawPath(
                                    path = path,
                                    color = lineColor,
                                    style = Stroke(width = 3.dp.toPx())
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        modifier = Modifier
                            .clip(RoundedCornerShape(50))
                            .background(Color(0xFF065F46))
                            .padding(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .clip(RoundedCornerShape(50))
                                .background(Color(0xFF34D399))
                        )
                        Text(
                            text = compensationStatus,
                            style = MaterialTheme.typography.bodySmall,
                            color = Color(0xFFD1FAE5)
                        )
                    }
                    Spacer(modifier = Modifier.height(20.dp))
                    Button(
                        onClick = { isMonitoringActive = !isMonitoringActive },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (isMonitoringActive) Color(0xFFEF4444) else Color(0xFF0284C7)
                        ),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = if (isMonitoringActive) "Зупинити моніторинг" else "Запустити моніторинг",
                            style = MaterialTheme.typography.titleSmall,
                            color = Color.White,
                            modifier = Modifier.padding(vertical = 8.dp)
                        )
                    }
                }
            }
        }
    }
}