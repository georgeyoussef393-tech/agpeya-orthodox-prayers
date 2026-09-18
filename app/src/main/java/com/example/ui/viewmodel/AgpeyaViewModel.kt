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

    // Cloud Sync States
    val syncStatus: StateFlow<SyncStatus>
    val syncKey: StateFlow<String>
    val userEmail: StateFlow<String?>
    val lastSyncTime: StateFlow<Long>
    val isRealtimeSyncActive: StateFlow<Boolean>

    // Auth / Onboarding
    private val _isAuthCompleted = MutableStateFlow(false)
    val isAuthCompleted: StateFlow<Boolean> = _isAuthCompleted.asStateFlow()

    init {
        val db = AgpeyaDatabase.getDatabase(application)
        repository = AgpeyaRepository(db.prayerLogDao(), application)
        _currentLanguage.value = repository.getSavedLanguage()
        _isAuthCompleted.value = repository.isAuthOnboardingCompleted()

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

        // Initialize real-time sync listener & trigger background initial sync
        repository.startRealtimeSync(viewModelScope)
        triggerSync()
    }

    fun signInWithEmail(email: String, password: String? = null, onResult: (Boolean, String?) -> Unit) {
        val clean = email.trim()
        if (clean.isBlank()) {
            onResult(false, "Email required")
            return
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

    fun continueAsGuest() {
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

    fun refreshDailyVerse() {
        _dailyVerse.value = com.example.data.model.DailyScriptureProvider.getDailyVerseForCalendar()
    }

    fun setVerseForPrayer(prayerId: PrayerId) {
        _dailyVerse.value = com.example.data.model.DailyScriptureProvider.getVerseForPrayer(prayerId)
    }

    override fun onCleared() {
        super.onCleared()
        com.example.audio.SpiritualAudioPlayer.stopPreview()
    }

    fun testNotificationNow() {
        AgpeyaNotificationHelper.showTestNotification(getApplication(), _currentLanguage.value)
    }

    fun getCurrentTimezoneInfo(): String {
        val tz = TimeZone.getDefault()
        val displayName = tz.getDisplayName(tz.inDaylightTime(Date()), TimeZone.LONG)
        val offsetHours = tz.rawOffset / (1000 * 60 * 60)
        val sign = if (offsetHours >= 0) "+$offsetHours" else "$offsetHours"
        return "$displayName (UTC$sign)"
    }
}
