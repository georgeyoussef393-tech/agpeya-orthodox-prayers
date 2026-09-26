package com.example

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.NavigationRailItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.alarm.AgpeyaAlarmScheduler
import com.example.data.model.PrayerId
import com.example.localization.AgpeyaStrings
import com.example.localization.AppLanguage
import com.example.service.AgpeyaNotificationHelper
import com.example.ui.animation.AgpeyaMotion
import com.example.ui.components.CopticCrossCanvas
import com.example.ui.components.SpiritualAtmosphereBackdrop
import com.example.ui.components.SubtleCrossWatermark
import com.example.ui.screens.AmbientPrayerScreen
import com.example.ui.screens.AuthOnboardingScreen
import com.example.ui.screens.CopticCalendarScreen
import com.example.ui.screens.PrayersScreen
import com.example.ui.screens.ReportsAndSettingsHostScreen
import com.example.ui.screens.SettingsScreen
import com.example.ui.screens.SpiritualJournalScreen
import com.example.ui.theme.AgpeyaTheme
import com.example.ui.theme.GoldPrimary
import com.example.ui.viewmodel.AgpeyaViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Ensure notification channel is created
        AgpeyaNotificationHelper.createNotificationChannel(this)

        // Reschedule alarms to ensure precision
        AgpeyaAlarmScheduler.rescheduleAllActiveAlarms(this)

        val prayerCodeFromIntent = intent.getStringExtra("EXTRA_PRAYER_CODE")

        setContent {
            AgpeyaTheme {
                AgpeyaApp(initialPrayerCode = prayerCodeFromIntent)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Composable
fun AgpeyaApp(
    initialPrayerCode: String? = null,
    viewModel: AgpeyaViewModel = viewModel()
) {
    val context = LocalContext.current
    val windowSizeClass = calculateWindowSizeClass(activity = context as ComponentActivity)
    val useNavRail = windowSizeClass.widthSizeClass != WindowWidthSizeClass.Compact

    val lang by viewModel.currentLanguage.collectAsState()
    var selectedScreen by rememberSaveable { mutableIntStateOf(0) }

    // Navigation Items Data
    val navItems = listOf(
        Triple(Icons.Default.MenuBook, AgpeyaStrings.tabPrayers(lang), "nav_tab_prayers"),
        Triple(Icons.Default.DateRange, AgpeyaStrings.tabCalendar(lang), "nav_tab_calendar"),
        Triple(Icons.Default.WbSunny, AgpeyaStrings.tabAmbient(lang), "nav_tab_ambient"),
        Triple(Icons.Default.BarChart, AgpeyaStrings.tabReportsAndSettings(lang), "nav_tab_reports_journal"),
        Triple(Icons.Default.Settings, AgpeyaStrings.tabSettings(lang), "nav_tab_settings")
    )

    // Notification permission handling for Android 13+
    var hasNotificationPermission by remember {
        mutableStateOf(
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.POST_NOTIFICATIONS
                ) == PackageManager.PERMISSION_GRANTED
            } else {
                true
            }
        )
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        hasNotificationPermission = isGranted
    }

    LaunchedEffect(Unit) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU && !hasNotificationPermission) {
            permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        }
        // If opened from prayer alarm notification, open reading immediately
        initialPrayerCode?.let { code ->
            viewModel.openPrayerReading(PrayerId.fromCode(code))
        }
    }

    val isAuthCompleted by viewModel.isAuthCompleted.collectAsState()
    val spiritualTheme by viewModel.spiritualTheme.collectAsState()
    val spiritualOpacity by viewModel.spiritualOpacity.collectAsState()
    val isCandleGlowEnabled by viewModel.isCandleGlowEnabled.collectAsState()

    val layoutDirection = if (lang.isRtl) LayoutDirection.Rtl else LayoutDirection.Ltr

    CompositionLocalProvider(LocalLayoutDirection provides layoutDirection) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            Box(modifier = Modifier.fillMaxSize()) {
                // Immersive spiritual backdrop that fills the entire background globally
                SpiritualAtmosphereBackdrop(
                    theme = spiritualTheme,
                    opacity = (spiritualOpacity * 0.70f).coerceAtMost(0.24f),
                    enableCandleGlow = isCandleGlowEnabled,
                    overlayColor = MaterialTheme.colorScheme.background // Adapt to theme
                )

                if (!isAuthCompleted) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        AuthOnboardingScreen(
                            viewModel = viewModel,
                            modifier = Modifier
                                .fillMaxWidth()
                                .widthIn(max = 640.dp)
                                .statusBarsPadding()
                                .windowInsetsPadding(WindowInsets.navigationBars)
                        )
                    }
                } else {
                    Row(modifier = Modifier.fillMaxSize()) {
                        if (useNavRail) {
                            NavigationRail(
                                containerColor = Color.Transparent, // Let the backdrop show through
                                header = {
                                    Spacer(modifier = Modifier.height(20.dp))
                                    CopticCrossCanvas(size = 32.dp)
                                    Spacer(modifier = Modifier.height(20.dp))
                                },
                                modifier = Modifier.testTag("side_navigation_rail")
                            ) {
                                navItems.forEachIndexed { index, (icon, label, tag) ->
                                    NavigationRailItem(
                                        selected = selectedScreen == index,
                                        onClick = { selectedScreen = index },
                                        icon = { Icon(imageVector = icon, contentDescription = label) },
                                        label = { Text(text = label, fontSize = 10.sp) },
                                        colors = NavigationRailItemDefaults.colors(
                                            selectedIconColor = Color.Black,
                                            selectedTextColor = GoldPrimary,
                                            indicatorColor = GoldPrimary,
                                            unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                                            unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                                        ),
                                        modifier = Modifier.testTag(tag)
                                    )
                                }
                            }
                        }

                        Scaffold(
                            modifier = Modifier.weight(1f),
                            containerColor = Color.Transparent, // Essential for backdrop visibility
                            bottomBar = {
                                if (!useNavRail) {
                                    Surface(
                                        color = MaterialTheme.colorScheme.surface.copy(alpha = 0.92f),
                                        tonalElevation = 8.dp,
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .windowInsetsPadding(NavigationBarDefaults.windowInsets)
                                                .padding(top = 4.dp, bottom = 14.dp)
                                        ) {
                                            NavigationBar(
                                                containerColor = Color.Transparent,
                                                tonalElevation = 0.dp,
                                                windowInsets = WindowInsets(0, 0, 0, 0),
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .testTag("bottom_navigation_bar")
                                            ) {
                                                navItems.forEachIndexed { index, (icon, label, tag) ->
                                                    NavigationBarItem(
                                                        selected = selectedScreen == index,
                                                        onClick = { selectedScreen = index },
                                                        icon = { Icon(imageVector = icon, contentDescription = label) },
                                                        label = {
                                                            Text(
                                                                text = label,
                                                                fontWeight = if (selectedScreen == index) FontWeight.Bold else FontWeight.Normal,
                                                                fontSize = 10.sp,
                                                                maxLines = 1
                                                            )
                                                        },
                                                        colors = NavigationBarItemDefaults.colors(
                                                            selectedIconColor = Color.Black,
                                                            selectedTextColor = GoldPrimary,
                                                            indicatorColor = GoldPrimary,
                                                            unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                                                            unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
                                                        ),
                                                        modifier = Modifier.testTag(tag)
                                                    )
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        ) { innerPadding ->
                            Column(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(innerPadding)
                            ) {
                                // Permission request banner if permission is denied on Android 13+
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU && !hasNotificationPermission) {
                                    Card(
                                        shape = RoundedCornerShape(0.dp),
                                        colors = CardDefaults.cardColors(
                                            containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.9f)
                                        ),
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(horizontal = 16.dp, vertical = 10.dp)
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.NotificationsActive,
                                                contentDescription = null,
                                                tint = GoldPrimary,
                                                modifier = Modifier.size(24.dp)
                                            )
                                            Spacer(modifier = Modifier.width(12.dp))
                                            Text(
                                                text = AgpeyaStrings.notificationPermissionRequired(lang),
                                                style = MaterialTheme.typography.bodySmall,
                                                color = MaterialTheme.colorScheme.onPrimaryContainer,
                                                modifier = Modifier.weight(1f),
                                                lineHeight = 16.sp
                                            )
                                            Spacer(modifier = Modifier.width(8.dp))
                                            Button(
                                                onClick = {
                                                    permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                                                },
                                                colors = ButtonDefaults.buttonColors(containerColor = GoldPrimary),
                                                shape = RoundedCornerShape(10.dp),
                                                contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 10.dp, vertical = 4.dp)
                                            ) {
                                                Text(
                                                    text = AgpeyaStrings.allowPermission(lang),
                                                    style = MaterialTheme.typography.labelSmall,
                                                    fontWeight = FontWeight.Bold,
                                                    color = Color.Black
                                                )
                                            }
                                        }
                                    }
                                }

                                // Screen switcher with smooth, consistent spring-based transitions
                                Box(modifier = Modifier.fillMaxSize()) {
                                    AnimatedContent(
                                        targetState = selectedScreen,
                                        transitionSpec = {
                                            val isForward = if (lang.isRtl) (targetState < initialState) else (targetState > initialState)
                                            AgpeyaMotion.directionalSlide(forward = isForward)
                                        },
                                        label = "main_screen_transition",
                                        modifier = Modifier.fillMaxSize()
                                    ) { screen ->
                                        Box(
                                            modifier = Modifier.fillMaxSize(),
                                            contentAlignment = Alignment.TopCenter
                                        ) {
                                            // Constrain width on tablets/foldables for better readability
                                            val screenModifier = if (useNavRail) {
                                                Modifier.fillMaxWidth().widthIn(max = 840.dp)
                                            } else {
                                                Modifier.fillMaxSize()
                                            }

                                            when (screen) {
                                                0 -> PrayersScreen(
                                                    viewModel = viewModel,
                                                    onNavigateToCalendar = { selectedScreen = 1 },
                                                    onNavigateToAmbient = { selectedScreen = 2 },
                                                    onNavigateToJournal = { selectedScreen = 3 },
                                                    onNavigateToSettings = { selectedScreen = 4 },
                                                    modifier = screenModifier
                                                )
                                                1 -> CopticCalendarScreen(
                                                    viewModel = viewModel,
                                                    onNavigateToPrayers = { selectedScreen = 0 },
                                                    modifier = screenModifier
                                                )
                                                2 -> AmbientPrayerScreen(
                                                    viewModel = viewModel,
                                                    modifier = screenModifier
                                                )
                                                3 -> ReportsAndSettingsHostScreen(
                                                    viewModel = viewModel,
                                                    modifier = screenModifier
                                                )
                                                4 -> SettingsScreen(
                                                    viewModel = viewModel,
                                                    modifier = screenModifier
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
