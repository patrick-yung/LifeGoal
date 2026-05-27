package com.cencol.patrick.lifegoals_patrickyung

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.cencol.patrick.lifegoals_patrickyung.ui.screens.AchievementListScreen
import com.cencol.patrick.lifegoals_patrickyung.ui.screens.CameraScreen
import com.cencol.patrick.lifegoals_patrickyung.ui.screens.HomeScreen
import com.cencol.patrick.lifegoals_patrickyung.ui.theme.LifeGoals_PatrickYungTheme

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

    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = "home",  // Changed from "splash" to "home"
            modifier = Modifier.fillMaxSize()
        ) {
            composable("home") {
                HomeScreen(
                    onCategoryClick = { categoryId ->
                        navController.navigate("achievements/$categoryId")
                    }
                )
            }
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