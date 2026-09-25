package com.example.data.model

import com.example.localization.AppLanguage

object AgpeyaPrayerContent {

    fun getThanksgivingPrayer(lang: AppLanguage): String = when (lang) {
        AppLanguage.ARABIC, AppLanguage.SYRIAN_ARABIC, AppLanguage.SYRIAC -> """
صلاة الشكر:
فلنشكر صانع الخيرات الرحوم الله، أبا ربنا وإلهنا ومخلصنا يسوع المسيح، لأنه سترنا وأعاننا، وحفظنا، وقبلنا إليه وأشفق علينا وعضدنا، وأتى بنا إلى هذه الساعة.
هو أيضاً فلنسأله أن يحفظنا في هذا اليوم المقدس وكل أيام حياتنا بكل سلام، الضابط الكل الرب إلهنا.
أيها السيد الإله ضابط الكل، أبو ربنا وإلهنا ومخلصنا يسوع المسيح، نشكرك على كل حال ومن أجل كل حال وفي كل حال...
        """.trimIndent()
        AppLanguage.COPTIC -> """
Ⲡⲓⲉⲩⲭⲁⲣⲓⲥⲧⲓⲁ:
Ⲙⲁⲣⲉⲛϣⲉⲡϩⲙⲟⲧ ⲛ̀ⲧⲟⲧϥ ⲙ̀Ⲡⲓⲣⲉϥⲉⲣⲡⲉⲑⲛⲁⲛⲉϥ Ⲟⲩⲟϩ ⲛ̀Ⲛⲁⲏⲧ Ⲫⲛⲟⲩϯ: Ⲫⲓⲱⲧ ⲙ̀Ⲡⲉⲛϭⲟⲓⲥ Ⲟⲩⲟϩ Ⲡⲉⲛⲛⲟⲩϯ Ⲟⲩⲟϩ Ⲡⲉⲛⲥⲱⲧⲏⲣ Ⲓⲏⲥⲟⲩⲥ Ⲡⲓⲭⲣⲓⲥⲧⲟⲥ: ϫⲉ ⲁϥⲉⲣⲥⲕⲉⲡⲁⲍⲓⲛ ⲉ̀ϫⲱⲛ ⲁϥⲉⲣⲃⲟⲏⲑⲓⲛ ⲉ̀ⲣⲟⲛ: ⲁϥⲁⲣⲉϩ ⲉ̀ⲣⲟⲛ ⲁϥϣⲟⲡⲧⲉⲛ ⲉ̀ⲣⲟϥ ⲁϥϯⲁⲥⲟ ⲉ̀ⲣⲟⲛ ⲁϥϯⲧⲟⲧⲉⲛ: ⲁϥⲉⲛⲧⲉⲛ ϣⲁ ⲉ̀ϧⲟⲩⲛ ⲉ̀ⲧⲁⲓⲁϫⲡ ⲑⲁⲓ...
        """.trimIndent()
        AppLanguage.ENGLISH -> """
The Prayer of Thanksgiving:
Let us give thanks to the beneficent and merciful God, the Father of our Lord, God, and Savior Jesus Christ, for He has covered us, helped us, guarded us, accepted us unto Him, spared us, supported us, and brought us to this hour.
Let us also ask Him, the Lord our God, the Pantocrator, to guard us in all peace this holy day and all the days of our life.
O Master, Lord, God Almighty, the Father of our Lord, God, and Savior Jesus Christ, we thank You on every occasion, in every condition, and for all things...
        """.trimIndent()
        AppLanguage.FRENCH -> """
Prière d'action de grâce :
Rendons grâces au Dieu bienfaisant et miséricordieux, le Père de notre Seigneur, Dieu et Sauveur Jésus-Christ, car Il nous a protégés, secourus, gardés, accueillis, épargnés, soutenus et amenés jusqu'à cette heure...
        """.trimIndent()
        AppLanguage.SPANISH -> """
Oración de Acción de Gracias:
Demos gracias al Dios bienhechor y misericordioso, Padre de nuestro Señor, Dios y Salvador Jesucristo, porque nos ha cubierto, ayudado, guardado, aceptado, perdonado, sostenido y conducido hasta esta hora...
        """.trimIndent()
        AppLanguage.GERMAN, AppLanguage.AUSTRIAN_GERMAN, AppLanguage.SWISS_GERMAN -> """
Dankgebet:
Lasst uns dem gütigen und barmherzigen Gott danken, dem Vater unseres Herrn, Gottes und Erlösers Jesus Christus, denn Er hat uns beschirmt, geholfen, bewahrt, aufgenommen, verschont, gestützt und uns bis zu dieser Stunde geführt...
        """.trimIndent()
        AppLanguage.ITALIAN -> """
Preghiera di Ringraziamento:
Rendiamo grazie al Dio benefico e misericordioso, Padre del nostro Signore, Dio e Salvatore Gesù Cristo, poiché ci ha protetti, aiutati, custoditi, accolti, risparmiati, sostenuti e condotti fino a quest'ora...
        """.trimIndent()
        AppLanguage.CHINESE -> """
感恩祷文：
让我们感谢广行善事、满怀怜悯的上帝，我们主、上帝和救主耶稣基督的父。因为祂庇护了我们，扶助了我们，保守了我们，接纳了我们，垂怜了我们，坚固了我们，并引领我们到达此时此刻。
让我们也祈求全能者主我们的上帝，在这个神圣的日子以及我们一生的岁月中，赐我们平安与护佑。
主啊，全能的上帝，我们主、上帝和救主耶稣基督的父，我们在一切境遇中、因着一切事、并在一切事上感谢祢...
        """.trimIndent()
        AppLanguage.JAPANESE -> """
感謝の祈り：
善を行い、憐れみ深き神、私たちの主、神、救い主イエス・キリストの父に感謝を捧げましょう。神は私たちを覆い、助け、守り、御もとに受け入れ、惜しみ、支え、この時まで導いてくださいました。
全能の主、私たちの神に、この聖なる日と私たちの生涯のすべての日を、あらゆる平安のうちに守ってくださるよう願い求めましょう。
全能の主なる神、私たちの主、神、救い主イエス・キリストの父よ、私たちはあらゆる状況において、あらゆることのために、感謝を捧げます...
        """.trimIndent()
        AppLanguage.KOREAN -> """
감사의 기도:
선하시고 자비로우신 하나님, 우리 주님이시요 하나님이시며 구원자이신 예수 그리스도의 아버지께 감사를 드립시다. 그분은 우리를 덮어주시고, 도와주시고, 지켜주시고, 품어주시고, 불쌍히 여기시고, 붙들어 주시며, 지금까지 이끌어 주셨습니다.
전능하신 주 우리 하나님께 이 거룩한 날과 우리 평생의 모든 날을 온전한 평강 가운데 지켜주시기를 간구합시다.
주 하나님, 전능하신 아버지, 우리 주 하나님 구원자 예수 그리스도의 아버지여, 우리는 모든 상황에서, 모든 일로 인해 주께 감사드립니다...
        """.trimIndent()
        AppLanguage.HINDI -> """
धन्यवाद की प्रार्थना:
आइए हम परोपकारी और दयालु परमेश्वर, हमारे प्रभु, परमेश्वर और उद्धारकर्ता यीशु मसीह के पिता का धन्यवाद करें, क्योंकि उन्होंने हमें आश्रय दिया, सहायता की, रक्षा की, अपने पास स्वीकार किया, दया की, संभाला और हमें इस समय तक पहुँचाया है।
हम उनसे, हमारे प्रभु परमेश्वर सर्वशक्तिमान से यह भी विनती करते हैं कि वह इस पवित्र दिन और हमारे जीवन के सभी दिनों में पूर्ण शांति में हमारी रक्षा करें।
हे प्रभु सर्वशक्तिमान परमेश्वर, हमारे प्रभु और उद्धारकर्ता यीशु मसीह के पिता, हम हर अवसर पर, हर परिस्थिति में और हर बात के लिए आपका धन्यवाद करते हैं...
        """.trimIndent()
    }

