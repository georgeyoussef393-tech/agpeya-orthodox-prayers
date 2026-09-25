package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.alarm.AgpeyaAlarmScheduler
import com.example.data.db.AgpeyaDatabase
import com.example.data.model.PrayerAlarmSetting
import com.example.data.model.PrayerId
import com.example.data.model.PrayerLogEntity
import com.example.data.repository.AgpeyaRepository
import com.example.data.sync.SyncStatus
import com.example.localization.AppLanguage
import com.example.service.AgpeyaNotificationHelper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.Date
import java.util.TimeZone

enum class ReportPeriod {
    DAILY, MONTHLY, YEARLY
}

class AgpeyaViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: AgpeyaRepository

    private val _currentLanguage = MutableStateFlow(AppLanguage.ARABIC)
    val currentLanguage: StateFlow<AppLanguage> = _currentLanguage.asStateFlow()

    private val _alarmSettings = MutableStateFlow<Map<String, PrayerAlarmSetting>>(emptyMap())
    val alarmSettings: StateFlow<Map<String, PrayerAlarmSetting>> = _alarmSettings.asStateFlow()

    private val _selectedReportPeriod = MutableStateFlow(ReportPeriod.DAILY)
    val selectedReportPeriod: StateFlow<ReportPeriod> = _selectedReportPeriod.asStateFlow()

    private val _selectedDateString = MutableStateFlow(AgpeyaRepository.getTodayString())
    val selectedDateString: StateFlow<String> = _selectedDateString.asStateFlow()

    private val calendar = Calendar.getInstance()
    private val _selectedYear = MutableStateFlow(calendar.get(Calendar.YEAR))
    val selectedYear: StateFlow<Int> = _selectedYear.asStateFlow()

    private val _selectedMonth = MutableStateFlow(calendar.get(Calendar.MONTH) + 1)
    val selectedMonth: StateFlow<Int> = _selectedMonth.asStateFlow()

    private val _activePrayerForReading = MutableStateFlow<PrayerId?>(null)
    val activePrayerForReading: StateFlow<PrayerId?> = _activePrayerForReading.asStateFlow()

    val allLogs: StateFlow<List<PrayerLogEntity>>
    val totalCount: StateFlow<Int>

    // Room Database Caching State
    val totalCachedPrayerSections: StateFlow<Int>
    val totalCachedMeditations: StateFlow<Int>
    val allSpiritualNotes: StateFlow<List<com.example.data.model.SpiritualNoteEntity>>
    private val _isPreCachingInProgress = MutableStateFlow(false)
    val isPreCachingInProgress: StateFlow<Boolean> = _isPreCachingInProgress.asStateFlow()

    // Cloud Sync States
    val syncStatus: StateFlow<SyncStatus>
    val syncKey: StateFlow<String>
    val userEmail: StateFlow<String?>
    val lastSyncTime: StateFlow<Long>
    val isRealtimeSyncActive: StateFlow<Boolean>

    // Auth / Onboarding
    private val _isAuthCompleted = MutableStateFlow(false)
    val isAuthCompleted: StateFlow<Boolean> = _isAuthCompleted.asStateFlow()

    // User Full Name (الاسم ثلاثي أو رباعي)
    private val _userFullName = MutableStateFlow<String?>(null)
    val userFullName: StateFlow<String?> = _userFullName.asStateFlow()

    // Global Font Size Scaling for Reading
    private val _fontSizeMultiplier = MutableStateFlow(1.0f)
    val fontSizeMultiplier: StateFlow<Float> = _fontSizeMultiplier.asStateFlow()

    // Optional Church / Ministry Name
    private val _churchName = MutableStateFlow<String?>(null)
    val churchName: StateFlow<String?> = _churchName.asStateFlow()

    private val _churchEmblem = MutableStateFlow("coptic_cross")
    val churchEmblem: StateFlow<String> = _churchEmblem.asStateFlow()

    // Spiritual Atmosphere & Background Themes
    private val _spiritualTheme = MutableStateFlow(com.example.data.model.SpiritualBackgroundTheme.CANDLE_SANCTUARY)
    val spiritualTheme: StateFlow<com.example.data.model.SpiritualBackgroundTheme> = _spiritualTheme.asStateFlow()

    private val _spiritualOpacity = MutableStateFlow(0.22f)
    val spiritualOpacity: StateFlow<Float> = _spiritualOpacity.asStateFlow()

    private val _isCandleGlowEnabled = MutableStateFlow(true)
    val isCandleGlowEnabled: StateFlow<Boolean> = _isCandleGlowEnabled.asStateFlow()

    // Multilingual & Dual-Language State
    private val _isBilingualEnabled = MutableStateFlow(true)
    val isBilingualEnabled: StateFlow<Boolean> = _isBilingualEnabled.asStateFlow()

    private val _secondaryLanguage = MutableStateFlow(AppLanguage.COPTIC)
    val secondaryLanguage: StateFlow<AppLanguage> = _secondaryLanguage.asStateFlow()

    private val _isPhoneticGuideEnabled = MutableStateFlow(true)
    val isPhoneticGuideEnabled: StateFlow<Boolean> = _isPhoneticGuideEnabled.asStateFlow()

    // Timezone & Advanced Notification State
    private val _isAutoTimezoneSyncEnabled = MutableStateFlow(true)
    val isAutoTimezoneSyncEnabled: StateFlow<Boolean> = _isAutoTimezoneSyncEnabled.asStateFlow()

    private val _selectedTimezoneId = MutableStateFlow(TimeZone.getDefault().id)
    val selectedTimezoneId: StateFlow<String> = _selectedTimezoneId.asStateFlow()

    private val _notificationLeadTimeMinutes = MutableStateFlow(0)
    val notificationLeadTimeMinutes: StateFlow<Int> = _notificationLeadTimeMinutes.asStateFlow()

    private val _isQuietHoursEnabled = MutableStateFlow(false)
    val isQuietHoursEnabled: StateFlow<Boolean> = _isQuietHoursEnabled.asStateFlow()

    private val _quietHoursStartHour = MutableStateFlow(23)
    val quietHoursStartHour: StateFlow<Int> = _quietHoursStartHour.asStateFlow()

    private val _quietHoursEndHour = MutableStateFlow(5)
    val quietHoursEndHour: StateFlow<Int> = _quietHoursEndHour.asStateFlow()

    init {
        val db = AgpeyaDatabase.getDatabase(application)
        repository = AgpeyaRepository(db.prayerLogDao(), application)
        _currentLanguage.value = repository.getSavedLanguage()
        _isAuthCompleted.value = repository.isAuthOnboardingCompleted()
        _userFullName.value = repository.getUserFullName()
        _fontSizeMultiplier.value = repository.getFontSizeMultiplier()
        _churchName.value = repository.getChurchName()
        _churchEmblem.value = repository.getChurchEmblem()
        _spiritualTheme.value = repository.getSpiritualBackgroundTheme()
        _spiritualOpacity.value = repository.getSpiritualBackgroundOpacity()
        _isCandleGlowEnabled.value = repository.isCandleGlowEnabled()

        _isBilingualEnabled.value = repository.isBilingualEnabled()
        _secondaryLanguage.value = repository.getSecondaryLanguage()
        _isPhoneticGuideEnabled.value = repository.isPhoneticGuideEnabled()
        _isAutoTimezoneSyncEnabled.value = repository.isAutoTimezoneSyncEnabled()
        _selectedTimezoneId.value = repository.getSelectedTimezoneId()
        _notificationLeadTimeMinutes.value = repository.getNotificationLeadTimeMinutes()
        _isQuietHoursEnabled.value = repository.isQuietHoursEnabled()
        _quietHoursStartHour.value = repository.getQuietHoursStartHour()
        _quietHoursEndHour.value = repository.getQuietHoursEndHour()

        syncStatus = repository.syncManager.syncStatus
        syncKey = repository.syncManager.syncKey
        userEmail = repository.syncManager.userEmail
        lastSyncTime = repository.syncManager.lastSyncTimestamp
        isRealtimeSyncActive = repository.syncManager.isRealtimeSyncActive

        loadAlarmSettings()

        allLogs = repository.getAllLogs().stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptyList()
        )

        totalCount = repository.getTotalLogsCount().stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            0
        )

        totalCachedPrayerSections = repository.getTotalCachedPrayerSectionsCount().stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            0
        )

        totalCachedMeditations = repository.getTotalCachedMeditationsCount().stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            0
        )

        allSpiritualNotes = repository.getAllSpiritualNotesFlow().stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptyList()
        )

        // Pre-populate offline Room cache and load daily meditation
        viewModelScope.launch {
            repository.ensureAllOfflineDataCached()
            val meditation = repository.getDailyMeditation()
            _dailyVerse.value = meditation
        }

        // Initialize real-time sync listener & trigger background initial sync
        repository.startRealtimeSync(viewModelScope)
        triggerSync()
    }

    fun saveChurchName(name: String?) {
        val trimmed = name?.trim()
        val cleanName = if (trimmed.isNullOrBlank()) null else trimmed
        _churchName.value = cleanName
        repository.saveChurchName(cleanName)
    }

    fun signInWithEmail(email: String, password: String? = null, churchName: String? = null, onResult: (Boolean, String?) -> Unit) {
        val clean = email.trim()
        if (clean.isBlank()) {
            onResult(false, "Email required")
            return
        }
        if (!churchName.isNullOrBlank()) {
            saveChurchName(churchName)
        }
        repository.syncManager.authenticateWithEmail(clean, password) { success, error ->
            if (success) {
                repository.setAuthOnboardingCompleted(true)
                _isAuthCompleted.value = true
                triggerSync()
                repository.startRealtimeSync(viewModelScope)
            }
            onResult(success, error)
        }
    }

    fun continueAsGuest(churchName: String? = null) {
        if (!churchName.isNullOrBlank()) {
            saveChurchName(churchName)
        }
        repository.setAuthOnboardingCompleted(true)
        _isAuthCompleted.value = true
    }

    fun signOut(clearLocalHistory: Boolean = false) {
        viewModelScope.launch {
            if (clearLocalHistory) {
                repository.clearAllLocalLogs()
            }
            repository.setUserEmail(null)
            repository.setAuthOnboardingCompleted(false)
            _isAuthCompleted.value = false
        }
    }

    fun openAuthOnboarding() {
        _isAuthCompleted.value = false
    }

    fun triggerSync() {
        viewModelScope.launch {
            repository.performFullSync()
        }
    }

    fun updateSyncKey(newKey: String) {
        repository.syncManager.updateSyncKey(newKey) {
            triggerSync()
            repository.startRealtimeSync(viewModelScope)
        }
    }

    fun setRealtimeSync(enabled: Boolean) {
        repository.syncManager.setRealtimeSyncEnabled(enabled)
        if (enabled) {
            repository.startRealtimeSync(viewModelScope)
        }
    }

    private fun loadAlarmSettings() {
        val map = mutableMapOf<String, PrayerAlarmSetting>()
        for (prayer in PrayerId.canonicalPrayers) {
            map[prayer.code] = repository.getAlarmSetting(prayer)
        }
        _alarmSettings.value = map
    }

    fun setLanguage(language: AppLanguage) {
        _currentLanguage.value = language
        repository.saveLanguage(language)
    }

    fun openPrayerReading(prayerId: PrayerId) {
        _activePrayerForReading.value = prayerId
    }

    fun closePrayerReading() {
        _activePrayerForReading.value = null
    }

    fun setReportPeriod(period: ReportPeriod) {
        _selectedReportPeriod.value = period
    }

    fun setSelectedDate(dateString: String) {
        _selectedDateString.value = dateString
    }

    fun setSelectedMonth(month: Int) {
        _selectedMonth.value = month
    }

    fun setSelectedYear(year: Int) {
        _selectedYear.value = year
    }

    fun togglePrayerToday(prayerId: PrayerId) {
        viewModelScope.launch {
            val today = AgpeyaRepository.getTodayString()
            val already = repository.isPrayerPrayedOnDate(prayerId.code, today)
            if (already) {
                repository.removePrayerOnDate(prayerId.code, today)
            } else {
                repository.logPrayer(prayerId)
            }
        }
    }

    fun markPrayerAsPrayedToday(prayerId: PrayerId) {
        viewModelScope.launch {
            val today = AgpeyaRepository.getTodayString()
            val already = repository.isPrayerPrayedOnDate(prayerId.code, today)
            if (!already) {
                repository.logPrayer(prayerId)
            }
        }
    }

    fun logPrayerForDate(prayerId: PrayerId, timestamp: Long) {
        viewModelScope.launch {
            repository.logPrayer(prayerId, timestamp)
        }
    }

    fun deleteLog(id: Long) {
        viewModelScope.launch {
            repository.deleteLogById(id)
        }
    }

    fun updateAlarmSetting(setting: PrayerAlarmSetting) {
        val updated = _alarmSettings.value.toMutableMap()
        updated[setting.prayerCode] = setting
        _alarmSettings.value = updated

        repository.saveAlarmSetting(setting)

        val prayerId = PrayerId.fromCode(setting.prayerCode)
        if (setting.isEnabled) {
            AgpeyaAlarmScheduler.scheduleAlarm(
                getApplication(),
                prayerId,
                setting.hour,
                setting.minute
            )
        } else {
            AgpeyaAlarmScheduler.cancelAlarm(getApplication(), prayerId)
        }
    }

    private val _playingSoundId = MutableStateFlow<String?>(null)
    val playingSoundId: StateFlow<String?> = _playingSoundId.asStateFlow()

    private val _dailyVerse = MutableStateFlow(com.example.data.model.DailyScriptureProvider.getDailyVerseForCalendar())
    val dailyVerse: StateFlow<com.example.data.model.DailyVerseMeditation> = _dailyVerse.asStateFlow()

    fun playSoundPreview(sound: com.example.audio.SpiritualSound) {
        if (_playingSoundId.value == sound.id) {
            stopSoundPreview()
            return
        }
        _playingSoundId.value = sound.id
        com.example.audio.SpiritualAudioPlayer.playPreview(getApplication(), sound) {
            _playingSoundId.value = null
        }
    }

    fun stopSoundPreview() {
        _playingSoundId.value = null
        com.example.audio.SpiritualAudioPlayer.stopPreview()
    }

    fun setPrayerSound(prayerId: PrayerId, soundId: String) {
        val current = _alarmSettings.value[prayerId.code] ?: repository.getAlarmSetting(prayerId)
        updateAlarmSetting(current.copy(soundId = soundId))
    }

    fun setSoundForAllPrayers(soundId: String) {
        PrayerId.canonicalPrayers.forEach { prayerId ->
            val current = _alarmSettings.value[prayerId.code] ?: repository.getAlarmSetting(prayerId)
            updateAlarmSetting(current.copy(soundId = soundId))
        }
    }

    fun setVibrateForAllPrayers(enabled: Boolean) {
        PrayerId.canonicalPrayers.forEach { prayerId ->
            val current = _alarmSettings.value[prayerId.code] ?: repository.getAlarmSetting(prayerId)
            updateAlarmSetting(current.copy(vibrateEnabled = enabled))
        }
    }

    fun setSoundForAllPrayersEnabled(enabled: Boolean) {
        PrayerId.canonicalPrayers.forEach { prayerId ->
            val current = _alarmSettings.value[prayerId.code] ?: repository.getAlarmSetting(prayerId)
            updateAlarmSetting(current.copy(soundEnabled = enabled))
        }
    }

    fun setMasterNotificationMode(soundEnabled: Boolean, vibrateEnabled: Boolean) {
        PrayerId.canonicalPrayers.forEach { prayerId ->
            val current = _alarmSettings.value[prayerId.code] ?: repository.getAlarmSetting(prayerId)
            updateAlarmSetting(current.copy(soundEnabled = soundEnabled, vibrateEnabled = vibrateEnabled))
        }
    }

    fun toggleAllAlarms(enabled: Boolean) {
        PrayerId.canonicalPrayers.forEach { prayerId ->
            val current = _alarmSettings.value[prayerId.code] ?: repository.getAlarmSetting(prayerId)
            updateAlarmSetting(current.copy(isEnabled = enabled))
        }
    }

    fun refreshDailyVerse() {
        viewModelScope.launch {
            val meditation = repository.getDailyMeditation()
            _dailyVerse.value = meditation
        }
    }

    fun setVerseForPrayer(prayerId: PrayerId) {
        viewModelScope.launch {
            val meditation = repository.getMeditationForPrayer(prayerId)
            _dailyVerse.value = meditation
        }
    }

    fun getCachedPrayerSectionsFlow(prayerId: PrayerId, lang: AppLanguage): Flow<List<com.example.data.model.PrayerSectionItem>> {
        return repository.getCachedPrayerSectionsFlow(prayerId, lang)
    }

    suspend fun getOrCachePrayerSections(prayerId: PrayerId, lang: AppLanguage): List<com.example.data.model.PrayerSectionItem> {
        return repository.getOrCachePrayerSections(prayerId, lang)
    }

    fun cacheFetchedMeditation(meditation: com.example.data.model.DailyVerseMeditation, source: String = "AI_INSIGHT") {
        viewModelScope.launch {
            repository.cacheFetchedMeditation(meditation, source)
        }
    }

    fun preCacheAllOfflineData(onComplete: () -> Unit = {}) {
        viewModelScope.launch {
            _isPreCachingInProgress.value = true
            repository.ensureAllOfflineDataCached()
            _isPreCachingInProgress.value = false
            onComplete()
        }
    }

    override fun onCleared() {
        super.onCleared()
        com.example.audio.SpiritualAudioPlayer.stopPreview()
    }

    fun testNotificationNow() {
        AgpeyaNotificationHelper.showTestNotification(getApplication(), _currentLanguage.value)
    }

    fun setSpiritualTheme(theme: com.example.data.model.SpiritualBackgroundTheme) {
        _spiritualTheme.value = theme
        repository.saveSpiritualBackgroundTheme(theme)
    }

    fun setSpiritualOpacity(opacity: Float) {
        val clamped = opacity.coerceIn(0.04f, 0.60f)
        _spiritualOpacity.value = clamped
        repository.saveSpiritualBackgroundOpacity(clamped)
    }

    fun setCandleGlow(enabled: Boolean) {
        _isCandleGlowEnabled.value = enabled
        repository.setCandleGlowEnabled(enabled)
    }

    fun setBilingualEnabled(enabled: Boolean) {
        _isBilingualEnabled.value = enabled
        repository.setBilingualEnabled(enabled)
    }

    fun setSecondaryLanguage(lang: AppLanguage) {
        _secondaryLanguage.value = lang
        repository.saveSecondaryLanguage(lang)
    }

    fun setPhoneticGuideEnabled(enabled: Boolean) {
        _isPhoneticGuideEnabled.value = enabled
        repository.setPhoneticGuideEnabled(enabled)
    }

    fun setAutoTimezoneSyncEnabled(enabled: Boolean) {
        _isAutoTimezoneSyncEnabled.value = enabled
        repository.setAutoTimezoneSyncEnabled(enabled)
        if (enabled) {
            val defaultTz = TimeZone.getDefault().id
            _selectedTimezoneId.value = defaultTz
            repository.saveSelectedTimezoneId(defaultTz)
        }
    }

    fun setSelectedTimezoneId(tzId: String) {
        _selectedTimezoneId.value = tzId
        repository.saveSelectedTimezoneId(tzId)
    }

    fun setNotificationLeadTimeMinutes(minutes: Int) {
        _notificationLeadTimeMinutes.value = minutes
        repository.saveNotificationLeadTimeMinutes(minutes)
    }

    fun setQuietHoursEnabled(enabled: Boolean) {
        _isQuietHoursEnabled.value = enabled
        repository.setQuietHoursEnabled(enabled)
    }

    fun setQuietHoursStartHour(hour: Int) {
        _quietHoursStartHour.value = hour
        repository.saveQuietHoursStartHour(hour)
    }

    fun setQuietHoursEndHour(hour: Int) {
        _quietHoursEndHour.value = hour
        repository.saveQuietHoursEndHour(hour)
    }

    fun saveUserFullName(fullName: String?) {
        _userFullName.value = fullName
        repository.saveUserFullName(fullName)
    }

    fun saveUserEmail(email: String?, onComplete: () -> Unit = {}) {
        repository.setUserEmail(email, onComplete)
    }

    fun setFontSizeMultiplier(multiplier: Float) {
        val clamped = multiplier.coerceIn(0.85f, 1.85f)
        _fontSizeMultiplier.value = clamped
        repository.saveFontSizeMultiplier(clamped)
    }

    fun syncAlarmsWithLocalTimezone() {
        AgpeyaAlarmScheduler.rescheduleAllActiveAlarms(getApplication())
    }

    fun saveChurchEmblem(emblemCode: String) {
        _churchEmblem.value = emblemCode
        repository.saveChurchEmblem(emblemCode)
    }

    fun exportBackupJson(context: android.content.Context, onComplete: (java.io.File) -> Unit) {
        viewModelScope.launch {
            val file = com.example.data.backup.BackupManager.createBackupJsonFile(context, repository)
            onComplete(file)
        }
    }

    fun restoreBackupFromUri(
        context: android.content.Context,
        uri: android.net.Uri,
        onResult: (Result<Int>) -> Unit
    ) {
        viewModelScope.launch {
            val result = com.example.data.backup.BackupManager.restoreBackupFromUri(context, repository, uri)
            if (result.isSuccess) {
                // Refresh ViewModel State
                _churchName.value = repository.getChurchName()
                _churchEmblem.value = repository.getChurchEmblem()
                _currentLanguage.value = repository.getSavedLanguage()
                _notificationLeadTimeMinutes.value = repository.getNotificationLeadTimeMinutes()
                loadAlarmSettings()
                AgpeyaAlarmScheduler.rescheduleAllActiveAlarms(getApplication())
            }
            onResult(result)
        }
    }

    fun getCurrentTimezoneInfo(): String {
        val tz = TimeZone.getDefault()
        val displayName = tz.getDisplayName(tz.inDaylightTime(Date()), TimeZone.LONG)
        val offsetHours = tz.rawOffset / (1000 * 60 * 60)
        val sign = if (offsetHours >= 0) "+$offsetHours" else "$offsetHours"
        return "$displayName (UTC$sign)"
    }

    // Spiritual Journal & Confession Notes
    fun addSpiritualNote(
        title: String,
        content: String,
        category: com.example.data.model.NoteCategory,
        dateString: String = AgpeyaRepository.getTodayString(),
        isPinned: Boolean = false
    ) {
        viewModelScope.launch {
            val note = com.example.data.model.SpiritualNoteEntity(
                title = title.trim(),
                content = content.trim(),
                categoryCode = category.code,
                dateString = dateString,
                isPinned = isPinned
            )
            repository.insertSpiritualNote(note)
        }
    }

    fun toggleNoteCompletion(note: com.example.data.model.SpiritualNoteEntity) {
        viewModelScope.launch {
            repository.updateSpiritualNote(
                note.copy(isCompletedOrConfessed = !note.isCompletedOrConfessed)
            )
        }
    }

    fun toggleNotePin(note: com.example.data.model.SpiritualNoteEntity) {
        viewModelScope.launch {
            repository.updateSpiritualNote(
                note.copy(isPinned = !note.isPinned)
            )
        }
    }

    fun updateSpiritualNote(note: com.example.data.model.SpiritualNoteEntity) {
        viewModelScope.launch {
            repository.updateSpiritualNote(note)
        }
    }

    fun deleteSpiritualNote(id: Long) {
        viewModelScope.launch {
            repository.deleteSpiritualNote(id)
        }
    }

    fun clearConfessedNotes() {
        viewModelScope.launch {
            repository.clearConfessedSpiritualNotes()
        }
    }
}
