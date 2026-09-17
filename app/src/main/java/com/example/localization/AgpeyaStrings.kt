package com.example.localization

object AgpeyaStrings {
    fun appTitle(lang: AppLanguage): String = when (lang) {
        AppLanguage.ARABIC -> "صلوات الأجبية"
        AppLanguage.COPTIC -> "Ϯⲁⲅⲡⲓⲁ ⲉⲑⲟⲩⲁⲃ"
        AppLanguage.ENGLISH -> "Agpeya Prayers"
        AppLanguage.FRENCH -> "Prières de l'Agpeya"
        AppLanguage.SPANISH -> "Oraciones del Agpeya"
        AppLanguage.GERMAN -> "Agpeya Gebete"
        AppLanguage.ITALIAN -> "Preghiere dell'Agpeya"
    }

    fun appSubtitle(lang: AppLanguage): String = when (lang) {
        AppLanguage.ARABIC -> "كتاب صلوات السواعي الأرثوذكسي"
        AppLanguage.COPTIC -> "Ⲡⲓϫⲱⲙ ⲛ̀ⲧⲉ ⲛⲓⲁϫⲡ ⲉⲑⲟⲩⲁⲃ"
        AppLanguage.ENGLISH -> "The Coptic Book of Canonical Hours"
        AppLanguage.FRENCH -> "Livre des Heures Canoniques Coptes"
        AppLanguage.SPANISH -> "Libro de las Horas Canónicas Copto"
        AppLanguage.GERMAN -> "Koptisches Stundengebetbuch"
        AppLanguage.ITALIAN -> "Libro delle Ore Canoniche Copte"
    }

    // Navigation Tabs
    fun tabPrayers(lang: AppLanguage): String = when (lang) {
        AppLanguage.ARABIC -> "الصلوات"
        AppLanguage.COPTIC -> "Ⲛⲓⲉⲩⲭⲏ"
        AppLanguage.ENGLISH -> "Prayers"
        AppLanguage.FRENCH -> "Prières"
        AppLanguage.SPANISH -> "Oraciones"
        AppLanguage.GERMAN -> "Gebete"
        AppLanguage.ITALIAN -> "Preghiere"
    }

    fun tabReports(lang: AppLanguage): String = when (lang) {
        AppLanguage.ARABIC -> "التقارير"
        AppLanguage.COPTIC -> "Ⲡⲓⲧⲱⲡ"
        AppLanguage.ENGLISH -> "Reports"
        AppLanguage.FRENCH -> "Rapports"
        AppLanguage.SPANISH -> "Informes"
        AppLanguage.GERMAN -> "Berichte"
        AppLanguage.ITALIAN -> "Rapporti"
    }

    fun tabSettings(lang: AppLanguage): String = when (lang) {
        AppLanguage.ARABIC -> "التنبيهات واللغة"
        AppLanguage.COPTIC -> "Ⲛⲓⲥⲩⲛⲑⲏⲕⲏ"
        AppLanguage.ENGLISH -> "Alarms & Settings"
        AppLanguage.FRENCH -> "Alarmes & Paramètres"
        AppLanguage.SPANISH -> "Alarmas y Ajustes"
        AppLanguage.GERMAN -> "Alarme & Einstellungen"
        AppLanguage.ITALIAN -> "Allarmi e Impostazioni"
    }

    // Daily / Monthly / Yearly
    fun reportDaily(lang: AppLanguage): String = when (lang) {
        AppLanguage.ARABIC -> "يومي"
        AppLanguage.COPTIC -> "Ⲙⲏⲛⲓ"
        AppLanguage.ENGLISH -> "Daily"
        AppLanguage.FRENCH -> "Quotidien"
        AppLanguage.SPANISH -> "Diario"
        AppLanguage.GERMAN -> "Täglich"
        AppLanguage.ITALIAN -> "Giornaliero"
    }

    fun reportMonthly(lang: AppLanguage): String = when (lang) {
        AppLanguage.ARABIC -> "شهري"
        AppLanguage.COPTIC -> "Ⲫⲏⲧ"
        AppLanguage.ENGLISH -> "Monthly"
        AppLanguage.FRENCH -> "Mensuel"
        AppLanguage.SPANISH -> "Mensual"
        AppLanguage.GERMAN -> "Monatlich"
        AppLanguage.ITALIAN -> "Mensile"
    }

    fun reportYearly(lang: AppLanguage): String = when (lang) {
        AppLanguage.ARABIC -> "سنوي"
        AppLanguage.COPTIC -> "Ⲣⲟⲙⲡⲓ"
        AppLanguage.ENGLISH -> "Yearly"
        AppLanguage.FRENCH -> "Annuel"
        AppLanguage.SPANISH -> "Anual"
        AppLanguage.GERMAN -> "Jährlich"
        AppLanguage.ITALIAN -> "Annuale"
    }

    // Action strings
    fun markPrayed(lang: AppLanguage): String = when (lang) {
        AppLanguage.ARABIC -> "صليت هذه الساعة"
        AppLanguage.COPTIC -> "Ⲁⲓⲉⲣⲡⲣⲟⲥⲉⲩⲭⲉⲥⲑⲉ"
        AppLanguage.ENGLISH -> "Mark as Prayed"
        AppLanguage.FRENCH -> "Marquer comme prié"
        AppLanguage.SPANISH -> "Marcar como orado"
        AppLanguage.GERMAN -> "Als gebetet markieren"
        AppLanguage.ITALIAN -> "Segna come pregato"
    }

    fun alreadyPrayed(lang: AppLanguage): String = when (lang) {
        AppLanguage.ARABIC -> "تمت الصلاة اليوم"
        AppLanguage.COPTIC -> "Ⲁⲓⲧⲱⲃϩ"
        AppLanguage.ENGLISH -> "Prayed Today"
        AppLanguage.FRENCH -> "Prié aujourd'hui"
        AppLanguage.SPANISH -> "Orado hoy"
        AppLanguage.GERMAN -> "Heute gebetet"
        AppLanguage.ITALIAN -> "Pregato oggi"
    }

    fun readPrayer(lang: AppLanguage): String = when (lang) {
        AppLanguage.ARABIC -> "قراءة الصلاة والتأمل"
        AppLanguage.COPTIC -> "Ⲱϣ ⲛ̀ϯⲉⲩⲭⲏ"
        AppLanguage.ENGLISH -> "Read Prayer & Meditation"
        AppLanguage.FRENCH -> "Lire la prière & méditation"
        AppLanguage.SPANISH -> "Leer oración y meditación"
        AppLanguage.GERMAN -> "Gebet lesen & Meditation"
        AppLanguage.ITALIAN -> "Leggi la preghiera e meditazione"
    }

