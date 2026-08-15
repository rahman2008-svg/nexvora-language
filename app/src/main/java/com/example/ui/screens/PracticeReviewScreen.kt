package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.VocabularyEntity
import com.example.data.model.ExerciseType
import com.example.data.model.Lesson
import com.example.ui.theme.*

@Composable
fun PracticeReviewScreen(
    vocabularyList: List<VocabularyEntity>,
    reviewQueue: List<VocabularyEntity>,
    flashcardIndex: Int,
    isCardFlipped: Boolean,
    onFlipCard: () -> Unit,
    onNextCard: (Int) -> Unit,
    onPrevCard: (Int) -> Unit,
    onRecordReviewResult: (VocabularyEntity, Boolean) -> Unit,
    onPlayAudio: (String) -> Unit,
    onStartSpecificPractice: (ExerciseType) -> Unit,
    modifier: Modifier = Modifier
) {
    val currentCard = vocabularyList.getOrNull(flashcardIndex) ?: vocabularyList.firstOrNull()

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(VibrantBg)
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(top = 12.dp, bottom = 96.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Smart Review Queue Hero Banner
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("smart_review_banner"),
                shape = RoundedCornerShape(22.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (reviewQueue.isNotEmpty()) AccentEmerald else PrimaryIndigo
                ),
                elevation = CardDefaults.cardElevation(4.dp)
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = Color.White.copy(alpha = 0.2f)
                        ) {
                            Text(
                                text = "🔄 SPACED REPETITION ENGINE",
                                color = Color.White,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                            )
                        }

                        Text(
                            text = if (reviewQueue.isNotEmpty()) "${reviewQueue.size} due today" else "Queue empty ✓",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = if (reviewQueue.isNotEmpty()) "Smart Review: Retain Failed Words" else "All Caught Up! Master New Vocabulary",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Text(
                        text = "Words you find challenging automatically resurface for optimal long-term memory retention.",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.White.copy(alpha = 0.85f)
                    )
                }
            }
        }

        // Section Title: Interactive Flashcards Studio
        item {
            Text(
                text = "📇 Flashcard Learning Lab",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
        }

        // Flashcard Interactive Card
        if (currentCard != null) {
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(240.dp)
                        .clip(RoundedCornerShape(24.dp))
                        .clickable { onFlipCard() }
                        .testTag("flashcard_card"),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    border = BorderStroke(2.dp, PrimaryIndigo.copy(alpha = 0.4f)),
                    elevation = CardDefaults.cardElevation(6.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(20.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = PrimaryIndigo.copy(alpha = 0.12f)
                            ) {
                                Text(
                                    text = if (isCardFlipped) "TRANSLATION / MEANING (Tap to Flip)" else "TARGET WORD (Tap to Flip)",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = PrimaryIndigo,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                )
                            }

                            Spacer(modifier = Modifier.height(12.dp))

                            if (!isCardFlipped) {
                                Text(
                                    text = currentCard.visualEmoji,
                                    fontSize = 36.sp
                                )
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = currentCard.word,
                                    style = MaterialTheme.typography.headlineMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = PrimaryIndigo,
                                    textAlign = TextAlign.Center
                                )
                                if (currentCard.pronunciation.isNotEmpty()) {
                                    Text(
                                        text = "/ ${currentCard.pronunciation} /",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            } else {
                                Text(
                                    text = currentCard.translation,
                                    style = MaterialTheme.typography.headlineSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = AccentEmerald,
                                    textAlign = TextAlign.Center
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = "“${currentCard.exampleTarget}”",
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = FontWeight.SemiBold,
                                    textAlign = TextAlign.Center,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Text(
                                    text = currentCard.exampleTranslation,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    textAlign = TextAlign.Center
                                )
                            }

                            Spacer(modifier = Modifier.height(12.dp))

                            IconButton(
                                onClick = { onPlayAudio(if (isCardFlipped) currentCard.exampleTarget else currentCard.word) },
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(CircleShape)
                                    .background(PrimaryIndigo.copy(alpha = 0.15f))
                            ) {
                                Icon(Icons.Default.VolumeUp, contentDescription = "Audio", tint = PrimaryIndigo, modifier = Modifier.size(20.dp))
                            }
                        }
                    }
                }
            }

            // Flashcard Navigation & Mastered / Need Practice Buttons
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = { onPrevCard(vocabularyList.size) },
                        modifier = Modifier
                            .size(44.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.surfaceVariant)
                    ) {
                        Icon(Icons.Default.ChevronLeft, contentDescription = "Previous")
                    }

                    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        Button(
                            onClick = {
                                onRecordReviewResult(currentCard, false)
                                onNextCard(vocabularyList.size)
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = AccentRose),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text("Need Review ❌", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }

                        Button(
                            onClick = {
                                onRecordReviewResult(currentCard, true)
                                onNextCard(vocabularyList.size)
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = AccentEmerald),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text("Got it! ✅", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }
                    }

                    IconButton(
                        onClick = { onNextCard(vocabularyList.size) },
                        modifier = Modifier
                            .size(44.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.surfaceVariant)
                    ) {
                        Icon(Icons.Default.ChevronRight, contentDescription = "Next")
                    }
                }
            }
        }

        // Section Title: 10 Exercise Modes Sandbox
        item {
            Text(
                text = "🧠 10 Interactive Learning Modes",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
        }

        // Grid of 10 exercise modes
        items(ExerciseType.entries.chunked(2)) { pair ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                pair.forEach { mode ->
                    Card(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(16.dp))
                            .clickable { onStartSpecificPractice(mode) },
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        elevation = CardDefaults.cardElevation(2.dp)
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(text = mode.icon, fontSize = 22.sp)
                                Surface(
                                    shape = RoundedCornerShape(6.dp),
                                    color = AccentAmber.copy(alpha = 0.15f)
                                ) {
                                    Text(
                                        text = "+${mode.xpReward} XP",
                                        color = AccentAmber,
                                        fontWeight = FontWeight.ExtraBold,
                                        fontSize = 10.sp,
                                        modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(text = mode.label, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                        }
                    }
                }
                if (pair.size == 1) {
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
        }
    }
}
