package com.example.weatherforecastapplication.datasource.local

import android.content.Context
import androidx.lifecycle.LiveData

import com.example.weatherforecastapplication.model.OpenWeatherJason

interface LocalSourceInterface {
    suspend fun insertWeather(openWeatherJason: OpenWeatherJason)

    suspend fun getCurrentWeatherZone(
        timezone: String,
        isFavourite: Boolean = false
    ): OpenWeatherJason

    suspend fun getWeather(lat: Double, long: Double): OpenWeatherJason

    suspend fun updateWeather(openWeatherJason: OpenWeatherJason)
    fun getContext(): Context

    // for fav
    suspend fun getFavWeathersZone(

    ): List<OpenWeatherJason>

}