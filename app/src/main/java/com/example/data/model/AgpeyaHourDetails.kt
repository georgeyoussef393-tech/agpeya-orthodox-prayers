package com.example.data.model

import com.example.localization.AppLanguage

/**
 * Detailed Psalms and Specific Prayers for the Agpeya hours.
 */
object AgpeyaHourDetails {

    /**
     * Prime (صلاة باكر):
     * Includes Opening Morning Praise, Key Morning Psalms, Gospel, Litanies, Gloria in Excelsis, Creed, Absolution.
     */
    fun getPrimeSections(lang: AppLanguage): List<PrayerSectionItem> {
        return listOf(
            AgpeyaCommonPrayers.getIntroduction(lang),
            AgpeyaCommonPrayers.getThanksgivingPrayer(),
            PrayerSectionItem(
                id = "prime_opening_hymn",
                titleAr = "تسبحة باكر المباركة (فلنقومن يا بني النور)",
                titleEn = "Prime Morning Hymn (Arise, O Children of the Light)",
                rubricAr = "تقال في مطلع صلاة باكر استنهاضاً للروح والشكر على انبثاق النور:",
                rubricEn = "Chanted at the start of Prime to greet the rising of the physical and spiritual light:",
                contentAr = """فلنقومن يا بني النور، لنسبح رب القوات، لكي ينعم علينا بخلاص نفوسنا.
عندما نقف جسدياً أمامك، انزع من عقولنا نوم الغفلة، وأعطنا يا رب يقظة لكي نفهم كيف نقف أمامك وقت الصلاة، ونرسل لك إلى فوق التمجيد اللائق، ونفوز بمغفرة خطايانا الكثيرة.
المجد لك يا محب البشر.""",
                contentEn = """Arise, O children of the light, let us praise the Lord of hosts, that He may grant us the salvation of our souls.
When we stand in the flesh before You, take away from our minds the slumber of sleep, give us, O Lord, vigilance that we may understand how to stand before You in prayer, and send up unto You the worthy glorification, and win forgiveness of our many sins.
Glory to You, O Lover of mankind."""
            ),
            AgpeyaCommonPrayers.getPsalm50(),
            PrayerSectionItem(
                id = "prime_psalms",
                titleAr = "مزامير باكر المباركة",
                titleEn = "The Psalms of Prime",
                subtitleAr = "المزمور الأول، الثاني، الثالث، ومزمور ٦٢",
                subtitleEn = "Psalms 1, 2, 3, and 63",
                contentAr = """المزمور الأول:
طوبى للرجل الذي لم يسلك في مشورة المنافقين، وفي طريق الخطاة لم يقف، وفي مجلس المستهزئين لم يجلس. بل في ناموس الرب إرادته، وفي ناموسه يلهج نهاراً وليلاً. فيكون كشجرة مغروسة على مجاري المياه، تعطي ثمرها في حينه، وورقها لا ينتثر، وكل ما يصنعه ينجح فيه. ليس كذلك المنافقون، ليس كذلك، بل هم كالهباء الذي تذريه الريح عن وجه الأرض. هللويا.

المزمور الثالث:
يا رب لماذا كثر الذين يحزنونني؟ كثيرون قاموا عليّ. كثيرون يقولون لنفسي: ليس له خلاص بإلهه. أما أنت يا رب فأنت ناصري ومجدي ورافع رأسي. بصوتي إلى الرب صرخت فاستجاب لي من جبل قدسه. أنا اضطجعت ونمت ثم استيقظت لأن الرب عضدني. هللويا.

المزمور الثاني والستون:
يا الله، إلهي إليك أبكر، عطشت إليك نفسي، يشتاق إليك جسدي في أرض مقفرة وموضع خرب لا ماء فيه. هكذا تراءيت لك في القدس لأرى قوتك ومجدك، لأن رحمتك أفضل من الحياة، شفتي تسبحانك. هكذا أباركك في حياتي وباسمك أرفع يدي. هللويا.""",
                contentEn = """Psalm 1:
Blessed is the man who walks not in the counsel of the ungodly, nor stands in the path of sinners, nor sits in the seat of the scornful; but his delight is in the law of the Lord, and in His law he meditates day and night. He shall be like a tree planted by the rivers of water, that brings forth its fruit in its season, whose leaf also shall not wither; and whatever he does shall prosper. The ungodly are not so, but are like the chaff which the wind drives away. Alleluia.

Psalm 3:
Lord, how they have increased who trouble me! Many are they who rise up against me. Many are they who say of me, 'There is no help for him in God.' But You, O Lord, are a shield for me, my glory and the One Who lifts up my head. I cried to the Lord with my voice, and He heard me from His holy hill. I lay down and slept; I awoke, for the Lord sustained me. Alleluia.

Psalm 63:
O God, You are my God; early will I seek You; my soul thirsts for You; my flesh longs for You in a dry and thirsty land where there is no water. So I have looked for You in the sanctuary, to see Your power and Your glory. Because Your lovingkindness is better than life, my lips shall praise You. Thus I will bless You while I live; I will lift up my hands in Your name. Alleluia."""
            ),
            PrayerSectionItem(
                id = "prime_gospel",
                titleAr = "الإنجيل المقدس لباكر (يوحنا ١: ١-١٧)",
                titleEn = "The Holy Gospel for Prime (John 1:1-17)",
                subtitleAr = "في البدء كان الكلمة.. وفيه كانت الحياة والنور",
                subtitleEn = "In the beginning was the Word.. In Him was life and light",
                contentAr = AgpeyaPrayerContent.getHolyGospel(PrayerId.PRIME, AppLanguage.ARABIC).text,
                contentEn = AgpeyaPrayerContent.getHolyGospel(PrayerId.PRIME, AppLanguage.ENGLISH).text,
                isGospel = true
            ),
            PrayerSectionItem(
                id = "prime_litanies",
                titleAr = "قطع باكر (أيها النور الحقيقي)",
                titleEn = "Litanies of Prime (O True Light)",
                rubricAr = "تأملات وابتهالات الصباح لمخلصنا نور العالم:",
                rubricEn = "Morning petitions honoring Christ, the Light of the World:",
                contentAr = """أيها النور الحقيقي الذي يضيء لكل إنسان آتٍ إلى العالم، أتيت إلى العالم بمحبتك للبشر، وكل الخليقة تهللت بمجيئك. خلصت أبانا آدم من الضلالة، وعتقت أمنا حواء من طلقات الموت، وأعطيتنا روح البنوة. فلنسبحك ونباركك قائلين: ذوكصابتري...
(المجد للآب والابن والروح القدس)

إذ أشرق فينا الصباح الحسي، أيها المسيح إلهنا، النور الحقيقي، فلتشرق فينا حواسك المضيئة، والأفكار النورانية، ولا تغطنا ظلمة الآلام، لكي نسبحك عقلياً مع داود قائلين: سبقت عيناي وقت السحر لألهج في جميع أقوالك، اسمع أصواتنا كعظيم رحمتك، وخلصنا أيها الرب إلهنا برأفتك.
(الآن وكل أوان وإلى دهر الداهرين، آمين)

أنتِ هي المكرمة أم النور، من مشارق الشمس إلى مغاربها يقدمون لكِ التماجيد يا والدة الإله، الكرمة الحقيقية الحاملة عنقود الحياة، نسألكِ أيتها الممتلئة نعمة، مع الرسل، من أجل خلاص نفوسنا. مبارك الرب إلهنا، مبارك الرب يوماً فيوماً، يهيئ طريقنا لأنه إله خلاصنا.""",
                contentEn = """O True Light that gives light to every person coming into the world, in Your love for mankind You came to the world, and all creation rejoiced at Your coming. You saved our father Adam from delusion and delivered Eve from the pangs of death, and gave us the Spirit of adoption. We praise and bless You saying: Glory to the Father and to the Son and to the Holy Spirit.

As the sensible morning shines upon us, O Christ our God, the True Light, let Your radiant senses and luminous thoughts shine within us, and do not let the darkness of passions overshadow us; that we may praise You spiritually with David saying: 'My eyes stay open through the watches of the night, that I may meditate on Your promises.' Hear our voices according to Your great mercy, and save us, O Lord our God, through Your compassion.
(Now and forever and unto the ages of ages. Amen.)

You are the honored Mother of the Light; from the rising of the sun to its setting, praises are offered to you, O Theotokos, the true vine who bore the Cluster of Life. We entreat you, O full of grace, with the apostles, for the salvation of our souls. Blessed is the Lord our God; blessed is the Lord day by day, Who prepares our way, for He is the God of our salvation."""
            ),
            PrayerSectionItem(
                id = "prime_gloria",
                titleAr = "تسبحة الملائكة (المجد لله في الأعالي)",
                titleEn = "Gloria in Excelsis (Glory to God in the Highest)",
                contentAr = """المجد لله في الأعالي، وعلى الأرض السلام، وفي الناس المسرة.
نسبحك، نباركك، نخدمك، نسجد لك، نعترف لك، ننطق بمجدك، نشكرك من أجل عظيم مجدك.
أيها الرب الملك السمائي، الله الآب ضابط الكل.
أيها الرب الابن الوحيد يسوع المسيح والروح القدس.
أيها الرب الإله يا حمل الله، يا ابن الآب، رافع خطية العالم، ارحمنا.
يا رافع خطية العالم، اقبل طلباتنا إليك.
أيها الجالس عن يمين أبيه، ارحمنا.
أنت وحدك قدوس، أنت وحدك العلي، يا ربي يسوع المسيح والروح القدس، في مجد الله الآب. آمين.""",
                contentEn = """Glory to God in the highest, peace on earth, and goodwill toward men.
We praise You, we bless You, we serve You, we worship You, we confess You, we proclaim Your glory, we give thanks to You for Your great glory.
O Lord, Heavenly King, God the Father Almighty.
O Lord, the Only-Begotten Son, Jesus Christ, and the Holy Spirit.
O Lord God, Lamb of God, Son of the Father, Who take away the sin of the world, have mercy on us.
You Who take away the sin of the world, receive our prayer.
You Who sit at the right hand of the Father, have mercy on us.
For You only are holy; You only are the Most High, O my Lord Jesus Christ, with the Holy Spirit, in the glory of God the Father. Amen."""
            ),
            PrayerSectionItem(
                id = "orthodox_creed",
                titleAr = "قانون الإيمان الأرثوذكسي المقدس",
                titleEn = "The Orthodox Nicene-Constantinopolitan Creed",
                rubricAr = "قانون الإيمان بالثالوث القدوس والتجسد والقيامة والكنيسة الواحدة:",
                rubricEn = "The confession of the Holy Trinity, the Incarnation, and the One Holy Church:",
                contentAr = """بالحقيقة نؤمن بإله واحد، الله الآب، ضابط الكل، خالق السماء والأرض، ما يُرى وما لا يُرى.
نؤمن برب واحد يسوع المسيح، ابن الله الوحيد، المولود من الآب قبل كل الدهور، نور من نور، إله حق من إله حق، مولود غير مخلوق، مساوٍ للآب في الجوهر، الذي به كان كل شيء.
هذا الذي من أجلنا نحن البشر، ومن أجل خلاصنا، نزل من السماء، وتجسد من الروح القدس ومن مريم العذراء، وتأنس.
وصُلب عنا على عهد بيلاطس البنطي، وتألم وقُبر وقام من بين الأموات في اليوم الثالث كما في الكتب، وصعد إلى السموات، وجلس عن يمين أبيه، وأيضاً يأتي في مجده ليدين الأحياء والأموات، الذي ليس لملكه انقضاء.
نعم نؤمن بالروح القدس، الرب المحيي المنبثق من الآب، نسجد له ونمجده مع الآب والابن، الناطق في الأنبياء.
وبكنيسة واحدة مقدسة جامعة رسولية، ونعترف بمعمودية واحدة لمغفرة الخطايا، وننتظر قيامة الأموات وحياة الدهر الآتي. آمين.""",
                contentEn = """Truly we believe in one God, God the Father Almighty, Maker of heaven and earth, of all things visible and invisible.
We believe in one Lord Jesus Christ, the Only-Begotten Son of God, begotten of the Father before all ages; Light of Light, true God of true God, begotten not created, of one essence with the Father, by Whom all things were made;
Who for us men and for our salvation came down from heaven, and was incarnate of the Holy Spirit and of the Virgin Mary, and became Man.
And He was crucified for us under Pontius Pilate, suffered, was buried, and rose from the dead on the third day according to the Scriptures; He ascended into the heavens and sits at the right hand of His Father; and He shall come again in His glory to judge the living and the dead, Whose kingdom shall have no end.
Yes, we believe in the Holy Spirit, the Lord, the Giver of life, Who proceeds from the Father, Who with the Father and the Son together is worshipped and glorified, Who spoke by the prophets.
And in one holy, universal, and apostolic Church. We confess one baptism for the remission of sins. We look for the resurrection of the dead, and the life of the age to come. Amen."""
            ),
            AgpeyaCommonPrayers.getTrisagion(),
            AgpeyaCommonPrayers.getKyrieEleisonAndConcludingSupplication(),
            PrayerSectionItem(
                id = "prime_absolution",
                titleAr = "تحليل صلاة باكر",
                titleEn = "The Absolution of Prime",
                rubricAr = "صلاة البركة ومغفرة الخطايا الخاصة بباكر:",
                rubricEn = "The prayer of blessing and forgiveness of sins proper to the morning:",
                contentAr = """أيها الرب الإله ضابط الكل، أبو ربنا وإلهنا ومخلصنا يسوع المسيح، نشكرك لأنك أقمتنا من نوم الفراش، ووهبتنا نعمة هذا اليوم الجديد، لنقف أمامك في موضعك المقدس ونسجد لاسمك الطاهر.
نسألك يا محب البشر، احفظنا في هذا اليوم بغير خطية، وانزع عنا كل شهوة ردية، ونور بصيرتنا بمعرفة وصاياك المحيية، لكي نسلك بما يرضيك نهاراً وليلاً، ونصنع الصلاح قدامك في كل حين.
لأنك أنت هو حياتنا ورجاؤنا ونورنا وخلاصنا، وإليك نرفع المجد والإكرام والسجود، أيها الآب والابن والروح القدس، الآن وكل أوان وإلى دهر الدهور. آمين.""",
                contentEn = """O Lord God Almighty, Father of our Lord, God, and Savior Jesus Christ, we thank You that You have raised us from the bed of sleep, and bestowed upon us the grace of this new day, to stand before You in Your holy place and adore Your pure name.
We ask You, O Lover of mankind, keep us this day without sin, remove from us every evil desire, and enlighten our inner eyes with the knowledge of Your life-giving commandments, that we may walk in a manner pleasing to You day and night, doing good before You at all times.
For You are our life, our hope, our light, and our salvation, and unto You we send up glory, honor, and adoration, Father, Son, and Holy Spirit, now and forever and unto the age of ages. Amen."""
            ),
            AgpeyaCommonPrayers.getFinalConcludingPrayer(lang)
        )
    }

