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
            AppLanguage.COPTIC -> "Ϯⲉⲩⲭⲏ ⲛ̀ϯϣⲟⲣⲡ"
            AppLanguage.ENGLISH -> "Prime (Morning Prayer)"
            AppLanguage.FRENCH -> "Prière de l'Aube (Prime)"
            AppLanguage.SPANISH -> "Prima (Oración Matutina)"
            AppLanguage.GERMAN -> "Prim (Morgegebet)"
            AppLanguage.ITALIAN -> "Prima (Preghiera del Mattino)"
        }
        TERCE -> when (lang) {
            AppLanguage.ARABIC -> "صلاة الساعة الثالثة"
            AppLanguage.COPTIC -> "Ϯⲉⲩⲭⲏ ⲛ̀ϯⲁϫⲡ ⲅ̅"
            AppLanguage.ENGLISH -> "Terce (3rd Hour)"
            AppLanguage.FRENCH -> "Tierce (3ème Heure)"
            AppLanguage.SPANISH -> "Tercia (3ª Hora)"
            AppLanguage.GERMAN -> "Terz (3. Stunde)"
            AppLanguage.ITALIAN -> "Terza (3ª Ora)"
        }
        SEXT -> when (lang) {
            AppLanguage.ARABIC -> "صلاة الساعة السادسة"
            AppLanguage.COPTIC -> "Ϯⲉⲩⲭⲏ ⲛ̀ϯⲁϫⲡ ⲋ̅"
            AppLanguage.ENGLISH -> "Sext (6th Hour)"
            AppLanguage.FRENCH -> "Sexte (6ème Heure)"
            AppLanguage.SPANISH -> "Sexta (6ª Hora)"
            AppLanguage.GERMAN -> "Sext (6. Stunde)"
            AppLanguage.ITALIAN -> "Sesta (6ª Ora)"
        }
        NONE -> when (lang) {
            AppLanguage.ARABIC -> "صلاة الساعة التاسعة"
            AppLanguage.COPTIC -> "Ϯⲉⲩⲭⲏ ⲛ̀ϯⲁϫⲡ ⲑ̅"
            AppLanguage.ENGLISH -> "None (9th Hour)"
            AppLanguage.FRENCH -> "None (9ème Heure)"
            AppLanguage.SPANISH -> "Nona (9ª Hora)"
            AppLanguage.GERMAN -> "Non (9. Stunde)"
            AppLanguage.ITALIAN -> "Nona (9ª Ora)"
        }
        VESPERS -> when (lang) {
            AppLanguage.ARABIC -> "صلاة الغروب (الحادية عشر)"
            AppLanguage.COPTIC -> "Ϯⲉⲩⲭⲏ ⲛ̀ϯⲁϫⲡ ⲓ̅ⲁ̅"
            AppLanguage.ENGLISH -> "Vespers (11th Hour)"
            AppLanguage.FRENCH -> "Vêpres (11ème Heure)"
            AppLanguage.SPANISH -> "Vísperas (11ª Hora)"
            AppLanguage.GERMAN -> "Vesper (11. Stunde)"
            AppLanguage.ITALIAN -> "Vespri (11ª Ora)"
        }
        COMPLINE -> when (lang) {
            AppLanguage.ARABIC -> "صلاة النوم (الثانية عشر)"
            AppLanguage.COPTIC -> "Ϯⲉⲩⲭⲏ ⲛ̀ϯⲁϫⲡ ⲓ̅ⲃ̅"
            AppLanguage.ENGLISH -> "Compline (12th Hour)"
            AppLanguage.FRENCH -> "Complies (12ème Heure)"
            AppLanguage.SPANISH -> "Completas (12ª Hora)"
            AppLanguage.GERMAN -> "Komplet (12. Stunde)"
            AppLanguage.ITALIAN -> "Compieta (12ª Ora)"
        }
        VEIL -> when (lang) {
            AppLanguage.ARABIC -> "صلاة الستار"
            AppLanguage.COPTIC -> "Ϯⲉⲩⲭⲏ ⲙ̀ⲡⲓⲕⲁⲧⲁⲡⲉⲧⲁⲥⲙⲁ"
            AppLanguage.ENGLISH -> "The Veil (Monastic Hour)"
            AppLanguage.FRENCH -> "Prière du Voile"
            AppLanguage.SPANISH -> "Oración del Velo"
            AppLanguage.GERMAN -> "Schleier-Gebet"
            AppLanguage.ITALIAN -> "Preghiera del Velo"
        }
        MIDNIGHT -> when (lang) {
            AppLanguage.ARABIC -> "صلاة نصف الليل"
            AppLanguage.COPTIC -> "Ϯⲉⲩⲭⲏ ⲙ̀ⲡⲓⲫⲁϣⲓ ⲛ̀ϫⲱⲣϩ"
            AppLanguage.ENGLISH -> "Midnight Service"
            AppLanguage.FRENCH -> "Office de Minuit"
            AppLanguage.SPANISH -> "Oficio de Medianoche"
            AppLanguage.GERMAN -> "Mitternachtsgebet"
            AppLanguage.ITALIAN -> "Ufficio di Mezzanotte"
        }
    }

    fun getSpiritualTheme(lang: AppLanguage): String = when (this) {
        PRIME -> when (lang) {
            AppLanguage.ARABIC -> "تذكار قيامة مخلصنا الصالح، وإشراق النور الحقيقي في قلوبنا وبداية اليوم مع الله."
            AppLanguage.COPTIC -> "Ⲡⲓⲙⲉⲩⲓ ⲛ̀ϯⲁⲛⲁⲥⲧⲁⲥⲓⲥ ⲛ̀ⲧⲉ Ⲡⲉⲛⲥⲱⲧⲏⲣ ⲙ̀ⲡⲓⲟⲩⲱⲓⲛⲓ ⲛ̀ⲧⲁⲫⲙⲏⲓ."
            AppLanguage.ENGLISH -> "Commemoration of the glorious Resurrection of Christ, the True Light, and greeting the new day with thanksgiving."
            AppLanguage.FRENCH -> "Commémoration de la Résurrection du Christ, la Vraie Lumière, et remerciement pour le nouveau jour."
            AppLanguage.SPANISH -> "Conmemoración de la Resurrección de Cristo, la Luz Verdadera, y alabanza al iniciar el día."
            AppLanguage.GERMAN -> "Gedenken an die Auferstehung Christi, das wahre Licht, und Dank für den neuen Morgen."
            AppLanguage.ITALIAN -> "Commemorazione della Risurrezione di Cristo, la Vera Luce, e ringraziamento all'alba del giorno."
        }
        TERCE -> when (lang) {
            AppLanguage.ARABIC -> "تذكار حلول الروح القدس المعزي على التلاميذ الأطهار في يوم الخمسين، وتذكار محاكمة بيلاطس."
            AppLanguage.COPTIC -> "Ⲡⲓϫⲓⲛⲓ̀ ⲉ̀ϧⲣⲏⲓ ⲛ̀ⲧⲉ Ⲡⲓⲡⲛⲉⲩⲙⲁ ⲉⲑⲟⲩⲁⲃ Ⲡⲓⲡⲁⲣⲁⲕⲗⲏⲧⲟⲥ."
            AppLanguage.ENGLISH -> "Commemoration of the descent of the Holy Spirit on Pentecost and the trial of Christ before Pontius Pilate."
            AppLanguage.FRENCH -> "Commémoration de la descente du Saint-Esprit le jour de la Pentecôte et du procès de Pilate."
            AppLanguage.SPANISH -> "Conmemoración de la venida del Espíritu Santo en Pentecostés y el juicio de Pilato."
            AppLanguage.GERMAN -> "Gedenken an das Herabkommen des Heiligen Geistes an Pfingsten und das Verhör vor Pilatus."
            AppLanguage.ITALIAN -> "Commemorazione della discesa dello Spirito Santo a Pentecoste e del processo davanti a Pilato."
        }
        SEXT -> when (lang) {
            AppLanguage.ARABIC -> "تذكار صلب فادينا يسوع المسيح على الجلجثة في منتصف النهار لأجل خلاص جنس البشر."
            AppLanguage.COPTIC -> "Ⲡⲓⲁϣⲓ ⲉ̀ϫⲉⲛ ⲡⲓⲥⲧⲁⲩⲣⲟⲥ ϧⲉⲛ ϯⲕⲣⲁⲛⲓⲟⲛ ⲉ̀ⲑⲃⲉ ⲡⲉⲛⲟⲩϫⲁⲓ."
            AppLanguage.ENGLISH -> "Commemoration of the Crucifixion and Passion of our Lord Jesus Christ on Golgotha at midday."
            AppLanguage.FRENCH -> "Commémoration de la Crucifixion de notre Seigneur Jésus-Christ au Calvaire à midi."
            AppLanguage.SPANISH -> "Conmemoración de la Crucifixión de nuestro Señor Jesucristo en el Gólgota al mediodía."
            AppLanguage.GERMAN -> "Gedenken an die Kreuzigung unseres Herrn Jesus Christus auf Golgatha zur Mittagszeit."
            AppLanguage.ITALIAN -> "Commemorazione della Crocifissione di nostro Signore Gesù Cristo sul Golgota a mezzogiorno."
        }
        NONE -> when (lang) {
            AppLanguage.ARABIC -> "تذكار موت المسيح بالجسد على الصليب، وقبول توبة اللص اليمين، وخلاص العالم."
            AppLanguage.COPTIC -> "Ⲡⲓⲙⲟⲩ ⲛ̀ⲧⲉ Ⲡⲭ̅ⲥ̅ ϧⲉⲛ ⲧⲥⲁⲣⲝ ⲉ̀ϫⲉⲛ ⲡⲓⲥⲧⲁⲩⲣⲟⲥ."
            AppLanguage.ENGLISH -> "Commemoration of the life-giving death of Christ on the Cross and the salvation of the penitent thief."
            AppLanguage.FRENCH -> "Commémoration de la mort du Christ sur la Croix et du salut du bon larron."
            AppLanguage.SPANISH -> "Conmemoración de la muerte vivificante de Cristo en la Cruz y el perdón del ladrón arrepentido."
            AppLanguage.GERMAN -> "Gedenken an den lebenspendenden Tod Christi am Kreuz und die Rettung des reumütigen Schächers."
            AppLanguage.ITALIAN -> "Commemorazione della morte vivificante di Cristo sulla Croce e della salvezza del buon ladrone."
        }
        VESPERS -> when (lang) {
            AppLanguage.ARABIC -> "تذكار إنزال الجسد المقدس من على الصليب، وشكر الله على حفظنا خلال ساعات النهار."
            AppLanguage.COPTIC -> "Ⲡⲓⲓⲛⲓ ⲉ̀ⲡⲉⲥⲏⲧ ⲛ̀ⲧⲉ ⲡⲓⲥⲱⲙⲁ ⲉⲑⲟⲩⲁⲃ ⲉ̀ⲃⲟⲗ ϩⲓ ⲡⲓⲥⲧⲁⲩⲣⲟⲥ."
            AppLanguage.ENGLISH -> "Commemoration of taking down Christ's sacred body from the Cross and thanksgiving at sunset."
            AppLanguage.FRENCH -> "Commémoration de la descente de Croix du corps sacré du Christ et action de grâce au coucher du soleil."
            AppLanguage.SPANISH -> "Conmemoración del descendimiento de la Cruz del cuerpo santo de Cristo y acción de gracias al atardecer."
            AppLanguage.GERMAN -> "Gedenken an die Kreuzabnahme des heiligen Leibes Christi und Dankgebet bei Sonnenuntergang."
            AppLanguage.ITALIAN -> "Commemorazione della deposizione dalla Croce del corpo sacro di Cristo e ringraziamento al tramonto."
        }
        COMPLINE -> when (lang) {
            AppLanguage.ARABIC -> "تذكار دفن الجسد الطاهر في القبر، وتذكر الموت والدينونة والخلود، وطلب حراسة ملائكة السلامة في النوم."
            AppLanguage.COPTIC -> "Ⲡⲓϫⲓⲛⲭⲱ ϧⲉⲛ ⲡⲓⲙϩⲁⲟⲩ ⲉⲑⲟⲩⲁⲃ ⲛ̀ⲧⲉ Ⲡⲉⲛⲥⲱⲧⲏⲣ."
            AppLanguage.ENGLISH -> "Commemoration of the burial of Christ in the tomb, reflection on eternity, and prayer for peaceful rest under divine protection."
            AppLanguage.FRENCH -> "Commémoration de la mise au tombeau du Christ, méditation sur l'éternité et prière pour un sommeil paisible."
            AppLanguage.SPANISH -> "Conmemoración de la sepultura de Cristo, examen de conciencia y descanso en la paz de Dios."
            AppLanguage.GERMAN -> "Gedenken an die Grablegung Christi, Besinnung auf die Ewigkeit und Bitte um behüteten Schlaf."
            AppLanguage.ITALIAN -> "Commemorazione della sepoltura di Cristo, esame di coscienza e preghiera per il riposo notturno."
        }
        VEIL -> when (lang) {
            AppLanguage.ARABIC -> "صلاة رهبانية خاصة بالتوبة والانسحاق العميق وطلب مراحم الرب قبل هجوع الليل."
            AppLanguage.COPTIC -> "Ϯⲙⲉⲧⲁⲛⲟⲓⲁ ⲛ̀ⲧⲉ ⲛⲓⲙⲟⲛⲁⲭⲟⲥ ϧⲁⲧⲉⲛ ⲡⲓϫⲱⲕ."
            AppLanguage.ENGLISH -> "Monastic prayer of deep repentance, compunction, and vigilance before the midnight watch."
            AppLanguage.FRENCH -> "Prière monastique de repentir profond et de vigilance avant la garde de minuit."
            AppLanguage.SPANISH -> "Oración monástica de profundo arrepentimiento y vigilancia nocturna."
            AppLanguage.GERMAN -> "Mönchisches Bußgebet tiefster Reue und Wachsamkeit vor der Mitternacht."
            AppLanguage.ITALIAN -> "Preghiera monastica di sincero pentimento e veglia notturna."
        }
        MIDNIGHT -> when (lang) {
            AppLanguage.ARABIC -> "تذكار المجيء الثاني لربنا يسوع المسيح والسهر الروحي: «ها العريس يأتي في نصف الليل فطوبى للعبد الذي يجده ساهراً»."
            AppLanguage.COPTIC -> "Ⲡⲓⲙⲉⲩⲓ ⲛ̀ϯⲙⲁϩⲥⲛⲟⲩϯ ⲙ̀ⲡⲁⲣⲟⲩⲥⲓⲁ: Ϩⲏⲡⲡⲉ ⲓⲥ ⲡⲓⲡⲁⲧϣⲉⲗⲉⲧ ϥⲛⲏⲟⲩ ϧⲉⲛ ⲧⲫⲁϣⲓ ⲛ̀ϫⲱⲣϩ."
            AppLanguage.ENGLISH -> "Commemoration of the Second Coming of Christ and spiritual watchfulness: 'Behold, the Bridegroom cometh at midnight'."
            AppLanguage.FRENCH -> "Commémoration du Second Avènement du Christ et vigilance spirituelle : « Voici l'Époux qui vient au milieu de la nuit »."
            AppLanguage.SPANISH -> "Conmemoración de la Segunda Venida de Cristo y vigilancia espiritual: «He aquí el Esposo viene a medianoche»."
            AppLanguage.GERMAN -> "Gedenken an die Wiederkunft Christi und geistliche Wachsamkeit: „Siehe, der Bräutigam kommt um Mitternacht“."
            AppLanguage.ITALIAN -> "Commemorazione della Seconda Venuta di Cristo e vigilanza: «Ecco, lo Sposo arriva a mezzanotte»."
        }
    }

    fun getKeyVerse(lang: AppLanguage): String = when (this) {
        PRIME -> when (lang) {
            AppLanguage.ARABIC -> "«يا الله أنت إلهي، إليك أبكر، عطشت إليك نفسي» (مزمور 63: 1)"
            AppLanguage.COPTIC -> "«Ⲫⲛⲟⲩϯ ⲡⲁⲛⲟⲩϯ ϯⲛⲁϣⲟⲣⲡ ⲉ̀ⲣⲟⲓ ϩⲁⲣⲟⲕ»"
            AppLanguage.ENGLISH -> "\"O God, You are my God; early will I seek You; my soul thirsts for You.\" (Psalm 63:1)"
            AppLanguage.FRENCH -> "« Ô Dieu ! Tu es mon Dieu, je te cherche dès l'aube; mon âme a soif de toi. » (Psaume 63:1)"
            AppLanguage.SPANISH -> "«Dios, Dios mío eres tú; de madrugada te buscaré; mi alma tiene sed de ti.» (Salmo 63:1)"
            AppLanguage.GERMAN -> "„Gott, du bist mein Gott; frühe suche ich dich; es dürstet meine Seele nach dir.“ (Psalm 63:1)"
            AppLanguage.ITALIAN -> "«O Dio, tu sei il mio Dio, dall'aurora ti cerco; ha sete di te l'anima mia.» (Salmo 63:1)"
        }
        TERCE -> when (lang) {
            AppLanguage.ARABIC -> "«روحك القدوس لا تنزعه مني، رد لي بهجة خلاصك» (مزمور 51: 11)"
            AppLanguage.COPTIC -> "«Ⲡⲉⲕⲡⲛⲉⲩⲙⲁ ⲉⲑⲟⲩⲁⲃ ⲙ̀ⲡⲉⲣⲟⲗϥ ⲉ̀ⲃⲟⲗ ϩⲁⲣⲟⲓ»"
            AppLanguage.ENGLISH -> "\"Do not cast me away from Your presence, and do not take Your Holy Spirit from me.\" (Psalm 51:11)"
            AppLanguage.FRENCH -> "« Ne me rejette pas loin de ta face, ne me retire pas ton Esprit Saint. » (Psaume 51:11)"
            AppLanguage.SPANISH -> "«No me eches de delante de ti, y no quites de mí tu Santo Espíritu.» (Salmo 51:11)"
            AppLanguage.GERMAN -> "„Verwirf mich nicht von deinem Angesicht und nimm deinen heiligen Geist nicht von mir.“ (Psalm 51:11)"
            AppLanguage.ITALIAN -> "«Non respingermi dalla tua presenza e non privarmi del tuo santo spirito.» (Salmo 51:11)"
        }
        SEXT -> when (lang) {
            AppLanguage.ARABIC -> "«مساميرك سمرت في أقدامي وأيديك، ومسحت كل خطاياي بدمك الطاهر»"
            AppLanguage.COPTIC -> "«Ⲁⲩⲁϣⲧ ϧⲉⲛ ⲧⲁϫⲡ ⲋ̅ ⲉ̀ϫⲉⲛ ⲡⲓⲥⲧⲁⲩⲣⲟⲥ»"
            AppLanguage.ENGLISH -> "\"O You who on the sixth hour of the sixth day nailed the sin of Adam to the Cross, blot out the handwriting of our sins.\""
            AppLanguage.FRENCH -> "« Toi qui, à la sixième heure du sixième jour, as cloué sur la Croix le péché d'Adam, efface nos fautes. »"
            AppLanguage.SPANISH -> "«Tú que en la sexta hora clavaste en la Cruz el pecado del hombre, perdona nuestras transgresiones.»"
            AppLanguage.GERMAN -> "„Der du zur sechsten Stunde am Kreuz die Sünde der Welt getragen hast, vergib uns unsere Schuld.“"
            AppLanguage.ITALIAN -> "«Tu che alla sesta ora hai inchiodato sulla Croce il peccato, cancella le nostre colpe.»"
        }
        NONE -> when (lang) {
            AppLanguage.ARABIC -> "«اذكرني يا رب متى جئت في ملكوتك» (لوقا 23: 42)"
            AppLanguage.COPTIC -> "«Ⲁⲣⲓⲡⲁⲙⲉⲩⲓ ⲱ Ⲡⲁϭⲟⲓⲥ ⲁⲕϣⲁⲛⲓ̀ ϧⲉⲛ ⲧⲉⲕⲙⲉⲧⲟⲩⲣⲟ»"
            AppLanguage.ENGLISH -> "\"Lord, remember me when You come into Your kingdom.\" (Luke 23:42)"
            AppLanguage.FRENCH -> "« Souviens-toi de moi, Seigneur, quand tu viendras dans ton règne. » (Luc 23:42)"
            AppLanguage.SPANISH -> "«Acuérdate de mí, Señor, cuando vengas en tu reino.» (Lucas 23:42)"
            AppLanguage.GERMAN -> "„Herr, gedenke meiner, wenn du in dein Reich kommst!“ (Lukas 23:42)"
            AppLanguage.ITALIAN -> "«Signore, ricordati di me quando entrerai nel tuo regno.» (Luca 23:42)"
        }
        VESPERS -> when (lang) {
            AppLanguage.ARABIC -> "«لتستقم صلاتي كالبخور قدامك، ورفع يدي كذبيحة مسائية» (مزمور 141: 2)"
            AppLanguage.COPTIC -> "«Ⲙⲁⲣⲉ ⲧⲁⲡⲣⲟⲥⲉⲩⲭⲏ ⲥⲱⲟⲩⲧⲉⲛ ⲙ̀ⲫⲣⲏϯ ⲛ̀ⲟⲩⲥⲑⲟⲩⲛⲟⲩϥⲓ ⲙ̀ⲡⲉⲕⲙ̀ⲑⲟ»"
            AppLanguage.ENGLISH -> "\"Let my prayer be set before You as incense, the lifting up of my hands as the evening sacrifice.\" (Psalm 141:2)"
            AppLanguage.FRENCH -> "« Que ma prière soit devant ta face comme l'encens, et l'élévation de mes mains comme le sacrifice du soir. » (Psaume 141:2)"
            AppLanguage.SPANISH -> "«Suba mi oración delante de ti como el incienso, el don de mis manos como la ofrenda vespertina.» (Salmo 141:2)"
            AppLanguage.GERMAN -> "„Mein Gebet möge vor dir gelten als ein Räucheropfer, das Aufheben meiner Hände als ein Abendopfer.“ (Psalm 141:2)"
            AppLanguage.ITALIAN -> "«Salga a te la mia preghiera come l'incenso, l'elevarsi delle mie mani come sacrificio della sera.» (Salmo 141:2)"
        }
        COMPLINE -> when (lang) {
            AppLanguage.ARABIC -> "«الآن تطلق عبدك يا سيدي بسلام حسب قولك، لأن عيني قد أبصرتا خلاصك» (لوقا 2: 29-30)"
            AppLanguage.COPTIC -> "«Ϯⲛⲟⲩ ⲭⲁ ⲡⲉⲕⲃⲱⲕ ⲉ̀ⲃⲟⲗ ϧⲉⲛ ⲟⲩϩⲓⲣⲏⲛⲏ ⲕⲁⲧⲁ ⲡⲉⲕⲥⲁϫⲓ»"
            AppLanguage.ENGLISH -> "\"Lord, now You are letting Your servant depart in peace, according to Your word; for my eyes have seen Your salvation.\" (Luke 2:29-30)"
            AppLanguage.FRENCH -> "« Maintenant, Seigneur, tu laisses ton serviteur s'en aller en paix, selon ta parole. Car mes yeux ont vu ton salut. » (Luc 2:29-30)"
            AppLanguage.SPANISH -> "«Ahora, Señor, despides a tu siervo en paz, conforme a tu palabra; porque han visto mis ojos tu salvación.» (Lucas 2:29-30)"
            AppLanguage.GERMAN -> "„Nun lässt du deinen Diener in Frieden scheiden, Herr, wie du gesagt hast; denn meine Augen haben das Heil gesehen.“ (Lukas 2:29-30)"
            AppLanguage.ITALIAN -> "«Ora lascia, o Signore, che il tuo servo vada in pace secondo la tua parola; perché i miei occhi han visto la tua salvezza.» (Luca 2:29-30)"
        }
        VEIL -> when (lang) {
            AppLanguage.ARABIC -> "«من الأعماق صرخت إليك يا رب، يا رب استمع صوتي لتكن أذناك مصغيتين لصوت تضرعي» (مزمور 130: 1-2)"
            AppLanguage.COPTIC -> "«Ⲉ̀ⲃⲟⲗ ϧⲉⲛ ⲛⲓϣⲏⲕ ⲁⲓⲱϣ ⲉ̀ⲡϣⲱⲓ ϩⲁⲣⲟⲕ Ⲡϭⲟⲓⲥ»"
            AppLanguage.ENGLISH -> "\"Out of the depths I have cried to You, O Lord; Lord, hear my voice! Let Your ears be attentive to the voice of my supplications.\" (Psalm 130:1-2)"
            AppLanguage.FRENCH -> "« Des profondeurs je crie vers toi, Seigneur. Seigneur, écoute mon appel ! » (Psaume 130:1-2)"
            AppLanguage.SPANISH -> "«De lo profundo, oh Señor, a ti clamo. Señor, oye mi voz; estén atentos tus oídos a la voz de mi súplica.» (Salmo 130:1-2)"
            AppLanguage.GERMAN -> "„Aus der Tiefe rufe ich, Herr, zu dir. Herr, höre meine Stimme!“ (Psalm 130:1-2)"
            AppLanguage.ITALIAN -> "«Dal profondo a te grido, o Signore; Signore, ascolta la mia voce.» (Salmo 130:1-2)"
        }
        MIDNIGHT -> when (lang) {
            AppLanguage.ARABIC -> "«وفي نصف الليل صار صراخ: هوذا العريس مقبل فاخرجن للقائه» (متى 25: 6)"
            AppLanguage.COPTIC -> "«Ϧⲉⲛ ⲧⲫⲁϣⲓ ⲇⲉ ⲛ̀ϫⲱⲣϩ ⲟⲩϧⲣⲱⲟⲩ ⲁϥϣⲱⲡⲓ: Ϩⲏⲡⲡⲉ ⲓⲥ ⲡⲓⲡⲁⲧϣⲉⲗⲉⲧ ϥⲛⲏⲟⲩ»"
            AppLanguage.ENGLISH -> "\"And at midnight a cry was heard: 'Behold, the bridegroom is coming; go out to meet him!'\" (Matthew 25:6)"
            AppLanguage.FRENCH -> "« Au milieu de la nuit, un cri se fit entendre : 'Voici l'Époux qui vient, sortez à sa rencontre !' » (Matthieu 25:6)"
            AppLanguage.SPANISH -> "«Y a la medianoche se oyó un clamor: '¡Aquí viene el esposo; salid a recibirle!'» (Mateo 25:6)"
            AppLanguage.GERMAN -> "„Um Mitternacht aber erhob sich lautes Rufen: Siehe, der Bräutigam kommt! Geht hinaus, ihm entgegen!“ (Matthäus 25:6)"
            AppLanguage.ITALIAN -> "«A mezzanotte si alzò un grido: 'Ecco lo sposo, andategli incontro!'» (Matteo 25:6)"
        }
    }

    companion object {
        val canonicalPrayers = listOf(PRIME, TERCE, SEXT, NONE, VESPERS, COMPLINE, MIDNIGHT, VEIL)
        fun fromCode(code: String): PrayerId = entries.find { it.code.equals(code, ignoreCase = true) } ?: PRIME
    }
}
