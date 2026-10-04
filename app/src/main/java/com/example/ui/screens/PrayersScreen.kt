package com.example.ui.screens

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.AccessTime
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.model.PrayerAlarmSetting
import com.example.data.model.PrayerId
import com.example.data.repository.AgpeyaRepository
import com.example.localization.AgpeyaStrings
import com.example.localization.AppLanguage
import com.example.ui.animation.pressBounce
import com.example.ui.components.CopticCrossCanvas
import com.example.ui.components.DailyArrowPrayersCard
import com.example.ui.components.DailyScriptureCard
import com.example.ui.components.IchthysDivider
import com.example.ui.components.PeaceDoveBadge
import com.example.ui.components.PrayerReadingModal
import com.example.ui.theme.BurgundyDeep
import com.example.ui.theme.BurgundyLight
import com.example.ui.theme.GoldLight
import com.example.ui.theme.GoldPrimary
import com.example.ui.theme.PeacefulGreen
import com.example.ui.viewmodel.AgpeyaViewModel

@Composable
fun PrayersScreen(
    viewModel: AgpeyaViewModel,
    onNavigateToCalendar: () -> Unit = {},
    onNavigateToAmbient: () -> Unit = {},
    onNavigateToJournal: () -> Unit = {},
    onNavigateToSettings: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val lang by viewModel.currentLanguage.collectAsState()
    val alarmSettings by viewModel.alarmSettings.collectAsState()
    val allLogs by viewModel.allLogs.collectAsState()
    val allArrowLogs by viewModel.allArrowLogs.collectAsState()
    val dailyArrowTarget by viewModel.dailyArrowTarget.collectAsState()
    val activeReadingPrayer by viewModel.activePrayerForReading.collectAsState()
    val spiritualTheme by viewModel.spiritualTheme.collectAsState()
    val spiritualOpacity by viewModel.spiritualOpacity.collectAsState()
    val isCandleGlowEnabled by viewModel.isCandleGlowEnabled.collectAsState()
    val isBilingual by viewModel.isBilingualEnabled.collectAsState()
    val secondaryLang by viewModel.secondaryLanguage.collectAsState()
    val fontSizeMultiplier by viewModel.fontSizeMultiplier.collectAsState()

    val todayString = AgpeyaRepository.getTodayString()
    val todayLogs = remember(allLogs, todayString) {
        allLogs.filter { it.dateString == todayString }
    }
    val todayArrowCount = remember(allArrowLogs, todayString) {
        allArrowLogs.filter { it.dateString == todayString }.sumOf { it.count }
    }
    val prayedCodesToday = remember(todayLogs) {
        todayLogs.map { it.prayerCode }.toSet()
    }

    val dailyVerse by viewModel.dailyVerse.collectAsState()

    val canonicalList = PrayerId.canonicalPrayers
    val totalCanonical = 7 // Prime, Terce, Sext, None, Vespers, Compline, Midnight
    val completedCanonicalCount = canonicalList.count { it != PrayerId.VEIL && prayedCodesToday.contains(it.code) }
    val progressFraction = (completedCanonicalCount.toFloat() / totalCanonical.toFloat()).coerceIn(0f, 1f)
    val animatedProgress by animateFloatAsState(
        targetValue = progressFraction,
        animationSpec = tween(durationMillis = 180, easing = FastOutSlowInEasing),
        label = "progress"
    )

    Box(modifier = modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 88.dp)
        ) {
            // Hero Banner
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(230.dp)
                ) {
                    Image(
                        painter = painterResource(id = spiritualTheme.drawableResId ?: R.drawable.agpeya_banner),
                        contentDescription = "Agpeya Banner",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )

                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                Brush.verticalGradient(
                                    colors = listOf(
                                        Color.Black.copy(alpha = 0.20f),
                                        Color.Black.copy(alpha = 0.70f)
                                    )
                                )
                            )
                    )

                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .statusBarsPadding()
                            .padding(20.dp),
                        verticalArrangement = Arrangement.Bottom
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.weight(1f)
                            ) {
                                CopticCrossCanvas(size = 40.dp)
                                Spacer(modifier = Modifier.width(14.dp))
                                Column {
                                    Text(
                                        text = AgpeyaStrings.appTitle(lang),
                                        style = MaterialTheme.typography.headlineSmall,
                                        fontWeight = FontWeight.Bold,
                                        color = GoldLight
                                    )
                                    Text(
                                        text = AgpeyaStrings.appSubtitle(lang),
                                        style = MaterialTheme.typography.bodySmall,
                                        color = Color(0xFFE2E8F0)
                                    )
                                }
                            }

                            IconButton(
                                onClick = onNavigateToSettings,
                                modifier = Modifier
                                    .clip(CircleShape)
                                    .background(Color.Black.copy(alpha = 0.45f))
                                    .testTag("top_bar_settings_btn")
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Settings,
                                    contentDescription = AgpeyaStrings.tabSettings(lang),
                                    tint = GoldPrimary
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Text(
                            text = AgpeyaStrings.verseSevenTimes(lang),
                            style = MaterialTheme.typography.labelSmall,
                            color = GoldLight.copy(alpha = 0.9f),
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }

            // Daily Orthodox Scripture Verse & Patristic Reflection
            item {
                DailyScriptureCard(
                    dailyVerse = dailyVerse,
                    language = lang,
                    onPrayerSelected = { prayerId ->
                        viewModel.setVerseForPrayer(prayerId)
                    },
                    onRefreshToday = {
                        viewModel.refreshDailyVerse()
                    },
                    onReadPrayer = { prayerId ->
                        viewModel.openPrayerReading(prayerId)
                    }
                )
            }

            // Daily Canonical Prayers Progress Tracker Card
            item {
                Card(
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                        .testTag("today_progress_card")
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = AgpeyaStrings.prayersTodayLabel(lang),
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    PeaceDoveBadge(
                                        text = AgpeyaStrings.peaceAndDiscipline(lang)
                                    )
                                }
                                Text(
                                    text = "$completedCanonicalCount / $totalCanonical ${AgpeyaStrings.completedPrayersTodaySuffix(lang)}",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }

                            // Percentage Badge
                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(GoldPrimary)
                                    .padding(horizontal = 12.dp, vertical = 6.dp)
                            ) {
                                Text(
                                    text = "${(animatedProgress * 100).toInt()}%",
                                    style = MaterialTheme.typography.labelLarge,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.Black
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        LinearProgressIndicator(
                            progress = { animatedProgress },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(8.dp)
                                .clip(RoundedCornerShape(4.dp)),
                            color = GoldPrimary,
                            trackColor = MaterialTheme.colorScheme.outlineVariant
                        )
                    }
                }
            }

            // Daily Arrow Prayers Target & Progress Ring Card
            item {
                DailyArrowPrayersCard(
                    todayArrowCount = todayArrowCount,
                    dailyTarget = dailyArrowTarget,
                    language = lang,
                    onSetDailyTarget = { newTarget ->
                        viewModel.setDailyArrowTarget(newTarget)
                    },
                    onQuickTap = {
                        viewModel.recordArrowPrayerTap()
                    },
                    onNavigateToAmbient = onNavigateToAmbient,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
                )
            }

            // Ichthys Fish Section Divider
            item {
                IchthysDivider(modifier = Modifier.padding(horizontal = 24.dp))
            }

            // Prayer Cards Header
            item {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = AgpeyaStrings.tabPrayers(lang),
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = GoldPrimary
                    )
                }
            }

            // Canonical Prayers List
            items(canonicalList, key = { it.code }) { prayerId ->
                val isPrayed = prayedCodesToday.contains(prayerId.code)
                val setting = alarmSettings[prayerId.code] ?: PrayerAlarmSetting(
                    prayerCode = prayerId.code,
                    hour = prayerId.defaultHour,
                    minute = prayerId.defaultMinute,
                    isEnabled = prayerId != PrayerId.VEIL
                )

                PrayerCardItem(
                    prayerId = prayerId,
                    lang = lang,
                    setting = setting,
                    isPrayedToday = isPrayed,
                    onTogglePrayed = { viewModel.togglePrayerToday(prayerId) },
                    onOpenReading = { viewModel.openPrayerReading(prayerId) }
                )
            }
        }
    }

    // Prayer Reading Modal
    activeReadingPrayer?.let { prayerId ->
        val isPrayed = prayedCodesToday.contains(prayerId.code)
        val todayLog = todayLogs.firstOrNull { it.prayerCode == prayerId.code }
        PrayerReadingModal(
            prayerId = prayerId,
            lang = lang,
            isPrayedToday = isPrayed,
            prayerLog = todayLog,
            spiritualTheme = spiritualTheme,
            spiritualOpacity = spiritualOpacity,
            isCandleGlowEnabled = isCandleGlowEnabled,
            isBilingual = isBilingual,
            secondaryLang = secondaryLang,
            fontSizeMultiplier = fontSizeMultiplier,
            onFontSizeMultiplierChange = { viewModel.setFontSizeMultiplier(it) },
            onLanguageChange = { viewModel.setLanguage(it) },
            onSpiritualThemeChange = { viewModel.setSpiritualTheme(it) },
            onAutoMarkPrayed = { viewModel.markPrayerAsPrayedToday(prayerId) },
            onTogglePrayed = { viewModel.togglePrayerToday(prayerId) },
            onDismiss = { viewModel.closePrayerReading() }
        )
    }
}