    /**
     * Terce (صلاة الساعة الثالثة - ٩ صباحاً):
     * Descent of Holy Spirit, Psalms, Gospel, Litanies, Absolution.
     */
    fun getTerceSections(lang: AppLanguage): List<PrayerSectionItem> {
        return listOf(
            AgpeyaCommonPrayers.getIntroduction(lang),
            AgpeyaCommonPrayers.getThanksgivingPrayer(),
            AgpeyaCommonPrayers.getPsalm50(),
            PrayerSectionItem(
                id = "terce_psalms",
                titleAr = "مزامير الساعة الثالثة",
                titleEn = "Psalms of Terce",
                subtitleAr = "المزمور التاسع عشر، والمزمور الثاني والعشرون (الرب يرعاني)",
                subtitleEn = "Psalm 20 and Psalm 23 (The Lord is My Shepherd)",
                contentAr = """المزمور التاسع عشر:
يستجيب لك الرب في يوم الشدة، ينصرك اسم إله يعقوب. يرسل لك عوناً من قدسه، ومن صهيون يعضدك. يذكر جميع ذبائحك، ويستسمن محرقاتك. يعطيك الرب حسب قلبك، ويتمم كل مشورتك. نهلل بخلاصك، وباسم إلهنا ننمو. يكمل الرب كل سؤلك. هللويا.

المزمور الثاني والعشرون:
الرب يرعاني فلا يعوزني شيء. في مراعٍ خضر يربضني، إلى مياه الراحة يوردني، يرد نفسي. يهديني إلى سبل البر من أجل اسمه. إن سلكت في وادي ظل الموت لا أخاف شراً لأنك أنت معي، عصاك وعكازك هما يعزيانني. رتبت قدامي مائدة تجاه مضايقي، دهنت بالزيت رأسي، وكأسك روتني بقوة. ورحمتك تدركني جميع أيام حياتي، وأسكُن في بيت الرب إلى مدى الأيام. هللويا.""",
                contentEn = """Psalm 20:
May the Lord answer you in the day of trouble; may the name of the God of Jacob defend you; may He send you help from the sanctuary, and strengthen you out of Zion; may He remember all your offerings, and accept your burnt sacrifice. May He grant you according to your heart's desire, and fulfill all your purpose. We will rejoice in your salvation, and in the name of our God we will set up our banners! May the Lord fulfill all your petitions. Alleluia.

Psalm 23:
The Lord is my shepherd; I shall not want. He makes me to lie down in green pastures; He leads me beside the still waters. He restores my soul; He leads me in the paths of righteousness for His name's sake. Yea, though I walk through the valley of the shadow of death, I will fear no evil; for You are with me; Your rod and Your staff, they comfort me. You prepare a table before me in the presence of my enemies; You anoint my head with oil; my cup runs over. Surely goodness and mercy shall follow me all the days of my life; and I will dwell in the house of the Lord forever. Alleluia."""
            ),
            PrayerSectionItem(
                id = "terce_gospel",
                titleAr = "الإنجيل المقدس للساعة الثالثة (يوحنا ١٤: ٢٦ - ١٥: ٤)",
                titleEn = "The Holy Gospel for Terce (John 14:26 - 15:4)",
                subtitleAr = "وعد الروح القدس المعزي والكرمة الحقيقية",
                subtitleEn = "The promise of the Comforter and the True Vine",
                contentAr = AgpeyaPrayerContent.getHolyGospel(PrayerId.TERCE, AppLanguage.ARABIC).text,
                contentEn = AgpeyaPrayerContent.getHolyGospel(PrayerId.TERCE, AppLanguage.ENGLISH).text,
                isGospel = true
            ),
            PrayerSectionItem(
                id = "terce_litanies",
                titleAr = "قطع الساعة الثالثة (روحك القدوس يا رب)",
                titleEn = "Litanies of Terce (Your Holy Spirit, O Lord)",
                rubricAr = "طلب حلول الروح القدس وتطهير القلب:",
                rubricEn = "Supplication for the grace of the Holy Spirit:",
                contentAr = """روحك القدوس يا رب الذي أرسلته على تلاميذك القديسين ورسلك المكرمين في الساعة الثالثة، هذا لا تنزعه منا أيها الصالح، بل جدده في أحشائنا.
قلباً نقياً اخلق فيّ يا الله، وروحاً مستقيماً جدده في أحشائي، لا تطرحني من قدام وجهك، وروحك القدوس لا تنزعه مني.
(المجد للآب والابن والروح القدس)

أيها المسيح إلهنا، الكلمة الحقيقي الذي أرسلت روحك القدوس على رسلك في الساعة الثالثة، أعطنا نحن المتوسلين إليك نصيباً من مواهبك الإلهية، واصنع معنا رحمة ونعمة، لكي نثبت فيك وتثبت فينا وتفيض فينا ثمار الروح.
(الآن وكل أوان وإلى دهر الداهرين، آمين)

يا والدة الإله، أنتِ هي الكرمة الحقيقية الحاملة عنقود الحياة، نسألكِ أيتها الممتلئة نعمة، مع الرسل، من أجل خلاص نفوسنا. مبارك الرب إلهنا، مبارك الرب يوماً فيوماً، يهيئ طريقنا لأنه إله خلاصنا.""",
                contentEn = """Your Holy Spirit, O Lord, Whom You sent upon Your holy disciples and honored apostles at the third hour, do not take away from us, O Good One, but renew Him within our inward parts.
Create in me a clean heart, O God, and renew a steadfast spirit within me. Do not cast me away from Your presence, and do not take Your Holy Spirit from me.
(Glory to the Father and to the Son and to the Holy Spirit)

O Christ our God, the true Word, Who sent Your Holy Spirit upon Your apostles at the third hour, give us who entreat You a portion of Your divine gifts, and show us mercy and grace, that we may abide in You and You in us, bearing the fruit of the Spirit.
(Now and forever and unto the ages of ages. Amen)

O Theotokos, you are the true vine who bore the Cluster of Life. We ask you, O full of grace, together with the apostles, for the salvation of our souls. Blessed is the Lord our God, blessed is the Lord day by day; He prepares our way, for He is the God of our salvation."""
            ),
            AgpeyaCommonPrayers.getTrisagion(),
            AgpeyaCommonPrayers.getKyrieEleisonAndConcludingSupplication(),
            PrayerSectionItem(
                id = "terce_absolution",
                titleAr = "تحليل الساعة الثالثة",
                titleEn = "The Absolution of Terce",
                rubricAr = "طلب سكنى الروح القدس:",
                rubricEn = "The prayer of absolution at the third hour:",
                contentAr = """أيها الإله المحيي، صانع الكل برحمته، يا من أرسلت روحك القدوس على رسلك القديسين في الساعة الثالثة، أرسل علينا نحن عبيدك غير المستحقين نعمة روحك القدوس المحيي، وطهرنا من كل دنس الجسد والروح، واجعلنا هيكلاً نقياً لحلولك الإلهي، لنقدم لك صلوات مرضية في كل وقت، لك المجد مع ابنك الوحيد وروحك القدوس الآن وكل أوان وإلى دهر الداهرين. آمين.""",
                contentEn = """O God of all compassion, Creator of all in mercy, Who sent Your Holy Spirit upon Your holy disciples at the third hour, send down upon us, Your unworthy servants, the grace of Your life-giving Holy Spirit; cleanse us from all defilement of flesh and spirit, and make us a pure sanctuary for Your divine presence, that we may offer You acceptable prayers at all times. Glory be to You, with Your only-begotten Son and Your Holy Spirit, now and forever. Amen."""
            ),
            AgpeyaCommonPrayers.getFinalConcludingPrayer(lang)
        )
    }

