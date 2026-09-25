package com.example.ui.screens

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.NotificationsOff
import androidx.compose.material.icons.filled.VolumeOff
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material.icons.outlined.AccessTime
import androidx.compose.material.icons.outlined.Vibration
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.audio.SpiritualSound
import com.example.data.model.PrayerAlarmSetting
import com.example.data.model.PrayerId
import com.example.data.repository.AgpeyaRepository
import com.example.localization.AgpeyaStrings
import com.example.localization.AppLanguage
import com.example.ui.components.CopticCrossCanvas
import com.example.ui.components.DailyScriptureCard
import com.example.ui.components.IchthysDivider
import com.example.ui.components.PeaceDoveBadge
import com.example.ui.components.PrayerReadingModal
import com.example.ui.components.SpiritualAtmosphereBackdrop
import com.example.ui.components.SpiritualSoundPickerDialog
import com.example.ui.components.SubtleCrossWatermark
import com.example.ui.components.TimePickerDialog
import com.example.ui.theme.BurgundyDeep
import com.example.ui.theme.BurgundyLight
import com.example.ui.theme.GoldContainerLight
import com.example.ui.theme.GoldLight
import com.example.ui.theme.GoldPrimary
import com.example.ui.theme.NavyMidnight
import com.example.ui.theme.PeacefulGreen
import com.example.ui.viewmodel.AgpeyaViewModel

