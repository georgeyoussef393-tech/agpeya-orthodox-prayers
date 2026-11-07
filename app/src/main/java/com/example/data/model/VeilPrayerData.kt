package com.example.data.model

import com.example.localization.AppLanguage

/**
 * Complete authentic liturgical text of the Monastic Veil Prayer (صلاة الستار - الخاصة بالرهبان).
 * Updated with exact Arabic liturgical text provided by the user.
 */
object VeilPrayerData {

    fun getSections(lang: AppLanguage): List<PrayerSectionItem> {
        val list = mutableListOf<PrayerSectionItem>()

        // 1. مقدمة كل ساعة
        list.add(AgpeyaCommonPrayers.getIntroduction(lang))

        // 2. صلاة الشكر
        list.add(AgpeyaCommonPrayers.getThanksgivingPrayer())

        // 3. المزمور الخمسون
        list.add(AgpeyaCommonPrayers.getPsalm50())

        // 4. مقدمة ومزامير صلاة الستار
        list.add(
            PrayerSectionItem(
                id = "veil_intro_psalms",
                titleAr = "تسبحة ومزامير صلاة الستار المباركة (للرهبان)",
                titleEn = "Praise and Psalms of the Monastic Veil Prayer",
                subtitleAr = "تسبحة الخضوع وتلاوة المزامير المخصصة لصلوات الستار الرهبانية",
                subtitleEn = "Praise of Repentance & Psalms recited during the Monastic Veil",
                rubricAr = "تتلى في صلاة الستار المزامير التالية من صلوات سابقة في الأجبية للتعزية والتأمل والتوبة:",
                rubricEn = "The following Psalms are recited in the Monastic Veil Prayer for comfort, meditation, and repentance:",
                contentAr = """تسبحة صلاة الستار المباركة أقدمها للمسيح ملكي وإلهي، وأرجوه أن يغفر لي خطاياي.

(مزمور ٤) إذ دعوت استجبت لي يا إله بري، في الضيق وسّعت لي، تراءف عليّ واسمع صلاتي...
(مزمور ٦) يا رب لا تبكتني بغضبك، ولا تؤدبني بسخطك...
(مزمور ١٢) إلى متى يا رب تنساني إلى الانقضاء؟ حتى متى تصرف وجهك عني؟...
(مزمور ١٥) احفظني يا رب فإني عليك توكلت. قلت للرب: أنت ربي ولا تحتاج إلى صلاحي...
(مزمور ٢٤) إليك يا رب رفعت نفسي، إلهي عليك توكلت فلا تخزني إلى الأبد...
(مزمور ٢٦) الرب نوري وخلاصي ممن أخاف؟! الرب حصن حياتي ممن أرتعب؟!...
(مزمور ٦٦) ليتراءف الله علينا ويباركنا، وليظهر وجهه علينا ويرحمنا...
(مزمور ٦٩) اللهم التفت إلى معونتي، يا رب أسرع وأعني...
(مزمور ٨٥) أمل يا رب أذنك واستمعني لأني مسكين وبائس أنا...
(مزمور ٩٠) الساكن في عون العلي يستريح في ظل إله السماء...
(مزمور ١١٦) سبحوا الرب يا جميع الأمم ولتباركه كافة الشعوب...
(مزمور ١١٧) اعترفوا للرب فإنه صالح، لأن إلى الأبد رحمته...
(مزمور ١٢٩) من الأعماق صرخت إليك يا رب، يا رب استمع صوتي...
(مزمور ١٣٠) يا رب لم يرتفع قلبي، ولم تستعلِ عيناي...
(مزمور ١٣١) اذكر يا رب داود وكل دعته، كيف حلف للرب ونذر لإله يعقوب...
(مزمور ١٣٢) هوذا ما أحسن وما أحلى أن يسكن الإخوة معاً!...
(مزمور ١٣٣) ها باركوا الرب يا جميع عبيد الرب، القائمين في بيت الرب...
(مزمور ١٣٦) على أنهار بابل هناك جلسنا فبكينا عندما تذكرنا صهيون...
(مزمور ١٣٧) أعترف لك يا رب من كل قلبي، وأرتل لك أمام الملائكة...
(مزمور ١٤٠) يا رب إليك صرخت فاستمع لي، أنصت إلى صوت تضرعي...
(مزمور ١٤١) بصوتي إلى الرب صرخت، بصوتي إلى الرب تضرعت...
(مزمور ١٤٥) سبحي يا نفسي الرب، أسبح الرب في حياتي وأرتل لإلهي...
(مزمور ١٤٦) سبحوا الرب فإن المزمار جيد، لإلهنا يلذ التسبيح...
(مزمور ١٤٧) سبحي الرب يا أورشليم، سبحي إلهك يا صهيون...""",
                contentEn = """Blessed Praise of the Veil Prayer, offered to Christ my King and my God, asking Him to forgive my sins.

Psalms recited in the Veil Prayer:
Psalm 4, Psalm 6, Psalm 12, Psalm 15, Psalm 24, Psalm 26, Psalm 66, Psalm 69, Psalm 85, Psalm 90, Psalm 116, Psalm 117, Psalm 129, Psalm 130, Psalm 131, Psalm 132, Psalm 133, Psalm 136, Psalm 137, Psalm 140, Psalm 141, Psalm 145, Psalm 146, Psalm 147."""
            )
        )

        // 5. الإنجيل المقدس
        list.add(
            PrayerSectionItem(
                id = "veil_gospel",
                titleAr = "إنجيل صلاة الستار (من بشارة يوحنا ٦: ١٥-٢١)",
                titleEn = "The Holy Gospel for the Monastic Veil (John 6:15-21)",
                subtitleAr = "مشيه على البحر ودخوله السفينة (أنا هو لا تخافوا)",
                subtitleEn = "Jesus walking on the sea (It is I; do not be afraid)",
                rubricAr = "فصل شريف من بشارة الإنجيل لمعلمنا مار يوحنا البشير، بركاته علينا جميعاً آمين:",
                rubricEn = "A reading from the Holy Gospel according to Saint John:",
                contentAr = """فَلَمَّا عَلِمَ يَسُوعُ أَنَّهُمْ مُزْمِعُونَ أَنْ يَأْتُوا وَيَخْتَطِفُوهُ لِيَجْعَلُوهُ مَلِكاً، انْصَرَفَ أَيْضاً إِلَى الْجَبَلِ وَحْدَهُ.

وَلَمَّا كَانَ الْمَسَاءُ نَزَلَ تَلاَمِيذُهُ إِلَى الْبَحْرِ، فَدَخَلُوا السَّفِينَةَ وَكَانُوا يَذْهَبُونَ إِلَى عَبْرِ الْبَحْرِ إِلَى كَفَرْنَاحُومَ. وَكَانَ الظَّلاَمُ قَدْ أَقْبَلَ، وَلَمْ يَكُنْ يَسُوعُ قَدْ أَتَى إِلَيْهِمْ. وَهَاجَ الْبَحْرُ مِنْ رِيحٍ عَظِيمَةٍ تَهُبُّ.

فَلَمَّا جَذَفُوا نَحْوَ خَمْسٍ وَعِشْرِينَ أَوْ ثَلاَثِينَ غَلْوَةً، نَظَرُوا يَسُوعَ مَاشِياً عَلَى الْبَحْرِ مُقْتَرِباً مِنَ السَّفِينَةِ فَخَافُوا. فَقَالَ لَهُمْ: «أَنَا هُوَ، لاَ تَخَافُوا!».

فَرَضُوا أَنْ يَقْبَلُوهُ فِي السَّفِينَةِ. وَلِلْوَقْتِ صَارَتِ السَّفِينَةُ إِلَى الأَرْضِ الَّتِي كَانُوا ذَاهِبِينَ إِلَيْهَا.

(وَالْمَجْدُ لِلَّهِ دَائِماً أَبَدِياً آمِين).""",
                contentEn = """Therefore when Jesus perceived that they were about to come and take Him by force to make Him king, He departed again to the mountain by Himself alone.

Now when evening came, His disciples went down to the sea, got into the boat, and went over the sea toward Capernaum. And it was already dark, and Jesus had not come to them. Then the sea arose because a great wind was blowing.

So when they had rowed about three or four miles, they saw Jesus walking on the sea and drawing near the boat; and they were afraid. But He said to them, "It is I; do not be afraid."

Then they willingly received Him into the boat, and immediately the boat was at the land where they were going.

(Glory to God forever. Amen.)""",
                isGospel = true
            )
        )

        // 6. القطع
        list.add(
            PrayerSectionItem(
                id = "veil_litanies",
                titleAr = "قطع صلاة الستار",
                titleEn = "Litanies of the Monastic Veil",
                rubricAr = "قطع الخشوع والطلب في خلوة القلاية:",
                rubricEn = "Contrite petitions for the monastic evening prayer:",
                contentAr = """(١) يا رب لا تبكتني بغضبك، ولا تؤدبني بسخطك، لأني لا أحتمل ضربتك، فإني خاطئ وضعيف أنا، فارحمني لكي أحيا وتفرح عظامي التي سحقتها خطاياي. فلك سجدت، يا رب، متضرعاً إليك. فانظر إلى تذللي وضائقتي، واغفر لي جميع خطاياي.
(ذوكصاصي فيلان إثروبيه - المجد للآب والابن والروح القدس)

(٢) يا رب قدامك كل شهوتي، وتنهدي ليس بمخفي عنك، فافتقدني برحمتك. وفي يوم غضبك العظيم، يا رب لا توبخني، ولا بسخطك تؤدبني، لئلا أنحدر إلى الجحيم، بل أعطني يا رب دموعاً لكي أبكي بها بحرقة، كدموع الخاطئة، لكيما أتوسل إليك، يا محب البشر، فأتطهر من جميع خطاياي، وأخلص.
(كيه نين كيه آ إي كيه إس توس إيه أوناس طون إيه أونون آمين - الآن وكل أوان وإلى دهر الدهور، آمين)

(٣) أيتها البتول الطاهرة، التي بلا عيب، والدة الإله، مريم الخدر الذي لا يتدنس، خلصيني أنا المضطرب العقل بالشهوات، والمظلم من الأفكار الرديئة، واهتمي بنفسي الممتلئة عيباً. هبيني سهراً في الصلاة، لكيما أسبحك وأطوبك إلى الأبد.
(كيه نين كيه آ إي كيه إس توس إيه أوناس طون إيه أونون آمين - الآن وكل أوان وإلى دهر الدهور، آمين)""",
                contentEn = """(1) O Lord, do not rebuke me in Your anger, nor chasten me in Your hot displeasure, for I cannot bear Your stroke; for I am a sinner and weak. Have mercy on me that I may live and my bones, crushed by my sins, may rejoice. Unto You I bow down, O Lord, supplicating You. Look upon my humiliation and distress, and forgive all my sins.
(Glory to the Father and to the Son and to the Holy Spirit)

(2) O Lord, all my desire is before You, and my sighing is not hidden from You, so visit me in Your mercy. In the day of Your great anger, O Lord, do not chasten me nor discipline me in Your wrath, lest I descend into Hades; but grant me, O Lord, tears to weep bitterly like the sinful woman, so that I may entreat You, O Lover of mankind, to be cleansed from all my sins and be saved.
(Now and forever and unto the ages of ages. Amen)

(3) O pure Virgin, immaculate Mother of God, Mary the undefiled bridal chamber, save me who am disturbed in mind by passions, and darkened by evil thoughts, and care for my soul full of flaws. Grant me watchfulness in prayer, that I may praise and bless you forever.
(Now and forever and unto the ages of ages. Amen)"""
            )
        )

        // 7. الثلاث تقديسات والطلبة
        list.add(AgpeyaCommonPrayers.getTrisagion())
        list.add(AgpeyaCommonPrayers.getKyrieEleisonAndConcludingSupplication())

        // 8. تحليل صلاة الستار
        list.add(
            PrayerSectionItem(
                id = "veil_absolution",
                titleAr = "تحليل صلاة الستار (أيها الرب الإله ضابط الكل)",
                titleEn = "The Absolution of the Monastic Veil",
                rubricAr = "طلب المغفرة والستر والسلامة في هذه الليلة المباركة:",
                rubricEn = "Prayer for forgiveness, protection, and peace for the night:",
                contentAr = """أيها الرب الإله ضابط الكل، أبو ربنا وإلهنا ومخلصنا يسوع المسيح، نسأل ونطلب من صلاحك يا محب البشر، اغفر لنا خطايانا التي أخطأنا بها إليك في هذا اليوم، إن كان بالفعل أو بالقول أو بالفكر أو بجميع الحواس. واصفح عنا من أجل اسمك القدوس المحبوب الذي دُعي علينا.

وأنعم علينا اللهم بليلة هادئة، ونوماً سالماً، طاهراً من كل قلق، خفياً كان أم ظاهراً. وأرسل لنا ملاك السلامة ليحرسنا من كل شر، ومن كل بلية، ومن كل سهم طائر في الظلمة.

لأنك أنت الصالح ومحب البشر، ولك المجد والإكرام مع الآب الصالح والروح القدس المحيي، المساوي لك، الآن وكل أوان وإلى دهر الدهور. آمين.""",
                contentEn = """O Lord God Almighty, the Father of our Lord, God, and Savior Jesus Christ, we ask and entreat Your goodness, O Lover of mankind: forgive us our sins which we have committed against You this day, whether by deed, word, thought, or through all our senses. Pardon us for the sake of Your beloved Holy Name which is invoked upon us.

Grant us, O God, a peaceful night, a quiet and pure sleep, free from all anxiety, whether hidden or manifest. Send to us the angel of peace to guard us from every evil, every calamity, and every arrow that flies in darkness.

For You are good and the Lover of mankind, and to You belong glory and honor, with Your Good Father and the Holy Life-giving Spirit, co-equal with You, now and forever and unto the ages of ages. Amen."""
            )
        )

        // 9. ختام الصلاة
        list.add(AgpeyaCommonPrayers.getFinalConcludingPrayer(lang))

        return list
    }
}
