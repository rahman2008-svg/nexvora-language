package com.example.data.repository

import com.example.data.content.GrammarContentProvider
import com.example.data.content.LanguageContentProvider
import com.example.data.local.*
import com.example.data.model.GrammarRule
import com.example.data.model.Lesson
import com.example.data.model.UnitData
import kotlinx.coroutines.flow.Flow
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class LanguageRepository(private val database: AppDatabase) {
    private val userProfileDao = database.userProfileDao()
    private val lessonProgressDao = database.lessonProgressDao()
    private val vocabularyDao = database.vocabularyDao()
    private val achievementDao = database.achievementDao()
    private val streakDao = database.streakDao()

    val userProfile: Flow<UserProfileEntity?> = userProfileDao.getUserProfile()
    val allAchievements: Flow<List<AchievementEntity>> = achievementDao.getAllAchievements()
    val recentStreaks: Flow<List<StreakDayEntity>> = streakDao.getRecentStreaks()

    fun getVocabulary(targetLang: String): Flow<List<VocabularyEntity>> =
        vocabularyDao.getAllVocabulary(targetLang)

    fun getReviewQueue(targetLang: String): Flow<List<VocabularyEntity>> =
        vocabularyDao.getReviewQueue(targetLang, System.currentTimeMillis())

    fun getLessonProgress(targetLang: String): Flow<List<LessonProgressEntity>> =
        lessonProgressDao.getProgressForLanguage(targetLang)

    suspend fun initializeDefaultsIfNeeded() {
        var profile = userProfileDao.getUserProfileOnce()
        if (profile == null) {
            profile = UserProfileEntity(
                id = 1,
                name = "Prince AR",
                sourceLanguageCode = "bn",
                targetLanguageCode = "en",
                totalXp = 120,
                level = 2,
                streakDays = 7,
                hearts = 5,
                dailyGoalMinutes = 15,
                dailyProgressMinutes = 8,
                completedLessonsCount = 2,
                learnedWordsCount = 18
            )
            userProfileDao.insertOrUpdate(profile)

            // Pre-seed achievements
            achievementDao.insertAchievements(LanguageContentProvider.getDefaultAchievements())

            // Pre-seed vocabulary for target language
            val initialVocab = LanguageContentProvider.getInitialVocabulary(profile.targetLanguageCode, profile.sourceLanguageCode)
            vocabularyDao.insertVocabularyList(initialVocab)

            // Seed sample streak days for rich 7-day calendar UI
            val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val dayFormat = SimpleDateFormat("EEE", Locale.getDefault())
            val today = System.currentTimeMillis()
            val dayMillis = 24 * 60 * 60 * 1000L

            for (i in 6 downTo 0) {
                val d = Date(today - (i * dayMillis))
                streakDao.recordStreakDay(
                    StreakDayEntity(
                        dateString = dateFormat.format(d),
                        dayOfWeek = dayFormat.format(d),
                        completed = true,
                        xpEarned = 35 + (i * 10)
                    )
                )
            }
        }
    }

    suspend fun switchLanguagePair(sourceCode: String, targetCode: String) {
        val current = userProfileDao.getUserProfileOnce() ?: UserProfileEntity()
        val updated = current.copy(
            sourceLanguageCode = sourceCode,
            targetLanguageCode = targetCode
        )
        userProfileDao.update(updated)

        // Seed target vocab if empty
        val newVocab = LanguageContentProvider.getInitialVocabulary(targetCode, sourceCode)
        vocabularyDao.insertVocabularyList(newVocab)
    }

    suspend fun awardXp(amount: Int, minutesSpent: Int = 1) {
        val current = userProfileDao.getUserProfileOnce() ?: return
        val newXp = current.totalXp + amount
        val newLevel = calculateLevel(newXp)
        val newDailyProgress = (current.dailyProgressMinutes + minutesSpent).coerceAtMost(current.dailyGoalMinutes * 2)

        val updated = current.copy(
            totalXp = newXp,
            level = newLevel,
            dailyProgressMinutes = newDailyProgress,
            lastActiveTimestamp = System.currentTimeMillis()
        )
        userProfileDao.update(updated)

        // Check XP & level achievements
        if (newXp >= 1000) {
            achievementDao.unlockAchievement("xp_1000", System.currentTimeMillis())
        }
    }

    suspend fun deductHeart(): Boolean {
        val current = userProfileDao.getUserProfileOnce() ?: return false
        val newHearts = (current.hearts - 1).coerceAtLeast(0)
        userProfileDao.update(current.copy(hearts = newHearts))
        return newHearts > 0
    }

    suspend fun refillHearts() {
        val current = userProfileDao.getUserProfileOnce() ?: return
        userProfileDao.update(current.copy(hearts = current.maxHearts))
    }

    suspend fun completeLesson(lessonId: String, scorePercentage: Int, stars: Int, perfectLesson: Boolean) {
        val current = userProfileDao.getUserProfileOnce() ?: return
        val progress = LessonProgressEntity(
            lessonId = lessonId,
            targetLang = current.targetLanguageCode,
            isCompleted = true,
            stars = stars,
            scorePercentage = scorePercentage,
            completedAt = System.currentTimeMillis()
        )
        lessonProgressDao.insertOrUpdate(progress)

        val updatedLessonsCount = current.completedLessonsCount + 1
        val updatedWordsCount = current.learnedWordsCount + 5
        userProfileDao.update(
            current.copy(
                completedLessonsCount = updatedLessonsCount,
                learnedWordsCount = updatedWordsCount
            )
        )

        // Check achievements
        achievementDao.unlockAchievement("first_lesson", System.currentTimeMillis())
        if (perfectLesson) {
            achievementDao.unlockAchievement("perfect_lesson", System.currentTimeMillis())
        }
        if (updatedWordsCount >= 50) {
            achievementDao.unlockAchievement("words_50", System.currentTimeMillis())
        }
    }

    suspend fun toggleFavoriteWord(id: Long, isFavorite: Boolean) {
        vocabularyDao.setFavorite(id, isFavorite)
    }

    suspend fun recordWordReviewResult(item: VocabularyEntity, isCorrect: Boolean) {
        val updated = if (isCorrect) {
            item.copy(
                reviewCount = item.reviewCount + 1,
                errorCount = (item.errorCount - 1).coerceAtLeast(0),
                isMastered = item.reviewCount >= 2,
                nextReviewTimestamp = System.currentTimeMillis() + (24 * 60 * 60 * 1000L * (item.reviewCount + 1))
            )
        } else {
            item.copy(
                errorCount = item.errorCount + 1,
                isMastered = false,
                nextReviewTimestamp = System.currentTimeMillis() // Due immediately
            )
        }
        vocabularyDao.updateVocabulary(updated)
    }

    suspend fun updateDailyGoal(minutes: Int) {
        val current = userProfileDao.getUserProfileOnce() ?: return
        userProfileDao.update(current.copy(dailyGoalMinutes = minutes))
    }

    fun getCurriculum(targetLang: String, sourceLang: String): List<UnitData> {
        return LanguageContentProvider.getCourseCurriculum(targetLang, sourceLang)
    }

    fun getGrammarRules(sourceLang: String): List<GrammarRule> {
        return GrammarContentProvider.getGrammarRules(sourceLang)
    }

    private fun calculateLevel(xp: Int): Int {
        // Level curve: Level 1 (0-100), Level 2 (101-250), Level 3 (251-450)...
        return when {
            xp < 100 -> 1
            xp < 250 -> 2
            xp < 450 -> 3
            xp < 700 -> 4
            xp < 1000 -> 5
            xp < 1400 -> 6
            xp < 1900 -> 7
            xp < 2500 -> 8
            xp < 3200 -> 9
            xp < 4000 -> 10
            else -> 10 + (xp - 4000) / 1000
        }
    }
}