    /**
     * Sext (صلاة الساعة السادسة - ١٢ ظهراً):
     * The Crucifixion at Golgotha, Psalms, Gospel, Litanies, Absolution.
     */
    fun getSextSections(lang: AppLanguage): List<PrayerSectionItem> {
        return listOf(
            AgpeyaCommonPrayers.getIntroduction(lang),
            AgpeyaCommonPrayers.getThanksgivingPrayer(),
            AgpeyaCommonPrayers.getPsalm50(),
            PrayerSectionItem(
                id = "sext_psalms",
                titleAr = "مزامير الساعة السادسة (آلام الصليب)",
                titleEn = "Psalms of Sext (The Passion on the Cross)",
                subtitleAr = "المزمور الثالث والخمسون، والمزمور التسعون (الساكن في عون العلي)",
                subtitleEn = "Psalm 54 and Psalm 91 (He Who Dwells in the Secret Place)",
                contentAr = """المزمور الثالث والخمسون:
اللهم باسمك خلصني وبقوتك احكم لي. استمع يا الله صلاتي، واصغِ إلى كلام فمي، فإن الغرباء قد قاموا عليّ والأعزاء طلبوا نفسي، ولم يجعلوا الله أمامهم. هوذا الله عوني والرب ناصر نفسي، يرد الشرور على أعدائي وبحقك استأصلهم. فأذبح لك طائعاً وأعترف لاسمك يا رب فإنه صالح، لأنك من كل ضيقة نجيتني، وبأعدائي نظرت عيني. هللويا.

المزمور التسعون:
الساكن في عون العلي، يستريح في ظل إله السماء. يقول للرب: أنت هو ناصري وملجأي، إلهي فأتكل عليه. لأنه ينجيك من فخ الصياد ومن كلمة مقلقة. في وسط منكبيه يظللك، وتحت جناحيه تعتصم، عدله يحيط بك كالسلاح. فلا تخشى من خوف الليل، ولا من سهم يطير في النهار، ولا من أمر يسلك في الظلمة، ولا من سقطة وشيطان الظهيرة. يسقط عن جانبك ألوف، وعن يمينك ربوات، وأما إليك فلا يقتربون. بل بعينيك تعاين ومجازاة الخطاة تبصر. هللويا.""",
                contentEn = """Psalm 54:
Save me, O God, by Your name, and vindicate me by Your strength. Hear my prayer, O God; give ear to the words of my mouth. For strangers have risen up against me, and oppressors have sought after my life; they have not set God before them. Behold, God is my helper; the Lord is with those who uphold my life. He will repay my enemies for their evil. Cut them off in Your truth. I will freely sacrifice to You; I will praise Your name, O Lord, for it is good. For He has delivered me out of all trouble; and my eye has seen its desire upon my enemies. Alleluia.

Psalm 91:
He who dwells in the secret place of the Most High shall abide under the shadow of the Almighty. I will say of the Lord, 'He is my refuge and my fortress; my God, in Him I will trust.' Surely He shall deliver you from the snare of the fowler and from the perilous pestilence. He shall cover you with His feathers, and under His wings you shall take refuge; His truth shall be your shield and buckler. You shall not be afraid of the terror by night, nor of the arrow that flies by day, nor of the pestilence that walks in darkness, nor of the destruction that lays waste at noonday. A thousand may fall at your side, and ten thousand at your right hand; but it shall not come near you. Only with your eyes shall you look, and see the reward of the wicked. Alleluia."""
            ),
            PrayerSectionItem(
                id = "sext_gospel",
                titleAr = "الإنجيل المقدس للساعة السادسة (متى ٥: ١-١٦)",
                titleEn = "The Holy Gospel for Sext (Matthew 5:1-16)",
                subtitleAr = "التطويبات على الجبل ونور العالم وملح الأرض",
                subtitleEn = "The Beatitudes on the Mount, Light of the World, and Salt of the Earth",
                contentAr = AgpeyaPrayerContent.getHolyGospel(PrayerId.SEXT, AppLanguage.ARABIC).text,
                contentEn = AgpeyaPrayerContent.getHolyGospel(PrayerId.SEXT, AppLanguage.ENGLISH).text,
                isGospel = true
            ),
            PrayerSectionItem(
                id = "sext_litanies",
                titleAr = "قطع الساعة السادسة (يا من سمرت على الصليب)",
                titleEn = "Litanies of Sext (Nailed to the Cross)",
                rubricAr = "تأمل ذبيحة الصليب الكفارية في منتصف النهار:",
                rubricEn = "Contemplation of Christ's sacrifice at midday:",
                contentAr = """يا من في اليوم السادس وفي وقت الساعة السادسة، سمرت على الصليب الخطية التي تجرأ عليها أبونا آدم في الفردوس، مزق صك خطايانا أيها المسيح إلهنا وخلصنا.
أنا صرخت إلى الرب فاستجاب لي، عشية وباكر ووقت الظهر أتكلم وأصرخ فيسمع صوتي، يخلص نفسي بسلام.
(المجد للآب والابن والروح القدس)

أيها المسيح إلهنا، يا من بسطت يديك الطاهرتين على خشبة الصليب، وقبلت الآلام لتردنا من سبي إبليس، نجنا من كل سهم يطير في النهار ومن شيطان الظهيرة، وقدس نفوسنا وأجسادنا.
(الآن وكل أوان وإلى دهر الداهرين، آمين)

إذ ليس لنا دالة ولا حجة ولا معذرة، من أجل كثرة خطايانا، فنحن بكِ نتوسل إلى الذي ولد منكِ يا والدة الإله العذراء، لأن كثير القوة هو رجاؤكِ ومقبولة شفاعتكِ عند مخلصنا، فلا ترفضي الخطاة من شفاعتكِ يا طاهرة، لأنه رحيم وقادر على خلاصنا، هذا الذي تألم من أجلنا لكي ينقذنا.""",
                contentEn = """O You, Who on the sixth day and in the sixth hour, were nailed to the cross for the sin which our father Adam dared commit in Paradise, tear up the handwriting of our sins, O Christ our God, and save us.
I cried to the Lord, and He heard me. Evening and morning and at noon I will pray, and cry aloud, and He shall hear my voice. He has redeemed my soul in peace.
(Glory to the Father and to the Son and to the Holy Spirit)

O Christ our God, Who stretched out Your pure hands on the wood of the Cross and endured suffering to bring us back from the captivity of Satan, deliver us from every arrow flying by day and from the demon of noonday, and sanctify our souls and bodies.
(Now and forever and unto the ages of ages. Amen)

Since we have neither boldness nor excuse because of our many sins, we entreat through you to Him Who was born of you, O Virgin Theotokos, for great and acceptable is your intercession with our Savior. Do not reject sinners from your intercession, O pure one, for He is merciful and able to save us, He Who suffered for our sake to redeem us."""
            ),
            AgpeyaCommonPrayers.getTrisagion(),
            AgpeyaCommonPrayers.getKyrieEleisonAndConcludingSupplication(),
            PrayerSectionItem(
                id = "sext_absolution",
                titleAr = "تحليل الساعة السادسة",
                titleEn = "The Absolution of Sext",
                rubricAr = "الشكر على قوة الصليب المحيي:",
                rubricEn = "The prayer of absolution at the sixth hour:",
                contentAr = """نشكرك يا ملكنا ضابط الكل، أبو ربنا وإلهنا ومخلصنا يسوع المسيح، ونمجدك لأنك جعلت أوقات صلوات سواعينا أوقات قداسة وسكون وتعزية. نشكرك لأنك صعدت على الصليب لأجل خلاصنا ومزقت صك خطايانا. نطلب إليك يا مخلصنا الصالح، انزع عنا ظلمة الشهوات والآلام المردية، واغرس فينا مخافتك وطاعتك، واهدنا إلى ملكوتك الأبدي، بالمسيح يسوع ربنا، له المجد معك ومع الروح القدس إلى الأبد. آمين.""",
                contentEn = """We thank You, O Sovereign King Almighty, Father of our Lord, God, and Savior Jesus Christ, and we glorify You for making the hours of our prayers times of holiness, peace, and solace. We thank You for ascending the Cross for our salvation and tearing the handwriting of our sins. We beseech You, O our Good Savior, remove from us the darkness of worldly desires and corrupt passions, plant in us Your fear and obedience, and guide us to Your eternal Kingdom, in Christ Jesus our Lord. Amen."""
            ),
            AgpeyaCommonPrayers.getFinalConcludingPrayer(lang)
        )
    }

