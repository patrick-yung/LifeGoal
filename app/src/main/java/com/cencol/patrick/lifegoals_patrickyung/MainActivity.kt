package com.cencol.patrick.lifegoals_patrickyung

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember  // ADD THIS IMPORT
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.cencol.patrick.lifegoals_patrickyung.location.LocationService
import com.cencol.patrick.lifegoals_patrickyung.ui.screens.AchievementListScreen
import com.cencol.patrick.lifegoals_patrickyung.ui.screens.CameraScreen
import com.cencol.patrick.lifegoals_patrickyung.ui.screens.HomeScreen
import com.cencol.patrick.lifegoals_patrickyung.location.LocationPermissionScreen
import com.cencol.patrick.lifegoals_patrickyung.ui.theme.LifeGoals_PatrickYungTheme
import com.cencol.patrick.lifegoals_patrickyung.utils.LocationPermissionHandler

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            LifeGoals_PatrickYungTheme {
                LifeGoalsApp()
            }
        }
    }
}

@Composable
fun LifeGoalsApp() {
    val navController = rememberNavController()
    val context = LocalContext.current
    val locationService = remember { LocationService(context) }  // Now 'remember' is imported

    // Check if location permission is already granted
    val hasLocationPermission = LocationPermissionHandler.hasLocationPermission(context)

    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = if (hasLocationPermission) "home" else "location_permission",
            modifier = Modifier.fillMaxSize()
        ) {
            // Location Permission Screen
            composable("location_permission") {
                LocationPermissionScreen(
                    onPermissionGranted = {
                        navController.navigate("home") {
                            popUpTo("location_permission") { inclusive = true }
                        }
                    },
                    onPermissionDenied = {
                        // Navigate to home even without permission
                        navController.navigate("home") {
                            popUpTo("location_permission") { inclusive = true }
                        }
                    }
                )
            }

            // Home Screen
            composable("home") {
                HomeScreen(
                    onCategoryClick = { categoryId ->
                        navController.navigate("achievements/$categoryId")
                    }
                )
            }

            // Achievement List Screen
            composable("achievements/{categoryId}") { backStackEntry ->
                val categoryId = backStackEntry.arguments?.getString("categoryId") ?: "basic"
                AchievementListScreen(
                    categoryId = categoryId,
                    onBackPressed = { navController.popBackStack() },
                    onTakePhoto = { achievementId ->
                        navController.navigate("camera/$achievementId")
                    }
                )
            }

            // Camera Screen
            composable("camera/{achievementId}") { backStackEntry ->
                val achievementId = backStackEntry.arguments?.getString("achievementId") ?: ""
                CameraScreen(
                    achievementId = achievementId,
                    onPhotoTaken = { photoPath ->
                        navController.popBackStack()
                    },
                    onCancel = { navController.popBackStack() }
                )
            }
        }
    }
}