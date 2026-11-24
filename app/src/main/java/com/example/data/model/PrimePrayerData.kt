package com.example.data.model

import com.example.localization.AppLanguage

/**
 * Full liturgical text of Prime Prayer (صلاة باكر),
 * updated verbatim from the user's verified church Agpeya edition.
 * All Psalms (50 + the 19 Prime Psalms) are pulled directly from AgpeyaPsalmsRegistry.
 */
object PrimePrayerData {

    fun getSections(lang: AppLanguage): List<PrayerSectionItem> {
        val list = mutableListOf<PrayerSectionItem>()

        // 1. مقدمة صلاة باكر والصلاة الربانية
        list.add(AgpeyaCommonPrayers.getIntroduction(lang).copy(id = "prime_intro"))

        // 2. صلاة الشكر
        list.add(AgpeyaCommonPrayers.getThanksgivingPrayer().copy(id = "prime_thanksgiving"))

        // 3. المزمور ٥٠
        list.add(AgpeyaCommonPrayers.getPsalm50().copy(id = "prime_psalm50"))

        // 4. بدء الصلاة (هلم نسجد - البولس - من إيمان الكنيسة - بدء صلاة باكر)
        list.add(
            PrayerSectionItem(
                id = "prime_opening_epistle_faith",
                titleAr = "بدء الصلاة (هلم نسجد - البولس - من إيمان الكنيسة)",
                titleEn = "Beginning of Prime (Come Let Us Kneel - Epistle - Faith of the Church)",
                subtitleAr = "هلم نسجد • البولس من رسالة أفسس (٤: ١-٥) • من إيمان الكنيسة • بدء صلاة باكر",
                subtitleEn = "Come Let Us Kneel • Ephesians 4:1-5 • Faith of the Church",
                contentAr = """هلم نسجد :
هلم نسجد هلم نسأل المسيح إلهنا. هلم نسجد، هلم نطلب من المسيح ملكنا. هلم نسجد، هلم نتضرع إلى المسيح مخلصنا. يا ربنا يسوع المسيح كلمة الله إلهنا، بشفاعة القديسة مريم وجميع قديسيك، احفظنا ولنبدأ بدءا حسنا. ارحمنا كإرادتك إلى الأبد. الليل عبر، نشكرك يا رب ونسأل أن تحفظنا في هذا اليوم بغير خطية وأنقذنا.

البولس من رسالة أفسس (4: 1-5)
أسألكم أنا الأسير في الرب أن تسلكوا كما يحق للدعوة التي دعيتم إليها، بكل تواضع القلب والوداعة وطول الأناة، محتملين بعضكم بعضا بالمحبة، مسرعين إلى حفظ وحدانية الروح برباط الصلح الكامل لكي تكونوا جسدًا واحدًا وروحًا واحدا، كما دعيتم في رجاء دعوتكم الواحد. ربٍّ واحد. وإيمان واحد. ومعمودية واحدة.

من إيمان الكنيسة
واحد هو الله أبو كل أحد.
واحد هو أيضا ابنه يسوع المسيح الكلمة، الذي تجسد ومات وقام من الأموات في اليوم الثالث وأقامنا معه، واحد هو الروح القدس المعزى الواحد بأقنومه، منبثق من الآب، يطهر كل البرية. يعلمنا أن نسجد للثالوث القدوس بلاهوت واحد وطبيعة واحدة، نسبحه ونباركه إلى الأبد. آمين.

بدء صلاة باكر
صلاة باكر من النهار المبارك، أقدمها للمسيح ملكي وإلهي، وأرجوه أن يغفر لي خطاياي. من مزامير معلمنا داود النبي بركاته علينا أمين.""",
                contentEn = """Come, let us kneel down, come let us ask Christ our God. Come, let us kneel down, come let us entreat Christ our King. Come, let us kneel down, come let us beseech Christ our Savior. O our Lord Jesus Christ, the Word of God, our God, through the intercessions of Saint Mary and all Your saints, guard us and let us begin a good beginning. Have mercy on us according to Your will forever. The night has passed; we thank You, O Lord, and ask You to keep us this day without sin and deliver us.

Pauline Epistle (Ephesians 4:1-5):
I, therefore, the prisoner of the Lord, beseech you to walk worthy of the calling with which you were called, with all lowliness and gentleness, with longsuffering, bearing with one another in love, endeavoring to keep the unity of the Spirit in the bond of peace. There is one body and one Spirit, just as you were called in one hope of your calling; one Lord, one faith, one baptism.

From the Faith of the Church:
One is God, the Father of everyone. One is also His Son, Jesus Christ the Word, Who took flesh and died, and rose from the dead on the third day, and raised us with Him. One is the Holy Spirit, the Comforter, one in His hypostasis, proceeding from the Father, purifying the whole creation, and teaching us to worship the Holy Trinity, one in divinity and one in nature. We praise Him and bless Him forever. Amen.

Beginning of the First Hour (Prime):
The First Hour prayer of the blessed day, I offer to Christ my King and my God, and I beseech Him to forgive me my sins. From the Psalms of our teacher David the prophet, his blessings be with us all. Amen."""
            )
        )

        // 5. مزامير صلاة باكر كاملة (19 مزموراً) من السجل المركزي الموحّد
        val primePsalms = listOf(
            Triple(1, "المزمور ١ — طوبى للرجل", "(Psalm 1) Blessed is the Man"),
            Triple(2, "المزمور ٢ — لماذا ارتجت الأمم", "(Psalm 2) Why Do the Nations Rage"),
            Triple(3, "المزمور ٣ — يا رب لماذا كثر الذين يحزنونني", "(Psalm 3) Lord, How They Have Increased"),
            Triple(4, "المزمور ٤ — إذ دعوت استجبت لي", "(Psalm 4) Hear Me When I Call"),
            Triple(5, "المزمور ٥ — أنصت يا رب لكلماتي", "(Psalm 5) Give Ear to My Words"),
            Triple(6, "المزمور ٦ — يا رب لا تبكتني بغضبك", "(Psalm 6) O Lord, Do Not Rebuke Me"),
            Triple(8, "المزمور ٨ — أيها الرب ربنا ما أعجب اسمك", "(Psalm 8) O Lord, Our Lord"),
            Triple(11, "المزمور ١١ — خلصني يا رب فإن البار قد فني", "(Psalm 11) Help, Lord, for the Godly Man Ceases"),
            Triple(12, "المزمور ١٢ — إلى متى يا رب تنساني", "(Psalm 12) How Long, O Lord?"),
            Triple(14, "المزمور ١٤ — يا رب من يسكن في مسكنك", "(Psalm 14) Lord, Who May Abide"),
            Triple(15, "المزمور ١٥ — احفظني يا رب فإني عليك توكلت", "(Psalm 15) Preserve Me, O God"),
            Triple(18, "المزمور ١٨ — السموات تحدث بمجد الله", "(Psalm 18) The Heavens Declare the Glory of God"),
            Triple(24, "المزمور ٢٤ — إليك يا رب رفعت نفسي", "(Psalm 24) To You, O Lord, I Lift Up My Soul"),
            Triple(26, "المزمور ٢٦ — الرب نوري وخلاصي", "(Psalm 26) The Lord is My Light and My Salvation"),
            Triple(62, "المزمور ٦٢ — يا الله إلهي إليك أبكر", "(Psalm 62) O God, You Are My God; Early Will I Seek You"),
            Triple(66, "المزمور ٦٦ — ليتراءف الله علينا ويباركنا", "(Psalm 66) God Be Merciful to Us and Bless Us"),
            Triple(69, "المزمور ٦٩ — اللهم التفت إلى معونتي", "(Psalm 69) Make Haste, O God, to Deliver Me"),
            Triple(112, "المزمور ١١٢ — سبحوا الرب أيها الفتيان", "(Psalm 112) Praise the Lord, O Servants of the Lord"),
            Triple(142, "المزمور ١٤٢ — يا رب اسمع صلاتي", "(Psalm 142) Hear My Prayer, O Lord")
        )

        val primePsalmsEnglish = mapOf(
            1 to "Blessed is the man who walks not in the counsel of the ungodly, nor stands in the path of sinners, nor sits in the seat of the scornful... Alleluia.",
            2 to "Why do the nations rage, and the people plot a vain thing? The kings of the earth set themselves, and the rulers take counsel together against the Lord and against His Anointed... Alleluia.",
            3 to "Lord, how they have increased who trouble me! Many are they who rise up against me. But You, O Lord, are a shield for me... Alleluia.",
            4 to "Hear me when I call, O God of my righteousness! You have relieved me in my distress; have mercy upon me, and hear my prayer... Alleluia.",
            5 to "Give ear to my words, O Lord, consider my meditation. Give heed to the voice of my cry, my King and my God, for to You I will pray... Alleluia.",
            6 to "O Lord, do not rebuke me in Your anger, nor chasten me in Your hot displeasure. Have mercy on me, O Lord, for I am weak... Alleluia.",
            8 to "O Lord, our Lord, how excellent is Your name in all the earth, Who have set Your glory above the heavens! Alleluia.",
            11 to "Help, Lord, for the godly man ceases! For the faithful fail from among the sons of men... Alleluia.",
            12 to "How long, O Lord? Will You forget me forever? How long will You hide Your face from me? Alleluia.",
            14 to "Lord, who may abide in Your tabernacle? Who may dwell in Your holy hill? He who walks uprightly, and works righteousness... Alleluia.",
            15 to "Preserve me, O God, for in You I put my trust. O my soul, you have said to the Lord, 'You are my Lord, my goodness is nothing apart from You'... Alleluia.",
            18 to "The heavens declare the glory of God; and the firmament shows His handiwork. Day unto day utters speech, and night unto night reveals knowledge... Alleluia.",
            24 to "To You, O Lord, I lift up my soul. O my God, I trust in You; let me not be ashamed; let not my enemies triumph over me... Alleluia.",
            26 to "The Lord is my light and my salvation; whom shall I fear? The Lord is the strength of my life; of whom shall I be afraid?... Alleluia.",
            62 to "O God, You are my God; early will I seek You; my soul thirsts for You; my flesh longs for You in a dry and thirsty land... Alleluia.",
            66 to "God be merciful to us and bless us, and cause His face to shine upon us, that Your way may be known on earth, Your salvation among all nations... Alleluia.",
            69 to "Make haste, O God, to deliver me! Make haste to help me, O Lord! Let them be ashamed and confounded who seek my life... Alleluia.",
            112 to "Praise the Lord! Praise, O servants of the Lord, praise the name of the Lord! Blessed be the name of the Lord from this time forth and forevermore... Alleluia.",
            142 to "Hear my prayer, O Lord, give ear to my supplications! In Your faithfulness answer me, and in Your righteousness... Alleluia."
        )

        primePsalms.forEach { (num, titleAr, titleEn) ->
            list.add(
                PrayerSectionItem(
                    id = "prime_psalm_$num",
                    titleAr = titleAr,
                    titleEn = titleEn,
                    contentAr = AgpeyaPsalmsRegistry.requireArabicPsalm(num),
                    contentEn = primePsalmsEnglish[num].orEmpty()
                )
            )
        }

        // 6. إنجيل باكر (يوحنا 1: 1-17)
        list.add(
            PrayerSectionItem(
                id = "prime_gospel",
                titleAr = "إنجيل باكر (إنجيل يوحنا ١ : ١-١٧)",
                titleEn = "The Gospel of Prime (John 1:1-17)",
                subtitleAr = "من إنجيل معلمنا يوحنا البشير بركاته علينا أمين",
                subtitleEn = "According to Saint John (1:1-17)",
                contentAr = """من إنجيل معلمنا يوحنا البشير بركاته علينا أمين.
في البدء كان الكلمة، والكلمة كان عندَ الله. وكان الكلمة الله، هذا كان في البدء عند الله. كل شيء به كان، وبغيره لم يكن شيء مما كان. فيه كانت الحياة، والحياة كانت نورَ الناس، والنور أضاء في الظلمة، والظلمة لم تدركه. كان إنسان مرسل من الله اسمه يوحنا، هذا جاء للشهادة ليشهد للنور ليؤمن الكل بواسطته. لم يكن هو النور بل ليشهد للنور، كان النورُ الحقيقي الذي ينير كل إنسان آتيا إلى العالم. كان في العالم، وكُوِّنَ العالم به، ولم يعرفه العالم. إلى خاصته جاء، وخاصته لم تقبله. وأما الذين قبلوه فأعطاهم سلطانًا أن يصيروا أبناء الله، الذين يؤمنون باسمه، الذين ولدوا ليس من دم، ولا من مشيئة جسد، ولا من مشيئة رجل، لكن من الله ولدوا. والكلمة صار جسدًا وحل بيننا ورأينا مجده مثل مجد ابن وحيد لأبيه مملوءًا نعمة وحقًا.

يوحنا شهد له وصرخ قائلًا: هذا هو الذي قلت عنه إن الذي يأتي بعدى كان قبلي، لأنه كان أقدم منى. ونحن جميعا أخذنا من ملئه، ونعمة عوضًا عن نعمة. لأن الناموس بموسى أعطى. أما النعمة والحق فبيسوع المسيح صارا. والمجد لله دائمًا أبديًا أمين.""",
                contentEn = """In the beginning was the Word, and the Word was with God, and the Word was God. The same was in the beginning with God. All things were made by Him, and without Him was not any thing made that was made... Glory to God forever.""",
                isGospel = true
            )
        )

        // 7. القطع
        list.add(
            PrayerSectionItem(
                id = "prime_litanies",
                titleAr = "القطع",
                titleEn = "Litanies of Prime",
                contentAr = """أيها النور الحقيقي الذي يضئ لكل إنسان آت إلى العالم، أتيت إلى العالم بمحبتك للبشر، وكل الخليقة تهللت بمجيئك. خلصت أبانا آدم من الغواية، وعتقت أمنا حواء من طلقات الموت، وأعطيتنا روح البنوة، نسبحك ونباركك قائلين:
( ذوكصابتري كيه إيو كي آجيو ابنيفماتي Doxa Patri ke Uiw ke `agiw `Pneumati - المجد للآب والابن والروح القدس ).

إذا ما دخل إلينا وقت الصباح أيها المسيح إلهنا النور الحقيقي، فلتشرق فينا الحواس المضيئة والأفكار النورانية. ولا تغطينا ظلمة الآلام، لكي نسبحك عقليا مع داود قائلين: "سبقت عيناي وقت السحر لأتلو في جميع أقوالك". اسمع أصواتنا كعظيم رحمتك، ونجنا أيها الرب إلهنا بتحننك.
( كي نين ، كي آ إي، كي ايستوس إي أوناس تون إي أونون آمين Ke nun ke `a`i ke ic touc `e`wnac twn `e`wnwn `amyn - الآن وكل أوان وإلى دهر الداهرين آمين )

أنت هي أم النور المكرمة، من مشارق الشمس إلى مغاربها يقدمون لك تمجيدات يا والدة الإله السماء الثانية، لأنك أنت هي الزهرة النيرة غير المتغيرة والأم الباقية عذراء، لأن الآب اختارك، والروح القدس ظللك، والابن تنازل وتجسد منك. فاسأليه أن يعطى الخلاص للعالم الذي خلقه، وأن ينجيَّه من التجارب. نسبحه تسبيحا جديدا ونباركه الآن وكل أوان وإلى الأبد. أمين.""",
                contentEn = """1- O True Light that gives light to every person coming into the world, in Your love for mankind You came to the world, and all creation rejoiced at Your coming...
2- As we enter the morning hour, O Christ our God, the True Light, we pray that You enlighten our minds...
3- You are the Mother of Light, honored from the rising of the sun to its setting..."""
            )
        )

        // 8. تسبحة الملائكة
        list.add(
            PrayerSectionItem(
                id = "prime_angelic_hymn",
                titleAr = "تسبحة الملائكة",
                titleEn = "Angelic Hymn (Glory to God in the Highest)",
                contentAr = """فلنسبح مع الملائكة قائلين: المجد لله في الأعالي وعلى الأرض السلام وفى الناس المسرة. نسبحك. نباركك. نخدمك. نسجد لك. نعترف لك. ننطق بمجدك. نشكرك من أجل عظم مجدك، أيها الرب المالك على السموات، الله الآب ضابط الكل، والرب الابن الواحد الوحيد يسوع المسيح، والروح القدس. أيها الرب الإله، حمل الله، ابن الآب، رافع خطية العالم، ارحمنا.

يا حامل خطية العالم، اقبل طلباتنا إليك. أيها الجالس عن يمين أبيه، ارحمنا. أنت وحدك القدوس. أنت وحدك العالي يا ربى يسوع المسيح والروح القدس. مجدا لله الآب أمين. أباركك كل يوم، وأسبح اسمك القدوس إلى الأبد. وإلى أبد الأبد. أمين. منذ الليل روحي تبكر إليك يا إلهي، لأن أوامرك هي نور على الأرض. كنت أتلو في طرقك، لأنك صرت لي معينا. باكرا يا رب تسمع صوتي، بالغداة أقف أمامك وتراني.""",
                contentEn = """Let us praise with the angels saying: Glory to God in the highest, peace on earth, and goodwill toward men. We praise You, we bless You, we worship You..."""
            )
        )

        // 9. الثلاث تقديسات
        list.add(AgpeyaCommonPrayers.getTrisagion().copy(id = "prime_trisagion"))

        // 10. السلام لك
        list.add(AgpeyaCommonPrayers.getHailToYou().copy(id = "prime_hail"))

        // 11. بدء قانون الإيمان وقانون الإيمان المقدس الأرثوذكسي
        list.add(AgpeyaCommonPrayers.getCreed().copy(id = "prime_creed"))

        // 12. كيرياليسون 41 مرة وقدوس قدوس قدوس
        list.add(AgpeyaCommonPrayers.getKyrieEleisonAndConcludingSupplication().copy(id = "prime_kyrie_41"))

        // 13. التحليل الأول والتحليل الثاني لصلاة باكر
        list.add(
            PrayerSectionItem(
                id = "prime_absolution",
                titleAr = "التحليل وتحليل آخر لصلاة باكر",
                titleEn = "Absolutions of Prime (First & Second Absolution)",
                contentAr = """التحليل
أيها الرب إله القوات، الكائن قبل الدهور والدائم إلى الأبد، الذي خلق الشمس لضياء النهار، والليل راحة لكل البشر، نشكرك يا ملك الدهور لأنك أجزتنا هذا الليل بسلام وأتيت بنا إلى مبدأ النهار. من أجل هذا نسأل يا ملكنا ملك الدهور، ليشرق لنا نور وجهك، وليضيء علينا نور علمك الإلهي. واجعلنا يا سيدنا أن نكون بنى النور وبني النهار، لكي نجوز هذا اليوم ببر وطهارة وتدبير حسن، لنكمل بقية أيام حياتنا بلا عثرة. بالنعمة والرأفة ومحبة البشر اللواتي لابنك الوحيد يسوع المسيح، وموهبة روحك القدوس. الآن وكل أوان وإلى الأبد. أمين.

تحليل آخر
أيها الباعث النور فينطلق، المشرق شمسه على الأبرار والأشرار، الذي صنع النور الذي يضئ على المسكونة، أنر عقولنا وقلوبنا وأفهامنا يا سيد الكل. هب لنا في هذا اليوم الحاضر أن نرضيك فيه. واحرسنا من كل شيء رديء ومن كل خطيئة، ومن كل قوة مضادة بالمسيح يسوع ربنا. هذا الذي أنت مبارك معه مع الروح القدس المحيى المساوي لك، الآن وكل أوان وإلى دهر الدهور. أمين.""",
                contentEn = """First Absolution:
O Lord, God of hosts, Who is before all ages and abides forever, Who created the sun to give light by day, and the night as rest for all flesh; we thank You, O King of the ages, that You have brought us through this night in peace, and brought us unto the beginning of the day...

Second Absolution:
O Sender of light, Who causes it to go forth, Who makes His sun shine on the righteous and the wicked, Who made the light which illuminates the whole world; enlighten our minds, our hearts, and our understandings, O Master of all... Amen."""
            )
        )

        // 14. طلبة تصلى آخر كل ساعة والصلاة الربانية
        list.add(AgpeyaCommonPrayers.getFinalConcludingPrayer(lang).copy(id = "prime_conclusion"))

        return list
    }
}