    /**
     * None (صلاة الساعة التاسعة - ٣ ظهراً):
     * Death of Christ in the Flesh, Repentance of the Right Hand Thief, Psalms, Gospel, Litanies, Absolution.
     */
    fun getNoneSections(lang: AppLanguage): List<PrayerSectionItem> {
        return listOf(
            AgpeyaCommonPrayers.getIntroduction(lang),
            AgpeyaCommonPrayers.getThanksgivingPrayer(),
            AgpeyaCommonPrayers.getPsalm50(),
            PrayerSectionItem(
                id = "none_psalms",
                titleAr = "مزامير الساعة التاسعة",
                titleEn = "Psalms of None",
                subtitleAr = "المزمور الخامس والتسعون، والمزمور المائة والتاسع عشر (قطع من القطعة الطويلة)",
                subtitleEn = "Psalm 96 and selections from Psalm 119",
                contentAr = """المزمور الخامس والتسعون:
سبحوا الرب تسبيحاً جديداً، سبحي الرب يا كل الأرض. سبحوا الرب وباركوا اسمه، بشروا من يوم إلى يوم بخلاصه. حدثوا في الأمم بمجده، وفي جميع الشعوب بعجائبه. لأن الرب عظيم هو ومسبح جداً، ومرهوب على كل الآلهة. لأن كل آلهة الأمم شياطين، أما الرب فصنع السموات. الجلال والبهاء قدامه، الطهر والجلال العظيم في قدسه. هللويا.

المزمور المائة والتاسع عشر (تدن وسيلتي):
لتدنُ وسيلتي قدامك يا رب، كقولك فهمني. لتدخل طلبتي إلى حضرتك، ككلمتك أحيني. تفيض شفتاي التسبيح إذا ما علمتني فرائضك. ينطق لساني بأقوالك، لأن جميع وصاياك عادلة. لتكن يدك لمعونتي، لأني اشتهيت وصاياك. اشتقت إلى خلاصك يا رب، وناموسك هو تلاوتي. تحيا نفسي وتسبحك، وأحكامك تعينني. ضللت مثل خروف ضال، فاطلب عبدك، فإني لوصاياك لم أنسَ. هللويا.""",
                contentEn = """Psalm 96:
Oh, sing to the Lord a new song! Sing to the Lord, all the earth. Sing to the Lord, bless His name; proclaim the good news of His salvation from day to day. Declare His glory among the nations, His wonders among all peoples. For the Lord is great and greatly to be praised; He is to be feared above all gods. For all the gods of the peoples are idols, but the Lord made the heavens. Honor and majesty are before Him; strength and beauty are in His sanctuary. Alleluia.

From Psalm 119:
Let my cry come before You, O Lord; give me understanding according to Your word. Let my supplication come before You; deliver me according to Your word. My lips shall utter praise, for You teach me Your statutes. My tongue shall speak of Your word, for all Your commandments are righteousness. Let Your hand become my help, for I have chosen Your precepts. I long for Your salvation, O Lord, and Your law is my delight. Let my soul live, and it shall praise You; and let Your judgments help me. I have gone astray like a lost sheep; seek Your servant, for I do not forget Your commandments. Alleluia."""
            ),
            PrayerSectionItem(
                id = "none_gospel",
                titleAr = "الإنجيل المقدس للساعة التاسعة (لوقا ٩: ١٠-١٧)",
                titleEn = "The Holy Gospel for None (Luke 9:10-17)",
                subtitleAr = "معجزة إشباع الجموع بالخمس خبزات والسمكتين",
                subtitleEn = "Feeding the five thousand with five loaves and two fish",
                contentAr = AgpeyaPrayerContent.getHolyGospel(PrayerId.NONE, AppLanguage.ARABIC).text,
                contentEn = AgpeyaPrayerContent.getHolyGospel(PrayerId.NONE, AppLanguage.ENGLISH).text,
                isGospel = true
            ),
            PrayerSectionItem(
                id = "none_litanies",
                titleAr = "قطع الساعة التاسعة (يا من ذاق الموت بالجسد)",
                titleEn = "Litanies of None (You Who Tasted Death in the Flesh)",
                rubricAr = "تأمل موت المسيح المحيي واعتراف اللص اليمين:",
                rubricEn = "Contemplation of the Lord's death and the confession of the penitent thief:",
                contentAr = """يا من ذاق الموت بالجسد في وقت الساعة التاسعة من أجلنا نحن الخطاة، أمت حواسنا الجسمانية أيها المسيح إلهنا ونجنا.
لتدنُ وسيلتي قدامك يا رب، كقولك فهمني، لتدخل طلبتي إلى حضرتك، ككلمتك أحيني.
(المجد للآب والابن والروح القدس)

يا من أسلم الروح في يد الآب وأنت معلق على الصليب في وقت الساعة التاسعة، وفتحت للص اليمين باب الفردوس، لا تغفل عني ولا ترذلني أنا الضال، بل اهدني كما هديته واذكرني يا رب متى جئت في ملكوتك.
(الآن وكل أوان وإلى دهر الداهرين، آمين)

يا من ولدتِ البار لأجل خلاصنا، يا عذراء مريم والدة الإله، تشفعي فينا عند ابنك وإلهك، لكي يغفر لنا خطايانا وينعم علينا برحمته الواسعة.""",
                contentEn = """O You, Who tasted death in the flesh in the ninth hour for our sake, put to death our carnal senses, O Christ our God, and deliver us.
Let my cry come near before You, O Lord; give me understanding according to Your word. Let my supplication come before You; deliver me according to Your word.
(Glory to the Father and to the Son and to the Holy Spirit)

O You Who gave up the ghost into the hands of the Father as You were hanging on the Cross in the ninth hour, and opened the door of Paradise to the penitent thief, do not disregard me or cast me away, I who am lost, but lead me as You led him and remember me, O Lord, when You come into Your kingdom.
(Now and forever and unto the ages of ages. Amen)

O you who gave birth to the Righteous One for our salvation, Virgin Mary Mother of God, intercede for us before your Son and your God, that He may forgive our sins and bestow upon us His abundant mercies."""
            ),
            AgpeyaCommonPrayers.getTrisagion(),
            AgpeyaCommonPrayers.getKyrieEleisonAndConcludingSupplication(),
            PrayerSectionItem(
                id = "none_absolution",
                titleAr = "تحليل الساعة التاسعة",
                titleEn = "The Absolution of None",
                rubricAr = "شكر على خلاص الفردوس وقبول التوبة:",
                rubricEn = "Thanksgiving for the opening of Paradise to the repentant thief:",
                contentAr = """أيها السيد الرب يسوع المسيح إلهنا، يا من تأنيت على خطايانا حتى بلغت بنا إلى هذه الساعة المقدسة، التي فيها علقت على العود المحيي، وأوضحت للص التائب طريق الدخول إلى الفردوس، وأبطلت الموت بموتك المحيي. اقبل منا توبتنا واعترافنا، واغفر لنا كل ما فرط منا بفكر أو قول أو عمل، واكتب أسماءنا في سفر الحياة، برحمتك ونعمتك، يا من لك المجد والقوة مع أبيك والروح القدس إلى الأبد. آمين.""",
                contentEn = """O Master, Lord Jesus Christ our God, Who were patient with our sins until You brought us to this holy hour, in which You hung upon the life-giving tree, showed the repentant thief the path into Paradise, and abolished death by Your life-giving death. Accept our repentance and confession, forgive us all our failings in thought, word, or deed, and write our names in the Book of Life, by Your mercy and grace, for to You belong glory and power with the Father and the Holy Spirit, forever. Amen."""
            ),
            AgpeyaCommonPrayers.getFinalConcludingPrayer(lang)
        )
    }

