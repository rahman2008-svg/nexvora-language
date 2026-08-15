package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.UserProfileEntity
import com.example.data.model.CEFRLevel
import com.example.data.model.GrammarRule
import com.example.data.model.Lesson
import com.example.data.model.UnitData
import com.example.ui.theme.*

@Composable
fun CoursesGrammarScreen(
    userProfile: UserProfileEntity,
    curriculum: List<UnitData>,
    grammarRules: List<GrammarRule>,
    onStartLesson: (Lesson) -> Unit,
    onPlayAudio: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedTab by remember { mutableStateOf(0) } // 0: Curriculum & CEFR, 1: Grammar Hub, 2: Real-Life Tracks
    var selectedGrammarRule by remember { mutableStateOf<GrammarRule?>(null) }
    var grammarQuizSelectedOption by remember { mutableStateOf<String?>(null) }
    var isQuizAnswerChecked by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(VibrantBg)
            .padding(horizontal = 16.dp)
    ) {
        Spacer(modifier = Modifier.height(12.dp))

        // Tab Selector
        TabRow(
            selectedTabIndex = selectedTab,
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
            contentColor = PrimaryIndigo,
            modifier = Modifier.clip(RoundedCornerShape(14.dp))
        ) {
            Tab(
                selected = selectedTab == 0,
                onClick = { selectedTab = 0 },
                text = { Text("Levels (A1–C2)", fontWeight = FontWeight.Bold, fontSize = 12.sp) }
            )
            Tab(
                selected = selectedTab == 1,
                onClick = { selectedTab = 1 },
                text = { Text("Grammar Hub", fontWeight = FontWeight.Bold, fontSize = 12.sp) }
            )
            Tab(
                selected = selectedTab == 2,
                onClick = { selectedTab = 2 },
                text = { Text("Real-Life Tracks", fontWeight = FontWeight.Bold, fontSize = 12.sp) }
            )
        }

        Spacer(modifier = Modifier.height(14.dp))

        when (selectedTab) {
            0 -> {
                // CEFR Levels Explorer
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(bottom = 96.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    items(CEFRLevel.entries) { level ->
                        val unitsInLevel = curriculum.filter { it.level == level }

                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(18.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                            elevation = CardDefaults.cardElevation(2.dp)
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(38.dp)
                                            .clip(CircleShape)
                                            .background(Color(level.colorHex)),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(text = level.code, color = Color.White, fontWeight = FontWeight.ExtraBold, fontSize = 13.sp)
                                    }
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(text = level.title, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                                        Text(text = level.subtitle, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                    }
                                }

                                if (unitsInLevel.isNotEmpty()) {
                                    Spacer(modifier = Modifier.height(10.dp))
                                    unitsInLevel.forEach { u ->
                                        Surface(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(vertical = 4.dp)
                                                .clip(RoundedCornerShape(12.dp))
                                                .clickable {
                                                    u.lessons.firstOrNull()?.let { onStartLesson(it) }
                                                },
                                            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                                        ) {
                                            Row(
                                                modifier = Modifier.padding(12.dp),
                                                verticalAlignment = Alignment.CenterVertically,
                                                horizontalArrangement = Arrangement.SpaceBetween
                                            ) {
                                                Row(
                                                    verticalAlignment = Alignment.CenterVertically,
                                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                                ) {
                                                    Text(text = u.icon, fontSize = 18.sp)
                                                    Text(text = u.title, fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
                                                }
                                                Icon(Icons.Default.PlayArrow, contentDescription = "Start", tint = PrimaryIndigo, modifier = Modifier.size(20.dp))
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            1 -> {
                // Grammar Section with Explanations and Mini-Quizzes
                if (selectedGrammarRule == null) {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(bottom = 96.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(grammarRules) { rule ->
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(16.dp))
                                    .clickable {
                                        selectedGrammarRule = rule
                                        grammarQuizSelectedOption = null
                                        isQuizAnswerChecked = false
                                    },
                                shape = RoundedCornerShape(16.dp),
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                                elevation = CardDefaults.cardElevation(2.dp)
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Column(modifier = Modifier.weight(1f)) {
                                        Surface(
                                            shape = RoundedCornerShape(6.dp),
                                            color = PrimaryIndigo.copy(alpha = 0.12f)
                                        ) {
                                            Text(
                                                text = rule.category,
                                                color = PrimaryIndigo,
                                                fontSize = 10.sp,
                                                fontWeight = FontWeight.Bold,
                                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                            )
                                        }
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Text(text = rule.title, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                                        Text(text = rule.summary, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant, maxLines = 2)
                                    }
                                    Icon(Icons.Default.ChevronRight, contentDescription = "Open", tint = PrimaryIndigo)
                                }
                            }
                        }
                    }
                } else {
                    // Grammar Detail & Interactive Quiz
                    val rule = selectedGrammarRule!!
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(bottom = 96.dp),
                        verticalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        item {
                            TextButton(
                                onClick = { selectedGrammarRule = null },
                                contentPadding = PaddingValues(0.dp)
                            ) {
                                Icon(Icons.Default.ArrowBack, contentDescription = "Back", modifier = Modifier.size(18.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Back to Grammar Topics", fontWeight = FontWeight.Bold)
                            }
                        }

                        item {
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(20.dp),
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                            ) {
                                Column(modifier = Modifier.padding(18.dp)) {
                                    Text(text = rule.title, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = PrimaryIndigo)
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text(text = rule.explanation, style = MaterialTheme.typography.bodyMedium, lineHeight = 22.sp)

                                    Spacer(modifier = Modifier.height(14.dp))
                                    // Formula Structure Box
                                    Surface(
                                        modifier = Modifier.fillMaxWidth(),
                                        shape = RoundedCornerShape(12.dp),
                                        color = MaterialTheme.colorScheme.primaryContainer
                                    ) {
                                        Column(modifier = Modifier.padding(12.dp)) {
                                            Text(text = "STRUCTURE / FORMULA:", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onPrimaryContainer)
                                            Spacer(modifier = Modifier.height(4.dp))
                                            Text(text = rule.formulaOrStructure, fontWeight = FontWeight.Bold, fontSize = 13.sp, color = PrimaryIndigoDark)
                                        }
                                    }

                                    Spacer(modifier = Modifier.height(16.dp))
                                    Text(text = "Examples:", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                                    Spacer(modifier = Modifier.height(6.dp))

                                    rule.examples.forEach { ex ->
                                        Surface(
                                            modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                                            shape = RoundedCornerShape(10.dp),
                                            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                                        ) {
                                            Row(
                                                modifier = Modifier.padding(10.dp),
                                                verticalAlignment = Alignment.CenterVertically,
                                                horizontalArrangement = Arrangement.SpaceBetween
                                            ) {
                                                Column(modifier = Modifier.weight(1f)) {
                                                    Text(text = ex.targetSentence, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                                                    Text(text = ex.translatedSentence, fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                                }
                                                IconButton(
                                                    onClick = { onPlayAudio(ex.targetSentence) },
                                                    modifier = Modifier.size(32.dp)
                                                ) {
                                                    Icon(Icons.Default.VolumeUp, contentDescription = "Play", tint = PrimaryIndigo, modifier = Modifier.size(18.dp))
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }

                        // Interactive Rule Mini-Quiz
                        item {
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(20.dp),
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                                border = BorderStroke(1.5.dp, PrimaryIndigo.copy(alpha = 0.3f))
                            ) {
                                Column(modifier = Modifier.padding(18.dp)) {
                                    Text(text = "🎯 Quick Rule Quiz", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = PrimaryIndigo)
                                    Spacer(modifier = Modifier.height(6.dp))
                                    Text(text = rule.quizQuestion, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.SemiBold)

                                    Spacer(modifier = Modifier.height(12.dp))

                                    rule.quizOptions.forEach { opt ->
                                        val isSelected = grammarQuizSelectedOption == opt
                                        val isCorrect = opt == rule.quizCorrectAnswer

                                        Surface(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(vertical = 4.dp)
                                                .clip(RoundedCornerShape(12.dp))
                                                .clickable(enabled = !isQuizAnswerChecked) {
                                                    grammarQuizSelectedOption = opt
                                                }
                                                .border(
                                                    width = if (isSelected) 2.dp else 1.dp,
                                                    color = when {
                                                        isQuizAnswerChecked && isCorrect -> AccentEmerald
                                                        isQuizAnswerChecked && isSelected && !isCorrect -> AccentRose
                                                        isSelected -> PrimaryIndigo
                                                        else -> MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
                                                    },
                                                    shape = RoundedCornerShape(12.dp)
                                                ),
                                            color = when {
                                                isQuizAnswerChecked && isCorrect -> AccentEmerald.copy(alpha = 0.15f)
                                                isQuizAnswerChecked && isSelected && !isCorrect -> AccentRose.copy(alpha = 0.15f)
                                                isSelected -> PrimaryIndigo.copy(alpha = 0.12f)
                                                else -> MaterialTheme.colorScheme.surface
                                            }
                                        ) {
                                            Row(
                                                modifier = Modifier.padding(12.dp),
                                                verticalAlignment = Alignment.CenterVertically,
                                                horizontalArrangement = Arrangement.SpaceBetween
                                            ) {
                                                Text(text = opt, fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
                                                if (isQuizAnswerChecked && isCorrect) {
                                                    Icon(Icons.Default.CheckCircle, contentDescription = "Correct", tint = AccentEmerald)
                                                }
                                            }
                                        }
                                    }

                                    Spacer(modifier = Modifier.height(12.dp))

                                    if (!isQuizAnswerChecked) {
                                        Button(
                                            onClick = { isQuizAnswerChecked = true },
                                            enabled = grammarQuizSelectedOption != null,
                                            modifier = Modifier.fillMaxWidth().height(44.dp),
                                            shape = RoundedCornerShape(12.dp),
                                            colors = ButtonDefaults.buttonColors(containerColor = PrimaryIndigo)
                                        ) {
                                            Text("Check Quiz Answer", fontWeight = FontWeight.Bold)
                                        }
                                    } else {
                                        Surface(
                                            modifier = Modifier.fillMaxWidth(),
                                            shape = RoundedCornerShape(10.dp),
                                            color = if (grammarQuizSelectedOption == rule.quizCorrectAnswer) AccentEmerald.copy(alpha = 0.15f) else AccentRose.copy(alpha = 0.15f)
                                        ) {
                                            Text(
                                                text = if (grammarQuizSelectedOption == rule.quizCorrectAnswer) "🎉 Correct! ${rule.quizExplanation}" else "❌ ${rule.quizExplanation}",
                                                modifier = Modifier.padding(12.dp),
                                                fontSize = 12.sp,
                                                fontWeight = FontWeight.Medium
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            2 -> {
                // Real-Life Courses (Airport, Restaurant, Job Interview, Shopping, Hotel, Tech)
                val realLifeTracks = listOf(
                    RealLifeTrack("Travel & Airport", "Check-in, boarding, immigration, baggage claim", "✈️", "unit_4_travel"),
                    RealLifeTrack("Restaurant & Dining", "Ordering food, bills, dietary requests, tipping", "🍽️", "unit_3_food"),
                    RealLifeTrack("Job Interview & Work", "Professional introductions, strengths, negotiation", "💼", "unit_5_career"),
                    RealLifeTrack("Hotel & Lodging", "Reservations, room service, check-out, amenities", "🏨", "unit_4_travel"),
                    RealLifeTrack("Shopping & Market", "Prices, sizes, bargaining, returns and receipts", "🛍️", "unit_1_basics"),
                    RealLifeTrack("Tech & Internet", "Digital tools, email etiquette, remote collaboration", "💻", "unit_5_career")
                )

                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(bottom = 96.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(realLifeTracks) { track ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(18.dp))
                                .clickable {
                                    val matchedUnit = curriculum.firstOrNull { it.id == track.unitId } ?: curriculum.firstOrNull()
                                    matchedUnit?.lessons?.firstOrNull()?.let { onStartLesson(it) }
                                },
                            shape = RoundedCornerShape(18.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                            elevation = CardDefaults.cardElevation(2.dp)
                        ) {
                            Row(
                                modifier = Modifier.padding(16.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(14.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(46.dp)
                                        .clip(CircleShape)
                                        .background(PrimaryIndigo.copy(alpha = 0.12f)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(text = track.icon, fontSize = 24.sp)
                                }
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(text = track.title, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                                    Text(text = track.description, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                }
                                Icon(Icons.Default.PlayArrow, contentDescription = "Start", tint = PrimaryIndigo)
                            }
                        }
                    }
                }
            }
        }
    }
}

data class RealLifeTrack(
    val title: String,
    val description: String,
    val icon: String,
    val unitId: String
)
