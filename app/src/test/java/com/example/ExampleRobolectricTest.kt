package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode

@RunWith(RobolectricTestRunner::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
@Config(sdk = [36])
class ExampleRobolectricTest {

  @Test
  fun `read string from context`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val appName = context.getString(R.string.app_name)
    assertEquals("Agpeya Prayers", appName)
  }

  @Test
  fun `verify canonical prayers count and ids`() {
    val prayers = com.example.data.model.PrayerId.canonicalPrayers
    assertEquals(8, prayers.size)
    assertEquals(com.example.data.model.PrayerId.PRIME, prayers[0])
  }

  @Test
  fun `verify multilingual support titles`() {
    val prime = com.example.data.model.PrayerId.PRIME
    assertEquals("صلاة باكر", prime.getDisplayName(com.example.localization.AppLanguage.ARABIC))
    assertEquals("Prime (Morning Prayer)", prime.getDisplayName(com.example.localization.AppLanguage.ENGLISH))
  }

  @Test
  fun `verify firestore sync key generation and update`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val syncManager = com.example.data.sync.FirestoreSyncManager(context)
    val initialKey = syncManager.syncKey.value
    org.junit.Assert.assertTrue("Sync key should not be empty", initialKey.isNotEmpty())
    org.junit.Assert.assertTrue("Sync key should start with agpeya-sync-", initialKey.startsWith("agpeya-sync-"))

    val customKey = "agpeya-sync-test-device-123"
    var callbackInvoked = false
    syncManager.updateSyncKey(customKey) {
      callbackInvoked = true
    }
    assertEquals(customKey, syncManager.syncKey.value)
    org.junit.Assert.assertTrue("Callback should be invoked on key update", callbackInvoked)
  }

  @Test
  fun `verify email sync key normalization`() {
    val email = "georgeyoussef393@gmail.com"
    val normalizedKey = com.example.data.sync.FirestoreSyncManager.normalizeEmailToKey(email)
    assertEquals("usr_georgeyoussef393_at_gmail_com", normalizedKey)

    val context = ApplicationProvider.getApplicationContext<Context>()
    val syncManager = com.example.data.sync.FirestoreSyncManager(context)
    syncManager.setUserEmail(email)
    assertEquals(email, syncManager.userEmail.value)
    assertEquals(normalizedKey, syncManager.syncKey.value)
  }

  @Test
  fun `verify full prayer sections structure and completeness`() {
    for (prayer in com.example.data.model.PrayerId.canonicalPrayers) {
      val sections = com.example.data.model.AgpeyaHourDetails.getFullPrayerSections(
        prayer,
        com.example.localization.AppLanguage.ARABIC
      )
      // Must contain at least intro, thanksgiving, psalm 50, psalms, gospel, litanies, trisagion, kyrie, absolution, conclusion
      org.junit.Assert.assertTrue("Sections should have all parts for ${prayer.name}", sections.size >= 8)

      val hasGospel = sections.any { it.isGospel }
      org.junit.Assert.assertTrue("Should contain holy gospel for ${prayer.name}", hasGospel)

      val thanksgiving = sections.find { it.id == "thanksgiving" }
      org.junit.Assert.assertNotNull("Thanksgiving prayer must be present", thanksgiving)
      org.junit.Assert.assertTrue(thanksgiving!!.contentAr.contains("فلنشكر صانع الخيرات"))

      val psalm50 = sections.find { it.id == "psalm50" }
      org.junit.Assert.assertNotNull("Psalm 50 must be present", psalm50)
      org.junit.Assert.assertTrue(psalm50!!.contentAr.contains("ارحمني يا الله كعظيم رحمتك"))
    }
  }

  @Test
  fun `verify pdf generation with user name and prayer details`() {
    val testEmail = "georgeyoussef393@gmail.com"
    val testLogs = listOf(
      com.example.data.model.PrayerLogEntity(
        id = 1,
        prayerCode = com.example.data.model.PrayerId.PRIME.code,
        dateString = "2026-09-18",
        year = 2026,
        month = 9,
        day = 18,
        hour = 6,
        minute = 30,
        timestamp = System.currentTimeMillis()
      ),
      com.example.data.model.PrayerLogEntity(
        id = 2,
        prayerCode = com.example.data.model.PrayerId.TERCE.code,
        dateString = "2026-09-18",
        year = 2026,
        month = 9,
        day = 18,
        hour = 9,
        minute = 15,
        timestamp = System.currentTimeMillis()
      )
    )

    val summaryText = com.example.report.PrayerPdfExporter.buildReportSummaryText(
      userEmail = testEmail,
      userName = "George Youssef",
      churchName = "كنيسة مارجرجس والأنبا أنطونيوس",
      syncKey = "agpeya-sync-test",
      lang = com.example.localization.AppLanguage.ARABIC,
      periodName = "يومي",
      allLogs = testLogs
    )

    org.junit.Assert.assertNotNull("Summary text should not be null", summaryText)
    org.junit.Assert.assertTrue("Should contain user email", summaryText.contains(testEmail))
    org.junit.Assert.assertTrue("Should contain church name", summaryText.contains("كنيسة مارجرجس والأنبا أنطونيوس"))
    org.junit.Assert.assertTrue("Should contain sync key", summaryText.contains("agpeya-sync-test"))
    org.junit.Assert.assertTrue("Should contain total count 2", summaryText.contains("2"))
    org.junit.Assert.assertTrue("Should contain canonical hour name", summaryText.contains("باكر"))
  }

  @Test
  fun `verify coptic calendar conversion and synaxarium lookups`() {
    val calendar = java.util.Calendar.getInstance().apply {
      set(2026, java.util.Calendar.SEPTEMBER, 22)
    }

    val copticDate = com.example.data.coptic.CopticCalendarHelper.getCopticDate(calendar)
    org.junit.Assert.assertTrue("Coptic day should be positive", copticDate.day > 0)
    org.junit.Assert.assertTrue("Coptic month should be 1..13", copticDate.monthIndex in 1..13)
    org.junit.Assert.assertTrue("Coptic year should be in 1740s AM", copticDate.yearAM >= 1740)
    org.junit.Assert.assertTrue("Commemoration summary should not be empty", copticDate.commemorationSummaryAr.isNotEmpty())
    org.junit.Assert.assertNotNull("Fasting type must be present", copticDate.fastingType)
  }

  @Test
  fun `verify spiritual notes category properties`() {
    val categories = com.example.data.model.NoteCategory.values()
    assertEquals(4, categories.size)
    org.junit.Assert.assertTrue("Must include confession prep", categories.contains(com.example.data.model.NoteCategory.CONFESSION_PREP))
    org.junit.Assert.assertTrue("Must include personal prayer", categories.contains(com.example.data.model.NoteCategory.PERSONAL_PRAYER))
  }
}

