package com.yomagic.weather.data.repository

import com.yomagic.weather.domain.repository.WeatherRepository
import com.yomagic.weather.domain.util.Resource
import com.yomagic.weather.domain.weather.WeatherInfo

class WeatherRepositoryImpl : WeatherRepository {
    override suspend fun getWeatherData(lat: Double, long: Double): Resource<WeatherInfo> {
        TODO("Not yet implemented")
    }
}