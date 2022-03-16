package com.example.weatherforecastapplication.model

import androidx.lifecycle.LiveData

interface IRepository {
    suspend fun getCurrentWeather(
        lat: Double, long: Double, lan: String, unit: String, isFavourite: Boolean
    ): Result<OpenWeatherJason>

    suspend fun getFavWeathers(
        lat: Double, long: Double, lan: String, unit: String
    ): Result<List<OpenWeatherJason>>

    suspend fun getLocalFavWeathers(): Result<List<OpenWeatherJason>>
}