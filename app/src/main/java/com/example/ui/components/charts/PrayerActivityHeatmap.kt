package com.example.ui.components.charts

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.rememberScrollState
import java.util.Calendar
import java.util.Locale
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.localization.AppLanguage
import com.example.ui.theme.GoldPrimary
import com.example.ui.theme.PeacefulGreen

data class HeatmapDayData(
    val dayOfMonth: Int,
    val prayerCount: Int,
    val dayOfWeek: Int // Calendar.SUNDAY ... Calendar.SATURDAY
)

/**
 * D3-inspired Calendar Activity Heatmap for Jetpack Compose.
 * Displays a 7-column calendar matrix where cell color intensity reflects
 * the daily prayer count, supporting interactive inspection on tap.
 */
@Composable
fun PrayerActivityHeatmap(
    year: Int,
    month: Int, // 1-12
    daysData: List<HeatmapDayData>,
    title: String,
    subtitle: String? = null,
    lang: AppLanguage,
    modifier: Modifier = Modifier,
    onDayClicked: ((Int) -> Unit)? = null
) {
    var selectedDay by remember { mutableStateOf<HeatmapDayData?>(null) }

    // Weekday labels
    val weekDays = if (lang == AppLanguage.ARABIC) {
        listOf("أحد", "إثن", "ثلا", "أرب", "خمي", "جمع", "سبت")
    } else {
        listOf("S", "M", "T", "W", "T", "F", "S")
    }

    // Determine leading empty cells for the 1st day of the month
    val firstDayCal = remember(year, month) {
        Calendar.getInstance().apply {
            set(Calendar.YEAR, year)
            set(Calendar.MONTH, month - 1)
            set(Calendar.DAY_OF_MONTH, 1)
        }
    }
    val startDayOfWeek = firstDayCal.get(Calendar.DAY_OF_WEEK) // 1 = Sunday ... 7 = Saturday
    val leadingEmptyCells = startDayOfWeek - 1

    Card(
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = modifier
            .fillMaxWidth()
            .testTag("prayer_activity_heatmap")
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

                // Selected Day Badge
                selectedDay?.let { day ->
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = MaterialTheme.colorScheme.surfaceVariant,
                        border = androidx.compose.foundation.BorderStroke(1.dp, GoldPrimary.copy(alpha = 0.5f))
                    ) {
                        Text(
                            text = if (lang == AppLanguage.ARABIC) {
                                "يوم ${day.dayOfMonth}: ${day.prayerCount} صلوات"
                            } else {
                                "Day ${day.dayOfMonth}: ${day.prayerCount} prayers"
                            },
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = GoldPrimary,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Weekday Header Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                weekDays.forEach { label ->
                    Text(
                        text = label,
                        style = MaterialTheme.typography.labelSmall.copy(fontSize = 11.sp),
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontWeight = FontWeight.SemiBold,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Days Grid
            val totalCells = leadingEmptyCells + daysData.size
            val rows = (totalCells + 6) / 7

            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                for (r in 0 until rows) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(5.dp)
                    ) {
                        for (c in 0..6) {
                            val cellIndex = r * 7 + c
                            val dayIndex = cellIndex - leadingEmptyCells

                            if (dayIndex in daysData.indices) {
                                val dayData = daysData[dayIndex]
                                val count = dayData.prayerCount
                                val isSelected = selectedDay?.dayOfMonth == dayData.dayOfMonth

                                // Color intensity based on frequency
                                val cellColor = when {
                                    count == 0 -> MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f)
                                    count in 1..2 -> GoldPrimary.copy(alpha = 0.32f)
                                    count in 3..4 -> GoldPrimary.copy(alpha = 0.68f)
                                    count in 5..6 -> GoldPrimary
                                    else -> PeacefulGreen // 7+ canonical completed!
                                }

                                val textColor = when {
                                    count >= 5 -> Color.White
                                    count >= 3 -> Color.Black
                                    else -> MaterialTheme.colorScheme.onSurface
                                }

                                Box(
                                    contentAlignment = Alignment.Center,
                                    modifier = Modifier
                                        .weight(1f)
                                        .aspectRatio(1f)
                                        .clip(RoundedCornerShape(6.dp))
                                        .background(cellColor)
                                        .then(
                                            if (isSelected) {
                                                Modifier.border(2.dp, GoldPrimary, RoundedCornerShape(6.dp))
                                            } else Modifier
                                        )
                                        .clickable {
                                            selectedDay = dayData
                                            onDayClicked?.invoke(dayData.dayOfMonth)
                                        }
                                ) {
                                    Text(
                                        text = "${dayData.dayOfMonth}",
                                        style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.5.sp),
                                        fontWeight = if (count > 0) FontWeight.Bold else FontWeight.Normal,
                                        color = textColor
                                    )
                                }
                            } else {
                                // Empty cell spacer
                                Spacer(
                                    modifier = Modifier
                                        .weight(1f)
                                        .aspectRatio(1f)
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Color Intensity Legend (D3 style)
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.End,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = if (lang == AppLanguage.ARABIC) "أقل" else "Less",
                    style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.width(6.dp))

                listOf(
                    MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f),
                    GoldPrimary.copy(alpha = 0.32f),
                    GoldPrimary.copy(alpha = 0.68f),
                    GoldPrimary,
                    PeacefulGreen
                ).forEach { color ->
                    Box(
                        modifier = Modifier
                            .size(12.dp)
                            .clip(RoundedCornerShape(3.dp))
                            .background(color)
                    )
                    Spacer(modifier = Modifier.width(3.dp))
                }

                Spacer(modifier = Modifier.width(3.dp))
                Text(
                    text = if (lang == AppLanguage.ARABIC) "أكثر (7)" else "More (7)",
                    style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

/**
 * GitHub-style 52-Week Annual Coptic Prayer Calendar Heatmap Grid & Streak Metrics.
 */
@Composable
fun AnnualPrayerHeatmap(
    year: Int,
    logs: List<com.example.data.model.PrayerLogEntity>,
    lang: AppLanguage,
    modifier: Modifier = Modifier
) {
    var selectedDayInfo by remember { mutableStateOf<String?>(null) }

    // Map logs to dateString "YYYY-MM-DD" -> count
    val dailyCounts = remember(logs, year) {
        val map = mutableMapOf<String, Int>()
        for (log in logs) {
            if (log.year == year) {
                map[log.dateString] = (map[log.dateString] ?: 0) + 1
            }
        }
        map
    }

    // Streak Calculations
    val (currentStreak, longestStreak, activeDaysCount, totalDaysElapsed) = remember(dailyCounts, year) {
        val cal = Calendar.getInstance()
        val currentYear = cal.get(Calendar.YEAR)
        val todayJulian = if (year == currentYear) cal.get(Calendar.DAY_OF_YEAR) else if (year < currentYear) 365 else 0

        var current = 0
        var maxStreak = 0
        var tempStreak = 0
        var totalActive = 0

        val tempCal = Calendar.getInstance()
        val daysInYear = if (year % 4 == 0 && (year % 100 != 0 || year % 400 == 0)) 366 else 365

        // Calculate streaks & active days up to todayJulian
        for (d in 1..daysInYear) {
            tempCal.set(Calendar.YEAR, year)
            tempCal.set(Calendar.DAY_OF_YEAR, d)
            val y = tempCal.get(Calendar.YEAR)
            val m = tempCal.get(Calendar.MONTH) + 1
            val day = tempCal.get(Calendar.DAY_OF_MONTH)
            val dateStr = String.format(Locale.US, "%04d-%02d-%02d", y, m, day)

            val count = dailyCounts[dateStr] ?: 0
            if (count > 0) {
                totalActive++
                tempStreak++
                if (tempStreak > maxStreak) maxStreak = tempStreak
            } else {
                tempStreak = 0
            }

            // Check current streak ending today or yesterday
            if (d == todayJulian || (d == todayJulian - 1 && count > 0)) {
                current = tempStreak
            }
        }

        val elapsed = if (year == currentYear) todayJulian else daysInYear
        Quadruple(current, maxStreak, totalActive, elapsed.coerceAtLeast(1))
    }

    val consistencyPercent = remember(activeDaysCount, totalDaysElapsed) {
        ((activeDaysCount.toFloat() / totalDaysElapsed) * 100).toInt().coerceIn(0, 100)
    }

    Card(
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = modifier
            .fillMaxWidth()
            .testTag("annual_prayer_heatmap")
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Section Title
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column {
                    Text(
                        text = com.example.localization.AgpeyaStrings.annualHeatmapTitle(lang),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = if (lang == AppLanguage.ARABIC) "سنة $year - 52 أسبوعًا من الصلوات" else "Year $year - 52 Weeks Grid",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                selectedDayInfo?.let { info ->
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = GoldPrimary.copy(alpha = 0.15f),
                        border = androidx.compose.foundation.BorderStroke(1.dp, GoldPrimary.copy(alpha = 0.5f))
                    ) {
                        Text(
                            text = info,
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = GoldPrimary,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Streak & Metrics Cards Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Current Streak
                Card(
                    modifier = Modifier.weight(1f),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(10.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "🔥 $currentStreak",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.ExtraBold,
                            color = GoldPrimary
                        )
                        Text(
                            text = com.example.localization.AgpeyaStrings.currentStreakTitle(lang),
                            style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                            textAlign = TextAlign.Center,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                // Longest Streak
                Card(
                    modifier = Modifier.weight(1f),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(10.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "🏆 $longestStreak",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.ExtraBold,
                            color = GoldPrimary
                        )
                        Text(
                            text = com.example.localization.AgpeyaStrings.longestStreakTitle(lang),
                            style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                            textAlign = TextAlign.Center,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                // Annual Consistency %
                Card(
                    modifier = Modifier.weight(1f),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(10.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "📈 $consistencyPercent%",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.ExtraBold,
                            color = PeacefulGreen
                        )
                        Text(
                            text = com.example.localization.AgpeyaStrings.annualConsistencyTitle(lang),
                            style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                            textAlign = TextAlign.Center,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // 52-Week Horizontal Scrollable Grid Matrix
            val scrollState = rememberScrollState()
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(scrollState)
            ) {
                // 7 Rows for Days of Week (Sun..Sat)
                Column(
                    verticalArrangement = Arrangement.spacedBy(3.dp)
                ) {
                    val daysInYear = if (year % 4 == 0 && (year % 100 != 0 || year % 400 == 0)) 366 else 365
                    val calendar = Calendar.getInstance()

                    // Build 7 rows (dow 0..6)
                    for (dow in 0..6) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(3.dp)
                        ) {
                            // 52 Weeks
                            for (week in 0..52) {
                                val dayOfYearIndex = week * 7 + dow + 1
                                if (dayOfYearIndex <= daysInYear) {
                                    calendar.set(Calendar.YEAR, year)
                                    calendar.set(Calendar.DAY_OF_YEAR, dayOfYearIndex)

                                    val y = calendar.get(Calendar.YEAR)
                                    val m = calendar.get(Calendar.MONTH) + 1
                                    val d = calendar.get(Calendar.DAY_OF_MONTH)
                                    val dateStr = String.format(Locale.US, "%04d-%02d-%02d", y, m, d)

                                    val count = dailyCounts[dateStr] ?: 0

                                    val cellColor = when {
                                        count == 0 -> MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                                        count in 1..2 -> GoldPrimary.copy(alpha = 0.35f)
                                        count in 3..4 -> GoldPrimary.copy(alpha = 0.7f)
                                        count in 5..6 -> GoldPrimary
                                        else -> PeacefulGreen
                                    }

                                    Box(
                                        modifier = Modifier
                                            .size(11.dp)
                                            .clip(RoundedCornerShape(2.5.dp))
                                            .background(cellColor)
                                            .clickable {
                                                selectedDayInfo = if (lang == AppLanguage.ARABIC) {
                                                    "$d/$m/$y: $count صلوات"
                                                } else {
                                                    "$y-$m-$d: $count prayers"
                                                }
                                            }
                                    )
                                } else {
                                    Spacer(modifier = Modifier.size(11.dp))
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Grid Legend
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.End,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = if (lang == AppLanguage.ARABIC) "أقل" else "Less",
                    style = MaterialTheme.typography.labelSmall.copy(fontSize = 9.5.sp),
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.width(4.dp))
                listOf(
                    MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                    GoldPrimary.copy(alpha = 0.35f),
                    GoldPrimary.copy(alpha = 0.7f),
                    GoldPrimary,
                    PeacefulGreen
                ).forEach { color ->
                    Box(
                        modifier = Modifier
                            .size(10.dp)
                            .clip(RoundedCornerShape(2.dp))
                            .background(color)
                    )
                    Spacer(modifier = Modifier.width(3.dp))
                }
                Spacer(modifier = Modifier.width(2.dp))
                Text(
                    text = if (lang == AppLanguage.ARABIC) "أكثر" else "More",
                    style = MaterialTheme.typography.labelSmall.copy(fontSize = 9.5.sp),
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

private data class Quadruple<A, B, C, D>(val first: A, val second: B, val third: C, val fourth: D)
