package com.example.weatherforecastapplication.datasource.network

import com.example.weatherforecastapplication.model.OpenWeatherJason
import retrofit2.Response

interface RemoteSource {
    suspend fun getCurrentWeather(
        lat: Double,
        long: Double,
        lang: String ,
        tempUnit: String
    ): Response<OpenWeatherJason>
}