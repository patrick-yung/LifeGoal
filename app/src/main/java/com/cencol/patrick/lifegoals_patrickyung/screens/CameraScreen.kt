package com.cencol.patrick.lifegoals_patrickyung.ui.screens

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.FileProvider
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CameraScreen(
    achievementId: String,
    onPhotoTaken: (String) -> Unit,
    onCancel: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var capturedImage by remember { mutableStateOf<Bitmap?>(null) }
    var currentPhotoPath by remember { mutableStateOf<String?>(null) }
    var photoCount by remember { mutableStateOf(0) }

    val targetPhotoCount = 3 // This should come from the achievement

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success ->
        if (success) {
            currentPhotoPath?.let { path ->
                val bitmap = BitmapFactory.decodeFile(path)
                capturedImage = bitmap
                photoCount++
            }
        }
    }

    val createTempFileLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.CreateDocument("image/jpg")
    ) { uri ->
        uri?.let {
            capturedImage?.let { bitmap ->
                context.contentResolver.openOutputStream(uri)?.use { outputStream ->
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 90, outputStream)
                }
                onPhotoTaken(uri.toString())
            }
        }
    }

    fun dispatchTakePictureIntent() {
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val storageDir = context.cacheDir
        val photoFile = File.createTempFile(
            "JPEG_${timeStamp}_",
            ".jpg",
            storageDir
        )
        currentPhotoPath = photoFile.absolutePath

        val photoURI = FileProvider.getUriForFile(
            context,
            "${context.packageName}.fileprovider",
            photoFile
        )
        cameraLauncher.launch(photoURI)
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFF5F7FA))
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Take photos to complete achievement",
            fontSize = 18.sp,
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        capturedImage?.let {
            Image(
                bitmap = it.asImageBitmap(),
                contentDescription = "Captured image",
                modifier = Modifier
                    .size(300.dp)
                    .padding(bottom = 16.dp)
            )
        } ?: Box(
            modifier = Modifier
                .size(300.dp)
                .background(Color.DarkGray)
                .padding(bottom = 16.dp)
        )

        Text(
            text = "Photos taken: $photoCount/$targetPhotoCount",
            fontSize = 16.sp,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Button(
            onClick = { dispatchTakePictureIntent() },
            modifier = Modifier.padding(bottom = 16.dp)
        ) {
            Text("Take Photo")
        }

        Row(
            horizontalArrangement = Arrangement.Center
        ) {
            Button(
                onClick = {
                    capturedImage?.let { bitmap ->
                        createTempFileLauncher.launch("photo_${System.currentTimeMillis()}.jpg")
                    }
                },
                enabled = capturedImage != null,
                modifier = Modifier.padding(end = 16.dp)
            ) {
                Text("Complete")
            }

            Button(
                onClick = onCancel
            ) {
                Text("Cancel")
            }
        }
    }
}