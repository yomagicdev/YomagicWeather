package com.yomagic.weather.domain.location

import android.location.Location
import com.yomagic.weather.domain.util.Resource

interface LocationTracker {

    suspend fun getCurrentLocation(): Resource<Location>
}