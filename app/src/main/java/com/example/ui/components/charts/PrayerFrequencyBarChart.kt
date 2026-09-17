package com.example.ui.components.charts

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.BurgundyPrimary
import com.example.ui.theme.GoldLight
import com.example.ui.theme.GoldPrimary
import com.example.ui.theme.PeacefulGreen

/**
 * Data item for interactive Bar Chart
 */
data class BarChartItem(
    val label: String,
    val value: Int,
    val secondaryText: String? = null,
    val highlight: Boolean = false
)

/**
 * Interactive D3/Recharts-inspired Bar Chart for Jetpack Compose.
 * Displays frequency bars with smooth gradients, grid lines, average benchmark line,
 * and tap-to-inspect tooltips with sleek animated indicators.
 */
@Composable
fun PrayerFrequencyBarChart(
    items: List<BarChartItem>,
    title: String,
    subtitle: String? = null,
    chartHeight: Dp = 190.dp,
    primaryBarColor: Color = GoldPrimary,
    secondaryBarColor: Color = GoldLight,
    maxCustomValue: Int? = null,
    averageValue: Float? = null,
    modifier: Modifier = Modifier,
    onItemSelected: ((BarChartItem) -> Unit)? = null
) {
    var selectedIndex by remember { mutableStateOf<Int?>(null) }
    var animationPlayed by remember { mutableStateOf(false) }

    LaunchedEffect(items) {
        animationPlayed = true
    }

    val animatedProgress by animateFloatAsState(
        targetValue = if (animationPlayed) 1f else 0f,
        animationSpec = tween(durationMillis = 800),
        label = "bar_chart_anim"
    )

    val maxValue = remember(items, maxCustomValue) {
        maxCustomValue ?: (items.maxOfOrNull { it.value }?.coerceAtLeast(1) ?: 1)
    }

    Card(
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = modifier
            .fillMaxWidth()
            .testTag("prayer_frequency_bar_chart")
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Header with Title & Stats
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

                // Interactive Tooltip / Selected Indicator
                selectedIndex?.let { idx ->
                    if (idx in items.indices) {
                        val item = items[idx]
                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = MaterialTheme.colorScheme.surfaceVariant,
                            border = androidx.compose.foundation.BorderStroke(1.dp, GoldPrimary.copy(alpha = 0.5f))
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(8.dp)
                                        .clip(CircleShape)
                                        .background(GoldPrimary)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "${item.label}: ${item.value}",
                                    style = MaterialTheme.typography.labelMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Chart Canvas
            val gridColor = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f)
            val avgLineColor = PeacefulGreen.copy(alpha = 0.8f)

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(chartHeight)
            ) {
                Canvas(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(chartHeight)
                        .pointerInput(items) {
                            detectTapGestures { offset ->
                                if (items.isNotEmpty()) {
                                    val barSlotWidth = size.width / items.size.toFloat()
                                    val tappedIdx = (offset.x / barSlotWidth).toInt().coerceIn(0, items.size - 1)
                                    selectedIndex = tappedIdx
                                    onItemSelected?.invoke(items[tappedIdx])
                                }
                            }
                        }
                ) {
                    val width = size.width
                    val height = size.height
                    val bottomPadding = 24.dp.toPx()
                    val chartPlotHeight = height - bottomPadding
                    val count = items.size

                    if (count == 0) return@Canvas

                    // 1. Draw Grid Lines (horizontal reference lines)
                    val stepCount = 4
                    for (i in 0..stepCount) {
                        val y = chartPlotHeight * (1f - i.toFloat() / stepCount)
                        drawLine(
                            color = gridColor,
                            start = Offset(0f, y),
                            end = Offset(width, y),
                            strokeWidth = 1.dp.toPx(),
                            pathEffect = PathEffect.dashPathEffect(floatArrayOf(6f, 6f), 0f)
                        )
                    }

                    // 2. Draw Average reference line if provided
                    if (averageValue != null && maxValue > 0) {
                        val avgY = chartPlotHeight * (1f - (averageValue / maxValue).coerceIn(0f, 1f))
                        drawLine(
                            color = avgLineColor,
                            start = Offset(0f, avgY),
                            end = Offset(width, avgY),
                            strokeWidth = 1.5.dp.toPx(),
                            pathEffect = PathEffect.dashPathEffect(floatArrayOf(8f, 6f), 0f)
                        )
                    }

                    // 3. Draw Bars
                    val slotWidth = width / count.toFloat()
                    val barWidth = (slotWidth * 0.62f).coerceAtMost(32.dp.toPx()).coerceAtLeast(6.dp.toPx())

                    for (i in 0 until count) {
                        val item = items[i]
                        val fraction = if (maxValue > 0) {
                            (item.value.toFloat() / maxValue.toFloat()).coerceIn(0f, 1f) * animatedProgress
                        } else 0f

                        val barHeight = chartPlotHeight * fraction
                        val xCenter = i * slotWidth + (slotWidth / 2f)
                        val xLeft = xCenter - (barWidth / 2f)
                        val yTop = chartPlotHeight - barHeight

                        val isSelected = selectedIndex == i

                        // Gradient brush for Recharts aesthetic
                        val barBrush = Brush.verticalGradient(
                            colors = if (isSelected) {
                                listOf(PeacefulGreen, GoldPrimary)
                            } else if (item.highlight) {
                                listOf(BurgundyPrimary, GoldPrimary)
                            } else {
                                listOf(primaryBarColor, secondaryBarColor)
                            },
                            startY = yTop,
                            endY = chartPlotHeight
                        )

                        if (barHeight > 0f) {
                            // Rounded bar top
                            drawRoundRect(
                                brush = barBrush,
                                topLeft = Offset(xLeft, yTop),
                                size = Size(barWidth, barHeight),
                                cornerRadius = CornerRadius(barWidth / 2f, barWidth / 2f)
                            )
                        } else {
                            // Empty dot baseline indicator
                            drawCircle(
                                color = gridColor,
                                radius = 2.dp.toPx(),
                                center = Offset(xCenter, chartPlotHeight - 2.dp.toPx())
                            )
                        }

                        // Selected indicator glow
                        if (isSelected) {
                            drawCircle(
                                color = GoldPrimary,
                                radius = 4.dp.toPx(),
                                center = Offset(xCenter, yTop - 6.dp.toPx())
                            )
                        }
                    }
                }
            }

            // X-Axis Labels (Display a readable subset if count is large)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                val step = when {
                    items.size > 20 -> 5 // e.g. for 31 days: 1, 6, 11, 16, 21, 26, 31
                    items.size > 10 -> 2 // e.g. for 12 months: Jan, Mar, May, Jul, Sep, Nov
                    else -> 1
                }

                items.forEachIndexed { index, item ->
                    if (index % step == 0 || index == items.size - 1) {
                        Text(
                            text = item.label,
                            style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                            color = if (selectedIndex == index) GoldPrimary else MaterialTheme.colorScheme.onSurfaceVariant,
                            fontWeight = if (selectedIndex == index) FontWeight.Bold else FontWeight.Normal,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.clickable {
                                selectedIndex = index
                                onItemSelected?.invoke(item)
                            }
                        )
                    }
                }
            }
        }
    }
}
