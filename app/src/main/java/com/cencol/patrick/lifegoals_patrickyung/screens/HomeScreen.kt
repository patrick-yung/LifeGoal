package com.cencol.patrick.lifegoals_patrickyung.ui.screens

import android.Manifest
import android.location.Location
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cencol.patrick.lifegoals_patrickyung.location.LocationService
import com.cencol.patrick.lifegoals_patrickyung.ui.components.Category
import com.cencol.patrick.lifegoals_patrickyung.ui.components.CategoryItem
import com.cencol.patrick.lifegoals_patrickyung.ui.components.TopAppBar
import com.cencol.patrick.lifegoals_patrickyung.utils.LocationPermissionHandler
import kotlinx.coroutines.launch

@Composable
fun HomeScreen(
    onCategoryClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val locationService = remember { LocationService(context) }

    // Location states
    var locationText by remember { mutableStateOf("⏳ Getting location...") }
    var isLocationLoading by remember { mutableStateOf(false) }
    var hasLocationError by remember { mutableStateOf(false) }
    var location by remember { mutableStateOf<Location?>(null) }
    var address by remember { mutableStateOf<String?>(null) }

    // Check if we have permission
    val hasLocationPermission = LocationPermissionHandler.hasLocationPermission(context)

    // Function to get location with address
    fun fetchLocation() {
        if (hasLocationPermission) {
            scope.launch {
                isLocationLoading = true
                hasLocationError = false
                locationText = "⏳ Getting location..."

                try {
                    // Get location and address together
                    val (loc, addr) = locationService.getLocationWithAddress()

                    if (loc != null) {
                        location = loc
                        address = addr

                        // Display address if available, otherwise show coordinates
                        locationText = if (!addr.isNullOrEmpty()) {
                            "📍 $addr"
                        } else {
                            "📍 ${String.format("%.6f", loc.latitude)}, ${String.format("%.6f", loc.longitude)}"
                        }
                        hasLocationError = false
                    } else {
                        locationText = "⚠️ No location available. Set location in emulator."
                        hasLocationError = true
                    }
                } catch (e: Exception) {
                    locationText = "❌ Error: ${e.message}"
                    hasLocationError = true
                }

                isLocationLoading = false
            }
        } else {
            locationText = "🔒 Location permission required"
            hasLocationError = true
        }
    }

    // Get location when the screen loads (only once)
    LaunchedEffect(Unit) {
        fetchLocation()
    }

    val categories: List<Category> = listOf(
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

        // Location Status Card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            colors = CardDefaults.cardColors(
                containerColor = when {
                    hasLocationError -> MaterialTheme.colorScheme.errorContainer
                    hasLocationPermission -> MaterialTheme.colorScheme.secondaryContainer
                    else -> MaterialTheme.colorScheme.errorContainer
                }
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = if (hasLocationPermission) "📍 Current Location" else "🔒 Location Status",
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = when {
                            hasLocationError -> MaterialTheme.colorScheme.onErrorContainer
                            hasLocationPermission -> MaterialTheme.colorScheme.onSecondaryContainer
                            else -> MaterialTheme.colorScheme.onErrorContainer
                        }
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = if (isLocationLoading) "⏳ Loading..." else locationText,
                        fontSize = 12.sp,
                        color = when {
                            hasLocationError -> MaterialTheme.colorScheme.onErrorContainer.copy(alpha = 0.8f)
                            hasLocationPermission -> MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha = 0.8f)
                            else -> MaterialTheme.colorScheme.onErrorContainer.copy(alpha = 0.8f)
                        }
                    )
                }

                // Refresh button
                Button(
                    onClick = { fetchLocation() },
                    modifier = Modifier
                        .height(32.dp)
                        .width(if (hasLocationPermission) 80.dp else 100.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = when {
                            hasLocationError && hasLocationPermission -> MaterialTheme.colorScheme.error
                            hasLocationPermission -> MaterialTheme.colorScheme.primary
                            else -> MaterialTheme.colorScheme.error
                        }
                    ),
                    enabled = !isLocationLoading
                ) {
                    Text(
                        text = when {
                            isLocationLoading -> "Loading"
                            hasLocationError && hasLocationPermission -> "Retry"
                            hasLocationPermission -> "Refresh"
                            else -> "Enable"
                        },
                        fontSize = 12.sp
                    )
                }
            }
        }

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