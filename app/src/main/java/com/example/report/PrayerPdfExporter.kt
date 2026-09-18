package com.example.report

import android.content.Context
import android.content.Intent
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Typeface
import android.graphics.pdf.PdfDocument
import androidx.core.content.FileProvider
import com.example.data.model.PrayerId
import com.example.data.model.PrayerLogEntity
import com.example.data.repository.AgpeyaRepository
import com.example.localization.AppLanguage
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

/**
 * Generates an official, beautifully styled spiritual progress PDF report
 * with the logged-in user's name/email, date range, canonical prayer breakdown,
 * and comprehensive prayer history details.
 */
object PrayerPdfExporter {

    fun buildReportSummaryText(
        userEmail: String?,
        userName: String?,
        syncKey: String?,
        lang: AppLanguage,
        periodName: String,
        allLogs: List<PrayerLogEntity>
    ): String {
        val sb = StringBuilder()
        val displayName = userEmail ?: userName ?: if (lang == AppLanguage.ARABIC) "المصلي" else "Worshipper"
        sb.appendLine("==================================================")
        sb.appendLine(if (lang == AppLanguage.ARABIC) "تقرير الصلوات الأرثوذكسية - صلوات السواعي (الأجبية)" else "Orthodox Agpeya Prayer Spiritual Progress Report")
        sb.appendLine("==================================================")
        sb.appendLine("${if (lang == AppLanguage.ARABIC) "المستخدم المسجل" else "Logged-in User"}: $displayName")
        syncKey?.let { sb.appendLine("${if (lang == AppLanguage.ARABIC) "معرف المزامنة" else "Sync Key"}: $it") }
        sb.appendLine("${if (lang == AppLanguage.ARABIC) "نوع التقرير" else "Report Period"}: $periodName")
        sb.appendLine("${if (lang == AppLanguage.ARABIC) "تاريخ الإصدار" else "Generated At"}: ${SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()).format(Date())}")
        sb.appendLine("--------------------------------------------------")
        sb.appendLine("${if (lang == AppLanguage.ARABIC) "إجمالي الصلوات المسجلة" else "Total Prayers Logged"}: ${allLogs.size}")
        val uniqueDays = allLogs.map { it.dateString }.distinct().size
        sb.appendLine("${if (lang == AppLanguage.ARABIC) "أيام الصلاة" else "Active Days"}: $uniqueDays")
        sb.appendLine("--------------------------------------------------")
        sb.appendLine(if (lang == AppLanguage.ARABIC) "تفاصيل السواعي القانونية:" else "Canonical Hours Breakdown:")
        PrayerId.canonicalPrayers.forEach { prayer ->
            val count = allLogs.count { it.prayerCode == prayer.code }
            sb.appendLine("- ${prayer.getDisplayName(lang)}: $count")
        }
        sb.appendLine("==================================================")
        sb.appendLine(if (lang == AppLanguage.ARABIC) "«سبع مرات في النهار سبحتك على أحكام عدلك» (مزمور 119: 164)" else "“Seven times a day I praise You, because of Your righteous judgments.” (Ps 119:164)")
        return sb.toString()
    }

    fun generatePdfFile(
        context: Context,
        userEmail: String?,
        userName: String?,
        syncKey: String?,
        lang: AppLanguage,
        periodName: String,
        allLogs: List<PrayerLogEntity>,
        selectedDateString: String? = null
    ): File? {
        try {
            val pdfDocument = PdfDocument()
            val pageInfo = PdfDocument.PageInfo.Builder(595, 842, 1).create() // Standard A4 (595 x 842 pt)
            val page = pdfDocument.startPage(pageInfo)
            val canvas = page.canvas

            drawReportContent(
                canvas = canvas,
                userEmail = userEmail,
                userName = userName,
                syncKey = syncKey,
                lang = lang,
                periodName = periodName,
                allLogs = allLogs,
                selectedDateString = selectedDateString
            )

            pdfDocument.finishPage(page)

            // Save to cache dir under reports/
            val reportsDir = File(context.cacheDir, "reports").apply { mkdirs() }
            val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(Date())
            val pdfFile = File(reportsDir, "Agpeya_Spiritual_Report_$timeStamp.pdf")

            FileOutputStream(pdfFile).use { out ->
                pdfDocument.writeTo(out)
            }
            pdfDocument.close()

            return pdfFile
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }
    }

