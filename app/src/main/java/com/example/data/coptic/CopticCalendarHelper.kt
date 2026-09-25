package com.example.data.coptic

import com.example.localization.AppLanguage
import java.util.Calendar
import java.util.Date
import java.util.Locale

enum class FastingType(
    val englishName: String,
    val arabicName: String,
    val iconEmoji: String,
    val descriptionEn: String,
    val descriptionAr: String
) {
    STRICT_FAST(
        "Strict Fast (No Fish)",
        "صوم انقطاعي (ممنوع السمك)",
        "🌿",
        "Abstinence from animal products & fish (Lent, Passion Week, Jonah, Wednesdays & Fridays).",
        "انقطاع مع الامتناع عن الأسماك والمنتجات الحيوانية (الصوم الكبير، البصخة، صوم يونان، الأربعاء والجمعة)."
    ),
    FISH_ALLOWED(
        "Fast (Fish Allowed)",
        "صوم درجة ثانية (يسمح بالسمك)",
        "🐟",
        "Fast where fish is permitted (Nativity Fast, Apostles Fast, St. Mary Fast).",
        "صوم كنسي يُسمح فيه بتناول الأسماك للتخفيف (صوم الميلاد، صوم الرسل، صوم السيدة العذراء)."
    ),
    NON_FASTING_FEAST(
        "Lord's Feast / Festive",
        "عيد سيدي / فطر وفرح",
        "🕊️",
        "Festive non-fasting period or Major/Minor Feast of the Lord.",
        "أيام فطر وأعياد سيدية مبهجة (الخمسين المقدسة والأعياد الكبرى)."
    ),
    REGULAR_DAY(
        "Non-Fasting Day",
        "يوم عادي (فطر)",
        "✝️",
        "Regular non-fasting liturgical day.",
        "يوم عادي في الطقس الكنسي السنوي."
    )
}

data class CopticDate(
    val day: Int,
    val monthIndex: Int, // 1 to 13
    val monthNameEn: String,
    val monthNameAr: String,
    val monthNameCoptic: String,
    val yearAM: Int, // Anno Martyrum (e.g. 1743)
    val fastingType: FastingType,
    val feastNameEn: String?,
    val feastNameAr: String?,
    val commemorationSummaryEn: String,
    val commemorationSummaryAr: String,
    val katamerosPsalmEn: String,
    val katamerosPsalmAr: String,
    val katamerosGospelEn: String,
    val katamerosGospelAr: String
)

object CopticCalendarHelper {

    val copticMonths = listOf(
        Triple("Tout", "توت", "Ⲑⲱⲟⲩⲧ"),
        Triple("Baba", "بابه", "Ⲡⲁⲟⲡⲓ"),
        Triple("Hathor", "هاتور", "Ϩⲁⲑⲱⲣ"),
        Triple("Kiahk", "كيهك", "Ⲭⲟⲓⲁⲕ"),
        Triple("Tobi", "طوبة", "Ⲧⲱⲃⲓ"),
        Triple("Amshir", "أمشير", "Ⲙⲉϣⲓⲣ"),
        Triple("Baramhat", "برمهات", "Ⲡⲁⲣⲉⲙϩⲁⲧ"),
        Triple("Baramouda", "برمودة", "Ⲡⲁⲣⲙⲟⲩⲧⲉ"),
        Triple("Bashans", "بشنس", "Ⲡⲁϣⲟⲛⲥ"),
        Triple("Paona", "بؤونة", "Ⲡⲁⲱⲛⲓ"),
        Triple("Abib", "أبيب", "Ⲉⲡⲏⲡ"),
        Triple("Mesra", "مسرى", "Ⲙⲉⲥⲱⲣⲏ"),
        Triple("Nasie (Little Month)", "النسئ (الشهر الصغير)", "Ⲡⲓⲕⲟⲩϫⲓ ⲛ̀ⲁ̀ⲃⲟⲧ")
    )

    /**
     * Converts a Gregorian Calendar instance to Coptic Date accurately
     */
    fun getCopticDate(calendar: Calendar = Calendar.getInstance()): CopticDate {
        val gYear = calendar.get(Calendar.YEAR)
        val gMonth = calendar.get(Calendar.MONTH) // 0-11
        val gDay = calendar.get(Calendar.DAY_OF_MONTH)
        val dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK) // 1=Sun, 4=Wed, 6=Fri

