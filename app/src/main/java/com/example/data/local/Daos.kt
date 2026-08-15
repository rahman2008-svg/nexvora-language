package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface UserProfileDao {
    @Query("SELECT * FROM user_profile WHERE id = 1")
    fun getUserProfile(): Flow<UserProfileEntity?>

    @Query("SELECT * FROM user_profile WHERE id = 1")
    suspend fun getUserProfileOnce(): UserProfileEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdate(profile: UserProfileEntity)

    @Update
    suspend fun update(profile: UserProfileEntity)
}

@Dao
interface LessonProgressDao {
    @Query("SELECT * FROM lesson_progress WHERE targetLang = :targetLang")
    fun getProgressForLanguage(targetLang: String): Flow<List<LessonProgressEntity>>

    @Query("SELECT * FROM lesson_progress WHERE lessonId = :lessonId LIMIT 1")
    suspend fun getProgressForLesson(lessonId: String): LessonProgressEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdate(progress: LessonProgressEntity)

    @Query("SELECT COUNT(*) FROM lesson_progress WHERE isCompleted = 1")
    fun getCompletedLessonCount(): Flow<Int>
}

@Dao
interface VocabularyDao {
    @Query("SELECT * FROM vocabulary_bank WHERE targetLang = :targetLang ORDER BY isFavorite DESC, word ASC")
    fun getAllVocabulary(targetLang: String): Flow<List<VocabularyEntity>>

    @Query("SELECT * FROM vocabulary_bank WHERE targetLang = :targetLang AND (errorCount > 0 OR nextReviewTimestamp <= :currentTime) ORDER BY nextReviewTimestamp ASC")
    fun getReviewQueue(targetLang: String, currentTime: Long): Flow<List<VocabularyEntity>>

    @Query("SELECT * FROM vocabulary_bank WHERE targetLang = :targetLang AND isFavorite = 1")
    fun getFavoriteVocabulary(targetLang: String): Flow<List<VocabularyEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertVocabularyList(items: List<VocabularyEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertVocabulary(item: VocabularyEntity)

    @Update
    suspend fun updateVocabulary(item: VocabularyEntity)

    @Query("UPDATE vocabulary_bank SET isFavorite = :isFavorite WHERE id = :id")
    suspend fun setFavorite(id: Long, isFavorite: Boolean)

    @Query("SELECT COUNT(*) FROM vocabulary_bank WHERE targetLang = :targetLang")
    fun getVocabularyCount(targetLang: String): Flow<Int>
}

@Dao
interface AchievementDao {
    @Query("SELECT * FROM achievements ORDER BY isUnlocked DESC, id ASC")
    fun getAllAchievements(): Flow<List<AchievementEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAchievements(achievements: List<AchievementEntity>)

    @Update
    suspend fun updateAchievement(achievement: AchievementEntity)

    @Query("UPDATE achievements SET isUnlocked = 1, unlockedAt = :timestamp WHERE id = :id")
    suspend fun unlockAchievement(id: String, timestamp: Long)

    @Query("UPDATE achievements SET currentProgress = :progress WHERE id = :id")
    suspend fun updateProgress(id: String, progress: Int)
}

@Dao
interface StreakDao {
    @Query("SELECT * FROM streak_days ORDER BY dateString DESC LIMIT 7")
    fun getRecentStreaks(): Flow<List<StreakDayEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun recordStreakDay(day: StreakDayEntity)
}
