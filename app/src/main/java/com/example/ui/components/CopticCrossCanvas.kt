package com.example.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.ui.theme.GoldLight
import com.example.ui.theme.GoldPrimary

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
        val centerX = w / 2f
        val centerY = h / 2f
        val stroke = w * 0.08f

        // Central Ring / Halo
        drawCircle(
            color = ringColor.copy(alpha = 0.85f),
            radius = w * 0.22f,
            center = Offset(centerX, centerY),
            style = Stroke(width = stroke * 0.8f)
        )

        // Vertical Bar
        drawLine(
            color = color,
            start = Offset(centerX, h * 0.08f),
            end = Offset(centerX, h * 0.92f),
            strokeWidth = stroke,
            cap = StrokeCap.Round
        )

        // Horizontal Bar
        drawLine(
            color = color,
            start = Offset(w * 0.08f, centerY),
            end = Offset(w * 0.92f, centerY),
            strokeWidth = stroke,
            cap = StrokeCap.Round
        )

        // 3-point Trefoil ends on 4 tips (symbolizing the Holy Trinity)
        val tipRadius = w * 0.045f
        val offsetTip = w * 0.07f

        // Top arm tips
        drawCircle(color, radius = tipRadius, center = Offset(centerX, h * 0.08f))
        drawCircle(color, radius = tipRadius * 0.8f, center = Offset(centerX - offsetTip, h * 0.11f))
        drawCircle(color, radius = tipRadius * 0.8f, center = Offset(centerX + offsetTip, h * 0.11f))

        // Bottom arm tips
        drawCircle(color, radius = tipRadius, center = Offset(centerX, h * 0.92f))
        drawCircle(color, radius = tipRadius * 0.8f, center = Offset(centerX - offsetTip, h * 0.89f))
        drawCircle(color, radius = tipRadius * 0.8f, center = Offset(centerX + offsetTip, h * 0.89f))

        // Left arm tips
        drawCircle(color, radius = tipRadius, center = Offset(w * 0.08f, centerY))
        drawCircle(color, radius = tipRadius * 0.8f, center = Offset(w * 0.11f, centerY - offsetTip))
        drawCircle(color, radius = tipRadius * 0.8f, center = Offset(w * 0.11f, centerY + offsetTip))

        // Right arm tips
        drawCircle(color, radius = tipRadius, center = Offset(w * 0.92f, centerY))
        drawCircle(color, radius = tipRadius * 0.8f, center = Offset(w * 0.89f, centerY - offsetTip))
        drawCircle(color, radius = tipRadius * 0.8f, center = Offset(w * 0.89f, centerY + offsetTip))

        // Center jewel dot
        drawCircle(color = ringColor, radius = stroke * 0.8f, center = Offset(centerX, centerY))
    }
}
