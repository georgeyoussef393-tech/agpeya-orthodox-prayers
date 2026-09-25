package com.example.ui.components

import android.app.Activity
import android.view.WindowManager
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.animateScrollBy
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.FormatSize
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Translate
import androidx.compose.material.icons.filled.Undo
import androidx.compose.material.icons.filled.WbIncandescent
import androidx.compose.material.icons.outlined.WbIncandescent
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInParent
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.material.icons.filled.Wallpaper
import com.example.data.model.AgpeyaHourDetails
import com.example.data.model.PrayerId
import com.example.data.model.PrayerLogEntity
import com.example.data.model.PrayerSectionItem
import com.example.data.model.SpiritualBackgroundTheme
import com.example.localization.AgpeyaStrings
import com.example.localization.AppLanguage
import com.example.ui.theme.BurgundyDeep
import com.example.ui.theme.BurgundyPrimary
import com.example.ui.theme.GoldPrimary
import com.example.ui.theme.PeacefulGreen
import com.example.ui.theme.PrayerReadingTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun PrayerReadingModal(
    prayerId: PrayerId,
    lang: AppLanguage,
    isPrayedToday: Boolean,
    prayerLog: PrayerLogEntity? = null,
    spiritualTheme: SpiritualBackgroundTheme = SpiritualBackgroundTheme.CANDLE_SANCTUARY,
    spiritualOpacity: Float = 0.22f,
    isCandleGlowEnabled: Boolean = true,
    isBilingual: Boolean = false,
    secondaryLang: AppLanguage = AppLanguage.COPTIC,
    fontSizeMultiplier: Float = 1.0f,
    onFontSizeMultiplierChange: (Float) -> Unit = {},
    onLanguageChange: (AppLanguage) -> Unit = {},
    onSpiritualThemeChange: (SpiritualBackgroundTheme) -> Unit = {},
    onAutoMarkPrayed: () -> Unit,
    onTogglePrayed: () -> Unit,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    val scrollState = rememberScrollState()

    var fullSections by remember(prayerId, lang) {
        mutableStateOf(AgpeyaHourDetails.getFullPrayerSections(prayerId, lang))
    }
    var isRoomCached by remember { mutableStateOf(false) }

    LaunchedEffect(prayerId, lang) {
        val repo = com.example.data.repository.AgpeyaRepository(
            com.example.data.db.AgpeyaDatabase.getDatabase(context).prayerLogDao(),
            context
        )
        val cached = repo.getOrCachePrayerSections(prayerId, lang)
        if (cached.isNotEmpty()) {
            fullSections = cached
            isRoomCached = true
        }
    }

    // 1. Reader Personalization States
    var currentFontSizeMultiplier by remember(fontSizeMultiplier) { mutableFloatStateOf(fontSizeMultiplier) }
    var readingTheme by remember { mutableStateOf(PrayerReadingTheme.DEFAULT) }
    var currentSpiritualTheme by remember { mutableStateOf(spiritualTheme) }
    var isCandleModeActive by remember { mutableStateOf(isCandleGlowEnabled) }
    var keepScreenOn by remember { mutableStateOf(true) }
    var isAutoScrolling by remember { mutableStateOf(false) }
    var scrollSpeedMultiplier by remember { mutableFloatStateOf(1.0f) } // 0.5x, 1.0x, 1.5x, 2.0x, 3.0x
    var showThemeMenu by remember { mutableStateOf(false) }
    var showAtmosphereMenu by remember { mutableStateOf(false) }
    var showSpeedMenu by remember { mutableStateOf(false) }
    var showLanguageMenu by remember { mutableStateOf(false) }

    // Coordinates of Gospel section to trigger automatic recorded prayer
    var gospelOffsetY by remember { mutableStateOf<Float?>(null) }
    var hasAutoRecordedThisSession by remember { mutableStateOf(false) }

    // 2. Keep Screen Awake implementation
    DisposableEffect(keepScreenOn) {
        val window = (context as? Activity)?.window
        if (keepScreenOn && window != null) {
            window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        }
        onDispose {
            if (window != null) {
                window.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
            }
        }
    }

    // 3. Auto-Scroll Loop Engine (Fluid, responsive, smooth scrolling)
    LaunchedEffect(isAutoScrolling, scrollSpeedMultiplier) {
        if (isAutoScrolling) {
            val stepDuration = 80
            while (isActive && isAutoScrolling) {
                val currentScroll = scrollState.value
                val maxScroll = scrollState.maxValue
                if (currentScroll >= maxScroll) {
                    isAutoScrolling = false
                    break
                }
                val scrollDelta = 8f * scrollSpeedMultiplier
                scrollState.animateScrollBy(
                    value = scrollDelta,
                    animationSpec = tween(durationMillis = stepDuration, easing = LinearEasing)
                )
                delay(stepDuration.toLong())
            }
        }
    }

    // 4. Automatic Gospel recording check
    LaunchedEffect(scrollState.value, gospelOffsetY, isPrayedToday) {
        val y = gospelOffsetY
        if (!isPrayedToday && !hasAutoRecordedThisSession && y != null && y > 0f) {
            if (scrollState.value >= (y - 500f)) {
                hasAutoRecordedThisSession = true
                onAutoMarkPrayed()
            }
        }
    }

    // Dynamic Colors based on active theme
    val activeBgColor = if (readingTheme.backgroundColor != Color.Unspecified) {
        readingTheme.backgroundColor
    } else {
        MaterialTheme.colorScheme.background
    }

    val activeSurfaceColor = if (readingTheme.surfaceColor != Color.Unspecified) {
        readingTheme.surfaceColor
    } else {
        MaterialTheme.colorScheme.surfaceVariant
    }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 20.dp, bottom = 24.dp, start = 8.dp, end = 8.dp),
            shape = RoundedCornerShape(24.dp),
            color = activeBgColor,
            tonalElevation = 6.dp
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                // Top Header with Title & Quick Global Controls
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(activeSurfaceColor)
                        .padding(horizontal = 14.dp, vertical = 10.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        CopticCrossCanvas(size = 32.dp)
                        Spacer(modifier = Modifier.width(10.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = prayerId.getDisplayName(lang),
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = if (readingTheme.textColor != Color.Unspecified) readingTheme.textColor else MaterialTheme.colorScheme.onSurface
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Surface(
                                    shape = RoundedCornerShape(6.dp),
                                    color = readingTheme.accentColor.copy(alpha = 0.2f),
                                    modifier = Modifier.padding(horizontal = 4.dp)
                                ) {
                                    Text(
                                        text = if (lang == AppLanguage.ARABIC) "صلاة كاملة" else "Complete",
                                        style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                                        fontWeight = FontWeight.Bold,
                                        color = readingTheme.accentColor,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }
                                Surface(
                                    shape = RoundedCornerShape(6.dp),
                                    color = PeacefulGreen.copy(alpha = 0.18f),
                                    modifier = Modifier.padding(horizontal = 2.dp)
                                ) {
                                    Text(
                                        text = if (lang == AppLanguage.ARABIC) "✓ أوفلاين" else "✓ Offline",
                                        style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                                        fontWeight = FontWeight.Bold,
                                        color = PeacefulGreen,
                                        modifier = Modifier.padding(horizontal = 5.dp, vertical = 2.dp)
                                    )
                                }
                            }
                            Text(
                                text = "${prayerId.copticTitle} • ${fullSections.size} ${if (lang == AppLanguage.ARABIC) "أقسام ليتورجية" else "Liturgical Sections"}",
                                style = MaterialTheme.typography.bodySmall,
                                color = readingTheme.accentColor
                            )
                        }

                        // Close Dialog Button
                        IconButton(
                            onClick = onDismiss,
                            modifier = Modifier
                                .size(34.dp)
                                .clip(CircleShape)
                                .background(activeBgColor)
                                .testTag("close_prayer_reading_btn")
                        ) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Close",
                                tint = if (readingTheme.textColor != Color.Unspecified) readingTheme.textColor else MaterialTheme.colorScheme.onSurface,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                }

                // Dedicated Prayer Controls Toolbar (Auto-Scroll, Keep Screen Awake, Theme, Zoom)
                Surface(
                    color = activeSurfaceColor.copy(alpha = 0.85f),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 12.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        // 1. Auto-Scroll Control Group
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .clip(RoundedCornerShape(20.dp))
                                .background(
                                    if (isAutoScrolling) readingTheme.accentColor.copy(alpha = 0.25f)
                                    else activeBgColor
                                )
                                .padding(horizontal = 6.dp, vertical = 3.dp)
                        ) {
                            IconButton(
                                onClick = { isAutoScrolling = !isAutoScrolling },
                                modifier = Modifier.size(30.dp)
                            ) {
                                Icon(
                                    imageVector = if (isAutoScrolling) Icons.Default.Pause else Icons.Default.PlayArrow,
                                    contentDescription = "Toggle Auto-Scroll",
                                    tint = readingTheme.accentColor,
                                    modifier = Modifier.size(20.dp)
                                )
                            }

                            Text(
                                text = if (isAutoScrolling) {
                                    if (lang == AppLanguage.ARABIC) "تمرير تلقائي" else "Auto-Scroll"
                                } else {
                                    if (lang == AppLanguage.ARABIC) "تمرير" else "Scroll"
                                },
                                style = MaterialTheme.typography.labelSmall.copy(fontSize = 11.sp),
                                fontWeight = if (isAutoScrolling) FontWeight.Bold else FontWeight.Medium,
                                color = if (isAutoScrolling) readingTheme.accentColor else (if (readingTheme.textSecondaryColor != Color.Unspecified) readingTheme.textSecondaryColor else MaterialTheme.colorScheme.onSurfaceVariant),
                                modifier = Modifier
                                    .clickable { isAutoScrolling = !isAutoScrolling }
                                    .padding(end = 4.dp)
                            )

                            // Speed Selector
                            Box {
                                Surface(
                                    shape = RoundedCornerShape(12.dp),
                                    color = readingTheme.accentColor.copy(alpha = 0.15f),
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(12.dp))
                                        .clickable { showSpeedMenu = true }
                                        .padding(horizontal = 6.dp, vertical = 2.dp)
                                ) {
                                    Text(
                                        text = "${scrollSpeedMultiplier}x",
                                        style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.5.sp),
                                        fontWeight = FontWeight.Bold,
                                        color = readingTheme.accentColor
                                    )
                                }

                                DropdownMenu(
                                    expanded = showSpeedMenu,
                                    onDismissRequest = { showSpeedMenu = false }
                                ) {
                                    listOf(0.5f, 1.0f, 1.5f, 2.0f, 3.0f).forEach { speed ->
                                        DropdownMenuItem(
                                            text = {
                                                Text(
                                                    text = when (speed) {
                                                        0.5f -> if (lang == AppLanguage.ARABIC) "٠.٥x (هادئ جداً)" else "0.5x (Very Slow)"
                                                        1.0f -> if (lang == AppLanguage.ARABIC) "١.٠x (عادي)" else "1.0x (Normal)"
                                                        1.5f -> if (lang == AppLanguage.ARABIC) "١.٥x (متوسط)" else "1.5x (Medium)"
                                                        2.0f -> if (lang == AppLanguage.ARABIC) "٢.٠x (سريع)" else "2.0x (Fast)"
                                                        else -> if (lang == AppLanguage.ARABIC) "٣.٠x (سريع جداً)" else "3.0x (Very Fast)"
                                                    },
                                                    fontWeight = if (scrollSpeedMultiplier == speed) FontWeight.Bold else FontWeight.Normal
                                                )
                                            },
                                            onClick = {
                                                scrollSpeedMultiplier = speed
                                                showSpeedMenu = false
                                            }
                                        )
                                    }
                                }
                            }
                        }

                        // Right Group: Candle Sanctuary Toggle + Keep Screen On + Theme Picker + Font Size
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            // Candle Sanctuary Mode Toggle
                            IconButton(
                                onClick = {
                                    isCandleModeActive = !isCandleModeActive
                                },
                                modifier = Modifier
                                    .size(32.dp)
                                    .clip(CircleShape)
                                    .background(
                                        if (isCandleModeActive) GoldPrimary.copy(alpha = 0.28f)
                                        else activeBgColor
                                    )
                                    .testTag("candle_sanctuary_toggle_btn")
                            ) {
                                Icon(
                                    imageVector = Icons.Default.AutoAwesome,
                                    contentDescription = "Candle Sanctuary Mode",
                                    tint = if (isCandleModeActive) GoldPrimary else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
                                    modifier = Modifier.size(17.dp)
                                )
                            }

                            Spacer(modifier = Modifier.width(6.dp))

                            // 2. Keep Screen Awake Button
                            IconButton(
                                onClick = { keepScreenOn = !keepScreenOn },
                                modifier = Modifier
                                    .size(32.dp)
                                    .clip(CircleShape)
                                    .background(
                                        if (keepScreenOn) readingTheme.accentColor.copy(alpha = 0.2f)
                                        else activeBgColor
                                    )
                            ) {
                                Icon(
                                    imageVector = if (keepScreenOn) Icons.Filled.WbIncandescent else Icons.Outlined.WbIncandescent,
                                    contentDescription = "Keep Screen On",
                                    tint = if (keepScreenOn) readingTheme.accentColor else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
                                    modifier = Modifier.size(17.dp)
                                )
                            }

                            Spacer(modifier = Modifier.width(6.dp))

                            // 3. Spiritual Atmosphere & Background Picker
                            Box {
                                IconButton(
                                    onClick = { showAtmosphereMenu = true },
                                    modifier = Modifier
                                        .size(32.dp)
                                        .clip(CircleShape)
                                        .background(
                                            if (currentSpiritualTheme.drawableResId != null) readingTheme.accentColor.copy(alpha = 0.2f)
                                            else activeBgColor
                                        )
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Wallpaper,
                                        contentDescription = "Spiritual Atmosphere",
                                        tint = readingTheme.accentColor,
                                        modifier = Modifier.size(17.dp)
                                    )
                                }

                                DropdownMenu(
                                    expanded = showAtmosphereMenu,
                                    onDismissRequest = { showAtmosphereMenu = false }
                                ) {
                                    SpiritualBackgroundTheme.entries.forEach { theme ->
                                        DropdownMenuItem(
                                            text = {
                                                Row(verticalAlignment = Alignment.CenterVertically) {
                                                    Text(
                                                        text = if (lang == AppLanguage.ARABIC) theme.titleAr else theme.titleEn,
                                                        fontWeight = if (currentSpiritualTheme == theme) FontWeight.Bold else FontWeight.Normal,
                                                        color = if (currentSpiritualTheme == theme) readingTheme.accentColor else MaterialTheme.colorScheme.onSurface
                                                    )
                                                }
                                            },
                                            onClick = {
                                                currentSpiritualTheme = theme
                                                onSpiritualThemeChange(theme)
                                                showAtmosphereMenu = false
                                            }
                                        )
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.width(6.dp))

                            // 4. Reading Color Theme Selector
                            Box {
                                IconButton(
                                    onClick = { showThemeMenu = true },
                                    modifier = Modifier
                                        .size(32.dp)
                                        .clip(CircleShape)
                                        .background(activeBgColor)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Palette,
                                        contentDescription = "Reading Theme",
                                        tint = readingTheme.accentColor,
                                        modifier = Modifier.size(17.dp)
                                    )
                                }

                                DropdownMenu(
                                    expanded = showThemeMenu,
                                    onDismissRequest = { showThemeMenu = false }
                                ) {
                                    PrayerReadingTheme.values().forEach { theme ->
                                        DropdownMenuItem(
                                            text = {
                                                Row(verticalAlignment = Alignment.CenterVertically) {
                                                    Box(
                                                        modifier = Modifier
                                                            .size(14.dp)
                                                            .clip(CircleShape)
                                                            .background(
                                                                if (theme.backgroundColor != Color.Unspecified) theme.backgroundColor
                                                                else Color.Gray
                                                            )
                                                            .border(1.dp, theme.accentColor, CircleShape)
                                                    )
                                                    Spacer(modifier = Modifier.width(8.dp))
                                                    Text(
                                                        text = if (lang == AppLanguage.ARABIC) theme.titleAr else theme.titleEn,
                                                        fontWeight = if (readingTheme == theme) FontWeight.Bold else FontWeight.Normal
                                                    )
                                                }
                                            },
                                            onClick = {
                                                readingTheme = theme
                                                showThemeMenu = false
                                            }
                                        )
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.width(6.dp))

                            // 5. Font Size Zoom Toggle
                            IconButton(
                                onClick = {
                                    val nextMultiplier = when {
                                        currentFontSizeMultiplier < 1.15f -> 1.30f
                                        currentFontSizeMultiplier < 1.45f -> 1.60f
                                        else -> 1.0f
                                    }
                                    currentFontSizeMultiplier = nextMultiplier
                                    onFontSizeMultiplierChange(nextMultiplier)
                                },
                                modifier = Modifier
                                    .size(32.dp)
                                    .clip(CircleShape)
                                    .background(activeBgColor)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.FormatSize,
                                    contentDescription = "Font size",
                                    tint = readingTheme.accentColor,
                                    modifier = Modifier.size(17.dp)
                                )
                            }

                            Spacer(modifier = Modifier.width(6.dp))

                            // 6. Language Quick Switcher
                            Box {
                                IconButton(
                                    onClick = { showLanguageMenu = true },
                                    modifier = Modifier
                                        .size(32.dp)
                                        .clip(CircleShape)
                                        .background(activeBgColor)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Translate,
                                        contentDescription = "Language",
                                        tint = readingTheme.accentColor,
                                        modifier = Modifier.size(17.dp)
                                    )
                                }

                                DropdownMenu(
                                    expanded = showLanguageMenu,
                                    onDismissRequest = { showLanguageMenu = false }
                                ) {
                                    AppLanguage.entries.forEach { appLang ->
                                        DropdownMenuItem(
                                            text = {
                                                Row(verticalAlignment = Alignment.CenterVertically) {
                                                    Text(
                                                        text = "${appLang.flagEmoji}  ${appLang.nativeName}",
                                                        fontWeight = if (lang == appLang) FontWeight.Bold else FontWeight.Normal,
                                                        color = if (lang == appLang) readingTheme.accentColor else MaterialTheme.colorScheme.onSurface
                                                    )
                                                    if (lang == appLang) {
                                                        Spacer(modifier = Modifier.width(8.dp))
                                                        Icon(
                                                            imageVector = Icons.Default.Check,
                                                            contentDescription = "Selected",
                                                            tint = readingTheme.accentColor,
                                                            modifier = Modifier.size(16.dp)
                                                        )
                                                    }
                                                }
                                            },
                                            onClick = {
                                                onLanguageChange(appLang)
                                                showLanguageMenu = false
                                            }
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                HorizontalDivider(color = readingTheme.borderColor.copy(alpha = 0.3f))

                // Quick Navigation Chips for Sections
                LazyRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(activeBgColor)
                        .padding(horizontal = 12.dp, vertical = 6.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    itemsIndexed(fullSections) { index, section ->
                        val chipBg = if (section.isGospel) {
                            readingTheme.accentColor.copy(alpha = 0.22f)
                        } else {
                            activeSurfaceColor
                        }
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = chipBg,
                            border = if (section.isGospel) androidx.compose.foundation.BorderStroke(1.dp, readingTheme.accentColor) else null,
                            modifier = Modifier.clip(RoundedCornerShape(8.dp))
                        ) {
                            Text(
                                text = "${index + 1}. ${section.getTitle(lang).take(18)}..",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = if (section.isGospel) FontWeight.Bold else FontWeight.Medium,
                                color = if (section.isGospel) readingTheme.accentColor else (if (readingTheme.textSecondaryColor != Color.Unspecified) readingTheme.textSecondaryColor else MaterialTheme.colorScheme.onSurfaceVariant),
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }
                }

                HorizontalDivider(color = readingTheme.borderColor.copy(alpha = 0.2f))

                // Scrollable Prayer Content (All Complete Liturgical Sections) framed by Spiritual Atmosphere
                Box(modifier = Modifier.weight(1f)) {
                    // Sacred Reverence Background Backdrop
                    SpiritualAtmosphereBackdrop(
                        theme = currentSpiritualTheme,
                        opacity = spiritualOpacity,
                        enableCandleGlow = isCandleModeActive,
                        overlayColor = activeBgColor
                    )

                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(scrollState)
                            .padding(horizontal = 16.dp, vertical = 14.dp)
                    ) {
                        // Spiritual Commemoration Header Card
                    Card(
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = if (readingTheme.surfaceColor != Color.Unspecified) {
                                readingTheme.surfaceColor.copy(alpha = 0.9f)
                            } else {
                                MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.45f)
                            }
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(1.dp, readingTheme.borderColor.copy(alpha = 0.5f), RoundedCornerShape(16.dp))
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Text(
                                text = "✝ " + prayerId.getSpiritualTheme(lang),
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    fontSize = 14.sp * currentFontSizeMultiplier,
                                    lineHeight = 22.sp * currentFontSizeMultiplier
                                ),
                                fontWeight = FontWeight.Medium,
                                color = if (readingTheme.textColor != Color.Unspecified) readingTheme.textColor else MaterialTheme.colorScheme.onPrimaryContainer
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = prayerId.getKeyVerse(lang),
                                style = MaterialTheme.typography.bodySmall.copy(
                                    fontSize = 12.5.sp * currentFontSizeMultiplier
                                ),
                                color = readingTheme.accentColor,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Iterate over each complete liturgical section
                    fullSections.forEachIndexed { index, section ->
                        if (section.isGospel) {
                            // Gospel Section with position measurement for automatic prayer recording
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .onGloballyPositioned { coordinates ->
                                        gospelOffsetY = coordinates.positionInParent().y
                                    }
                            ) {
                                DetailedGospelBlock(
                                    section = section,
                                    lang = lang,
                                    multiplier = currentFontSizeMultiplier,
                                    theme = readingTheme,
                                    isBilingual = isBilingual,
                                    secondaryLang = secondaryLang
                                )
                            }

                            Spacer(modifier = Modifier.height(14.dp))

                            // Interactive Gospel Completion & Automatic Prayer Log Card
                            GospelCompletionTrackerCard(
                                isPrayedToday = isPrayedToday,
                                prayerLog = prayerLog,
                                lang = lang,
                                onAutoMarkPrayed = onAutoMarkPrayed,
                                onTogglePrayed = onTogglePrayed,
                                theme = readingTheme
                            )
                        } else {
                            DetailedPrayerSectionCard(
                                section = section,
                                lang = lang,
                                stepNumber = index + 1,
                                multiplier = currentFontSizeMultiplier,
                                theme = readingTheme,
                                isBilingual = isBilingual,
                                secondaryLang = secondaryLang
                            )
                        }

                        Spacer(modifier = Modifier.height(14.dp))
                    }

                    Spacer(modifier = Modifier.height(20.dp))
                }
                }

                // Audio Recitation & Chants Player Bar
                PrayerAudioPlayerBar(
                    prayerId = prayerId,
                    lang = lang,
                    theme = readingTheme,
                    scrollState = scrollState
                )

                // Bottom Action Bar
                Surface(
                    tonalElevation = 10.dp,
                    color = activeSurfaceColor,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Box(
                        modifier = Modifier
                            .navigationBarsPadding()
                            .padding(start = 16.dp, end = 16.dp, top = 12.dp, bottom = 32.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Button(
                                onClick = onTogglePrayed,
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = if (isPrayedToday) PeacefulGreen else readingTheme.accentColor
                                ),
                                shape = RoundedCornerShape(16.dp),
                                modifier = Modifier
                                    .weight(1f)
                                    .height(52.dp)
                                    .testTag("bottom_toggle_prayed_btn")
                            ) {
                                Icon(
                                    imageVector = if (isPrayedToday) Icons.Default.CheckCircle else Icons.Default.Check,
                                    contentDescription = null,
                                    tint = if (isPrayedToday) Color.White else Color.Black
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = if (isPrayedToday) {
                                        AgpeyaStrings.prayerRecordedSuccess(lang)
                                    } else {
                                        AgpeyaStrings.markPrayed(lang)
                                    },
                                    color = if (isPrayedToday) Color.White else Color.Black,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 15.sp
                                )
                            }

                            Spacer(modifier = Modifier.width(12.dp))

                            OutlinedButton(
                                onClick = onDismiss,
                                shape = RoundedCornerShape(16.dp),
                                modifier = Modifier.height(52.dp)
                            ) {
                                Text(
                                    text = if (lang == AppLanguage.ARABIC) "إغلاق" else "Close",
                                    style = MaterialTheme.typography.labelLarge,
                                    fontWeight = FontWeight.SemiBold,
                                    color = if (readingTheme.textColor != Color.Unspecified) readingTheme.textColor else MaterialTheme.colorScheme.onSurface
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
 * Liturgical Section Card supporting rubrics, subtitles, Coptic references, and custom reading themes.
 */
@Composable
private fun DetailedPrayerSectionCard(
    section: PrayerSectionItem,
    lang: AppLanguage,
    stepNumber: Int,
    multiplier: Float,
    theme: PrayerReadingTheme,
    isBilingual: Boolean = false,
    secondaryLang: AppLanguage = AppLanguage.COPTIC
) {
    val cardColor = if (theme.surfaceColor != Color.Unspecified) {
        theme.surfaceColor
    } else {
        MaterialTheme.colorScheme.surface
    }

    val textColor = if (theme.textColor != Color.Unspecified) {
        theme.textColor
    } else {
        MaterialTheme.colorScheme.onSurface
    }

    val subTextColor = if (theme.textSecondaryColor != Color.Unspecified) {
        theme.textSecondaryColor
    } else {
        MaterialTheme.colorScheme.onSurfaceVariant
    }

    Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = cardColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, theme.borderColor.copy(alpha = 0.25f), RoundedCornerShape(14.dp))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Section Title and Step Badge
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Surface(
                    shape = CircleShape,
                    color = theme.accentColor.copy(alpha = 0.15f),
                    modifier = Modifier.size(26.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text(
                            text = "$stepNumber",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = theme.accentColor
                        )
                    }
                }
                Spacer(modifier = Modifier.width(10.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = section.getTitle(lang),
                        style = MaterialTheme.typography.titleSmall.copy(
                            fontSize = 15.sp * multiplier
                        ),
                        fontWeight = FontWeight.Bold,
                        color = theme.accentColor
                    )
                    section.getSubtitle(lang)?.let { sub ->
                        Text(
                            text = sub,
                            style = MaterialTheme.typography.bodySmall.copy(fontSize = 12.sp),
                            color = subTextColor
                        )
                    }
                }
                section.copticIntro?.let { coptic ->
                    Text(
                        text = coptic,
                        style = MaterialTheme.typography.bodySmall,
                        color = theme.rubricColor,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            // Rubric / Direction
            section.getRubric(lang)?.let { rubric ->
                Spacer(modifier = Modifier.height(8.dp))
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = theme.rubricBgColor,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "❖ $rubric",
                        style = MaterialTheme.typography.bodySmall.copy(
                            fontSize = 11.5.sp,
                            fontStyle = FontStyle.Italic
                        ),
                        color = theme.rubricColor,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Text Content with dynamic readable sizing
            Text(
                text = section.getContent(lang),
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontSize = 15.sp * multiplier,
                    lineHeight = 25.sp * multiplier
                ),
                color = textColor
            )

            // Parallel Bilingual Text display when enabled
            if (isBilingual && secondaryLang != lang) {
                Spacer(modifier = Modifier.height(12.dp))
                HorizontalDivider(color = theme.accentColor.copy(alpha = 0.25f))
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "${secondaryLang.flagEmoji}  ${secondaryLang.nativeName}",
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Bold,
                    color = theme.accentColor
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = section.getContent(secondaryLang),
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontSize = 14.sp * multiplier,
                        lineHeight = 23.sp * multiplier
                    ),
                    color = textColor.copy(alpha = 0.88f)
                )
            }
        }
    }
}

/**
 * Rich Holy Gospel Display Card supporting themes.
 */
@Composable
private fun DetailedGospelBlock(
    section: PrayerSectionItem,
    lang: AppLanguage,
    multiplier: Float,
    theme: PrayerReadingTheme,
    isBilingual: Boolean = false,
    secondaryLang: AppLanguage = AppLanguage.COPTIC
) {
    val cardColor = if (theme.surfaceColor != Color.Unspecified) {
        theme.surfaceColor
    } else {
        MaterialTheme.colorScheme.surface
    }

    val textColor = if (theme.textColor != Color.Unspecified) {
        theme.textColor
    } else {
        MaterialTheme.colorScheme.onSurface
    }

    Card(
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = cardColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
        modifier = Modifier
            .fillMaxWidth()
            .border(1.5.dp, theme.accentColor.copy(alpha = 0.6f), RoundedCornerShape(18.dp))
            .testTag("holy_gospel_card")
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            // Header
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(theme.accentColor.copy(alpha = 0.18f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.MenuBook,
                        contentDescription = null,
                        tint = theme.accentColor,
                        modifier = Modifier.size(20.dp)
                    )
                }
                Spacer(modifier = Modifier.width(10.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = section.getTitle(lang),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = theme.accentColor
                    )
                    section.getSubtitle(lang)?.let { sub ->
                        Text(
                            text = sub,
                            style = MaterialTheme.typography.bodySmall,
                            color = if (theme.textSecondaryColor != Color.Unspecified) theme.textSecondaryColor else MaterialTheme.colorScheme.onSurfaceVariant,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Evangelist Litany Banner
            Surface(
                shape = RoundedCornerShape(10.dp),
                color = theme.rubricBgColor,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(10.dp)) {
                    Text(
                        text = if (lang == AppLanguage.ARABIC) "✝ قفوا بخوف أمام الله لسماع الإنجيل المقدس" else "✝ Stand in the fear of God, let us hear the Holy Gospel",
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.Bold,
                        color = theme.rubricColor
                    )
                }
            }

            HorizontalDivider(
                color = theme.borderColor.copy(alpha = 0.4f),
                modifier = Modifier.padding(vertical = 12.dp)
            )

            // Scripture Text
            Text(
                text = section.getContent(lang),
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontSize = 16.5.sp * multiplier,
                    lineHeight = 28.sp * multiplier
                ),
                color = textColor
            )

            // Parallel Bilingual Text display when enabled
            if (isBilingual && secondaryLang != lang) {
                Spacer(modifier = Modifier.height(14.dp))
                HorizontalDivider(color = theme.accentColor.copy(alpha = 0.35f))
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "${secondaryLang.flagEmoji}  ${secondaryLang.nativeName}",
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Bold,
                    color = theme.accentColor
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = section.getContent(secondaryLang),
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontSize = 15.sp * multiplier,
                        lineHeight = 25.sp * multiplier
                    ),
                    color = textColor.copy(alpha = 0.9f)
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Doxology
            Text(
                text = if (lang == AppLanguage.ARABIC) "والمجد لله دائماً أبدياً، آمين." else "Glory to God forever. Amen.",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold,
                color = theme.accentColor,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .background(theme.accentColor.copy(alpha = 0.12f))
                    .padding(vertical = 6.dp)
            )
        }
    }
}

/**
 * Interactive card situated immediately at the end of the Holy Gospel.
 */
@Composable
private fun GospelCompletionTrackerCard(
    isPrayedToday: Boolean,
    prayerLog: PrayerLogEntity?,
    lang: AppLanguage,
    onAutoMarkPrayed: () -> Unit,
    onTogglePrayed: () -> Unit,
    theme: PrayerReadingTheme
) {
    val formattedDateTime = formatLogDateTime(prayerLog?.timestamp ?: System.currentTimeMillis(), lang)

    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isPrayedToday) {
                PeacefulGreen.copy(alpha = 0.12f)
            } else {
                theme.accentColor.copy(alpha = 0.12f)
            }
        ),
        modifier = Modifier
            .fillMaxWidth()
            .border(
                width = 1.5.dp,
                color = if (isPrayedToday) PeacefulGreen.copy(alpha = 0.6f) else theme.accentColor.copy(alpha = 0.5f),
                shape = RoundedCornerShape(16.dp)
            )
            .testTag("gospel_completion_card")
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    imageVector = if (isPrayedToday) Icons.Default.CheckCircle else Icons.Default.AutoAwesome,
                    contentDescription = null,
                    tint = if (isPrayedToday) PeacefulGreen else theme.accentColor,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = if (lang == AppLanguage.ARABIC) {
                        "إتمام قراءة الإنجيل المقدس وتسجيل الصلاة"
                    } else {
                        "Gospel Reading & Automatic Prayer Record"
                    },
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = if (isPrayedToday) PeacefulGreen else theme.accentColor
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            if (isPrayedToday) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(10.dp))
                            .background(PeacefulGreen.copy(alpha = 0.18f))
                            .padding(horizontal = 12.dp, vertical = 10.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = "Recorded",
                            tint = PeacefulGreen,
                            modifier = Modifier.size(22.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = AgpeyaStrings.prayerRecordedSuccess(lang),
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Bold,
                                color = PeacefulGreen
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = if (lang == AppLanguage.ARABIC) {
                                    "وقت التسجيل الفعلي: $formattedDateTime"
                                } else {
                                    "Recorded at: $formattedDateTime"
                                },
                                style = MaterialTheme.typography.bodySmall.copy(fontSize = 12.sp),
                                fontWeight = FontWeight.Medium,
                                color = if (theme.textColor != Color.Unspecified) theme.textColor else MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedButton(
                        onClick = onTogglePrayed,
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier
                            .align(Alignment.End)
                            .testTag("unmark_prayer_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Undo,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = AgpeyaStrings.unmarkPrayerPrompt(lang),
                            style = MaterialTheme.typography.labelSmall
                        )
                    }
                }
            } else {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = AgpeyaStrings.autoRecordedNotice(lang),
                        style = MaterialTheme.typography.bodySmall,
                        color = if (theme.textSecondaryColor != Color.Unspecified) theme.textSecondaryColor else MaterialTheme.colorScheme.onSurfaceVariant,
                        lineHeight = 18.sp
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Button(
                        onClick = onAutoMarkPrayed,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = theme.accentColor
                        ),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("record_after_gospel_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = null,
                            tint = Color.Black
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (lang == AppLanguage.ARABIC) {
                                "✓ قراءة الإنجيل تمت (تأكيد تسجيل الصلاة الآن)"
                            } else {
                                "✓ Gospel Read (Confirm Prayer Log Now)"
                            },
                            color = Color.Black,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.5.sp
                        )
                    }
                }
            }
        }
    }
}

