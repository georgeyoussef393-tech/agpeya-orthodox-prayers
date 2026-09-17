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
}


