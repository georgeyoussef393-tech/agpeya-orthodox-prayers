package com.example.ui.screens

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Build
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Church
import androidx.compose.material.icons.filled.CloudSync
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Devices
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.FormatSize
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PictureAsPdf
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Stop
import com.example.ui.components.OrthodoxGroundedSearchDialog
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material.icons.filled.VolumeOff
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material.icons.outlined.AccessTime
import androidx.compose.material.icons.outlined.Vibration
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.audio.SpiritualSound
import com.example.data.model.PrayerAlarmSetting
import com.example.data.model.PrayerId
import com.example.data.sync.SyncStatus
import com.example.localization.AgpeyaStrings
import com.example.localization.AppLanguage
import com.example.report.PrayerPdfExporter
import com.example.ui.components.CopticCrossCanvas
import com.example.ui.components.MultilingualAndNotificationSettings
import com.example.ui.components.SpiritualSoundPickerDialog
import com.example.ui.components.TimePickerDialog
import com.example.ui.theme.BurgundyDeep
import com.example.ui.theme.GoldLight
import com.example.ui.theme.GoldPrimary
import com.example.ui.viewmodel.AgpeyaViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.aspectRatio
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import androidx.compose.material.icons.filled.Backup
import androidx.compose.material.icons.filled.FileDownload
import androidx.compose.material.icons.filled.FileUpload
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Wallpaper
import androidx.compose.material.icons.filled.WbIncandescent
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import com.example.data.model.SpiritualBackgroundTheme

