package com.example.ui.components.charts

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import java.util.Calendar

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
