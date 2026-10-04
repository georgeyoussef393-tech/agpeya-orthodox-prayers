package com.example.ui.components.charts

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.ShowChart
import androidx.compose.material.icons.filled.Timeline
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.ArrowPrayerLogEntity
import com.example.data.model.PrayerId
import com.example.data.model.PrayerLogEntity
import com.example.localization.AgpeyaStrings
import com.example.localization.AppLanguage
import com.example.ui.components.CopticCrossCanvas
import com.example.ui.theme.BurgundyDeep
import com.example.ui.theme.GoldLight
import com.example.ui.theme.GoldPrimary
import com.example.ui.theme.PeacefulGreen
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

/**
 * Data model for each day in the 30-day rolling trend.
 */
data class DayTrendData(
    val date: Date,
    val dateString: String,      // "yyyy-MM-dd"
    val dayLabel: String,        // "27"
    val dayOfWeekLabel: String,  // "Fri" / "الجمعة"
    val canonicalCount: Int,     // 0..7
    val prayedCanonicalCodes: List<String>,
    val arrowCount: Int,
    val completionFraction: Float // 0f..1f (canonicalCount / 7)
)

enum class TrendMetricType {
    CANONICAL_HOURS,
    COMPLETION_RATE,
    ARROW_PRAYERS
}

