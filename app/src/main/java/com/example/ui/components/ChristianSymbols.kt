package com.example.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.GoldPrimary

/**
 * Subtle Coptic / Traditional Cross Watermark Icon
 */
@Composable
fun SubtleCrossWatermark(
    modifier: Modifier = Modifier,
    size: Dp = 28.dp,
    tint: Color = GoldPrimary,
    alpha: Float = 0.25f
) {
    Canvas(
        modifier = modifier
            .size(size)
            .alpha(alpha)
    ) {
        val w = this.size.width
        val h = this.size.height
        val strokeWidth = w * 0.12f

        // Vertical Beam
        drawLine(
            color = tint,
            start = androidx.compose.ui.geometry.Offset(w / 2f, 0f),
            end = androidx.compose.ui.geometry.Offset(w / 2f, h),
            strokeWidth = strokeWidth
        )

        // Horizontal Beam
        drawLine(
            color = tint,
            start = androidx.compose.ui.geometry.Offset(0f, h * 0.35f),
            end = androidx.compose.ui.geometry.Offset(w, h * 0.35f),
            strokeWidth = strokeWidth
        )

        // 4 Subtle Coptic Cross Corner Accents
        val r = w * 0.10f
        drawCircle(color = tint, radius = r, center = androidx.compose.ui.geometry.Offset(w / 2f, 0f))
        drawCircle(color = tint, radius = r, center = androidx.compose.ui.geometry.Offset(w / 2f, h))
        drawCircle(color = tint, radius = r, center = androidx.compose.ui.geometry.Offset(0f, h * 0.35f))
        drawCircle(color = tint, radius = r, center = androidx.compose.ui.geometry.Offset(w, h * 0.35f))
    }
}

/**
 * Subtle Ichthys Fish (IXΘΥΣ) Section Divider
 */
@Composable
fun IchthysDivider(
    modifier: Modifier = Modifier,
    color: Color = GoldPrimary.copy(alpha = 0.3f)
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp)
    ) {
        Canvas(
            modifier = Modifier
                .weight(1f)
                .height(1.dp)
                .padding(horizontal = 16.dp)
        ) {
            drawLine(
                color = color,
                start = androidx.compose.ui.geometry.Offset(0f, 0f),
                end = androidx.compose.ui.geometry.Offset(size.width, 0f),
                strokeWidth = 1f
            )
        }

        // Ichthys Fish Vector Canvas
        Canvas(
            modifier = Modifier.size(width = 32.dp, height = 18.dp)
        ) {
            val w = size.width
            val h = size.height

            val path = Path().apply {
                // Top Arc
                moveTo(w * 0.1f, h * 0.5f)
                cubicTo(w * 0.35f, 0f, w * 0.75f, 0f, w * 0.95f, h * 0.8f)
                // Bottom Arc
                moveTo(w * 0.1f, h * 0.5f)
                cubicTo(w * 0.35f, h, w * 0.75f, h, w * 0.95f, h * 0.2f)
            }

            drawPath(
                path = path,
                color = color,
                style = Stroke(width = 2.dp.toPx())
            )
        }

        Canvas(
            modifier = Modifier
                .weight(1f)
                .height(1.dp)
                .padding(horizontal = 16.dp)
        ) {
            drawLine(
                color = color,
                start = androidx.compose.ui.geometry.Offset(0f, 0f),
                end = androidx.compose.ui.geometry.Offset(size.width, 0f),
                strokeWidth = 1f
            )
        }
    }
}

/**
 * Peaceful Dove (🕊️) Badge for spiritual milestone cards & titles
 */
@Composable
fun PeaceDoveBadge(
    text: String,
    modifier: Modifier = Modifier,
    containerColor: Color = GoldPrimary.copy(alpha = 0.12f),
    contentColor: Color = GoldPrimary
) {
    Surface(
        shape = RoundedCornerShape(16.dp),
        color = containerColor,
        modifier = modifier
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
        ) {
            Text(
                text = "🕊️",
                fontSize = 12.sp
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = text,
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Bold,
                color = contentColor
            )
        }
    }
}
