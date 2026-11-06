package com.example.ui.components.graphics3d

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
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
import androidx.compose.material.icons.filled.ViewInAr
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
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
 * Interactive 3D Coptic Cross with real-time 3D perspective rotation,
 * ambient floating motion, specular metallic lighting, and touch drag gestures.
 */
@Composable
fun Interactive3DCopticCross(
    modifier: Modifier = Modifier,
    size: Dp = 180.dp,
    enableTouchRotation: Boolean = true,
    showControls: Boolean = true,
    subtitle: String? = "الصليب القبطي ثلاثي الأبعاد - اسحب للتدوير 3D"
) {
    val coroutineScope = rememberCoroutineScope()
    val density = LocalDensity.current

    // 3D Rotation Angles
    var rotX by remember { mutableFloatStateOf(12f) }
    var rotY by remember { mutableFloatStateOf(20f) }

    val rotXAnim = remember { Animatable(rotX) }
    val rotYAnim = remember { Animatable(rotY) }

    // Ambient floating idle animation
    val infiniteTransition = rememberInfiniteTransition(label = "3d_float")
    val floatOffset by infiniteTransition.animateFloat(
        initialValue = -6f,
        targetValue = 6f,
        animationSpec = infiniteRepeatable(
            animation = tween(2800, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "float"
    )

    val shimmerRot by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(12000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "shimmer"
    )

    Card(
        modifier = modifier,
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.95f)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, GoldPrimary.copy(alpha = 0.4f))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Header bar
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
                            imageVector = Icons.Default.ViewInAr,
                            contentDescription = "3D",
                            tint = GoldPrimary,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "مجسم الصليب القبطي 3D",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = GoldPrimary
                    )
                }

                if (showControls) {
                    IconButton(
                        onClick = {
                            coroutineScope.launch {
                                rotX = 12f
                                rotY = 20f
                                rotXAnim.snapTo(12f)
                                rotYAnim.snapTo(20f)
                            }
                        },
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = "إعادة ضبط الـ 3D",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // 3D Canvas Box
            Box(
                modifier = Modifier
                    .size(size)
                    .clip(RoundedCornerShape(20.dp))
                    .background(
                        Brush.radialGradient(
                            colors = listOf(
                                BurgundyDeep.copy(alpha = 0.35f),
                                Color.Black.copy(alpha = 0.85f)
                            )
                        )
                    )
                    .pointerInput(enableTouchRotation) {
                        if (!enableTouchRotation) return@pointerInput
                        detectDragGestures { change, dragAmount ->
                            change.consume()
                            val sensitivity = 0.45f
                            rotY += dragAmount.x * sensitivity
                            rotX -= dragAmount.y * sensitivity
                            rotX = rotX.coerceIn(-60f, 60f)
                            coroutineScope.launch {
                                rotXAnim.snapTo(rotX)
                                rotYAnim.snapTo(rotY)
                            }
                        }
                    },
                contentAlignment = Alignment.Center
            ) {
                val cameraDistPx = 16f * density.density

                // 3D Perspective Container
                Box(
                    modifier = Modifier
                        .size(size * 0.82f)
                        .graphicsLayer {
                            rotationX = rotXAnim.value + floatOffset
                            rotationY = rotYAnim.value
                            cameraDistance = cameraDistPx
                            shadowElevation = 16.dp.toPx()
                            shape = RoundedCornerShape(12.dp)
                            clip = false
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Canvas(modifier = Modifier.fillMaxSize()) {
                        draw3DCopticCrossCanvas(
                            rotX = rotXAnim.value,
                            rotY = rotYAnim.value,
                            shimmerAngle = shimmerRot
                        )
                    }
                }
            }

            if (subtitle != null) {
                Spacer(modifier = Modifier.height(10.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.AutoAwesome,
                        contentDescription = null,
                        tint = GoldPrimary,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = subtitle,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontSize = 11.sp
                    )
                }
            }
        }
    }
}

/**
 * Draws the 3D Coptic Cross with bevels, central ring, metallic gradient lighting,
 * and 3D depth shadows.
 */
