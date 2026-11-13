package com.example.data.sync

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.base.FirestoreEmulatorTestBase
import com.example.data.model.ArrowPrayerLogEntity
import com.example.data.model.PrayerAlarmSetting
import com.example.data.model.PrayerLogEntity
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.snapshots
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withTimeout
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Assert.fail
import org.junit.Test
import java.util.UUID

class FirestoreSyncRuleTest : FirestoreEmulatorTestBase() {

  @Test
  fun syncProfileToCloud_authenticatedOwner_createsAndUpdatesProfile() = runBlocking {
    val uid = signInTestUser(ALICE_EMAIL)
    val context = ApplicationProvider.getApplicationContext<Context>()
    val syncManager = FirestoreSyncManager(context, firestore, auth)

    syncManager.setUserFullName("Mina Youssef Boutros")
    syncManager.setUserEmail(ALICE_EMAIL)
    syncManager.setUserChurchAndService("St. Mark Church", "Youth Service")

    val firstResult = withTimeout(DEFAULT_TIMEOUT_MS) {
      syncManager.syncProfileToCloud()
    }
    assertTrue("Expected initial profile sync to succeed for uid=$uid, syncKey=${syncManager.syncKey.value}: ${firstResult.exceptionOrNull()}", firstResult.isSuccess)

    // Update profile fields to validate update rule (immutable userId & createdAt preserved)
    syncManager.setUserChurchAndService("St. Mary Church", "Deacon Service")
    val updateResult = withTimeout(DEFAULT_TIMEOUT_MS) {
      syncManager.syncProfileToCloud()
    }
    assertTrue("Expected profile update to succeed: ${updateResult.exceptionOrNull()}", updateResult.isSuccess)

    val snapshot = withTimeout(DEFAULT_TIMEOUT_MS) {
      firestore.collection("users").document(uid).get().let { task ->
        com.google.android.gms.tasks.Tasks.await(task)
      }
    }
    assertTrue(snapshot.exists())
    assertEquals("St. Mary Church", snapshot.getString("churchName"))
  }

  @Test
  fun upsertAndFetchPrayerLogs_authenticatedOwner_succeeds() = runBlocking {
    signInTestUser(ALICE_EMAIL)
    val context = ApplicationProvider.getApplicationContext<Context>()
    val syncManager = FirestoreSyncManager(context, firestore, auth)
    val uniqueTs = System.currentTimeMillis() + (UUID.randomUUID().hashCode() and 0xFFFF)

    val log = PrayerLogEntity(
      prayerCode = "prime",
      timestamp = uniqueTs,
      dateString = "2026-11-13",
      year = 2026,
      month = 11,
      day = 13,
      hour = 6,
      minute = 30
    )

    val upsertResult = withTimeout(DEFAULT_TIMEOUT_MS) {
      syncManager.upsertPrayerLogSuspending(log)
    }
    assertTrue("Expected prayer log upsert to succeed: ${upsertResult.exceptionOrNull()}", upsertResult.isSuccess)
    val docId = upsertResult.getOrThrow()
    assertTrue(docId.startsWith("prime_2026-11-13_"))

    val fetchResult = withTimeout(DEFAULT_TIMEOUT_MS) {
      syncManager.fetchPrayerLogsSuspending()
    }
    assertTrue("Expected prayer log fetch to succeed: ${fetchResult.exceptionOrNull()}", fetchResult.isSuccess)
    assertTrue(fetchResult.getOrThrow().any { it.timestamp == uniqueTs && it.prayerCode == "prime" })
  }

  @Test
  fun observePrayerLogsFlow_authenticatedOwner_emitsRealtimeUpdates() = runBlocking {
    signInTestUser(ALICE_EMAIL)
    val context = ApplicationProvider.getApplicationContext<Context>()
    val syncManager = FirestoreSyncManager(context, firestore, auth)
    val uniqueTs = System.currentTimeMillis() + (UUID.randomUUID().hashCode() and 0xFFFF) + 1000L

    val log = PrayerLogEntity(
      prayerCode = "terce",
      timestamp = uniqueTs,
      dateString = "2026-11-13",
      year = 2026,
      month = 11,
      day = 13,
      hour = 9,
      minute = 0
    )

    val upsertResult = withTimeout(DEFAULT_TIMEOUT_MS) {
      syncManager.upsertPrayerLogSuspending(log)
    }
    assertTrue(upsertResult.isSuccess)

    val emittedLogs = withTimeout(FLOW_TIMEOUT_MS) {
      syncManager.observePrayerLogsFlow().first { list -> list.any { it.timestamp == uniqueTs } }
    }
    assertTrue(emittedLogs.any { it.prayerCode == "terce" && it.timestamp == uniqueTs })
  }

