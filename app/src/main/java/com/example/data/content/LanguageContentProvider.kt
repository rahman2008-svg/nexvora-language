package com.example.data.content

import com.example.data.local.AchievementEntity
import com.example.data.local.VocabularyEntity
import com.example.data.model.*

object LanguageContentProvider {

    fun getDefaultAchievements(): List<AchievementEntity> = listOf(
        AchievementEntity("first_lesson", "First Step", "Complete your first lesson", "🌱", isUnlocked = false, currentProgress = 0, targetGoal = 1, xpReward = 50),
        AchievementEntity("streak_7", "7 Day Flame", "Maintain a 7-day study streak", "🔥", isUnlocked = false, currentProgress = 1, targetGoal = 7, xpReward = 100),
        AchievementEntity("streak_30", "Unstoppable", "Reach a 30-day streak", "⚡", isUnlocked = false, currentProgress = 1, targetGoal = 30, xpReward = 300),
        AchievementEntity("words_50", "Word Collector", "Learn 50 new vocabulary words", "📚", isUnlocked = false, currentProgress = 0, targetGoal = 50, xpReward = 150),
        AchievementEntity("xp_1000", "XP Legend", "Accumulate 1,000 total XP", "⭐", isUnlocked = false, currentProgress = 0, targetGoal = 1000, xpReward = 200),
        AchievementEntity("perfect_lesson", "Flawless", "Complete a lesson without losing any heart", "🎯", isUnlocked = false, currentProgress = 0, targetGoal = 1, xpReward = 75),
        AchievementEntity("speaking_starter", "Vocal Pioneer", "Complete 5 speaking exercises", "🎙️", isUnlocked = false, currentProgress = 0, targetGoal = 5, xpReward = 100),
        AchievementEntity("grammar_master", "Grammar Wizard", "Score 100% on 3 grammar quizzes", "📖", isUnlocked = false, currentProgress = 0, targetGoal = 3, xpReward = 120),
        AchievementEntity("polyglot", "Global Explorer", "Switch and practice 2 different languages", "🌍", isUnlocked = false, currentProgress = 1, targetGoal = 2, xpReward = 150)
    )

    fun getInitialVocabulary(targetLang: String, sourceLang: String): List<VocabularyEntity> {
        return when (targetLang) {
            "en" -> getEnglishVocab(sourceLang)
            "es" -> getSpanishVocab(sourceLang)
            "ar" -> getArabicVocab(sourceLang)
            "hi" -> getHindiVocab(sourceLang)
            "fr" -> getFrenchVocab(sourceLang)
            "de" -> getGermanVocab(sourceLang)
            "ja" -> getJapaneseVocab(sourceLang)
            "bn" -> getBengaliVocab(sourceLang)
            else -> getEnglishVocab(sourceLang)
        }
    }

