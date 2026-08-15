package com.example.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.audio.SpeechRecognitionManager
import com.example.audio.SpeechState
import com.example.audio.TTSManager
import com.example.data.local.*
import com.example.data.model.*
import com.example.data.repository.LanguageRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class LessonSessionState(
    val lesson: Lesson? = null,
    val currentExerciseIndex: Int = 0,
    val totalExercises: Int = 0,
    val heartsRemaining: Int = 5,
    val earnedXpInSession: Int = 0,
    val correctAnswersCount: Int = 0,
    val isCompleted: Boolean = false,
    val selectedOption: String? = null,
    val matchedPairs: Map<String, String> = emptyMap(),
    val selectedLeftPair: String? = null,
    val selectedRightPair: String? = null,
    val arrangedWords: List<String> = emptyList(),
    val availableWords: List<String> = emptyList(),
    val dialogueProgressIndex: Int = 0,
    val feedbackState: FeedbackState? = null,
    val speechEvaluationScore: Int? = null,
    val speechEvaluationText: String? = null
)

sealed class FeedbackState {
    data class Correct(val explanation: String?, val xpAwarded: Int = 10) : FeedbackState()
    data class Incorrect(val correctAnswer: String, val explanation: String?) : FeedbackState()
}

class NexVoraViewModel(application: Application) : AndroidViewModel(application) {
    private val database = AppDatabase.getDatabase(application)
    val repository = LanguageRepository(database)

    val ttsManager = TTSManager(application)
    val speechManager = SpeechRecognitionManager(application)

