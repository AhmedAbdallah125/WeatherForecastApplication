package com.example.weatherforecastapplication.datasource.local

import androidx.lifecycle.LiveData

import com.example.weatherforecastapplication.model.OpenWeatherJason

interface LocalSourceInterface {
    suspend fun insertWeather(openWeatherJason: OpenWeatherJason)

    suspend fun getCurrentWeatherZone(timezone: String): OpenWeatherJason
    suspend fun getWeather(lat: Double, long: Double): OpenWeatherJason

    suspend fun updateWeather(openWeatherJason: OpenWeatherJason)
}