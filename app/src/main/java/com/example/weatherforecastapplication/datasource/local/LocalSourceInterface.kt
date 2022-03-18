package com.example.weatherforecastapplication.datasource.local

import android.content.Context
import androidx.lifecycle.LiveData

import com.example.weatherforecastapplication.model.OpenWeatherJason
import com.example.weatherforecastapplication.model.WeatherAlert

interface LocalSourceInterface {
    suspend fun insertWeather(openWeatherJason: OpenWeatherJason):Long

    suspend fun getCurrentWeatherZone(
        id: Int,
        isFavourite: Boolean
    ): OpenWeatherJason

    suspend fun getWeather(lat: Double, long: Double): OpenWeatherJason

    suspend fun updateWeather(openWeatherJason: OpenWeatherJason)
    fun getContext(): Context

    // for fav
    suspend fun getFavWeathersZone(

    ): List<OpenWeatherJason>

    suspend fun deleteWeather(openWeatherJason: OpenWeatherJason)
    suspend fun deleteFavWeather(
        id: Int,
    )

    //for Alerts
    suspend fun insertWeatherAlert(weatherAlert: WeatherAlert)
    suspend fun getWeatherAlerts(): List<WeatherAlert>
    suspend fun deleteWeatherAlert(id: Int)

}