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
        AppLanguage.CHINESE -> "日课经祷文 (Agpeya)"
        AppLanguage.JAPANESE -> "アグペヤの祈り (時課の祈り)"
        AppLanguage.KOREAN -> "아그페야 기도서 (성무일도)"
    }

    fun appSubtitle(lang: AppLanguage): String = when (lang) {
        AppLanguage.ARABIC -> "كتاب صلوات السواعي الأرثوذكسي"
        AppLanguage.COPTIC -> "Ⲡⲓϫⲱⲙ ⲛ̀ⲧⲉ ⲛⲓⲁϫⲡ ⲉⲑⲟⲩⲁⲃ"
        AppLanguage.ENGLISH -> "The Coptic Book of Canonical Hours"
        AppLanguage.FRENCH -> "Livre des Heures Canoniques Coptes"
        AppLanguage.SPANISH -> "Libro de las Horas Canónicas Copto"
        AppLanguage.GERMAN -> "Koptisches Stundengebetbuch"
        AppLanguage.ITALIAN -> "Libro delle Ore Canoniche Copte"
        AppLanguage.CHINESE -> "科普特正教会日课经 (Canonical Hours)"
        AppLanguage.JAPANESE -> "コプト正教会 時課の祈りの書"
        AppLanguage.KOREAN -> "콥트 정교회 시간경 기도서"
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
        AppLanguage.CHINESE -> "祷文"
        AppLanguage.JAPANESE -> "祈り"
        AppLanguage.KOREAN -> "기도"
    }

    fun tabReports(lang: AppLanguage): String = when (lang) {
        AppLanguage.ARABIC -> "التقارير"
        AppLanguage.COPTIC -> "Ⲡⲓⲧⲱⲡ"
        AppLanguage.ENGLISH -> "Reports"
        AppLanguage.FRENCH -> "Rapports"
        AppLanguage.SPANISH -> "Informes"
        AppLanguage.GERMAN -> "Berichte"
        AppLanguage.ITALIAN -> "Rapporti"
        AppLanguage.CHINESE -> "报告"
        AppLanguage.JAPANESE -> "記録と統計"
        AppLanguage.KOREAN -> "보고서"
    }

    fun tabSettings(lang: AppLanguage): String = when (lang) {
        AppLanguage.ARABIC -> "التنبيهات واللغة"
        AppLanguage.COPTIC -> "Ⲛⲓⲥⲩⲛⲑⲏⲕⲏ"
        AppLanguage.ENGLISH -> "Alarms & Settings"
        AppLanguage.FRENCH -> "Alarmes & Paramètres"
        AppLanguage.SPANISH -> "Alarmas y Ajustes"
        AppLanguage.GERMAN -> "Alarme & Einstellungen"
        AppLanguage.ITALIAN -> "Allarmi e Impostazioni"
        AppLanguage.CHINESE -> "闹钟与设置"
        AppLanguage.JAPANESE -> "通知と設定"
        AppLanguage.KOREAN -> "알람 및 설정"
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
        AppLanguage.CHINESE -> "日视图"
        AppLanguage.JAPANESE -> "日別"
        AppLanguage.KOREAN -> "일간"
    }

    fun reportMonthly(lang: AppLanguage): String = when (lang) {
        AppLanguage.ARABIC -> "شهري"
        AppLanguage.COPTIC -> "Ⲫⲏⲧ"
        AppLanguage.ENGLISH -> "Monthly"
        AppLanguage.FRENCH -> "Mensuel"
        AppLanguage.SPANISH -> "Mensual"
        AppLanguage.GERMAN -> "Monatlich"
        AppLanguage.ITALIAN -> "Mensile"
        AppLanguage.CHINESE -> "月视图"
        AppLanguage.JAPANESE -> "月別"
        AppLanguage.KOREAN -> "월간"
    }

    fun reportYearly(lang: AppLanguage): String = when (lang) {
        AppLanguage.ARABIC -> "سنوي"
        AppLanguage.COPTIC -> "Ⲣⲟⲙⲡⲓ"
        AppLanguage.ENGLISH -> "Yearly"
        AppLanguage.FRENCH -> "Annuel"
        AppLanguage.SPANISH -> "Anual"
        AppLanguage.GERMAN -> "Jährlich"
        AppLanguage.ITALIAN -> "Annuale"
        AppLanguage.CHINESE -> "年视图"
        AppLanguage.JAPANESE -> "年別"
        AppLanguage.KOREAN -> "연간"
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
        AppLanguage.CHINESE -> "已完成此时课祈祷"
        AppLanguage.JAPANESE -> "この時課を祈りました"
        AppLanguage.KOREAN -> "이 시간 기도 완료"
    }

    fun alreadyPrayed(lang: AppLanguage): String = when (lang) {
        AppLanguage.ARABIC -> "تمت الصلاة اليوم"
        AppLanguage.COPTIC -> "Ⲁⲓⲧⲱⲃϩ"
        AppLanguage.ENGLISH -> "Prayed Today"
        AppLanguage.FRENCH -> "Prié aujourd'hui"
        AppLanguage.SPANISH -> "Orado hoy"
        AppLanguage.GERMAN -> "Heute gebetet"
        AppLanguage.ITALIAN -> "Pregato oggi"
        AppLanguage.CHINESE -> "今日已祈祷"
        AppLanguage.JAPANESE -> "本日祈祷済み"
        AppLanguage.KOREAN -> "오늘 기도함"
    }

    fun readPrayer(lang: AppLanguage): String = when (lang) {
        AppLanguage.ARABIC -> "قراءة الصلاة والتأمل"
        AppLanguage.COPTIC -> "Ⲱϣ ⲛ̀ϯⲉⲩⲭⲏ"
        AppLanguage.ENGLISH -> "Read Prayer & Meditation"
        AppLanguage.FRENCH -> "Lire la prière & méditation"
        AppLanguage.SPANISH -> "Leer oración y meditación"
        AppLanguage.GERMAN -> "Gebet lesen & Meditation"
        AppLanguage.ITALIAN -> "Leggi la preghiera e meditazione"
        AppLanguage.CHINESE -> "阅读祷文与默想"
        AppLanguage.JAPANESE -> "祈りと黙想を読む"
        AppLanguage.KOREAN -> "기도문 읽기 및 묵상"
    }

    fun testAlert(lang: AppLanguage): String = when (lang) {
        AppLanguage.ARABIC -> "تجربة تنبيه الآن"
        AppLanguage.COPTIC -> "Ⲡⲓⲧⲱⲛ ⲧⲏⲣϥ"
        AppLanguage.ENGLISH -> "Test Notification Now"
        AppLanguage.FRENCH -> "Tester l'alerte"
        AppLanguage.SPANISH -> "Probar alerta ahora"
        AppLanguage.GERMAN -> "Jetzt Benachrichtigung testen"
        AppLanguage.ITALIAN -> "Prova notifica ora"
        AppLanguage.CHINESE -> "测试提示音与振动"
        AppLanguage.JAPANESE -> "通知と振動をテスト"
        AppLanguage.KOREAN -> "지금 알림 테스트"
    }

    fun timezoneWorldwide(lang: AppLanguage): String = when (lang) {
        AppLanguage.ARABIC -> "توقيتك المحلي الحالي"
        AppLanguage.COPTIC -> "Ⲡⲓⲥⲏⲟⲩ ⲛ̀ⲧⲁⲕ"
        AppLanguage.ENGLISH -> "Current Local Timezone"
        AppLanguage.FRENCH -> "Fuseau horaire local"
        AppLanguage.SPANISH -> "Zona horaria local"
        AppLanguage.GERMAN -> "Aktuelle lokale Zeitzone"
        AppLanguage.ITALIAN -> "Fuso orario locale"
        AppLanguage.CHINESE -> "当前本地时区"
        AppLanguage.JAPANESE -> "現在の現地タイムゾーン"
        AppLanguage.KOREAN -> "현재 현지 표준시"
    }

    fun alarmNotificationTitle(prayerName: String, lang: AppLanguage): String = when (lang) {
        AppLanguage.ARABIC -> "حان موعد صلاة $prayerName"
        AppLanguage.COPTIC -> "Ⲁϥⲓ̀ ⲉ̀ⲡⲓⲥⲏⲟⲩ ⲛ̀$prayerName"
        AppLanguage.ENGLISH -> "Time for $prayerName Prayer"
        AppLanguage.FRENCH -> "L'heure de la prière de $prayerName"
        AppLanguage.SPANISH -> "Hora de la oración de $prayerName"
        AppLanguage.GERMAN -> "Zeit für das $prayerName Gebet"
        AppLanguage.ITALIAN -> "È l'ora della preghiera di $prayerName"
        AppLanguage.CHINESE -> "已到${prayerName}祷告时间"
        AppLanguage.JAPANESE -> "${prayerName}の祈りの時間です"
        AppLanguage.KOREAN -> "${prayerName} 기도 시간입니다"
    }

    fun alarmNotificationBody(lang: AppLanguage): String = when (lang) {
        AppLanguage.ARABIC -> "«سبع مرات في النهار سبحتك على أحكام عدلك» - هلُم نسجد ونركع للمسيح ملكنا وإلهنا"
        AppLanguage.COPTIC -> "«Ϣⲁϣϥ ⲛ̀ⲥⲟⲡ ϧⲉⲛ ⲡⲓⲉϩⲟⲟⲩ ⲁⲓⲥⲙⲟⲩ ⲉ̀ⲣⲟⲕ» - Ⲁⲙⲱⲓⲛⲓ ⲙⲁⲣⲉⲛⲟⲩⲱϣⲧ ⲙ̀Ⲡⲭ̅ⲥ̅"
        AppLanguage.ENGLISH -> "\"Seven times a day I praise You, because of Your righteous judgments.\" - Come let us worship Christ our King."
        AppLanguage.FRENCH -> "« Sept fois par jour je te célèbre, à cause des lois de ta justice. » Venez, adorons le Christ notre Roi."
        AppLanguage.SPANISH -> "«Siete veces al día te alabo a causa de tus justos juicios.» - Venid, adoremos a Cristo nuestro Rey."
        AppLanguage.GERMAN -> "„Siebenmal am Tag lobe ich dich wegen deiner gerechten Urteile.“ - Kommt, lasst uns Christus, unseren König, anbeten."
        AppLanguage.ITALIAN -> "«Sette volte al giorno ti ho lodato per i tuoi giusti giudizi.» - Venite, adoriamo Cristo nostro Re."
        AppLanguage.CHINESE -> "“我因你公义的典章一天七次赞美你。” —— 来吧，让我们向我们的君王基督敬拜跪拜。"
        AppLanguage.JAPANESE -> "「私はあなたの一日に七度、あなたの正しい裁きのためにあなたを賛美します。」— 来たりて王なるキリストを拝みましょう。"
        AppLanguage.KOREAN -> "“내가 주의 의로운 규례들로 말미암아 하루 일곱 번씩 주를 찬양하나이다.” — 오라 우리가 우리 왕이신 그리스도께 엎드려 경배하자."
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
        AppLanguage.CHINESE -> "已完成祷告总数"
        AppLanguage.JAPANESE -> "完了した祈りの総数"
        AppLanguage.KOREAN -> "총 완료된 기도"
    }

    fun prayersTodayLabel(lang: AppLanguage): String = when (lang) {
        AppLanguage.ARABIC -> "صلوات اليوم"
        AppLanguage.COPTIC -> "Ⲛⲓⲉⲩⲭⲏ ⲙ̀ⲫⲟⲟⲩ"
        AppLanguage.ENGLISH -> "Today's Prayers"
        AppLanguage.FRENCH -> "Prières d'aujourd'hui"
        AppLanguage.SPANISH -> "Oraciones de hoy"
        AppLanguage.GERMAN -> "Heutige Gebete"
        AppLanguage.ITALIAN -> "Preghiere di oggi"
        AppLanguage.CHINESE -> "今日祷告"
        AppLanguage.JAPANESE -> "今日の祈り"
        AppLanguage.KOREAN -> "오늘의 기도"
    }

    fun monthlyBreakdown(lang: AppLanguage): String = when (lang) {
        AppLanguage.ARABIC -> "توزيع الصلوات خلال الشهر"
        AppLanguage.COPTIC -> "Ⲡⲓⲫⲱϣ ⲛ̀ⲧⲉ ⲡⲓⲁⲃⲟⲧ"
        AppLanguage.ENGLISH -> "Monthly Prayer Breakdown"
        AppLanguage.FRENCH -> "Répartition mensuelle des prières"
        AppLanguage.SPANISH -> "Desglose mensual de oraciones"
        AppLanguage.GERMAN -> "Monatliche Gebetsaufteilung"
        AppLanguage.ITALIAN -> "Dettaglio mensile delle preghiere"
        AppLanguage.CHINESE -> "当月祷告分布"
        AppLanguage.JAPANESE -> "月間祈祷内訳"
        AppLanguage.KOREAN -> "월별 기도 분석"
    }

    fun yearlySummary(lang: AppLanguage): String = when (lang) {
        AppLanguage.ARABIC -> "التقرير السنوي والمعدل الشهري"
        AppLanguage.COPTIC -> "Ⲡⲓϫⲱⲕ ⲛ̀ⲧⲉ ϯⲣⲟⲙⲡⲓ"
        AppLanguage.ENGLISH -> "Yearly Summary & Monthly Trend"
        AppLanguage.FRENCH -> "Bilan annuel et évolution mensuelle"
        AppLanguage.SPANISH -> "Resumen anual y tendencia mensual"
        AppLanguage.GERMAN -> "Jahresübersicht und Monatstrends"
        AppLanguage.ITALIAN -> "Riepilogo annuale e andamento mensile"
        AppLanguage.CHINESE -> "年度总结与月度走势"
        AppLanguage.JAPANESE -> "年間総括と月別推移"
        AppLanguage.KOREAN -> "연간 요약 및 월간 추이"
    }

    fun prayersLogHistory(lang: AppLanguage): String = when (lang) {
        AppLanguage.ARABIC -> "سجل الصلوات بالتواريخ والأوقات"
        AppLanguage.COPTIC -> "Ⲡⲓⲥϧⲁⲓ ⲛ̀ⲧⲉ ⲛⲓⲥⲏⲟⲩ"
        AppLanguage.ENGLISH -> "Prayer Log History with Dates & Times"
        AppLanguage.FRENCH -> "Historique des prières avec dates et heures"
        AppLanguage.SPANISH -> "Historial de oraciones con fechas y horas"
        AppLanguage.GERMAN -> "Gebetsprotokoll mit Datum und Uhrzeit"
        AppLanguage.ITALIAN -> "Cronologia preghiere con date e orari"
        AppLanguage.CHINESE -> "带日期时间的祷告记录历史"
        AppLanguage.JAPANESE -> "日時付き祈祷履歴ログ"
        AppLanguage.KOREAN -> "일시별 기도 기록 히스토리"
    }

    fun noLogsYet(lang: AppLanguage): String = when (lang) {
        AppLanguage.ARABIC -> "لا توجد صلوات مسجلة في هذه الفترة حتى الآن. اضغط على أيقونة الصلاة لتسجيلها."
        AppLanguage.COPTIC -> "Ⲙⲙⲟⲛ ⲉⲩⲭⲏ ⲉⲥⲥϧⲏⲟⲩⲧ ⲙ̀ⲡⲁⲓⲥⲏⲟⲩ."
        AppLanguage.ENGLISH -> "No prayers recorded in this period yet. Tap any prayer card to log it."
        AppLanguage.FRENCH -> "Aucune prière enregistrée pour cette période. Appuyez sur une prière pour l'enregistrer."
        AppLanguage.SPANISH -> "No hay oraciones registradas en este período. Toca una oración para registrarla."
        AppLanguage.GERMAN -> "Noch keine Gebete in diesem Zeitraum erfasst. Tippen Sie auf ein Gebet, um es zu protokollieren."
        AppLanguage.ITALIAN -> "Nessuna preghiera registrata in questo periodo. Tocca una preghiera per registrarla."
        AppLanguage.CHINESE -> "此期间尚无祈祷记录。点击任一时课卡片即可记录。"
        AppLanguage.JAPANESE -> "この期間の祈りの記録はまだありません。祈りカードをタップして記録できます。"
        AppLanguage.KOREAN -> "이 기간에 기록된 기도가 아직 없습니다. 기도 카드를 눌러 기록하세요."
    }

    fun verseSevenTimes(lang: AppLanguage): String = when (lang) {
        AppLanguage.ARABIC -> "«سَبْعَ مَرَّاتٍ فِي النَّهَارِ سَبَّحْتُكَ عَلَى أَحْكَامِ عَدْلِكَ» (مزمور 119: 164)"
        AppLanguage.COPTIC -> "«Ϣⲁϣϥ ⲛ̀ⲥⲟⲡ ϧⲉⲛ ⲡⲓⲉϩⲟⲟⲩ ⲁⲓⲥⲙⲟⲩ ⲉ̀ⲣⲟⲕ ⲉ̀ϫⲉⲛ ⲛⲓϩⲁⲡ ⲛ̀ⲧⲉ ⲧⲉⲕⲙⲉⲑⲙⲏⲓ»"
        AppLanguage.ENGLISH -> "\"Seven times a day I praise You, because of Your righteous judgments.\" (Psalm 119:164)"
        AppLanguage.FRENCH -> "« Sept fois par jour je te célèbre, à cause des lois de ta justice. » (Psaume 119:164)"
        AppLanguage.SPANISH -> "«Siete veces al día te alabo a causa de tus justos juicios.» (Salmo 119:164)"
        AppLanguage.GERMAN -> "„Siebenmal am Tag lobe ich dich wegen deiner gerechten Urteile.“ (Psalm 119:164)"
        AppLanguage.ITALIAN -> "«Sette volte al giorno ti ho lodato per i tuoi giusti giudizi.» (Salmo 119:164)"
        AppLanguage.CHINESE -> "“我因你公义的典章一天七次赞美你。”（诗篇 119:164）"
        AppLanguage.JAPANESE -> "「私はあなたの一日に七度、あなたの正しい裁きのためにあなたを賛美します。」(詩篇 119:164)"
        AppLanguage.KOREAN -> "“내가 주의 의로운 규례들로 말미암아 하루 일곱 번씩 주를 찬양하나이다” (시편 119:164)"
    }

    fun languageSelection(lang: AppLanguage): String = when (lang) {
        AppLanguage.ARABIC -> "لغة التطبيق"
        AppLanguage.COPTIC -> "Ϯⲁⲥⲡⲓ ⲛ̀ⲧⲉ ⲡⲓⲉⲫⲁⲣⲙⲟⲅⲏ"
        AppLanguage.ENGLISH -> "App Language"
        AppLanguage.FRENCH -> "Langue de l'application"
        AppLanguage.SPANISH -> "Idioma de la aplicación"
        AppLanguage.GERMAN -> "App-Sprache"
        AppLanguage.ITALIAN -> "Lingua dell'applicazione"
        AppLanguage.CHINESE -> "应用语言"
        AppLanguage.JAPANESE -> "言語設定"
        AppLanguage.KOREAN -> "앱 언어"
    }

    fun alarmTime(lang: AppLanguage): String = when (lang) {
        AppLanguage.ARABIC -> "توقيت التنبيه"
        AppLanguage.COPTIC -> "Ⲡⲓⲥⲏⲟⲩ ⲛ̀ⲧⲉ ⲡⲓⲥⲟϩⲓ"
        AppLanguage.ENGLISH -> "Alarm Time"
        AppLanguage.FRENCH -> "Heure de l'alarme"
        AppLanguage.SPANISH -> "Hora de la alarma"
        AppLanguage.GERMAN -> "Alarmzeit"
        AppLanguage.ITALIAN -> "Orario sveglia"
        AppLanguage.CHINESE -> "提醒时间"
        AppLanguage.JAPANESE -> "アラーム時刻"
        AppLanguage.KOREAN -> "알람 시간"
    }

    fun soundAlert(lang: AppLanguage): String = when (lang) {
        AppLanguage.ARABIC -> "الصوت والاهتزاز"
        AppLanguage.COPTIC -> "Ⲡⲓϧⲣⲱⲟⲩ"
        AppLanguage.ENGLISH -> "Sound & Vibration"
        AppLanguage.FRENCH -> "Son et vibration"
        AppLanguage.SPANISH -> "Sonido y vibración"
        AppLanguage.GERMAN -> "Ton & Vibration"
        AppLanguage.ITALIAN -> "Suono e vibrazione"
        AppLanguage.CHINESE -> "声音与振动"
        AppLanguage.JAPANESE -> "音とバイブレーション"
        AppLanguage.KOREAN -> "소리 및 진동"
    }

    fun worldwideNotice(lang: AppLanguage): String = when (lang) {
        AppLanguage.ARABIC -> "يعمل التطبيق في جميع دول العالم بحسب التوقيت المحلي لهاتفك، ويمكنك تعديل موعد كل صلاة بدقة بحسب رغبتك."
        AppLanguage.COPTIC -> "Ⲫⲁⲓ ⲉⲣϩⲱⲃ ϧⲉⲛ ⲡⲓⲕⲟⲥⲙⲟⲥ ⲧⲏⲣϥ ⲕⲁⲧⲁ ⲡⲉⲕⲥⲏⲟⲩ."
        AppLanguage.ENGLISH -> "Works in all countries worldwide based on your device's local timezone. You can adjust the alarm time for each prayer as you wish."
        AppLanguage.FRENCH -> "Fonctionne dans tous les pays selon votre fuseau horaire local. Vous pouvez ajuster l'heure de chaque prière."
        AppLanguage.SPANISH -> "Funciona en todos los países según la zona horaria de su dispositivo. Puede ajustar la hora de cada oración a su gusto."
        AppLanguage.GERMAN -> "Funktioniert weltweit in allen Ländern basierend auf der lokalen Zeitzone Ihres Geräts. Die Alarmzeiten können frei angepasst werden."
        AppLanguage.ITALIAN -> "Funziona in tutto il mondo in base al fuso orario locale. Puoi regolare l'orario di ogni preghiera liberamente."
        AppLanguage.CHINESE -> "基于您设备的本地时区，全球皆可使用。您可以随心调整每个时课的提醒时间。"
        AppLanguage.JAPANESE -> "端末の現地タイムゾーンに基づき世界中で機能します。各時課のアラーム時刻は自由に設定可能です。"
        AppLanguage.KOREAN -> "기기의 현지 표준시에 맞춰 전 세계 어디서나 동작합니다. 각 시간경 알람 시간을 자유롭게 설정할 수 있습니다."
    }

    fun close(lang: AppLanguage): String = when (lang) {
        AppLanguage.ARABIC -> "إغلاق"
        AppLanguage.COPTIC -> "Ⲙⲁϣⲑⲁⲙ"
        AppLanguage.ENGLISH -> "Close"
        AppLanguage.FRENCH -> "Fermer"
        AppLanguage.SPANISH -> "Cerrar"
        AppLanguage.GERMAN -> "Schließen"
        AppLanguage.ITALIAN -> "Chiudi"
        AppLanguage.CHINESE -> "关闭"
        AppLanguage.JAPANESE -> "閉じる"
        AppLanguage.KOREAN -> "닫기"
    }

    fun save(lang: AppLanguage): String = when (lang) {
        AppLanguage.ARABIC -> "حفظ"
        AppLanguage.COPTIC -> "Ⲁⲣⲉϩ"
        AppLanguage.ENGLISH -> "Save"
        AppLanguage.FRENCH -> "Enregistrer"
        AppLanguage.SPANISH -> "Guardar"
        AppLanguage.GERMAN -> "Speichern"
        AppLanguage.ITALIAN -> "Salva"
        AppLanguage.CHINESE -> "保存"
        AppLanguage.JAPANESE -> "保存"
        AppLanguage.KOREAN -> "저장"
    }

    fun deleteConfirm(lang: AppLanguage): String = when (lang) {
        AppLanguage.ARABIC -> "حذف هذا السجل؟"
        AppLanguage.COPTIC -> "Ⲃⲱⲗ ⲉⲃⲟⲗ"
        AppLanguage.ENGLISH -> "Delete this log?"
        AppLanguage.FRENCH -> "Supprimer cet enregistrement ?"
        AppLanguage.SPANISH -> "¿Eliminar este registro?"
        AppLanguage.GERMAN -> "Diesen Eintrag löschen?"
        AppLanguage.ITALIAN -> "Eliminare questa voce?"
        AppLanguage.CHINESE -> "删除此记录？"
        AppLanguage.JAPANESE -> "この記録を削除しますか？"
        AppLanguage.KOREAN -> "이 기록을 삭제하시겠습니까?"
    }

    fun notificationPermissionRequired(lang: AppLanguage): String = when (lang) {
        AppLanguage.ARABIC -> "يرجى السماح بالتنبيهات لكي يتمكن التطبيق من تذكيرك بمواعيد صلوات الأجبية."
        AppLanguage.COPTIC -> "Ⲙⲁ ϯⲉⲝⲟⲩⲥⲓⲁ ⲉ̀ⲡⲓⲥⲟϩⲓ"
        AppLanguage.ENGLISH -> "Please enable notification permissions so the app can alert you at prayer times."
        AppLanguage.FRENCH -> "Veuillez autoriser les notifications pour recevoir les alertes des heures de prière."
        AppLanguage.SPANISH -> "Por favor, active los permisos de notificación para recibir las alertas de oración."
        AppLanguage.GERMAN -> "Bitte erlauben Sie Benachrichtigungen, damit die App Sie an die Gebetszeiten erinnern kann."
        AppLanguage.ITALIAN -> "Abilita i permessi di notifica per ricevere gli avvisi negli orari di preghiera."
        AppLanguage.CHINESE -> "请开启通知权限，以便应用在祷告时间提醒您。"
        AppLanguage.JAPANESE -> "祈りの時間に通知を受け取れるよう、通知権限を許可してください。"
        AppLanguage.KOREAN -> "기도 시간에 알림을 받을 수 있도록 알림 권한을 허용해 주세요."
    }

    fun allowPermission(lang: AppLanguage): String = when (lang) {
        AppLanguage.ARABIC -> "تفعيل التنبيهات"
        AppLanguage.COPTIC -> "Ⲧⲱⲟⲩⲛⲟⲩ"
        AppLanguage.ENGLISH -> "Enable Notifications"
        AppLanguage.FRENCH -> "Activer les notifications"
        AppLanguage.SPANISH -> "Activar notificaciones"
        AppLanguage.GERMAN -> "Benachrichtigungen aktivieren"
        AppLanguage.ITALIAN -> "Attiva notifiche"
        AppLanguage.CHINESE -> "启用通知"
        AppLanguage.JAPANESE -> "通知を有効にする"
        AppLanguage.KOREAN -> "알림 활성화"
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
        AppLanguage.CHINESE -> "今日金句与灵修默想"
        AppLanguage.JAPANESE -> "今日のみ言葉と黙想"
        AppLanguage.KOREAN -> "오늘의 말씀과 묵상"
    }

    fun orthodoxBibleEgypt(lang: AppLanguage): String = when (lang) {
        AppLanguage.ARABIC -> "الكتاب المقدس الأرثوذكسي المعتمد في مصر"
        AppLanguage.COPTIC -> "Ⲡⲓϫⲱⲙ ⲉⲑⲟⲩⲁⲃ ⲛ̀Ⲟⲣⲑⲟⲇⲟⲝⲟⲥ"
        AppLanguage.ENGLISH -> "Orthodox Holy Scripture (Egypt)"
        AppLanguage.FRENCH -> "Sainte Écriture Orthodoxe (Égypte)"
        AppLanguage.SPANISH -> "Santa Escritura Ortodoxa (Egipto)"
        AppLanguage.GERMAN -> "Orthodoxe Heilige Schrift (Ägypten)"
        AppLanguage.ITALIAN -> "Sacra Scrittura Ortodossa (Egitto)"
        AppLanguage.CHINESE -> "正教圣经权威译本"
        AppLanguage.JAPANESE -> "正教会公認聖書"
        AppLanguage.KOREAN -> "정교회 성경 권위역"
    }

    fun spiritualExplanation(lang: AppLanguage): String = when (lang) {
        AppLanguage.ARABIC -> "الشرح والتأمل الآبائي الموجز"
        AppLanguage.COPTIC -> "Ⲡⲓⲃⲱⲗ ⲛ̀ⲧⲉ ⲛⲓⲓⲟϯ"
        AppLanguage.ENGLISH -> "Patristic Commentary & Reflection"
        AppLanguage.FRENCH -> "Commentaire et réflexion patristique"
        AppLanguage.SPANISH -> "Comentario y reflexión patrística"
        AppLanguage.GERMAN -> "Patristischer Kommentar & Betrachtung"
        AppLanguage.ITALIAN -> "Commento e riflessione patristica"
        AppLanguage.CHINESE -> "教父注疏与灵修启发"
        AppLanguage.JAPANESE -> "教父の註解と省察"
        AppLanguage.KOREAN -> "교부 주석 및 묵상"
    }

    fun spiritualSoundsTitle(lang: AppLanguage): String = when (lang) {
        AppLanguage.ARABIC -> "الأصوات الروحية والألحان الكنسية للتنبيه"
        AppLanguage.COPTIC -> "Ⲛⲓϧⲣⲱⲟⲩ ⲉⲑⲟⲩⲁⲃ ⲛⲉⲙ ⲛⲓϩⲱⲥ"
        AppLanguage.ENGLISH -> "Spiritual Sounds & Hymns for Alarms"
        AppLanguage.FRENCH -> "Sons spirituels et hymnes pour alarmes"
        AppLanguage.SPANISH -> "Sonidos espirituales e himnos para alarmas"
        AppLanguage.GERMAN -> "Geistliche Klänge & Hymnen für Alarme"
        AppLanguage.ITALIAN -> "Suoni spirituali e inni per allarmi"
        AppLanguage.CHINESE -> "传统教会圣乐与灵修提示音"
        AppLanguage.JAPANESE -> "教会の聖なる音色とアラーム"
        AppLanguage.KOREAN -> "영적 성음 및 교회 성가 알람"
    }

    fun previewSound(lang: AppLanguage): String = when (lang) {
        AppLanguage.ARABIC -> "معاينة الصوت"
        AppLanguage.COPTIC -> "Ⲥⲱⲧⲉⲙ"
        AppLanguage.ENGLISH -> "Preview Sound"
        AppLanguage.FRENCH -> "Écouter"
        AppLanguage.SPANISH -> "Escuchar"
        AppLanguage.GERMAN -> "Anhören"
        AppLanguage.ITALIAN -> "Ascolta"
        AppLanguage.CHINESE -> "试听音效"
        AppLanguage.JAPANESE -> "音をプレビュー"
        AppLanguage.KOREAN -> "소리 미리듣기"
    }

    fun stopSound(lang: AppLanguage): String = when (lang) {
        AppLanguage.ARABIC -> "إيقاف"
        AppLanguage.COPTIC -> "Ⲙⲟⲛⲓ"
        AppLanguage.ENGLISH -> "Stop"
        AppLanguage.FRENCH -> "Arrêter"
        AppLanguage.SPANISH -> "Detener"
        AppLanguage.GERMAN -> "Stoppen"
        AppLanguage.ITALIAN -> "Ferma"
        AppLanguage.CHINESE -> "停止"
        AppLanguage.JAPANESE -> "停止"
        AppLanguage.KOREAN -> "정지"
    }

    fun selectAlarmTone(lang: AppLanguage): String = when (lang) {
        AppLanguage.ARABIC -> "اختر نغمة التنبيه الروحية"
        AppLanguage.COPTIC -> "Ⲥⲱⲧⲡ ⲙ̀ⲡⲓⲥⲟϩⲓ"
        AppLanguage.ENGLISH -> "Select Spiritual Alarm Tone"
        AppLanguage.FRENCH -> "Choisir la sonnerie spirituelle"
        AppLanguage.SPANISH -> "Seleccionar tono espiritual"
        AppLanguage.GERMAN -> "Geistlichen Alarmton wählen"
        AppLanguage.ITALIAN -> "Seleziona tono spirituale"
        AppLanguage.CHINESE -> "选择圣乐提醒铃声"
        AppLanguage.JAPANESE -> "アラーム音を選択"
        AppLanguage.KOREAN -> "영적 알람음 선택"
    }

    fun spiritualSoundsSubtitle(lang: AppLanguage): String = when (lang) {
        AppLanguage.ARABIC -> "استمع لمعاينة نغمات الأجراس القبطية والدُف والمثلث والناقوس الكنسي لتحديدها لكل صلاة:"
        AppLanguage.COPTIC -> "Ⲥⲱⲧⲉⲙ ⲉ̀ⲛⲓϧⲣⲱⲟⲩ ⲉⲑⲟⲩⲁⲃ ⲛ̀ⲧⲉ Ϯⲉⲕⲕⲗⲏⲥⲓⲁ:"
        AppLanguage.ENGLISH -> "Preview authentic Coptic bells, cymbals, triangle, and monastic wood chimes for your prayer reminders:"
        AppLanguage.FRENCH -> "Écoutez et choisissez parmi les cloches coptes, cymbales et carillons monastiques :"
        AppLanguage.SPANISH -> "Escucha y selecciona entre campanas coptas, címbalos y campanillas monásticas:"
        AppLanguage.GERMAN -> "Hören Sie sich koptische Glocken, Zimbeln und klösterliche Glockenspiele an:"
        AppLanguage.ITALIAN -> "Ascolta e seleziona campane copte, cembali e campane monastiche:"
        AppLanguage.CHINESE -> "试听科普特钟声、钹、三角铁与修道院木板声，为各时课选择铃声："
        AppLanguage.JAPANESE -> "コプト教会の鐘、シンバル、トライアングル、修道院の木板音を試聴して祈りごとに設定できます："
        AppLanguage.KOREAN -> "콥트 교회 종소리, 심벌즈, 트라이앵글, 수도원 목제 타종음을 미리듣고 설정하세요:"
    }

    fun cancel(lang: AppLanguage): String = when (lang) {
        AppLanguage.ARABIC -> "إلغاء"
        AppLanguage.COPTIC -> "Ⲭⲱ ⲉ̀ⲃⲟⲗ"
        AppLanguage.ENGLISH -> "Cancel"
        AppLanguage.FRENCH -> "Annuler"
        AppLanguage.SPANISH -> "Cancelar"
        AppLanguage.GERMAN -> "Abbrechen"
        AppLanguage.ITALIAN -> "Annulla"
        AppLanguage.CHINESE -> "取消"
        AppLanguage.JAPANESE -> "キャンセル"
        AppLanguage.KOREAN -> "취소"
    }

    fun holyGospel(lang: AppLanguage): String = when (lang) {
        AppLanguage.ARABIC -> "الإنجيل المقدس"
        AppLanguage.COPTIC -> "Ⲡⲓⲉⲩⲁⲅⲅⲉⲗⲓⲟⲛ ⲉⲑⲟⲩⲁⲃ"
        AppLanguage.ENGLISH -> "The Holy Gospel"
        AppLanguage.FRENCH -> "Le Saint Évangile"
        AppLanguage.SPANISH -> "El Santo Evangelio"
        AppLanguage.GERMAN -> "Das Heilige Evangelium"
        AppLanguage.ITALIAN -> "Il Santo Vangelo"
        AppLanguage.CHINESE -> "神圣福音"
        AppLanguage.JAPANESE -> "聖福音"
        AppLanguage.KOREAN -> "거룩한 복음"
    }

    fun holyGospelStand(lang: AppLanguage): String = when (lang) {
        AppLanguage.ARABIC -> "قفوا بخوف أمام الله لسماع الإنجيل المقدس"
        AppLanguage.COPTIC -> "Ⲟⲩⲁⲛⲁⲅⲛⲱⲥⲓⲥ ⲉ̀ⲃⲟⲗ ϧⲉⲛ ⲡⲓⲉⲩⲁⲅⲅⲉⲗⲓⲟⲛ ⲉⲑⲟⲩⲁⲃ"
        AppLanguage.ENGLISH -> "Stand in the fear of God, let us hear the Holy Gospel"
        AppLanguage.FRENCH -> "Levons-nous avec crainte de Dieu pour écouter le Saint Évangile"
        AppLanguage.SPANISH -> "De pie con temor de Dios para escuchar el Santo Evangelio"
        AppLanguage.GERMAN -> "Steht in Gottesfurcht auf, um das Heilige Evangelium zu hören"
        AppLanguage.ITALIAN -> "In piedi con timore di Dio per ascoltare il Santo Vangelo"
        AppLanguage.CHINESE -> "以敬畏神的心站立，恭听神圣福音"
        AppLanguage.JAPANESE -> "神を畏れ敬いて立ち、聖福音を聴きましょう"
        AppLanguage.KOREAN -> "하나님을 경외함으로 일어서서 거룩한 복음을 들읍시다"
    }

    fun gloryToGodForever(lang: AppLanguage): String = when (lang) {
        AppLanguage.ARABIC -> "والمجد لله دائماً أبدياً، آمين."
        AppLanguage.COPTIC -> "Ⲇⲟⲝⲁ ⲥⲓ Ⲕⲩⲣⲓⲉ ⲇⲟⲝⲁ ⲥⲓ."
        AppLanguage.ENGLISH -> "Glory to God forever. Amen."
        AppLanguage.FRENCH -> "Gloire à Dieu pour toujours. Amen."
        AppLanguage.SPANISH -> "Gloria a Dios por siempre. Amén."
        AppLanguage.GERMAN -> "Ehre sei Gott in Ewigkeit. Amen."
        AppLanguage.ITALIAN -> "Gloria a Dio per sempre. Amen."
        AppLanguage.CHINESE -> "荣耀归于上帝，直到永远。阿们。"
        AppLanguage.JAPANESE -> "神に栄光あれ、世々限りなく。アーメン。"
        AppLanguage.KOREAN -> "영원토록 하나님께 영광을. 아멘."
    }

    fun gospelFinishedPrompt(lang: AppLanguage): String = when (lang) {
        AppLanguage.ARABIC -> "أتممت قراءة الإنجيل المقدس"
        AppLanguage.COPTIC -> "Ⲁⲓϫⲱⲕ ⲉ̀ⲃⲟⲗ ⲙ̀ⲡⲓⲉⲩⲁⲅⲅⲉⲗⲓⲟⲛ"
        AppLanguage.ENGLISH -> "I finished reading the Holy Gospel"
        AppLanguage.FRENCH -> "J'ai terminé la lecture du Saint Évangile"
        AppLanguage.SPANISH -> "He terminado la lectura del Santo Evangelio"
        AppLanguage.GERMAN -> "Ich habe das Heilige Evangelium gelesen"
        AppLanguage.ITALIAN -> "Ho completato la lettura del Santo Vangelo"
        AppLanguage.CHINESE -> "我已读完神圣福音"
        AppLanguage.JAPANESE -> "聖福音の朗読を終えました"
        AppLanguage.KOREAN -> "거룩한 복음 읽기를 마쳤습니다"
    }

    fun recordPrayerNow(lang: AppLanguage): String = when (lang) {
        AppLanguage.ARABIC -> "سجّل الصلاة الآن تلقائياً"
        AppLanguage.COPTIC -> "Ⲥϧⲁⲓ ϯⲡⲣⲟⲥⲉⲩⲭⲏ ϯⲛⲟⲩ"
        AppLanguage.ENGLISH -> "Record Prayer Automatically Now"
        AppLanguage.FRENCH -> "Enregistrer la prière automatiquement maintenant"
        AppLanguage.SPANISH -> "Registrar la oración automáticamente ahora"
        AppLanguage.GERMAN -> "Gebet jetzt automatisch aufzeichnen"
        AppLanguage.ITALIAN -> "Registra la preghiera automaticamente ora"
        AppLanguage.CHINESE -> "立即自动记录此祈祷"
        AppLanguage.JAPANESE -> "今すぐ祈りを自動記録する"
        AppLanguage.KOREAN -> "지금 자동으로 기도 기록하기"
    }

    fun autoRecordedNotice(lang: AppLanguage): String = when (lang) {
        AppLanguage.ARABIC -> "يتعرف التطبيق تلقائياً على إتمامك للصلاة في موعدها بمجرد الوصول لقراءة الإنجيل المقدس وتسجيلها بالتاريخ والوقت الفعليين."
        AppLanguage.COPTIC -> "Ⲥⲛⲁⲥϧⲁⲓ ⲉ̀ⲡⲓⲥⲁϫⲓ ⲙ̀ⲡⲓⲉϩⲟⲟⲩ ⲛⲉⲙ ϯⲁϫⲡ ϧⲉⲛ ⲡϫⲱⲕ ⲉ̀ⲃⲟⲗ ⲙ̀ⲡⲓⲉⲩⲁⲅⲅⲉⲗⲓⲟⲛ."
        AppLanguage.ENGLISH -> "The app automatically detects that you prayed on time upon reading the Holy Gospel, logging the exact date & time."
        AppLanguage.FRENCH -> "L'application reconnaît automatiquement que vous avez prié à l'heure dès la lecture du Saint Évangile."
        AppLanguage.SPANISH -> "La aplicación reconoce automáticamente que oraste a tiempo al leer el Santo Evangelio."
        AppLanguage.GERMAN -> "Die App erkennt automatisch, dass Sie rechtzeitig gebetet haben, sobald Sie das Evangelium lesen."
        AppLanguage.ITALIAN -> "L'app riconosce automaticamente che hai pregato in tempo leggendo il Santo Vangelo."
        AppLanguage.CHINESE -> "当您阅读完神圣福音时，应用会自动记录您已准时完成祷告，并记录确切的日期和时间。"
        AppLanguage.JAPANESE -> "聖福音を朗読すると、アプリが時間通りの祈祷を自動認識し、正確な日時を記録します。"
        AppLanguage.KOREAN -> "거룩한 복음을 읽으면 앱이 정시 기도를 자동으로 인식하여 정확한 날짜와 시간을 기록합니다."
    }

    fun prayerRecordedSuccess(lang: AppLanguage): String = when (lang) {
        AppLanguage.ARABIC -> "✓ تم تسجيل الصلاة تلقائياً في موعدها بمجرد قراءة الإنجيل"
        AppLanguage.COPTIC -> "✓ Ⲁⲓⲧⲱⲃϩ - ⲁⲩⲥϧⲏⲧⲥ ϧⲉⲛ ⲡⲉⲥⲥⲏⲟⲩ"
        AppLanguage.ENGLISH -> "✓ Prayer Automatically Logged Upon Reading Gospel"
        AppLanguage.FRENCH -> "✓ Prière automatiquement enregistrée dès la lecture de l'Évangile"
        AppLanguage.SPANISH -> "✓ Oración registrada automáticamente al leer el Evangelio"
        AppLanguage.GERMAN -> "✓ Gebet beim Lesen des Evangeliums automatisch erfasst"
        AppLanguage.ITALIAN -> "✓ Preghiera registrata automaticamente leggendo il Vangelo"
        AppLanguage.CHINESE -> "✓ 阅读福音后已自动记录祈祷"
        AppLanguage.JAPANESE -> "✓ 福音朗読により祈りが自動記録されました"
        AppLanguage.KOREAN -> "✓ 복음 묵상 후 기도가 자동으로 기록되었습니다"
    }

    fun unmarkPrayerPrompt(lang: AppLanguage): String = when (lang) {
        AppLanguage.ARABIC -> "إلغاء التسجيل (لم أصلِّ)"
        AppLanguage.COPTIC -> "Ⲭⲱ ⲉ̀ⲃⲟⲗ"
        AppLanguage.ENGLISH -> "Unmark (Did not pray)"
        AppLanguage.FRENCH -> "Annuler l'enregistrement (pas prié)"
        AppLanguage.SPANISH -> "Desmarcar (no oré)"
        AppLanguage.GERMAN -> "Markierung aufheben (nicht gebetet)"
        AppLanguage.ITALIAN -> "Annulla registrazione (non ho pregato)"
        AppLanguage.CHINESE -> "取消标记（未祈祷）"
        AppLanguage.JAPANESE -> "取り消す（祈っていません）"
        AppLanguage.KOREAN -> "기록 취소 (기도하지 않음)"
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
        AppLanguage.CHINESE -> "视觉报表与图表"
        AppLanguage.JAPANESE -> "ビジュアル統計ダッシュボード"
        AppLanguage.KOREAN -> "시각적 대시보드 및 차트"
    }

    fun prayerFrequency(lang: AppLanguage): String = when (lang) {
        AppLanguage.ARABIC -> "معدل تكرار الصلوات"
        AppLanguage.COPTIC -> "Ⲧⲁϫⲣⲟ ⲛ̀ⲛⲓⲉⲩⲭⲏ"
        AppLanguage.ENGLISH -> "Prayer Frequency"
        AppLanguage.FRENCH -> "Fréquence des prières"
        AppLanguage.SPANISH -> "Frecuencia de oraciones"
        AppLanguage.GERMAN -> "Gebetshäufigkeit"
        AppLanguage.ITALIAN -> "Frequenza delle preghiere"
        AppLanguage.CHINESE -> "祷告频次"
        AppLanguage.JAPANESE -> "祈祷頻度"
        AppLanguage.KOREAN -> "기도 빈도"
    }

    fun dailyProgressChart(lang: AppLanguage): String = when (lang) {
        AppLanguage.ARABIC -> "مخطط السواعي وتوزيع الـ 24 ساعة"
        AppLanguage.COPTIC -> "Ⲕⲩⲕⲗⲟⲥ ⲛ̀ⲛⲓⲁϫⲡ"
        AppLanguage.ENGLISH -> "Daily Canonical Hours & 24h Timeline"
        AppLanguage.FRENCH -> "Heures canoniques quotidiennes & ligne de 24h"
        AppLanguage.SPANISH -> "Horas canónicas diarias y cronología de 24h"
        AppLanguage.GERMAN -> "Tägliche Horen & 24h-Zeitachse"
        AppLanguage.ITALIAN -> "Ore canoniche quotidiane e timeline 24h"
        AppLanguage.CHINESE -> "每日时课与24小时时间轴"
        AppLanguage.JAPANESE -> "時課と24時間タイムライン"
        AppLanguage.KOREAN -> "일일 시간경 및 24시간 타임라인"
    }

    fun monthlyProgressChart(lang: AppLanguage): String = when (lang) {
        AppLanguage.ARABIC -> "رسم بياني لتكرار الصلوات اليومي"
        AppLanguage.COPTIC -> "Ⲡⲓⲥⲱⲛⲧ ⲛ̀ⲧⲉ ⲡⲓⲁⲃⲟⲧ"
        AppLanguage.ENGLISH -> "Daily Prayer Frequency Bar Chart"
        AppLanguage.FRENCH -> "Graphique à barres de fréquence quotidienne"
        AppLanguage.SPANISH -> "Gráfico de barras de frecuencia diaria"
        AppLanguage.GERMAN -> "Balkendiagramm der täglichen Gebetshäufigkeit"
        AppLanguage.ITALIAN -> "Grafico a barre della frequenza giornaliera"
        AppLanguage.CHINESE -> "每日祷告频率柱状图"
        AppLanguage.JAPANESE -> "日別祈祷頻度バーチャート"
        AppLanguage.KOREAN -> "일일 기도 빈도 막대 그래프"
    }

    fun yearlyProgressChart(lang: AppLanguage): String = when (lang) {
        AppLanguage.ARABIC -> "منحنى التقدم الشهري عبر العام (12 شهراً)"
        AppLanguage.COPTIC -> "Ⲡⲓϫⲱⲕ ⲛ̀ϯⲣⲟⲙⲡⲓ ⲧⲏⲣⲥ"
        AppLanguage.ENGLISH -> "12-Month Annual Prayer Trajectory"
        AppLanguage.FRENCH -> "Courbe annuelle de progression sur 12 mois"
        AppLanguage.SPANISH -> "Curva anual de progreso de 12 meses"
        AppLanguage.GERMAN -> "12-Monats-Jahrestrendkurve"
        AppLanguage.ITALIAN -> "Curva di progressione annuale a 12 mesi"
        AppLanguage.CHINESE -> "12个月年度祈祷趋势走势图"
        AppLanguage.JAPANESE -> "12ヶ月の年間祈祷推移グラフ"
        AppLanguage.KOREAN -> "12개월 연간 기도 진행 추이"
    }

    fun calendarActivityHeatmap(lang: AppLanguage): String = when (lang) {
        AppLanguage.ARABIC -> "خريطة الالتزام الشهري (Activity Heatmap)"
        AppLanguage.COPTIC -> "Ⲡⲓⲙⲏⲏϣ ⲛ̀ⲛⲓⲉϩⲟⲟⲩ"
        AppLanguage.ENGLISH -> "Monthly Activity Heatmap"
        AppLanguage.FRENCH -> "Carte d'activité mensuelle"
        AppLanguage.SPANISH -> "Mapa de actividad mensual"
        AppLanguage.GERMAN -> "Monatliche Aktivitätskarte"
        AppLanguage.ITALIAN -> "Mappa delle attività mensili"
        AppLanguage.CHINESE -> "月度活跃度热力图"
        AppLanguage.JAPANESE -> "月間活動ヒートマップ"
        AppLanguage.KOREAN -> "월간 활동 히트맵"
    }

    fun prayerDistribution(lang: AppLanguage): String = when (lang) {
        AppLanguage.ARABIC -> "توزيع نسب الصلوات (Donut Chart)"
        AppLanguage.COPTIC -> "Ⲡⲓⲫⲱϣ ⲛ̀ⲧⲉ ⲛⲓⲁϫⲡ"
        AppLanguage.ENGLISH -> "Prayer Distribution Breakdown"
        AppLanguage.FRENCH -> "Répartition des heures de prière"
        AppLanguage.SPANISH -> "Distribución de las horas de oración"
        AppLanguage.GERMAN -> "Verteilung der Gebetszeiten"
        AppLanguage.ITALIAN -> "Distribuzione delle ore di preghiera"
        AppLanguage.CHINESE -> "各时课祷告比例分布"
        AppLanguage.JAPANESE -> "時課ごとの祈り分布"
        AppLanguage.KOREAN -> "기도별 비율 분포도"
    }

    fun consistencyScore(lang: AppLanguage): String = when (lang) {
        AppLanguage.ARABIC -> "نسبة الالتزام"
        AppLanguage.COPTIC -> "Ⲧⲁϫⲣⲟ"
        AppLanguage.ENGLISH -> "Consistency"
        AppLanguage.FRENCH -> "Régularité"
        AppLanguage.SPANISH -> "Constancia"
        AppLanguage.GERMAN -> "Beständigkeit"
        AppLanguage.ITALIAN -> "Costanza"
        AppLanguage.CHINESE -> "坚持度评分"
        AppLanguage.JAPANESE -> "継続達成度"
        AppLanguage.KOREAN -> "꾸준함 지수"
    }

    fun dailyAverage(lang: AppLanguage): String = when (lang) {
        AppLanguage.ARABIC -> "المعدل اليومي"
        AppLanguage.COPTIC -> "Ⲡⲓⲙⲏⲧⲣⲟⲛ ⲙ̀ⲙⲏⲛⲓ"
        AppLanguage.ENGLISH -> "Daily Average"
        AppLanguage.FRENCH -> "Moyenne quotidienne"
        AppLanguage.SPANISH -> "Promedio diario"
        AppLanguage.GERMAN -> "Tagesdurchschnitt"
        AppLanguage.ITALIAN -> "Media giornaliera"
        AppLanguage.CHINESE -> "日均祈祷次数"
        AppLanguage.JAPANESE -> "1日の平均祈祷回数"
        AppLanguage.KOREAN -> "일일 평균"
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
        AppLanguage.CHINESE -> "跨设备云端同步 (Cloud Sync)"
        AppLanguage.JAPANESE -> "マルチデバイス・クラウド同期"
        AppLanguage.KOREAN -> "기기 간 클라우드 동기화"
    }

    fun cloudSyncSubtitle(lang: AppLanguage): String = when (lang) {
        AppLanguage.ARABIC -> "مزامنة سجل الصلوات والإعدادات تلقائياً عبر جميع أجهزتك بواسطة Firestore"
        AppLanguage.COPTIC -> "Ⲥϧⲁⲓ ⲛⲓⲉⲩⲭⲏ ⲛⲉⲙ ⲛⲓⲣⲁⲛ ϧⲉⲛ ⲡⲓⲥⲩⲛⲭⲣⲟⲛ"
        AppLanguage.ENGLISH -> "Sync prayer history and settings across all your devices with Firestore"
        AppLanguage.FRENCH -> "Synchronisez l'historique des prières et les paramètres sur tous vos appareils"
        AppLanguage.SPANISH -> "Sincroniza el historial de oraciones y configuraciones en todos tus dispositivos"
        AppLanguage.GERMAN -> "Synchronisieren Sie Gebetsverlauf und Einstellungen auf allen Ihren Geräten"
        AppLanguage.ITALIAN -> "Sincronizza la cronologia delle preghiere e le impostazioni su tutti i dispositivi"
        AppLanguage.CHINESE -> "通过 Firestore 自动在您的所有设备间同步祷告历史和个性化设置"
        AppLanguage.JAPANESE -> "Firestoreにより、すべての端末で祈りの履歴と設定を自動同期します"
        AppLanguage.KOREAN -> "Firestore를 통해 모든 기기에서 기도 기록 및 설정을 자동으로 동기화합니다"
    }

    fun syncStatusSynced(lang: AppLanguage): String = when (lang) {
        AppLanguage.ARABIC -> "متزامن سحابياً بالكامل (Firestore)"
        AppLanguage.COPTIC -> "Ⲁⲩⲧⲱⲙ ⲉ̀ⲃⲟⲗ"
        AppLanguage.ENGLISH -> "All Synced via Firestore"
        AppLanguage.FRENCH -> "Entièrement synchronisé (Firestore)"
        AppLanguage.SPANISH -> "Completamente sincronizado (Firestore)"
        AppLanguage.GERMAN -> "Vollständig synchronisiert (Firestore)"
        AppLanguage.ITALIAN -> "Completamente sincronizzato (Firestore)"
        AppLanguage.CHINESE -> "已全部云同步 (Firestore)"
        AppLanguage.JAPANESE -> "クラウド同期完了 (Firestore)"
        AppLanguage.KOREAN -> "클라우드 동기화 완료 (Firestore)"
    }

    fun syncStatusSyncing(lang: AppLanguage): String = when (lang) {
        AppLanguage.ARABIC -> "جارٍ المزامنة السحابية..."
        AppLanguage.COPTIC -> "Ⲉϥⲧⲱⲙ..."
        AppLanguage.ENGLISH -> "Syncing with Cloud..."
        AppLanguage.FRENCH -> "Synchronisation en cours..."
        AppLanguage.SPANISH -> "Sincronizando con la nube..."
        AppLanguage.GERMAN -> "Wird synchronisiert..."
        AppLanguage.ITALIAN -> "Sincronizzazione in corso..."
        AppLanguage.CHINESE -> "正在同步至云端..."
        AppLanguage.JAPANESE -> "クラウドと同期中..."
        AppLanguage.KOREAN -> "클라우드 동기화 중..."
    }

    fun syncStatusOffline(lang: AppLanguage): String = when (lang) {
        AppLanguage.ARABIC -> "الوضع المحلي (حفظ على هذا الجهاز)"
        AppLanguage.COPTIC -> "Ϧⲉⲛ ⲡⲁⲓⲥⲏⲟⲩ"
        AppLanguage.ENGLISH -> "Local Storage Mode"
        AppLanguage.FRENCH -> "Mode stockage local"
        AppLanguage.SPANISH -> "Modo de almacenamiento local"
        AppLanguage.GERMAN -> "Lokaler Speichermodus"
        AppLanguage.ITALIAN -> "Modalità memoria locale"
        AppLanguage.CHINESE -> "本地存储模式（保存在本机）"
        AppLanguage.JAPANESE -> "ローカル保存モード"
        AppLanguage.KOREAN -> "로컬 저장 모드 (이 기기에 저장)"
    }

    fun syncNow(lang: AppLanguage): String = when (lang) {
        AppLanguage.ARABIC -> "مزامنة الآن"
        AppLanguage.COPTIC -> "Ⲧⲱⲙ ϯⲛⲟⲩ"
        AppLanguage.ENGLISH -> "Sync Now"
        AppLanguage.FRENCH -> "Synchroniser"
        AppLanguage.SPANISH -> "Sincronizar ahora"
        AppLanguage.GERMAN -> "Jetzt synchronisieren"
        AppLanguage.ITALIAN -> "Sincronizza ora"
        AppLanguage.CHINESE -> "立即同步"
        AppLanguage.JAPANESE -> "今すぐ同期"
        AppLanguage.KOREAN -> "지금 동기화"
    }

    fun deviceSyncKey(lang: AppLanguage): String = when (lang) {
        AppLanguage.ARABIC -> "مفتاح المزامنة (Sync Key)"
        AppLanguage.COPTIC -> "Ⲡⲓϣⲟϣⲧ ⲛ̀ⲧⲉ ⲡⲓⲧⲱⲙ"
        AppLanguage.ENGLISH -> "Device Sync Key"
        AppLanguage.FRENCH -> "Clé de synchronisation"
        AppLanguage.SPANISH -> "Clave de sincronización"
        AppLanguage.GERMAN -> "Sync-Schlüssel"
        AppLanguage.ITALIAN -> "Chiave di sincronizzazione"
        AppLanguage.CHINESE -> "设备同步密钥 (Sync Key)"
        AppLanguage.JAPANESE -> "デバイス同期キー"
        AppLanguage.KOREAN -> "기기 동기화 키 (Sync Key)"
    }

    fun syncKeyDescription(lang: AppLanguage): String = when (lang) {
        AppLanguage.ARABIC -> "استخدم نفس هذا المفتاح في هاتفك الآخر أو جهاز التابلت لربط السجل والصلوات تلقائياً."
        AppLanguage.COPTIC -> "Ⲕⲱ ⲙ̀ⲡⲁⲓϣⲟϣⲧ ϧⲉⲛ ⲛⲓⲥⲩⲥⲕⲉⲩⲏ ⲧⲏⲣⲟⲩ."
        AppLanguage.ENGLISH -> "Use this same key on your other phone or tablet to link your prayer history automatically."
        AppLanguage.FRENCH -> "Utilisez cette même clé sur vos autres appareils pour lier automatiquement votre historique."
        AppLanguage.SPANISH -> "Usa esta misma clave en tus otros dispositivos para vincular tu historial automáticamente."
        AppLanguage.GERMAN -> "Verwenden Sie denselben Schlüssel auf Ihren anderen Geräten für die automatische Verknüpfung."
        AppLanguage.ITALIAN -> "Usa questa stessa chiave sui tuoi altri dispositivi per collegare automaticamente la cronologia."
        AppLanguage.CHINESE -> "在您的其他手机或平板电脑上使用此相同密钥，即可自动关联同步祈祷历史。"
        AppLanguage.JAPANESE -> "他のスマートフォンやタブレットで同じキーを使用すると、祈りの履歴が自動的に連携されます。"
        AppLanguage.KOREAN -> "다른 스마트폰이나 태블릿에서 이 키를 입력하면 기도 기록이 자동으로 연동됩니다."
    }

    fun linkAnotherDevice(lang: AppLanguage): String = when (lang) {
        AppLanguage.ARABIC -> "ربط بجهاز آخر بواسطة مفتاح"
        AppLanguage.COPTIC -> "Ⲧⲱⲙ ⲛⲉⲙ ⲕⲉⲥⲩⲥⲕⲉⲩⲏ"
        AppLanguage.ENGLISH -> "Link with another Device Key"
        AppLanguage.FRENCH -> "Lier avec une autre clé"
        AppLanguage.SPANISH -> "Vincular con otra clave"
        AppLanguage.GERMAN -> "Mit anderem Schlüssel verknüpfen"
        AppLanguage.ITALIAN -> "Collega con un'altra chiave"
        AppLanguage.CHINESE -> "通过密钥关联其他设备"
        AppLanguage.JAPANESE -> "別のデバイスキーと連携"
        AppLanguage.KOREAN -> "다른 기기 키와 연동하기"
    }

    fun enterSyncKeyPrompt(lang: AppLanguage): String = when (lang) {
        AppLanguage.ARABIC -> "أدخل مفتاح المزامنة الخاص بجهازك الآخر للربط ومشاركة السجل:"
        AppLanguage.COPTIC -> "Ⲕⲱ ⲙ̀ⲡⲓϣⲟϣⲧ ⲛ̀ⲧⲉ ⲕⲉⲥⲩⲥⲕⲉⲩⲏ:"
        AppLanguage.ENGLISH -> "Enter the Sync Key from your other device to link and share history:"
        AppLanguage.FRENCH -> "Entrez la clé de synchronisation de votre autre appareil :"
        AppLanguage.SPANISH -> "Ingresa la clave de sincronización de tu otro dispositivo:"
        AppLanguage.GERMAN -> "Geben Sie den Sync-Schlüssel Ihres anderen Geräts ein:"
        AppLanguage.ITALIAN -> "Inserisci la chiave di sincronizzazione dell'altro dispositivo:"
        AppLanguage.CHINESE -> "输入另一台设备的同步密钥以同步并共享祈祷历史："
        AppLanguage.JAPANESE -> "同期して履歴を共有するため、別の端末の同期キーを入力してください："
        AppLanguage.KOREAN -> "다른 기기의 동기화 키를 입력하여 기도 기록을 연결하고 공유하세요:"
    }

    fun saveAndSync(lang: AppLanguage): String = when (lang) {
        AppLanguage.ARABIC -> "حفظ ومزامنة"
        AppLanguage.COPTIC -> "Ⲥϧⲁⲓ ⲛⲉⲙ ⲧⲱⲙ"
        AppLanguage.ENGLISH -> "Save & Sync"
        AppLanguage.FRENCH -> "Enregistrer et synchroniser"
        AppLanguage.SPANISH -> "Guardar y sincronizar"
        AppLanguage.GERMAN -> "Speichern & Synchronisieren"
        AppLanguage.ITALIAN -> "Salva e sincronizza"
        AppLanguage.CHINESE -> "保存并同步"
        AppLanguage.JAPANESE -> "保存して同期"
        AppLanguage.KOREAN -> "저장 및 동기화"
    }

    fun vibrationAndSoundTitle(lang: AppLanguage): String = when (lang) {
        AppLanguage.ARABIC -> "التنبيه بالاهتزاز والصوت معاً"
        AppLanguage.COPTIC -> "Ⲡⲓⲙⲟⲛⲙⲉⲛ ⲛⲉⲙ ⲡⲓϧⲣⲱⲟⲩ ⲉⲩⲥⲟⲡ"
        AppLanguage.ENGLISH -> "Vibration & Sound Alert Together"
        AppLanguage.FRENCH -> "Vibration et sonnerie ensemble"
        AppLanguage.SPANISH -> "Alerta por vibración y sonido juntos"
        AppLanguage.GERMAN -> "Vibration und Ton zusammen"
        AppLanguage.ITALIAN -> "Vibrazione e suono insieme"
        AppLanguage.CHINESE -> "振动与声音同步提醒"
        AppLanguage.JAPANESE -> "バイブレーションと音による同時アラーム"
        AppLanguage.KOREAN -> "진동 및 소리 동시 알림"
    }

    fun vibrationAndSoundDescription(lang: AppLanguage): String = when (lang) {
        AppLanguage.ARABIC -> "يعمل الاهتزاز الإيقاعي جنباً إلى جنب مع الصوت الروحاني لضمان تنبيهك لموعد الصلاة حتى في الأماكن الهادئة أو عند خفض الصوت."
        AppLanguage.COPTIC -> "Ⲡⲓⲙⲟⲛⲙⲉⲛ ϣⲁϥⲉⲣϩⲱⲃ ⲛⲉⲙ ⲡⲓϧⲣⲱⲟⲩ ⲉ̀ⲑⲣⲉⲕⲛⲁϩϣ ⲉ̀ϯⲡⲣⲟⲥⲉⲩⲭⲏ."
        AppLanguage.ENGLISH -> "Rhythmic vibration pulses alongside the sacred chime ensure you never miss your canonical prayer time, even in silent mode."
        AppLanguage.FRENCH -> "Les vibrations rythmiques associées au carillon spirituel vous assurent de ne jamais manquer l'heure de la prière."
        AppLanguage.SPANISH -> "La vibración rítmica junto con el sonido sagrado asegura que no te pierdas la hora de la oración canónica."
        AppLanguage.GERMAN -> "Rhythmische Vibration zusammen mit dem geistlichen Ton erinnert Sie pünktlich an das Stundengebet."
        AppLanguage.ITALIAN -> "La vibrazione ritmica insieme al suono spirituale assicura di non perdere mai l'ora della preghiera."
        AppLanguage.CHINESE -> "富有韵律的振动脉冲与神圣铃声协同生效，确保您在静音或喧闹环境中都不会错过时课祷告。"
        AppLanguage.JAPANESE -> "聖なるチャイムとリズミカルな振動が連携し、マナーモード時でも祈りの時間を確実に通知します。"
        AppLanguage.KOREAN -> "영적인 성음과 리드미컬한 진동이 함께 작동하여 무음 모드에서도 시간경 기도를 놓치지 않도록 알려줍니다."
    }

    fun enableVibrationAll(lang: AppLanguage): String = when (lang) {
        AppLanguage.ARABIC -> "تفعيل الاهتزاز للكل"
        AppLanguage.COPTIC -> "Ⲧⲁϩⲟ ⲙ̀ⲡⲓⲙⲟⲛⲙⲉⲛ ⲉ̀ⲡⲧⲏⲣϥ"
        AppLanguage.ENGLISH -> "Enable Vibrate for All"
        AppLanguage.FRENCH -> "Activer vibration pour tous"
        AppLanguage.SPANISH -> "Activar vibración para todos"
        AppLanguage.GERMAN -> "Vibration für alle aktivieren"
        AppLanguage.ITALIAN -> "Attiva vibrazione per tutti"
        AppLanguage.CHINESE -> "一键开启所有时课振动"
        AppLanguage.JAPANESE -> "すべての時課で振動を有効化"
        AppLanguage.KOREAN -> "모든 기도에 진동 활성화"
    }

    fun disableVibrationAll(lang: AppLanguage): String = when (lang) {
        AppLanguage.ARABIC -> "إيقاف الاهتزاز للكل"
        AppLanguage.COPTIC -> "Ⲱϣⲉⲙ ⲙ̀ⲡⲓⲙⲟⲛⲙⲉⲛ"
        AppLanguage.ENGLISH -> "Mute Vibration for All"
        AppLanguage.FRENCH -> "Couper vibration pour tous"
        AppLanguage.SPANISH -> "Desactivar vibración para todos"
        AppLanguage.GERMAN -> "Vibration für alle stummschalten"
        AppLanguage.ITALIAN -> "Disattiva vibrazione per tutti"
        AppLanguage.CHINESE -> "一键关闭所有时课振动"
        AppLanguage.JAPANESE -> "すべての時課で振動を無効化"
        AppLanguage.KOREAN -> "모든 기도에 진동 비활성화"
    }

    fun enableSoundAll(lang: AppLanguage): String = when (lang) {
        AppLanguage.ARABIC -> "تفعيل الصوت للكل"
        AppLanguage.COPTIC -> "Ⲧⲁϩⲟ ⲙ̀ⲡⲓϧⲣⲱⲟⲩ ⲉ̀ⲡⲧⲏⲣϥ"
        AppLanguage.ENGLISH -> "Enable Sound for All"
        AppLanguage.FRENCH -> "Activer son pour tous"
        AppLanguage.SPANISH -> "Activar sonido para todos"
        AppLanguage.GERMAN -> "Ton für alle aktivieren"
        AppLanguage.ITALIAN -> "Attiva suono per tutti"
        AppLanguage.CHINESE -> "一键开启所有时课声音"
        AppLanguage.JAPANESE -> "すべての時課で音を有効化"
        AppLanguage.KOREAN -> "모든 기도에 소리 활성화"
    }

    fun disableSoundAll(lang: AppLanguage): String = when (lang) {
        AppLanguage.ARABIC -> "إيقاف الصوت للكل"
        AppLanguage.COPTIC -> "Ⲱϣⲉⲙ ⲙ̀ⲡⲓϧⲣⲱⲟⲩ"
        AppLanguage.ENGLISH -> "Mute Sound for All"
        AppLanguage.FRENCH -> "Couper le son pour tous"
        AppLanguage.SPANISH -> "Desactivar sonido para todos"
        AppLanguage.GERMAN -> "Ton für alle stummschalten"
        AppLanguage.ITALIAN -> "Disattiva suono per tutti"
        AppLanguage.CHINESE -> "一键静音所有时课声音"
        AppLanguage.JAPANESE -> "すべての時課で音をミュート"
        AppLanguage.KOREAN -> "모든 기도에 소리 음소거"
    }

    fun soundAndVibrationMode(lang: AppLanguage): String = when (lang) {
        AppLanguage.ARABIC -> "صوت واهتزاز"
        AppLanguage.COPTIC -> "Ϧⲣⲱⲟⲩ + Ⲙⲟⲛⲙⲉⲛ"
        AppLanguage.ENGLISH -> "Sound & Vibrate"
        AppLanguage.FRENCH -> "Son & Vibreur"
        AppLanguage.SPANISH -> "Sonido y Vibración"
        AppLanguage.GERMAN -> "Ton & Vibration"
        AppLanguage.ITALIAN -> "Suono e Vibrazione"
        AppLanguage.CHINESE -> "声音 + 振动"
        AppLanguage.JAPANESE -> "音と振動"
        AppLanguage.KOREAN -> "소리 및 진동"
    }

    fun vibrationOnlyMode(lang: AppLanguage): String = when (lang) {
        AppLanguage.ARABIC -> "اهتزاز فقط (صامت)"
        AppLanguage.COPTIC -> "Ⲙⲟⲛⲙⲉⲛ ⲙ̀ⲙⲁⲩⲁⲧϥ"
        AppLanguage.ENGLISH -> "Vibrate Only (Silent)"
        AppLanguage.FRENCH -> "Vibreur seul (Silencieux)"
        AppLanguage.SPANISH -> "Solo vibración (Silencio)"
        AppLanguage.GERMAN -> "Nur Vibration (Stumm)"
        AppLanguage.ITALIAN -> "Solo vibrazione (Silenzioso)"
        AppLanguage.CHINESE -> "仅振动（静音模式）"
        AppLanguage.JAPANESE -> "バイブレーションのみ（マナー）"
        AppLanguage.KOREAN -> "진동만 (무음 모드)"
    }

    fun soundOnlyMode(lang: AppLanguage): String = when (lang) {
        AppLanguage.ARABIC -> "صوت فقط"
        AppLanguage.COPTIC -> "Ϧⲣⲱⲟⲩ ⲙ̀ⲙⲁⲩⲁⲧϥ"
        AppLanguage.ENGLISH -> "Sound Only"
        AppLanguage.FRENCH -> "Son seul"
        AppLanguage.SPANISH -> "Solo sonido"
        AppLanguage.GERMAN -> "Nur Ton"
        AppLanguage.ITALIAN -> "Solo suono"
        AppLanguage.CHINESE -> "仅声音"
        AppLanguage.JAPANESE -> "音のみ"
        AppLanguage.KOREAN -> "소리만"
    }

    fun completelyMuteMode(lang: AppLanguage): String = when (lang) {
        AppLanguage.ARABIC -> "إشعار صامت بدون صوت أو اهتزاز"
        AppLanguage.COPTIC -> "Ⲭⲁⲣⲱϥ ⲉ̀ⲡⲧⲏⲣϥ"
        AppLanguage.ENGLISH -> "Silent Notifications (No Sound/Vibration)"
        AppLanguage.FRENCH -> "Silencieux (Sans son ni vibration)"
        AppLanguage.SPANISH -> "Silencioso (Sin sonido ni vibración)"
        AppLanguage.GERMAN -> "Stumm (Kein Ton/Keine Vibration)"
        AppLanguage.ITALIAN -> "Silenzioso (Nessun suono né vibrazione)"
        AppLanguage.CHINESE -> "完全静音（无声音与振动）"
        AppLanguage.JAPANESE -> "完全サイレント（音・振動なし）"
        AppLanguage.KOREAN -> "완전 무음 (소리/진동 없음)"
    }

    fun authWelcomeTitle(lang: AppLanguage): String = when (lang) {
        AppLanguage.ARABIC -> "الأجبية المقدسة - كتاب صلوات السواعي"
        AppLanguage.COPTIC -> "Ϯⲁ̀ϫⲡⲓⲁ ⲉⲑⲟⲩⲁⲃ - Ⲡⲓϫⲱⲙ ⲛ̀ⲧⲉ ⲛⲓⲁ̀ϫⲡ"
        AppLanguage.ENGLISH -> "Holy Agpeya - Book of Canonical Hours"
        AppLanguage.FRENCH -> "Sainte Agpeya - Livre des Heures Canoniales"
        AppLanguage.SPANISH -> "Santa Agpeya - Libro de las Horas Canónicas"
        AppLanguage.GERMAN -> "Heilige Agpeya - Stundengebetbuch"
        AppLanguage.ITALIAN -> "Santa Agpeya - Libro delle Ore Canoniche"
        AppLanguage.CHINESE -> "科普特东正教圣时课经 (Agpeya)"
        AppLanguage.JAPANESE -> "コプト正教会 時課の祈り (アグペヤ)"
        AppLanguage.KOREAN -> "콥트 정교회 아그페야 시간경 기도서"
    }

    fun authWelcomeSubtitle(lang: AppLanguage): String = when (lang) {
        AppLanguage.ARABIC -> "صلوات السواعي القبطية الأرثوذكسية والمزامنة السحابية الفورية"
        AppLanguage.COPTIC -> "Ⲛⲓⲉⲩⲭⲏ ⲉⲑⲟⲩⲁⲃ ⲛ̀ⲧⲉ ϯⲉⲕⲕⲗⲏⲥⲓⲁ ⲛ̀ⲣⲉⲙⲛ̀ⲭⲏⲙⲓ ⲛ̀ⲟⲣⲑⲟⲇⲟⲝⲟⲥ"
        AppLanguage.ENGLISH -> "Coptic Orthodox Canonical Hours & Instant Cloud Prayer Sync"
        AppLanguage.FRENCH -> "Prières canoniales coptes orthodoxes et synchronisation cloud instantanée"
        AppLanguage.SPANISH -> "Oraciones canónicas coptas ortodoxas y sincronización en la nube al instante"
        AppLanguage.GERMAN -> "Koptisch-orthodoxe Stundengebete und sofortige Cloud-Synchronisierung"
        AppLanguage.ITALIAN -> "Preghiere canoniche copte ortodosse e sincronizzazione cloud istantanea"
        AppLanguage.CHINESE -> "科普特东正教七次日课与多设备即时云端祈祷同步"
        AppLanguage.JAPANESE -> "コプト正教会の日課の祈りとマルチデバイス即時クラウド同期"
        AppLanguage.KOREAN -> "콥트 정교회 7대 시간경 기도 및 기기 간 실시간 클라우드 동기화"
    }

    fun stepSelectLanguage(lang: AppLanguage): String = when (lang) {
        AppLanguage.ARABIC -> "الخطوة الأولى: اختر لغة التطبيق"
        AppLanguage.COPTIC -> "Ⲡⲓⲧⲁϫⲣⲟ ⲁ̅: Ⲥⲱⲧⲡ ⲛ̀ϯⲁⲥⲡⲓ"
        AppLanguage.ENGLISH -> "Step 1: Choose Your Language"
        AppLanguage.FRENCH -> "Étape 1 : Choisissez votre langue"
        AppLanguage.SPANISH -> "Paso 1: Seleccione su idioma"
        AppLanguage.GERMAN -> "Schritt 1: Sprache auswählen"
        AppLanguage.ITALIAN -> "Passo 1: Scegli la tua lingua"
        AppLanguage.CHINESE -> "第一步：选择应用与祷告语言"
        AppLanguage.JAPANESE -> "ステップ 1：言語を選択してください"
        AppLanguage.KOREAN -> "1단계: 기도 및 앱 언어 선택"
    }

    fun stepAccountSync(lang: AppLanguage): String = when (lang) {
        AppLanguage.ARABIC -> "الخطوة الثانية: تسجيل البريد لحفظ الصلوات سحابياً"
        AppLanguage.COPTIC -> "Ⲡⲓⲧⲁϫⲣⲟ ⲃ̅: Ⲧⲱⲙⲧ ⲙ̀ⲡⲉⲕ-Email ⲉ̀ϯⲡⲉ"
        AppLanguage.ENGLISH -> "Step 2: Sign In with Email to Sync Prayers Online"
        AppLanguage.FRENCH -> "Étape 2 : Connectez-vous avec un e-mail pour synchroniser vos prières"
        AppLanguage.SPANISH -> "Paso 2: Iniciar sesión con correo para sincronizar oraciones"
        AppLanguage.GERMAN -> "Schritt 2: Mit E-Mail anmelden, um Gebete online zu sichern"
        AppLanguage.ITALIAN -> "Passo 2: Accedi con l'e-mail per sincronizzare le preghiere online"
        AppLanguage.CHINESE -> "第二步：输入电子邮箱以在云端保存与同步祷告记录"
        AppLanguage.JAPANESE -> "ステップ 2：メールでログインし祈りの記録をクラウド同期"
        AppLanguage.KOREAN -> "2단계: 이메일로 로그인하여 기도 기록을 온라인에 동기화"
    }

    fun authEmailDescription(lang: AppLanguage): String = when (lang) {
        AppLanguage.ARABIC -> "سجّل بريدك الإلكتروني لحفظ صلواتك على الإنترنت. عند فتح الأجبية من أي هاتف أو جهاز آخر بنفس البريد، ستجد سجل صلواتك ومواعيدك وتقاريرك مسجلة تلقائياً."
        AppLanguage.COPTIC -> "Ⲥϧⲁⲓ ⲙ̀ⲡⲉⲕ-Email ⲉ̀ⲑⲣⲉ ⲛⲉⲕⲉⲩⲭⲏ ⲙⲟⲩⲛ ⲉ̀ⲃⲟⲗ ϧⲉⲛ ⲛⲓⲙⲁ ⲧⲏⲣⲟⲩ."
        AppLanguage.ENGLISH -> "Enter your email to save your prayers online. When you open the Agpeya on any other device with this email, your prayer records, alarms, and history sync automatically."
        AppLanguage.FRENCH -> "Enregistrez votre e-mail pour sauvegarder vos prières en ligne. Si vous ouvrez l'Agpeya sur un autre appareil, vos prières et rapports seront automatiquement restaurés."
        AppLanguage.SPANISH -> "Ingrese su correo para guardar sus oraciones en la nube. Al abrir la Agpeya desde cualquier otro dispositivo con este correo, sus datos se sincronizan automáticamente."
        AppLanguage.GERMAN -> "Geben Sie Ihre E-Mail ein, um Gebete online zu speichern. Wenn Sie die Agpeya auf einem anderen Gerät mit derselben E-Mail öffnen, werden alle Gebete automatisch synchronisiert."
        AppLanguage.ITALIAN -> "Inserisci la tua e-mail per salvare le preghiere online. Aprendo l'Agpeya da qualsiasi altro dispositivo, le tue preghiere e la cronologia si sincronizzeranno automaticamente."
        AppLanguage.CHINESE -> "输入您的邮箱以在云端同步祷告记录。无论在任何手机、平板或新设备上使用此邮箱登录，您的祈祷历史、闹钟设置和图表都将自动恢复。"
        AppLanguage.JAPANESE -> "メールアドレスを登録すると祈りの記録がオンラインに保存されます。他のスマートフォンや端末で同じメールで開いても、祈り・アラーム・履歴が自動同期されます。"
        AppLanguage.KOREAN -> "이메일을 등록하여 기도 기록을 온라인에 저장하세요. 다른 휴대폰이나 기기에서 동일한 이메일로 앱을 열면 기도 기록과 알람 설정이 자동으로 복원됩니다."
    }

    fun emailAddressLabel(lang: AppLanguage): String = when (lang) {
        AppLanguage.ARABIC -> "البريد الإلكتروني"
        AppLanguage.COPTIC -> "Ⲡⲓ-Email"
        AppLanguage.ENGLISH -> "Email Address"
        AppLanguage.FRENCH -> "Adresse e-mail"
        AppLanguage.SPANISH -> "Correo electrónico"
        AppLanguage.GERMAN -> "E-Mail-Adresse"
        AppLanguage.ITALIAN -> "Indirizzo e-mail"
        AppLanguage.CHINESE -> "电子邮箱地址"
        AppLanguage.JAPANESE -> "メールアドレス"
        AppLanguage.KOREAN -> "이메일 주소"
    }

    fun passwordOptionalLabel(lang: AppLanguage): String = when (lang) {
        AppLanguage.ARABIC -> "كلمة المرور (اختياري لحماية حسابك)"
        AppLanguage.COPTIC -> "Ⲡⲓ-Password (ⲕⲁⲧⲁ ⲡⲉⲕⲟⲩⲱϣ)"
        AppLanguage.ENGLISH -> "Password (Optional / for account protection)"
        AppLanguage.FRENCH -> "Mot de passe (Facultatif / pour la protection)"
        AppLanguage.SPANISH -> "Contraseña (Opcional / para protección de la cuenta)"
        AppLanguage.GERMAN -> "Passwort (Optional / zum Kontoschutz)"
        AppLanguage.ITALIAN -> "Password (Facoltativa / per proteggere l'account)"
        AppLanguage.CHINESE -> "密码（可选 / 用于保护您的云端账户）"
        AppLanguage.JAPANESE -> "パスワード（任意 / アカウント保護用）"
        AppLanguage.KOREAN -> "비밀번호 (선택사항 / 계정 보호용)"
    }

    fun signInAndSyncButton(lang: AppLanguage): String = when (lang) {
        AppLanguage.ARABIC -> "تسجيل الدخول والمزامنة السحابية"
        AppLanguage.COPTIC -> "Ϣⲱⲡ ⲉ̀ϧⲟⲩⲛ ⲟⲩⲟϩ ⲧⲱⲙⲧ ⲉ̀ϯⲡⲉ"
        AppLanguage.ENGLISH -> "Sign In & Sync Across Devices"
        AppLanguage.FRENCH -> "Se connecter et synchroniser sur le Cloud"
        AppLanguage.SPANISH -> "Iniciar sesión y sincronizar en la nube"
        AppLanguage.GERMAN -> "Anmelden & Geräte synchronisieren"
        AppLanguage.ITALIAN -> "Accedi e sincronizza sul Cloud"
        AppLanguage.CHINESE -> "登录并启用多设备云端同步"
        AppLanguage.JAPANESE -> "ログインしてクラウド同期を開始"
        AppLanguage.KOREAN -> "로그인 및 클라우드 동기화 시작"
    }

    fun continueAsGuestButton(lang: AppLanguage): String = when (lang) {
        AppLanguage.ARABIC -> "المتابعة كضيف (بدون مزامنة سحابية)"
        AppLanguage.COPTIC -> "Ⲙⲟϣⲓ ⲙ̀ⲫⲣⲏϯ ⲛ̀ⲟⲩϣⲉⲙⲙⲟ"
        AppLanguage.ENGLISH -> "Continue as Guest (Offline only)"
        AppLanguage.FRENCH -> "Continuer comme invité (Mode local)"
        AppLanguage.SPANISH -> "Continuar como invitado (Sin sincronización)"
        AppLanguage.GERMAN -> "Als Gast fortfahren (Nur lokal)"
        AppLanguage.ITALIAN -> "Continua come ospite (Solo locale)"
        AppLanguage.CHINESE -> "以访客身份继续（仅本地离线）"
        AppLanguage.JAPANESE -> "ゲストとして続行（ローカルのみ）"
        AppLanguage.KOREAN -> "게스트로 계속하기 (오프라인 전용)"
    }

    fun accountLinkedTitle(lang: AppLanguage): String = when (lang) {
        AppLanguage.ARABIC -> "الحساب السحابي المتصل"
        AppLanguage.COPTIC -> "Ⲡⲓⲧⲱⲙⲧ ⲉ̀ϯⲡⲉ ⲉⲧⲧⲁϫⲣⲏⲟⲩⲧ"
        AppLanguage.ENGLISH -> "Connected Cloud Account"
        AppLanguage.FRENCH -> "Compte Cloud connecté"
        AppLanguage.SPANISH -> "Cuenta en la nube conectada"
        AppLanguage.GERMAN -> "Verbundenes Cloud-Konto"
        AppLanguage.ITALIAN -> "Account Cloud connesso"
        AppLanguage.CHINESE -> "已连接的云端同步账户"
        AppLanguage.JAPANESE -> "接続されたクラウド同期アカウント"
        AppLanguage.KOREAN -> "연결된 클라우드 동기화 계정"
    }

    fun switchAccountButton(lang: AppLanguage): String = when (lang) {
        AppLanguage.ARABIC -> "تبديل الحساب أو تغيير البريد"
        AppLanguage.COPTIC -> "Ϣⲓⲃϯ ⲙ̀ⲡⲓ-Email"
        AppLanguage.ENGLISH -> "Switch Account / Change Email"
        AppLanguage.FRENCH -> "Changer de compte / d'e-mail"
        AppLanguage.SPANISH -> "Cambiar de cuenta / de correo"
        AppLanguage.GERMAN -> "Konto wechseln / E-Mail ändern"
        AppLanguage.ITALIAN -> "Cambia account / e-mail"
        AppLanguage.CHINESE -> "切换账户或更换邮箱"
        AppLanguage.JAPANESE -> "アカウントの切り替え / メール変更"
        AppLanguage.KOREAN -> "계정 전환 / 이메일 변경"
    }

    fun emailRequiredPrompt(lang: AppLanguage): String = when (lang) {
        AppLanguage.ARABIC -> "يرجى إدخال عنوان بريد إلكتروني صالح للمزامنة"
        AppLanguage.COPTIC -> "Ⲁⲣⲓϩⲙⲟⲧ ⲥϧⲁⲓ ⲟⲩ-Email ⲉϥⲧⲁϫⲣⲏⲟⲩⲧ"
        AppLanguage.ENGLISH -> "Please enter a valid email address for synchronization"
        AppLanguage.FRENCH -> "Veuillez entrer une adresse e-mail valide"
        AppLanguage.SPANISH -> "Por favor ingrese un correo electrónico válido"
        AppLanguage.GERMAN -> "Bitte geben Sie eine gültige E-Mail-Adresse ein"
        AppLanguage.ITALIAN -> "Inserisci un indirizzo e-mail valido"
        AppLanguage.CHINESE -> "请输入有效的电子邮箱地址以进行同步"
        AppLanguage.JAPANESE -> "同期用の有効なメールアドレスを入力してください"
        AppLanguage.KOREAN -> "동기화에 사용할 올바른 이메일 주소를 입력해 주세요"
    }

    fun cloudSyncAccount(lang: AppLanguage): String = when (lang) {
        AppLanguage.ARABIC -> "حساب المزامنة السحابية"
        AppLanguage.COPTIC -> "Ⲡⲓⲧⲱⲙⲧ ⲉ̀ϯⲡⲉ"
        AppLanguage.ENGLISH -> "Cloud Sync Account"
        AppLanguage.FRENCH -> "Compte de synchronisation Cloud"
        AppLanguage.SPANISH -> "Cuenta de sincronización en la nube"
        AppLanguage.GERMAN -> "Cloud-Synchronisationskonto"
        AppLanguage.ITALIAN -> "Account di sincronizzazione Cloud"
        AppLanguage.CHINESE -> "云端同步账户"
        AppLanguage.JAPANESE -> "クラウド同期アカウント"
        AppLanguage.KOREAN -> "클라우드 동기화 계정"
    }

    fun exportPdfReport(lang: AppLanguage): String = when (lang) {
        AppLanguage.ARABIC -> "تصدير تقرير التقدم (PDF)"
        AppLanguage.COPTIC -> "Ⲉⲃⲟⲗ PDF"
        AppLanguage.ENGLISH -> "Export Progress Report (PDF)"
        AppLanguage.FRENCH -> "Exporter le rapport de progression (PDF)"
        AppLanguage.SPANISH -> "Exportar informe de progreso (PDF)"
        AppLanguage.GERMAN -> "Fortschrittsbericht exportieren (PDF)"
        AppLanguage.ITALIAN -> "Esporta rapporto di avanzamento (PDF)"
        AppLanguage.CHINESE -> "导出灵修进度报告 (PDF)"
        AppLanguage.JAPANESE -> "進行状況レポートをエクスポート (PDF)"
        AppLanguage.KOREAN -> "진행 보고서 내보내기 (PDF)"
    }

    fun exportPdfSubtitle(lang: AppLanguage): String = when (lang) {
        AppLanguage.ARABIC -> "وثيقة رسمية باسم المستخدم وتفاصيل الصلوات ومعدل الإتمام"
        AppLanguage.COPTIC -> "Ⲡⲓⲥϧⲁⲓ ⲛ̀ⲧⲉ ⲛⲓⲉⲩⲭⲏ ⲙ̀ⲡⲓⲣⲉϥⲧⲱⲃϩ"
        AppLanguage.ENGLISH -> "Official document with user account name, details & prayer breakdown"
        AppLanguage.FRENCH -> "Document avec nom de l'utilisateur et détails des prières"
        AppLanguage.SPANISH -> "Documento con nombre de usuario y detalles de oraciones"
        AppLanguage.GERMAN -> "Dokument mit Benutzername und Gebetsdetails"
        AppLanguage.ITALIAN -> "Documento con nome utente e dettagli delle preghiere"
        AppLanguage.CHINESE -> "包含用户账户名、祷告明细与完成率的正式文件"
        AppLanguage.JAPANESE -> "ユーザー名、祈りの詳細、達成度を記載した公式レポート"
        AppLanguage.KOREAN -> "사용자 이름, 기도 세부 내역 및 완료율이 포함된 공식 문서"
    }

    fun churchNameOptionalLabel(lang: AppLanguage): String = when (lang) {
        AppLanguage.ARABIC -> "اسم الكنيسة / الخدمة التي تقدم فيها (اختياري)"
        AppLanguage.COPTIC -> "Ⲫⲣⲁⲛ ⲛ̀ⲧⲉ ϯⲉⲕⲕⲗⲏⲥⲓⲁ (اختياري)"
        AppLanguage.ENGLISH -> "Church Name / Ministry Served (Optional)"
        AppLanguage.FRENCH -> "Nom de l'église / Service (Optionnel)"
        AppLanguage.SPANISH -> "Nombre de la Iglesia / Servicio (Opcional)"
        AppLanguage.GERMAN -> "Name der Kirche / Dienst (Optional)"
        AppLanguage.ITALIAN -> "Nome della Chiesa / Servizio (Opzionale)"
        AppLanguage.CHINESE -> "所属教堂 / 事工名称 (可选)"
        AppLanguage.JAPANESE -> "所属教会名 / 奉仕先 (任意)"
        AppLanguage.KOREAN -> "출석 교회 / 사역지 (선택 사항)"
    }

    fun churchNamePlaceholder(lang: AppLanguage): String = when (lang) {
        AppLanguage.ARABIC -> "مثال: كنيسة السيدة العذراء مريم"
        AppLanguage.COPTIC -> "Ϯⲉⲕⲕⲗⲏⲥⲓⲁ ⲛ̀ⲧⲉ ϯⲑⲉⲟⲧⲟⲕⲟⲥ"
        AppLanguage.ENGLISH -> "e.g., St. Mark Coptic Orthodox Church"
        AppLanguage.FRENCH -> "ex: Église Copte Orthodoxe Saint Marc"
        AppLanguage.SPANISH -> "ej: Iglesia Copta Ortodoxa San Marcos"
        AppLanguage.GERMAN -> "z.B. Koptisch-Orthodoxe St. Markus Kirche"
        AppLanguage.ITALIAN -> "es: Chiesa Copta Ortodossa San Marco"
        AppLanguage.CHINESE -> "例如：圣马克科普特正教会"
        AppLanguage.JAPANESE -> "例：聖マルコ・コプト正教会"
        AppLanguage.KOREAN -> "예: 성 마르코 콥트 정교회"
    }

    fun churchEmblemTitle(lang: AppLanguage): String = when (lang) {
        AppLanguage.ARABIC -> "شعار / أيقونة الكنيسة للتقرير"
        AppLanguage.COPTIC -> "Ⲡⲓⲥⲩⲙⲃⲟⲗⲟⲛ ⲛ̀ⲧⲉ ϯⲉⲕⲕⲗⲏⲥⲓⲁ"
        AppLanguage.ENGLISH -> "Church Emblem / Badge for PDF Reports"
        AppLanguage.FRENCH -> "Insigne / Blason de l'église pour PDF"
        AppLanguage.SPANISH -> "Insignia / Emblema de la Iglesia para PDF"
        AppLanguage.GERMAN -> "Kirchen-Emblem / Wappen für PDF-Berichte"
        AppLanguage.ITALIAN -> "Emblema / Stemma della Chiesa per PDF"
        AppLanguage.CHINESE -> "PDF 报告的教堂徽章"
        AppLanguage.JAPANESE -> "PDF レポート用の教会エンブレム"
        AppLanguage.KOREAN -> "PDF 보고서용 교회 엠블럼"
    }

    fun backupSectionTitle(lang: AppLanguage): String = when (lang) {
        AppLanguage.ARABIC -> "النسخ الاحتياطي والاستعادة المحلّية"
        AppLanguage.COPTIC -> "Ⲡⲓⲧⲱⲡ ⲛ̀ⲧⲉ ⲛⲓⲟⲩⲁⲓ"
        AppLanguage.ENGLISH -> "Local Backup & Data Restore"
        AppLanguage.FRENCH -> "Sauvegarde et Restauration Locale"
        AppLanguage.SPANISH -> "Copia de Seguridad y Restauración"
        AppLanguage.GERMAN -> "Lokale Sicherung und Wiederherstellung"
        AppLanguage.ITALIAN -> "Backup e Ripristino Locale"
        AppLanguage.CHINESE -> "本地备份与恢复"
        AppLanguage.JAPANESE -> "ローカルバックアップと復元"
        AppLanguage.KOREAN -> "로컬 백업 및 데이터 복원"
    }

    fun backupExportButton(lang: AppLanguage): String = when (lang) {
        AppLanguage.ARABIC -> "تصدير نسخة احتياطية (JSON)"
        AppLanguage.COPTIC -> "Ⲥⲱⲧⲡ ⲛ̀ⲟⲩⲧⲱⲡ (JSON)"
        AppLanguage.ENGLISH -> "Export Backup File (JSON)"
        AppLanguage.FRENCH -> "Exporter Sauvegarde (JSON)"
        AppLanguage.SPANISH -> "Exportar Copia (JSON)"
        AppLanguage.GERMAN -> "Sicherung exportieren (JSON)"
        AppLanguage.ITALIAN -> "Esporta Backup (JSON)"
        AppLanguage.CHINESE -> "导出备份文件 (JSON)"
        AppLanguage.JAPANESE -> "バックアップの出力 (JSON)"
        AppLanguage.KOREAN -> "백업 파일 내보내기 (JSON)"
    }

    fun backupRestoreButton(lang: AppLanguage): String = when (lang) {
        AppLanguage.ARABIC -> "استعادة نسخة احتياطية"
        AppLanguage.COPTIC -> "Ⲧⲁⲥⲑⲟ ⲛ̀ⲟⲩⲧⲱⲡ"
        AppLanguage.ENGLISH -> "Restore from Backup File"
        AppLanguage.FRENCH -> "Restaurer une Sauvegarde"
        AppLanguage.SPANISH -> "Restaurar desde Copia"
        AppLanguage.GERMAN -> "Aus Sicherung wiederherstellen"
        AppLanguage.ITALIAN -> "Ripristina da Backup"
        AppLanguage.CHINESE -> "从备份文件恢复"
        AppLanguage.JAPANESE -> "バックアップから復元"
        AppLanguage.KOREAN -> "백업 파일에서 복원"
    }

    fun backupRestoreSuccessMsg(count: Int, lang: AppLanguage): String = when (lang) {
        AppLanguage.ARABIC -> "تمت استعادة $count سجلاً روحيًا وجميع الإعدادات بنجاح!"
        AppLanguage.COPTIC -> "Ⲁⲛⲧⲁⲥⲑⲟ $count ⲛ̀ⲉⲩⲭⲏ ⲉⲑⲟⲩⲁⲃ!"
        AppLanguage.ENGLISH -> "Successfully restored $count prayer records & settings!"
        AppLanguage.FRENCH -> "Restauration réussie de $count enregistrements et paramètres !"
        AppLanguage.SPANISH -> "¡Se restauraron $count registros de oración y ajustes!"
        AppLanguage.GERMAN -> "$count Gebetseinträge und Einstellungen erfolgreich wiederhergestellt!"
        AppLanguage.ITALIAN -> "Ripristinati con successo $count record di preghiera e impostazioni!"
        AppLanguage.CHINESE -> "成功恢复了 $count 条祷告记录及配置！"
        AppLanguage.JAPANESE -> "$count 件の祈りの記録と設定を復元しました！"
        AppLanguage.KOREAN -> "${count}개의 기도 기록과 설정이 성공적으로 복원되었습니다!"
    }

    // Candle Sanctuary Mode
    fun candleSanctuaryTitle(lang: AppLanguage): String = when (lang) {
        AppLanguage.ARABIC -> "محراب الشمعة والتأمل الروحي"
        AppLanguage.COPTIC -> "Ⲡⲓⲙⲁ ⲛ̀ⲑⲱⲃϣ ⲛ̀ⲧⲉ ⲛⲓⲗⲁⲙⲡⲁⲥ"
        AppLanguage.ENGLISH -> "Candle Sanctuary Focus Mode"
        AppLanguage.FRENCH -> "Santuaires des Bougies & Méditation"
        AppLanguage.SPANISH -> "Santuario de Velas y Reflexión"
        AppLanguage.GERMAN -> "Kerzen-Sanktuarium Fokussierung"
        AppLanguage.ITALIAN -> "Santuario delle Candele & Meditazione"
        AppLanguage.CHINESE -> "烛光沉思专注模式"
        AppLanguage.JAPANESE -> "ろうそくの聖所・瞑想モード"
        AppLanguage.KOREAN -> "촛불 성소 묵상 모드"
    }

    // Annual Heatmap & Streaks
    fun annualHeatmapTitle(lang: AppLanguage): String = when (lang) {
        AppLanguage.ARABIC -> "التقويم الروحي الحراري السنوي (365 يومًا)"
        AppLanguage.COPTIC -> "Ⲡⲓⲁⲃⲟⲧ ⲛ̀ⲧⲉ ⲛⲓⲉⲩⲭⲏ ⲛ̀ⲧⲉ ϯⲣⲟⲙⲡⲓ"
        AppLanguage.ENGLISH -> "Annual Spiritual Heatmap Grid (365 Days)"
        AppLanguage.FRENCH -> "Matrice Annuelle de Prières (365 Jours)"
        AppLanguage.SPANISH -> "Mapa de Calor Anual de Oración (365 Días)"
        AppLanguage.GERMAN -> "Jahres-Gebets-Heatmap (365 Tage)"
        AppLanguage.ITALIAN -> "Mappa di Calore Annuale di Preghiera"
        AppLanguage.CHINESE -> "年度祷告热力图 (365 天)"
        AppLanguage.JAPANESE -> "年間祈りのヒートマップ (365 日)"
        AppLanguage.KOREAN -> "연간 기도 히트맵 (365일)"
    }

    fun currentStreakTitle(lang: AppLanguage): String = when (lang) {
        AppLanguage.ARABIC -> "سلسلة المواظبة الحالية"
        AppLanguage.COPTIC -> "Ⲧⲙⲏⲧ ⲛ̀ⲧⲉ ⲡⲓⲥⲏⲟⲩ"
        AppLanguage.ENGLISH -> "Current Prayer Streak"
        AppLanguage.FRENCH -> "Série Actuelle de Prières"
        AppLanguage.SPANISH -> "Racha Actual de Oración"
        AppLanguage.GERMAN -> "Aktuelle Gebetsserie"
        AppLanguage.ITALIAN -> "Serie Attuale di Preghiera"
        AppLanguage.CHINESE -> "当前连续祷告"
        AppLanguage.JAPANESE -> "現在の祈りストリーク"
        AppLanguage.KOREAN -> "현재 기도 스트릭"
    }

    fun longestStreakTitle(lang: AppLanguage): String = when (lang) {
        AppLanguage.ARABIC -> "أطول سلسلة مواظبة"
        AppLanguage.COPTIC -> "ϯⲙⲏⲧ ⲉⲑⲛⲁϣⲱⲥ"
        AppLanguage.ENGLISH -> "Longest Prayer Streak"
        AppLanguage.FRENCH -> "Plus Longue Série"
        AppLanguage.SPANISH -> "Racha Más Larga"
        AppLanguage.GERMAN -> "Längste Gebetsserie"
        AppLanguage.ITALIAN -> "Serie Più Lunga"
        AppLanguage.CHINESE -> "最长连续祷告"
        AppLanguage.JAPANESE -> "最长ストリーク"
        AppLanguage.KOREAN -> "최장 기도 스트릭"
    }

    fun annualConsistencyTitle(lang: AppLanguage): String = when (lang) {
        AppLanguage.ARABIC -> "نسبة الالتزام السنوي"
        AppLanguage.COPTIC -> "Ⲡⲓϣⲏⲓ ⲛ̀ⲧⲉ ϯⲣⲟⲙⲡⲓ"
        AppLanguage.ENGLISH -> "Annual Consistency"
        AppLanguage.FRENCH -> "Consistance Annuelle"
        AppLanguage.SPANISH -> "Consistencia Anual"
        AppLanguage.GERMAN -> "Jahres-Konsistenz"
        AppLanguage.ITALIAN -> "Costanza Annuale"
        AppLanguage.CHINESE -> "年度坚持率"
        AppLanguage.JAPANESE -> "年間継続率"
        AppLanguage.KOREAN -> "연간 일관성"
    }

    // Audio Recitation & Chants Player
    fun audioPlayerTitle(lang: AppLanguage): String = when (lang) {
        AppLanguage.ARABIC -> "الاستماع والتلاوة الروحية"
        AppLanguage.COPTIC -> "Ⲡⲓⲥⲱⲧⲉⲙ ⲛ̀ⲧⲉ ⲛⲓⲉⲩⲭⲏ"
        AppLanguage.ENGLISH -> "Audio Recitation & Liturgical Chants"
        AppLanguage.FRENCH -> "Récitation Audio & Chant Liturgique"
        AppLanguage.SPANISH -> "Recitación de Audio y Cantos Coptos"
        AppLanguage.GERMAN -> "Audio-Rezitation & Liturgischer Gesang"
        AppLanguage.ITALIAN -> "Recitazione Audio e Canti Copti"
        AppLanguage.CHINESE -> "语音朗读与科普特圣咏"
        AppLanguage.JAPANESE -> "音声朗読とコプト聖歌"
        AppLanguage.KOREAN -> "음성 낭독 및 콥트 성가"
    }

    fun autoScrollSyncTitle(lang: AppLanguage): String = when (lang) {
        AppLanguage.ARABIC -> "مزامنة التمرير التلقائي مع الصوت"
        AppLanguage.COPTIC -> "Ⲡⲓⲕⲓⲙ ⲛ̀ⲧⲉ ⲛⲓⲥϧⲁⲓ"
        AppLanguage.ENGLISH -> "Auto-Scroll Sync with Recitation"
        AppLanguage.FRENCH -> "Défilement Auto Synchro"
        AppLanguage.SPANISH -> "Sincronización de Desplazamiento"
        AppLanguage.GERMAN -> "Automatischer Textlauf"
        AppLanguage.ITALIAN -> "Sincronizzazione Scorrimento Testo"
        AppLanguage.CHINESE -> "自动滚动与语音同步"
        AppLanguage.JAPANESE -> "音声連動自動スクロール"
        AppLanguage.KOREAN -> "음성 동기화 자동 스크롤"
    }
}

