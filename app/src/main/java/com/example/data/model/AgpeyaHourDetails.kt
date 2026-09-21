package com.example.data.model

import com.example.localization.AppLanguage

/**
 * Detailed Psalms and Specific Prayers for the Agpeya hours.
 */
object AgpeyaHourDetails {

    /**
     * Prime (صلاة باكر):
     * Complete purified liturgical order with all 19 Psalms, Gospel, Litanies, and Absolutions.
     */
    fun getPrimeSections(lang: AppLanguage): List<PrayerSectionItem> {
        return PrimePrayerData.getSections(lang)
    }

    /**
     * Terce (صلاة الساعة الثالثة - ٩ صباحاً):
     * Descent of Holy Spirit, complete unabridged Psalms (19, 20, 21, 25, 28, 29), Gospel, Litanies, Absolution.
     */
    fun getTerceSections(lang: AppLanguage): List<PrayerSectionItem> {
        return TercePrayerData.getSections(lang)
    }

    /**
     * Sext (صلاة الساعة السادسة - ١٢ ظهراً):
     * The Crucifixion at Golgotha, unabridged Psalms (53, 54, 55, 66, 69, 85, 90, 92), Gospel of Beatitudes, Litanies, Absolution.
     */
    fun getSextSections(lang: AppLanguage): List<PrayerSectionItem> {
        return SextPrayerData.getSections(lang)
    }

    /**
     * None (صلاة الساعة التاسعة - ٣ ظهراً):
     * Death of Christ in the Flesh, Repentance of the Right Hand Thief, unabridged 12 Psalms (95, 96, 97, 98, 99, 100, 109, 110, 111, 112, 114, 115), Gospel, Litanies, Absolution.
     */
    fun getNoneSections(lang: AppLanguage): List<PrayerSectionItem> {
        return NonePrayerData.getSections(lang)
    }

    /**
     * Vespers (صلاة الغروب - الساعة الحادية عشرة - ٥ مساءً):
     * The Sunset hour, taking down Christ from the Cross, unabridged 12 Psalms (116, 117, 119, 120, 121, 122, 123, 124, 125, 126, 127, 128), Gospel, Litanies, Absolution.
     */
    fun getVespersSections(lang: AppLanguage): List<PrayerSectionItem> {
        return VespersPrayerData.getSections(lang)
    }

    /**
     * Compline (صلاة النوم - الساعة الثانية عشرة - ٩ مساءً):
     * Burial of Christ, Readiness for Eternity, unabridged 12 Psalms (129, 130, 131, 132, 133, 136, 137, 140, 141, 145, 146, 147), Gospel, Litanies, Absolution.
     */
    fun getComplineSections(lang: AppLanguage): List<PrayerSectionItem> {
        return ComplinePrayerData.getSections(lang)
    }