/**
 * Recharts / D3-inspired Visual Representation for 30-Day Prayer Completion Trends.
 * Includes cubic Bezier curve interpolation, glowing area gradient fill, 7-day moving average,
 * interactive scrubber tooltip, and 30-day sparkline strip.
 */
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ThirtyDayPrayerTrendChart(
    allLogs: List<PrayerLogEntity>,
    allArrowLogs: List<ArrowPrayerLogEntity>,
    language: AppLanguage,
    dailyArrowTarget: Int = 33,
    modifier: Modifier = Modifier,
    onDaySelected: ((String) -> Unit)? = null
) {
    var selectedMetric by remember { mutableStateOf(TrendMetricType.CANONICAL_HOURS) }
    var selectedIndex by remember { mutableStateOf<Int?>(29) } // Default to today (last index)
    var showMovingAverage by remember { mutableStateOf(true) }

    // Compute rolling 30 days data
    val thirtyDaysData = remember(allLogs, allArrowLogs) {
        val list = mutableListOf<DayTrendData>()
        val cal = Calendar.getInstance()
        val sdfDate = SimpleDateFormat("yyyy-MM-dd", Locale.US)
        val sdfDay = SimpleDateFormat("d", Locale.US)
        val sdfDayOfWeek = SimpleDateFormat("EEE", if (language == AppLanguage.ARABIC) Locale("ar") else Locale.ENGLISH)

        // Generate 30 days: from 29 days ago (i=29 down to 0)
        for (offset in 29 downTo 0) {
            val c = Calendar.getInstance().apply {
                add(Calendar.DAY_OF_YEAR, -offset)
            }
            val date = c.time
            val dateStr = sdfDate.format(date)
            val dayLabel = sdfDay.format(date)
            val dowLabel = sdfDayOfWeek.format(date)

            val dayCanonicalLogs = allLogs.filter { it.dateString == dateStr }
            val dayArrowLogs = allArrowLogs.filter { it.dateString == dateStr }

            val canonicalCodes = dayCanonicalLogs.map { it.prayerCode }.distinct()
            val canonicalCount = canonicalCodes.size.coerceAtMost(7)
            val arrowCount = dayArrowLogs.sumOf { it.count }
            val fraction = (canonicalCount.toFloat() / 7f).coerceIn(0f, 1f)

            list.add(
                DayTrendData(
                    date = date,
                    dateString = dateStr,
                    dayLabel = dayLabel,
                    dayOfWeekLabel = dowLabel,
                    canonicalCount = canonicalCount,
                    prayedCanonicalCodes = canonicalCodes,
                    arrowCount = arrowCount,
                    completionFraction = fraction
                )
            )
        }
        list
    }

    // 7-day moving averages
    val movingAverages = remember(thirtyDaysData, selectedMetric) {
        thirtyDaysData.mapIndexed { idx, _ ->
            val windowStart = (idx - 6).coerceAtLeast(0)
            val window = thirtyDaysData.subList(windowStart, idx + 1)
            val avg = when (selectedMetric) {
                TrendMetricType.CANONICAL_HOURS -> window.map { it.canonicalCount.toFloat() }.average().toFloat()
                TrendMetricType.COMPLETION_RATE -> window.map { it.completionFraction * 100f }.average().toFloat()
                TrendMetricType.ARROW_PRAYERS -> window.map { it.arrowCount.toFloat() }.average().toFloat()
            }
            if (avg.isNaN()) 0f else avg
        }
    }

    // Key statistics across 30 days
    val totalCanonical30Days = remember(thirtyDaysData) { thirtyDaysData.sumOf { it.canonicalCount } }
    val totalArrow30Days = remember(thirtyDaysData) { thirtyDaysData.sumOf { it.arrowCount } }
    val activeDaysCount = remember(thirtyDaysData) { thirtyDaysData.count { it.canonicalCount > 0 || it.arrowCount > 0 } }
    val perfectDaysCount = remember(thirtyDaysData) { thirtyDaysData.count { it.canonicalCount >= 7 } }
    val commitmentPercentage = ((activeDaysCount.toFloat() / 30f) * 100).toInt()

    // Compute current active streak
    val currentStreak = remember(thirtyDaysData) {
        var streak = 0
        for (i in thirtyDaysData.indices.reversed()) {
            if (thirtyDaysData[i].canonicalCount > 0 || thirtyDaysData[i].arrowCount > 0) {
                streak++
            } else {
                break
            }
        }
        streak
    }

    // Longest streak in 30 days
    val longestStreak = remember(thirtyDaysData) {
        var maxStreak = 0
        var tempStreak = 0
        for (day in thirtyDaysData) {
            if (day.canonicalCount > 0 || day.arrowCount > 0) {
                tempStreak++
                if (tempStreak > maxStreak) maxStreak = tempStreak
            } else {
                tempStreak = 0
            }
        }
        maxStreak
    }

    // Animation progress
    var animPlayed by remember { mutableStateOf(false) }
    LaunchedEffect(selectedMetric) {
        animPlayed = true
    }
    val animatedProgress by animateFloatAsState(
        targetValue = if (animPlayed) 1f else 0f,
        animationSpec = tween(durationMillis = 300, easing = FastOutSlowInEasing),
        label = "trend_anim"
    )

    Card(
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        border = BorderStroke(1.dp, GoldPrimary.copy(alpha = 0.25f)),
        modifier = modifier
            .fillMaxWidth()
            .testTag("thirty_day_prayer_trend_chart")
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Header: Title & Subtitle
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    Surface(
                        shape = CircleShape,
                        color = GoldPrimary.copy(alpha = 0.15f),
                        modifier = Modifier.size(38.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.Default.Timeline,
                                contentDescription = null,
                                tint = GoldPrimary,
                                modifier = Modifier.size(22.dp)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = AgpeyaStrings.thirtyDayTrendsTitle(language),
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = AgpeyaStrings.thirtyDayTrendsSubtitle(language),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontSize = 11.5.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // 4 KPI Summary Metric Badges
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Canonical Total
                TrendSummaryCard(
                    title = AgpeyaStrings.metricCanonicalPrayers(language),
                    value = "$totalCanonical30Days",
                    subtitle = if (language == AppLanguage.ARABIC) "ساعة في 30 يوم" else "prayed / 30d",
                    color = GoldPrimary,
                    modifier = Modifier.weight(1f)
                )

                // Active Days & Commitment %
                TrendSummaryCard(
                    title = AgpeyaStrings.metricCompletionRate(language),
                    value = "$commitmentPercentage%",
                    subtitle = if (language == AppLanguage.ARABIC) "$activeDaysCount/30 يوماً" else "$activeDaysCount/30 active days",
                    color = PeacefulGreen,
                    modifier = Modifier.weight(1f)
                )

                // Arrow Beads
                TrendSummaryCard(
                    title = if (language == AppLanguage.ARABIC) "السهمية" else "Arrows",
                    value = "$totalArrow30Days",
                    subtitle = if (language == AppLanguage.ARABIC) "تكرار تسبيح" else "total beads",
                    color = BurgundyDeep,
                    modifier = Modifier.weight(1f)
                )

                // Current Streak
                TrendSummaryCard(
                    title = if (language == AppLanguage.ARABIC) "السلسلة 🔥" else "Streak 🔥",
                    value = "$currentStreak",
                    subtitle = if (language == AppLanguage.ARABIC) "يوم متواصل" else "days streak",
                    color = Color(0xFFFF9800),
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Metric Selector Filter Chips & Moving Average Toggle
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    FilterChip(
                        selected = selectedMetric == TrendMetricType.CANONICAL_HOURS,
                        onClick = { selectedMetric = TrendMetricType.CANONICAL_HOURS },
                        label = {
                            Text(
                                text = AgpeyaStrings.metricCanonicalPrayers(language),
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = if (selectedMetric == TrendMetricType.CANONICAL_HOURS) FontWeight.Bold else FontWeight.Normal
                            )
                        },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = GoldPrimary,
                            selectedLabelColor = Color.Black
                        )
                    )

                    FilterChip(
                        selected = selectedMetric == TrendMetricType.COMPLETION_RATE,
                        onClick = { selectedMetric = TrendMetricType.COMPLETION_RATE },
                        label = {
                            Text(
                                text = AgpeyaStrings.metricCompletionRate(language),
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = if (selectedMetric == TrendMetricType.COMPLETION_RATE) FontWeight.Bold else FontWeight.Normal
                            )
                        },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = PeacefulGreen,
                            selectedLabelColor = Color.White
                        )
                    )

                    FilterChip(
                        selected = selectedMetric == TrendMetricType.ARROW_PRAYERS,
                        onClick = { selectedMetric = TrendMetricType.ARROW_PRAYERS },
                        label = {
                            Text(
                                text = if (language == AppLanguage.ARABIC) "السهمية" else "Arrows",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = if (selectedMetric == TrendMetricType.ARROW_PRAYERS) FontWeight.Bold else FontWeight.Normal
                            )
                        },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = BurgundyDeep,
                            selectedLabelColor = GoldLight
                        )
                    )
                }

                // 7-day moving avg toggle
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = if (showMovingAverage) GoldPrimary.copy(alpha = 0.15f) else MaterialTheme.colorScheme.surfaceVariant,
                    border = BorderStroke(1.dp, if (showMovingAverage) GoldPrimary.copy(alpha = 0.4f) else Color.Transparent),
                    modifier = Modifier.clickable { showMovingAverage = !showMovingAverage }
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.TrendingUp,
                            contentDescription = null,
                            tint = if (showMovingAverage) GoldPrimary else MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(13.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "7d Avg",
                            style = MaterialTheme.typography.labelSmall,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (showMovingAverage) GoldPrimary else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Recharts/D3 Area Chart Canvas (30-day cubic spline)
            val chartLineColor = when (selectedMetric) {
                TrendMetricType.CANONICAL_HOURS -> GoldPrimary
                TrendMetricType.COMPLETION_RATE -> PeacefulGreen
                TrendMetricType.ARROW_PRAYERS -> BurgundyDeep
            }
            val chartFillColor = when (selectedMetric) {
                TrendMetricType.CANONICAL_HOURS -> GoldLight
                TrendMetricType.COMPLETION_RATE -> PeacefulGreen
                TrendMetricType.ARROW_PRAYERS -> BurgundyDeep
            }

            val maxScaleValue = remember(thirtyDaysData, selectedMetric) {
                when (selectedMetric) {
                    TrendMetricType.CANONICAL_HOURS -> 7f
                    TrendMetricType.COMPLETION_RATE -> 100f
                    TrendMetricType.ARROW_PRAYERS -> {
                        val maxLogged = thirtyDaysData.maxOfOrNull { it.arrowCount.toFloat() } ?: 0f
                        maxOf(maxLogged, dailyArrowTarget.toFloat(), 33f)
                    }
                }
            }

            val valuesList = remember(thirtyDaysData, selectedMetric) {
                thirtyDaysData.map { day ->
                    when (selectedMetric) {
                        TrendMetricType.CANONICAL_HOURS -> day.canonicalCount.toFloat()
                        TrendMetricType.COMPLETION_RATE -> day.completionFraction * 100f
                        TrendMetricType.ARROW_PRAYERS -> day.arrowCount.toFloat()
                    }
                }
            }

            val gridColor = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.35f)

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
            ) {
                Canvas(
                    modifier = Modifier
                        .fillMaxSize()
                        .pointerInput(thirtyDaysData) {
                            detectTapGestures { offset ->
                                val slotWidth = size.width / 29f
                                val tappedIndex = ((offset.x + (slotWidth / 2f)) / slotWidth).toInt().coerceIn(0, 29)
                                selectedIndex = tappedIndex
                                onDaySelected?.invoke(thirtyDaysData[tappedIndex].dateString)
                            }
                        }
                        .pointerInput(thirtyDaysData) {
                            detectDragGestures { change, _ ->
                                val slotWidth = size.width / 29f
                                val draggedIndex = ((change.position.x + (slotWidth / 2f)) / slotWidth).toInt().coerceIn(0, 29)
                                selectedIndex = draggedIndex
                                onDaySelected?.invoke(thirtyDaysData[draggedIndex].dateString)
                            }
                        }
                ) {
                    val width = size.width
                    val height = size.height
                    val bottomPadding = 24.dp.toPx()
                    val plotHeight = height - bottomPadding
                    val n = thirtyDaysData.size

                    if (n < 2) return@Canvas

                    // 1. Horizontal Reference Grid Lines
                    val gridSteps = 4
                    for (i in 0..gridSteps) {
                        val y = plotHeight * (1f - (i.toFloat() / gridSteps.toFloat()))
                        drawLine(
                            color = gridColor,
                            start = Offset(0f, y),
                            end = Offset(width, y),
                            strokeWidth = 1.dp.toPx(),
                            pathEffect = PathEffect.dashPathEffect(floatArrayOf(6f, 6f), 0f)
                        )
                    }

                    // 2. Target Reference Line
                    if (selectedMetric == TrendMetricType.CANONICAL_HOURS) {
                        // 7 Canonical Prayers Target Line
                        val goalY = plotHeight * (1f - (7f / maxScaleValue))
                        drawLine(
                            color = PeacefulGreen.copy(alpha = 0.6f),
                            start = Offset(0f, goalY),
                            end = Offset(width, goalY),
                            strokeWidth = 1.5.dp.toPx(),
                            pathEffect = PathEffect.dashPathEffect(floatArrayOf(8f, 4f), 0f)
                        )
                    } else if (selectedMetric == TrendMetricType.ARROW_PRAYERS && dailyArrowTarget > 0) {
                        val goalY = plotHeight * (1f - (dailyArrowTarget.toFloat() / maxScaleValue).coerceIn(0f, 1f))
                        drawLine(
                            color = GoldPrimary.copy(alpha = 0.6f),
                            start = Offset(0f, goalY),
                            end = Offset(width, goalY),
                            strokeWidth = 1.5.dp.toPx(),
                            pathEffect = PathEffect.dashPathEffect(floatArrayOf(8f, 4f), 0f)
                        )
                    }

                    // 3. Compute Coordinates
                    val coords = valuesList.mapIndexed { idx, value ->
                        val x = (idx.toFloat() / (n - 1).toFloat()) * width
                        val frac = (value / maxScaleValue).coerceIn(0f, 1f) * animatedProgress
                        val y = plotHeight * (1f - frac)
                        Offset(x, y)
                    }

                    // 4. Build Smooth Cubic Bezier Curves (D3 / Recharts Spline Algorithm)
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
                            chartFillColor.copy(alpha = 0.40f),
                            chartFillColor.copy(alpha = 0.04f)
                        ),
                        startY = 0f,
                        endY = plotHeight
                    )
                    drawPath(path = fillPath, brush = areaGradient)

                    // Draw Smooth Line Stroke
                    drawPath(
                        path = strokePath,
                        color = chartLineColor,
                        style = Stroke(width = 3.dp.toPx(), cap = StrokeCap.Round)
                    )

                    // 5. Draw 7-Day Moving Average Line (if enabled)
                    if (showMovingAverage && movingAverages.isNotEmpty()) {
                        val avgCoords = movingAverages.mapIndexed { idx, avgVal ->
                            val x = (idx.toFloat() / (n - 1).toFloat()) * width
                            val frac = (avgVal / maxScaleValue).coerceIn(0f, 1f) * animatedProgress
                            val y = plotHeight * (1f - frac)
                            Offset(x, y)
                        }

                        val avgPath = Path()
                        avgPath.moveTo(avgCoords.first().x, avgCoords.first().y)
                        for (i in 0 until n - 1) {
                            val p0 = avgCoords[i]
                            val p1 = avgCoords[i + 1]
                            val cx = (p0.x + p1.x) / 2f
                            avgPath.cubicTo(cx, p0.y, cx, p1.y, p1.x, p1.y)
                        }

                        drawPath(
                            path = avgPath,
                            color = Color(0xFFFF9800).copy(alpha = 0.85f),
                            style = Stroke(
                                width = 1.8.dp.toPx(),
                                pathEffect = PathEffect.dashPathEffect(floatArrayOf(8f, 6f), 0f),
                                cap = StrokeCap.Round
                            )
                        )
                    }

                    // 6. Draw Selected Index Scrubber Vertical Line & Highlight Marker
                    selectedIndex?.let { idx ->
                        if (idx in coords.indices) {
                            val pt = coords[idx]

                            // Vertical dashed guide
                            drawLine(
                                color = chartLineColor.copy(alpha = 0.6f),
                                start = Offset(pt.x, 0f),
                                end = Offset(pt.x, plotHeight),
                                strokeWidth = 1.5.dp.toPx(),
                                pathEffect = PathEffect.dashPathEffect(floatArrayOf(4f, 4f), 0f)
                            )

                            // Outer pulse halo
                            drawCircle(
                                color = chartLineColor.copy(alpha = 0.25f),
                                radius = 9.dp.toPx(),
                                center = pt
                            )
                            // Outer ring
                            drawCircle(
                                color = chartLineColor,
                                radius = 6.dp.toPx(),
                                center = pt
                            )
                            // White Center
                            drawCircle(
                                color = Color.White,
                                radius = 3.dp.toPx(),
                                center = pt
                            )
                        }
                    }
                }
            }

            // X-Axis Day Numbers (Sampled every 5 days + last day)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                listOf(0, 6, 12, 18, 24, 29).forEach { idx ->
                    if (idx < thirtyDaysData.size) {
                        val dayData = thirtyDaysData[idx]
                        Text(
                            text = if (idx == 29) {
                                if (language == AppLanguage.ARABIC) "اليوم" else "Today"
                            } else {
                                "${dayData.dayLabel}/${dayData.dateString.substring(5, 7)}"
                            },
                            style = MaterialTheme.typography.labelSmall,
                            fontSize = 10.sp,
                            color = if (selectedIndex == idx) GoldPrimary else MaterialTheme.colorScheme.onSurfaceVariant,
                            fontWeight = if (selectedIndex == idx) FontWeight.Bold else FontWeight.Normal
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Interactive Day Detail Tooltip Card (for tapped or selected day)
            selectedIndex?.let { idx ->
                if (idx in thirtyDaysData.indices) {
                    val dayData = thirtyDaysData[idx]
                    DayInspectionCard(
                        dayData = dayData,
                        language = language,
                        dailyArrowTarget = dailyArrowTarget
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // 30-Day Interactive Sparkline Mini-Strip (Tappable bar matrix)
            Text(
                text = if (language == AppLanguage.ARABIC) "شريط الـ 30 يوماً (انقر لاستعراض أي يوم):" else "30-Day Strip (Tap any day to inspect):",
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(6.dp))

            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                itemsIndexed(thirtyDaysData) { index, day ->
                    val isSelected = selectedIndex == index
                    val barHeightFraction = (day.canonicalCount.toFloat() / 7f).coerceIn(0.12f, 1f)
                    val barColor = when {
                        day.canonicalCount >= 7 -> PeacefulGreen
                        day.canonicalCount >= 4 -> GoldPrimary
                        day.canonicalCount > 0 -> GoldLight
                        else -> MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.35f)
                    }

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .width(20.dp)
                            .clip(RoundedCornerShape(4.dp))
                            .background(if (isSelected) GoldPrimary.copy(alpha = 0.2f) else Color.Transparent)
                            .clickable {
                                selectedIndex = index
                                onDaySelected?.invoke(day.dateString)
                            }
                            .padding(vertical = 2.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .width(12.dp)
                                .height(36.dp)
                                .clip(RoundedCornerShape(3.dp))
                                .background(MaterialTheme.colorScheme.surfaceVariant),
                            contentAlignment = Alignment.BottomCenter
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height((36 * barHeightFraction).dp)
                                    .clip(RoundedCornerShape(3.dp))
                                    .background(barColor)
                            )
                        }
                        Spacer(modifier = Modifier.height(3.dp))
                        Text(
                            text = day.dayLabel,
                            style = MaterialTheme.typography.labelSmall,
                            fontSize = 8.5.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                            color = if (isSelected) GoldPrimary else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun TrendSummaryCard(
    title: String,
    value: String,
    subtitle: String,
    color: Color,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = MaterialTheme.colorScheme.surfaceVariant,
        border = BorderStroke(1.dp, color.copy(alpha = 0.35f)),
        modifier = modifier
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = value,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = color,
                fontSize = 15.sp
            )
            Text(
                text = title,
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface,
                fontSize = 10.sp,
                maxLines = 1
            )
            Text(
                text = subtitle,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontSize = 8.5.sp,
                maxLines = 1
            )
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun DayInspectionCard(
    dayData: DayTrendData,
    language: AppLanguage,
    dailyArrowTarget: Int
) {
    Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        border = BorderStroke(1.dp, GoldPrimary.copy(alpha = 0.4f)),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Surface(
                        shape = CircleShape,
                        color = if (dayData.canonicalCount >= 7) PeacefulGreen else GoldPrimary,
                        modifier = Modifier.size(26.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Text(
                                text = "${dayData.canonicalCount}",
                                color = if (dayData.canonicalCount >= 7) Color.White else Color.Black,
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp
                            )
                        }
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Text(
                            text = "${dayData.dayOfWeekLabel} • ${dayData.dateString}",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = if (dayData.canonicalCount >= 7) {
                                if (language == AppLanguage.ARABIC) "اكتملت جميع صلوات السواعي السبع ✝️" else "All 7 canonical prayers completed ✝️"
                            } else {
                                "${dayData.canonicalCount} / 7 " + if (language == AppLanguage.ARABIC) "سواعي" else "canonical hours"
                            },
                            style = MaterialTheme.typography.bodySmall,
                            color = if (dayData.canonicalCount >= 7) PeacefulGreen else MaterialTheme.colorScheme.onSurfaceVariant,
                            fontSize = 11.sp
                        )
                    }
                }

                // Arrow prayers badge
                if (dayData.arrowCount > 0) {
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = BurgundyDeep.copy(alpha = 0.18f),
                        border = BorderStroke(1.dp, BurgundyDeep.copy(alpha = 0.4f))
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = "${dayData.arrowCount}",
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.Bold,
                                color = BurgundyDeep
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = if (language == AppLanguage.ARABIC) "سهمية" else "arrows",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                fontSize = 9.sp
                            )
                        }
                    }
                }
            }

            // Prayed Canonical Hours Chips (if any)
            if (dayData.prayedCanonicalCodes.isNotEmpty()) {
                Spacer(modifier = Modifier.height(8.dp))
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    dayData.prayedCanonicalCodes.forEach { code ->
                        val prayer = PrayerId.fromCode(code)
                        if (prayer != null) {
                            Surface(
                                shape = RoundedCornerShape(6.dp),
                                color = GoldPrimary.copy(alpha = 0.15f),
                                border = BorderStroke(1.dp, GoldPrimary.copy(alpha = 0.35f))
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Check,
                                        contentDescription = null,
                                        tint = PeacefulGreen,
                                        modifier = Modifier.size(11.dp)
                                    )
                                    Spacer(modifier = Modifier.width(3.dp))
                                    Text(
                                        text = prayer.getDisplayName(language),
                                        style = MaterialTheme.typography.labelSmall,
                                        fontSize = 10.sp,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