    fun getPsalm50(lang: AppLanguage): String = when (lang) {
        AppLanguage.ARABIC, AppLanguage.SYRIAN_ARABIC, AppLanguage.SYRIAC -> """
المزمور الخمسون:
ارحمني يا الله كعظيم رحمتك، وكمثل كثرة رأفتك امحُ إثمي. اغسلني كثيراً من إثمي ومن خطيتي طهرني، لأني أنا عارف بإثمي وخطيتي أمامي في كل حين. لك وحدك أخطأت، والشر قدامك صنعت، لكي تتبرر في أقوالك وتغلب إذا حوكمت... قلباً نقياً اخلق فيّ يا الله، وروحاً مستقيماً جدده في أحشائي. لا تطرحني من قدام وجهك، وروحك القدوس لا تنزعه مني. هللويا.
        """.trimIndent()
        AppLanguage.COPTIC -> """
Ⲡⲓⲯⲁⲗⲙⲟⲥ ⲛ̅:
Ⲛⲁⲓ ⲛⲏⲓ Ⲫⲛⲟⲩϯ ⲕⲁⲧⲁ ⲡⲉⲕⲛⲓϣϯ ⲛ̀ⲛⲁⲓ: Ⲟⲩⲟϩ ⲕⲁⲧⲁ ⲡⲁϣⲁⲓ ⲛ̀ⲧⲉ ⲛⲉⲕⲙⲉⲧϣⲉⲛϩⲏⲧ ⲉⲕⲉ̀ⲥⲱⲗϫ ⲙ̀ⲡⲁⲁⲛⲟⲙⲓⲁ: Ⲙⲁⲧⲟⲩⲃⲟⲓ ⲛ̀ϩⲟⲩⲟ̀ ⲉ̀ⲃⲟⲗ ϩⲁ ⲧⲁⲁⲛⲟⲙⲓⲁ: Ⲟⲩⲟϩ ⲉ̀ⲃⲟⲗ ϩⲁ ⲡⲁⲛⲟⲃⲓ ⲙⲁⲧⲟⲩⲃⲟⲓ: Ϫⲉ ϯⲥⲱⲟⲩⲛ ⲁ̀ⲛⲟⲕ ⲛ̀ⲧⲁⲁⲛⲟⲙⲓⲁ: Ⲟⲩⲟϩ ⲡⲁⲛⲟⲃⲓ ⲙ̀ⲡⲁⲙ̀ⲑⲟ ⲉ̀ⲃⲟⲗ ⲛ̀ⲥⲏⲟⲩ ⲛⲓⲃⲉⲛ... Ⲁⲗⲗⲏⲗⲟⲩⲓⲁ.
        """.trimIndent()
        AppLanguage.ENGLISH -> """
Psalm 51 (50 LXX):
Have mercy upon me, O God, according to Your great mercy; and according to the multitude of Your tender mercies blot out my transgressions. Wash me thoroughly from my iniquity, and cleanse me from my sin. For I acknowledge my transgressions, and my sin is always before me... Create in me a clean heart, O God, and renew a steadfast spirit within me. Do not cast me away from Your presence, and do not take Your Holy Spirit from me. Alleluia.
        """.trimIndent()
        AppLanguage.FRENCH -> """
Psaume 50 :
Aie pitié de moi, ô Dieu, selon ta miséricorde ; selon ta grande compassion, efface mes transgressions ; lave-moi complètement de mon iniquité, et purifie-moi de mon péché. Car je reconnais mes transgressions, et mon péché est constamment devant moi... Ô Dieu ! crée en moi un cœur pur, et renouvelle en moi un esprit bien disposé. Alléluia.
        """.trimIndent()
        AppLanguage.SPANISH -> """
Salmo 51 (50):
Ten piedad de mí, oh Dios, conforme a tu misericordia; conforme a la multitud de tus piedades borra mis rebeliones. Lávame más y más de mi maldad, y límpiame de mi pecado... Crea en mí, oh Dios, un corazón limpio, y renueva un espíritu recto dentro de mí. No me eches de delante de ti, y no quites de mí tu Santo Espíritu. Aleluya.
        """.trimIndent()
        AppLanguage.GERMAN, AppLanguage.AUSTRIAN_GERMAN, AppLanguage.SWISS_GERMAN -> """
Psalm 51:
Gott, sei mir gnädig nach deiner Güte, tilge meine Sünden nach deiner großen Barmherzigkeit. Wasche mich rein von meiner Missetat und reinige mich von meiner Sünde; denn ich erkenne meine Missetat, und meine Sünde ist immer vor mir... Schaffe in mir, Gott, ein reines Herz und gib mir einen neuen, beständigen Geist. Halleluja.
        """.trimIndent()
        AppLanguage.ITALIAN -> """
Salmo 50 (51):
Pietà di me, o Dio, secondo la tua misericordia; nella tua grande bontà cancella il mio peccato. Lavami da tutte le mie colpe, mondami dal mio peccato... Crea in me, o Dio, un cuore puro, rinnova in me uno spirito saldo. Non cacciarmi lontano dalla tua presenza, non privarmi del tuo santo spirito. Alleluia.
        """.trimIndent()
        AppLanguage.CHINESE -> """
诗篇 51 (50):
　神啊，求你按你的慈爱怜恤我！按你丰盛的慈悲涂抹我的过犯！求你将我的罪孽洗除净尽，并洁除我的罪！因为，我知道我的过犯；我的罪常在我面前。我向你犯罪，惟独得罪了你；在你眼前行了这恶... 　神啊，求你为我造清洁的心，使我里面重新有正直的灵。不要丢弃我，使我离开你的面；不要从我收回你的圣灵。阿利路亚。
        """.trimIndent()
        AppLanguage.JAPANESE -> """
詩篇 51篇:
神よ、あなたの慈しみによって私を憐れみ、豊かな憐れみによって私の背きの罪をぬぐい去ってください。私の咎をことごとく洗い流し、罪から清めてください。私は自分の背きを知っており、私の罪は常に私の前にあります... 神よ、私のうちに清い心を造り、私のうちに新しい正しい霊を与えてください。私を御前から投げ捨てず、あなたの聖霊を私から取り去らないでください。ハレルヤ。
        """.trimIndent()
        AppLanguage.KOREAN -> """
시편 51편:
하나님이여 주의 인자를 따라 내게 은혜를 베푸시며 주의 많은 긍휼을 따라 내 죄악을 지워 주소서. 나의 죄악을 말갛게 씻으시며 나의 죄를 깨끗이 제하소서. 무릇 나는 내 죄과를 아오니 내 죄가 항상 내 앞에 있나이다... 하나님이여 내 속에 정한 마음을 창조하시고 내 안에 정직한 영을 새롭게 하소서. 나를 주 앞에서 쫓아내지 마시며 주의 성령을 내게서 거두지 마소서. 할렐루야.
        """.trimIndent()
        AppLanguage.HINDI -> """
भजन संहिता 51 (50):
हे परमेश्वर, अपनी करुणा के अनुसार मुझ पर अनुग्रह कर; अपनी बड़ी दया के अनुसार मेरे अपराधों को मिटा दे! मुझे मेरे अधर्म से भली-भाँति धो, और मेरे पाप से मुझे शुद्ध कर! क्योंकि मैं अपने अपराधों को जानता हूँ, और मेरा पाप निरन्तर मेरे सामने रहता है... हे परमेश्वर, मेरे भीतर शुद्ध मन उत्पन्न कर, और मेरे भीतर स्थिर आत्मा नए सिरे से उत्पन्न कर। मुझे अपने सामने से न निकाल, और अपने पवित्र आत्मा को मुझसे अलग न कर। हालेलुयाह।
        """.trimIndent()
    }