    fun testAlert(lang: AppLanguage): String = when (lang) {
        AppLanguage.ARABIC -> "تجربة تنبيه الآن"
        AppLanguage.COPTIC -> "Ⲡⲓⲧⲱⲛ ⲧⲏⲣϥ"
        AppLanguage.ENGLISH -> "Test Notification Now"
        AppLanguage.FRENCH -> "Tester l'alerte"
        AppLanguage.SPANISH -> "Probar alerta ahora"
        AppLanguage.GERMAN -> "Jetzt Benachrichtigung testen"
        AppLanguage.ITALIAN -> "Prova notifica ora"
    }

    fun timezoneWorldwide(lang: AppLanguage): String = when (lang) {
        AppLanguage.ARABIC -> "توقيتك المحلي الحالي"
        AppLanguage.COPTIC -> "Ⲡⲓⲥⲏⲟⲩ ⲛ̀ⲧⲁⲕ"
        AppLanguage.ENGLISH -> "Current Local Timezone"
        AppLanguage.FRENCH -> "Fuseau horaire local"
        AppLanguage.SPANISH -> "Zona horaria local"
        AppLanguage.GERMAN -> "Aktuelle lokale Zeitzone"
        AppLanguage.ITALIAN -> "Fuso orario locale"
    }

    fun alarmNotificationTitle(prayerName: String, lang: AppLanguage): String = when (lang) {
        AppLanguage.ARABIC -> "حان موعد صلاة $prayerName"
        AppLanguage.COPTIC -> "Ⲁϥⲓ̀ ⲉ̀ⲡⲓⲥⲏⲟⲩ ⲛ̀$prayerName"
        AppLanguage.ENGLISH -> "Time for $prayerName Prayer"
        AppLanguage.FRENCH -> "L'heure de la prière de $prayerName"
        AppLanguage.SPANISH -> "Hora de la oración de $prayerName"
        AppLanguage.GERMAN -> "Zeit für das $prayerName Gebet"
        AppLanguage.ITALIAN -> "È l'ora della preghiera di $prayerName"
    }

    fun alarmNotificationBody(lang: AppLanguage): String = when (lang) {
        AppLanguage.ARABIC -> "«سبع مرات في النهار سبحتك على أحكام عدلك» - هلُم نسجد ونركع للمسيح ملكنا وإلهنا"
        AppLanguage.COPTIC -> "«Ϣⲁϣϥ ⲛ̀ⲥⲟⲡ ϧⲉⲛ ⲡⲓⲉϩⲟⲟⲩ ⲁⲓⲥⲙⲟⲩ ⲉ̀ⲣⲟⲕ» - Ⲁⲙⲱⲓⲛⲓ ⲙⲁⲣⲉⲛⲟⲩⲱϣⲧ ⲙ̀Ⲡⲭ̅ⲥ̅"
        AppLanguage.ENGLISH -> "\"Seven times a day I praise You, because of Your righteous judgments.\" - Come let us worship Christ our King."
        AppLanguage.FRENCH -> "« Sept fois par jour je te célèbre, à cause des lois de ta justice. » Venez, adorons le Christ notre Roi."
        AppLanguage.SPANISH -> "«Siete veces al día te alabo a causa de tus justos juicios.» - Venid, adoremos a Cristo nuestro Rey."
        AppLanguage.GERMAN -> "„Siebenmal am Tag lobe ich dich wegen deiner gerechten Urteile.“ - Kommt, lasst uns Christus, unseren König, anbeten."
        AppLanguage.ITALIAN -> "«Sette volte al giorno ti ho lodato per i tuoi giusti giudizi.» - Venite, adoriamo Cristo nostro Re."
    }

    // Reports Headers & Stats
    fun totalPrayersCount(lang: AppLanguage): String = when (lang) {
        AppLanguage.ARABIC -> "إجمالي الصلوات المكتملة"
        AppLanguage.COPTIC -> "Ⲡⲓⲧⲏⲣϥ ⲛ̀ⲛⲓⲉⲩⲭⲏ"
        AppLanguage.ENGLISH -> "Total Completed Prayers"
        AppLanguage.FRENCH -> "Total des prières accomplies"
        AppLanguage.SPANISH -> "Total de oraciones rezadas"
        AppLanguage.GERMAN -> "Gesamtzahl gebeteter Gebete"
        AppLanguage.ITALIAN -> "Totale preghiere completate"
    }

    fun prayersTodayLabel(lang: AppLanguage): String = when (lang) {
        AppLanguage.ARABIC -> "صلوات اليوم"
        AppLanguage.COPTIC -> "Ⲛⲓⲉⲩⲭⲏ ⲙ̀ⲫⲟⲟⲩ"
        AppLanguage.ENGLISH -> "Today's Prayers"
        AppLanguage.FRENCH -> "Prières d'aujourd'hui"
        AppLanguage.SPANISH -> "Oraciones de hoy"
        AppLanguage.GERMAN -> "Heutige Gebete"
        AppLanguage.ITALIAN -> "Preghiere di oggi"
    }

    fun monthlyBreakdown(lang: AppLanguage): String = when (lang) {
        AppLanguage.ARABIC -> "توزيع الصلوات خلال الشهر"
        AppLanguage.COPTIC -> "Ⲡⲓⲫⲱϣ ⲛ̀ⲧⲉ ⲡⲓⲁⲃⲟⲧ"
        AppLanguage.ENGLISH -> "Monthly Prayer Breakdown"
        AppLanguage.FRENCH -> "Répartition mensuelle des prières"
        AppLanguage.SPANISH -> "Desglose mensual de oraciones"
        AppLanguage.GERMAN -> "Monatliche Gebetsaufteilung"
        AppLanguage.ITALIAN -> "Dettaglio mensile delle preghiere"
    }

    fun yearlySummary(lang: AppLanguage): String = when (lang) {
        AppLanguage.ARABIC -> "التقرير السنوي والمعدل الشهري"
        AppLanguage.COPTIC -> "Ⲡⲓϫⲱⲕ ⲛ̀ⲧⲉ ϯⲣⲟⲙⲡⲓ"
        AppLanguage.ENGLISH -> "Yearly Summary & Monthly Trend"
        AppLanguage.FRENCH -> "Bilan annuel et évolution mensuelle"
        AppLanguage.SPANISH -> "Resumen anual y tendencia mensual"
        AppLanguage.GERMAN -> "Jahresübersicht und Monatstrends"
        AppLanguage.ITALIAN -> "Riepilogo annuale e andamento mensile"
    }

