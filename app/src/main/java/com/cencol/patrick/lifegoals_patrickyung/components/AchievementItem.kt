package com.cencol.patrick.lifegoals_patrickyung.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star  // Changed from EmojiEvents
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cencol.patrick.lifegoals_patrickyung.ui.screens.Achievement

@Composable
fun AchievementItem(
    achievement: Achievement,
    onCheckedChange: (Boolean) -> Unit,
    onTakePhoto: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        shape = MaterialTheme.shapes.small,
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Using Star instead of EmojiEvents
                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = null,
                    modifier = Modifier.size(40.dp),
                    tint = Color(0xFF9C27B0)
                )

                Spacer(modifier = Modifier.width(16.dp))

                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = achievement.title,
                        fontSize = 18.sp,
                        style = MaterialTheme.typography.titleMedium,
                        color = Color(0xFF333333)
                    )
                    Text(
                        text = achievement.description,
                        fontSize = 14.sp,
                        color = Color(0xFF666666)
                    )
                }

                Checkbox(
                    checked = achievement.isCompleted,
                    onCheckedChange = onCheckedChange,
                    colors = CheckboxDefaults.colors(
                        checkedColor = Color(0xFF9C27B0)
                    )
                )
            }

            if (achievement.requiresPhotos) {
                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "📷 Taken Photos: ${achievement.photos.size}/${achievement.targetPhotoCount}",
                        fontSize = 14.sp,
                        style = MaterialTheme.typography.bodyMedium
                    )

                    if (achievement.photos.size < achievement.targetPhotoCount && !achievement.isCompleted) {
                        Button(
                            onClick = onTakePhoto,
                            modifier = Modifier.height(32.dp)
                        ) {
                            Text("Take Photo", fontSize = 12.sp)
                        }
                    }
                }

                if (achievement.photos.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(8.dp))

                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(achievement.photos) { photoPath ->
                            PhotoThumbnail(photoPath = photoPath)
                        }
                    }
                }
            }
        }
    }
}