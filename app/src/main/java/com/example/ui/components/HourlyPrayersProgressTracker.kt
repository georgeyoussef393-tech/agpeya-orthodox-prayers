package com.example.ui.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.RadioButtonUnchecked
import androidx.compose.material.icons.filled.Storage
import androidx.compose.material.icons.filled.Today
import androidx.compose.material.icons.outlined.AccessTime
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.coptic.CopticCalendarHelper
import com.example.data.model.HourlyPrayerCompletionEntity
import com.example.data.model.HourlyPrayerDailySummary
import com.example.data.model.HourlyPrayerWeeklyProgress
import com.example.data.model.PrayerId
import com.example.localization.AgpeyaStrings
import com.example.localization.AppLanguage
import com.example.ui.theme.BurgundyDeep
import com.example.ui.theme.BurgundyLight
import com.example.ui.theme.GoldLight
import com.example.ui.theme.GoldPrimary
import com.example.ui.theme.PeacefulGreen
import com.example.ui.viewmodel.AgpeyaViewModel

/**
 * UI Component to track completed hourly prayers (الأجبية) stored in Room Database,
 * displaying progress for both the current day and the current week.
 */
@Composable
fun HourlyPrayersProgressTracker(
    viewModel: AgpeyaViewModel,
    onOpenPrayerReading: (PrayerId) -> Unit = {},
    modifier: Modifier = Modifier
) {
    val lang by viewModel.currentLanguage.collectAsState()
    val isAr = lang == AppLanguage.ARABIC

    val todayCompletions by viewModel.todayHourlyCompletions.collectAsState()
    val weeklyProgress by viewModel.weeklyHourlyProgress.collectAsState()

    var selectedViewMode by remember { mutableIntStateOf(0) } // 0: Today, 1: Current Week, 2: Both
    var isExpanded by remember { mutableStateOf(true) }

    val todayCompletedCodes = remember(todayCompletions) {
        todayCompletions.map { it.prayerCode }.toSet()
    }

    val canonicalList = PrayerId.canonicalPrayers
    val totalCanonicalCount = 7 // Prime to Midnight (Veil is monastic)
    val completedCanonicalToday = canonicalList.count { it != PrayerId.VEIL && todayCompletedCodes.contains(it.code) }
    val todayFraction = (completedCanonicalToday.toFloat() / totalCanonicalCount.toFloat()).coerceIn(0f, 1f)

    val animatedDayProgress by animateFloatAsState(
        targetValue = todayFraction,
        animationSpec = tween(durationMillis = 240, easing = FastOutSlowInEasing),
        label = "animated_day_progress"
    )

    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        border = BorderStroke(1.2.dp, GoldPrimary.copy(alpha = 0.5f)),
        modifier = modifier
            .fillMaxWidth()
            .testTag("hourly_prayers_progress_tracker")
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            // Header Bar
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .size(42.dp)
                            .clip(CircleShape)
                            .background(
                                Brush.linearGradient(
                                    colors = listOf(BurgundyDeep, BurgundyLight)
                                )
                            )
                            .border(1.dp, GoldPrimary, CircleShape)
                    ) {
                        CopticCrossCanvas(size = 24.dp, color = GoldLight)
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = if (isAr) "متابعة صلوات السواعي" else "Hourly Prayers Tracker",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Surface(
                                shape = RoundedCornerShape(6.dp),
                                color = PeacefulGreen.copy(alpha = 0.15f)
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Storage,
                                        contentDescription = "Room DB",
                                        tint = PeacefulGreen,
                                        modifier = Modifier.size(10.dp)
                                    )
                                    Spacer(modifier = Modifier.width(3.dp))
                                    Text(
                                        text = "Room DB",
                                        fontSize = 9.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = PeacefulGreen
                                    )
                                }
                            }
                        }

                        Text(
                            text = if (isAr) "تسجيل الإنجاز لليوم والأسبوع الحالي" else "Daily & Weekly Progress Tracking",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                Surface(
                    shape = CircleShape,
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .clickable { isExpanded = !isExpanded }
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = if (isExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                            contentDescription = if (isExpanded) "Collapse" else "Expand",
                            tint = GoldPrimary
                        )
                    }
                }
            }

            AnimatedVisibility(visible = isExpanded) {
                Column(modifier = Modifier.padding(top = 16.dp)) {
                    // View Mode Tabs: [اليوم (Day)] - [الأسبوع الحالي (Week)] - [الكل (Both)]
                    TabRow(
                        selectedTabIndex = selectedViewMode,
                        containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                        contentColor = GoldPrimary,
                        indicator = { tabPositions ->
                            TabRowDefaults.SecondaryIndicator(
                                modifier = Modifier.tabIndicatorOffset(tabPositions[selectedViewMode]),
                                color = GoldPrimary,
                                height = 3.dp
                            )
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                    ) {
                        Tab(
                            selected = selectedViewMode == 0,
                            onClick = { selectedViewMode = 0 },
                            text = {
                                Text(
                                    text = if (isAr) "☀️ تقدم اليوم ($completedCanonicalToday / $totalCanonicalCount)" else "☀️ Today ($completedCanonicalToday / $totalCanonicalCount)",
                                    fontSize = 12.sp,
                                    fontWeight = if (selectedViewMode == 0) FontWeight.Bold else FontWeight.Normal
                                )
                            }
                        )
                        Tab(
                            selected = selectedViewMode == 1,
                            onClick = { selectedViewMode = 1 },
                            text = {
                                Text(
                                    text = if (isAr) "📅 الأسبوع الحالي (${weeklyProgress.totalWeeklyCompleted} صلاة)" else "📅 This Week (${weeklyProgress.totalWeeklyCompleted})",
                                    fontSize = 12.sp,
                                    fontWeight = if (selectedViewMode == 1) FontWeight.Bold else FontWeight.Normal
                                )
                            }
                        )
                        Tab(
                            selected = selectedViewMode == 2,
                            onClick = { selectedViewMode = 2 },
                            text = {
                                Text(
                                    text = if (isAr) "📊 عرض شامل" else "📊 Both",
                                    fontSize = 12.sp,
                                    fontWeight = if (selectedViewMode == 2) FontWeight.Bold else FontWeight.Normal
                                )
                            }
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Content Switcher
                    when (selectedViewMode) {
                        0 -> {
                            TodayHourlyProgressSection(
                                canonicalList = canonicalList,
                                completedCodes = todayCompletedCodes,
                                completedCount = completedCanonicalToday,
                                totalCount = totalCanonicalCount,
                                progressFraction = animatedDayProgress,
                                lang = lang,
                                onTogglePrayer = { viewModel.toggleHourlyPrayer(it) },
                                onOpenReading = onOpenPrayerReading
                            )
                        }
                        1 -> {
                            CurrentWeekProgressSection(
                                weeklyProgress = weeklyProgress,
                                lang = lang
                            )
                        }
                        2 -> {
                            Column {
                                TodayHourlyProgressSection(
                                    canonicalList = canonicalList,
                                    completedCodes = todayCompletedCodes,
                                    completedCount = completedCanonicalToday,
                                    totalCount = totalCanonicalCount,
                                    progressFraction = animatedDayProgress,
                                    lang = lang,
                                    onTogglePrayer = { viewModel.toggleHourlyPrayer(it) },
                                    onOpenReading = onOpenPrayerReading
                                )
                                Spacer(modifier = Modifier.height(16.dp))
                                HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
                                Spacer(modifier = Modifier.height(16.dp))
                                CurrentWeekProgressSection(
                                    weeklyProgress = weeklyProgress,
                                    lang = lang
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

/**
 * Daily section displaying the 7 canonical hourly prayers with interactive check toggle buttons.
 */
@Composable
private fun TodayHourlyProgressSection(
    canonicalList: List<PrayerId>,
    completedCodes: Set<String>,
    completedCount: Int,
    totalCount: Int,
    progressFraction: Float,
    lang: AppLanguage,
    onTogglePrayer: (PrayerId) -> Unit,
    onOpenReading: (PrayerId) -> Unit
) {
    val isAr = lang == AppLanguage.ARABIC

    Column(modifier = Modifier.fillMaxWidth()) {
        // Daily Summary Card with Circular Progress
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = BurgundyDeep.copy(alpha = 0.08f),
            border = BorderStroke(1.dp, BurgundyDeep.copy(alpha = 0.2f)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.size(64.dp)
                ) {
                    CircularProgressIndicator(
                        progress = { progressFraction },
                        modifier = Modifier.fillMaxSize(),
                        color = GoldPrimary,
                        trackColor = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f),
                        strokeWidth = 6.dp,
                        strokeCap = StrokeCap.Round
                    )
                    Text(
                        text = "${(progressFraction * 100).toInt()}%",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = BurgundyDeep
                    )
                }

                Spacer(modifier = Modifier.width(16.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = if (isAr) "$completedCount من أصل $totalCount صلوات مكتملة اليوم" else "$completedCount of $totalCount prayers completed today",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = if (completedCount == totalCount) {
                            if (isAr) "✨ تم إتمام جميع صلوات السواعي بنجاح مبارك!" else "✨ All canonical hours completed today!"
                        } else {
                            if (isAr) "«سبع مرات في اليوم سبحتك على أحكام عدلك» (مز 119)" else "«Seven times a day I praise You» (Ps 119)"
                        },
                        style = MaterialTheme.typography.bodySmall,
                        color = if (completedCount == totalCount) PeacefulGreen else GoldPrimary,
                        lineHeight = 16.sp
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Hourly Prayer Check Items Grid / Column
        val currentHourPrayer = remember { PrayerId.getCurrentPrayerForTime() }
        canonicalList.forEach { prayerId ->
            val isCompleted = completedCodes.contains(prayerId.code)
            val isCurrent = prayerId == currentHourPrayer
            HourlyPrayerItemRow(
                prayerId = prayerId,
                isCompleted = isCompleted,
                isCurrentPrayer = isCurrent,
                lang = lang,
                onToggle = { onTogglePrayer(prayerId) },
                onRead = { onOpenReading(prayerId) }
            )
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

/**
 * Single Hourly Prayer Row with toggle check and fast read link.
 */
@Composable
private fun HourlyPrayerItemRow(
    prayerId: PrayerId,
    isCompleted: Boolean,
    isCurrentPrayer: Boolean = false,
    lang: AppLanguage,
    onToggle: () -> Unit,
    onRead: () -> Unit
) {
    val isAr = lang == AppLanguage.ARABIC
    val bgColor by animateColorAsState(
        targetValue = if (isCompleted) PeacefulGreen.copy(alpha = 0.10f) else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.45f),
        label = "hourly_row_bg"
    )

    val infiniteTransition = rememberInfiniteTransition(label = "hourly_row_pulse")
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 1.0f,
        targetValue = 1.05f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse_scale"
    )
    val pulseGlowAlpha by infiniteTransition.animateFloat(
        initialValue = 0.35f,
        targetValue = 0.95f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse_glow_alpha"
    )

    Surface(
        shape = RoundedCornerShape(12.dp),
        color = bgColor,
        border = BorderStroke(
            1.dp,
            if (isCompleted) {
                PeacefulGreen.copy(alpha = 0.5f)
            } else if (isCurrentPrayer) {
                GoldPrimary.copy(alpha = pulseGlowAlpha)
            } else {
                MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f)
            }
        ),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onToggle() }
            .testTag("hourly_row_${prayerId.code.lowercase()}")
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 10.dp)
        ) {
            // Checkbox / Completion Icon
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape)
                    .background(
                        if (isCompleted) PeacefulGreen else MaterialTheme.colorScheme.surface
                    )
                    .border(
                        1.5.dp,
                        if (isCompleted) PeacefulGreen else GoldPrimary.copy(alpha = 0.7f),
                        CircleShape
                    )
            ) {
                if (isCompleted) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = "Completed",
                        tint = Color.White,
                        modifier = Modifier.size(18.dp)
                    )
                } else {
                    CopticCrossCanvas(size = 16.dp, color = GoldPrimary)
                }
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = prayerId.getDisplayName(lang),
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "• ${prayerId.copticTitle}",
                        style = MaterialTheme.typography.labelSmall,
                        color = GoldPrimary,
                        fontSize = 11.sp
                    )

                    if (isCurrentPrayer && !isCompleted) {
                        Spacer(modifier = Modifier.width(6.dp))
                        Surface(
                            shape = RoundedCornerShape(4.dp),
                            color = GoldPrimary.copy(alpha = 0.15f),
                            border = BorderStroke(1.dp, GoldPrimary.copy(alpha = pulseGlowAlpha))
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(horizontal = 5.dp, vertical = 1.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(6.dp)
                                        .clip(CircleShape)
                                        .background(GoldPrimary.copy(alpha = pulseGlowAlpha))
                                )
                                Spacer(modifier = Modifier.width(3.dp))
                                Text(
                                    text = if (isAr) "الآن ⏰" else "Now ⏰",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = GoldPrimary
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(2.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Outlined.AccessTime,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(12.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = String.format("%02d:00", prayerId.defaultHour),
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = prayerId.getSpiritualTheme(lang),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        fontSize = 11.sp
                    )
                }
            }

            Spacer(modifier = Modifier.width(8.dp))

            // Read Prayer Icon Button with subtle pulse if current hour
            Surface(
                shape = RoundedCornerShape(8.dp),
                color = if (isCurrentPrayer && !isCompleted) BurgundyDeep else BurgundyDeep.copy(alpha = 0.12f),
                border = if (isCurrentPrayer && !isCompleted) BorderStroke(1.dp, GoldPrimary.copy(alpha = pulseGlowAlpha)) else null,
                modifier = if (isCurrentPrayer && !isCompleted) {
                    Modifier
                        .graphicsLayer {
                            scaleX = pulseScale
                            scaleY = pulseScale
                        }
                        .clip(RoundedCornerShape(8.dp))
                        .clickable { onRead() }
                } else {
                    Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .clickable { onRead() }
                }
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.MenuBook,
                        contentDescription = "Read",
                        tint = if (isCurrentPrayer && !isCompleted) GoldLight else BurgundyDeep,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = if (isAr) "قراءة" else "Read",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isCurrentPrayer && !isCompleted) GoldLight else BurgundyDeep
                    )
                }
            }
        }
    }
}

/**
 * Current Week section displaying a 7-day column chart and weekly summary statistics.
 */
@Composable
private fun CurrentWeekProgressSection(
    weeklyProgress: HourlyPrayerWeeklyProgress,
    lang: AppLanguage
) {
    val isAr = lang == AppLanguage.ARABIC

    Column(modifier = Modifier.fillMaxWidth()) {
        // Weekly Statistics Summary Row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Stat 1: Total Prayers Completed
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = BurgundyDeep.copy(alpha = 0.08f),
                border = BorderStroke(1.dp, BurgundyDeep.copy(alpha = 0.2f)),
                modifier = Modifier.weight(1f)
            ) {
                Column(modifier = Modifier.padding(10.dp)) {
                    Text(
                        text = if (isAr) "صلوات الأسبوع" else "Weekly Prayers",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "${weeklyProgress.totalWeeklyCompleted} / ${weeklyProgress.totalWeeklyPossible}",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = BurgundyDeep
                    )
                }
            }

            // Stat 2: Completion Rate
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = GoldPrimary.copy(alpha = 0.10f),
                border = BorderStroke(1.dp, GoldPrimary.copy(alpha = 0.3f)),
                modifier = Modifier.weight(1f)
            ) {
                Column(modifier = Modifier.padding(10.dp)) {
                    Text(
                        text = if (isAr) "نسبة الإنجاز" else "Completion Rate",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "${(weeklyProgress.weeklyCompletionRate * 100).toInt()}%",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = GoldPrimary
                    )
                }
            }

            // Stat 3: Current Streak
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = PeacefulGreen.copy(alpha = 0.10f),
                border = BorderStroke(1.dp, PeacefulGreen.copy(alpha = 0.3f)),
                modifier = Modifier.weight(1f)
            ) {
                Column(modifier = Modifier.padding(10.dp)) {
                    Text(
                        text = if (isAr) "المواظبة (أيام)" else "Streak (Days)",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "🔥 ${weeklyProgress.currentStreakDays}",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = PeacefulGreen
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(18.dp))

        // 7-Day Visual Column Chart (Sunday to Saturday)
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = if (isAr) "المخطط اليومي لأيام الأسبوع (7 صلوات كحد أقصى)" else "Weekly 7-Day Chart (Max 7 Prayers/Day)",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = if (isAr) "الأسبوع #${weeklyProgress.weekOfYear}" else "Week #${weeklyProgress.weekOfYear}",
                        style = MaterialTheme.typography.labelSmall,
                        color = GoldPrimary,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Chart Columns Row
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(160.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Bottom
                ) {
                    weeklyProgress.days.forEach { daySummary ->
                        WeekDayBarColumn(
                            summary = daySummary,
                            isArabic = isAr,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
        }
    }
}

/**
 * Single Day Bar Column in the Weekly 7-Day Chart.
 */
@Composable
private fun WeekDayBarColumn(
    summary: HourlyPrayerDailySummary,
    isArabic: Boolean,
    modifier: Modifier = Modifier
) {
    val barFraction = summary.completionFraction
    val animatedHeightFraction by animateFloatAsState(
        targetValue = barFraction,
        animationSpec = tween(durationMillis = 280, easing = FastOutSlowInEasing),
        label = "bar_column_anim"
    )

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Bottom,
        modifier = modifier
            .fillMaxHeight()
            .padding(horizontal = 2.dp)
    ) {
        // Count Badge on top of the bar
        Text(
            text = "${summary.completedCount}",
            style = MaterialTheme.typography.labelSmall,
            fontWeight = if (summary.isToday) FontWeight.Bold else FontWeight.Medium,
            color = if (summary.isToday) GoldPrimary else MaterialTheme.colorScheme.onSurface,
            fontSize = 10.sp
        )

        Spacer(modifier = Modifier.height(4.dp))

        // Vertical Bar Container
        Box(
            modifier = Modifier
                .width(18.dp)
                .height(95.dp)
                .clip(RoundedCornerShape(6.dp))
                .background(MaterialTheme.colorScheme.surfaceVariant)
                .border(
                    width = if (summary.isToday) 1.5.dp else 0.dp,
                    color = if (summary.isToday) GoldPrimary else Color.Transparent,
                    shape = RoundedCornerShape(6.dp)
                ),
            contentAlignment = Alignment.BottomCenter
        ) {
            // Filled Portion
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(animatedHeightFraction.coerceAtLeast(0.04f))
                    .clip(RoundedCornerShape(6.dp))
                    .background(
                        when {
                            summary.completedCount >= 7 -> PeacefulGreen
                            summary.completedCount >= 4 -> GoldPrimary
                            summary.completedCount > 0 -> BurgundyDeep
                            else -> MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f)
                        }
                    )
            )
        }

        Spacer(modifier = Modifier.height(6.dp))

        // Day of month
        Text(
            text = "${summary.dayOfMonth}",
            style = MaterialTheme.typography.labelSmall,
            fontWeight = if (summary.isToday) FontWeight.Bold else FontWeight.Normal,
            color = if (summary.isToday) GoldPrimary else MaterialTheme.colorScheme.onSurfaceVariant,
            fontSize = 11.sp
        )

        // Day Name abbreviation
        Text(
            text = if (isArabic) {
                summary.dayNameAr.take(3)
            } else {
                summary.dayNameEn.take(3)
            },
            style = MaterialTheme.typography.labelSmall,
            fontWeight = if (summary.isToday) FontWeight.Bold else FontWeight.Normal,
            color = if (summary.isToday) BurgundyDeep else MaterialTheme.colorScheme.onSurfaceVariant,
            fontSize = 10.sp
        )

        // Today dot indicator
        if (summary.isToday) {
            Box(
                modifier = Modifier
                    .padding(top = 2.dp)
                    .size(4.dp)
                    .clip(CircleShape)
                    .background(GoldPrimary)
            )
        } else {
            Spacer(modifier = Modifier.height(6.dp))
        }
    }
}
