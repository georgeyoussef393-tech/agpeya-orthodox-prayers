package com.example.ui.components.graphics3d

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.spring
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

/**
 * Adds an interactive 3D perspective tilt effect to any Composable on touch/press.
 * As the user presses or drags over the card, it tilts dynamically in 3D space
 * toward the touch coordinate and springs back upon release.
 */
fun Modifier.card3dTilt(
    maxTiltDegrees: Float = 12f,
    cameraDistanceDp: Dp = 12.dp
): Modifier = composed {
    val scope = rememberCoroutineScope()
    val density = LocalDensity.current

    val rotX = remember { Animatable(0f) }
    val rotY = remember { Animatable(0f) }

    val cameraDistPx = cameraDistanceDp.value * density.density

    this
        .graphicsLayer {
            rotationX = rotX.value
            rotationY = rotY.value
            cameraDistance = cameraDistPx
        }
        .pointerInput(Unit) {
            detectTapGestures(
                onPress = { offset ->
                    val width = size.width.toFloat()
                    val height = size.height.toFloat()

                    val normX = (offset.x / width - 0.5f) * 2f // -1.0 to +1.0
                    val normY = (offset.y / height - 0.5f) * 2f // -1.0 to +1.0

                    val targetRotX = -normY * maxTiltDegrees
                    val targetRotY = normX * maxTiltDegrees

                    scope.launch {
                        rotX.animateTo(targetRotX, spring(dampingRatio = 0.5f, stiffness = 600f))
                        rotY.animateTo(targetRotY, spring(dampingRatio = 0.5f, stiffness = 600f))
                    }

                    tryAwaitRelease()

                    scope.launch {
                        rotX.animateTo(0f, spring(dampingRatio = 0.6f, stiffness = 400f))
                        rotY.animateTo(0f, spring(dampingRatio = 0.6f, stiffness = 400f))
                    }
                }
            )
        }
}
