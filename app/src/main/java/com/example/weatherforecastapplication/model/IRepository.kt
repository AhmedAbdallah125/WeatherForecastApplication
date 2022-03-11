package com.example.weatherforecastapplication.model

import androidx.lifecycle.LiveData

interface IRepository {
    suspend fun getCurrentWeather(lat: Double, long: Double
    ,lan :String =Languages.ENGLISH.name,unit :String =Units.IMPERIAL.name
    ): Result<OpenWeatherJason>
}