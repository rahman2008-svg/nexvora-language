package com.example.ui.screens

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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.LessonProgressEntity
import com.example.data.local.UserProfileEntity
import com.example.data.model.Lesson
import com.example.data.model.SupportedLanguage
import com.example.data.model.UnitData
import com.example.ui.theme.*

@Composable
fun HomeScreen(
    userProfile: UserProfileEntity,
    curriculum: List<UnitData>,
    completedLessons: List<LessonProgressEntity>,
    onStartLesson: (Lesson) -> Unit,
    onOpenLanguageSelector: () -> Unit,
    onOpenHeartsDialog: () -> Unit,
    modifier: Modifier = Modifier
) {
    val completedIds = remember(completedLessons) { completedLessons.filter { it.isCompleted }.map { it.lessonId }.toSet() }
    val currentTargetLang = SupportedLanguage.fromCode(userProfile.targetLanguageCode)
    val currentSourceLang = SupportedLanguage.fromCode(userProfile.sourceLanguageCode)
    val isBengali = userProfile.sourceLanguageCode == "bn"

    // Find next uncompleted lesson or first lesson
    val nextLesson = remember(curriculum, completedIds) {
        curriculum.flatMap { it.lessons }.firstOrNull { !completedIds.contains(it.id) }
            ?: curriculum.firstOrNull()?.lessons?.firstOrNull()
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(VibrantBg)
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(top = 12.dp, bottom = 96.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Welcome & Daily Goal Card (Vibrant Purple Hero Card)
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(elevation = 8.dp, shape = RoundedCornerShape(26.dp), spotColor = VibrantPurple.copy(alpha = 0.35f))
                    .testTag("home_welcome_card"),
                shape = RoundedCornerShape(26.dp),
                colors = CardDefaults.cardColors(containerColor = VibrantPurple)
            ) {
                Box(modifier = Modifier.fillMaxWidth()) {
                    // Decorative glow
                    Box(
                        modifier = Modifier
                            .size(110.dp)
                            .align(Alignment.BottomEnd)
                            .offset(x = 24.dp, y = 24.dp)
                            .clip(CircleShape)
                            .background(Color.White.copy(alpha = 0.12f))
                    )

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(22.dp)
                    ) {
                        Text(
                            text = if (isBengali) "আজকের লক্ষ্য" else "Today's Goal",
                            color = Color.White.copy(alpha = 0.85f),
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Medium
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        val dailyPercent = ((userProfile.dailyProgressMinutes.toFloat() / userProfile.dailyGoalMinutes.coerceAtLeast(1).toFloat()) * 100).toInt().coerceIn(0, 100)

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.Bottom
                        ) {
                            Row(verticalAlignment = Alignment.Bottom) {
                                Text(
                                    text = "${userProfile.totalXp} ",
                                    color = Color.White,
                                    fontSize = 32.sp,
                                    fontWeight = FontWeight.ExtraBold
                                )
                                Text(
                                    text = "XP",
                                    color = Color.White.copy(alpha = 0.75f),
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Normal,
                                    modifier = Modifier.padding(bottom = 4.dp)
                                )
                            }
                            Text(
                                text = if (isBengali) "$dailyPercent% সম্পন্ন" else "$dailyPercent% reached",
                                color = Color.White,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        // Custom Vibrant Progress Bar
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(10.dp)
                                .clip(RoundedCornerShape(5.dp))
                                .background(Color.White.copy(alpha = 0.25f))
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth(fraction = (dailyPercent / 100f).coerceIn(0.05f, 1f))
                                    .fillMaxHeight()
                                    .clip(RoundedCornerShape(5.dp))
                                    .background(VibrantPurpleLight)
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = if (isBengali) "দৈনিক সময়: ${userProfile.dailyProgressMinutes}/${userProfile.dailyGoalMinutes} মিনিট" else "Time: ${userProfile.dailyProgressMinutes}/${userProfile.dailyGoalMinutes} min",
                                color = Color.White.copy(alpha = 0.8f),
                                fontSize = 12.sp
                            )
                            Text(
                                text = if (isBengali) "ধারাবাহিকতা: ${userProfile.streakDays} দিন 🔥" else "Streak: ${userProfile.streakDays} days 🔥",
                                color = Color.White.copy(alpha = 0.9f),
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }

        // Section Title: Units Path
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp, bottom = 2.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = if (isBengali) "শেখার যাত্রা" else "Learning Path",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = VibrantOnBg
                    )
                    Text(
                        text = "${currentTargetLang.flag} ${currentTargetLang.displayName} (${currentTargetLang.nativeName})",
                        style = MaterialTheme.typography.bodySmall,
                        color = VibrantOnSurfaceVariant
                    )
                }
                TextButton(
                    onClick = onOpenLanguageSelector,
                    colors = ButtonDefaults.textButtonColors(contentColor = VibrantPurple)
                ) {
                    Text(
                        text = if (isBengali) "ভাষা পরিবর্তন" else "Change Language",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        // Units and Lessons Timeline
        items(curriculum) { unit ->
            UnitCard(
                unit = unit,
                completedLessonIds = completedIds,
                isBengali = isBengali,
                onStartLesson = onStartLesson
            )
        }
    }
}

@Composable
fun UnitCard(
    unit: UnitData,
    completedLessonIds: Set<String>,
    isBengali: Boolean,
    onStartLesson: (Lesson) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = VibrantSurface),
        border = androidx.compose.foundation.BorderStroke(1.dp, VibrantOutline.copy(alpha = 0.5f)),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            // Unit Header
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(46.dp)
                        .clip(CircleShape)
                        .background(VibrantPurpleContainer),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = unit.icon, fontSize = 24.sp)
                }
                Column(modifier = Modifier.weight(1f)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = VibrantPurpleContainer
                        ) {
                            Text(
                                text = "UNIT ${unit.unitNumber}",
                                color = VibrantPurpleDeep,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.ExtraBold,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                            )
                        }
                        Text(
                            text = unit.title,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = VibrantOnBg
                        )
                    }
                    Text(
                        text = unit.description,
                        style = MaterialTheme.typography.bodySmall,
                        color = VibrantOnSurfaceVariant
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            HorizontalDivider(color = VibrantOutline.copy(alpha = 0.3f))
            Spacer(modifier = Modifier.height(14.dp))

            // Visual Path Nodes for Lessons
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                unit.lessons.forEachIndexed { index, lesson ->
                    val isDone = completedLessonIds.contains(lesson.id)
                    val isCurrent = !isDone && unit.lessons.take(index).all { completedLessonIds.contains(it.id) }

                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(16.dp))
                            .clickable { onStartLesson(lesson) }
                            .border(
                                width = if (isCurrent) 2.dp else 1.dp,
                                color = when {
                                    isDone -> AccentEmerald.copy(alpha = 0.6f)
                                    isCurrent -> VibrantPurple
                                    else -> VibrantOutline.copy(alpha = 0.3f)
                                },
                                shape = RoundedCornerShape(16.dp)
                            ),
                        color = when {
                            isDone -> AccentEmerald.copy(alpha = 0.08f)
                            isCurrent -> VibrantPurpleContainer.copy(alpha = 0.35f)
                            else -> VibrantSurfaceVariant.copy(alpha = 0.4f)
                        }
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(12.dp),
                                modifier = Modifier.weight(1f)
                            ) {
                                // Icon or Node circle
                                Box(
                                    modifier = Modifier
                                        .size(42.dp)
                                        .clip(CircleShape)
                                        .background(
                                            when {
                                                isDone -> AccentEmerald
                                                isCurrent -> VibrantPurple
                                                else -> VibrantOutline.copy(alpha = 0.3f)
                                            }
                                        ),
                                    contentAlignment = Alignment.Center
                                ) {
                                    if (isDone) {
                                        Icon(
                                            imageVector = Icons.Default.Check,
                                            contentDescription = "Completed",
                                            tint = Color.White,
                                            modifier = Modifier.size(22.dp)
                                        )
                                    } else {
                                        Text(
                                            text = lesson.icon,
                                            fontSize = 20.sp
                                        )
                                    }
                                }

                                Column {
                                    Text(
                                        text = lesson.title,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 15.sp,
                                        color = VibrantOnBg
                                    )
                                    Text(
                                        text = "${lesson.exercises.size} ${if (isBengali) "অনুশীলন" else "exercises"} • ${lesson.estimatedMinutes} ${if (isBengali) "মিনিট" else "mins"}",
                                        fontSize = 12.sp,
                                        color = VibrantOnSurfaceVariant
                                    )
                                }
                            }

                            if (isCurrent) {
                                Button(
                                    onClick = { onStartLesson(lesson) },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = VibrantPurple,
                                        contentColor = Color.White
                                    ),
                                    shape = RoundedCornerShape(12.dp),
                                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                                ) {
                                    Text(
                                        text = if (isBengali) "শুরু করুন" else "START",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            } else if (!isDone) {
                                Icon(
                                    imageVector = Icons.Default.PlayArrow,
                                    contentDescription = "Start",
                                    tint = VibrantPurple,
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
