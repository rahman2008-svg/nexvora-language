package com.example.data.model

enum class SupportedLanguage(
    val code: String,
    val displayName: String,
    val nativeName: String,
    val flag: String,
    val greeting: String,
    val localeTag: String
) {
    ENGLISH("en", "English", "English", "🇬🇧", "Hello!", "en-US"),
    BENGALI("bn", "Bengali", "বাংলা", "🇧🇩", "হ্যালো / নমস্কার!", "bn-BD"),
    HINDI("hi", "Hindi", "हिन्दी", "🇮🇳", "नमस्ते!", "hi-IN"),
    ARABIC("ar", "Arabic", "العربية", "🇸🇦", "مرحباً!", "ar-SA"),
    SPANISH("es", "Spanish", "Español", "🇪🇸", "¡Hola!", "es-ES"),
    FRENCH("fr", "French", "Français", "🇫🇷", "Bonjour!", "fr-FR"),
    GERMAN("de", "German", "Deutsch", "🇩🇪", "Hallo!", "de-DE"),
    JAPANESE("ja", "Japanese", "日本語", "🇯🇵", "こんにちは!", "ja-JP"),
    KOREAN("ko", "Korean", "한국어", "🇰🇷", "안녕하세요!", "ko-KR"),
    CHINESE("zh", "Chinese", "中文", "🇨🇳", "你好!", "zh-CN"),
    ITALIAN("it", "Italian", "Italiano", "🇮🇹", "Ciao!", "it-IT"),
    RUSSIAN("ru", "Russian", "Русский", "🇷🇺", "Привет!", "ru-RU"),
    TURKISH("tr", "Turkish", "Türkçe", "🇹🇷", "Merhaba!", "tr-TR"),
    PORTUGUESE("pt", "Portuguese", "Português", "🇵🇹", "Olá!", "pt-PT");

    companion object {
        fun fromCode(code: String): SupportedLanguage {
            return entries.firstOrNull { it.code.equals(code, ignoreCase = true) } ?: ENGLISH
        }
    }
}

enum class CEFRLevel(
    val code: String,
    val title: String,
    val subtitle: String,
    val requiredLessons: Int,
    val colorHex: Long
) {
    A1("A1", "Beginner", "Foundational words & greetings", 0, 0xFF10B981),
    A2("A2", "Elementary", "Daily routines & simple phrases", 8, 0xFF06B6D4),
    B1("B1", "Intermediate", "Conversations & travel fluency", 16, 0xFF4F46E5),
    B2("B2", "Upper Intermediate", "Complex topics & work situations", 24, 0xFF8B5CF6),
    C1("C1", "Advanced", "Fluent expression & idiomatic usage", 32, 0xFFF59E0B),
    C2("C2", "Mastery", "Near-native precision & nuance", 40, 0xFFEF4444)
}

enum class ExerciseType(val label: String, val icon: String, val xpReward: Int) {
    MULTIPLE_CHOICE("Multiple Choice", "📝", 10),
    WORD_MATCHING("Word Matching", "🧩", 15),
    ARRANGE_SENTENCE("Arrange Sentence", "🔤", 12),
    FILL_IN_THE_BLANK("Fill in the Blank", "✏️", 10),
    LISTENING("Listening Lab", "🎧", 15),
    SPEAKING("Speaking Studio", "🗣️", 20),
    TRANSLATION("Translation", "🌐", 12),
    WORD_BUILDER("Word Builder", "🏗️", 12),
    PICTURE_LEARNING("Visual Card", "🖼️", 10),
    CONVERSATION("Conversation", "👥", 20)
}

data class MatchingPair(
    val left: String,
    val right: String
)

data class DialogueTurn(
    val speaker: String, // "AI" or "User"
    val avatar: String,
    val text: String,
    val translation: String,
    val audioText: String? = null
)

data class Exercise(
    val id: String,
    val type: ExerciseType,
    val prompt: String, // Question / instruction in source language
    val targetPhrase: String? = null, // The key word/phrase in target language
    val pronunciation: String? = null,
    val audioToPlay: String? = null, // Text for TTS
    val visualEmoji: String? = null,
    val options: List<String> = emptyList(), // For MCQ, Fill Blank, Listening
    val correctAnswer: String = "", // Exact string answer
    val matchingPairs: List<MatchingPair> = emptyList(), // For Word Matching
    val scrambledWords: List<String> = emptyList(), // For Arrange / Word Builder
    val dialogueTurns: List<DialogueTurn> = emptyList(), // For Conversation
    val explanation: String? = null
)

data class Lesson(
    val id: String,
    val unitId: String,
    val level: CEFRLevel,
    val title: String,
    val description: String,
    val icon: String,
    val estimatedMinutes: Int = 4,
    val exercises: List<Exercise> = emptyList()
)

data class UnitData(
    val id: String,
    val level: CEFRLevel,
    val unitNumber: Int,
    val title: String,
    val description: String,
    val icon: String,
    val lessons: List<Lesson>
)

data class GrammarRule(
    val id: String,
    val title: String,
    val category: String, // Noun, Pronoun, Verb, Tense, Preposition, etc.
    val summary: String,
    val formulaOrStructure: String,
    val explanation: String,
    val examples: List<GrammarExample>,
    val quizQuestion: String,
    val quizOptions: List<String>,
    val quizCorrectAnswer: String,
    val quizExplanation: String
)

data class GrammarExample(
    val targetSentence: String,
    val translatedSentence: String,
    val highlightedPart: String
)
