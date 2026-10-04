package com.example.ui.screens

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import com.example.ui.animation.AgpeyaMotion
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.CloudDone
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.PictureAsPdf
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material.icons.filled.TableChart
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.ArrowPrayerLogEntity
import com.example.data.model.PrayerId
import com.example.data.model.PrayerLogEntity
import com.example.data.repository.AgpeyaRepository
import com.example.data.sync.SyncStatus
import com.example.localization.AgpeyaStrings
import com.example.localization.AppLanguage
import com.example.report.PrayerPdfExporter
import com.example.ui.components.CopticCrossCanvas
import com.example.ui.components.charts.AnnualPrayerHeatmap
import com.example.ui.components.charts.AreaPoint
import com.example.ui.components.charts.BarChartItem
import com.example.ui.components.charts.DailyTimelineChart
import com.example.ui.components.charts.DonutSlice
import com.example.ui.components.charts.HeatmapDayData
import com.example.ui.components.charts.PrayerActivityHeatmap
import com.example.ui.components.charts.PrayerFrequencyBarChart
import com.example.ui.components.charts.PrayerRadialDonutChart
import com.example.ui.components.charts.PrayerTrendAreaChart
import com.example.ui.components.charts.ThirtyDayPrayerTrendChart
import com.example.ui.theme.BurgundyDeep
import com.example.ui.theme.BurgundyPrimary
import com.example.ui.theme.GoldLight
import com.example.ui.theme.GoldPrimary
import com.example.ui.theme.PeacefulGreen
import com.example.ui.viewmodel.AgpeyaViewModel
import com.example.ui.viewmodel.ReportPeriod
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

// Liturgical color palette for canonical hours in charts
private val CanonicalPrayerColors = mapOf(
    PrayerId.PRIME.code to Color(0xFFD4AF37),            // Gold
    PrayerId.TERCE.code to Color(0xFFE5A93C),            // Amber
    PrayerId.SEXT.code to Color(0xFF800020),             // Burgundy
    PrayerId.NONE.code to Color(0xFF5B0E2D),             // Deep Wine
    PrayerId.VESPERS.code to Color(0xFFD97724),          // Sunset Amber
    PrayerId.COMPLINE.code to Color(0xFF2E4057),         // Twilight Blue
    PrayerId.VEIL.code to Color(0xFF4A154B),              // Veil Purple
    PrayerId.MIDNIGHT.code to Color(0xFF1B4965)          // Midnight Blue
)

@Composable
fun ReportsScreen(
    viewModel: AgpeyaViewModel,
    modifier: Modifier = Modifier
) {
    val lang by viewModel.currentLanguage.collectAsState()
    val allLogs by viewModel.allLogs.collectAsState()
    val allArrowLogs by viewModel.allArrowLogs.collectAsState()
    val totalCount by viewModel.totalCount.collectAsState()
    val totalArrowCount by viewModel.totalArrowCount.collectAsState()
    val dailyArrowTarget by viewModel.dailyArrowTarget.collectAsState()
    val syncStatus by viewModel.syncStatus.collectAsState()
    val userEmail by viewModel.userEmail.collectAsState()
    val churchName by viewModel.churchName.collectAsState()
    val syncKey by viewModel.syncKey.collectAsState()
    val context = LocalContext.current

    var selectedTabIndex by remember { mutableIntStateOf(0) }
    val tabTitles = listOf(
        AgpeyaStrings.reportDaily(lang),
        AgpeyaStrings.reportMonthly(lang),
        AgpeyaStrings.reportQuarterly(lang),
        AgpeyaStrings.reportSemiAnnual(lang),
        AgpeyaStrings.reportYearly(lang),
        if (lang == AppLanguage.ARABIC) "سجل التواريخ" else "History"
    )

    Column(modifier = modifier.fillMaxSize()) {
        // Sacred Header with Total Canonical & Arrow Stats
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surfaceVariant)
                .padding(horizontal = 16.dp, vertical = 14.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f, fill = false)
                ) {
                    CopticCrossCanvas(size = 30.dp)
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = AgpeyaStrings.visualDashboard(lang),
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = if (lang == AppLanguage.ARABIC) "صلوات السواعي والصلوات السهمية" else "Canonical Hours & Arrow Prayers",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                // Dual Badges: Canonical Hours Count & Arrow Beads Count
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = GoldPrimary.copy(alpha = 0.15f),
                        border = androidx.compose.foundation.BorderStroke(1.dp, GoldPrimary.copy(alpha = 0.35f)),
                        modifier = Modifier.padding(end = 6.dp)
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = "$totalCount",
                                style = MaterialTheme.typography.labelLarge,
                                fontWeight = FontWeight.Bold,
                                color = GoldPrimary
                            )
                            Text(
                                text = if (lang == AppLanguage.ARABIC) "سواعي" else "hours",
                                style = MaterialTheme.typography.labelSmall.copy(fontSize = 9.sp),
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = BurgundyDeep.copy(alpha = 0.15f),
                        border = androidx.compose.foundation.BorderStroke(1.dp, BurgundyDeep.copy(alpha = 0.35f)),
                        modifier = Modifier.padding(end = 8.dp)
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = "$totalArrowCount",
                                style = MaterialTheme.typography.labelLarge,
                                fontWeight = FontWeight.Bold,
                                color = BurgundyDeep
                            )
                            Text(
                                text = if (lang == AppLanguage.ARABIC) "سهمية" else "arrows",
                                style = MaterialTheme.typography.labelSmall.copy(fontSize = 9.sp),
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    // Export PDF Button
                    IconButton(
                        onClick = {
                            val periodLabel = tabTitles[selectedTabIndex]
                            PrayerPdfExporter.generateAndSharePdf(
                                context = context,
                                userEmail = userEmail,
                                userName = userEmail?.substringBefore("@"),
                                churchName = churchName,
                                syncKey = syncKey,
                                lang = lang,
                                periodName = periodLabel,
                                allLogs = allLogs,
                                allArrowLogs = allArrowLogs
                            )
                        },
                        modifier = Modifier
                            .size(34.dp)
                            .clip(CircleShape)
                            .background(GoldPrimary.copy(alpha = 0.15f))
                            .testTag("export_pdf_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.PictureAsPdf,
                            contentDescription = AgpeyaStrings.exportPdfReport(lang),
                            tint = GoldPrimary,
                            modifier = Modifier.size(18.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(4.dp))

                    IconButton(
                        onClick = { viewModel.triggerSync() },
                        modifier = Modifier
                            .size(34.dp)
                            .testTag("reports_sync_btn")
                    ) {
                        if (syncStatus == SyncStatus.SYNCING) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(16.dp),
                                strokeWidth = 2.dp,
                                color = GoldPrimary
                            )
                        } else {
                            Icon(
                                imageVector = if (syncStatus == SyncStatus.SYNCED) Icons.Default.CloudDone else Icons.Default.Sync,
                                contentDescription = "Sync",
                                tint = if (syncStatus == SyncStatus.SYNCED) Color(0xFF4CAF50) else GoldPrimary,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                }
            }
        }

        // 6 Tabs: Daily, Monthly, Quarterly, Semi-Annual, Yearly, History
        ScrollableTabRow(
            selectedTabIndex = selectedTabIndex,
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = GoldPrimary,
            edgePadding = 12.dp,
            modifier = Modifier.fillMaxWidth()
        ) {
            tabTitles.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTabIndex == index,
                    onClick = {
                        selectedTabIndex = index
                        when (index) {
                            0 -> viewModel.setReportPeriod(ReportPeriod.DAILY)
                            1 -> viewModel.setReportPeriod(ReportPeriod.MONTHLY)
                            2 -> viewModel.setReportPeriod(ReportPeriod.QUARTERLY)
                            3 -> viewModel.setReportPeriod(ReportPeriod.SEMI_ANNUAL)
                            4 -> viewModel.setReportPeriod(ReportPeriod.YEARLY)
                        }
                    },
                    text = {
                        Text(
                            text = title,
                            fontWeight = if (selectedTabIndex == index) FontWeight.Bold else FontWeight.Normal,
                            fontSize = 13.sp
                        )
                    },
                    modifier = Modifier.testTag("report_tab_$index")
                )
            }
        }

        // Selected Tab Content
        AnimatedContent(
            targetState = selectedTabIndex,
            transitionSpec = {
                val isForward = if (lang.isRtl) (targetState < initialState) else (targetState > initialState)
                AgpeyaMotion.directionalSlide(forward = isForward)
            },
            label = "report_tab_transition",
            modifier = Modifier.fillMaxSize()
        ) { tabIndex ->
            when (tabIndex) {
                0 -> DailyReportView(viewModel = viewModel, allLogs = allLogs, allArrowLogs = allArrowLogs, lang = lang)
                1 -> MonthlyReportView(viewModel = viewModel, allLogs = allLogs, allArrowLogs = allArrowLogs, lang = lang, dailyArrowTarget = dailyArrowTarget)
                2 -> QuarterlyReportView(viewModel = viewModel, allLogs = allLogs, allArrowLogs = allArrowLogs, lang = lang)
                3 -> SemiAnnualReportView(viewModel = viewModel, allLogs = allLogs, allArrowLogs = allArrowLogs, lang = lang)
                4 -> YearlyReportView(viewModel = viewModel, allLogs = allLogs, allArrowLogs = allArrowLogs, lang = lang)
                5 -> HistoryLogView(viewModel = viewModel, allLogs = allLogs, allArrowLogs = allArrowLogs, lang = lang)
            }
        }
    }
}