    /**
     * Vespers (صلاة الغروب - ٥ مساءً):
     * The Sunset hour, taking down Christ from the Cross, Psalms, Gospel, Litanies, Absolution.
     */
    fun getVespersSections(lang: AppLanguage): List<PrayerSectionItem> {
        return listOf(
            AgpeyaCommonPrayers.getIntroduction(lang),
            AgpeyaCommonPrayers.getThanksgivingPrayer(),
            AgpeyaCommonPrayers.getPsalm50(),
            PrayerSectionItem(
                id = "vespers_psalms",
                titleAr = "مزامير الغروب المباركة (مزامير المصاعد)",
                titleEn = "Psalms of Vespers (Psalms of Ascents)",
                subtitleAr = "المزمور المائة والتاسع عشر (رفعت عيني إلى الجبال)، والمزمور المائة والثاني والعشرون",
                subtitleEn = "Psalm 121 (I Lift Up My Eyes) and Psalm 123",
                contentAr = """المزمور المائة والتاسع عشر:
رفعت عينيّ إلى الجبال من حيث يأتي عوني. معونتي من عند الرب صانع السماء والأرض. لا يدع رجلك تزل، لا ينعس حافظك. هوذا لا ينعس ولا ينام حارس إسرائيل. الرب يحفظك، الرب ظل لك على يدك اليمنى. لا تضربك الشمس بالنهار ولا القمر بالليل. الرب يحفظك من كل سوء، الرب يحفظ نفسك. الرب يحفظ دخولك وخروجك من الآن وإلى الأبد. هللويا.

المزمور المائة والثاني والعشرون:
إليك رفعت عينيّ يا ساكناً في السماء. ها هما مثل عيون العبيد إلى أيدي مواليهم، ومثل عيني الأمة إلى يدي سيدتها، كذلك أعيننا نحو الرب إلهنا حتى يترأف علينا. ارحمنا يا رب ارحمنا، فإننا كثيراً ما امتلأنا هواناً، وكثيراً ما شبعت نفوسنا عاراً من المخصبين وإهانة من المتعالين. هللويا.""",
                contentEn = """Psalm 121:
I will lift up my eyes to the hills—from whence comes my help? My help comes from the Lord, Who made heaven and earth. He will not allow your foot to be moved; He Who keeps you will not slumber. Behold, He Who keeps Israel shall neither slumber nor sleep. The Lord is your keeper; the Lord is your shade at your right hand. The sun shall not strike you by day, nor the moon by night. The Lord shall preserve you from all evil; He shall preserve your soul. The Lord shall preserve your going out and your coming in from this time forth, and even forevermore. Alleluia.

Psalm 123:
Unto You I lift up my eyes, O You Who dwell in the heavens. Behold, as the eyes of servants look to the hand of their masters, as the eyes of a maid to the hand of her mistress, so our eyes look to the Lord our God, until He has mercy on us. Have mercy on us, O Lord, have mercy on us! For we are exceedingly filled with contempt. Our soul is exceedingly filled with the scorning of those who are at ease, with the contempt of the proud. Alleluia."""
            ),
            PrayerSectionItem(
                id = "vespers_gospel",
                titleAr = "الإنجيل المقدس للغروب (لوقا ٤: ٣٨-٤١)",
                titleEn = "The Holy Gospel for Vespers (Luke 4:38-41)",
                subtitleAr = "شفاء حماة سمعان وشفاء المرضى عند غروب الشمس",
                subtitleEn = "Healing Simon's mother-in-law and healing the sick at sunset",
                contentAr = AgpeyaPrayerContent.getHolyGospel(PrayerId.VESPERS, AppLanguage.ARABIC).text,
                contentEn = AgpeyaPrayerContent.getHolyGospel(PrayerId.VESPERS, AppLanguage.ENGLISH).text,
                isGospel = true
            ),
            PrayerSectionItem(
                id = "vespers_litanies",
                titleAr = "قطع الغروب (إذا ما وقفنا في هيكلك المقدس)",
                titleEn = "Litanies of Vespers (Standing in Your Holy Temple)",
                rubricAr = "تأمل انقضاء النهار والشكر على مراحم الرب الدائمة:",
                rubricEn = "Evening contemplation on the temple of God and spiritual protection:",
                contentAr = """إذا ما وقفنا في هيكلك المقدس نحسب كالقيام في السماء، يا والدة الإله، أنتِ هي باب السماء، افتحي لنا باب الرحمة.
(المجد للآب والابن والروح القدس)

أيها المسيح إلهنا، يا من أنزلت من على خشبة الصليب عند المساء وكفنت بالكتان النقي ووضعت في القبر الجديد، هب لنا في مساء هذا اليوم سلامك وغفرانك، ونجنا من فخاخ العدو.
(الآن وكل أوان وإلى دهر الداهرين، آمين)

يا والدة الإله، رجاء كل أقطار الأرض، بكِ نعتصم وإليكِ نلجأ، احفظي شعبكِ ونجنا من الشدائد بشفاعتكِ المقبولة عند ابنكِ الحبيب ربنا يسوع المسيح.""",
                contentEn = """When standing in Your holy temple, we are considered as standing in heaven. O Theotokos, you are the gate of heaven; open unto us the door of mercy.
(Glory to the Father and to the Son and to the Holy Spirit)

O Christ our God, Who at eventide were taken down from the wood of the Cross, wrapped in pure linen, and laid in a new tomb, grant us in the evening of this day Your peace and forgiveness, and deliver us from the snares of the enemy.
(Now and forever and unto the ages of ages. Amen)

O Theotokos, hope of all the ends of the earth, in you we take refuge; preserve your people and deliver us from afflictions through your acceptable intercessions before your beloved Son, our Lord Jesus Christ."""
            ),
            AgpeyaCommonPrayers.getTrisagion(),
            AgpeyaCommonPrayers.getKyrieEleisonAndConcludingSupplication(),
            PrayerSectionItem(
                id = "vespers_absolution",
                titleAr = "تحليل صلاة الغروب",
                titleEn = "The Absolution of Vespers",
                rubricAr = "الشكر على اجتياز ساعات النهار بسلام:",
                rubricEn = "Thanksgiving for passing through the daytime in peace:",
                contentAr = """نشكرك يا سيدنا الحنون محب البشر، لأنك عبرت بنا النهار بسلام، وأتيت بنا إلى المساء شاكرين، وجعلتنا مستحقين أن نعاين النور في المساء. اقبل منا تمجيدنا هذا المسائي، وانزع عنا كل فكر ردئ وشكوك مظلمة، وامنحنا ليلة هادئة نقية خالية من كل قلق، لننهض في باكر ونسبح اسمك القدوس مع قديسيك، برأفات ابنك الوحيد ربنا يسوع المسيح، آمين.""",
                contentEn = """We thank You, our compassionate Master, Lover of mankind, for You have enabled us to pass through the day in peace and brought us to the evening giving thanks, making us worthy to see the light of eventide. Receive our evening glorification, remove from us every evil thought and dark anxiety, and grant us a tranquil and pure night free from all disturbance, that we may arise in the morning and praise Your holy name with all Your saints, through the compassion of Your only-begotten Son, Jesus Christ our Lord. Amen."""
            ),
            AgpeyaCommonPrayers.getFinalConcludingPrayer(lang)
        )
    }

