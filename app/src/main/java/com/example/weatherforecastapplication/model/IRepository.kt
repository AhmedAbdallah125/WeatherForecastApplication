package com.example.weatherforecastapplication.model

import androidx.lifecycle.LiveData

interface IRepository {
    suspend fun getCurrentWeather(lat: Double, long: Double
    ,lan :String ,unit :String
    ): Result<OpenWeatherJason>
}