    fun getHourLitanies(prayerId: PrayerId, lang: AppLanguage): String = when (prayerId) {
        PrayerId.PRIME -> when (lang) {
            AppLanguage.ARABIC, AppLanguage.SYRIAN_ARABIC, AppLanguage.SYRIAC -> """
القطع:
أيها النور الحقيقي الذي يضيء لكل إنسان آتٍ إلى العالم، أتيت إلى العالم بمحبتك للبشر، وكل الخليقة تهللت بمجيئك. خلصت أبانا آدم من الضلالة، وعتقت أمنا حواء من طلقات الموت، وأعطيتنا روح البنوة. فلنسبحك ونباركك قائلين: ذوكصابتري...
يا والدة الإله، أنتِ هي الكرمة الحقيقية الحاملة عنقود الحياة، نسألكِ أيتها الممتلئة نعمة، مع الرسل، من أجل خلاص نفوسنا...
            """.trimIndent()
            else -> """
Litanies:
O True Light that gives light to every person coming into the world, in Your love for mankind You came to the world, and all creation rejoiced at Your coming. You saved our father Adam from delusion and delivered Eve from the pangs of death. We praise and bless You saying: Glory to the Father and to the Son and to the Holy Spirit...
            """.trimIndent()
        }
        PrayerId.TERCE -> when (lang) {
            AppLanguage.ARABIC, AppLanguage.SYRIAN_ARABIC, AppLanguage.SYRIAC -> """
القطع:
روحك القدوس يا رب الذي أرسلته على تلاميذك القديسين ورسلك المكرمين في الساعة الثالثة، هذا لا تنزعه منا أيها الصالح، بل جدده في أحشائنا.
قلباً نقياً اخلق فيّ يا الله وروحاً مستقيماً جدده في أحشائي. لا تطرحني من قدام وجهك وروحك القدوس لا تنزعه مني...
            """.trimIndent()
            else -> """
Litanies:
Your Holy Spirit, O Lord, Whom You sent upon Your holy disciples and honored apostles at the third hour, do not take away from us, O Good One, but renew Him within our inward parts.
Create in me a clean heart, O God, and renew a steadfast spirit within me...
            """.trimIndent()
        }
        PrayerId.SEXT -> when (lang) {
            AppLanguage.ARABIC, AppLanguage.SYRIAN_ARABIC, AppLanguage.SYRIAC -> """
القطع:
يا من في اليوم السادس وفي وقت الساعة السادسة، سمرت على الصليب الخطية التي تجرأ عليها أبونا آدم في الفردوس، مزق صك خطايانا أيها المسيح إلهنا وخلصنا.
أنا صرخت إلى الله والرب سمعني، عشية وباكر ووقت الظهر أتكلم وأصرخ فيسمع صوتي...
            """.trimIndent()
            else -> """
Litanies:
O You, Who on the sixth day and in the sixth hour, were nailed to the cross for the sin our father Adam dared commit in Paradise, tear the handwriting of our sins, O Christ our God, and save us...
            """.trimIndent()
        }
        PrayerId.NONE -> when (lang) {
            AppLanguage.ARABIC, AppLanguage.SYRIAN_ARABIC, AppLanguage.SYRIAC -> """
القطع:
يا من ذاق الموت بالجسد في وقت الساعة التاسعة من أجلنا نحن الخطاة، أمت حواسنا الجسمانية أيها المسيح إلهنا ونجنا.
لتدنُ وسيلتي قدامك يا رب، كقولك فهمني. لتدخل طلبتي إلى حضرتك، ككلمتك أحيني...
            """.trimIndent()
            else -> """
Litanies:
O You, Who tasted death in the flesh in the ninth hour for our sake, put to death our carnal senses, O Christ our God, and deliver us.
Let my cry come near before You, O Lord; give me understanding according to Your word...
            """.trimIndent()
        }
        PrayerId.VESPERS -> when (lang) {
            AppLanguage.ARABIC, AppLanguage.SYRIAN_ARABIC, AppLanguage.SYRIAC -> """
القطع:
إذا ما وقفنا في هيكلك المقدس نحسب كالقيام في السماء. يا والدة الإله، أنتِ هي باب السماء، افتحي لنا باب الرحمة.
المجد للآب والابن والروح القدس... الآن وكل أوان وإلى دهر الداهرين، آمين.
            """.trimIndent()
            else -> """
Litanies:
When standing in Your holy temple, we are considered as standing in heaven. O Theotokos, you are the gate of heaven; open unto us the door of mercy.
Glory to the Father and the Son and the Holy Spirit...
            """.trimIndent()
        }
        PrayerId.COMPLINE -> when (lang) {
            AppLanguage.ARABIC, AppLanguage.SYRIAN_ARABIC, AppLanguage.SYRIAC -> """
القطع:
هوذا أنا عتيد أن أقف أمام الديان العادل مرعوباً ومرتعباً من كثرة ذنوبي، لأن العمر المنقضي في الملاهي يستوجب الدينونة. لكن توبي يا نفسي ما دمتِ في الأرض ساكنة...
لو كان العمر ثابتاً وهذا العالم مؤبداً، لكان لكِ يا نفسي حجة واضحة، لكن إذا انكشفت أفعالكِ الرديئة وشروركِ القبيحة أمام الديان العادل، فأي جواب تجيبين؟
            """.trimIndent()
            else -> """
Litanies:
Behold, I am about to stand before the Just Judge, trembling because of my many sins, for life spent in vanities warrants condemnation. But repent, O my soul, while you still dwell on this earth...
            """.trimIndent()
        }
        PrayerId.VEIL -> when (lang) {
            AppLanguage.ARABIC, AppLanguage.SYRIAN_ARABIC, AppLanguage.SYRIAC -> """
القطع:
أعطني يا رب ينابيع دموع كثيرة كما أعطيت المرأة الخاطئة في القديم، واجعلني مستحقاً أن أبل قدميك اللتين أعتقتاني من طريق الضلالة...
إلهي لا تغلق في وجهي باب مراحمك، بل أنعم عليّ بمغفرة خطاياي الكثيرة برحمتك.
            """.trimIndent()
            else -> """
Litanies:
Give me, O Lord, fountains of tears as You gave the sinful woman of old, and make me worthy to wash Your feet which delivered me from the path of astray...
            """.trimIndent()
        }
        PrayerId.MIDNIGHT -> when (lang) {
            AppLanguage.ARABIC, AppLanguage.SYRIAN_ARABIC, AppLanguage.SYRIAC -> """
القطع:
ها هوذا العريس يأتي في نصف الليل، طوبى للعبد الذي يجده مستيقظاً، وأما الذي يجده غافلاً فإنه غير مستحق المضي معه. فانظري يا نفسي لئلا تثقلي نوماً، فتلقي خارج الملكوت، بل اسهري واصرخي قائلة: قدوس قدوس قدوس أنت يا الله، من أجل والدة الإله ارحمنا...
            """.trimIndent()
            else -> """
Litanies:
Behold, the Bridegroom cometh at midnight, blessed is that servant whom He shall find watching; but he whom He shall find heedless is unworthy of going with Him. Beware therefore, O my soul, lest you be weighed down by sleep and cast out of the Kingdom, but be watchful and cry out: Holy, Holy, Holy are You, O God; for the sake of the Theotokos have mercy on us...
            """.trimIndent()
        }
    }

