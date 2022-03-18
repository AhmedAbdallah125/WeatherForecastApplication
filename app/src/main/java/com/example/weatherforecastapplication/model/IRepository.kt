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

    //
    suspend fun getCurrentFavWeather(
        lat: Double, long: Double, lan: String, unit: String, isFavourite: Boolean
    ): Result<OpenWeatherJason>

    //
    suspend fun deleteWeather(openWeatherJason: OpenWeatherJason)
    suspend fun deleteFavWeather(id: Int)

    // for Alerts
    suspend fun insertWeatherAlert(weatherAlert: WeatherAlert)
    suspend fun getWeatherAlerts(): List<WeatherAlert>
    suspend fun deleteWeatherAlert(id: Int)
}