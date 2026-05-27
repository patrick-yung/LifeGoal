package com.cencol.patrick.lifegoals_patrickyung.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.background  // ADD THIS IMPORT
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star  // Changed from EmojiEvents to Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    onTimeout: () -> Unit,
    modifier: Modifier = Modifier
) {
    LaunchedEffect(Unit) {
        delay(2000) // Show splash screen for 2 seconds
        onTimeout()
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFF9C27B0)), // Purple 500
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Changed from Icons.Default.EmojiEvents to Icons.Default.Star
            Icon(
                imageVector = Icons.Default.Star,
                contentDescription = "Logo",
                modifier = Modifier.size(120.dp),
                tint = Color.White
            )

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "Life Goals",
                color = Color.White,
                fontSize = 28.sp,
                style = MaterialTheme.typography.headlineMedium
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Track your achievements",
                color = Color.White.copy(alpha = 0.8f),
                fontSize = 16.sp
            )
        }
    }
}