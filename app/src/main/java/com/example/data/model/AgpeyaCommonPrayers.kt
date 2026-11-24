package com.example.data.model

import com.example.localization.AppLanguage

/**
 * Common liturgical prayers present in every canonical hour of the Agpeya,
 * updated verbatim to match the user's verified church edition:
 * - مقدمة كل ساعة والصلاة الربانية
 * - صلاة الشكر كاملة
 * - المزمور الخمسون كاملاً (من AgpeyaPsalmsRegistry)
 * - الثلاث تقديسات (أجيوس)
 * - السلام لك (التيوتوكية)
 * - بدء قانون الإيمان وقانون الإيمان المقدس الأرثوذكسي
 * - كيرياليسون ٤١ مرة مع قدوس قدوس قدوس
 * - طلبة تصلى آخر كل ساعة والصلاة الربانية
 */
object AgpeyaCommonPrayers {

    fun getIntroduction(lang: AppLanguage): PrayerSectionItem = PrayerSectionItem(
        id = "intro",
        titleAr = "مقدمة كل ساعة والصلاة الربانية",
        titleEn = "Introduction & The Lord's Prayer",
        rubricAr = "يقول المصلي راشماً ذاته بعلامة الصليب المقدس:",
        rubricEn = "The worshipper signs themselves with the sign of the Cross:",
        contentAr = """باسم الآب والابن والروح القدس الإله الواحد آمين.
يا رب ارحم. يا رب ارحم. يا رب بارك. آمين.
المجد للآب والابن والروح القدس الآن وكل أوان وإلى دهر الدهور آمين.

الصلاة الربانية
اللهم اجعلنا مستحقين أن نقول بشكر:
أبانا الذي في السموات. ليتقدس اسمك. ليأت ملكوتك. لتكن مشيئتك. كما في السماء كذلك على الأرض. خبزنا الذي للغد أعطنا اليوم. وأغفر لنا ذنوبنا كما نغفر نحن أيضا للمذنبين إلينا. ولا تدخلنا في تجربة. لكن نجنا من الشرير. بالمسيح يسوع ربنا لأن لك الملك والقوة والمجد إلى الأبد. آمين.""",
        contentEn = """In the name of the Father, and of the Son, and of the Holy Spirit, one God. Amen.
Lord have mercy, Lord have mercy, Lord bless. Amen.
Glory to the Father, and to the Son, and to the Holy Spirit, now and forever and unto the ages of ages. Amen.

Make us worthy to pray thankfully:
Our Father Who art in heaven; hallowed be Thy name. Thy kingdom come. Thy will be done on earth as it is in heaven. Give us this day our daily bread; and forgive us our trespasses, as we forgive those who trespass against us; and lead us not into temptation, but deliver us from evil. In Christ Jesus our Lord. For Thine is the kingdom, the power, and the glory, forever. Amen."""
    )

