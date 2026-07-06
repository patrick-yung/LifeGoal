package com.cencol.patrick.lifegoals_patrickyung.location

import android.Manifest
import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.util.Log
import com.cencol.patrick.lifegoals_patrickyung.utils.LocationPermissionHandler
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.withTimeoutOrNull
import java.util.Locale

class LocationService(private val context: Context) {
    private val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)

    private val _currentLocation = MutableStateFlow<Location?>(null)
    val currentLocation: StateFlow<Location?> = _currentLocation.asStateFlow()

    private val _currentAddress = MutableStateFlow<String?>(null)
    val currentAddress: StateFlow<String?> = _currentAddress.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    suspend fun getCurrentLocation(): Location? {
        Log.d("LocationService", "Getting current location...")

        if (!LocationPermissionHandler.hasLocationPermission(context)) {
            Log.w("LocationService", "Location permission not granted")
            _error.value = "Location permission not granted"
            return null
        }

        return try {
            val result = withTimeoutOrNull(3000) {
                val deferred = CompletableDeferred<Location?>()

                fusedLocationClient.lastLocation
                    .addOnSuccessListener { location ->
                        if (location != null) {
                            Log.d("LocationService", "Location received: ${location.latitude}, ${location.longitude}")
                            _currentLocation.value = location
                            deferred.complete(location)
                        } else {
                            Log.w("LocationService", "Location is null")
                            deferred.complete(null)
                        }
                    }
                    .addOnFailureListener { exception ->
                        Log.e("LocationService", "Failed to get location", exception)
                        deferred.completeExceptionally(exception)
                    }

                deferred.await()
            }

            if (result == null) {
                _error.value = "Location request timed out. Please set location in emulator."
            }

            result
        } catch (e: Exception) {
            Log.e("LocationService", "Error: ${e.message}", e)
            _error.value = e.message
            null
        }
    }

    suspend fun getAddressFromLocation(location: Location): String? {
        return try {
            val geocoder = Geocoder(context, Locale.getDefault())
            val addresses: List<Address>? = geocoder.getFromLocation(
                location.latitude,
                location.longitude,
                1 // Max results
            )

            if (!addresses.isNullOrEmpty()) {
                val address = addresses[0]
                val fullAddress = StringBuilder()

                // Get the most complete address available
                val addressLine = address.getAddressLine(0)
                if (!addressLine.isNullOrEmpty()) {
                    fullAddress.append(addressLine)
                } else {
                    // Build address from components
                    val locality = address.locality ?: ""
                    val subLocality = address.subLocality ?: ""
                    val thoroughfare = address.thoroughfare ?: ""
                    val featureName = address.featureName ?: ""
                    val adminArea = address.adminArea ?: ""
                    val countryName = address.countryName ?: ""
                    val postalCode = address.postalCode ?: ""

                    if (featureName.isNotEmpty()) fullAddress.append(featureName).append(", ")
                    if (thoroughfare.isNotEmpty()) fullAddress.append(thoroughfare).append(", ")
                    if (subLocality.isNotEmpty()) fullAddress.append(subLocality).append(", ")
                    if (locality.isNotEmpty()) fullAddress.append(locality).append(", ")
                    if (adminArea.isNotEmpty()) fullAddress.append(adminArea).append(", ")
                    if (postalCode.isNotEmpty()) fullAddress.append(postalCode).append(", ")
                    if (countryName.isNotEmpty()) fullAddress.append(countryName)
                }

                val result = fullAddress.toString().trimEnd(',', ' ')
                _currentAddress.value = result
                Log.d("LocationService", "Address: $result")
                result
            } else {
                _currentAddress.value = null
                null
            }
        } catch (e: Exception) {
            Log.e("LocationService", "Error getting address: ${e.message}", e)
            _currentAddress.value = null
            null
        }
    }

    suspend fun getLocationWithAddress(): Pair<Location?, String?> {
        val location = getCurrentLocation()
        val address = if (location != null) {
            getAddressFromLocation(location)
        } else {
            null
        }
        return Pair(location, address)
    }
}