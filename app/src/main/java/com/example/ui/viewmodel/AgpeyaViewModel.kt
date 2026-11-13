package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.alarm.AgpeyaAlarmScheduler
import com.example.data.db.AgpeyaDatabase
import com.example.data.model.HourlyPrayerCompletionEntity
import com.example.data.model.HourlyPrayerDailySummary
import com.example.data.model.HourlyPrayerWeeklyProgress
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
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.Date
import java.util.TimeZone

enum class ReportPeriod {
    DAILY, MONTHLY, QUARTERLY, SEMI_ANNUAL, YEARLY
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

    private val _selectedQuarter = MutableStateFlow(
        when (calendar.get(Calendar.MONTH) + 1) {
            in 1..3 -> 1
            in 4..6 -> 2
            in 7..9 -> 3
            else -> 4
        }
    )
    val selectedQuarter: StateFlow<Int> = _selectedQuarter.asStateFlow()

    private val _selectedHalfYear = MutableStateFlow(if (calendar.get(Calendar.MONTH) + 1 <= 6) 1 else 2)
    val selectedHalfYear: StateFlow<Int> = _selectedHalfYear.asStateFlow()

    private val _activePrayerForReading = MutableStateFlow<PrayerId?>(null)
    val activePrayerForReading: StateFlow<PrayerId?> = _activePrayerForReading.asStateFlow()

    val allLogs: StateFlow<List<PrayerLogEntity>>
    val totalCount: StateFlow<Int>

    // Arrow Prayers (الصلوات السهمية)
    val allArrowLogs: StateFlow<List<com.example.data.model.ArrowPrayerLogEntity>>
    val totalArrowCount: StateFlow<Int>
    private val _dailyArrowTarget = MutableStateFlow(33)
    val dailyArrowTarget: StateFlow<Int> = _dailyArrowTarget.asStateFlow()

    // Hourly Prayer Completion Progress Tracking (Room Database)
    val allHourlyCompletions: StateFlow<List<HourlyPrayerCompletionEntity>>
    val todayHourlyCompletions: StateFlow<List<HourlyPrayerCompletionEntity>>
    val weeklyHourlyProgress: StateFlow<HourlyPrayerWeeklyProgress>

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
    val userPhone: StateFlow<String?>
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

    // Church Name
    private val _churchName = MutableStateFlow<String?>(null)
    val churchName: StateFlow<String?> = _churchName.asStateFlow()

    // Service / Ministry Name (اسم الخدمة)
    private val _serviceName = MutableStateFlow<String?>(null)
    val serviceName: StateFlow<String?> = _serviceName.asStateFlow()

    private val _churchEmblem = MutableStateFlow("coptic_cross")
    val churchEmblem: StateFlow<String> = _churchEmblem.asStateFlow()

    // Spiritual Atmosphere & Background Themes
    private val _spiritualTheme = MutableStateFlow(com.example.data.model.SpiritualBackgroundTheme.CANDLE_SANCTUARY)
    val spiritualTheme: StateFlow<com.example.data.model.SpiritualBackgroundTheme> = _spiritualTheme.asStateFlow()

    private val _spiritualOpacity = MutableStateFlow(0.22f)
    val spiritualOpacity: StateFlow<Float> = _spiritualOpacity.asStateFlow()

    private val _isCandleGlowEnabled = MutableStateFlow(true)
    val isCandleGlowEnabled: StateFlow<Boolean> = _isCandleGlowEnabled.asStateFlow()

    // Theme Mode (Dark, Light, System)
    private val _themeMode = MutableStateFlow(com.example.ui.theme.AppThemeMode.SYSTEM)
    val themeMode: StateFlow<com.example.ui.theme.AppThemeMode> = _themeMode.asStateFlow()

    // Multilingual & Dual-Language State
    private val _isBilingualEnabled = MutableStateFlow(false)
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

