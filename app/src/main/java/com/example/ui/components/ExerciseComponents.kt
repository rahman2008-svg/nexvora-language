package com.example.ui.components

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.*
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.example.audio.SpeechState
import com.example.data.model.*
import com.example.ui.theme.*

@Composable
fun ExerciseHeader(
    prompt: String,
    targetPhrase: String? = null,
    pronunciation: String? = null,
    audioToPlay: String? = null,
    visualEmoji: String? = null,
    onPlayAudio: ((String) -> Unit)? = null
) {
    Column(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 4.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Visual Emoji if present
        if (visualEmoji != null) {
            Box(
                modifier = Modifier
                    .size(90.dp)
                    .clip(CircleShape)
                    .background(PrimaryIndigo.copy(alpha = 0.1f)),
                contentAlignment = Alignment.Center
            ) {
                Text(text = visualEmoji, fontSize = 48.sp)
            }
            Spacer(modifier = Modifier.height(14.dp))
        }

        // Prompt
        Text(
            text = prompt,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 8.dp)
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Target phrase / Audio play banner
        if (targetPhrase != null || audioToPlay != null) {
            Surface(
                modifier = Modifier
                    .clip(RoundedCornerShape(16.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant)
                    .padding(horizontal = 16.dp, vertical = 10.dp),
                color = Color.Transparent
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    if (audioToPlay != null && onPlayAudio != null) {
                        IconButton(
                            onClick = { onPlayAudio(audioToPlay) },
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(PrimaryIndigo),
                            colors = IconButtonDefaults.iconButtonColors(contentColor = Color.White)
                        ) {
                            Icon(
                                imageVector = Icons.Default.VolumeUp,
                                contentDescription = "Listen pronunciation",
                                modifier = Modifier.size(22.dp)
                            )
                        }
                    }

                    if (targetPhrase != null) {
                        Column {
                            Text(
                                text = targetPhrase,
                                style = MaterialTheme.typography.headlineSmall,
                                fontWeight = FontWeight.Bold,
                                color = PrimaryIndigo
                            )
                            if (pronunciation != null) {
                                Text(
                                    text = "/ $pronunciation /",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun MultipleChoiceExerciseView(
    options: List<String>,
    selectedOption: String?,
    onSelectOption: (String) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        options.forEachIndexed { index, option ->
            val isSelected = selectedOption == option
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .clickable { onSelectOption(option) }
                    .border(
                        width = if (isSelected) 2.5.dp else 1.dp,
                        color = if (isSelected) PrimaryIndigo else MaterialTheme.colorScheme.outline.copy(alpha = 0.4f),
                        shape = RoundedCornerShape(16.dp)
                    )
                    .testTag("option_item_$index"),
                color = if (isSelected) PrimaryIndigo.copy(alpha = 0.12f) else MaterialTheme.colorScheme.surface,
                tonalElevation = if (isSelected) 4.dp else 1.dp
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 14.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(28.dp)
                            .clip(CircleShape)
                            .background(
                                if (isSelected) PrimaryIndigo else MaterialTheme.colorScheme.surfaceVariant
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "${('A' + index)}",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    Text(
                        text = option,
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }
    }
}

@Composable
fun WordMatchingExerciseView(
    pairs: List<MatchingPair>,
    matchedPairs: Map<String, String>,
    selectedLeft: String?,
    selectedRight: String?,
    onSelectLeft: (String) -> Unit,
    onSelectRight: (String) -> Unit
) {
    val leftItems = remember(pairs) { pairs.map { it.left }.shuffled() }
    val rightItems = remember(pairs) { pairs.map { it.right }.shuffled() }

    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Left column
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            leftItems.forEach { left ->
                val isMatched = matchedPairs.containsKey(left)
                val isSelected = selectedLeft == left

                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(14.dp))
                        .clickable(enabled = !isMatched) { onSelectLeft(left) }
                        .border(
                            width = if (isSelected || isMatched) 2.dp else 1.dp,
                            color = when {
                                isMatched -> AccentEmerald
                                isSelected -> PrimaryIndigo
                                else -> MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
                            },
                            shape = RoundedCornerShape(14.dp)
                        ),
                    color = when {
                        isMatched -> AccentEmerald.copy(alpha = 0.15f)
                        isSelected -> PrimaryIndigo.copy(alpha = 0.15f)
                        else -> MaterialTheme.colorScheme.surface
                    }
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = left,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        if (isMatched) {
                            Icon(Icons.Default.Check, contentDescription = "Matched", tint = AccentEmerald, modifier = Modifier.size(18.dp))
                        }
                    }
                }
            }
        }

        // Right column
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            rightItems.forEach { right ->
                val isMatched = matchedPairs.containsValue(right)
                val isSelected = selectedRight == right

                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(14.dp))
                        .clickable(enabled = !isMatched) { onSelectRight(right) }
                        .border(
                            width = if (isSelected || isMatched) 2.dp else 1.dp,
                            color = when {
                                isMatched -> AccentEmerald
                                isSelected -> PrimaryIndigo
                                else -> MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
                            },
                            shape = RoundedCornerShape(14.dp)
                        ),
                    color = when {
                        isMatched -> AccentEmerald.copy(alpha = 0.15f)
                        isSelected -> PrimaryIndigo.copy(alpha = 0.15f)
                        else -> MaterialTheme.colorScheme.surface
                    }
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = right,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        if (isMatched) {
                            Icon(Icons.Default.Check, contentDescription = "Matched", tint = AccentEmerald, modifier = Modifier.size(18.dp))
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ArrangeSentenceExerciseView(
    arrangedWords: List<String>,
    availableWords: List<String>,
    onPickWord: (String) -> Unit,
    onRemoveWord: (String) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Arranged sentence target slot box
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .defaultMinSize(minHeight = 80.dp)
                .clip(RoundedCornerShape(16.dp))
                .border(1.5.dp, PrimaryIndigo.copy(alpha = 0.4f), RoundedCornerShape(16.dp)),
            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
        ) {
            if (arrangedWords.isEmpty()) {
                Box(
                    modifier = Modifier.padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Tap words below to arrange the sentence in correct order",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                        textAlign = TextAlign.Center
                    )
                }
            } else {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    arrangedWords.forEach { word ->
                        Surface(
                            modifier = Modifier
                                .clip(RoundedCornerShape(10.dp))
                                .clickable { onRemoveWord(word) },
                            color = PrimaryIndigo,
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Text(
                                text = word,
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                                fontSize = 14.sp
                            )
                        }
                    }
                }
            }
        }

        Text(
            text = "Available Words:",
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            fontWeight = FontWeight.Bold
        )

        // Available words pool
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            availableWords.forEach { word ->
                Surface(
                    modifier = Modifier
                        .clip(RoundedCornerShape(10.dp))
                        .clickable { onPickWord(word) }
                        .border(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.5f), RoundedCornerShape(10.dp)),
                    color = MaterialTheme.colorScheme.surface,
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text(
                        text = word,
                        color = MaterialTheme.colorScheme.onSurface,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                        fontSize = 14.sp
                    )
                }
            }
        }
    }
}

@Composable
fun SpeakingExerciseView(
    targetPhrase: String,
    speechState: SpeechState,
    score: Int?,
    spokenText: String?,
    onStartSpeak: () -> Unit,
    onStopSpeak: () -> Unit,
    onManualVerify: () -> Unit
) {
    val context = LocalContext.current
    var hasRecordPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(context, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED
        )
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        hasRecordPermission = isGranted
        if (isGranted) onStartSpeak()
    }

    Column(
        modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Target phrase big card
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "“ $targetPhrase ”",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = PrimaryIndigo,
                    textAlign = TextAlign.Center
                )
            }
        }

        // Live Speech Feedback
        if (spokenText != null) {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                color = MaterialTheme.colorScheme.surface,
                border = BorderStroke(1.dp, PrimaryIndigo.copy(alpha = 0.3f))
            ) {
                Column(modifier = Modifier.padding(12.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = "We heard:", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Text(text = spokenText, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                    if (score != null) {
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Pronunciation Accuracy: $score%",
                            fontWeight = FontWeight.ExtraBold,
                            color = if (score >= 60) AccentEmerald else AccentAmber,
                            fontSize = 14.sp
                        )
                    }
                }
            }
        }

        // Microphone Button
        val isListening = speechState is SpeechState.Listening
        Box(
            modifier = Modifier
                .size(76.dp)
                .clip(CircleShape)
                .background(
                    if (isListening) AccentRose else PrimaryIndigo
                )
                .clickable {
                    if (!hasRecordPermission) {
                        permissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
                    } else {
                        if (isListening) onStopSpeak() else onStartSpeak()
                    }
                }
                .testTag("speak_microphone_button"),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = if (isListening) Icons.Default.MicOff else Icons.Default.Mic,
                contentDescription = "Speak Mic",
                tint = Color.White,
                modifier = Modifier.size(36.dp)
            )
        }

        Text(
            text = if (isListening) "🎙️ Listening... Speak now" else "Tap microphone & read aloud",
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.SemiBold,
            color = if (isListening) AccentRose else MaterialTheme.colorScheme.onSurfaceVariant
        )

        // Manual Verify fallback button for seamless testing & noisy environments
        TextButton(onClick = onManualVerify) {
            Text("I spoke it correctly (Manual Verify)", fontSize = 12.sp, color = PrimaryIndigo)
        }
    }
}

