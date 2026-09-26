package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import com.example.ui.animation.AgpeyaMotion
import com.example.ui.animation.pressBounce
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material.icons.filled.Translate
import androidx.compose.material.icons.outlined.Bedtime
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Timer
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.localization.AgpeyaStrings
import com.example.localization.AppLanguage
import com.example.ui.theme.GoldLight
import com.example.ui.theme.GoldPrimary
import com.example.ui.viewmodel.AgpeyaViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun MultilingualAndNotificationSettings(
    viewModel: AgpeyaViewModel,
    modifier: Modifier = Modifier
) {
    val lang by viewModel.currentLanguage.collectAsState()
    val isBilingual by viewModel.isBilingualEnabled.collectAsState()
    val secondaryLang by viewModel.secondaryLanguage.collectAsState()
    val isPhoneticGuide by viewModel.isPhoneticGuideEnabled.collectAsState()

    val isAutoTimezone by viewModel.isAutoTimezoneSyncEnabled.collectAsState()
    val selectedTzId by viewModel.selectedTimezoneId.collectAsState()
    val leadTimeMins by viewModel.notificationLeadTimeMinutes.collectAsState()
    val isQuietHours by viewModel.isQuietHoursEnabled.collectAsState()
    val quietStart by viewModel.quietHoursStartHour.collectAsState()
    val quietEnd by viewModel.quietHoursEndHour.collectAsState()

    val scope = rememberCoroutineScope()
    var isSyncingAlarms by remember { mutableStateOf(false) }
    var syncSuccessMessage by remember { mutableStateOf<String?>(null) }
    var showTimezoneMenu by remember { mutableStateOf(false) }

    val isArabic = lang == AppLanguage.ARABIC || lang == AppLanguage.SYRIAN_ARABIC || lang == AppLanguage.SYRIAC

    // Popular Spiritual & Global Timezone presets
    val timezonesList = remember {
        listOf(
            TimeZone.getDefault().id to (if (isArabic) "التوقيت المحلي التلقائي للجهاز" else "Local Device Default Timezone"),
            "Africa/Cairo" to (if (isArabic) "القاهرة / المقر البابوي (مصر UTC+2/+3)" else "Cairo / Coptic Patriarchate (UTC+2/+3)"),
            "Asia/Jerusalem" to (if (isArabic) "القدس الشريف / الأراضي المقدسة" else "Jerusalem / Holy Land (UTC+2/+3)"),
            "Europe/London" to (if (isArabic) "لندن / غرينتش (UTC+0)" else "London / Greenwich (UTC+0)"),
            "America/New_York" to (if (isArabic) "نيويورك / التوقيت الشرقي (EST UTC-5)" else "New York / Eastern Time (UTC-5)"),
            "America/Los_Angeles" to (if (isArabic) "لوس أنجلوس / التوقيت الهادي (PST UTC-8)" else "Los Angeles / Pacific Time (UTC-8)"),
            "Australia/Sydney" to (if (isArabic) "سيدني / أستراليا (AEST UTC+10)" else "Sydney / Australian Eastern (UTC+10)")
        )
    }

    Column(modifier = modifier.fillMaxWidth()) {

        // ==========================================
        // 1. MULTILINGUAL SUPPORT & DUAL-LANGUAGE CARD
        // ==========================================
        Text(
            text = if (isArabic) "التحكم في لغة التطبيق بالكامل والدعم متعدد اللغات" else "Global App Language & Multilingual Control",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = GoldPrimary,
            modifier = Modifier.padding(bottom = 4.dp)
        )
        Text(
            text = if (isArabic) "التحكم في لغة البرنامج بالكامل: الواجهات، نصوص الصلوات، المزامير، القراءات الإنجيلية، والتبويبات. اختر اللغة الأساسية وعرض النصوص بلغتين متوازيتين" else "Control language for the entire app: UI, prayers, psalms, gospels, and tabs. Choose primary language and configure parallel dual-language reading",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(bottom = 12.dp)
        )

        Card(
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            modifier = Modifier
                .fillMaxWidth()
                .testTag("multilingual_support_card")
        ) {
            Column(modifier = Modifier.padding(16.dp)) {

                // Primary App Language Picker Grid / Flow
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(
                        imageVector = Icons.Default.Language,
                        contentDescription = null,
                        tint = GoldPrimary,
                        modifier = Modifier.size(22.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = if (isArabic) "اللغة الرئيسية للتطبيق بالكامل" else "Primary App-Wide Language",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    AppLanguage.entries.forEach { appLang ->
                        val isSelected = appLang == lang
                        val chipBg = if (isSelected) GoldPrimary.copy(alpha = 0.18f) else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                        val chipBorderColor = if (isSelected) GoldPrimary else MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .background(chipBg)
                                .border(1.dp, chipBorderColor, RoundedCornerShape(12.dp))
                                .clickable { viewModel.setLanguage(appLang) }
                                .padding(horizontal = 12.dp, vertical = 8.dp)
                                .testTag("lang_chip_${appLang.code}")
                        ) {
                            Text(
                                text = "${appLang.flagEmoji}  ${appLang.nativeName}",
                                style = MaterialTheme.typography.bodySmall,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                color = if (isSelected) GoldPrimary else MaterialTheme.colorScheme.onSurface
                            )
                            if (isSelected) {
                                Spacer(modifier = Modifier.width(6.dp))
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = "Selected",
                                    tint = GoldPrimary,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
                HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f))
                Spacer(modifier = Modifier.height(12.dp))

                // Bilingual Reading Switch
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp))
                        .clickable { viewModel.setBilingualEnabled(!isBilingual) }
                        .padding(vertical = 6.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Translate,
                            contentDescription = null,
                            tint = if (isBilingual) GoldPrimary else MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = if (isArabic) "عرض القراءة بلغتين متوازيتين" else "Parallel Bilingual Reading Mode",
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = if (isArabic) "إظهار النص القبطي أو الإنجليزي بجانب اللغة الرئيسية أثناء الصلاة" else "Display Coptic or English text alongside primary language during prayer",
                                style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    Switch(
                        checked = isBilingual,
                        onCheckedChange = { viewModel.setBilingualEnabled(it) },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = Color.Black,
                            checkedTrackColor = GoldPrimary
                        ),
                        modifier = Modifier.testTag("bilingual_switch")
                    )
                }

                // Secondary Language Dropdown when Bilingual is Enabled
                AnimatedVisibility(
                    visible = isBilingual,
                    enter = AgpeyaMotion.expandSpring(),
                    exit = AgpeyaMotion.shrinkSpring()
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 10.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f))
                            .padding(12.dp)
                    ) {
                        Text(
                            text = if (isArabic) "اللغة الثانوية الموازية للنص" else "Secondary Parallel Language",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold,
                            color = GoldPrimary
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        FlowRow(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            AppLanguage.entries.forEach { secLang ->
                                val isSecSelected = secLang == secondaryLang
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(10.dp))
                                        .background(if (isSecSelected) GoldPrimary else MaterialTheme.colorScheme.surface)
                                        .border(
                                            width = if (isSecSelected) 1.5.dp else 1.dp,
                                            color = if (isSecSelected) GoldPrimary else MaterialTheme.colorScheme.outlineVariant,
                                            shape = RoundedCornerShape(10.dp)
                                        )
                                        .clickable { viewModel.setSecondaryLanguage(secLang) }
                                        .padding(horizontal = 12.dp, vertical = 8.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = "${secLang.flagEmoji}  ${secLang.nativeName}",
                                        style = MaterialTheme.typography.labelSmall,
                                        fontWeight = FontWeight.Bold,
                                        color = if (isSecSelected) Color.Black else MaterialTheme.colorScheme.onSurface
                                    )
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Phonetic Pronunciation Guide Switch
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp))
                        .clickable { viewModel.setPhoneticGuideEnabled(!isPhoneticGuide) }
                        .padding(vertical = 6.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Public,
                            contentDescription = null,
                            tint = if (isPhoneticGuide) GoldPrimary else MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = if (isArabic) "دليل النطق الصوتي للألحان القبطية" else "Coptic Phonetic Pronunciation Guide",
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Medium,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = if (isArabic) "كتابة الألحان بالحروف العربية/الإنجليزية لتسهيل المتابعة" else "Show phonetic transliteration for Coptic chants and responses",
                                style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    Switch(
                        checked = isPhoneticGuide,
                        onCheckedChange = { viewModel.setPhoneticGuideEnabled(it) },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = Color.Black,
                            checkedTrackColor = GoldPrimary
                        )
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // ==========================================
        // 2. LOCAL TIMEZONE & NOTIFICATION PREFERENCES CARD
        // ==========================================
        Text(
            text = if (isArabic) "تخصيص التوقيت المحلي والتنبيهات الذكية" else "Local Timezone & Notification Preferences",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = GoldPrimary,
            modifier = Modifier.padding(bottom = 4.dp)
        )
        Text(
            text = if (isArabic) "ضبط مواعيد صلوات السواعي المحددة حسب منطقتك الزمنية بدقة وتعديل تنبيهاتها" else "Custom time offset, lead-time alerts, and quiet hours for your local timezone",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(bottom = 12.dp)
        )

        Card(
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            modifier = Modifier
                .fillMaxWidth()
                .testTag("timezone_notification_card")
        ) {
            Column(modifier = Modifier.padding(16.dp)) {

                // Current Detected Local Timezone Header Banner
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(14.dp))
                        .background(GoldPrimary.copy(alpha = 0.12f))
                        .padding(14.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Schedule,
                        contentDescription = null,
                        tint = GoldPrimary,
                        modifier = Modifier.size(28.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = if (isArabic) "التوقيت المحلي المكتشف للجهاز" else "Detected Local Device Timezone",
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = viewModel.getCurrentTimezoneInfo(),
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Bold,
                            color = GoldLight
                        )
                        val currentTimeFormatted = remember {
                            val sdf = SimpleDateFormat("hh:mm a z", Locale.getDefault())
                            sdf.format(Date())
                        }
                        Text(
                            text = "${if (isArabic) "الساعة الآن:" else "Local Time:"} $currentTimeFormatted",
                            style = MaterialTheme.typography.labelSmall,
                            color = GoldPrimary,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Auto-Detect Local Timezone Switch
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp))
                        .clickable { viewModel.setAutoTimezoneSyncEnabled(!isAutoTimezone) }
                        .padding(vertical = 6.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Sync,
                            contentDescription = null,
                            tint = if (isAutoTimezone) GoldPrimary else MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = if (isArabic) "مزامنة التوقيت المحلي التلقائي" else "Auto-Sync with Local Device Timezone",
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = if (isArabic) "تعديل مواعيد التنبيهات تلقائياً عند السفر أو تغيير المنطقة الزمنية" else "Recalibrate alarm schedules automatically when travelling across timezones",
                                style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    Switch(
                        checked = isAutoTimezone,
                        onCheckedChange = { viewModel.setAutoTimezoneSyncEnabled(it) },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = Color.Black,
                            checkedTrackColor = GoldPrimary
                        ),
                        modifier = Modifier.testTag("auto_timezone_switch")
                    )
                }

                // Custom Timezone Override Selector Dropdown
                AnimatedVisibility(
                    visible = !isAutoTimezone,
                    enter = AgpeyaMotion.expandSpring(),
                    exit = AgpeyaMotion.shrinkSpring()
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 10.dp)
                    ) {
                        OutlinedButton(
                            onClick = { showTimezoneMenu = true },
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Icon(
                                imageVector = Icons.Default.Public,
                                contentDescription = null,
                                tint = GoldPrimary,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            val currentTzName = timezonesList.find { it.first == selectedTzId }?.second ?: selectedTzId
                            Text(
                                text = currentTzName,
                                style = MaterialTheme.typography.bodySmall,
                                fontWeight = FontWeight.Bold,
                                color = GoldLight,
                                modifier = Modifier.weight(1f)
                            )
                        }

                        DropdownMenu(
                            expanded = showTimezoneMenu,
                            onDismissRequest = { showTimezoneMenu = false }
                        ) {
                            timezonesList.forEach { (tzId, label) ->
                                DropdownMenuItem(
                                    text = {
                                        Text(
                                            text = label,
                                            fontWeight = if (selectedTzId == tzId) FontWeight.Bold else FontWeight.Normal,
                                            color = if (selectedTzId == tzId) GoldPrimary else MaterialTheme.colorScheme.onSurface
                                        )
                                    },
                                    onClick = {
                                        viewModel.setSelectedTimezoneId(tzId)
                                        showTimezoneMenu = false
                                    }
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
                HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f))
                Spacer(modifier = Modifier.height(14.dp))

                // Lead-Time Reminder Customization
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Timer,
                        contentDescription = null,
                        tint = GoldPrimary,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = if (isArabic) "التنبيه المسبق قبل موعد الساعة" else "Lead-Time Prayer Reminder",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = if (isArabic) "إرسال إشعار تذكير مسبق للاستعداد للصلاة" else "Receive an advance alert before the exact canonical hour",
                            style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    listOf(
                        0 to (if (isArabic) "في الموعد" else "At Hour"),
                        5 to (if (isArabic) "قبل ٥ د" else "5 min prior"),
                        10 to (if (isArabic) "قبل ١٠ د" else "10 min prior"),
                        15 to (if (isArabic) "قبل ١٥ د" else "15 min prior")
                    ).forEach { (mins, label) ->
                        val isLeadSelected = leadTimeMins == mins
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(10.dp))
                                .background(if (isLeadSelected) GoldPrimary else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
                                .border(
                                    width = if (isLeadSelected) 1.5.dp else 1.dp,
                                    color = if (isLeadSelected) GoldPrimary else MaterialTheme.colorScheme.outlineVariant,
                                    shape = RoundedCornerShape(10.dp)
                                )
                                .clickable { viewModel.setNotificationLeadTimeMinutes(mins) }
                                .padding(vertical = 8.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = label,
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = if (isLeadSelected) Color.Black else MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
                HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f))
                Spacer(modifier = Modifier.height(14.dp))

                // Quiet Hours / Do Not Disturb Window
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp))
                        .clickable { viewModel.setQuietHoursEnabled(!isQuietHours) }
                        .padding(vertical = 4.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Bedtime,
                            contentDescription = null,
                            tint = if (isQuietHours) GoldPrimary else MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = if (isArabic) "ساعات الهدوء والسكينة (أثناء النوم)" else "Quiet Sleep Hours (Do Not Disturb)",
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = if (isArabic) "كتم صوت الاهتزاز والتنبيهات المرتفعة من $quietStart:00 إلى $quietEnd:00" else "Mute loud alerts between $quietStart:00 and $quietEnd:00",
                                style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    Switch(
                        checked = isQuietHours,
                        onCheckedChange = { viewModel.setQuietHoursEnabled(it) },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = Color.Black,
                            checkedTrackColor = GoldPrimary
                        ),
                        modifier = Modifier.testTag("quiet_hours_switch")
                    )
                }

                Spacer(modifier = Modifier.height(18.dp))

                // Action Buttons: Sync Alarms with Local Timezone & Test Notification
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Button(
                        onClick = {
                            scope.launch {
                                isSyncingAlarms = true
                                viewModel.syncAlarmsWithLocalTimezone()
                                delay(600)
                                isSyncingAlarms = false
                                syncSuccessMessage = if (isArabic) "تم تحديث كافة المواعيد بالتوقيت المحلي!" else "Alarms synchronized with local timezone!"
                                delay(2500)
                                syncSuccessMessage = null
                            }
                        },
                        enabled = !isSyncingAlarms,
                        colors = ButtonDefaults.buttonColors(containerColor = GoldPrimary),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .weight(1.3f)
                            .testTag("sync_timezone_alarms_btn")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Sync,
                            contentDescription = null,
                            tint = Color.Black,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = if (isArabic) "إعادة ضبط بالتوقيت المحلي" else "Sync Local Time Alarms",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
                        )
                    }

                    OutlinedButton(
                        onClick = { viewModel.testNotificationNow() },
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .weight(1f)
                            .testTag("test_timezone_notification_btn")
                    ) {
                        Icon(
                            imageVector = Icons.Default.NotificationsActive,
                            contentDescription = null,
                            tint = GoldPrimary,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = if (isArabic) "تنبيه تجريبي" else "Test Alert",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                // Toast/Status feedback banner
                syncSuccessMessage?.let { msg ->
                    Spacer(modifier = Modifier.height(10.dp))
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(10.dp))
                            .background(Color(0xFF1E3A2F))
                            .padding(vertical = 8.dp, horizontal = 12.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = null,
                                tint = Color(0xFF4CAF50),
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = msg,
                                style = MaterialTheme.typography.labelMedium,
                                color = Color(0xFF4CAF50),
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }
    }
}
