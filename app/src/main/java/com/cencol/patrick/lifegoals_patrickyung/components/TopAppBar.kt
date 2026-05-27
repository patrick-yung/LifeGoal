package com.cencol.patrick.lifegoals_patrickyung.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier  // ADD THIS IMPORT
import androidx.compose.ui.graphics.Color

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopAppBar(
    title: String,
    onNavigationClick: (() -> Unit)?,
    modifier: Modifier = Modifier  // Now Modifier is imported
) {
    CenterAlignedTopAppBar(
        title = { Text(title, color = Color.White) },
        navigationIcon = {
            if (onNavigationClick != null) {
                IconButton(onClick = onNavigationClick) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        tint = Color.White
                    )
                }
            }
        },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = Color(0xFF9C27B0) // Purple 500
        ),
        modifier = modifier
    )
}