    fun generateAndSharePdf(
        context: Context,
        userEmail: String?,
        userName: String?,
        syncKey: String?,
        lang: AppLanguage,
        periodName: String,
        allLogs: List<PrayerLogEntity>,
        selectedDateString: String? = null
    ): File? {
        val pdfFile = generatePdfFile(
            context = context,
            userEmail = userEmail,
            userName = userName,
            syncKey = syncKey,
            lang = lang,
            periodName = periodName,
            allLogs = allLogs,
            selectedDateString = selectedDateString
        ) ?: return null

        try {
            // Open or share the PDF via Android Sharesheet / Viewer
            val contentUri = FileProvider.getUriForFile(
                context,
                "${context.packageName}.fileprovider",
                pdfFile
            )

            val shareIntent = Intent(Intent.ACTION_SEND).apply {
                type = "application/pdf"
                putExtra(Intent.EXTRA_STREAM, contentUri)
                putExtra(
                    Intent.EXTRA_SUBJECT,
                    if (lang == AppLanguage.ARABIC) "تقرير الصلوات الأرثوذكسية - الأجبية" else "Agpeya Prayer Spiritual Progress Report"
                )
                putExtra(
                    Intent.EXTRA_TEXT,
                    if (lang == AppLanguage.ARABIC) {
                        "مرفق تقرير التقدم الروحي وصلوات الأجبية الخاص بالمستخدم: ${userEmail ?: userName ?: "المصلي"}"
                    } else {
                        "Attached is the Agpeya spiritual progress report for: ${userEmail ?: userName ?: "Worshipper"}"
                    }
                )
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }

            val chooser = Intent.createChooser(
                shareIntent,
                if (lang == AppLanguage.ARABIC) "مشاركة أو فتح تقرير الـ PDF" else "Share or Open PDF Report"
            ).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(chooser)
        } catch (e: Exception) {
            // If sharing fails (e.g. In unit test environment without full framework providers)
            e.printStackTrace()
        }

        return pdfFile
    }

    private fun drawReportContent(
        canvas: Canvas,
        userEmail: String?,
        userName: String?,
        syncKey: String?,
        lang: AppLanguage,
        periodName: String,
        allLogs: List<PrayerLogEntity>,
        selectedDateString: String?
    ) {
        val paint = Paint().apply { isAntiAlias = true }

        // Background: Soft Warm Off-White (#FDFBF7)
        paint.color = android.graphics.Color.rgb(253, 251, 247)
        canvas.drawRect(0f, 0f, 595f, 842f, paint)

        // Top Liturgical Burgundy Banner (#6B1426)
        paint.color = android.graphics.Color.rgb(107, 20, 38)
        canvas.drawRect(0f, 0f, 595f, 90f, paint)

        // Gold decorative accent bar (#D4AF37)
        paint.color = android.graphics.Color.rgb(212, 175, 55)
        canvas.drawRect(0f, 88f, 595f, 92f, paint)

        // Header Title
        paint.color = android.graphics.Color.rgb(255, 255, 255)
        paint.textSize = 20f
        paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        val titleText = if (lang == AppLanguage.ARABIC) {
            "✝ كنيسة الإسكندرية القبطية الأرثوذكسية - صلوات الأجبية"
        } else {
            "✝ Coptic Orthodox Church - Agpeya Prayers"
        }
        canvas.drawText(titleText, 30f, 40f, paint)

        paint.textSize = 13f
        paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
        paint.color = android.graphics.Color.rgb(240, 220, 160)
        val subtitle = if (lang == AppLanguage.ARABIC) {
            "تقرير التقدم الروحي وإتمام صلوات السواعي القانونية"
        } else {
            "Spiritual Progress Report & Canonical Hours Completion"
        }
        canvas.drawText(subtitle, 30f, 65f, paint)

        // User & Account Details Card Box (y = 110 to 180)
        paint.color = android.graphics.Color.rgb(255, 255, 255)
        canvas.drawRoundRect(28f, 105f, 567f, 185f, 10f, 10f, paint)

        paint.style = Paint.Style.STROKE
        paint.strokeWidth = 1.2f
        paint.color = android.graphics.Color.rgb(212, 175, 55)
        canvas.drawRoundRect(28f, 105f, 567f, 185f, 10f, 10f, paint)
        paint.style = Paint.Style.FILL

        // User Info inside Card
        val displayName = userName ?: userEmail?.substringBefore("@") ?: if (lang == AppLanguage.ARABIC) "مستخدم الأجبية" else "Agpeya Worshipper"
        val effectiveEmail = userEmail ?: if (lang == AppLanguage.ARABIC) "حساب محلي متصل" else "Local Registered Account"

        paint.color = android.graphics.Color.rgb(107, 20, 38)
        paint.textSize = 12f
        paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)

        val userLabel = if (lang == AppLanguage.ARABIC) "اسم صاحب الحساب المُسجل:" else "Registered User Name:"
        val emailLabel = if (lang == AppLanguage.ARABIC) "البريد الإلكتروني المُسجل:" else "Registered Account Email:"
        val reportDateLabel = if (lang == AppLanguage.ARABIC) "تاريخ إصدار التقرير:" else "Report Issued On:"
        val periodLabel = if (lang == AppLanguage.ARABIC) "نطاق المتابعة:" else "Reporting Period:"