    fun getThanksgivingPrayer(): PrayerSectionItem = PrayerSectionItem(
        id = "thanksgiving",
        titleAr = "صلاة الشكر",
        titleEn = "The Complete Prayer of Thanksgiving",
        rubricAr = "صلاة الشكر لصانع الخيرات التي تفتتح بها جميع صلوات الكنيسة القبطية الأرثوذكسية:",
        rubricEn = "The introductory prayer said at the beginning of all Coptic Orthodox prayers:",
        contentAr = """فلنشكر صانع الخيرات الرحوم الله، أبا ربنا وإلهنا ومخلصنا يسوع المسيح، لأنه سترنا وأعاننا، وحفظنا، وقبلنا إليه وأشفق علينا وعضدنا، وأتى بنا إلى هذه الساعة. هو أيضا فلنسأله أن يحفظنا في هذا اليوم المقدس وكل أيام حياتنا بكل سلام. الضابط الكل الرب إلهنا.

أيها السيد الإله ضابط الكل أبو ربنا وإلهنا ومخلصنا يسوع المسيح، نشكرك على كل حال ومن أجل كل حال، وفى كل حال، لأنك سترتنا، وأعنتنا، وحفظتنا، وقبلتنا إليك، وأشفقت علينا، وعضدتنا، وأتيت بنا إلى هذه الساعة. من أجل هذا نسأل ونطلب من صلاحك يا محب البشر، امنحنا أن نكمل هذا اليوم المقدس وكل أيام حياتنا بكل سلام مع خوفك. كل حسد، وكل تجربة وكل فعل الشيطان ومؤامرة الناس الأشرار، وقيام الأعداء الخفيين والظاهريين، انزعها عنا وعن سائر شعبك، وعن موضعك المقدس هذا. أما الصالحات والنافعات فارزقنا إياها. لأنك أنت الذي أعطيتنا السلطان أن ندوس الحيات والعقارب وكل قوة العدو. ولا تدخلنا في تجربة، لكن نجنا من الشرير. بالنعمة والرأفات ومحبة البشر اللواتي لابنك الوحيد ربنا وإلهنا ومخلصنا يسوع المسيح. هذا الذي من قبله المجد والإكرام والعزة والسجود تليق بك معه مع الروح القدس المحيي المساوي لك الآن وكل أوان وإلى دهر الدهور آمين.""",
        contentEn = """Let us give thanks to the beneficent and merciful God, the Father of our Lord, God and Savior Jesus Christ, for He has covered us, helped us, guarded us, accepted us unto Him, spared us, supported us, and brought us unto this hour.

Let us also ask Him, the Lord our God, the Pantocrator, to guard us in all peace this holy day and all the days of our life.

O Master, Lord, God Almighty, the Father of our Lord, God and Savior Jesus Christ, we thank You upon every occasion, in every condition, and for all things; for You have covered us, helped us, guarded us, accepted us unto You, spared us, supported us, and brought us unto this hour.

Therefore, we ask and entreat Your goodness, O Lover of mankind, grant us to complete this holy day, and all the days of our life, in all peace with Your fear.
All envy, all temptation, all the work of Satan, the counsel of wicked men, and the rising up of enemies, hidden and manifest, take them away from us, and from all Your people, and from this holy place of Yours.

But those things which are good and profitable do provide for us; for You are He Who gave us the authority to trample on serpents and scorpions, and upon all the power of the enemy.
And lead us not into temptation, but deliver us from evil.
Through the grace, compassion and love of mankind, of Your only-begotten Son, our Lord, God and Savior Jesus Christ.
Through Whom the glory, the honor, the dominion, and the adoration are due unto You, with Him and the Holy Spirit, the Life-Giver, Who is of one essence with You, now and at all times, and unto the age of all ages. Amen."""
    )

    fun getPsalm50(): PrayerSectionItem = PrayerSectionItem(
        id = "psalm50",
        titleAr = "المزمور ٥٠",
        titleEn = "Psalm 50 (51 LXX) - Complete Penitential Psalm",
        subtitleAr = "مزمور لداود النبي عندما جاءه ناثان النبي",
        subtitleEn = "A Psalm of David when Nathan the prophet came to him",
        contentAr = AgpeyaPsalmsRegistry.requireArabicPsalm(50),
        contentEn = """Have mercy upon me, O God, according to Your great mercy; and according to the multitude of Your tender mercies blot out my transgression.
Wash me thoroughly from my iniquity, and cleanse me from my sin.
For I acknowledge my transgression, and my sin is ever before me.
Against You only have I sinned, and done this evil in Your sight, that You may be found just when You speak, and blameless when You judge.
For behold, I was brought forth in iniquity, and in sin my mother conceived me.
For behold, You desire truth in the inward parts, and in the hidden part You make me to know wisdom.
Purge me with hyssop, and I shall be clean; wash me, and I shall be whiter than snow.
Make me to hear joy and gladness, that the bones which You have broken may rejoice.
Hide Your face from my sins, and blot out all my iniquities.
Create in me a clean heart, O God, and renew a steadfast spirit within me.
Do not cast me away from Your presence, and do not take Your Holy Spirit from me.
Restore to me the joy of Your salvation, and uphold me by Your generous Spirit.
Then I will teach transgressors Your ways, and sinners shall be converted to You.
Deliver me from bloodguiltiness, O God, the God of my salvation, and my tongue shall sing aloud of Your righteousness.
O Lord, open my lips, and my mouth shall show forth Your praise.
For You do not desire sacrifice, or else I would give it; You do not delight in burnt offering.
The sacrifices of God are a broken spirit, a broken and a contrite heart—these, O God, You will not despise.
Do good in Your good pleasure to Zion; build the walls of Jerusalem.
Then You shall be pleased with the sacrifices of righteousness, with burnt offering and whole burnt offering; then they shall offer bulls on Your altar. Alleluia."""
    )

