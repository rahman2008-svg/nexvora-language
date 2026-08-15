package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_profile")
data class UserProfileEntity(
    @PrimaryKey val id: Int = 1,
    val name: String = "NexVora Learner",
    val sourceLanguageCode: String = "bn", // Default: Bengali speaker
    val targetLanguageCode: String = "en", // Default: Learning English
    val totalXp: Int = 0,
    val level: Int = 1,
    val streakDays: Int = 1,
    val hearts: Int = 5,
    val maxHearts: Int = 5,
    val lastActiveTimestamp: Long = System.currentTimeMillis(),
    val dailyGoalMinutes: Int = 10,
    val dailyProgressMinutes: Int = 2,
    val completedLessonsCount: Int = 0,
    val learnedWordsCount: Int = 0
)

@Entity(tableName = "lesson_progress")
data class LessonProgressEntity(
    @PrimaryKey val lessonId: String,
    val targetLang: String,
    val isCompleted: Boolean = false,
    val stars: Int = 0,
    val scorePercentage: Int = 0,
    val completedAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "vocabulary_bank")
data class VocabularyEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val targetLang: String,
    val sourceLang: String,
    val word: String,
    val translation: String,
    val pronunciation: String,
    val exampleTarget: String,
    val exampleTranslation: String,
    val category: String,
    val difficulty: String, // "Easy", "Medium", "Hard"
    val visualEmoji: String = "📖",
    val isFavorite: Boolean = false,
    val isMastered: Boolean = false,
    val errorCount: Int = 0,
    val reviewCount: Int = 0,
    val nextReviewTimestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "achievements")
data class AchievementEntity(
    @PrimaryKey val id: String,
    val title: String,
    val description: String,
    val icon: String,
    val isUnlocked: Boolean = false,
    val currentProgress: Int = 0,
    val targetGoal: Int = 1,
    val unlockedAt: Long? = null,
    val xpReward: Int = 50
)

@Entity(tableName = "streak_days")
data class StreakDayEntity(
    @PrimaryKey val dateString: String, // Format: YYYY-MM-DD
    val dayOfWeek: String, // Mon, Tue, etc.
    val completed: Boolean = true,
    val xpEarned: Int = 0
)
