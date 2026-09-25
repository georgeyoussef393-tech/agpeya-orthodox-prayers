package com.example.ui.components.charts

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.PrayerId
import com.example.data.model.PrayerLogEntity
import com.example.localization.AppLanguage
import com.example.ui.components.CopticCrossCanvas
import com.example.ui.theme.GoldPrimary
import com.example.ui.theme.PeacefulGreen

/**
 * 24-Hour Timeline Distribution & Radial Ring for the Daily Prayer Progress.
 */
@Composable
fun DailyTimelineChart(
    dateLogs: List<PrayerLogEntity>,
    completedCanonicalCount: Int,
    totalCanonical: Int = 7,
    lang: AppLanguage,
    title: String,
    subtitle: String? = null,
    modifier: Modifier = Modifier
) {
    var selectedHour by remember { mutableStateOf<Int?>(null) }
    var animationPlayed by remember { mutableStateOf(false) }

    LaunchedEffect(dateLogs) {
        animationPlayed = true
    }

    val progressFraction = (completedCanonicalCount.toFloat() / totalCanonical.toFloat()).coerceIn(0f, 1f)
    val animatedRadialProgress by animateFloatAsState(
        targetValue = if (animationPlayed) progressFraction else 0f,
        animationSpec = tween(durationMillis = 280, easing = FastOutSlowInEasing),
        label = "daily_radial_anim"
    )

    // Map each of the 24 hours to prayer logs
    val hourlyCounts = remember(dateLogs) {
        IntArray(24) { h -> dateLogs.count { it.hour == h } }
    }

    Card(
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = modifier
            .fillMaxWidth()
            .testTag("daily_timeline_chart")
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Header with radial summary
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    if (subtitle != null) {
                        Text(
                            text = subtitle,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                // Radial Mini Gauge (D3 Circular Gauge style)
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.size(64.dp)
                ) {
                    val trackColor = MaterialTheme.colorScheme.surfaceVariant
                    val activeColor = if (completedCanonicalCount >= totalCanonical) PeacefulGreen else GoldPrimary

                    Canvas(modifier = Modifier.size(56.dp)) {
                        val stroke = 6.dp.toPx()
                        val arcSize = Size(size.width - stroke, size.height - stroke)
                        val topLeft = Offset(stroke / 2f, stroke / 2f)

                        drawArc(
                            color = trackColor,
                            startAngle = -90f,
                            sweepAngle = 360f,
                            useCenter = false,
                            topLeft = topLeft,
                            size = arcSize,
                            style = Stroke(width = stroke)
                        )

                        drawArc(
                            color = activeColor,
                            startAngle = -90f,
                            sweepAngle = 360f * animatedRadialProgress,
                            useCenter = false,
                            topLeft = topLeft,
                            size = arcSize,
                            style = Stroke(width = stroke, cap = StrokeCap.Round)
                        )
                    }

                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "${(progressFraction * 100).toInt()}%",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold,
                            color = activeColor
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 24-Hour Timeline Bar Chart
            val hours = (0..23).toList()
            val maxCount = hourlyCounts.maxOrNull()?.coerceAtLeast(1) ?: 1

            Text(
                text = if (lang == AppLanguage.ARABIC) "توزيع أوقات الصلاة على مدار الـ 24 ساعة:" else "24-Hour Prayer Log Distribution:",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Canvas rendering 24 bars
            val chartHeight = 70.dp
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(chartHeight)
            ) {
                Canvas(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(chartHeight)
                ) {
                    val width = size.width
                    val height = size.height
                    val barSlot = width / 24f
                    val barWidth = barSlot * 0.72f

                    for (h in 0..23) {
                        val count = hourlyCounts[h]
                        val x = h * barSlot + (barSlot - barWidth) / 2f

                        // Baseline background slot
                        drawRoundRect(
                            color = Color.LightGray.copy(alpha = 0.2f),
                            topLeft = Offset(x, 0f),
                            size = Size(barWidth, height),
                            cornerRadius = CornerRadius(4.dp.toPx(), 4.dp.toPx())
                        )

                        if (count > 0) {
                            val barHeight = (count.toFloat() / maxCount.toFloat()) * height
                            val y = height - barHeight
                            drawRoundRect(
                                color = if (selectedHour == h) PeacefulGreen else GoldPrimary,
                                topLeft = Offset(x, y),
                                size = Size(barWidth, barHeight),
                                cornerRadius = CornerRadius(4.dp.toPx(), 4.dp.toPx())
                            )
                        }
                    }
                }
            }

            // X-Axis Timeline markers (00h, 06h, 12h, 18h, 23h)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                listOf("00:00", "06:00 (باكر)", "12:00 (السادسة)", "18:00 (الغروب)", "23:00 (نصف الليل)").forEach { mark ->
                    val label = if (lang == AppLanguage.ARABIC) mark else {
                        when (mark) {
                            "00:00" -> "00:00"
                            "06:00 (باكر)" -> "06h Prime"
                            "12:00 (السادسة)" -> "12h 6th"
                            "18:00 (الغروب)" -> "18h 11th"
                            else -> "23h Mid"
                        }
                    }
                    Text(
                        text = label,
                        style = MaterialTheme.typography.labelSmall.copy(fontSize = 9.sp),
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}
