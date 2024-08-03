package com.yomagic.weather.data.remote

import com.squareup.moshi.Json

data class ApiWeatherData(
    val time: List<String>,
    @field:Json(name = "temperature_2m")
    val temperatures: List<Double>,
    @field:Json(name = "weathercode")
    val weatherCodes: List<Int>,
    @field:Json(name = "relativehumidity_2m")
    val relativeHumidities: List<Double>,
    @field:Json(name = "windspeed_10m")
    val windSpeeds: List<Double>,
    @field:Json(name = "pressure_msl")
    val pressures: List<Double>,
    val latitude: Double,
    val longitude: Double,
    val timezone: String
)
