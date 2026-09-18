package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material.icons.filled.Undo
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.data.model.AgpeyaHourDetails
import com.example.data.model.PrayerId
import com.example.data.model.PrayerLogEntity
import com.example.data.model.PrayerSectionItem
import com.example.localization.AgpeyaStrings
import com.example.localization.AppLanguage
import com.example.ui.theme.BurgundyDeep
import com.example.ui.theme.BurgundyPrimary
import com.example.ui.theme.GoldPrimary
import com.example.ui.theme.PeacefulGreen
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun PrayerReadingModal(
    prayerId: PrayerId,
    lang: AppLanguage,
    isPrayedToday: Boolean,
    prayerLog: PrayerLogEntity? = null,
    onAutoMarkPrayed: () -> Unit,
    onTogglePrayed: () -> Unit,
    onDismiss: () -> Unit
) {
    val fullSections = remember(prayerId, lang) {
        AgpeyaHourDetails.getFullPrayerSections(prayerId, lang)
    }

    val scrollState = rememberScrollState()
    var fontSizeMultiplier by remember { mutableFloatStateOf(1.0f) }

    // Coordinates of Gospel section to trigger automatic recorded prayer
    var gospelOffsetY by remember { mutableStateOf<Float?>(null) }
    var hasAutoRecordedThisSession by remember { mutableStateOf(false) }

    LaunchedEffect(scrollState.value, gospelOffsetY, isPrayedToday) {
        val y = gospelOffsetY
        if (!isPrayedToday && !hasAutoRecordedThisSession && y != null && y > 0f) {
            if (scrollState.value >= (y - 500f)) {
                hasAutoRecordedThisSession = true
                onAutoMarkPrayed()
            }
        }
    }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 24.dp, bottom = 12.dp, start = 8.dp, end = 8.dp),
            shape = RoundedCornerShape(24.dp),
            color = MaterialTheme.colorScheme.background,
            tonalElevation = 6.dp
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                // Top Header with Title, Coptic Name & Controls
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.surfaceVariant)
                        .padding(horizontal = 16.dp, vertical = 12.dp)
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
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Surface(
                                    shape = RoundedCornerShape(6.dp),
                                    color = GoldPrimary.copy(alpha = 0.2f),
                                    modifier = Modifier.padding(horizontal = 4.dp)
                                ) {
                                    Text(
                                        text = if (lang == AppLanguage.ARABIC) "صلاة كاملة" else "Complete",
                                        style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                                        fontWeight = FontWeight.Bold,
                                        color = GoldPrimary,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }
                            }
                            Text(
                                text = "${prayerId.copticTitle} • ${fullSections.size} ${if (lang == AppLanguage.ARABIC) "أقسام ليتورجية" else "Liturgical Sections"}",
                                style = MaterialTheme.typography.bodySmall,
                                color = GoldPrimary
                            )
                        }

                        // Text Size Zoom Toggle (Small, Normal, Large)
                        IconButton(
                            onClick = {
                                fontSizeMultiplier = when {
                                    fontSizeMultiplier < 1.1f -> 1.25f
                                    fontSizeMultiplier < 1.35f -> 1.45f
                                    else -> 1.0f
                                }
                            },
                            modifier = Modifier
                                .size(34.dp)
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.surface)
                        ) {
                            Icon(
                                imageVector = Icons.Default.FormatSize,
                                contentDescription = "Font size",
                                tint = GoldPrimary,
                                modifier = Modifier.size(18.dp)
                            )
                        }

                        Spacer(modifier = Modifier.width(8.dp))

                        // Close Dialog Button
                        IconButton(
                            onClick = onDismiss,
                            modifier = Modifier
                                .size(34.dp)
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.surface)
                                .testTag("close_prayer_reading_btn")
                        ) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Close",
                                tint = MaterialTheme.colorScheme.onSurface,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                }

                HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)

                // Quick Navigation Chips for Sections
                LazyRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.surface)
                        .padding(horizontal = 12.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    itemsIndexed(fullSections) { index, section ->
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = if (section.isGospel) GoldPrimary.copy(alpha = 0.25f) else MaterialTheme.colorScheme.surfaceVariant,
                            border = if (section.isGospel) androidx.compose.foundation.BorderStroke(1.dp, GoldPrimary) else null,
                            modifier = Modifier.clip(RoundedCornerShape(8.dp))
                        ) {
                            Text(
                                text = "${index + 1}. ${section.getTitle(lang).take(18)}..",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = if (section.isGospel) FontWeight.Bold else FontWeight.Medium,
                                color = if (section.isGospel) GoldPrimary else MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }
                }

                HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))

                // Scrollable Prayer Content (All Complete Liturgical Sections)
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .verticalScroll(scrollState)
                        .padding(horizontal = 16.dp, vertical = 14.dp)
                ) {
                    // Spiritual Commemoration Header Card
                    Card(
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.45f)
                        ),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Text(
                                text = "✝ " + prayerId.getSpiritualTheme(lang),
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    fontSize = 14.sp * fontSizeMultiplier,
                                    lineHeight = 22.sp * fontSizeMultiplier
                                ),
                                fontWeight = FontWeight.Medium,
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = prayerId.getKeyVerse(lang),
                                style = MaterialTheme.typography.bodySmall.copy(
                                    fontSize = 12.5.sp * fontSizeMultiplier
                                ),
                                color = MaterialTheme.colorScheme.secondary,
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
                                    multiplier = fontSizeMultiplier
                                )
                            }

                            Spacer(modifier = Modifier.height(14.dp))

                            // Interactive Gospel Completion & Automatic Prayer Log Card
                            GospelCompletionTrackerCard(
                                isPrayedToday = isPrayedToday,
                                prayerLog = prayerLog,
                                lang = lang,
                                onAutoMarkPrayed = onAutoMarkPrayed,
                                onTogglePrayed = onTogglePrayed
                            )
                        } else {
                            DetailedPrayerSectionCard(
                                section = section,
                                lang = lang,
                                stepNumber = index + 1,
                                multiplier = fontSizeMultiplier
                            )
                        }

                        Spacer(modifier = Modifier.height(14.dp))
                    }

                    Spacer(modifier = Modifier.height(20.dp))
                }

                // Bottom Action Bar
                Surface(
                    tonalElevation = 8.dp,
                    color = MaterialTheme.colorScheme.surface,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        Button(
                            onClick = onTogglePrayed,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (isPrayedToday) PeacefulGreen else GoldPrimary
                            ),
                            shape = RoundedCornerShape(14.dp),
                            modifier = Modifier
                                .weight(1f)
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
                                fontSize = 14.5.sp
                            )
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        OutlinedButton(
                            onClick = onDismiss,
                            shape = RoundedCornerShape(14.dp)
                        ) {
                            Text(
                                text = if (lang == AppLanguage.ARABIC) "إغلاق" else "Close",
                                style = MaterialTheme.typography.labelLarge
                            )
                        }
                    }
                }
            }
        }
    }
}

