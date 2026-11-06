package com.example.ui.components

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
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Church
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.graphics3d.card3dTilt
import com.example.ui.theme.BurgundyDeep
import com.example.ui.theme.GoldDark
import com.example.ui.theme.GoldLight
import com.example.ui.theme.GoldPrimary
import kotlin.math.cos
import kotlin.math.sin

/**
 * High-fidelity Coptic Orthodox Sacred Visual Graphics System.
 * Renders illuminated gold foil banners, 3D glassmorphic cards, sacred Coptic filigree dividers,
 * and flickering candle particle halos for an immersive, premium spiritual atmosphere.
 */

/**
 * Glassmorphic Sacred Card with beveled gold foil border, 3D touch perspective tilt,
 * and translucent frosted sanctuary background.
 */
@Composable
fun SacredGlassCard(
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
    enable3dTilt: Boolean = false,
    content: @Composable () -> Unit
) {
    val goldFoilBrush = remember {
        Brush.linearGradient(
            colors = listOf(
                GoldLight.copy(alpha = 0.9f),
                GoldPrimary,
                GoldDark.copy(alpha = 0.8f),
                GoldLight.copy(alpha = 0.9f)
            )
        )
    }

    val cardModifier = if (enable3dTilt) {
        modifier.card3dTilt(maxTiltDegrees = 8f)
    } else {
        modifier
    }

    Card(
        modifier = cardModifier
            .then(
                if (onClick != null) Modifier.clickable { onClick() } else Modifier
            ),
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.92f)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        border = androidx.compose.foundation.BorderStroke(1.2.dp, goldFoilBrush)
    ) {
        Box(
            modifier = Modifier
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            BurgundyDeep.copy(alpha = 0.08f),
                            Color.Transparent,
                            GoldPrimary.copy(alpha = 0.04f)
                        )
                    )
                )
        ) {
            content()
        }
    }
}

/**
 * Illuminated Coptic Manuscript Header Banner with glowing sanctuary aura,
 * golden 3D Coptic Cross, and gold foil calligraphic header.
 */
@Composable
fun IlluminatedCopticHeaderBanner(
    title: String,
    subtitle: String? = null,
    copticDateText: String? = null,
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "banner_shimmer")
    val shimmerAlpha by infiniteTransition.animateFloat(
        initialValue = 0.7f,
        targetValue = 1.0f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 2200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "shimmer_alpha"
    )

    SacredGlassCard(
        modifier = modifier.fillMaxWidth(),
        enable3dTilt = true
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                // Left Ornamental Wing
                CopticFiligreeWing(modifier = Modifier.size(width = 36.dp, height = 18.dp))

                Spacer(modifier = Modifier.width(12.dp))

                // Sacred Coptic Cross Emblem
                Box(
                    modifier = Modifier
                        .size(52.dp)
                        .clip(CircleShape)
                        .background(
                            Brush.radialGradient(
                                colors = listOf(
                                    GoldLight.copy(alpha = shimmerAlpha),
                                    GoldPrimary,
                                    BurgundyDeep
                                )
                            )
                        )
                        .border(1.5.dp, GoldLight, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    CopticCrossCanvas(
                        size = 32.dp,
                        color = Color.Black,
                        ringColor = GoldLight
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                // Right Ornamental Wing
                CopticFiligreeWing(modifier = Modifier.size(width = 36.dp, height = 18.dp), isReversed = true)
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Title
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = GoldPrimary,
                textAlign = TextAlign.Center,
                fontSize = 21.sp,
                lineHeight = 28.sp
            )

            if (!subtitle.isNullOrBlank()) {
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center,
                    fontSize = 13.sp,
                    lineHeight = 18.sp
                )
            }

            if (!copticDateText.isNullOrBlank()) {
                Spacer(modifier = Modifier.height(10.dp))
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = BurgundyDeep.copy(alpha = 0.85f),
                    border = androidx.compose.foundation.BorderStroke(1.dp, GoldPrimary.copy(alpha = 0.7f))
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = "📅 ",
                            fontSize = 13.sp
                        )
                        Text(
                            text = copticDateText,
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold,
                            color = GoldLight,
                            fontSize = 13.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Filigree Line Divider
            CopticGoldFiligreeDivider(modifier = Modifier.fillMaxWidth(0.85f))
        }
    }
}

/**
 * Traditional Coptic Manuscript Filigree Line Divider with central Coptic Cross ornament.
 */
@Composable
fun CopticGoldFiligreeDivider(
    modifier: Modifier = Modifier,
    color: Color = GoldPrimary
) {
    Canvas(
        modifier = modifier.height(16.dp)
    ) {
        val w = size.width
        val h = size.height
        val cy = h / 2f
        val cx = w / 2f

        // Left Line
        drawLine(
            brush = Brush.horizontalGradient(
                colors = listOf(Color.Transparent, color, color)
            ),
            start = Offset(0f, cy),
            end = Offset(cx - 16.dp.toPx(), cy),
            strokeWidth = 1.5.dp.toPx(),
            cap = StrokeCap.Round
        )

        // Right Line
        drawLine(
            brush = Brush.horizontalGradient(
                colors = listOf(color, color, Color.Transparent)
            ),
            start = Offset(cx + 16.dp.toPx(), cy),
            end = Offset(w, cy),
            strokeWidth = 1.5.dp.toPx(),
            cap = StrokeCap.Round
        )

        // Center Diamond Dot
        val diamondSize = 5.dp.toPx()
        val path = Path().apply {
            moveTo(cx, cy - diamondSize)
            lineTo(cx + diamondSize, cy)
            lineTo(cx, cy + diamondSize)
            lineTo(cx - diamondSize, cy)
            close()
        }
        drawPath(path = path, color = color)
    }
}

/**
 * Coptic Manuscript Filigree Wing Ornament.
 */
@Composable
private fun CopticFiligreeWing(
    modifier: Modifier = Modifier,
    color: Color = GoldPrimary,
    isReversed: Boolean = false
) {
    Canvas(modifier = modifier) {
        val w = size.width
        val h = size.height
        val cy = h / 2f

        val path = Path().apply {
            if (!isReversed) {
                moveTo(0f, cy)
                cubicTo(w * 0.4f, cy - h * 0.4f, w * 0.7f, cy + h * 0.4f, w, cy)
            } else {
                moveTo(w, cy)
                cubicTo(w * 0.6f, cy - h * 0.4f, w * 0.3f, cy + h * 0.4f, 0f, cy)
            }
        }

        drawPath(
            path = path,
            color = color,
            style = Stroke(width = 1.5.dp.toPx(), cap = StrokeCap.Round)
        )
    }
}
