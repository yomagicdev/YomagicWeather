package com.yomagic.weather.data.repository

import com.yomagic.weather.data.remote.WeatherApi
import com.yomagic.weather.domain.repository.WeatherRepository
import com.yomagic.weather.domain.util.Resource
import com.yomagic.weather.domain.weather.WeatherInfo
import javax.inject.Inject

class WeatherRepositoryImpl @Inject constructor(
    private val weatherApi: WeatherApi,
) : WeatherRepository {

    override suspend fun getWeatherData(lat: Double, long: Double): Resource<WeatherInfo> {
        return try {
            Resource.Success(
                data = weatherApi.getWeatherData(
                    latitude = lat,
                    longitude = long
                ).body()?.toDomainModel()
            )
        } catch (e: Exception) {
            Resource.Error(e.message ?: "An unknown error occurred")
        }
    }
}