@Composable
fun SettingsScreen(
    viewModel: AgpeyaViewModel,
    modifier: Modifier = Modifier
) {
    val lang by viewModel.currentLanguage.collectAsState()
    val alarmSettings by viewModel.alarmSettings.collectAsState()
    val playingSoundId by viewModel.playingSoundId.collectAsState()
    val syncStatus by viewModel.syncStatus.collectAsState()
    val syncKey by viewModel.syncKey.collectAsState()
    val lastSyncTime by viewModel.lastSyncTime.collectAsState()
    val isRealtimeSyncActive by viewModel.isRealtimeSyncActive.collectAsState()
    val userEmail by viewModel.userEmail.collectAsState()
    val userFullName by viewModel.userFullName.collectAsState()
    val fontSizeMultiplier by viewModel.fontSizeMultiplier.collectAsState()
    val churchName by viewModel.churchName.collectAsState()
    val churchEmblem by viewModel.churchEmblem.collectAsState()
    val allLogs by viewModel.allLogs.collectAsState()
    val spiritualTheme by viewModel.spiritualTheme.collectAsState()
    val spiritualOpacity by viewModel.spiritualOpacity.collectAsState()
    val isCandleGlowEnabled by viewModel.isCandleGlowEnabled.collectAsState()
    val totalCachedPrayerSections by viewModel.totalCachedPrayerSections.collectAsState()
    val totalCachedMeditations by viewModel.totalCachedMeditations.collectAsState()
    val isPreCachingInProgress by viewModel.isPreCachingInProgress.collectAsState()

    val context = LocalContext.current
    val restoreLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            viewModel.restoreBackupFromUri(context, it) { result ->
                result.onSuccess { count ->
                    Toast.makeText(
                        context,
                        AgpeyaStrings.backupRestoreSuccessMsg(count, lang),
                        Toast.LENGTH_LONG
                    ).show()
                }.onFailure { err ->
                    Toast.makeText(
                        context,
                        "Restore Error: ${err.message}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }
    var showLinkDialog by remember { mutableStateOf(false) }
    var inputSyncKey by remember { mutableStateOf("") }
    var prayerToEditTime by remember { mutableStateOf<PrayerId?>(null) }
    var prayerToEditSound by remember { mutableStateOf<PrayerId?>(null) }

    var isEditingFullName by remember { mutableStateOf(false) }
    var fullNameInput by remember(userFullName) { mutableStateOf(userFullName ?: "") }

    var isEditingEmail by remember { mutableStateOf(false) }
    var emailInput by remember(userEmail) { mutableStateOf(userEmail ?: "") }

    var isEditingChurch by remember { mutableStateOf(false) }
    var churchInput by remember(churchName) { mutableStateOf(churchName ?: "") }
    var showPrivacyDialog by remember { mutableStateOf(false) }
    var showGroundedSearchDialog by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp)
    ) {
        // Header
        item {
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                border = BorderStroke(1.dp, GoldPrimary.copy(alpha = 0.35f)),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("settings_header_card")
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(18.dp)
                ) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .size(50.dp)
                            .clip(CircleShape)
                            .background(GoldPrimary.copy(alpha = 0.2f))
                    ) {
                        CopticCrossCanvas(size = 32.dp, color = GoldPrimary)
                    }
                    Spacer(modifier = Modifier.width(14.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = if (lang == AppLanguage.ARABIC) "إعدادات الأجبية والتحكم الشامل" else "Agpeya Complete Settings",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = GoldPrimary
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = if (lang == AppLanguage.ARABIC) {
                                "تخصيص اللغات، حجم الخط، أجراس السواعي، المزامنة السحابية، والخلفيات الروحية"
                            } else {
                                "Customize languages, text size, alarms, cloud sync, and spiritual themes"
                            },
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            lineHeight = 16.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }

        // Multilingual Support & Local Timezone Notification Preferences
        item {
            MultilingualAndNotificationSettings(viewModel = viewModel)
            Spacer(modifier = Modifier.height(20.dp))
        }

        // ==========================================
        // TEXT SIZE & FONT SCALE CONTROL CARD
        // ==========================================
        item {
            Text(
                text = if (lang == AppLanguage.ARABIC) "التحكم في حجم النص والخط للصلوات" else "Prayer Text Size & Font Scale Control",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = GoldPrimary,
                modifier = Modifier.padding(bottom = 4.dp)
            )
            Text(
                text = if (lang == AppLanguage.ARABIC) "تكبير أو تصغير حجم خطوط الصلوات والمزامير للقراءة المريحة والخاشعة على جميع الشاشات" else "Scale prayer and psalm font size for comfortable, strain-free reading",
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
                    .testTag("text_size_control_card")
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.FormatSize,
                                contentDescription = null,
                                tint = GoldPrimary,
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(
                                    text = if (lang == AppLanguage.ARABIC) "حجم خط الصلوات والمزامير" else "Prayer Text Scale",
                                    style = MaterialTheme.typography.titleSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                val percentage = (fontSizeMultiplier * 100).toInt()
                                val scaleLabel = when {
                                    fontSizeMultiplier < 0.95f -> if (lang == AppLanguage.ARABIC) "صغير" else "Small"
                                    fontSizeMultiplier in 0.95f..1.10f -> if (lang == AppLanguage.ARABIC) "عادي (قياسي)" else "Normal"
                                    fontSizeMultiplier in 1.11f..1.35f -> if (lang == AppLanguage.ARABIC) "كبير (مريح)" else "Large"
                                    fontSizeMultiplier in 1.36f..1.60f -> if (lang == AppLanguage.ARABIC) "كبير جداً" else "Very Large"
                                    else -> if (lang == AppLanguage.ARABIC) "عملاق (مكبر)" else "Huge"
                                }
                                Text(
                                    text = "$percentage% - $scaleLabel",
                                    style = MaterialTheme.typography.labelMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = GoldPrimary
                                )
                            }
                        }

                        // Stepper buttons (- and +)
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            IconButton(
                                onClick = {
                                    val newScale = (fontSizeMultiplier - 0.1f).coerceIn(0.85f, 1.85f)
                                    viewModel.setFontSizeMultiplier(newScale)
                                },
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(CircleShape)
                                    .background(MaterialTheme.colorScheme.surfaceVariant)
                            ) {
                                Text(
                                    text = "A-",
                                    style = MaterialTheme.typography.labelMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }
                            Spacer(modifier = Modifier.width(6.dp))
                            IconButton(
                                onClick = {
                                    val newScale = (fontSizeMultiplier + 0.1f).coerceIn(0.85f, 1.85f)
                                    viewModel.setFontSizeMultiplier(newScale)
                                },
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(CircleShape)
                                    .background(MaterialTheme.colorScheme.surfaceVariant)
                            ) {
                                Text(
                                    text = "A+",
                                    style = MaterialTheme.typography.labelMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = GoldPrimary
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Slider
                    Slider(
                        value = fontSizeMultiplier,
                        onValueChange = { viewModel.setFontSizeMultiplier(it) },
                        valueRange = 0.85f..1.85f,
                        steps = 9,
                        colors = SliderDefaults.colors(
                            thumbColor = GoldPrimary,
                            activeTrackColor = GoldPrimary,
                            inactiveTrackColor = MaterialTheme.colorScheme.outlineVariant
                        ),
                        modifier = Modifier.fillMaxWidth().testTag("font_size_slider")
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    // Quick Presets Row
                    val presets = listOf(
                        0.85f to (if (lang == AppLanguage.ARABIC) "صغير 85%" else "85%"),
                        1.00f to (if (lang == AppLanguage.ARABIC) "عادي 100%" else "100%"),
                        1.25f to (if (lang == AppLanguage.ARABIC) "كبير 125%" else "125%"),
                        1.50f to (if (lang == AppLanguage.ARABIC) "كبير جداً 150%" else "150%"),
                        1.75f to (if (lang == AppLanguage.ARABIC) "عملاق 175%" else "175%")
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        presets.forEach { (scale, label) ->
                            val isCurrent = kotlin.math.abs(fontSizeMultiplier - scale) < 0.06f
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(if (isCurrent) GoldPrimary else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f))
                                    .clickable { viewModel.setFontSizeMultiplier(scale) }
                                    .padding(vertical = 8.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = label,
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = if (isCurrent) FontWeight.Bold else FontWeight.Normal,
                                    color = if (isCurrent) Color.Black else MaterialTheme.colorScheme.onSurface,
                                    maxLines = 1
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Interactive Live Preview Container
                    Text(
                        text = if (lang == AppLanguage.ARABIC) "معاينة حية لحجم الخط:" else "Live Text Size Preview:",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(6.dp))

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.45f))
                            .border(1.dp, GoldPrimary.copy(alpha = 0.3f), RoundedCornerShape(12.dp))
                            .padding(14.dp)
                    ) {
                        Column {
                            val sampleVerse = when (lang) {
                                AppLanguage.COPTIC -> "Ⲡϭⲟⲓⲥ ⲡⲉ ⲡⲁⲙⲁⲛⲉⲥⲱⲟⲩ ⲟⲩⲟϩ ⲛ̀ⲛⲉ ϩⲗⲓ ϣⲱⲧ ⲙ̀ⲙⲟⲓ. Ϧⲉⲛ ⲟⲩⲙⲁ ⲛ̀ⲟⲩⲟⲧ ⲁϥⲑⲣⲓϣⲱⲡⲓ ⲙ̀ⲙⲁⲩ."
                                AppLanguage.ENGLISH -> "«The Lord is my shepherd; I shall not want. He makes me to lie down in green pastures; He leads me beside the still waters.» (Psalm 23:1-2)"
                                AppLanguage.GERMAN, AppLanguage.SWISS_GERMAN -> "«Der Herr ist mein Hirt, mir wird nichts mangeln. Er weidet mich auf grünen Auen und führet mich zum frischen Wasser.» (Psalm 23:1-2)"
                                AppLanguage.AUSTRIAN_GERMAN -> "«Der Herr is mei Hirt, mir wird nix föhln. Er weidet mi auf saftign Wiesn und führt mi zum frischen Wossa.» (Psalm 23:1-2)"
                                AppLanguage.SPANISH -> "«El Señor es mi pastor, nada me faltará. En lugares de delicados pastos me hará descansar; junto a aguas de reposo me pastoreará.» (Salmo 23:1-2)"
                                AppLanguage.HINDI -> "«यहोवा मेरा चरवाहा है; मुझे कुछ घटी न होगी। वह मुझे हरी-हरी चराइयों में बैठाता है; वह मुझे सुखदाई जल के झरने के पास ले चलता है॥» (भजन संहिता 23:1-2)"
                                AppLanguage.CHINESE -> "«耶和华是我的牧者，我必不致缺乏。祂使我躺卧在青草地上，领我在可安歇的水边。»（诗篇 23:1-2）"
                                AppLanguage.ITALIAN -> "«Il Signore è il mio pastore: non manco di nulla. Su pascoli erbosi mi fa riposare, ad acque tranquille mi conduce.» (Salmo 23:1-2)"
                                AppLanguage.FRENCH -> "«L'Éternel est mon berger: je ne manquerai de rien. Il me fait reposer dans de verts pâturages, Il me dirige près des eaux paisibles.» (Psaume 23:1-2)"
                                else -> "«الرَّبُّ رَاعِيَّ فَلَا يُعْوِزُنِي شَيْءٌ. فِي مَرَاعٍ خُضْرٍ يُرْبِضُنِي. إِلَى مِيَاهِ الرَّاحَةِ يُورِدُنِي. يَرُدُّ نَفْسِي. يَهْدِينِي إِلَى سُبُلِ الْبِرِّ مِنْ أَجْلِ اسْمِهِ.» (مزمور 23: 1-3)"
                            }

                            Text(
                                text = sampleVerse,
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    fontSize = (15 * fontSizeMultiplier).sp,
                                    lineHeight = (24 * fontSizeMultiplier).sp
                                ),
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))
        }

        // Cross-Device Cloud Sync Card (Firestore)
        item {
            Card(
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier.fillMaxWidth().testTag("cloud_sync_card")
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(
                                imageVector = Icons.Default.CloudSync,
                                contentDescription = null,
                                tint = GoldPrimary,
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = AgpeyaStrings.cloudSyncTitle(lang),
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }

                        // Status Badge
                        val (statusText, statusBg, statusFg) = when (syncStatus) {
                            SyncStatus.SYNCED -> Triple(
                                AgpeyaStrings.syncStatusSynced(lang),
                                Color(0xFF1E3A2F),
                                Color(0xFF4CAF50)
                            )
                            SyncStatus.SYNCING -> Triple(
                                AgpeyaStrings.syncStatusSyncing(lang),
                                Color(0xFF3E3214),
                                GoldPrimary
                            )
                            SyncStatus.ERROR -> Triple(
                                "Sync Error",
                                Color(0xFF3E1E1E),
                                Color(0xFFFF6B6B)
                            )
                            else -> Triple(
                                AgpeyaStrings.syncStatusOffline(lang),
                                MaterialTheme.colorScheme.surfaceVariant,
                                MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(statusBg)
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                if (syncStatus == SyncStatus.SYNCING) {
                                    CircularProgressIndicator(
                                        modifier = Modifier.size(12.dp),
                                        strokeWidth = 2.dp,
                                        color = statusFg
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                }
                                Text(
                                    text = statusText,
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = statusFg
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = AgpeyaStrings.cloudSyncSubtitle(lang),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        lineHeight = 18.sp
                    )

                    Spacer(modifier = Modifier.height(14.dp))
                    HorizontalDivider(color = MaterialTheme.colorScheme.surfaceVariant)
                    Spacer(modifier = Modifier.height(14.dp))

                    // 1. User Full Name Row (الاسم ثلاثي أو رباعي)
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
                            .padding(12.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = null,
                            tint = GoldPrimary,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))

                        if (isEditingFullName) {
                            Column(modifier = Modifier.weight(1f)) {
                                OutlinedTextField(
                                    value = fullNameInput,
                                    onValueChange = { fullNameInput = it },
                                    label = {
                                        Text(if (lang == AppLanguage.ARABIC) "الاسم ثلاثي أو رباعي" else "Full Name (3 or 4 names)")
                                    },
                                    placeholder = {
                                        Text(if (lang == AppLanguage.ARABIC) "مثال: مينا يوسف بطرس جرجس" else "e.g. Mina Youssef Boutros Guirguis")
                                    },
                                    singleLine = true,
                                    modifier = Modifier.fillMaxWidth()
                                )
                                val words = fullNameInput.trim().split("\\s+".toRegex()).filter { it.isNotBlank() }
                                val wordCount = words.size
                                Spacer(modifier = Modifier.height(4.dp))
                                if (wordCount >= 4) {
                                    Text(
                                        text = if (lang == AppLanguage.ARABIC) "✅ اسم رباعي مكتمل ومثالي للتسجيل السحابي ($wordCount كلمات)" else "✅ Complete 4-part name ($wordCount words)",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = Color(0xFF4CAF50),
                                        fontWeight = FontWeight.SemiBold
                                    )
                                } else if (wordCount == 3) {
                                    Text(
                                        text = if (lang == AppLanguage.ARABIC) "✅ اسم ثلاثي معتمد للتسجيل السحابي (3 كلمات)" else "✅ Valid 3-part name (3 words)",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = GoldPrimary,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                } else if (wordCount in 1..2) {
                                    Text(
                                        text = if (lang == AppLanguage.ARABIC) "⚠️ يُفضل إدخال الاسم ثلاثياً أو رباعياً لضمان التوثيق السحابي الدقيق" else "⚠️ 3 or 4 names recommended for cloud sync",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = Color(0xFFFFA726)
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            IconButton(
                                onClick = {
                                    viewModel.saveUserFullName(fullNameInput.ifBlank { null })
                                    isEditingFullName = false
                                    Toast.makeText(
                                        context,
                                        if (lang == AppLanguage.ARABIC) "تم حفظ الاسم بنجاح" else "Name saved successfully",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = "Save Full Name",
                                    tint = GoldPrimary
                                )
                            }
                        } else {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = if (lang == AppLanguage.ARABIC) "الاسم ثلاثي أو رباعي (للتوثيق السحابي والتقارير)" else "Full Name (For Cloud & Reports)",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Text(
                                    text = if (!userFullName.isNullOrBlank()) userFullName!! else (if (lang == AppLanguage.ARABIC) "اضغط لإدخال الاسم ثلاثي أو رباعي" else "Tap to enter full name"),
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = if (!userFullName.isNullOrBlank()) FontWeight.Bold else FontWeight.Normal,
                                    color = if (!userFullName.isNullOrBlank()) GoldLight else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
                                )
                                val words = (userFullName ?: "").trim().split("\\s+".toRegex()).filter { it.isNotBlank() }
                                if (words.size >= 4) {
                                    Text(
                                        text = if (lang == AppLanguage.ARABIC) "✅ اسم رباعي موثق سحابياً" else "✅ Quadripartite Cloud Verified",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = Color(0xFF4CAF50),
                                        fontWeight = FontWeight.SemiBold
                                    )
                                } else if (words.size == 3) {
                                    Text(
                                        text = if (lang == AppLanguage.ARABIC) "✅ اسم ثلاثي موثق سحابياً" else "✅ Tripartite Cloud Verified",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = GoldPrimary,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                }
                            }
                            IconButton(
                                onClick = {
                                    fullNameInput = userFullName ?: ""
                                    isEditingFullName = true
                                },
                                modifier = Modifier.size(36.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Edit,
                                    contentDescription = "Edit Full Name",
                                    tint = GoldPrimary
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // 2. Connected Cloud Email Row (البريد الإلكتروني للتسجيل السحابي)
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(GoldPrimary.copy(alpha = 0.12f))
                            .padding(12.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Email,
                            contentDescription = null,
                            tint = GoldPrimary,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))

                        if (isEditingEmail) {
                            Column(modifier = Modifier.weight(1f)) {
                                OutlinedTextField(
                                    value = emailInput,
                                    onValueChange = { emailInput = it },
                                    label = {
                                        Text(if (lang == AppLanguage.ARABIC) "البريد الإلكتروني للتسجيل السحابي" else "Cloud Sync Email")
                                    },
                                    placeholder = { Text("user@example.com") },
                                    singleLine = true,
                                    modifier = Modifier.fillMaxWidth()
                                )
                                val cleanEmail = emailInput.trim()
                                val isValid = cleanEmail.isBlank() || android.util.Patterns.EMAIL_ADDRESS.matcher(cleanEmail).matches()
                                if (!isValid && cleanEmail.isNotBlank()) {
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = if (lang == AppLanguage.ARABIC) "⚠️ صيغة البريد الإلكتروني غير صحيحة" else "⚠️ Invalid email format",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = Color(0xFFFF6B6B)
                                    )
                                } else if (isValid && cleanEmail.isNotBlank()) {
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = if (lang == AppLanguage.ARABIC) "✅ بريد إلكتروني صالح للتسجيل والمزامنة" else "✅ Valid email for cloud sync",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = Color(0xFF4CAF50)
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            IconButton(
                                onClick = {
                                    val cleanEmail = emailInput.trim()
                                    if (cleanEmail.isBlank() || android.util.Patterns.EMAIL_ADDRESS.matcher(cleanEmail).matches()) {
                                        viewModel.saveUserEmail(cleanEmail.ifBlank { null }) {
                                            Toast.makeText(
                                                context,
                                                if (lang == AppLanguage.ARABIC) "تم تحديث حساب المزامنة السحابية" else "Cloud sync account updated",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }
                                        isEditingEmail = false
                                    } else {
                                        Toast.makeText(
                                            context,
                                            if (lang == AppLanguage.ARABIC) "صيغة البريد الإلكتروني غير صحيحة" else "Invalid email format",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = "Save Email",
                                    tint = GoldPrimary
                                )
                            }
                        } else {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = if (lang == AppLanguage.ARABIC) "البريد الإلكتروني للتسجيل السحابي" else "Cloud Sync Email Account",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Text(
                                    text = if (!userEmail.isNullOrBlank()) userEmail!! else (if (lang == AppLanguage.ARABIC) "اضغط لإدخال بريدك السحابي" else "Tap to enter cloud email"),
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = if (!userEmail.isNullOrBlank()) FontWeight.Bold else FontWeight.Normal,
                                    color = if (!userEmail.isNullOrBlank()) GoldLight else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
                                )
                            }
                            IconButton(
                                onClick = {
                                    emailInput = userEmail ?: ""
                                    isEditingEmail = true
                                },
                                modifier = Modifier.size(36.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Edit,
                                    contentDescription = "Edit Cloud Email",
                                    tint = GoldPrimary
                                )
                            }
                            IconButton(
                                onClick = { viewModel.openAuthOnboarding() },
                                modifier = Modifier.size(36.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.SwapHoriz,
                                    contentDescription = AgpeyaStrings.switchAccountButton(lang),
                                    tint = GoldPrimary
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Church / Ministry Row (Optional Profile Field)
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
                            .padding(12.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Church,
                            contentDescription = null,
                            tint = GoldPrimary,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        
                        if (isEditingChurch) {
                            OutlinedTextField(
                                value = churchInput,
                                onValueChange = { churchInput = it },
                                label = { Text(AgpeyaStrings.churchNameOptionalLabel(lang)) },
                                singleLine = true,
                                modifier = Modifier.weight(1f)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            IconButton(
                                onClick = {
                                    viewModel.saveChurchName(churchInput.ifBlank { null })
                                    isEditingChurch = false
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = "Save",
                                    tint = GoldPrimary
                                )
                            }
                        } else {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = AgpeyaStrings.churchNameOptionalLabel(lang),
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Text(
                                    text = if (!churchName.isNullOrBlank()) churchName!! else AgpeyaStrings.churchNamePlaceholder(lang),
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = if (!churchName.isNullOrBlank()) FontWeight.Bold else FontWeight.Normal,
                                    color = if (!churchName.isNullOrBlank()) GoldLight else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
                                )
                            }
                            IconButton(
                                onClick = { isEditingChurch = true },
                                modifier = Modifier.size(36.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Edit,
                                    contentDescription = "Edit Church Name",
                                    tint = GoldPrimary
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = AgpeyaStrings.churchEmblemTitle(lang),
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        val emblems = listOf(
                            "coptic_cross" to "✝️ صليب",
                            "st_mark_lion" to "🦁 مارمرقس",
                            "st_mary_dove" to "🕊️ حمامة",
                            "monastic_anchor" to "⚓ مرساة"
                        )
                        emblems.forEach { (code, label) ->
                            val isSelected = churchEmblem == code
                            FilterChip(
                                selected = isSelected,
                                onClick = { viewModel.saveChurchEmblem(code) },
                                label = { Text(label, style = MaterialTheme.typography.labelSmall) },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = GoldPrimary.copy(alpha = 0.25f),
                                    selectedLabelColor = GoldLight
                                )
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))
                    HorizontalDivider(color = MaterialTheme.colorScheme.surfaceVariant)
                    Spacer(modifier = Modifier.height(14.dp))

                    // Device Sync Key & Share
                    Text(
                        text = AgpeyaStrings.deviceSyncKey(lang),
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = AgpeyaStrings.syncKeyDescription(lang),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        lineHeight = 16.sp
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f))
                            .padding(horizontal = 12.dp, vertical = 8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Key,
                            contentDescription = null,
                            tint = GoldPrimary,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = syncKey,
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Bold,
                            color = GoldLight,
                            modifier = Modifier.weight(1f)
                        )
                        IconButton(
                            onClick = {
                                val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as? ClipboardManager
                                clipboard?.setPrimaryClip(ClipData.newPlainText("Agpeya Sync Key", syncKey))
                            },
                            modifier = Modifier.size(36.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.ContentCopy,
                                contentDescription = "Copy Sync Key",
                                tint = GoldPrimary,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Action buttons: Sync Now & Link Another Device
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Button(
                            onClick = { viewModel.triggerSync() },
                            colors = ButtonDefaults.buttonColors(containerColor = GoldPrimary),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.weight(1f).testTag("manual_sync_btn")
                        ) {
                            Icon(
                                imageVector = Icons.Default.Sync,
                                contentDescription = null,
                                tint = Color.Black,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = AgpeyaStrings.syncNow(lang),
                                fontWeight = FontWeight.Bold,
                                color = Color.Black
                            )
                        }

                        OutlinedButton(
                            onClick = {
                                inputSyncKey = syncKey
                                showLinkDialog = true
                            },
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Devices,
                                contentDescription = null,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = AgpeyaStrings.linkAnotherDevice(lang),
                                style = MaterialTheme.typography.bodySmall,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }

                    if (lastSyncTime > 0) {
                        Spacer(modifier = Modifier.height(10.dp))
                        val formattedTime = remember(lastSyncTime) {
                            val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
                            sdf.format(Date(lastSyncTime))
                        }
                        Text(
                            text = "${if (lang == AppLanguage.ARABIC) "آخر مزامنة ناجحة" else "Last synced"}: $formattedTime",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))
                    HorizontalDivider(color = MaterialTheme.colorScheme.surfaceVariant)
                    Spacer(modifier = Modifier.height(14.dp))

                    // PDF Spiritual Progress Report Export Button
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = AgpeyaStrings.exportPdfReport(lang),
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = userEmail?.let {
                                    if (lang == AppLanguage.ARABIC) "مُخصص باسم الحساب: $it" else "Account: $it"
                                } ?: AgpeyaStrings.exportPdfSubtitle(lang),
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

                        Button(
                            onClick = {
                                PrayerPdfExporter.generateAndSharePdf(
                                    context = context,
                                    userEmail = userEmail,
                                    userName = userEmail?.substringBefore("@"),
                                    churchName = churchName,
                                    churchEmblem = churchEmblem,
                                    syncKey = syncKey,
                                    lang = lang,
                                    periodName = if (lang == AppLanguage.ARABIC) "تقرير السجل الشامل" else "Full History Report",
                                    allLogs = allLogs
                                )
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = GoldPrimary),
                            shape = RoundedCornerShape(10.dp),
                            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.PictureAsPdf,
                                contentDescription = null,
                                tint = Color.Black,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = if (lang == AppLanguage.ARABIC) "تصدير PDF" else "Export PDF",
                                color = Color.Black,
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }

        // ==========================================
        // ORTHODOX GROUNDED SEARCH & GOOGLE VERIFICATION CARD
        // ==========================================
        item {
            Card(
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                border = BorderStroke(1.dp, GoldPrimary.copy(alpha = 0.35f)),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("settings_search_grounding_card")
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier
                                    .size(38.dp)
                                    .clip(CircleShape)
                                    .background(GoldPrimary.copy(alpha = 0.2f))
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Search,
                                    contentDescription = null,
                                    tint = GoldPrimary,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(
                                    text = if (lang == AppLanguage.ARABIC) "البحث الأرثوذكسي الموثق" else "Orthodox Grounded Search",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Text(
                                    text = "Google Search Grounding & Gemini",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = GoldPrimary,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }

                        Surface(
                            color = Color(0xFF4CAF50).copy(alpha = 0.15f),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text(
                                text = if (lang == AppLanguage.ARABIC) "مُفعل ونشط" else "Active",
                                style = MaterialTheme.typography.labelSmall,
                                color = Color(0xFF4CAF50),
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = if (lang == AppLanguage.ARABIC) {
                            "محرك بحث كنسي ذكي مدعوم ببيانات جوجل المباشرة لتوثيق مواعيد الأعياد والأصوام القبطية، قراءات القطمارس، وتفاسير المزامير مع روابط المصادر."
                        } else {
                            "Live Orthodox search engine powered by real-time Google Search data to verify feast/fast dates, Katameros scriptures, and psalm commentaries with cited sources."
                        },
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        lineHeight = 18.sp
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    Button(
                        onClick = { showGroundedSearchDialog = true },
                        colors = ButtonDefaults.buttonColors(containerColor = GoldPrimary),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(44.dp)
                            .testTag("btn_open_grounded_search_settings")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = null,
                            tint = Color.Black,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (lang == AppLanguage.ARABIC) "فتح نافذة البحث الأرثوذكسي الموثق" else "Open Grounded Search Dialog",
                            color = Color.Black,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }

        // Local Backup & Restore Card (JSON Export & Import)
        item {
            Card(
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                border = BorderStroke(1.dp, GoldPrimary.copy(alpha = 0.3f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Backup,
                            contentDescription = null,
                            tint = GoldPrimary,
                            modifier = Modifier.size(22.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = AgpeyaStrings.backupSectionTitle(lang),
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                    Spacer(modifier = Modifier.height(14.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        // Export JSON Backup Button
                        Button(
                            onClick = {
                                viewModel.exportBackupJson(context) { file ->
                                    val contentUri = FileProvider.getUriForFile(
                                        context,
                                        "${context.packageName}.fileprovider",
                                        file
                                    )
                                    val shareIntent = Intent(Intent.ACTION_SEND).apply {
                                        type = "application/json"
                                        putExtra(Intent.EXTRA_STREAM, contentUri)
                                        putExtra(Intent.EXTRA_SUBJECT, "Agpeya Orthodox Backup")
                                        addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                                    }
                                    context.startActivity(Intent.createChooser(shareIntent, AgpeyaStrings.backupExportButton(lang)))
                                }
                            },
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.buttonColors(containerColor = GoldPrimary),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.FileUpload,
                                contentDescription = null,
                                tint = Color.Black,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = AgpeyaStrings.backupExportButton(lang),
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = Color.Black
                            )
                        }

                        // Restore JSON Backup Button
                        OutlinedButton(
                            onClick = {
                                restoreLauncher.launch("application/json")
                            },
                            modifier = Modifier.weight(1f),
                            border = BorderStroke(1.dp, GoldPrimary),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.FileDownload,
                                contentDescription = null,
                                tint = GoldPrimary,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = AgpeyaStrings.backupRestoreButton(lang),
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = GoldPrimary
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }

        // Room Database Offline Cache Status & Management Card
        item {
            Card(
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                border = BorderStroke(1.dp, GoldPrimary.copy(alpha = 0.3f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.CloudSync,
                                contentDescription = null,
                                tint = GoldPrimary,
                                modifier = Modifier.size(22.dp)
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(
                                    text = if (lang == AppLanguage.ARABIC) "قاعدة بيانات روم والتخزين دون إنترنت" else "Room DB Offline Cache",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Text(
                                    text = if (lang == AppLanguage.ARABIC) "نصوص الصلوات والتأملات متاحة 100% دون اتصال" else "100% Offline Prayers & Meditations",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }

                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = Color(0xFF2E7D32).copy(alpha = 0.15f)
                        ) {
                            Text(
                                text = if (lang == AppLanguage.ARABIC) "✓ متصل محلياً" else "✓ Local Active",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF2E7D32),
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        // Stat 1: Cached Prayer Sections
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                            modifier = Modifier.weight(1f)
                        ) {
                            Column(
                                modifier = Modifier.padding(12.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = "$totalCachedPrayerSections",
                                    style = MaterialTheme.typography.titleLarge,
                                    fontWeight = FontWeight.Bold,
                                    color = BurgundyDeep
                                )
                                Text(
                                    text = if (lang == AppLanguage.ARABIC) "قسم صلوات مخزن" else "Cached Sections",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }

                        // Stat 2: Cached Meditations
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                            modifier = Modifier.weight(1f)
                        ) {
                            Column(
                                modifier = Modifier.padding(12.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = "$totalCachedMeditations",
                                    style = MaterialTheme.typography.titleLarge,
                                    fontWeight = FontWeight.Bold,
                                    color = GoldPrimary
                                )
                                Text(
                                    text = if (lang == AppLanguage.ARABIC) "تأمل وآية مخزنة" else "Cached Meditations",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Re-cache / Sync All Offline Data Button
                    Button(
                        onClick = {
                            viewModel.preCacheAllOfflineData {
                                Toast.makeText(
                                    context,
                                    if (lang == AppLanguage.ARABIC) "تم تحديث كافة نصوص الأجبية والتأملات بقاعدة البيانات المحلية" else "All prayers & meditations refreshed in Room DB",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        },
                        enabled = !isPreCachingInProgress,
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = BurgundyDeep),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        if (isPreCachingInProgress) {
                            CircularProgressIndicator(
                                color = GoldLight,
                                strokeWidth = 2.dp,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = if (lang == AppLanguage.ARABIC) "جاري ملء التخزين المحلي..." else "Caching in progress...",
                                color = GoldLight,
                                style = MaterialTheme.typography.labelMedium
                            )
                        } else {
                            Icon(
                                imageVector = Icons.Default.Sync,
                                contentDescription = null,
                                tint = GoldLight,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = if (lang == AppLanguage.ARABIC) "إعادة ملء التخزين المحلي للأجبية (Room DB)" else "Pre-Cache All Hours & Meditations",
                                color = GoldLight,
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
        item {
            Card(
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(18.dp)
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = if (lang == AppLanguage.ARABIC) "تجربة التنبيه والاهتزاز الفوري" else AgpeyaStrings.testAlert(lang),
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = if (lang == AppLanguage.ARABIC) "تشغيل محرك اهتزاز الجهاز ونغمة التنبيه للتأكد من عملهما" else "Test device vibration motor and alert sound",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    Button(
                        onClick = {
                            viewModel.testNotificationNow()
                            com.example.service.AgpeyaNotificationHelper.triggerDeviceVibration(context)
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = GoldPrimary),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.testTag("test_notification_btn")
                    ) {
                        Icon(
                            imageVector = Icons.Default.NotificationsActive,
                            contentDescription = null,
                            tint = Color.Black,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = if (lang == AppLanguage.ARABIC) "تجربة الاهتزاز" else "Test Vibration",
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))
        }

        // Spiritual Atmosphere & Reverence Background Themes Section
        item {
            Text(
                text = if (lang == AppLanguage.ARABIC) "الأجواء الروحية والخشوع والتأمل" else "Spiritual Atmosphere & Reverence",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = GoldPrimary,
                modifier = Modifier.padding(bottom = 4.dp)
            )
            Text(
                text = if (lang == AppLanguage.ARABIC) "تخصيص خلفيات بصرية مقدسة وإضاءة الشموع لزيادة الخشوع أثناء الصلاة" else "Customize sacred background art and candlelight glow for deep prayer focus",
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
                    .testTag("spiritual_atmosphere_card")
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(
                            imageVector = Icons.Default.Wallpaper,
                            contentDescription = null,
                            tint = GoldPrimary,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = if (lang == AppLanguage.ARABIC) "الخلفية الروحية المختارة" else "Selected Sacred Background",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = if (lang == AppLanguage.ARABIC) spiritualTheme.titleAr else spiritualTheme.titleEn,
                                style = MaterialTheme.typography.bodySmall,
                                color = GoldPrimary,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Theme options list
                    SpiritualBackgroundTheme.entries.forEachIndexed { idx, theme ->
                        val isSelected = theme == spiritualTheme
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp))
                                .background(if (isSelected) MaterialTheme.colorScheme.surfaceVariant else Color.Transparent)
                                .clickable {
                                    viewModel.setSpiritualTheme(theme)
                                    viewModel.setSpiritualOpacity(theme.defaultOpacity)
                                }
                                .padding(8.dp)
                        ) {
                            // Thumbnail Preview
                            Box(
                                modifier = Modifier
                                    .size(54.dp, 44.dp)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(Color(0xFF140F0D))
                                    .border(
                                        width = if (isSelected) 2.dp else 1.dp,
                                        color = if (isSelected) GoldPrimary else MaterialTheme.colorScheme.outlineVariant,
                                        shape = RoundedCornerShape(8.dp)
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                if (theme.drawableResId != null) {
                                    Image(
                                        painter = painterResource(id = theme.drawableResId),
                                        contentDescription = null,
                                        contentScale = ContentScale.Crop,
                                        modifier = Modifier.fillMaxSize()
                                    )
                                } else {
                                    Icon(
                                        imageVector = Icons.Default.AutoAwesome,
                                        contentDescription = null,
                                        tint = Color.Gray,
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.width(12.dp))

                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = if (lang == AppLanguage.ARABIC) theme.titleAr else theme.titleEn,
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                    color = if (isSelected) GoldPrimary else MaterialTheme.colorScheme.onSurface
                                )
                                Text(
                                    text = if (lang == AppLanguage.ARABIC) theme.subtitleAr else theme.subtitleEn,
                                    style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    lineHeight = 14.sp
                                )
                            }

                            if (isSelected) {
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = "Selected",
                                    tint = GoldPrimary,
                                    modifier = Modifier.size(22.dp)
                                )
                            }
                        }

                        if (idx < SpiritualBackgroundTheme.entries.size - 1) {
                            HorizontalDivider(
                                color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f),
                                modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                    HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
                    Spacer(modifier = Modifier.height(12.dp))

                    // Opacity Slider
                    if (spiritualTheme.drawableResId != null) {
                        Column(modifier = Modifier.fillMaxWidth()) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    text = if (lang == AppLanguage.ARABIC) "درجة ظهور الخلفية والخشوع" else "Background Presence & Opacity",
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = FontWeight.SemiBold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Text(
                                    text = "${(spiritualOpacity * 100).toInt()}%",
                                    style = MaterialTheme.typography.labelMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = GoldPrimary
                                )
                            }

                            Slider(
                                value = spiritualOpacity,
                                onValueChange = { viewModel.setSpiritualOpacity(it) },
                                valueRange = 0.05f..0.55f,
                                steps = 9,
                                colors = SliderDefaults.colors(
                                    thumbColor = GoldPrimary,
                                    activeTrackColor = GoldPrimary,
                                    inactiveTrackColor = MaterialTheme.colorScheme.surfaceVariant
                                ),
                                modifier = Modifier.fillMaxWidth()
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        // Candle Glow Breathing Animation Toggle
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(10.dp))
                                .clickable { viewModel.setCandleGlow(!isCandleGlowEnabled) }
                                .padding(vertical = 6.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.weight(1f)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.WbIncandescent,
                                    contentDescription = null,
                                    tint = if (isCandleGlowEnabled) GoldPrimary else MaterialTheme.colorScheme.onSurfaceVariant,
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(10.dp))
                                Column {
                                    Text(
                                        text = if (lang == AppLanguage.ARABIC) "وهج الشموع الحي (تأثير الخشوع)" else "Living Candlelight Breathing Glow",
                                        style = MaterialTheme.typography.bodyMedium,
                                        fontWeight = FontWeight.Medium,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                    Text(
                                        text = if (lang == AppLanguage.ARABIC) "نبض نوراني خافت وهادئ جداً أثناء الصلاة" else "Soft, subtle ambient breathing light during prayer",
                                        style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }

                            Switch(
                                checked = isCandleGlowEnabled,
                                onCheckedChange = { viewModel.setCandleGlow(it) },
                                colors = SwitchDefaults.colors(
                                    checkedThumbColor = Color.Black,
                                    checkedTrackColor = GoldPrimary,
                                    uncheckedThumbColor = MaterialTheme.colorScheme.outline,
                                    uncheckedTrackColor = MaterialTheme.colorScheme.surfaceVariant
                                )
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }


        // Spiritual Sounds and Sacred Melodies Showcase Section
        item {
            Text(
                text = AgpeyaStrings.spiritualSoundsTitle(lang),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = GoldPrimary,
                modifier = Modifier.padding(bottom = 4.dp)
            )
            Text(
                text = AgpeyaStrings.spiritualSoundsSubtitle(lang),
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
                    .testTag("spiritual_sounds_card")
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    SpiritualSound.entries.forEachIndexed { idx, sound ->
                        val isPlaying = playingSoundId == sound.id

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp)
                        ) {
                            IconButton(
                                onClick = {
                                    if (isPlaying) {
                                        viewModel.stopSoundPreview()
                                    } else {
                                        viewModel.playSoundPreview(sound)
                                    }
                                },
                                modifier = Modifier
                                    .clip(CircleShape)
                                    .background(if (isPlaying) BurgundyDeep else GoldPrimary.copy(alpha = 0.15f))
                                    .size(40.dp)
                                    .testTag("preview_btn_${sound.id}")
                            ) {
                                Icon(
                                    imageVector = if (isPlaying) Icons.Default.Stop else Icons.Default.PlayArrow,
                                    contentDescription = if (isPlaying) "Stop" else "Play",
                                    tint = if (isPlaying) GoldLight else GoldPrimary,
                                    modifier = Modifier.size(22.dp)
                                )
                            }

                            Spacer(modifier = Modifier.width(12.dp))

                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = sound.getName(lang),
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Text(
                                    text = sound.getDescription(lang),
                                    style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    lineHeight = 15.sp
                                )
                            }

                            Spacer(modifier = Modifier.width(8.dp))

                            OutlinedButton(
                                onClick = {
                                    viewModel.setSoundForAllPrayers(sound.id)
                                },
                                shape = RoundedCornerShape(10.dp),
                                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp),
                                modifier = Modifier.testTag("set_all_${sound.id}")
                            ) {
                                Text(
                                    text = if (lang == AppLanguage.ARABIC) "لكل الصلوات" else "All Prayers",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = GoldPrimary
                                )
                            }
                        }

                        if (idx < SpiritualSound.entries.size - 1) {
                            HorizontalDivider(
                                color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f),
                                modifier = Modifier.padding(vertical = 4.dp)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }

        // Alarms Configuration per Canonical Prayer Header & Master Vibration / Sound Controls
        item {
            val allPrayers = PrayerId.canonicalPrayers
            val anySoundActive = allPrayers.any { alarmSettings[it.code]?.soundEnabled == true }
            val anyVibrateActive = allPrayers.any { alarmSettings[it.code]?.vibrateEnabled == true }
            val allSoundActive = allPrayers.all { alarmSettings[it.code]?.soundEnabled == true }
            val allVibrateActive = allPrayers.all { alarmSettings[it.code]?.vibrateEnabled == true }

            Card(
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 14.dp)
                    .testTag("vibration_sound_card")
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Vibration,
                            contentDescription = null,
                            tint = GoldPrimary,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = AgpeyaStrings.vibrationAndSoundTitle(lang),
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = if (lang == AppLanguage.ARABIC) "التحكم الشامل في الصوت والاهتزاز لجميع التنبيهات" else "Master sound & vibration controls for all alerts",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Master Switch for Sound & Master Switch for Vibration
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f))
                            .padding(horizontal = 14.dp, vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = if (anySoundActive) Icons.Default.VolumeUp else Icons.Default.VolumeOff,
                                contentDescription = null,
                                tint = if (anySoundActive) GoldPrimary else MaterialTheme.colorScheme.outline,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(
                                    text = if (lang == AppLanguage.ARABIC) "تشغيل الصوت لجميع الصلوات" else "Sound for all prayers",
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = FontWeight.SemiBold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Text(
                                    text = if (allSoundActive) {
                                        if (lang == AppLanguage.ARABIC) "مفعل لكل الصلوات" else "Enabled for all"
                                    } else if (anySoundActive) {
                                        if (lang == AppLanguage.ARABIC) "مفعل لبعض الصلوات" else "Enabled for some"
                                    } else {
                                        if (lang == AppLanguage.ARABIC) "مكتوم للكل" else "Muted for all"
                                    },
                                    style = MaterialTheme.typography.labelSmall,
                                    color = if (anySoundActive) GoldPrimary else MaterialTheme.colorScheme.outline
                                )
                            }
                        }

                        Switch(
                            checked = anySoundActive,
                            onCheckedChange = { isChecked ->
                                viewModel.setSoundForAllPrayersEnabled(isChecked)
                            },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = Color.Black,
                                checkedTrackColor = GoldPrimary
                            ),
                            modifier = Modifier.testTag("master_sound_switch")
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f))
                            .padding(horizontal = 14.dp, vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Outlined.Vibration,
                                contentDescription = null,
                                tint = if (anyVibrateActive) GoldPrimary else MaterialTheme.colorScheme.outline,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(
                                    text = if (lang == AppLanguage.ARABIC) "تشغيل الاهتزاز لجميع الصلوات" else "Vibration for all prayers",
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = FontWeight.SemiBold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Text(
                                    text = if (allVibrateActive) {
                                        if (lang == AppLanguage.ARABIC) "مفعل لكل الصلوات" else "Enabled for all"
                                    } else if (anyVibrateActive) {
                                        if (lang == AppLanguage.ARABIC) "مفعل لبعض الصلوات" else "Enabled for some"
                                    } else {
                                        if (lang == AppLanguage.ARABIC) "متوقف للكل" else "Disabled for all"
                                    },
                                    style = MaterialTheme.typography.labelSmall,
                                    color = if (anyVibrateActive) GoldPrimary else MaterialTheme.colorScheme.outline
                                )
                            }
                        }

                        Switch(
                            checked = anyVibrateActive,
                            onCheckedChange = { isChecked ->
                                viewModel.setVibrateForAllPrayers(isChecked)
                            },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = Color.Black,
                                checkedTrackColor = GoldPrimary
                            ),
                            modifier = Modifier.testTag("master_vibrate_switch")
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Text(
                        text = if (lang == AppLanguage.ARABIC) "أوضاع التنبيه السريعة:" else "Quick Alert Presets:",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold,
                        color = GoldPrimary,
                        modifier = Modifier.padding(bottom = 6.dp)
                    )

                    // Quick Mode Presets
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedButton(
                            onClick = { viewModel.setMasterNotificationMode(soundEnabled = true, vibrateEnabled = true) },
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.weight(1f),
                            contentPadding = PaddingValues(horizontal = 4.dp, vertical = 6.dp),
                            border = if (allSoundActive && allVibrateActive) BorderStroke(1.5.dp, GoldPrimary) else ButtonDefaults.outlinedButtonBorder
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Icon(imageVector = Icons.Default.NotificationsActive, contentDescription = null, tint = GoldPrimary, modifier = Modifier.size(16.dp))
                                Text(
                                    text = AgpeyaStrings.soundAndVibrationMode(lang),
                                    style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                                    maxLines = 1
                                )
                            }
                        }

                        OutlinedButton(
                            onClick = { viewModel.setMasterNotificationMode(soundEnabled = false, vibrateEnabled = true) },
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.weight(1f),
                            contentPadding = PaddingValues(horizontal = 4.dp, vertical = 6.dp),
                            border = if (!anySoundActive && allVibrateActive) BorderStroke(1.5.dp, GoldPrimary) else ButtonDefaults.outlinedButtonBorder
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Icon(imageVector = Icons.Outlined.Vibration, contentDescription = null, tint = GoldPrimary, modifier = Modifier.size(16.dp))
                                Text(
                                    text = AgpeyaStrings.vibrationOnlyMode(lang),
                                    style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                                    maxLines = 1
                                )
                            }
                        }

                        OutlinedButton(
                            onClick = { viewModel.setMasterNotificationMode(soundEnabled = true, vibrateEnabled = false) },
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.weight(1f),
                            contentPadding = PaddingValues(horizontal = 4.dp, vertical = 6.dp),
                            border = if (allSoundActive && !anyVibrateActive) BorderStroke(1.5.dp, GoldPrimary) else ButtonDefaults.outlinedButtonBorder
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Icon(imageVector = Icons.Default.VolumeUp, contentDescription = null, tint = GoldPrimary, modifier = Modifier.size(16.dp))
                                Text(
                                    text = AgpeyaStrings.soundOnlyMode(lang),
                                    style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                                    maxLines = 1
                                )
                            }
                        }

                        OutlinedButton(
                            onClick = { viewModel.setMasterNotificationMode(soundEnabled = false, vibrateEnabled = false) },
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.weight(1f),
                            contentPadding = PaddingValues(horizontal = 4.dp, vertical = 6.dp),
                            border = if (!anySoundActive && !anyVibrateActive) BorderStroke(1.5.dp, GoldPrimary) else ButtonDefaults.outlinedButtonBorder
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Icon(imageVector = Icons.Default.VolumeOff, contentDescription = null, tint = GoldPrimary, modifier = Modifier.size(16.dp))
                                Text(
                                    text = if (lang == AppLanguage.ARABIC) "صامت للكل" else "Mute All",
                                    style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                                    maxLines = 1
                                )
                            }
                        }
                    }
                }
            }

            Text(
                text = if (lang == AppLanguage.ARABIC) "مواعيد وتخصيص تنبيهات الصلوات" else "Canonical Prayer Alarm Schedules",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = GoldPrimary,
                modifier = Modifier.padding(bottom = 8.dp)
            )
        }

        // Canonical Prayers List with Alarm and Sound Controls
        val canonicalList = PrayerId.canonicalPrayers
        items(canonicalList, key = { it.code }) { prayerId ->
            val setting = alarmSettings[prayerId.code] ?: PrayerAlarmSetting(
                prayerCode = prayerId.code,
                hour = prayerId.defaultHour,
                minute = prayerId.defaultMinute,
                isEnabled = prayerId != PrayerId.VEIL
            )

            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 5.dp)
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        CopticCrossCanvas(size = 24.dp)
                        Spacer(modifier = Modifier.width(10.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = prayerId.getDisplayName(lang),
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = prayerId.copticTitle,
                                style = MaterialTheme.typography.bodySmall,
                                color = GoldPrimary
                            )
                        }

                        // Change Time Button
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(10.dp))
                                .background(MaterialTheme.colorScheme.surfaceVariant)
                                .clickable { prayerToEditTime = prayerId }
                                .padding(horizontal = 10.dp, vertical = 6.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Outlined.AccessTime,
                                    contentDescription = null,
                                    tint = GoldPrimary,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = String.format("%02d:%02d", setting.hour, setting.minute),
                                    style = MaterialTheme.typography.labelMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }
                        }

                        Spacer(modifier = Modifier.width(10.dp))

                        // Enable/Disable Alarm Switch
                        Switch(
                            checked = setting.isEnabled,
                            onCheckedChange = { isEnabled ->
                                viewModel.updateAlarmSetting(setting.copy(isEnabled = isEnabled))
                            },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = Color.Black,
                                checkedTrackColor = GoldPrimary
                            )
                        )
                    }

                    // Toggles for Sound and Vibration if enabled
                    if (setting.isEnabled) {
                        Spacer(modifier = Modifier.height(10.dp))
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.End,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            // Sound toggle chip
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .clickable {
                                        viewModel.updateAlarmSetting(setting.copy(soundEnabled = !setting.soundEnabled))
                                    }
                                    .background(if (setting.soundEnabled) GoldPrimary.copy(alpha = 0.2f) else MaterialTheme.colorScheme.surfaceVariant)
                                    .padding(horizontal = 8.dp, vertical = 4.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.VolumeUp,
                                    contentDescription = "Sound",
                                    tint = if (setting.soundEnabled) GoldPrimary else MaterialTheme.colorScheme.outline,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = if (lang == AppLanguage.ARABIC) "صوت" else "Sound",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = if (setting.soundEnabled) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }

                            if (setting.soundEnabled) {
                                Spacer(modifier = Modifier.width(8.dp))
                                val prayerSound = SpiritualSound.fromId(setting.soundId)
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(8.dp))
                                        .clickable { prayerToEditSound = prayerId }
                                        .background(GoldPrimary.copy(alpha = 0.15f))
                                        .padding(horizontal = 8.dp, vertical = 4.dp)
                                        .testTag("tone_chip_${prayerId.code.lowercase()}")
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.MusicNote,
                                        contentDescription = "Tone",
                                        tint = GoldPrimary,
                                        modifier = Modifier.size(15.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = prayerSound.getName(lang),
                                        style = MaterialTheme.typography.labelSmall,
                                        fontWeight = FontWeight.Medium,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                    Spacer(modifier = Modifier.width(2.dp))
                                    Text(
                                        text = "▾",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = GoldPrimary
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.width(8.dp))

                            // Vibration toggle chip
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .clickable {
                                        viewModel.updateAlarmSetting(setting.copy(vibrateEnabled = !setting.vibrateEnabled))
                                    }
                                    .background(if (setting.vibrateEnabled) GoldPrimary.copy(alpha = 0.2f) else MaterialTheme.colorScheme.surfaceVariant)
                                    .padding(horizontal = 8.dp, vertical = 4.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Outlined.Vibration,
                                    contentDescription = "Vibrate",
                                    tint = if (setting.vibrateEnabled) GoldPrimary else MaterialTheme.colorScheme.outline,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = if (lang == AppLanguage.ARABIC) "اهتزاز" else "Vibrate",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = if (setting.vibrateEnabled) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                }
            }
        }

        // About & Google Play Privacy Policy Section
        item {
            Spacer(modifier = Modifier.height(20.dp))
            Card(
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { showPrivacyDialog = true }
                    .testTag("privacy_policy_card")
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(16.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = "Privacy Policy",
                        tint = GoldPrimary,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = if (lang == AppLanguage.ARABIC) "سياسة الخصوصية ومعلومات التطبيق" else "Privacy Policy & App Info",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = if (lang == AppLanguage.ARABIC) "الإصدار 1.0.0 • حماية كاملة للبيانات والصلوات محلياً" else "v1.0.0 • 100% Offline Local Data Privacy",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    Text(
                        text = "❯",
                        color = GoldPrimary,
                        style = MaterialTheme.typography.titleMedium
                    )
                }
            }
            Spacer(modifier = Modifier.height(30.dp))
        }
    }

    // Spiritual Sound Picker Dialog
    prayerToEditSound?.let { prayerId ->
        val currentSetting = alarmSettings[prayerId.code] ?: PrayerAlarmSetting(
            prayerCode = prayerId.code,
            hour = prayerId.defaultHour,
            minute = prayerId.defaultMinute
        )
        SpiritualSoundPickerDialog(
            currentSoundId = currentSetting.soundId,
            prayerId = prayerId,
            language = lang,
            playingSoundId = playingSoundId,
            onPlayPreview = { sound -> viewModel.playSoundPreview(sound) },
            onStopPreview = { viewModel.stopSoundPreview() },
            onSelectSound = { sound ->
                viewModel.setPrayerSound(prayerId, sound.id)
            },
            onDismiss = {
                viewModel.stopSoundPreview()
                prayerToEditSound = null
            }
        )
    }

    // Time Picker Dialog
    prayerToEditTime?.let { prayerId ->
        val currentSetting = alarmSettings[prayerId.code] ?: PrayerAlarmSetting(
            prayerCode = prayerId.code,
            hour = prayerId.defaultHour,
            minute = prayerId.defaultMinute
        )
        TimePickerDialog(
            initialHour = currentSetting.hour,
            initialMinute = currentSetting.minute,
            lang = lang,
            onDismiss = { prayerToEditTime = null },
            onConfirm = { hour, minute ->
                viewModel.updateAlarmSetting(
                    currentSetting.copy(hour = hour, minute = minute, isEnabled = true)
                )
                prayerToEditTime = null
            }
        )
    }

    // Link Another Device Dialog
    if (showLinkDialog) {
        AlertDialog(
            onDismissRequest = { showLinkDialog = false },
            title = {
                Text(
                    text = AgpeyaStrings.linkAnotherDevice(lang),
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Column {
                    Text(
                        text = AgpeyaStrings.enterSyncKeyPrompt(lang),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    OutlinedTextField(
                        value = inputSyncKey,
                        onValueChange = { inputSyncKey = it },
                        label = { Text(AgpeyaStrings.deviceSyncKey(lang)) },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (inputSyncKey.isNotBlank()) {
                            viewModel.updateSyncKey(inputSyncKey)
                        }
                        showLinkDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = GoldPrimary)
                ) {
                    Text(
                        text = AgpeyaStrings.saveAndSync(lang),
                        color = Color.Black,
                        fontWeight = FontWeight.Bold
                    )
                }
            },
            dismissButton = {
                OutlinedButton(onClick = { showLinkDialog = false }) {
                    Text(text = if (lang == AppLanguage.ARABIC) "إلغاء" else "Cancel")
                }
            }
        )
    }

    // Google Play Store Compliance Privacy Policy Dialog
    if (showPrivacyDialog) {
        AlertDialog(
            onDismissRequest = { showPrivacyDialog = false },
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = null,
                        tint = GoldPrimary,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (lang == AppLanguage.ARABIC) "سياسة الخصوصية وأمان البيانات" else "Privacy Policy & Data Safety",
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.titleMedium
                    )
                }
            },
            text = {
                LazyColumn(modifier = Modifier.height(320.dp)) {
                    item {
                        Text(
                            text = if (lang == AppLanguage.ARABIC) {
                                """
                                تطبيق الأجبية الأرثوذكسية (Agpeya Prayers)
                                الإصدار: 1.0.0 (Release)
                                
                                1. الخصوصية وحفظ البيانات:
                                • جميع سجلات الصلوات، الملاحظات الروحية، ومذكرات سر الاعتراف يتم حفظها حصرياً داخل جهازك في قاعدة بيانات محلية مشفرة وآمنة (Room Database).
                                • التطبيق لا يشارك أياً من نصوصك أو صلواتك مع أي طرف ثالث أو خوادم خارجية إطلاقاً.
                                
                                2. التزامن السحابي الاختياري:
                                • المزامنة السحابية هي ميزة اختيارية تماماً، وتتم فقط عند تفعيلك لها لحفظ نسخة احتياطية من سجلات الصلوات عبر مفتاح التزامن الخاص بك.
                                
                                3. الأذونات المستخدمة:
                                • الإشعارات (POST_NOTIFICATIONS): لتنبيهك بمواعيد الساعات القانونية للصلوات.
                                • المنبه الدقيق (USE_EXACT_ALARM): لضمان دقة مواعيد التنبيهات في وقتها المحدد حتى عند إغلاق التطبيق.
                                • الاهتزاز (VIBRATE): لإصدار اهتزاز لمسي هادئ عند الصلاة وأثناء التسبيح.
                                
                                4. الدعم الفني والتواصل:
                                • البريد الإلكتروني للمطور: georgeyoussef393@gmail.com
                                • التطبيق مجاني وخالٍ تماماً من أي إعلانات تجارية.
                                """.trimIndent()
                            } else {
                                """
                                Agpeya Orthodox Prayers
                                Version: 1.0.0 (Release)
                                
                                1. Privacy & Local Data Storage:
                                • All prayer logs, spiritual journal entries, and confession notes are stored locally on your device in a secure Room database.
                                • No personal prayers or confession notes are ever shared with third parties.
                                
                                2. Optional Cloud Synchronization:
                                • Cloud sync is 100% optional and only functions when you authenticate or share your personal sync key to backup prayer logs across your devices.
                                
                                3. Permissions Used:
                                • Notifications: To alert you at canonical prayer hours.
                                • Exact Alarms: To ensure canonical hour bells ring precisely on time.
                                • Vibration: For gentle haptic feedback during prayer beads and alarms.
                                
                                4. Developer & Support:
                                • Developer Contact: georgeyoussef393@gmail.com
                                • The application is free and completely ad-free.
                                """.trimIndent()
                            },
                            style = MaterialTheme.typography.bodySmall,
                            lineHeight = 20.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = { showPrivacyDialog = false },
                    colors = ButtonDefaults.buttonColors(containerColor = GoldPrimary)
                ) {
                    Text(
                        text = if (lang == AppLanguage.ARABIC) "فهمت وموافق" else "Close",
                        color = Color.Black,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        )
    }

    if (showGroundedSearchDialog) {
        OrthodoxGroundedSearchDialog(
            language = lang,
            onDismissRequest = { showGroundedSearchDialog = false }
        )
    }
}

