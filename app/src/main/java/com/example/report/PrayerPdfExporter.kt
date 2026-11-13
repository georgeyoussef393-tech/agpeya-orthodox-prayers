package com.example.report

import android.content.Context
import android.content.Intent
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.graphics.RectF
import android.graphics.Typeface
import android.graphics.pdf.PdfDocument
import android.text.Layout
import android.text.StaticLayout
import android.text.TextDirectionHeuristics
import android.text.TextPaint
import android.text.TextUtils
import androidx.core.content.FileProvider
import com.example.data.model.ArrowPrayerLogEntity
import com.example.data.model.PrayerId
import com.example.data.model.PrayerLogEntity
import com.example.localization.AppLanguage
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.math.max
import kotlin.math.roundToInt

/**
 * Generates a polished, editorial-grade, multi-page A4 spiritual progress PDF report
 * with full RTL/LTR bidirectional typography, dynamic wrapped account & church metadata,
 * executive KPI cards, canonical hours visual progress bars, arrow prayers breakdown,
 * and paginated detailed prayer records.
 */
object PrayerPdfExporter {

    private const val PAGE_WIDTH = 595f
    private const val PAGE_HEIGHT = 842f
    private const val MARGIN_X = 30f
    private const val CONTENT_WIDTH = PAGE_WIDTH - (MARGIN_X * 2f) // 535f
    private const val CONTENT_RIGHT = PAGE_WIDTH - MARGIN_X        // 565f
    private const val FOOTER_TOP_Y = 782f
    private const val MAX_CONTENT_Y = 766f
    private const val MAX_RECENT_LOGS_IN_PDF = 80
    private const val MAX_ARROW_SESSIONS_IN_PDF = 6

    // Sacred Liturgical Editorial Palette
    private const val COLOR_BG_PARCHMENT = 0xFFFCF9F2.toInt()
    private const val COLOR_BURGUNDY = 0xFF6B1426.toInt()
    private const val COLOR_BURGUNDY_DARK = 0xFF4A0D1A.toInt()
    private const val COLOR_BURGUNDY_SOFT = 0xFFF8EFF1.toInt()
    private const val COLOR_GOLD = 0xFFD4AF37.toInt()
    private const val COLOR_GOLD_LIGHT = 0xFFF3E0A5.toInt()
    private const val COLOR_GOLD_TINT = 0xFFFBF5E2.toInt()
    private const val COLOR_SURFACE = 0xFFFFFFFF.toInt()
    private const val COLOR_ROW_ALT = 0xFFF8F4EC.toInt()
    private const val COLOR_BORDER = 0xFFE1D9C7.toInt()
    private const val COLOR_TRACK_BG = 0xFFECE6DA.toInt()
    private const val COLOR_TEXT_PRIMARY = 0xFF26221E.toInt()
    private const val COLOR_TEXT_MUTED = 0xFF706960.toInt()
    private const val COLOR_GREEN = 0xFF267630.toInt()
    private const val COLOR_GREEN_TINT = 0xFFE8F5E9.toInt()
    private const val COLOR_AMBER = 0xFFC67D16.toInt()

    private fun prayerAccentColor(code: String): Int = when (code) {
        PrayerId.PRIME.code -> 0xFFC89D28.toInt()
        PrayerId.TERCE.code -> 0xFFD98422.toInt()
        PrayerId.SEXT.code -> 0xFF80142A.toInt()
        PrayerId.NONE.code -> 0xFF5B0E2D.toInt()
        PrayerId.VESPERS.code -> 0xFFC4661C.toInt()
        PrayerId.COMPLINE.code -> 0xFF2E4057.toInt()
        PrayerId.VEIL.code -> 0xFF582260.toInt()
        PrayerId.MIDNIGHT.code -> 0xFF1B4965.toInt()
        else -> COLOR_BURGUNDY
    }

    fun buildReportSummaryText(
        userEmail: String?,
        userName: String?,
        churchName: String? = null,
        serviceName: String? = null,
        syncKey: String?,
        lang: AppLanguage,
        periodName: String,
        allLogs: List<PrayerLogEntity>,
        allArrowLogs: List<ArrowPrayerLogEntity> = emptyList()
    ): String {
        val sb = StringBuilder()
        val displayName = userName ?: userEmail ?: if (lang == AppLanguage.ARABIC) "المصلي" else "Worshipper"
        sb.appendLine("==================================================")
        sb.appendLine(if (lang == AppLanguage.ARABIC) "تقرير الصلوات الأرثوذكسية - صلوات السواعي والصلوات السهمية" else "Orthodox Agpeya & Arrow Prayers Progress Report")
        sb.appendLine("==================================================")
        sb.appendLine("${if (lang == AppLanguage.ARABIC) "الاسم المسجل" else "Logged-in User"}: $displayName")
        if (!userEmail.isNullOrBlank() && userEmail != displayName) {
            sb.appendLine("${if (lang == AppLanguage.ARABIC) "البريد / الهاتف" else "Email / Phone"}: $userEmail")
        }
        if (!churchName.isNullOrBlank()) {
            sb.appendLine("${if (lang == AppLanguage.ARABIC) "اسم الكنيسة" else "Church"}: $churchName")
        }
        if (!serviceName.isNullOrBlank()) {
            sb.appendLine("${if (lang == AppLanguage.ARABIC) "اسم الخدمة" else "Ministry / Service"}: $serviceName")
        }
        syncKey?.let { sb.appendLine("${if (lang == AppLanguage.ARABIC) "معرف المزامنة" else "Sync Key"}: $it") }
        sb.appendLine("${if (lang == AppLanguage.ARABIC) "نوع التقرير" else "Report Period"}: $periodName")
        sb.appendLine("${if (lang == AppLanguage.ARABIC) "تاريخ الإصدار" else "Generated At"}: ${SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.US).format(Date())}")
        sb.appendLine("--------------------------------------------------")
        sb.appendLine("${if (lang == AppLanguage.ARABIC) "إجمالي صلوات السواعي المسجلة" else "Total Canonical Prayers Logged"}: ${allLogs.size}")
        val totalArrowBeads = allArrowLogs.sumOf { it.count }
        sb.appendLine("${if (lang == AppLanguage.ARABIC) "إجمالي الصلوات السهمية المسجلة" else "Total Arrow Prayers Count"}: $totalArrowBeads")
        val uniqueDays = (allLogs.map { it.dateString } + allArrowLogs.map { it.dateString }).distinct().size
        sb.appendLine("${if (lang == AppLanguage.ARABIC) "أيام الصلاة" else "Active Days"}: $uniqueDays")
        sb.appendLine("--------------------------------------------------")
        sb.appendLine(if (lang == AppLanguage.ARABIC) "تفاصيل السواعي القانونية:" else "Canonical Hours Breakdown:")
        PrayerId.canonicalPrayers.forEach { prayer ->
            val count = allLogs.count { it.prayerCode == prayer.code }
            sb.appendLine("- ${prayer.getDisplayName(lang)}: $count")
        }
        if (allArrowLogs.isNotEmpty()) {
            sb.appendLine("--------------------------------------------------")
            sb.appendLine(if (lang == AppLanguage.ARABIC) "تفاصيل الصلوات السهمية:" else "Arrow Prayers Breakdown:")
            sb.appendLine("${if (lang == AppLanguage.ARABIC) "عدد الجلسات" else "Sessions"}: ${allArrowLogs.size}")
            sb.appendLine("${if (lang == AppLanguage.ARABIC) "إجمالي التكرارات" else "Total Repetitions"}: $totalArrowBeads")
        }
        sb.appendLine("==================================================")
        sb.appendLine(if (lang == AppLanguage.ARABIC) "«سبع مرات في النهار سبحتك على أحكام عدلك» (مزمور 119: 164)" else "“Seven times a day I praise You, because of Your righteous judgments.” (Ps 119:164)")
        return sb.toString()
    }

