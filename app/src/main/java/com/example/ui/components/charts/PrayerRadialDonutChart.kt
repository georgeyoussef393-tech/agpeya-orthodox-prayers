package com.example.ui.components.charts

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.CopticCrossCanvas
import com.example.ui.theme.GoldPrimary

data class DonutSlice(
    val label: String,
    val value: Int,
    val color: Color
)

/**
 * Recharts PieChart / Donut-style Composable for Jetpack Compose.
 * Shows prayer composition proportions with interactive legend and center summary badge.
 */
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun PrayerRadialDonutChart(
    slices: List<DonutSlice>,
    title: String,
    subtitle: String? = null,
    totalLabel: String = "Total",
    chartSize: Dp = 180.dp,
    strokeWidth: Dp = 22.dp,
    modifier: Modifier = Modifier
) {
    var selectedSliceIndex by remember { mutableStateOf<Int?>(null) }
    var animationPlayed by remember { mutableStateOf(false) }

    LaunchedEffect(slices) {
        animationPlayed = true
    }

    val animatedProgress by animateFloatAsState(
        targetValue = if (animationPlayed) 1f else 0f,
        animationSpec = tween(durationMillis = 800),
        label = "donut_anim"
    )

    val totalValue = remember(slices) {
        slices.sumOf { it.value }
    }

    Card(
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = modifier
            .fillMaxWidth()
            .testTag("prayer_radial_donut_chart")
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Header
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column {
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
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Donut Canvas with Center Text
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Canvas(modifier = Modifier.size(chartSize)) {
                    val strokePx = strokeWidth.toPx()
                    val arcSize = Size(size.width - strokePx, size.height - strokePx)
                    val arcTopLeft = Offset(strokePx / 2f, strokePx / 2f)

                    if (totalValue == 0) {
                        // Empty outline
                        drawArc(
                            color = Color.LightGray.copy(alpha = 0.3f),
                            startAngle = 0f,
                            sweepAngle = 360f,
                            useCenter = false,
                            topLeft = arcTopLeft,
                            size = arcSize,
                            style = Stroke(width = strokePx)
                        )
                    } else {
                        var currentAngle = -90f
                        val filteredSlices = slices.filter { it.value > 0 }

                        filteredSlices.forEachIndexed { idx, slice ->
                            val sweep = (slice.value.toFloat() / totalValue.toFloat()) * 360f * animatedProgress
                            val gap = if (filteredSlices.size > 1) 3f else 0f
                            val actualSweep = (sweep - gap).coerceAtLeast(0f)

                            val isSelected = selectedSliceIndex == idx
                            val effectiveStroke = if (isSelected) strokePx * 1.22f else strokePx

                            drawArc(
                                color = slice.color,
                                startAngle = currentAngle + (gap / 2f),
                                sweepAngle = actualSweep,
                                useCenter = false,
                                topLeft = arcTopLeft,
                                size = arcSize,
                                style = Stroke(width = effectiveStroke, cap = StrokeCap.Round)
                            )
                            currentAngle += sweep
                        }
                    }
                }

                // Center Summary
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    CopticCrossCanvas(size = 20.dp)
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "$totalValue",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = GoldPrimary
                    )
                    Text(
                        text = totalLabel,
                        style = MaterialTheme.typography.labelSmall.copy(fontSize = 11.sp),
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Legend / Composition Breakdown Chips
            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                slices.filter { it.value > 0 }.forEachIndexed { index, slice ->
                    val percentage = if (totalValue > 0) {
                        (slice.value.toFloat() / totalValue.toFloat() * 100).toInt()
                    } else 0

                    val isSelected = selectedSliceIndex == index

                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = if (isSelected) slice.color.copy(alpha = 0.22f) else MaterialTheme.colorScheme.surfaceVariant,
                        modifier = Modifier.clickable {
                            selectedSliceIndex = if (isSelected) null else index
                        }
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(8.dp)
                                    .clip(CircleShape)
                                    .background(slice.color)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "${slice.label} ($percentage%)",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }
            }
        }
    }
}