    fun prayersLogHistory(lang: AppLanguage): String = when (lang) {
        AppLanguage.ARABIC -> "سجل الصلوات بالتواريخ والأوقات"
        AppLanguage.COPTIC -> "Ⲡⲓⲥϧⲁⲓ ⲛ̀ⲧⲉ ⲛⲓⲥⲏⲟⲩ"
        AppLanguage.ENGLISH -> "Prayer Log History with Dates & Times"
        AppLanguage.FRENCH -> "Historique des prières avec dates et heures"
        AppLanguage.SPANISH -> "Historial de oraciones con fechas y horas"
        AppLanguage.GERMAN -> "Gebetsprotokoll mit Datum und Uhrzeit"
        AppLanguage.ITALIAN -> "Cronologia preghiere con date e orari"
    }

    fun noLogsYet(lang: AppLanguage): String = when (lang) {
        AppLanguage.ARABIC -> "لا توجد صلوات مسجلة في هذه الفترة حتى الآن. اضغط على أيقونة الصلاة لتسجيلها."
        AppLanguage.COPTIC -> "Ⲙⲙⲟⲛ ⲉⲩⲭⲏ ⲉⲥⲥϧⲏⲟⲩⲧ ⲙ̀ⲡⲁⲓⲥⲏⲟⲩ."
        AppLanguage.ENGLISH -> "No prayers recorded in this period yet. Tap any prayer card to log it."
        AppLanguage.FRENCH -> "Aucune prière enregistrée pour cette période. Appuyez sur une prière pour l'enregistrer."
        AppLanguage.SPANISH -> "No hay oraciones registradas en este período. Toca una oración para registrarla."
        AppLanguage.GERMAN -> "Noch keine Gebete in diesem Zeitraum erfasst. Tippen Sie auf ein Gebet, um es zu protokollieren."
        AppLanguage.ITALIAN -> "Nessuna preghiera registrata in questo periodo. Tocca una preghiera per registrarla."
    }

    fun verseSevenTimes(lang: AppLanguage): String = when (lang) {
        AppLanguage.ARABIC -> "«سَبْعَ مَرَّاتٍ فِي النَّهَارِ سَبَّحْتُكَ عَلَى أَحْكَامِ عَدْلِكَ» (مزمور 119: 164)"
        AppLanguage.COPTIC -> "«Ϣⲁϣϥ ⲛ̀ⲥⲟⲡ ϧⲉⲛ ⲡⲓⲉϩⲟⲟⲩ ⲁⲓⲥⲙⲟⲩ ⲉ̀ⲣⲟⲕ ⲉ̀ϫⲉⲛ ⲛⲓϩⲁⲡ ⲛ̀ⲧⲉ ⲧⲉⲕⲙⲉⲑⲙⲏⲓ»"
        AppLanguage.ENGLISH -> "\"Seven times a day I praise You, because of Your righteous judgments.\" (Psalm 119:164)"
        AppLanguage.FRENCH -> "« Sept fois par jour je te célèbre, à cause des lois de ta justice. » (Psaume 119:164)"
        AppLanguage.SPANISH -> "«Siete veces al día te alabo a causa de tus justos juicios.» (Salmo 119:164)"
        AppLanguage.GERMAN -> "„Siebenmal am Tag lobe ich dich wegen deiner gerechten Urteile.“ (Psalm 119:164)"
        AppLanguage.ITALIAN -> "«Sette volte al giorno ti ho lodato per i tuoi giusti giudizi.» (Salmo 119:164)"
    }

    fun languageSelection(lang: AppLanguage): String = when (lang) {
        AppLanguage.ARABIC -> "لغة التطبيق"
        AppLanguage.COPTIC -> "Ϯⲁⲥⲡⲓ ⲛ̀ⲧⲉ ⲡⲓⲉⲫⲁⲣⲙⲟⲅⲏ"
        AppLanguage.ENGLISH -> "App Language"
        AppLanguage.FRENCH -> "Langue de l'application"
        AppLanguage.SPANISH -> "Idioma de la aplicación"
        AppLanguage.GERMAN -> "App-Sprache"
        AppLanguage.ITALIAN -> "Lingua dell'applicazione"
    }

    fun alarmTime(lang: AppLanguage): String = when (lang) {
        AppLanguage.ARABIC -> "توقيت التنبيه"
        AppLanguage.COPTIC -> "Ⲡⲓⲥⲏⲟⲩ ⲛ̀ⲧⲉ ⲡⲓⲥⲟϩⲓ"
        AppLanguage.ENGLISH -> "Alarm Time"
        AppLanguage.FRENCH -> "Heure de l'alarme"
        AppLanguage.SPANISH -> "Hora de la alarma"
        AppLanguage.GERMAN -> "Alarmzeit"
        AppLanguage.ITALIAN -> "Orario sveglia"
    }

    fun soundAlert(lang: AppLanguage): String = when (lang) {
        AppLanguage.ARABIC -> "الصوت والاهتزاز"
        AppLanguage.COPTIC -> "Ⲡⲓϧⲣⲱⲟⲩ"
        AppLanguage.ENGLISH -> "Sound & Vibration"
        AppLanguage.FRENCH -> "Son et vibration"
        AppLanguage.SPANISH -> "Sonido y vibración"
        AppLanguage.GERMAN -> "Ton & Vibration"
        AppLanguage.ITALIAN -> "Suono e vibrazione"
    }

    fun worldwideNotice(lang: AppLanguage): String = when (lang) {
        AppLanguage.ARABIC -> "يعمل التطبيق في جميع دول العالم بحسب التوقيت المحلي لهاتفك، ويمكنك تعديل موعد كل صلاة بدقة بحسب رغبتك."
        AppLanguage.COPTIC -> "Ⲫⲁⲓ ⲉⲣϩⲱⲃ ϧⲉⲛ ⲡⲓⲕⲟⲥⲙⲟⲥ ⲧⲏⲣϥ ⲕⲁⲧⲁ ⲡⲉⲕⲥⲏⲟⲩ."
        AppLanguage.ENGLISH -> "Works in all countries worldwide based on your device's local timezone. You can adjust the alarm time for each prayer as you wish."
        AppLanguage.FRENCH -> "Fonctionne dans tous les pays selon votre fuseau horaire local. Vous pouvez ajuster l'heure de chaque prière."
        AppLanguage.SPANISH -> "Funciona en todos los países según la zona horaria de su dispositivo. Puede ajustar la hora de cada oración a su gusto."
        AppLanguage.GERMAN -> "Funktioniert weltweit in allen Ländern basierend auf der lokalen Zeitzone Ihres Geräts. Die Alarmzeiten können frei angepasst werden."
        AppLanguage.ITALIAN -> "Funziona in tutto il mondo in base al fuso orario locale. Puoi regolare l'orario di ogni preghiera liberamente."
    }

