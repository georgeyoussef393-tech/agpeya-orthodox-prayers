package com.example

import com.example.audio.SpiritualSound
import com.example.data.model.DailyScriptureProvider
import com.example.data.model.PrayerId
import com.example.localization.AppLanguage
import org.junit.Assert.*
import org.junit.Test

class ExampleUnitTest {
  @Test
  fun addition_isCorrect() {
    assertEquals(4, 2 + 2)
  }

  @Test
  fun dailyScriptureProvider_returnsValidOrthodoxVerses() {
    val daily = DailyScriptureProvider.getDailyVerseForCalendar()
    assertNotNull(daily)
    assertTrue(daily.verseArabic.isNotEmpty())
    assertTrue(daily.explanationArabic.isNotEmpty())
    assertTrue(daily.referenceArabic.isNotEmpty())

    PrayerId.canonicalPrayers.forEach { prayerId ->
      val prayerVerse = DailyScriptureProvider.getVerseForPrayer(prayerId)
      assertNotNull(prayerVerse)
      assertTrue(prayerVerse.verseArabic.isNotEmpty())
      assertTrue(prayerVerse.verseEnglish.isNotEmpty())
    }
  }

  @Test
  fun spiritualSounds_haveValidNamesAndDescriptions() {
    SpiritualSound.entries.forEach { sound ->
      assertTrue(sound.getName(AppLanguage.ARABIC).isNotEmpty())
      assertTrue(sound.getName(AppLanguage.ENGLISH).isNotEmpty())
      assertTrue(sound.getDescription(AppLanguage.ARABIC).isNotEmpty())
      assertTrue(sound.getDescription(AppLanguage.ENGLISH).isNotEmpty())
    }
  }

  @Test
  fun vibrationLocalization_hasValidText() {
    AppLanguage.entries.forEach { lang ->
      assertTrue(com.example.localization.AgpeyaStrings.vibrationAndSoundTitle(lang).isNotEmpty())
      assertTrue(com.example.localization.AgpeyaStrings.vibrationAndSoundDescription(lang).isNotEmpty())
      assertTrue(com.example.localization.AgpeyaStrings.enableVibrationAll(lang).isNotEmpty())
      assertTrue(com.example.localization.AgpeyaStrings.enableSoundAll(lang).isNotEmpty())
    }
  }

  @Test
  fun alarmScheduler_calculatesNextTriggerInUserDefinedTimezone() {
    val cairoTz = java.util.TimeZone.getTimeZone("Africa/Cairo")
    val now = System.currentTimeMillis()

    // Test scheduling for 6:00 AM in Cairo
    val triggerMillis = com.example.alarm.AgpeyaAlarmScheduler.calculateNextTriggerTimeMillis(
      hour = 6,
      minute = 0,
      targetTimezone = cairoTz,
      leadTimeMinutes = 0,
      referenceNowMillis = now
    )

    assertTrue("Trigger millis must be in the future", triggerMillis > now)

    // Verify when converted to Cairo Calendar, the hour is 6 and minute is 0
    val calInCairo = java.util.Calendar.getInstance(cairoTz).apply {
      timeInMillis = triggerMillis
    }
    assertEquals(6, calInCairo.get(java.util.Calendar.HOUR_OF_DAY))
    assertEquals(0, calInCairo.get(java.util.Calendar.MINUTE))
    assertEquals(0, calInCairo.get(java.util.Calendar.SECOND))
  }

  @Test
  fun alarmScheduler_appliesLeadTimeCorrectlyInUserTimezone() {
    val londonTz = java.util.TimeZone.getTimeZone("Europe/London")
    val now = System.currentTimeMillis()

    // Test scheduling for 12:00 PM with 15 minutes lead time
    val triggerMillisWithLead = com.example.alarm.AgpeyaAlarmScheduler.calculateNextTriggerTimeMillis(
      hour = 12,
      minute = 0,
      targetTimezone = londonTz,
      leadTimeMinutes = 15,
      referenceNowMillis = now
    )

    val calInLondon = java.util.Calendar.getInstance(londonTz).apply {
      timeInMillis = triggerMillisWithLead
    }
    // 12:00 - 15 minutes = 11:45
    assertEquals(11, calInLondon.get(java.util.Calendar.HOUR_OF_DAY))
    assertEquals(45, calInLondon.get(java.util.Calendar.MINUTE))
  }

