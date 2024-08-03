package com.yomagic.weather.domain.repository

import com.yomagic.weather.domain.util.Resource
import com.yomagic.weather.domain.weather.WeatherInfo

interface WeatherRepository {

    suspend fun getWeatherData(lat: Double, long: Double): Resource<WeatherInfo>
}