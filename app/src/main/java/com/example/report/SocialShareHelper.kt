package com.example.report

import android.content.Context
import android.content.Intent
import com.example.data.model.PrayerId
import com.example.data.model.PrayerLogEntity
import com.example.localization.AppLanguage
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object SocialShareHelper {

    fun buildSocialStreakMessage(
        lang: AppLanguage,
        allLogs: List<PrayerLogEntity>
    ): String {
        val totalCount = allLogs.size
        val uniqueDays = allLogs.map { it.dateString }.distinct().size
        val activeStreak = calculateCurrentStreak(allLogs)

        val sb = StringBuilder()
        if (lang == AppLanguage.ARABIC) {
            sb.appendLine("✝️ تقرير الصلوات الأرثوذكسية - كتاب الأجبية (السواعي)")
            sb.appendLine("🔥 سلسلة الصلاة المستمرة: $activeStreak أيام متتالية")
            sb.appendLine("📅 الأيام النشطة هذا الشهر: $uniqueDays يوماً")
            sb.appendLine("🙏 إجمالي الصلوات المسجلة: $totalCount صلاة")
            sb.appendLine("")
            sb.appendLine("«سبع مرات في النهار سبحتك على أحكام عدلك» (مزمور 119: 164)")
            sb.appendLine("📲 تم التصدير عبر تطبيق صلوات الأجبية المقدسة")
        } else {
            sb.appendLine("✝️ Orthodox Agpeya Prayer Spiritual Streak Report")
            sb.appendLine("🔥 Current Prayer Streak: $activeStreak consecutive days")
            sb.appendLine("📅 Active Prayer Days: $uniqueDays days")
            sb.appendLine("🙏 Total Prayers Logged: $totalCount prayers")
            sb.appendLine("")
            sb.appendLine("“Seven times a day I praise You, because of Your righteous judgments.” (Ps 119:164)")
            sb.appendLine("📲 Exported via Holy Agpeya Prayer App")
        }
        return sb.toString()
    }

    private fun calculateCurrentStreak(logs: List<PrayerLogEntity>): Int {
        if (logs.isEmpty()) return 0
        val dateStrings = logs.map { it.dateString }.distinct().toSet()
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.US)
        var streak = 0
        val cal = java.util.Calendar.getInstance()

        // Check backwards from today
        for (i in 0..365) {
            val dateStr = sdf.format(cal.time)
            if (dateStrings.contains(dateStr)) {
                streak++
                cal.add(java.util.Calendar.DAY_OF_YEAR, -1)
            } else if (i == 0) {
                // If today hasn't been logged yet, check yesterday before breaking streak
                cal.add(java.util.Calendar.DAY_OF_YEAR, -1)
                val yesterdayStr = sdf.format(cal.time)
                if (dateStrings.contains(yesterdayStr)) {
                    // Start streak from yesterday
                    continue
                } else {
                    break
                }
            } else {
                break
            }
        }
        return if (streak == 0 && dateStrings.isNotEmpty()) 1 else streak
    }

    fun shareToSocialMedia(
        context: Context,
        lang: AppLanguage,
        allLogs: List<PrayerLogEntity>,
        packageName: String? = null // e.g. "com.whatsapp" or "org.telegram.messenger"
    ) {
        val message = buildSocialStreakMessage(lang, allLogs)

        val sendIntent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, message)
            if (!packageName.isNullOrBlank()) {
                setPackage(packageName)
            }
        }

        try {
            val chooser = Intent.createChooser(sendIntent, if (lang == AppLanguage.ARABIC) "مشاركة التقدم الروحي" else "Share Spiritual Streak")
            chooser.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(chooser)
        } catch (e: Exception) {
            // Fallback without package if specific app not installed
            try {
                val fallbackIntent = Intent(Intent.ACTION_SEND).apply {
                    type = "text/plain"
                    putExtra(Intent.EXTRA_TEXT, message)
                }
                val chooser = Intent.createChooser(fallbackIntent, if (lang == AppLanguage.ARABIC) "مشاركة التقدم الروحي" else "Share Spiritual Streak")
                chooser.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                context.startActivity(chooser)
            } catch (ex: Exception) {
                ex.printStackTrace()
            }
        }
    }
}