    fun close(lang: AppLanguage): String = when (lang) {
        AppLanguage.ARABIC -> "إغلاق"
        AppLanguage.COPTIC -> "Ⲙⲁϣⲑⲁⲙ"
        AppLanguage.ENGLISH -> "Close"
        AppLanguage.FRENCH -> "Fermer"
        AppLanguage.SPANISH -> "Cerrar"
        AppLanguage.GERMAN -> "Schließen"
        AppLanguage.ITALIAN -> "Chiudi"
    }

    fun save(lang: AppLanguage): String = when (lang) {
        AppLanguage.ARABIC -> "حفظ"
        AppLanguage.COPTIC -> "Ⲁⲣⲉϩ"
        AppLanguage.ENGLISH -> "Save"
        AppLanguage.FRENCH -> "Enregistrer"
        AppLanguage.SPANISH -> "Guardar"
        AppLanguage.GERMAN -> "Speichern"
        AppLanguage.ITALIAN -> "Salva"
    }

    fun deleteConfirm(lang: AppLanguage): String = when (lang) {
        AppLanguage.ARABIC -> "حذف هذا السجل؟"
        AppLanguage.COPTIC -> "Ⲃⲱⲗ ⲉⲃⲟⲗ"
        AppLanguage.ENGLISH -> "Delete this log?"
        AppLanguage.FRENCH -> "Supprimer cet enregistrement ?"
        AppLanguage.SPANISH -> "¿Eliminar este registro?"
        AppLanguage.GERMAN -> "Diesen Eintrag löschen?"
        AppLanguage.ITALIAN -> "Eliminare questa voce?"
    }

    fun notificationPermissionRequired(lang: AppLanguage): String = when (lang) {
        AppLanguage.ARABIC -> "يرجى السماح بالتنبيهات لكي يتمكن التطبيق من تذكيرك بمواعيد صلوات الأجبية."
        AppLanguage.COPTIC -> "Ⲙⲁ ϯⲉⲝⲟⲩⲥⲓⲁ ⲉ̀ⲡⲓⲥⲟϩⲓ"
        AppLanguage.ENGLISH -> "Please enable notification permissions so the app can alert you at prayer times."
        AppLanguage.FRENCH -> "Veuillez autoriser les notifications pour recevoir les alertes des heures de prière."
        AppLanguage.SPANISH -> "Por favor, active los permisos de notificación para recibir las alertas de oración."
        AppLanguage.GERMAN -> "Bitte erlauben Sie Benachrichtigungen, damit die App Sie an die Gebetszeiten erinnern kann."
        AppLanguage.ITALIAN -> "Abilita i permessi di notifica per ricevere gli avvisi negli orari di preghiera."
    }

    fun allowPermission(lang: AppLanguage): String = when (lang) {
        AppLanguage.ARABIC -> "تفعيل التنبيهات"
        AppLanguage.COPTIC -> "Ⲧⲱⲟⲩⲛⲟⲩ"
        AppLanguage.ENGLISH -> "Enable Notifications"
        AppLanguage.FRENCH -> "Activer les notifications"
        AppLanguage.SPANISH -> "Activar notificaciones"
        AppLanguage.GERMAN -> "Benachrichtigungen aktivieren"
        AppLanguage.ITALIAN -> "Attiva notifiche"
    }

    // Daily Verse & Meditation
    fun dailyVerseTitle(lang: AppLanguage): String = when (lang) {
        AppLanguage.ARABIC -> "آية اليوم والتأمل الروحي"
        AppLanguage.COPTIC -> "Ϯⲥⲙⲏ ⲛ̀ϯⲅⲣⲁⲫⲏ ⲙ̀ⲙⲏⲛⲓ"
        AppLanguage.ENGLISH -> "Daily Verse & Meditation"
        AppLanguage.FRENCH -> "Verset Quotidien & Méditation"
        AppLanguage.SPANISH -> "Versículo Diario y Meditación"
        AppLanguage.GERMAN -> "Täglicher Vers & Meditation"
        AppLanguage.ITALIAN -> "Versetto del Giorno e Meditazione"
    }

    fun orthodoxBibleEgypt(lang: AppLanguage): String = when (lang) {
        AppLanguage.ARABIC -> "الكتاب المقدس الأرثوذكسي المعتمد في مصر"
        AppLanguage.COPTIC -> "Ⲡⲓϫⲱⲙ ⲉⲑⲟⲩⲁⲃ ⲛ̀Ⲟⲣⲑⲟⲇⲟⲝⲟⲥ"
        AppLanguage.ENGLISH -> "Orthodox Holy Scripture (Egypt)"
        AppLanguage.FRENCH -> "Sainte Écriture Orthodoxe (Égypte)"
        AppLanguage.SPANISH -> "Santa Escritura Ortodoxa (Egipto)"
        AppLanguage.GERMAN -> "Orthodoxe Heilige Schrift (Ägypten)"
        AppLanguage.ITALIAN -> "Sacra Scrittura Ortodossa (Egitto)"
    }

    fun spiritualExplanation(lang: AppLanguage): String = when (lang) {
        AppLanguage.ARABIC -> "الشرح والتأمل الآبائي الموجز"
        AppLanguage.COPTIC -> "Ⲡⲓⲃⲱⲗ ⲛ̀ⲧⲉ ⲛⲓⲓⲟϯ"
        AppLanguage.ENGLISH -> "Patristic Commentary & Reflection"
        AppLanguage.FRENCH -> "Commentaire et réflexion patristique"
        AppLanguage.SPANISH -> "Comentario y reflexión patrística"
        AppLanguage.GERMAN -> "Patristischer Kommentar & Betrachtung"
        AppLanguage.ITALIAN -> "Commento e riflessione patristica"
    }

    fun spiritualSoundsTitle(lang: AppLanguage): String = when (lang) {
        AppLanguage.ARABIC -> "الأصوات الروحية والألحان الكنسية للتنبيه"
        AppLanguage.COPTIC -> "Ⲛⲓϧⲣⲱⲟⲩ ⲉⲑⲟⲩⲁⲃ ⲛⲉⲙ ⲛⲓϩⲱⲥ"
        AppLanguage.ENGLISH -> "Spiritual Sounds & Hymns for Alarms"
        AppLanguage.FRENCH -> "Sons spirituels et hymnes pour alarmes"
        AppLanguage.SPANISH -> "Sonidos espirituales e himnos para alarmas"
        AppLanguage.GERMAN -> "Geistliche Klänge & Hymnen für Alarme"
        AppLanguage.ITALIAN -> "Suoni spirituali e inni per allarmi"
    }