// ─────────────────────────────────────────────────────────────
// 1. Daily Report View (Canonical Hours + Arrow Prayers)
// ─────────────────────────────────────────────────────────────
@Composable
private fun DailyReportView(
    viewModel: AgpeyaViewModel,
    allLogs: List<PrayerLogEntity>,
    allArrowLogs: List<ArrowPrayerLogEntity>,
    lang: AppLanguage
) {
    val selectedDateStr by viewModel.selectedDateString.collectAsState()

    val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.US)
    val parsedDate = try {
        sdf.parse(selectedDateStr) ?: Date()
    } catch (e: Exception) {
        Date()
    }

    val dateLogs = remember(allLogs, selectedDateStr) {
        allLogs.filter { it.dateString == selectedDateStr }
    }
    val dateArrowLogs = remember(allArrowLogs, selectedDateStr) {
        allArrowLogs.filter { it.dateString == selectedDateStr }
    }

    val prayedCodesOnDate = remember(dateLogs) {
        dateLogs.map { it.prayerCode }.toSet()
    }

    val canonicalList = PrayerId.canonicalPrayers
    val totalCanonical = 7
    val completedCount = canonicalList.count { it != PrayerId.VEIL && prayedCodesOnDate.contains(it.code) }
    val progress = (completedCount.toFloat() / totalCanonical.toFloat()).coerceIn(0f, 1f)

    val totalDayArrowCount = dateArrowLogs.sumOf { it.count }
    val arrowSessionsCount = dateArrowLogs.size

    var showQuickArrowDialog by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp)
    ) {
        // Date Selector Row
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp, vertical = 8.dp)
                ) {
                    IconButton(onClick = {
                        val cal = Calendar.getInstance().apply {
                            time = parsedDate
                            add(Calendar.DAY_OF_YEAR, -1)
                        }
                        viewModel.setSelectedDate(sdf.format(cal.time))
                    }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                            contentDescription = "Previous Day",
                            tint = GoldPrimary
                        )
                    }

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.clickable {
                            viewModel.setSelectedDate(AgpeyaRepository.getTodayString())
                        }
                    ) {
                        val displayFormat = SimpleDateFormat("EEEE, d MMMM yyyy", Locale(lang.code))
                        Text(
                            text = displayFormat.format(parsedDate),
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        if (selectedDateStr == AgpeyaRepository.getTodayString()) {
                            Text(
                                text = if (lang == AppLanguage.ARABIC) "اليوم (الحالي)" else "Today",
                                style = MaterialTheme.typography.labelSmall,
                                color = GoldPrimary,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    IconButton(onClick = {
                        val cal = Calendar.getInstance().apply {
                            time = parsedDate
                            add(Calendar.DAY_OF_YEAR, 1)
                        }
                        viewModel.setSelectedDate(sdf.format(cal.time))
                    }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                            contentDescription = "Next Day",
                            tint = GoldPrimary
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))
        }

        // Dual Daily KPI Card: Canonical Hours & Arrow Prayers
        item {
            Card(
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column {
                            Text(
                                text = if (lang == AppLanguage.ARABIC) "تقرير الصلوات اليومية" else "Daily Prayer Completion",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = "$completedCount / $totalCanonical " +
                                        if (lang == AppLanguage.ARABIC) "سواعي تم أداؤها" else "canonical hours completed",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .background(if (completedCount >= totalCanonical) PeacefulGreen else GoldPrimary)
                                .padding(horizontal = 14.dp, vertical = 6.dp)
                        ) {
                            Text(
                                text = "${(progress * 100).toInt()}%",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = if (completedCount >= totalCanonical) Color.White else Color.Black
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    LinearProgressIndicator(
                        progress = { progress },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(8.dp)
                            .clip(RoundedCornerShape(4.dp)),
                        color = if (completedCount >= totalCanonical) PeacefulGreen else GoldPrimary,
                        trackColor = MaterialTheme.colorScheme.surfaceVariant
                    )

                    Spacer(modifier = Modifier.height(14.dp))
                    HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
                    Spacer(modifier = Modifier.height(12.dp))

                    // Arrow Prayers Daily Summary Row
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier
                                    .size(32.dp)
                                    .clip(CircleShape)
                                    .background(BurgundyDeep.copy(alpha = 0.15f))
                            ) {
                                CopticCrossCanvas(color = BurgundyDeep, modifier = Modifier.size(18.dp))
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(
                                    text = AgpeyaStrings.arrowPrayersTitle(lang),
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Text(
                                    text = if (lang == AppLanguage.ARABIC) "$totalDayArrowCount سهمية في $arrowSessionsCount جلسات" else "$totalDayArrowCount prayers across $arrowSessionsCount sessions",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }

                        Button(
                            onClick = { showQuickArrowDialog = true },
                            colors = ButtonDefaults.buttonColors(containerColor = BurgundyDeep),
                            shape = RoundedCornerShape(10.dp),
                            contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                            modifier = Modifier.testTag("quick_log_arrow_btn")
                        ) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = null,
                                tint = GoldLight,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = if (lang == AppLanguage.ARABIC) "تسجيل سهمية" else "Log Arrow",
                                color = GoldLight,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))
        }

        // 24-Hour Timeline Visual Chart
        item {
            DailyTimelineChart(
                dateLogs = dateLogs,
                completedCanonicalCount = completedCount,
                totalCanonical = totalCanonical,
                lang = lang,
                title = AgpeyaStrings.dailyProgressChart(lang),
                subtitle = if (lang == AppLanguage.ARABIC) {
                    "توزيع السواعي وأوقات الصلاة على مدار الـ 24 ساعة"
                } else {
                    "Visual hourly breakdown & canonical completion rate"
                }
            )

            Spacer(modifier = Modifier.height(16.dp))
        }

        // Section Title: Canonical Hours
        item {
            Text(
                text = AgpeyaStrings.canonicalPrayersLabel(lang),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = GoldPrimary,
                modifier = Modifier.padding(bottom = 8.dp)
            )
        }

        // Canonical Prayers Status List for this Day
        items(canonicalList, key = { it.code }) { prayerId ->
            val matchingLog = dateLogs.find { it.prayerCode == prayerId.code }
            val isPrayed = matchingLog != null

            Card(
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (isPrayed) MaterialTheme.colorScheme.surfaceVariant else MaterialTheme.colorScheme.surface
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 14.dp, vertical = 10.dp)
                ) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .size(34.dp)
                            .clip(CircleShape)
                            .background(if (isPrayed) PeacefulGreen else MaterialTheme.colorScheme.surfaceVariant)
                    ) {
                        Icon(
                            imageVector = if (isPrayed) Icons.Default.Check else Icons.Default.Close,
                            contentDescription = null,
                            tint = if (isPrayed) Color.White else MaterialTheme.colorScheme.outline,
                            modifier = Modifier.size(18.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = prayerId.getDisplayName(lang),
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        if (isPrayed && matchingLog != null) {
                            val timeStr = String.format("%02d:%02d", matchingLog.hour, matchingLog.minute)
                            Text(
                                text = if (lang == AppLanguage.ARABIC) "صُلّيت في تمام $timeStr" else "Prayed at $timeStr",
                                style = MaterialTheme.typography.bodySmall,
                                color = PeacefulGreen
                            )
                        } else {
                            Text(
                                text = if (lang == AppLanguage.ARABIC) "لم تُسجل في هذا التاريخ" else "Not recorded on this date",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    // Button to toggle log on this specific date
                    TextButton(onClick = {
                        if (isPrayed && matchingLog != null) {
                            viewModel.deleteLog(matchingLog.id)
                        } else {
                            viewModel.logPrayerForDate(prayerId, parsedDate.time)
                        }
                    }) {
                        Text(
                            text = if (isPrayed) {
                                if (lang == AppLanguage.ARABIC) "إلغاء" else "Undo"
                            } else {
                                if (lang == AppLanguage.ARABIC) "تسجيل" else "Log"
                            },
                            color = if (isPrayed) MaterialTheme.colorScheme.error else GoldPrimary,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }

        // Arrow Prayers Logged Today List (if any)
        if (dateArrowLogs.isNotEmpty()) {
            item {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = if (lang == AppLanguage.ARABIC) "الصلوات السهمية المسجلة في هذا اليوم" else "Arrow Prayers Recorded Today",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = BurgundyDeep,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }

            items(dateArrowLogs, key = { it.id }) { arrowLog ->
                val timeStr = String.format("%02d:%02d", arrowLog.hour, arrowLog.minute)
                Card(
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 14.dp, vertical = 10.dp)
                    ) {
                        Surface(
                            shape = CircleShape,
                            color = BurgundyDeep.copy(alpha = 0.15f),
                            modifier = Modifier.size(36.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Text(
                                    text = "${arrowLog.count}",
                                    style = MaterialTheme.typography.labelMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = BurgundyDeep
                                )
                            }
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = arrowLog.prayerText,
                                style = MaterialTheme.typography.bodySmall,
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = "${arrowLog.dateString} • $timeStr",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

                        IconButton(onClick = { viewModel.deleteArrowLog(arrowLog.id) }) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = "Delete arrow log",
                                tint = MaterialTheme.colorScheme.error.copy(alpha = 0.7f),
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                }
            }
        }
    }

    // Quick Arrow Prayer Log Dialog
    if (showQuickArrowDialog) {
        var selectedCount by remember { mutableIntStateOf(33) }
        var selectedTextIndex by remember { mutableIntStateOf(0) }
        val presets = listOf(
            "«يارب يسوع المسيح، ابن الله، ارحمني أنا الخاطئ» (صلاة يسوع)",
            "«يا ربي يسوع المسيح، أسرع إلى معونتي وعضدني» (سهمية الاستغاثة)",
            "«اللهم اغفر لي خطاياي ونقِ قلبي برحمتك» (سهمية التوبة)",
            "«نشكرك يا صانع الخيرات الرحوم في كل حين» (سهمية الشكر)"
        )

        AlertDialog(
            onDismissRequest = { showQuickArrowDialog = false },
            title = {
                Text(
                    text = if (lang == AppLanguage.ARABIC) "تسجيل صلوات سهمية" else "Log Arrow Prayer",
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Column {
                    Text(
                        text = if (lang == AppLanguage.ARABIC) "اختر عدد المرات:" else "Select bead count:",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        listOf(33, 50, 100).forEach { cnt ->
                            val isSelected = selectedCount == cnt
                            Surface(
                                shape = RoundedCornerShape(10.dp),
                                color = if (isSelected) BurgundyDeep else MaterialTheme.colorScheme.surfaceVariant,
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(10.dp))
                                    .clickable { selectedCount = cnt }
                            ) {
                                Text(
                                    text = "$cnt",
                                    textAlign = TextAlign.Center,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isSelected) GoldLight else MaterialTheme.colorScheme.onSurface,
                                    modifier = Modifier.padding(vertical = 8.dp)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))
                    Text(
                        text = if (lang == AppLanguage.ARABIC) "نص الصلاة السهمية:" else "Prayer text:",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    presets.forEachIndexed { idx, text ->
                        val isSel = selectedTextIndex == idx
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = if (isSel) GoldPrimary.copy(alpha = 0.15f) else Color.Transparent,
                            border = androidx.compose.foundation.BorderStroke(
                                1.dp,
                                if (isSel) GoldPrimary else MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f)
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 3.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .clickable { selectedTextIndex = idx }
                        ) {
                            Text(
                                text = text,
                                style = MaterialTheme.typography.bodySmall,
                                fontWeight = if (isSel) FontWeight.Bold else FontWeight.Normal,
                                color = if (isSel) GoldPrimary else MaterialTheme.colorScheme.onSurface,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp)
                            )
                        }
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.logArrowPrayer(selectedCount, presets[selectedTextIndex])
                        showQuickArrowDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = BurgundyDeep)
                ) {
                    Text(
                        text = if (lang == AppLanguage.ARABIC) "حفظ في التقرير" else "Save to Report",
                        color = GoldLight,
                        fontWeight = FontWeight.Bold
                    )
                }
            },
            dismissButton = {
                OutlinedButton(onClick = { showQuickArrowDialog = false }) {
                    Text(AgpeyaStrings.close(lang))
                }
            }
        )
    }
}

// ─────────────────────────────────────────────────────────────
// 2. Monthly Report View (Canonical + Arrow Prayers + Charts)
// ─────────────────────────────────────────────────────────────
@Composable
private fun MonthlyReportView(
    viewModel: AgpeyaViewModel,
    allLogs: List<PrayerLogEntity>,
    allArrowLogs: List<ArrowPrayerLogEntity>,
    lang: AppLanguage,
    dailyArrowTarget: Int = 33
) {
    val selectedYear by viewModel.selectedYear.collectAsState()
    val selectedMonth by viewModel.selectedMonth.collectAsState()

    val monthLogs = remember(allLogs, selectedYear, selectedMonth) {
        allLogs.filter { it.year == selectedYear && it.month == selectedMonth }
    }
    val monthArrowLogs = remember(allArrowLogs, selectedYear, selectedMonth) {
        allArrowLogs.filter { it.year == selectedYear && it.month == selectedMonth }
    }

    val totalMonthPrayers = monthLogs.size
    val totalMonthArrowBeads = monthArrowLogs.sumOf { it.count }
    val activeDays = remember(monthLogs, monthArrowLogs) {
        (monthLogs.map { it.day } + monthArrowLogs.map { it.day }).toSet().size
    }

    val daysInMonth = remember(selectedYear, selectedMonth) {
        val cal = Calendar.getInstance().apply {
            set(Calendar.YEAR, selectedYear)
            set(Calendar.MONTH, selectedMonth - 1)
            set(Calendar.DAY_OF_MONTH, 1)
        }
        cal.getActualMaximum(Calendar.DAY_OF_MONTH)
    }

    val dailyAverage = if (daysInMonth > 0) {
        String.format(Locale.US, "%.1f", totalMonthPrayers.toFloat() / daysInMonth.toFloat())
    } else "0.0"

    val consistencyPercent = if (daysInMonth > 0) {
        ((activeDays.toFloat() / daysInMonth.toFloat()) * 100).toInt()
    } else 0

    val monthNames = listOf(
        "January", "February", "March", "April", "May", "June",
        "July", "August", "September", "October", "November", "December"
    )
    val arabicMonthNames = listOf(
        "يناير", "فبراير", "مارس", "أبريل", "مايو", "يونيو",
        "يوليو", "أغسطس", "سبتمبر", "أكتوبر", "نوفمبر", "ديسمبر"
    )

    val currentMonthName = if (lang == AppLanguage.ARABIC) {
        arabicMonthNames[selectedMonth - 1]
    } else {
        monthNames[selectedMonth - 1]
    }

    // Daily Bar Chart Items
    val dailyBarItems = remember(monthLogs, daysInMonth) {
        (1..daysInMonth).map { day ->
            val count = monthLogs.count { it.day == day }
            BarChartItem(
                label = "$day",
                value = count,
                highlight = count >= 7
            )
        }
    }

    // Calendar Heatmap Days Data
    val heatmapDays = remember(monthLogs, selectedYear, selectedMonth, daysInMonth) {
        (1..daysInMonth).map { day ->
            val cal = Calendar.getInstance().apply {
                set(Calendar.YEAR, selectedYear)
                set(Calendar.MONTH, selectedMonth - 1)
                set(Calendar.DAY_OF_MONTH, day)
            }
            val count = monthLogs.count { it.day == day }
            HeatmapDayData(
                dayOfMonth = day,
                prayerCount = count,
                dayOfWeek = cal.get(Calendar.DAY_OF_WEEK)
            )
        }
    }

    // Donut Slices for Canonical Prayers
    val donutSlices = remember(monthLogs, lang) {
        PrayerId.canonicalPrayers.map { prayer ->
            val count = monthLogs.count { it.prayerCode == prayer.code }
            val color = CanonicalPrayerColors[prayer.code] ?: GoldPrimary
            DonutSlice(
                label = prayer.getDisplayName(lang),
                value = count,
                color = color
            )
        }
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp)
    ) {
        // Month / Year Selector
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 8.dp)
                ) {
                    IconButton(onClick = {
                        if (selectedMonth == 1) {
                            viewModel.setSelectedMonth(12)
                            viewModel.setSelectedYear(selectedYear - 1)
                        } else {
                            viewModel.setSelectedMonth(selectedMonth - 1)
                        }
                    }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                            contentDescription = "Previous Month",
                            tint = GoldPrimary
                        )
                    }

                    Text(
                        text = "$currentMonthName $selectedYear",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    IconButton(onClick = {
                        if (selectedMonth == 12) {
                            viewModel.setSelectedMonth(1)
                            viewModel.setSelectedYear(selectedYear + 1)
                        } else {
                            viewModel.setSelectedMonth(selectedMonth + 1)
                        }
                    }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                            contentDescription = "Next Month",
                            tint = GoldPrimary
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))
        }

        // 4 KPI Cards (including Arrow Prayers)
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                MetricCard(
                    title = if (lang == AppLanguage.ARABIC) "إجمالي السواعي" else "Canonical Hours",
                    value = "$totalMonthPrayers",
                    subtitle = if (lang == AppLanguage.ARABIC) "صلاة هذا الشهر" else "prayed this month",
                    modifier = Modifier.weight(1f)
                )
                MetricCard(
                    title = if (lang == AppLanguage.ARABIC) "الصلوات السهمية" else "Arrow Prayers",
                    value = "$totalMonthArrowBeads",
                    subtitle = if (lang == AppLanguage.ARABIC) "${monthArrowLogs.size} جلسة تسبيح" else "${monthArrowLogs.size} sessions",
                    modifier = Modifier.weight(1f),
                    accentColor = BurgundyDeep
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                MetricCard(
                    title = if (lang == AppLanguage.ARABIC) "أيام الصلاة" else "Active Days",
                    value = "$activeDays / $daysInMonth",
                    subtitle = if (lang == AppLanguage.ARABIC) "يوماً به صلوات" else "active days",
                    modifier = Modifier.weight(1f)
                )
                MetricCard(
                    title = AgpeyaStrings.consistencyScore(lang),
                    value = "$consistencyPercent%",
                    subtitle = if (lang == AppLanguage.ARABIC) "نسبة الالتزام" else "commitment rate",
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
        }

        // 30-Day Rolling Prayer Trend Chart (D3 / Recharts-inspired Spline & Area Fill)
        item {
            ThirtyDayPrayerTrendChart(
                allLogs = allLogs,
                allArrowLogs = allArrowLogs,
                language = lang,
                dailyArrowTarget = dailyArrowTarget,
                onDaySelected = { dateStr ->
                    viewModel.setSelectedDate(dateStr)
                }
            )

            Spacer(modifier = Modifier.height(18.dp))
        }

        // 1. Frequency Bar Chart
        item {
            val avgFloat = if (daysInMonth > 0) totalMonthPrayers.toFloat() / daysInMonth.toFloat() else 0f
            PrayerFrequencyBarChart(
                items = dailyBarItems,
                title = AgpeyaStrings.monthlyProgressChart(lang),
                subtitle = if (lang == AppLanguage.ARABIC) {
                    "تكرار الصلوات لكل يوم من أيام الشهر (1 إلى $daysInMonth)"
                } else {
                    "Prayer frequency per day (1 to $daysInMonth) with average line"
                },
                averageValue = avgFloat,
                primaryBarColor = GoldPrimary,
                secondaryBarColor = GoldLight,
                chartHeight = 180.dp,
                onItemSelected = { selectedItem ->
                    val dayInt = selectedItem.label.toIntOrNull() ?: 1
                    val dayStr = String.format("%04d-%02d-%02d", selectedYear, selectedMonth, dayInt)
                    viewModel.setSelectedDate(dayStr)
                }
            )

            Spacer(modifier = Modifier.height(18.dp))
        }

        // 2. Monthly Calendar Activity Heatmap
        item {
            PrayerActivityHeatmap(
                year = selectedYear,
                month = selectedMonth,
                daysData = heatmapDays,
                title = AgpeyaStrings.calendarActivityHeatmap(lang),
                subtitle = if (lang == AppLanguage.ARABIC) {
                    "خريطة الالتزام بحسب درجات تكرار الصلاة (انقر على أي يوم)"
                } else {
                    "Calendar commitment matrix color-coded by frequency"
                },
                lang = lang,
                onDayClicked = { day ->
                    val dayStr = String.format("%04d-%02d-%02d", selectedYear, selectedMonth, day)
                    viewModel.setSelectedDate(dayStr)
                }
            )

            Spacer(modifier = Modifier.height(18.dp))
        }

        // 3. Canonical Prayer Distribution Donut Chart
        item {
            PrayerRadialDonutChart(
                slices = donutSlices,
                title = AgpeyaStrings.prayerDistribution(lang),
                subtitle = if (lang == AppLanguage.ARABIC) {
                    "نسبة صلاة كل ساعة من سواعي الأجبية خلال الشهر"
                } else {
                    "Proportion of canonical hours prayed this month"
                },
                totalLabel = if (lang == AppLanguage.ARABIC) "صلاة" else "Prayers"
            )

            Spacer(modifier = Modifier.height(18.dp))
        }

        // 4. Canonical Hours Breakdown
        item {
            Text(
                text = AgpeyaStrings.monthlyBreakdown(lang),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = GoldPrimary,
                modifier = Modifier.padding(bottom = 8.dp)
            )
        }

        val canonicalList = PrayerId.canonicalPrayers
        val maxCount = canonicalList.maxOfOrNull { prayer ->
            monthLogs.count { it.prayerCode == prayer.code }
        }?.coerceAtLeast(1) ?: 1

        items(canonicalList, key = { it.code }) { prayerId ->
            val count = monthLogs.count { it.prayerCode == prayerId.code }
            val fraction = (count.toFloat() / maxCount.toFloat()).coerceIn(0f, 1f)
            val pColor = CanonicalPrayerColors[prayerId.code] ?: GoldPrimary

            Card(
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 5.dp)
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(10.dp)
                                    .clip(CircleShape)
                                    .background(pColor)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = prayerId.getDisplayName(lang),
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }

                        Text(
                            text = "$count " + if (lang == AppLanguage.ARABIC) "مرات" else "times",
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.Bold,
                            color = pColor
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    LinearProgressIndicator(
                        progress = { fraction },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(8.dp)
                            .clip(RoundedCornerShape(4.dp)),
                        color = pColor,
                        trackColor = MaterialTheme.colorScheme.surfaceVariant
                    )
                }
            }
        }
    }
}

// ─────────────────────────────────────────────────────────────
// 3. Quarterly Report View (Q1, Q2, Q3, Q4)
// ─────────────────────────────────────────────────────────────
@Composable
private fun QuarterlyReportView(
    viewModel: AgpeyaViewModel,
    allLogs: List<PrayerLogEntity>,
    allArrowLogs: List<ArrowPrayerLogEntity>,
    lang: AppLanguage
) {
    val selectedYear by viewModel.selectedYear.collectAsState()
    val selectedQuarter by viewModel.selectedQuarter.collectAsState()

    val quarterMonthRange = when (selectedQuarter) {
        1 -> 1..3
        2 -> 4..6
        3 -> 7..9
        else -> 10..12
    }

    val quarterLogs = remember(allLogs, selectedYear, quarterMonthRange) {
        allLogs.filter { it.year == selectedYear && it.month in quarterMonthRange }
    }
    val quarterArrowLogs = remember(allArrowLogs, selectedYear, quarterMonthRange) {
        allArrowLogs.filter { it.year == selectedYear && it.month in quarterMonthRange }
    }

    val totalQuarterCanonical = quarterLogs.size
    val totalQuarterArrowBeads = quarterArrowLogs.sumOf { it.count }
    val activeDays = remember(quarterLogs, quarterArrowLogs) {
        (quarterLogs.map { "${it.month}_${it.day}" } + quarterArrowLogs.map { "${it.month}_${it.day}" }).distinct().size
    }

    val quarterNameAr = when (selectedQuarter) {
        1 -> "الربع الأول (يناير - مارس)"
        2 -> "الربع الثاني (أبريل - يونيو)"
        3 -> "الربع الثالث (يوليو - سبتمبر)"
        else -> "الربع الرابع (أكتوبر - ديسمبر)"
    }
    val quarterNameEn = "Quarter $selectedQuarter (Q$selectedQuarter)"

    val monthNamesAr = listOf("يناير", "فبراير", "مارس", "أبريل", "مايو", "يونيو", "يوليو", "أغسطس", "سبتمبر", "أكتوبر", "نوفمبر", "ديسمبر")
    val monthNamesEn = listOf("Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec")

    // 3 Months Bar Chart Items in Quarter
    val quarterBarItems = remember(quarterLogs, selectedQuarter, quarterMonthRange) {
        quarterMonthRange.map { m ->
            val label = if (lang == AppLanguage.ARABIC) monthNamesAr[m - 1] else monthNamesEn[m - 1]
            val count = quarterLogs.count { it.month == m }
            BarChartItem(
                label = label,
                value = count,
                highlight = count > 0
            )
        }
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp)
    ) {
        // Quarter Selector Row
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        IconButton(onClick = { viewModel.setSelectedYear(selectedYear - 1) }) {
                            Icon(Icons.AutoMirrored.Filled.KeyboardArrowLeft, contentDescription = null, tint = GoldPrimary)
                        }
                        Text(
                            text = if (lang == AppLanguage.ARABIC) "$quarterNameAr $selectedYear" else "$quarterNameEn $selectedYear",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        IconButton(onClick = { viewModel.setSelectedYear(selectedYear + 1) }) {
                            Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, contentDescription = null, tint = GoldPrimary)
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Quarter Pills (Q1, Q2, Q3, Q4)
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        (1..4).forEach { q ->
                            val isSel = selectedQuarter == q
                            Surface(
                                shape = RoundedCornerShape(10.dp),
                                color = if (isSel) GoldPrimary else MaterialTheme.colorScheme.surface,
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(10.dp))
                                    .clickable { viewModel.setSelectedQuarter(q) }
                            ) {
                                Text(
                                    text = if (lang == AppLanguage.ARABIC) "الربع $q" else "Q$q",
                                    textAlign = TextAlign.Center,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isSel) Color.Black else MaterialTheme.colorScheme.onSurface,
                                    modifier = Modifier.padding(vertical = 8.dp)
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))
        }

        // 4 KPI Cards for the Quarter
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                MetricCard(
                    title = if (lang == AppLanguage.ARABIC) "سواعي الربع سنوي" else "Quarter Canonical",
                    value = "$totalQuarterCanonical",
                    subtitle = if (lang == AppLanguage.ARABIC) "صلاة خلال الربع" else "prayers in quarter",
                    modifier = Modifier.weight(1f)
                )
                MetricCard(
                    title = if (lang == AppLanguage.ARABIC) "سهمية الربع سنوي" else "Quarter Arrow",
                    value = "$totalQuarterArrowBeads",
                    subtitle = if (lang == AppLanguage.ARABIC) "${quarterArrowLogs.size} جلسة تسبيح" else "${quarterArrowLogs.size} sessions",
                    modifier = Modifier.weight(1f),
                    accentColor = BurgundyDeep
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                MetricCard(
                    title = if (lang == AppLanguage.ARABIC) "أيام الصلاة بالربع" else "Active Days in Q",
                    value = "$activeDays / 90",
                    subtitle = if (lang == AppLanguage.ARABIC) "يوماً به صلوات" else "active days",
                    modifier = Modifier.weight(1f)
                )
                MetricCard(
                    title = AgpeyaStrings.consistencyScore(lang),
                    value = "${((activeDays.toFloat() / 90f) * 100).toInt()}%",
                    subtitle = if (lang == AppLanguage.ARABIC) "نسبة التزام الربع" else "commitment rate",
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
        }

        // Quarterly Monthly Progression Chart
        item {
            val avgQuarterMonth = if (quarterBarItems.isNotEmpty()) totalQuarterCanonical.toFloat() / 3f else 0f
            PrayerFrequencyBarChart(
                items = quarterBarItems,
                title = if (lang == AppLanguage.ARABIC) "مقارنة شهور الربع سنوي" else "Quarterly Monthly Comparison",
                subtitle = if (lang == AppLanguage.ARABIC) "توزيع صلوات السواعي على مدار شهور الربع الـ 3" else "Breakdown across the 3 months of this quarter",
                averageValue = avgQuarterMonth,
                primaryBarColor = BurgundyDeep,
                secondaryBarColor = GoldPrimary,
                chartHeight = 175.dp
            )

            Spacer(modifier = Modifier.height(18.dp))
        }

        // Canonical Breakdown
        item {
            Text(
                text = if (lang == AppLanguage.ARABIC) "تفاصيل سواعي الربع سنوي" else "Quarterly Canonical Hours Breakdown",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = GoldPrimary,
                modifier = Modifier.padding(bottom = 8.dp)
            )
        }

        items(PrayerId.canonicalPrayers, key = { it.code }) { prayerId ->
            val count = quarterLogs.count { it.prayerCode == prayerId.code }
            val pColor = CanonicalPrayerColors[prayerId.code] ?: GoldPrimary

            Card(
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 14.dp, vertical = 10.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(10.dp)
                                .clip(CircleShape)
                                .background(pColor)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = prayerId.getDisplayName(lang),
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }

                    Text(
                        text = "$count " + if (lang == AppLanguage.ARABIC) "صلاة" else "times",
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.Bold,
                        color = pColor
                    )
                }
            }
        }
    }
}

