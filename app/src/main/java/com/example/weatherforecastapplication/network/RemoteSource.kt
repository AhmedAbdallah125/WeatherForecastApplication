package com.example.weatherforecastapplication.network

import com.example.weatherforecastapplication.model.OpenWeatherJason
import retrofit2.Response

interface RemoteSource {
    suspend fun getCurrentWeather(
        lat: Double,
        long: Double,
        lang: String = "en",
        tempUnit: String = "imperial"
    ): Response<OpenWeatherJason>

}