    fun previewSound(lang: AppLanguage): String = when (lang) {
        AppLanguage.ARABIC -> "معاينة الصوت"
        AppLanguage.COPTIC -> "Ⲥⲱⲧⲉⲙ"
        AppLanguage.ENGLISH -> "Preview Sound"
        AppLanguage.FRENCH -> "Écouter"
        AppLanguage.SPANISH -> "Escuchar"
        AppLanguage.GERMAN -> "Anhören"
        AppLanguage.ITALIAN -> "Ascolta"
    }

    fun stopSound(lang: AppLanguage): String = when (lang) {
        AppLanguage.ARABIC -> "إيقاف"
        AppLanguage.COPTIC -> "Ⲙⲟⲛⲓ"
        AppLanguage.ENGLISH -> "Stop"
        AppLanguage.FRENCH -> "Arrêter"
        AppLanguage.SPANISH -> "Detener"
        AppLanguage.GERMAN -> "Stoppen"
        AppLanguage.ITALIAN -> "Ferma"
    }

    fun selectAlarmTone(lang: AppLanguage): String = when (lang) {
        AppLanguage.ARABIC -> "اختر نغمة التنبيه الروحية"
        AppLanguage.COPTIC -> "Ⲥⲱⲧⲡ ⲙ̀ⲡⲓⲥⲟϩⲓ"
        AppLanguage.ENGLISH -> "Select Spiritual Alarm Tone"
        AppLanguage.FRENCH -> "Choisir la sonnerie spirituelle"
        AppLanguage.SPANISH -> "Seleccionar tono espiritual"
        AppLanguage.GERMAN -> "Geistlichen Alarmton wählen"
        AppLanguage.ITALIAN -> "Seleziona tono spirituale"
    }

    fun spiritualSoundsSubtitle(lang: AppLanguage): String = when (lang) {
        AppLanguage.ARABIC -> "استمع لمعاينة نغمات الأجراس القبطية والدُف والمثلث والناقوس الكنسي لتحديدها لكل صلاة:"
        AppLanguage.COPTIC -> "Ⲥⲱⲧⲉⲙ ⲉ̀ⲛⲓϧⲣⲱⲟⲩ ⲉⲑⲟⲩⲁⲃ ⲛ̀ⲧⲉ Ϯⲉⲕⲕⲗⲏⲥⲓⲁ:"
        AppLanguage.ENGLISH -> "Preview authentic Coptic bells, cymbals, triangle, and monastic wood chimes for your prayer reminders:"
        AppLanguage.FRENCH -> "Écoutez et choisissez parmi les cloches coptes, cymbales et carillons monastiques :"
        AppLanguage.SPANISH -> "Escucha y selecciona entre campanas coptas, címbalos y campanillas monásticas:"
        AppLanguage.GERMAN -> "Hören Sie sich koptische Glocken, Zimbeln und klösterliche Glockenspiele an:"
        AppLanguage.ITALIAN -> "Ascolta e seleziona campane copte, cembali e campane monastiche:"
    }

    fun cancel(lang: AppLanguage): String = when (lang) {
        AppLanguage.ARABIC -> "إلغاء"
        AppLanguage.COPTIC -> "Ⲭⲱ ⲉ̀ⲃⲟⲗ"
        AppLanguage.ENGLISH -> "Cancel"
        AppLanguage.FRENCH -> "Annuler"
        AppLanguage.SPANISH -> "Cancelar"
        AppLanguage.GERMAN -> "Abbrechen"
        AppLanguage.ITALIAN -> "Annulla"
    }

    fun holyGospel(lang: AppLanguage): String = when (lang) {
        AppLanguage.ARABIC -> "الإنجيل المقدس"
        AppLanguage.COPTIC -> "Ⲡⲓⲉⲩⲁⲅⲅⲉⲗⲓⲟⲛ ⲉⲑⲟⲩⲁⲃ"
        AppLanguage.ENGLISH -> "The Holy Gospel"
        AppLanguage.FRENCH -> "Le Saint Évangile"
        AppLanguage.SPANISH -> "El Santo Evangelio"
        AppLanguage.GERMAN -> "Das Heilige Evangelium"
        AppLanguage.ITALIAN -> "Il Santo Vangelo"
    }

    fun holyGospelStand(lang: AppLanguage): String = when (lang) {
        AppLanguage.ARABIC -> "قفوا بخوف أمام الله لسماع الإنجيل المقدس"
        AppLanguage.COPTIC -> "Ⲟⲩⲁⲛⲁⲅⲛⲱⲥⲓⲥ ⲉ̀ⲃⲟⲗ ϧⲉⲛ ⲡⲓⲉⲩⲁⲅⲅⲉⲗⲓⲟⲛ ⲉⲑⲟⲩⲁⲃ"
        AppLanguage.ENGLISH -> "Stand in the fear of God, let us hear the Holy Gospel"
        AppLanguage.FRENCH -> "Levons-nous avec crainte de Dieu pour écouter le Saint Évangile"
        AppLanguage.SPANISH -> "De pie con temor de Dios para escuchar el Santo Evangelio"
        AppLanguage.GERMAN -> "Steht in Gottesfurcht auf, um das Heilige Evangelium zu hören"
        AppLanguage.ITALIAN -> "In piedi con timore di Dio per ascoltare il Santo Vangelo"
    }

    fun gloryToGodForever(lang: AppLanguage): String = when (lang) {
        AppLanguage.ARABIC -> "والمجد لله دائماً أبدياً، آمين."
        AppLanguage.COPTIC -> "Ⲇⲟⲝⲁ ⲥⲓ Ⲕⲩⲣⲓⲉ ⲇⲟⲝⲁ ⲥⲓ."
        AppLanguage.ENGLISH -> "Glory to God forever. Amen."
        AppLanguage.FRENCH -> "Gloire à Dieu pour toujours. Amen."
        AppLanguage.SPANISH -> "Gloria a Dios por siempre. Amén."
        AppLanguage.GERMAN -> "Ehre sei Gott in Ewigkeit. Amen."
        AppLanguage.ITALIAN -> "Gloria a Dio per sempre. Amen."
    }

    fun gospelFinishedPrompt(lang: AppLanguage): String = when (lang) {
        AppLanguage.ARABIC -> "أتممت قراءة الإنجيل المقدس"
        AppLanguage.COPTIC -> "Ⲁⲓϫⲱⲕ ⲉ̀ⲃⲟⲗ ⲙ̀ⲡⲓⲉⲩⲁⲅⲅⲉⲗⲓⲟⲛ"
        AppLanguage.ENGLISH -> "I finished reading the Holy Gospel"
        AppLanguage.FRENCH -> "J'ai terminé la lecture du Saint Évangile"
        AppLanguage.SPANISH -> "He terminado la lectura del Santo Evangelio"
        AppLanguage.GERMAN -> "Ich habe das Heilige Evangelium gelesen"
        AppLanguage.ITALIAN -> "Ho completato la lettura del Santo Vangelo"
    }