private fun formatLogDateTime(timestamp: Long, lang: AppLanguage): String {
    val date = Date(timestamp)
    val locale = when (lang) {
        AppLanguage.ARABIC, AppLanguage.SYRIAN_ARABIC, AppLanguage.SYRIAC -> Locale("ar")
        AppLanguage.FRENCH -> Locale.FRENCH
        AppLanguage.SPANISH -> Locale("es")
        AppLanguage.GERMAN, AppLanguage.AUSTRIAN_GERMAN, AppLanguage.SWISS_GERMAN -> Locale.GERMAN
        AppLanguage.ITALIAN -> Locale.ITALIAN
        AppLanguage.HINDI -> Locale("hi")
        AppLanguage.CHINESE -> Locale.SIMPLIFIED_CHINESE
        AppLanguage.JAPANESE -> Locale.JAPANESE
        AppLanguage.KOREAN -> Locale.KOREAN
        else -> Locale.ENGLISH
    }
    val pattern = if (lang.isRtl) "EEEE، d MMMM yyyy • hh:mm a" else "EEEE, d MMMM yyyy • hh:mm a"
    val sdf = SimpleDateFormat(pattern, locale)
    return sdf.format(date)
}

/**
 * Audio Recitation & Chants Player Bar with Seek Bar, Auto-Scroll Sync, and Speed Controls.
 */
