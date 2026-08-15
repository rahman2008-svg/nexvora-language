package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.UserProfileEntity
import com.example.data.model.SupportedLanguage
import com.example.ui.theme.*

@Composable
fun AppTopBar(
    userProfile: UserProfileEntity,
    onLanguageClick: () -> Unit,
    onHeartsClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val sourceLang = SupportedLanguage.fromCode(userProfile.sourceLanguageCode)
    val targetLang = SupportedLanguage.fromCode(userProfile.targetLanguageCode)

    Surface(
        modifier = modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.surface.copy(alpha = 0.95f),
        tonalElevation = 2.dp,
        shadowElevation = 1.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(horizontal = 16.dp, vertical = 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Language Pair Badge
            Surface(
                modifier = Modifier
                    .clip(RoundedCornerShape(20.dp))
                    .clickable { onLanguageClick() }
                    .testTag("language_selector_button"),
                color = MaterialTheme.colorScheme.primaryContainer,
                shape = RoundedCornerShape(20.dp)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(text = sourceLang.flag, fontSize = 16.sp)
                    Text(text = "→", fontSize = 12.sp, color = MaterialTheme.colorScheme.onPrimaryContainer, fontWeight = FontWeight.Bold)
                    Text(text = targetLang.flag, fontSize = 16.sp)
                    Text(
                        text = targetLang.displayName,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
            }

            // Status Indicators: Streak, XP, Hearts
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Streak Indicator
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(16.dp))
                        .border(1.dp, AccentStreakBorder, RoundedCornerShape(16.dp))
                        .background(AccentStreakBg)
                        .padding(horizontal = 8.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.LocalFireDepartment,
                        contentDescription = "Streak",
                        tint = AccentStreakOrange,
                        modifier = Modifier.size(16.dp)
                    )
                    Text(
                        text = "${userProfile.streakDays}",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = AccentStreakOrange
                    )
                }

                // XP Indicator
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(16.dp))
                        .border(1.dp, VibrantPurpleContainer, RoundedCornerShape(16.dp))
                        .background(VibrantPurpleContainer.copy(alpha = 0.5f))
                        .padding(horizontal = 8.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = "XP",
                        tint = VibrantPurple,
                        modifier = Modifier.size(15.dp)
                    )
                    Text(
                        text = "${userProfile.totalXp}",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = VibrantPurpleDeep
                    )
                }

                // Hearts Indicator
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(16.dp))
                        .border(1.dp, AccentHeartBorder, RoundedCornerShape(16.dp))
                        .background(AccentHeartBg)
                        .clickable { onHeartsClick() }
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                        .testTag("hearts_counter_button"),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Favorite,
                        contentDescription = "Hearts",
                        tint = AccentHeartRed,
                        modifier = Modifier.size(15.dp)
                    )
                    Text(
                        text = "${userProfile.hearts}",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = AccentHeartRed
                    )
                }
            }
        }
    }
}
