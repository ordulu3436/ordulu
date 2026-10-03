package com.example.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.CalorieOrange
import com.example.ui.theme.EmeraldPrimary
import kotlin.math.min

@Composable
fun CalorieGauge(
    consumedCalories: Int,
    targetCalories: Int,
    modifier: Modifier = Modifier,
    size: Dp = 190.dp
) {
    val progress = if (targetCalories > 0) {
        (consumedCalories.toFloat() / targetCalories.toFloat()).coerceIn(0f, 1.25f)
    } else 0f

    val animatedProgress = remember { Animatable(0f) }
    LaunchedEffect(progress) {
        animatedProgress.animateTo(
            targetValue = progress,
            animationSpec = tween(durationMillis = 850, easing = FastOutSlowInEasing)
        )
    }

    val isOverBudget = consumedCalories > targetCalories
    val remaining = targetCalories - consumedCalories

    val activeColorStart = if (isOverBudget) Color(0xFFEF4444) else EmeraldPrimary
    val activeColorEnd = if (isOverBudget) Color(0xFFDC2626) else CalorieOrange
    val trackColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f)

    Box(
        modifier = modifier
            .size(size)
            .testTag("calorie_gauge_container"),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize().padding(12.dp)) {
            val strokeWidth = 14.dp.toPx()
            val arcSize = Size(this.size.width, this.size.height)
            val topLeft = Offset(0f, 0f)

            val startAngle = 140f
            val totalSweep = 260f

            // Arka Plan Çizgisi
            drawArc(
                color = trackColor,
                startAngle = startAngle,
                sweepAngle = totalSweep,
                useCenter = false,
                topLeft = topLeft,
                size = arcSize,
                style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
            )

            // İlerleme Yayı
            val sweep = min(totalSweep, animatedProgress.value * totalSweep)
            if (sweep > 0f) {
                drawArc(
                    brush = Brush.sweepGradient(
                        0.0f to activeColorStart,
                        0.8f to activeColorEnd,
                        center = center
                    ),
                    startAngle = startAngle,
                    sweepAngle = sweep,
                    useCenter = false,
                    topLeft = topLeft,
                    size = arcSize,
                    style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
                )
            }
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "$consumedCalories",
                style = MaterialTheme.typography.headlineLarge.copy(
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 32.sp
                ),
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = "Hedef: $targetCalories kcal",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = if (isOverBudget) "+${-remaining} kcal aşıldı" else "Kalan: $remaining kcal",
                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.SemiBold),
                color = if (isOverBudget) Color(0xFFDC2626) else EmeraldPrimary,
                modifier = Modifier.padding(top = 4.dp)
            )
        }
    }
}
