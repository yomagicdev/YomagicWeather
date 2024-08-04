package com.yomagic.weather.presentation

import com.yomagic.weather.domain.weather.WeatherInfo

data class WeatherState(
    val isLoading: Boolean = false,
    val weather: WeatherInfo? = null,
    val error: String? = null
)
