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
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.animateScrollBy
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import kotlinx.coroutines.launch
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
import androidx.compose.ui.text.style.TextOverflow
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
    val coroutineScope = rememberCoroutineScope()

    var showQuickIndexSheet by remember { mutableStateOf(false) }
    val sectionPositions = remember { mutableStateMapOf<Int, Float>() }

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
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Surface(
                modifier = Modifier
                    .fillMaxHeight()
                    .widthIn(max = 840.dp)
                    .padding(top = 20.dp, bottom = 24.dp, start = 8.dp, end = 8.dp),
                shape = RoundedCornerShape(24.dp),
                color = activeBgColor,
                tonalElevation = 6.dp
            ) {
                Column(modifier = Modifier.fillMaxSize()) {
                // Top Header with Title, Quick Index Button, Auto-Scroll & Close
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
                            Text(
                                text = prayerId.getDisplayName(lang),
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = if (readingTheme.textColor != Color.Unspecified) readingTheme.textColor else MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = "${prayerId.copticTitle} • ${fullSections.size} ${if (lang == AppLanguage.ARABIC) "أقسام ليتورجية" else "Liturgical Sections"}",
                                style = MaterialTheme.typography.bodySmall,
                                color = readingTheme.accentColor
                            )
                        }

                        // Right Controls: Quick Index Button + Auto-Scroll Button + Close Button
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            // 1. Prominent Quick Index Button ("فهرس الأقسام")
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = readingTheme.accentColor.copy(alpha = 0.18f),
                                border = BorderStroke(1.dp, readingTheme.accentColor.copy(alpha = 0.5f)),
                                modifier = Modifier
                                    .clip(RoundedCornerShape(12.dp))
                                    .clickable { showQuickIndexSheet = true }
                                    .testTag("quick_index_btn")
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.MenuBook,
                                        contentDescription = "Quick Index",
                                        tint = readingTheme.accentColor,
                                        modifier = Modifier.size(18.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = if (lang == AppLanguage.ARABIC) "فهرس الأقسام" else "Index",
                                        style = MaterialTheme.typography.labelMedium,
                                        fontWeight = FontWeight.Bold,
                                        color = readingTheme.accentColor
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.width(8.dp))

                            // 2. Compact Auto-Scroll Toggle
                            IconButton(
                                onClick = { isAutoScrolling = !isAutoScrolling },
                                modifier = Modifier
                                    .size(34.dp)
                                    .clip(CircleShape)
                                    .background(
                                        if (isAutoScrolling) readingTheme.accentColor.copy(alpha = 0.25f)
                                        else activeBgColor
                                    )
                                    .testTag("auto_scroll_toggle_btn")
                            ) {
                                Icon(
                                    imageVector = if (isAutoScrolling) Icons.Default.Pause else Icons.Default.PlayArrow,
                                    contentDescription = "Toggle Auto-Scroll",
                                    tint = readingTheme.accentColor,
                                    modifier = Modifier.size(18.dp)
                                )
                            }

                            Spacer(modifier = Modifier.width(8.dp))

                            // 3. Close Dialog Button
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
                }

                HorizontalDivider(color = readingTheme.borderColor.copy(alpha = 0.2f))

                // Scrollable Prayer Content (All Complete Liturgical Sections) framed by Spiritual Atmosphere
                Box(modifier = Modifier.weight(1f)) {
                    // Sacred Reverence Background Backdrop
                    SpiritualAtmosphereBackdrop(
                        theme = currentSpiritualTheme,
                        opacity = (spiritualOpacity * 1.5f).coerceIn(0.28f, 0.65f),
                        enableCandleGlow = isCandleModeActive,
                        overlayColor = activeBgColor.copy(alpha = 0.55f)
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
                                readingTheme.surfaceColor.copy(alpha = 0.82f)
                            } else {
                                MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.40f)
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
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .onGloballyPositioned { coordinates ->
                                    val y = coordinates.positionInParent().y
                                    sectionPositions[index] = y
                                    if (section.isGospel) {
                                        gospelOffsetY = y
                                    }
                                }
                        ) {
                            if (section.isGospel) {
                                Column {
                                    DetailedGospelBlock(
                                        section = section,
                                        lang = lang,
                                        multiplier = currentFontSizeMultiplier,
                                        theme = readingTheme,
                                        isBilingual = isBilingual,
                                        secondaryLang = secondaryLang
                                    )

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
                                }
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
                                    text = AgpeyaStrings.closeButton(lang),
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

    // Quick Index Selection Dialog Modal
    if (showQuickIndexSheet) {
        Dialog(
            onDismissRequest = { showQuickIndexSheet = false },
            properties = DialogProperties(usePlatformDefaultWidth = false)
        ) {
            Surface(
                shape = RoundedCornerShape(24.dp),
                color = activeBgColor,
                tonalElevation = 8.dp,
                modifier = Modifier
                    .fillMaxWidth(0.92f)
                    .fillMaxHeight(0.80f)
                    .padding(12.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(18.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.MenuBook,
                                contentDescription = null,
                                tint = readingTheme.accentColor,
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = if (lang == AppLanguage.ARABIC) "فهرس أقسام الصلاة" else "Prayer Sections Index",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = if (readingTheme.textColor != Color.Unspecified) readingTheme.textColor else MaterialTheme.colorScheme.onSurface
                            )
                        }
                        IconButton(
                            onClick = { showQuickIndexSheet = false },
                            modifier = Modifier
                                .size(32.dp)
                                .clip(CircleShape)
                                .background(activeSurfaceColor)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Close",
                                tint = readingTheme.accentColor,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }

                    HorizontalDivider(
                        modifier = Modifier.padding(vertical = 12.dp),
                        color = readingTheme.borderColor.copy(alpha = 0.3f)
                    )

                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        itemsIndexed(fullSections) { index, section ->
                            val isGospel = section.isGospel
                            Card(
                                onClick = {
                                    showQuickIndexSheet = false
                                    val targetY = sectionPositions[index] ?: 0f
                                    coroutineScope.launch {
                                        scrollState.animateScrollTo(targetY.toInt().coerceAtLeast(0))
                                    }
                                },
                                shape = RoundedCornerShape(14.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = if (isGospel) readingTheme.accentColor.copy(alpha = 0.18f) else activeSurfaceColor
                                ),
                                border = if (isGospel) BorderStroke(1.dp, readingTheme.accentColor) else BorderStroke(0.5.dp, readingTheme.borderColor.copy(alpha = 0.3f)),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 14.dp, vertical = 12.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Surface(
                                        shape = CircleShape,
                                        color = readingTheme.accentColor.copy(alpha = 0.2f),
                                        modifier = Modifier.size(32.dp)
                                    ) {
                                        Box(contentAlignment = Alignment.Center) {
                                            Text(
                                                text = "${index + 1}",
                                                style = MaterialTheme.typography.labelMedium,
                                                fontWeight = FontWeight.Bold,
                                                color = readingTheme.accentColor
                                            )
                                        }
                                    }

                                    Spacer(modifier = Modifier.width(12.dp))

                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            text = section.getTitle(lang),
                                            style = MaterialTheme.typography.bodyMedium,
                                            fontWeight = if (isGospel) FontWeight.Bold else FontWeight.SemiBold,
                                            color = if (readingTheme.textColor != Color.Unspecified) readingTheme.textColor else MaterialTheme.colorScheme.onSurface,
                                            maxLines = 1,
                                            overflow = TextOverflow.Ellipsis
                                        )
                                        val subtitleText = section.getSubtitle(lang)
                                        if (!subtitleText.isNullOrBlank()) {
                                            Text(
                                                text = subtitleText,
                                                style = MaterialTheme.typography.bodySmall,
                                                color = readingTheme.accentColor.copy(alpha = 0.8f),
                                                maxLines = 1,
                                                overflow = TextOverflow.Ellipsis
                                            )
                                        }
                                    }

                                    if (isGospel) {
                                        Surface(
                                            shape = RoundedCornerShape(6.dp),
                                            color = GoldPrimary.copy(alpha = 0.25f),
                                            modifier = Modifier.padding(start = 6.dp)
                                        ) {
                                            Text(
                                                text = if (lang == AppLanguage.ARABIC) "✝ الإنجيل" else "✝ Gospel",
                                                style = MaterialTheme.typography.labelSmall,
                                                fontWeight = FontWeight.Bold,
                                                color = GoldPrimary,
                                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
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
        theme.surfaceColor.copy(alpha = 0.88f)
    } else {
        MaterialTheme.colorScheme.surface.copy(alpha = 0.88f)
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
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = coptic,
                        style = MaterialTheme.typography.bodySmall,
                        color = theme.rubricColor,
                        fontWeight = FontWeight.SemiBold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
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

            // Parallel Bilingual Text display when explicitly enabled and content is distinct
            if (isBilingual && secondaryLang != lang) {
                val secContent = section.getContent(secondaryLang)
                val mainContent = section.getContent(lang)
                if (secContent.isNotBlank() && secContent.trim() != mainContent.trim()) {
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
                        text = secContent,
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
        theme.surfaceColor.copy(alpha = 0.88f)
    } else {
        MaterialTheme.colorScheme.surface.copy(alpha = 0.88f)
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

            // Parallel Bilingual Text display when explicitly enabled and content is distinct
            if (isBilingual && secondaryLang != lang) {
                val secContent = section.getContent(secondaryLang)
                val mainContent = section.getContent(lang)
                if (secContent.isNotBlank() && secContent.trim() != mainContent.trim()) {
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
                        text = secContent,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontSize = 15.sp * multiplier,
                            lineHeight = 25.sp * multiplier
                        ),
                        color = textColor.copy(alpha = 0.9f)
                    )
                }
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
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = "🎧 " + com.example.localization.AgpeyaStrings.audioPlayerTitle(lang),
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        color = theme.accentColor
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "(${prayerId.getDisplayName(lang)})",
                        style = MaterialTheme.typography.bodySmall.copy(fontSize = 10.sp),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        color = if (theme.textSecondaryColor != Color.Unspecified) theme.textSecondaryColor else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

                // Auto-Scroll Sync Toggle Chip (Compact)
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = if (isAutoScrollSyncEnabled) theme.accentColor.copy(alpha = 0.2f) else theme.borderColor.copy(alpha = 0.15f),
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .clickable { isAutoScrollSyncEnabled = !isAutoScrollSyncEnabled }
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                    ) {
                        Icon(
                            imageVector = if (isAutoScrollSyncEnabled) Icons.Default.Check else Icons.Default.Close,
                            contentDescription = null,
                            tint = theme.accentColor,
                            modifier = Modifier.size(10.dp)
                        )
                        Spacer(modifier = Modifier.width(3.dp))
                        Text(
                            text = com.example.localization.AgpeyaStrings.autoScrollSyncTitle(lang),
                            style = MaterialTheme.typography.labelSmall.copy(fontSize = 9.sp),
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