  @Test
  fun upsertArrowPrayerLogAndSettings_authenticatedOwner_succeeds() = runBlocking {
    signInTestUser(ALICE_EMAIL)
    val context = ApplicationProvider.getApplicationContext<Context>()
    val syncManager = FirestoreSyncManager(context, firestore, auth)
    val uniqueTs = System.currentTimeMillis() + (UUID.randomUUID().hashCode() and 0xFFFF) + 2000L

    val arrowLog = ArrowPrayerLogEntity(
      count = 33,
      prayerText = "يارب يسوع المسيح ارحمني",
      timestamp = uniqueTs,
      dateString = "2026-11-13",
      year = 2026,
      month = 11,
      quarter = 4,
      halfYear = 2,
      day = 13,
      hour = 12,
      minute = 10
    )

    val arrowResult = withTimeout(DEFAULT_TIMEOUT_MS) {
      syncManager.upsertArrowPrayerLogSuspending(arrowLog)
    }
    assertTrue("Expected arrow log upsert to succeed: ${arrowResult.exceptionOrNull()}", arrowResult.isSuccess)

    val settingsResult = withTimeout(DEFAULT_TIMEOUT_MS) {
      syncManager.upsertAlarmSettingsSuspending(
        languageCode = "ar",
        alarmSettings = listOf(
          PrayerAlarmSetting(
            prayerCode = "prime",
            hour = 6,
            minute = 0,
            isEnabled = true,
            soundEnabled = true,
            vibrateEnabled = true,
            soundId = "church_bells"
          )
        )
      )
    }
    assertTrue("Expected settings upsert to succeed: ${settingsResult.exceptionOrNull()}", settingsResult.isSuccess)
  }

  @Test
  fun crossUserAccess_otherUserReadsAliceLogs_failsWithPermissionDenied() = runBlocking {
    val aliceUid = signInTestUser(ALICE_EMAIL)
    val context = ApplicationProvider.getApplicationContext<Context>()
    val aliceSyncManager = FirestoreSyncManager(context, firestore, auth)
    val uniqueTs = System.currentTimeMillis() + (UUID.randomUUID().hashCode() and 0xFFFF) + 3000L

    val log = PrayerLogEntity(
      prayerCode = "sext",
      timestamp = uniqueTs,
      dateString = "2026-11-13",
      year = 2026,
      month = 11,
      day = 13,
      hour = 12,
      minute = 0
    )
    val createResult = withTimeout(DEFAULT_TIMEOUT_MS) {
      aliceSyncManager.upsertPrayerLogSuspending(log)
    }
    assertTrue(createResult.isSuccess)
    val docId = createResult.getOrThrow()

    // Sign in as Bob and attempt to read Alice's prayer log document directly
    signInTestUser(BOB_EMAIL)
    try {
      withTimeout(DEFAULT_TIMEOUT_MS) {
        com.google.android.gms.tasks.Tasks.await(
          firestore.collection("users").document(aliceUid)
            .collection("prayer_logs").document(docId).get()
        )
      }
      fail("Expected PERMISSION_DENIED when Bob reads Alice's prayer log")
    } catch (e: Exception) {
      val firestoreException = e.unwrapFirestoreException()
      assertNotNull("Expected FirebaseFirestoreException, got $e", firestoreException)
      assertEquals(FirebaseFirestoreException.Code.PERMISSION_DENIED, firestoreException!!.code)
    }
  }

  @Test
  fun unauthenticatedAccess_signedOutUser_rejectedBySyncManagerAndRules() = runBlocking {
    auth.signOut()
    val context = ApplicationProvider.getApplicationContext<Context>()
    val syncManager = FirestoreSyncManager(context, firestore, auth)

    // 1. Client guard rejects unauthenticated sync attempts before hitting network
    val log = PrayerLogEntity(
      prayerCode = "prime",
      timestamp = 1730000005000L,
      dateString = "2026-11-13",
      year = 2026,
      month = 11,
      day = 13,
      hour = 6,
      minute = 0
    )
    try {
      withTimeout(DEFAULT_TIMEOUT_MS) {
        syncManager.upsertPrayerLogSuspending(log)
      }
      fail("Expected IllegalStateException when calling upsertPrayerLogSuspending while signed out")
    } catch (e: IllegalStateException) {
      assertTrue(e.message?.contains("signed in") == true)
    }

    // 2. Rules reject unauthenticated Flow listener with PERMISSION_DENIED
    try {
      withTimeout(FLOW_TIMEOUT_MS) {
        firestore.collection("users").document("alice_123")
          .collection("prayer_logs").snapshots().first()
      }
      fail("Expected PERMISSION_DENIED for unauthenticated snapshot listener")
    } catch (e: Exception) {
      val firestoreException = e.unwrapFirestoreException()
      assertNotNull("Expected nested FirebaseFirestoreException, got $e", firestoreException)
      assertEquals(FirebaseFirestoreException.Code.PERMISSION_DENIED, firestoreException!!.code)
    }
  }

  private companion object {
    const val ALICE_EMAIL = "alice@test.com"
    const val BOB_EMAIL = "bob@test.com"
    const val DEFAULT_TIMEOUT_MS = 5000L
    const val FLOW_TIMEOUT_MS = 3000L
  }
}
