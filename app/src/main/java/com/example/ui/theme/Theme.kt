package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val ChristianDarkColorScheme = darkColorScheme(
    primary = GoldPrimary,
    onPrimary = NavyMidnight,
    primaryContainer = GoldContainerDark,
    onPrimaryContainer = GoldLight,
    secondary = BurgundyLight,
    onSecondary = Color.White,
    secondaryContainer = BurgundyDeep,
    onSecondaryContainer = Color(0xFFFFD9DF),
    tertiary = GoldLight,
    onTertiary = NavyMidnight,
    background = NavyMidnight,
    onBackground = Color(0xFFF1F5F9),
    surface = NavyDeep,
    onSurface = Color(0xFFF1F5F9),
    surfaceVariant = NavySurface,
    onSurfaceVariant = Color(0xFFCBD5E1),
    outline = NavyBorder,
    outlineVariant = Color(0xFF1E293B)
)

private val ChristianLightColorScheme = lightColorScheme(
    primary = DeepGold,
    onPrimary = Color.White,
    primaryContainer = GoldContainerLight,
    onPrimaryContainer = Color(0xFF4A3800),
    secondary = BurgundyPrimary,
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFFFD9DF),
    onSecondaryContainer = BurgundyDeep,
    tertiary = OrthodoxBlue,
    onTertiary = Color.White,
    background = ParchmentCanvas,
    onBackground = Color(0xFF1C1917),
    surface = ParchmentSurface,
    onSurface = Color(0xFF1C1917),
    surfaceVariant = ParchmentCard,
    onSurfaceVariant = Color(0xFF44403C),
    outline = ParchmentBorder,
    outlineVariant = Color(0xFFE7E0D3)
)

@Composable
fun AgpeyaTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    val colorScheme = if (darkTheme) ChristianDarkColorScheme else ChristianLightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}

// Backward compatibility alias
@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    AgpeyaTheme(darkTheme = darkTheme, content = content)
}
