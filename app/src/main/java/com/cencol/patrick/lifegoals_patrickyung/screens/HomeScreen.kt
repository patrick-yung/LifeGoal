package com.cencol.patrick.lifegoals_patrickyung.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.cencol.patrick.lifegoals_patrickyung.ui.components.CategoryItem
import com.cencol.patrick.lifegoals_patrickyung.ui.components.TopAppBar

data class Category(
    val id: String,
    val title: String,
    val iconRes: Int,
    val achievementCount: Int,
    val progress: Int
)

@Composable
fun HomeScreen(
    onCategoryClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val categories = listOf(
        Category("basic", "Basic", android.R.drawable.ic_menu_edit, 5, 60),
        Category("health", "Health", android.R.drawable.ic_menu_edit, 3, 33),
        Category("learning", "Learning", android.R.drawable.ic_menu_edit, 4, 75),
        Category("social", "Social", android.R.drawable.ic_menu_edit, 6, 50)
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFF5F7FA))
    ) {
        TopAppBar(
            title = "Achievement Categories",
            onNavigationClick = null
        )

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(categories) { category ->
                CategoryItem(
                    category = category,
                    onClick = { onCategoryClick(category.id) }
                )
            }
        }
    }
}