    fun generatePdfFile(
        context: Context,
        userEmail: String?,
        userName: String?,
        churchName: String? = null,
        serviceName: String? = null,
        churchEmblem: String = "coptic_cross",
        syncKey: String?,
        lang: AppLanguage,
        periodName: String,
        allLogs: List<PrayerLogEntity>,
        allArrowLogs: List<ArrowPrayerLogEntity> = emptyList(),
        selectedDateString: String? = null,
        isCloudConnected: Boolean = false
    ): File? {
        return try {
            val data = ReportInput(
                userEmail = userEmail,
                userName = userName,
                churchName = churchName,
                serviceName = serviceName,
                churchEmblem = churchEmblem,
                syncKey = syncKey,
                lang = lang,
                periodName = periodName,
                allLogs = allLogs.sortedByDescending { it.timestamp },
                allArrowLogs = allArrowLogs.sortedByDescending { it.timestamp },
                selectedDateString = selectedDateString,
                isCloudConnected = isCloudConnected
            )

            // Pass 1: measure total page count cleanly
            val totalPages = renderDocument(pageTarget = null, data = data, totalPagesHint = 1)

            // Pass 2: render final PDF pages with exact "Page X of Y" footers
            val reportsDir = File(context.cacheDir, "reports").apply { mkdirs() }
            val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(Date())
            val pdfFile = File(reportsDir, "Agpeya_Spiritual_Report_$timeStamp.pdf")

            val pdfDocument = PdfDocument()
            try {
                renderDocument(pageTarget = PdfDocumentPageTarget(pdfDocument), data = data, totalPagesHint = totalPages)
                FileOutputStream(pdfFile).use { out ->
                    pdfDocument.writeTo(out)
                }
            } finally {
                pdfDocument.close()
            }
            pdfFile
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    internal fun renderPagesToCanvasesForTest(
        userEmail: String?,
        userName: String?,
        churchName: String? = null,
        serviceName: String? = null,
        churchEmblem: String = "coptic_cross",
        syncKey: String? = null,
        lang: AppLanguage,
        periodName: String,
        allLogs: List<PrayerLogEntity>,
        allArrowLogs: List<ArrowPrayerLogEntity> = emptyList(),
        isCloudConnected: Boolean = false,
        onPageCanvasCreated: (pageNumber: Int) -> Canvas
    ): Int {
        val data = ReportInput(
            userEmail = userEmail,
            userName = userName,
            churchName = churchName,
            serviceName = serviceName,
            churchEmblem = churchEmblem,
            syncKey = syncKey,
            lang = lang,
            periodName = periodName,
            allLogs = allLogs.sortedByDescending { it.timestamp },
            allArrowLogs = allArrowLogs.sortedByDescending { it.timestamp },
            selectedDateString = null,
            isCloudConnected = isCloudConnected
        )
        val totalPages = renderDocument(pageTarget = null, data = data, totalPagesHint = 1)
        val testTarget = object : PageTarget {
            override fun startPage(pageNumber: Int): Canvas = onPageCanvasCreated(pageNumber)
            override fun finishPage() = Unit
        }
        return renderDocument(pageTarget = testTarget, data = data, totalPagesHint = totalPages)
    }

    fun generateAndSharePdf(
        context: Context,
        userEmail: String?,
        userName: String?,
        churchName: String? = null,
        serviceName: String? = null,
        churchEmblem: String = "coptic_cross",
        syncKey: String?,
        lang: AppLanguage,
        periodName: String,
        allLogs: List<PrayerLogEntity>,
        allArrowLogs: List<ArrowPrayerLogEntity> = emptyList(),
        selectedDateString: String? = null,
        isCloudConnected: Boolean = false
    ): File? {
        val pdfFile = generatePdfFile(
            context = context,
            userEmail = userEmail,
            userName = userName,
            churchName = churchName,
            serviceName = serviceName,
            churchEmblem = churchEmblem,
            syncKey = syncKey,
            lang = lang,
            periodName = periodName,
            allLogs = allLogs,
            allArrowLogs = allArrowLogs,
            selectedDateString = selectedDateString,
            isCloudConnected = isCloudConnected
        ) ?: return null

        try {
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
                        "مرفق تقرير التقدم الروحي وصلوات الأجبية الخاص بالمستخدم: ${userName ?: userEmail ?: "المصلي"}"
                    } else {
                        "Attached is the Agpeya spiritual progress report for: ${userName ?: userEmail ?: "Worshipper"}"
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
            e.printStackTrace()
        }

        return pdfFile
    }

    private data class ReportInput(
        val userEmail: String?,
        val userName: String?,
        val churchName: String?,
        val serviceName: String?,
        val churchEmblem: String,
        val syncKey: String?,
        val lang: AppLanguage,
        val periodName: String,
        val allLogs: List<PrayerLogEntity>,
        val allArrowLogs: List<ArrowPrayerLogEntity>,
        val selectedDateString: String?,
        val isCloudConnected: Boolean
    )

    private interface PageTarget {
        fun startPage(pageNumber: Int): Canvas
        fun finishPage()
    }

    private class PdfDocumentPageTarget(
        private val pdfDocument: PdfDocument
    ) : PageTarget {
        private var activePage: PdfDocument.Page? = null

        override fun startPage(pageNumber: Int): Canvas {
            val info = PdfDocument.PageInfo.Builder(PAGE_WIDTH.toInt(), PAGE_HEIGHT.toInt(), pageNumber).create()
            val page = pdfDocument.startPage(info)
            activePage = page
            return page.canvas
        }

        override fun finishPage() {
            activePage?.let { pdfDocument.finishPage(it) }
            activePage = null
        }
    }

    private class PageContext(
        private val pageTarget: PageTarget?,
        private val data: ReportInput,
        private val totalPagesHint: Int
    ) {
        var pageNumber: Int = 0
            private set
        var canvas: Canvas? = null
            private set
        var cursorY: Float = 0f

        fun startFirstPage() {
            beginPageInternal(isCoverPage = true)
        }

        fun ensureSpace(requiredHeight: Float, onPageBreak: (() -> Unit)? = null) {
            if (cursorY + requiredHeight > MAX_CONTENT_Y) {
                finishCurrentPage()
                beginPageInternal(isCoverPage = false)
                onPageBreak?.invoke()
            }
        }

        fun finishCurrentPage() {
            canvas?.let { c ->
                drawPageFooter(c, data, pageNumber, totalPagesHint)
            }
            pageTarget?.finishPage()
            canvas = null
        }

        private fun beginPageInternal(isCoverPage: Boolean) {
            pageNumber++
            if (pageTarget != null) {
                val pageCanvas = pageTarget.startPage(pageNumber)
                canvas = pageCanvas
                drawPageBackdrop(pageCanvas)
            } else {
                canvas = null
            }

            if (isCoverPage) {
                canvas?.let { drawCoverBanner(it, data) }
                cursorY = 108f
            } else {
                canvas?.let { drawContinuationBanner(it, data, pageNumber) }
                cursorY = 64f
            }
        }
    }

    private fun renderDocument(
        pageTarget: PageTarget?,
        data: ReportInput,
        totalPagesHint: Int
    ): Int {
        val ctx = PageContext(pageTarget, data, totalPagesHint)
        ctx.startFirstPage()

        // 1. Worshipper & Account Profile Card (dynamically measured)
        drawIdentityCard(ctx, data)

        // 2. Executive KPI Summary Strip (4 balanced cards)
        ctx.ensureSpace(74f)
        ctx.canvas?.let { drawKpiSummaryRow(it, ctx.cursorY, data) }
        ctx.cursorY += 74f

        // 3. Canonical Hours Breakdown Table with visual progress bars & total row
        drawCanonicalHoursSection(ctx, data)

        // 4. Arrow Prayers Section (if user has arrow prayer logs)
        if (data.allArrowLogs.isNotEmpty()) {
            drawArrowPrayersSection(ctx, data)
        }

        // 5. Detailed Chronological Prayer Activity Log (paginated cleanly across pages)
        drawDetailedLogsSection(ctx, data)

        ctx.finishCurrentPage()
        return ctx.pageNumber
    }

    private fun drawPageBackdrop(canvas: Canvas) {
        val paint = Paint(Paint.ANTI_ALIAS_FLAG)
        paint.color = COLOR_BG_PARCHMENT
        canvas.drawRect(0f, 0f, PAGE_WIDTH, PAGE_HEIGHT, paint)

        // Subtle outer liturgical border frame
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = 0.8f
        paint.color = 0x5AD4AF37
        canvas.drawRect(12f, 12f, PAGE_WIDTH - 12f, PAGE_HEIGHT - 12f, paint)
    }

    private fun drawCoverBanner(canvas: Canvas, data: ReportInput) {
        val isRtl = data.lang.isRtl
        val paint = Paint(Paint.ANTI_ALIAS_FLAG)

        // Deep burgundy header banner
        paint.color = COLOR_BURGUNDY
        canvas.drawRect(0f, 0f, PAGE_WIDTH, 92f, paint)

        // Subtle darker lower strip for editorial depth
        paint.color = COLOR_BURGUNDY_DARK
        canvas.drawRect(0f, 70f, PAGE_WIDTH, 92f, paint)

        // Double gold accent bottom rule
        paint.color = COLOR_GOLD
        canvas.drawRect(0f, 92f, PAGE_WIDTH, 95f, paint)
        paint.color = COLOR_GOLD_LIGHT
        canvas.drawRect(0f, 96f, PAGE_WIDTH, 97f, paint)

        // Emblem placed opposite to the reading start side
        val emblemCx = if (isRtl) 62f else PAGE_WIDTH - 62f
        drawChurchEmblemBadge(canvas, emblemCx, 46f, data.churchEmblem)

        val textLeft = if (isRtl) 104f else MARGIN_X
        val textWidth = PAGE_WIDTH - MARGIN_X - 104f // ~461f

        val titleText = if (data.lang == AppLanguage.ARABIC) {
            "الكنيسة القبطية الأرثوذكسية — تقرير صلوات الأجبية"
        } else {
            "Coptic Orthodox Church — Agpeya Progress Report"
        }
        val titlePaint = makeTextPaint(16.5f, Color.WHITE, bold = true)
        val titleLayout = buildStaticLayout(titleText, titlePaint, textWidth, isRtl)
        canvas.save()
        canvas.translate(textLeft, 18f)
        titleLayout.draw(canvas)
        canvas.restore()

        val subtitleText = buildString {
            if (!data.churchName.isNullOrBlank()) {
                append(if (data.lang == AppLanguage.ARABIC) "كنيسة: ${data.churchName}" else "Church: ${data.churchName}")
            }
            if (!data.serviceName.isNullOrBlank()) {
                if (isNotEmpty()) append("  •  ")
                append(if (data.lang == AppLanguage.ARABIC) "خدمة: ${data.serviceName}" else "Ministry: ${data.serviceName}")
            }
            if (isEmpty()) {
                append(
                    if (data.lang == AppLanguage.ARABIC) {
                        "تقرير متابعة التقدم الروحي وإتمام صلوات السواعي والصلوات السهمية"
                    } else {
                        "Spiritual Progress Report for Canonical Hours & Arrow Prayers"
                    }
                )
            }
        }
        val subtitlePaint = makeTextPaint(10.5f, COLOR_GOLD_LIGHT, bold = false)
        val subLayout = buildStaticLayout(subtitleText, subtitlePaint, textWidth, isRtl)
        canvas.save()
        canvas.translate(textLeft, 18f + titleLayout.height + 5f)
        subLayout.draw(canvas)
        canvas.restore()

        // Crisp pill tag inside the dark lower band
        val tagText = if (data.lang == AppLanguage.ARABIC) {
            "نطاق التقرير: ${data.periodName}"
        } else {
            "Period: ${data.periodName}"
        }
        val tagPaint = makeTextPaint(9f, COLOR_GOLD_LIGHT, bold = true)
        drawAlignedSingleLine(canvas, tagText, textLeft, textLeft + textWidth, 84f, tagPaint, isRtl)
    }

    private fun drawContinuationBanner(canvas: Canvas, data: ReportInput, pageNumber: Int) {
        val isRtl = data.lang.isRtl
        val paint = Paint(Paint.ANTI_ALIAS_FLAG)
        paint.color = COLOR_BURGUNDY
        canvas.drawRect(0f, 0f, PAGE_WIDTH, 46f, paint)
        paint.color = COLOR_GOLD
        canvas.drawRect(0f, 46f, PAGE_WIDTH, 48.5f, paint)

        val displayName = data.userName?.takeIf { it.isNotBlank() }
            ?: data.userEmail?.substringBefore("@")
            ?: if (data.lang == AppLanguage.ARABIC) "المصلي" else "Worshipper"

        val headerTitle = if (data.lang == AppLanguage.ARABIC) {
            "✝ تقرير صلوات الأجبية — $displayName (متابعة السجل)"
        } else {
            "✝ Agpeya Progress Report — $displayName (Continued)"
        }
        val titlePaint = makeTextPaint(11.5f, Color.WHITE, bold = true)
        drawAlignedSingleLine(canvas, headerTitle, MARGIN_X, CONTENT_RIGHT, 28f, titlePaint, isRtl)

        val pageLabel = if (data.lang == AppLanguage.ARABIC) "صفحة $pageNumber" else "Page $pageNumber"
        val pagePaint = makeTextPaint(9.5f, COLOR_GOLD_LIGHT, bold = true)
        drawAlignedSingleLine(canvas, pageLabel, MARGIN_X, CONTENT_RIGHT, 28f, pagePaint, !isRtl)
    }

    private fun drawIdentityCard(ctx: PageContext, data: ReportInput) {
        val isRtl = data.lang.isRtl
        val displayName = data.userName?.takeIf { it.isNotBlank() }
            ?: data.userEmail?.substringBefore("@")?.takeIf { it.isNotBlank() }
            ?: if (data.lang == AppLanguage.ARABIC) "مستخدم الأجبية" else "Agpeya Worshipper"
        val effectiveContact = data.userEmail?.takeIf { it.isNotBlank() }
            ?: if (data.lang == AppLanguage.ARABIC) "حساب محلي" else "Local Device Profile"
        val notSetText = if (data.lang == AppLanguage.ARABIC) "غير مسجل" else "Not specified"
        val churchVal = data.churchName?.takeIf { it.isNotBlank() } ?: notSetText
        val serviceVal = data.serviceName?.takeIf { it.isNotBlank() } ?: notSetText
        val issuedAt = SimpleDateFormat("yyyy-MM-dd  HH:mm", Locale.US).format(Date())

        val labelPrimary1 = if (data.lang == AppLanguage.ARABIC) "الاسم المسجل" else "Registered Name"
        val labelPrimary2 = if (data.lang == AppLanguage.ARABIC) "البريد / الهاتف" else "Email / Phone"
        val labelPrimary3 = if (data.lang == AppLanguage.ARABIC) "اسم الكنيسة" else "Church"
        val labelPrimary4 = if (data.lang == AppLanguage.ARABIC) "اسم الخدمة" else "Ministry / Service"

        val labelMeta1 = if (data.lang == AppLanguage.ARABIC) "نطاق التقرير" else "Report Period"
        val labelMeta2 = if (data.lang == AppLanguage.ARABIC) "تاريخ الإصدار" else "Issued On"
        val labelMeta3 = if (data.lang == AppLanguage.ARABIC) "حالة المزامنة" else "Cloud Status"
        val cloudStatusText = if (data.isCloudConnected) {
            if (data.lang == AppLanguage.ARABIC) "✓ متصل ومؤمّن سحابياً" else "✓ Cloud Synced"
        } else {
            if (data.lang == AppLanguage.ARABIC) "محفوظ محلياً على الجهاز" else "Saved Locally"
        }
        val labelMeta4 = if (data.lang == AppLanguage.ARABIC) "معرف المزامنة" else "Sync Key"
        val syncKeyShort = data.syncKey?.take(18)?.let { if ((data.syncKey.length) > 18) "$it…" else it } ?: "—"

        val colGap = 16f
        val innerPad = 14f
        val primaryColWidth = 292f
        val metaColWidth = CONTENT_WIDTH - (innerPad * 2f) - colGap - primaryColWidth

        val primaryLeft = if (isRtl) MARGIN_X + innerPad + metaColWidth + colGap else MARGIN_X + innerPad
        val metaLeft = if (isRtl) MARGIN_X + innerPad else MARGIN_X + innerPad + primaryColWidth + colGap
        val dividerX = if (isRtl) MARGIN_X + innerPad + metaColWidth + (colGap / 2f) else MARGIN_X + innerPad + primaryColWidth + (colGap / 2f)

        val rowPaintLabel = makeTextPaint(8.8f, COLOR_TEXT_MUTED, bold = true)
        val rowPaintValue = makeTextPaint(10f, COLOR_TEXT_PRIMARY, bold = true)
        val rowPaintSecondary = makeTextPaint(9.5f, COLOR_TEXT_PRIMARY, bold = false)
        val rowPaintStatus = makeTextPaint(9.5f, if (data.isCloudConnected) COLOR_GREEN else COLOR_BURGUNDY, bold = true)

        val primaryPairs = listOf(
            labelPrimary1 to displayName,
            labelPrimary2 to effectiveContact,
            labelPrimary3 to churchVal,
            labelPrimary4 to serviceVal
        )
        val metaPairs = listOf(
            Triple(labelMeta1, data.periodName, rowPaintValue),
            Triple(labelMeta2, issuedAt, rowPaintSecondary),
            Triple(labelMeta3, cloudStatusText, rowPaintStatus),
            Triple(labelMeta4, syncKeyShort, rowPaintSecondary)
        )

        // Pre-measure heights using StaticLayout so text never overflows or overlaps
        val primaryLayouts = primaryPairs.mapIndexed { idx, (lbl, value) ->
            val lLayout = buildStaticLayout(lbl, rowPaintLabel, 92f, isRtl)
            val vLayout = buildStaticLayout(value, if (idx == 0) rowPaintValue else rowPaintSecondary, primaryColWidth - 98f, isRtl)
            Pair(lLayout, vLayout) to max(lLayout.height, vLayout.height).toFloat()
        }
        val metaLayouts = metaPairs.map { (lbl, value, vPaint) ->
            val lLayout = buildStaticLayout(lbl, rowPaintLabel, 76f, isRtl)
            val vLayout = buildStaticLayout(value, vPaint, metaColWidth - 82f, isRtl)
            Pair(lLayout, vLayout) to max(lLayout.height, vLayout.height).toFloat()
        }

        val headerStripHeight = 24f
        val primaryBodyHeight = primaryLayouts.sumOf { it.second.toDouble() }.toFloat() + (primaryLayouts.size - 1) * 6f
        val metaBodyHeight = metaLayouts.sumOf { it.second.toDouble() }.toFloat() + (metaLayouts.size - 1) * 6f
        val cardBodyHeight = max(primaryBodyHeight, metaBodyHeight) + 20f
        val totalCardHeight = headerStripHeight + cardBodyHeight

        ctx.ensureSpace(totalCardHeight + 12f)
        val topY = ctx.cursorY

        ctx.canvas?.let { c ->
            val paint = Paint(Paint.ANTI_ALIAS_FLAG)
            val cardRect = RectF(MARGIN_X, topY, CONTENT_RIGHT, topY + totalCardHeight)

            // Card fill
            paint.color = COLOR_SURFACE
            c.drawRoundRect(cardRect, 10f, 10f, paint)

            // Header strip inside card
            c.save()
            val clipPath = Path().apply { addRoundRect(cardRect, 10f, 10f, Path.Direction.CW) }
            c.clipPath(clipPath)
            paint.color = COLOR_BURGUNDY_SOFT
            c.drawRect(MARGIN_X, topY, CONTENT_RIGHT, topY + headerStripHeight, paint)
            paint.color = COLOR_GOLD
            c.drawRect(MARGIN_X, topY + headerStripHeight - 1.2f, CONTENT_RIGHT, topY + headerStripHeight, paint)
            c.restore()

            // Outer border
            paint.style = Paint.Style.STROKE
            paint.strokeWidth = 1.1f
            paint.color = COLOR_BORDER
            c.drawRoundRect(cardRect, 10f, 10f, paint)
            paint.style = Paint.Style.FILL

            // Card ribbon title
            val cardTitle = if (data.lang == AppLanguage.ARABIC) {
                "بيانات صاحب الحساب والخدمة الكنسية"
            } else {
                "Worshipper & Ministry Profile"
            }
            val cardTitlePaint = makeTextPaint(10f, COLOR_BURGUNDY, bold = true)
            drawAlignedSingleLine(
                c,
                cardTitle,
                MARGIN_X + innerPad,
                CONTENT_RIGHT - innerPad,
                topY + 16f,
                cardTitlePaint,
                isRtl
            )

            // Subtle vertical column divider
            paint.color = COLOR_BORDER
            paint.strokeWidth = 0.8f
            c.drawLine(dividerX, topY + headerStripHeight + 8f, dividerX, topY + totalCardHeight - 8f, paint)

            // Draw Primary Column rows
            var rowY = topY + headerStripHeight + 10f
            primaryLayouts.forEach { (layouts, rowH) ->
                val (lLayout, vLayout) = layouts
                val lblX = if (isRtl) primaryLeft + (primaryColWidth - 92f) else primaryLeft
                val valX = if (isRtl) primaryLeft else primaryLeft + 98f

                c.save()
                c.translate(lblX, rowY)
                lLayout.draw(c)
                c.restore()

                c.save()
                c.translate(valX, rowY)
                vLayout.draw(c)
                c.restore()

                rowY += rowH + 6f
            }

            // Draw Metadata Column rows
            rowY = topY + headerStripHeight + 10f
            metaLayouts.forEach { (layouts, rowH) ->
                val (lLayout, vLayout) = layouts
                val lblX = if (isRtl) metaLeft + (metaColWidth - 76f) else metaLeft
                val valX = if (isRtl) metaLeft else metaLeft + 82f

                c.save()
                c.translate(lblX, rowY)
                lLayout.draw(c)
                c.restore()

                c.save()
                c.translate(valX, rowY)
                vLayout.draw(c)
                c.restore()

                rowY += rowH + 6f
            }
        }

        ctx.cursorY += totalCardHeight + 12f
    }

    private fun drawKpiSummaryRow(canvas: Canvas, topY: Float, data: ReportInput) {
        val isRtl = data.lang.isRtl
        val totalCanonical = data.allLogs.size
        val totalArrowBeads = data.allArrowLogs.sumOf { it.count }
        val arrowSessions = data.allArrowLogs.size
        val uniqueDays = (data.allLogs.map { it.dateString } + data.allArrowLogs.map { it.dateString }).distinct().size

        val topPrayerEntry = PrayerId.canonicalPrayers
            .map { it to data.allLogs.count { log -> log.prayerCode == it.code } }
            .maxByOrNull { it.second }
        val topPrayerName = if (topPrayerEntry != null && topPrayerEntry.second > 0) {
            topPrayerEntry.first.getDisplayName(data.lang)
        } else {
            "—"
        }
        val topPrayerSub = if (topPrayerEntry != null && topPrayerEntry.second > 0) {
            if (data.lang == AppLanguage.ARABIC) "${topPrayerEntry.second} مرة" else "${topPrayerEntry.second} times"
        } else {
            if (data.lang == AppLanguage.ARABIC) "لا يوجد سجل بعد" else "No logs yet"
        }

        val cards = listOf(
            KpiCard(
                label = if (data.lang == AppLanguage.ARABIC) "صلوات السواعي" else "Canonical Prayers",
                value = "$totalCanonical",
                sub = if (data.lang == AppLanguage.ARABIC) "إجمالي السواعي المسجلة" else "Total hours completed",
                accent = COLOR_BURGUNDY
            ),
            KpiCard(
                label = if (data.lang == AppLanguage.ARABIC) "الصلوات السهمية" else "Arrow Prayers",
                value = "$totalArrowBeads",
                sub = if (data.lang == AppLanguage.ARABIC) "$arrowSessions جلسة تسبيح" else "$arrowSessions sessions",
                accent = COLOR_GOLD
            ),
            KpiCard(
                label = if (data.lang == AppLanguage.ARABIC) "أيام المواظبة" else "Active Days",
                value = "$uniqueDays",
                sub = if (data.lang == AppLanguage.ARABIC) "يوم صلاة موثق" else "Days with prayers",
                accent = COLOR_GREEN
            ),
            KpiCard(
                label = if (data.lang == AppLanguage.ARABIC) "الأكثر مواظبة" else "Most Prayed Hour",
                value = topPrayerName,
                sub = topPrayerSub,
                accent = COLOR_AMBER,
                smallerValue = true
            )
        )

        val orderedCards = if (isRtl) cards.reversed() else cards
        val gap = 9f
        val cardWidth = (CONTENT_WIDTH - gap * 3f) / 4f
        val cardHeight = 64f

        orderedCards.forEachIndexed { idx, card ->
            val x = MARGIN_X + idx * (cardWidth + gap)
            drawSingleKpiCard(canvas, x, topY, cardWidth, cardHeight, card, isRtl)
        }
    }

    private data class KpiCard(
        val label: String,
        val value: String,
        val sub: String,
        val accent: Int,
        val smallerValue: Boolean = false
    )

    private fun drawSingleKpiCard(
        canvas: Canvas,
        x: Float,
        y: Float,
        width: Float,
        height: Float,
        card: KpiCard,
        isRtl: Boolean
    ) {
        val paint = Paint(Paint.ANTI_ALIAS_FLAG)
        val rect = RectF(x, y, x + width, y + height)

        paint.color = COLOR_SURFACE
        canvas.drawRoundRect(rect, 9f, 9f, paint)

        // Top colored accent bar clipped cleanly
        canvas.save()
        val clip = Path().apply { addRoundRect(rect, 9f, 9f, Path.Direction.CW) }
        canvas.clipPath(clip)
        paint.color = card.accent
        canvas.drawRect(x, y, x + width, y + 4f, paint)
        canvas.restore()

        paint.style = Paint.Style.STROKE
        paint.strokeWidth = 0.9f
        paint.color = COLOR_BORDER
        canvas.drawRoundRect(rect, 9f, 9f, paint)
        paint.style = Paint.Style.FILL

        val pad = 9f
        val labelPaint = makeTextPaint(8.5f, COLOR_TEXT_MUTED, bold = true)
        drawAlignedSingleLine(canvas, card.label, x + pad, x + width - pad, y + 18f, labelPaint, isRtl)

        val valSize = if (card.smallerValue && card.value.length > 8) 12.5f else if (card.smallerValue) 14f else 18f
        val valPaint = makeTextPaint(valSize, card.accent, bold = true)
        drawAlignedSingleLine(canvas, card.value, x + pad, x + width - pad, y + 39f, valPaint, isRtl)

        val subPaint = makeTextPaint(7.8f, COLOR_TEXT_MUTED, bold = false)
        drawAlignedSingleLine(canvas, card.sub, x + pad, x + width - pad, y + 54f, subPaint, isRtl)
    }

    private fun drawSectionHeading(canvas: Canvas, y: Float, title: String, subtitle: String?, isRtl: Boolean) {
        val paint = Paint(Paint.ANTI_ALIAS_FLAG)
        paint.color = COLOR_GOLD
        if (isRtl) {
            canvas.drawRoundRect(CONTENT_RIGHT - 4f, y - 11f, CONTENT_RIGHT, y + 3f, 2f, 2f, paint)
        } else {
            canvas.drawRoundRect(MARGIN_X, y - 11f, MARGIN_X + 4f, y + 3f, 2f, 2f, paint)
        }

        val titlePaint = makeTextPaint(12f, COLOR_BURGUNDY, bold = true)
        val titleLeft = if (isRtl) MARGIN_X else MARGIN_X + 10f
        val titleRight = if (isRtl) CONTENT_RIGHT - 10f else CONTENT_RIGHT
        drawAlignedSingleLine(canvas, title, titleLeft, titleRight, y, titlePaint, isRtl)

        if (!subtitle.isNullOrBlank()) {
            val subPaint = makeTextPaint(8.8f, COLOR_TEXT_MUTED, bold = false)
            drawAlignedSingleLine(canvas, subtitle, titleLeft, titleRight, y, subPaint, !isRtl)
        }
    }

    private fun drawCanonicalHoursSection(ctx: PageContext, data: ReportInput) {
        val isRtl = data.lang.isRtl
        val prayers = PrayerId.canonicalPrayers
        val counts = prayers.associate { it.code to data.allLogs.count { log -> log.prayerCode == it.code } }
        val totalLogs = data.allLogs.size
        val maxCount = max(1, counts.values.maxOrNull() ?: 1)

        val headerH = 24f
        val rowH = 21f
        val totalRowH = 22f
        val sectionHeight = 20f + headerH + (prayers.size * rowH) + totalRowH + 14f

        ctx.ensureSpace(sectionHeight)

        ctx.canvas?.let { c ->
            val headingTitle = if (data.lang == AppLanguage.ARABIC) {
                "توزيع الساعات القانونية (صلوات السواعي القبطية)"
            } else {
                "Canonical Hours Completion Breakdown"
            }
            val headingSub = if (data.lang == AppLanguage.ARABIC) {
                "النسبة المئوية من إجمالي الصلوات"
            } else {
                "Share of total logged hours"
            }
            drawSectionHeading(c, ctx.cursorY + 12f, headingTitle, headingSub, isRtl)

            val tableTop = ctx.cursorY + 20f
            val tableBottom = tableTop + headerH + (prayers.size * rowH) + totalRowH
            val tableRect = RectF(MARGIN_X, tableTop, CONTENT_RIGHT, tableBottom)

            val paint = Paint(Paint.ANTI_ALIAS_FLAG)
            paint.color = COLOR_SURFACE
            c.drawRoundRect(tableRect, 9f, 9f, paint)

            c.save()
            val clip = Path().apply { addRoundRect(tableRect, 9f, 9f, Path.Direction.CW) }
            c.clipPath(clip)

            // Burgundy Table Header
            paint.color = COLOR_BURGUNDY
            c.drawRect(MARGIN_X, tableTop, CONTENT_RIGHT, tableTop + headerH, paint)

            // Column X bounds (535pt width)
            // LTR: Hour [38..148] | Coptic [154..234] | Theme [240..388] | Progress [396..482] | Count [490..557]
            // RTL: Count [38..105] | Progress [113..199] | Theme [207..355] | Coptic [361..441] | Hour [447..557]
            val headerPaint = makeTextPaint(9.2f, Color.WHITE, bold = true)
            val headerY = tableTop + 15.5f

            if (isRtl) {
                drawAlignedSingleLine(c, "الساعة القانونية", 447f, 555f, headerY, headerPaint, true)
                drawAlignedSingleLine(c, "بالقبطية", 361f, 441f, headerY, headerPaint, true)
                drawAlignedSingleLine(c, "التذكار الروحي", 207f, 355f, headerY, headerPaint, true)
                drawAlignedSingleLine(c, "مؤشر الإنجاز", 113f, 199f, headerY, headerPaint, true)
                drawAlignedSingleLine(c, "العدد", 38f, 105f, headerY, headerPaint, true)
            } else {
                drawAlignedSingleLine(c, "Canonical Hour", 38f, 148f, headerY, headerPaint, false)
                drawAlignedSingleLine(c, "Coptic", 154f, 234f, headerY, headerPaint, false)
                drawAlignedSingleLine(c, "Commemoration", 240f, 388f, headerY, headerPaint, false)
                drawAlignedSingleLine(c, "Progress", 396f, 482f, headerY, headerPaint, false)
                drawAlignedSingleLine(c, "Count", 490f, 557f, headerY, headerPaint, false, alignEnd = true)
            }

            var y = tableTop + headerH
            val cellPaint = makeTextPaint(9.2f, COLOR_TEXT_PRIMARY, bold = false)
            val boldCellPaint = makeTextPaint(9.2f, COLOR_TEXT_PRIMARY, bold = true)
            val mutedCellPaint = makeTextPaint(8.5f, COLOR_TEXT_MUTED, bold = false)

            prayers.forEachIndexed { idx, prayer ->
                val count = counts[prayer.code] ?: 0
                val accent = prayerAccentColor(prayer.code)

                if (idx % 2 == 1) {
                    paint.color = COLOR_ROW_ALT
                    c.drawRect(MARGIN_X, y, CONTENT_RIGHT, y + rowH, paint)
                }

                // Hairline row divider
                paint.color = COLOR_BORDER
                paint.strokeWidth = 0.5f
                c.drawLine(MARGIN_X, y + rowH, CONTENT_RIGHT, y + rowH, paint)

                val baseY = y + 14f
                val name = prayer.getDisplayName(data.lang)
                val coptic = prayer.copticTitle
                val theme = prayer.getSpiritualTheme(data.lang)
                val pct = if (totalLogs > 0) ((count * 100f) / totalLogs).roundToInt() else 0
                val countLabel = if (count > 0) "$count ($pct%)" else "0"
                val ratio = (count.toFloat() / maxCount.toFloat()).coerceIn(0f, 1f)

                val countPaint = makeTextPaint(
                    9.2f,
                    if (count > 0) COLOR_GREEN else COLOR_TEXT_MUTED,
                    bold = count > 0
                )
                val copticPaint = makeTextPaint(8.8f, COLOR_BURGUNDY, bold = false)

                if (isRtl) {
                    // Dot badge beside prayer name on the right
                    paint.color = accent
                    c.drawCircle(553f, y + rowH / 2f, 3.2f, paint)
                    drawAlignedSingleLine(c, name, 447f, 545f, baseY, boldCellPaint, true)
                    drawAlignedSingleLine(c, coptic, 361f, 441f, baseY, copticPaint, true)
                    drawAlignedSingleLine(c, theme, 207f, 355f, baseY, mutedCellPaint, true)

                    // RTL progress bar fills from right to left
                    val barLeft = 115f
                    val barRight = 197f
                    val barTop = y + 7.5f
                    val barBottom = y + 13.5f
                    paint.color = COLOR_TRACK_BG
                    c.drawRoundRect(barLeft, barTop, barRight, barBottom, 3f, 3f, paint)
                    if (count > 0) {
                        val fillW = max(6f, (barRight - barLeft) * ratio)
                        paint.color = accent
                        c.drawRoundRect(barRight - fillW, barTop, barRight, barBottom, 3f, 3f, paint)
                    }

                    drawAlignedSingleLine(c, countLabel, 38f, 105f, baseY, countPaint, true)
                } else {
                    paint.color = accent
                    c.drawCircle(42f, y + rowH / 2f, 3.2f, paint)
                    drawAlignedSingleLine(c, name, 50f, 148f, baseY, boldCellPaint, false)
                    drawAlignedSingleLine(c, coptic, 154f, 234f, baseY, copticPaint, false)
                    drawAlignedSingleLine(c, theme, 240f, 388f, baseY, mutedCellPaint, false)

                    val barLeft = 396f
                    val barRight = 480f
                    val barTop = y + 7.5f
                    val barBottom = y + 13.5f
                    paint.color = COLOR_TRACK_BG
                    c.drawRoundRect(barLeft, barTop, barRight, barBottom, 3f, 3f, paint)
                    if (count > 0) {
                        val fillW = max(6f, (barRight - barLeft) * ratio)
                        paint.color = accent
                        c.drawRoundRect(barLeft, barTop, barLeft + fillW, barBottom, 3f, 3f, paint)
                    }

                    drawAlignedSingleLine(c, countLabel, 490f, 557f, baseY, countPaint, false, alignEnd = true)
                }

                y += rowH
            }

            // Totals Footer Row inside table
            paint.color = COLOR_GOLD_TINT
            c.drawRect(MARGIN_X, y, CONTENT_RIGHT, y + totalRowH, paint)
            val totLabel = if (data.lang == AppLanguage.ARABIC) "إجمالي صلوات السواعي" else "Total Canonical Prayers"
            val totVal = if (data.lang == AppLanguage.ARABIC) "$totalLogs صلاة" else "$totalLogs prayers"
            val totPaint = makeTextPaint(9.5f, COLOR_BURGUNDY, bold = true)
            if (isRtl) {
                drawAlignedSingleLine(c, totLabel, 207f, 555f, y + 14.5f, totPaint, true)
                drawAlignedSingleLine(c, totVal, 38f, 197f, y + 14.5f, totPaint, true)
            } else {
                drawAlignedSingleLine(c, totLabel, 38f, 388f, y + 14.5f, totPaint, false)
                drawAlignedSingleLine(c, totVal, 396f, 557f, y + 14.5f, totPaint, false, alignEnd = true)
            }

            c.restore()

            // Crisp border around whole table
            paint.style = Paint.Style.STROKE
            paint.strokeWidth = 1f
            paint.color = COLOR_BORDER
            c.drawRoundRect(tableRect, 9f, 9f, paint)
            paint.style = Paint.Style.FILL
        }

        ctx.cursorY += sectionHeight
    }

    private fun drawArrowPrayersSection(ctx: PageContext, data: ReportInput) {
        val isRtl = data.lang.isRtl
        val sessionsToShow = data.allArrowLogs.take(MAX_ARROW_SESSIONS_IN_PDF)
        val totalRepetitions = data.allArrowLogs.sumOf { it.count }

        val headerH = 22f
        val rowH = 19f
        val sectionH = 20f + headerH + (sessionsToShow.size * rowH) + 14f

        ctx.ensureSpace(sectionH)

        ctx.canvas?.let { c ->
            val title = if (data.lang == AppLanguage.ARABIC) {
                "سجل الصلوات السهمية (صلاة يسوع)"
            } else {
                "Arrow Prayers Log (Jesus Prayer)"
            }
            val sub = if (data.lang == AppLanguage.ARABIC) {
                "الإجمالي: $totalRepetitions تكرار (${data.allArrowLogs.size} جلسة)"
            } else {
                "Total: $totalRepetitions repetitions (${data.allArrowLogs.size} sessions)"
            }
            drawSectionHeading(c, ctx.cursorY + 12f, title, sub, isRtl)

            val tableTop = ctx.cursorY + 20f
            val tableBottom = tableTop + headerH + (sessionsToShow.size * rowH)
            val tableRect = RectF(MARGIN_X, tableTop, CONTENT_RIGHT, tableBottom)

            val paint = Paint(Paint.ANTI_ALIAS_FLAG)
            paint.color = COLOR_SURFACE
            c.drawRoundRect(tableRect, 9f, 9f, paint)

            c.save()
            val clip = Path().apply { addRoundRect(tableRect, 9f, 9f, Path.Direction.CW) }
            c.clipPath(clip)

            paint.color = COLOR_BURGUNDY_DARK
            c.drawRect(MARGIN_X, tableTop, CONTENT_RIGHT, tableTop + headerH, paint)

            val headerPaint = makeTextPaint(9f, Color.WHITE, bold = true)
            val hY = tableTop + 14.5f
            if (isRtl) {
                drawAlignedSingleLine(c, "التاريخ والوقت", 440f, 555f, hY, headerPaint, true)
                drawAlignedSingleLine(c, "نص الصلاة السهمية", 125f, 432f, hY, headerPaint, true)
                drawAlignedSingleLine(c, "التكرارات", 38f, 118f, hY, headerPaint, true)
            } else {
                drawAlignedSingleLine(c, "Date & Time", 38f, 152f, hY, headerPaint, false)
                drawAlignedSingleLine(c, "Arrow Prayer Text", 160f, 468f, hY, headerPaint, false)
                drawAlignedSingleLine(c, "Count", 476f, 557f, hY, headerPaint, false, alignEnd = true)
            }

            var y = tableTop + headerH
            val cellPaint = makeTextPaint(8.8f, COLOR_TEXT_PRIMARY, bold = false)
            val countPaint = makeTextPaint(9f, COLOR_BURGUNDY, bold = true)

            sessionsToShow.forEachIndexed { idx, log ->
                if (idx % 2 == 1) {
                    paint.color = COLOR_ROW_ALT
                    c.drawRect(MARGIN_X, y, CONTENT_RIGHT, y + rowH, paint)
                }
                if (idx < sessionsToShow.lastIndex) {
                    paint.color = COLOR_BORDER
                    paint.strokeWidth = 0.5f
                    c.drawLine(MARGIN_X, y + rowH, CONTENT_RIGHT, y + rowH, paint)
                }

                val dateTimeStr = String.format(Locale.US, "%s  %02d:%02d", log.dateString, log.hour, log.minute)
                val countText = if (data.lang == AppLanguage.ARABIC) "${log.count} مرة" else "${log.count}x"
                val baseY = y + 13f

                if (isRtl) {
                    drawAlignedSingleLine(c, dateTimeStr, 440f, 555f, baseY, cellPaint, true)
                    drawAlignedSingleLine(c, log.prayerText, 125f, 432f, baseY, cellPaint, true)
                    drawAlignedSingleLine(c, countText, 38f, 118f, baseY, countPaint, true)
                } else {
                    drawAlignedSingleLine(c, dateTimeStr, 38f, 152f, baseY, cellPaint, false)
                    drawAlignedSingleLine(c, log.prayerText, 160f, 468f, baseY, cellPaint, false)
                    drawAlignedSingleLine(c, countText, 476f, 557f, baseY, countPaint, false, alignEnd = true)
                }

                y += rowH
            }

            c.restore()

            paint.style = Paint.Style.STROKE
            paint.strokeWidth = 1f
            paint.color = COLOR_BORDER
            c.drawRoundRect(tableRect, 9f, 9f, paint)
            paint.style = Paint.Style.FILL
        }

        ctx.cursorY += sectionH
    }

    private fun drawDetailedLogsSection(ctx: PageContext, data: ReportInput) {
        val isRtl = data.lang.isRtl
        val logsToShow = data.allLogs.take(MAX_RECENT_LOGS_IN_PDF)
        val headerH = 23f
        val rowH = 19.5f

        // Ensure room for heading + header + at least 2 rows (or empty state box)
        ctx.ensureSpace(20f + headerH + rowH * 2f + 10f)

        fun drawLogsSectionTitle(isContinuation: Boolean) {
            ctx.canvas?.let { c ->
                val title = if (data.lang == AppLanguage.ARABIC) {
                    if (isContinuation) "سجل الصلوات التفصيلي (تابع)" else "سجل الصلوات التفصيلي والمزامنة"
                } else {
                    if (isContinuation) "Detailed Prayer Activity Log (Cont.)" else "Detailed Prayer Activity Log"
                }
                val sub = if (logsToShow.isNotEmpty()) {
                    if (data.lang == AppLanguage.ARABIC) {
                        "عرض أحدث ${logsToShow.size} من أصل ${data.allLogs.size} صلاة"
                    } else {
                        "Showing ${logsToShow.size} of ${data.allLogs.size} records"
                    }
                } else null
                drawSectionHeading(c, ctx.cursorY + 12f, title, sub, isRtl)
            }
            ctx.cursorY += 20f
        }

        fun drawLogsTableHeader() {
            ctx.canvas?.let { c ->
                val paint = Paint(Paint.ANTI_ALIAS_FLAG)
                paint.color = COLOR_BURGUNDY
                c.drawRoundRect(MARGIN_X, ctx.cursorY, CONTENT_RIGHT, ctx.cursorY + headerH, 6f, 6f, paint)

                val hPaint = makeTextPaint(9f, Color.WHITE, bold = true)
                val hY = ctx.cursorY + 15f
                if (isRtl) {
                    drawAlignedSingleLine(c, "#", 534f, 557f, hY, hPaint, true)
                    drawAlignedSingleLine(c, "التاريخ", 442f, 528f, hY, hPaint, true)
                    drawAlignedSingleLine(c, "الوقت", 384f, 436f, hY, hPaint, true)
                    drawAlignedSingleLine(c, "الصلاة القانونية", 190f, 376f, hY, hPaint, true)
                    drawAlignedSingleLine(c, "حالة التوثيق", 38f, 180f, hY, hPaint, true)
                } else {
                    drawAlignedSingleLine(c, "#", 38f, 62f, hY, hPaint, false)
                    drawAlignedSingleLine(c, "Date", 68f, 152f, hY, hPaint, false)
                    drawAlignedSingleLine(c, "Time", 158f, 212f, hY, hPaint, false)
                    drawAlignedSingleLine(c, "Canonical Prayer", 220f, 405f, hY, hPaint, false)
                    drawAlignedSingleLine(c, "Verification Status", 412f, 557f, hY, hPaint, false, alignEnd = true)
                }
            }
            ctx.cursorY += headerH
        }

        drawLogsSectionTitle(isContinuation = false)

        if (logsToShow.isEmpty()) {
            ctx.canvas?.let { c ->
                val paint = Paint(Paint.ANTI_ALIAS_FLAG)
                val boxRect = RectF(MARGIN_X, ctx.cursorY, CONTENT_RIGHT, ctx.cursorY + 48f)
                paint.color = COLOR_SURFACE
                c.drawRoundRect(boxRect, 8f, 8f, paint)
                paint.style = Paint.Style.STROKE
                paint.strokeWidth = 0.9f
                paint.color = COLOR_BORDER
                c.drawRoundRect(boxRect, 8f, 8f, paint)
                paint.style = Paint.Style.FILL

                val emptyMsg = if (data.lang == AppLanguage.ARABIC) {
                    "لا توجد صلوات مسجلة في هذا النطاق بعد — ابدأ بتسجيل صلاتك اليومية ليتم توثيقها هنا."
                } else {
                    "No prayer records logged in this period yet."
                }
                val emptyPaint = makeTextPaint(9.5f, COLOR_TEXT_MUTED, bold = false)
                drawAlignedSingleLine(c, emptyMsg, MARGIN_X + 14f, CONTENT_RIGHT - 14f, ctx.cursorY + 28f, emptyPaint, isRtl)
            }
            ctx.cursorY += 54f
            return
        }

        drawLogsTableHeader()

        val numPaint = makeTextPaint(8.5f, COLOR_TEXT_MUTED, bold = true)
        val datePaint = makeTextPaint(9f, COLOR_TEXT_PRIMARY, bold = false)
        val prayerPaint = makeTextPaint(9.2f, COLOR_BURGUNDY, bold = true)
        val badgeTextPaint = makeTextPaint(
            8f,
            if (data.isCloudConnected) COLOR_GREEN else COLOR_BURGUNDY,
            bold = true
        )
        val statusText = if (data.isCloudConnected) {
            if (data.lang == AppLanguage.ARABIC) "✓ موثق سحابياً" else "✓ Cloud Synced"
        } else {
            if (data.lang == AppLanguage.ARABIC) "✓ محفوظ محلياً" else "✓ Saved Locally"
        }

        logsToShow.forEachIndexed { index, log ->
            ctx.ensureSpace(rowH) {
                drawLogsSectionTitle(isContinuation = true)
                drawLogsTableHeader()
            }

            ctx.canvas?.let { c ->
                val y = ctx.cursorY
                val paint = Paint(Paint.ANTI_ALIAS_FLAG)
                val bgColor = if (index % 2 == 1) COLOR_ROW_ALT else COLOR_SURFACE
                paint.color = bgColor
                c.drawRect(MARGIN_X, y, CONTENT_RIGHT, y + rowH, paint)

                // Left & right hairline border for table continuity
                paint.color = COLOR_BORDER
                paint.strokeWidth = 0.7f
                c.drawLine(MARGIN_X, y, MARGIN_X, y + rowH, paint)
                c.drawLine(CONTENT_RIGHT, y, CONTENT_RIGHT, y + rowH, paint)
                c.drawLine(MARGIN_X, y + rowH, CONTENT_RIGHT, y + rowH, paint)

                val baseY = y + 13f
                val rowNum = "${index + 1}"
                val timeStr = String.format(Locale.US, "%02d:%02d", log.hour, log.minute)
                val prayerObj = PrayerId.fromCode(log.prayerCode)
                val prayerTitle = prayerObj.getDisplayName(data.lang)
                val accent = prayerAccentColor(prayerObj.code)

                if (isRtl) {
                    drawAlignedSingleLine(c, rowNum, 534f, 557f, baseY, numPaint, true)
                    drawAlignedSingleLine(c, log.dateString, 442f, 528f, baseY, datePaint, true)
                    drawAlignedSingleLine(c, timeStr, 384f, 436f, baseY, datePaint, true)

                    paint.color = accent
                    c.drawCircle(372f, y + rowH / 2f, 2.8f, paint)
                    drawAlignedSingleLine(c, prayerTitle, 190f, 364f, baseY, prayerPaint, true)

                    // Status capsule pill
                    paint.color = if (data.isCloudConnected) COLOR_GREEN_TINT else COLOR_BURGUNDY_SOFT
                    c.drawRoundRect(38f, y + 3.5f, 150f, y + rowH - 3.5f, 6f, 6f, paint)
                    drawAlignedSingleLine(c, statusText, 44f, 144f, baseY, badgeTextPaint, true)
                } else {
                    drawAlignedSingleLine(c, rowNum, 38f, 62f, baseY, numPaint, false)
                    drawAlignedSingleLine(c, log.dateString, 68f, 152f, baseY, datePaint, false)
                    drawAlignedSingleLine(c, timeStr, 158f, 212f, baseY, datePaint, false)

                    paint.color = accent
                    c.drawCircle(224f, y + rowH / 2f, 2.8f, paint)
                    drawAlignedSingleLine(c, prayerTitle, 232f, 405f, baseY, prayerPaint, false)

                    paint.color = if (data.isCloudConnected) COLOR_GREEN_TINT else COLOR_BURGUNDY_SOFT
                    c.drawRoundRect(442f, y + 3.5f, 557f, y + rowH - 3.5f, 6f, 6f, paint)
                    drawAlignedSingleLine(c, statusText, 448f, 551f, baseY, badgeTextPaint, false, alignEnd = true)
                }
            }

            ctx.cursorY += rowH
        }
    }

    private fun drawPageFooter(canvas: Canvas, data: ReportInput, pageNumber: Int, totalPages: Int) {
        val isRtl = data.lang.isRtl
        val paint = Paint(Paint.ANTI_ALIAS_FLAG)

        paint.color = COLOR_GOLD
        paint.strokeWidth = 0.8f
        canvas.drawLine(MARGIN_X, FOOTER_TOP_Y, CONTENT_RIGHT, FOOTER_TOP_Y, paint)

        val verse = if (data.lang == AppLanguage.ARABIC) {
            "«سَبْعَ مَرَّاتٍ فِي النَّهَارِ سَبَّحْتُكَ عَلَى أَحْكَامِ عَدْلِكَ» (مز ١١٩: ١٦٤) • تطبيق صلوات الأجبية الأرثوذكسية"
        } else {
            "“Seven times a day I praise You, because of Your righteous judgments.” (Ps 119:164) • Agpeya Orthodox App"
        }
        val versePaint = makeTextPaint(8.5f, COLOR_TEXT_MUTED, bold = false)
        drawAlignedSingleLine(canvas, verse, MARGIN_X, CONTENT_RIGHT, FOOTER_TOP_Y + 15f, versePaint, isRtl)

        val ownerInfo = buildString {
            append(data.userName?.takeIf { it.isNotBlank() } ?: data.userEmail ?: if (data.lang == AppLanguage.ARABIC) "المصلي" else "Worshipper")
            if (!data.churchName.isNullOrBlank()) {
                append(" • ${data.churchName}")
            }
        }
        val pageStamp = if (data.lang == AppLanguage.ARABIC) {
            "صفحة $pageNumber من $totalPages"
        } else {
            "Page $pageNumber of $totalPages"
        }

        val infoPaint = makeTextPaint(8.5f, COLOR_BURGUNDY, bold = true)
        if (isRtl) {
            drawAlignedSingleLine(canvas, ownerInfo, MARGIN_X + 95f, CONTENT_RIGHT, FOOTER_TOP_Y + 30f, versePaint, true)
            drawAlignedSingleLine(canvas, pageStamp, MARGIN_X, MARGIN_X + 90f, FOOTER_TOP_Y + 30f, infoPaint, false)
        } else {
            drawAlignedSingleLine(canvas, ownerInfo, MARGIN_X, CONTENT_RIGHT - 95f, FOOTER_TOP_Y + 30f, versePaint, false)
            drawAlignedSingleLine(canvas, pageStamp, CONTENT_RIGHT - 90f, CONTENT_RIGHT, FOOTER_TOP_Y + 30f, infoPaint, true)
        }
    }

    private fun makeTextPaint(sizeSp: Float, colorInt: Int, bold: Boolean): TextPaint {
        return TextPaint(Paint.ANTI_ALIAS_FLAG).apply {
            textSize = sizeSp
            color = colorInt
            typeface = Typeface.create(Typeface.DEFAULT, if (bold) Typeface.BOLD else Typeface.NORMAL)
        }
    }

    private fun buildStaticLayout(text: String, paint: TextPaint, width: Float, isRtl: Boolean): StaticLayout {
        val safeWidth = max(1, width.toInt())
        return StaticLayout.Builder.obtain(text, 0, text.length, paint, safeWidth)
            .setAlignment(Layout.Alignment.ALIGN_NORMAL)
            .setTextDirection(if (isRtl) TextDirectionHeuristics.RTL else TextDirectionHeuristics.LTR)
            .setLineSpacing(1.5f, 1.06f)
            .setIncludePad(false)
            .build()
    }

    private fun drawAlignedSingleLine(
        canvas: Canvas,
        text: String,
        left: Float,
        right: Float,
        baselineY: Float,
        paint: TextPaint,
        isRtl: Boolean,
        alignEnd: Boolean = false
    ) {
        val avail = max(8f, right - left)
        val ellipsized = TextUtils.ellipsize(text, paint, avail, TextUtils.TruncateAt.END).toString()
        val alignRight = if (isRtl) !alignEnd else alignEnd
        paint.textAlign = if (alignRight) Paint.Align.RIGHT else Paint.Align.LEFT
        val x = if (alignRight) right else left
        canvas.drawText(ellipsized, x, baselineY, paint)
    }

    private fun drawChurchEmblemBadge(canvas: Canvas, cx: Float, cy: Float, emblemCode: String) {
        val paint = Paint(Paint.ANTI_ALIAS_FLAG)
        // Outer Gold Ring
        paint.color = COLOR_GOLD
        canvas.drawCircle(cx, cy, 26f, paint)

        // Inner Burgundy Circle
        paint.color = COLOR_BURGUNDY_DARK
        canvas.drawCircle(cx, cy, 22.5f, paint)

        // Inner thin Gold Ring
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = 0.8f
        paint.color = COLOR_GOLD_LIGHT
        canvas.drawCircle(cx, cy, 19.5f, paint)

        // Draw Emblem Motif in Gold
        paint.color = COLOR_GOLD_LIGHT
        paint.strokeWidth = 2.4f
        paint.style = Paint.Style.STROKE

        when (emblemCode) {
            "st_mark_lion" -> {
                canvas.drawLine(cx, cy - 12f, cx, cy + 12f, paint)
                canvas.drawLine(cx - 10f, cy - 2f, cx + 10f, cy - 2f, paint)
                paint.style = Paint.Style.FILL
                canvas.drawCircle(cx - 5f, cy - 6f, 2.2f, paint)
                canvas.drawCircle(cx + 5f, cy - 6f, 2.2f, paint)
            }
            "st_mary_dove" -> {
                canvas.drawLine(cx, cy - 12f, cx, cy + 12f, paint)
                canvas.drawLine(cx - 12f, cy - 4f, cx + 12f, cy - 4f, paint)
                paint.style = Paint.Style.FILL
                canvas.drawCircle(cx, cy - 12f, 2.6f, paint)
            }
            "monastic_anchor" -> {
                canvas.drawLine(cx, cy - 12f, cx, cy + 12f, paint)
                canvas.drawLine(cx - 9f, cy - 3f, cx + 9f, cy - 3f, paint)
                canvas.drawArc(cx - 9f, cy + 1f, cx + 9f, cy + 12f, 0f, 180f, false, paint)
            }
            else -> { // "coptic_cross"
                canvas.drawLine(cx, cy - 13f, cx, cy + 13f, paint)
                canvas.drawLine(cx - 13f, cy, cx + 13f, cy, paint)
                paint.style = Paint.Style.FILL
                canvas.drawCircle(cx - 6f, cy - 6f, 1.8f, paint)
                canvas.drawCircle(cx + 6f, cy - 6f, 1.8f, paint)
                canvas.drawCircle(cx - 6f, cy + 6f, 1.8f, paint)
                canvas.drawCircle(cx + 6f, cy + 6f, 1.8f, paint)
            }
        }
    }
}
