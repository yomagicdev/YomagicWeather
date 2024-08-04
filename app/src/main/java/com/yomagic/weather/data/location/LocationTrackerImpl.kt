package com.yomagic.weather.data.location

import android.Manifest
import android.app.Application
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.yomagic.weather.domain.location.LocationTracker
import com.yomagic.weather.domain.util.Resource
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.suspendCancellableCoroutine
import javax.inject.Inject

class LocationTrackerImpl @Inject constructor(
    private val locationClient: FusedLocationProviderClient,
    private val application: Application,
): LocationTracker {
    @OptIn(ExperimentalCoroutinesApi::class)
    override suspend fun getCurrentLocation(): Resource<Location> {
        val hasAccessFineLocationPermission = ContextCompat.checkSelfPermission(
            application,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        val hasAccessCourseLocationPermission = ContextCompat.checkSelfPermission(
            application,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        val locationManager =
            application.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val isGpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)

        if (!hasAccessFineLocationPermission || !hasAccessCourseLocationPermission || !isGpsEnabled) {
            return Resource.Error("Location permission or GPS is not enabled")
        }

        return suspendCancellableCoroutine { cont ->
            locationClient.lastLocation.apply {
                if (isComplete) {
                    if (isSuccessful) {
                        cont.resume(Resource.Success(result), onCancellation = null)
                    } else {
                        cont.resume(Resource.Error("Location not found"), onCancellation = null)
                    }
                    return@suspendCancellableCoroutine
                }
                addOnSuccessListener {
                    cont.resume(Resource.Success(it), onCancellation = null)
                }
                addOnFailureListener {
                    cont.resume(
                        Resource.Error(it.message ?: "Location not found"),
                        onCancellation = null
                    )
                }
                addOnCanceledListener {
                    cont.cancel()
                }
            }
        }
    }
}