    fun recordPrayerNow(lang: AppLanguage): String = when (lang) {
        AppLanguage.ARABIC -> "سجّل الصلاة الآن تلقائياً"
        AppLanguage.COPTIC -> "Ⲥϧⲁⲓ ϯⲡⲣⲟⲥⲉⲩⲭⲏ ϯⲛⲟⲩ"
        AppLanguage.ENGLISH -> "Record Prayer Automatically Now"
        AppLanguage.FRENCH -> "Enregistrer la prière automatiquement maintenant"
        AppLanguage.SPANISH -> "Registrar la oración automáticamente ahora"
        AppLanguage.GERMAN -> "Gebet jetzt automatisch aufzeichnen"
        AppLanguage.ITALIAN -> "Registra la preghiera automaticamente ora"
    }

    fun autoRecordedNotice(lang: AppLanguage): String = when (lang) {
        AppLanguage.ARABIC -> "يتعرف التطبيق تلقائياً على إتمامك للصلاة في موعدها بمجرد الوصول لقراءة الإنجيل المقدس وتسجيلها بالتاريخ والوقت الفعليين."
        AppLanguage.COPTIC -> "Ⲥⲛⲁⲥϧⲁⲓ ⲉ̀ⲡⲓⲥⲁϫⲓ ⲙ̀ⲡⲓⲉϩⲟⲟⲩ ⲛⲉⲙ ϯⲁϫⲡ ϧⲉⲛ ⲡϫⲱⲕ ⲉ̀ⲃⲟⲗ ⲙ̀ⲡⲓⲉⲩⲁⲅⲅⲉⲗⲓⲟⲛ."
        AppLanguage.ENGLISH -> "The app automatically detects that you prayed on time upon reading the Holy Gospel, logging the exact date & time."
        AppLanguage.FRENCH -> "L'application reconnaît automatiquement que vous avez prié à l'heure dès la lecture du Saint Évangile."
        AppLanguage.SPANISH -> "La aplicación reconoce automáticamente que oraste a tiempo al leer el Santo Evangelio."
        AppLanguage.GERMAN -> "Die App erkennt automatisch, dass Sie rechtzeitig gebetet haben, sobald Sie das Evangelium lesen."
        AppLanguage.ITALIAN -> "L'app riconosce automaticamente che hai pregato in tempo leggendo il Santo Vangelo."
    }

    fun prayerRecordedSuccess(lang: AppLanguage): String = when (lang) {
        AppLanguage.ARABIC -> "✓ تم تسجيل الصلاة تلقائياً في موعدها بمجرد قراءة الإنجيل"
        AppLanguage.COPTIC -> "✓ Ⲁⲓⲧⲱⲃϩ - ⲁⲩⲥϧⲏⲧⲥ ϧⲉⲛ ⲡⲉⲥⲥⲏⲟⲩ"
        AppLanguage.ENGLISH -> "✓ Prayer Automatically Logged Upon Reading Gospel"
        AppLanguage.FRENCH -> "✓ Prière automatiquement enregistrée dès la lecture de l'Évangile"
        AppLanguage.SPANISH -> "✓ Oración registrada automáticamente al leer el Evangelio"
        AppLanguage.GERMAN -> "✓ Gebet beim Lesen des Evangeliums automatisch erfasst"
        AppLanguage.ITALIAN -> "✓ Preghiera registrata automaticamente leggendo il Vangelo"
    }

    fun unmarkPrayerPrompt(lang: AppLanguage): String = when (lang) {
        AppLanguage.ARABIC -> "إلغاء التسجيل (لم أصلِّ)"
        AppLanguage.COPTIC -> "Ⲭⲱ ⲉ̀ⲃⲟⲗ"
        AppLanguage.ENGLISH -> "Unmark (Did not pray)"
        AppLanguage.FRENCH -> "Annuler l'enregistrement (pas prié)"
        AppLanguage.SPANISH -> "Desmarcar (no oré)"
        AppLanguage.GERMAN -> "Markierung aufheben (nicht gebetet)"
        AppLanguage.ITALIAN -> "Annulla registrazione (non ho pregato)"
    }

    // Visual Reports & Interactive Charts
    fun visualDashboard(lang: AppLanguage): String = when (lang) {
        AppLanguage.ARABIC -> "لوحة التقارير والرسوم البيانية"
        AppLanguage.COPTIC -> "Ⲡⲓⲫⲱϣ ⲛ̀ⲧⲉ ⲛⲓⲉⲩⲭⲏ"
        AppLanguage.ENGLISH -> "Visual Dashboard & Charts"
        AppLanguage.FRENCH -> "Tableau de bord visuel & graphiques"
        AppLanguage.SPANISH -> "Panel visual y gráficos"
        AppLanguage.GERMAN -> "Visuelles Dashboard & Diagramme"
        AppLanguage.ITALIAN -> "Dashboard visivo e grafici"
    }

    fun prayerFrequency(lang: AppLanguage): String = when (lang) {
        AppLanguage.ARABIC -> "معدل تكرار الصلوات"
        AppLanguage.COPTIC -> "Ⲧⲁϫⲣⲟ ⲛ̀ⲛⲓⲉⲩⲭⲏ"
        AppLanguage.ENGLISH -> "Prayer Frequency"
        AppLanguage.FRENCH -> "Fréquence des prières"
        AppLanguage.SPANISH -> "Frecuencia de oraciones"
        AppLanguage.GERMAN -> "Gebetshäufigkeit"
        AppLanguage.ITALIAN -> "Frequenza delle preghiere"
    }

    fun dailyProgressChart(lang: AppLanguage): String = when (lang) {
        AppLanguage.ARABIC -> "مخطط السواعي وتوزيع الـ 24 ساعة"
        AppLanguage.COPTIC -> "Ⲕⲩⲕⲗⲟⲥ ⲛ̀ⲛⲓⲁϫⲡ"
        AppLanguage.ENGLISH -> "Daily Canonical Hours & 24h Timeline"
        AppLanguage.FRENCH -> "Heures canoniques quotidiennes & ligne de 24h"
        AppLanguage.SPANISH -> "Horas canónicas diarias y cronología de 24h"
        AppLanguage.GERMAN -> "Tägliche Horen & 24h-Zeitachse"
        AppLanguage.ITALIAN -> "Ore canoniche quotidiane e timeline 24h"
    }

