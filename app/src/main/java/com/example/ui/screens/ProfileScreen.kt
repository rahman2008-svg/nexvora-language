package com.example.ui.screens

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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.AchievementEntity
import com.example.data.local.StreakDayEntity
import com.example.data.local.UserProfileEntity
import com.example.data.model.SupportedLanguage
import com.example.ui.theme.*

@Composable
fun ProfileScreen(
    userProfile: UserProfileEntity,
    achievements: List<AchievementEntity>,
    streakHistory: List<StreakDayEntity>,
    onOpenLanguageSelector: () -> Unit,
    onOpenHeartsDialog: () -> Unit,
    onUpdateDailyGoal: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val sourceLang = SupportedLanguage.fromCode(userProfile.sourceLanguageCode)
    val targetLang = SupportedLanguage.fromCode(userProfile.targetLanguageCode)
    val isBengali = userProfile.sourceLanguageCode == "bn"

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(VibrantBg)
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(top = 12.dp, bottom = 96.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // User Profile Header Card
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("profile_header_card"),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = VibrantSurface),
                border = BorderStroke(1.dp, VibrantOutline.copy(alpha = 0.5f)),
                elevation = CardDefaults.cardElevation(2.dp)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .size(76.dp)
                            .clip(CircleShape)
                            .background(VibrantPurple),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "👑", fontSize = 38.sp)
                    }

                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = userProfile.name,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = VibrantOnBg
                    )
                    Text(
                        text = if (isBengali) "লেভেল ${userProfile.level} • ${userProfile.totalXp} মোট XP" else "Level ${userProfile.level} • ${userProfile.totalXp} Total XP",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = VibrantPurple
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    // Language Pair Setting Pill
                    Surface(
                        modifier = Modifier
                            .clip(RoundedCornerShape(14.dp))
                            .clickable { onOpenLanguageSelector() }
                            .border(1.dp, VibrantPurple.copy(alpha = 0.3f), RoundedCornerShape(14.dp)),
                        color = VibrantPurpleContainer
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(
                                text = "${sourceLang.flag} ${sourceLang.displayName} → ${targetLang.flag} ${targetLang.displayName}",
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp,
                                color = VibrantPurpleDeep
                            )
                            Icon(Icons.Default.Edit, contentDescription = "Edit Pair", modifier = Modifier.size(16.dp), tint = VibrantPurple)
                        }
                    }
                }
            }
        }

        // 4 Core Stat Counters Grid
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                StatPill(
                    icon = "🔥",
                    value = if (isBengali) "${userProfile.streakDays} দিন" else "${userProfile.streakDays} Days",
                    label = if (isBengali) "ধারাবাহিকতা" else "Day Streak",
                    modifier = Modifier.weight(1f)
                )
                StatPill(
                    icon = "⭐",
                    value = "${userProfile.totalXp} XP",
                    label = if (isBengali) "মোট পয়েন্ট" else "Total XP",
                    modifier = Modifier.weight(1f)
                )
            }
        }
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                StatPill(
                    icon = "📖",
                    value = "${userProfile.completedLessonsCount}",
                    label = if (isBengali) "সম্পন্ন লেসন" else "Lessons Done",
                    modifier = Modifier.weight(1f)
                )
                StatPill(
                    icon = "📚",
                    value = "${userProfile.learnedWordsCount}",
                    label = if (isBengali) "শব্দ আয়ত্ত" else "Words Mastered",
                    modifier = Modifier.weight(1f)
                )
            }
        }

        // 7-Day Streak Calendar
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = VibrantSurface),
                border = BorderStroke(1.dp, VibrantOutline.copy(alpha = 0.5f)),
                elevation = CardDefaults.cardElevation(2.dp)
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = if (isBengali) "🔥 ৭ দিনের ধারাবাহিকতা" else "🔥 7-Day Streak Calendar",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = VibrantOnBg
                        )
                        Text(
                            text = if (isBengali) "${userProfile.streakDays} দিনের স্ট্রিক" else "${userProfile.streakDays} Day Streak",
                            color = AccentAmber,
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    val daysOfWeek = if (isBengali) listOf("সোম", "মঙ্গল", "বুধ", "বৃহঃ", "শুক্র", "শনি", "রবি") else listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun")
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        daysOfWeek.forEachIndexed { idx, day ->
                            val isCompleted = idx < 6 // Visual representation
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Text(
                                    text = day,
                                    fontSize = 11.sp,
                                    color = VibrantOnSurfaceVariant,
                                    fontWeight = FontWeight.SemiBold
                                )
                                Box(
                                    modifier = Modifier
                                        .size(36.dp)
                                        .clip(CircleShape)
                                        .background(
                                            if (isCompleted) AccentAmber else VibrantSurfaceVariant
                                        ),
                                    contentAlignment = Alignment.Center
                                ) {
                                    if (isCompleted) {
                                        Icon(
                                            Icons.Default.Check,
                                            contentDescription = "Streak checked",
                                            tint = Color.White,
                                            modifier = Modifier.size(18.dp)
                                        )
                                    } else {
                                        Text(text = "🔥", fontSize = 14.sp)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        // Daily Study Goal Target Setting
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = VibrantSurface),
                border = BorderStroke(1.dp, VibrantOutline.copy(alpha = 0.5f)),
                elevation = CardDefaults.cardElevation(2.dp)
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Text(
                        text = if (isBengali) "⏱️ দৈনিক অনুশীলনের লক্ষ্য" else "⏱️ Daily Study Goal",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = VibrantOnBg
                    )
                    Spacer(modifier = Modifier.height(10.dp))

                    val goals = if (isBengali) {
                        listOf(5 to "সহজ", 10 to "নিয়মিত", 15 to "গভীর", 20 to "তীব্র")
                    } else {
                        listOf(5 to "Casual", 10 to "Regular", 15 to "Serious", 20 to "Intense")
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        goals.forEach { (mins, label) ->
                            val isSelected = userProfile.dailyGoalMinutes == mins
                            Surface(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(14.dp))
                                    .clickable { onUpdateDailyGoal(mins) }
                                    .border(
                                        width = if (isSelected) 2.dp else 1.dp,
                                        color = if (isSelected) VibrantPurple else VibrantOutline.copy(alpha = 0.4f),
                                        shape = RoundedCornerShape(14.dp)
                                    ),
                                color = if (isSelected) VibrantPurpleContainer else VibrantSurface
                            ) {
                                Column(
                                    modifier = Modifier.padding(vertical = 10.dp, horizontal = 4.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text(
                                        text = "$mins ${if (isBengali) "মি." else "min"}",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 13.sp,
                                        color = if (isSelected) VibrantPurpleDeep else VibrantOnBg
                                    )
                                    Text(
                                        text = label,
                                        fontSize = 10.sp,
                                        color = VibrantOnSurfaceVariant
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // Achievements / Badges Collection
        item {
            Text(
                text = if (isBengali) "🏆 অর্জন ও পদকসমূহ" else "🏆 Achievements & Badges",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = VibrantOnBg
            )
        }

        items(achievements) { achievement ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = VibrantSurface),
                border = BorderStroke(1.dp, VibrantOutline.copy(alpha = 0.5f)),
                elevation = CardDefaults.cardElevation(2.dp)
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(46.dp)
                            .clip(CircleShape)
                            .background(
                                if (achievement.isUnlocked) AccentAmber.copy(alpha = 0.2f) else VibrantSurfaceVariant
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = achievement.icon, fontSize = 24.sp)
                    }

                    Column(modifier = Modifier.weight(1f)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Text(
                                text = achievement.title,
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp,
                                color = VibrantOnBg
                            )
                            if (achievement.isUnlocked) {
                                Surface(
                                    shape = RoundedCornerShape(6.dp),
                                    color = AccentEmerald.copy(alpha = 0.15f)
                                ) {
                                    Text(
                                        text = if (isBengali) "আনলকড ✓" else "UNLOCKED ✓",
                                        color = AccentEmerald,
                                        fontSize = 9.sp,
                                        fontWeight = FontWeight.ExtraBold,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }
                            }
                        }
                        Text(
                            text = achievement.description,
                            fontSize = 12.sp,
                            color = VibrantOnSurfaceVariant
                        )
                    }

                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = VibrantPurpleContainer
                    ) {
                        Text(
                            text = "+${achievement.xpReward} XP",
                            color = VibrantPurpleDeep,
                            fontWeight = FontWeight.Bold,
                            fontSize = 11.sp,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun StatPill(
    icon: String,
    value: String,
    label: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = VibrantSurface),
        border = BorderStroke(1.dp, VibrantOutline.copy(alpha = 0.5f)),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Text(text = icon, fontSize = 24.sp)
            Column {
                Text(text = value, fontWeight = FontWeight.Bold, fontSize = 15.sp, color = VibrantOnBg)
                Text(text = label, fontSize = 11.sp, color = VibrantOnSurfaceVariant)
            }
        }
    }
}
