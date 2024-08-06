package com.yomagic.weather.presentation

import android.app.Application
import android.location.Geocoder
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yomagic.weather.domain.location.LocationTracker
import com.yomagic.weather.domain.repository.WeatherRepository
import com.yomagic.weather.domain.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val weatherRepository: WeatherRepository,
    private val locationTracker: LocationTracker,
    private val application: Application,
) : ViewModel() {

    var state by mutableStateOf(WeatherState())
        private set

    private val geoCoder = Geocoder(application, Locale.getDefault())

    var city by mutableStateOf("")
        private set

    fun loadWeather() = viewModelScope.launch {
        state = state.copy(isLoading = true)

        when (val locationResult = locationTracker.getCurrentLocation()) {

            is Resource.Success -> {
                locationResult.data?.let { location ->
                    val weatherResult = weatherRepository.getWeatherData(
                        lat = location.latitude,
                        long = location.longitude
                    )
                    weatherResult.data?.city = geoCoder.getFromLocation(location.latitude, location.longitude, 1)?.firstOrNull()?.locality
                    state = when (weatherResult) {
                        is Resource.Success -> {
                            state.copy(
                                isLoading = false,
                                weather = weatherResult.data,
                                error = null
                            )
                        }

                        is Resource.Error -> {
                            state.copy(
                                isLoading = false,
                                weather = null,
                                error = weatherResult.message
                            )
                        }
                    }
                } ?: run {
                    state = state.copy(
                        isLoading = false,
                        weather = null,
                        error = "Couldn't retrieve location. Make sure to grant permission and enable GPS.")
                }
            }

            is Resource.Error -> {
                state = state.copy(isLoading = false, weather = null, error = locationResult.message)
            }
        }

    }
}