    private val _timezoneSyncResult = MutableStateFlow<com.example.service.TimezoneSyncResult?>(null)
    val timezoneSyncResult: StateFlow<com.example.service.TimezoneSyncResult?> = _timezoneSyncResult.asStateFlow()

    private val _notificationLeadTimeMinutes = MutableStateFlow(0)
    val notificationLeadTimeMinutes: StateFlow<Int> = _notificationLeadTimeMinutes.asStateFlow()

    private val _isQuietHoursEnabled = MutableStateFlow(false)
    val isQuietHoursEnabled: StateFlow<Boolean> = _isQuietHoursEnabled.asStateFlow()

    private val _quietHoursStartHour = MutableStateFlow(23)
    val quietHoursStartHour: StateFlow<Int> = _quietHoursStartHour.asStateFlow()

    private val _quietHoursEndHour = MutableStateFlow(5)
    val quietHoursEndHour: StateFlow<Int> = _quietHoursEndHour.asStateFlow()

    private val _upcomingAlarm = MutableStateFlow<com.example.alarm.ScheduledPrayerAlarmInfo?>(null)
    val upcomingAlarm: StateFlow<com.example.alarm.ScheduledPrayerAlarmInfo?> = _upcomingAlarm.asStateFlow()

    private val _scheduledAlarmsList = MutableStateFlow<List<com.example.alarm.ScheduledPrayerAlarmInfo>>(emptyList())
    val scheduledAlarmsList: StateFlow<List<com.example.alarm.ScheduledPrayerAlarmInfo>> = _scheduledAlarmsList.asStateFlow()

    init {
        val db = AgpeyaDatabase.getDatabase(application)
        repository = AgpeyaRepository(db.prayerLogDao(), application)
        _currentLanguage.value = repository.getSavedLanguage()
        _isAuthCompleted.value = repository.isAuthOnboardingCompleted()
        _userFullName.value = repository.getUserFullName()
        _fontSizeMultiplier.value = repository.getFontSizeMultiplier()
        _dailyArrowTarget.value = repository.getDailyArrowPrayerTarget()
        _churchName.value = repository.getChurchName()
        _serviceName.value = repository.getServiceName()
        _churchEmblem.value = repository.getChurchEmblem()
        _spiritualTheme.value = repository.getSpiritualBackgroundTheme()
        _spiritualOpacity.value = repository.getSpiritualBackgroundOpacity()
        _isCandleGlowEnabled.value = repository.isCandleGlowEnabled()
        _themeMode.value = repository.getThemeMode()

        _isBilingualEnabled.value = repository.isBilingualEnabled()
        _secondaryLanguage.value = repository.getSecondaryLanguage()
        _isPhoneticGuideEnabled.value = repository.isPhoneticGuideEnabled()
        _isAutoTimezoneSyncEnabled.value = repository.isAutoTimezoneSyncEnabled()
        _selectedTimezoneId.value = repository.getSelectedTimezoneId()
        _notificationLeadTimeMinutes.value = repository.getNotificationLeadTimeMinutes()
        _isQuietHoursEnabled.value = repository.isQuietHoursEnabled()
        _quietHoursStartHour.value = repository.getQuietHoursStartHour()
        _quietHoursEndHour.value = repository.getQuietHoursEndHour()

        // Automatically detect timezone and recalibrate prayer alarms if needed
        val tzResult = com.example.service.AgpeyaTimezoneService.detectAndAdjustTimezone(
            context = application,
            isSystemBroadcast = false
        )
        _timezoneSyncResult.value = tzResult

        syncStatus = repository.syncManager.syncStatus
        syncKey = repository.syncManager.syncKey
        userEmail = repository.syncManager.userEmail
        userPhone = repository.syncManager.userPhone
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

        allArrowLogs = repository.getAllArrowLogs().stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptyList()
        )