@Composable
private fun PrayerAudioPlayerBar(
    prayerId: PrayerId,
    lang: AppLanguage,
    theme: PrayerReadingTheme,
    scrollState: androidx.compose.foundation.ScrollState,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var isPlaying by remember { mutableStateOf(false) }
    var audioProgress by remember { mutableFloatStateOf(0f) }
    var speed by remember { mutableFloatStateOf(1.0f) }
    var isAutoScrollSyncEnabled by remember { mutableStateOf(true) }

    val totalDurationSeconds = 180
    val currentSeconds = (audioProgress * totalDurationSeconds).toInt()

    val currentMinStr = String.format(Locale.US, "%02d:%02d", currentSeconds / 60, currentSeconds % 60)
    val totalMinStr = String.format(Locale.US, "%02d:%02d", totalDurationSeconds / 60, totalDurationSeconds % 60)

    // Playback Loop & Auto-Scroll Synchronization
    LaunchedEffect(isPlaying, speed, isAutoScrollSyncEnabled) {
        if (isPlaying) {
            com.example.audio.SpiritualAudioPlayer.playPreview(context, com.example.audio.SpiritualSound.EPECHOIS_HARMONY)

            while (isActive && isPlaying && audioProgress < 1f) {
                delay((1000 / speed).toLong())
                audioProgress = (audioProgress + (1f / totalDurationSeconds)).coerceAtMost(1f)

                if (isAutoScrollSyncEnabled && scrollState.maxValue > 0) {
                    val targetScroll = (scrollState.maxValue * audioProgress).toInt()
                    scrollState.animateScrollTo(targetScroll, animationSpec = tween(durationMillis = 250, easing = LinearEasing))
                }
            }
            if (audioProgress >= 1f) {
                isPlaying = false
            }
        }
    }

    Surface(
        shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
        color = if (theme.surfaceColor != Color.Unspecified) theme.surfaceColor else MaterialTheme.colorScheme.surface,
        border = androidx.compose.foundation.BorderStroke(1.dp, theme.accentColor.copy(alpha = 0.35f)),
        shadowElevation = 4.dp,
        modifier = modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp)) {
            // Header Row: Audio Title & Auto-Scroll Sync Toggle
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "🎧 " + com.example.localization.AgpeyaStrings.audioPlayerTitle(lang),
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = theme.accentColor
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "(${prayerId.getDisplayName(lang)})",
                        style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                        color = if (theme.textSecondaryColor != Color.Unspecified) theme.textSecondaryColor else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                // Auto-Scroll Sync Toggle Chip
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = if (isAutoScrollSyncEnabled) theme.accentColor.copy(alpha = 0.2f) else theme.borderColor.copy(alpha = 0.15f),
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .clickable { isAutoScrollSyncEnabled = !isAutoScrollSyncEnabled }
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Icon(
                            imageVector = if (isAutoScrollSyncEnabled) Icons.Default.Check else Icons.Default.Close,
                            contentDescription = null,
                            tint = theme.accentColor,
                            modifier = Modifier.size(12.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = com.example.localization.AgpeyaStrings.autoScrollSyncTitle(lang),
                            style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                            fontWeight = FontWeight.Bold,
                            color = theme.accentColor
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            // Player Controls Row (Play/Pause, Slider, Time, Speed)
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                IconButton(
                    onClick = { isPlaying = !isPlaying },
                    modifier = Modifier
                        .size(38.dp)
                        .clip(CircleShape)
                        .background(theme.accentColor)
                        .testTag("audio_player_play_pause_btn")
                ) {
                    Icon(
                        imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                        contentDescription = "Play Chants Recitation",
                        tint = Color.Black,
                        modifier = Modifier.size(22.dp)
                    )
                }

                Spacer(modifier = Modifier.width(10.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = currentMinStr,
                            style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                            color = theme.accentColor,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = totalMinStr,
                            style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                            color = if (theme.textSecondaryColor != Color.Unspecified) theme.textSecondaryColor else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    Slider(
                        value = audioProgress,
                        onValueChange = { audioProgress = it },
                        modifier = Modifier
                            .height(20.dp)
                            .testTag("audio_player_slider"),
                        colors = SliderDefaults.colors(
                            thumbColor = theme.accentColor,
                            activeTrackColor = theme.accentColor,
                            inactiveTrackColor = theme.accentColor.copy(alpha = 0.25f)
                        )
                    )
                }

                Spacer(modifier = Modifier.width(10.dp))

                // Speed Selector Button
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = theme.accentColor.copy(alpha = 0.15f),
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .clickable {
                            speed = when (speed) {
                                1.0f -> 1.25f
                                1.25f -> 1.5f
                                1.5f -> 0.8f
                                else -> 1.0f
                            }
                        }
                        .padding(horizontal = 6.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = "${speed}x",
                        style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.5.sp),
                        fontWeight = FontWeight.Bold,
                        color = theme.accentColor
                    )
                }
            }
        }
    }
}