@Composable
private fun PrayerCardItem(
    prayerId: PrayerId,
    lang: AppLanguage,
    setting: PrayerAlarmSetting,
    isPrayedToday: Boolean,
    onTogglePrayed: () -> Unit,
    onOpenReading: () -> Unit
) {
    val cardBg by animateColorAsState(
        targetValue = if (isPrayedToday) {
            MaterialTheme.colorScheme.surfaceVariant
        } else {
            MaterialTheme.colorScheme.surface
        },
        animationSpec = tween(durationMillis = 150, easing = FastOutSlowInEasing),
        label = "cardBg"
    )

    Card(
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = cardBg),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        border = BorderStroke(
            1.dp,
            if (isPrayedToday) PeacefulGreen.copy(alpha = 0.4f) else GoldPrimary.copy(alpha = 0.25f)
        ),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp)
            .pressBounce()
            .clickable { onOpenReading() }
            .testTag("prayer_card_${prayerId.code.lowercase()}")
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Header Row: Cross, Names, Time Badge
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                CopticCrossCanvas(
                    size = 28.dp,
                    color = if (isPrayedToday) PeacefulGreen else GoldPrimary
                )

                Spacer(modifier = Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = prayerId.getDisplayName(lang),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = prayerId.copticTitle,
                        style = MaterialTheme.typography.bodySmall,
                        color = GoldPrimary,
                        fontSize = 12.sp
                    )
                }

                // Time Badge
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f))
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.AccessTime,
                            contentDescription = null,
                            tint = if (setting.isEnabled) GoldPrimary else MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        val formattedTime = String.format("%02d:%02d", setting.hour, setting.minute)
                        Text(
                            text = formattedTime,
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface,
                            fontSize = 12.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Spiritual Theme preview
            Text(
                text = prayerId.getSpiritualTheme(lang),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                lineHeight = 17.sp
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Clean Action Buttons (Read Prayer + Toggle Prayed Status)
            Row(
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Button(
                    onClick = onOpenReading,
                    colors = ButtonDefaults.buttonColors(containerColor = BurgundyDeep),
                    shape = RoundedCornerShape(10.dp),
                    contentPadding = PaddingValues(horizontal = 14.dp, vertical = 6.dp),
                    modifier = Modifier
                        .weight(1f)
                        .height(38.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.MenuBook,
                        contentDescription = null,
                        tint = GoldLight,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = AgpeyaStrings.readPrayer(lang),
                        color = GoldLight,
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp
                    )
                }

                // Completion status button
                if (isPrayedToday) {
                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = PeacefulGreen.copy(alpha = 0.18f),
                        border = BorderStroke(1.dp, PeacefulGreen.copy(alpha = 0.5f)),
                        modifier = Modifier
                            .height(38.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .clickable { onTogglePrayed() }
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 12.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = null,
                                tint = PeacefulGreen,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = if (lang == AppLanguage.ARABIC) "صليت اليوم ✓" else "Prayed ✓",
                                color = PeacefulGreen,
                                fontWeight = FontWeight.Bold,
                                fontSize = 11.5.sp
                            )
                        }
                    }
                } else {
                    OutlinedButton(
                        onClick = onTogglePrayed,
                        shape = RoundedCornerShape(10.dp),
                        border = BorderStroke(1.dp, GoldPrimary.copy(alpha = 0.5f)),
                        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 6.dp),
                        modifier = Modifier.height(38.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.CheckCircle,
                            contentDescription = null,
                            tint = GoldPrimary,
                            modifier = Modifier.size(15.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = if (lang == AppLanguage.ARABIC) "تسجيل الصلاة" else "Mark Done",
                            color = GoldPrimary,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 11.5.sp
                        )
                    }
                }
            }
        }
    }
}
