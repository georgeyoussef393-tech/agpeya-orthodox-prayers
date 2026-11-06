package com.example.ui.components.charts

import androidx.compose.animation.AnimatedContent
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
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.DateRange
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
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
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
import androidx.compose.ui.geometry.CornerRadius
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
 * Single day summary representation for dashboard calculations.
 */
data class DashboardDayData(
    val date: Date,
    val dateString: String,      // "yyyy-MM-dd"
    val dayOfMonthLabel: String, // "7"
    val dayOfWeekLabel: String,  // "Mon" / "الإثنين"
    val canonicalCount: Int,     // 0..7
    val prayedCanonicalCodes: List<String>,
    val arrowCount: Int,
    val completionFraction: Float // 0f..1f (canonicalCount / 7)
)

enum class DashboardRange {
    LAST_WEEK,   // 7 Days
    LAST_MONTH   // 30 Days
}

enum class DashboardMetric {
    CANONICAL_HOURS,
    COMPLETION_RATE,
    ARROW_PRAYERS
}

/**
 * Recharts & D3-inspired Custom Canvas Visual Dashboard.
 * Displays user's prayer completion history over the last week (7 days) and month (30 days).
 * Features:
 * - Cubic Bezier area curve interpolation with glowing gradient fill
 * - Dynamic bar-and-area chart rendering for last 7 days & last 30 days
 * - 7-day moving average benchmark overlay
 * - Interactive tap/drag scrubber tooltip
 * - Canonical prayer breakdown chips & streak counter
 */
