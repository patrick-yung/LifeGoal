// LocationPermissionHandler.kt
package com.cencol.patrick.lifegoals_patrickyung.utils

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState
import com.google.accompanist.permissions.isGranted

@OptIn(ExperimentalPermissionsApi::class)
object LocationPermissionHandler {

    fun hasLocationPermission(context: Context): Boolean {
        val fineLocation = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        val coarseLocation = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        return fineLocation && coarseLocation
    }

    fun checkAllPermissionsGranted(
        fineLocationState: PermissionState,
        coarseLocationState: PermissionState
    ): Boolean {
        return fineLocationState.status.isGranted &&
                coarseLocationState.status.isGranted
    }

    fun requestPermissions(
        fineLocationState: PermissionState,
        coarseLocationState: PermissionState
    ) {
        fineLocationState.launchPermissionRequest()
        coarseLocationState.launchPermissionRequest()
    }
}