    private fun getEnglishVocab(sourceLang: String): List<VocabularyEntity> {
        val isBengali = sourceLang == "bn"
        return listOf(
            VocabularyEntity(
                targetLang = "en", sourceLang = sourceLang,
                word = "Hello",
                translation = if (isBengali) "হ্যালো / নমস্কার" else "Hello",
                pronunciation = "heh-LOH",
                exampleTarget = "Hello! How are you today?",
                exampleTranslation = if (isBengali) "হ্যালো! আজ আপনি কেমন আছেন?" else "Hello! How are you today?",
                category = "Greetings", difficulty = "Easy", visualEmoji = "👋"
            ),
            VocabularyEntity(
                targetLang = "en", sourceLang = sourceLang,
                word = "Thank you",
                translation = if (isBengali) "ধন্যবাদ" else "Thank you",
                pronunciation = "THANGK yoo",
                exampleTarget = "Thank you for your warm help.",
                exampleTranslation = if (isBengali) "আপনার আন্তরিক সাহায্যের জন্য ধন্যবাদ।" else "Thank you for your help.",
                category = "Greetings", difficulty = "Easy", visualEmoji = "🙏"
            ),
            VocabularyEntity(
                targetLang = "en", sourceLang = sourceLang,
                word = "Apple",
                translation = if (isBengali) "আপেল" else "Apple",
                pronunciation = "AP-uhl",
                exampleTarget = "An apple a day keeps the doctor away.",
                exampleTranslation = if (isBengali) "প্রতিদিন একটি আপেল ডাক্তার থেকে দূরে রাখে।" else "An apple a day.",
                category = "Food", difficulty = "Easy", visualEmoji = "🍎"
            ),
            VocabularyEntity(
                targetLang = "en", sourceLang = sourceLang,
                word = "Water",
                translation = if (isBengali) "পানি / জল" else "Water",
                pronunciation = "WAW-ter",
                exampleTarget = "Please give me a glass of fresh water.",
                exampleTranslation = if (isBengali) "দয়া করে আমাকে এক গ্লাস পরিষ্কার পানি দিন।" else "Please give me water.",
                category = "Food", difficulty = "Easy", visualEmoji = "💧"
            ),
            VocabularyEntity(
                targetLang = "en", sourceLang = sourceLang,
                word = "Book",
                translation = if (isBengali) "বই" else "Book",
                pronunciation = "book",
                exampleTarget = "I love reading this fascinating book.",
                exampleTranslation = if (isBengali) "আমি এই চমৎকার বইটি পড়তে ভালোবাসি।" else "I love this book.",
                category = "Education", difficulty = "Easy", visualEmoji = "📖"
            ),
            VocabularyEntity(
                targetLang = "en", sourceLang = sourceLang,
                word = "School",
                translation = if (isBengali) "বিদ্যালয় / স্কুল" else "School",
                pronunciation = "skool",
                exampleTarget = "The students walk together to school.",
                exampleTranslation = if (isBengali) "শিক্ষার্থীরা একসাথে হেঁটে স্কুলে যায়।" else "Students walk to school.",
                category = "Education", difficulty = "Easy", visualEmoji = "🏫"
            ),
            VocabularyEntity(
                targetLang = "en", sourceLang = sourceLang,
                word = "Family",
                translation = if (isBengali) "পরিবার" else "Family",
                pronunciation = "FAM-uh-lee",
                exampleTarget = "My family gathered for dinner tonight.",
                exampleTranslation = if (isBengali) "আজ রাতে আমার পরিবার রাতের খাবারের জন্য জড়ো হয়েছে।" else "My family gathered.",
                category = "People", difficulty = "Easy", visualEmoji = "👨‍👩‍👧‍👦"
            ),
            VocabularyEntity(
                targetLang = "en", sourceLang = sourceLang,
                word = "Friend",
                translation = if (isBengali) "বন্ধু" else "Friend",
                pronunciation = "frend",
                exampleTarget = "A true friend is a great blessing in life.",
                exampleTranslation = if (isBengali) "একজন সত্যিকারের বন্ধু জীবনের পরম উপহার।" else "A true friend.",
                category = "People", difficulty = "Easy", visualEmoji = "🤝"
            ),
            VocabularyEntity(
                targetLang = "en", sourceLang = sourceLang,
                word = "Airport",
                translation = if (isBengali) "বিমানবন্দর" else "Airport",
                pronunciation = "AIR-port",
                exampleTarget = "We arrived at the international airport early.",
                exampleTranslation = if (isBengali) "আমরা সময়মতো আন্তর্জাতিক বিমানবন্দরে পৌঁছেছি।" else "We arrived at the airport.",
                category = "Travel", difficulty = "Medium", visualEmoji = "✈️"
            ),
            VocabularyEntity(
                targetLang = "en", sourceLang = sourceLang,
                word = "Restaurant",
                translation = if (isBengali) "রেস্তোরাঁ" else "Restaurant",
                pronunciation = "RES-tuh-rahnt",
                exampleTarget = "Let's reserve a table at the new Italian restaurant.",
                exampleTranslation = if (isBengali) "চলুন নতুন ইতালীয় রেস্তোরাঁয় একটি টেবিল বুক করি।" else "Let's book a table.",
                category = "Travel", difficulty = "Medium", visualEmoji = "🍽️"
            ),
            VocabularyEntity(
                targetLang = "en", sourceLang = sourceLang,
                word = "Interview",
                translation = if (isBengali) "চাকরির ইন্টারভিউ" else "Interview",
                pronunciation = "IN-ter-vyoo",
                exampleTarget = "He prepared thoroughly for his job interview.",
                exampleTranslation = if (isBengali) "তিনি তার চাকরির ইন্টারভিউয়ের জন্য পুঙ্খানুপুঙ্খ প্রস্তুতি নিয়েছেন।" else "Job interview prep.",
                category = "Career", difficulty = "Medium", visualEmoji = "💼"
            ),
            VocabularyEntity(
                targetLang = "en", sourceLang = sourceLang,
                word = "Opportunity",
                translation = if (isBengali) "সুযোগ" else "Opportunity",
                pronunciation = "op-er-TOO-nuh-tee",
                exampleTarget = "Language learning unlocks endless global opportunities.",
                exampleTranslation = if (isBengali) "ভাষা শেখা অসংখ্য বিশ্বব্যাপী সুযোগের দ্বার উন্মোচন করে।" else "Global opportunities.",
                category = "Career", difficulty = "Hard", visualEmoji = "🚀"
            )
        )
    }

    private fun getSpanishVocab(sourceLang: String): List<VocabularyEntity> {
        val isBengali = sourceLang == "bn"
        return listOf(
            VocabularyEntity(
                targetLang = "es", sourceLang = sourceLang,
                word = "Hola",
                translation = if (isBengali) "হ্যালো / হাই" else "Hello",
                pronunciation = "OH-lah",
                exampleTarget = "¡Hola! ¿Cómo estás?",
                exampleTranslation = if (isBengali) "হ্যালো! আপনি কেমন আছেন?" else "Hello! How are you?",
                category = "Greetings", difficulty = "Easy", visualEmoji = "👋"
            ),
            VocabularyEntity(
                targetLang = "es", sourceLang = sourceLang,
                word = "Gracias",
                translation = if (isBengali) "ধন্যবাদ" else "Thank you",
                pronunciation = "GRAH-syahs",
                exampleTarget = "Muchas gracias por tu ayuda.",
                exampleTranslation = if (isBengali) "আপনার সাহায্যের জন্য অনেক ধন্যবাদ।" else "Thank you very much for your help.",
                category = "Greetings", difficulty = "Easy", visualEmoji = "🙏"
            ),
            VocabularyEntity(
                targetLang = "es", sourceLang = sourceLang,
                word = "Manzana",
                translation = if (isBengali) "আপেল" else "Apple",
                pronunciation = "mahn-SAH-nah",
                exampleTarget = "Me gusta comer una manzana roja.",
                exampleTranslation = if (isBengali) "আমি লাল আপেল খেতে পছন্দ করি।" else "I like eating a red apple.",
                category = "Food", difficulty = "Easy", visualEmoji = "🍎"
            ),
            VocabularyEntity(
                targetLang = "es", sourceLang = sourceLang,
                word = "Agua",
                translation = if (isBengali) "পানি" else "Water",
                pronunciation = "AH-gwah",
                exampleTarget = "Un vaso de agua, por favor.",
                exampleTranslation = if (isBengali) "দয়া করে এক গ্লাস পানি দিন।" else "A glass of water, please.",
                category = "Food", difficulty = "Easy", visualEmoji = "💧"
            ),
            VocabularyEntity(
                targetLang = "es", sourceLang = sourceLang,
                word = "Amigo",
                translation = if (isBengali) "বন্ধু" else "Friend",
                pronunciation = "ah-MEE-goh",
                exampleTarget = "Él es mi mejor amigo de la escuela.",
                exampleTranslation = if (isBengali) "সে স্কুলের আমার সবচেয়ে ভালো বন্ধু।" else "He is my best friend.",
                category = "People", difficulty = "Easy", visualEmoji = "🤝"
            )
        )
    }

