package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Schedule
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
import com.example.data.model.AgpeyaPrayerContent
import com.example.data.model.HolyGospelContent
import com.example.data.model.PrayerId
import com.example.data.model.PrayerLogEntity
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
    val gospel = AgpeyaPrayerContent.getHolyGospel(prayerId, lang)
    val scrollState = rememberScrollState()

    // Coordinate of the gospel section in the scrollable column
    var gospelOffsetY by remember { mutableStateOf<Float?>(null) }
    var hasAutoRecordedThisSession by remember { mutableStateOf(false) }

    // Automatic detection: Once the user scrolls to / reads the Gospel section,
    // the system understands that the user has prayed this prayer in its time!
    LaunchedEffect(scrollState.value, gospelOffsetY, isPrayedToday) {
        val y = gospelOffsetY
        if (!isPrayedToday && !hasAutoRecordedThisSession && y != null && y > 0f) {
            // If the user scrolled far enough down to reach the Gospel section
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
                .padding(top = 28.dp, bottom = 16.dp, start = 12.dp, end = 12.dp),
            shape = RoundedCornerShape(24.dp),
            color = MaterialTheme.colorScheme.background,
            tonalElevation = 6.dp
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                // Top Header
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.surfaceVariant)
                        .padding(horizontal = 20.dp, vertical = 14.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        CopticCrossCanvas(size = 32.dp)
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
                                color = GoldPrimary
                            )
                        }
                        IconButton(
                            onClick = onDismiss,
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.surface)
                                .testTag("close_prayer_reading_btn")
                        ) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Close",
                                tint = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }

                HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)

                // Scrollable Prayer Content
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .verticalScroll(scrollState)
                        .padding(horizontal = 18.dp, vertical = 16.dp)
                ) {
                    // Spiritual Commemoration Card
                    Card(
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f)
                        ),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = "✝ " + prayerId.getSpiritualTheme(lang),
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Medium,
                                color = MaterialTheme.colorScheme.onPrimaryContainer,
                                lineHeight = 22.sp
                            )
                            Spacer(modifier = Modifier.height(10.dp))
                            Text(
                                text = prayerId.getKeyVerse(lang),
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.secondary,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(18.dp))

                    // 1. Thanksgiving Prayer
                    PrayerSectionBlock(
                        title = if (lang == AppLanguage.ARABIC) "صلاة الشكر" else "The Prayer of Thanksgiving",
                        body = AgpeyaPrayerContent.getThanksgivingPrayer(lang)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // 2. Psalm 50
                    PrayerSectionBlock(
                        title = if (lang == AppLanguage.ARABIC) "المزمور الخمسون (توبة وانسحاق)" else "Psalm 50 (Penitential)",
                        body = AgpeyaPrayerContent.getPsalm50(lang)
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    // 3. The Holy Gospel Section (الإنجيل المقدس الخاص بالساعة)
                    // We measure its position to trigger automatic recording upon reaching the Gospel
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .onGloballyPositioned { coordinates ->
                                gospelOffsetY = coordinates.positionInParent().y
                            }
                    ) {
                        HolyGospelCard(
                            gospel = gospel,
                            lang = lang
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // 4. Gospel Completion & Automatic Prayer Record Card
                    GospelCompletionTrackerCard(
                        isPrayedToday = isPrayedToday,
                        prayerLog = prayerLog,
                        lang = lang,
                        onAutoMarkPrayed = onAutoMarkPrayed,
                        onTogglePrayed = onTogglePrayed
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    // 5. Litanies of the hour (قطع الساعة والمراحم)
                    PrayerSectionBlock(
                        title = if (lang == AppLanguage.ARABIC) "قطع الساعة والمراحم" else "Litanies of the Hour",
                        body = AgpeyaPrayerContent.getHourLitanies(prayerId, lang)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // 6. Trisagion and conclusion (الثلاث تقديسات وخاتمة الساعة)
                    PrayerSectionBlock(
                        title = if (lang == AppLanguage.ARABIC) "الثلاث تقديسات وخاتمة الساعة" else "Trisagion & Conclusion",
                        body = AgpeyaPrayerContent.getTrisagionAndConclusion(lang)
                    )

                    Spacer(modifier = Modifier.height(24.dp))
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

@Composable
private fun HolyGospelCard(
    gospel: HolyGospelContent,
    lang: AppLanguage
) {
    Card(
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
        modifier = Modifier
            .fillMaxWidth()
            .border(1.5.dp, GoldPrimary.copy(alpha = 0.45f), RoundedCornerShape(18.dp))
            .testTag("holy_gospel_card")
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            // Gospel Header Badge
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
                        text = AgpeyaStrings.holyGospel(lang),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = GoldPrimary
                    )
                    Text(
                        text = gospel.reference,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Evangelist Title & Litany
            Surface(
                shape = RoundedCornerShape(10.dp),
                color = BurgundyDeep.copy(alpha = 0.08f),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(10.dp)) {
                    Text(
                        text = "✝ " + gospel.evangelistTitle,
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.Bold,
                        color = BurgundyPrimary
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = gospel.introLitany,
                        style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.5.sp),
                        fontStyle = FontStyle.Italic,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            HorizontalDivider(
                color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f),
                modifier = Modifier.padding(vertical = 12.dp)
            )

            // Gospel Scripture Body
            Text(
                text = gospel.text,
                style = MaterialTheme.typography.bodyLarge.copy(fontSize = 16.5.sp),
                lineHeight = 28.sp,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Doxology / Conclusion
            Text(
                text = gospel.response,
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
 * Automatically acknowledges that reading the Gospel marks the prayer in its time,
 * records with current date & time, and allows the user to undo if needed.
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
                // State 1: Prayer automatically detected and recorded with Date and Time
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

                    // Undo / Unmark button so user has full control ("تسجيل اني صليت او لا")
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
                // State 2: Automatic prompt / 1-click fallback
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

@Composable
private fun PrayerSectionBlock(title: String, body: String) {
    Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = GoldPrimary
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = body,
                style = MaterialTheme.typography.bodyMedium,
                lineHeight = 24.sp,
                color = MaterialTheme.colorScheme.onSurface
            )
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
