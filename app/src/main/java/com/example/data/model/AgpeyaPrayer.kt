package com.example.data.model

import com.example.localization.AppLanguage

enum class PrayerId(
    val code: String,
    val defaultHour: Int,
    val defaultMinute: Int,
    val copticTitle: String
) {
    PRIME("PRIME", 6, 0, "Ϯϣⲟⲣⲡ"),
    TERCE("TERCE", 9, 0, "Ϯⲁϫⲡ ⲅ̅"),
    SEXT("SEXT", 12, 0, "Ϯⲁϫⲡ ⲋ̅"),
    NONE("NONE", 15, 0, "Ϯⲁϫⲡ ⲑ̅"),
    VESPERS("VESPERS", 17, 0, "Ϯⲁϫⲡ ⲓ̅ⲁ̅"),
    COMPLINE("COMPLINE", 21, 0, "Ϯⲁϫⲡ ⲓ̅ⲃ̅"),
    VEIL("VEIL", 22, 30, "Ⲡⲓⲕⲁⲧⲁⲡⲉⲧⲁⲥⲙⲁ"),
    MIDNIGHT("MIDNIGHT", 0, 0, "Ⲡⲓⲫⲁϣⲓ ⲛ̀ϫⲱⲣϩ");

    fun getDisplayName(lang: AppLanguage): String = when (this) {
        PRIME -> when (lang) {
            AppLanguage.ARABIC -> "صلاة باكر"
            AppLanguage.SYRIAN_ARABIC -> "صلاة باكر (الصباح الباكر)"
            AppLanguage.SYRIAC -> "ܨܠܘܬܐ ܕܨܦܪܐ (صلاة باكر)"
            AppLanguage.COPTIC -> "Ϯⲉⲩⲭⲏ ⲛ̀ϯϣⲟⲣⲡ"
            AppLanguage.ENGLISH -> "Prime (Morning Prayer)"
            AppLanguage.FRENCH -> "Prière de l'Aube (Prime)"
            AppLanguage.SPANISH -> "Prima (Oración Matutina)"
            AppLanguage.GERMAN -> "Prim (Morgengebet)"
            AppLanguage.AUSTRIAN_GERMAN -> "Prim (Morgengebet - Österreich)"
            AppLanguage.SWISS_GERMAN -> "Prim (Morgegebet - Schwiiz)"
            AppLanguage.ITALIAN -> "Prima (Preghiera del Mattino)"
            AppLanguage.CHINESE -> "晨祷 (第一时课)"
            AppLanguage.JAPANESE -> "朝の祈り (第1時課)"
            AppLanguage.KOREAN -> "아침 기도 (제1시경)"
        }
        TERCE -> when (lang) {
            AppLanguage.ARABIC -> "صلاة الساعة الثالثة"
            AppLanguage.SYRIAN_ARABIC -> "صلاة الساعة التالتة"
            AppLanguage.SYRIAC -> "ܨܠܘܬܐ ܕܬܠܬ ܫܥ̈ܝܢ (الساعة الثالثة)"
            AppLanguage.COPTIC -> "Ϯⲉⲩⲭⲏ ⲛ̀ϯⲁϫⲡ ⲅ̅"
            AppLanguage.ENGLISH -> "Terce (3rd Hour)"
            AppLanguage.FRENCH -> "Tierce (3ème Heure)"
            AppLanguage.SPANISH -> "Tercia (3ª Hora)"
            AppLanguage.GERMAN -> "Terz (3. Stunde)"
            AppLanguage.AUSTRIAN_GERMAN -> "Terz (3. Stunde)"
            AppLanguage.SWISS_GERMAN -> "Terz (3. Schtund)"
            AppLanguage.ITALIAN -> "Terza (3ª Ora)"
            AppLanguage.CHINESE -> "第三时课祷告 (巳时)"
            AppLanguage.JAPANESE -> "第3時課の祈り (午前9時)"
            AppLanguage.KOREAN -> "제3시경 기도 (오전 9시)"
        }
        SEXT -> when (lang) {
            AppLanguage.ARABIC -> "صلاة الساعة السادسة"
            AppLanguage.SYRIAN_ARABIC -> "صلاة الساعة السادسة (الضهر)"
            AppLanguage.SYRIAC -> "ܨܠܘܬܐ ܕܫܬ ܫܥ̈ܝܢ (الساعة السادسة)"
            AppLanguage.COPTIC -> "Ϯⲉⲩⲭⲏ ⲛ̀ϯⲁϫⲡ ⲋ̅"
            AppLanguage.ENGLISH -> "Sext (6th Hour)"
            AppLanguage.FRENCH -> "Sexte (6ème Heure)"
            AppLanguage.SPANISH -> "Sexta (6ª Hora)"
            AppLanguage.GERMAN -> "Sext (6. Stunde)"
            AppLanguage.AUSTRIAN_GERMAN -> "Sext (Mittagsgebet)"
            AppLanguage.SWISS_GERMAN -> "Sext (Mittagsgebet)"
            AppLanguage.ITALIAN -> "Sesta (6ª Ora)"
            AppLanguage.CHINESE -> "第六时课祷告 (午时)"
            AppLanguage.JAPANESE -> "第6時課の祈り (正午)"
            AppLanguage.KOREAN -> "제6시경 기도 (정오)"
        }
        NONE -> when (lang) {
            AppLanguage.ARABIC -> "صلاة الساعة التاسعة"
            AppLanguage.SYRIAN_ARABIC -> "صلاة الساعة التاسعة (العصر)"
            AppLanguage.SYRIAC -> "ܨܠܘܬܐ ܕܬܫܥ ܫܥ̈ܝܢ (الساعة التاسعة)"
            AppLanguage.COPTIC -> "Ϯⲉⲩⲭⲏ ⲛ̀ϯⲁϫⲡ ⲑ̅"
            AppLanguage.ENGLISH -> "None (9th Hour)"
            AppLanguage.FRENCH -> "None (9ème Heure)"
            AppLanguage.SPANISH -> "Nona (9ª Hora)"
            AppLanguage.GERMAN -> "Non (9. Stunde)"
            AppLanguage.AUSTRIAN_GERMAN -> "Non (9. Stunde)"
            AppLanguage.SWISS_GERMAN -> "Non (9. Schtund)"
            AppLanguage.ITALIAN -> "Nona (9ª Ora)"
            AppLanguage.CHINESE -> "第九时课祷告 (申时)"
            AppLanguage.JAPANESE -> "第9時課の祈り (午後3時)"
            AppLanguage.KOREAN -> "제9시경 기도 (오후 3시)"
        }
        VESPERS -> when (lang) {
            AppLanguage.ARABIC -> "صلاة الغروب (الحادية عشر)"
            AppLanguage.SYRIAN_ARABIC -> "صلاة الغروب (المغربية)"
            AppLanguage.SYRIAC -> "ܨܠܘܬܐ ܕܪܡܫܐ (صلاة الغروب)"
            AppLanguage.COPTIC -> "Ϯⲉⲩⲭⲏ ⲛ̀ϯⲁϫⲡ ⲓ̅ⲁ̅"
            AppLanguage.ENGLISH -> "Vespers (11th Hour)"
            AppLanguage.FRENCH -> "Vêpres (11ème Heure)"
            AppLanguage.SPANISH -> "Vísperas (11ª Hora)"
            AppLanguage.GERMAN -> "Vesper (11. Stunde)"
            AppLanguage.AUSTRIAN_GERMAN -> "Vesper (Abendgebet)"
            AppLanguage.SWISS_GERMAN -> "Vesper (Aabiggebet)"
            AppLanguage.ITALIAN -> "Vespri (11ª Ora)"
            AppLanguage.CHINESE -> "晚祷 (第十一时课/日落)"
            AppLanguage.JAPANESE -> "晩課 (第11時課/日没の祈り)"
            AppLanguage.KOREAN -> "만과 (제11시경/저녁 기도)"
        }
        COMPLINE -> when (lang) {
            AppLanguage.ARABIC -> "صلاة النوم (الثانية عشر)"
            AppLanguage.SYRIAN_ARABIC -> "صلاة النوم (قبل النوم)"
            AppLanguage.SYRIAC -> "ܨܠܘܬܐ ܕܣܘܬܪܐ (صلاة النوم)"
            AppLanguage.COPTIC -> "Ϯⲉⲩⲭⲏ ⲛ̀ϯⲁϫⲡ ⲓ̅ⲃ̅"
            AppLanguage.ENGLISH -> "Compline (12th Hour)"
            AppLanguage.FRENCH -> "Complies (12ème Heure)"
            AppLanguage.SPANISH -> "Completas (12ª Hora)"
            AppLanguage.GERMAN -> "Komplet (12. Stunde)"
            AppLanguage.AUSTRIAN_GERMAN -> "Komplet (Nachtgebet)"
            AppLanguage.SWISS_GERMAN -> "Komplet (Nachtgebet)"
            AppLanguage.ITALIAN -> "Compieta (12ª Ora)"
            AppLanguage.CHINESE -> "寝前终课 (第十二时课)"
            AppLanguage.JAPANESE -> "終課 (第12時課/就寝前の祈り)"
            AppLanguage.KOREAN -> "종과 (제12시경/취침 전 기도)"
        }
        VEIL -> when (lang) {
            AppLanguage.ARABIC -> "صلاة الستار"
            AppLanguage.SYRIAN_ARABIC -> "صلاة الستار (للرهبان)"
            AppLanguage.SYRIAC -> "ܨܠܘܬܐ ܕܦܪܣܐ (صلاة الستار)"
            AppLanguage.COPTIC -> "Ϯⲉⲩⲭⲏ ⲙ̀ⲡⲓⲕⲁⲧⲁⲡⲉⲧⲁⲥⲙⲁ"
            AppLanguage.ENGLISH -> "The Veil (Monastic Hour)"
            AppLanguage.FRENCH -> "Prière du Voile"
            AppLanguage.SPANISH -> "Oración del Velo"
            AppLanguage.GERMAN -> "Schleier-Gebet"
            AppLanguage.AUSTRIAN_GERMAN -> "Schleiergebet"
            AppLanguage.SWISS_GERMAN -> "Schleiergebet"
            AppLanguage.ITALIAN -> "Preghiera del Velo"
            AppLanguage.CHINESE -> "帷幔祷文 (修道院特别课)"
            AppLanguage.JAPANESE -> "ヴェールの祈り (修道院の祈祷)"
            AppLanguage.KOREAN -> "휘장 기도 (수도원 특별 기도)"
        }
        MIDNIGHT -> when (lang) {
            AppLanguage.ARABIC -> "صلاة نصف الليل"
            AppLanguage.SYRIAN_ARABIC -> "صلاة نص الليل"
            AppLanguage.SYRIAC -> "ܨܠܘܬܐ ܕܦܠܓܗ ܕܠܠܝܐ (صلاة نصف الليل)"
            AppLanguage.COPTIC -> "Ϯⲉⲩⲭⲏ ⲙ̀ⲡⲓⲫⲁϣⲓ ⲛ̀ϫⲱⲣϩ"
            AppLanguage.ENGLISH -> "Midnight Service"
            AppLanguage.FRENCH -> "Office de Minuit"
            AppLanguage.SPANISH -> "Oficio de Medianoche"
            AppLanguage.GERMAN -> "Mitternachtsgebet"
            AppLanguage.AUSTRIAN_GERMAN -> "Matutin / Mitternachtsgebet"
            AppLanguage.SWISS_GERMAN -> "Mitternachtsgebet"
            AppLanguage.ITALIAN -> "Ufficio di Mezzanotte"
            AppLanguage.CHINESE -> "子夜课祷告 (半夜课)"
            AppLanguage.JAPANESE -> "真夜中の祈り (夜課)"
            AppLanguage.KOREAN -> "자정 기도 (심야경)"
        }
    }

    fun getSpiritualTheme(lang: AppLanguage): String = when (this) {
        PRIME -> when (lang) {
            AppLanguage.ARABIC, AppLanguage.SYRIAN_ARABIC -> "تذكار قيامة مخلصنا الصالح، وإشراق النور الحقيقي في قلوبنا وبداية اليوم مع الله."
            AppLanguage.SYRIAC -> "ܥܘܗܕܢܐ ܕܩܝܡܬܗ ܕܦܪܘܩܢ ܛܒܐ ܘܕܢܚܐ ܕܢܘܗܪܐ ܫܪܝܪܐ ܒܠܒܘ̈ܬܢ."
            AppLanguage.COPTIC -> "Ⲡⲓⲙⲉⲩⲓ ⲛ̀ϯⲁⲛⲁⲥⲧⲁⲥⲓⲥ ⲛ̀ⲧⲉ Ⲡⲉⲛⲥⲱⲧⲏⲣ ⲙ̀ⲡⲓⲟⲩⲱⲓⲛⲓ ⲛ̀ⲧⲁⲫⲙⲏⲓ."
            AppLanguage.ENGLISH -> "Commemoration of the glorious Resurrection of Christ, the True Light, and greeting the new day with thanksgiving."
            AppLanguage.FRENCH -> "Commémoration de la Résurrection du Christ, la Vraie Lumière, et remerciement pour le nouveau jour."
            AppLanguage.SPANISH -> "Conmemoración de la Resurrección de Cristo, la Luz Verdadera, y alabanza al iniciar el día."
            AppLanguage.GERMAN, AppLanguage.AUSTRIAN_GERMAN, AppLanguage.SWISS_GERMAN -> "Gedenken an die Auferstehung Christi, das wahre Licht, und Dank für den neuen Morgen."
            AppLanguage.ITALIAN -> "Commemorazione della Risurrezione di Cristo, la Vera Luce, e ringraziamento all'alba del giorno."
            AppLanguage.CHINESE -> "纪念基督荣耀复活、真光普照世界，并以感恩之心开启新的一天。"
            AppLanguage.JAPANESE -> "真の光であるキリストの栄光ある復活の記念と、感謝をもって新しい日を迎える祈り。"
            AppLanguage.KOREAN -> "참 빛이신 그리스도의 영광스러운 부활을 기념하며 감사로 하루를 시작하는 기도."
        }
        TERCE -> when (lang) {
            AppLanguage.ARABIC, AppLanguage.SYRIAN_ARABIC -> "تذكار حلول الروح القدس المعزي على التلاميذ الأطهار في يوم الخمسين، وتذكار محاكمة بيلاطس."
            AppLanguage.SYRIAC -> "ܥܘܗܕܢܐ ܕܡܚܬܬܐ ܕܪܘܚܐ ܩܕܝܫܐ ܦܪܩܠܝܛܐ ܥܠ ܝܘܠܦܢܐ ܩܕܝܫܐ."
            AppLanguage.COPTIC -> "Ⲡⲓϫⲓⲛⲓ̀ ⲉ̀ϧⲣⲏⲓ ⲛ̀ⲧⲉ Ⲡⲓⲡⲛⲉⲩⲙⲁ ⲉⲑⲟⲩⲁⲃ Ⲡⲓⲡⲁⲣⲁⲕⲗⲏⲧⲟⲥ."
            AppLanguage.ENGLISH -> "Commemoration of the descent of the Holy Spirit on Pentecost and the trial of Christ before Pontius Pilate."
            AppLanguage.FRENCH -> "Commémoration de la descente du Saint-Esprit le jour de la Pentecôte et du procès de Pilate."
            AppLanguage.SPANISH -> "Conmemoración de la venida del Espíritu Santo en Pentecostés y el juicio de Pilato."
            AppLanguage.GERMAN, AppLanguage.AUSTRIAN_GERMAN, AppLanguage.SWISS_GERMAN -> "Gedenken an das Herabkommen des Heiligen Geistes an Pfingsten und das Verhör vor Pilatus."
            AppLanguage.ITALIAN -> "Commemorazione della discesa dello Spirito Santo a Pentecoste e del processo davanti a Pilato."
            AppLanguage.CHINESE -> "纪念五旬节圣灵保惠师降临在门徒身上，以及基督在彼拉多面前受审。"
            AppLanguage.JAPANESE -> "ペンテコステにおける聖霊降臨と、ピラトの法廷でのキリストの裁判の記念。"
            AppLanguage.KOREAN -> "오순절 보혜사 성령 강림과 빌라도 앞에서의 그리스도의 재판을 기념함."
        }
        SEXT -> when (lang) {
            AppLanguage.ARABIC, AppLanguage.SYRIAN_ARABIC -> "تذكار صلب فادينا يسوع المسيح على الجلجثة في منتصف النهار لأجل خلاص جنس البشر."
            AppLanguage.SYRIAC -> "ܥܘܗܕܢܐ ܕܙܩܝܦܘܬܗ ܕܡܪܢ ܝܫܘܥ ܡܫܝܚܐ ܒܓܓܘܠܬܐ ܒܦܠܓܗ ܕܝܘܡܐ."
            AppLanguage.COPTIC -> "Ⲡⲓⲁϣⲓ ⲉ̀ϫⲉⲛ ⲡⲓⲥⲧⲁⲩⲣⲟⲥ ϧⲉⲛ ϯⲕⲣⲁⲛⲓⲟⲛ ⲉ̀ⲑⲃⲉ ⲡⲉⲛⲟⲩϫⲁⲓ."
            AppLanguage.ENGLISH -> "Commemoration of the Crucifixion and Passion of our Lord Jesus Christ on Golgotha at midday."
            AppLanguage.FRENCH -> "Commémoration de la Crucifixion de notre Seigneur Jésus-Christ au Calvaire à midi."
            AppLanguage.SPANISH -> "Conmemoración de la Crucifixión de nuestro Señor Jesucristo en el Gólgota al mediodía."
            AppLanguage.GERMAN, AppLanguage.AUSTRIAN_GERMAN, AppLanguage.SWISS_GERMAN -> "Gedenken an die Kreuzigung unseres Herrn Jesus Christus auf Golgatha zur Mittagszeit."
            AppLanguage.ITALIAN -> "Commemorazione della Crocifissione di nostro Signore Gesù Cristo sul Golgota a mezzogiorno."
            AppLanguage.CHINESE -> "纪念主耶稣基督正午在各各他十字架上的受难与受钉，为全人类成就救赎。"
            AppLanguage.JAPANESE -> "人類の救いのために正午にゴルゴタで十字架につけられた主イエス・キリストの受難の記念。"
            AppLanguage.KOREAN -> "인류 구원을 위해 정오에 골고다 십자가에 달리신 우리 주 예수 그리스도의 수난을 기념함."
        }
        NONE -> when (lang) {
            AppLanguage.ARABIC, AppLanguage.SYRIAN_ARABIC -> "تذكار موت المسيح بالجسد على الصليب، وقبول توبة اللص اليمين، وخلاص العالم."
            AppLanguage.SYRIAC -> "ܥܘܗܕܢܐ ܕܡܘܬܗ ܕܡܫܝܚܐ ܒܦܓܪ ܥܠ ܨܠܝܒܐ ܘܬܝܒܘܬܗ ܕܓܝܣܐ ܕܡܢ ܝܡܝܢܐ."
            AppLanguage.COPTIC -> "Ⲡⲓⲙⲟⲩ ⲛ̀ⲧⲉ Ⲡⲭ̅ⲥ̅ ϧⲉⲛ ⲧⲥⲁⲣⲝ ⲉ̀ϫⲉⲛ ⲡⲓⲥⲧⲁⲩⲣⲟⲥ."
            AppLanguage.ENGLISH -> "Commemoration of the life-giving death of Christ on the Cross and the salvation of the penitent thief."
            AppLanguage.FRENCH -> "Commémoration de la mort du Christ sur la Croix et du salut du bon larron."
            AppLanguage.SPANISH -> "Conmemoración de la muerte vivificante de Cristo en la Cruz y el perdón del ladrón arrepentido."
            AppLanguage.GERMAN, AppLanguage.AUSTRIAN_GERMAN, AppLanguage.SWISS_GERMAN -> "Gedenken an den lebenspendenden Tod Christi am Kreuz und die Rettung des reumütigen Schächers."
            AppLanguage.ITALIAN -> "Commemorazione della morte vivificante di Cristo sulla Croce e della salvezza del buon ladrone."
            AppLanguage.CHINESE -> "纪念基督肉身在十字架上赐予生命的受死、右盗悔改蒙恩与世界得救。"
            AppLanguage.JAPANESE -> "十字架上でのキリストの命を与える死と、悔い改めた盗人の救いの記念。"
            AppLanguage.KOREAN -> "십자가 위에서 생명을 주시는 그리스도의 죽으심과 회개한 강도의 구원을 기념함."
        }
        VESPERS -> when (lang) {
            AppLanguage.ARABIC, AppLanguage.SYRIAN_ARABIC -> "تذكار إنزال الجسد المقدس من على الصليب، وشكر الله على حفظنا خلال ساعات النهار."
            AppLanguage.SYRIAC -> "ܥܘܗܕܢܐ ܕܢܚܬܬ ܦܓܪܗ ܩܕܝܫܐ ܡܢ ܨܠܝܒܐ ܘܬܘܕܝܬܐ ܒܥܕܢ ܪܡܫܐ."
            AppLanguage.COPTIC -> "Ⲡⲓⲓⲛⲓ ⲉ̀ⲡⲉⲥⲏⲧ ⲛ̀ⲧⲉ ⲡⲓⲥⲱⲙⲁ ⲉⲑⲟⲩⲁⲃ ⲉ̀ⲃⲟⲗ ϩⲓ ⲡⲓⲥⲧⲁⲩⲣⲟⲥ."
            AppLanguage.ENGLISH -> "Commemoration of taking down Christ's sacred body from the Cross and thanksgiving at sunset."
            AppLanguage.FRENCH -> "Commémoration de la descente de Croix du corps sacré du Christ et action de grâce au coucher du soleil."
            AppLanguage.SPANISH -> "Conmemoración del descendimiento de la Cruz del cuerpo santo de Cristo y acción de gracias al atardecer."
            AppLanguage.GERMAN, AppLanguage.AUSTRIAN_GERMAN, AppLanguage.SWISS_GERMAN -> "Gedenken an die Kreuzabnahme des heiligen Leibes Christi und Dankgebet bei Sonnenuntergang."
            AppLanguage.ITALIAN -> "Commemorazione della deposizione dalla Croce del corpo sacro di Cristo e ringraziamento al tramonto."
            AppLanguage.CHINESE -> "纪念从十字架上取下基督至圣圣体，并在日落黄昏时向神献上感恩。"
            AppLanguage.JAPANESE -> "十字架からのキリストの聖なる遺体の降下と、日没における神への感謝の記念。"
            AppLanguage.KOREAN -> "십자가에서 그리스도의 거룩한 시신을 내리심과 일몰 때 드리는 감사의 기념."
        }
        COMPLINE -> when (lang) {
            AppLanguage.ARABIC, AppLanguage.SYRIAN_ARABIC -> "تذكار دفن الجسد الطاهر في القبر، وتذكر الموت والدينونة والخلود، وطلب حراسة ملائكة السلامة في النوم."
            AppLanguage.SYRIAC -> "ܥܘܗܕܢܐ ܕܩܒܘܪܬܗ ܕܡܪܢ ܘܨܠܘܬܐ ܕܢܛܘܪܘܬܐ ܕܡܠܐܟ̈ܐ ܒܫܢܬܐ."
            AppLanguage.COPTIC -> "Ⲡⲓϫⲓⲛⲭⲱ ϧⲉⲛ ⲡⲓⲙϩⲁⲟⲩ ⲉⲑⲟⲩⲁⲃ ⲛ̀ⲧⲉ Ⲡⲉⲛⲥⲱⲧⲏⲣ."
            AppLanguage.ENGLISH -> "Commemoration of the burial of Christ in the tomb, reflection on eternity, and prayer for peaceful rest under divine protection."
            AppLanguage.FRENCH -> "Commémoration de la mise au tombeau du Christ, méditation sur l'éternité et prière pour un sommeil paisible."
            AppLanguage.SPANISH -> "Conmemoración de la sepultura de Cristo, examen de conciencia y descanso en la paz de Dios."
            AppLanguage.GERMAN, AppLanguage.AUSTRIAN_GERMAN, AppLanguage.SWISS_GERMAN -> "Gedenken an die Grablegung Christi, Besinnung auf die Ewigkeit und Bitte um behüteten Schlaf."
            AppLanguage.ITALIAN -> "Commemorazione della sepoltura di Cristo, esame di coscienza e preghiera per il riposo notturno."
            AppLanguage.CHINESE -> "纪念基督圣体安葬于坟墓，省察永恒与审判，祈求天主恩赐夜间平安歇息与护守。"
            AppLanguage.JAPANESE -> "キリストの墓への埋葬の記念、永遠についての省察、神の守りのもとでの安らかな眠りの祈り。"
            AppLanguage.KOREAN -> "무덤에 묻히신 그리스도의 장사와 영원을 묵상하며 밤 동안의 평안한 안식을 청하는 기도."
        }
        VEIL -> when (lang) {
            AppLanguage.ARABIC, AppLanguage.SYRIAN_ARABIC -> "صلاة رهبانية خاصة بالتوبة والانسحاق العميق وطلب مراحم الرب قبل هجوع الليل."
            AppLanguage.SYRIAC -> "ܨܠܘܬܐ ܕܕܝܪܝ̈ܐ ܕܬܝܒܘܬܐ ܥܡܝܩܬܐ ܘܒܥܘܬܐ ܕܪ̈ܚܡܐ."
            AppLanguage.COPTIC -> "Ϯⲙⲉⲧⲁⲛⲟⲓⲁ ⲛ̀ⲧⲉ ⲛⲓⲙⲟⲛⲁⲭⲟⲥ ϧⲁⲧⲉⲛ ⲡⲓϫⲱⲕ."
            AppLanguage.ENGLISH -> "Monastic prayer of deep repentance, compunction, and vigilance before the midnight watch."
            AppLanguage.FRENCH -> "Prière monastique de repentir profond et de vigilance avant la garde de minuit."
            AppLanguage.SPANISH -> "Oración monástica de profundo arrepentimiento y vigilancia nocturna."
            AppLanguage.GERMAN, AppLanguage.AUSTRIAN_GERMAN, AppLanguage.SWISS_GERMAN -> "Mönchisches Bußgebet tiefster Reue und Wachsamkeit vor der Mitternacht."
            AppLanguage.ITALIAN -> "Preghiera monastica di sincero pentimento e veglia notturna."
            AppLanguage.CHINESE -> "修道院在深夜守护前，专注深切悔改、痛悔前非并祈求主恩怜悯的特别祷告。"
            AppLanguage.JAPANESE -> "真夜中の徹夜祈祷の前の、深い悔い改めと痛悔、主の憐れみを求める修道士の祈り。"
            AppLanguage.KOREAN -> "한밤의 파수 전에 깊은 회개와 참회로 주님의 자비를 구하는 수도원 기도."
        }
        MIDNIGHT -> when (lang) {
            AppLanguage.ARABIC, AppLanguage.SYRIAN_ARABIC -> "تذكار المجيء الثاني لربنا يسوع المسيح والسهر الروحي: «ها العريس يأتي في نصف الليل فطوبى للعبد الذي يجده ساهراً»."
            AppLanguage.SYRIAC -> "ܥܘܗܕܢܐ ܕܡܐܬܝܬܗ ܕܬܪ̈ܬܝܢ ܕܡܪܢ: «ܗܐ ܚܬܢܐ ܐܬܐ ܒܦܠܓܗ ܕܠܠܝܐ»."
            AppLanguage.COPTIC -> "Ⲡⲓⲙⲉⲩⲓ ⲛ̀ϯⲙⲁϩⲥⲛⲟⲩϯ ⲙ̀ⲡⲁⲣⲟⲩⲥⲓⲁ: Ϩⲏⲡⲡⲉ ⲓⲥ ⲡⲓⲡⲁⲧϣⲉⲗⲉⲧ ϥⲛⲏⲟⲩ ϧⲉⲛ ⲧⲫⲁϣⲓ ⲛ̀ϫⲱⲣϩ."
            AppLanguage.ENGLISH -> "Commemoration of the Second Coming of Christ and spiritual watchfulness: 'Behold, the Bridegroom cometh at midnight'."
            AppLanguage.FRENCH -> "Commémoration du Second Avènement du Christ et vigilance spirituelle : « Voici l'Époux qui vient au milieu de la nuit »."
            AppLanguage.SPANISH -> "Conmemoración de la Segunda Venida de Cristo y vigilancia espiritual: «He aquí el Esposo viene a medianoche»."
            AppLanguage.GERMAN, AppLanguage.AUSTRIAN_GERMAN, AppLanguage.SWISS_GERMAN -> "Gedenken an die Wiederkunft Christi und geistliche Wachsamkeit: „Siehe, der Bräutigam kommt um Mitternacht“."
            AppLanguage.ITALIAN -> "Commemorazione della Seconda Venuta di Cristo e vigilanza: «Ecco, lo Sposo arriva a mezzanotte»."
            AppLanguage.CHINESE -> "纪念基督二次降临与属灵儆醒：“看哪，新郎在半夜到了，那儆醒守候的仆人有福了！”"
            AppLanguage.JAPANESE -> "キリストの再臨と霊的な目覚めの記念：「見よ、花婿だ。夜中に迎えに出よ」。"
            AppLanguage.KOREAN -> "그리스도의 재림과 영적 깨어있음을 기념함: \"보라, 신랑이 밤중에 오니 깨어 있는 종은 복이 있도다\"."
        }
    }

    fun getKeyVerse(lang: AppLanguage): String = when (this) {
        PRIME -> when (lang) {
            AppLanguage.ARABIC, AppLanguage.SYRIAN_ARABIC -> "«يا الله أنت إلهي، إليك أبكر، عطشت إليك نفسي» (مزمور 63: 1)"
            AppLanguage.SYRIAC -> "«ܐܠܗܐ ܐܠܗܝ ܐܢܬ ܠܟ ܐܫܚܪ ܨܗܝܬ ܠܟ ܢܦܫܝ» (ܡܙܡܘܪܐ ܣܓ: ܐ)"
            AppLanguage.COPTIC -> "«Ⲫⲛⲟⲩϯ ⲡⲁⲛⲟⲩϯ ϯⲛⲁϣⲟⲣⲡ ⲉ̀ⲣⲟⲓ ϩⲁⲣⲟⲕ»"
            AppLanguage.ENGLISH -> "\"O God, You are my God; early will I seek You; my soul thirsts for You.\" (Psalm 63:1)"
            AppLanguage.FRENCH -> "« Ô Dieu ! Tu es mon Dieu, je te cherche dès l'aube; mon âme a soif de toi. » (Psaume 63:1)"
            AppLanguage.SPANISH -> "«Dios, Dios mío eres tú; de madrugada te buscaré; mi alma tiene sed de ti.» (Salmo 63:1)"
            AppLanguage.GERMAN, AppLanguage.AUSTRIAN_GERMAN, AppLanguage.SWISS_GERMAN -> "„Gott, du bist mein Gott; frühe suche ich dich; es dürstet meine Seele nach dir.“ (Psalm 63:1)"
            AppLanguage.ITALIAN -> "«O Dio, tu sei il mio Dio, dall'aurora ti cerco; ha sete di te l'anima mia.» (Salmo 63:1)"
            AppLanguage.CHINESE -> "“　神啊，你是我的　神，我要切切地寻求你；在干旱疲乏无水之地，我渴想你。” (诗篇 63:1)"
            AppLanguage.JAPANESE -> "「神よ、あなたは私の神。私は切にあなたを求めます。水のない渇いた地で、私の魂はあなたを渇望します。」(詩篇 63:1)"
            AppLanguage.KOREAN -> "\"하나님이여 주는 나의 하나님이시라 내가 간절히 주를 찾되 물이 없어 마르고 황폐한 땅에서 내 영혼이 주를 갈망하며\" (시편 63:1)"
        }
        TERCE -> when (lang) {
            AppLanguage.ARABIC, AppLanguage.SYRIAN_ARABIC -> "«روحك القدوس لا تنزعه مني، رد لي بهجة خلاصك» (مزمور 51: 11)"
            AppLanguage.SYRIAC -> "«ܘܪܘܚܟ ܩܕܝܫܐ ܠܐ ܬܣܒ ܡܢܝ» (ܡܙܡܘܪܐ ܢܐ: ܝܐ)"
            AppLanguage.COPTIC -> "«Ⲡⲉⲕⲡⲛⲉⲩⲙⲁ ⲉⲑⲟⲩⲁⲃ ⲙ̀ⲡⲉⲣⲟⲗϥ ⲉ̀ⲃⲟⲗ ϩⲁⲣⲟⲓ»"
            AppLanguage.ENGLISH -> "\"Do not cast me away from Your presence, and do not take Your Holy Spirit from me.\" (Psalm 51:11)"
            AppLanguage.FRENCH -> "« Ne me rejette pas loin de ta face, ne me retire pas ton Esprit Saint. » (Psaume 51:11)"
            AppLanguage.SPANISH -> "«No me eches de delante de ti, y no quites de mí tu Santo Espíritu.» (Salmo 51:11)"
            AppLanguage.GERMAN, AppLanguage.AUSTRIAN_GERMAN, AppLanguage.SWISS_GERMAN -> "„Verwirf mich nicht von deinem Angesicht und nimm deinen heiligen Geist nicht von mir.“ (Psalm 51:11)"
            AppLanguage.ITALIAN -> "«Non respingermi dalla tua presenza e non privarmi del tuo santo spirito.» (Salmo 51:11)"
            AppLanguage.CHINESE -> "“不要丢弃我，使我离开你的面；不要从我收回你的圣灵。求你使我仍得救恩之乐。” (诗篇 51:11-12)"
            AppLanguage.JAPANESE -> "「私を御前から投げ捨てず、あなたの聖霊を私から取り去らないでください。救いの喜びを私に回復させてください。」(詩篇 51:11)"
            AppLanguage.KOREAN -> "\"나를 주 앞에서 쫓아내지 마시며 주의 성령을 내게서 거두지 마소서 주의 구원의 즐거움을 내게 회복시키시고\" (시편 51:11-12)"
        }
        SEXT -> when (lang) {
            AppLanguage.ARABIC, AppLanguage.SYRIAN_ARABIC -> "«مساميرك سمرت في أقدامي وأيديك، ومسحت كل خطاياي بدمك الطاهر»"
            AppLanguage.SYRIAC -> "«ܒܙܩܝܦܘܬܟ ܦܪܩܬ ܠܢ ܡܢ ܛܘܥܝܝ»"
            AppLanguage.COPTIC -> "«Ⲁⲩⲁϣⲧ ϧⲉⲛ ⲧⲁϫⲡ ⲋ̅ ⲉ̀ϫⲉⲛ ⲡⲓⲥⲧⲁⲩⲣⲟⲥ»"
            AppLanguage.ENGLISH -> "\"O You who on the sixth hour of the sixth day nailed the sin of Adam to the Cross, blot out the handwriting of our sins.\""
            AppLanguage.FRENCH -> "« Toi qui, à la sixième heure du sixième jour, as cloué sur la Croix le péché d'Adam, efface nos fautes. »"
            AppLanguage.SPANISH -> "«Tú que en la sexta hora clavaste en la Cruz el pecado del hombre, perdona nuestras transgresiones.»"
            AppLanguage.GERMAN, AppLanguage.AUSTRIAN_GERMAN, AppLanguage.SWISS_GERMAN -> "„Der du zur sechsten Stunde am Kreuz die Sünde der Welt getragen hast, vergib uns unsere Schuld.“"
            AppLanguage.ITALIAN -> "«Tu che alla sesta ora hai inchiodato sulla Croce il peccato, cancella le nostre colpe.»"
            AppLanguage.CHINESE -> "“在第六日第六时将始祖罪过钉于十字架上的基督，求祢涂抹我们的过犯，拯救我们。”"
            AppLanguage.JAPANESE -> "「第6日の第6時課にアダムの罪を十字架に釘付けにされた主よ、私たちの罪の証書を破り捨ててください。」"
            AppLanguage.KOREAN -> "\"제6일 제6시경에 아담의 죄를 십자가에 못 박으신 주여, 우리 죄의 빚 문서를 찢으시고 구원하소서.\""
        }
        NONE -> when (lang) {
            AppLanguage.ARABIC, AppLanguage.SYRIAN_ARABIC -> "«اذكرني يا رب متى جئت في ملكوتك» (لوقا 23: 42)"
            AppLanguage.SYRIAC -> "«ܐܬܕܟܪܝܢܝ ܡܪܝ ܡܐ ܕܐܬܐ ܐܢܬ ܒܡܠܟܘܬܟ» (ܠܘܩܐ ܟܓ: ܡܒ)"
            AppLanguage.COPTIC -> "«Ⲁⲣⲓⲡⲁⲙⲉⲩⲓ ⲱ Ⲡⲁϭⲟⲓⲥ ⲁⲕϣⲁⲛⲓ̀ ϧⲉⲛ ⲧⲉⲕⲙⲉⲧⲟⲩⲣⲟ»"
            AppLanguage.ENGLISH -> "\"Lord, remember me when You come into Your kingdom.\" (Luke 23:42)"
            AppLanguage.FRENCH -> "« Souviens-toi de moi, Seigneur, quand tu viendras dans ton règne. » (Luc 23:42)"
            AppLanguage.SPANISH -> "«Acuérdate de mí, Señor, cuando vengas en tu reino.» (Lucas 23:42)"
            AppLanguage.GERMAN, AppLanguage.AUSTRIAN_GERMAN, AppLanguage.SWISS_GERMAN -> "„Herr, gedenke meiner, wenn du in dein Reich kommst!“ (Lukas 23:42)"
            AppLanguage.ITALIAN -> "«Signore, ricordati di me quando entrerai nel tuo regno.» (Luca 23:42)"
            AppLanguage.CHINESE -> "“耶稣啊，你得国降临的时候，求你纪念我！” (路加福音 23:42)"
            AppLanguage.JAPANESE -> "「イエスよ、あなたが御国に入られるとき、私を思い出してください。」(ルカ 23:42)"
            AppLanguage.KOREAN -> "\"예수여 당신의 나라에 임하실 때에 나를 기억하소서\" (누가복음 23:42)"
        }
        VESPERS -> when (lang) {
            AppLanguage.ARABIC, AppLanguage.SYRIAN_ARABIC -> "«لتستقم صلاتي كالبخور قدامك، ورفع يدي كذبيحة مسائية» (مزمور 141: 2)"
            AppLanguage.SYRIAC -> "«ܬܬܩܢ ܨܠܘܬܝ ܐܝܟ ܒܣܡܐ ܩܕܡܝܟ ܘܡܬܟܠܬܐ ܕܐܝܕܝ ܐܝܟ ܩܘܪܒܢܐ ܕܪܡܫܐ»"
            AppLanguage.COPTIC -> "«Ⲙⲁⲣⲉ ⲧⲁⲡⲣⲟⲥⲉⲩⲭⲏ ⲥⲱⲟⲩⲧⲉⲛ ⲙ̀ⲫⲣⲏϯ ⲛ̀ⲟⲩⲥⲑⲟⲩⲛⲟⲩϥⲓ ⲙ̀ⲡⲉⲕⲙ̀ⲑⲟ»"
            AppLanguage.ENGLISH -> "\"Let my prayer be set before You as incense, the lifting up of my hands as the evening sacrifice.\" (Psalm 141:2)"
            AppLanguage.FRENCH -> "« Que ma prière soit devant ta face comme l'encens, et l'élévation de mes mains comme le sacrifice du soir. » (Psaume 141:2)"
            AppLanguage.SPANISH -> "«Suba mi oración delante de ti como el incienso, el don de mis manos como la ofrenda vespertina.» (Salmo 141:2)"
            AppLanguage.GERMAN, AppLanguage.AUSTRIAN_GERMAN, AppLanguage.SWISS_GERMAN -> "„Mein Gebet möge vor dir gelten als ein Räucheropfer, das Aufheben meiner Hände als ein Abendopfer.“ (Psalm 141:2)"
            AppLanguage.ITALIAN -> "«Salga a te la mia preghiera come l'incenso, l'elevarsi delle mie mani come sacrificio della sera.» (Salmo 141:2)"
            AppLanguage.CHINESE -> "“愿我的祷告如香陈列在你面前；愿我举手祈求，如献晚祭。” (诗篇 141:2)"
            AppLanguage.JAPANESE -> "「私の祈りが御前に香のように立ちのぼり、手を上げることが夕べのいけにえとなりますように。」(詩篇 141:2)"
            AppLanguage.KOREAN -> "\"나의 기도가 주의 앞에 분향함과 같이 되며 나의 손 드는 것이 저녁 제사 같이 되게 하소서\" (시편 141:2)"
        }
        COMPLINE -> when (lang) {
            AppLanguage.ARABIC, AppLanguage.SYRIAN_ARABIC -> "«الآن تطلق عبدك يا سيدي بسلام حسب قولك، لأن عيني قد أبصرتا خلاصك» (لوقا 2: 29-30)"
            AppLanguage.SYRIAC -> "«ܡܟܝܠ ܫܪܐ ܐܢܬ ܠܥܒܕܟ ܡܪܝ ܒܫܠܡܐ ܐܝܟ ܡܠܬܟ» (ܠܘܩܐ ܒ: ܟܛ)"
            AppLanguage.COPTIC -> "«Ϯⲛⲟⲩ ⲭⲁ ⲡⲉⲕⲃⲱⲕ ⲉ̀ⲃⲟⲗ ϧⲉⲛ ⲟⲩϩⲓⲣⲏⲛⲏ ⲕⲁⲧⲁ ⲡⲉⲕⲥⲁϫⲓ»"
            AppLanguage.ENGLISH -> "\"Lord, now You are letting Your servant depart in peace, according to Your word; for my eyes have seen Your salvation.\" (Luke 2:29-30)"
            AppLanguage.FRENCH -> "« Maintenant, Seigneur, tu laisses ton serviteur s'en aller en paix, selon ta parole. Car mes yeux ont vu ton salut. » (Luc 2:29-30)"
            AppLanguage.SPANISH -> "«Ahora, Señor, despides a tu siervo en paz, conforme a tu palabra; porque han visto mis ojos tu salvación.» (Lucas 2:29-30)"
            AppLanguage.GERMAN, AppLanguage.AUSTRIAN_GERMAN, AppLanguage.SWISS_GERMAN -> "„Nun lässt du deinen Diener in Frieden scheiden, Herr, wie du gesagt hast; denn meine Augen haben das Heil gesehen.“ (Lukas 2:29-30)"
            AppLanguage.ITALIAN -> "«Ora lascia, o Signore, che il tuo servo vada in pace secondo la tua parola; perché i miei occhi han visto la tua salvezza.» (Luca 2:29-30)"
            AppLanguage.CHINESE -> "“主啊，如今可以照你的话，容你的仆人安然去世，因为我的眼睛已经看见你的救恩。” (路加福音 2:29-30)"
            AppLanguage.JAPANESE -> "「主よ、今こそあなたはみ言葉のとおり、僕を安らかに去らせてくださいます。私の目があなたの救いを見たからです。」(ルカ 2:29-30)"
            AppLanguage.KOREAN -> "\"주재여 이제는 말씀하신 대로 종을 평안히 놓아 주시는도다 내 눈이 주의 구원을 보았사오니\" (누가복음 2:29-30)"
        }
        VEIL -> when (lang) {
            AppLanguage.ARABIC, AppLanguage.SYRIAN_ARABIC -> "«من الأعماق صرخت إليك يا رب، يا رب استمع صوتي لتكن أذناك مصغيتين لصوت تضرعي» (مزمور 130: 1-2)"
            AppLanguage.SYRIAC -> "«ܡܢ ܥܘܡܩܐ ܩܪܝܬܟ ܡܪܝܐ ܡܪܝ ܫܡܥ ܒܩܠܝ» (ܡܙܡܘܪܐ ܩܠ: ܐ)"
            AppLanguage.COPTIC -> "«Ⲉ̀ⲃⲟⲗ ϧⲉⲛ ⲛⲓϣⲏⲕ ⲁⲓⲱϣ ⲉ̀ⲡϣⲱⲓ ϩⲁⲣⲟⲕ Ⲡϭⲟⲓⲥ»"
            AppLanguage.ENGLISH -> "\"Out of the depths I have cried to You, O Lord; Lord, hear my voice! Let Your ears be attentive to the voice of my supplications.\" (Psalm 130:1-2)"
            AppLanguage.FRENCH -> "« Des profondeurs je crie vers toi, Seigneur. Seigneur, écoute mon appel ! » (Psaume 130:1-2)"
            AppLanguage.SPANISH -> "«De lo profundo, oh Señor, a ti clamo. Señor, oye mi voz; estén atentos tus oídos a la voz de mi súplica.» (Salmo 130:1-2)"
            AppLanguage.GERMAN, AppLanguage.AUSTRIAN_GERMAN, AppLanguage.SWISS_GERMAN -> "„Aus der Tiefe rufe ich, Herr, zu dir. Herr, höre meine Stimme!“ (Psalm 130:1-2)"
            AppLanguage.ITALIAN -> "«Dal profondo a te grido, o Signore; Signore, ascolta la mia voce.» (Salmo 130:1-2)"
            AppLanguage.CHINESE -> "“耶和华啊，我从深处向你求告。主啊，求你听我的声音，愿你侧耳听我恳求的声音。” (诗篇 130:1-2)"
            AppLanguage.JAPANESE -> "「主よ、深い淵から私はあなたに叫びます。主よ、私の声を聞き、私の願いの声に耳を傾けてください。」(詩篇 130:1-2)"
            AppLanguage.KOREAN -> "\"여호와여 내가 깊은 곳에서 주께 부르짖었나이다 주여 내 소리를 들으시며 나의 부르짖는 소리에 귀를 기울이소서\" (시편 130:1-2)"
        }
        MIDNIGHT -> when (lang) {
            AppLanguage.ARABIC, AppLanguage.SYRIAN_ARABIC -> "«وفي نصف الليل صار صراخ: هوذا العريس مقبل فاخرجن للقائه» (متى 25: 6)"
            AppLanguage.SYRIAC -> "«ܒܦܠܓܗ ܕܠܠܝܐ ܗܘܬ ܩܥܬܐ: ܗܐ ܚܬܢܐ ܐܬܐ ܦܘܩܘ ܠܐܘܪܥܗ» (ܡܬܝ ܟܗ: ܘ)"
            AppLanguage.COPTIC -> "«Ϧⲉⲛ ⲧⲫⲁϣⲓ ⲇⲉ ⲛ̀ϫⲱⲣϩ ⲟⲩϧⲣⲱⲟⲩ ⲁϥϣⲱⲡⲓ: Ϩⲏⲡⲡⲉ ⲓⲥ ⲡⲓⲡⲁⲧϣⲉⲗⲉⲧ ϥⲛⲏⲟⲩ»"
            AppLanguage.ENGLISH -> "\"And at midnight a cry was heard: 'Behold, the bridegroom is coming; go out to meet him!'\" (Matthew 25:6)"
            AppLanguage.FRENCH -> "« Au milieu de la nuit, un cri se fit entendre : 'Voici l'Époux qui vient, sortez à sa rencontre !' » (Matthieu 25:6)"
            AppLanguage.SPANISH -> "«Y a la medianoche se oyó un clamor: '¡Aquí viene el esposo; salid a recibirle!'» (Mateo 25:6)"
            AppLanguage.GERMAN, AppLanguage.AUSTRIAN_GERMAN, AppLanguage.SWISS_GERMAN -> "„Um Mitternacht aber erhob sich lautes Rufen: Siehe, der Bräutigam kommt! Geht hinaus, ihm entgegen!“ (Matthäus 25:6)"
            AppLanguage.ITALIAN -> "«A mezzanotte si alzò un grido: 'Ecco lo sposo, andategli incontro!'» (Matteo 25:6)"
            AppLanguage.CHINESE -> "“半夜有人喊着说：‘新郎来了，你们出来迎接他！’” (马太福音 25:6)"
            AppLanguage.JAPANESE -> "「夜中に叫ぶ声がした。『花婿だ、迎えに出なさい』。」(マタイ 25:6)"
            AppLanguage.KOREAN -> "\"밤중에 소리가 나되 보라 신랑이로다 맞으러 나오라 하매\" (마태복음 25:6)"
        }
    }

    companion object {
        val canonicalPrayers = listOf(PRIME, TERCE, SEXT, NONE, VESPERS, COMPLINE, MIDNIGHT, VEIL)
        fun fromCode(code: String): PrayerId = entries.find { it.code.equals(code, ignoreCase = true) } ?: PRIME
    }
}