// ─────────────────────────────────────────────────────────────
// 4. Semi-Annual Report View (H1, H2)
// ─────────────────────────────────────────────────────────────
@Composable
private fun SemiAnnualReportView(
    viewModel: AgpeyaViewModel,
    allLogs: List<PrayerLogEntity>,
    allArrowLogs: List<ArrowPrayerLogEntity>,
    lang: AppLanguage
) {
    val selectedYear by viewModel.selectedYear.collectAsState()
    val selectedHalfYear by viewModel.selectedHalfYear.collectAsState()

    val halfYearMonthRange = if (selectedHalfYear == 1) 1..6 else 7..12

    val halfLogs = remember(allLogs, selectedYear, halfYearMonthRange) {
        allLogs.filter { it.year == selectedYear && it.month in halfYearMonthRange }
    }
    val halfArrowLogs = remember(allArrowLogs, selectedYear, halfYearMonthRange) {
        allArrowLogs.filter { it.year == selectedYear && it.month in halfYearMonthRange }
    }

    val totalHalfCanonical = halfLogs.size
    val totalHalfArrowBeads = halfArrowLogs.sumOf { it.count }
    val activeDays = remember(halfLogs, halfArrowLogs) {
        (halfLogs.map { "${it.month}_${it.day}" } + halfArrowLogs.map { "${it.month}_${it.day}" }).distinct().size
    }

    val halfNameAr = if (selectedHalfYear == 1) "النصف الأول (يناير - يونيو)" else "النصف الثاني (يوليو - ديسمبر)"
    val halfNameEn = if (selectedHalfYear == 1) "First Half (H1: Jan - Jun)" else "Second Half (H2: Jul - Dec)"

    val monthNamesAr = listOf("يناير", "فبراير", "مارس", "أبريل", "مايو", "يونيو", "يوليو", "أغسطس", "سبتمبر", "أكتوبر", "نوفمبر", "ديسمبر")
    val monthNamesEn = listOf("Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec")

    // 6 Months Bar Chart Items
    val halfBarItems = remember(halfLogs, selectedHalfYear, halfYearMonthRange) {
        halfYearMonthRange.map { m ->
            val label = if (lang == AppLanguage.ARABIC) monthNamesAr[m - 1] else monthNamesEn[m - 1]
            val count = halfLogs.count { it.month == m }
            BarChartItem(
                label = label,
                value = count,
                highlight = count > 0
            )
        }
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp)
    ) {
        // Semi-Annual Selector
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        IconButton(onClick = { viewModel.setSelectedYear(selectedYear - 1) }) {
                            Icon(Icons.AutoMirrored.Filled.KeyboardArrowLeft, contentDescription = null, tint = GoldPrimary)
                        }
                        Text(
                            text = if (lang == AppLanguage.ARABIC) "$halfNameAr $selectedYear" else "$halfNameEn $selectedYear",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        IconButton(onClick = { viewModel.setSelectedYear(selectedYear + 1) }) {
                            Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, contentDescription = null, tint = GoldPrimary)
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        listOf(1, 2).forEach { h ->
                            val isSel = selectedHalfYear == h
                            val label = if (lang == AppLanguage.ARABIC) {
                                if (h == 1) "النصف الأول (H1)" else "النصف الثاني (H2)"
                            } else {
                                if (h == 1) "First Half (H1)" else "Second Half (H2)"
                            }
                            Surface(
                                shape = RoundedCornerShape(10.dp),
                                color = if (isSel) GoldPrimary else MaterialTheme.colorScheme.surface,
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(10.dp))
                                    .clickable { viewModel.setSelectedHalfYear(h) }
                            ) {
                                Text(
                                    text = label,
                                    textAlign = TextAlign.Center,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isSel) Color.Black else MaterialTheme.colorScheme.onSurface,
                                    modifier = Modifier.padding(vertical = 8.dp)
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))
        }

        // 4 KPI Cards for the Half-Year
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                MetricCard(
                    title = if (lang == AppLanguage.ARABIC) "سواعي النصف سنوي" else "Half-Year Canonical",
                    value = "$totalHalfCanonical",
                    subtitle = if (lang == AppLanguage.ARABIC) "صلاة بالأجبية" else "canonical prayers",
                    modifier = Modifier.weight(1f)
                )
                MetricCard(
                    title = if (lang == AppLanguage.ARABIC) "سهمية النصف سنوي" else "Half-Year Arrow",
                    value = "$totalHalfArrowBeads",
                    subtitle = if (lang == AppLanguage.ARABIC) "${halfArrowLogs.size} جلسة تسبيح" else "${halfArrowLogs.size} sessions",
                    modifier = Modifier.weight(1f),
                    accentColor = BurgundyDeep
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                MetricCard(
                    title = if (lang == AppLanguage.ARABIC) "أيام الصلاة بالنصف" else "Active Days in H",
                    value = "$activeDays / 182",
                    subtitle = if (lang == AppLanguage.ARABIC) "يوماً به صلوات" else "active days",
                    modifier = Modifier.weight(1f)
                )
                MetricCard(
                    title = AgpeyaStrings.consistencyScore(lang),
                    value = "${((activeDays.toFloat() / 182f) * 100).toInt()}%",
                    subtitle = if (lang == AppLanguage.ARABIC) "نسبة التزام النصف" else "commitment rate",
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
        }

        // 6-Month Comparison Chart
        item {
            val avgHalfMonth = if (halfBarItems.isNotEmpty()) totalHalfCanonical.toFloat() / 6f else 0f
            PrayerFrequencyBarChart(
                items = halfBarItems,
                title = if (lang == AppLanguage.ARABIC) "مقارنة شهور النصف سنوي" else "Semi-Annual Monthly Comparison",
                subtitle = if (lang == AppLanguage.ARABIC) "توزيع صلوات السواعي على مدار شهور النصف الـ 6" else "Breakdown across the 6 months of this half-year",
                averageValue = avgHalfMonth,
                primaryBarColor = BurgundyPrimary,
                secondaryBarColor = GoldPrimary,
                chartHeight = 175.dp
            )

            Spacer(modifier = Modifier.height(18.dp))
        }

        // Canonical Breakdown
        item {
            Text(
                text = if (lang == AppLanguage.ARABIC) "تفاصيل سواعي النصف سنوي" else "Semi-Annual Canonical Hours Breakdown",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = GoldPrimary,
                modifier = Modifier.padding(bottom = 8.dp)
            )
        }

        items(PrayerId.canonicalPrayers, key = { it.code }) { prayerId ->
            val count = halfLogs.count { it.prayerCode == prayerId.code }
            val pColor = CanonicalPrayerColors[prayerId.code] ?: GoldPrimary

            Card(
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 14.dp, vertical = 10.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(10.dp)
                                .clip(CircleShape)
                                .background(pColor)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = prayerId.getDisplayName(lang),
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }

                    Text(
                        text = "$count " + if (lang == AppLanguage.ARABIC) "صلاة" else "times",
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.Bold,
                        color = pColor
                    )
                }
            }
        }
    }
}

