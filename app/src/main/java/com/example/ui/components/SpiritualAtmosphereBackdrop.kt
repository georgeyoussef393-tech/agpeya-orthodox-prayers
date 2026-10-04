package com.example.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import com.example.data.model.SpiritualBackgroundTheme

/**
 * Spiritual backdrop with reverence lighting, holy icon/church art, and soft candle glow.
 * Ultra-lightweight implementation using graphicsLayer and GPU draw scope without recomposition overhead.
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

    val clampedOpacity = opacity.coerceIn(0.02f, 0.70f)

    Box(modifier = modifier.fillMaxSize()) {
        // Base dark warm backdrop
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(overlayColor)
        )

        // Sacred background artwork with draw-phase alpha
        Image(
            painter = painterResource(id = theme.drawableResId),
            contentDescription = null,
            modifier = Modifier
                .fillMaxSize()
                .graphicsLayer {
                    alpha = clampedOpacity
                },
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

        // Isolated ultra-lightweight animated candlelight halo
        if (enableCandleGlow) {
            OptimizedCandleGlowCanvas()
        }
    }
}

@Composable
private fun OptimizedCandleGlowCanvas() {
    val infiniteTransition = rememberInfiniteTransition(label = "candle_glow")
    val glowAlpha by infiniteTransition.animateFloat(
        initialValue = 0.85f,
        targetValue = 1.05f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1800, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "glow_alpha"
    )

    androidx.compose.foundation.Canvas(
        modifier = Modifier.fillMaxSize()
    ) {
        val canvasWidth = size.width
        val radius = 200f * (canvasWidth / 360f)

        // Top-Center Candle Light Halo Radial Gradient
        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(
                    Color(0xFFFFD700).copy(alpha = 0.16f * glowAlpha),
                    Color(0xFFFF8C00).copy(alpha = 0.08f * glowAlpha),
                    Color.Transparent
                ),
                center = Offset(canvasWidth / 2f, 90f),
                radius = radius
            ),
            center = Offset(canvasWidth / 2f, 90f),
            radius = radius
        )
    }
}