    private fun getArabicVocab(sourceLang: String): List<VocabularyEntity> {
        val isBengali = sourceLang == "bn"
        return listOf(
            VocabularyEntity(
                targetLang = "ar", sourceLang = sourceLang,
                word = "مرحباً (Marhaban)",
                translation = if (isBengali) "হ্যালো / স্বাগতম" else "Hello",
                pronunciation = "mar-HA-ban",
                exampleTarget = "مرحباً! كيف حالك اليوم؟",
                exampleTranslation = if (isBengali) "হ্যালো! আজ কেমন আছেন?" else "Hello! How are you today?",
                category = "Greetings", difficulty = "Easy", visualEmoji = "👋"
            ),
            VocabularyEntity(
                targetLang = "ar", sourceLang = sourceLang,
                word = "شكراً (Shukran)",
                translation = if (isBengali) "ধন্যবাদ" else "Thank you",
                pronunciation = "shook-RAHN",
                exampleTarget = "شكراً جزيلاً على المساعدة.",
                exampleTranslation = if (isBengali) "সাহায্যের জন্য আপনাকে অনেক ধন্যবাদ।" else "Thank you very much for help.",
                category = "Greetings", difficulty = "Easy", visualEmoji = "🙏"
            ),
            VocabularyEntity(
                targetLang = "ar", sourceLang = sourceLang,
                word = "ماء (Maa')",
                translation = if (isBengali) "পানি" else "Water",
                pronunciation = "maa'",
                exampleTarget = "أريد شرب الماء البارد.",
                exampleTranslation = if (isBengali) "আমি ঠান্ডা পানি পান করতে চাই।" else "I want to drink cold water.",
                category = "Food", difficulty = "Easy", visualEmoji = "💧"
            ),
            VocabularyEntity(
                targetLang = "ar", sourceLang = sourceLang,
                word = "كتاب (Kitaab)",
                translation = if (isBengali) "বই" else "Book",
                pronunciation = "ki-TAAB",
                exampleTarget = "هذا كتاب مفيد جداً.",
                exampleTranslation = if (isBengali) "এটি একটি খুব দরকারী বই।" else "This is a very useful book.",
                category = "Education", difficulty = "Easy", visualEmoji = "📖"
            )
        )
    }

    private fun getHindiVocab(sourceLang: String): List<VocabularyEntity> {
        val isBengali = sourceLang == "bn"
        return listOf(
            VocabularyEntity(
                targetLang = "hi", sourceLang = sourceLang,
                word = "नमस्ते (Namaste)",
                translation = if (isBengali) "নমস্কার / হ্যালো" else "Hello",
                pronunciation = "nah-mah-STAY",
                exampleTarget = "नमस्ते! आप कैसे हैं?",
                exampleTranslation = if (isBengali) "নমস্কার! আপনি কেমন আছেন?" else "Hello! How are you?",
                category = "Greetings", difficulty = "Easy", visualEmoji = "🙏"
            ),
            VocabularyEntity(
                targetLang = "hi", sourceLang = sourceLang,
                word = "धन्यवाद (Dhanyavaad)",
                translation = if (isBengali) "ধন্যবাদ" else "Thank you",
                pronunciation = "dhun-yuh-VAAD",
                exampleTarget = "आपकी मदद के लिए धन्यवाद।",
                exampleTranslation = if (isBengali) "আপনার সাহায্যের জন্য ধন্যবাদ।" else "Thank you for your help.",
                category = "Greetings", difficulty = "Easy", visualEmoji = "🤝"
            ),
            VocabularyEntity(
                targetLang = "hi", sourceLang = sourceLang,
                word = "पानी (Paani)",
                translation = if (isBengali) "পানি" else "Water",
                pronunciation = "PAA-nee",
                exampleTarget = "कृपया मुझे एक गिलास पानी दीजिए।",
                exampleTranslation = if (isBengali) "দয়া করে আমাকে এক গ্লাস পানি দিন।" else "Please give me a glass of water.",
                category = "Food", difficulty = "Easy", visualEmoji = "💧"
            )
        )
    }

    private fun getFrenchVocab(sourceLang: String): List<VocabularyEntity> = listOf(
        VocabularyEntity(targetLang = "fr", sourceLang = sourceLang, word = "Bonjour", translation = "Hello / Good morning", pronunciation = "bohn-zhoor", exampleTarget = "Bonjour, comment allez-vous?", exampleTranslation = "Hello, how are you?", category = "Greetings", difficulty = "Easy", visualEmoji = "🥐"),
        VocabularyEntity(targetLang = "fr", sourceLang = sourceLang, word = "Merci", translation = "Thank you", pronunciation = "mehr-see", exampleTarget = "Merci beaucoup pour tout!", exampleTranslation = "Thank you very much for everything!", category = "Greetings", difficulty = "Easy", visualEmoji = "🙏"),
        VocabularyEntity(targetLang = "fr", sourceLang = sourceLang, word = "L'eau", translation = "Water", pronunciation = "loh", exampleTarget = "Une bouteille d'eau, s'il vous plaît.", exampleTranslation = "A bottle of water, please.", category = "Food", difficulty = "Easy", visualEmoji = "💧")
    )

