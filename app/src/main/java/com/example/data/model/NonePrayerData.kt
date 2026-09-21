package com.example.data.model

import com.example.localization.AppLanguage

/**
 * Full liturgical text of the Ninth Hour Prayer (صلاة الساعة التاسعة - صلاة موت مخلصنا بالجسد وتوبة اللص اليمين)
 * Purified, unabridged, containing the exact 12 Psalms (95, 96, 97, 98, 99, 100, 109, 110, 111, 112, 114, 115),
 * Gospel of Luke, Litanies of the 9th Hour, Absolution, and Concluding Prayers.
 */
object NonePrayerData {

    fun getSections(lang: AppLanguage): List<PrayerSectionItem> {
        val list = mutableListOf<PrayerSectionItem>()

        // 1. مقدمة كل ساعة
        list.add(AgpeyaCommonPrayers.getIntroduction(lang))

        // 2. صلاة الشكر
        list.add(AgpeyaCommonPrayers.getThanksgivingPrayer())

        // 3. المزمور الخمسون
        list.add(AgpeyaCommonPrayers.getPsalm50())

        // 4. بدء صلاة الساعة التاسعة
        list.add(
            PrayerSectionItem(
                id = "none_intro",
                titleAr = "بدء صلاة الساعة التاسعة",
                titleEn = "Beginning of the Ninth Hour Prayer",
                rubricAr = "صلاة الساعة التاسعة من النهار المبارك نذكر فيها موت مخلصنا الصالح بالجسد على عود الصليب وقبول توبة اللص اليمين:",
                rubricEn = "In the Ninth Hour we commemorate the death in the flesh of our Savior on the Cross and the repentance of the right-hand thief:",
                contentAr = """صلاة الساعة التاسعة من النهار المبارك، أقدمها للمسيح ملكي وإلهي، وأرجوه أن يغفر لي خطاياي.
من مزامير معلمنا داود النبي، بركاته علينا. آمين.""",
                contentEn = """The Ninth Hour prayer of the blessed day, I offer to Christ my King and my God, and I beseech Him to forgive me my sins.
From the Psalms of our father David the prophet, his blessings be with us all. Amen."""
            )
        )

        // 5. مزامير الساعة التاسعة كاملة (١٢ مزموراً)
        // المجموعة الأولى: المزامير ٩٥، ٩٦، ٩٧، ٩٨
        list.add(
            PrayerSectionItem(
                id = "none_psalms_part1",
                titleAr = "مزامير الساعة التاسعة (المزامير: ٩٥، ٩٦، ٩٧، ٩٨)",
                titleEn = "Psalms of the Ninth Hour (Psalms 95, 96, 97, 98)",
                subtitleAr = "المزمور الخامس والتسعون، السادس والتسعون، السابع والتسعون، والثامن والتسعون",
                subtitleEn = "Psalms 95, 96, 97, and 98",
                contentAr = """(١) المزمور الخامس والتسعون:
سبحوا الرب تسبيحاً جديداً، سبحي الرب يا كل الأرض. سبحوا الرب وباركوا اسمه، بشروا من يوم إلى يوم بخلاصه. حدثوا في الأمم بمجده، وفي جميع الشعوب بعجائبه. لأن الرب عظيم هو ومسبح جداً، ومرهوب على كل الآلهة. لأن كل آلهة الأمم شياطين، أما الرب فصنع السموات. الجلال والبهاء قدامه، الطهر والجلال العظيم في قدسه. قدموا للرب يا قبائل الأمم، قدموا للرب مجداً وكرامة. قدموا للرب مجداً لاسمه، احملوا الذبائح وادخلوا دياره. اسجدوا للرب في دار قدسه، فلتتزلزل الأرض كلها من وجهه. قولوا بين الأمم إن الرب قد ملك على خشبة، وثبت المسكونة فلن تتزعزع، يدين الشعوب بالاستقامة. لتفرح السموات ولتبتهج الأرض، ليعج البحر وملؤه، تفرح الصحاري وكل ما فيها. حينئذ يبتهج كل شجر الغاب أمام وجه الرب لأنه آتٍ، لأنه آتٍ ليدين الأرض. يدين المسكونة بالعدل، والشعوب بحقه. هللويا.

(٢) المزمور السادس والتسعون:
الرب قد ملك فلتبتهج الأرض، ولتفرح الجزائر الكثيرة. سحاب وضباب حوله، العدل والقضاء قوام كرسيه. النار قدامه تسلك، وتحرق أعداءه الذين حوله. أضاءت بروقه المسكونة، رأت الأرض فتزلزلت. الجبال ذابت كالشمع من قدام وجه الرب، من قدام وجه رب الأرض كلها. أخبرت السموات بعدله، وعاينت جميع الشعوب مجده. يخزى كل الساجدين للصور المنحوتة، المفتخرين بأصنامهم. اسجدوا له يا جميع ملائكته. سمعت صهيون ففرحت، وابتهجت بنات يهوذا من أجل أحكامك يا رب. لأنك أنت هو الرب العلي على كل الأرض، ارتفعت جداً على جميع الآلهة. يا محبي الرب ابغضوا الشر، يحفظ الرب نفوس قديسيه، وينقذهم من أيدي الخطاة. نور أشرق للصديقين، وفرح لمستقيمي القلوب. افرحوا أيها الصديقون بالرب، واعترفوا لذكر قدسه. هللويا.

(٣) المزمور السابع والتسعون:
سبحوا الرب تسبيحاً جديداً، لأن الرب قد صنع أعمالاً عجيبة. خلصت له يمينه وذراعه القدوسة. أعلن الرب خلاصه، كشف قدام الأمم عدله. ذكر رحمته ليعقوب، وحقه لبيت إسرائيل. رأت جميع أقاصي الأرض خلاص إلهنا. هللوا لله يا كل الأرض، سبحوا وهللوا ورتلوا. رتلوا للرب بالقيثارة، بالقيثارة وصوت المزمار، بأبواق قرنية وصوت بوق القرن. هللوا أمام الرب الملك، ليعج البحر وملؤه، المسكونة والساكنون فيها. لتصفق الأنهار بالأيدي معاً، الجبال تبتهج أمام الرب لأنه آتٍ ليدين الأرض. يدين المسكونة بالعدل، والشعوب بالاستقامة. هللويا.

(٤) المزمور الثامن والتسعون:
الرب قد ملك فلتغضب الشعوب، الجالس على الشاروبيم فلتتزلزل الأرض. الرب عظيم في صهيون، ومتعالٍ هو على كل الشعوب. ليعترفوا لاسمك العظيم المرهوب، فإنه قدوس. وكرامة الملك تحب العدل، أنت هيأت الاستقامة، أنت أجريت القضاء والعدل في يعقوب. علوا الرب إلهنا، واسجدوا لموطئ قدميه فإنه قدوس هو. موسى وهارون في كهنته، وصموئيل في الذين يدعون باسمه. كانوا يدعون الرب وهو يستجيب لهم، بعمود الغمام كان يكلمهم، لأنهم حفظوا شهاداته والفرائض التي أعطاهم. أيها الرب إلهنا أنت استجبت لهم، صرت لهم يا الله غفوراً، ومنتقماً على جميع أعمالهم. علوا الرب إلهنا، واسجدوا في جبله المقدس، فإن الرب إلهنا قدوس. هللويا.""",
                contentEn = """(1) Psalm 95 (96): Oh, sing to the Lord a new song! Sing to the Lord, all the earth...
(2) Psalm 96 (97): The Lord reigns; let the earth rejoice; let the multitude of isles be glad!...
(3) Psalm 97 (98): Oh, sing to the Lord a new song! For He has done marvelous things...
(4) Psalm 98 (99): The Lord reigns; let the peoples tremble! He dwells between the cherubim; let the earth be moved!... Alleluia."""
            )
        )

        // المجموعة الثانية: المزامير ٩٩، ١٠٠، ١٠٩، ١١٠
        list.add(
            PrayerSectionItem(
                id = "none_psalms_part2",
                titleAr = "مزامير الساعة التاسعة (المزامير: ٩٩، ١٠٠، ١٠٩، ١١٠)",
                titleEn = "Psalms of the Ninth Hour (Psalms 99, 100, 109, 110)",
                subtitleAr = "المزمور التاسع والتسعون، المائة، المائة والتاسع، والمائة والعاشر",
                subtitleEn = "Psalms 99, 100, 109, and 110",
                contentAr = """(٥) المزمور التاسع والتسعون:
هللوا للرب يا كل الأرض، اعبدوا الرب بفرح. ادخلوا أمامه بالابتهاج. اعلموا أن الرب هو الله، هو صنعنا ولسنا نحن، ونحن شعبه وغنم رعيته. ادخلوا أبوابه بالاعتراف، ودياره بالتسابيح، اعترفوا له وباركوا اسمه. فإن الرب صالح هو، وإلى الأبد رحمته، وإلى جيل فجيل حقه. هللويا.

(٦) المزمور المائة:
رحمة وحكماً أسبحك يا رب، أرتل وأفهم في طريق بلا عيب. متى تأتي إليّ؟ كنت أسلك بدعة قلبي في وسط بيتي. لم أضع أمام عيني أمراً مخالفاً للناموس، صناع المعصية أبغضت. لم يلصق بي قلب معوج، وعند ميلان الشرير عني لم أكن أعلم. والذي يغتاب قريبه سراً، هذا طردته. المتكبر العين والمنتفخ القلب، لم أواكله. عيناي على أمناء الأرض ليجلسوا معي، السالك في طريق بلا عيب، هذا كان يخدمني. المتكلم بالكبرياء لم يسكن في وسط بيتي، والمتكلم بالظلم لم يستقم أمام عيني. باكراً كنت أقتل جميع خطاة الأرض، لأبيد من مدينة الرب جميع صانعي الإثم. هللويا.

(٧) المزمور المائة والتاسع:
قال الرب لربي: اجلس عن يميني، حتى أضع أعداءك موطئاً لقدميك. يرسل لك الرب قضيب قوة من صهيون، وتسود في وسط أعدائك. معك الرئاسة في يوم قوتك، في بهاء القديسين من البطن، قبل كوكب الصبح ولدتك. حلف الرب ولن يندم: أنت هو الكاهن إلى الأبد على رتبة ملكي صادق. الرب عن يمينك، يحطم في يوم رجزه ملوكاً، يدين بين الأمم، يملأ جثثاً، يسحق رؤوساً على أرض كثيرة. يشرب من الوادي في الطريق، لذلك يرفع رأسه. هللويا.

(٨) المزمور المائة والعاشر:
أعترف لك يا رب من كل قلبي، في مجلس المستقيمين ومجمعهم. عظيمة هي أعمال الرب، ومطلوبة في جميع مشيئاته. عمله اعتراف وبهاء، وبره دائم إلى أبد الأبد. صنع ذكراً لعجائبه، رحيم هو الرب ورؤوف. أعطى خائفيه طعاماً، يذكر إلى الدهر عهده. أخبر شعبه بقوة أعماله، ليعطيهم ميراث الأمم. أعمال يديه حق وعدل، وصاياه كلها ثابتة، معتمدة إلى دهر الدهور، مصنوعة بالحق والاستقامة. أرسل خلاصاً لشعبه، أوصى إلى الأبد بعهده، قدوس ومرهوب اسمه. رأس الحكمة مخافة الرب، وفهم صالح لكل عامليها، وتسبحته قائمة إلى أبد الأبد. هللويا.""",
                contentEn = """(5) Psalm 99 (100): Make a joyful shout to the Lord, all you lands! Serve the Lord with gladness...
(6) Psalm 100 (101): I will sing of mercy and justice; to You, O Lord, I will sing praises...
(7) Psalm 109 (110): The Lord said to my Lord, 'Sit at My right hand, till I make Your enemies Your footstool'...
(8) Psalm 110 (111): Praise the Lord! I will praise the Lord with my whole heart, in the assembly of the upright... Alleluia."""
            )
        )

        // المجموعة الثالثة: المزامير ١١١، ١١٢، ١١٤، ١١٥
        list.add(
            PrayerSectionItem(
                id = "none_psalms_part3",
                titleAr = "مزامير الساعة التاسعة (المزامير: ١١١، ١١٢، ١١٤، ١١٥)",
                titleEn = "Psalms of the Ninth Hour (Psalms 111, 112, 114, 115)",
                subtitleAr = "المزمور المائة والحادي عشر، الثاني عشر، الرابع عشر، والخامس عشر",
                subtitleEn = "Psalms 111, 112, 114, and 115",
                contentAr = """(٩) المزمور المائة والحادي عشر:
طوبى للرجل الخائف الرب، ويهوى وصاياه جداً. يقوى نسله في الأرض، جيل المستقيمين يبارك. مجد وغنى في بيته، وبره يدوم إلى أبد الأبد. أشرق في الظلمة نور للمستقيمين، رحيم ورؤوف وبار. صالح هو الرجل الذي يترأف ويقرض، ويدبر أموره بالعدل، لأنه لا يتزعزع إلى الدهر. الصديق يكون لذكر أبدي، من خبر السوء لا يخاف. قلبه مستعد متكل على الرب، قلبه ثابت لا يتزعزع حتى يرى بأعدائه. فرق وأعطى المساكين، بره قائم إلى أبد الأبد، يرتفع قرنه بالمجد. الخاطئ يرى ويغضب، يصر بأسنانه ويذوب، شهوة الخطاة تبيد. هللويا.

(١٠) المزمور المائة والثاني عشر:
سبحوا الرب أيها الفتيان، سبحوا اسم الرب. ليكن اسم الرب مباركاً من الآن وإلى الأبد. من مشارق الشمس إلى مغاربها اسم الرب مسبح. الرب عالٍ على كل الأمم، فوق السموات مجده. من مثل الرب إلهنا الساكن في الأعالي، الناظر إلى المتواضعات في السماء وعلى الأرض؟! المقيم المسكين من الأرض، والرافع البائس من المزبلة، لكي يجلسه مع رؤساء شعبه، الذي يجعل العاقر ساكنة في بيت أم أولاد فرحانة. هللويا.

(١١) المزمور المائة والرابع عشر:
أحببت أن يسمع الرب صوت تضرعي، لأنه أمال أذنه إليّ فأدعوه في أيامي. لأن أوجاع الموت أحاطت بي، وشدائد الجحيم أصابتني، ضيقاً وحزناً وجدت. وباسم الرب دعوت: يا رب نجِ نفسي. رحيم هو الرب وصديق، وإلهنا يرحم. الذي يحفظ الأطفال هو الرب، اتضعت فخلصني. ارجعي يا نفسي إلى موضع راحتك، لأن الرب قد أحسن إليك. لأنه أنقذ نفسي من الموت، وعيني من الدموع، ورجلي من الزلل. أرضي الرب في كورة الأحياء. هللويا.

(١٢) المزمور المائة والخامس عشر:
آمنت لذلك تكلمت، وأنا اتضعت جداً. أنا قلت في حيرتي: إن كل الناس كاذبون. بماذا أكافئ الرب عن كل ما أعطانيه؟! كأس الخلاص آخذ، وباسم الرب أدعو. أوفي نذوري للرب قدام كل شعبه. كريم في عيني الرب موت قديسيه. يا رب أنا عبدك، أنا عبدك وابن أمتك، قطعت قيودي. فلك أذبح ذبيحة التسبيح، وباسم الرب أدعو. أوفي نذوري للرب قدام كل شعبه، في ديار بيت الرب، في وسطك يا أورشليم. هللويا.""",
                contentEn = """(9) Psalm 111 (112): Praise the Lord! Blessed is the man who fears the Lord, who delights greatly in His commandments...
(10) Psalm 112 (113): Praise the Lord! Praise, O servants of the Lord, praise the name of the Lord!...
(11) Psalm 114 (116:1-9): I love the Lord, because He has heard my voice and my supplications...
(12) Psalm 115 (116:10-19): I believed, therefore I spoke, 'I am greatly afflicted'... Alleluia."""
            )
        )

        // 6. الإنجيل المقدس
        list.add(
            PrayerSectionItem(
                id = "none_gospel",
                titleAr = "الإنجيل المقدس للساعة التاسعة (لوقا ٩: ١٠-١٧)",
                titleEn = "The Holy Gospel for the Ninth Hour (Luke 9:10-17)",
                subtitleAr = "من إنجيل معلمنا لوقا البشير (معجزة إشباع الجموع الخمسة آلاف)",
                subtitleEn = "According to Saint Luke (Feeding of the 5000)",
                contentAr = """ولما رجع الرسل أخبروه بجميع ما فعلوا، فأخذهم وانفرد بهم في موضع خلاء لمدينة تسمى بيت صيدا. فالجموع إذ علموا تبعوه، فقبلهم وكلمهم عن ملكوت الله، والمحتاجون إلى الشفاء شفاهم.
وابتدأ النهار يميل، فتقدم إليه الاثنا عشر وقالوا له: اصرف الجمع ليمضوا إلى القرى والضياع حوالينا فيبيتوا ويجدوا طعاماً، لأننا ههنا في موضع خلاء. فقال لهم: أعطوهم أنتم ليأكلوا.
فقالوا: ليس عندنا أكثر من خمسة أرغفة وسمكتين، إلا أن نمضي ونشتري طعاماً لهذا الشعب كله، لأنهم كانوا نحو خمسة آلاف رجل.
فقال لتلاميذه: اتكئوهم فرقاً خمسين خمسين، ففعلوا هكذا وأتكأوا الجميع.
فأخذ الخمسة الأرغفة والسمكتين، ونظر إلى السماء وباركها وكسر، وأعطى التلاميذ ليقدموا للجمع. فأكلوا وشبعوا كلهم، ثم رُفِعَ ما فضل عنهم من الكِسَر اثنتا عشرة قُفّة.
والمجد لله دائماً أبدياً. آمين.""",
                contentEn = """And the apostles, when they had returned, told Him all that they had done. Then He took them and went aside privately into a deserted place belonging to the city called Bethsaida... And He said to His disciples, 'Make them sit down in groups of fifty.' And they did so, and made them all sit down. Then He took the five loaves and the two fish, and looking up to heaven, He blessed and broke them, and gave them to the disciples to set before the multitude... Glory to God forever. Amen.""",
                isGospel = true
            )
        )

        // 7. القطع
        list.add(
            PrayerSectionItem(
                id = "none_litanies",
                titleAr = "قطع الساعة التاسعة (موت الفادي وتوبة اللص اليمين)",
                titleEn = "The Litanies of the Ninth Hour (Death of the Savior & the Right-Hand Thief)",
                rubricAr = "ابتهالات الساعة التاسعة في ذكرى الموت المحيي على عود الصليب:",
                rubricEn = "Supplications commemorating Christ's life-giving death and the penitent thief:",
                contentAr = """يا من ذاق الموت بالجسد في وقت الساعة التاسعة من أجلنا نحن الخطاة، أمت حواسنا الجسمانية أيها المسيح إلهنا ونجنا.
فلتدنُ وسيلتي قدامك يا رب كقولك فهمني، لتدخل طلبتي إلى حضرتك ككلمتك أحيني.
ذوكصابتري كيه إيو كي آجيو ابنيفماتي (المجد للآب والابن والروح القدس).

عندما أبصر اللص رئيس الحياة معلقاً على الصليب قال: لولا أن الذي صُلب معنا هو إله متجسد، ما كانت الشمس أخفت شعاعها، ولا الأرض تزلزلت وارتعدت! بل أيها القادر على كل شيء، الصابر على هذا كله، اذكرني يا رب متى جئت في ملكوتك.
كي نين، كي آ إي، كي ايستوس إي أوناس تون إي أونون آمين (الآن وكل أوان وإلى دهر الداهرين، آمين).

يا من ولدتِ البار من أجل خلاصنا، لما نظرتِ الحمل والراعي ومخلص العالم على الصليب معلقاً، قلتِ وأنتِ باكية: أما العالم فيفرح بقبوله الخلاص، وأما أحشائي فتلتهب عند نظري إلى صلبك الذي أنت صابر عليه من أجل الكل، يا ابني وإلهي.
الآن وكل أوان وإلى دهر الدهور كلها. آمين.""",
                contentEn = """O You, Who tasted death in the flesh in the ninth hour for our sake, O Christ our God, mortify our carnal senses and deliver us... (Glory to the Father and to the Son and to the Holy Spirit)... When the thief saw the Prince of Life hanging on the Cross, he said: 'If He Who is crucified with us were not God incarnate, the sun would not have hidden its rays, nor would the earth have quaked and trembled. But O You, the Almighty, remember me, O Lord, when You come into Your kingdom'... (Now and forever and unto the ages of ages. Amen)... When the mother saw the Lamb and Shepherd and Savior of the world hanging on the Cross, she wept and said: 'The world rejoices in receiving salvation, but my heart burns as I look upon Your crucifixion...' Amen."""
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

        // 11. تحليل صلاة الساعة التاسعة
        list.add(
            PrayerSectionItem(
                id = "none_absolution",
                titleAr = "تحليل صلاة الساعة التاسعة",
                titleEn = "The Absolution of the Ninth Hour",
                rubricAr = "صلاة التوبة والتحليل لمغفرة الخطايا:",
                rubricEn = "The prayer of absolution asking for forgiveness and eternal life:",
                contentAr = """يا الله الآب، أبو ربنا وإلهنا ومخلصنا يسوع المسيح، الذي بآلامه المخلصة حطم قوى العدو، وبموته المحيي أباد سلطان الموت وفتح الفردوس للص اليمين.

نسألك ونطلب من صلاحك يا محب البشر، أن تقبل تضرعاتنا في هذه الساعة التاسعة المقدسة، وتغفر لنا خطايانا وزلاتنا التي صنعناها بمعرفة وبغير معرفة.
امت حواسنا الجسدية ونقِّ أفكارنا، واجعلنا شركاء في ميراث قديسيك، لنحيا في بر وتقوى كل أيام حياتنا، ونشكرك ونمجدك إلى الأبد.
بالمسيح يسوع ربنا، هذا الذي من قبله يليق بك معه ومع الروح القدس المحيي المساوي لك، المجد والإكرام والعزة والسجود، الآن وكل أوان وإلى دهر الدهور كلها. آمين.""",
                contentEn = """O God the Father, Father of our Lord, God, and Savior Jesus Christ, Who by His saving passion crushed the power of the enemy, and by His life-giving death abolished the authority of death and opened Paradise to the right-hand thief. We ask and entreat Your goodness, O Lover of mankind, accept our supplications in this holy ninth hour, forgive us our sins and trespasses, mortify our carnal desires, and make us partakers in the inheritance of Your saints... Amen."""
            )
        )

        // 12. طلبة الختام لكل ساعة
        list.add(AgpeyaCommonPrayers.getFinalConcludingPrayer(lang))

        return list
    }
}
