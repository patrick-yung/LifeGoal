package com.cencol.patrick.lifegoals_patrickyung.ui.components

import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize  // ADD THIS IMPORT
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp

@Composable
fun PhotoThumbnail(
    photoPath: String,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val bitmap = try {
        BitmapFactory.decodeFile(photoPath)
    } catch (e: Exception) {
        null
    }

    Box(
        modifier = modifier
            .size(80.dp)
            .background(Color.DarkGray)
    ) {
        bitmap?.let {
            Image(
                bitmap = it.asImageBitmap(),
                contentDescription = "Photo thumbnail",
                modifier = Modifier.fillMaxSize(),  // This now works with the import
                contentScale = ContentScale.Crop
            )
        }
    }
}