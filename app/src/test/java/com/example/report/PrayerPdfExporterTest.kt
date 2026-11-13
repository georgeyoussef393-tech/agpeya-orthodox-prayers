package com.example.report

import android.graphics.Bitmap
import android.graphics.Canvas
import com.example.data.model.ArrowPrayerLogEntity
import com.example.data.model.PrayerId
import com.example.data.model.PrayerLogEntity
import com.example.localization.AppLanguage
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(manifest = Config.NONE)
class PrayerPdfExporterTest {

    private fun sampleLogs(count: Int): List<PrayerLogEntity> {
        val prayers = PrayerId.canonicalPrayers
        val now = System.currentTimeMillis()
        return (0 until count).map { i ->
            val prayer = prayers[i % prayers.size]
            val dayNum = (i % 28) + 1
            PrayerLogEntity(
                id = (i + 1).toLong(),
                prayerCode = prayer.code,
                dateString = "2026-11-%02d".format(dayNum),
                year = 2026,
                month = 11,
                day = dayNum,
                timestamp = now - (i * 3_600_000L),
                hour = (6 + (i * 2)) % 24,
                minute = (i * 7) % 60
            )
        }
    }

    private fun sampleArrowLogs(): List<ArrowPrayerLogEntity> {
        val now = System.currentTimeMillis()
        return listOf(
            ArrowPrayerLogEntity(
                id = 1L,
                prayerText = "يا ربي يسوع المسيح ارحمني أنا الخاطئ",
                count = 33,
                dateString = "2026-11-12",
                year = 2026,
                month = 11,
                quarter = 4,
                halfYear = 2,
                day = 12,
                timestamp = now - 5_000_000L,
                hour = 21,
                minute = 15
            ),
            ArrowPrayerLogEntity(
                id = 2L,
                prayerText = "كيرياليسون",
                count = 41,
                dateString = "2026-11-13",
                year = 2026,
                month = 11,
                quarter = 4,
                halfYear = 2,
                day = 13,
                timestamp = now - 1_000_000L,
                hour = 7,
                minute = 30
            )
        )
    }

    @Test
    fun renderPages_arabicMultiPage_rendersMultiplePagesCleanly() {
        val renderedPages = mutableListOf<Int>()
        val bitmaps = mutableListOf<Bitmap>()
        try {
            val totalPages = PrayerPdfExporter.renderPagesToCanvasesForTest(
                userEmail = "georgeyoussef393@gmail.com",
                userName = "جرجس يوسف",
                churchName = "كنيسة السيدة العذراء مريم",
                serviceName = "خدمة الشباب الجامعي",
                churchEmblem = "coptic_cross",
                syncKey = "sync_george_2026_abcd1234",
                lang = AppLanguage.ARABIC,
                periodName = "التقرير الشهري",
                allLogs = sampleLogs(45),
                allArrowLogs = sampleArrowLogs(),
                isCloudConnected = true
            ) { pageNum ->
                renderedPages.add(pageNum)
                val bmp = Bitmap.createBitmap(595, 842, Bitmap.Config.ARGB_8888)
                bitmaps.add(bmp)
                Canvas(bmp)
            }

            assertTrue("Expected at least 2 pages for 45 logs + sections, got $totalPages", totalPages >= 2)
            assertEquals((1..totalPages).toList(), renderedPages)
        } finally {
            bitmaps.forEach { it.recycle() }
        }
    }

    @Test
    fun renderPages_englishLtr_rendersCleanly() {
        val renderedPages = mutableListOf<Int>()
        val bitmaps = mutableListOf<Bitmap>()
        try {
            val totalPages = PrayerPdfExporter.renderPagesToCanvasesForTest(
                userEmail = "+20 1222563688",
                userName = "George Youssef",
                churchName = "St. Mary Coptic Orthodox Church",
                serviceName = "Youth Ministry",
                churchEmblem = "st_mark_lion",
                syncKey = null,
                lang = AppLanguage.ENGLISH,
                periodName = "Monthly Report",
                allLogs = sampleLogs(12),
                allArrowLogs = sampleArrowLogs(),
                isCloudConnected = false
            ) { pageNum ->
                renderedPages.add(pageNum)
                val bmp = Bitmap.createBitmap(595, 842, Bitmap.Config.ARGB_8888)
                bitmaps.add(bmp)
                Canvas(bmp)
            }

            assertTrue("Expected at least 1 page, got $totalPages", totalPages >= 1)
            assertEquals((1..totalPages).toList(), renderedPages)
        } finally {
            bitmaps.forEach { it.recycle() }
        }
    }

    @Test
    fun renderPages_emptyLogs_handlesEmptyStateOnSinglePage() {
        val renderedPages = mutableListOf<Int>()
        val bitmaps = mutableListOf<Bitmap>()
        try {
            val totalPages = PrayerPdfExporter.renderPagesToCanvasesForTest(
                userEmail = null,
                userName = "مستخدم جديد",
                churchName = null,
                serviceName = null,
                churchEmblem = "st_mary_dove",
                syncKey = null,
                lang = AppLanguage.ARABIC,
                periodName = "التقرير اليومي",
                allLogs = emptyList(),
                allArrowLogs = emptyList(),
                isCloudConnected = false
            ) { pageNum ->
                renderedPages.add(pageNum)
                val bmp = Bitmap.createBitmap(595, 842, Bitmap.Config.ARGB_8888)
                bitmaps.add(bmp)
                Canvas(bmp)
            }

            assertEquals(1, totalPages)
            assertEquals(listOf(1), renderedPages)
        } finally {
            bitmaps.forEach { it.recycle() }
        }
    }
}