/**
 * Liturgical Section Card supporting rubrics, subtitles, Coptic references, and collapsible details.
 */
@Composable
private fun DetailedPrayerSectionCard(
    section: PrayerSectionItem,
    lang: AppLanguage,
    stepNumber: Int,
    multiplier: Float
) {
    Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Section Title and Step Badge
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Surface(
                    shape = CircleShape,
                    color = GoldPrimary.copy(alpha = 0.15f),
                    modifier = Modifier.size(26.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text(
                            text = "$stepNumber",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = GoldPrimary
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
                        color = GoldPrimary
                    )
                    section.getSubtitle(lang)?.let { sub ->
                        Text(
                            text = sub,
                            style = MaterialTheme.typography.bodySmall.copy(fontSize = 12.sp),
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
                section.copticIntro?.let { coptic ->
                    Text(
                        text = coptic,
                        style = MaterialTheme.typography.bodySmall,
                        color = BurgundyPrimary,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            // Rubric / Direction
            section.getRubric(lang)?.let { rubric ->
                Spacer(modifier = Modifier.height(8.dp))
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = BurgundyDeep.copy(alpha = 0.08f),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "❖ $rubric",
                        style = MaterialTheme.typography.bodySmall.copy(
                            fontSize = 11.5.sp,
                            fontStyle = FontStyle.Italic
                        ),
                        color = BurgundyPrimary,
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
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

/**
 * Rich Holy Gospel Display Card.
 */
@Composable
private fun DetailedGospelBlock(
    section: PrayerSectionItem,
    lang: AppLanguage,
    multiplier: Float
) {
    Card(
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
        modifier = Modifier
            .fillMaxWidth()
            .border(1.5.dp, GoldPrimary.copy(alpha = 0.5f), RoundedCornerShape(18.dp))
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
                        .background(GoldPrimary.copy(alpha = 0.18f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.MenuBook,
                        contentDescription = null,
                        tint = GoldPrimary,
                        modifier = Modifier.size(20.dp)
                    )
                }
                Spacer(modifier = Modifier.width(10.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = section.getTitle(lang),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = GoldPrimary
                    )
                    section.getSubtitle(lang)?.let { sub ->
                        Text(
                            text = sub,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Evangelist Litany Banner
            Surface(
                shape = RoundedCornerShape(10.dp),
                color = BurgundyDeep.copy(alpha = 0.08f),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(10.dp)) {
                    Text(
                        text = if (lang == AppLanguage.ARABIC) "✝ قفوا بخوف أمام الله لسماع الإنجيل المقدس" else "✝ Stand in the fear of God, let us hear the Holy Gospel",
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.Bold,
                        color = BurgundyPrimary
                    )
                }
            }

            HorizontalDivider(
                color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f),
                modifier = Modifier.padding(vertical = 12.dp)
            )

            // Scripture Text
            Text(
                text = section.getContent(lang),
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontSize = 16.5.sp * multiplier,
                    lineHeight = 28.sp * multiplier
                ),
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Doxology
            Text(
                text = if (lang == AppLanguage.ARABIC) "والمجد لله دائماً أبدياً، آمين." else "Glory to God forever. Amen.",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold,
                color = GoldPrimary,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .background(GoldPrimary.copy(alpha = 0.10f))
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
    onTogglePrayed: () -> Unit
) {
    val formattedDateTime = formatLogDateTime(prayerLog?.timestamp ?: System.currentTimeMillis(), lang)

    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isPrayedToday) {
                PeacefulGreen.copy(alpha = 0.12f)
            } else {
                GoldPrimary.copy(alpha = 0.12f)
            }
        ),
        modifier = Modifier
            .fillMaxWidth()
            .border(
                width = 1.5.dp,
                color = if (isPrayedToday) PeacefulGreen.copy(alpha = 0.6f) else GoldPrimary.copy(alpha = 0.5f),
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
                    tint = if (isPrayedToday) PeacefulGreen else GoldPrimary,
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
                    color = if (isPrayedToday) PeacefulGreen else GoldPrimary
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
                                color = MaterialTheme.colorScheme.onSurface
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
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        lineHeight = 18.sp
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Button(
                        onClick = onAutoMarkPrayed,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = GoldPrimary
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
        AppLanguage.ARABIC -> Locale("ar")
        AppLanguage.FRENCH -> Locale.FRENCH
        AppLanguage.SPANISH -> Locale("es")
        AppLanguage.GERMAN -> Locale.GERMAN
        AppLanguage.ITALIAN -> Locale.ITALIAN
        else -> Locale.ENGLISH
    }
    val sdf = SimpleDateFormat("EEEE، d MMMM yyyy • hh:mm a", locale)
    return sdf.format(date)
}
