package com.example.data.model

import com.example.localization.AppLanguage

/**
 * Full liturgical text of the Eleventh Hour Prayer (صلاة الساعة الحادية عشرة - صلاة الغروب)
 * Purified, unabridged, containing the exact 12 Psalms (116, 117, 119, 120, 121, 122, 123, 124, 125, 126, 127, 128),
 * Gospel of Luke, Litanies of the 11th Hour ("إذا كان البار بالجهد يخلص"), Absolution, and Concluding Prayers.
 */
object VespersPrayerData {

    fun getSections(lang: AppLanguage): List<PrayerSectionItem> {
        val list = mutableListOf<PrayerSectionItem>()

        // 1. مقدمة كل ساعة
        list.add(AgpeyaCommonPrayers.getIntroduction(lang))

        // 2. صلاة الشكر
        list.add(AgpeyaCommonPrayers.getThanksgivingPrayer())

        // 3. المزمور الخمسون
        list.add(AgpeyaCommonPrayers.getPsalm50())

        // 4. بدء صلاة الغروب
        list.add(
            PrayerSectionItem(
                id = "vespers_intro",
                titleAr = "بدء صلاة الغروب (الساعة الحادية عشرة)",
                titleEn = "Beginning of the Eleventh Hour Prayer (Vespers)",
                rubricAr = "صلاة الساعة الحادية عشرة من النهار المبارك نذكر فيها إنزال جسد مخلصنا الصالح من على الصليب وتكفينه ووضعه في القبر:",
                rubricEn = "In the Eleventh Hour we commemorate the taking down of our Savior's body from the Cross and His burial:",
                contentAr = """صلاة الساعة الحادية عشرة من النهار المبارك، أقدمها للمسيح ملكي وإلهي، وأرجوه أن يغفر لي خطاياي.
من مزامير معلمنا داود النبي، بركاته علينا. آمين.""",
                contentEn = """The Eleventh Hour prayer of the blessed day, I offer to Christ my King and my God, and I beseech Him to forgive me my sins.
From the Psalms of our father David the prophet, his blessings be with us all. Amen."""
            )
        )

        // 5. مزامير صلاة الغروب كاملة (١٢ مزموراً)
        // المجموعة الأولى: المزامير ١١٦، ١١٧، ١١٩، ١٢٠
        list.add(
            PrayerSectionItem(
                id = "vespers_psalms_part1",
                titleAr = "مزامير صلاة الغروب (المزامير: ١١٦، ١١٧، ١١٩، ١٢٠)",
                titleEn = "Psalms of Vespers (Psalms 116, 117, 119, 120)",
                subtitleAr = "المزمور المائة والسادس عشر، السابع عشر، التاسع عشر، والعشرون",
                subtitleEn = "Psalms 116, 117, 119, and 120",
                contentAr = """(١) المزمور المائة والسادس عشر:
سبحوا الرب يا جميع الأمم، ولتباركه كافة الشعوب. لأن رحمته قد ثبتت علينا، وحق الرب يدوم إلى الأبد. هللويا.

(٢) المزمور المائة والسابع عشر:
اعترفوا للرب فإنه صالح، وإن إلى الأبد رحمته. ليقل بيت إسرائيل: إنه صالح، وإن إلى الأبد رحمته. ليقل بيت هارون: إنه صالح، وإن إلى الأبد رحمته. ليقل جميع خائفي الرب: إنه صالح، وإن إلى الأبد رحمته. في ضيقي صرخت إلى الرب فاستجاب لي وأخرجني إلى الرحب. الرب عوني فلا أخشى ماذا يصنع بي الإنسان. الرب عوني وأنا أرى بأعدائي. الاتكال على الرب خير من الاتكال على البشر. الرجاء بالرب خير من الرجاء بالرؤساء. كل الأمم أحاطوا بي وباسم الرب انتقمت منهم. أحاطوا بي واكتنفوني وباسم الرب انتقمت منهم. أحاطوا بي مثل النحل حول الشهد، والتهبوا كنار في الشوك وباسم الرب انتقمت منهم. دفعت فتعثرت لأسقط، والرب عضدني. قوتي وتسبحتي هو الرب، وقد صار لي خلاصاً. صوت التهليل والخلاص في مساكن الصديقين: يمين الرب صنعت قوة، يمين الرب رفعتني، يمين الرب صنعت قوة. لا أموت بل أحيا، وأحدث بأعمال الرب. تأديباً أدبني الرب، وإلى الموت لم يسلمني. افتحوا لي أبواب البر لكي أدخل فيها وأعترف للرب. هذا هو باب الرب، والصديقون يدخلون فيه. أعترف لك يا رب لأنك استجبت لي، وصرت لي خلاصاً. الحجر الذي رذله البناؤون، هذا صار رأساً للزاوية. من قِبَل الرب كان هذا، وهو عجيب في أعيننا. هذا هو اليوم الذي صنعه الرب، فلنبتهج ونفرح فيه. يا رب خلصنا، يا رب سهل طريقنا. مبارك الآتي باسم الرب، باركناكم من بيت الرب. الله الرب أضاء علينا، رتبوا عيداً في الواصلين إلى قرون المذبح. أنت هو إلهي فأشكرك، إلهي أنت فأرفعك. أعترف لك يا رب لأنك استجبت لي، وصرت لي خلاصاً. اعترفوا للرب فإنه صالح، وإن إلى الأبد رحمته. هللويا.

(٣) المزمور المائة والتاسع عشر:
إليك يا رب صرخت في حزني فاستجبت لي. يا رب نجِ نفسي من الشفاه الظالمة، ومن اللسان الغاش. ماذا تُعطى وماذا تُزاد أيتها اللسان الغاش؟! سهام الأقوياء المرهفة مع جمر البرية. ويلي فإن غربتي قد طالت عليّ، وسكنت في خيام قيدار. طويلاً سكنت نفسي في الغربة، مع مبغضي السلام كنت صاحب سلام، وحين كنت أكلمهم كانوا يحاربونني باطلاً. هللويا.

(٤) المزمور المائة والعشرون:
رفعت عيني إلى الجبال، من حيث يأتي عوني. معونتي من عند الرب، الذي صنع السماء والأرض. لا يدع رجلك تزل، ولا ينعس حافظك. هوذا لا ينعس ولا ينام حارس إسرائيل. الرب يحفظك، الرب ظل على يدك اليمنى. لا تضربك الشمس بالنهار، ولا القمر بالليل. الرب يحفظك من كل سوء، الرب يحفظ نفسك. الرب يحفظ دخولك وخروجك، من الآن وإلى الأبد. هللويا.""",
                contentEn = """(1) Psalm 116 (117): Praise the Lord, all you Gentiles! Laud Him, all you peoples!...
(2) Psalm 117 (118): Oh, give thanks to the Lord, for He is good! For His mercy endures forever...
(3) Psalm 119 (120): In my distress I cried to the Lord, and He heard me...
(4) Psalm 120 (121): I will lift up my eyes to the hills—from whence comes my help?... Alleluia."""
            )
        )

        // المجموعة الثانية: المزامير ١٢١، ١٢٢، ١٢٣، ١٢٤
        list.add(
            PrayerSectionItem(
                id = "vespers_psalms_part2",
                titleAr = "مزامير صلاة الغروب (المزامير: ١٢١، ١٢٢، ١٢٣، ١٢٤)",
                titleEn = "Psalms of Vespers (Psalms 121, 122, 123, 124)",
                subtitleAr = "المزمور المائة والحادي والعشرون، الثاني والعشرون، الثالث والعشرون، والرابع والعشرون",
                subtitleEn = "Psalms 121, 122, 123, and 124",
                contentAr = """(٥) المزمور المائة والحادي والعشرون:
فرحت بالقائلين لي: إلى بيت الرب نذهب. وقفت أرجلنا في ديارك يا أورشليم. أورشليم المبنية كمدينة متصلة ببعضها. لأن هناك صعدت القبائل، قبائل الرب، شهادة لإسرائيل للاعتراف لاسم الرب. لأن هناك نُصبت كراسي للقضاء، كراسي بيت داود. اسألوا السلامة لأورشليم، والخصب لمحبيكِ. ليكن السلام في حصنك، والخصب في أبراجك الرصينة. من أجل إخوتي وأقرابي تكلمت من أجلك بالسلام، ومن أجل بيت الرب إلهنا التمست لك الخيرات. هللويا.

(٦) المزمور المائة والثاني والعشرون:
إليك رفعت عيني يا ساكناً في السماء. ها هما مثل عيون العبيد إلى أيدي مواليهم، ومثل عيني الأمة إلى يدي سيدتها، كذلك أعيننا نحو الرب إلهنا حتى يتراءف علينا. ارحمنا يا رب ارحمنا، فإننا كثيراً ما امتلأنا هواناً، وكثيراً ما شبعت نفوسنا عاراً من المخصبين، وإهانة من المتعالين. هللويا.

(٧) المزمور المائة والثالث والعشرون:
لولا أن الرب كان معنا، ليقل إسرائيل: لولا أن الرب كان معنا حين قام الناس علينا، لابتلعونا ونحن أحياء، عند احتدام غضبهم علينا، إذاً لغرقنا في الماء، لعبرت نفوسنا السيل، بل لعبرت نفوسنا الماء الذي لا قوام له. مبارك الرب الذي لم يسلمنا فريسة لأسنانهم. نجت نفوسنا مثل العصفور من فخ الصيادين، الفخ انكسر ونحن نجونا. عوننا باسم الرب، الذي صنع السماء والأرض. هللويا.

(٨) المزمور المائة والرابع والعشرون:
المتوكلون على الرب مثل جبل صهيون، لا يتزعزع إلى الأبد، الساكن في أورشليم. الجبال حولها، والرب حول شعبه، من الآن وإلى الأبد. لأن الرب لا يترك عصا الخطاة تستقر على نصيب الصديقين، لكيلا يمد الصديقون أيديهم إلى الإثم. أحسن يا رب إلى الصالحين وإلى المستقيمي القلوب. أما الذين يميلون إلى المسالك الملتوية، فيسلكهم الرب مع فعلة الإثم، والسلام على إسرائيل. هللويا.""",
                contentEn = """(5) Psalm 121 (122): I was glad when they said to me, 'Let us go into the house of the Lord'...
(6) Psalm 122 (123): Unto You I lift up my eyes, O You who dwell in the heavens...
(7) Psalm 123 (124): If it had not been the Lord who was on our side, let Israel now say...
(8) Psalm 124 (125): Those who trust in the Lord are like Mount Zion, which cannot be moved... Alleluia."""
            )
        )

        // المجموعة الثالثة: المزامير ١٢٥، ١٢٦، ١٢٧، ١٢٨
        list.add(
            PrayerSectionItem(
                id = "vespers_psalms_part3",
                titleAr = "مزامير صلاة الغروب (المزامير: ١٢٥، ١٢٦، ١٢٧، ١٢٨)",
                titleEn = "Psalms of Vespers (Psalms 125, 126, 127, 128)",
                subtitleAr = "المزمور المائة والخامس والعشرون، السادس والعشرون، السابع والعشرون، والثامن والعشرون",
                subtitleEn = "Psalms 125, 126, 127, and 128",
                contentAr = """(٩) المزمور المائة والخامس والعشرون:
إذا ما رد الرب سبي صهيون صرنا مثل الفرحين. حينئذ امتلأ فمنا فرحاً، ولساننا تهليلاً. حينئذ يقال في الأمم: إن الرب قد عظم الصنيع معهم. عظم الرب الصنيع معنا فصرنا فرحين. اردد يا رب سبينا مثل السيول في الجنوب. الذين يزرعون بالدموع يحصدون بالابتهاج. سيراً كانوا يسيرون وهم يبكون حاملين بذورهم، وعوداً يعودون بالفرح حاملين حزمهم. هللويا.

(١٠) المزمور المائة والسادس والعشرون:
إن لم يبنِ الرب البيت فباطلاً تعب البناؤون، وإن لم يحرس الرب المدينة فباطلاً سهر الحراس. باطل هو لكم التبكير إلى القيام، والقعود بعد التعب، يا آكلي خبز الأتعاب، فإنه يعطي أحباءه نوماً. البنون ميراث من الرب، ثمرة البطن أجر منه. كالسهام بيد الجبار، كذلك بنو الشبيبة. طوبى للرجل الذي يملأ جعبته منهم، لا يخزون إذا كلموا أعداءهم في الأبواب. هللويا.

(١١) المزمور المائة والسابع والعشرون:
طوبى لجميع الذين يتقون الرب، السالكين في طرقه. تأكل من تعب يديك، طوباك وخير لك. امرأتك مثل كرمة مخصبة في جوانب بيتك، بنوك مثل غصون الزيتون الجدد حول مائدتك. هكذا يبارك الرجل المتقي الرب، يباركك الرب من صهيون، وتبصر خيرات أورشليم كل أيام حياتك، وتبصر بني بنيك، والسلام على إسرائيل. هللويا.

(١٢) المزمور المائة والثامن والعشرون:
مراراً كثيرة حاربوني منذ صباي، ليقل إسرائيل: مراراً كثيرة قاتلوني منذ شبابي، وإنهم لم يقدروا عليّ. على ظهري جلدني الخطاة وأطالوا إثمهم. الرب صديق هو، يقطع أعناق الخطاة. فليخزَ وليرتد إلى الوراء كل الذين يبغضون صهيون. وليكونوا مثل عشب السطوح الذي يجف قبل أن يقلع، الذي لم يملأ الحاصد منه كفه، ولا الذي يجمع الحزم إبطه، ولم يقل المجتازون: بركة الرب عليكم، باركناكم باسم الرب. هللويا.""",
                contentEn = """(9) Psalm 125 (126): When the Lord brought back the captivity of Zion, we were like those who dream...
(10) Psalm 126 (127): Unless the Lord builds the house, they labor in vain who build it...
(11) Psalm 127 (128): Blessed is every one who fears the Lord, who walks in His ways...
(12) Psalm 128 (129): Many a time they have afflicted me from my youth, let Israel now say... Alleluia."""
            )
        )

        // 6. الإنجيل المقدس
        list.add(
            PrayerSectionItem(
                id = "vespers_gospel",
                titleAr = "الإنجيل المقدس للغروب (لوقا ٤: ٣٨-٤١)",
                titleEn = "The Holy Gospel for Vespers (Luke 4:38-41)",
                subtitleAr = "من إنجيل معلمنا لوقا البشير (شفاء حماة سمعان ومرضى الغروب)",
                subtitleEn = "According to Saint Luke (Healing at Sunset)",
                contentAr = """ولما قام من المجمع دخل بيت سمعان. وكانت حماة سمعان ممسوكة بحمى شديدة، فسألوه من أجلها. فوقف فوقها وانتهر الحمى فتركتها، وفي الحال قامت وخدمتهم.
وعند غروب الشمس، كان كل الذين عندهم مرضى بأنواع أمراض مختلفة يقدمونهم إليه، فوضع يديه على كل واحد منهم وشفاهم.
وكانت الشياطين تخرج من كثيرين وهي تصرخ وتقول: أنت هو المسيح ابن الله! فانتهرهم ولم يدعهم يتكلمون، لأنهم كانوا قد عرفوا أنه هو المسيح.
والمجد لله دائماً أبدياً. آمين.""",
                contentEn = """Now He arose from the synagogue and entered Simon’s house. But Simon’s wife’s mother was sick with a high fever, and they made request of Him concerning her. So He stood over her and rebuked the fever, and it left her. And immediately she arose and served them. When the sun was setting, all those who had any that were sick with various diseases brought them to Him; and He laid His hands on every one of them and healed them... Glory to God forever. Amen.""",
                isGospel = true
            )
        )

        // 7. القطع
        list.add(
            PrayerSectionItem(
                id = "vespers_litanies",
                titleAr = "قطع صلاة الغروب (توبة أصحاب الساعة الحادية عشرة)",
                titleEn = "The Litanies of Vespers (Repentance of the Eleventh Hour Workers)",
                rubricAr = "ابتهالات المساء والتوبة وقبول المراحم الإلهية:",
                rubricEn = "Evening supplications pleading for repentance and acceptance with the laborers of the 11th hour:",
                contentAr = """إذا كان البار بالجهد يخلص، فأين أظهر أنا الخاطئ؟! ثقل النهار وحره لم أحتمل لضعف بشريتي، لكن أنت يا الله الرحوم احسبني مع أصحاب الساعة الحادية عشرة، لأني هاأنذا بالآثام حبل بي، وبالخطايا ولدتني أمي، فما أجسر أن أنظر إلى علو السماء، بل اتكل على غنى رحمتك ومحبتك للبشر صارخاً وقائلاً: اللهم اغفر لي أنا الخاطئ وارحمني.
ذوكصابتري كيه إيو كي آجيو ابنيفماتي (المجد للآب والابن والروح القدس).

أسرع لي يا مخلصي بفتح الأحضان الأبوية، لأني أفنيت عمري في اللذات والشهوات، وقد مضى مني النهار وفات، فالآن اتكل على غنى رأفاتك التي لا تفرغ، فلا تغفل قلباً خاشعاً مفتقراً لرحمتك، لأني إليك أصرخ يا رب بخشوع: أخطأت يا أبتاه في السماء وقدامك، ولست مستحقاً بعد أن أدعى لك ابناً، بل اجعلني كأحد أجرائك.
كي نين، كي آ إي، كي ايستوس إي أوناس تون إي أونون آمين (الآن وكل أوان وإلى دهر الداهرين، آمين).

لكل إثم بحرص ونشاط صنعت، ولكل خطية بشوق وهوى ارتكبت، ولكل عقوبة وانتقام استوجبت، فهيئي لي أسباب التوبة أيتها السيدة العذراء، فإليكِ ألتجئ وبكِ أتشفع، وإياكِ أدعو أن تعينيني لئلا أخزى، وعند مفارقة نفسي من جسدي احضري عندي، واهزمي مؤامرة الأعداء، وأغلقي أبواب الجحيم لئلا يبتلعوا نفسي، يا عروساً بلا عيب للختن الحقيقي.
الآن وكل أوان وإلى دهر الدهور كلها. آمين.""",
                contentEn = """If the righteous one is scarcely saved, where shall I, the sinner, appear? The burden and heat of the day I could not bear because of the weakness of my humanity. But count me, O merciful God, with the fellows of the eleventh hour... (Glory to the Father and to the Son and to the Holy Spirit)... Hasten unto me, O my Savior, with fatherly open arms... (Now and forever and unto the ages of ages. Amen)... Unto every iniquity with zeal I have committed... prepare for me the ways of repentance, O Virgin Lady... Amen."""
            )
        )

        // 8. الثلاث تقديسات
        list.add(AgpeyaCommonPrayers.getTrisagion())

        // 9. قانون الإيمان الأرثوذكسي
        list.add(
            PrayerSectionItem(
                id = "orthodox_creed",
                titleAr = "قانون الإيمان المقدس الأرثوذكسي",
                titleEn = "The Orthodox Creed",
                contentAr = """بالحقيقة نؤمن بإله واحد، الله الآب، ضابط الكل، خالق السماء والأرض، ما يُرى وما لا يُرى.

نؤمن برب واحد يسوع المسيح، ابن الله الوحيد، المولود من الآب قبل كل الدهور، نور من نور، إله حق من إله حق، مولود غير مخلوق، مساوٍ للآب في الجوهر، الذي به كان كل شيء.
هذا الذي من أجلنا نحن البشر، ومن أجل خلاصنا، نزل من السماء، وتجسد من الروح القدس ومن مريم العذراء، وتأنس.
وصُلب عنا على عهد بيلاطس البنطي، وتألم وقُبر وقام من بين الأموات في اليوم الثالث كما في الكتب، وصعد إلى السموات، وجلس عن يمين أبيه، وأيضاً يأتي في مجده ليدين الأحياء والأموات، الذي ليس لملكه انقضاء.

نعم نؤمن بالروح القدس، الرب المحيي المنبثق من الآب، نسجد له ونمجده مع الآب والابن، الناطق في الأنبياء.

وبكنيسة واحدة مقدسة جامعة رسولية.
ونعترف بمعمودية واحدة لمغفرة الخطايا.
وننتظر قيامة الأموات وحياة الدهر الآتي. آمين.""",
                contentEn = """Truly we believe in one God, God the Father Almighty, Maker of heaven and earth, of all things visible and invisible..."""
            )
        )

        // 10. كيرياليسون ٤١ مرة مع قدوس قدوس قدوس
        list.add(
            PrayerSectionItem(
                id = "kyrie_and_holy_holy",
                titleAr = "كيرياليسون (يا رب ارحم ٤١ مرة) وقدوس قدوس قدوس",
                titleEn = "Lord Have Mercy (41 times) & Holy, Holy, Holy",
                rubricAr = "يقرأ المصلي كيرياليسون ٤١ مرة بخشوع ثم صلاة قدوس قدوس قدوس:",
                rubricEn = "The worshipper recites Kyrie Eleison 41 times followed by Holy, Holy, Holy:",
                contentAr = """كيرياليسون (يا رب ارحم) ٤١ مرة.

قدوس، قدوس، قدوس، رب الصباؤوت. السماء والأرض مملوءتان من مجدك وكرامتك. ارحمنا يا الله الآب ضابط الكل. أيها الثالوث القدوس ارحمنا. أيها الرب إله القوات كن معنا، لأنه ليس لنا معين في شدائدنا وضيقاتنا سواك.

حل واغفر واصفح لنا يا الله عن سيئاتنا، التي صنعناها بإرادتنا والتي صنعناها بغير إرادتنا، التي فعلناها بمعرفة والتي فعلناها بغير معرفة، الخفية والظاهرة. يا رب اغفرها لنا، من أجل اسمك القدوس الذي دُعي علينا، كرحمتك يا رب وليس كخطايانا.

واجعلنا مستحقين أن نقول بشكر:
أبانا الذي في السموات...""",
                contentEn = """Lord have mercy (Kyrie Eleison) 41 times.

Holy, Holy, Holy, Lord of Sabaoth. Heaven and earth are full of Your glory and honor. Have mercy on us, O God the Father, the Pantocrator..."""
            )
        )

        // 11. تحليل صلاة الغروب
        list.add(
            PrayerSectionItem(
                id = "vespers_absolution",
                titleAr = "تحليل صلاة الغروب",
                titleEn = "The Absolution of Vespers",
                rubricAr = "شكر على اجتياز النهار بسلام وطلب الغفران:",
                rubricEn = "Thanksgiving for passing the day in peace and petition for forgiveness:",
                contentAr = """نشكرك يا ملكنا الرحوم، لأنك منحتنا أن نعبر هذا اليوم بسلام، وأتيت بنا إلى المساء شاكرين، وجعلتنا مستحقين أن ننظر النور إلى الغروب.

اللهم اقبل منا تمجيدنا هذا الذي صيرناه الآن، ونجنا من حيل المضاد، وأبطل عنا كافة فخاخه المنصوبة لنا.
امنحنا في هذا الليل المقبل سلاماً بغير قلق، وهدوءاً بغير اضطراب، لكي نجتازه أيضاً بطهارة وعفة، وننهض لنسبحك ونمجد اسمك القدوس في كل حين، مع أبيك غير المبتدئ والروح القدس المحيي المساوي لك، الآن وكل أوان وإلى دهر الداهرين كلها. آمين.""",
                contentEn = """We thank You, our compassionate King, for You have granted us to pass this day in peace, and brought us to the evening giving thanks, and made us worthy to behold the light until sunset. O God, accept our praise which is offered now, and deliver us from the snares of the adversary, and abolish all his traps set against us. Grant us in this coming night peace without anxiety, and tranquility without disturbance, that we may pass it also in purity and righteousness... Amen."""
            )
        )

        // 12. طلبة الختام لكل ساعة
        list.add(AgpeyaCommonPrayers.getFinalConcludingPrayer(lang))

        return list
    }
}
