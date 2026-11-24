package com.example.report

import com.example.data.model.ArrowPrayerLogEntity
import com.example.data.model.PrayerId
import com.example.data.model.PrayerLogEntity
import com.example.localization.AppLanguage
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class ReportExportFilterTest {

    private fun logOn(date: String, year: Int, month: Int, day: Int): PrayerLogEntity {
        return PrayerLogEntity(
            prayerCode = PrayerId.PRIME.code,
            dateString = date,
            year = year,
            month = month,
            day = day,
            hour = 7,
            minute = 0
        )
    }

    private fun arrowOn(date: String, year: Int, month: Int, day: Int, count: Int = 10): ArrowPrayerLogEntity {
        val quarter = ((month - 1) / 3) + 1
        val halfYear = if (month <= 6) 1 else 2
        return ArrowPrayerLogEntity(
            count = count,
            prayerText = "كيرياليسون",
            dateString = date,
            year = year,
            month = month,
            quarter = quarter,
            halfYear = halfYear,
            day = day,
            hour = 12,
            minute = 0
        )
    }

    private val canonicalLogs = listOf(
        logOn("2025-12-20", 2025, 12, 20),
        logOn("2026-01-15", 2026, 1, 15), // Q1, H1
        logOn("2026-03-28", 2026, 3, 28), // Q1, H1
        logOn("2026-05-10", 2026, 5, 10), // Q2, H1
        logOn("2026-08-05", 2026, 8, 5),  // Q3, H2
        logOn("2026-11-01", 2026, 11, 1), // Q4, H2
        logOn("2026-11-12", 2026, 11, 12) // Q4, H2
    )

    private val arrowLogs = listOf(
        arrowOn("2025-11-01", 2025, 11, 1, 33),
        arrowOn("2026-02-14", 2026, 2, 14, 20), // Q1, H1
        arrowOn("2026-06-20", 2026, 6, 20, 40), // Q2, H1
        arrowOn("2026-11-10", 2026, 11, 10, 50) // Q4, H2
    )

    @Test
    fun customDuration_filtersLastNDaysAccurately() {
        val config = ReportExportConfig(
            scope = ReportExportScope.CUSTOM_DURATION,
            durationDays = 14
        )
        val today = "2026-11-13"
        val filteredC = ReportExportFilter.filterCanonicalLogs(canonicalLogs, config, today)
        val filteredA = ReportExportFilter.filterArrowLogs(arrowLogs, config, today)

        // 14 days ending 2026-11-13 starts at 2026-10-31
        assertEquals(listOf("2026-11-01", "2026-11-12"), filteredC.map { it.dateString })
        assertEquals(listOf("2026-11-10"), filteredA.map { it.dateString })
        val labelAr = ReportExportFilter.buildPeriodDisplayLabel(config, AppLanguage.ARABIC, today)
        assertTrue(labelAr.contains("14"))
        assertTrue(labelAr.contains("2026-10-31"))
    }

    @Test
    fun dateRange_filtersInclusiveAndNormalizesReversedRange() {
        val config = ReportExportConfig(
            scope = ReportExportScope.DATE_RANGE,
            startDateString = "2026-08-10",
            endDateString = "2026-03-01" // Reversed intentionally
        )
        val filteredC = ReportExportFilter.filterCanonicalLogs(canonicalLogs, config)
        val filteredA = ReportExportFilter.filterArrowLogs(arrowLogs, config)

        assertEquals(listOf("2026-03-28", "2026-05-10", "2026-08-05"), filteredC.map { it.dateString })
        assertEquals(listOf("2026-06-20"), filteredA.map { it.dateString })
    }

    @Test
    fun quarterly_filtersQ1AndOtherQuarters() {
        val q1Config = ReportExportConfig(
            scope = ReportExportScope.QUARTERLY,
            year = 2026,
            quarter = 1
        )
        val filteredC = ReportExportFilter.filterCanonicalLogs(canonicalLogs, q1Config)
        val filteredA = ReportExportFilter.filterArrowLogs(arrowLogs, q1Config)

        assertEquals(listOf("2026-01-15", "2026-03-28"), filteredC.map { it.dateString })
        assertEquals(listOf("2026-02-14"), filteredA.map { it.dateString })
        assertTrue(
            ReportExportFilter.buildPeriodDisplayLabel(q1Config, AppLanguage.ARABIC)
                .contains("الربع الأول")
        )
    }

    @Test
    fun semiAnnual_filtersH1AndH2() {
        val h1Config = ReportExportConfig(
            scope = ReportExportScope.SEMI_ANNUAL,
            year = 2026,
            halfYear = 1
        )
        val h2Config = ReportExportConfig(
            scope = ReportExportScope.SEMI_ANNUAL,
            year = 2026,
            halfYear = 2
        )
        assertEquals(
            listOf("2026-01-15", "2026-03-28", "2026-05-10"),
            ReportExportFilter.filterCanonicalLogs(canonicalLogs, h1Config).map { it.dateString }
        )
        assertEquals(
            listOf("2026-08-05", "2026-11-01", "2026-11-12"),
            ReportExportFilter.filterCanonicalLogs(canonicalLogs, h2Config).map { it.dateString }
        )
        assertEquals(
            listOf("2026-02-14", "2026-06-20"),
            ReportExportFilter.filterArrowLogs(arrowLogs, h1Config).map { it.dateString }
        )
    }

    @Test
    fun annual_filtersSelectedYearOnly() {
        val config2026 = ReportExportConfig(scope = ReportExportScope.ANNUAL, year = 2026)
        val config2025 = ReportExportConfig(scope = ReportExportScope.ANNUAL, year = 2025)

        assertEquals(6, ReportExportFilter.filterCanonicalLogs(canonicalLogs, config2026).size)
        assertEquals(3, ReportExportFilter.filterArrowLogs(arrowLogs, config2026).size)
        assertEquals(listOf("2025-12-20"), ReportExportFilter.filterCanonicalLogs(canonicalLogs, config2025).map { it.dateString })
        assertEquals(listOf("2025-11-01"), ReportExportFilter.filterArrowLogs(arrowLogs, config2025).map { it.dateString })
    }

    @Test
    fun allRecords_returnsUnfilteredLogs() {
        val config = ReportExportConfig(scope = ReportExportScope.ALL_RECORDS)
        assertEquals(canonicalLogs.size, ReportExportFilter.filterCanonicalLogs(canonicalLogs, config).size)
        assertEquals(arrowLogs.size, ReportExportFilter.filterArrowLogs(arrowLogs, config).size)
    }
}