        totalArrowCount = repository.getTotalArrowPrayersCount().stateIn(
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

        allHourlyCompletions = repository.getHourlyCompletionsForWeek(calendar.get(Calendar.YEAR), calendar.get(Calendar.WEEK_OF_YEAR)).stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptyList()
        )

        val todayStr = AgpeyaRepository.getTodayString()
        todayHourlyCompletions = repository.getHourlyCompletionsForDate(todayStr).stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptyList()
        )

        weeklyHourlyProgress = allHourlyCompletions.map { list ->
            computeWeeklyProgress(list)
        }.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            computeWeeklyProgress(emptyList())
        )

        // Pre-populate offline Room cache and load daily meditation
        viewModelScope.launch {
            repository.ensureAllOfflineDataCached()
            repository.syncPastLogsToHourlyCompletions()
            val meditation = repository.getDailyMeditation()
            _dailyVerse.value = meditation
        }

        // Initialize real-time sync listener & trigger background initial sync
        repository.startRealtimeSync(viewModelScope)
        triggerSync()
    }

    private fun computeWeeklyProgress(allCompletions: List<HourlyPrayerCompletionEntity>): HourlyPrayerWeeklyProgress {
        val cal = Calendar.getInstance()
        val currentYear = cal.get(Calendar.YEAR)
        val currentWeek = cal.get(Calendar.WEEK_OF_YEAR)
        val todayStr = AgpeyaRepository.getTodayString()

        // Calculate 7 days of the current week starting from Sunday to Saturday
        val weekCal = (cal.clone() as Calendar).apply {
            set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY)
        }

        val dayNamesAr = listOf("الأحد", "الاثنين", "الثلاثاء", "الأربعاء", "الخميس", "الجمعة", "السبت")
        val dayNamesEn = listOf("Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday")

        val dailySummaries = mutableListOf<HourlyPrayerDailySummary>()
        var totalWeekly = 0

        for (i in 0 until 7) {
            val dateStr = AgpeyaRepository.formatDate(weekCal.time)
            val dOfWeek = weekCal.get(Calendar.DAY_OF_WEEK)
            val dOfMonth = weekCal.get(Calendar.DAY_OF_MONTH)
            val dayIndex = (dOfWeek - 1).coerceIn(0, 6)

            val dayCompletions = allCompletions.filter { it.dateString == dateStr }
            val completedCodes = dayCompletions.map { it.prayerCode }.toSet()
            val completedCount = completedCodes.count { it != PrayerId.VEIL.code }
            totalWeekly += completedCount

            dailySummaries.add(
                HourlyPrayerDailySummary(
                    dateString = dateStr,
                    dayNameEn = dayNamesEn[dayIndex],
                    dayNameAr = dayNamesAr[dayIndex],
                    dayOfMonth = dOfMonth,
                    dayOfWeek = dOfWeek,
                    isToday = (dateStr == todayStr),
                    completedCount = completedCount,
                    totalPossibleCount = 7,
                    completedPrayerCodes = completedCodes
                )
            )
            weekCal.add(Calendar.DAY_OF_YEAR, 1)
        }

        val rate = (totalWeekly.toFloat() / 49f).coerceIn(0f, 1f)

        // Calculate streak of consecutive days with prayers up to today
        var streak = 0
        val streakCal = (cal.clone() as Calendar)
        for (d in 0 until 30) {
            val dStr = AgpeyaRepository.formatDate(streakCal.time)
            val count = allCompletions.count { it.dateString == dStr && it.prayerCode != PrayerId.VEIL.code }
            if (count > 0) {
                streak++
            } else if (d > 0) {
                break
            }
            streakCal.add(Calendar.DAY_OF_YEAR, -1)
        }

        val bestDay = dailySummaries.maxByOrNull { it.completedCount }

        return HourlyPrayerWeeklyProgress(
            year = currentYear,
            weekOfYear = currentWeek,
            days = dailySummaries,
            totalWeeklyCompleted = totalWeekly,
            totalWeeklyPossible = 49,
            weeklyCompletionRate = rate,
            currentStreakDays = streak,
            bestDayNameAr = bestDay?.dayNameAr,
            mostPrayedHourCode = allCompletions.groupBy { it.prayerCode }.maxByOrNull { it.value.size }?.key
        )
    }

    fun toggleHourlyPrayer(prayerId: PrayerId, dateString: String = AgpeyaRepository.getTodayString()) {
        viewModelScope.launch {
            repository.toggleHourlyPrayerCompletion(prayerId, dateString)
        }
    }

    fun markHourlyPrayerCompleted(prayerId: PrayerId, durationMinutes: Int = 15, notes: String? = null) {
        viewModelScope.launch {
            repository.recordHourlyPrayerCompletion(
                prayerId = prayerId,
                dateString = AgpeyaRepository.getTodayString(),
                durationMinutes = durationMinutes,
                notes = notes
            )
        }
    }

    fun saveChurchName(name: String?) {
        val trimmed = name?.trim()
        val cleanName = if (trimmed.isNullOrBlank()) null else trimmed
        _churchName.value = cleanName
        repository.saveChurchName(cleanName)
    }

    fun saveServiceName(name: String?) {
        val trimmed = name?.trim()
        val cleanName = if (trimmed.isNullOrBlank()) null else trimmed
        _serviceName.value = cleanName
        repository.saveServiceName(cleanName)
    }

    fun signInWithPhone(
        phone: String,
        fullName: String? = null,
        churchName: String? = null,
        serviceName: String? = null,
        onResult: (Boolean, String?) -> Unit
    ) {
        val clean = phone.trim()
        if (clean.isBlank()) {
            onResult(false, "رقم الهاتف مطلوب")
            return
        }
        if (!fullName.isNullOrBlank()) {
            saveUserFullName(fullName)
        }
        if (!churchName.isNullOrBlank()) {
            saveChurchName(churchName)
        }
        if (!serviceName.isNullOrBlank()) {
            saveServiceName(serviceName)
        }
        repository.syncManager.authenticateWithPhone(clean, fullName) { success, error ->
            if (success) {
                repository.setAuthOnboardingCompleted(true)
                _isAuthCompleted.value = true
                triggerSync()
                repository.startRealtimeSync(viewModelScope)
            }
            onResult(success, error)
        }
    }

    fun signInWithEmail(
        email: String,
        password: String? = null,
        fullName: String? = null,
        churchName: String? = null,
        serviceName: String? = null,
        onResult: (Boolean, String?) -> Unit
    ) {
        val clean = email.trim()
        if (clean.isBlank()) {
            onResult(false, "البريد الإلكتروني مطلوب")
            return
        }
        if (!fullName.isNullOrBlank()) {
            saveUserFullName(fullName)
        }
        if (!churchName.isNullOrBlank()) {
            saveChurchName(churchName)
        }
        if (!serviceName.isNullOrBlank()) {
            saveServiceName(serviceName)
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

    fun continueAsGuest(churchName: String? = null, serviceName: String? = null) {
        if (!churchName.isNullOrBlank()) {
            saveChurchName(churchName)
        }
        if (!serviceName.isNullOrBlank()) {
            saveServiceName(serviceName)
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
        refreshScheduledAlarms()
    }

    fun refreshScheduledAlarms() {
        val lang = _currentLanguage.value
        val all = AgpeyaAlarmScheduler.getAllScheduledAlarms(getApplication(), lang)
        _scheduledAlarmsList.value = all
        _upcomingAlarm.value = all.firstOrNull()
    }

    fun testPrayerAlarm(prayerId: PrayerId) {
        AgpeyaAlarmScheduler.testImmediateAlarmNotification(getApplication(), prayerId)
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

    fun setSelectedQuarter(quarter: Int) {
        _selectedQuarter.value = quarter.coerceIn(1, 4)
    }

    fun setSelectedHalfYear(halfYear: Int) {
        _selectedHalfYear.value = halfYear.coerceIn(1, 2)
    }

    fun setSelectedYear(year: Int) {
        _selectedYear.value = year
    }

    fun logArrowPrayer(count: Int, prayerText: String = "يارب يسوع المسيح، ابن الله، ارحمني أنا الخاطئ") {
        viewModelScope.launch {
            repository.logArrowPrayer(count, prayerText)
        }
    }

    fun setDailyArrowTarget(target: Int) {
        val clean = target.coerceIn(1, 10000)
        _dailyArrowTarget.value = clean
        repository.saveDailyArrowPrayerTarget(clean)
    }

    fun recordArrowPrayerTap(prayerText: String = "«يارب يسوع المسيح، ابن الله، ارحمني أنا الخاطئ» (صلاة يسوع)") {
        viewModelScope.launch {
            repository.recordArrowPrayerTap(prayerText, 1)
        }
    }

    fun deleteArrowLog(id: Long) {
        viewModelScope.launch {
            repository.deleteArrowLogById(id)
        }
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
        refreshScheduledAlarms()
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
            val result = com.example.service.AgpeyaTimezoneService.detectAndAdjustTimezone(
                context = getApplication(),
                isSystemBroadcast = false,
                forceReschedule = true
            )
            _selectedTimezoneId.value = result.currentTimezoneId
            _timezoneSyncResult.value = result
            repository.saveSelectedTimezoneId(result.currentTimezoneId)
        }
        refreshScheduledAlarms()
    }

    fun setSelectedTimezoneId(tzId: String) {
        _selectedTimezoneId.value = tzId
        repository.saveSelectedTimezoneId(tzId)
        AgpeyaAlarmScheduler.rescheduleAllActiveAlarms(getApplication())
        refreshScheduledAlarms()
    }

    fun setNotificationLeadTimeMinutes(minutes: Int) {
        _notificationLeadTimeMinutes.value = minutes
        repository.saveNotificationLeadTimeMinutes(minutes)
        AgpeyaAlarmScheduler.rescheduleAllActiveAlarms(getApplication())
        refreshScheduledAlarms()
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
        repository.setUserEmail(email) {
            triggerSync()
            repository.startRealtimeSync(viewModelScope)
            onComplete()
        }
    }

    fun setFontSizeMultiplier(multiplier: Float) {
        val clamped = multiplier.coerceIn(0.85f, 1.85f)
        _fontSizeMultiplier.value = clamped
        repository.saveFontSizeMultiplier(clamped)
    }

    fun syncAlarmsWithLocalTimezone(): com.example.service.TimezoneSyncResult {
        val result = com.example.service.AgpeyaTimezoneService.detectAndAdjustTimezone(
            context = getApplication(),
            isSystemBroadcast = false,
            forceReschedule = true
        )
        _timezoneSyncResult.value = result
        _selectedTimezoneId.value = result.currentTimezoneId
        return result
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
        val isDst = tz.inDaylightTime(Date())
        val displayName = tz.getDisplayName(isDst, TimeZone.LONG)
        val offsetHours = (tz.rawOffset + (if (isDst) tz.dstSavings else 0)) / (1000 * 60 * 60)
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

    fun setThemeMode(mode: com.example.ui.theme.AppThemeMode) {
        _themeMode.value = mode
        repository.saveThemeMode(mode)
    }

    fun toggleDarkLightMode() {
        val nextMode = when (_themeMode.value) {
            com.example.ui.theme.AppThemeMode.DARK -> com.example.ui.theme.AppThemeMode.LIGHT
            com.example.ui.theme.AppThemeMode.LIGHT -> com.example.ui.theme.AppThemeMode.DARK
            com.example.ui.theme.AppThemeMode.SYSTEM -> com.example.ui.theme.AppThemeMode.LIGHT
        }
        setThemeMode(nextMode)
    }
}