        // Julian Day Number algorithm for accurate astronomical Coptic date conversion
        val a = (14 - (gMonth + 1)) / 12
        val y = gYear + 4800 - a
        val m = (gMonth + 1) + 12 * a - 3
        val jdn = gDay + (153 * m + 2) / 5 + 365 * y + y / 4 - y / 100 + y / 400 - 32045

        // Coptic Epoch JDN is 1824665 (August 29, 284 AD Julian)
        val copticJdnEpoch = 1824665
        val copticDaysSinceEpoch = jdn - copticJdnEpoch

        val copticYear = (copticDaysSinceEpoch / 1461) * 4 + ((copticDaysSinceEpoch % 1461) / 365)
        val dayOfYear = (copticDaysSinceEpoch % 1461) % 365

        val cMonth = (dayOfYear / 30) + 1
        val cDay = (dayOfYear % 30) + 1

        val clampedMonth = cMonth.coerceIn(1, 13)
        val clampedDay = cDay.coerceIn(1, 30)
        val monthInfo = copticMonths.getOrElse(clampedMonth - 1) { copticMonths[0] }

        val feastInfo = getFeastAndCommemoration(clampedMonth, clampedDay, gMonth, gDay, dayOfWeek)

        return CopticDate(
            day = clampedDay,
            monthIndex = clampedMonth,
            monthNameEn = monthInfo.first,
            monthNameAr = monthInfo.second,
            monthNameCoptic = monthInfo.third,
            yearAM = copticYear + 1,
            fastingType = feastInfo.fastingType,
            feastNameEn = feastInfo.feastEn,
            feastNameAr = feastInfo.feastAr,
            commemorationSummaryEn = feastInfo.commemorationEn,
            commemorationSummaryAr = feastInfo.commemorationAr,
            katamerosPsalmEn = feastInfo.katamerosPsalmEn,
            katamerosPsalmAr = feastInfo.katamerosPsalmAr,
            katamerosGospelEn = feastInfo.katamerosGospelEn,
            katamerosGospelAr = feastInfo.katamerosGospelAr
        )
    }

    private data class DayLiturgicalInfo(
        val fastingType: FastingType,
        val feastEn: String?,
        val feastAr: String?,
        val commemorationEn: String,
        val commemorationAr: String,
        val katamerosPsalmEn: String,
        val katamerosPsalmAr: String,
        val katamerosGospelEn: String,
        val katamerosGospelAr: String
    )

    private fun getFeastAndCommemoration(
        cMonth: Int,
        cDay: Int,
        gMonth: Int,
        gDay: Int,
        dayOfWeek: Int
    ): DayLiturgicalInfo {
        val isWedOrFri = (dayOfWeek == Calendar.WEDNESDAY || dayOfWeek == Calendar.FRIDAY)

        // 1. Check Major / Minor Fixed Feasts
        // 1 Tout: Nayrouz / Coptic New Year
        if (cMonth == 1 && cDay == 1) {
            return DayLiturgicalInfo(
                fastingType = FastingType.NON_FASTING_FEAST,
                feastEn = "Feast of El-Nayrouz (Coptic New Year)",
                feastAr = "عيد النيروز المجيد (رأس السنة القبطية وتذكار الشهداء)",
                commemorationEn = "Commemoration of the Glorious Martyrs of the Coptic Church and the dawn of the Year of the Martyrs (Anno Martyrum).",
                commemorationAr = "تذكار الشهداء القديسين الأبرار وافتتاح السنة القبطية التوتية المباركة.",
                katamerosPsalmEn = "Psalm 65:11 - 'You crown the year with Your goodness.'",
                katamerosPsalmAr = "مزمور 65: 11 - «باركت إكليل السنة بصلاحك وبقاعك تمتلئ دسماً.»",
                katamerosGospelEn = "Luke 4:16-22 (The acceptable year of the Lord)",
                katamerosGospelAr = "لوقا 4: 16-22 (سنة الرب المقبولة وبشارة الخلاص)"
            )
        }

        // 17 Tout: Feast of the Glorious Cross
        if (cMonth == 1 && cDay in 17..19) {
            return DayLiturgicalInfo(
                fastingType = FastingType.NON_FASTING_FEAST,
                feastEn = "Feast of the Holy Cross",
                feastAr = "عيد الصليب المجيد",
                commemorationEn = "Apparition and discovery of the Precious Life-giving Cross of our Lord Jesus Christ by Queen Helena.",
                commemorationAr = "تذكار ظهور الصليب المقدس المحيي للملكة القديسة هيلانة في أورشليم.",
                katamerosPsalmEn = "Psalm 98:1-2 - 'Oh, sing to the Lord a new song!'",
                katamerosPsalmAr = "مزمور 98: 1-2 - «رنموا للرب ترنيمة جديدة لأنه صنع عجائب.»",
                katamerosGospelEn = "John 8:28-42 (When you lift up the Son of Man)",
                katamerosGospelAr = "يوحنا 8: 28-42 (متى رفعتم ابن الإنسان فحينئذ تفهمون أني أنا هو)"
            )
        }

        // 28 / 29 Kiahk: Glorious Nativity of Christ
        if (cMonth == 4 && (cDay == 28 || cDay == 29)) {
            return DayLiturgicalInfo(
                fastingType = FastingType.NON_FASTING_FEAST,
                feastEn = "Glorious Feast of the Nativity",
                feastAr = "عيد الميلاد المجيد لمخلصنا يسوع المسيح",
                commemorationEn = "The Incarnation and Birth of our Lord and Savior Jesus Christ from the Holy Virgin Mary in Bethlehem.",
                commemorationAr = "تذكار ميلاد ربنا وإلهنا ومخلصنا يسوع المسيح بالجسد في مزود بيت لحم.",
                katamerosPsalmEn = "Psalm 2:7 - 'You are My Son, Today I have begotten You.'",
                katamerosPsalmAr = "مزمور 2: 7 - «أنت ابني، أنا اليوم ولدتك. اسألني فأعطيك الأمم ميراثاً.»",
                katamerosGospelEn = "Matthew 2:1-12 (The Adoration of the Magi)",
                katamerosGospelAr = "متى 2: 1-12 (سجود المجوس وتقديم الهدايا للطفل يسوع)"
            )
        }

        // 11 Tobi: Holy Theophany / Epiphany
        if (cMonth == 5 && cDay == 11) {
            return DayLiturgicalInfo(
                fastingType = FastingType.NON_FASTING_FEAST,
                feastEn = "Feast of the Holy Theophany (Epiphany)",
                feastAr = "عيد الغطاس المجيد (الظهور الإلهي في نهر الأردن)",
                commemorationEn = "The Holy Baptism of our Lord Jesus Christ by St. John the Baptist and the manifestation of the Holy Trinity.",
                commemorationAr = "معمودية مخلصنا الصالح في نهر الأردن بيدي القديس يوحنا المعمدان وظهور الثالوث القدوس.",
                katamerosPsalmEn = "Psalm 114:3 - 'The sea saw it and fled; Jordan turned back.'",
                katamerosPsalmAr = "مزمور 114: 3 - «البحر رآه فهرب، الأردن رجع إلى خلف.»",
                katamerosGospelEn = "Matthew 3:13-17 (This is My beloved Son)",
                katamerosGospelAr = "متى 3: 13-17 (هذا هو ابني الحبيب الذي به سررت)"
            )
        }

        // 29 Baramhat: Holy Annunciation
        if (cMonth == 7 && cDay == 29) {
            return DayLiturgicalInfo(
                fastingType = FastingType.NON_FASTING_FEAST,
                feastEn = "Feast of the Annunciation",
                feastAr = "عيد البشارة المجيد لستنا مريم العذراء",
                commemorationEn = "The Annunciation of the Archangel Gabriel to the Holy Mother of God of the birth of the Savior.",
                commemorationAr = "بشارة الملاك الجليل جبرائيل لستنا العذراء مريم بميلاد مخلص العالم.",
                katamerosPsalmEn = "Psalm 45:10-11 - 'Listen, O daughter, Consider and incline your ear.'",
                katamerosPsalmAr = "مزمور 45: 10-11 - «اسمعي يا ابنة وانظري وأميلي أذنكِ.»",
                katamerosGospelEn = "Luke 1:26-38 (The Annunciation)",
                katamerosGospelAr = "لوقا 1: 26-38 (ها أنت ستحبلين وتلدين ابناً وتسمينه يسوع)"
            )
        }

        // 16 Mesori: Assumption of the Holy Virgin Mary
        if (cMonth == 12 && cDay == 16) {
            return DayLiturgicalInfo(
                fastingType = FastingType.NON_FASTING_FEAST,
                feastEn = "Feast of the Assumption of St. Mary",
                feastAr = "عيد صعود جسد القديسة مريم العذراء",
                commemorationEn = "The Glorious Assumption of the immaculate body of the Theotokos to Heaven.",
                commemorationAr = "تذكار صعود جسد والدة الإله القديسة الطاهرة مريم العذراء إلى السماء.",
                katamerosPsalmEn = "Psalm 45:9 - 'At Your right hand stands the queen in gold from Ophir.'",
                katamerosPsalmAr = "مزمور 45: 9 - «قامت الملكة عن يمينك بذهب أوفير.»",
                katamerosGospelEn = "Luke 1:39-56 (The Magnificat)",
                katamerosGospelAr = "لوقا 1: 39-56 (تعظم نفسي الرب وتبتهج روحي بالله مخلصي)"
            )
        }

        // 5 Epip: Apostles Peter and Paul Feast
        if (cMonth == 11 && cDay == 5) {
            return DayLiturgicalInfo(
                fastingType = FastingType.NON_FASTING_FEAST,
                feastEn = "Feast of the Apostles Peter and Paul",
                feastAr = "عيد الرسل الأطهار واستشهاد القديسين بطرس وبولس",
                commemorationEn = "Martyrdom of the Holy Apostles St. Peter and St. Paul in Rome.",
                commemorationAr = "تذكار استشهاد هامتَي الرسل القديس بطرس والقديس بولس في رومية.",
                katamerosPsalmEn = "Psalm 19:4 - 'Their line has gone out through all the earth.'",
                katamerosPsalmAr = "مزمور 19: 4 - «في كل الأرض خرج منطقهم، وإلى أقطار المسكونة بلغت أقوالهم.»",
                katamerosGospelEn = "Matthew 10:1-15 (The sending of the Twelve)",
                katamerosGospelAr = "متى 10: 1-15 (دعوة الرسل وإرسالهم للكرازة بملكوت السموات)"
            )
        }

        // 2. Monthly Commemorations
        // 12th of each Coptic month: Archangel Michael
        if (cDay == 12) {
            val fast = if (cMonth in 4..4 || (cMonth == 12 && cDay <= 15)) FastingType.FISH_ALLOWED
            else if (isWedOrFri) FastingType.STRICT_FAST else FastingType.REGULAR_DAY

            return DayLiturgicalInfo(
                fastingType = fast,
                feastEn = "Monthly Commemoration of Archangel Michael",
                feastAr = "تذكار رئيس الملائكة الجليل ميخائيل",
                commemorationEn = "Monthly commemoration of Archangel Michael, commander of the Heavenly Host and protector of believers.",
                commemorationAr = "تذكار الملاك الجليل ميخائيل رئيس جند الرب والشفيع المنجي لشعب الله.",
                katamerosPsalmEn = "Psalm 103:20 - 'Bless the Lord, you His angels.'",
                katamerosPsalmAr = "مزمور 103: 20 - «باركوا الرب يا ملائكته المقتدرين قوة الفاعلين أمره.»",
                katamerosGospelEn = "Matthew 13:41-43 (The Angels of the Kingdom)",
                katamerosGospelAr = "متى 13: 41-43 (يرسل ابن الإنسان ملائكته فيجمعون من ملكوته كل المعاثر)"
            )
        }

        // 21st of each Coptic month: Theotokos St. Mary
        if (cDay == 21) {
            val fast = if (isWedOrFri) FastingType.STRICT_FAST else FastingType.REGULAR_DAY
            return DayLiturgicalInfo(
                fastingType = fast,
                feastEn = "Monthly Commemoration of the Holy Virgin Mary",
                feastAr = "تذكار القديسة الطاهرة مريم العذراء والدة الإله",
                commemorationEn = "Monthly remembrance of the pure Theotokos, our heavenly intercessor before Christ.",
                commemorationAr = "التذكار الشهري لستنا مريم العذراء فخر جنسنا وشفيعتنا المؤتمنة أمام العرش الإلهي.",
                katamerosPsalmEn = "Psalm 87:3 - 'Glorious things are spoken of you, O city of God!'",
                katamerosPsalmAr = "مزمور 87: 3 - «أعمال مجيدة قد قيلت عنكِ يا مدينة الله.»",
                katamerosGospelEn = "Luke 11:27-28 (Blessed is the womb that bore You)",
                katamerosGospelAr = "لوقا 11: 27-28 (طوبى للبطن الذي حملك والثديين اللذين رضعتهما)"
            )
        }

        // 3. Special Saints Days
        // 22 Tobi: St. Anthony the Great
        if (cMonth == 5 && cDay == 22) {
            return DayLiturgicalInfo(
                fastingType = if (isWedOrFri) FastingType.STRICT_FAST else FastingType.REGULAR_DAY,
                feastEn = "Departure of St. Anthony the Great",
                feastAr = "نياحة القديس العظيم الأنبا أنطونيوس أب الرهبان",
                commemorationEn = "Departure of the Father of Monasticism, St. Anthony the Great, who filled the desert with prayers.",
                commemorationAr = "نياحة كوكب البرية وأب جميع الرهبان القديس العظيم الأنبا أنطونيوس الكبير.",
                katamerosPsalmEn = "Psalm 1:1-2 - 'Blessed is the man who walks not in the counsel of the ungodly.'",
                katamerosPsalmAr = "مزمور 1: 1-2 - «طوبى للرجل الذي لم يسلك في مشورة الأشرار.»",
                katamerosGospelEn = "Matthew 19:21-30 (If you want to be perfect, go and sell what you have)",
                katamerosGospelAr = "متى 19: 21-30 (إن أردت أن تكون كاملاً فاذهب وبع كل أملاكك وتعال اتبعني)"
            )
        }

        // 23 Paremoude: St. George
        if (cMonth == 8 && cDay == 23) {
            return DayLiturgicalInfo(
                fastingType = if (isWedOrFri) FastingType.STRICT_FAST else FastingType.REGULAR_DAY,
                feastEn = "Martyrdom of St. George (Prince of Martyrs)",
                feastAr = "استشهاد أمير الشهداء القديس العظيم مارجرجس",
                commemorationEn = "Martyrdom of the Great Martyr St. George of Cappadocia after 7 years of enduring tortures.",
                commemorationAr = "تذكار استشهاد أمير الشهداء والقائد العظيم مارجرجس الروماني بعد ثباته العظيم.",
                katamerosPsalmEn = "Psalm 97:11 - 'Light is sown for the righteous, and gladness for the upright in heart.'",
                katamerosPsalmAr = "مزمور 97: 11 - «نور قد زرع للصديق، وفرح للمستقيمي القلوب.»",
                katamerosGospelEn = "Luke 21:12-19 (By your patience possess your souls)",
                katamerosGospelAr = "لوقا 21: 12-19 (بصبركم اقتنوا أنفسكم، لا تهلك شعرة من رؤوسكم)"
            )
        }

        // 15 Hathor: St. Mina the Wonderworker
        if (cMonth == 3 && cDay == 15) {
            return DayLiturgicalInfo(
                fastingType = FastingType.FISH_ALLOWED,
                feastEn = "Martyrdom of St. Mina the Wonderworker",
                feastAr = "استشهاد القديس العظيم مارمينا العجائبي",
                commemorationEn = "Martyrdom of the brave soldier of Christ, St. Mina the Wonderworker.",
                commemorationAr = "تذكار استشهاد القديس البار المحبوب مارمينا صاحب الشفاعات والعجائب الكثيرة.",
                katamerosPsalmEn = "Psalm 34:19 - 'Many are the afflictions of the righteous, but the Lord delivers him out of them all.'",
                katamerosPsalmAr = "مزمور 34: 19 - «كثيرة هي أحزان الصديقين، ومن جميعها ينجيهم الرب.»",
                katamerosGospelEn = "Matthew 10:32-42 (Whoever confesses Me before men)",
                katamerosGospelAr = "متى 10: 32-42 (كل من يعترف بي قدام الناس أعترف أنا أيضاً به قدام أبي الذي في السماوات)"
            )
        }

        // 4. Season Fasting Rules (Nativity Fast: 16 Hathor to 28 Kiahk, St. Mary: 1 to 16 Mesori)
        if ((cMonth == 3 && cDay >= 16) || (cMonth == 4 && cDay <= 27)) {
            val isParamoun = (cMonth == 4 && cDay == 27)
            val fast = if (isParamoun || isWedOrFri) FastingType.STRICT_FAST else FastingType.FISH_ALLOWED
            return DayLiturgicalInfo(
                fastingType = fast,
                feastEn = if (isParamoun) "Paramoun of the Nativity" else "Nativity Fast (Holy Kiahk Praises)",
                feastAr = if (isParamoun) "برمون عيد الميلاد المجيد" else "صوم الميلاد المجيد وتسابيح شهر كيهك المبارك",
                commemorationEn = "Holy Season of expectation and preparation for the Nativity of Christ with blessed Kiahk Theotokia praises.",
                commemorationAr = "أيام الصوم والاستعداد لميلاد المسيح بالتسابيح الكيهكية والمدائح الروحية للعذراء مريم.",
                katamerosPsalmEn = "Psalm 148:1 - 'Praise the Lord from the heavens!'",
                katamerosPsalmAr = "مزمور 148: 1 - «سبحوا الرب من السماوات، سبحوه في الأعالي.»",
                katamerosGospelEn = "Luke 1:46-55 (My soul magnifies the Lord)",
                katamerosGospelAr = "لوقا 1: 46-55 (تعظم نفسي الرب وتبتهج روحي بالله مخلصي)"
            )
        }

        if (cMonth == 12 && cDay in 1..15) {
            return DayLiturgicalInfo(
                fastingType = FastingType.FISH_ALLOWED,
                feastEn = "St. Mary's Fast",
                feastAr = "صوم السيدة العذراء مريم المبارك",
                commemorationEn = "Blessed 15-day fast dedicated to the Holy Virgin Mary in prayer and asceticism.",
                commemorationAr = "صوم السيدة العذراء الطاهرة أم النور، صوم الفرح والشفاعة والطلبات المقبولة.",
                katamerosPsalmEn = "Psalm 45:13 - 'The royal daughter is all glorious within the palace.'",
                katamerosPsalmAr = "مزمور 45: 13 - «كل مجد ابنة الملك في خدرها، منسوجة بذهب ملابسها.»",
                katamerosGospelEn = "Luke 1:39-45 (Mary visited Elizabeth)",
                katamerosGospelAr = "لوقا 1: 39-45 (قامت مريم وذهبت بسرعة إلى الجبال إلى مدينة يهوذا وسلمت على أليصابات)"
            )
        }

        // 5. Default Regular or Wednesday/Friday fast
        if (isWedOrFri) {
            return DayLiturgicalInfo(
                fastingType = FastingType.STRICT_FAST,
                feastEn = if (dayOfWeek == Calendar.WEDNESDAY) "Wednesday Fast (Betrayal of Christ)" else "Friday Fast (Crucifixion of the Lord)",
                feastAr = if (dayOfWeek == Calendar.WEDNESDAY) "صوم يوم الأربعاء (تذكار التآمر على المخلص)" else "صوم يوم الجمعة (تذكار صلب المسيح الفادي)",
                commemorationEn = "Weekly canonical fasting day commemorating the Lord's suffering and salvation.",
                commemorationAr = "صوم كنسي أسبوعي مبارك لذكرى آلام الفداء والصليب ومحبة المخلص الباذلة.",
                katamerosPsalmEn = "Psalm 22:1 - 'My God, My God, why have You forsaken Me?'",
                katamerosPsalmAr = "مزمور 22: 1 - «إلهي إلهي لماذا تركتني، بعيداً عن خلاصي عن كلام زفيري.»",
                katamerosGospelEn = "Matthew 27:32-50 (The Crucifixion)",
                katamerosGospelAr = "متى 27: 32-50 (ولما صلبوه اقتسموا ثيابه مقترعين عليها)"
            )
        }

        return DayLiturgicalInfo(
            fastingType = FastingType.REGULAR_DAY,
            feastEn = null,
            feastAr = null,
            commemorationEn = "Liturgical commemoration of the fathers and saints of this day.",
            commemorationAr = "تذكار الآباء والقديسين الكرام لهذا اليوم المبارك.",
            katamerosPsalmEn = "Psalm 119:105 - 'Your word is a lamp to my feet and a light to my path.'",
            katamerosPsalmAr = "مزمور 119: 105 - «سراج لرجلي كلامك ونور لسبيلي.»",
            katamerosGospelEn = "John 15:1-8 (I am the true vine)",
            katamerosGospelAr = "يوحنا 15: 1-8 (أنا الكرمة الحقيقية وأنتم الأغصان، الذي يثبت في وأنا فيه يأتي بثمر كثير)"
        )
    }
}
