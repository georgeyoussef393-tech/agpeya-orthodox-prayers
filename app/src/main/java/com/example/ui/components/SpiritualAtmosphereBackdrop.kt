package com.example.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import com.example.data.model.SpiritualBackgroundTheme

/**
 * Spiritual backdrop with reverence lighting, holy icon/church art, and soft candle glow.
 * خلفية روحية بصرية لتعزيز الخشوع والتركيز في الصلاة والتأمل.
 */
@Composable
fun SpiritualAtmosphereBackdrop(
    theme: SpiritualBackgroundTheme,
    opacity: Float = 0.22f,
    enableCandleGlow: Boolean = true,
    overlayColor: Color = Color(0xFF0F0B09), // Deep warm night tone
    modifier: Modifier = Modifier
) {
    if (theme.drawableResId == null) {
        Box(
            modifier = modifier
                .fillMaxSize()
                .background(overlayColor)
        )
        return
    }

    // Gentle candle flicker / breathing animation for deep reverence (optimized and responsive)
    val infiniteTransition = rememberInfiniteTransition(label = "candle_glow")
    val glowAlpha by infiniteTransition.animateFloat(
        initialValue = 0.92f,
        targetValue = 1.08f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1600, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "glow_alpha"
    )

    val effectiveAlpha = if (enableCandleGlow) {
        (opacity * glowAlpha).coerceIn(0.02f, 0.70f)
    } else {
        opacity.coerceIn(0.02f, 0.70f)
    }

    Box(modifier = modifier.fillMaxSize()) {
        // Base dark warm backdrop
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(overlayColor)
        )

        // Sacred background artwork
        Image(
            painter = painterResource(id = theme.drawableResId),
            contentDescription = null,
            modifier = Modifier
                .fillMaxSize()
                .alpha(effectiveAlpha),
            contentScale = ContentScale.Crop
        )

        // Soft vertical and vignette gradients for 100% crystal text readability
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            overlayColor.copy(alpha = 0.70f),
                            overlayColor.copy(alpha = 0.35f),
                            overlayColor.copy(alpha = 0.85f)
                        )
                    )
                )
        )

        // Animated Candle Flame & Incense Glow Canvas
        if (enableCandleGlow) {
            val candleFlickerRadius by infiniteTransition.animateFloat(
                initialValue = 180f,
                targetValue = 240f,
                animationSpec = infiniteRepeatable(
                    animation = tween(durationMillis = 1200, easing = FastOutSlowInEasing),
                    repeatMode = RepeatMode.Reverse
                ),
                label = "candle_radius"
            )

            val smokeOffsetY by infiniteTransition.animateFloat(
                initialValue = 0f,
                targetValue = -120f,
                animationSpec = infiniteRepeatable(
                    animation = tween(durationMillis = 2000, easing = androidx.compose.animation.core.LinearEasing),
                    repeatMode = RepeatMode.Restart
                ),
                label = "smoke_offset"
            )

            androidx.compose.foundation.Canvas(modifier = Modifier.fillMaxSize()) {
                val canvasWidth = size.width
                val canvasHeight = size.height

                // Top-Center Candle Light Halo Radial Gradient
                drawCircle(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            Color(0xFFFFD700).copy(alpha = 0.22f * glowAlpha), // Golden inner light
                            Color(0xFFFF8C00).copy(alpha = 0.12f * glowAlpha), // Amber warmth
                            Color.Transparent
                        ),
                        center = androidx.compose.ui.geometry.Offset(canvasWidth / 2f, 90f),
                        radius = candleFlickerRadius * (canvasWidth / 360f)
                    ),
                    center = androidx.compose.ui.geometry.Offset(canvasWidth / 2f, 90f),
                    radius = candleFlickerRadius * (canvasWidth / 360f)
                )

                // Incense smoke wisps (3 rising translucent particles)
                for (i in 0..2) {
                    val pX = canvasWidth / 2f + kotlin.math.sin((smokeOffsetY + i * 40) / 20f) * 25f
                    val pY = 80f + (smokeOffsetY - i * 35)
                    if (pY > 0f) {
                        drawCircle(
                            color = Color.White.copy(alpha = (0.15f * (pY / 120f)).coerceIn(0f, 0.15f)),
                            radius = 12f + (120f - pY) * 0.15f,
                            center = androidx.compose.ui.geometry.Offset(pX, pY)
                        )
                    }
                }
            }
        }
    }
}
