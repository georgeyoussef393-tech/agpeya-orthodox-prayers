package com.example.data.sync

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.example.R
import com.example.data.model.ArrowPrayerLogEntity
import com.example.data.model.PrayerAlarmSetting
import com.example.data.model.PrayerLogEntity
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.snapshots
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject
import java.util.UUID

enum class SyncStatus {
    IDLE,
    SYNCING,
    SYNCED,
    OFFLINE,
    ERROR
}

enum class OperationType(val value: String) {
    CREATE("create"),
    UPDATE("update"),
    DELETE("delete"),
    LIST("list"),
    GET("get"),
    WRITE("write")
}

/**
 * Enterprise-grade Cross-Device Sync Manager using Google Cloud Firestore.
 * Ensures bidirectional, real-time synchronization of Agpeya prayer history and settings
 * scoped to the Google-authenticated Firebase user (/users/{uid}/...) while maintaining
 * 100% offline local-first operability via Room.
 */
class FirestoreSyncManager(
    private val context: Context,
    injectedFirestore: FirebaseFirestore? = null,
    injectedAuth: FirebaseAuth? = null
) {

    private val TAG = "FirestoreSyncManager"
    private val prefs: SharedPreferences =
        context.applicationContext.getSharedPreferences("agpeya_cloud_sync_prefs", Context.MODE_PRIVATE)

    private val ioScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    private val _syncStatus = MutableStateFlow(SyncStatus.IDLE)
    val syncStatus: StateFlow<SyncStatus> = _syncStatus.asStateFlow()

    private val _userEmail = MutableStateFlow(prefs.getString("user_email", null))
    val userEmail: StateFlow<String?> = _userEmail.asStateFlow()

    private val _userPhone = MutableStateFlow(prefs.getString("user_phone", null))
    val userPhone: StateFlow<String?> = _userPhone.asStateFlow()

    private val _userFullName = MutableStateFlow(prefs.getString("user_full_name", null))
    val userFullName: StateFlow<String?> = _userFullName.asStateFlow()

    private val _syncKey = MutableStateFlow(resolveInitialSyncKey())
    val syncKey: StateFlow<String> = _syncKey.asStateFlow()

    private val _lastSyncTimestamp = MutableStateFlow(prefs.getLong("last_sync_time", 0L))
    val lastSyncTimestamp: StateFlow<Long> = _lastSyncTimestamp.asStateFlow()

    private val _isRealtimeSyncActive = MutableStateFlow(prefs.getBoolean("realtime_sync_enabled", true))
    val isRealtimeSyncActive: StateFlow<Boolean> = _isRealtimeSyncActive.asStateFlow()

    private var firestore: FirebaseFirestore? = injectedFirestore
    private var auth: FirebaseAuth? = injectedAuth
    private var logsListenerRegistration: ListenerRegistration? = null
    private var arrowLogsListenerRegistration: ListenerRegistration? = null
    private var settingsListenerRegistration: ListenerRegistration? = null
    private val profileSyncMutex = Mutex()

    init {
        if (firestore == null || auth == null) {
            initializeFirebaseIfPossible()
        }
    }

    private fun resolveInitialSyncKey(): String {
        val savedPhone = prefs.getString("user_phone", null)
        if (!savedPhone.isNullOrBlank()) {
            return normalizePhoneToKey(savedPhone)
        }
        val savedEmail = prefs.getString("user_email", null)
        if (!savedEmail.isNullOrBlank()) {
            return normalizeEmailToKey(savedEmail)
        }
        val existing = prefs.getString("user_sync_key", null)
        if (!existing.isNullOrBlank()) {
            return existing
        }
        val generated = "agpeya-sync-" + UUID.randomUUID().toString().substring(0, 8)
        prefs.edit().putString("user_sync_key", generated).apply()
        return generated
    }

    companion object {
        fun normalizeEmailToKey(email: String): String {
            return "usr_" + email.trim().lowercase(java.util.Locale.ROOT)
                .replace("@", "_at_")
                .replace(".", "_")
                .replace("-", "_")
                .replace("+", "_")
        }

        fun normalizePhoneToKey(phone: String): String {
            val digits = phone.replace("[^0-9]".toRegex(), "")
            return "ph_" + digits
        }
    }

    fun handleFirestoreError(exception: Throwable, operationType: OperationType, path: String?): String {
        val causes = generateSequence(exception) { it.cause }
        val rootError = causes.firstOrNull { it is FirebaseFirestoreException }
            ?: causes.firstOrNull { it !is CancellationException }
            ?: exception

        val currentUser = auth?.currentUser
        val providerInfoList = currentUser?.providerData?.map { provider ->
            JSONObject().apply {
                put("providerId", provider.providerId)
                put("email", provider.email)
            }
        } ?: emptyList()

        val authInfoJson = JSONObject().apply {
            put("userId", currentUser?.uid)
            put("email", currentUser?.email)
            put("emailVerified", currentUser?.isEmailVerified)
            put("tenantId", currentUser?.tenantId)
            put("providerInfo", JSONArray(providerInfoList))
        }

        val errorInfoJson = JSONObject().apply {
            put("error", rootError.message ?: rootError.toString())
            put("operationType", operationType.value)
            put("path", path)
            put("authInfo", authInfoJson)
        }

        val jsonString = errorInfoJson.toString()
        Log.e("FirestoreError", "Firestore Error: $jsonString")
        return jsonString
    }

    private fun requireUserId(): String {
        return auth?.currentUser?.uid
            ?: throw IllegalStateException("User must be signed in with Google before accessing Firestore.")
    }

    fun isGoogleSignedIn(): Boolean {
        return auth?.currentUser != null
    }

    fun getSignedInGoogleEmail(): String? {
        return auth?.currentUser?.email
    }

    fun setUserPhone(phone: String?, onComplete: () -> Unit = {}) {
        val trimmed = phone?.trim()
        if (!trimmed.isNullOrBlank()) {
            val key = normalizePhoneToKey(trimmed)
            prefs.edit()
                .putString("user_phone", trimmed)
                .putString("user_email", "$trimmed (هاتف)")
                .putString("user_sync_key", key)
                .apply()
            _userPhone.value = trimmed
            _userEmail.value = "$trimmed (هاتف)"
            _syncKey.value = key
        } else {
            prefs.edit()
                .remove("user_phone")
                .remove("user_email")
                .remove("user_sync_key")
                .apply()
            _userPhone.value = null
            _userEmail.value = null
            val fallbackKey = getOrGenerateSyncKey()
            _syncKey.value = fallbackKey
        }
        stopRealtimeListener()
        onComplete()
    }

    fun authenticateWithPhone(
        phone: String,
        fullName: String? = null,
        onResult: (Boolean, String?) -> Unit
    ) {
        val cleanPhone = phone.trim()
        val digits = cleanPhone.replace("[^0-9]".toRegex(), "")
        if (digits.length < 8) {
            onResult(false, "رقم الهاتف يجب أن يتكون من 8 أرقام على الأقل")
            return
        }
        if (!fullName.isNullOrBlank()) {
            setUserFullName(fullName)
        }
        setUserPhone(cleanPhone) {
            uploadUserProfileToFirestore()
            onResult(true, null)
        }
    }

    fun setUserEmail(email: String?, onComplete: () -> Unit = {}) {
        val trimmed = email?.trim()
        if (!trimmed.isNullOrBlank()) {
            val key = normalizeEmailToKey(trimmed)
            prefs.edit()
                .putString("user_email", trimmed)
                .putString("user_sync_key", key)
                .apply()
            _userEmail.value = trimmed
            _syncKey.value = key
        } else {
            prefs.edit()
                .remove("user_email")
                .remove("user_sync_key")
                .apply()
            _userEmail.value = null
            val fallbackKey = getOrGenerateSyncKey()
            _syncKey.value = fallbackKey
        }
        stopRealtimeListener()
        onComplete()
    }

    fun setUserFullName(fullName: String?) {
        val trimmed = fullName?.trim()
        if (!trimmed.isNullOrBlank()) {
            prefs.edit().putString("user_full_name", trimmed).apply()
            _userFullName.value = trimmed
        } else {
            prefs.edit().remove("user_full_name").apply()
            _userFullName.value = null
        }
        uploadUserProfileToFirestore()
    }

    fun setUserChurchAndService(church: String?, service: String?) {
        if (!church.isNullOrBlank()) {
            prefs.edit().putString("user_church_name", church.trim()).apply()
        } else {
            prefs.edit().remove("user_church_name").apply()
        }
        if (!service.isNullOrBlank()) {
            prefs.edit().putString("user_service_name", service.trim()).apply()
        } else {
            prefs.edit().remove("user_service_name").apply()
        }
        uploadUserProfileToFirestore()
    }

    fun onGoogleAuthenticated(email: String?, displayName: String?) {
        if (!email.isNullOrBlank() && _userEmail.value.isNullOrBlank()) {
            setUserEmail(email)
        }
        if (!displayName.isNullOrBlank() && _userFullName.value.isNullOrBlank()) {
            prefs.edit().putString("user_full_name", displayName.trim()).apply()
            _userFullName.value = displayName.trim()
        }
        uploadUserProfileToFirestore()
    }

    private suspend fun upsertDocumentWithServerTimestamps(
        docRef: DocumentReference,
        fields: MutableMap<String, Any>,
        path: String
    ) {
        val existingSnap = try {
            docRef.get().await()
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            handleFirestoreError(e, OperationType.GET, path)
            throw e
        }

        if (!existingSnap.exists()) {
            fields["createdAt"] = FieldValue.serverTimestamp()
            fields["updatedAt"] = FieldValue.serverTimestamp()
            try {
                docRef.set(fields).await()
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                handleFirestoreError(e, OperationType.CREATE, path)
                throw e
            }
        } else {
            fields["updatedAt"] = FieldValue.serverTimestamp()
            try {
                docRef.update(fields as Map<String, Any>).await()
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                handleFirestoreError(e, OperationType.UPDATE, path)
                throw e
            }
        }
    }

    suspend fun syncProfileToCloud(): Result<Unit> = withContext(Dispatchers.IO) {
        profileSyncMutex.withLock {
            if (!isFirebaseReady()) return@withContext Result.failure(IllegalStateException("Firebase not initialized"))
            val db = firestore ?: return@withContext Result.failure(IllegalStateException("Firestore unavailable"))
            val uid = requireUserId()
            val path = "users/$uid"
            val docRef = db.collection("users").document(uid)

            val profileData = hashMapOf<String, Any>(
                "userId" to uid,
                "fullName" to (prefs.getString("user_full_name", null) ?: ""),
                "email" to (_userEmail.value ?: auth?.currentUser?.email ?: ""),
                "phone" to (_userPhone.value ?: ""),
                "churchName" to (prefs.getString("user_church_name", null) ?: ""),
                "serviceName" to (prefs.getString("user_service_name", null) ?: ""),
                "syncKey" to _syncKey.value
            )

            return@withContext try {
                upsertDocumentWithServerTimestamps(docRef, profileData, path)
                Result.success(Unit)
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    /**
     * Uploads user profile (Full Name, Phone, Email, Church Name, Service Name)
     * to Firestore under /users/{uid} when signed in with Google.
     */
    fun uploadUserProfileToFirestore() {
        if (!isFirebaseReady() || auth?.currentUser == null) return
        ioScope.launch {
            syncProfileToCloud()
        }
    }

    fun authenticateWithEmail(
        email: String,
        @Suppress("UNUSED_PARAMETER") password: String? = null,
        onResult: (Boolean, String?) -> Unit
    ) {
        val cleanEmail = email.trim()
        if (cleanEmail.isBlank() || !android.util.Patterns.EMAIL_ADDRESS.matcher(cleanEmail).matches()) {
            onResult(false, "Invalid email format")
            return
        }

        setUserEmail(cleanEmail) {
            uploadUserProfileToFirestore()
            onResult(true, null)
        }
    }

    private fun initializeFirebaseIfPossible(): Boolean {
        return try {
            if (FirebaseApp.getApps(context).isEmpty()) {
                try {
                    FirebaseApp.initializeApp(context)
                } catch (e: Exception) {
                    Log.w(TAG, "FirebaseApp not auto-initialized: ${e.message}")
                }
            }

            if (FirebaseApp.getApps(context).isNotEmpty()) {
                val databaseId = try {
                    context.applicationContext.getString(R.string.firestore_database_id)
                } catch (e: Exception) {
                    null
                }
                if (databaseId.isNullOrBlank()) {
                    _syncStatus.value = SyncStatus.OFFLINE
                    return false
                }
                firestore = FirebaseFirestore.getInstance(databaseId)
                auth = FirebaseAuth.getInstance()
                true
            } else {
                _syncStatus.value = SyncStatus.OFFLINE
                false
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error initializing Firebase: ${e.message}")
            _syncStatus.value = SyncStatus.OFFLINE
            false
        }
    }

    fun isFirebaseReady(): Boolean {
        if (firestore != null) return true
        return initializeFirebaseIfPossible()
    }

    /**
     * Retrieves or generates a unique, human-friendly Sync Key
     * (e.g. "agpeya-sync-8ab12c").
     */
    fun getOrGenerateSyncKey(): String {
        val existing = prefs.getString("user_sync_key", null)
        if (!existing.isNullOrBlank()) {
            return existing
        }
        val generated = "agpeya-sync-" + UUID.randomUUID().toString().substring(0, 8)
        prefs.edit().putString("user_sync_key", generated).apply()
        return generated
    }

    fun updateSyncKey(newKey: String, onComplete: () -> Unit = {}) {
        val trimmed = newKey.trim()
        if (trimmed.isNotBlank() && trimmed != _syncKey.value) {
            prefs.edit().putString("user_sync_key", trimmed).apply()
            _syncKey.value = trimmed
            stopRealtimeListener()
            onComplete()
        }
    }

    fun setRealtimeSyncEnabled(enabled: Boolean) {
        prefs.edit().putBoolean("realtime_sync_enabled", enabled).apply()
        _isRealtimeSyncActive.value = enabled
        if (!enabled) {
            stopRealtimeListener()
        }
    }

    suspend fun upsertPrayerLogSuspending(log: PrayerLogEntity): Result<String> = withContext(Dispatchers.IO) {
        if (!isFirebaseReady()) {
            return@withContext Result.failure(IllegalStateException("Firebase not initialized"))
        }
        val db = firestore ?: return@withContext Result.failure(IllegalStateException("Firestore unavailable"))
        val uid = requireUserId()
        val docId = "${log.prayerCode}_${log.dateString}_${log.timestamp}"
        val path = "users/$uid/prayer_logs/$docId"
        val docRef = db.collection("users")
            .document(uid)
            .collection("prayer_logs")
            .document(docId)

        val data = hashMapOf<String, Any>(
            "prayerCode" to log.prayerCode,
            "timestamp" to log.timestamp,
            "dateString" to log.dateString,
            "year" to log.year,
            "month" to log.month,
            "day" to log.day,
            "hour" to log.hour,
            "minute" to log.minute
        )

        return@withContext try {
            upsertDocumentWithServerTimestamps(docRef, data, path)
            _lastSyncTimestamp.value = System.currentTimeMillis()
            prefs.edit().putLong("last_sync_time", _lastSyncTimestamp.value).apply()
            _syncStatus.value = SyncStatus.SYNCED
            Result.success(docId)
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun fetchPrayerLogsSuspending(): Result<List<PrayerLogEntity>> = withContext(Dispatchers.IO) {
        if (!isFirebaseReady()) {
            return@withContext Result.failure(IllegalStateException("Firebase not initialized"))
        }
        val db = firestore ?: return@withContext Result.failure(IllegalStateException("Firestore unavailable"))
        val uid = requireUserId()
        val path = "users/$uid/prayer_logs"

        return@withContext try {
            val snapshot = db.collection("users")
                .document(uid)
                .collection("prayer_logs")
                .get()
                .await()
            val logs = snapshot.documents.mapNotNull { documentToPrayerLog(it) }
            Result.success(logs)
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            handleFirestoreError(e, OperationType.LIST, path)
            Result.failure(e)
        }
    }

    fun observePrayerLogsFlow(): Flow<List<PrayerLogEntity>> = flow {
        val db = firestore ?: throw IllegalStateException("Firestore unavailable")
        val uid = requireUserId()
        val path = "users/$uid/prayer_logs"
        emitAll(
            db.collection("users")
                .document(uid)
                .collection("prayer_logs")
                .snapshots()
                .map { snapshot -> snapshot.documents.mapNotNull { documentToPrayerLog(it) } }
                .catch { error ->
                    val firestoreError = generateSequence(error) { it.cause }
                        .filterIsInstance<FirebaseFirestoreException>()
                        .firstOrNull()
                    if (firestoreError == null && error is CancellationException) throw error
                    val exceptionToLog = firestoreError ?: error
                    if (auth?.currentUser?.uid == uid) {
                        handleFirestoreError(exceptionToLog, OperationType.LIST, path)
                    }
                    throw error
                }
        )
    }

    /**
     * Uploads a single prayer log entry to Firestore when signed in with Google.
     */
    fun uploadPrayerLog(log: PrayerLogEntity) {
        if (!isFirebaseReady() || auth?.currentUser == null) {
            Log.d(TAG, "Local-first: Google session not active, log kept locally.")
            return
        }
        ioScope.launch {
            upsertPrayerLogSuspending(log)
        }
    }

    /**
     * Deletes a prayer log from Firestore.
     */
    fun deletePrayerLog(prayerCode: String, dateString: String) {
        if (!isFirebaseReady() || auth?.currentUser == null) return
        val db = firestore ?: return
        val uid = auth?.currentUser?.uid ?: return
        val path = "users/$uid/prayer_logs"

        db.collection("users")
            .document(uid)
            .collection("prayer_logs")
            .whereEqualTo("prayerCode", prayerCode)
            .whereEqualTo("dateString", dateString)
            .get()
            .addOnSuccessListener { snapshot ->
                for (doc in snapshot.documents) {
                    doc.reference.delete()
                        .addOnFailureListener { delEx ->
                            handleFirestoreError(delEx, OperationType.DELETE, doc.reference.path)
                        }
                }
                Log.d(TAG, "Remote prayer logs deleted for: $prayerCode on $dateString")
            }
            .addOnFailureListener { e ->
                handleFirestoreError(e, OperationType.LIST, path)
            }
    }

    suspend fun upsertArrowPrayerLogSuspending(log: ArrowPrayerLogEntity): Result<String> = withContext(Dispatchers.IO) {
        if (!isFirebaseReady()) {
            return@withContext Result.failure(IllegalStateException("Firebase not initialized"))
        }
        val db = firestore ?: return@withContext Result.failure(IllegalStateException("Firestore unavailable"))
        val uid = requireUserId()
        val docId = "arrow_${log.dateString}_${log.timestamp}"
        val path = "users/$uid/arrow_prayer_logs/$docId"
        val docRef = db.collection("users")
            .document(uid)
            .collection("arrow_prayer_logs")
            .document(docId)

        val data = hashMapOf<String, Any>(
            "count" to log.count,
            "prayerText" to log.prayerText,
            "timestamp" to log.timestamp,
            "dateString" to log.dateString,
            "year" to log.year,
            "month" to log.month,
            "quarter" to log.quarter,
            "halfYear" to log.halfYear,
            "day" to log.day,
            "hour" to log.hour,
            "minute" to log.minute
        )

        return@withContext try {
            upsertDocumentWithServerTimestamps(docRef, data, path)
            _lastSyncTimestamp.value = System.currentTimeMillis()
            prefs.edit().putLong("last_sync_time", _lastSyncTimestamp.value).apply()
            Result.success(docId)
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Uploads an arrow prayer log entry to Firestore when signed in with Google.
     */
    fun uploadArrowPrayerLog(log: ArrowPrayerLogEntity) {
        if (!isFirebaseReady() || auth?.currentUser == null) return
        ioScope.launch {
            upsertArrowPrayerLogSuspending(log)
        }
    }

    suspend fun upsertAlarmSettingsSuspending(
        languageCode: String,
        alarmSettings: List<PrayerAlarmSetting>
    ): Result<Unit> = withContext(Dispatchers.IO) {
        if (!isFirebaseReady()) {
            return@withContext Result.failure(IllegalStateException("Firebase not initialized"))
        }
        val db = firestore ?: return@withContext Result.failure(IllegalStateException("Firestore unavailable"))
        val uid = requireUserId()
        val path = "users/$uid/settings/agpeya_settings"
        val docRef = db.collection("users")
            .document(uid)
            .collection("settings")
            .document("agpeya_settings")

        val alarmsMap = hashMapOf<String, Any>()
        for (setting in alarmSettings) {
            alarmsMap[setting.prayerCode] = hashMapOf(
                "hour" to setting.hour,
                "minute" to setting.minute,
                "isEnabled" to setting.isEnabled,
                "soundEnabled" to setting.soundEnabled,
                "vibrateEnabled" to setting.vibrateEnabled,
                "soundId" to setting.soundId
            )
        }

        val payload = hashMapOf<String, Any>(
            "languageCode" to languageCode,
            "alarms" to alarmsMap
        )

        return@withContext try {
            upsertDocumentWithServerTimestamps(docRef, payload, path)
            val now = System.currentTimeMillis()
            _lastSyncTimestamp.value = now
            prefs.edit().putLong("last_sync_time", now).apply()
            _syncStatus.value = SyncStatus.SYNCED
            Result.success(Unit)
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Directly uploads alarm preferences to Firestore when signed in with Google.
     */
    fun uploadAlarmSettings(
        languageCode: String,
        alarmSettings: List<PrayerAlarmSetting>
    ) {
        if (!isFirebaseReady() || auth?.currentUser == null) return
        ioScope.launch {
            upsertAlarmSettingsSuspending(languageCode, alarmSettings)
        }
    }

    fun syncSettings(
        languageCode: String,
        alarmSettings: List<PrayerAlarmSetting>,
        @Suppress("UNUSED_PARAMETER") onRemoteAlarmsReceived: ((String, List<PrayerAlarmSetting>) -> Unit)? = null
    ) {
        uploadAlarmSettings(languageCode, alarmSettings)
    }

    /**
     * Performs a full bidirectional sync across devices when signed in with Google.
     */
    suspend fun performFullBidirectionalSync(
        localLogs: List<PrayerLogEntity>,
        localArrowLogs: List<ArrowPrayerLogEntity> = emptyList(),
        localLanguageCode: String = "ar",
        localAlarmSettings: List<PrayerAlarmSetting> = emptyList(),
        onRemoteLogsReceived: suspend (List<PrayerLogEntity>) -> Unit,
        onRemoteArrowLogsReceived: (suspend (List<ArrowPrayerLogEntity>) -> Unit)? = null,
        onRemoteAlarmsReceived: (suspend (String, List<PrayerAlarmSetting>) -> Unit)? = null
    ) = withContext(Dispatchers.IO) {
        val currentUid = auth?.currentUser?.uid
        if (!isFirebaseReady() || currentUid == null) {
            _syncStatus.value = SyncStatus.OFFLINE
            return@withContext
        }

        val db = firestore ?: run {
            _syncStatus.value = SyncStatus.OFFLINE
            return@withContext
        }

        try {
            _syncStatus.value = SyncStatus.SYNCING

            // 1. Sync Canonical Prayer Reading Logs
            val logsPath = "users/$currentUid/prayer_logs"
            val snapshot = try {
                db.collection("users")
                    .document(currentUid)
                    .collection("prayer_logs")
                    .get()
                    .await()
            } catch (e: Exception) {
                handleFirestoreError(e, OperationType.LIST, logsPath)
                throw e
            }

            val remoteLogs = mutableListOf<PrayerLogEntity>()
            val remoteKeys = mutableSetOf<String>()

            for (doc in snapshot.documents) {
                val entity = documentToPrayerLog(doc)
                if (entity != null) {
                    remoteLogs.add(entity)
                    remoteKeys.add("${entity.prayerCode}_${entity.dateString}")
                }
            }

            if (remoteLogs.isNotEmpty()) {
                onRemoteLogsReceived(remoteLogs)
            }

            for (local in localLogs) {
                val key = "${local.prayerCode}_${local.dateString}"
                if (!remoteKeys.contains(key)) {
                    upsertPrayerLogSuspending(local)
                }
            }

            // 2. Sync Arrow Prayer Logs
            if (onRemoteArrowLogsReceived != null) {
                val arrowPath = "users/$currentUid/arrow_prayer_logs"
                val arrowSnapshot = try {
                    db.collection("users")
                        .document(currentUid)
                        .collection("arrow_prayer_logs")
                        .get()
                        .await()
                } catch (e: Exception) {
                    handleFirestoreError(e, OperationType.LIST, arrowPath)
                    throw e
                }

                val remoteArrowLogs = mutableListOf<ArrowPrayerLogEntity>()
                val remoteArrowTimestamps = mutableSetOf<Long>()

                for (doc in arrowSnapshot.documents) {
                    val arrowEntity = documentToArrowPrayerLog(doc)
                    if (arrowEntity != null) {
                        remoteArrowLogs.add(arrowEntity)
                        remoteArrowTimestamps.add(arrowEntity.timestamp)
                    }
                }

                if (remoteArrowLogs.isNotEmpty()) {
                    onRemoteArrowLogsReceived(remoteArrowLogs)
                }

                for (local in localArrowLogs) {
                    if (!remoteArrowTimestamps.contains(local.timestamp)) {
                        upsertArrowPrayerLogSuspending(local)
                    }
                }
            }

            // 3. Sync Alarm Settings & Preferences
            if (onRemoteAlarmsReceived != null) {
                val settingsPath = "users/$currentUid/settings/agpeya_settings"
                val settingsDocRef = db.collection("users")
                    .document(currentUid)
                    .collection("settings")
                    .document("agpeya_settings")

                val doc = try {
                    settingsDocRef.get().await()
                } catch (e: Exception) {
                    handleFirestoreError(e, OperationType.GET, settingsPath)
                    throw e
                }
                if (doc.exists()) {
                    val remoteLang = doc.getString("languageCode") ?: localLanguageCode
                    val alarmsList = mutableListOf<PrayerAlarmSetting>()
                    val alarmsMap = doc.get("alarms") as? Map<*, *>
                    if (alarmsMap != null) {
                        for ((code, data) in alarmsMap) {
                            val map = data as? Map<*, *> ?: continue
                            alarmsList.add(
                                PrayerAlarmSetting(
                                    prayerCode = code.toString(),
                                    hour = (map["hour"] as? Number)?.toInt() ?: 0,
                                    minute = (map["minute"] as? Number)?.toInt() ?: 0,
                                    isEnabled = map["isEnabled"] as? Boolean ?: true,
                                    soundEnabled = map["soundEnabled"] as? Boolean ?: true,
                                    vibrateEnabled = map["vibrateEnabled"] as? Boolean ?: true,
                                    soundId = map["soundId"] as? String ?: "church_bells"
                                )
                            )
                        }
                    }
                    if (alarmsList.isNotEmpty()) {
                        onRemoteAlarmsReceived(remoteLang, alarmsList)
                    } else if (localAlarmSettings.isNotEmpty()) {
                        upsertAlarmSettingsSuspending(localLanguageCode, localAlarmSettings)
                    }
                } else if (localAlarmSettings.isNotEmpty()) {
                    upsertAlarmSettingsSuspending(localLanguageCode, localAlarmSettings)
                }
            }

            // 4. Sync User Profile
            try {
                val userPath = "users/$currentUid"
                val userDocRef = db.collection("users").document(currentUid)
                val userDoc = userDocRef.get().await()
                if (userDoc.exists()) {
                    val remoteFullName = userDoc.getString("fullName")
                    val remoteChurch = userDoc.getString("churchName")
                    val remoteService = userDoc.getString("serviceName")

                    if (!remoteFullName.isNullOrBlank() && prefs.getString("user_full_name", null).isNullOrBlank()) {
                        prefs.edit().putString("user_full_name", remoteFullName).apply()
                        _userFullName.value = remoteFullName
                    }
                    if (!remoteChurch.isNullOrBlank() && prefs.getString("user_church_name", null).isNullOrBlank()) {
                        prefs.edit().putString("user_church_name", remoteChurch).apply()
                    }
                    if (!remoteService.isNullOrBlank() && prefs.getString("user_service_name", null).isNullOrBlank()) {
                        prefs.edit().putString("user_service_name", remoteService).apply()
                    }
                }
                syncProfileToCloud()
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                handleFirestoreError(e, OperationType.GET, "users/$currentUid")
            }

            val now = System.currentTimeMillis()
            _lastSyncTimestamp.value = now
            prefs.edit().putLong("last_sync_time", now).apply()
            _syncStatus.value = SyncStatus.SYNCED
            Log.d(TAG, "Full cross-device sync completed successfully.")
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Log.e(TAG, "Sync error: ${e.message}", e)
            _syncStatus.value = SyncStatus.ERROR
        }
    }

    /**
     * Starts real-time listeners for live cross-device sync scoped to the signed-in Google user.
     */
    fun startRealtimeListener(
        onRemoteLogAddedOrUpdated: (PrayerLogEntity) -> Unit,
        onRemoteArrowLogAddedOrUpdated: ((ArrowPrayerLogEntity) -> Unit)? = null,
        onRemoteAlarmsChanged: ((String, List<PrayerAlarmSetting>) -> Unit)? = null
    ) {
        val currentUid = auth?.currentUser?.uid
        if (!_isRealtimeSyncActive.value || !isFirebaseReady() || currentUid == null) return
        val db = firestore ?: return

        stopRealtimeListener()

        try {
            val logsPath = "users/$currentUid/prayer_logs"
            logsListenerRegistration = db.collection("users")
                .document(currentUid)
                .collection("prayer_logs")
                .addSnapshotListener { snapshots, error ->
                    if (error != null) {
                        if (auth?.currentUser?.uid == currentUid) {
                            handleFirestoreError(error, OperationType.LIST, logsPath)
                        }
                        return@addSnapshotListener
                    }
                    if (snapshots != null) {
                        for (dc in snapshots.documentChanges) {
                            val entity = documentToPrayerLog(dc.document)
                            if (entity != null) {
                                onRemoteLogAddedOrUpdated(entity)
                            }
                        }
                    }
                }

            if (onRemoteArrowLogAddedOrUpdated != null) {
                val arrowPath = "users/$currentUid/arrow_prayer_logs"
                arrowLogsListenerRegistration = db.collection("users")
                    .document(currentUid)
                    .collection("arrow_prayer_logs")
                    .addSnapshotListener { snapshots, error ->
                        if (error != null) {
                            if (auth?.currentUser?.uid == currentUid) {
                                handleFirestoreError(error, OperationType.LIST, arrowPath)
                            }
                            return@addSnapshotListener
                        }
                        if (snapshots != null) {
                            for (dc in snapshots.documentChanges) {
                                val entity = documentToArrowPrayerLog(dc.document)
                                if (entity != null) {
                                    onRemoteArrowLogAddedOrUpdated(entity)
                                }
                            }
                        }
                    }
            }

            if (onRemoteAlarmsChanged != null) {
                val settingsPath = "users/$currentUid/settings/agpeya_settings"
                settingsListenerRegistration = db.collection("users")
                    .document(currentUid)
                    .collection("settings")
                    .document("agpeya_settings")
                    .addSnapshotListener { doc, error ->
                        if (error != null) {
                            if (auth?.currentUser?.uid == currentUid) {
                                handleFirestoreError(error, OperationType.GET, settingsPath)
                            }
                            return@addSnapshotListener
                        }
                        if (doc == null || !doc.exists()) {
                            return@addSnapshotListener
                        }
                        val remoteLang = doc.getString("languageCode") ?: "ar"
                        val alarmsList = mutableListOf<PrayerAlarmSetting>()
                        val alarmsMap = doc.get("alarms") as? Map<*, *>
                        if (alarmsMap != null) {
                            for ((code, data) in alarmsMap) {
                                val map = data as? Map<*, *> ?: continue
                                alarmsList.add(
                                    PrayerAlarmSetting(
                                        prayerCode = code.toString(),
                                        hour = (map["hour"] as? Number)?.toInt() ?: 0,
                                        minute = (map["minute"] as? Number)?.toInt() ?: 0,
                                        isEnabled = map["isEnabled"] as? Boolean ?: true,
                                        soundEnabled = map["soundEnabled"] as? Boolean ?: true,
                                        vibrateEnabled = map["vibrateEnabled"] as? Boolean ?: true,
                                        soundId = map["soundId"] as? String ?: "church_bells"
                                    )
                                )
                            }
                        }
                        if (alarmsList.isNotEmpty()) {
                            onRemoteAlarmsChanged(remoteLang, alarmsList)
                        }
                    }
            }
        } catch (e: Exception) {
            handleFirestoreError(e, OperationType.LIST, "users/$currentUid")
        }
    }

    fun stopRealtimeListener() {
        logsListenerRegistration?.remove()
        logsListenerRegistration = null
        arrowLogsListenerRegistration?.remove()
        arrowLogsListenerRegistration = null
        settingsListenerRegistration?.remove()
        settingsListenerRegistration = null
    }

    private fun documentToPrayerLog(doc: DocumentSnapshot): PrayerLogEntity? {
        val prayerCode = doc.getString("prayerCode") ?: return null
        val dateString = doc.getString("dateString") ?: return null
        val timestamp = doc.getLong("timestamp") ?: System.currentTimeMillis()
        val year = doc.getLong("year")?.toInt() ?: 0
        val month = doc.getLong("month")?.toInt() ?: 0
        val day = doc.getLong("day")?.toInt() ?: 0
        val hour = doc.getLong("hour")?.toInt() ?: 0
        val minute = doc.getLong("minute")?.toInt() ?: 0

        return PrayerLogEntity(
            id = 0,
            prayerCode = prayerCode,
            timestamp = timestamp,
            dateString = dateString,
            year = year,
            month = month,
            day = day,
            hour = hour,
            minute = minute
        )
    }

    private fun documentToArrowPrayerLog(doc: DocumentSnapshot): ArrowPrayerLogEntity? {
        val count = doc.getLong("count")?.toInt() ?: return null
        val prayerText = doc.getString("prayerText") ?: return null
        val dateString = doc.getString("dateString") ?: return null
        val timestamp = doc.getLong("timestamp") ?: System.currentTimeMillis()
        val year = doc.getLong("year")?.toInt() ?: 0
        val month = doc.getLong("month")?.toInt() ?: 0
        val quarter = doc.getLong("quarter")?.toInt() ?: 1
        val halfYear = doc.getLong("halfYear")?.toInt() ?: 1
        val day = doc.getLong("day")?.toInt() ?: 0
        val hour = doc.getLong("hour")?.toInt() ?: 0
        val minute = doc.getLong("minute")?.toInt() ?: 0

        return ArrowPrayerLogEntity(
            id = 0,
            count = count,
            prayerText = prayerText,
            timestamp = timestamp,
            dateString = dateString,
            year = year,
            month = month,
            quarter = quarter,
            halfYear = halfYear,
            day = day,
            hour = hour,
            minute = minute
        )
    }
}