    fun monthlyProgressChart(lang: AppLanguage): String = when (lang) {
        AppLanguage.ARABIC -> "رسم بياني لتكرار الصلوات اليومي"
        AppLanguage.COPTIC -> "Ⲡⲓⲥⲱⲛⲧ ⲛ̀ⲧⲉ ⲡⲓⲁⲃⲟⲧ"
        AppLanguage.ENGLISH -> "Daily Prayer Frequency Bar Chart"
        AppLanguage.FRENCH -> "Graphique à barres de fréquence quotidienne"
        AppLanguage.SPANISH -> "Gráfico de barras de frecuencia diaria"
        AppLanguage.GERMAN -> "Balkendiagramm der täglichen Gebetshäufigkeit"
        AppLanguage.ITALIAN -> "Grafico a barre della frequenza giornaliera"
    }

    fun yearlyProgressChart(lang: AppLanguage): String = when (lang) {
        AppLanguage.ARABIC -> "منحنى التقدم الشهري عبر العام (12 شهراً)"
        AppLanguage.COPTIC -> "Ⲡⲓϫⲱⲕ ⲛ̀ϯⲣⲟⲙⲡⲓ ⲧⲏⲣⲥ"
        AppLanguage.ENGLISH -> "12-Month Annual Prayer Trajectory"
        AppLanguage.FRENCH -> "Courbe annuelle de progression sur 12 mois"
        AppLanguage.SPANISH -> "Curva anual de progreso de 12 meses"
        AppLanguage.GERMAN -> "12-Monats-Jahrestrendkurve"
        AppLanguage.ITALIAN -> "Curva di progressione annuale a 12 mesi"
    }

    fun calendarActivityHeatmap(lang: AppLanguage): String = when (lang) {
        AppLanguage.ARABIC -> "خريطة الالتزام الشهري (Activity Heatmap)"
        AppLanguage.COPTIC -> "Ⲡⲓⲙⲏⲏϣ ⲛ̀ⲛⲓⲉϩⲟⲟⲩ"
        AppLanguage.ENGLISH -> "Monthly Activity Heatmap"
        AppLanguage.FRENCH -> "Carte d'activité mensuelle"
        AppLanguage.SPANISH -> "Mapa de actividad mensual"
        AppLanguage.GERMAN -> "Monatliche Aktivitätskarte"
        AppLanguage.ITALIAN -> "Mappa delle attività mensili"
    }

    fun prayerDistribution(lang: AppLanguage): String = when (lang) {
        AppLanguage.ARABIC -> "توزيع نسب الصلوات (Donut Chart)"
        AppLanguage.COPTIC -> "Ⲡⲓⲫⲱϣ ⲛ̀ⲧⲉ ⲛⲓⲁϫⲡ"
        AppLanguage.ENGLISH -> "Prayer Distribution Breakdown"
        AppLanguage.FRENCH -> "Répartition des heures de prière"
        AppLanguage.SPANISH -> "Distribución de las horas de oración"
        AppLanguage.GERMAN -> "Verteilung der Gebetszeiten"
        AppLanguage.ITALIAN -> "Distribuzione delle ore di preghiera"
    }

    fun consistencyScore(lang: AppLanguage): String = when (lang) {
        AppLanguage.ARABIC -> "نسبة الالتزام"
        AppLanguage.COPTIC -> "Ⲧⲁϫⲣⲟ"
        AppLanguage.ENGLISH -> "Consistency"
        AppLanguage.FRENCH -> "Régularité"
        AppLanguage.SPANISH -> "Constancia"
        AppLanguage.GERMAN -> "Beständigkeit"
        AppLanguage.ITALIAN -> "Costanza"
    }

    fun dailyAverage(lang: AppLanguage): String = when (lang) {
        AppLanguage.ARABIC -> "المعدل اليومي"
        AppLanguage.COPTIC -> "Ⲡⲓⲙⲏⲧⲣⲟⲛ ⲙ̀ⲙⲏⲛⲓ"
        AppLanguage.ENGLISH -> "Daily Average"
        AppLanguage.FRENCH -> "Moyenne quotidienne"
        AppLanguage.SPANISH -> "Promedio diario"
        AppLanguage.GERMAN -> "Tagesdurchschnitt"
        AppLanguage.ITALIAN -> "Media giornaliera"
    }

    // Cloud Sync & Firestore
    fun cloudSyncTitle(lang: AppLanguage): String = when (lang) {
        AppLanguage.ARABIC -> "المزامنة السحابية عبر الأجهزة (Cloud Sync)"
        AppLanguage.COPTIC -> "Ⲡⲓⲧⲱⲙ ⲛ̀ⲛⲓⲉⲩⲭⲏ ⲉ̀ⲡϣⲱⲓ"
        AppLanguage.ENGLISH -> "Cross-Device Cloud Sync"
        AppLanguage.FRENCH -> "Synchronisation Cloud multi-appareils"
        AppLanguage.SPANISH -> "Sincronización en la nube multidispositivo"
        AppLanguage.GERMAN -> "Geräteübergreifende Cloud-Synchronisierung"
        AppLanguage.ITALIAN -> "Sincronizzazione cloud multidispositivo"
    }

    fun cloudSyncSubtitle(lang: AppLanguage): String = when (lang) {
        AppLanguage.ARABIC -> "مزامنة سجل الصلوات والإعدادات تلقائياً عبر جميع أجهزتك بواسطة Firestore"
        AppLanguage.COPTIC -> "Ⲥϧⲁⲓ ⲛⲓⲉⲩⲭⲏ ⲛⲉⲙ ⲛⲓⲣⲁⲛ ϧⲉⲛ ⲡⲓⲥⲩⲛⲭⲣⲟⲛ"
        AppLanguage.ENGLISH -> "Sync prayer history and settings across all your devices with Firestore"
        AppLanguage.FRENCH -> "Synchronisez l'historique des prières et les paramètres sur tous vos appareils"
        AppLanguage.SPANISH -> "Sincroniza el historial de oraciones y configuraciones en todos tus dispositivos"
        AppLanguage.GERMAN -> "Synchronisieren Sie Gebetsverlauf und Einstellungen auf allen Ihren Geräten"
        AppLanguage.ITALIAN -> "Sincronizza la cronologia delle preghiere e le impostazioni su tutti i dispositivi"
    }