// ─────────────────────────────────────────────────────────────
// 5. Yearly Report View (Annual Harvest & 52-Week Heatmap)
// ─────────────────────────────────────────────────────────────
@Composable
private fun YearlyReportView(
    viewModel: AgpeyaViewModel,
    allLogs: List<PrayerLogEntity>,
    allArrowLogs: List<ArrowPrayerLogEntity>,
    lang: AppLanguage
) {
    val selectedYear by viewModel.selectedYear.collectAsState()

    val yearLogs = remember(allLogs, selectedYear) {
        allLogs.filter { it.year == selectedYear }
    }
    val yearArrowLogs = remember(allArrowLogs, selectedYear) {
        allArrowLogs.filter { it.year == selectedYear }
    }

    val totalYearPrayers = yearLogs.size
    val totalYearArrowBeads = yearArrowLogs.sumOf { it.count }

    val mostPrayedPrayerCode = remember(yearLogs) {
        yearLogs.groupBy { it.prayerCode }.maxByOrNull { it.value.size }?.key
    }
    val mostPrayedPrayer = mostPrayedPrayerCode?.let { PrayerId.fromCode(it) }

    val monthLabels = if (lang == AppLanguage.ARABIC) {
        listOf("يناير", "فبراير", "مارس", "أبريل", "مايو", "يونيو", "يوليو", "أغسطس", "سبتمبر", "أكتوبر", "نوفمبر", "ديسمبر")
    } else {
        listOf("Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec")
    }

    val monthCounts = remember(yearLogs) {
        (1..12).map { m -> yearLogs.count { it.month == m } }
    }

    val areaPoints = remember(monthCounts, monthLabels) {
        (0..11).map { idx ->
            AreaPoint(
                label = monthLabels[idx],
                value = monthCounts[idx].toFloat()
            )
        }
    }

    val barItems = remember(monthCounts, monthLabels) {
        (0..11).map { idx ->
            BarChartItem(
                label = monthLabels[idx],
                value = monthCounts[idx],
                highlight = monthCounts[idx] == monthCounts.maxOrNull() && monthCounts[idx] > 0
            )
        }
    }

    val annualDonutSlices = remember(yearLogs, lang) {
        PrayerId.canonicalPrayers.map { prayer ->
            val count = yearLogs.count { it.prayerCode == prayer.code }
            val color = CanonicalPrayerColors[prayer.code] ?: GoldPrimary
            DonutSlice(
                label = prayer.getDisplayName(lang),
                value = count,
                color = color
            )
        }
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp)
    ) {
        // Year Selector
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    IconButton(onClick = { viewModel.setSelectedYear(selectedYear - 1) }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                            contentDescription = "Previous Year",
                            tint = GoldPrimary
                        )
                    }

                    Text(
                        text = "$selectedYear",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    IconButton(onClick = { viewModel.setSelectedYear(selectedYear + 1) }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                            contentDescription = "Next Year",
                            tint = GoldPrimary
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))
        }

        // Yearly Highlights Card
        item {
            Card(
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Text(
                        text = if (lang == AppLanguage.ARABIC) "إجمالي حصاد الصلاة لعام $selectedYear" else "Annual Prayer Harvest for $selectedYear",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column {
                            Text(
                                text = "$totalYearPrayers",
                                style = MaterialTheme.typography.headlineLarge,
                                fontWeight = FontWeight.Bold,
                                color = GoldPrimary
                            )
                            Text(
                                text = if (lang == AppLanguage.ARABIC) "صلاة أجبية مرفوعة" else "canonical prayers offered",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

                        // Arrow Prayers Annual Summary Box
                        Surface(
                            shape = RoundedCornerShape(14.dp),
                            color = BurgundyDeep.copy(alpha = 0.15f),
                            border = androidx.compose.foundation.BorderStroke(1.dp, BurgundyDeep.copy(alpha = 0.35f))
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp)
                            ) {
                                Text(
                                    text = "$totalYearArrowBeads",
                                    style = MaterialTheme.typography.titleLarge,
                                    fontWeight = FontWeight.Bold,
                                    color = BurgundyDeep
                                )
                                Text(
                                    text = if (lang == AppLanguage.ARABIC) "سهمية سنوية" else "arrow prayers",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }

                    if (mostPrayedPrayer != null) {
                        Spacer(modifier = Modifier.height(14.dp))
                        HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = (if (lang == AppLanguage.ARABIC) "أكثر السواعي صلاةً هذا العام: " else "Most prayed canonical hour: ") +
                                    mostPrayedPrayer.getDisplayName(lang),
                            style = MaterialTheme.typography.bodySmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.secondary
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(18.dp))
        }

        // Annual 52-Week Coptic Prayer Heatmap Grid & Streaks
        item {
            AnnualPrayerHeatmap(
                year = selectedYear,
                logs = allLogs,
                lang = lang
            )

            Spacer(modifier = Modifier.height(18.dp))
        }

        // 1. Smooth Area Trajectory Chart
        item {
            PrayerTrendAreaChart(
                points = areaPoints,
                title = AgpeyaStrings.yearlyProgressChart(lang),
                subtitle = if (lang == AppLanguage.ARABIC) {
                    "منحنى بياني لانسيابية الصلوات على مدار الـ 12 شهراً"
                } else {
                    "Smooth annual trajectory curve across all 12 months"
                },
                lineColor = GoldPrimary,
                fillColor = GoldLight,
                chartHeight = 180.dp,
                onPointSelected = { pt ->
                    val monthIdx = monthLabels.indexOf(pt.label)
                    if (monthIdx >= 0) {
                        viewModel.setSelectedMonth(monthIdx + 1)
                    }
                }
            )

            Spacer(modifier = Modifier.height(18.dp))
        }

        // 2. 12-Month Frequency Bar Chart
        item {
            val avgYearMonth = if (monthCounts.isNotEmpty()) totalYearPrayers.toFloat() / 12f else 0f
            PrayerFrequencyBarChart(
                items = barItems,
                title = if (lang == AppLanguage.ARABIC) "معدل تكرار الصلوات الشهري" else "Monthly Frequency Comparison",
                subtitle = if (lang == AppLanguage.ARABIC) {
                    "مقارنة الأعمدة البيانية بين الشهور مع خط المتوسط"
                } else {
                    "Bar comparison between months with benchmark average"
                },
                averageValue = avgYearMonth,
                primaryBarColor = BurgundyPrimary,
                secondaryBarColor = GoldPrimary,
                chartHeight = 175.dp,
                onItemSelected = { item ->
                    val mIdx = monthLabels.indexOf(item.label)
                    if (mIdx >= 0) {
                        viewModel.setSelectedMonth(mIdx + 1)
                    }
                }
            )

            Spacer(modifier = Modifier.height(18.dp))
        }

        // 3. Annual Canonical Prayer Distribution Donut Chart
        item {
            PrayerRadialDonutChart(
                slices = annualDonutSlices,
                title = if (lang == AppLanguage.ARABIC) "توزيع السواعي السنوي" else "Annual Prayer Distribution",
                subtitle = if (lang == AppLanguage.ARABIC) {
                    "النسب المئوية لإتمام كل صلاة من صلوات الأجبية طوال العام"
                } else {
                    "Annual distribution breakdown by canonical hour"
                },
                totalLabel = if (lang == AppLanguage.ARABIC) "صلاة" else "Total"
            )

            Spacer(modifier = Modifier.height(18.dp))
        }
    }
}

// ─────────────────────────────────────────────────────────────
// 6. Detailed History Log View (Canonical & Arrow Prayers)
// ─────────────────────────────────────────────────────────────
@Composable
private fun HistoryLogView(
    viewModel: AgpeyaViewModel,
    allLogs: List<PrayerLogEntity>,
    allArrowLogs: List<ArrowPrayerLogEntity>,
    lang: AppLanguage
) {
    var logToDelete by remember { mutableStateOf<PrayerLogEntity?>(null) }
    var arrowLogToDelete by remember { mutableStateOf<ArrowPrayerLogEntity?>(null) }
    var selectedFilterIndex by remember { mutableIntStateOf(0) } // 0: All, 1: Canonical, 2: Arrow

    val userEmail by viewModel.userEmail.collectAsState()
    val churchName by viewModel.churchName.collectAsState()
    val syncKey by viewModel.syncKey.collectAsState()
    val context = LocalContext.current

    val filterLabels = listOf(
        if (lang == AppLanguage.ARABIC) "الكل (${allLogs.size + allArrowLogs.size})" else "All (${allLogs.size + allArrowLogs.size})",
        if (lang == AppLanguage.ARABIC) "السواعي (${allLogs.size})" else "Canonical (${allLogs.size})",
        if (lang == AppLanguage.ARABIC) "السهمية (${allArrowLogs.size})" else "Arrow (${allArrowLogs.size})"
    )

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp)
    ) {
        // Social Sharing & Export Hub Card
        item {
            Card(
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 14.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(GoldPrimary.copy(alpha = 0.2f))
                        ) {
                            Icon(
                                imageVector = Icons.Default.Share,
                                contentDescription = null,
                                tint = GoldPrimary,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = if (lang == AppLanguage.ARABIC) "تصدير ومشاركة السجل الروحي" else "Spiritual History & Export Hub",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = if (lang == AppLanguage.ARABIC) "تصدير الصلوات والسهميات بصيغ Excel و PDF والمشاركة" else "Export canonical & arrow prayers to Excel, PDF & Social",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        // 1. Social Media Streaks Share Button
                        Button(
                            onClick = {
                                com.example.report.SocialShareHelper.shareToSocialMedia(
                                    context = context,
                                    lang = lang,
                                    allLogs = allLogs
                                )
                            },
                            modifier = Modifier
                                .weight(1f)
                                .testTag("social_share_streak_btn"),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2E7D32)),
                            shape = RoundedCornerShape(12.dp),
                            contentPadding = PaddingValues(horizontal = 4.dp, vertical = 8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Share,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = if (lang == AppLanguage.ARABIC) "مشاركة" else "Social",
                                color = Color.White,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        // 2. Excel (CSV) Export Button
                        Button(
                            onClick = {
                                com.example.report.PrayerExcelExporter.generateAndShareCsv(
                                    context = context,
                                    userEmail = userEmail,
                                    userName = userEmail?.substringBefore("@"),
                                    syncKey = syncKey,
                                    lang = lang,
                                    allLogs = allLogs,
                                    allArrowLogs = allArrowLogs
                                )
                            },
                            modifier = Modifier
                                .weight(1f)
                                .testTag("export_excel_button"),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1B5E20)),
                            shape = RoundedCornerShape(12.dp),
                            contentPadding = PaddingValues(horizontal = 4.dp, vertical = 8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.TableChart,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = if (lang == AppLanguage.ARABIC) "Excel" else "Excel",
                                color = Color.White,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        // 3. PDF Report Export Button
                        Button(
                            onClick = {
                                PrayerPdfExporter.generateAndSharePdf(
                                    context = context,
                                    userEmail = userEmail,
                                    userName = userEmail?.substringBefore("@"),
                                    churchName = churchName,
                                    syncKey = syncKey,
                                    lang = lang,
                                    periodName = AgpeyaStrings.prayersLogHistory(lang),
                                    allLogs = allLogs,
                                    allArrowLogs = allArrowLogs
                                )
                            },
                            modifier = Modifier
                                .weight(1f)
                                .testTag("export_pdf_button"),
                            colors = ButtonDefaults.buttonColors(containerColor = GoldPrimary),
                            shape = RoundedCornerShape(12.dp),
                            contentPadding = PaddingValues(horizontal = 4.dp, vertical = 8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.PictureAsPdf,
                                contentDescription = null,
                                tint = Color.Black,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = if (lang == AppLanguage.ARABIC) "PDF" else "PDF",
                                color = Color.Black,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }

        // Filter Pills Row
        item {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp)
            ) {
                filterLabels.forEachIndexed { idx, label ->
                    val isSel = selectedFilterIndex == idx
                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = if (isSel) GoldPrimary else MaterialTheme.colorScheme.surfaceVariant,
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(10.dp))
                            .clickable { selectedFilterIndex = idx }
                    ) {
                        Text(
                            text = label,
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = if (isSel) FontWeight.Bold else FontWeight.Normal,
                            color = if (isSel) Color.Black else MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.padding(vertical = 8.dp)
                        )
                    }
                }
            }
        }

        // 1. Canonical Prayers Logs
        if (selectedFilterIndex == 0 || selectedFilterIndex == 1) {
            if (allLogs.isNotEmpty()) {
                item {
                    Text(
                        text = AgpeyaStrings.canonicalPrayersLabel(lang),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = GoldPrimary,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                }

                items(allLogs, key = { "can_${it.id}" }) { log ->
                    val prayerId = PrayerId.fromCode(log.prayerCode)
                    val timeStr = String.format("%02d:%02d", log.hour, log.minute)

                    Card(
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 14.dp, vertical = 10.dp)
                        ) {
                            CopticCrossCanvas(size = 24.dp)
                            Spacer(modifier = Modifier.width(12.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = prayerId.getDisplayName(lang),
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Text(
                                    text = "${log.dateString} • $timeStr",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }

                            IconButton(onClick = { logToDelete = log }) {
                                Icon(
                                    imageVector = Icons.Default.Delete,
                                    contentDescription = "Delete entry",
                                    tint = MaterialTheme.colorScheme.error.copy(alpha = 0.7f),
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                    }
                }
            }
        }

        // 2. Arrow Prayers Logs
        if (selectedFilterIndex == 0 || selectedFilterIndex == 2) {
            if (allArrowLogs.isNotEmpty()) {
                item {
                    Spacer(modifier = Modifier.height(14.dp))
                    Text(
                        text = AgpeyaStrings.arrowPrayersTitle(lang),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = BurgundyDeep,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                }

                items(allArrowLogs, key = { "arr_${it.id}" }) { arrowLog ->
                    val timeStr = String.format("%02d:%02d", arrowLog.hour, arrowLog.minute)

                    Card(
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 14.dp, vertical = 10.dp)
                        ) {
                            Surface(
                                shape = CircleShape,
                                color = BurgundyDeep.copy(alpha = 0.15f),
                                modifier = Modifier.size(36.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Text(
                                        text = "${arrowLog.count}",
                                        style = MaterialTheme.typography.labelMedium,
                                        fontWeight = FontWeight.Bold,
                                        color = BurgundyDeep
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.width(12.dp))

                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = arrowLog.prayerText,
                                    style = MaterialTheme.typography.bodySmall,
                                    fontWeight = FontWeight.SemiBold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Text(
                                    text = "${arrowLog.dateString} • $timeStr • ${arrowLog.count} " + (if (lang == AppLanguage.ARABIC) "سهمية" else "beads"),
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }

                            IconButton(onClick = { arrowLogToDelete = arrowLog }) {
                                Icon(
                                    imageVector = Icons.Default.Delete,
                                    contentDescription = "Delete entry",
                                    tint = MaterialTheme.colorScheme.error.copy(alpha = 0.7f),
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                    }
                }
            }
        }

        if (allLogs.isEmpty() && allArrowLogs.isEmpty()) {
            item {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(40.dp)
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        CopticCrossCanvas(size = 48.dp)
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = AgpeyaStrings.noLogsYet(lang),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }
    }

    // Confirm Delete Canonical Log Dialog
    logToDelete?.let { log ->
        AlertDialog(
            onDismissRequest = { logToDelete = null },
            title = { Text(AgpeyaStrings.deleteConfirm(lang)) },
            text = {
                val prayer = PrayerId.fromCode(log.prayerCode)
                Text("${prayer.getDisplayName(lang)} - ${log.dateString}")
            },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.deleteLog(log.id)
                        logToDelete = null
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                ) {
                    Text(if (lang == AppLanguage.ARABIC) "حذف" else "Delete")
                }
            },
            dismissButton = {
                OutlinedButton(onClick = { logToDelete = null }) {
                    Text(AgpeyaStrings.close(lang))
                }
            }
        )
    }

    // Confirm Delete Arrow Log Dialog
    arrowLogToDelete?.let { arrowLog ->
        AlertDialog(
            onDismissRequest = { arrowLogToDelete = null },
            title = { Text(AgpeyaStrings.deleteConfirm(lang)) },
            text = {
                Text("${arrowLog.prayerText} (${arrowLog.count}) - ${arrowLog.dateString}")
            },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.deleteArrowLog(arrowLog.id)
                        arrowLogToDelete = null
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                ) {
                    Text(if (lang == AppLanguage.ARABIC) "حذف" else "Delete")
                }
            },
            dismissButton = {
                OutlinedButton(onClick = { arrowLogToDelete = null }) {
                    Text(AgpeyaStrings.close(lang))
                }
            }
        )
    }
}

@Composable
private fun MetricCard(
    title: String,
    value: String,
    subtitle: String,
    modifier: Modifier = Modifier,
    accentColor: Color = GoldPrimary
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = modifier
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Text(
                text = title,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = value,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = accentColor
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = subtitle,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