    /**
     * Midnight (صلاة نصف الليل):
     * The Three Services (الخدمة الأولى، الثانية، الثالثة).
     */
    fun getMidnightSections(lang: AppLanguage): List<PrayerSectionItem> {
        return listOf(
            AgpeyaCommonPrayers.getIntroduction(lang),
            AgpeyaCommonPrayers.getThanksgivingPrayer(),
            AgpeyaCommonPrayers.getPsalm50(),
            PrayerSectionItem(
                id = "midnight_service1_psalms",
                titleAr = "الخدمة الأولى من صلاة نصف الليل (قوموا يا بني النور)",
                titleEn = "First Watch of Midnight (Arise, O Children of the Light)",
                subtitleAr = "المزمور المائة والثالث والثلاثون والمزمور المائة والسابع عشر",
                subtitleEn = "Psalm 134 and Psalm 118",
                contentAr = """تسبحة الخدمة الأولى:
قوموا يا بني النور لنسبح رب القوات، لكي ينعم علينا بخلاص نفوسنا.
هوذا باركوا الرب يا جميع عبيد الرب، القائمين في بيت الرب في ديار بيت إلهنا، في الليالي ارفعوا أيديكم إلى القدس وباركوا الرب. يبارككم الرب من صهيون، الذي صنع السماء والأرض. هللويا.

المزمور المائة والثامن والأربعون:
سبحوا الرب من السموات، سبحوه في الأعالي. سبحوه يا جميع ملائكته، سبحوه يا جميع جنوده. سبحيه أيتها الشمس والقمر، سبحيه يا جميع كواكب النور. سبحيه يا سماء السموات، ويا أيتها المياه التي فوق السموات، لتسبح اسم الرب لأنه أمر فخلقت، أقامها إلى الدهر وإلى دهر الداهرين، وضع لها أمراً فلن تتعداه. هللويا.""",
                contentEn = """Opening Praise of the First Watch:
Arise, O children of the light, let us praise the Lord of hosts, that He may grant us the salvation of our souls.
Behold, bless the Lord, all you servants of the Lord, who by night stand in the house of the Lord! Lift up your hands in the sanctuary, and bless the Lord. The Lord who made heaven and earth bless you from Zion! Alleluia.

Psalm 148:
Praise the Lord from the heavens; praise Him in the heights! Praise Him, all His angels; praise Him, all His hosts! Praise Him, sun and moon; praise Him, all you stars of light! Praise Him, you heavens of heavens, and you waters above the heavens! Let them praise the name of the Lord, for He commanded and they were created. He also established them forever and ever; He made a decree which shall not pass away. Alleluia."""
            ),
            PrayerSectionItem(
                id = "midnight_gospel",
                titleAr = "الإنجيل المقدس لنصف الليل (متى ٢٥: ١-١٣)",
                titleEn = "The Holy Gospel for Midnight (Matthew 25:1-13)",
                subtitleAr = "مثل العذارى الحكيمات والجاهلات ومجيء العريس في نصف الليل",
                subtitleEn = "Parable of the Wise and Foolish Virgins & The Bridegroom at Midnight",
                contentAr = AgpeyaPrayerContent.getHolyGospel(PrayerId.MIDNIGHT, AppLanguage.ARABIC).text,
                contentEn = AgpeyaPrayerContent.getHolyGospel(PrayerId.MIDNIGHT, AppLanguage.ENGLISH).text,
                isGospel = true
            ),
            PrayerSectionItem(
                id = "midnight_litanies",
                titleAr = "قطع نصف الليل (ها هوذا العريس يأتي في نصف الليل)",
                titleEn = "Litanies of Midnight (Behold, the Bridegroom Comes at Midnight)",
                rubricAr = "قطع السهر والاستعداد لملاقاة العريس السماوي:",
                rubricEn = "Litanies of watchfulness awaiting the Heavenly Bridegroom:",
                contentAr = """ها هوذا العريس يأتي في نصف الليل، طوبى للعبد الذي يجده مستيقظاً، وأما الذي يجده غافلاً فإنه غير مستحق المضي معه. فانظري يا نفسي لئلا تثقلي نوماً، فتلقي خارج الملكوت، بل اسهري واصرخي قائلة: قدوس قدوس قدوس أنت يا الله، من أجل والدة الإله ارحمنا.
(المجد للآب والابن والروح القدس)

تفهمي يا نفسي ذلك اليوم الرهيب، واستيقظي وأضيئي مصباحكِ بزيت التوبة والدموع، لأنكِ لا تعلمين متى ينادى بالصوت: ها هوذا العريس قد أقبل! فانظري يا نفسي لئلا تنعسي فتبقي خارجاً تقرعين بلا فائدة مثل العذارى الجاهلات، بل اسهري بخشوع مصلية لتلقي المسيح الرب بدهن دسم، ويعطيكِ مجد عرسه الإلهي الحقيقي.
(الآن وكل أوان وإلى دهر الداهرين، آمين)

أنتِ هي سور خلاصنا الحصين يا والدة الإله العذراء، الحصن المنيع غير المنهدم، أبطلي مشورة المعاندين، وحولي حزن عبيدكِ إلى فرح، وحصني مدينتنا، وعضدي كهنتنا ورهباننا وشعبنا، وتشفعي في سلام العالم، لأنكِ أنتِ هي رجاؤنا يا والدة الإله.""",
                contentEn = """Behold, the Bridegroom cometh at midnight; blessed is the servant whom He shall find watching, but he whom He shall find heedless is unworthy of going with Him. Beware, therefore, O my soul, lest you be weighed down by sleep and be cast out of the Kingdom, but be watchful and cry out: Holy, Holy, Holy are You, O God; for the sake of the Theotokos have mercy on us.
(Glory to the Father and to the Son and to the Holy Spirit)

Consider, O my soul, that fearful day, awake and illuminate your lamp with the oil of repentance and tears; for you know not at what hour the cry shall ring out: 'Behold, the Bridegroom is coming!' Beware, O my soul, lest you slumber and remain outside knocking in vain like the foolish virgins; but watch in humility, praying that you may meet Christ the Lord with rich oil, and He may grant you the joy of His true heavenly wedding feast.
(Now and forever and unto the ages of ages. Amen)

You are the fortress of our salvation, O Virgin Theotokos, an invincible and unbreakable rampart. Frustrate the counsels of adversaries, turn the grief of your servants into joy, protect our churches and people, and intercede for the peace of the world, for you are our hope, O Mother of God."""
            ),
            AgpeyaCommonPrayers.getTrisagion(),
            AgpeyaCommonPrayers.getKyrieEleisonAndConcludingSupplication(),
            PrayerSectionItem(
                id = "midnight_absolution",
                titleAr = "تحليل صلاة نصف الليل",
                titleEn = "The Absolution of Midnight",
                rubricAr = "طلب اليقظة الروحية والنجاة من فخاخ الظلام:",
                rubricEn = "The absolution of the midnight watches:",
                contentAr = """أيها الرب يسوع المسيح، ابن الله الحي، يا من سهرت في جثسيماني مصلياً حتى قطرت عرقاً كقطرات دم نازلة على الأرض، علمنا كيف نسهر معك ساعة واحدة ونجاهد في الصلاة. نجنا من نوم الغفلة، وامنحنا توبة حقيقية، وأنر بصائرنا لنعاين أسرار حبك الفائق، واجعلنا مستعدين لمجيئك الثاني المخوف المملوء مجداً، برحمتك مع أبيك الصالح والروح القدس، آمين.""",
                contentEn = """O Lord Jesus Christ, Son of the Living God, Who watched and prayed in Gethsemane until Your sweat fell like great drops of blood upon the ground, teach us how to watch with You one hour and persevere in prayer. Deliver us from the slumber of carelessness, grant us true repentance, enlighten our spiritual sight to behold the mysteries of Your transcendent love, and make us ready for Your second awe-inspiring and glorious Coming, through Your mercy, with Your Good Father and the Holy Spirit. Amen."""
            ),
            AgpeyaCommonPrayers.getFinalConcludingPrayer(lang)
        )
    }