    fun syncStatusSynced(lang: AppLanguage): String = when (lang) {
        AppLanguage.ARABIC -> "متزامن سحابياً بالكامل (Firestore)"
        AppLanguage.COPTIC -> "Ⲁⲩⲧⲱⲙ ⲉ̀ⲃⲟⲗ"
        AppLanguage.ENGLISH -> "All Synced via Firestore"
        AppLanguage.FRENCH -> "Entièrement synchronisé (Firestore)"
        AppLanguage.SPANISH -> "Completamente sincronizado (Firestore)"
        AppLanguage.GERMAN -> "Vollständig synchronisiert (Firestore)"
        AppLanguage.ITALIAN -> "Completamente sincronizzato (Firestore)"
    }

    fun syncStatusSyncing(lang: AppLanguage): String = when (lang) {
        AppLanguage.ARABIC -> "جارٍ المزامنة السحابية..."
        AppLanguage.COPTIC -> "Ⲉϥⲧⲱⲙ..."
        AppLanguage.ENGLISH -> "Syncing with Cloud..."
        AppLanguage.FRENCH -> "Synchronisation en cours..."
        AppLanguage.SPANISH -> "Sincronizando con la nube..."
        AppLanguage.GERMAN -> "Wird synchronisiert..."
        AppLanguage.ITALIAN -> "Sincronizzazione in corso..."
    }

    fun syncStatusOffline(lang: AppLanguage): String = when (lang) {
        AppLanguage.ARABIC -> "الوضع المحلي (حفظ على هذا الجهاز)"
        AppLanguage.COPTIC -> "Ϧⲉⲛ ⲡⲁⲓⲥⲏⲟⲩ"
        AppLanguage.ENGLISH -> "Local Storage Mode"
        AppLanguage.FRENCH -> "Mode stockage local"
        AppLanguage.SPANISH -> "Modo de almacenamiento local"
        AppLanguage.GERMAN -> "Lokaler Speichermodus"
        AppLanguage.ITALIAN -> "Modalità memoria locale"
    }

    fun syncNow(lang: AppLanguage): String = when (lang) {
        AppLanguage.ARABIC -> "مزامنة الآن"
        AppLanguage.COPTIC -> "Ⲧⲱⲙ ϯⲛⲟⲩ"
        AppLanguage.ENGLISH -> "Sync Now"
        AppLanguage.FRENCH -> "Synchroniser"
        AppLanguage.SPANISH -> "Sincronizar ahora"
        AppLanguage.GERMAN -> "Jetzt synchronisieren"
        AppLanguage.ITALIAN -> "Sincronizza ora"
    }

    fun deviceSyncKey(lang: AppLanguage): String = when (lang) {
        AppLanguage.ARABIC -> "مفتاح المزامنة (Sync Key)"
        AppLanguage.COPTIC -> "Ⲡⲓϣⲟϣⲧ ⲛ̀ⲧⲉ ⲡⲓⲧⲱⲙ"
        AppLanguage.ENGLISH -> "Device Sync Key"
        AppLanguage.FRENCH -> "Clé de synchronisation"
        AppLanguage.SPANISH -> "Clave de sincronización"
        AppLanguage.GERMAN -> "Sync-Schlüssel"
        AppLanguage.ITALIAN -> "Chiave di sincronizzazione"
    }

    fun syncKeyDescription(lang: AppLanguage): String = when (lang) {
        AppLanguage.ARABIC -> "استخدم نفس هذا المفتاح في هاتفك الآخر أو جهاز التابلت لربط السجل والصلوات تلقائياً."
        AppLanguage.COPTIC -> "Ⲕⲱ ⲙ̀ⲡⲁⲓϣⲟϣⲧ ϧⲉⲛ ⲛⲓⲥⲩⲥⲕⲉⲩⲏ ⲧⲏⲣⲟⲩ."
        AppLanguage.ENGLISH -> "Use this same key on your other phone or tablet to link your prayer history automatically."
        AppLanguage.FRENCH -> "Utilisez cette même clé sur vos autres appareils pour lier automatiquement votre historique."
        AppLanguage.SPANISH -> "Usa esta misma clave en tus otros dispositivos para vincular tu historial automáticamente."
        AppLanguage.GERMAN -> "Verwenden Sie denselben Schlüssel auf Ihren anderen Geräten für die automatische Verknüpfung."
        AppLanguage.ITALIAN -> "Usa questa stessa chiave sui tuoi altri dispositivi per collegare automaticamente la cronologia."
    }

    fun linkAnotherDevice(lang: AppLanguage): String = when (lang) {
        AppLanguage.ARABIC -> "ربط بجهاز آخر بواسطة مفتاح"
        AppLanguage.COPTIC -> "Ⲧⲱⲙ ⲛⲉⲙ ⲕⲉⲥⲩⲥⲕⲉⲩⲏ"
        AppLanguage.ENGLISH -> "Link with another Device Key"
        AppLanguage.FRENCH -> "Lier avec une autre clé"
        AppLanguage.SPANISH -> "Vincular con otra clave"
        AppLanguage.GERMAN -> "Mit anderem Schlüssel verknüpfen"
        AppLanguage.ITALIAN -> "Collega con un'altra chiave"
    }

    fun enterSyncKeyPrompt(lang: AppLanguage): String = when (lang) {
        AppLanguage.ARABIC -> "أدخل مفتاح المزامنة الخاص بجهازك الآخر للربط ومشاركة السجل:"
        AppLanguage.COPTIC -> "Ⲕⲱ ⲙ̀ⲡⲓϣⲟϣⲧ ⲛ̀ⲧⲉ ⲕⲉⲥⲩⲥⲕⲉⲩⲏ:"
        AppLanguage.ENGLISH -> "Enter the Sync Key from your other device to link and share history:"
        AppLanguage.FRENCH -> "Entrez la clé de synchronisation de votre autre appareil :"
        AppLanguage.SPANISH -> "Ingresa la clave de sincronización de tu otro dispositivo:"
        AppLanguage.GERMAN -> "Geben Sie den Sync-Schlüssel Ihres anderen Geräts ein:"
        AppLanguage.ITALIAN -> "Inserisci la chiave di sincronizzazione dell'altro dispositivo:"
    }

    fun saveAndSync(lang: AppLanguage): String = when (lang) {
        AppLanguage.ARABIC -> "حفظ ومزامنة"
        AppLanguage.COPTIC -> "Ⲥϧⲁⲓ ⲛⲉⲙ ⲧⲱⲙ"
        AppLanguage.ENGLISH -> "Save & Sync"
        AppLanguage.FRENCH -> "Enregistrer et synchroniser"
        AppLanguage.SPANISH -> "Guardar y sincronizar"
        AppLanguage.GERMAN -> "Speichern & Synchronisieren"
        AppLanguage.ITALIAN -> "Salva e sincronizza"
    }
}


