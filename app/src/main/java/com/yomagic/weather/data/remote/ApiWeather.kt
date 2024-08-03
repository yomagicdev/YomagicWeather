package com.yomagic.weather.data.remote

import com.squareup.moshi.Json
import com.yomagic.weather.domain.weather.WeatherData
import com.yomagic.weather.domain.weather.WeatherInfo
import com.yomagic.weather.domain.weather.WeatherType
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

data class ApiWeather(
    @field:Json(name = "hourly")
    val weatherData: ApiWeatherData
) {
    fun toDomainModel(): WeatherInfo {
        val weatherDataPerDay = weatherData.time.mapIndexed { index, time ->
            val weatherData = WeatherData(
                time = LocalDateTime.parse(time, DateTimeFormatter.ISO_DATE_TIME),
                temperatureCelsius = weatherData.temperatures[index],
                pressure = weatherData.pressures[index],
                windSpeed = weatherData.windSpeeds[index],
                humidity = weatherData.relativeHumidities[index],
                weatherType = WeatherType.fromWMO(weatherData.weatherCodes[index])
            )
            Pair(index, weatherData)
        }.groupBy {
            it.first / 24
        }.mapValues { data ->
            data.value.map { it.second }
        }

        val now = LocalDateTime.now()
        val currentWeatherData = weatherDataPerDay[0]?.find {
            val hour = if (now.minute < 30) now.hour else now.hour + 1
            it.time.hour == hour
        }

        return WeatherInfo(
            weatherDataPerDay = weatherDataPerDay,
            currentWeather = currentWeatherData,
        )
    }
}
