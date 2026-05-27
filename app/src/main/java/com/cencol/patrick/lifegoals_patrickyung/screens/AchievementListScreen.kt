package com.cencol.patrick.lifegoals_patrickyung.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.cencol.patrick.lifegoals_patrickyung.ui.components.AchievementItem
import com.cencol.patrick.lifegoals_patrickyung.ui.components.TopAppBar

data class Achievement(
    val id: String,
    val title: String,
    val description: String,
    val isCompleted: Boolean = false,
    val requiresPhotos: Boolean = false,
    val photos: List<String> = emptyList(),
    val targetPhotoCount: Int = 0
)

@Composable
fun AchievementListScreen(
    categoryId: String,
    onBackPressed: () -> Unit,
    onTakePhoto: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var achievements by remember { mutableStateOf(getAchievementsForCategory(categoryId)) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFF5F7FA))
    ) {
        TopAppBar(
            title = when (categoryId) {
                "basic" -> "Basic Achievements"
                "health" -> "Health Achievements"
                "learning" -> "Learning Achievements"
                else -> "Achievements"
            },
            onNavigationClick = onBackPressed
        )

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(achievements) { achievement ->
                AchievementItem(
                    achievement = achievement,
                    onCheckedChange = { isChecked: Boolean ->
                        achievements = achievements.map {
                            if (it.id == achievement.id) it.copy(isCompleted = isChecked)
                            else it
                        }
                    },
                    onTakePhoto = { onTakePhoto(achievement.id) }
                )
            }
        }
    }
}

private fun getAchievementsForCategory(categoryId: String): List<Achievement> {
    return when (categoryId) {
        "basic" -> listOf(
            Achievement(id = "basic_1", title = "Wake up before 7 AM", description = "Wake up before 7 AM for 30 consecutive days", isCompleted = false, requiresPhotos = false, photos = emptyList(), targetPhotoCount = 0),
            Achievement(id = "basic_2", title = "Complete daily tasks", description = "Complete all daily tasks for 7 days", isCompleted = false, requiresPhotos = false, photos = emptyList(), targetPhotoCount = 0),
            Achievement(id = "basic_3", title = "Photo Challenge", description = "Take 5 photos of nature", isCompleted = false, requiresPhotos = true, photos = emptyList(), targetPhotoCount = 5),
            Achievement(id = "basic_4", title = "Exercise routine", description = "Exercise for 30 minutes daily", isCompleted = false, requiresPhotos = false, photos = emptyList(), targetPhotoCount = 0),
            Achievement(id = "basic_5", title = "Read a book", description = "Read 10 pages every day", isCompleted = false, requiresPhotos = false, photos = emptyList(), targetPhotoCount = 0)
        )
        else -> listOf(
            Achievement(id = "ach_1", title = "Sample Achievement", description = "Complete this achievement", isCompleted = false, requiresPhotos = false, photos = emptyList(), targetPhotoCount = 0),
            Achievement(id = "ach_2", title = "Photo Achievement", description = "Take 3 photos", isCompleted = false, requiresPhotos = true, photos = emptyList(), targetPhotoCount = 3)
        )
    }
}