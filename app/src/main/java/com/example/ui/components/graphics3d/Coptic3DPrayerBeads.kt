package com.example.ui.components.graphics3d

import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.TouchApp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.BurgundyDeep
import com.example.ui.theme.GoldDark
import com.example.ui.theme.GoldLight
import com.example.ui.theme.GoldPrimary
import kotlinx.coroutines.launch
import kotlin.math.cos
import kotlin.math.sin

/**
 * Interactive 3D Prayer Beads / Tasbeha (المسبحة الإلكترونية ثلاثية الأبعاد)
 * Renders 3D spherical beads on an orbital track with perspective scale depth,
 * haptic vibration feedback, and custom Coptic prayer targets.
 */
@Composable
fun Coptic3DPrayerBeads(
    modifier: Modifier = Modifier,
    initialCount: Int = 0,
    targetCount: Int = 33,
    onCountChanged: (Int) -> Unit = {}
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    var count by remember { mutableIntStateOf(initialCount) }
    val beadRotation = remember { Animatable(0f) }

    fun triggerHaptic() {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                val vm = context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as? VibratorManager
                vm?.defaultVibrator?.vibrate(VibrationEffect.createPredefined(VibrationEffect.EFFECT_CLICK))
            } else {
                @Suppress("DEPRECATION")
                val v = context.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    v?.vibrate(VibrationEffect.createOneShot(20L, VibrationEffect.DEFAULT_AMPLITUDE))
                } else {
                    @Suppress("DEPRECATION")
                    v?.vibrate(20L)
                }
            }
        } catch (_: Exception) {
            // Safe fallback if vibration permission or hardware unavailable
        }
    }

    fun incrementBeads() {
        count++
        onCountChanged(count)
        triggerHaptic()
        scope.launch {
            beadRotation.animateTo(
                targetValue = beadRotation.value + 360f / 12f,
                animationSpec = spring(dampingRatio = 0.6f, stiffness = 400f)
            )
        }
    }

    Card(
        modifier = modifier.testTag("card_3d_prayer_beads"),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.95f)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, GoldPrimary.copy(alpha = 0.4f))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape)
                            .background(GoldPrimary.copy(alpha = 0.15f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.AutoAwesome,
                            contentDescription = null,
                            tint = GoldPrimary,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "المسبحة الإلكترونية 3D",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = GoldPrimary
                    )
                }

                IconButton(
                    onClick = {
                        count = 0
                        onCountChanged(0)
                        triggerHaptic()
                    },
                    modifier = Modifier.size(32.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Refresh,
                        contentDescription = "صفر المسبحة",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // 3D Orbital Canvas for Prayer Beads
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                BurgundyDeep.copy(alpha = 0.4f),
                                Color.Black.copy(alpha = 0.9f)
                            )
                        )
                    )
                    .pointerInput(Unit) {
                        detectTapGestures {
                            incrementBeads()
                        }
                    },
                contentAlignment = Alignment.Center
            ) {
                Canvas(
                    modifier = Modifier.fillMaxSize()
                ) {
                    draw3DPrayerBeadsOrbitalTrack(
                        rotationAngle = beadRotation.value,
                        activeCount = count
                    )
                }

                // Center Display Counter inside 3D Ring
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "$count",
                        fontSize = 42.sp,
                        fontWeight = FontWeight.Bold,
                        color = GoldPrimary
                    )
                    Text(
                        text = "من $targetCount صلاة / تسبيحة",
                        style = MaterialTheme.typography.labelSmall,
                        color = GoldLight.copy(alpha = 0.85f)
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Tap Button
            Button(
                onClick = { incrementBeads() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .testTag("btn_tap_3d_beads"),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(containerColor = GoldPrimary)
            ) {
                Icon(
                    imageVector = Icons.Default.TouchApp,
                    contentDescription = null,
                    tint = Color.Black,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "انقر للتسبيح والعد (+1)",
                    color = Color.Black,
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp
                )
            }
        }
    }
}

/**
 * Draws 12 3D spherical beads on an elliptical track with perspective depth scaling,
 * specular highlights, and gold metallic shaders.
 */
private fun DrawScope.draw3DPrayerBeadsOrbitalTrack(
    rotationAngle: Float,
    activeCount: Int
) {
    val cx = size.width / 2f
    val cy = size.height / 2f
    val rx = size.width * 0.38f
    val ry = size.height * 0.32f

    // Draw Elliptical String Track
    drawOval(
        color = GoldPrimary.copy(alpha = 0.35f),
        topLeft = Offset(cx - rx, cy - ry),
        size = androidx.compose.ui.geometry.Size(rx * 2f, ry * 2f),
        style = Stroke(width = 2.dp.toPx())
    )

    val beadCount = 12
    for (i in 0 until beadCount) {
        val angleDeg = (i * (360f / beadCount)) + rotationAngle
        val angleRad = Math.toRadians(angleDeg.toDouble())

        // 3D perspective projection onto 2D plane
        val bx = cx + (rx * cos(angleRad)).toFloat()
        val by = cy + (ry * sin(angleRad)).toFloat()

        // Depth perspective scale
        val depthFactor = (sin(angleRad) + 1f) / 2f
        val beadRadius = 10.dp.toPx() + (6.dp.toPx() * depthFactor.toFloat())
        val alpha = 0.5f + (0.5f * depthFactor.toFloat())

        val isHighlighted = (i == 0)

        val beadBrush = if (isHighlighted) {
            Brush.radialGradient(
                colors = listOf(Color.White, GoldLight, GoldDark),
                center = Offset(bx - beadRadius * 0.3f, by - beadRadius * 0.3f),
                radius = beadRadius * 1.2f
            )
        } else {
            Brush.radialGradient(
                colors = listOf(GoldLight.copy(alpha = alpha), GoldPrimary.copy(alpha = alpha), BurgundyDeep.copy(alpha = alpha)),
                center = Offset(bx - beadRadius * 0.3f, by - beadRadius * 0.3f),
                radius = beadRadius * 1.2f
            )
        }

        // Draw 3D Bead Drop Shadow
        drawCircle(
            color = Color.Black.copy(alpha = 0.4f * depthFactor.toFloat()),
            radius = beadRadius * 0.9f,
            center = Offset(bx + 3.dp.toPx(), by + 3.dp.toPx())
        )

        // Draw 3D Sphere Bead
        drawCircle(
            brush = beadBrush,
            radius = beadRadius,
            center = Offset(bx, by)
        )

        // Specular Highlight Dot
        drawCircle(
            color = Color.White.copy(alpha = 0.8f * alpha),
            radius = beadRadius * 0.25f,
            center = Offset(bx - beadRadius * 0.35f, by - beadRadius * 0.35f)
        )
    }
}
