package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.audio.SpeechState
import com.example.data.model.*
import com.example.ui.components.*
import com.example.ui.theme.*
import com.example.viewmodel.FeedbackState
import com.example.viewmodel.LessonSessionState

@Composable
fun LessonExerciseScreen(
    sessionState: LessonSessionState,
    speechState: SpeechState,
    onOptionSelected: (String) -> Unit,
    onLeftPairSelected: (String) -> Unit,
    onRightPairSelected: (String) -> Unit,
    onPickArrangedWord: (String) -> Unit,
    onRemoveArrangedWord: (String) -> Unit,
    onPlayAudio: (String) -> Unit,
    onStartSpeech: (String) -> Unit,
    onStopSpeech: () -> Unit,
    onCheckAnswer: () -> Unit,
    onNextExercise: () -> Unit,
    onExitLesson: () -> Unit
) {
    if (sessionState.isCompleted) {
        LessonCompletedView(
            sessionState = sessionState,
            onFinish = onExitLesson
        )
        return
    }

    val currentEx = sessionState.lesson?.exercises?.getOrNull(sessionState.currentExerciseIndex)

    if (currentEx == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
        return
    }

    val progress = (sessionState.currentExerciseIndex.toFloat() / sessionState.totalExercises.toFloat()).coerceIn(0f, 1f)
    val hasAnswered = when (currentEx.type) {
        ExerciseType.MULTIPLE_CHOICE,
        ExerciseType.FILL_IN_THE_BLANK,
        ExerciseType.LISTENING,
        ExerciseType.TRANSLATION,
        ExerciseType.PICTURE_LEARNING,
        ExerciseType.CONVERSATION -> sessionState.selectedOption != null
        ExerciseType.WORD_MATCHING -> sessionState.matchedPairs.isNotEmpty()
        ExerciseType.ARRANGE_SENTENCE,
        ExerciseType.WORD_BUILDER -> sessionState.arrangedWords.isNotEmpty()
        ExerciseType.SPEAKING -> sessionState.speechEvaluationScore != null || sessionState.selectedOption == "VERIFIED"
    }

    Scaffold(
        topBar = {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = MaterialTheme.colorScheme.surface,
                tonalElevation = 2.dp
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .statusBarsPadding()
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    IconButton(onClick = onExitLesson, modifier = Modifier.size(36.dp)) {
                        Icon(Icons.Default.Close, contentDescription = "Exit Lesson")
                    }

                    // Linear Progress Indicator
                    LinearProgressIndicator(
                        progress = { progress },
                        modifier = Modifier
                            .weight(1f)
                            .height(10.dp)
                            .clip(RoundedCornerShape(5.dp)),
                        color = AccentEmerald,
                        trackColor = MaterialTheme.colorScheme.surfaceVariant
                    )

                    // Hearts counter
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(Icons.Default.Favorite, contentDescription = "Hearts", tint = AccentRose, modifier = Modifier.size(20.dp))
                        Text(
                            text = "${sessionState.heartsRemaining}",
                            fontWeight = FontWeight.Bold,
                            color = AccentRose,
                            fontSize = 14.sp
                        )
                    }
                }
            }
        },
        bottomBar = {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = when (val fb = sessionState.feedbackState) {
                    is FeedbackState.Correct -> AccentEmerald.copy(alpha = 0.15f)
                    is FeedbackState.Incorrect -> AccentRose.copy(alpha = 0.15f)
                    null -> MaterialTheme.colorScheme.surface
                },
                tonalElevation = 4.dp
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .navigationBarsPadding()
                        .padding(horizontal = 16.dp, vertical = 14.dp)
                ) {
                    // Feedback content
                    when (val fb = sessionState.feedbackState) {
                        is FeedbackState.Correct -> {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Icon(Icons.Default.CheckCircle, contentDescription = "Correct", tint = AccentEmerald)
                                Column {
                                    Text(
                                        text = "Awesome! +${fb.xpAwarded} XP",
                                        fontWeight = FontWeight.ExtraBold,
                                        color = AccentEmerald,
                                        fontSize = 15.sp
                                    )
                                    if (fb.explanation != null) {
                                        Text(
                                            text = fb.explanation,
                                            fontSize = 12.sp,
                                            color = MaterialTheme.colorScheme.onSurface
                                        )
                                    }
                                }
                            }
                            Spacer(modifier = Modifier.height(10.dp))
                            Button(
                                onClick = onNextExercise,
                                modifier = Modifier.fillMaxWidth().height(48.dp).testTag("continue_exercise_button"),
                                shape = RoundedCornerShape(14.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = AccentEmerald)
                            ) {
                                Text("Continue", fontWeight = FontWeight.Bold, fontSize = 15.sp)
                            }
                        }
                        is FeedbackState.Incorrect -> {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Icon(Icons.Default.Cancel, contentDescription = "Incorrect", tint = AccentRose)
                                Column {
                                    Text(
                                        text = "Correct answer: ${fb.correctAnswer}",
                                        fontWeight = FontWeight.ExtraBold,
                                        color = AccentRose,
                                        fontSize = 14.sp
                                    )
                                    if (fb.explanation != null) {
                                        Text(
                                            text = fb.explanation,
                                            fontSize = 12.sp,
                                            color = MaterialTheme.colorScheme.onSurface
                                        )
                                    }
                                }
                            }
                            Spacer(modifier = Modifier.height(10.dp))
                            Button(
                                onClick = onNextExercise,
                                modifier = Modifier.fillMaxWidth().height(48.dp).testTag("continue_exercise_button"),
                                shape = RoundedCornerShape(14.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = AccentRose)
                            ) {
                                Text("Got It", fontWeight = FontWeight.Bold, fontSize = 15.sp)
                            }
                        }
                        null -> {
                            Button(
                                onClick = onCheckAnswer,
                                enabled = hasAnswered,
                                modifier = Modifier.fillMaxWidth().height(48.dp).testTag("check_answer_button"),
                                shape = RoundedCornerShape(14.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = PrimaryIndigo,
                                    disabledContainerColor = MaterialTheme.colorScheme.surfaceVariant
                                )
                            ) {
                                Text("Check Answer", fontWeight = FontWeight.Bold, fontSize = 15.sp)
                            }
                        }
                    }
                }
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            // Exercise Type Badge
            Surface(
                shape = RoundedCornerShape(8.dp),
                color = PrimaryIndigo.copy(alpha = 0.12f)
            ) {
                Text(
                    text = "${currentEx.type.icon} ${currentEx.type.label.uppercase()}",
                    color = PrimaryIndigo,
                    fontWeight = FontWeight.Bold,
                    fontSize = 11.sp,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Header (Prompt + Audio + Emoji)
            ExerciseHeader(
                prompt = currentEx.prompt,
                targetPhrase = currentEx.targetPhrase,
                pronunciation = currentEx.pronunciation,
                audioToPlay = currentEx.audioToPlay,
                visualEmoji = currentEx.visualEmoji,
                onPlayAudio = onPlayAudio
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Dynamic Body Component
            when (currentEx.type) {
                ExerciseType.MULTIPLE_CHOICE,
                ExerciseType.FILL_IN_THE_BLANK,
                ExerciseType.LISTENING,
                ExerciseType.TRANSLATION,
                ExerciseType.PICTURE_LEARNING -> {
                    MultipleChoiceExerciseView(
                        options = currentEx.options,
                        selectedOption = sessionState.selectedOption,
                        onSelectOption = onOptionSelected
                    )
                }
                ExerciseType.WORD_MATCHING -> {
                    WordMatchingExerciseView(
                        pairs = currentEx.matchingPairs,
                        matchedPairs = sessionState.matchedPairs,
                        selectedLeft = sessionState.selectedLeftPair,
                        selectedRight = sessionState.selectedRightPair,
                        onSelectLeft = onLeftPairSelected,
                        onSelectRight = onRightPairSelected
                    )
                }
                ExerciseType.ARRANGE_SENTENCE,
                ExerciseType.WORD_BUILDER -> {
                    ArrangeSentenceExerciseView(
                        arrangedWords = sessionState.arrangedWords,
                        availableWords = sessionState.availableWords,
                        onPickWord = onPickArrangedWord,
                        onRemoveWord = onRemoveArrangedWord
                    )
                }
                ExerciseType.SPEAKING -> {
                    SpeakingExerciseView(
                        targetPhrase = currentEx.correctAnswer.ifEmpty { currentEx.targetPhrase ?: "" },
                        speechState = speechState,
                        score = sessionState.speechEvaluationScore,
                        spokenText = sessionState.speechEvaluationText,
                        onStartSpeak = {
                            onStartSpeech(currentEx.correctAnswer.ifEmpty { currentEx.targetPhrase ?: "" })
                        },
                        onStopSpeak = onStopSpeech,
                        onManualVerify = {
                            onOptionSelected("VERIFIED")
                        }
                    )
                }
                ExerciseType.CONVERSATION -> {
                    ConversationExerciseView(
                        dialogueTurns = currentEx.dialogueTurns,
                        options = currentEx.options,
                        selectedOption = sessionState.selectedOption,
                        onSelectOption = onOptionSelected,
                        onPlayAudio = onPlayAudio
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
fun LessonCompletedView(
    sessionState: LessonSessionState,
    onFinish: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier.fillMaxWidth().testTag("lesson_completed_card"),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(8.dp)
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = "🎉", fontSize = 56.sp)
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = "Lesson Complete!",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.ExtraBold,
                    color = PrimaryIndigo
                )
                Text(
                    text = "You're making steady daily progress!",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(20.dp))

                Row(
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        shape = RoundedCornerShape(14.dp),
                        color = AccentAmber.copy(alpha = 0.15f)
                    ) {
                        Column(
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(text = "⭐ +${sessionState.earnedXpInSession + 20} XP", fontWeight = FontWeight.Bold, color = AccentAmber, fontSize = 16.sp)
                            Text(text = "XP Earned", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                    }

                    Surface(
                        shape = RoundedCornerShape(14.dp),
                        color = AccentEmerald.copy(alpha = 0.15f)
                    ) {
                        Column(
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            val accuracy = ((sessionState.correctAnswersCount.toFloat() / sessionState.totalExercises.toFloat().coerceAtLeast(1f)) * 100).toInt()
                            Text(text = "🎯 $accuracy%", fontWeight = FontWeight.Bold, color = AccentEmerald, fontSize = 16.sp)
                            Text(text = "Accuracy", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = onFinish,
                    modifier = Modifier.fillMaxWidth().height(50.dp).testTag("finish_lesson_button"),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = PrimaryIndigo)
                ) {
                    Text("Finish & Collect XP", fontWeight = FontWeight.Bold, fontSize = 15.sp)
                }
            }
        }
    }
}
