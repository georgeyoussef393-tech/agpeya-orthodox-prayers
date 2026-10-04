package com.example.report

import android.content.Context
import android.content.Intent
import androidx.core.content.FileProvider
import com.example.data.model.ArrowPrayerLogEntity
import com.example.data.model.PrayerId
import com.example.data.model.PrayerLogEntity
import com.example.localization.AppLanguage
import java.io.File
import java.io.FileWriter
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Generates an Excel-compatible CSV spreadsheet report containing prayer logs,
 * timestamps, active days, canonical hours, and arrow prayers.
 */
object PrayerExcelExporter {

    fun generateCsvFile(
        context: Context,
        userEmail: String?,
        userName: String?,
        syncKey: String?,
        lang: AppLanguage,
        allLogs: List<PrayerLogEntity>,
        allArrowLogs: List<ArrowPrayerLogEntity> = emptyList()
    ): File? {
        try {
            val cacheDir = File(context.cacheDir, "reports").apply { mkdirs() }
            val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(Date())
            val csvFile = File(cacheDir, "Agpeya_Prayer_Report_$timeStamp.csv")

            FileWriter(csvFile).use { writer ->
                // Header Information
                writer.append("=== Orthodox Agpeya Prayer Progress Report ===\n")
                writer.append("User," + (userEmail ?: userName ?: "Worshipper") + "\n")
                if (!syncKey.isNullOrBlank()) {
                    writer.append("Sync Key,$syncKey\n")
                }
                writer.append("Generated At," + SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US).format(Date()) + "\n")
                writer.append("Total Canonical Prayers Logged,${allLogs.size}\n")
                val totalArrowBeads = allArrowLogs.sumOf { it.count }
                writer.append("Total Arrow Prayers Count,$totalArrowBeads\n")
                val allDates = (allLogs.map { it.dateString } + allArrowLogs.map { it.dateString }).distinct()
                writer.append("Active Prayer Days,${allDates.size}\n\n")

                // Canonical Breakdown Summary
                writer.append("--- Canonical Hours Breakdown ---\n")
                writer.append("Prayer Name (English),Prayer Name (Arabic),Completed Count\n")
                PrayerId.canonicalPrayers.forEach { prayer ->
                    val count = allLogs.count { it.prayerCode == prayer.code }
                    val nameEn = prayer.getDisplayName(AppLanguage.ENGLISH)
                    val nameAr = prayer.getDisplayName(AppLanguage.ARABIC)
                    writer.append("\"$nameEn\",\"$nameAr\",$count\n")
                }
                writer.append("\n")

                // Arrow Prayers Summary
                if (allArrowLogs.isNotEmpty()) {
                    writer.append("--- Arrow Prayers (الصلوات السهمية) Summary ---\n")
                    writer.append("Total Repetitions,$totalArrowBeads\n")
                    writer.append("Total Sessions,${allArrowLogs.size}\n")
                    writer.append("Log ID,Date (YYYY-MM-DD),Time,Count,Prayer Text\n")
                    allArrowLogs.sortedByDescending { it.timestamp }.forEach { arrowLog ->
                        val timeStr = SimpleDateFormat("HH:mm:ss", Locale.US).format(Date(arrowLog.timestamp))
                        val cleanText = arrowLog.prayerText.replace("\"", "\"\"")
                        writer.append("${arrowLog.id},${arrowLog.dateString},$timeStr,${arrowLog.count},\"$cleanText\"\n")
                    }
                    writer.append("\n")
                }

                // Detailed Canonical Log Entries
                writer.append("--- Detailed Canonical Prayer Log History ---\n")
                writer.append("Log ID,Date (YYYY-MM-DD),Time,Prayer Code,Prayer Title (English),Prayer Title (Arabic)\n")
                allLogs.sortedByDescending { it.timestamp }.forEach { log ->
                    val prayer = PrayerId.fromCode(log.prayerCode)
                    val dateStr = log.dateString
                    val timeStr = SimpleDateFormat("HH:mm:ss", Locale.US).format(Date(log.timestamp))
                    val nameEn = prayer.getDisplayName(AppLanguage.ENGLISH)
                    val nameAr = prayer.getDisplayName(AppLanguage.ARABIC)
                    writer.append("${log.id},$dateStr,$timeStr,${log.prayerCode},\"$nameEn\",\"$nameAr\"\n")
                }
            }

            return csvFile
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }
    }

    fun generateAndShareCsv(
        context: Context,
        userEmail: String?,
        userName: String?,
        syncKey: String?,
        lang: AppLanguage,
        allLogs: List<PrayerLogEntity>,
        allArrowLogs: List<ArrowPrayerLogEntity> = emptyList()
    ) {
        val csvFile = generateCsvFile(context, userEmail, userName, syncKey, lang, allLogs, allArrowLogs) ?: return

        try {
            val contentUri = FileProvider.getUriForFile(
                context,
                "${context.packageName}.fileprovider",
                csvFile
            )

            val shareIntent = Intent(Intent.ACTION_SEND).apply {
                type = "text/csv"
                putExtra(Intent.EXTRA_STREAM, contentUri)
                putExtra(
                    Intent.EXTRA_SUBJECT,
                    if (lang == AppLanguage.ARABIC) "تقرير الأجبية والصلوات السهمية (Excel / CSV)" else "Agpeya & Arrow Prayer Spreadsheet Report (CSV)"
                )
                putExtra(
                    Intent.EXTRA_TEXT,
                    if (lang == AppLanguage.ARABIC) {
                        "مرفق جدول بيانات الأجبية وصلوات السواعي والصلوات السهمية (يفتح ببرنامج Excel أو Google Sheets)."
                    } else {
                        "Attached is your Agpeya canonical & arrow prayers progress spreadsheet report (compatible with Excel, Google Sheets & Numbers)."
                    }
                )
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }

            val chooser = Intent.createChooser(shareIntent, if (lang == AppLanguage.ARABIC) "مشاركة ملف Excel / CSV" else "Share Spreadsheet Report")
            chooser.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(chooser)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
