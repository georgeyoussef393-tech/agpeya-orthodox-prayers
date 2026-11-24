package com.example.report

import com.example.data.model.ArrowPrayerLogEntity
import com.example.data.model.PrayerLogEntity
import com.example.data.repository.AgpeyaRepository
import com.example.localization.AppLanguage
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

/**
 * Defines all export scope options available to the user when generating
 * PDF or Excel/CSV spiritual prayer reports.
 */
enum class ReportExportScope {
    CUSTOM_DURATION, // مدة معينة (آخر 7 / 14 / 30 / 90 يوم أو عدد أيام مخصص)
    DATE_RANGE,      // من تاريخ محدد إلى تاريخ محدد
    QUARTERLY,       // ربع سنوي (الربع الأول / الثاني / الثالث / الرابع)
    SEMI_ANNUAL,     // نصف سنوي (النصف الأول / النصف الثاني)
    ANNUAL,          // تقرير سنوي
    ALL_RECORDS      // السجل الشامل
}

enum class ReportExportFormat {
    PDF,
    EXCEL_CSV
}

data class ReportExportConfig(
    val scope: ReportExportScope = ReportExportScope.ALL_RECORDS,
    val format: ReportExportFormat = ReportExportFormat.PDF,
    val durationDays: Int = 30,
    val startDateString: String = AgpeyaRepository.getTodayString(),
    val endDateString: String = AgpeyaRepository.getTodayString(),
    val year: Int = Calendar.getInstance().get(Calendar.YEAR),
    val quarter: Int = 1,
    val halfYear: Int = 1
)

object ReportExportFilter {

    fun filterCanonicalLogs(
        logs: List<PrayerLogEntity>,
        config: ReportExportConfig,
        todayDate: String = AgpeyaRepository.getTodayString()
    ): List<PrayerLogEntity> {
        return when (config.scope) {
            ReportExportScope.ALL_RECORDS -> logs
            ReportExportScope.CUSTOM_DURATION -> {
                val (startStr, endStr) = resolveDurationRange(todayDate, config.durationDays)
                logs.filter { it.dateString in startStr..endStr }
            }
            ReportExportScope.DATE_RANGE -> {
                val (startStr, endStr) = normalizeRange(config.startDateString, config.endDateString)
                logs.filter { it.dateString in startStr..endStr }
            }
            ReportExportScope.QUARTERLY -> {
                logs.filter { log ->
                    log.year == config.year && quarterOfMonth(log.month) == config.quarter
                }
            }
            ReportExportScope.SEMI_ANNUAL -> {
                logs.filter { log ->
                    log.year == config.year && halfYearOfMonth(log.month) == config.halfYear
                }
            }
            ReportExportScope.ANNUAL -> {
                logs.filter { it.year == config.year }
            }
        }
    }

    fun filterArrowLogs(
        logs: List<ArrowPrayerLogEntity>,
        config: ReportExportConfig,
        todayDate: String = AgpeyaRepository.getTodayString()
    ): List<ArrowPrayerLogEntity> {
        return when (config.scope) {
            ReportExportScope.ALL_RECORDS -> logs
            ReportExportScope.CUSTOM_DURATION -> {
                val (startStr, endStr) = resolveDurationRange(todayDate, config.durationDays)
                logs.filter { it.dateString in startStr..endStr }
            }
            ReportExportScope.DATE_RANGE -> {
                val (startStr, endStr) = normalizeRange(config.startDateString, config.endDateString)
                logs.filter { it.dateString in startStr..endStr }
            }
            ReportExportScope.QUARTERLY -> {
                logs.filter { log ->
                    log.year == config.year && log.quarter == config.quarter
                }
            }
            ReportExportScope.SEMI_ANNUAL -> {
                logs.filter { log ->
                    log.year == config.year && log.halfYear == config.halfYear
                }
            }
            ReportExportScope.ANNUAL -> {
                logs.filter { it.year == config.year }
            }
        }
    }

    fun buildPeriodDisplayLabel(
        config: ReportExportConfig,
        lang: AppLanguage,
        todayDate: String = AgpeyaRepository.getTodayString()
    ): String {
        val isAr = lang == AppLanguage.ARABIC
        return when (config.scope) {
            ReportExportScope.ALL_RECORDS -> {
                if (isAr) "السجل الشامل (كل الفترات)" else "Full History (All Time)"
            }
            ReportExportScope.CUSTOM_DURATION -> {
                val days = config.durationDays.coerceAtLeast(1)
                val (startStr, endStr) = resolveDurationRange(todayDate, days)
                if (isAr) {
                    "مدة محددة: آخر $days يوم ($startStr إلى $endStr)"
                } else {
                    "Last $days Days ($startStr to $endStr)"
                }
            }
            ReportExportScope.DATE_RANGE -> {
                val (startStr, endStr) = normalizeRange(config.startDateString, config.endDateString)
                if (isAr) "من $startStr إلى $endStr" else "From $startStr to $endStr"
            }
            ReportExportScope.QUARTERLY -> {
                val qAr = when (config.quarter) {
                    1 -> "الربع الأول (يناير – مارس)"
                    2 -> "الربع الثاني (أبريل – يونيو)"
                    3 -> "الربع الثالث (يوليو – سبتمبر)"
                    else -> "الربع الرابع (أكتوبر – ديسمبر)"
                }
                val qEn = when (config.quarter) {
                    1 -> "Q1 (Jan – Mar)"
                    2 -> "Q2 (Apr – Jun)"
                    3 -> "Q3 (Jul – Sep)"
                    else -> "Q4 (Oct – Dec)"
                }
                if (isAr) "$qAr ${config.year}" else "$qEn ${config.year}"
            }
            ReportExportScope.SEMI_ANNUAL -> {
                val hAr = if (config.halfYear == 1) "النصف الأول (يناير – يونيو)" else "النصف الثاني (يوليو – ديسمبر)"
                val hEn = if (config.halfYear == 1) "H1 (Jan – Jun)" else "H2 (Jul – Dec)"
                if (isAr) "تقرير نصف سنوي: $hAr ${config.year}" else "Semi-Annual: $hEn ${config.year}"
            }
            ReportExportScope.ANNUAL -> {
                if (isAr) "التقرير السنوي ${config.year} (يناير – ديسمبر)" else "Annual Report ${config.year} (Jan – Dec)"
            }
        }
    }

    fun resolveDurationRange(todayDate: String, durationDays: Int): Pair<String, String> {
        val safeDays = durationDays.coerceAtLeast(1)
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.US)
        val cal = Calendar.getInstance()
        val parsed = runCatching { sdf.parse(todayDate) }.getOrNull()
        if (parsed != null) {
            cal.time = parsed
        }
        val endStr = sdf.format(cal.time)
        cal.add(Calendar.DAY_OF_YEAR, -(safeDays - 1))
        val startStr = sdf.format(cal.time)
        return startStr to endStr
    }

    fun normalizeRange(startDate: String, endDate: String): Pair<String, String> {
        return if (startDate <= endDate) startDate to endDate else endDate to startDate
    }

    private fun quarterOfMonth(month: Int): Int = when (month) {
        in 1..3 -> 1
        in 4..6 -> 2
        in 7..9 -> 3
        else -> 4
    }

    private fun halfYearOfMonth(month: Int): Int = if (month in 1..6) 1 else 2
}