@Composable
fun ConversationExerciseView(
    dialogueTurns: List<DialogueTurn>,
    options: List<String>,
    selectedOption: String?,
    onSelectOption: (String) -> Unit,
    onPlayAudio: (String) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth().padding(vertical = 10.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Chat bubbles
        dialogueTurns.forEachIndexed { index, turn ->
            val isAi = turn.speaker != "You"
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = if (isAi) Arrangement.Start else Arrangement.End
            ) {
                Surface(
                    modifier = Modifier
                        .widthIn(max = 280.dp)
                        .clip(RoundedCornerShape(16.dp)),
                    color = if (isAi) MaterialTheme.colorScheme.surfaceVariant else PrimaryIndigo,
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Text(text = turn.avatar, fontSize = 16.sp)
                            Text(
                                text = turn.speaker,
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp,
                                color = if (isAi) MaterialTheme.colorScheme.primary else Color.White
                            )
                            Spacer(modifier = Modifier.weight(1f))
                            if (turn.audioText != null) {
                                IconButton(
                                    onClick = { onPlayAudio(turn.audioText) },
                                    modifier = Modifier.size(24.dp)
                                ) {
                                    Icon(
                                        Icons.Default.VolumeUp,
                                        contentDescription = "Play audio",
                                        tint = if (isAi) PrimaryIndigo else Color.White,
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                            }
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = turn.text,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 14.sp,
                            color = if (isAi) MaterialTheme.colorScheme.onSurface else Color.White
                        )
                        Text(
                            text = turn.translation,
                            fontSize = 11.sp,
                            color = if (isAi) MaterialTheme.colorScheme.onSurfaceVariant else Color.White.copy(alpha = 0.8f)
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Your response:",
            fontWeight = FontWeight.Bold,
            fontSize = 13.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        // Options to choose for user turn
        options.forEachIndexed { index, option ->
            val isSelected = selectedOption == option
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    .clickable { onSelectOption(option) }
                    .border(
                        width = if (isSelected) 2.dp else 1.dp,
                        color = if (isSelected) PrimaryIndigo else MaterialTheme.colorScheme.outline.copy(alpha = 0.3f),
                        shape = RoundedCornerShape(14.dp)
                    ),
                color = if (isSelected) PrimaryIndigo.copy(alpha = 0.1f) else MaterialTheme.colorScheme.surface
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text(text = "💬", fontSize = 16.sp)
                    Text(
                        text = option,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                        fontSize = 14.sp
                    )
                }
            }
        }
    }
}