private fun DrawScope.draw3DCopticCrossCanvas(
    rotX: Float,
    rotY: Float,
    shimmerAngle: Float
) {
    val w = size.width
    val h = size.height
    val cx = w / 2f
    val cy = h / 2f

    val armWidth = w * 0.22f
    val armLength = w * 0.38f
    val ringRadius = w * 0.16f

    // Dynamic 3D lighting offset based on rotation angles
    val lightAngleRad = Math.toRadians((rotY + shimmerAngle).toDouble())
    val highlightDx = (cos(lightAngleRad) * 15f).toFloat()
    val highlightDy = (sin(lightAngleRad) * 15f).toFloat()

    // 1. Draw 3D Depth Shadow Layer (extruded back shadow)
    val depthOffset = Offset(
        x = (sin(Math.toRadians(rotY.toDouble())) * 14f).toFloat(),
        y = (cos(Math.toRadians(rotX.toDouble())) * 14f).toFloat()
    )

    val shadowBrush = Brush.radialGradient(
        colors = listOf(Color.Black.copy(alpha = 0.6f), Color.Transparent),
        center = Offset(cx + depthOffset.x, cy + depthOffset.y),
        radius = w * 0.5f
    )
    drawCircle(brush = shadowBrush, radius = w * 0.48f, center = Offset(cx + depthOffset.x, cy + depthOffset.y))

    // 2. Draw 3D Beveled Coptic Cross Arms (Vertical & Horizontal)
    val goldBrushMain = Brush.linearGradient(
        colors = listOf(GoldLight, GoldPrimary, GoldDark, BurgundyDeep),
        start = Offset(cx - armLength + highlightDx, cy - armLength + highlightDy),
        end = Offset(cx + armLength - highlightDx, cy + armLength - highlightDy)
    )

    val goldBevelBrush = Brush.linearGradient(
        colors = listOf(Color.White.copy(alpha = 0.8f), GoldLight, GoldDark, Color.Black.copy(alpha = 0.7f)),
        start = Offset(cx - armLength, cy - armLength),
        end = Offset(cx + armLength, cy + armLength)
    )

    // Vertical Arm
    drawRect(
        brush = goldBrushMain,
        topLeft = Offset(cx - armWidth / 2f, cy - armLength),
        size = Size(armWidth, armLength * 2f)
    )
    drawRect(
        brush = goldBevelBrush,
        topLeft = Offset(cx - armWidth / 2f, cy - armLength),
        size = Size(armWidth, armLength * 2f),
        style = Stroke(width = 3.dp.toPx())
    )

    // Horizontal Arm
    drawRect(
        brush = goldBrushMain,
        topLeft = Offset(cx - armLength, cy - armWidth / 2f),
        size = Size(armLength * 2f, armWidth)
    )
    drawRect(
        brush = goldBevelBrush,
        topLeft = Offset(cx - armLength, cy - armWidth / 2f),
        size = Size(armLength * 2f, armWidth),
        style = Stroke(width = 3.dp.toPx())
    )

    // 3. Four Coptic End Flares (Tri-flared ends on 4 cross tips)
    val flareSize = armWidth * 0.7f
    val flareOffsets = listOf(
        Offset(cx, cy - armLength), // Top
        Offset(cx, cy + armLength), // Bottom
        Offset(cx - armLength, cy), // Left
        Offset(cx + armLength, cy)  // Right
    )

    flareOffsets.forEach { flareCenter ->
        val path = Path().apply {
            moveTo(flareCenter.x - flareSize, flareCenter.y)
            lineTo(flareCenter.x + flareSize, flareCenter.y)
            lineTo(flareCenter.x, flareCenter.y + (if (flareCenter.y < cy) -flareSize else flareSize))
            close()
        }
        drawPath(path = path, brush = goldBrushMain)
        drawPath(path = path, color = GoldLight, style = Stroke(width = 2.dp.toPx()))
    }

    // 4. Central Coptic Ring (Circle of Eternity & Victory)
    drawCircle(
        brush = goldBrushMain,
        radius = ringRadius,
        center = Offset(cx, cy)
    )
    drawCircle(
        brush = goldBevelBrush,
        radius = ringRadius,
        center = Offset(cx, cy),
        style = Stroke(width = 4.dp.toPx())
    )

    // Inner Ring Core
    drawCircle(
        color = BurgundyDeep,
        radius = ringRadius * 0.55f,
        center = Offset(cx, cy)
    )
    drawCircle(
        color = GoldPrimary,
        radius = ringRadius * 0.25f,
        center = Offset(cx, cy)
    )

    // Specular Highlight Spot
    val specularCenter = Offset(cx - ringRadius * 0.3f + highlightDx * 0.2f, cy - ringRadius * 0.3f + highlightDy * 0.2f)
    drawCircle(
        brush = Brush.radialGradient(
            colors = listOf(Color.White, Color.White.copy(alpha = 0.5f), Color.Transparent),
            center = specularCenter,
            radius = ringRadius * 0.4f
        ),
        radius = ringRadius * 0.3f,
        center = specularCenter
    )
}
