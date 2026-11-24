package com.example.ui.components

import android.app.DatePickerDialog
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.PictureAsPdf
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.TableChart
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.data.model.ArrowPrayerLogEntity
import com.example.data.model.PrayerLogEntity
import com.example.data.repository.AgpeyaRepository
import com.example.localization.AppLanguage
import com.example.report.ReportExportConfig
import com.example.report.ReportExportFilter
import com.example.report.ReportExportFormat
import com.example.report.ReportExportScope
import com.example.ui.theme.BurgundyDeep
import com.example.ui.theme.BurgundyPrimary
import com.example.ui.theme.GoldPrimary
import com.example.ui.theme.PeacefulGreen
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ReportExportDialog(
    lang: AppLanguage,
    initialFormat: ReportExportFormat = ReportExportFormat.PDF,
    initialScope: ReportExportScope = ReportExportScope.CUSTOM_DURATION,
    allLogs: List<PrayerLogEntity>,
    allArrowLogs: List<ArrowPrayerLogEntity>,
    onDismiss: () -> Unit,
    onConfirmExport: (
        config: ReportExportConfig,
        filteredLogs: List<PrayerLogEntity>,
        filteredArrowLogs: List<ArrowPrayerLogEntity>,
        periodLabel: String
    ) -> Unit
) {
    val context = LocalContext.current
    val isAr = lang == AppLanguage.ARABIC
    val layoutDirection = if (lang.isRtl) LayoutDirection.Rtl else LayoutDirection.Ltr
    val currentYear = remember { Calendar.getInstance().get(Calendar.YEAR) }
    val todayStr = remember { AgpeyaRepository.getTodayString() }
    val defaultStartStr = remember(todayStr) {
        ReportExportFilter.resolveDurationRange(todayStr, 30).first
    }

    var selectedFormat by remember { mutableStateOf(initialFormat) }
    var selectedScope by remember { mutableStateOf(initialScope) }
    var durationDays by remember { mutableIntStateOf(30) }
    var startDateStr by remember { mutableStateOf(defaultStartStr) }
    var endDateStr by remember { mutableStateOf(todayStr) }
    var selectedYear by remember { mutableIntStateOf(currentYear) }
    var selectedQuarter by remember { mutableIntStateOf(1) } // Q1 highlighted first as requested
    var selectedHalfYear by remember { mutableIntStateOf(1) }

    val currentConfig = ReportExportConfig(
        scope = selectedScope,
        format = selectedFormat,
        durationDays = durationDays,
        startDateString = startDateStr,
        endDateString = endDateStr,
        year = selectedYear,
        quarter = selectedQuarter,
        halfYear = selectedHalfYear
    )

    val filteredCanonical = remember(allLogs, currentConfig) {
        ReportExportFilter.filterCanonicalLogs(allLogs, currentConfig, todayStr)
    }
    val filteredArrow = remember(allArrowLogs, currentConfig) {
        ReportExportFilter.filterArrowLogs(allArrowLogs, currentConfig, todayStr)
    }
    val resolvedLabel = remember(currentConfig, lang) {
        ReportExportFilter.buildPeriodDisplayLabel(currentConfig, lang, todayStr)
    }

    fun openDatePicker(initialDateStr: String, onPicked: (String) -> Unit) {
        val cal = Calendar.getInstance()
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.US)
        runCatching { sdf.parse(initialDateStr) }.getOrNull()?.let { cal.time = it }
        DatePickerDialog(
            context,
            { _, year, month, dayOfMonth ->
                onPicked(String.format(Locale.US, "%04d-%02d-%02d", year, month + 1, dayOfMonth))
            },
            cal.get(Calendar.YEAR),
            cal.get(Calendar.MONTH),
            cal.get(Calendar.DAY_OF_MONTH)
        ).show()
    }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        CompositionLocalProvider(LocalLayoutDirection provides layoutDirection) {
            Surface(
                shape = RoundedCornerShape(24.dp),
                color = MaterialTheme.colorScheme.surface,
                border = BorderStroke(1.2.dp, GoldPrimary.copy(alpha = 0.45f)),
                tonalElevation = 8.dp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 20.dp)
            ) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    // Sacred Burgundy-Gold Dialog Header
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                Brush.horizontalGradient(
                                    colors = listOf(BurgundyDeep, BurgundyPrimary)
                                )
                            )
                            .padding(horizontal = 18.dp, vertical = 14.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier
                                    .size(40.dp)
                                    .clip(CircleShape)
                                    .background(GoldPrimary.copy(alpha = 0.2f))
                            ) {
                                Icon(
                                    imageVector = if (selectedFormat == ReportExportFormat.PDF) {
                                        Icons.Default.PictureAsPdf
                                    } else {
                                        Icons.Default.TableChart
                                    },
                                    contentDescription = null,
                                    tint = GoldPrimary,
                                    modifier = Modifier.size(22.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = if (isAr) "تخصيص وإخراج التقرير" else "Configure & Export Report",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                                Text(
                                    text = if (isAr) {
                                        "اختر الفترة الزمنية ونوع التقرير المطلوب إخراجه"
                                    } else {
                                        "Choose the duration, date range, or periodic scope"
                                    },
                                    style = MaterialTheme.typography.labelSmall,
                                    color = Color.White.copy(alpha = 0.85f)
                                )
                            }
                        }
                    }

                    // Scrollable Body
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(max = 520.dp)
                            .verticalScroll(rememberScrollState())
                            .padding(horizontal = 16.dp, vertical = 14.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        // 1. Export Format Selector (PDF vs Excel CSV)
                        Text(
                            text = if (isAr) "صيغة الملف:" else "Export Format:",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(10.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            FormatChoicePill(
                                label = if (isAr) "ملف PDF احترافي" else "PDF Report",
                                icon = Icons.Default.PictureAsPdf,
                                selected = selectedFormat == ReportExportFormat.PDF,
                                accentColor = GoldPrimary,
                                testTag = "export_format_pdf",
                                onClick = { selectedFormat = ReportExportFormat.PDF },
                                modifier = Modifier.weight(1f)
                            )
                            FormatChoicePill(
                                label = if (isAr) "جدول Excel (CSV)" else "Excel (CSV)",
                                icon = Icons.Default.TableChart,
                                selected = selectedFormat == ReportExportFormat.EXCEL_CSV,
                                accentColor = PeacefulGreen,
                                testTag = "export_format_excel",
                                onClick = { selectedFormat = ReportExportFormat.EXCEL_CSV },
                                modifier = Modifier.weight(1f)
                            )
                        }

                        HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))

                        // 2. Period Scope Selector
                        Text(
                            text = if (isAr) "نطاق التقرير المطلوب:" else "Report Time Scope:",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )

                        val scopeOptions = listOf(
                            ReportExportScope.CUSTOM_DURATION to (if (isAr) "مدة معينة" else "Specific Duration"),
                            ReportExportScope.DATE_RANGE to (if (isAr) "من تاريخ إلى تاريخ" else "From Date – To Date"),
                            ReportExportScope.QUARTERLY to (if (isAr) "ربع سنوي" else "Quarterly"),
                            ReportExportScope.SEMI_ANNUAL to (if (isAr) "نصف سنوي" else "Semi-Annual"),
                            ReportExportScope.ANNUAL to (if (isAr) "تقرير سنوي" else "Annual Report"),
                            ReportExportScope.ALL_RECORDS to (if (isAr) "السجل الشامل" else "All Records")
                        )

                        FlowRow(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            scopeOptions.forEach { (scope, label) ->
                                val isSelected = selectedScope == scope
                                FilterChip(
                                    selected = isSelected,
                                    onClick = { selectedScope = scope },
                                    label = {
                                        Text(
                                            text = label,
                                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                            fontSize = 12.sp
                                        )
                                    },
                                    colors = FilterChipDefaults.filterChipColors(
                                        selectedContainerColor = GoldPrimary.copy(alpha = 0.22f),
                                        selectedLabelColor = MaterialTheme.colorScheme.onSurface
                                    ),
                                    border = FilterChipDefaults.filterChipBorder(
                                        enabled = true,
                                        selected = isSelected,
                                        selectedBorderColor = GoldPrimary,
                                        selectedBorderWidth = 1.4.dp
                                    ),
                                    modifier = Modifier.testTag("export_scope_${scope.name.lowercase()}")
                                )
                            }
                        }

                        // 3. Dynamic Configuration Pane for the Selected Scope
                        Surface(
                            shape = RoundedCornerShape(14.dp),
                            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.55f),
                            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.6f)),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            AnimatedContent(
                                targetState = selectedScope,
                                label = "export_scope_panel"
                            ) { scope ->
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(14.dp),
                                    verticalArrangement = Arrangement.spacedBy(10.dp)
                                ) {
                                    when (scope) {
                                        ReportExportScope.CUSTOM_DURATION -> {
                                            Text(
                                                text = if (isAr) "اختر المدة بالأيام:" else "Select Duration (Days):",
                                                style = MaterialTheme.typography.labelMedium,
                                                fontWeight = FontWeight.SemiBold
                                            )
                                            FlowRow(
                                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                                verticalArrangement = Arrangement.spacedBy(6.dp)
                                            ) {
                                                listOf(7, 14, 30, 60, 90, 180).forEach { preset ->
                                                    FilterChip(
                                                        selected = durationDays == preset,
                                                        onClick = { durationDays = preset },
                                                        label = {
                                                            Text(
                                                                text = if (isAr) "$preset يوم" else "$preset days",
                                                                fontSize = 12.sp
                                                            )
                                                        },
                                                        modifier = Modifier.testTag("duration_preset_$preset")
                                                    )
                                                }
                                            }
                                            // Custom day stepper (+/- 1 day)
                                            Row(
                                                verticalAlignment = Alignment.CenterVertically,
                                                horizontalArrangement = Arrangement.SpaceBetween,
                                                modifier = Modifier.fillMaxWidth()
                                            ) {
                                                Text(
                                                    text = if (isAr) "أو حدد عدداً مخصصاً:" else "Custom days:",
                                                    style = MaterialTheme.typography.bodySmall,
                                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                                )
                                                Row(verticalAlignment = Alignment.CenterVertically) {
                                                    IconButton(
                                                        onClick = { if (durationDays > 1) durationDays-- },
                                                        modifier = Modifier.size(40.dp)
                                                    ) {
                                                        Icon(Icons.Default.Remove, contentDescription = null, modifier = Modifier.size(18.dp))
                                                    }
                                                    Text(
                                                        text = if (isAr) "$durationDays يوم" else "$durationDays d",
                                                        style = MaterialTheme.typography.titleSmall,
                                                        fontWeight = FontWeight.Bold,
                                                        color = GoldPrimary,
                                                        modifier = Modifier.padding(horizontal = 8.dp)
                                                    )
                                                    IconButton(
                                                        onClick = { if (durationDays < 3650) durationDays++ },
                                                        modifier = Modifier.size(40.dp)
                                                    ) {
                                                        Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(18.dp))
                                                    }
                                                }
                                            }
                                        }

                                        ReportExportScope.DATE_RANGE -> {
                                            Text(
                                                text = if (isAr) "حدد تاريخ البداية والنهاية:" else "Select Start & End Dates:",
                                                style = MaterialTheme.typography.labelMedium,
                                                fontWeight = FontWeight.SemiBold
                                            )
                                            Row(
                                                horizontalArrangement = Arrangement.spacedBy(10.dp),
                                                modifier = Modifier.fillMaxWidth()
                                            ) {
                                                DatePickerFieldButton(
                                                    label = if (isAr) "من تاريخ" else "From Date",
                                                    dateValue = startDateStr,
                                                    testTag = "export_from_date_btn",
                                                    onClick = { openDatePicker(startDateStr) { startDateStr = it } },
                                                    modifier = Modifier.weight(1f)
                                                )
                                                DatePickerFieldButton(
                                                    label = if (isAr) "إلى تاريخ" else "To Date",
                                                    dateValue = endDateStr,
                                                    testTag = "export_to_date_btn",
                                                    onClick = { openDatePicker(endDateStr) { endDateStr = it } },
                                                    modifier = Modifier.weight(1f)
                                                )
                                            }
                                        }

                                        ReportExportScope.QUARTERLY -> {
                                            YearStepperRow(
                                                isAr = isAr,
                                                year = selectedYear,
                                                onYearChange = { selectedYear = it }
                                            )
                                            val quarters = listOf(
                                                1 to (if (isAr) "الربع الأول (يناير – مارس)" else "Q1 (Jan – Mar)"),
                                                2 to (if (isAr) "الربع الثاني (أبريل – يونيو)" else "Q2 (Apr – Jun)"),
                                                3 to (if (isAr) "الربع الثالث (يوليو – سبتمبر)" else "Q3 (Jul – Sep)"),
                                                4 to (if (isAr) "الربع الرابع (أكتوبر – ديسمبر)" else "Q4 (Oct – Dec)")
                                            )
                                            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                                                quarters.forEach { (q, qTitle) ->
                                                    SelectablePeriodRow(
                                                        title = qTitle,
                                                        selected = selectedQuarter == q,
                                                        testTag = "quarter_option_$q",
                                                        onClick = { selectedQuarter = q }
                                                    )
                                                }
                                            }
                                        }

                                        ReportExportScope.SEMI_ANNUAL -> {
                                            YearStepperRow(
                                                isAr = isAr,
                                                year = selectedYear,
                                                onYearChange = { selectedYear = it }
                                            )
                                            val halves = listOf(
                                                1 to (if (isAr) "النصف الأول (يناير – يونيو)" else "H1 (Jan – Jun)"),
                                                2 to (if (isAr) "النصف الثاني (يوليو – ديسمبر)" else "H2 (Jul – Dec)")
                                            )
                                            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                                                halves.forEach { (h, hTitle) ->
                                                    SelectablePeriodRow(
                                                        title = hTitle,
                                                        selected = selectedHalfYear == h,
                                                        testTag = "half_option_$h",
                                                        onClick = { selectedHalfYear = h }
                                                    )
                                                }
                                            }
                                        }

                                        ReportExportScope.ANNUAL -> {
                                            YearStepperRow(
                                                isAr = isAr,
                                                year = selectedYear,
                                                onYearChange = { selectedYear = it }
                                            )
                                            Text(
                                                text = if (isAr) {
                                                    "سيشمل التقرير جميع الصلوات المسجلة خلال عام $selectedYear بالكامل (من يناير حتى ديسمبر)."
                                                } else {
                                                    "Includes all canonical and arrow prayers recorded across all 12 months of $selectedYear."
                                                },
                                                style = MaterialTheme.typography.bodySmall,
                                                color = MaterialTheme.colorScheme.onSurfaceVariant
                                            )
                                        }

                                        ReportExportScope.ALL_RECORDS -> {
                                            Row(verticalAlignment = Alignment.CenterVertically) {
                                                Icon(
                                                    imageVector = Icons.Default.Schedule,
                                                    contentDescription = null,
                                                    tint = GoldPrimary,
                                                    modifier = Modifier.size(18.dp)
                                                )
                                                Spacer(modifier = Modifier.width(8.dp))
                                                Text(
                                                    text = if (isAr) {
                                                        "تصدير السجل الكامل لجميع الصلوات المسجلة منذ بدء استخدام التطبيق."
                                                    } else {
                                                        "Exports the complete record of all prayers logged across all dates."
                                                    },
                                                    style = MaterialTheme.typography.bodySmall,
                                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }

                        // 4. Live Filtered Records Preview Banner
                        val totalArrowRep = filteredArrow.sumOf { it.count }
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = GoldPrimary.copy(alpha = 0.11f),
                            border = BorderStroke(1.dp, GoldPrimary.copy(alpha = 0.4f)),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(
                                modifier = Modifier.padding(12.dp),
                                verticalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Text(
                                    text = resolvedLabel,
                                    style = MaterialTheme.typography.labelLarge,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Text(
                                    text = if (isAr) {
                                        "المطابق للإخراج: ${filteredCanonical.size} صلاة سواعي • $totalArrowRep صلاة سهمية (${filteredArrow.size} جلسة)"
                                    } else {
                                        "Matching records: ${filteredCanonical.size} canonical • $totalArrowRep arrow prayers (${filteredArrow.size} sessions)"
                                    },
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }

                    HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))

                    // Action Buttons Row
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 12.dp)
                    ) {
                        TextButton(
                            onClick = onDismiss,
                            modifier = Modifier
                                .height(48.dp)
                                .testTag("export_dialog_cancel")
                        ) {
                            Text(
                                text = if (isAr) "إلغاء" else "Cancel",
                                fontWeight = FontWeight.SemiBold
                            )
                        }

                        Button(
                            onClick = {
                                onConfirmExport(
                                    currentConfig,
                                    filteredCanonical,
                                    filteredArrow,
                                    resolvedLabel
                                )
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (selectedFormat == ReportExportFormat.PDF) GoldPrimary else PeacefulGreen,
                                contentColor = if (selectedFormat == ReportExportFormat.PDF) Color.Black else Color.White
                            ),
                            shape = RoundedCornerShape(12.dp),
                            contentPadding = PaddingValues(horizontal = 16.dp),
                            modifier = Modifier
                                .weight(1f)
                                .height(48.dp)
                                .testTag("export_dialog_confirm")
                        ) {
                            Icon(
                                imageVector = if (selectedFormat == ReportExportFormat.PDF) {
                                    Icons.Default.PictureAsPdf
                                } else {
                                    Icons.Default.TableChart
                                },
                                contentDescription = null,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = if (isAr) {
                                    if (selectedFormat == ReportExportFormat.PDF) "إخراج ومشاركة PDF" else "إخراج ومشاركة Excel"
                                } else {
                                    if (selectedFormat == ReportExportFormat.PDF) "Export & Share PDF" else "Export & Share Excel"
                                },
                                fontWeight = FontWeight.Bold,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun FormatChoicePill(
    label: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    selected: Boolean,
    accentColor: Color,
    testTag: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = if (selected) accentColor.copy(alpha = 0.16f) else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f),
        border = BorderStroke(
            width = if (selected) 1.5.dp else 1.dp,
            color = if (selected) accentColor else MaterialTheme.colorScheme.outlineVariant
        ),
        modifier = modifier
            .height(48.dp)
            .clip(RoundedCornerShape(12.dp))
            .clickable(onClick = onClick)
            .testTag(testTag)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.padding(horizontal = 10.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = if (selected) accentColor else MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = label,
                style = MaterialTheme.typography.labelLarge,
                fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium,
                color = if (selected) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
private fun DatePickerFieldButton(
    label: String,
    dateValue: String,
    testTag: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedButton(
        onClick = onClick,
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(1.dp, GoldPrimary.copy(alpha = 0.6f)),
        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 8.dp),
        modifier = modifier
            .height(54.dp)
            .testTag(testTag)
    ) {
        Icon(
            imageVector = Icons.Default.DateRange,
            contentDescription = label,
            tint = GoldPrimary,
            modifier = Modifier.size(18.dp)
        )
        Spacer(modifier = Modifier.width(6.dp))
        Column(horizontalAlignment = Alignment.Start) {
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = dateValue,
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

@Composable
private fun YearStepperRow(
    isAr: Boolean,
    year: Int,
    onYearChange: (Int) -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = Icons.Default.CalendarMonth,
                contentDescription = null,
                tint = GoldPrimary,
                modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = if (isAr) "السنة:" else "Year:",
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Bold
            )
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(
                onClick = { onYearChange(year - 1) },
                modifier = Modifier
                    .size(40.dp)
                    .testTag("export_year_prev")
            ) {
                Icon(Icons.Default.Remove, contentDescription = null, modifier = Modifier.size(18.dp))
            }
            Text(
                text = "$year",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = GoldPrimary,
                modifier = Modifier.padding(horizontal = 10.dp)
            )
            IconButton(
                onClick = { onYearChange(year + 1) },
                modifier = Modifier
                    .size(40.dp)
                    .testTag("export_year_next")
            ) {
                Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(18.dp))
            }
        }
    }
}

@Composable
private fun SelectablePeriodRow(
    title: String,
    selected: Boolean,
    testTag: String,
    onClick: () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(10.dp),
        color = if (selected) GoldPrimary.copy(alpha = 0.16f) else MaterialTheme.colorScheme.surface,
        border = BorderStroke(
            width = if (selected) 1.4.dp else 1.dp,
            color = if (selected) GoldPrimary else MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.6f)
        ),
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .clickable(onClick = onClick)
            .testTag(testTag)
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal,
            color = if (selected) GoldPrimary else MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp)
        )
    }
}
