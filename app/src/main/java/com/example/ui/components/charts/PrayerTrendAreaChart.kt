package com.example.ui.components.charts

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.BurgundyPrimary
import com.example.ui.theme.GoldLight
import com.example.ui.theme.GoldPrimary
import com.example.ui.theme.PeacefulGreen

data class AreaPoint(
    val label: String,
    val value: Float,
    val description: String? = null
)

/**
 * Recharts / D3-inspired Smooth Area Trend Chart for Jetpack Compose.
 * Uses cubic Bezier curve interpolation, gradient area under fill,
 * interactive inspection point, and trend statistics.
 */
@Composable
fun PrayerTrendAreaChart(
    points: List<AreaPoint>,
    title: String,
    subtitle: String? = null,
    chartHeight: Dp = 190.dp,
    lineColor: Color = GoldPrimary,
    fillColor: Color = GoldLight,
    modifier: Modifier = Modifier,
    onPointSelected: ((AreaPoint) -> Unit)? = null
) {
    var selectedIndex by remember { mutableStateOf<Int?>(null) }
    var animationPlayed by remember { mutableStateOf(false) }

    LaunchedEffect(points) {
        animationPlayed = true
    }

    val animatedProgress by animateFloatAsState(
        targetValue = if (animationPlayed) 1f else 0f,
        animationSpec = tween(durationMillis = 280, easing = FastOutSlowInEasing),
        label = "area_chart_anim"
    )

    val maxValue = remember(points) {
        points.maxOfOrNull { it.value }?.coerceAtLeast(1f) ?: 1f
    }

    Card(
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = modifier
            .fillMaxWidth()
            .testTag("prayer_trend_area_chart")
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

                // Interactive inspect pill
                selectedIndex?.let { idx ->
                    if (idx in points.indices) {
                        val pt = points[idx]
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
                                        .background(lineColor)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "${pt.label}: ${pt.value.toInt()}",
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

            val gridColor = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.35f)

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(chartHeight)
            ) {
                Canvas(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(chartHeight)
                        .pointerInput(points) {
                            detectTapGestures { offset ->
                                if (points.size >= 2) {
                                    val slotWidth = size.width / (points.size - 1).toFloat()
                                    val tappedIdx = ((offset.x + (slotWidth / 2f)) / slotWidth).toInt().coerceIn(0, points.size - 1)
                                    selectedIndex = tappedIdx
                                    onPointSelected?.invoke(points[tappedIdx])
                                }
                            }
                        }
                ) {
                    val width = size.width
                    val height = size.height
                    val bottomPadding = 20.dp.toPx()
                    val plotHeight = height - bottomPadding
                    val n = points.size

                    if (n < 2) return@Canvas

                    // 1. Grid Lines
                    val steps = 3
                    for (i in 0..steps) {
                        val y = plotHeight * (1f - i.toFloat() / steps)
                        drawLine(
                            color = gridColor,
                            start = Offset(0f, y),
                            end = Offset(width, y),
                            strokeWidth = 1.dp.toPx(),
                            pathEffect = PathEffect.dashPathEffect(floatArrayOf(6f, 6f), 0f)
                        )
                    }

                    // 2. Compute point coordinates
                    val coords = points.mapIndexed { idx, pt ->
                        val x = (idx.toFloat() / (n - 1).toFloat()) * width
                        val frac = (pt.value / maxValue).coerceIn(0f, 1f) * animatedProgress
                        val y = plotHeight * (1f - frac)
                        Offset(x, y)
                    }

                    // 3. Build Smooth Cubic Bezier Path
                    val strokePath = Path()
                    val fillPath = Path()

                    strokePath.moveTo(coords.first().x, coords.first().y)
                    fillPath.moveTo(coords.first().x, plotHeight)
                    fillPath.lineTo(coords.first().x, coords.first().y)

                    for (i in 0 until n - 1) {
                        val p0 = coords[i]
                        val p1 = coords[i + 1]

                        val cx = (p0.x + p1.x) / 2f
                        strokePath.cubicTo(
                            cx, p0.y,
                            cx, p1.y,
                            p1.x, p1.y
                        )
                        fillPath.cubicTo(
                            cx, p0.y,
                            cx, p1.y,
                            p1.x, p1.y
                        )
                    }

                    fillPath.lineTo(coords.last().x, plotHeight)
                    fillPath.close()

                    // Draw Area Gradient Fill
                    val areaGradient = Brush.verticalGradient(
                        colors = listOf(
                            fillColor.copy(alpha = 0.38f),
                            fillColor.copy(alpha = 0.05f)
                        ),
                        startY = 0f,
                        endY = plotHeight
                    )
                    drawPath(path = fillPath, brush = areaGradient)

                    // Draw Smooth Line Stroke
                    drawPath(
                        path = strokePath,
                        color = lineColor,
                        style = Stroke(width = 3.dp.toPx(), cap = StrokeCap.Round)
                    )

                    // 4. Draw data points
                    coords.forEachIndexed { idx, pt ->
                        val isSelected = selectedIndex == idx
                        val radius = if (isSelected) 6.dp.toPx() else 3.5.dp.toPx()

                        // Outer ring
                        drawCircle(
                            color = if (isSelected) PeacefulGreen else lineColor,
                            radius = radius,
                            center = pt
                        )
                        // Inner white center
                        drawCircle(
                            color = Color.White,
                            radius = radius * 0.55f,
                            center = pt
                        )
                    }
                }
            }

            // X-Axis Labels
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                points.forEachIndexed { index, pt ->
                    if (index % 2 == 0 || index == points.size - 1) {
                        Text(
                            text = pt.label,
                            style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                            color = if (selectedIndex == index) GoldPrimary else MaterialTheme.colorScheme.onSurfaceVariant,
                            fontWeight = if (selectedIndex == index) FontWeight.Bold else FontWeight.Normal
                        )
                    }
                }
            }
        }
    }
}