    fun getTrisagion(): PrayerSectionItem = PrayerSectionItem(
        id = "trisagion",
        titleAr = "الثلاث تقديسات",
        titleEn = "The Trisagion (Agios)",
        copticIntro = "Ⲁ̀ⲅⲓⲟⲥ ⲟ̀ Ⲑⲉⲟⲥ",
        contentAr = """قدوس الله، قدوس القوى، قدوس الحي الذي لا يموت، الذي ولد من العذراء، ارحمنا.
قدوس الله، قدوس القوى، قدوس الحي الذي لا يموت، الذي صلب عنا، ارحمنا.
قدوس الله، قدوس القوى، قدوس الحي الذي لا يموت، الذي قام من الأموات وصعد إلى السموات، ارحمنا.

المجد للآب والابن والروح القدس، الآن وكل أوان وإلى دهر الدهور. أمين.
أيها الثالوث القدوس ارحمنا. أيها الثالوث القدوس ارحمنا. أيها الثالوث القدوس ارحمنا.
يا رب اغفر لنا خطايانا. يا رب اغفر لنا آثامنا. يا رب اغفر لنا زلاتنا.
يا رب افتقد مرضى شعبك، اشفهم من أجل اسمك القدوس.
آباؤنا وإخوتنا الذين رقدوا، يا رب نيح نفوسهم.
يا من هو بلا خطية، يا رب ارحمنا. يا من بلا خطية، يا رب أعنا، واقبل طلباتنا إليك.
لأن لك المجد والعزة والتقديس المثلث.
يا رب ارحم. يا رب ارحم يا رب بارك. أمين.

واجعلنا مستحقين أن نقول بشكر: أبانا الذي في السموات ....""",
        contentEn = """Holy God, Holy Mighty, Holy Immortal, Who was born of the Virgin, have mercy on us.
Holy God, Holy Mighty, Holy Immortal, Who was crucified for us, have mercy on us.
Holy God, Holy Mighty, Holy Immortal, Who rose from the dead and ascended into the heavens, have mercy on us.

Glory to the Father, and to the Son, and to the Holy Spirit, now and forever and unto the ages of ages. Amen.
O Holy Trinity, have mercy on us. O Holy Trinity, have mercy on us. O Holy Trinity, have mercy on us.
O Lord, forgive us our sins. O Lord, forgive us our iniquities. O Lord, forgive us our trespasses.
O Lord, visit the sick of Your people, heal them for the sake of Your holy name.
Our fathers and brethren who have fallen asleep, O Lord, repose their souls.
O You Who are without sin, Lord have mercy upon us. O You Who are without sin, Lord help us, and accept our supplications.
For Thine is the glory, the dominion, and the thrice-holy adoration.
Lord have mercy, Lord have mercy, Lord bless. Amen.

Make us worthy to pray thankfully:
Our Father Who art in heaven..."""
    )

    fun getHailToYou(): PrayerSectionItem = PrayerSectionItem(
        id = "hail_to_you",
        titleAr = "السلام لك",
        titleEn = "Hail to You (Prayer to St. Mary)",
        contentAr = """السلام لك. نسألك أيتها القديسة الممتلئة مجدا العذراء كل حين، والدة الإله أم المسيح، أصعدي صلواتنا إلى ابنك الحبيب ليغفر لنا خطايانا.
السلام للتي ولدت لنا النور الحقيقي المسيح إلهنا، العذراء القديسة، اسألي الرب عنا، ليصنع رحمة مع نفوسنا، ويغفر لنا خطايانا.
أيتها العذراء مريم والدة الإله، القديسة الشفيعة الأمينة لجنس البشرية، اشفعي فينا أمام المسيح الذي ولدته لكي ينعم علينا بغفران خطايانا.
السلام لك أيتها العذراء الملكة الحقيقية، السلام لفخر جنسنا، ولدت لنا عمانوئيل.
نسألك: اذكرينا، أيتها الشفيعة المؤتمنة، أمام ربنا يسوع المسيح، ليغفر لنا خطايانا.""",
        contentEn = """Hail to you. We ask you, O Saint, full of glory, the ever-virgin, the Mother of God, the Mother of Christ, lift up our prayers unto your beloved Son, that He may forgive us our sins.
Hail to the Holy Virgin, who has brought forth unto us the True Light, Christ our God. Ask the Lord on our behalf, to have mercy on our souls, and to forgive us our sins.
O Virgin Mary, Mother of God, the holy and faithful advocate of the human race, intercede on our behalf before Christ Whom you bore, that He may grant us the forgiveness of our sins.
Hail to you, O Virgin, the right and true Queen; hail to the pride of our race, who bore to us Emmanuel.
We ask you to remember us, O our faithful advocate, before our Lord Jesus Christ, that He may forgive us our sins."""
    )