    /**
     * Compline (صلاة النوم - ٩ مساءً):
     * Burial of Christ, Readiness for Eternity, Psalms, Gospel, Litanies, Absolution.
     */
    fun getComplineSections(lang: AppLanguage): List<PrayerSectionItem> {
        return listOf(
            AgpeyaCommonPrayers.getIntroduction(lang),
            AgpeyaCommonPrayers.getThanksgivingPrayer(),
            AgpeyaCommonPrayers.getPsalm50(),
            PrayerSectionItem(
                id = "compline_psalms",
                titleAr = "مزامير النوم والانسحاق",
                titleEn = "Psalms of Compline (Bedtime & Contrition)",
                subtitleAr = "المزمور المائة والتاسع والعشرون (من الأعماق صرخت إليك يا رب)، والمزمور المائة والثلاثون",
                subtitleEn = "Psalm 130 (Out of the Depths) and Psalm 131",
                contentAr = """المزمور المائة والتاسع والعشرون:
من الأعماق صرخت إليك يا رب، يا رب استمع صوتي. لتكن أذناك مصغيتين إلى صوت تضرعي. إن كنت للآثام راصداً يا رب، يا رب من يثبت؟ لأن من عندك المغفرة، من أجل اسمك صبرت لك يا رب. انتظرت نفسي ناموسك، ترجت نفسي الرب من محرس الصبح إلى الليل. من محرس الصبح فليتكل إسرائيل على الرب، لأن من عند الرب الرحمة، وعنده فداء كثير، وهو يفتدي إسرائيل من كل آثامه. هللويا.

المزمور المائة والثلاثون:
يا رب لم يرتفع قلبي ولم تستعلِ عيناي، ولم أسلك في العظائم ولا في العجائب التي هي أعلى مني. إن كنت لم أتضع، لكن رفعت صوتي مثل الفطيم نحو أمه، كذلك المجازاة على نفسي. فليتكل إسرائيل على الرب من الآن وإلى الأبد. هللويا.""",
                contentEn = """Psalm 130:
Out of the depths I have cried to You, O Lord; Lord, hear my voice! Let Your ears be attentive to the voice of my supplications. If You, Lord, should mark iniquities, O Lord, who could stand? But there is forgiveness with You, that You may be feared. I wait for the Lord, my soul waits, and in His word I do hope. My soul waits for the Lord more than those who watch for the morning—yes, more than those who watch for the morning. O Israel, hope in the Lord; for with the Lord there is mercy, and with Him is abundant redemption. And He shall redeem Israel from all his iniquities. Alleluia.

Psalm 131:
Lord, my heart is not haughty, nor my eyes lofty. Neither do I concern myself with great matters, nor with things too profound for me. Surely I have calmed and quieted my soul, like a weaned child with his mother; like a weaned child is my soul within me. O Israel, hope in the Lord from this time forth and forevermore. Alleluia."""
            ),
            PrayerSectionItem(
                id = "compline_gospel",
                titleAr = "الإنجيل المقدس للنوم (لوقا ٢: ٢٥-٣٢)",
                titleEn = "The Holy Gospel for Compline (Luke 2:25-32)",
                subtitleAr = "تسبحة سمعان الشيخ (الآن تطلق عبدك يا سيد بسلام)",
                subtitleEn = "The Song of Simeon (Lord, now You let Your servant depart in peace)",
                contentAr = AgpeyaPrayerContent.getHolyGospel(PrayerId.COMPLINE, AppLanguage.ARABIC).text,
                contentEn = AgpeyaPrayerContent.getHolyGospel(PrayerId.COMPLINE, AppLanguage.ENGLISH).text,
                isGospel = true
            ),
            PrayerSectionItem(
                id = "compline_litanies",
                titleAr = "قطع النوم المؤثرة (هوذا أنا عتيد أن أقف أمام الديان العادل)",
                titleEn = "Litanies of Compline (Behold, I Am About to Stand Before the Judge)",
                rubricAr = "قطع التوبة والاستعداد للوقوف أمام منبر المسيح:",
                rubricEn = "Petitions of repentance and spiritual vigilance before sleep:",
                contentAr = """هوذا أنا عتيد أن أقف أمام الديان العادل مرعوباً ومرتعباً من كثرة ذنوبي، لأن العمر المنقضي في الملاهي يستوجب الدينونة. لكن توبي يا نفسي ما دمتِ في الأرض ساكنة، لأن التراب في القبر لا يسبح، وليس في الموتى من يذكر، ولا في الجحيم من يشكر. بل انهضي من رقاد الكسل، وتضرعي إلى المخلص بالتوبة قائلة: اللهم ارحمني وخلصني.
(المجد للآب والابن والروح القدس)

لو كان العمر ثابتاً وهذا العالم مؤبداً، لكان لكِ يا نفسي حجة واضحة، لكن إذا انكشفت أفعالكِ الرديئة وشروركِ القبيحة أمام الديان العادل، فأي جواب تجيبين وأنتِ على سرير الخطايا مضطجعة، وفي إخضاع الجسد متهاونة؟ أيها المسيح إلهنا، أمام منبرك المرهوب أرتاع، وأمام مجلس دينونتك أفزع، وعن نور لاهوتك أجزع، أنا الشقي المتدنس في كل حين، لكن بادرني برحمتك قبل الانقضاء، وخلصني يا محب البشر.
(الآن وكل أوان وإلى دهر الداهرين، آمين)

يا باب السماء العقلي، افتحي لنا باب الرحمة، يا والدة الإله المكرمة، نجنا من الشدائد والأهوال، واحرسينا بنعمة ابنك من ضربات العدو وأفكار الظلام، يا شفيعة جنسنا المقبولة، آمين.""",
                contentEn = """Behold, I am about to stand before the Just Judge, trembling because of my many sins; for the life spent in pleasures warrants condemnation. But repent, O my soul, as long as you dwell on earth, for the dust in the grave does not praise, nor is there remembrance of God in death, nor thanksgiving in Hades. Arise from the slumber of laziness, and cry out to the Savior in repentance saying: God have mercy on me and save me.
(Glory to the Father and to the Son and to the Holy Spirit)

If life were perpetual and this world everlasting, you would have a clear excuse, O my soul. But when your wicked deeds and ugly vices are revealed before the Just Judge, what answer will you give, lying upon the bed of sins and neglecting the mastery of the flesh? O Christ our God, before Your fearful judgment seat I am terrified, and of the light of Your divinity I am in dread, I the miserable sinner who am defiled at all times; yet anticipate me with Your mercy before the end, and save me, O Lover of mankind.
(Now and forever and unto the ages of ages. Amen)

O spiritual gate of heaven, open unto us the door of mercy, honored Theotokos; deliver us from afflictions and terrors, and guard us by the grace of your Son from the arrows of the enemy and thoughts of darkness, O acceptable advocate of our race. Amen."""
            ),
            AgpeyaCommonPrayers.getTrisagion(),
            AgpeyaCommonPrayers.getKyrieEleisonAndConcludingSupplication(),
            PrayerSectionItem(
                id = "compline_absolution",
                titleAr = "تحليل صلاة النوم",
                titleEn = "The Absolution of Compline",
                rubricAr = "طلب الراحة النقية والحراسة الملائكية أثناء الليل:",
                rubricEn = "The prayer of absolution asking for holy rest and angelical guardianship during sleep:",
                contentAr = """أيها الرب إلهنا، يا من غفرت لنا في هذا اليوم بمراحمك الجزيلة، وسترتنا بلطفك، نسألك أن تنعم علينا في هذه الليلة بنوم هادئ نقي، ونجنا من خيالات الليل وسهام العدو المتقدة ناراً.
أيقظنا في الوقت المناسب لتمجيدك وتسبيحك، وأحطنا بملائكتك الحراس ليحفظونا من كل سوء.
برأفات ومحبة البشر اللتين لابنك الوحيد، ربنا وإلهنا ومخلصنا يسوع المسيح، الذي له المجد والقدرة معك ومع الروح القدس إلى الأبد. آمين.""",
                contentEn = """O Lord our God, Who have forgiven us this day through Your abundant mercies and shielded us with Your loving-kindness, we beseech You to grant us this night a peaceful and pure sleep, and deliver us from fantasies of the night and the fiery darts of the enemy.
Awaken us at the proper time to glorify and praise You, and surround us with Your guardian angels to keep us from every evil.
Through the compassion and love of mankind of Your only-begotten Son, our Lord, God, and Savior Jesus Christ, to Whom with You and the Holy Spirit belong glory and dominion forever. Amen."""
            ),
            AgpeyaCommonPrayers.getFinalConcludingPrayer(lang)
        )
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
     */
    fun getVeilSections(lang: AppLanguage): List<PrayerSectionItem> {
        return listOf(
            AgpeyaCommonPrayers.getIntroduction(lang),
            AgpeyaCommonPrayers.getThanksgivingPrayer(),
            AgpeyaCommonPrayers.getPsalm50(),
            PrayerSectionItem(
                id = "veil_psalms",
                titleAr = "مزامير صلاة الستار (مناجاة الرهبان)",
                titleEn = "Psalms of the Veil (Monastic Solitude)",
                subtitleAr = "المزمور المائة والتاسع والثلاثون (يا رب اختبرتني وعرفتني)",
                subtitleEn = "Psalm 139 (O Lord, You Have Searched Me and Known Me)",
                contentAr = """المزمور المائة والتاسع والثلاثون:
يا رب اختبرتني وعرفتني. أنت عرفت جلوسي وقيامي، فهمت فكري من بعيد. مسلكي ومربضي ذرّيت، وكل طرقي عرفت. لأنه ليس في لساني كلمة إلا وأنت يا رب عرفتها كلها. من خلف ومن قدام حاصرتني، وجعلت عليّ يدك. عجيبة هذه المعرفة فوقي، ارتفعت لا أستطيعها. أين أذهب من روحك؟ ومن وجهك أين أهرب؟ إن صعدت إلى السموات فأنت هناك، وإن هبطت إلى الهاوية فها أنت. هللويا.""",
                contentEn = """Psalm 139:
O Lord, You have searched me and known me. You know my sitting down and my rising up; You understand my thought afar off. You comprehend my path and my lying down, and are acquainted with all my ways. For there is not a word on my tongue, but behold, O Lord, You know it altogether. You have hedged me behind and before, and laid Your hand upon me. Such knowledge is too wonderful for me; it is high, I cannot attain it. Where can I go from Your Spirit? Or where can I flee from Your presence? If I ascend into heaven, You are there; if I make my bed in hell, behold, You are there. Alleluia."""
            ),
            PrayerSectionItem(
                id = "veil_gospel",
                titleAr = "الإنجيل المقدس للستار (يوحنا ١: ١٤-٢٨)",
                titleEn = "The Holy Gospel for the Veil (John 1:14-28)",
                subtitleAr = "والكلمة صار جسداً وحل بيننا ورأينا مجده",
                subtitleEn = "And the Word became flesh and dwelt among us",
                contentAr = AgpeyaPrayerContent.getHolyGospel(PrayerId.VEIL, AppLanguage.ARABIC).text,
                contentEn = AgpeyaPrayerContent.getHolyGospel(PrayerId.VEIL, AppLanguage.ENGLISH).text,
                isGospel = true
            ),
            PrayerSectionItem(
                id = "veil_litanies",
                titleAr = "قطع صلاة الستار (أعطني يا رب ينابيع دموع كثيرة)",
                titleEn = "Litanies of the Veil (Give Me Fountains of Tears)",
                rubricAr = "قطع الانسحاق الشديد والدموع في خلوة القلاية:",
                rubricEn = "Contrite petitions of monastic tears and repentance:",
                contentAr = """أعطني يا رب ينابيع دموع كثيرة كما أعطيت المرأة الخاطئة في القديم، واجعلني مستحقاً أن أبل قدميك اللتين أعتقتاني من طريق الضلالة، وأسكب عليك طيباً فائحة رائحته، وأقتني لي بالتوبة سيرة طاهرة نقية، لكي أسمع أنا أيضاً ذلك الصوت الفرح: إيمانك خلصك فاذهب بسلام.
(المجد للآب والابن والروح القدس)

إذ أتأمل كثرة أعمالي الشريرة وأتذكر تلك الدينونة المخوفة، تأخذني رعدة وأهرب إليك يا الله محب البشر، فلا تعرض بوجهك عني أنا المتوسل إليك، أنت وحدك الذي بلا خطية، أنعم على نفسي المسكينة بالانسحاق قبل أن يأتي اليوم الذي فيه لا ينفع البكاء.
(الآن وكل أوان وإلى دهر الداهرين، آمين)

يا والدة الإله، يا ينبوع المراحم وميناء الخلاص، الجئينا تحت ظلال شفاعتك، ونجنا من بحر هذا العالم المتلاطم الأمواج، لنصل إلى ميناء الحياة الأبدية بسلام.""",
                contentEn = """Give me, O Lord, fountains of abundant tears as You once gave the sinful woman of old, and make me worthy to wash Your feet which delivered me from the path of error, pouring out precious ointment, that by true repentance I may gain a pure life and hear that joyful voice: 'Your faith has saved you, go in peace.'
(Glory to the Father and to the Son and to the Holy Spirit)

When I reflect upon the multitude of my evil deeds and recall that dreadful judgment, trembling overtakes me and I flee unto You, O God Lover of mankind. Do not turn away Your face from me as I entreat You; You Who alone are without sin, grant my poor soul contrition before the day arrives when weeping will no longer avail.
(Now and forever and unto the ages of ages. Amen)

O Theotokos, fountain of mercies and haven of salvation, shelter us under the shadow of your intercession, and deliver us from the stormy sea of this world, that we may safely reach the harbor of eternal life."""
            ),
            AgpeyaCommonPrayers.getTrisagion(),
            AgpeyaCommonPrayers.getKyrieEleisonAndConcludingSupplication(),
            PrayerSectionItem(
                id = "veil_absolution",
                titleAr = "تحليل صلاة الستار",
                titleEn = "The Absolution of the Veil",
                rubricAr = "تحليل خلوة الليل وستر الخطايا:",
                rubricEn = "Absolution of the night veil:",
                contentAr = """أيها الرب الإله يسوع المسيح، الذي بسط يديه على الصليب ليضم الجميع إلى خلاصه، استرنا بستر جناحيك، وانزع عنا كل فكر ردئ، واغفر لنا آثامنا، وهبنا نقاوة القلب وسلام الضمير، لكي ننام بسلام ونستيقظ لنسبحك مع جميع ملائكتك وقديسيك، لك المجد إلى الأبد. آمين.""",
                contentEn = """O Lord God Jesus Christ, Who stretched out Your hands on the Cross to embrace all into Your salvation, cover us under the shadow of Your wings, remove from us every evil thought, forgive our sins, and grant us purity of heart and peace of conscience, that we may sleep in peace and awake to praise You with all Your angels and saints, to You be glory forever. Amen."""
            ),
            AgpeyaCommonPrayers.getFinalConcludingPrayer(lang)
        )
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