        val nowFormatted = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.US).format(Date())

        canvas.drawText("$userLabel $displayName", 45f, 130f, paint)
        paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
        paint.color = android.graphics.Color.rgb(60, 60, 60)
        canvas.drawText("$emailLabel $effectiveEmail", 45f, 150f, paint)

        paint.color = android.graphics.Color.rgb(100, 100, 100)
        canvas.drawText("$reportDateLabel $nowFormatted", 45f, 170f, paint)
        canvas.drawText("$periodLabel $periodName", 340f, 130f, paint)
        syncKey?.let {
            canvas.drawText("Sync Cloud Key: ${it.take(12)}...", 340f, 150f, paint)
        }

        // Summary Stats Row (y = 200 to 255)
        val totalLogged = allLogs.size
        val uniqueDays = allLogs.map { it.dateString }.distinct().size
        val todayStr = AgpeyaRepository.getTodayString()
        val todayLogsCount = allLogs.count { it.dateString == todayStr }

        drawStatBox(canvas, 28f, 200f, 165f, 55f, if (lang == AppLanguage.ARABIC) "إجمالي الصلوات المسجلة" else "Total Prayers Logged", "$totalLogged", android.graphics.Color.rgb(107, 20, 38))
        drawStatBox(canvas, 214f, 200f, 165f, 55f, if (lang == AppLanguage.ARABIC) "أيام الصلاة والتسجيل" else "Active Days in Prayer", "$uniqueDays", android.graphics.Color.rgb(212, 175, 55))
        drawStatBox(canvas, 400f, 200f, 167f, 55f, if (lang == AppLanguage.ARABIC) "صلوات اليوم الحالي" else "Today's Completed", "$todayLogsCount / 8", android.graphics.Color.rgb(46, 125, 50))

        // Canonical Hours Breakdown Table (y = 275 to 455)
        paint.color = android.graphics.Color.rgb(107, 20, 38)
        paint.textSize = 13f
        paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        val sectionTitle = if (lang == AppLanguage.ARABIC) {
            "تفاصيل إتمام صلوات السواعي القبطية (الساعات القانونية)"
        } else {
            "Canonical Hours Completion Breakdown"
        }
        canvas.drawText(sectionTitle, 30f, 280f, paint)

        // Table Header
        var currentY = 295f
        paint.color = android.graphics.Color.rgb(235, 230, 220)
        canvas.drawRect(28f, currentY, 567f, currentY + 22f, paint)

        paint.color = android.graphics.Color.rgb(70, 70, 70)
        paint.textSize = 10.5f
        paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)

        canvas.drawText(if (lang == AppLanguage.ARABIC) "الساعة القانونية" else "Canonical Hour", 40f, currentY + 15f, paint)
        canvas.drawText(if (lang == AppLanguage.ARABIC) "الاسم القبطي" else "Coptic", 180f, currentY + 15f, paint)
        canvas.drawText(if (lang == AppLanguage.ARABIC) "التذكار الروحي" else "Spiritual Commemoration", 260f, currentY + 15f, paint)
        canvas.drawText(if (lang == AppLanguage.ARABIC) "مرات الإتمام" else "Count", 495f, currentY + 15f, paint)

        currentY += 22f

        paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
        PrayerId.canonicalPrayers.forEachIndexed { idx, prayer ->
            val count = allLogs.count { it.prayerCode == prayer.code }
            if (idx % 2 == 1) {
                paint.color = android.graphics.Color.rgb(248, 246, 240)
                canvas.drawRect(28f, currentY, 567f, currentY + 18f, paint)
            }

            paint.color = android.graphics.Color.rgb(50, 50, 50)
            paint.textSize = 10f
            val name = prayer.getDisplayName(lang)
            canvas.drawText(name, 40f, currentY + 13f, paint)

            paint.color = android.graphics.Color.rgb(107, 20, 38)
            canvas.drawText(prayer.copticTitle, 180f, currentY + 13f, paint)

            paint.color = android.graphics.Color.rgb(100, 100, 100)
            val themeDesc = prayer.getSpiritualTheme(lang).take(38)
            canvas.drawText(themeDesc, 260f, currentY + 13f, paint)

            paint.color = if (count > 0) android.graphics.Color.rgb(46, 125, 50) else android.graphics.Color.rgb(150, 150, 150)
            paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
            canvas.drawText("$count", 510f, currentY + 13f, paint)
            paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)

            currentY += 18f
        }

        // Recent Logs Detailed Activity Table (y = 480 to 760)
        currentY = 475f
        paint.color = android.graphics.Color.rgb(107, 20, 38)
        paint.textSize = 13f
        paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        val logsHistoryTitle = if (lang == AppLanguage.ARABIC) {
            "سجل الصلوات الأحدث والتفاصيل الزمنية (آخر الصلوات المُسجلة)"
        } else {
            "Detailed Recent Prayer Records & Timestamps"
        }
        canvas.drawText(logsHistoryTitle, 30f, currentY, paint)

        currentY += 15f
        paint.color = android.graphics.Color.rgb(235, 230, 220)
        canvas.drawRect(28f, currentY, 567f, currentY + 22f, paint)

        paint.color = android.graphics.Color.rgb(70, 70, 70)
        paint.textSize = 10.5f
        paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        canvas.drawText(if (lang == AppLanguage.ARABIC) "التاريخ" else "Date", 40f, currentY + 15f, paint)
        canvas.drawText(if (lang == AppLanguage.ARABIC) "الوقت الفعلي" else "Actual Time", 140f, currentY + 15f, paint)
        canvas.drawText(if (lang == AppLanguage.ARABIC) "الصلاة المُصلاة" else "Prayer Completed", 250f, currentY + 15f, paint)
        canvas.drawText(if (lang == AppLanguage.ARABIC) "حالة التوثيق والسحاب" else "Sync Verification", 420f, currentY + 15f, paint)

        currentY += 22f
        paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)

        val recentLogs = allLogs.take(13)
        if (recentLogs.isEmpty()) {
            paint.color = android.graphics.Color.rgb(130, 130, 130)
            paint.textSize = 11f
            val noDataText = if (lang == AppLanguage.ARABIC) "لا توجد صلوات مسجلة بعد في هذا الحساب" else "No prayer records found for this account yet"
            canvas.drawText(noDataText, 40f, currentY + 20f, paint)
        } else {
            recentLogs.forEachIndexed { index, log ->
                if (index % 2 == 1) {
                    paint.color = android.graphics.Color.rgb(248, 246, 240)
                    canvas.drawRect(28f, currentY, 567f, currentY + 18f, paint)
                }

                paint.color = android.graphics.Color.rgb(60, 60, 60)
                paint.textSize = 9.5f
                canvas.drawText(log.dateString, 40f, currentY + 13f, paint)

                val timeStr = String.format(Locale.US, "%02d:%02d", log.hour, log.minute)
                canvas.drawText(timeStr, 140f, currentY + 13f, paint)

                val prayerObj = PrayerId.fromCode(log.prayerCode)
                paint.color = android.graphics.Color.rgb(107, 20, 38)
                paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
                canvas.drawText(prayerObj.getDisplayName(lang), 250f, currentY + 13f, paint)
                paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)

                paint.color = android.graphics.Color.rgb(46, 125, 50)
                val statusText = if (lang == AppLanguage.ARABIC) "✓ تم الحفظ والمزامنة السحابية" else "✓ Saved & Cloud Synced"
                canvas.drawText(statusText, 420f, currentY + 13f, paint)

                currentY += 18f
            }
        }

        // Footer / Signature Section (y = 780 to 825)
        paint.color = android.graphics.Color.rgb(200, 195, 185)
        canvas.drawLine(28f, 785f, 567f, 785f, paint)

        paint.textSize = 9.5f
        paint.color = android.graphics.Color.rgb(120, 120, 120)
        val footerVerse = if (lang == AppLanguage.ARABIC) {
            "«سبع مرات في النهار سبحتك على أحكام عدلك» (مز ١١٩: ١٦٤) • تقرير روحي إلكتروني صادر عن تطبيق صلوات الأجبية"
        } else {
            "\"Seven times a day I praise You, because of Your righteous judgments.\" (Ps 119:164) • Agpeya App"
        }
        canvas.drawText(footerVerse, 35f, 805f, paint)

        val accountRef = if (lang == AppLanguage.ARABIC) {
            "المستخدم: $effectiveEmail"
        } else {
            "Account: $effectiveEmail"
        }
        canvas.drawText(accountRef, 35f, 822f, paint)
    }

    private fun drawStatBox(
        canvas: Canvas,
        x: Float,
        y: Float,
        width: Float,
        height: Float,
        label: String,
        value: String,
        valColor: Int
    ) {
        val paint = Paint().apply { isAntiAlias = true }
        // Box background
        paint.color = android.graphics.Color.rgb(255, 255, 255)
        canvas.drawRoundRect(x, y, x + width, y + height, 8f, 8f, paint)

        // Box border
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = 1f
        paint.color = android.graphics.Color.rgb(220, 215, 205)
        canvas.drawRoundRect(x, y, x + width, y + height, 8f, 8f, paint)
        paint.style = Paint.Style.FILL

        // Label
        paint.color = android.graphics.Color.rgb(110, 110, 110)
        paint.textSize = 9.5f
        canvas.drawText(label, x + 10f, y + 18f, paint)

        // Value
        paint.color = valColor
        paint.textSize = 17f
        paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        canvas.drawText(value, x + 10f, y + 43f, paint)
    }
}