@Composable
fun PrayersScreen(
    viewModel: AgpeyaViewModel,
    onNavigateToCalendar: () -> Unit = {},
    onNavigateToAmbient: () -> Unit = {},
    onNavigateToJournal: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val lang by viewModel.currentLanguage.collectAsState()
    val alarmSettings by viewModel.alarmSettings.collectAsState()
    val allLogs by viewModel.allLogs.collectAsState()
    val activeReadingPrayer by viewModel.activePrayerForReading.collectAsState()
    val spiritualTheme by viewModel.spiritualTheme.collectAsState()
    val spiritualOpacity by viewModel.spiritualOpacity.collectAsState()
    val isCandleGlowEnabled by viewModel.isCandleGlowEnabled.collectAsState()
    val isBilingual by viewModel.isBilingualEnabled.collectAsState()
    val secondaryLang by viewModel.secondaryLanguage.collectAsState()

    val todayString = AgpeyaRepository.getTodayString()
    val todayLogs = remember(allLogs, todayString) {
        allLogs.filter { it.dateString == todayString }
    }
    val prayedCodesToday = remember(todayLogs) {
        todayLogs.map { it.prayerCode }.toSet()
    }

    val dailyVerse by viewModel.dailyVerse.collectAsState()
    val playingSoundId by viewModel.playingSoundId.collectAsState()

    var prayerToEditTime by remember { mutableStateOf<PrayerId?>(null) }
    var prayerToEditSound by remember { mutableStateOf<PrayerId?>(null) }

    val canonicalList = PrayerId.canonicalPrayers
    val totalCanonical = 7 // Prime, Terce, Sext, None, Vespers, Compline, Midnight
    val completedCanonicalCount = canonicalList.count { it != PrayerId.VEIL && prayedCodesToday.contains(it.code) }
    val progressFraction = (completedCanonicalCount.toFloat() / totalCanonical.toFloat()).coerceIn(0f, 1f)
    val animatedProgress by animateFloatAsState(
        targetValue = progressFraction,
        animationSpec = tween(durationMillis = 250, easing = FastOutSlowInEasing),
        label = "progress"
    )

    Box(modifier = modifier.fillMaxSize()) {
        // Ambient Spiritual Reverence Backdrop for Whole Screen
        SpiritualAtmosphereBackdrop(
            theme = spiritualTheme,
            opacity = (spiritualOpacity * 0.40f).coerceAtMost(0.16f),
            enableCandleGlow = isCandleGlowEnabled
        )

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 80.dp)
        ) {
            // Hero Banner with Christian Motif
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                ) {
                    // Banner background image
                    Image(
                        painter = painterResource(id = spiritualTheme.drawableResId ?: R.drawable.agpeya_banner),
                        contentDescription = "Agpeya Banner",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )

                // Dark gradient overlay for text readability
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(
                                    Color.Black.copy(alpha = 0.35f),
                                    NavyMidnight.copy(alpha = 0.85f)
                                )
                            )
                        )
                )

                // Banner Content
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(20.dp),
                    verticalArrangement = Arrangement.Bottom
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
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

        // Quick Spiritual Shortcuts Row (Synaxarium, Ambient Candle, Spiritual Journal)
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 10.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Synaxarium & Calendar Card
                Card(
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                    border = androidx.compose.foundation.BorderStroke(1.dp, GoldPrimary.copy(alpha = 0.3f)),
                    modifier = Modifier
                        .weight(1f)
                        .clickable { onNavigateToCalendar() }
                ) {
                    Column(
                        modifier = Modifier.padding(10.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Default.DateRange,
                            contentDescription = null,
                            tint = GoldPrimary,
                            modifier = Modifier.size(22.dp)
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = if (lang == AppLanguage.ARABIC) "السنكسار والتقويم" else "Synaxarium",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface,
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                            maxLines = 1
                        )
                    }
                }

                // Ambient Candle Prayer Card
                Card(
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                    border = androidx.compose.foundation.BorderStroke(1.dp, GoldPrimary.copy(alpha = 0.3f)),
                    modifier = Modifier
                        .weight(1f)
                        .clickable { onNavigateToAmbient() }
                ) {
                    Column(
                        modifier = Modifier.padding(10.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Default.WbSunny,
                            contentDescription = null,
                            tint = Color(0xFFFFB300),
                            modifier = Modifier.size(22.dp)
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = if (lang == AppLanguage.ARABIC) "خلوة الشمعة" else "Candle Glow",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface,
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                            maxLines = 1
                        )
                    }
                }

                // Spiritual Journal & Confession Notes Card
                Card(
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                    border = androidx.compose.foundation.BorderStroke(1.dp, GoldPrimary.copy(alpha = 0.3f)),
                    modifier = Modifier
                        .weight(1f)
                        .clickable { onNavigateToJournal() }
                ) {
                    Column(
                        modifier = Modifier.padding(10.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = null,
                            tint = BurgundyDeep,
                            modifier = Modifier.size(22.dp)
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = if (lang == AppLanguage.ARABIC) "المفكرة والاعتراف" else "Journal Notes",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface,
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                            maxLines = 1
                        )
                    }
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

        // Daily Progress Tracker Card
        item {
            Card(
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp)
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
                                    text = if (lang == AppLanguage.ARABIC) "السلام والمواظبة" else "Peace & Discipline"
                                )
                            }
                            Text(
                                text = "$completedCanonicalCount / $totalCanonical " +
                                        if (lang == AppLanguage.ARABIC) "صلوات مكتملة اليوم" else "canonical prayers prayed",
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

        // Ichthys Fish Section Divider
        item {
            IchthysDivider(modifier = Modifier.padding(horizontal = 24.dp))
        }

        // Prayer Cards Header
        item {
            PaddingValues(horizontal = 16.dp, vertical = 4.dp).let {
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
                onToggleAlarm = { isEnabled ->
                    viewModel.updateAlarmSetting(setting.copy(isEnabled = isEnabled))
                },
                onToggleSound = {
                    viewModel.updateAlarmSetting(setting.copy(soundEnabled = !setting.soundEnabled))
                },
                onToggleVibrate = {
                    viewModel.updateAlarmSetting(setting.copy(vibrateEnabled = !setting.vibrateEnabled))
                },
                onEditTime = { prayerToEditTime = prayerId },
                onEditSound = { prayerToEditSound = prayerId },
                onOpenReading = { viewModel.openPrayerReading(prayerId) }
            )
        }
    }

    // Spiritual Sound Picker Dialog
    prayerToEditSound?.let { prayerId ->
        val currentSetting = alarmSettings[prayerId.code] ?: PrayerAlarmSetting(
            prayerCode = prayerId.code,
            hour = prayerId.defaultHour,
            minute = prayerId.defaultMinute
        )
        SpiritualSoundPickerDialog(
            currentSoundId = currentSetting.soundId,
            prayerId = prayerId,
            language = lang,
            playingSoundId = playingSoundId,
            onPlayPreview = { sound -> viewModel.playSoundPreview(sound) },
            onStopPreview = { viewModel.stopSoundPreview() },
            onSelectSound = { sound ->
                viewModel.setPrayerSound(prayerId, sound.id)
            },
            onDismiss = {
                viewModel.stopSoundPreview()
                prayerToEditSound = null
            }
        )
    }

    // Time Picker Dialog
    prayerToEditTime?.let { prayerId ->
        val currentSetting = alarmSettings[prayerId.code] ?: PrayerAlarmSetting(
            prayerCode = prayerId.code,
            hour = prayerId.defaultHour,
            minute = prayerId.defaultMinute
        )
        TimePickerDialog(
            initialHour = currentSetting.hour,
            initialMinute = currentSetting.minute,
            lang = lang,
            onDismiss = { prayerToEditTime = null },
            onConfirm = { hour, minute ->
                viewModel.updateAlarmSetting(
                    currentSetting.copy(hour = hour, minute = minute, isEnabled = true)
                )
                prayerToEditTime = null
            }
        )
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
    onToggleAlarm: (Boolean) -> Unit,
    onToggleSound: () -> Unit,
    onToggleVibrate: () -> Unit,
    onEditTime: () -> Unit,
    onEditSound: () -> Unit,
    onOpenReading: () -> Unit
) {
    val cardBg by animateColorAsState(
        targetValue = if (isPrayedToday) {
            MaterialTheme.colorScheme.surfaceVariant
        } else {
            MaterialTheme.colorScheme.surface
        },
        label = "cardBg"
    )

    Card(
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = cardBg),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp)
            .testTag("prayer_card_${prayerId.code.lowercase()}")
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Header Row: Cross, Names, Alarm switch
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
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = prayerId.getDisplayName(lang),
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                    Text(
                        text = prayerId.copticTitle,
                        style = MaterialTheme.typography.bodySmall,
                        color = GoldPrimary,
                        fontSize = 12.sp
                    )
                }

                // Time button
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(10.dp))
                        .background(MaterialTheme.colorScheme.surfaceVariant)
                        .clickable { onEditTime() }
                        .padding(horizontal = 10.dp, vertical = 6.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Outlined.AccessTime,
                            contentDescription = "Edit alarm time",
                            tint = if (setting.isEnabled) GoldPrimary else MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        val formattedTime = String.format("%02d:%02d", setting.hour, setting.minute)
                        Text(
                            text = formattedTime,
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold,
                            color = if (setting.isEnabled) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                Spacer(modifier = Modifier.width(8.dp))

                // Alarm Switch
                Switch(
                    checked = setting.isEnabled,
                    onCheckedChange = onToggleAlarm,
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = Color.Black,
                        checkedTrackColor = GoldPrimary,
                        uncheckedThumbColor = MaterialTheme.colorScheme.outline,
                        uncheckedTrackColor = MaterialTheme.colorScheme.surfaceVariant
                    ),
                    modifier = Modifier.testTag("alarm_switch_${prayerId.code.lowercase()}")
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Spiritual Theme preview
            Text(
                text = prayerId.getSpiritualTheme(lang),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                lineHeight = 18.sp
            )

            // Alarm controls (Sound toggle, Melody selector, Vibration toggle)
            if (setting.isEnabled) {
                Spacer(modifier = Modifier.height(10.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    // Sound toggle chip
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .clickable { onToggleSound() }
                            .background(if (setting.soundEnabled) GoldPrimary.copy(alpha = 0.2f) else MaterialTheme.colorScheme.surfaceVariant)
                            .padding(horizontal = 8.dp, vertical = 5.dp)
                    ) {
                        Icon(
                            imageVector = if (setting.soundEnabled) Icons.Default.VolumeUp else Icons.Default.VolumeOff,
                            contentDescription = "Sound toggle",
                            tint = if (setting.soundEnabled) GoldPrimary else MaterialTheme.colorScheme.outline,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = if (lang == AppLanguage.ARABIC) "صوت" else "Sound",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = if (setting.soundEnabled) FontWeight.Bold else FontWeight.Normal,
                            color = if (setting.soundEnabled) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    if (setting.soundEnabled) {
                        Spacer(modifier = Modifier.width(8.dp))
                        val currentSound = SpiritualSound.fromId(setting.soundId)
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(GoldPrimary.copy(alpha = 0.12f))
                                .clickable { onEditSound() }
                                .padding(horizontal = 8.dp, vertical = 5.dp)
                                .testTag("sound_chip_${prayerId.code.lowercase()}")
                        ) {
                            Icon(
                                imageVector = Icons.Default.MusicNote,
                                contentDescription = "Spiritual sound tone",
                                tint = GoldPrimary,
                                modifier = Modifier.size(15.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = currentSound.getName(lang),
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Medium,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Spacer(modifier = Modifier.width(2.dp))
                            Text(
                                text = "▾",
                                style = MaterialTheme.typography.labelSmall,
                                color = GoldPrimary
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    // Vibration toggle chip
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .clickable { onToggleVibrate() }
                            .background(if (setting.vibrateEnabled) GoldPrimary.copy(alpha = 0.2f) else MaterialTheme.colorScheme.surfaceVariant)
                            .padding(horizontal = 8.dp, vertical = 5.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Vibration,
                            contentDescription = "Vibration toggle",
                            tint = if (setting.vibrateEnabled) GoldPrimary else MaterialTheme.colorScheme.outline,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = if (lang == AppLanguage.ARABIC) "اهتزاز" else "Vibrate",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = if (setting.vibrateEnabled) FontWeight.Bold else FontWeight.Normal,
                            color = if (setting.vibrateEnabled) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Action Buttons: Read & Mark Prayed
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Read & Meditate
                FilledTonalButton(
                    onClick = onOpenReading,
                    shape = RoundedCornerShape(12.dp),
                    contentPadding = PaddingValues(horizontal = 14.dp, vertical = 8.dp),
                    colors = ButtonDefaults.filledTonalButtonColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant
                    ),
                    modifier = Modifier.testTag("read_btn_${prayerId.code.lowercase()}")
                ) {
                    Icon(
                        imageVector = Icons.Default.MenuBook,
                        contentDescription = null,
                        tint = GoldPrimary,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = AgpeyaStrings.readPrayer(lang),
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }

                // Mark as Prayed
                Button(
                    onClick = onTogglePrayed,
                    shape = RoundedCornerShape(12.dp),
                    contentPadding = PaddingValues(horizontal = 14.dp, vertical = 8.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isPrayedToday) PeacefulGreen else GoldPrimary
                    ),
                    modifier = Modifier.testTag("mark_btn_${prayerId.code.lowercase()}")
                ) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = null,
                        tint = if (isPrayedToday) Color.White else Color.Black,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = if (isPrayedToday) AgpeyaStrings.alreadyPrayed(lang) else AgpeyaStrings.markPrayed(lang),
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold,
                        color = if (isPrayedToday) Color.White else Color.Black
                    )
                }
            }
        }
    }
}
