package com.example.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.ui.theme.GoldLight
import com.example.ui.theme.GoldPrimary
import kotlin.math.cos
import kotlin.math.sin

/**
 * Authentic Ornate Coptic Orthodox Cross (الصليب القبطي الأرثوذكسي المزخرف).
 * Features:
 * - 4 Flared Flanged Arms with gold filigree inner inlay.
 * - 12 Apostolic Trefoil Pearls (3 pearls on each arm tip symbolizing the Holy Trinity & 12 Apostles).
 * - Central Sacred Halo Ring (حلقة الخلود الشمسية) with 4 diagonal resurrection rays.
 * - Central Coptic Diamond Jewel and core accents.
 */
@Composable
fun CopticCrossCanvas(
    modifier: Modifier = Modifier,
    size: Dp = 36.dp,
    color: Color = GoldPrimary,
    ringColor: Color = GoldLight
) {
    Canvas(modifier = modifier.size(size)) {
        val w = this.size.width
        val h = this.size.height
        val cx = w / 2f
        val cy = h / 2f
        val minDim = minOf(w, h)

        // 1. Four Diagonal Resurrection Rays (45°, 135°, 225°, 315°) between main arms
        val diagRayLength = minDim * 0.31f
        val rayAngles = floatArrayOf(45f, 135f, 225f, 315f)
        for (angleDeg in rayAngles) {
            val rad = Math.toRadians(angleDeg.toDouble())
            val rx = cx + (cos(rad) * diagRayLength).toFloat()
            val ry = cy + (sin(rad) * diagRayLength).toFloat()

            // Ray spoke line
            drawLine(
                color = ringColor.copy(alpha = 0.9f),
                start = Offset(cx, cy),
                end = Offset(rx, ry),
                strokeWidth = minDim * 0.035f,
                cap = StrokeCap.Round
            )
            // Ray tip pearl dot
            drawCircle(
                color = color,
                radius = minDim * 0.026f,
                center = Offset(rx, ry)
            )
        }

        // 2. Central Sacred Halo Ring (حلقة الخلود والنور القبطية)
        val outerHaloRadius = minDim * 0.22f
        val innerHaloRadius = minDim * 0.15f
        drawCircle(
            color = ringColor.copy(alpha = 0.95f),
            radius = outerHaloRadius,
            center = Offset(cx, cy),
            style = Stroke(width = minDim * 0.035f)
        )
        drawCircle(
            color = color.copy(alpha = 0.70f),
            radius = innerHaloRadius,
            center = Offset(cx, cy),
            style = Stroke(width = minDim * 0.020f)
        )

        // 3. Four Flared Flanged Coptic Arms (الأذرع الأربعة المتسعة المزخرفة)
        val innerWidth = minDim * 0.075f   // Width near center
        val outerWidth = minDim * 0.165f   // Width near arm tip
        val armReach = minDim * 0.40f      // Distance to tip

        val angles = floatArrayOf(0f, 90f, 180f, 270f) // Right, Bottom, Left, Top

        for (angleDeg in angles) {
            val rad = Math.toRadians(angleDeg.toDouble())
            val perpRad = rad + Math.PI / 2.0

            val cosA = cos(rad).toFloat()
            val sinA = sin(rad).toFloat()
            val cosP = cos(perpRad).toFloat()
            val sinP = sin(perpRad).toFloat()

            val pInnerStart = Offset(cx + cosA * (minDim * 0.05f), cy + sinA * (minDim * 0.05f))
            val pOuterEnd = Offset(cx + cosA * armReach, cy + sinA * armReach)

            // Calculate 4 corners of the flared arm polygon
            val p1 = Offset(pInnerStart.x + cosP * (innerWidth / 2f), pInnerStart.y + sinP * (innerWidth / 2f))
            val p2 = Offset(pInnerStart.x - cosP * (innerWidth / 2f), pInnerStart.y - sinP * (innerWidth / 2f))
            val p3 = Offset(pOuterEnd.x - cosP * (outerWidth / 2f), pOuterEnd.y - sinP * (outerWidth / 2f))
            val p4 = Offset(pOuterEnd.x + cosP * (outerWidth / 2f), pOuterEnd.y + sinP * (outerWidth / 2f))

            // Main Flared Arm Polygon Path
            val armPath = Path().apply {
                moveTo(p1.x, p1.y)
                lineTo(p2.x, p2.y)
                lineTo(p3.x, p3.y)
                lineTo(p4.x, p4.y)
                close()
            }
            drawPath(path = armPath, color = color)

            // Inner Gold Filigree Inlay Line down the middle of each arm
            drawLine(
                color = ringColor,
                start = Offset(cx + cosA * (minDim * 0.12f), cy + sinA * (minDim * 0.12f)),
                end = Offset(cx + cosA * (armReach - minDim * 0.02f), cy + sinA * (armReach - minDim * 0.02f)),
                strokeWidth = minDim * 0.025f,
                cap = StrokeCap.Round
            )

            // 4. Three Apostolic Trefoil Pearls at the end of each arm (الـ ١٢ رسولاً والثالوث القدوس)
            val tipCenter = Offset(cx + cosA * (armReach + minDim * 0.025f), cy + sinA * (armReach + minDim * 0.025f))
            val tipLeft = Offset(tipCenter.x - cosP * (minDim * 0.058f), tipCenter.y - sinP * (minDim * 0.058f))
            val tipRight = Offset(tipCenter.x + cosP * (minDim * 0.058f), tipCenter.y + sinP * (minDim * 0.058f))

            // Main center tip pearl
            drawCircle(color = ringColor, radius = minDim * 0.042f, center = tipCenter)
            drawCircle(color = color, radius = minDim * 0.026f, center = tipCenter)

            // Side trefoil pearls
            drawCircle(color = color, radius = minDim * 0.034f, center = tipLeft)
            drawCircle(color = color, radius = minDim * 0.034f, center = tipRight)
        }

        // 5. Center Coptic Diamond Jewel & Core Accent
        val diamondRadius = minDim * 0.075f
        val diamondPath = Path().apply {
            moveTo(cx, cy - diamondRadius)
            lineTo(cx + diamondRadius, cy)
            lineTo(cx, cy + diamondRadius)
            lineTo(cx - diamondRadius, cy)
            close()
        }
        drawPath(path = diamondPath, color = ringColor)

        // Core Jewel Pearl
        drawCircle(color = color, radius = minDim * 0.040f, center = Offset(cx, cy))
        drawCircle(color = ringColor, radius = minDim * 0.020f, center = Offset(cx, cy))
    }
}
