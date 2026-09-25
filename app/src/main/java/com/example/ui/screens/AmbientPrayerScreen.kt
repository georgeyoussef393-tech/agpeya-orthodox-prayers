package com.example.ui.screens

import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
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
import com.example.localization.AppLanguage
import com.example.ui.components.CopticCrossCanvas
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
    val context = LocalContext.current

    // Focus Retreat Timer State
    var selectedDurationMinutes by remember { mutableIntStateOf(10) } // 0 = open ended
    var isTimerRunning by remember { mutableStateOf(false) }
    var remainingSeconds by remember { mutableLongStateOf(10 * 60L) }

    // Jesus Prayer Bead Counter State
    var jesusPrayerCount by remember { mutableIntStateOf(0) }
    var prayerTargetBeads by remember { mutableIntStateOf(33) }

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

    // Animated Candle Flame Transitions (Responsive and lively)
    val infiniteTransition = rememberInfiniteTransition(label = "CandleFlame")
    val flameFlickerScaleY by infiniteTransition.animateFloat(
        initialValue = 0.92f,
        targetValue = 1.08f,
        animationSpec = infiniteRepeatable(
            animation = tween(550, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "FlameScaleY"
    )
    val flameSwayX by infiniteTransition.animateFloat(
        initialValue = -3f,
        targetValue = 3f,
        animationSpec = infiniteRepeatable(
            animation = tween(650, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "FlameSwayX"
    )
    val flameGlowAlpha by infiniteTransition.animateFloat(
        initialValue = 0.65f,
        targetValue = 0.95f,
        animationSpec = infiniteRepeatable(
            animation = tween(400, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "FlameGlowAlpha"
    )

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF0D0507),
                        Color(0xFF140A0F),
                        Color(0xFF0A0406)
                    )
                )
            )
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 20.dp),
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

                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = if (lang == AppLanguage.ARABIC) "«ادخل إلى مخدعك وأغلق بابك وصل إلى أبيك الذي في الخفاء»" else "“Enter into your closet, and when you have shut your door, pray to your Father”",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.White.copy(alpha = 0.7f),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(18.dp))
            }

            // Virtual Animated Candle Sanctuary Card
            item {
                Card(
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.Black.copy(alpha = 0.65f)
                    ),
                    border = androidx.compose.foundation.BorderStroke(1.dp, GoldPrimary.copy(alpha = 0.4f)),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(240.dp)
                ) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        // Ambient glowing aura behind candle
                        Canvas(modifier = Modifier.fillMaxSize()) {
                            val centerX = size.width / 2f
                            val centerY = size.height * 0.45f

                            // Outer Warm Amber Halo
                            drawCircle(
                                brush = Brush.radialGradient(
                                    colors = listOf(
                                        GoldPrimary.copy(alpha = 0.28f * flameGlowAlpha),
                                        Color(0xFFE65100).copy(alpha = 0.12f * flameGlowAlpha),
                                        Color.Transparent
                                    ),
                                    center = Offset(centerX, centerY),
                                    radius = 160f
                                ),
                                center = Offset(centerX, centerY),
                                radius = 160f
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
                            val flameTipY = flameBaseY - (48f * flameFlickerScaleY)

                            val flamePath = Path().apply {
                                moveTo(flameBaseX, flameTipY)
                                cubicTo(
                                    flameBaseX + 16f, flameTipY + 24f,
                                    flameBaseX + 14f, flameBaseY,
                                    flameBaseX, flameBaseY
                                )
                                cubicTo(
                                    flameBaseX - 14f, flameBaseY,
                                    flameBaseX - 16f, flameTipY + 24f,
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
                                    flameBaseX + 7f, innerTipY + 14f,
                                    flameBaseX + 6f, flameBaseY - 2f,
                                    flameBaseX, flameBaseY - 2f
                                )
                                cubicTo(
                                    flameBaseX - 6f, flameBaseY - 2f,
                                    flameBaseX - 7f, innerTipY + 14f,
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

            // Silent Retreat Focus Timer Card
            item {
                Spacer(modifier = Modifier.height(14.dp))
                Card(
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    ),
                    elevation = CardDefaults.cardElevation(2.dp),
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

            // Jesus Prayer Tasbeha Bead Counter (Chotki / مسبحة صلاة يسوع)
            item {
                Spacer(modifier = Modifier.height(14.dp))
                Card(
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant
                    ),
                    elevation = CardDefaults.cardElevation(2.dp),
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
                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier
                                    .size(34.dp)
                                    .clip(CircleShape)
                                    .background(BurgundyDeep.copy(alpha = 0.2f))
                            ) {
                                CopticCrossCanvas(color = BurgundyDeep, modifier = Modifier.size(18.dp))
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = if (lang == AppLanguage.ARABIC) "مسبحة صلاة يسوع القلبية (Chotki)" else "Jesus Prayer Bead Counter",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Text(
                                    text = if (lang == AppLanguage.ARABIC) "«يارب يسوع المسيح، ابن الله، ارحمني أنا الخاطئ»" else "“Lord Jesus Christ, Son of God, have mercy on me, a sinner”",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    lineHeight = 16.sp
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // Large Bead Counter Tap Button
                        Surface(
                            shape = CircleShape,
                            color = BurgundyDeep,
                            border = androidx.compose.foundation.BorderStroke(3.dp, GoldPrimary),
                            shadowElevation = 6.dp,
                            modifier = Modifier
                                .size(110.dp)
                                .clickable {
                                    jesusPrayerCount += 1
                                    // Light Haptic feedback on tap
                                    val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
                                    vibrator?.let {
                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                            it.vibrate(VibrationEffect.createOneShot(30, VibrationEffect.DEFAULT_AMPLITUDE))
                                        }
                                    }
                                }
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
                                    text = if (lang == AppLanguage.ARABIC) "اضغط للتسبيح" else "Tap Prayer",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = Color.White.copy(alpha = 0.85f),
                                    fontSize = 10.sp
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        // Target and Reset Row
                        Row(
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = (if (lang == AppLanguage.ARABIC) "الهدف: $prayerTargetBeads حبة" else "Target: $prayerTargetBeads beads"),
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )

                            Spacer(modifier = Modifier.width(16.dp))

                            OutlinedButton(
                                onClick = { jesusPrayerCount = 0 },
                                shape = RoundedCornerShape(10.dp),
                                contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp)
                            ) {
                                Text(
                                    text = if (lang == AppLanguage.ARABIC) "تصفير" else "Reset",
                                    fontSize = 11.sp
                                )
                            }
                        }
                    }
                }
            }

            // Rotating Patristic Quote Banner
            item {
                Spacer(modifier = Modifier.height(14.dp))
                Surface(
                    color = Color.Black.copy(alpha = 0.45f),
                    shape = RoundedCornerShape(16.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, GoldPrimary.copy(alpha = 0.25f)),
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