    fun getTrisagionAndConclusion(lang: AppLanguage): String = when (lang) {
        AppLanguage.ARABIC, AppLanguage.SYRIAN_ARABIC, AppLanguage.SYRIAC -> """
الثلاث تقديسات وخاتمة الصلاة:
قدوس الله، قدوس القوي، قدوس الحي الذي لا يموت، الذي ولد من العذراء، ارحمنا.
قدوس الله، قدوس القوي، قدوس الحي الذي لا يموت، الذي صلب عنا، ارحمنا.
قدوس الله، قدوس القوي، قدوس الحي الذي لا يموت، الذي قام من بين الأموات وصعد إلى السموات، ارحمنا.
المجد للآب والابن والروح القدس، الآن وكل أوان وإلى دهر الداهرين، آمين.
أيها الثالوث القدوس ارحمنا... يا رب اغفر لنا خطايانا...
كيرياليسون (يا رب ارحم) 41 مرة.
قدوس قدوس قدوس رب الصباؤوت، السماء والأرض مملوءتان من مجدك وكرامتك. ارحمنا يا الله الآب ضابط الكل...
اللهم اجعلنا مستحقين أن نقول بشكر: أبانا الذي في السموات...
        """.trimIndent()
        AppLanguage.COPTIC -> """
Ⲁⲅⲓⲟⲥ ⲟ̀ Ⲑⲉⲟⲥ:
Ⲁ̀ⲅⲓⲟⲥ ⲟ̀ Ⲑⲉⲟⲥ: ⲁ̀ⲅⲓⲟⲥ ⲓⲥⲭⲩⲣⲟⲥ: ⲁ̀ⲅⲓⲟⲥ ⲁ̀ⲑⲁⲛⲁⲧⲟⲥ: ⲟ̀ ⲉⲕ Ⲡⲁⲣⲑⲉⲛⲟⲩ ⲅⲉⲛⲛⲏⲑⲓⲥ: ⲉ̀ⲗⲉⲏ̀ⲥⲟⲛ ⲏ̀ⲙⲁⲥ.
Ⲁ̀ⲅⲓⲟⲥ ⲟ̀ Ⲑⲉⲟⲥ: ⲁ̀ⲅⲓⲟⲥ ⲓⲥⲭⲩⲣⲟⲥ: ⲁ̀ⲅⲓⲟⲥ ⲁ̀ⲑⲁⲛⲁⲧⲟⲥ: ⲟ̀ ⲥⲧⲁⲩⲣⲱⲑⲓⲥ ⲇⲓ ⲏ̀ⲙⲁⲥ: ⲉ̀ⲗⲉⲏ̀ⲥⲟⲛ ⲏ̀ⲙⲁⲥ.
Ⲁ̀ⲅⲓⲟⲥ ⲟ̀ Ⲑⲉⲟⲥ: ⲁ̀ⲅⲓⲟⲥ ⲓⲥⲭⲩⲣⲟⲥ: ⲁ̀ⲅⲓⲟⲥ ⲁ̀ⲑⲁⲛⲁⲧⲟⲥ: ⲟ̀ ⲁ̀ⲛⲁⲥⲧⲁⲥ ⲉⲕ ⲧⲱⲛ ⲛⲉⲕⲣⲱⲛ: ⲕⲉ ⲁ̀ⲛⲉⲗⲑⲱⲛ ⲓⲥ ⲧⲟⲩⲥ ⲟⲩⲣⲁⲛⲟⲩⲥ: ⲉ̀ⲗⲉⲏ̀ⲥⲟⲛ ⲏ̀ⲙⲁⲥ.
Ⲇⲟⲝⲁ Ⲡⲁⲧⲣⲓ ⲕⲉ Ⲩⲓⲱ ⲕⲉ Ⲁ̀ⲅⲓⲱ Ⲡⲛⲉⲩⲙⲁⲧⲓ: ⲕⲉ ⲛⲩⲛ ⲕⲉ ⲁ̀ⲓ̀ ⲕⲉ ⲓⲥ ⲧⲟⲩⲥ ⲉ̀ⲱ̀ⲛⲁⲥ ⲧⲱⲛ ⲉ̀ⲱ̀ⲛⲱⲛ ⲁ̀ⲙⲏⲛ.
        """.trimIndent()
        AppLanguage.ENGLISH -> """
The Trisagion & Conclusion:
Holy God, Holy Mighty, Holy Immortal, Who was born of the Virgin, have mercy on us.
Holy God, Holy Mighty, Holy Immortal, Who was crucified for us, have mercy on us.
Holy God, Holy Mighty, Holy Immortal, Who rose from the dead and ascended into the heavens, have mercy on us.
Glory to the Father and to the Son and to the Holy Spirit, now and forever and unto the ages of ages. Amen.
Lord have mercy (Kyrie Eleison) 41 times.
Holy, Holy, Holy, Lord of Sabaoth; heaven and earth are full of Your glory and Your honor. Have mercy on us, O God the Father Almighty...
Our Father Who art in heaven...
        """.trimIndent()
        AppLanguage.FRENCH -> """
Le Trisagion et Conclusion :
Saint Dieu, Saint Fort, Saint Immortel, qui es né de la Vierge, aie pitié de nous.
Saint Dieu, Saint Fort, Saint Immortel, qui as été crucifié pour nous, aie pitié de nous.
Saint Dieu, Saint Fort, Saint Immortel, qui es ressuscité des morts et monté aux cieux, aie pitié de nous.
Gloire au Père, au Fils et au Saint-Esprit, maintenant et toujours et dans les siècles des siècles. Amen.
Kyrie Eleison (Seigneur prends pitié) 41 fois...
Notre Père qui es aux cieux...
        """.trimIndent()
        AppLanguage.SPANISH -> """
El Trisagio y Conclusión:
Santo Dios, Santo Fuerte, Santo Inmortal, que naciste de la Virgen, ten piedad de nosotros.
Santo Dios, Santo Fuerte, Santo Inmortal, que fuiste crucificado por nosotros, ten piedad de nosotros.
Santo Dios, Santo Fuerte, Santo Inmortal, que resucitaste de entre los muertos y subiste a los cielos, ten piedad de nosotros.
Señor, ten piedad (Kyrie Eleison) 41 veces...
Padre nuestro que estás en los cielos...
        """.trimIndent()
        AppLanguage.GERMAN, AppLanguage.AUSTRIAN_GERMAN, AppLanguage.SWISS_GERMAN -> """
Das Trisagion und Gebetsabschluss:
Heiliger Gott, heiliger Starker, heiliger Unsterblicher, der du von der Jungfrau geboren bist, erbarme dich unser.
Heiliger Gott, heiliger Starker, heiliger Unsterblicher, der du für uns gekreuzigt wurdest, erbarme dich unser.
Heiliger Gott, heiliger Starker, heiliger Unsterblicher, der du von den Toten auferstanden und in den Himmel aufgefahren bist, erbarme dich unser.
Herr, erbarme dich (Kyrie Eleison) 41 Mal...
Vater unser im Himmel...
        """.trimIndent()
        AppLanguage.ITALIAN -> """
Il Trisagio e Conclusione:
Santo Dio, Santo Forte, Santo Immortale, nato dalla Vergine, abbi pietà di noi.
Santo Dio, Santo Forte, Santo Immortale, crocifisso per noi, abbi pietà di noi.
Santo Dio, Santo Forte, Santo Immortale, risorto dai morti e salito al cielo, abbi pietà di noi.
Signore pietà (Kyrie Eleison) 41 volte...
Padre nostro che sei nei cieli...
        """.trimIndent()
        AppLanguage.CHINESE -> """
三圣颂与结束祈祷：
圣哉上帝，圣哉大能者，圣哉永生者，由童贞女所生者，求祢怜悯我们。
圣哉上帝，圣哉大能者，圣哉永生者，为我们钉十字架者，求祢怜悯我们。
圣哉上帝，圣哉大能者，圣哉永生者，从死里复活并升天者，求祢怜悯我们。
荣耀归于父、子、圣灵，从现在直到永远，世世无尽。阿们。
求主怜悯（求主垂怜）41次。
圣哉，圣哉，圣哉，万军之主；天地充满祢的荣耀和尊贵。全能的父上帝啊，求祢怜悯我们...
我们在天上的父...
        """.trimIndent()
        AppLanguage.JAPANESE -> """
三聖誦と終祷：
聖なる神、聖なる力ある者、聖なる不滅の者、おとめより生まれし者よ、私たちを憐れんでください。
聖なる神、聖なる力ある者、聖なる不灭の者、私たちのために十字架につけられし者よ、私たちを憐れんでください。
聖なる神、聖なる力ある者、聖なる不滅の者、死者のうちから復活し、天に昇られた者よ、私たちを憐れんでください。
栄光は父と子と聖霊にあり、今もいつも世々に至るまで。アーメン。
主よ憐れんでください（キリエ・エレイソン）41回。
聖なるかな、聖なるかな、聖なるかな、万軍の主。天と地はあなたの栄光に満ちています。全能の父なる神よ、私たちを憐れんでください...
天におられる私たちの父よ...
        """.trimIndent()
        AppLanguage.KOREAN -> """
삼성송 및 마침 기도:
거룩하신 하나님, 거룩하고 전능하신 분, 거룩하고 불멸하시는 분, 동정녀에게서 나신 분이여, 우리를 불쌍히 여기소서.
거룩하신 하나님, 거룩하고 전능하신 분, 거룩하고 불멸하시는 분, 우리를 위해 십자가에 못 박히신 분이여, 우리를 불쌍히 여기소서.
거룩하신 하나님, 거룩하고 전능하신 분, 거룩하고 불멸하시는 분, 죽은 자 가운데서 부활하시고 하늘에 오르신 분이여, 우리를 불쌍히 여기소서.
영광이 성부와 성자와 성령께 이제와 항상 영원히 있나이다. 아멘.
주여 자비를 베푸소서 (키리에 엘레이손) 41회.
거룩하시다, 거룩하시다, 거룩하시다, 만군의 주여; 하늘과 땅에 주의 영광이 가득하나이다. 전능하신 아버지 하나님이여, 우리를 불쌍히 여기소서...
하늘에 계신 우리 아버지...
        """.trimIndent()
        AppLanguage.HINDI -> """
त्रिसगियोन (त्रि-पवित्र स्तुति) एवं प्रार्थना समापन:
पवित्र परमेश्वर, पवित्र सर्वशक्तिमान, पवित्र अमर, जो कुँवारी से जन्मे, हम पर दया करें।
पवित्र परमेश्वर, पवित्र सर्वशक्तिमान, पवित्र अमर, जो हमारे लिए क्रूस पर चढ़ाए गए, हम पर दया करें।
पवित्र परमेश्वर, पवित्र सर्वशक्तिमान, पवित्र अमर, जो मृतकों में से जी उठे और स्वर्ग पर चढ़ गए, हम पर दया करें।
पिता, पुत्र और पवित्र आत्मा की महिमा हो, अब और सदा सर्वदा और युगानुयुग। आमीन।
हे प्रभु, दया कर (किरिये एलिसन) 41 बार...
हे हमारे स्वर्गीय पिता, तेरा नाम पवित्र माना जाए...
        """.trimIndent()
    }