    /**
     * Veil (صلاة الستار - الخاصة بالرهبان):
     * Complete unabridged 29 Psalms (4, 6, 12, 15, 24, 26, 66, 69, 22, 29, 42, 56, 85, 90, 96, 109, 114, 115, 120, 128, 129, 130, 131, 132, 133, 136, 140, 145, 118),
     * Gospel of St. John, Litanies of contrition and tears, Absolution.
     */
    fun getVeilSections(lang: AppLanguage): List<PrayerSectionItem> {
        return VeilPrayerData.getSections(lang)
    }

    /**
     * Get the complete, detailed list of liturgical sections for any canonical hour.
     */
    fun getFullPrayerSections(prayerId: PrayerId, lang: AppLanguage): List<PrayerSectionItem> = when (prayerId) {
        PrayerId.PRIME -> getPrimeSections(lang)
        PrayerId.TERCE -> getTerceSections(lang)
        PrayerId.SEXT -> getSextSections(lang)
        PrayerId.NONE -> getNoneSections(lang)
        PrayerId.VESPERS -> getVespersSections(lang)
        PrayerId.COMPLINE -> getComplineSections(lang)
        PrayerId.MIDNIGHT -> getMidnightSections(lang)
        PrayerId.VEIL -> getVeilSections(lang)
    }
}