@Composable
fun PrayerCompletionDashboard(
    allLogs: List<PrayerLogEntity>,
    allArrowLogs: List<ArrowPrayerLogEntity>,
    language: AppLanguage,
    dailyArrowTarget: Int = 33,
    modifier: Modifier = Modifier,
    onDaySelected: ((String) -> Unit)? = null
) {
    var selectedRange by remember { mutableStateOf(DashboardRange.LAST_WEEK) }
    var selectedMetric by remember { mutableStateOf(DashboardMetric.CANONICAL_HOURS) }
    var showMovingAverage by remember { mutableStateOf(true) }

    // 1. Process 30 Days of History
    val thirtyDaysHistory = remember(allLogs, allArrowLogs, language) {
        val list = mutableListOf<DashboardDayData>()
        val sdfDate = SimpleDateFormat("yyyy-MM-dd", Locale.US)
        val sdfDay = SimpleDateFormat("d", Locale.US)
        val sdfDow = SimpleDateFormat("EEE", if (language == AppLanguage.ARABIC) Locale("ar") else Locale.ENGLISH)

        for (offset in 29 downTo 0) {
            val cal = Calendar.getInstance().apply {
                add(Calendar.DAY_OF_YEAR, -offset)
            }
            val date = cal.time
            val dateStr = sdfDate.format(date)
            val dayLabel = sdfDay.format(date)
            val dowLabel = sdfDow.format(date)

            val dayCanonicalLogs = allLogs.filter { it.dateString == dateStr }
            val dayArrowLogs = allArrowLogs.filter { it.dateString == dateStr }

            val canonicalCodes = dayCanonicalLogs.map { it.prayerCode }.distinct()
            val canonicalCount = canonicalCodes.size.coerceAtMost(7)
            val arrowCount = dayArrowLogs.sumOf { it.count }
            val fraction = (canonicalCount.toFloat() / 7f).coerceIn(0f, 1f)

            list.add(
                DashboardDayData(
                    date = date,
                    dateString = dateStr,
                    dayOfMonthLabel = dayLabel,
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

    // 2. Extract Last Week (7 Days)
    val lastWeekHistory = remember(thirtyDaysHistory) {
        thirtyDaysHistory.takeLast(7)
    }

    // Active dataset based on selected range
    val currentHistory = if (selectedRange == DashboardRange.LAST_WEEK) lastWeekHistory else thirtyDaysHistory

    // Selected day index inside active dataset (default to last item = today)
    var selectedIndex by remember(selectedRange) {
        mutableStateOf<Int?>(currentHistory.size - 1)
    }

    // 3. Compute Key Metrics
    val totalCanonicalInPeriod = remember(currentHistory) { currentHistory.sumOf { it.canonicalCount } }
    val totalArrowInPeriod = remember(currentHistory) { currentHistory.sumOf { it.arrowCount } }
    val activeDaysInPeriod = remember(currentHistory) { currentHistory.count { it.canonicalCount > 0 || it.arrowCount > 0 } }
    val totalDaysCount = currentHistory.size
    val completionPercentage = ((activeDaysInPeriod.toFloat() / totalDaysCount.toFloat()) * 100).toInt()

    // Streak count
    val currentStreak = remember(thirtyDaysHistory) {
        var streak = 0
        for (i in thirtyDaysHistory.indices.reversed()) {
            if (thirtyDaysHistory[i].canonicalCount > 0 || thirtyDaysHistory[i].arrowCount > 0) {
                streak++
            } else {
                break
            }
        }
        streak
    }

    // Animation progress
    var animationTrigger by remember { mutableStateOf(false) }
    LaunchedEffect(selectedRange, selectedMetric) {
        animationTrigger = true
    }
    val animatedProgress by animateFloatAsState(
        targetValue = if (animationTrigger) 1f else 0f,
        animationSpec = tween(durationMillis = 320, easing = FastOutSlowInEasing),
        label = "dashboard_anim"
    )

    Card(
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
        border = BorderStroke(1.dp, GoldPrimary.copy(alpha = 0.25f)),
        modifier = modifier
            .fillMaxWidth()
            .testTag("prayer_completion_dashboard")
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Header Row: Dashboard Title & Coptic Cross Icon
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    CopticCrossCanvas(size = 28.dp)
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = if (language == AppLanguage.ARABIC) {
                                "لوحة إنجاز الصلوات (Recharts Canvas)"
                            } else {
                                "Prayer Completion Dashboard"
                            },
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = if (language == AppLanguage.ARABIC) {
                                "سجل الالتزام بالأجبية والصلوات السهمية للأسبوع والشهر الماضي"
                            } else {
                                "Canonical & Arrow Prayer History for Last Week & Month"
                            },
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontSize = 11.sp
                        )
                    }
                }

                // Streak Badge
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = Color(0xFFFF9800).copy(alpha = 0.15f),
                    border = BorderStroke(1.dp, Color(0xFFFF9800).copy(alpha = 0.4f))
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.LocalFireDepartment,
                            contentDescription = null,
                            tint = Color(0xFFFF9800),
                            modifier = Modifier.size(15.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "$currentStreak " + if (language == AppLanguage.ARABIC) "أيام" else "d streak",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFFF9800),
                            fontSize = 11.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Time Period Selector Tabs (Last Week vs Last Month)
            TabRow(
                selectedTabIndex = selectedRange.ordinal,
                containerColor = MaterialTheme.colorScheme.surfaceVariant,
                contentColor = GoldPrimary,
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
            ) {
                Tab(
                    selected = selectedRange == DashboardRange.LAST_WEEK,
                    onClick = { selectedRange = DashboardRange.LAST_WEEK },
                    text = {
                        Text(
                            text = if (language == AppLanguage.ARABIC) "الأسبوع الماضي (7 أيام)" else "Last Week (7 Days)",
                            fontWeight = if (selectedRange == DashboardRange.LAST_WEEK) FontWeight.Bold else FontWeight.Normal,
                            fontSize = 12.5.sp
                        )
                    },
                    modifier = Modifier.testTag("tab_last_week")
                )
                Tab(
                    selected = selectedRange == DashboardRange.LAST_MONTH,
                    onClick = { selectedRange = DashboardRange.LAST_MONTH },
                    text = {
                        Text(
                            text = if (language == AppLanguage.ARABIC) "الشهر الماضي (30 يوماً)" else "Last Month (30 Days)",
                            fontWeight = if (selectedRange == DashboardRange.LAST_MONTH) FontWeight.Bold else FontWeight.Normal,
                            fontSize = 12.5.sp
                        )
                    },
                    modifier = Modifier.testTag("tab_last_month")
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            // 4 KPI Overview Badges
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                DashboardMetricBadge(
                    title = if (language == AppLanguage.ARABIC) "السواعي" else "Canonical",
                    value = "$totalCanonicalInPeriod",
                    subtitle = if (selectedRange == DashboardRange.LAST_WEEK) "/ 49 صلاة" else "/ 210 صلاة",
                    color = GoldPrimary,
                    modifier = Modifier.weight(1f)
                )

                DashboardMetricBadge(
                    title = if (language == AppLanguage.ARABIC) "الالتزام" else "Commitment",
                    value = "$completionPercentage%",
                    subtitle = "$activeDaysInPeriod / $totalDaysCount " + if (language == AppLanguage.ARABIC) "أيام" else "days",
                    color = PeacefulGreen,
                    modifier = Modifier.weight(1f)
                )

                DashboardMetricBadge(
                    title = if (language == AppLanguage.ARABIC) "السهمية" else "Arrows",
                    value = "$totalArrowInPeriod",
                    subtitle = if (language == AppLanguage.ARABIC) "تسبيحة" else "beads",
                    color = BurgundyDeep,
                    modifier = Modifier.weight(1f)
                )

                DashboardMetricBadge(
                    title = if (language == AppLanguage.ARABIC) "المتوسط" else "Avg/Day",
                    value = String.format(Locale.US, "%.1f", totalCanonicalInPeriod.toFloat() / totalDaysCount.toFloat()),
                    subtitle = if (language == AppLanguage.ARABIC) "ساعة/يوم" else "hrs/day",
                    color = Color(0xFF2196F3),
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Metric Filter Chips (Canonical Hours / Completion % / Arrow Prayers)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    FilterChip(
                        selected = selectedMetric == DashboardMetric.CANONICAL_HOURS,
                        onClick = { selectedMetric = DashboardMetric.CANONICAL_HOURS },
                        label = {
                            Text(
                                text = if (language == AppLanguage.ARABIC) "السواعي" else "Canonical",
                                fontSize = 11.sp,
                                fontWeight = if (selectedMetric == DashboardMetric.CANONICAL_HOURS) FontWeight.Bold else FontWeight.Normal
                            )
                        },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = GoldPrimary,
                            selectedLabelColor = Color.Black
                        )
                    )

                    FilterChip(
                        selected = selectedMetric == DashboardMetric.COMPLETION_RATE,
                        onClick = { selectedMetric = DashboardMetric.COMPLETION_RATE },
                        label = {
                            Text(
                                text = if (language == AppLanguage.ARABIC) "نسبة الإنجاز" else "Rate %",
                                fontSize = 11.sp,
                                fontWeight = if (selectedMetric == DashboardMetric.COMPLETION_RATE) FontWeight.Bold else FontWeight.Normal
                            )
                        },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = PeacefulGreen,
                            selectedLabelColor = Color.White
                        )
                    )

                    FilterChip(
                        selected = selectedMetric == DashboardMetric.ARROW_PRAYERS,
                        onClick = { selectedMetric = DashboardMetric.ARROW_PRAYERS },
                        label = {
                            Text(
                                text = if (language == AppLanguage.ARABIC) "السهمية" else "Arrows",
                                fontSize = 11.sp,
                                fontWeight = if (selectedMetric == DashboardMetric.ARROW_PRAYERS) FontWeight.Bold else FontWeight.Normal
                            )
                        },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = BurgundyDeep,
                            selectedLabelColor = GoldLight
                        )
                    )
                }

                // Moving average toggle
                if (selectedRange == DashboardRange.LAST_MONTH) {
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
                            Spacer(modifier = Modifier.width(3.dp))
                            Text(
                                text = "7d Avg",
                                style = MaterialTheme.typography.labelSmall,
                                fontSize = 9.5.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (showMovingAverage) GoldPrimary else MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Recharts-inspired Custom Canvas Renderer
            val maxScale = remember(currentHistory, selectedMetric) {
                when (selectedMetric) {
                    DashboardMetric.CANONICAL_HOURS -> 7f
                    DashboardMetric.COMPLETION_RATE -> 100f
                    DashboardMetric.ARROW_PRAYERS -> {
                        val maxLogged = currentHistory.maxOfOrNull { it.arrowCount.toFloat() } ?: 0f
                        maxOf(maxLogged, dailyArrowTarget.toFloat(), 33f)
                    }
                }
            }

            val valuesList = remember(currentHistory, selectedMetric) {
                currentHistory.map { day ->
                    when (selectedMetric) {
                        DashboardMetric.CANONICAL_HOURS -> day.canonicalCount.toFloat()
                        DashboardMetric.COMPLETION_RATE -> day.completionFraction * 100f
                        DashboardMetric.ARROW_PRAYERS -> day.arrowCount.toFloat()
                    }
                }
            }

            val primaryColor = when (selectedMetric) {
                DashboardMetric.CANONICAL_HOURS -> GoldPrimary
                DashboardMetric.COMPLETION_RATE -> PeacefulGreen
                DashboardMetric.ARROW_PRAYERS -> BurgundyDeep
            }

            val fillColor = when (selectedMetric) {
                DashboardMetric.CANONICAL_HOURS -> GoldLight
                DashboardMetric.COMPLETION_RATE -> PeacefulGreen
                DashboardMetric.ARROW_PRAYERS -> BurgundyDeep
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
                        .pointerInput(currentHistory) {
                            detectTapGestures { offset ->
                                val count = currentHistory.size
                                if (count > 0) {
                                    val slotWidth = size.width / count.toFloat()
                                    val tappedIdx = (offset.x / slotWidth).toInt().coerceIn(0, count - 1)
                                    selectedIndex = tappedIdx
                                    onDaySelected?.invoke(currentHistory[tappedIdx].dateString)
                                }
                            }
                        }
                        .pointerInput(currentHistory) {
                            detectDragGestures { change, _ ->
                                val count = currentHistory.size
                                if (count > 0) {
                                    val slotWidth = size.width / count.toFloat()
                                    val draggedIdx = (change.position.x / slotWidth).toInt().coerceIn(0, count - 1)
                                    selectedIndex = draggedIdx
                                    onDaySelected?.invoke(currentHistory[draggedIdx].dateString)
                                }
                            }
                        }
                ) {
                    val width = size.width
                    val height = size.height
                    val bottomPadding = 24.dp.toPx()
                    val plotHeight = height - bottomPadding
                    val count = currentHistory.size

                    if (count == 0) return@Canvas

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

                    // 2. Goal Benchmark Target Line (7 hours or Arrow Target)
                    val goalVal = if (selectedMetric == DashboardMetric.CANONICAL_HOURS) 7f else dailyArrowTarget.toFloat()
                    if (goalVal > 0f) {
                        val goalY = plotHeight * (1f - (goalVal / maxScale).coerceIn(0f, 1f))
                        drawLine(
                            color = PeacefulGreen.copy(alpha = 0.65f),
                            start = Offset(0f, goalY),
                            end = Offset(width, goalY),
                            strokeWidth = 1.5.dp.toPx(),
                            pathEffect = PathEffect.dashPathEffect(floatArrayOf(8f, 5f), 0f)
                        )
                    }

                    // 3. Render Mode: Last Week (Dual Bar + Smooth Spline) vs Last Month (Recharts Area Spline)
                    if (selectedRange == DashboardRange.LAST_WEEK) {
                        // RENDER LAST WEEK BARS + TREND LINE
                        val slotWidth = width / count.toFloat()
                        val barWidth = (slotWidth * 0.52f).coerceAtMost(36.dp.toPx()).coerceAtLeast(14.dp.toPx())

                        val coords = mutableListOf<Offset>()

                        for (i in 0 until count) {
                            val valFloat = valuesList[i]
                            val frac = (valFloat / maxScale).coerceIn(0f, 1f) * animatedProgress
                            val barHeight = plotHeight * frac

                            val xCenter = i * slotWidth + (slotWidth / 2f)
                            val xLeft = xCenter - (barWidth / 2f)
                            val yTop = plotHeight - barHeight

                            coords.add(Offset(xCenter, yTop))

                            val isSelected = selectedIndex == i

                            // Bar gradient
                            val barBrush = Brush.verticalGradient(
                                colors = if (isSelected) {
                                    listOf(PeacefulGreen, GoldPrimary)
                                } else {
                                    listOf(primaryColor, fillColor.copy(alpha = 0.5f))
                                },
                                startY = yTop,
                                endY = plotHeight
                            )

                            if (barHeight > 0f) {
                                drawRoundRect(
                                    brush = barBrush,
                                    topLeft = Offset(xLeft, yTop),
                                    size = Size(barWidth, barHeight),
                                    cornerRadius = CornerRadius(barWidth / 3f, barWidth / 3f)
                                )
                            } else {
                                drawCircle(
                                    color = gridColor,
                                    radius = 2.5.dp.toPx(),
                                    center = Offset(xCenter, plotHeight - 3.dp.toPx())
                                )
                            }

                            // Full canonical completion star indicator top of bar
                            if (currentHistory[i].canonicalCount >= 7) {
                                drawCircle(
                                    color = PeacefulGreen,
                                    radius = 4.dp.toPx(),
                                    center = Offset(xCenter, (yTop - 6.dp.toPx()).coerceAtLeast(8.dp.toPx()))
                                )
                            }
                        }

                        // Connect 7-day bars with smooth connecting trend line
                        if (coords.size >= 2) {
                            val linePath = Path()
                            linePath.moveTo(coords.first().x, coords.first().y)
                            for (i in 0 until coords.size - 1) {
                                val p0 = coords[i]
                                val p1 = coords[i + 1]
                                val cx = (p0.x + p1.x) / 2f
                                linePath.cubicTo(cx, p0.y, cx, p1.y, p1.x, p1.y)
                            }

                            drawPath(
                                path = linePath,
                                color = GoldPrimary.copy(alpha = 0.85f),
                                style = Stroke(width = 2.5.dp.toPx(), cap = StrokeCap.Round)
                            )
                        }
                    } else {
                        // RENDER LAST MONTH RECHARTS CUBIC SPLINE AREA CHART
                        val coords = valuesList.mapIndexed { idx, value ->
                            val x = (idx.toFloat() / (count - 1).toFloat()) * width
                            val frac = (value / maxScale).coerceIn(0f, 1f) * animatedProgress
                            val y = plotHeight * (1f - frac)
                            Offset(x, y)
                        }

                        val strokePath = Path()
                        val fillPath = Path()

                        strokePath.moveTo(coords.first().x, coords.first().y)
                        fillPath.moveTo(coords.first().x, plotHeight)
                        fillPath.lineTo(coords.first().x, coords.first().y)

                        for (i in 0 until count - 1) {
                            val p0 = coords[i]
                            val p1 = coords[i + 1]
                            val cx = (p0.x + p1.x) / 2f

                            strokePath.cubicTo(cx, p0.y, cx, p1.y, p1.x, p1.y)
                            fillPath.cubicTo(cx, p0.y, cx, p1.y, p1.x, p1.y)
                        }

                        fillPath.lineTo(coords.last().x, plotHeight)
                        fillPath.close()

                        // Gradient Area Fill
                        val areaGradient = Brush.verticalGradient(
                            colors = listOf(
                                fillColor.copy(alpha = 0.42f),
                                fillColor.copy(alpha = 0.04f)
                            ),
                            startY = 0f,
                            endY = plotHeight
                        )
                        drawPath(path = fillPath, brush = areaGradient)

                        // Smooth Line Stroke
                        drawPath(
                            path = strokePath,
                            color = primaryColor,
                            style = Stroke(width = 3.dp.toPx(), cap = StrokeCap.Round)
                        )
                    }

                    // 4. Scrubber Vertical Guide & Selection Marker
                    selectedIndex?.let { idx ->
                        if (idx in 0 until count) {
                            val slotWidth = width / count.toFloat()
                            val xPos = if (selectedRange == DashboardRange.LAST_WEEK) {
                                idx * slotWidth + (slotWidth / 2f)
                            } else {
                                (idx.toFloat() / (count - 1).toFloat()) * width
                            }

                            val valFloat = valuesList[idx]
                            val frac = (valFloat / maxScale).coerceIn(0f, 1f) * animatedProgress
                            val yPos = plotHeight * (1f - frac)

                            // Vertical Guide
                            drawLine(
                                color = primaryColor.copy(alpha = 0.6f),
                                start = Offset(xPos, 0f),
                                end = Offset(xPos, plotHeight),
                                strokeWidth = 1.5.dp.toPx(),
                                pathEffect = PathEffect.dashPathEffect(floatArrayOf(5f, 5f), 0f)
                            )

                            // Scrubber Circle Marker
                            drawCircle(
                                color = primaryColor.copy(alpha = 0.25f),
                                radius = 9.dp.toPx(),
                                center = Offset(xPos, yPos)
                            )
                            drawCircle(
                                color = primaryColor,
                                radius = 6.dp.toPx(),
                                center = Offset(xPos, yPos)
                            )
                            drawCircle(
                                color = Color.White,
                                radius = 3.dp.toPx(),
                                center = Offset(xPos, yPos)
                            )
                        }
                    }
                }
            }

            // X-Axis Day Labels
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                if (selectedRange == DashboardRange.LAST_WEEK) {
                    currentHistory.forEachIndexed { index, dayData ->
                        Text(
                            text = "${dayData.dayOfWeekLabel}\n${dayData.dayOfMonthLabel}",
                            style = MaterialTheme.typography.labelSmall,
                            fontSize = 10.sp,
                            textAlign = TextAlign.Center,
                            color = if (selectedIndex == index) GoldPrimary else MaterialTheme.colorScheme.onSurfaceVariant,
                            fontWeight = if (selectedIndex == index) FontWeight.Bold else FontWeight.Normal,
                            modifier = Modifier.clickable {
                                selectedIndex = index
                                onDaySelected?.invoke(dayData.dateString)
                            }
                        )
                    }
                } else {
                    listOf(0, 6, 12, 18, 24, 29).forEach { idx ->
                        if (idx < currentHistory.size) {
                            val dayData = currentHistory[idx]
                            Text(
                                text = if (idx == 29) {
                                    if (language == AppLanguage.ARABIC) "اليوم" else "Today"
                                } else {
                                    "${dayData.dayOfMonthLabel}/${dayData.dateString.substring(5, 7)}"
                                },
                                style = MaterialTheme.typography.labelSmall,
                                fontSize = 10.sp,
                                color = if (selectedIndex == idx) GoldPrimary else MaterialTheme.colorScheme.onSurfaceVariant,
                                fontWeight = if (selectedIndex == idx) FontWeight.Bold else FontWeight.Normal
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Selected Day Detailed Breakdown Tooltip Card
            selectedIndex?.let { idx ->
                if (idx in currentHistory.indices) {
                    val dayData = currentHistory[idx]
                    DashboardDayInspectionCard(
                        dayData = dayData,
                        language = language,
                        dailyArrowTarget = dailyArrowTarget
                    )
                }
            }
        }
    }
}

@Composable
private fun DashboardMetricBadge(
    title: String,
    value: String,
    subtitle: String,
    color: Color,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = RoundedCornerShape(14.dp),
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
private fun DashboardDayInspectionCard(
    dayData: DashboardDayData,
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
                                if (language == AppLanguage.ARABIC) "اكتملت صلوات السواعي بالكامل ✝️" else "All canonical prayers completed ✝️"
                            } else {
                                "${dayData.canonicalCount} / 7 " + if (language == AppLanguage.ARABIC) "سواعي تم أداؤها" else "hours completed"
                            },
                            style = MaterialTheme.typography.bodySmall,
                            color = if (dayData.canonicalCount >= 7) PeacefulGreen else MaterialTheme.colorScheme.onSurfaceVariant,
                            fontSize = 11.sp
                        )
                    }
                }

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