  @Test
  fun alarmScheduler_formatsTimeRemainingAccurately() {
    val fiveMinutes = 5 * 60 * 1000L
    val twoHoursThirtyMins = (2 * 60 + 30) * 60 * 1000L

    val arFiveMin = com.example.alarm.AgpeyaAlarmScheduler.formatTimeRemaining(fiveMinutes, AppLanguage.ARABIC)
    val enFiveMin = com.example.alarm.AgpeyaAlarmScheduler.formatTimeRemaining(fiveMinutes, AppLanguage.ENGLISH)
    val arTwoHours = com.example.alarm.AgpeyaAlarmScheduler.formatTimeRemaining(twoHoursThirtyMins, AppLanguage.ARABIC)
    val enTwoHours = com.example.alarm.AgpeyaAlarmScheduler.formatTimeRemaining(twoHoursThirtyMins, AppLanguage.ENGLISH)

    assertTrue(arFiveMin.contains("دقيقة"))
    assertTrue(enFiveMin.contains("5m"))
    assertTrue(arTwoHours.contains("2") && arTwoHours.contains("30"))
    assertTrue(enTwoHours.contains("2h 30m"))
  }

  @Test
  fun pdfExporter_buildReportSummaryText_includesUserChurchServiceAndTotals() {
    val logs = listOf(
      com.example.data.model.PrayerLogEntity(
        prayerCode = PrayerId.PRIME.code,
        dateString = "2026-11-13",
        year = 2026,
        month = 11,
        day = 13,
        timestamp = 1_700_000_000_000L,
        hour = 6,
        minute = 30
      )
    )
    val arrowLogs = listOf(
      com.example.data.model.ArrowPrayerLogEntity(
        prayerText = "كيرياليسون",
        count = 33,
        dateString = "2026-11-13",
        year = 2026,
        month = 11,
        quarter = 4,
        halfYear = 2,
        day = 13,
        timestamp = 1_700_000_500_000L,
        hour = 9,
        minute = 0
      )
    )

    val arabicSummary = com.example.report.PrayerPdfExporter.buildReportSummaryText(
      userEmail = "georgeyoussef393@gmail.com",
      userName = "George Youssef",
      churchName = "كنيسة مارمرقس",
      serviceName = "خدمة الشباب",
      syncKey = "key_123",
      lang = AppLanguage.ARABIC,
      periodName = "شهري",
      allLogs = logs,
      allArrowLogs = arrowLogs
    )
    assertTrue(arabicSummary.contains("George Youssef"))
    assertTrue(arabicSummary.contains("كنيسة مارمرقس"))
    assertTrue(arabicSummary.contains("خدمة الشباب"))
    assertTrue(arabicSummary.contains("33"))

    val englishSummary = com.example.report.PrayerPdfExporter.buildReportSummaryText(
      userEmail = "georgeyoussef393@gmail.com",
      userName = "George Youssef",
      churchName = "St. Mark Church",
      serviceName = "Youth Ministry",
      syncKey = null,
      lang = AppLanguage.ENGLISH,
      periodName = "Monthly",
      allLogs = logs,
      allArrowLogs = arrowLogs
    )
    assertTrue(englishSummary.contains("St. Mark Church"))
    assertTrue(englishSummary.contains("Youth Ministry"))
    assertTrue(englishSummary.contains("Total Canonical Prayers Logged: 1"))
    assertTrue(englishSummary.contains("Total Arrow Prayers Count: 33"))
  }
}