    private fun getGermanVocab(sourceLang: String): List<VocabularyEntity> = listOf(
        VocabularyEntity(targetLang = "de", sourceLang = sourceLang, word = "Hallo", translation = "Hello", pronunciation = "HAH-loh", exampleTarget = "Hallo! Wie geht es dir?", exampleTranslation = "Hello! How are you?", category = "Greetings", difficulty = "Easy", visualEmoji = "👋"),
        VocabularyEntity(targetLang = "de", sourceLang = sourceLang, word = "Danke", translation = "Thank you", pronunciation = "DAHN-kuh", exampleTarget = "Danke schön für die Hilfe.", exampleTranslation = "Thank you very much for the help.", category = "Greetings", difficulty = "Easy", visualEmoji = "🙏"),
        VocabularyEntity(targetLang = "de", sourceLang = sourceLang, word = "Wasser", translation = "Water", pronunciation = "VAH-ser", exampleTarget = "Ich trinke gerne frisches Wasser.", exampleTranslation = "I like drinking fresh water.", category = "Food", difficulty = "Easy", visualEmoji = "💧")
    )

    private fun getJapaneseVocab(sourceLang: String): List<VocabularyEntity> = listOf(
        VocabularyEntity(targetLang = "ja", sourceLang = sourceLang, word = "こんにちは (Konnichiwa)", translation = "Hello / Good afternoon", pronunciation = "kon-nee-chee-wah", exampleTarget = "こんにちは、お元気ですか？", exampleTranslation = "Hello, how are you?", category = "Greetings", difficulty = "Easy", visualEmoji = "🌸"),
        VocabularyEntity(targetLang = "ja", sourceLang = sourceLang, word = "ありがとう (Arigatou)", translation = "Thank you", pronunciation = "ah-ree-GAH-toh", exampleTarget = "どうもありがとうございます。", exampleTranslation = "Thank you very much.", category = "Greetings", difficulty = "Easy", visualEmoji = "🙏"),
        VocabularyEntity(targetLang = "ja", sourceLang = sourceLang, word = "水 (Mizu)", translation = "Water", pronunciation = "mee-zoo", exampleTarget = "お水を一杯ください。", exampleTranslation = "Please give me a cup of water.", category = "Food", difficulty = "Easy", visualEmoji = "💧")
    )

    private fun getBengaliVocab(sourceLang: String): List<VocabularyEntity> = listOf(
        VocabularyEntity(targetLang = "bn", sourceLang = sourceLang, word = "হ্যালো / নমস্কার", translation = "Hello / Greetings", pronunciation = "hæ-lo", exampleTarget = "হ্যালো! আপনি কেমন আছেন?", exampleTranslation = "Hello! How are you?", category = "Greetings", difficulty = "Easy", visualEmoji = "👋"),
        VocabularyEntity(targetLang = "bn", sourceLang = sourceLang, word = "ধন্যবাদ", translation = "Thank you", pronunciation = "dhon-no-baad", exampleTarget = "আপনার সাহায্যের জন্য অনেক ধন্যবাদ।", exampleTranslation = "Thank you very much for your help.", category = "Greetings", difficulty = "Easy", visualEmoji = "🙏"),
        VocabularyEntity(targetLang = "bn", sourceLang = sourceLang, word = "পানি", translation = "Water", pronunciation = "paa-nee", exampleTarget = "আমাকে এক গ্লাস পানি দিন।", exampleTranslation = "Give me a glass of water.", category = "Food", difficulty = "Easy", visualEmoji = "💧")
    )

