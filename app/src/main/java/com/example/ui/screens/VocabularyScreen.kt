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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.VocabularyEntity
import com.example.ui.theme.*

@Composable
fun VocabularyScreen(
    vocabularyList: List<VocabularyEntity>,
    searchQuery: String,
    selectedCategory: String?,
    onlyFavorites: Boolean,
    onSearchChange: (String) -> Unit,
    onCategorySelect: (String?) -> Unit,
    onToggleFavoritesOnly: () -> Unit,
    onToggleFavorite: (Long, Boolean) -> Unit,
    onPlayAudio: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val categories = listOf("All", "Greetings", "Food", "Education", "People", "Travel", "Career")

    val filteredList = remember(vocabularyList, searchQuery, selectedCategory, onlyFavorites) {
        vocabularyList.filter { item ->
            val matchesSearch = searchQuery.isBlank() ||
                    item.word.contains(searchQuery, ignoreCase = true) ||
                    item.translation.contains(searchQuery, ignoreCase = true)
            val matchesCategory = selectedCategory == null || selectedCategory == "All" || item.category.equals(selectedCategory, ignoreCase = true)
            val matchesFav = !onlyFavorites || item.isFavorite
            matchesSearch && matchesCategory && matchesFav
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(VibrantBg)
            .padding(horizontal = 16.dp)
    ) {
        Spacer(modifier = Modifier.height(12.dp))

        // Search Bar & Favorites Filter
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = onSearchChange,
                modifier = Modifier
                    .weight(1f)
                    .testTag("vocab_search_field"),
                placeholder = { Text("Search words or meanings...") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search", tint = VibrantPurple) },
                trailingIcon = {
                    if (searchQuery.isNotEmpty()) {
                        IconButton(onClick = { onSearchChange("") }) {
                            Icon(Icons.Default.Close, contentDescription = "Clear")
                        }
                    }
                },
                singleLine = true,
                shape = RoundedCornerShape(16.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = VibrantPurple,
                    unfocusedBorderColor = VibrantOutline.copy(alpha = 0.5f),
                    focusedContainerColor = VibrantSurface,
                    unfocusedContainerColor = VibrantSurface
                )
            )

            // Favorites Filter Toggle Button
            IconButton(
                onClick = onToggleFavoritesOnly,
                modifier = Modifier
                    .size(50.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(
                        if (onlyFavorites) AccentRose.copy(alpha = 0.15f) else VibrantSurfaceVariant
                    )
            ) {
                Icon(
                    imageVector = if (onlyFavorites) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                    contentDescription = "Favorites",
                    tint = if (onlyFavorites) AccentRose else VibrantOnSurfaceVariant
                )
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Category Filter Chips
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(vertical = 4.dp)
        ) {
            items(categories) { cat ->
                val isSelected = (cat == "All" && selectedCategory == null) || (cat == selectedCategory)
                FilterChip(
                    selected = isSelected,
                    onClick = {
                        onCategorySelect(if (cat == "All") null else cat)
                    },
                    label = { Text(cat, fontSize = 12.sp, fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal) },
                    shape = RoundedCornerShape(12.dp),
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = VibrantPurple,
                        selectedLabelColor = Color.White
                    )
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Vocabulary Items List
        if (filteredList.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = 96.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = "🔍", fontSize = 48.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "No vocabulary found",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = VibrantOnBg
                    )
                    Text(
                        text = "Try adjusting your search or category filter",
                        style = MaterialTheme.typography.bodySmall,
                        color = VibrantOnSurfaceVariant
                    )
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(bottom = 96.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(filteredList, key = { it.id }) { item ->
                    VocabCardItem(
                        item = item,
                        onToggleFavorite = { onToggleFavorite(item.id, item.isFavorite) },
                        onPlayAudio = { onPlayAudio(item.word) },
                        onPlayExampleAudio = { onPlayAudio(item.exampleTarget) }
                    )
                }
            }
        }
    }
}

@Composable
fun VocabCardItem(
    item: VocabularyEntity,
    onToggleFavorite: () -> Unit,
    onPlayAudio: () -> Unit,
    onPlayExampleAudio: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = VibrantSurface),
        border = BorderStroke(1.dp, VibrantOutline.copy(alpha = 0.5f)),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text(text = item.visualEmoji, fontSize = 24.sp)
                    Column {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Text(
                                text = item.word,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = VibrantPurple
                            )
                            if (item.pronunciation.isNotEmpty()) {
                                Text(
                                    text = "/${item.pronunciation}/",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = VibrantOnSurfaceVariant
                                )
                            }
                        }
                        Text(
                            text = item.translation,
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.SemiBold,
                            color = VibrantOnBg
                        )
                    }
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = onPlayAudio, modifier = Modifier.size(36.dp)) {
                        Icon(Icons.Default.VolumeUp, contentDescription = "Audio", tint = VibrantPurple)
                    }
                    IconButton(onClick = onToggleFavorite, modifier = Modifier.size(36.dp)) {
                        Icon(
                            imageVector = if (item.isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                            contentDescription = "Favorite",
                            tint = if (item.isFavorite) AccentRose else VibrantOutline
                        )
                    }
                }
            }

            if (item.exampleTarget.isNotEmpty()) {
                Spacer(modifier = Modifier.height(8.dp))
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp),
                    color = VibrantSurfaceVariant.copy(alpha = 0.5f)
                ) {
                    Row(
                        modifier = Modifier.padding(8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "“${item.exampleTarget}”",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = VibrantOnBg
                            )
                            Text(
                                text = item.exampleTranslation,
                                fontSize = 11.sp,
                                color = VibrantOnSurfaceVariant
                            )
                        }
                        IconButton(onClick = onPlayExampleAudio, modifier = Modifier.size(28.dp)) {
                            Icon(Icons.Default.VolumeUp, contentDescription = "Play Example", tint = VibrantPurple, modifier = Modifier.size(16.dp))
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Badges: Category, Difficulty, Review status
            Row(
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = VibrantPurpleContainer
                ) {
                    Text(
                        text = item.category,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = VibrantPurpleDeep,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }

                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = when (item.difficulty) {
                        "Easy" -> AccentEmerald.copy(alpha = 0.15f)
                        "Medium" -> AccentAmber.copy(alpha = 0.15f)
                        else -> AccentRose.copy(alpha = 0.15f)
                    }
                ) {
                    Text(
                        text = item.difficulty,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = when (item.difficulty) {
                            "Easy" -> AccentEmerald
                            "Medium" -> AccentAmber
                            else -> AccentRose
                        },
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }

                if (item.reviewCount > 0) {
                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = AccentEmerald.copy(alpha = 0.12f)
                    ) {
                        Text(
                            text = "Reviewed ${item.reviewCount}x",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = AccentEmerald,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                }
            }
        }
    }
}