    fun getCreed(): PrayerSectionItem = PrayerSectionItem(
        id = "orthodox_creed",
        titleAr = "بدء قانون الإيمان وقانون الإيمان المقدس الأرثوذكسي",
        titleEn = "Introduction to the Creed & The Orthodox Creed",
        contentAr = """بدء قانون الإيمان
نعظمك يا أم النور الحقيقي، ونمجدك أيتها العذراء القديسة، والدة الإله، لأنك ولدت لنا مخلص العالم، أتى وخلص نفوسنا.
المجد لكَ يا سيدنا وملكنا المسيح، فخر الرسل، إكليل الشهداء تهليل الصديقين، ثبات الكنائس، غفران الخطايا.
نبشر بالثالوث القدوس، لاهوت واحد، نسجد له ونمجده.
يا رب ارحم. يا رب ارحم. يا رب بارك. أمين.

قانون الإيمان المقدس الأرثوذكسي
بالحقيقة نؤمن بإله واحد، الله الآب، ضابط الكل، خالق السماء والأرض، ما يُرَى وما لا يرى.
نؤمن برب واحد يسوع المسيح، ابن الله الوحيد، المولود من الآب قبل كل الدهور، نور من نور، إله حق من إله حق، مولود غير مخلوق، مساو للآب في الجوهر، الذي به كان كل شيء.
هذا الذي من أجلنا نحن البشر، ومن أجل خلاصنا، نزل من السماء، وتجسد من الروح القدس ومن مريم العذراء، وتأنس.
وصلب عنا على عهد بيلاطس البنطي. وتألم وقبر وقام من بين الأموات في اليوم الثالث كما في الكتب، وصعد إلى السموات، وجلس عن يمين أبيه، وأيضا يأتي في مجده ليدين الأحياء والأموات، الذي ليس لملكه انقضاء.
نعم نؤمن بالروح القدس، الرب المحيى المنبثق من الآب. نسجد له ونمجده مع الآب والابن، الناطق في الأنبياء.
وبكنيسة واحدة مقدسة جامعة رسولية.
ونعترف بمعمودية واحدة لمغفرة الخطايا.
وننتظر قيامة الأموات وحياة الدهر الآتي. أمين.""",
        contentEn = """We exalt you, the Mother of the True Light. We glorify you, O Saint, the Mother of God, for you brought forth unto us the Savior of the whole world; He came and saved our souls.
Glory to You, our Master and King, Christ, the pride of the Apostles, the crown of the Martyrs, the joy of the righteous, the firmness of the churches, the forgiveness of sins.
We proclaim the Holy Trinity in One Godhead. We worship Him. We glorify Him.
Lord have mercy. Lord have mercy. Lord bless. Amen.

Truly we believe in one God, God the Father, the Pantocrator, Creator of heaven and earth, and of all things seen and unseen.
We believe in one Lord Jesus Christ, the Only-Begotten Son of God, begotten of the Father before all ages; Light of Light, true God of true God, begotten not created, of one essence with the Father, by Whom all things were made;
Who for us men and for our salvation came down from heaven, and was incarnate of the Holy Spirit and of the Virgin Mary, and became Man.
And He was crucified for us under Pontius Pilate, suffered and was buried. And on the third day He rose from the dead, according to the Scriptures, ascended to the heavens; He sits at the right hand of His Father, and He is coming again in His glory to judge the living and the dead, Whose kingdom shall have no end.
Yes, we believe in the Holy Spirit, the Lord, the Life-Giver, Who proceeds from the Father, Who with the Father and the Son is worshipped and glorified, Who spoke by the prophets.
And in one, holy, catholic, and apostolic church.
We confess one baptism for the remission of sins.
We look for the resurrection of the dead, and the life of the coming age. Amen."""
    )

