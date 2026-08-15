package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.audio.SpeechState
import com.example.data.model.ExerciseType
import com.example.data.model.Lesson
import com.example.ui.components.AppTopBar
import com.example.ui.components.HeartRefillDialog
import com.example.ui.components.LanguagePairDialog
import com.example.ui.screens.*
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.theme.PrimaryIndigo
import com.example.viewmodel.NexVoraViewModel

sealed class AppNavTab(
    val route: String,
    val title: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector
) {
    data object Learn : AppNavTab("learn", "Learn", Icons.Filled.School, Icons.Outlined.School)
    data object Courses : AppNavTab("courses", "Courses", Icons.Filled.AutoStories, Icons.Outlined.AutoStories)
    data object Practice : AppNavTab("practice", "Practice", Icons.Filled.Psychology, Icons.Outlined.Psychology)
    data object Vocabulary : AppNavTab("vocabulary", "Vocab", Icons.Filled.Translate, Icons.Outlined.Translate)
    data object Profile : AppNavTab("profile", "Profile", Icons.Filled.Person, Icons.Outlined.Person)
}

class MainActivity : ComponentActivity() {

    private val viewModel: NexVoraViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                MainApp(viewModel = viewModel)
            }
        }
    }
}

@Composable
fun MainApp(viewModel: NexVoraViewModel) {
    val userProfile by viewModel.userProfile.collectAsStateWithLifecycle()
    val achievements by viewModel.achievements.collectAsStateWithLifecycle()
    val streakHistory by viewModel.streakHistory.collectAsStateWithLifecycle()
    val allVocabulary by viewModel.allVocabulary.collectAsStateWithLifecycle()
    val reviewQueue by viewModel.reviewQueue.collectAsStateWithLifecycle()
    val completedLessons by viewModel.completedLessons.collectAsStateWithLifecycle()

    val lessonSession by viewModel.lessonSession.collectAsStateWithLifecycle()
    val speechState by viewModel.speechManager.speechState.collectAsStateWithLifecycle()

    val vocabSearchQuery by viewModel.vocabSearchQuery.collectAsStateWithLifecycle()
    val selectedVocabCategory by viewModel.selectedVocabCategory.collectAsStateWithLifecycle()
    val onlyFavoritesFilter by viewModel.onlyFavoritesFilter.collectAsStateWithLifecycle()

    val flashcardIndex by viewModel.flashcardIndex.collectAsStateWithLifecycle()
    val isCardFlipped by viewModel.isCardFlipped.collectAsStateWithLifecycle()

    var currentTab by remember { mutableStateOf<AppNavTab>(AppNavTab.Learn) }
    var showLanguageDialog by remember { mutableStateOf(false) }
    var showHeartsDialog by remember { mutableStateOf(false) }

    val curriculum = remember(userProfile.targetLanguageCode, userProfile.sourceLanguageCode) {
        viewModel.repository.getCurriculum(userProfile.targetLanguageCode, userProfile.sourceLanguageCode)
    }

    val grammarRules = remember(userProfile.sourceLanguageCode) {
        viewModel.repository.getGrammarRules(userProfile.sourceLanguageCode)
    }

    // Fullscreen Active Lesson View if active
    if (lessonSession != null) {
        LessonExerciseScreen(
            sessionState = lessonSession!!,
            speechState = speechState,
            onOptionSelected = { viewModel.selectOption(it) },
            onLeftPairSelected = { viewModel.selectLeftPair(it) },
            onRightPairSelected = { viewModel.selectRightPair(it) },
            onPickArrangedWord = { viewModel.pickArrangedWord(it) },
            onRemoveArrangedWord = { viewModel.removeArrangedWord(it) },
            onPlayAudio = { text -> viewModel.playAudio(text) },
            onStartSpeech = { expected -> viewModel.startVoiceRecognition(expected) },
            onStopSpeech = { viewModel.stopVoiceRecognition() },
            onCheckAnswer = { viewModel.checkAnswer() },
            onNextExercise = { viewModel.nextExercise() },
            onExitLesson = { viewModel.exitLesson() }
        )
        return
    }

    // Main App Shell with Top Bar & Bottom Navigation
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            AppTopBar(
                userProfile = userProfile,
                onLanguageClick = { showLanguageDialog = true },
                onHeartsClick = { showHeartsDialog = true }
            )
        },
        bottomBar = {
            NavigationBar(
                containerColor = MaterialTheme.colorScheme.surface,
                tonalElevation = 8.dp,
                modifier = Modifier.testTag("main_bottom_nav")
            ) {
                val tabs = listOf(
                    AppNavTab.Learn,
                    AppNavTab.Courses,
                    AppNavTab.Practice,
                    AppNavTab.Vocabulary,
                    AppNavTab.Profile
                )

                tabs.forEach { tab ->
                    val selected = currentTab == tab
                    NavigationBarItem(
                        selected = selected,
                        onClick = { currentTab = tab },
                        icon = {
                            Icon(
                                imageVector = if (selected) tab.selectedIcon else tab.unselectedIcon,
                                contentDescription = tab.title
                            )
                        },
                        label = {
                            Text(
                                text = tab.title,
                                fontSize = 11.sp,
                                fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal
                            )
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = MaterialTheme.colorScheme.primary,
                            selectedTextColor = MaterialTheme.colorScheme.primary,
                            indicatorColor = MaterialTheme.colorScheme.primaryContainer,
                            unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                            unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                        )
                    )
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (currentTab) {
                AppNavTab.Learn -> {
                    HomeScreen(
                        userProfile = userProfile,
                        curriculum = curriculum,
                        completedLessons = completedLessons,
                        onStartLesson = { lesson -> viewModel.startLesson(lesson) },
                        onOpenLanguageSelector = { showLanguageDialog = true },
                        onOpenHeartsDialog = { showHeartsDialog = true }
                    )
                }

                AppNavTab.Courses -> {
                    CoursesGrammarScreen(
                        userProfile = userProfile,
                        curriculum = curriculum,
                        grammarRules = grammarRules,
                        onStartLesson = { lesson -> viewModel.startLesson(lesson) },
                        onPlayAudio = { text -> viewModel.playAudio(text) }
                    )
                }

                AppNavTab.Practice -> {
                    PracticeReviewScreen(
                        vocabularyList = allVocabulary,
                        reviewQueue = reviewQueue,
                        flashcardIndex = flashcardIndex,
                        isCardFlipped = isCardFlipped,
                        onFlipCard = { viewModel.flipCard() },
                        onNextCard = { total -> viewModel.nextFlashcard(total) },
                        onPrevCard = { total -> viewModel.previousFlashcard(total) },
                        onRecordReviewResult = { item, isCorrect -> viewModel.recordReviewResult(item, isCorrect) },
                        onPlayAudio = { text -> viewModel.playAudio(text) },
                        onStartSpecificPractice = { mode ->
                            // Find a lesson containing this exercise type or start the first lesson
                            val matchingLesson = curriculum.flatMap { it.lessons }
                                .firstOrNull { l -> l.exercises.any { it.type == mode } }
                                ?: curriculum.firstOrNull()?.lessons?.firstOrNull()

                            matchingLesson?.let { l ->
                                viewModel.startLesson(l)
                            }
                        }
                    )
                }

                AppNavTab.Vocabulary -> {
                    VocabularyScreen(
                        vocabularyList = allVocabulary,
                        searchQuery = vocabSearchQuery,
                        selectedCategory = selectedVocabCategory,
                        onlyFavorites = onlyFavoritesFilter,
                        onSearchChange = { viewModel.setVocabSearch(it) },
                        onCategorySelect = { viewModel.setVocabCategory(it) },
                        onToggleFavoritesOnly = { viewModel.toggleFavoritesOnly() },
                        onToggleFavorite = { id, isFav -> viewModel.toggleFavorite(id, isFav) },
                        onPlayAudio = { text -> viewModel.playAudio(text) }
                    )
                }

                AppNavTab.Profile -> {
                    ProfileScreen(
                        userProfile = userProfile,
                        achievements = achievements,
                        streakHistory = streakHistory,
                        onOpenLanguageSelector = { showLanguageDialog = true },
                        onOpenHeartsDialog = { showHeartsDialog = true },
                        onUpdateDailyGoal = { mins -> viewModel.updateDailyGoal(mins) }
                    )
                }
            }
        }
    }

    // Dialogs
    if (showLanguageDialog) {
        LanguagePairDialog(
            currentSource = userProfile.sourceLanguageCode,
            currentTarget = userProfile.targetLanguageCode,
            onConfirmPair = { source, target ->
                viewModel.switchLanguagePair(source, target)
            },
            onDismiss = { showLanguageDialog = false }
        )
    }

    if (showHeartsDialog) {
        HeartRefillDialog(
            currentHearts = userProfile.hearts,
            maxHearts = userProfile.maxHearts,
            onRefill = { viewModel.refillHearts() },
            onDismiss = { showHeartsDialog = false }
        )
    }
}
