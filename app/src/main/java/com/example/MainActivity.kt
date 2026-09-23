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
import androidx.compose.foundation.layout.width
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
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
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
import com.example.ui.components.SubtleCrossWatermark
import com.example.ui.screens.AmbientPrayerScreen
import com.example.ui.screens.AuthOnboardingScreen
import com.example.ui.screens.CopticCalendarScreen
import com.example.ui.screens.PrayersScreen
import com.example.ui.screens.ReportsAndSettingsHostScreen
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

@Composable
fun AgpeyaApp(
    initialPrayerCode: String? = null,
    viewModel: AgpeyaViewModel = viewModel()
) {
    val context = LocalContext.current
    val lang by viewModel.currentLanguage.collectAsState()
    var selectedScreen by rememberSaveable { mutableIntStateOf(0) }

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

    val layoutDirection = if (lang.isRtl) LayoutDirection.Rtl else LayoutDirection.Ltr

    CompositionLocalProvider(LocalLayoutDirection provides layoutDirection) {
        if (!isAuthCompleted) {
            AuthOnboardingScreen(viewModel = viewModel)
        } else {
            Scaffold(
                modifier = Modifier.fillMaxSize(),
                bottomBar = {
                    NavigationBar(
                        containerColor = MaterialTheme.colorScheme.surface,
                        tonalElevation = 8.dp,
                        windowInsets = NavigationBarDefaults.windowInsets,
                        modifier = Modifier.testTag("bottom_navigation_bar")
                    ) {
                        // 0: Prayers
                        NavigationBarItem(
                            selected = selectedScreen == 0,
                            onClick = { selectedScreen = 0 },
                            icon = {
                                Icon(
                                    imageVector = Icons.Default.MenuBook,
                                    contentDescription = AgpeyaStrings.tabPrayers(lang)
                                )
                            },
                            label = {
                                Text(
                                    text = AgpeyaStrings.tabPrayers(lang),
                                    fontWeight = if (selectedScreen == 0) FontWeight.Bold else FontWeight.Normal,
                                    fontSize = 11.sp,
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
                            modifier = Modifier.testTag("nav_tab_prayers")
                        )

                        // 1: Coptic Calendar & Synaxarium
                        NavigationBarItem(
                            selected = selectedScreen == 1,
                            onClick = { selectedScreen = 1 },
                            icon = {
                                Icon(
                                    imageVector = Icons.Default.DateRange,
                                    contentDescription = AgpeyaStrings.tabCalendar(lang)
                                )
                            },
                            label = {
                                Text(
                                    text = AgpeyaStrings.tabCalendar(lang),
                                    fontWeight = if (selectedScreen == 1) FontWeight.Bold else FontWeight.Normal,
                                    fontSize = 11.sp,
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
                            modifier = Modifier.testTag("nav_tab_calendar")
                        )

                        // 2: Ambient Prayer & Candle Sanctuary
                        NavigationBarItem(
                            selected = selectedScreen == 2,
                            onClick = { selectedScreen = 2 },
                            icon = {
                                Icon(
                                    imageVector = Icons.Default.WbSunny,
                                    contentDescription = AgpeyaStrings.tabAmbient(lang)
                                )
                            },
                            label = {
                                Text(
                                    text = AgpeyaStrings.tabAmbient(lang),
                                    fontWeight = if (selectedScreen == 2) FontWeight.Bold else FontWeight.Normal,
                                    fontSize = 11.sp,
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
                            modifier = Modifier.testTag("nav_tab_ambient")
                        )

                        // 3: Spiritual Journal & Confession Notes
                        NavigationBarItem(
                            selected = selectedScreen == 3,
                            onClick = { selectedScreen = 3 },
                            icon = {
                                Icon(
                                    imageVector = Icons.Default.Edit,
                                    contentDescription = AgpeyaStrings.tabJournal(lang)
                                )
                            },
                            label = {
                                Text(
                                    text = AgpeyaStrings.tabJournal(lang),
                                    fontWeight = if (selectedScreen == 3) FontWeight.Bold else FontWeight.Normal,
                                    fontSize = 11.sp,
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
                            modifier = Modifier.testTag("nav_tab_journal")
                        )

                        // 4: Reports, Alarms & Settings Host
                        NavigationBarItem(
                            selected = selectedScreen == 4,
                            onClick = { selectedScreen = 4 },
                            icon = {
                                Icon(
                                    imageVector = Icons.Default.BarChart,
                                    contentDescription = AgpeyaStrings.tabReportsAndSettings(lang)
                                )
                            },
                            label = {
                                Text(
                                    text = AgpeyaStrings.tabReports(lang),
                                    fontWeight = if (selectedScreen == 4) FontWeight.Bold else FontWeight.Normal,
                                    fontSize = 11.sp,
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
                            modifier = Modifier.testTag("nav_tab_reports_settings")
                        )
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
                                containerColor = MaterialTheme.colorScheme.primaryContainer
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

                    // Screen switcher
                    when (selectedScreen) {
                        0 -> PrayersScreen(
                            viewModel = viewModel,
                            onNavigateToCalendar = { selectedScreen = 1 },
                            onNavigateToAmbient = { selectedScreen = 2 },
                            onNavigateToJournal = { selectedScreen = 3 }
                        )
                        1 -> CopticCalendarScreen(
                            viewModel = viewModel,
                            onNavigateToPrayers = { selectedScreen = 0 }
                        )
                        2 -> AmbientPrayerScreen(
                            viewModel = viewModel
                        )
                        3 -> SpiritualJournalScreen(
                            viewModel = viewModel
                        )
                        4 -> ReportsAndSettingsHostScreen(
                            viewModel = viewModel
                        )
                    }
                }
            }
        }
    }
}
