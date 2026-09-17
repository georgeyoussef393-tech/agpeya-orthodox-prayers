package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
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
}

