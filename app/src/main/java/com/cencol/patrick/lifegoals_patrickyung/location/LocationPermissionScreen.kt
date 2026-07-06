package com.cencol.patrick.lifegoals_patrickyung.location

import android.Manifest
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale  // ← IMPORT THIS

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun LocationPermissionScreen(
    onPermissionGranted: () -> Unit,
    onPermissionDenied: () -> Unit
) {
    val context = LocalContext.current

    val fineLocationPermissionState = rememberPermissionState(
        Manifest.permission.ACCESS_FINE_LOCATION
    )

    val coarseLocationPermissionState = rememberPermissionState(
        Manifest.permission.ACCESS_COARSE_LOCATION
    )

    val permissionsGranted = fineLocationPermissionState.status.isGranted &&
            coarseLocationPermissionState.status.isGranted

    // Handle permission results
    LaunchedEffect(permissionsGranted) {
        if (permissionsGranted) {
            onPermissionGranted()
        }
    }

    // Check if permission is permanently denied
    val shouldShowRationale = fineLocationPermissionState.status.shouldShowRationale ||
            coarseLocationPermissionState.status.shouldShowRationale

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "📍 Location Permission Required",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Life Goals needs location permission to:",
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(12.dp))

        Column(
            modifier = Modifier.padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("• Track your progress in different locations", textAlign = TextAlign.Center)
            Text("• Add location tags to your achievements", textAlign = TextAlign.Center)
            Text("• Show nearby goals and challenges", textAlign = TextAlign.Center)
        }

        Spacer(modifier = Modifier.height(32.dp))

        if (shouldShowRationale) {
            Text(
                text = "You've previously denied location permission. " +
                        "Please enable it in app settings to continue.",
                color = MaterialTheme.colorScheme.error,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 16.dp)
            )
        }

        Button(
            onClick = {
                fineLocationPermissionState.launchPermissionRequest()
                coarseLocationPermissionState.launchPermissionRequest()
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Grant Location Permission")
        }

        Spacer(modifier = Modifier.height(12.dp))

        Button(
            onClick = onPermissionDenied,
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer
            )
        ) {
            Text("Continue without Location")
        }
    }
}