    // User Profile
    val userProfile: StateFlow<UserProfileEntity> = repository.userProfile
        .filterNotNull()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = UserProfileEntity()
        )

    // Achievements
    val achievements: StateFlow<List<AchievementEntity>> = repository.allAchievements
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    // Recent 7 days streak
    val streakHistory: StateFlow<List<StreakDayEntity>> = repository.recentStreaks
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    // Target language vocabulary
    private val _vocabSearchQuery = MutableStateFlow("")
    val vocabSearchQuery: StateFlow<String> = _vocabSearchQuery

    private val _selectedVocabCategory = MutableStateFlow<String?>(null)
    val selectedVocabCategory: StateFlow<String?> = _selectedVocabCategory

    private val _onlyFavoritesFilter = MutableStateFlow(false)
    val onlyFavoritesFilter: StateFlow<Boolean> = _onlyFavoritesFilter

    val allVocabulary: StateFlow<List<VocabularyEntity>> = userProfile
        .flatMapLatest { profile -> repository.getVocabulary(profile.targetLanguageCode) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val reviewQueue: StateFlow<List<VocabularyEntity>> = userProfile
        .flatMapLatest { profile -> repository.getReviewQueue(profile.targetLanguageCode) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val completedLessons: StateFlow<List<LessonProgressEntity>> = userProfile
        .flatMapLatest { profile -> repository.getLessonProgress(profile.targetLanguageCode) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Active Lesson Flow State
    private val _lessonSession = MutableStateFlow<LessonSessionState?>(null)
    val lessonSession: StateFlow<LessonSessionState?> = _lessonSession

    // Flashcard Flip Mode State for Practice
    private val _flashcardIndex = MutableStateFlow(0)
    val flashcardIndex: StateFlow<Int> = _flashcardIndex

    private val _isCardFlipped = MutableStateFlow(false)
    val isCardFlipped: StateFlow<Boolean> = _isCardFlipped

    init {
        viewModelScope.launch {
            repository.initializeDefaultsIfNeeded()
        }

        // Observe speech recognition results
        viewModelScope.launch {
            speechManager.speechState.collect { state ->
                if (state is SpeechState.Success && _lessonSession.value != null) {
                    val currentEx = _lessonSession.value?.lesson?.exercises?.getOrNull(_lessonSession.value!!.currentExerciseIndex)
                    if (currentEx?.type == ExerciseType.SPEAKING) {
                        val score = speechManager.evaluateSpeech(state.spokenText, currentEx.correctAnswer.ifEmpty { currentEx.targetPhrase ?: "" })
                        _lessonSession.update {
                            it?.copy(
                                speechEvaluationScore = score,
                                speechEvaluationText = state.spokenText
                            )
                        }
                    }
                }
            }
        }
    }

    fun startLesson(lesson: Lesson) {
        val firstEx = lesson.exercises.firstOrNull()
        val initialAvailable = when (firstEx?.type) {
            ExerciseType.ARRANGE_SENTENCE, ExerciseType.WORD_BUILDER -> firstEx.scrambledWords.shuffled()
            else -> emptyList()
        }

        _lessonSession.value = LessonSessionState(
            lesson = lesson,
            currentExerciseIndex = 0,
            totalExercises = lesson.exercises.size,
            heartsRemaining = userProfile.value.hearts,
            availableWords = initialAvailable
        )

        // Play initial audio if available
        firstEx?.audioToPlay?.let {
            playAudio(it, userProfile.value.targetLanguageCode)
        }
    }

    fun selectOption(option: String) {
        _lessonSession.update { it?.copy(selectedOption = option) }
    }

    fun selectLeftPair(left: String) {
        _lessonSession.update { state ->
            if (state == null) return@update null
            if (state.selectedRightPair != null) {
                // Check if pair matches
                val currentEx = state.lesson?.exercises?.getOrNull(state.currentExerciseIndex)
                val isMatch = currentEx?.matchingPairs?.any { it.left == left && it.right == state.selectedRightPair } == true
                if (isMatch) {
                    val newPairs = state.matchedPairs.toMutableMap().apply { put(left, state.selectedRightPair!!) }
                    state.copy(
                        matchedPairs = newPairs,
                        selectedLeftPair = null,
                        selectedRightPair = null
                    )
                } else {
                    state.copy(selectedLeftPair = left)
                }
            } else {
                state.copy(selectedLeftPair = left)
            }
        }
    }

    fun selectRightPair(right: String) {
        _lessonSession.update { state ->
            if (state == null) return@update null
            if (state.selectedLeftPair != null) {
                val currentEx = state.lesson?.exercises?.getOrNull(state.currentExerciseIndex)
                val isMatch = currentEx?.matchingPairs?.any { it.left == state.selectedLeftPair && it.right == right } == true
                if (isMatch) {
                    val newPairs = state.matchedPairs.toMutableMap().apply { put(state.selectedLeftPair!!, right) }
                    state.copy(
                        matchedPairs = newPairs,
                        selectedLeftPair = null,
                        selectedRightPair = null
                    )
                } else {
                    state.copy(selectedRightPair = right)
                }
            } else {
                state.copy(selectedRightPair = right)
            }
        }
    }

    fun pickArrangedWord(word: String) {
        _lessonSession.update { state ->
            if (state == null) return@update null
            val newArranged = state.arrangedWords + word
            val newAvailable = state.availableWords.toMutableList().apply { remove(word) }
            state.copy(arrangedWords = newArranged, availableWords = newAvailable)
        }
    }

    fun removeArrangedWord(word: String) {
        _lessonSession.update { state ->
            if (state == null) return@update null
            val newArranged = state.arrangedWords.toMutableList().apply { remove(word) }
            val newAvailable = state.availableWords + word
            state.copy(arrangedWords = newArranged, availableWords = newAvailable)
        }
    }

    fun advanceDialogue() {
        _lessonSession.update { state ->
            if (state == null) return@update null
            val nextIdx = state.dialogueProgressIndex + 1
            state.copy(dialogueProgressIndex = nextIdx)
        }
    }

    fun checkAnswer() {
        val state = _lessonSession.value ?: return
        val currentEx = state.lesson?.exercises?.getOrNull(state.currentExerciseIndex) ?: return

        var isCorrect = false

        when (currentEx.type) {
            ExerciseType.MULTIPLE_CHOICE,
            ExerciseType.FILL_IN_THE_BLANK,
            ExerciseType.LISTENING,
            ExerciseType.TRANSLATION,
            ExerciseType.PICTURE_LEARNING,
            ExerciseType.CONVERSATION -> {
                isCorrect = state.selectedOption == currentEx.correctAnswer
            }
            ExerciseType.WORD_MATCHING -> {
                isCorrect = state.matchedPairs.size == currentEx.matchingPairs.size
            }
            ExerciseType.ARRANGE_SENTENCE,
            ExerciseType.WORD_BUILDER -> {
                val formed = state.arrangedWords.joinToString(" ").trim()
                isCorrect = formed.equals(currentEx.correctAnswer.trim(), ignoreCase = true)
            }
            ExerciseType.SPEAKING -> {
                val score = state.speechEvaluationScore ?: 0
                isCorrect = score >= 60 || state.selectedOption == "VERIFIED"
            }
        }

        viewModelScope.launch {
            if (isCorrect) {
                val xp = currentEx.type.xpReward
                repository.awardXp(xp)
                _lessonSession.update {
                    it?.copy(
                        feedbackState = FeedbackState.Correct(currentEx.explanation, xp),
                        earnedXpInSession = (it.earnedXpInSession) + xp,
                        correctAnswersCount = (it.correctAnswersCount) + 1
                    )
                }
            } else {
                val hasHearts = repository.deductHeart()
                _lessonSession.update {
                    it?.copy(
                        feedbackState = FeedbackState.Incorrect(
                            correctAnswer = currentEx.correctAnswer.ifEmpty { currentEx.targetPhrase ?: "" },
                            explanation = currentEx.explanation
                        ),
                        heartsRemaining = (it.heartsRemaining - 1).coerceAtLeast(0)
                    )
                }
            }
        }
    }

    fun nextExercise() {
        val state = _lessonSession.value ?: return
        val nextIdx = state.currentExerciseIndex + 1

        if (nextIdx >= state.totalExercises) {
            // Complete Lesson
            viewModelScope.launch {
                val isPerfect = state.heartsRemaining == userProfile.value.maxHearts
                val scorePercent = ((state.correctAnswersCount.toFloat() / state.totalExercises.toFloat()) * 100).toInt()
                val stars = when {
                    scorePercent >= 90 -> 3
                    scorePercent >= 60 -> 2
                    else -> 1
                }
                state.lesson?.let { l ->
                    repository.completeLesson(l.id, scorePercent, stars, isPerfect)
                    repository.awardXp(20) // Completion bonus
                }
                _lessonSession.update { it?.copy(isCompleted = true, feedbackState = null) }
            }
        } else {
            val nextEx = state.lesson?.exercises?.getOrNull(nextIdx)
            val initialAvailable = when (nextEx?.type) {
                ExerciseType.ARRANGE_SENTENCE, ExerciseType.WORD_BUILDER -> nextEx.scrambledWords.shuffled()
                else -> emptyList()
            }

            _lessonSession.value = state.copy(
                currentExerciseIndex = nextIdx,
                selectedOption = null,
                matchedPairs = emptyMap(),
                selectedLeftPair = null,
                selectedRightPair = null,
                arrangedWords = emptyList(),
                availableWords = initialAvailable,
                dialogueProgressIndex = 0,
                feedbackState = null,
                speechEvaluationScore = null,
                speechEvaluationText = null
            )

            speechManager.reset()

            // Auto-play audio if appropriate
            nextEx?.audioToPlay?.let {
                playAudio(it, userProfile.value.targetLanguageCode)
            }
        }
    }

    fun exitLesson() {
        speechManager.reset()
        ttsManager.stop()
        _lessonSession.value = null
    }

    fun playAudio(text: String, langCode: String? = null) {
        val code = langCode ?: userProfile.value.targetLanguageCode
        ttsManager.speak(text, code)
    }

    fun startVoiceRecognition(expectedPhrase: String) {
        speechManager.startListening(expectedPhrase, userProfile.value.targetLanguageCode)
    }

    fun stopVoiceRecognition() {
        speechManager.stopListening()
    }

    fun switchLanguagePair(source: String, target: String) {
        viewModelScope.launch {
            repository.switchLanguagePair(source, target)
        }
    }

    fun toggleFavorite(vocabId: Long, currentFav: Boolean) {
        viewModelScope.launch {
            repository.toggleFavoriteWord(vocabId, !currentFav)
        }
    }

    fun setVocabSearch(query: String) {
        _vocabSearchQuery.value = query
    }

    fun setVocabCategory(cat: String?) {
        _selectedVocabCategory.value = cat
    }

    fun toggleFavoritesOnly() {
        _onlyFavoritesFilter.value = !_onlyFavoritesFilter.value
    }

    fun refillHearts() {
        viewModelScope.launch {
            repository.refillHearts()
        }
    }

    fun updateDailyGoal(mins: Int) {
        viewModelScope.launch {
            repository.updateDailyGoal(mins)
        }
    }

    fun nextFlashcard(total: Int) {
        _isCardFlipped.value = false
        _flashcardIndex.update { (it + 1) % total.coerceAtLeast(1) }
    }

    fun previousFlashcard(total: Int) {
        _isCardFlipped.value = false
        _flashcardIndex.update { if (it > 0) it - 1 else (total - 1).coerceAtLeast(0) }
    }

    fun flipCard() {
        _isCardFlipped.update { !it }
    }

    fun recordReviewResult(item: VocabularyEntity, isCorrect: Boolean) {
        viewModelScope.launch {
            repository.recordWordReviewResult(item, isCorrect)
            if (isCorrect) repository.awardXp(15)
        }
    }

    override fun onCleared() {
        super.onCleared()
        ttsManager.shutdown()
        speechManager.destroy()
    }
}
