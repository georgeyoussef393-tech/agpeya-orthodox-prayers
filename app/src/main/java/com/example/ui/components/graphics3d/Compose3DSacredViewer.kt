package com.example.ui.components.graphics3d

import android.opengl.GLSurfaceView
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTransformGestures
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
import androidx.compose.material.icons.filled.Church
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.ViewInAr
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.ui.theme.BurgundyDeep
import com.example.ui.theme.GoldLight
import com.example.ui.theme.GoldPrimary

/**
 * Jetpack Compose 3D Engine Component using hardware-accelerated OpenGL ES 3.0.
 * Renders real-time 3D sacred objects (3D Coptic Cross, 3D Censer, 3D Church Bell, 3D Altar Icon)
 * with touch trackball rotation, zoom gestures, GLSL shaders, and specular lighting.
 */
@Composable
fun Compose3DSacredViewer(
    modifier: Modifier = Modifier,
    initialSymbol: Sacred3DSymbolType = Sacred3DSymbolType.COPTIC_CROSS,
    height: Dp = 260.dp,
    showSelectorTabs: Boolean = true,
    showTitleHeader: Boolean = true
) {
    val context = LocalContext.current
    var selectedSymbol by remember { mutableStateOf(initialSymbol) }

    var rotX by remember { mutableFloatStateOf(15f) }
    var rotY by remember { mutableFloatStateOf(25f) }
    var scale by remember { mutableFloatStateOf(1.0f) }

    val renderer = remember { Gl3DSacredRenderer(context, selectedSymbol) }

    // Update renderer when symbol changes
    remember(selectedSymbol) {
        renderer.symbolType = selectedSymbol
        renderer.updateMeshGeometry()
    }

    Card(
        modifier = modifier.testTag("compose_3d_sacred_viewer_card"),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.95f)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, GoldPrimary.copy(alpha = 0.45f))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (showTitleHeader) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(34.dp)
                                .clip(CircleShape)
                                .background(GoldPrimary.copy(alpha = 0.18f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.ViewInAr,
                                contentDescription = "3D Engine",
                                tint = GoldPrimary,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = "محرك المجسمات ثلاثية الأبعاد (OpenGL 3D Engine)",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = GoldPrimary
                            )
                            Text(
                                text = "عرض وتفاعل حقيقي ثلاثي الأبعاد مع الرموز المقدسة",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    IconButton(
                        onClick = {
                            rotX = 15f
                            rotY = 25f
                            scale = 1.0f
                            renderer.rotX = 15f
                            renderer.rotY = 25f
                            renderer.scale = 1.0f
                        },
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = "إعادة التوجيه",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))
            }

            if (showSelectorTabs) {
                val symbols = Sacred3DSymbolType.entries
                ScrollableTabRow(
                    selectedTabIndex = selectedSymbol.ordinal,
                    containerColor = MaterialTheme.colorScheme.surfaceVariant,
                    contentColor = GoldPrimary,
                    edgePadding = 8.dp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                ) {
                    symbols.forEach { sym ->
                        val isSelected = selectedSymbol == sym
                        val label = when (sym) {
                            Sacred3DSymbolType.COPTIC_CROSS -> "صليب قبطي 3D"
                            Sacred3DSymbolType.INCENSE_CENSER -> "مبخرة 3D"
                            Sacred3DSymbolType.CHURCH_BELL -> "جرس كنيسة 3D"
                            Sacred3DSymbolType.ALTAR_ICON_FRAME -> "أيقونة مقدسة 3D"
                        }

                        Tab(
                            selected = isSelected,
                            onClick = { selectedSymbol = sym },
                            text = {
                                Text(
                                    text = label,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                    fontSize = 12.sp
                                )
                            },
                            modifier = Modifier.testTag("tab_3d_symbol_${sym.name}")
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))
            }

            // OpenGL ES 3D View Surface Container
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(height)
                    .clip(RoundedCornerShape(20.dp))
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                BurgundyDeep.copy(alpha = 0.5f),
                                Color.Black.copy(alpha = 0.95f)
                            )
                        )
                    )
                    .pointerInput(Unit) {
                        detectDragGestures { change, dragAmount ->
                            change.consume()
                            val sensitivity = 0.5f
                            rotY += dragAmount.x * sensitivity
                            rotX += dragAmount.y * sensitivity
                            rotX = rotX.coerceIn(-85f, 85f)
                            renderer.rotX = rotX
                            renderer.rotY = rotY
                        }
                    }
                    .pointerInput(Unit) {
                        detectTransformGestures { _, _, zoom, _ ->
                            scale = (scale * zoom).coerceIn(0.5f, 2.5f)
                            renderer.scale = scale
                        }
                    },
                contentAlignment = Alignment.Center
            ) {
                AndroidView(
                    factory = { ctx ->
                        GLSurfaceView(ctx).apply {
                            setEGLContextClientVersion(3) // OpenGL ES 3.0
                            setRenderer(renderer)
                            renderMode = GLSurfaceView.RENDERMODE_CONTINUOUSLY
                        }
                    },
                    update = { view ->
                        renderer.rotX = rotX
                        renderer.rotY = rotY
                        renderer.scale = scale
                        view.requestRender()
                    },
                    modifier = Modifier.fillMaxSize()
                )

                // 3D Gesture Tip overlay badge
                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = Color.Black.copy(alpha = 0.65f),
                    border = androidx.compose.foundation.BorderStroke(1.dp, GoldPrimary.copy(alpha = 0.5f)),
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 12.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.ViewInAr,
                            contentDescription = null,
                            tint = GoldLight,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "اسحب بأصبعك للتدوير 3D • استخدم إصبعين للتكبير والتصغير",
                            style = MaterialTheme.typography.labelSmall,
                            color = GoldLight,
                            fontSize = 11.sp
                        )
                    }
                }
            }
        }
    }
}