    fun getHolyGospel(prayerId: PrayerId, lang: AppLanguage): HolyGospelContent {
        val isArabic = lang == AppLanguage.ARABIC || lang == AppLanguage.SYRIAN_ARABIC || lang == AppLanguage.SYRIAC
        return when (prayerId) {
            PrayerId.PRIME -> HolyGospelContent(
                evangelistTitle = if (isArabic) "فصل شريف من بشارة الإنجيل لمعلمنا يوحنا البشير" else "Holy Gospel according to St. John",
                reference = "John 1:1-17 / يوحنا ١: ١ - ١٧",
                introLitany = if (isArabic) "قفوا بخوف أمام الله لسماع الإنجيل المقدس، بركاته تكون معنا جميعاً آمين." else "Stand in the fear of God, let us hear the Holy Gospel.",
                text = if (isArabic) """
فِي الْبَدْءِ كَانَ الْكَلِمَةُ، وَالْكَلِمَةُ كَانَ عِنْدَ اللهِ، وَكَانَ الْكَلِمَةُ اللهَ. هَذَا كَانَ فِي الْبَدْءِ عِنْدَ اللهِ. كُلُّ شَيْءٍ بِهِ كَانَ، وَبِغَيْرِهِ لَمْ يَكُنْ شَيْءٌ مِمَّا كَانَ. فِيهِ كَانَتِ الْحَيَاةُ، وَالْحَيَاةُ كَانَتْ نُورَ النَّاسِ، وَالنُّورُ يُضِيءُ فِي الظُّلْمَةِ، وَالظُّلْمَةُ لَمْ تُدْرِكْهُ.

كَانَ إِنْسَانٌ مُرْسَلٌ مِنَ اللهِ اسْمُهُ يُوحَنَّا. هَذَا جَاءَ لِلشَّهَادَةِ لِيَشْهَدَ لِلنُّورِ لِكَيْ يُؤْمِنَ الْكُلُّ بِوَاسِطَتِهِ. لَمْ يَكُنْ هُوَ النُّورَ بَلْ لِيَشْهَدَ لِلنُّورِ. كَانَ النُّورُ الْحَقِيقِيُّ الَّذِي يُنِيرُ كُلَّ إِنْسَانٍ آتِياً إِلَى الْعَالَمِ. فِي الْعَالَمِ كَانَ، وَالْعَالَمُ بِهِ كُوِّنَ، وَلَمْ يَعْرِفْهُ الْعَالَمُ. إِلَى خَاصَّتِهِ جَاءَ، وَخَاصَّتُهُ لَمْ تَقْبَلْهُ.

وَأَمَّا كُلُّ الَّذِينَ قَبِلُوهُ فَأَعْطَاهُمْ سُلْطَاناً أَنْ يَصِيرُوا أَوْلاَدَ اللهِ، أَيِ الْمُؤْمِنُونَ بِاسْمِهِ. الَّذِينَ وُلِدُوا لَيْسَ مِنْ دَمٍ، وَلاَ مِنْ مَشِيئَةِ جَسَدٍ، وَلاَ مِنْ مَشِيئَةِ رَجُلٍ، بَلْ مِنَ اللهِ. وَالْكَلِمَةُ صَارَ جَسَداً وَحَلَّ بَيْنَنَا، وَرَأَيْنَا مَجْدَهُ، مَجْداً كَمَا لِوَحِيدٍ مِنَ الآبِ، مَمْلُوءاً نِعْمَةً وَحَقّاً.
            """.trimIndent()
            else """
In the beginning was the Word, and the Word was with God, and the Word was God. He was in the beginning with God. All things were made through Him, and without Him nothing was made that was made. In Him was life, and the life was the light of men. And the light shines in the darkness, and the darkness did not comprehend it.

There was a man sent from God, whose name was John. This man came for a witness, to bear witness of the Light, that all through him might believe. He was not that Light, but was sent to bear witness of that Light. That was the true Light which gives light to every man coming into the world.

He was in the world, and the world was made through Him, and the world did not know Him. He came to His own, and His own did not receive Him. But as many as received Him, to them He gave the right to become children of God, to those who believe in His name: who were born, not of blood, nor of the will of the flesh, nor of the will of man, but of God. And the Word became flesh and dwelt among us, and we beheld His glory, the glory as of the only begotten of the Father, full of grace and truth.
            """.trimIndent(),
            response = if (isArabic) "والمجد لله دائماً أبدياً، آمين." else "Glory to God forever. Amen."
        )

        PrayerId.TERCE -> HolyGospelContent(
            evangelistTitle = if (isArabic) "فصل شريف من بشارة الإنجيل لمعلمنا يوحنا البشير" else "Holy Gospel according to St. John",
            reference = "John 14:26-15:4 / يوحنا ١٤: ٢٦ - ١٥: ٤",
            introLitany = if (isArabic) "قفوا بخوف أمام الله لسماع الإنجيل المقدس." else "Stand in the fear of God, let us hear the Holy Gospel.",
            text = if (isArabic) """
وَأَمَّا الْمُعَزِّي، الرُّوحُ الْقُدُسُ، الَّذِي سَيُرْسِلُهُ الآبُ بِاسْمِي، فَهُوَ يُعَلِّمُكُمْ كُلَّ شَيْءٍ، وَيُذَكِّرُكُمْ بِكُلِّ مَا قُلْتُهُ لَكُمْ. سَلاَماً أَتْرُكُ لَكُمْ. سَلاَمِي أُعْطِيكُمْ. لَيْسَ كَمَا يُعْطِي الْعَالَمُ أُعْطِيكُمْ أَنَا. لاَ تَضْطَرِبْ قُلُوبُكُمْ وَلاَ تَرْهَبْ.

سَمِعْتُمْ أَنِّي قُلْتُ لَكُمْ: أَنَا أَذْهَبُ ثُمَّ آتِي إِلَيْكُمْ. لَوْ كُنْتُمْ تُحِبُّونَنِي لَكُنْتُمْ تَفْرَحُونَ بِأَنِّي قُلْتُ أَمْضِي إِلَى الآبِ، لأَنَّ أَبِي أَعْظَمُ مِنِّي. وَقُلْتُ لَكُمُ الآنَ قَبْلَ أَنْ يَكُونَ، حَتَّى مَتَى كَانَ تُؤْمِنُونَ. لاَ أَتَكَلَّمُ أَيْضاً مَعَكُمْ كَثِيراً، لأَنَّ رَئِيسَ هَذَا الْعَالَمِ يَأْتِي وَلَيْسَ لَهُ فِيَّ شَيْءٌ. وَلَكِنْ لِيَفْهَمَ الْعَالَمُ أَنِّي أُحِبُّ الآبَ، وَكَمَا أَوْصَانِي الآبُ هَكَذَا أَفْعَلُ. قُومُوا نَنْطَلِقْ مِنْ هَهُنَا.

أَنَا الْكَرْمَةُ الْحَقِيقِيَّةُ وَأَبِي الْكَرَّامُ. كُلُّ غُصْنٍ فِيَّ لاَ يَأْتِي بِثَمَرٍ يَنْزِعُهُ، وَكُلُّ مَا يَأْتِي بِثَمَرٍ يُنَقِّيهِ لِيَأْتِيَ بِثَمَرٍ أَكْثَرَ. أَنْتُمُ الآنَ أَنْقِيَاءُ لِسَبَبِ الْكَلاَمِ الَّذِي كَلَّمْتُكُمْ بِهِ. اثْبُتُوا فِيَّ وَأَنَا فِيكُمْ.
            """.trimIndent()
            else """
The Helper, the Holy Spirit, whom the Father will send in My name, He will teach you all things, and bring to your remembrance all things that I said to you. Peace I leave with you, My peace I give to you; not as the world gives do I give to you. Let not your heart be troubled, neither let it be afraid.

You have heard Me say to you, 'I am going away and coming back to you.' If you loved Me, you would rejoice because I said, 'I am going to the Father,' for My Father is greater than I. And now I have told you before it comes, that when it does come to pass, you may believe. I will no longer talk much with you, for the ruler of this world is coming, and he has nothing in Me. But that the world may know that I love the Father, and as the Father gave Me commandment, so I do. Arise, let us go from here.

I am the true vine, and My Father is the vinedresser. Every branch in Me that does not bear fruit He takes away; and every branch that bears fruit He prunes, that it may bear more fruit. You are already clean because of the word which I have spoken to you. Abide in Me, and I in you.
            """.trimIndent(),
            response = if (isArabic) "والمجد لله دائماً أبدياً، آمين." else "Glory to God forever. Amen."
        )

        PrayerId.SEXT -> HolyGospelContent(
            evangelistTitle = if (isArabic) "فصل شريف من بشارة الإنجيل لمعلمنا متى البشير" else "Holy Gospel according to St. Matthew",
            reference = "Matthew 5:1-16 / متى ٥: ١ - ١٦",
            introLitany = if (isArabic) "قفوا بخوف أمام الله لسماع الإنجيل المقدس." else "Stand in the fear of God, let us hear the Holy Gospel.",
            text = if (isArabic) """
وَلَمَّا رَأَى الْجُمُوعَ صَعِدَ إِلَى الْجَبَلِ، فَلَمَّا جَلَسَ تَقَدَّمَ إِلَيْهِ تَلاَمِيذُهُ. فَفَتَحَ فَاهُ وَعَلَّمَهُمْ قَائِلاً: طُوبَى لِلْمَسَاكِينِ بِالرُّوحِ، لأَنَّ لَهُمْ مَلَكُوتَ السَّمَاوَاتِ. طُوبَى لِلْحَزَانَى، لأَنَّهُمْ يُعَزَّوْنَ. طُوبَى لِلْوُدَعَاءِ، لأَنَّهُمْ يَرِثُونَ الأَرْضَ. طُوبَى لِلْجِيَاعِ وَالْعِطَاشِ إِلَى الْبِرِّ، لأَنَّهُمْ يُشْبَعُونَ. طُوبَى لِلرُّحَمَاءِ، لأَنَّهُمْ يُرْحَمُونَ. طُوبَى لأَنْقِيَاءِ الْقَلْبِ، لأَنَّهُمْ يُعَايِنُونَ اللهَ. طُوبَى لِصَانِعِي السَّلاَمِ، لأَنَّهُمْ أَبْنَاءَ اللهِ يُدْعَوْنَ. طُوبَى لِلْمَطْرُودِينَ مِنْ أَجْلِ الْبِرِّ، لأَنَّ لَهُمْ مَلَكُوتَ السَّمَاوَاتِ.

طُوبَى لَكُمْ إِذَا عَيَّرُوكُمْ وَطَرَدُوكُمْ وَقَالُوا عَلَيْكُمْ كُلَّ كَلِمَةٍ شِرِّيرَةٍ، مِنْ أَجْلِي، كَاذِبِينَ. اِفْرَحُوا وَتَهَلَّلُوا، لأَنَّ أَجْرَكُمْ عَظِيمٌ فِي السَّمَاوَاتِ، فَإِنَّهُمْ هَكَذَا طَرَدُوا الأَنْبِيَاءَ الَّذِينَ قَبْلَكُمْ.

أَنْتُمْ مِلْحُ الأَرْضِ، وَلَكِنْ إِنْ فَسَدَ الْمِلْحُ فَبِمَاذَا يُمَلَّحُ؟ لاَ يَصْلُحُ بَعْدُ لِشَيْءٍ، إِلاَّ لأَنْ يُطْرَحَ خَارِجاً وَيُدَاسَ مِنَ النَّاسِ. أَنْتُمْ نُورُ الْعَالَمِ. لاَ يُمْكِنُ أَنْ تُخْفَى مَدِينَةٌ مَوْضُوعَةٌ عَلَى جَبَلٍ. فَلْيُضِئْ نُورُكُمْ هَكَذَا قُدَّامَ النَّاسِ، لِكَيْ يَرَوْا أَعْمَالَكُمُ الْحَسَنَةَ، وَيُمَجِّدُوا أَبَاكُمُ الَّذِي فِي السَّمَاوَاتِ.
            """.trimIndent()
            else """
And seeing the multitudes, He went up on a mountain, and when He was seated His disciples came to Him. Then He opened His mouth and taught them, saying: Blessed are the poor in spirit, for theirs is the kingdom of heaven. Blessed are those who mourn, for they shall be comforted. Blessed are the meek, for they shall inherit the earth. Blessed are those who hunger and thirst for righteousness, for they shall be filled. Blessed are the merciful, for they shall obtain mercy. Blessed are the pure in heart, for they shall see God. Blessed are the peacemakers, for they shall be called sons of God. Blessed are those who are persecuted for righteousness' sake, for theirs is the kingdom of heaven.

Blessed are you when they revile and persecute you, and say all kinds of evil against you falsely for My sake. Rejoice and be exceedingly glad, for great is your reward in heaven.

You are the salt of the earth; but if the salt loses its flavor, how shall it be seasoned? It is then good for nothing but to be thrown out and trampled underfoot by men. You are the light of the world. A city that is set on a hill cannot be hidden. Let your light so shine before men, that they may see your good works and glorify your Father in heaven.
            """.trimIndent(),
            response = if (isArabic) "والمجد لله دائماً أبدياً، آمين." else "Glory to God forever. Amen."
        )

        PrayerId.NONE -> HolyGospelContent(
            evangelistTitle = if (isArabic) "فصل شريف من بشارة الإنجيل لمعلمنا لوقا البشير" else "Holy Gospel according to St. Luke",
            reference = "Luke 9:10-17 / لوقا ٩: ١٠ - ١٧",
            introLitany = if (isArabic) "قفوا بخوف أمام الله لسماع الإنجيل المقدس." else "Stand in the fear of God, let us hear the Holy Gospel.",
            text = if (isArabic) """
وَلَمَّا رَجَعَ الرُّسُلُ حَدَّثُوهُ بِمَا فَعَلُوا، فَأَخَذَهُمْ وَانْفَرَدَ بِهِمْ فِي مَوْضِعٍ خَلاَءٍ لِمَدِينَةٍ تُسَمَّى بَيْتَ صَيْدَا. فَالْجُمُوعُ إِذْ عَلِمُوا تَبِعُوهُ، فَقَبِلَهُمْ وَكَلَّمَهُمْ عَنْ مَلَكُوتِ اللهِ، وَالْمُحْتَاجُونَ إِلَى الشِّفَاءِ شَفَاهُمْ.

وَابْتَدَأَ النَّهَارُ يَمِيلُ، فَتَقَدَّمَ الاِثْنَا عَشَرَ وَقَالُوا لَهُ: «اصْرِفِ الْجَمْعَ لِيَذْهَبُوا إِلَى الْقُرَى وَالضِّيَاعِ حَوَالَيْنَا فَيَبِيتُوا وَيَجِدُوا طَعَاماً، لأَنَّنَا هَهُنَا فِي مَوْضِعٍ خَلاَءٍ». فَقَالَ لَهُمْ: «أَعْطُوهُمْ أَنْتُمْ لِيَأْكُلُوا». فَقَالُوا: «لَيْسَ عِنْدَنَا أَكْثَرُ مِنْ خَمْسَةِ أَرْغِفَةٍ وَسَمَكَتَيْنِ، إِلاَّ أَنْ نَذْهَبَ وَنَشْتَرِيَ طَعَاماً لِهَذَا الشَّعْبِ كُلِّهِ». لأَنَّهُمْ كَانُوا نَحْوَ خَمْسَةِ آلاَفِ رَجُلٍ. فَقَالَ لِتَلاَمِيذِهِ: «أَتَّكِئُوهُمْ فِرَقاً خَمْسِينَ خَمْسِينَ». فَفَعَلُوا هَكَذَا، وَأَتَّكَأُوا الْجَمِيعَ.

فَأَخَذَ الأَرْغِفَةَ الْخَمْسَةَ وَالسَّمَكَتَيْنِ، وَرَفَعَ نَظَرَهُ نَحْوَ السَّمَاءِ وَبَارَكَهَا، ثُمَّ كَسَّرَ وَأَعْطَى التَّلاَمِيذَ لِيَضَعُوا أَمَامَ الْجَمْعِ. فَأَكَلُوا وَشَبِعُوا جَمِيعاً. وَرُفِعَ مَا فَضَلَ عَنْهُمْ مِنَ الْكِسَرِ: اثْنَتَا عَشْرَةَ قُفَّةً.
            """.trimIndent()
            else """
And the apostles, when they had returned, told Him all that they had done. Then He took them and went aside privately into a deserted place belonging to the city called Bethsaida. But when the multitudes knew it, they followed Him; and He received them and spoke to them about the kingdom of God, and healed those who had need of healing.

When the day began to wear away, the twelve came and said to Him, 'Send the multitude away, that they may go into the surrounding towns and country, and lodge and get provisions; for we are in a deserted place here.' But He said to them, 'You give them something to eat.' And they said, 'We have no more than five loaves and two fish, unless we go and buy food for all these people.' For there were about five thousand men. Then He said to His disciples, 'Make them sit down in groups of fifty.' And they did so, and made them all sit down.

Then He took the five loaves and the two fish, and looking up to heaven, He blessed and broke them, and gave them to the disciples to set before the multitude. So they all ate and were filled, and twelve baskets of the leftover fragments were taken up by them.
            """.trimIndent(),
            response = if (isArabic) "والمجد لله دائماً أبدياً، آمين." else "Glory to God forever. Amen."
        )

        PrayerId.VESPERS -> HolyGospelContent(
            evangelistTitle = if (isArabic) "فصل شريف من بشارة الإنجيل لمعلمنا لوقا البشير" else "Holy Gospel according to St. Luke",
            reference = "Luke 4:38-41 / لوقا ٤: ٣٨ - ٤١",
            introLitany = if (isArabic) "قفوا بخوف أمام الله لسماع الإنجيل المقدس." else "Stand in the fear of God, let us hear the Holy Gospel.",
            text = if (isArabic) """
وَلَمَّا قَامَ مِنَ الْمَجْمَعِ دَخَلَ بَيْتَ سِمْعَانَ. وَكَانَتْ حَمَاةُ سِمْعَانَ قَدْ أَخَذَتْهَا حُمَّى شَدِيدَةٌ، فَسَأَلُوهُ مِنْ أَجْلِهَا. فَوَقَفَ فَوْقَهَا وَانْتَهَرَ الْحُمَّى فَتَرَكَتْهَا، وَفِي الْحَالِ قَامَتْ وَصَارَتْ تَخْدُمُهُمْ.

وَعِنْدَ غُرُوبِ الشَّمْسِ، كَانَ كُلُّ الَّذِينَ عِنْدَهُمْ مَرْضَى بِأَنْوَاعِ أَمْرَاضٍ مُخْتَلِفَةٍ يُقَدِّمُونَهُمْ إِلَيْهِ، فَوَضَعَ يَدَيْهِ عَلَى كُلِّ وَاحِدٍ مِنْهُمْ وَشَفَاهُمْ. وَكَانَتْ شَيَاطِينُ أَيْضاً تَخْرُجُ مِنْ كَثِيرِينَ وَهِيَ تَصْرُخُ وَتَقُولُ: «أَنْتَ هُوَ الْمَسِيحُ ابْنُ اللهِ!» فَانْتَهَرَهُمْ وَلَمْ يَدَعْهُمْ يَتَكَلَّمُونَ، لأَنَّهُمْ عَرَفُوا أَنَّهُ هُوَ الْمَسِيحُ.
            """.trimIndent()
            else """
Now He arose from the synagogue and entered Simon's house. But Simon's wife's mother was sick with a high fever, and they made request of Him concerning her. So He stood over her and rebuked the fever, and it left her. And immediately she arose and served them.

When the sun was setting, all those who had any that were sick with various diseases brought them to Him; and He laid His hands on every one of them and healed them. And demons also came out of many, crying out and saying, 'You are the Christ, the Son of God!' And He, rebuking them, did not allow them to speak, for they knew that He was the Christ.
            """.trimIndent(),
            response = if (isArabic) "والمجد لله دائماً أبدياً، آمين." else "Glory to God forever. Amen."
        )

        PrayerId.COMPLINE -> HolyGospelContent(
            evangelistTitle = if (isArabic) "فصل شريف من بشارة الإنجيل لمعلمنا لوقا البشير" else "Holy Gospel according to St. Luke",
            reference = "Luke 2:25-32 / لوقا ٢: ٢٥ - ٣٢",
            introLitany = if (isArabic) "قفوا بخوف أمام الله لسماع الإنجيل المقدس." else "Stand in the fear of God, let us hear the Holy Gospel.",
            text = if (isArabic) """
وَكَانَ رَجُلٌ فِي أُورُشَلِيمَ اسْمُهُ سِمْعَانُ، وَهَذَا الرَّجُلُ كَانَ بَارّاً تَقِيّاً يَنْتَظِرُ تَعْزِيَةَ إِسْرَائِيلَ، وَالرُّوحُ الْقُدُسُ كَانَ عَلَيْهِ. وَكَانَ قَدْ أُوحِيَ إِلَيْهِ بِالرُّوحِ الْقُدُسِ أَنَّهُ لاَ يَرَى الْمَوْتَ قَبْلَ أَنْ يَرَى مَسِيحَ الرَّبِّ.

فَأَتَى بِالرُّوحِ إِلَى الْهَيْكَلِ. وَعِنْدَمَا دَخَلَ بِالصَّبِيِّ يَسُوعَ أَبَوَاهُ، لِيَصْنَعَا لَهُ حَسَبَ عَادَةِ النَّامُوسِ، حَمَلَهُ عَلَى ذِرَاعَيْهِ وَبَارَكَ اللهَ وَقَالَ: «الآنَ تُطْلِقُ عَبْدَكَ يَا سَيِّدُ حَسَبَ قَوْلِكَ بِسَلاَمٍ، لأَنَّ عَيْنَيَّ قَدْ أَبْصَرَتَا خَلاَصَكَ، الَّذِي أَعْدَدْتَهُ قُدَّامَ وَجْهِ جَمِيعِ الشُّعُوبِ. نُورَ إِعْلاَنٍ لِلأُمَمِ، وَمَجْداً لِشَعْبِكَ إِسْرَائِيلَ».
            """.trimIndent()
            else """
And behold, there was a man in Jerusalem whose name was Simeon, and this man was just and devout, waiting for the Consolation of Israel, and the Holy Spirit was upon him. And it had been revealed to him by the Holy Spirit that he would not see death before he had seen the Lord's Christ.

So he came by the Spirit into the temple. And when the parents brought in the Child Jesus, to do for Him according to the custom of the law, he took Him up in his arms and blessed God and said: 'Lord, now You are letting Your servant depart in peace, according to Your word; for my eyes have seen Your salvation which You have prepared before the face of all peoples, a light to bring revelation to the Gentiles, and the glory of Your people Israel.'
            """.trimIndent(),
            response = if (isArabic) "والمجد لله دائماً أبدياً، آمين." else "Glory to God forever. Amen."
        )

        PrayerId.VEIL -> HolyGospelContent(
            evangelistTitle = if (isArabic) "فصل شريف من بشارة الإنجيل لمعلمنا يوحنا البشير" else "Holy Gospel according to St. John",
            reference = "John 1:14-28 / يوحنا ١: ١٤ - ٢٨",
            introLitany = if (isArabic) "قفوا بخوف أمام الله لسماع الإنجيل المقدس." else "Stand in the fear of God, let us hear the Holy Gospel.",
            text = if (isArabic) """
وَالْكَلِمَةُ صَارَ جَسَداً وَحَلَّ بَيْنَنَا، وَرَأَيْنَا مَجْدَهُ، مَجْداً كَمَا لِوَحِيدٍ مِنَ الآبِ، مَمْلُوءاً نِعْمَةً وَحَقّاً. يُوحَنَّا شَهِدَ لَهُ وَنَادَى قَائِلاً: «هَذَا هُوَ الَّذِي قُلْتُ عَنْهُ: إِنَّ الَّذِي يَأْتِي بَعْدِي صَارَ قُدَّامِي لأَنَّهُ كَانَ قَبْلِي».

وَمِنْ مِلْئِهِ نَحْنُ جَمِيعاً أَخَذْنَا، وَنِعْمَةً فَوْقَ نِعْمَةٍ. لأَنَّ النَّامُوسَ بِمُوسَى أُعْطِيَ، أَمَّا النِّعْمَةُ وَالْحَقُّ فَبِيَسُوعَ الْمَسِيحِ صَارَا. اَللهُ لَمْ يَرَهُ أَحَدٌ قَطُّ. اَلاِبْنُ الْوَحِيدُ الَّذِي هُوَ فِي حِضْنِ الآبِ هُوَ خَبَّرَ.
            """.trimIndent()
            else """
And the Word became flesh and dwelt among us, and we beheld His glory, the glory as of the only begotten of the Father, full of grace and truth. John bore witness of Him and cried out, saying, 'This was He of whom I said, "He who comes after me is preferred before me, for He was before me."'

And of His fullness we have all received, and grace for grace. For the law was given through Moses, but grace and truth came through Jesus Christ. No one has seen God at any time. The only begotten Son, who is in the bosom of the Father, He has declared Him.
            """.trimIndent(),
            response = if (isArabic) "والمجد لله دائماً أبدياً، آمين." else "Glory to God forever. Amen."
        )

        PrayerId.MIDNIGHT -> HolyGospelContent(
            evangelistTitle = if (isArabic) "فصل شريف من بشارة الإنجيل لمعلمنا متى البشير" else "Holy Gospel according to St. Matthew",
            reference = "Matthew 25:1-13 / متى ٢٥: ١ - ١٣",
            introLitany = if (isArabic) "قفوا بخوف أمام الله لسماع الإنجيل المقدس." else "Stand in the fear of God, let us hear the Holy Gospel.",
            text = if (isArabic) """
حِينَئِذٍ يُشْبِهُ مَلَكُوتُ السَّمَاوَاتِ عَشْرَ عَذَارَى، أَخَذْنَ مَصَابِيحَهُنَّ وَخَرَجْنَ لِلِقَاءِ الْعَرِيسِ. وَكَانَ خَمْسٌ مِنْهُنَّ حَكِيمَاتٍ، وَخَمْسٌ جَاهِلاَتٍ. فَأَخَذَتِ الْجَاهِلاَتُ مَصَابِيحَهُنَّ وَلَمْ يَأْخُذْنَ مَعَهُنَّ زَيْتاً، وَأَمَّا الْحَكِيمَاتُ فَأَخَذْنَ زَيْتاً فِي آنِيَتِهِنَّ مَعَ مَصَابِيحِهِنَّ.

وَفِيمَا أَبْطَأَ الْعَرِيسُ نَعَسْنَ جَمِيعُهُنَّ وَنِمْنَ. وَفِي نِصْفِ اللَّيْلِ صَارَ صُرَاخٌ: «هُوَذَا الْعَرِيسُ مُقْبِلٌ، فَاخْرُجْنَ لِلِقَائِهِ!» حِينَئِذٍ قَامَتْ جَمِيعُ أُولئِكَ الْعَذَارَى وَأَصْلَحْنَ مَصَابِيحَهُنَّ... فَجَاءَ الْعَرِيسُ، وَالْمُسْتَعِدَّاتُ دَخَلْنَ مَعَهُ إِلَى الْعُرْسِ، وَأُغْلِقَ الْبَابُ. أَخِيراً جَاءَتْ بَقِيَّةُ الْعَذَارَى أَيْضاً قَائِلاَتٍ: يَا رَبَّنَا، يَا رَبَّنَا، افْتَحْ لَنَا! فَأَجَابَ وَقَالَ: الْحَقَّ أَقُولُ لَكُنَّ: إِنِّي مَا أَعْرِفُكُنَّ. فَاسْهَرُوا إِذاً لأَنَّكُمْ لاَ تَعْرِفُونَ الْيَوْمَ وَلاَ السَّاعَةَ الَّتِي يَأْتِي فِيهَا ابْنُ الإِنْسَانِ.
            """.trimIndent()
            else """
Then the kingdom of heaven shall be likened to ten virgins who took their lamps and went out to meet the bridegroom. Now five of them were wise, and five were foolish. Those who were foolish took their lamps and took no oil with them, but the wise took oil in their vessels with their lamps. But while the bridegroom was delayed, they all slumbered and slept.

And at midnight a cry was heard: 'Behold, the bridegroom is coming; go out to meet him!' Then all those virgins arose and trimmed their lamps... And while they went to buy, the bridegroom came, and those who were ready went in with him to the wedding; and the door was shut. Afterward the other virgins came also, saying, 'Lord, Lord, open to us!' But he answered and said, 'Assuredly, I say to you, I do not know you.' Watch therefore, for you know neither the day nor the hour in which the Son of Man is coming.
            """.trimIndent(),
            response = if (isArabic) "والمجد لله دائماً أبدياً، آمين." else "Glory to God forever. Amen."
        )
    }
}
}

data class HolyGospelContent(
    val evangelistTitle: String,
    val reference: String,
    val introLitany: String,
    val text: String,
    val response: String
)

