package com.example.ui.screens

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import com.example.ui.animation.AgpeyaMotion
import com.example.ui.animation.pressBounce
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Today
import com.example.ui.components.OrthodoxGroundedSearchDialog
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.coptic.CopticCalendarHelper
import com.example.data.coptic.FastingType
import com.example.localization.AgpeyaStrings
import com.example.localization.AppLanguage
import com.example.ui.components.CopticCrossCanvas
import com.example.ui.components.SpiritualAtmosphereBackdrop
import com.example.ui.components.SubtleCrossWatermark
import com.example.ui.theme.BurgundyDeep
import com.example.ui.theme.BurgundyLight
import com.example.ui.theme.GoldContainerLight
import com.example.ui.theme.GoldLight
import com.example.ui.theme.GoldPrimary
import com.example.ui.theme.PeacefulGreen
import com.example.ui.viewmodel.AgpeyaViewModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@Composable
fun CopticCalendarScreen(
    viewModel: AgpeyaViewModel,
    onNavigateToPrayers: () -> Unit,
    modifier: Modifier = Modifier
) {
    val lang by viewModel.currentLanguage.collectAsState()
    val spiritualTheme by viewModel.spiritualTheme.collectAsState()
    val spiritualOpacity by viewModel.spiritualOpacity.collectAsState()
    val isCandleGlowEnabled by viewModel.isCandleGlowEnabled.collectAsState()

    val context = LocalContext.current
    var selectedCalendarOffsetDays by remember { mutableStateOf(0) }
    var showGroundedSearchDialog by remember { mutableStateOf(false) }
    var groundedSearchInitialQuery by remember { mutableStateOf("") }

    val activeCalendar = remember(selectedCalendarOffsetDays) {
        Calendar.getInstance().apply {
            add(Calendar.DAY_OF_YEAR, selectedCalendarOffsetDays)
        }
    }

    val copticDate = remember(activeCalendar) {
        CopticCalendarHelper.getCopticDate(activeCalendar)
    }

    val gregorianDateStr = remember(activeCalendar) {
        val sdf = SimpleDateFormat("EEEE, d MMMM yyyy", if (lang == AppLanguage.ARABIC) Locale("ar") else Locale.ENGLISH)
        sdf.format(activeCalendar.time)
    }

    Box(modifier = modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 88.dp)
        ) {
            // Top Hero Header with Coptic Date and Day Nav
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(
                                    BurgundyDeep,
                                    Color(0xFF1E0B11)
                                )
                            )
                        )
                        .padding(horizontal = 16.dp, vertical = 20.dp)
                ) {
                    SubtleCrossWatermark(modifier = Modifier.align(Alignment.CenterEnd))

                    Column {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier
                                    .size(46.dp)
                                    .clip(CircleShape)
                                    .background(GoldPrimary.copy(alpha = 0.22f))
                                    .border(1.5.dp, GoldPrimary, CircleShape)
                            ) {
                                CopticCrossCanvas(
                                    color = GoldLight,
                                    modifier = Modifier.size(26.dp)
                                )
                            }

                            Spacer(modifier = Modifier.width(12.dp))

                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = if (lang == AppLanguage.ARABIC) "التقويم القبطي والقطمارس اليومي" else "Coptic Calendar & Katameros",
                                    style = MaterialTheme.typography.titleLarge,
                                    fontWeight = FontWeight.Bold,
                                    color = GoldLight
                                )
                                Text(
                                    text = gregorianDateStr,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = Color.White.copy(alpha = 0.8f)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(18.dp))

                        // Big Coptic Date Banner Card
                        Card(
                            shape = RoundedCornerShape(18.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = Color.Black.copy(alpha = 0.45f)
                            ),
                            border = androidx.compose.foundation.BorderStroke(1.dp, GoldPrimary.copy(alpha = 0.5f)),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp)
                            ) {
                                AnimatedContent(
                                    targetState = selectedCalendarOffsetDays,
                                    transitionSpec = {
                                        val forward = if (lang.isRtl) targetState < initialState else targetState > initialState
                                        AgpeyaMotion.directionalSlide(forward = forward)
                                    },
                                    label = "coptic_date_transition"
                                ) { _ ->
                                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                        Text(
                                            text = "${copticDate.day} ${if (lang == AppLanguage.ARABIC) copticDate.monthNameAr else copticDate.monthNameEn} ${copticDate.yearAM} ش",
                                            style = MaterialTheme.typography.headlineMedium,
                                            fontWeight = FontWeight.Bold,
                                            color = GoldPrimary,
                                            textAlign = TextAlign.Center
                                        )
                                        Text(
                                            text = copticDate.monthNameCoptic,
                                            style = MaterialTheme.typography.titleMedium,
                                            color = Color.White.copy(alpha = 0.85f),
                                            textAlign = TextAlign.Center
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.height(12.dp))

                                // Day Navigator Row
                                Row(
                                    horizontalArrangement = Arrangement.Center,
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    IconButton(
                                        onClick = { selectedCalendarOffsetDays -= 1 },
                                        modifier = Modifier.size(36.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                            contentDescription = "Previous Day",
                                            tint = GoldLight
                                        )
                                    }

                                    Spacer(modifier = Modifier.width(8.dp))

                                    Button(
                                        onClick = { selectedCalendarOffsetDays = 0 },
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = if (selectedCalendarOffsetDays == 0) GoldPrimary else Color.White.copy(alpha = 0.15f)
                                        ),
                                        shape = RoundedCornerShape(20.dp),
                                        contentPadding = PaddingValues(horizontal = 14.dp, vertical = 6.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Today,
                                            contentDescription = null,
                                            tint = if (selectedCalendarOffsetDays == 0) Color.Black else Color.White,
                                            modifier = Modifier.size(16.dp)
                                        )
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text(
                                            text = if (lang == AppLanguage.ARABIC) "اليوم الحالي" else "Today",
                                            color = if (selectedCalendarOffsetDays == 0) Color.Black else Color.White,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 12.sp
                                        )
                                    }

                                    Spacer(modifier = Modifier.width(8.dp))

                                    IconButton(
                                        onClick = { selectedCalendarOffsetDays += 1 },
                                        modifier = Modifier.size(36.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                                            contentDescription = "Next Day",
                                            tint = GoldLight
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // Google Search Grounded Research & Calendar Verification Card
            item {
                Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)) {
                    Card(
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surface
                        ),
                        border = androidx.compose.foundation.BorderStroke(1.dp, GoldPrimary.copy(alpha = 0.45f)),
                        elevation = CardDefaults.cardElevation(2.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .pressBounce()
                            .clickable {
                                groundedSearchInitialQuery = if (lang == AppLanguage.ARABIC) {
                                    "طقس وأعياد اليوم ${copticDate.day} ${copticDate.monthNameAr} وقراءات القطمارس"
                                } else {
                                    "Liturgy and readings for ${copticDate.day} ${copticDate.monthNameEn}"
                                }
                                showGroundedSearchDialog = true
                            }
                            .testTag("card_grounded_search")
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(14.dp)
                        ) {
                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier
                                    .size(42.dp)
                                    .clip(CircleShape)
                                    .background(
                                        Brush.radialGradient(
                                            listOf(GoldLight, GoldPrimary, BurgundyDeep)
                                        )
                                    )
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Search,
                                    contentDescription = null,
                                    tint = Color.Black,
                                    modifier = Modifier.size(22.dp)
                                )
                            }

                            Spacer(modifier = Modifier.width(12.dp))

                            Column(modifier = Modifier.weight(1f)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = if (lang == AppLanguage.ARABIC) "البحث الأرثوذكسي الموثق" else "Orthodox Grounded Search",
                                        style = MaterialTheme.typography.titleSmall,
                                        fontWeight = FontWeight.Bold,
                                        color = GoldPrimary
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Surface(
                                        color = GoldPrimary.copy(alpha = 0.18f),
                                        shape = RoundedCornerShape(6.dp)
                                    ) {
                                        Text(
                                            text = "Google Search",
                                            style = MaterialTheme.typography.labelSmall,
                                            color = GoldLight,
                                            fontSize = 9.sp,
                                            fontWeight = FontWeight.Bold,
                                            modifier = Modifier.padding(horizontal = 5.dp, vertical = 2.dp)
                                        )
                                    }
                                }
                                Spacer(modifier = Modifier.height(3.dp))
                                Text(
                                    text = if (lang == AppLanguage.ARABIC) {
                                        "ابحث في الأعياد، مواعيد الأصوام، وتأملات وقراءات الكنيسة بدقة موثقة من محرك بحث Google"
                                    } else {
                                        "Search feasts, fast dates, and Church scripture reflections verified via Google Search"
                                    },
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    lineHeight = 16.sp
                                )
                            }
                        }
                    }
                }
            }

            // Fasting & Feast Classification Card
            item {
                Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)) {
                    val fastType = copticDate.fastingType
                    val fastBadgeColor = when (fastType) {
                        FastingType.STRICT_FAST -> BurgundyDeep
                        FastingType.FISH_ALLOWED -> Color(0xFF1565C0)
                        FastingType.NON_FASTING_FEAST -> PeacefulGreen
                        FastingType.REGULAR_DAY -> Color(0xFF424242)
                    }

                    Card(
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                        elevation = CardDefaults.cardElevation(2.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Surface(
                                    color = fastBadgeColor.copy(alpha = 0.18f),
                                    shape = RoundedCornerShape(12.dp),
                                    border = androidx.compose.foundation.BorderStroke(1.dp, fastBadgeColor.copy(alpha = 0.5f)),
                                    modifier = Modifier.padding(end = 12.dp)
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                                    ) {
                                        Text(
                                            text = fastType.iconEmoji,
                                            fontSize = 18.sp
                                        )
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text(
                                            text = if (lang == AppLanguage.ARABIC) fastType.arabicName else fastType.englishName,
                                            style = MaterialTheme.typography.labelMedium,
                                            fontWeight = FontWeight.Bold,
                                            color = fastBadgeColor
                                        )
                                    }
                                }

                                if (copticDate.feastNameAr != null) {
                                    Spacer(modifier = Modifier.weight(1f))
                                    Surface(
                                        color = GoldPrimary.copy(alpha = 0.2f),
                                        shape = RoundedCornerShape(12.dp)
                                    ) {
                                        Text(
                                            text = "⭐ " + (if (lang == AppLanguage.ARABIC) "عيد وتذكار" else "Feast Day"),
                                            style = MaterialTheme.typography.labelSmall,
                                            fontWeight = FontWeight.Bold,
                                            color = GoldPrimary,
                                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                        )
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            Text(
                                text = if (lang == AppLanguage.ARABIC) fastType.descriptionAr else fastType.descriptionEn,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                lineHeight = 18.sp
                            )
                        }
                    }
                }
            }

            // Daily Katameros Readings (Psalm & Gospel)
            item {
                Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp)) {
                    Card(
                        shape = RoundedCornerShape(18.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                        elevation = CardDefaults.cardElevation(2.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(18.dp)) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Box(
                                    contentAlignment = Alignment.Center,
                                    modifier = Modifier
                                        .size(38.dp)
                                        .clip(CircleShape)
                                        .background(BurgundyDeep.copy(alpha = 0.18f))
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.MenuBook,
                                        contentDescription = null,
                                        tint = BurgundyDeep,
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.width(12.dp))
                                Text(
                                    text = if (lang == AppLanguage.ARABIC) "قراءات قطمارس اليوم (المزمور والإنجيل)" else "Daily Katameros Readings",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }

                            Spacer(modifier = Modifier.height(14.dp))

                            // Psalm Reading
                            Surface(
                                color = MaterialTheme.colorScheme.surface,
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(modifier = Modifier.padding(14.dp)) {
                                    Text(
                                        text = if (lang == AppLanguage.ARABIC) "📜 مزمور اليوم:" else "📜 Daily Psalm:",
                                        style = MaterialTheme.typography.labelMedium,
                                        fontWeight = FontWeight.Bold,
                                        color = GoldPrimary
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = if (lang == AppLanguage.ARABIC) copticDate.katamerosPsalmAr else copticDate.katamerosPsalmEn,
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = MaterialTheme.colorScheme.onSurface,
                                        lineHeight = 20.sp
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            // Gospel Reading
                            Surface(
                                color = MaterialTheme.colorScheme.surface,
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(modifier = Modifier.padding(14.dp)) {
                                    Text(
                                        text = if (lang == AppLanguage.ARABIC) "✝️ إنجيل اليوم:" else "✝️ Daily Gospel:",
                                        style = MaterialTheme.typography.labelMedium,
                                        fontWeight = FontWeight.Bold,
                                        color = GoldPrimary
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = if (lang == AppLanguage.ARABIC) copticDate.katamerosGospelAr else copticDate.katamerosGospelEn,
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = MaterialTheme.colorScheme.onSurface,
                                        lineHeight = 20.sp
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(14.dp))

                            // Copy and Share Readings
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                OutlinedButton(
                                    onClick = {
                                        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                        val fullText = "${copticDate.day} ${copticDate.monthNameAr} ${copticDate.yearAM} ش\n\n" +
                                                "${copticDate.katamerosPsalmAr}\n" +
                                                "${copticDate.katamerosGospelAr}"
                                        clipboard.setPrimaryClip(ClipData.newPlainText("Coptic Reading", fullText))
                                        Toast.makeText(context, if (lang == AppLanguage.ARABIC) "تم نسخ قراءات اليوم" else "Copied to clipboard", Toast.LENGTH_SHORT).show()
                                    },
                                    modifier = Modifier.weight(1f),
                                    shape = RoundedCornerShape(10.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.ContentCopy,
                                        contentDescription = null,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = if (lang == AppLanguage.ARABIC) "نسخ" else "Copy",
                                        fontSize = 12.sp
                                    )
                                }

                                Button(
                                    onClick = onNavigateToPrayers,
                                    modifier = Modifier.weight(1f),
                                    colors = ButtonDefaults.buttonColors(containerColor = GoldPrimary),
                                    shape = RoundedCornerShape(10.dp)
                                ) {
                                    Text(
                                        text = if (lang == AppLanguage.ARABIC) "صلوات السواعي" else "Pray Agpeya",
                                        color = Color.Black,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 12.sp
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        if (showGroundedSearchDialog) {
            OrthodoxGroundedSearchDialog(
                language = lang,
                onDismissRequest = { showGroundedSearchDialog = false },
                initialQuery = groundedSearchInitialQuery
            )
        }
    }
}