    fun getKyrieEleisonAndConcludingSupplication(): PrayerSectionItem = PrayerSectionItem(
        id = "kyrie_41",
        titleAr = "كيرياليسون ٤١ مرة وقدوس قدوس قدوس",
        titleEn = "Lord Have Mercy (41 times) & Holy, Holy, Holy",
        rubricAr = "يطلب المصلي مراحم الرب ٤١ مرة تذكاراً لآلام المسيح وجراحاته:",
        rubricEn = "The worshipper asks for God's mercy 41 times commemorating Christ's 39 scourges, the crown of thorns, and the spear:",
        contentAr = """كيرياليسون Kuri`e `ele`hcon (يا رب ارحم) 41 مرة

قدوس قدوس قدوس
قدوس، قدوس، قدوس، رب الصباؤوت. السماء والأرض مملوءتان من مجدك وكرامتك.
ارحمنا يا الله الآب ضابط الكل. أيها الثالوث القدوس ارحمنا.
أيها الرب إله القوات كن معنا، لأنه ليس لنا معين في شدائدنا وضيقاتنا سواك.

حل واغفر واصفح لنا يا الله عن سيئاتنا، التي صنعناها بإرادتنا والتي صنعناها بغير إرادتنا، التي فعلناها بمعرفة والتي فعلناها بغير معرفة، الخفية والظاهرة.
يا رب اغفرها لنا، من أجل اسمك القدوس الذي دعي علينا. كرحمتك يا رب وليس كخطايانا.

واجعلنا مستحقين أن نقول بشكر: أبانا الذي في السموات ..""",
        contentEn = """Lord have mercy (Kyrie Eleison) 41 times...

Holy, Holy, Holy, Lord of Sabaoth; heaven and earth are full of Your glory and honor.
Have mercy on us, O God the Father Almighty. O Holy Trinity, have mercy on us.
O Lord God of hosts, be with us, for we have no helper in our tribulations and afflictions but You.

Absolve, forgive, and remit, O God, our transgressions; those which we have committed willingly and those we have committed unwillingly, those committed knowingly and those committed unknowingly, the hidden and the manifest, O Lord forgive us, for the sake of Your holy name which is called upon us, according to Your mercy, O Lord, and not according to our sins.

And make us worthy to pray thankfully: Our Father Who art in heaven..."""
    )

    fun getFinalConcludingPrayer(lang: AppLanguage): PrayerSectionItem = PrayerSectionItem(
        id = "conclusion_all_hours",
        titleAr = "طلبة تصلى آخر كل ساعة",
        titleEn = "The Concluding Supplication of Every Hour",
        rubricAr = "تقال في خاتمة كل صلاة من صلوات السواعي:",
        rubricEn = "Recited at the conclusion of every Canonical Hour:",
        contentAr = """ارحمنا يا الله ثم ارحمنا. يا من في كل وقت وكل ساعة، في السماء وعلى الأرض، مسجود له وممجد. المسيح إلهنا الصالح، الطويل الروح، الكثير الرحمة، الجزيل التحنن، الذي يحب الصديقين ويرحم الخطاة الذين أولهم أنا. الذي لا يشاء موت الخاطئ مثل ما يرجع ويحيا. الداعي الكل إلى الخلاص لأجل الموعد بالخيرات المنتظرة.

يا رب اقبل منا في هذه الساعة وكل ساعة طلباتنا. سهل حياتنا، وأرشدنا إلى العمل بوصاياك. قدس أرواحنا. طهر أجسامنا. قوم أفكارنا. نق نياتنا. اشف أمراضنا واغفر خطايانا. ونجنا من كل حزن رديء ووجع قلب. أحطنا بملائكتك القديسين، لكي نكون بمعسكرهم محفوظين ومرشدين، لنصل إلى اتحاد الإيمان وإلى معرفة مجدك غير المحسوس وغير المحدود، فإنك مبارك إلى الأبد. آمين.

اللهم اجعلنا مستحقين أن نقول بشكر:
أبانا الذي في السموات. ليتقدس اسمك. ليأت ملكوتك. لتكن مشيئتك. كما في السماء كذلك على الأرض. خبزنا الذي للغد أعطنا اليوم. وأغفر لنا ذنوبنا كما نغفر نحن أيضا للمذنبين إلينا. ولا تدخلنا في تجربة. لكن نجنا من الشرير. بالمسيح يسوع ربنا لأن لك الملك والقوة والمجد إلى الأبد. آمين.""",
        contentEn = """Have mercy on us, O God, and have mercy on us, Who at all times and at every hour, in heaven and on earth, is worshipped and glorified, Christ our God, the good, the long-suffering, the abundant in mercy, and the great in compassion, Who loves the righteous and has mercy on the sinners, of whom I am chief; Who does not desire the death of the sinner, but rather that he return and live; Who calls all to salvation for the promise of the blessings to come.

Lord, receive from us our prayers at this hour and at every hour. Ease our life, and guide us to act according to Your commandments. Sanctify our souls, purify our bodies, set right our thoughts, cleanse our intentions, heal our sicknesses, forgive our sins, and deliver us from all evil, grief, and distress.

Surround us by Your holy angels, that guarded and guided by their camp, we may attain to the unity of the faith and to the knowledge of Your unapproachable and infinite glory; for You are blessed forever. Amen.

Make us worthy to pray thankfully:
Our Father Who art in heaven; hallowed be Thy name. Thy kingdom come. Thy will be done on earth as it is in heaven. Give us this day our daily bread; and forgive us our trespasses, as we forgive those who trespass against us; and lead us not into temptation, but deliver us from evil. In Christ Jesus our Lord. For Thine is the kingdom, the power, and the glory, forever. Amen."""
    )
}
