package com.example.ui.screens

import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.widget.Toast
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.localization.AgpeyaStrings
import com.example.localization.AppLanguage
import com.example.ui.components.CopticCrossCanvas
import com.example.ui.components.SpiritualAtmosphereBackdrop
import com.example.ui.theme.BurgundyDeep
import com.example.ui.theme.GoldLight
import com.example.ui.theme.GoldPrimary
import com.example.ui.viewmodel.AgpeyaViewModel
import kotlinx.coroutines.delay
import java.util.Locale

@Composable
fun AmbientPrayerScreen(
    viewModel: AgpeyaViewModel,
    modifier: Modifier = Modifier
) {
    val lang by viewModel.currentLanguage.collectAsState()
    val spiritualTheme by viewModel.spiritualTheme.collectAsState()
    val spiritualOpacity by viewModel.spiritualOpacity.collectAsState()
    val isCandleGlowEnabled by viewModel.isCandleGlowEnabled.collectAsState()
    val dailyArrowTarget by viewModel.dailyArrowTarget.collectAsState()
    val context = LocalContext.current

    // Focus Retreat Timer State
    var selectedDurationMinutes by remember { mutableIntStateOf(10) } // 0 = open ended
    var isTimerRunning by remember { mutableStateOf(false) }
    var remainingSeconds by remember { mutableLongStateOf(10 * 60L) }

    // Jesus Prayer Bead Counter State
    var jesusPrayerCount by remember { mutableIntStateOf(0) }

    // Patristic Quotes Rotator
    var activeQuoteIndex by remember { mutableIntStateOf(0) }

    val patristicQuotes = remember(lang) {
        if (lang == AppLanguage.ARABIC) {
            listOf(
                "«الصلاة هي حديث مع الله، وهي اتحاد العقل بالخالق.» — القديس يوحنا ذهبي الفم",
                "«اسكت لسانك ليتكلم قلبك، واسكت قلبك ليتكلم الله فيك.» — القديس إسحق السرياني",
                "«الصلاة سياج للنفس وسلاح ضد الأفكار الرديئة وميناء هادئ من كل عاصفة.» — القديس باسيليوس الكبير",
                "«حينما تقف للصلاة، فانسَ الأرض وما فيها وكأنك واقف أمام عرش الله في السماء.» — القديس أنبا مقار الكبير",
                "«يارب يسوع المسيح، ابن الله الحي، ارحمني أنا الخاطئ وخلص نفسي.» — صلاة يسوع الدائمة"
            )
        } else {
            listOf(
                "“Prayer is a conversation with God, uniting the human mind with its Creator.” — St. John Chrysostom",
                "“Silence your tongue that your heart may speak, and silence your heart that God may speak within you.” — St. Isaac the Syrian",
                "“Prayer is a bulwark of the soul, a weapon against evil thoughts, and a safe harbor from all storms.” — St. Basil the Great",
                "“When you stand to pray, forget the world and stand as though in Heaven before the Throne of God.” — St. Macarius the Great",
                "“Lord Jesus Christ, Son of the Living God, have mercy on me, a sinner.” — The Jesus Prayer"
            )
        }
    }

    // Timer Countdown Coroutine
    LaunchedEffect(isTimerRunning, remainingSeconds) {
        if (isTimerRunning && remainingSeconds > 0) {
            delay(1000L)
            remainingSeconds -= 1
            if (remainingSeconds <= 0) {
                isTimerRunning = false
                // Vibrate on complete
                val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
                vibrator?.let {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        it.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE))
                    } else {
                        it.vibrate(500)
                    }
                }
            }
        }
    }

    // Quote rotation timer
    LaunchedEffect(Unit) {
        while (true) {
            delay(18000L)
            activeQuoteIndex = (activeQuoteIndex + 1) % patristicQuotes.size
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .statusBarsPadding()
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Screen Title
            item {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    CopticCrossCanvas(color = GoldPrimary, modifier = Modifier.size(24.dp))
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = if (lang == AppLanguage.ARABIC) "خلوة" else "Retreat",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = GoldLight
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    CopticCrossCanvas(color = GoldPrimary, modifier = Modifier.size(24.dp))
                }

                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = if (lang == AppLanguage.ARABIC) "«ادخل إلى مخدعك وأغلق بابك وصل إلى أبيك الذي في الخفاء»" else "“Enter into your closet, and when you have shut your door, pray to your Father”",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.White.copy(alpha = 0.7f),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
            }

            // Virtual Animated Candle Sanctuary Card
            item {
                VirtualCandleSanctuaryCard()
            }

            // Silent Retreat Focus Timer Card
            item {
                Spacer(modifier = Modifier.height(14.dp))
                Card(
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.45f)
                    ),
                    elevation = CardDefaults.cardElevation(1.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(18.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Icon(
                                imageVector = Icons.Default.Timer,
                                contentDescription = null,
                                tint = GoldPrimary,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = if (lang == AppLanguage.ARABIC) "مؤقت الخلوة والتركيز الصامت" else "Quiet Meditation Timer",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        // Duration Chips
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            listOf(5, 10, 15, 20, 30).forEach { mins ->
                                FilterChip(
                                    selected = selectedDurationMinutes == mins,
                                    onClick = {
                                        selectedDurationMinutes = mins
                                        remainingSeconds = mins * 60L
                                        isTimerRunning = false
                                    },
                                    label = { Text("$mins " + (if (lang == AppLanguage.ARABIC) "د" else "m"), fontSize = 12.sp) },
                                    colors = FilterChipDefaults.filterChipColors(
                                        selectedContainerColor = GoldPrimary,
                                        selectedLabelColor = Color.Black
                                    ),
                                    shape = RoundedCornerShape(12.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // Large Digital Countdown Display
                        val minutes = remainingSeconds / 60
                        val seconds = remainingSeconds % 60
                        val timeStr = String.format(Locale.US, "%02d:%02d", minutes, seconds)

                        Text(
                            text = timeStr,
                            style = MaterialTheme.typography.displayMedium,
                            fontWeight = FontWeight.Bold,
                            color = if (isTimerRunning) GoldPrimary else MaterialTheme.colorScheme.onSurface,
                            letterSpacing = 2.sp
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        // Controls
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Button(
                                onClick = { isTimerRunning = !isTimerRunning },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = if (isTimerRunning) BurgundyDeep else GoldPrimary
                                ),
                                shape = RoundedCornerShape(14.dp),
                                contentPadding = PaddingValues(horizontal = 24.dp, vertical = 10.dp)
                            ) {
                                Icon(
                                    imageVector = if (isTimerRunning) Icons.Default.Pause else Icons.Default.PlayArrow,
                                    contentDescription = null,
                                    tint = if (isTimerRunning) Color.White else Color.Black,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = if (isTimerRunning) {
                                        if (lang == AppLanguage.ARABIC) "إيقاف مؤقت" else "Pause"
                                    } else {
                                        if (lang == AppLanguage.ARABIC) "بدء الخلوة" else "Start Focus"
                                    },
                                    color = if (isTimerRunning) Color.White else Color.Black,
                                    fontWeight = FontWeight.Bold
                                )
                            }

                            OutlinedButton(
                                onClick = {
                                    isTimerRunning = false
                                    remainingSeconds = selectedDurationMinutes * 60L
                                },
                                shape = RoundedCornerShape(14.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Refresh,
                                    contentDescription = null,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }
                    }
                }
            }

            // Jesus Prayer & Arrow Prayers Bead Counter (الصلوات السهمية ومسبحة صلاة يسوع)
            item {
                Spacer(modifier = Modifier.height(14.dp))
                val allArrowLogs by viewModel.allArrowLogs.collectAsState()
                val todayString = com.example.data.repository.AgpeyaRepository.getTodayString()
                val todayArrowCount = remember(allArrowLogs, todayString) {
                    allArrowLogs.filter { it.dateString == todayString }.sumOf { it.count }
                }

                val arrowPrayerPresets = remember(lang) {
                    if (lang == AppLanguage.ARABIC || lang == AppLanguage.SYRIAN_ARABIC || lang == AppLanguage.SYRIAC) {
                        listOf(
                            "«يارب يسوع المسيح، ابن الله، ارحمني أنا الخاطئ» (صلاة يسوع)",
                            "«يا ربي يسوع المسيح، أسرع إلى معونتي وعضدني» (سهمية الاستغاثة)",
                            "«اللهم اغفر لي خطاياي ونقِ قلبي برحمتك» (سهمية التوبة)",
                            "«نشكرك يا صانع الخيرات الرحوم في كل حين» (سهمية الشكر)",
                            "«يا ربي يسوع خلصني، يا ربي يسوع أعني» (سهمية المعونة)",
                            "«المجد لك يا محب البشر الصالح، هللويا» (سهمية التمجيد)"
                        )
                    } else {
                        listOf(
                            "“Lord Jesus Christ, Son of God, have mercy on me, a sinner” (Jesus Prayer)",
                            "“O Lord, make haste to help me and deliver me” (Supplication)",
                            "“O God, forgive my sins and cleanse my heart” (Repentance)",
                            "“We give thanks to You, O Beneficent Lord” (Thanksgiving)",
                            "“My Lord Jesus Christ, save me and help me” (Salvation)",
                            "“Glory to You, O Lover of Mankind, Alleluia” (Praise)"
                        )
                    }
                }
                var selectedArrowPrayerIndex by remember { mutableIntStateOf(0) }

                Card(
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.45f)
                    ),
                    elevation = CardDefaults.cardElevation(1.dp),
                    modifier = Modifier.fillMaxWidth().testTag("jesus_prayer_beads_card")
                ) {
                    Column(
                        modifier = Modifier.padding(18.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(CircleShape)
                                    .background(BurgundyDeep.copy(alpha = 0.2f))
                            ) {
                                CopticCrossCanvas(color = BurgundyDeep, modifier = Modifier.size(20.dp))
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = AgpeyaStrings.arrowPrayersTitle(lang),
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Text(
                                    text = if (lang == AppLanguage.ARABIC) "مسبحة الصلوات السهمية لتسجيلها في التقارير الدورية" else "Sacred Arrow Prayers counter for daily & periodic reports",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    fontSize = 11.sp
                                )
                            }

                            // Today's Arrow count badge
                            Surface(
                                shape = RoundedCornerShape(10.dp),
                                color = GoldPrimary.copy(alpha = 0.15f),
                                border = androidx.compose.foundation.BorderStroke(1.dp, GoldPrimary.copy(alpha = 0.4f))
                            ) {
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                ) {
                                    Text(
                                        text = "$todayArrowCount",
                                        style = MaterialTheme.typography.labelMedium,
                                        fontWeight = FontWeight.Bold,
                                        color = GoldPrimary
                                    )
                                    Text(
                                        text = if (lang == AppLanguage.ARABIC) "سهمية اليوم" else "today",
                                        style = MaterialTheme.typography.labelSmall.copy(fontSize = 9.sp),
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // Arrow Prayer Preset Chips
                        LazyRow(
                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            itemsIndexed(arrowPrayerPresets) { idx, prayer ->
                                val isSelected = selectedArrowPrayerIndex == idx
                                Surface(
                                    shape = RoundedCornerShape(10.dp),
                                    color = if (isSelected) GoldPrimary.copy(alpha = 0.22f) else MaterialTheme.colorScheme.surface,
                                    border = androidx.compose.foundation.BorderStroke(
                                        1.dp,
                                        if (isSelected) GoldPrimary else MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f)
                                    ),
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(10.dp))
                                        .clickable { selectedArrowPrayerIndex = idx }
                                ) {
                                    Text(
                                        text = prayer.substringBefore("(").trim(),
                                        style = MaterialTheme.typography.labelSmall,
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                        color = if (isSelected) GoldPrimary else MaterialTheme.colorScheme.onSurface,
                                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        // Active Prayer Text Display
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = MaterialTheme.colorScheme.surface.copy(alpha = 0.8f),
                            border = androidx.compose.foundation.BorderStroke(1.dp, GoldPrimary.copy(alpha = 0.3f)),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = arrowPrayerPresets[selectedArrowPrayerIndex],
                                style = MaterialTheme.typography.bodySmall,
                                fontWeight = FontWeight.SemiBold,
                                color = GoldLight,
                                textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // Large Bead Counter Tap Button
                        Surface(
                            shape = CircleShape,
                            color = BurgundyDeep,
                            border = androidx.compose.foundation.BorderStroke(3.5.dp, GoldPrimary),
                            shadowElevation = 8.dp,
                            modifier = Modifier
                                .size(116.dp)
                                .clickable {
                                    jesusPrayerCount += 1
                                    val chosenText = arrowPrayerPresets[selectedArrowPrayerIndex]
                                    // Instantly record tap count and today's date in Room DB for reports
                                    viewModel.recordArrowPrayerTap(chosenText)

                                    // Light Haptic feedback on tap
                                    val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
                                    vibrator?.let {
                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                            it.vibrate(VibrationEffect.createOneShot(30, VibrationEffect.DEFAULT_AMPLITUDE))
                                        }
                                    }
                                }
                                .testTag("bead_counter_tap_btn")
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center,
                                modifier = Modifier.fillMaxSize()
                            ) {
                                Text(
                                    text = "$jesusPrayerCount",
                                    style = MaterialTheme.typography.headlineLarge,
                                    fontWeight = FontWeight.Bold,
                                    color = GoldLight
                                )
                                Text(
                                    text = if (lang == AppLanguage.ARABIC) "اضغط للتسبيح ✝" else "Tap to Pray ✝",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = Color.White.copy(alpha = 0.85f),
                                    fontSize = 10.sp
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        // Real-time Auto-Recording Status Banner
                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = if (isTimerRunning) GoldPrimary.copy(alpha = 0.18f) else MaterialTheme.colorScheme.surface.copy(alpha = 0.6f),
                            border = androidx.compose.foundation.BorderStroke(1.dp, if (isTimerRunning) GoldPrimary else MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f)),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                            ) {
                                Text(
                                    text = if (isTimerRunning) {
                                        if (lang == AppLanguage.ARABIC) "🕊️ الخلوة نشطة — تُسجل كل نقرة في التقرير اليومي لحظياً" else "🕊️ Retreat Active — Every tap recorded to daily report instantly"
                                    } else {
                                        if (lang == AppLanguage.ARABIC) "✓ تُسجل النقرات واليوم تلقائياً في التقرير بجانب الصلوات اليومية" else "✓ Taps & date auto-recorded in report alongside daily prayers"
                                    },
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.SemiBold,
                                    color = if (isTimerRunning) GoldLight else MaterialTheme.colorScheme.onSurfaceVariant,
                                    fontSize = 11.sp,
                                    textAlign = TextAlign.Center
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // Target Display and Reset Row
                        Row(
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = GoldPrimary.copy(alpha = 0.15f),
                                border = androidx.compose.foundation.BorderStroke(1.dp, GoldPrimary.copy(alpha = 0.35f))
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                ) {
                                    Text(
                                        text = if (lang == AppLanguage.ARABIC) "الهدف اليومي:" else "Daily Target:",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = "$dailyArrowTarget",
                                        style = MaterialTheme.typography.labelSmall,
                                        fontWeight = FontWeight.Bold,
                                        color = GoldPrimary
                                    )
                                }
                            }

                            OutlinedButton(
                                onClick = { jesusPrayerCount = 0 },
                                shape = RoundedCornerShape(10.dp),
                                contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp)
                            ) {
                                Text(
                                    text = if (lang == AppLanguage.ARABIC) "تصفير الجلسة" else "Reset Session",
                                    fontSize = 11.sp
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        // Primary Action: Save & Record to Reports
                        Button(
                            onClick = {
                                if (jesusPrayerCount > 0) {
                                    val chosenText = arrowPrayerPresets[selectedArrowPrayerIndex]
                                    viewModel.logArrowPrayer(jesusPrayerCount, chosenText)
                                    Toast.makeText(
                                        context,
                                        "${AgpeyaStrings.arrowPrayerSavedSuccess(lang)} ($jesusPrayerCount)",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    jesusPrayerCount = 0
                                } else {
                                    Toast.makeText(
                                        context,
                                        if (lang == AppLanguage.ARABIC) "اضغط على المسبحة أولاً لتسجيل الصلوات" else "Tap beads first to record prayer count",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = BurgundyDeep),
                            shape = RoundedCornerShape(12.dp),
                            border = androidx.compose.foundation.BorderStroke(1.dp, GoldPrimary),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(46.dp)
                                .testTag("save_arrow_prayers_btn")
                        ) {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = null,
                                tint = GoldLight,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "${AgpeyaStrings.saveArrowPrayers(lang)} ${if (jesusPrayerCount > 0) "($jesusPrayerCount)" else ""}",
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.Bold,
                                color = GoldLight
                            )
                        }
                    }
                }
            }

            // Rotating Patristic Quote Banner
            item {
                Spacer(modifier = Modifier.height(12.dp))
                Surface(
                    color = Color.Black.copy(alpha = 0.20f),
                    shape = RoundedCornerShape(16.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, GoldPrimary.copy(alpha = 0.20f)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = patristicQuotes[activeQuoteIndex],
                            style = MaterialTheme.typography.bodyMedium,
                            color = GoldLight,
                            textAlign = TextAlign.Center,
                            lineHeight = 22.sp
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun VirtualCandleSanctuaryCard() {
    val infiniteTransition = rememberInfiniteTransition(label = "CandleFlame")
    val flameFlickerScaleY by infiniteTransition.animateFloat(
        initialValue = 0.94f,
        targetValue = 1.06f,
        animationSpec = infiniteRepeatable(
            animation = tween(650, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "FlameScaleY"
    )
    val flameSwayX by infiniteTransition.animateFloat(
        initialValue = -2.5f,
        targetValue = 2.5f,
        animationSpec = infiniteRepeatable(
            animation = tween(750, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "FlameSwayX"
    )
    val flameGlowAlpha by infiniteTransition.animateFloat(
        initialValue = 0.70f,
        targetValue = 0.95f,
        animationSpec = infiniteRepeatable(
            animation = tween(500, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "FlameGlowAlpha"
    )

    Card(
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.Black.copy(alpha = 0.25f)
        ),
        border = androidx.compose.foundation.BorderStroke(1.dp, GoldPrimary.copy(alpha = 0.25f)),
        modifier = Modifier
            .fillMaxWidth()
            .height(160.dp)
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                val centerX = size.width / 2f
                val centerY = size.height * 0.45f

                // Outer Warm Amber Halo
                drawCircle(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            GoldPrimary.copy(alpha = 0.25f * flameGlowAlpha),
                            Color(0xFFE65100).copy(alpha = 0.10f * flameGlowAlpha),
                            Color.Transparent
                        ),
                        center = Offset(centerX, centerY),
                        radius = 150f
                    ),
                    center = Offset(centerX, centerY),
                    radius = 150f
                )

                // Candle Body Wax Column
                val candleWidth = 44f
                val candleHeight = 120f
                val candleTop = centerY + 28f
                drawRoundRect(
                    color = Color(0xFFF5E6CC),
                    topLeft = Offset(centerX - candleWidth / 2f, candleTop),
                    size = androidx.compose.ui.geometry.Size(candleWidth, candleHeight),
                    cornerRadius = androidx.compose.ui.geometry.CornerRadius(6f, 6f)
                )

                // Wick
                drawLine(
                    color = Color(0xFF212121),
                    start = Offset(centerX, candleTop),
                    end = Offset(centerX + (flameSwayX * 0.3f), candleTop - 14f),
                    strokeWidth = 3f
                )

                // Teardrop Flame
                val flameBaseX = centerX + flameSwayX
                val flameBaseY = candleTop - 12f
                val flameTipY = flameBaseY - (46f * flameFlickerScaleY)

                val flamePath = Path().apply {
                    moveTo(flameBaseX, flameTipY)
                    cubicTo(
                        flameBaseX + 15f, flameTipY + 22f,
                        flameBaseX + 13f, flameBaseY,
                        flameBaseX, flameBaseY
                    )
                    cubicTo(
                        flameBaseX - 13f, flameBaseY,
                        flameBaseX - 15f, flameTipY + 22f,
                        flameBaseX, flameTipY
                    )
                    close()
                }

                // Outer Flame (Gold/Orange)
                drawPath(
                    path = flamePath,
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFFFFD54F),
                            Color(0xFFFF6D00)
                        ),
                        startY = flameTipY,
                        endY = flameBaseY
                    ),
                    style = Fill
                )

                // Inner Core Flame (Bright White-Yellow)
                val innerFlamePath = Path().apply {
                    val innerTipY = flameTipY + 12f
                    moveTo(flameBaseX, innerTipY)
                    cubicTo(
                        flameBaseX + 6f, innerTipY + 12f,
                        flameBaseX + 5f, flameBaseY - 2f,
                        flameBaseX, flameBaseY - 2f
                    )
                    cubicTo(
                        flameBaseX - 5f, flameBaseY - 2f,
                        flameBaseX - 6f, innerTipY + 12f,
                        flameBaseX, innerTipY
                    )
                    close()
                }

                drawPath(
                    path = innerFlamePath,
                    color = Color(0xFFFFFFEE),
                    style = Fill
                )
            }

            // Cross Accent in Background of Altar
            CopticCrossCanvas(
                color = GoldLight.copy(alpha = 0.15f),
                modifier = Modifier
                    .size(120.dp)
                    .align(Alignment.Center)
            )
        }
    }
}