    /**
     * Generates rich course curriculum for the given target and source language pair
     */
    fun getCourseCurriculum(targetLang: String, sourceLang: String): List<UnitData> {
        val isBengaliSource = sourceLang == "bn"

        return listOf(
            // Unit 1: Basics & Foundations
            UnitData(
                id = "unit_1_basics",
                level = CEFRLevel.A1,
                unitNumber = 1,
                title = if (isBengaliSource) "Unit 1: প্রাথমিক ও সম্ভাষণ (Basics)" else "Unit 1: Basics & Greetings",
                description = if (isBengaliSource) "দৈনন্দিন সম্ভাষণ, পরিচয় প্রদান, সংখ্যা ও পরিবার" else "Greetings, introductions, numbers, and family basics",
                icon = "👋",
                lessons = listOf(
                    Lesson(
                        id = "lesson_1_greetings",
                        unitId = "unit_1_basics",
                        level = CEFRLevel.A1,
                        title = if (isBengaliSource) "Lesson 1: সম্ভাষণ (Greetings)" else "Lesson 1: Greetings",
                        description = if (isBengaliSource) "Hello, Good Morning, Goodbye এবং শিষ্টাচার" else "Learn essential greetings and courtesy phrases",
                        icon = "🌟",
                        exercises = listOf(
                            // 1. Multiple Choice
                            Exercise(
                                id = "ex_1_mcq",
                                type = ExerciseType.MULTIPLE_CHOICE,
                                prompt = if (isBengaliSource) "“Hello” এর সঠিক অর্থ কোনটি?" else "What is the meaning of \"Hello\"?",
                                targetPhrase = "Hello",
                                pronunciation = "heh-LOH",
                                audioToPlay = "Hello",
                                options = if (isBengaliSource) listOf("হ্যালো / নমস্কার", "ধন্যবাদ", "বিদায়", "দয়া করে") else listOf("Hello", "Goodbye", "Thank you", "Please"),
                                correctAnswer = if (isBengaliSource) "হ্যালো / নমস্কার" else "Hello",
                                explanation = if (isBengaliSource) "Hello হলো পরিচিত কারো সাথে দেখা হলে সম্ভাষণ জানানোর মূল শব্দ।" else "Hello is used as a standard greeting upon meeting."
                            ),
                            // 2. Picture Learning
                            Exercise(
                                id = "ex_2_pic",
                                type = ExerciseType.PICTURE_LEARNING,
                                prompt = if (isBengaliSource) "ছবি দেখে সঠিক শব্দটি নির্বাচন করুন:" else "Select the correct word for the image:",
                                visualEmoji = "🍎",
                                targetPhrase = "Apple",
                                pronunciation = "AP-uhl",
                                audioToPlay = "Apple",
                                options = if (isBengaliSource) listOf("আপেল (Apple)", "কলা (Banana)", "আম (Mango)", "কমলা (Orange)") else listOf("Apple", "Banana", "Mango", "Orange"),
                                correctAnswer = if (isBengaliSource) "আপেল (Apple)" else "Apple"
                            ),
                            // 3. Word Matching
                            Exercise(
                                id = "ex_3_matching",
                                type = ExerciseType.WORD_MATCHING,
                                prompt = if (isBengaliSource) "সঠিক জোড়া মিলিয়ে দিন:" else "Match the word pairs:",
                                matchingPairs = if (isBengaliSource) listOf(
                                    MatchingPair("Hello", "হ্যালো"),
                                    MatchingPair("Thank you", "ধন্যবাদ"),
                                    MatchingPair("Water", "পানি"),
                                    MatchingPair("Book", "বই")
                                ) else listOf(
                                    MatchingPair("Hello", "Greeting"),
                                    MatchingPair("Water", "Liquid drink"),
                                    MatchingPair("Apple", "Red fruit"),
                                    MatchingPair("Book", "Reading material")
                                )
                            ),
                            // 4. Fill in the Blank
                            Exercise(
                                id = "ex_4_fill",
                                type = ExerciseType.FILL_IN_THE_BLANK,
                                prompt = if (isBengaliSource) "বাক্যের শূন্যস্থান পূরণ করুন:" else "Fill in the blank:",
                                targetPhrase = "I ___ a student.",
                                options = listOf("am", "is", "are", "be"),
                                correctAnswer = "am",
                                explanation = if (isBengaliSource) "'I'-এর সাথে Present Tense-এ 'am' বসে।" else "'I' takes 'am' in the simple present tense."
                            ),
                            // 5. Listening Lab
                            Exercise(
                                id = "ex_5_listen",
                                type = ExerciseType.LISTENING,
                                prompt = if (isBengaliSource) "অডিও শুনে বলুন কী বলা হয়েছে:" else "Listen to the audio and select what you hear:",
                                audioToPlay = "Good morning, how are you?",
                                options = listOf(
                                    "Good morning, how are you?",
                                    "Good night, see you tomorrow",
                                    "Goodbye, have a great day",
                                    "Good evening, welcome"
                                ),
                                correctAnswer = "Good morning, how are you?"
                            ),
                            // 6. Speaking Studio
                            Exercise(
                                id = "ex_6_speak",
                                type = ExerciseType.SPEAKING,
                                prompt = if (isBengaliSource) "মাইক্রোফোন চেপে পরিষ্কারভাবে উচ্চারণ করুন:" else "Tap mic and speak this phrase clearly:",
                                targetPhrase = "I am very happy to meet you",
                                pronunciation = "Eye am VEH-ree HAP-ee too meet yoo",
                                audioToPlay = "I am very happy to meet you",
                                correctAnswer = "I am very happy to meet you",
                                explanation = if (isBengaliSource) "অর্থ: আপনার সাথে দেখা হয়ে আমি অত্যন্ত আনন্দিত।" else "Pronounce with confidence and clear pacing."
                            ),
                            // 7. Arrange Sentence
                            Exercise(
                                id = "ex_7_arrange",
                                type = ExerciseType.ARRANGE_SENTENCE,
                                prompt = if (isBengaliSource) "শব্দগুলো সাজিয়ে সঠিক বাক্য তৈরি করুন (আপনি কেমন আছেন?):" else "Arrange the words to form the question:",
                                scrambledWords = listOf("How", "you", "?", "are"),
                                correctAnswer = "How are you ?"
                            ),
                            // 8. Word Builder
                            Exercise(
                                id = "ex_8_builder",
                                type = ExerciseType.WORD_BUILDER,
                                prompt = if (isBengaliSource) "শব্দগুলো সাজিয়ে অনুবাদ গঠন করুন (আমি স্কুলে যাই):" else "Build the translated sentence (I go to school):",
                                scrambledWords = listOf("go", "to", "I", "school"),
                                correctAnswer = "I go to school"
                            ),
                            // 9. Translation
                            Exercise(
                                id = "ex_9_trans",
                                type = ExerciseType.TRANSLATION,
                                prompt = if (isBengaliSource) "“ধন্যবাদ” এর ইংরেজি অনুবাদ কী?" else "Translate \"ধন্যবাদ\" to English:",
                                targetPhrase = "ধন্যবাদ",
                                options = listOf("Thank you", "Excuse me", "Welcome", "Please"),
                                correctAnswer = "Thank you"
                            ),
                            // 10. Conversation Practice
                            Exercise(
                                id = "ex_10_convo",
                                type = ExerciseType.CONVERSATION,
                                prompt = if (isBengaliSource) "আলাপচারিতায় অংশ নিন ও সঠিক উত্তর বেছে দিন:" else "Participate in this friendly conversation:",
                                dialogueTurns = listOf(
                                    DialogueTurn("Alex", "👤", "Hello! My name is Alex. What is your name?", if (isBengaliSource) "হ্যালো! আমার নাম অ্যালেক্স। আপনার নাম কী?" else "Hello! What is your name?", "Hello! My name is Alex. What is your name?"),
                                    DialogueTurn("You", "🧑‍🎓", "Hello Alex! I am learning with NexVora.", if (isBengaliSource) "হ্যালো অ্যালেক্স! আমি নেক্সভোরার সাথে শিখছি।" else "Hello Alex! I am learning with NexVora.", "Hello Alex! I am learning with NexVora."),
                                    DialogueTurn("Alex", "👤", "Wonderful! Welcome to our language journey!", if (isBengaliSource) "দারুণ! আমাদের ভাষা শেখার যাত্রায় স্বাগতম!" else "Wonderful! Welcome to our language journey!", "Wonderful! Welcome to our language journey!")
                                ),
                                options = listOf(
                                    "Hello Alex! I am learning with NexVora.",
                                    "I don't know who you are.",
                                    "Goodbye forever.",
                                    "No thank you."
                                ),
                                correctAnswer = "Hello Alex! I am learning with NexVora."
                            )
                        )
                    ),
                    Lesson(
                        id = "lesson_2_intro",
                        unitId = "unit_1_basics",
                        level = CEFRLevel.A1,
                        title = if (isBengaliSource) "Lesson 2: পরিচয় প্রদান (Introductions)" else "Lesson 2: Introductions",
                        description = if (isBengaliSource) "নাম, পেশা ও নিজের দেশ সম্পর্কে কথা বলা" else "Tell your name, country, and occupation",
                        icon = "🤝",
                        exercises = listOf(
                            Exercise(
                                id = "ex_2_1",
                                type = ExerciseType.MULTIPLE_CHOICE,
                                prompt = if (isBengaliSource) "“What is your name?” এর উত্তর হিসেবে কোনটি সঠিক?" else "Which is the proper response to \"What is your name?\"?",
                                targetPhrase = "What is your name?",
                                audioToPlay = "What is your name?",
                                options = listOf("My name is John.", "I am eating apple.", "It is raining.", "At 5 PM."),
                                correctAnswer = "My name is John."
                            ),
                            Exercise(
                                id = "ex_2_2",
                                type = ExerciseType.ARRANGE_SENTENCE,
                                prompt = if (isBengaliSource) "বাক্যটি সাজান (আমি বাংলাদেশে বাস করি):" else "Arrange: I live in Bangladesh",
                                scrambledWords = listOf("live", "I", "in", "Bangladesh"),
                                correctAnswer = "I live in Bangladesh"
                            ),
                            Exercise(
                                id = "ex_2_3",
                                type = ExerciseType.SPEAKING,
                                prompt = if (isBengaliSource) "বলুন: Nice to meet you" else "Speak clearly: Nice to meet you",
                                targetPhrase = "Nice to meet you",
                                audioToPlay = "Nice to meet you",
                                correctAnswer = "Nice to meet you"
                            ),
                            Exercise(
                                id = "ex_2_4",
                                type = ExerciseType.WORD_MATCHING,
                                prompt = if (isBengaliSource) "শব্দ জোড়া মেলান:" else "Match words:",
                                matchingPairs = listOf(
                                    MatchingPair("Name", if (isBengaliSource) "নাম" else "Identity"),
                                    MatchingPair("Country", if (isBengaliSource) "দেশ" else "Nation"),
                                    MatchingPair("Live", if (isBengaliSource) "বাস করা" else "Reside"),
                                    MatchingPair("Student", if (isBengaliSource) "শিক্ষার্থী" else "Learner")
                                )
                            )
                        )
                    ),
                    Lesson(
                        id = "lesson_3_numbers",
                        unitId = "unit_1_basics",
                        level = CEFRLevel.A1,
                        title = if (isBengaliSource) "Lesson 3: সংখ্যা ও সময় (Numbers & Time)" else "Lesson 3: Numbers & Time",
                        description = if (isBengaliSource) "১ থেকে ১০০ এবং ঘড়ির সময় প্রকাশ" else "Master counting, prices, and telling time",
                        icon = "🔢",
                        exercises = listOf(
                            Exercise(
                                id = "ex_3_1",
                                type = ExerciseType.FILL_IN_THE_BLANK,
                                prompt = if (isBengaliSource) "শূন্যস্থান পূরণ করুন:" else "Complete the sentence:",
                                targetPhrase = "It is three ___ the afternoon.",
                                options = listOf("in", "at", "on", "by"),
                                correctAnswer = "in"
                            ),
                            Exercise(
                                id = "ex_3_2",
                                type = ExerciseType.LISTENING,
                                prompt = if (isBengaliSource) "কোন সংখ্যাটি শুনলেন?" else "Which number was spoken?",
                                audioToPlay = "Twenty-five",
                                options = listOf("25", "52", "15", "50"),
                                correctAnswer = "25"
                            ),
                            Exercise(
                                id = "ex_3_3",
                                type = ExerciseType.MULTIPLE_CHOICE,
                                prompt = if (isBengaliSource) "“What time is it?” এর অর্থ কী?" else "What does \"What time is it?\" mean?",
                                options = if (isBengaliSource) listOf("এখন কয়টা বাজে?", "আপনি কোথায় যাচ্ছেন?", "আজ কি বার?", "কত টাকা?") else listOf("What time is it?", "Where are you going?", "What day is today?", "How much?"),
                                correctAnswer = if (isBengaliSource) "এখন কয়টা বাজে?" else "What time is it?"
                            )
                        )
                    ),
                    Lesson(
                        id = "lesson_4_family",
                        unitId = "unit_1_basics",
                        level = CEFRLevel.A1,
                        title = if (isBengaliSource) "Lesson 4: পরিবার (Family)" else "Lesson 4: Family",
                        description = if (isBengaliSource) "মা, বাবা, ভাই, বোন ও পারিবারিক সম্পর্ক" else "Talk about parents, siblings, and relatives",
                        icon = "👨‍👩‍👧‍👦",
                        exercises = listOf(
                            Exercise(
                                id = "ex_4_1",
                                type = ExerciseType.WORD_MATCHING,
                                prompt = if (isBengaliSource) "পারিবারিক সম্পর্কের জোড়া মেলান:" else "Match family members:",
                                matchingPairs = listOf(
                                    MatchingPair("Mother", if (isBengaliSource) "মা" else "Female parent"),
                                    MatchingPair("Father", if (isBengaliSource) "বাবা" else "Male parent"),
                                    MatchingPair("Brother", if (isBengaliSource) "ভাই" else "Male sibling"),
                                    MatchingPair("Sister", if (isBengaliSource) "বোন" else "Female sibling")
                                )
                            ),
                            Exercise(
                                id = "ex_4_2",
                                type = ExerciseType.SPEAKING,
                                prompt = if (isBengaliSource) "উচ্চারণ করুন:" else "Speak:",
                                targetPhrase = "I love my family very much",
                                audioToPlay = "I love my family very much",
                                correctAnswer = "I love my family very much"
                            )
                        )
                    )
                )
            ),

            // Unit 2: Everyday Life & Social
            UnitData(
                id = "unit_2_daily",
                level = CEFRLevel.A2,
                unitNumber = 2,
                title = if (isBengaliSource) "Unit 2: প্রাত্যহিক জীবন (Everyday Life)" else "Unit 2: Everyday Life & Social",
                description = if (isBengaliSource) "দৈনন্দিন রুটিন, শখ, অনুভূতি এবং বন্ধুত্ব" else "Daily routines, feelings, hobbies, and social interactions",
                icon = "☀️",
                lessons = listOf(
                    Lesson(
                        id = "lesson_2_1_routine",
                        unitId = "unit_2_daily",
                        level = CEFRLevel.A2,
                        title = if (isBengaliSource) "Lesson 1: প্রতিদিনের রুটিন" else "Lesson 1: Daily Routine",
                        description = if (isBengaliSource) "সকালে ঘুম থেকে ওঠা, নাশতা ও কাজের সময়" else "Morning habits, meals, and daily tasks",
                        icon = "⏰",
                        exercises = listOf(
                            Exercise(
                                id = "ex_r1",
                                type = ExerciseType.ARRANGE_SENTENCE,
                                prompt = if (isBengaliSource) "সাজান (আমি প্রতিদিন সকাল ৬টায় ঘুম থেকে উঠি):" else "Arrange: I wake up at 6 AM every morning",
                                scrambledWords = listOf("wake", "at", "I", "6", "morning", "up", "every", "AM"),
                                correctAnswer = "I wake up at 6 AM every morning"
                            ),
                            Exercise(
                                id = "ex_r2",
                                type = ExerciseType.FILL_IN_THE_BLANK,
                                prompt = if (isBengaliSource) "শূন্যস্থান পূরণ করুন:" else "Choose the correct verb form:",
                                targetPhrase = "She ___ breakfast at 8:00 AM.",
                                options = listOf("eats", "eat", "eating", "is eat"),
                                correctAnswer = "eats",
                                explanation = if (isBengaliSource) "Third person singular (She) এর সাথে Present Indefinite-এ verb-এ 's/es' যোগ হয়।" else "Third person singular subjects take 'eats'."
                            ),
                            Exercise(
                                id = "ex_r3",
                                type = ExerciseType.LISTENING,
                                prompt = if (isBengaliSource) "অডিও শুনে বাক্যটি চিনুন:" else "Listen and select:",
                                audioToPlay = "I take a shower before going to work",
                                options = listOf(
                                    "I take a shower before going to work",
                                    "I go to work after dinner",
                                    "I sleep late on weekends",
                                    "I cook breakfast in the morning"
                                ),
                                correctAnswer = "I take a shower before going to work"
                            )
                        )
                    )
                )
            ),

            // Unit 3: Food & Dining
            UnitData(
                id = "unit_3_food",
                level = CEFRLevel.B1,
                unitNumber = 3,
                title = if (isBengaliSource) "Unit 3: খাবার ও রেস্তোরাঁ (Food & Dining)" else "Unit 3: Food & Dining",
                description = if (isBengaliSource) "রেস্তোরাঁয় অর্ডার, রান্নার স্বাদ এবং ক্যাফে কনভারসেশন" else "Ordering at restaurants, flavours, and cafe conversations",
                icon = "🍽️",
                lessons = listOf(
                    Lesson(
                        id = "lesson_3_1_order",
                        unitId = "unit_3_food",
                        level = CEFRLevel.B1,
                        title = if (isBengaliSource) "Lesson 1: রেস্তোরাঁয় অর্ডার (Ordering Food)" else "Lesson 1: Restaurant Orders",
                        description = if (isBengaliSource) "মেনু চাওয়া, বিল পরিশোধ ও ওয়েটারের সাথে কথা" else "Asking for menu, paying bill, speaking to waiter",
                        icon = "☕",
                        exercises = listOf(
                            Exercise(
                                id = "ex_f1",
                                type = ExerciseType.CONVERSATION,
                                prompt = if (isBengaliSource) "ওয়েটারের প্রশ্নের ভদ্র উত্তর দিন:" else "Respond politely to the waiter:",
                                dialogueTurns = listOf(
                                    DialogueTurn("Waiter", "🧑‍🍳", "Good evening! Are you ready to order your dinner?", if (isBengaliSource) "শুভ সন্ধ্যা! আপনি কি রাতের খাবারের অর্ডার দিতে প্রস্তুত?" else "Good evening! Ready to order?", "Good evening! Are you ready to order your dinner?"),
                                    DialogueTurn("You", "🧑‍🎓", "Yes, I would like grilled chicken with fresh salad, please.", if (isBengaliSource) "হ্যাঁ, দয়া করে ফ্রেশ সালাদের সাথে গ্রিল্ড চিকেন দিন।" else "Yes, grilled chicken please.", "Yes, I would like grilled chicken with fresh salad, please."),
                                    DialogueTurn("Waiter", "🧑‍🍳", "Excellent choice! And for drink?", if (isBengaliSource) "চমৎকার পছন্দ! আর পানের জন্য কী নেবেন?" else "Excellent choice! And drink?", "Excellent choice! And for drink?")
                                ),
                                options = listOf(
                                    "Yes, I would like grilled chicken with fresh salad, please.",
                                    "Give me food fast right now.",
                                    "I hate this restaurant.",
                                    "No, go away."
                                ),
                                correctAnswer = "Yes, I would like grilled chicken with fresh salad, please."
                            ),
                            Exercise(
                                id = "ex_f2",
                                type = ExerciseType.SPEAKING,
                                prompt = if (isBengaliSource) "বিল চাওয়ার চমৎকার বাক্যটি বলুন:" else "Speak to ask for the bill:",
                                targetPhrase = "Could we please have the bill?",
                                audioToPlay = "Could we please have the bill?",
                                correctAnswer = "Could we please have the bill?"
                            )
                        )
                    )
                )
            ),

            // Unit 4: Travel & Navigation
            UnitData(
                id = "unit_4_travel",
                level = CEFRLevel.B2,
                unitNumber = 4,
                title = if (isBengaliSource) "Unit 4: ভ্রমণ ও দিকনির্দেশ (Travel & Navigation)" else "Unit 4: Travel & Navigation",
                description = if (isBengaliSource) "বিমানবন্দর, হোটেল বুকিং এবং পথ চাওয়া" else "Airports, hotel reservations, and asking directions",
                icon = "✈️",
                lessons = listOf(
                    Lesson(
                        id = "lesson_4_1_airport",
                        unitId = "unit_4_travel",
                        level = CEFRLevel.B2,
                        title = if (isBengaliSource) "Lesson 1: বিমানবন্দর ও ট্রানজিট (Airport)" else "Lesson 1: Airport & Transit",
                        description = if (isBengaliSource) "বোর্ডিং পাস, লাগেজ চেক ও ইমিগ্রেশন" else "Boarding passes, luggage check, and immigration",
                        icon = "🛂",
                        exercises = listOf(
                            Exercise(
                                id = "ex_t1",
                                type = ExerciseType.MULTIPLE_CHOICE,
                                prompt = if (isBengaliSource) "“Where is the boarding gate 12?” এর সঠিক বাংলা কী?" else "Translate: Where is boarding gate 12?",
                                options = if (isBengaliSource) listOf(
                                    "১২ নম্বর বোর্ডিং গেটটি কোন দিকে?",
                                    "আমার লাগেজ কোথায়?",
                                    "ফ্লাইট কখন ছাড়বে?",
                                    "আমার টিকিট দিন"
                                ) else listOf(
                                    "Where is gate 12?",
                                    "Where is my baggage?",
                                    "When is flight leaving?",
                                    "Here is my ticket"
                                ),
                                correctAnswer = if (isBengaliSource) "১২ নম্বর বোর্ডিং গেটটি কোন দিকে?" else "Where is gate 12?"
                            ),
                            Exercise(
                                id = "ex_t2",
                                type = ExerciseType.WORD_BUILDER,
                                prompt = if (isBengaliSource) "সাজান (আমার লাগেজ হারিয়ে গেছে):" else "Build: My luggage is lost",
                                scrambledWords = listOf("lost", "luggage", "My", "is"),
                                correctAnswer = "My luggage is lost"
                            )
                        )
                    )
                )
            ),

            // Unit 5: Real-Life Career & Scenarios
            UnitData(
                id = "unit_5_career",
                level = CEFRLevel.C1,
                unitNumber = 5,
                title = if (isBengaliSource) "Unit 5: ক্যারিয়ার ও প্র্যাকটিক্যাল ইংলিশ (Career)" else "Unit 5: Real-Life Career & Scenarios",
                description = if (isBengaliSource) "চাকরির ইন্টারভিউ, নেগোসিয়েশন ও পেশাদার যোগাযোগ" else "Job interviews, negotiations, and workplace communication",
                icon = "💼",
                lessons = listOf(
                    Lesson(
                        id = "lesson_5_1_interview",
                        unitId = "unit_5_career",
                        level = CEFRLevel.C1,
                        title = if (isBengaliSource) "Lesson 1: চাকরির ইন্টারভিউ (Job Interview)" else "Lesson 1: Job Interview",
                        description = if (isBengaliSource) "যোগ্যতা, অভিজ্ঞতা ও শক্তির দিক তুলে ধরা" else "Highlight your skills, experience, and strengths",
                        icon = "🎯",
                        exercises = listOf(
                            Exercise(
                                id = "ex_c1",
                                type = ExerciseType.SPEAKING,
                                prompt = if (isBengaliSource) "ইন্টারভিউতে নিজের পরিচয় স্পষ্টভাবে বলুন:" else "Deliver this professional statement:",
                                targetPhrase = "I have five years of experience in software development",
                                audioToPlay = "I have five years of experience in software development",
                                correctAnswer = "I have five years of experience in software development"
                            ),
                            Exercise(
                                id = "ex_c2",
                                type = ExerciseType.FILL_IN_THE_BLANK,
                                prompt = if (isBengaliSource) "পেশাদার বাক্যের শূন্যস্থান পূরণ করুন:" else "Select the professional term:",
                                targetPhrase = "I look forward to ___ with your exceptional team.",
                                options = listOf("collaborating", "collaborate", "collaborated", "collaboration"),
                                correctAnswer = "collaborating",
                                explanation = "'Look forward to' এর পরে verb এর সাথে -ing যুক্ত হয়।"
                            )
                        )
                    )
                )
            )
        )
    }
}
