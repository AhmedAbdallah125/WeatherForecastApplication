package com.example.weatherforecastapplication.datasource.local

import androidx.lifecycle.LiveData

import com.example.weatherforecastapplication.model.OpenWeatherJason

interface LocalSourceInterface {
    suspend fun insertWeather(openWeatherJason: OpenWeatherJason)

    suspend fun getCurrentWeather(timezone: String): LiveData<OpenWeatherJason>

    suspend fun updateWeather(openWeatherJason: OpenWeatherJason)
}