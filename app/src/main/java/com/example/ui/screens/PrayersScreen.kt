package com.example.ui.screens

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material.icons.filled.CloudDone
import androidx.compose.material.icons.filled.CloudSync
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.ViewInAr
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
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
import com.example.ui.components.SpiritualAtmosphereBackdrop
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
    val themeMode by viewModel.themeMode.collectAsState()
    val userEmail by viewModel.userEmail.collectAsState()
    val syncStatus by viewModel.syncStatus.collectAsState()

    val todayString = AgpeyaRepository.getTodayString()
    val isAr = lang == AppLanguage.ARABIC
    val currentHourPrayer = remember { PrayerId.getCurrentPrayerForTime() }
    val dualDateString = remember(todayString, isAr) {
        com.example.data.coptic.CopticCalendarHelper.getFormattedDualDate(isArabic = isAr)
    }

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
        // Spiritual holy backdrop for the entire screen so the sacred image shines through
        SpiritualAtmosphereBackdrop(
            theme = spiritualTheme,
            opacity = (spiritualOpacity * 1.5f).coerceIn(0.30f, 0.65f),
            enableCandleGlow = isCandleGlowEnabled,
            overlayColor = MaterialTheme.colorScheme.background
        )

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

                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    IconButton(
                                        onClick = { viewModel.openAuthOnboarding() },
                                        modifier = Modifier
                                            .clip(CircleShape)
                                            .background(Color.Black.copy(alpha = 0.45f))
                                            .testTag("top_bar_cloud_auth_btn")
                                    ) {
                                        Icon(
                                            imageVector = if (!userEmail.isNullOrBlank()) Icons.Default.CloudDone else Icons.Default.CloudSync,
                                            contentDescription = if (lang == AppLanguage.ARABIC) "التسجيل السحابي والمزامنة" else "Cloud Sync & Registration",
                                            tint = if (!userEmail.isNullOrBlank()) Color(0xFF4CAF50) else GoldPrimary
                                        )
                                    }

                                    Spacer(modifier = Modifier.width(8.dp))

                                    IconButton(
                                        onClick = { viewModel.toggleDarkLightMode() },
                                        modifier = Modifier
                                            .clip(CircleShape)
                                            .background(Color.Black.copy(alpha = 0.45f))
                                            .testTag("top_bar_theme_toggle_btn")
                                    ) {
                                        val isDarkActive = themeMode == com.example.ui.theme.AppThemeMode.DARK ||
                                            (themeMode == com.example.ui.theme.AppThemeMode.SYSTEM)
                                        Icon(
                                            imageVector = if (isDarkActive) Icons.Default.LightMode else Icons.Default.DarkMode,
                                            contentDescription = if (isDarkActive) "Switch to Light Mode" else "Switch to Dark Mode",
                                            tint = GoldPrimary
                                        )
                                    }

                                    Spacer(modifier = Modifier.width(8.dp))

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

            // Illuminated Coptic Header Banner
            item {
                com.example.ui.components.IlluminatedCopticHeaderBanner(
                    title = AgpeyaStrings.appTitle(lang),
                    subtitle = AgpeyaStrings.appSubtitle(lang),
                    copticDateText = dualDateString,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)
                )
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

            // Cloud Registration & Sync Status Banner
            item {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.92f)
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    border = BorderStroke(
                        width = 1.dp,
                        color = if (!userEmail.isNullOrBlank()) Color(0xFF4CAF50).copy(alpha = 0.5f) else GoldPrimary.copy(alpha = 0.4f)
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 6.dp)
                        .testTag("prayers_cloud_sync_banner")
                ) {
                    if (userEmail.isNullOrBlank()) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Box(
                                    contentAlignment = Alignment.Center,
                                    modifier = Modifier
                                        .size(42.dp)
                                        .clip(CircleShape)
                                        .background(GoldPrimary.copy(alpha = 0.18f))
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.CloudSync,
                                        contentDescription = null,
                                        tint = GoldPrimary,
                                        modifier = Modifier.size(24.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.width(12.dp))
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = if (lang == AppLanguage.ARABIC) "التسجيل السحابي ومزامنة الصلوات" else "Cloud Registration & Sync",
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                    Text(
                                        text = if (lang == AppLanguage.ARABIC) "سجّل بريدك لحفظ صلواتك ومزامنتها سحابياً عبر أجهزتك" else "Sign in with email to sync prayers across all your devices",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                                        lineHeight = 16.sp
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(12.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Button(
                                    onClick = { viewModel.openAuthOnboarding() },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = GoldPrimary,
                                        contentColor = Color.Black
                                    ),
                                    shape = RoundedCornerShape(12.dp),
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(44.dp)
                                        .testTag("btn_prayers_open_cloud_register")
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.CloudSync,
                                        contentDescription = null,
                                        modifier = Modifier.size(18.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = if (lang == AppLanguage.ARABIC) "التسجيل السحابي الآن" else "Cloud Register",
                                        fontWeight = FontWeight.Bold,
                                        style = MaterialTheme.typography.labelMedium
                                    )
                                }

                                OutlinedButton(
                                    onClick = onNavigateToSettings,
                                    shape = RoundedCornerShape(12.dp),
                                    modifier = Modifier.height(44.dp)
                                ) {
                                    Text(
                                        text = if (lang == AppLanguage.ARABIC) "الإعدادات" else "Settings",
                                        style = MaterialTheme.typography.labelMedium,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                        }
                    } else {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 12.dp)
                        ) {
                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier
                                    .size(38.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFF1E3A2F))
                            ) {
                                Icon(
                                    imageVector = Icons.Default.CloudDone,
                                    contentDescription = null,
                                    tint = Color(0xFF4CAF50),
                                    modifier = Modifier.size(22.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = if (lang == AppLanguage.ARABIC) "التسجيل السحابي مفعل" else "Cloud Synced",
                                        style = MaterialTheme.typography.titleSmall,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFF4CAF50)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = "• ${AgpeyaStrings.syncStatusSynced(lang)}",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                                Text(
                                    text = userEmail ?: "",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurface,
                                    fontWeight = FontWeight.Medium,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            OutlinedButton(
                                onClick = { viewModel.openAuthOnboarding() },
                                shape = RoundedCornerShape(10.dp),
                                contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                                modifier = Modifier.height(34.dp)
                            ) {
                                Text(
                                    text = if (lang == AppLanguage.ARABIC) "إدارة الحساب" else "Manage",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = GoldPrimary
                                )
                            }
                        }
                    }
                }
            }



            // Room Database: Hourly Prayers Daily & Weekly Progress Tracker
            item {
                com.example.ui.components.HourlyPrayersProgressTracker(
                    viewModel = viewModel,
                    onOpenPrayerReading = { prayerId -> viewModel.openPrayerReading(prayerId) },
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)
                )
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
                val isCurrent = prayerId == currentHourPrayer
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
                    isCurrentPrayer = isCurrent,
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
    isCurrentPrayer: Boolean = false,
    onTogglePrayed: () -> Unit,
    onOpenReading: () -> Unit
) {
    val cardBg by animateColorAsState(
        targetValue = if (isPrayedToday) {
            MaterialTheme.colorScheme.surfaceVariant
        } else {
            MaterialTheme.colorScheme.surface
        },
        animationSpec = tween(durationMillis = 180, easing = FastOutSlowInEasing),
        label = "cardBg"
    )

    val infiniteTransition = rememberInfiniteTransition(label = "current_prayer_pulse")
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 1.0f,
        targetValue = 1.045f,
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

    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = cardBg),
        elevation = CardDefaults.cardElevation(
            defaultElevation = if (isCurrentPrayer && !isPrayedToday) 5.dp else 3.dp,
            pressedElevation = 6.dp
        ),
        border = if (isCurrentPrayer && !isPrayedToday) {
            BorderStroke(1.5.dp, GoldPrimary.copy(alpha = pulseGlowAlpha))
        } else null,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp)
            .clickable { onOpenReading() }
            .testTag("prayer_card_${prayerId.code.lowercase()}")
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            // Header Row: Cross Badge, Prayer Titles, Prayer Time Badge, Current Hour Badge
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .size(38.dp)
                        .clip(CircleShape)
                        .background(
                            if (isPrayedToday) PeacefulGreen.copy(alpha = 0.12f) else BurgundyDeep.copy(alpha = 0.10f)
                        )
                ) {
                    CopticCrossCanvas(
                        size = 22.dp,
                        color = if (isPrayedToday) PeacefulGreen else BurgundyDeep
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = prayerId.getDisplayName(lang),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = prayerId.copticTitle,
                        style = MaterialTheme.typography.bodySmall,
                        color = GoldPrimary,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

                if (isCurrentPrayer) {
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = GoldPrimary.copy(alpha = 0.15f),
                        border = BorderStroke(1.dp, GoldPrimary.copy(alpha = pulseGlowAlpha))
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 5.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(7.dp)
                                    .clip(CircleShape)
                                    .background(GoldPrimary.copy(alpha = pulseGlowAlpha))
                            )
                            Spacer(modifier = Modifier.width(5.dp))
                            Text(
                                text = if (lang == AppLanguage.ARABIC) "الساعة الحالية ⏰" else "Current Hour ⏰",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = GoldPrimary,
                                fontSize = 11.sp
                            )
                        }
                    }
                    Spacer(modifier = Modifier.width(6.dp))
                }

                // Prayer Time Badge with balanced padding and unified shape
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.AccessTime,
                            contentDescription = null,
                            tint = if (setting.isEnabled) GoldPrimary else MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(5.dp))
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

            Spacer(modifier = Modifier.height(10.dp))

            // Spiritual Theme & Liturgical focus
            Text(
                text = prayerId.getSpiritualTheme(lang),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                lineHeight = 19.sp
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Balanced Action Buttons: "قراءة الصلاة والتأمل" & Prayer status toggle
            Row(
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Button(
                    onClick = onOpenReading,
                    colors = ButtonDefaults.buttonColors(containerColor = BurgundyDeep),
                    shape = RoundedCornerShape(12.dp),
                    contentPadding = PaddingValues(horizontal = 14.dp, vertical = 8.dp),
                    modifier = if (isCurrentPrayer) {
                        Modifier
                            .weight(1f)
                            .height(42.dp)
                            .graphicsLayer {
                                scaleX = pulseScale
                                scaleY = pulseScale
                            }
                            .border(
                                width = 1.5.dp,
                                color = GoldPrimary.copy(alpha = pulseGlowAlpha),
                                shape = RoundedCornerShape(12.dp)
                            )
                    } else {
                        Modifier
                            .weight(1f)
                            .height(42.dp)
                    }
                ) {
                    if (isCurrentPrayer) {
                        Box(
                            modifier = Modifier
                                .size(7.dp)
                                .clip(CircleShape)
                                .background(GoldLight.copy(alpha = pulseGlowAlpha))
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                    }
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
                        fontSize = 12.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                // Completion status button
                if (isPrayedToday) {
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = PeacefulGreen.copy(alpha = 0.15f),
                        modifier = Modifier
                            .height(42.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .clickable { onTogglePrayed() }
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 14.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = null,
                                tint = PeacefulGreen,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = if (lang == AppLanguage.ARABIC) "صليت اليوم ✓" else "Prayed ✓",
                                color = PeacefulGreen,
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp
                            )
                        }
                    }
                } else {
                    OutlinedButton(
                        onClick = onTogglePrayed,
                        shape = RoundedCornerShape(12.dp),
                        border = BorderStroke(1.dp, GoldPrimary),
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp),
                        modifier = Modifier.height(42.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.CheckCircle,
                            contentDescription = null,
                            tint = GoldPrimary,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(5.dp))
                        Text(
                            text = if (lang == AppLanguage.ARABIC) "تسجيل الصلاة" else "Mark Done",
                            color = GoldPrimary,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 12.sp
                        )
                    }
                }
            }
        }
    }
}
