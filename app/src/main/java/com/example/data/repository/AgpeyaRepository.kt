package com.example.data.repository

import android.content.Context
import android.content.SharedPreferences
import com.example.data.db.AgpeyaDatabase
import com.example.data.db.MeditationDao
import com.example.data.db.PrayerLogDao
import com.example.data.db.PrayerTextDao
import com.example.data.model.AgpeyaHourDetails
import com.example.data.model.CachedMeditationEntity
import com.example.data.model.CachedPrayerSectionEntity
import com.example.data.model.DailyScriptureProvider
import com.example.data.model.DailyVerseMeditation
import com.example.data.model.PrayerAlarmSetting
import com.example.data.model.PrayerId
import com.example.data.model.PrayerLogEntity
import com.example.data.model.PrayerSectionItem
import com.example.data.sync.FirestoreSyncManager
import com.example.localization.AppLanguage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class AgpeyaRepository(
    private val dao: PrayerLogDao,
    private val context: Context,
    private val prayerTextDao: PrayerTextDao = AgpeyaDatabase.getDatabase(context).prayerTextDao(),
    private val meditationDao: MeditationDao = AgpeyaDatabase.getDatabase(context).meditationDao(),
    private val spiritualNoteDao: com.example.data.db.SpiritualNoteDao = AgpeyaDatabase.getDatabase(context).spiritualNoteDao()
) {
    private val prefs: SharedPreferences =
        context.getSharedPreferences("agpeya_settings_prefs", Context.MODE_PRIVATE)

    val syncManager = FirestoreSyncManager(context)

    // Auth & Onboarding state - defaults to true so downloaded app matches preview directly
    fun isAuthOnboardingCompleted(): Boolean {
        return prefs.getBoolean("auth_onboarding_completed", true)
    }

    fun setAuthOnboardingCompleted(completed: Boolean) {
        prefs.edit().putBoolean("auth_onboarding_completed", completed).apply()
    }

    fun getUserEmail(): String? {
        return syncManager.userEmail.value ?: prefs.getString("user_email", null)
    }

    fun setUserEmail(email: String?, onComplete: () -> Unit = {}) {
        syncManager.setUserEmail(email, onComplete)
    }

    // User Full Name (الاسم ثلاثي أو رباعي)
    fun getUserFullName(): String? {
        return syncManager.userFullName.value ?: prefs.getString("user_full_name", null)
    }

    fun saveUserFullName(fullName: String?) {
        val clean = fullName?.trim()
        if (clean.isNullOrBlank()) {
            prefs.edit().remove("user_full_name").apply()
        } else {
            prefs.edit().putString("user_full_name", clean).apply()
        }
        syncManager.setUserFullName(clean)
    }

    // Text & Reading Font Size Scaling
    fun getFontSizeMultiplier(): Float {
        return prefs.getFloat("reading_font_size_multiplier", 1.0f)
    }

    fun saveFontSizeMultiplier(multiplier: Float) {
        val clamped = multiplier.coerceIn(0.85f, 1.85f)
        prefs.edit().putFloat("reading_font_size_multiplier", clamped).apply()
    }

    // Church Name (Optional User Ministry / Church)
    fun getChurchName(): String? {
        return prefs.getString("user_church_name", null)
    }

    fun saveChurchName(churchName: String?) {
        val clean = churchName?.trim()
        if (clean.isNullOrBlank()) {
            prefs.edit().remove("user_church_name").apply()
        } else {
            prefs.edit().putString("user_church_name", clean).apply()
        }
    }

    // Church Emblem / Logo Badge Code (coptic_cross, st_mark_lion, st_mary_dove, monastic_anchor)
    fun getChurchEmblem(): String {
        return prefs.getString("user_church_emblem", "coptic_cross") ?: "coptic_cross"
    }

    fun saveChurchEmblem(emblemCode: String) {
        prefs.edit().putString("user_church_emblem", emblemCode).apply()
    }

    suspend fun getAllLogsDirect(): List<PrayerLogEntity> {
        return dao.getAllLogsList()
    }

    suspend fun importBackupLogs(logs: List<PrayerLogEntity>) {
        dao.insertLogs(logs)
    }

    suspend fun clearAllLocalLogs() {
        dao.clearAllLogs()
    }

    // Language
    fun getSavedLanguage(): AppLanguage {
        val code = prefs.getString("app_language_code", null)
        if (code != null) {
            return AppLanguage.fromCode(code)
        }
        // Fallback to device system language if matched
        val systemLang = Locale.getDefault().language
        return AppLanguage.fromCode(systemLang)
    }

    fun saveLanguage(language: AppLanguage) {
        prefs.edit().putString("app_language_code", language.code).apply()
        CoroutineScope(Dispatchers.IO).launch {
            syncManager.syncSettings(language.code, getAllAlarmSettings()) { _, _ -> }
        }
    }

    // Spiritual Background & Reverence Atmosphere
    fun getSpiritualBackgroundTheme(): com.example.data.model.SpiritualBackgroundTheme {
        val id = prefs.getString("spiritual_bg_theme_id", com.example.data.model.SpiritualBackgroundTheme.CANDLE_SANCTUARY.id)
        return com.example.data.model.SpiritualBackgroundTheme.fromId(id)
    }

    fun saveSpiritualBackgroundTheme(theme: com.example.data.model.SpiritualBackgroundTheme) {
        prefs.edit().putString("spiritual_bg_theme_id", theme.id).apply()
    }

    fun getSpiritualBackgroundOpacity(): Float {
        return prefs.getFloat("spiritual_bg_opacity", 0.22f)
    }

    fun saveSpiritualBackgroundOpacity(opacity: Float) {
        prefs.edit().putFloat("spiritual_bg_opacity", opacity).apply()
    }

    fun isCandleGlowEnabled(): Boolean {
        return prefs.getBoolean("spiritual_candle_glow_enabled", true)
    }

    fun setCandleGlowEnabled(enabled: Boolean) {
        prefs.edit().putBoolean("spiritual_candle_glow_enabled", enabled).apply()
    }

    // Multilingual support preferences
    fun isBilingualEnabled(): Boolean {
        return prefs.getBoolean("multilingual_bilingual_enabled", false)
    }

    fun setBilingualEnabled(enabled: Boolean) {
        prefs.edit().putBoolean("multilingual_bilingual_enabled", enabled).apply()
    }

    fun getSecondaryLanguage(): AppLanguage {
        val code = prefs.getString("multilingual_secondary_lang_code", AppLanguage.COPTIC.code) ?: AppLanguage.COPTIC.code
        return AppLanguage.fromCode(code)
    }

    fun saveSecondaryLanguage(language: AppLanguage) {
        prefs.edit().putString("multilingual_secondary_lang_code", language.code).apply()
    }

    fun isPhoneticGuideEnabled(): Boolean {
        return prefs.getBoolean("multilingual_phonetic_guide_enabled", true)
    }

    fun setPhoneticGuideEnabled(enabled: Boolean) {
        prefs.edit().putBoolean("multilingual_phonetic_guide_enabled", enabled).apply()
    }

    // Timezone & Notification preferences
    fun isAutoTimezoneSyncEnabled(): Boolean {
        return prefs.getBoolean("timezone_auto_sync_enabled", true)
    }

    fun setAutoTimezoneSyncEnabled(enabled: Boolean) {
        prefs.edit().putBoolean("timezone_auto_sync_enabled", enabled).apply()
    }

    fun getSelectedTimezoneId(): String {
        return prefs.getString("selected_timezone_id", java.util.TimeZone.getDefault().id) ?: java.util.TimeZone.getDefault().id
    }

    fun saveSelectedTimezoneId(tzId: String) {
        prefs.edit().putString("selected_timezone_id", tzId).apply()
    }

    fun getNotificationLeadTimeMinutes(): Int {
        return prefs.getInt("notification_lead_time_minutes", 0)
    }

    fun saveNotificationLeadTimeMinutes(minutes: Int) {
        prefs.edit().putInt("notification_lead_time_minutes", minutes).apply()
    }

    fun isQuietHoursEnabled(): Boolean {
        return prefs.getBoolean("notification_quiet_hours_enabled", false)
    }

    fun setQuietHoursEnabled(enabled: Boolean) {
        prefs.edit().putBoolean("notification_quiet_hours_enabled", enabled).apply()
    }

    fun getQuietHoursStartHour(): Int {
        return prefs.getInt("notification_quiet_hours_start_hour", 23)
    }

    fun saveQuietHoursStartHour(hour: Int) {
        prefs.edit().putInt("notification_quiet_hours_start_hour", hour).apply()
    }

    fun getQuietHoursEndHour(): Int {
        return prefs.getInt("notification_quiet_hours_end_hour", 5)
    }

    fun saveQuietHoursEndHour(hour: Int) {
        prefs.edit().putInt("notification_quiet_hours_end_hour", hour).apply()
    }

    // Alarm Settings
    fun getAlarmSetting(prayerId: PrayerId): PrayerAlarmSetting {
        val prefix = "alarm_${prayerId.code}"
        val hour = prefs.getInt("${prefix}_hour", prayerId.defaultHour)
        val minute = prefs.getInt("${prefix}_minute", prayerId.defaultMinute)
        val isEnabled = prefs.getBoolean("${prefix}_enabled", prayerId != PrayerId.VEIL) // Veil disabled by default as it's monastic
        val sound = prefs.getBoolean("${prefix}_sound", true)
        val vibrate = prefs.getBoolean("${prefix}_vibrate", true)
        val soundId = prefs.getString("${prefix}_sound_id", "church_bells") ?: "church_bells"

        return PrayerAlarmSetting(
            prayerCode = prayerId.code,
            hour = hour,
            minute = minute,
            isEnabled = isEnabled,
            soundEnabled = sound,
            vibrateEnabled = vibrate,
            soundId = soundId
        )
    }

    fun saveAlarmSetting(setting: PrayerAlarmSetting) {
        val prefix = "alarm_${setting.prayerCode}"
        prefs.edit()
            .putInt("${prefix}_hour", setting.hour)
            .putInt("${prefix}_minute", setting.minute)
            .putBoolean("${prefix}_enabled", setting.isEnabled)
            .putBoolean("${prefix}_sound", setting.soundEnabled)
            .putBoolean("${prefix}_vibrate", setting.vibrateEnabled)
            .putString("${prefix}_sound_id", setting.soundId)
            .apply()

        CoroutineScope(Dispatchers.IO).launch {
            syncManager.syncSettings(getSavedLanguage().code, getAllAlarmSettings()) { _, _ -> }
        }
    }

    fun getAllAlarmSettings(): List<PrayerAlarmSetting> {
        return PrayerId.canonicalPrayers.map { getAlarmSetting(it) }
    }

    // Prayer Logs (Room + Cloud Sync)
    fun getAllLogs(): Flow<List<PrayerLogEntity>> = dao.getAllLogs()

    fun getLogsForDate(dateString: String): Flow<List<PrayerLogEntity>> = dao.getLogsForDate(dateString)

    fun getLogsForMonth(year: Int, month: Int): Flow<List<PrayerLogEntity>> = dao.getLogsForMonth(year, month)

    fun getLogsForYear(year: Int): Flow<List<PrayerLogEntity>> = dao.getLogsForYear(year)

    fun getTotalLogsCount(): Flow<Int> = dao.getTotalLogsCount()

    suspend fun isPrayerPrayedOnDate(prayerCode: String, dateString: String): Boolean {
        return dao.getCountForPrayerOnDate(prayerCode, dateString) > 0
    }

    suspend fun logPrayer(prayerId: PrayerId, timestamp: Long = System.currentTimeMillis()): Long {
        val cal = Calendar.getInstance().apply { timeInMillis = timestamp }
        val dateString = formatDate(cal.time)
        val log = PrayerLogEntity(
            prayerCode = prayerId.code,
            timestamp = timestamp,
            dateString = dateString,
            year = cal.get(Calendar.YEAR),
            month = cal.get(Calendar.MONTH) + 1,
            day = cal.get(Calendar.DAY_OF_MONTH),
            hour = cal.get(Calendar.HOUR_OF_DAY),
            minute = cal.get(Calendar.MINUTE)
        )
        val id = dao.insertLog(log)
        // Upload to Firestore for cross-device sync
        syncManager.uploadPrayerLog(log.copy(id = id))
        return id
    }

    suspend fun removePrayerOnDate(prayerCode: String, dateString: String) {
        dao.deletePrayerOnDate(prayerCode, dateString)
        syncManager.deletePrayerLog(prayerCode, dateString)
    }

    suspend fun deleteLogById(id: Long) {
        dao.deleteLogById(id)
    }

    suspend fun performFullSync() {
        val localLogs = dao.getAllLogsList()
        val localKeys = localLogs.map { "${it.prayerCode}_${it.dateString}" }.toSet()
        syncManager.performFullBidirectionalSync(localLogs) { remoteLogs ->
            val toInsert = remoteLogs.filter { "${it.prayerCode}_${it.dateString}" !in localKeys }
            if (toInsert.isNotEmpty()) {
                dao.insertLogs(toInsert)
            }
        }
        syncManager.syncSettings(getSavedLanguage().code, getAllAlarmSettings()) { remoteLangCode, remoteAlarms ->
            if (remoteLangCode.isNotBlank()) {
                prefs.edit().putString("app_language_code", remoteLangCode).apply()
            }
            for (alarm in remoteAlarms) {
                saveAlarmSetting(alarm)
            }
        }
    }

    fun startRealtimeSync(scope: CoroutineScope) {
        syncManager.startRealtimeListener { remoteLog ->
            scope.launch(Dispatchers.IO) {
                val exists = dao.getCountForPrayerOnDate(remoteLog.prayerCode, remoteLog.dateString) > 0
                if (!exists) {
                    dao.insertLog(remoteLog)
                }
            }
        }
    }

    // ==========================================
    // Room Database Caching: Prayer Texts
    // ==========================================

    /**
     * Reactive stream of cached prayer liturgical sections directly from Room Database.
     */
    fun getCachedPrayerSectionsFlow(prayerId: PrayerId, lang: AppLanguage): Flow<List<PrayerSectionItem>> {
        return prayerTextDao.getSectionsForPrayer(prayerId.code, lang.code).map { list ->
            list.map { it.toPrayerSectionItem() }
        }
    }

    /**
     * Gets prayer liturgical sections from Room database cache. If not cached yet,
     * reads from liturgical repository, stores into Room DB, and returns the list.
     */
    suspend fun getOrCachePrayerSections(prayerId: PrayerId, lang: AppLanguage): List<PrayerSectionItem> = withContext(Dispatchers.IO) {
        val cached = prayerTextDao.getSectionsForPrayerDirect(prayerId.code, lang.code)
        if (cached.isNotEmpty()) {
            return@withContext cached.map { it.toPrayerSectionItem() }
        }

        // Cache miss: Load authentic liturgical order and store in Room
        val sections = AgpeyaHourDetails.getFullPrayerSections(prayerId, lang)
        val entities = sections.mapIndexed { index, section ->
            CachedPrayerSectionEntity.fromPrayerSectionItem(
                item = section,
                prayerCode = prayerId.code,
                languageCode = lang.code,
                order = index
            )
        }
        prayerTextDao.insertSections(entities)
        return@withContext sections
    }

    /**
     * Pre-caches all canonical prayer hours across all active languages into Room DB
     * to guarantee complete offline functionality.
     */
    suspend fun preCacheAllPrayerTexts() = withContext(Dispatchers.IO) {
        val languages = AppLanguage.entries
        for (lang in languages) {
            for (prayer in PrayerId.canonicalPrayers) {
                val count = prayerTextDao.getSectionCount(prayer.code, lang.code)
                if (count == 0) {
                    val sections = AgpeyaHourDetails.getFullPrayerSections(prayer, lang)
                    val entities = sections.mapIndexed { index, section ->
                        CachedPrayerSectionEntity.fromPrayerSectionItem(
                            item = section,
                            prayerCode = prayer.code,
                            languageCode = lang.code,
                            order = index
                        )
                    }
                    prayerTextDao.insertSections(entities)
                }
            }
        }
    }

    fun getTotalCachedPrayerSectionsCount(): Flow<Int> = prayerTextDao.getTotalCachedSectionsCount()

    // ==========================================
    // Room Database Caching: Daily Meditations & Verses
    // ==========================================

    /**
     * Reactive flow of cached daily meditation for a specific day of the year from Room.
     */
    fun getDailyMeditationFlow(dayOfYear: Int): Flow<DailyVerseMeditation?> {
        return meditationDao.getMeditationForDay(dayOfYear).map { it?.toDailyVerseMeditation() }
    }

    /**
     * Gets all cached meditations stored in Room DB.
     */
    fun getAllCachedMeditationsFlow(): Flow<List<DailyVerseMeditation>> {
        return meditationDao.getAllMeditations().map { list -> list.map { it.toDailyVerseMeditation() } }
    }

    /**
     * Retrieves daily verse meditation for today, reading from Room cache or seeding it into Room.
     */
    suspend fun getDailyMeditation(calendar: Calendar = Calendar.getInstance()): DailyVerseMeditation = withContext(Dispatchers.IO) {
        val dayOfYear = calendar.get(Calendar.DAY_OF_YEAR)
        val cached = meditationDao.getMeditationForDayDirect(dayOfYear)
        if (cached != null) {
            return@withContext cached.toDailyVerseMeditation()
        }

        // Cache miss: Fetch canonical meditation and persist in Room DB
        val defaultMeditation = DailyScriptureProvider.getDailyVerseForCalendar(calendar)
        val entity = CachedMeditationEntity.fromDailyVerseMeditation(
            meditation = defaultMeditation,
            id = "day_${dayOfYear}_${defaultMeditation.associatedPrayer.code}",
            source = "CANONICAL"
        )
        meditationDao.insertMeditation(entity)
        return@withContext defaultMeditation
    }

    /**
     * Retrieves meditation mapped to a canonical prayer hour, reading from Room cache or seeding it into Room.
     */
    suspend fun getMeditationForPrayer(prayerId: PrayerId): DailyVerseMeditation = withContext(Dispatchers.IO) {
        val cached = meditationDao.getMeditationForPrayerDirect(prayerId.code)
        if (cached != null) {
            return@withContext cached.toDailyVerseMeditation()
        }

        val defaultMeditation = DailyScriptureProvider.getVerseForPrayer(prayerId)
        val entity = CachedMeditationEntity.fromDailyVerseMeditation(
            meditation = defaultMeditation,
            id = "prayer_${prayerId.code}",
            source = "CANONICAL"
        )
        meditationDao.insertMeditation(entity)
        return@withContext defaultMeditation
    }

    /**
     * Caches custom or previously fetched meditations into Room DB.
     */
    suspend fun cacheFetchedMeditation(meditation: DailyVerseMeditation, source: String = "PATRISTIC_ARCHIVE") = withContext(Dispatchers.IO) {
        val entity = CachedMeditationEntity.fromDailyVerseMeditation(
            meditation = meditation,
            id = "custom_${System.currentTimeMillis()}_${meditation.associatedPrayer.code}",
            isCustomOrFetched = true,
            source = source
        )
        meditationDao.insertMeditation(entity)
    }

    /**
     * Pre-caches default canonical meditations into Room DB.
     */
    suspend fun preCacheDefaultMeditations() = withContext(Dispatchers.IO) {
        val count = meditationDao.getTotalMeditationsCountDirect()
        if (count == 0) {
            val entities = mutableListOf<CachedMeditationEntity>()
            for (day in 1..7) {
                val cal = Calendar.getInstance().apply { set(Calendar.DAY_OF_YEAR, day) }
                val med = DailyScriptureProvider.getDailyVerseForCalendar(cal)
                entities.add(
                    CachedMeditationEntity.fromDailyVerseMeditation(
                        meditation = med,
                        id = "day_${day}_${med.associatedPrayer.code}",
                        source = "CANONICAL"
                    )
                )
            }
            meditationDao.insertMeditations(entities)
        }
    }

    /**
     * Pre-populates all offline caches (prayer texts and meditations) in Room DB.
     */
    suspend fun ensureAllOfflineDataCached() = withContext(Dispatchers.IO) {
        preCacheDefaultMeditations()
        preCacheAllPrayerTexts()
    }

    fun getTotalCachedMeditationsCount(): Flow<Int> = meditationDao.getTotalMeditationsCount()

    // --- Spiritual Journal & Confession Notes Room persistence ---
    fun getAllSpiritualNotesFlow(): Flow<List<com.example.data.model.SpiritualNoteEntity>> =
        spiritualNoteDao.getAllNotesFlow()

    fun getSpiritualNotesByCategoryFlow(category: String): Flow<List<com.example.data.model.SpiritualNoteEntity>> =
        spiritualNoteDao.getNotesByCategoryFlow(category)

    suspend fun insertSpiritualNote(note: com.example.data.model.SpiritualNoteEntity): Long = withContext(Dispatchers.IO) {
        spiritualNoteDao.insertNote(note)
    }

    suspend fun updateSpiritualNote(note: com.example.data.model.SpiritualNoteEntity) = withContext(Dispatchers.IO) {
        spiritualNoteDao.updateNote(note)
    }

    suspend fun deleteSpiritualNote(id: Long) = withContext(Dispatchers.IO) {
        spiritualNoteDao.deleteNoteById(id)
    }

    suspend fun clearConfessedSpiritualNotes() = withContext(Dispatchers.IO) {
        spiritualNoteDao.clearConfessedNotes()
    }

    companion object {
        fun formatDate(date: Date): String {
            val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.US)
            return sdf.format(date)
        }

        fun formatReadableDate(date: Date, locale: Locale): String {
            val sdf = SimpleDateFormat("EEEE, d MMMM yyyy", locale)
            return sdf.format(date)
        }

        fun getTodayString(): String {
            return formatDate(Date())
        }
    }
}
