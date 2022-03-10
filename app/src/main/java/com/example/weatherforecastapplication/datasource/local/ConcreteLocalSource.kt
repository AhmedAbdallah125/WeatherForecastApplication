package com.example.weatherforecastapplication.datasource.local

import android.content.Context
import androidx.lifecycle.LiveData
import com.example.weatherforecastapplication.datasource.network.WeatherDataBase
import com.example.weatherforecastapplication.model.OpenWeatherJason

class ConcreteLocalSource(
    private val context: Context,
    private val weatherDao: WeatherDao = WeatherDataBase.getDataBase(context)
        .weatherDao()
) : LocalSourceInterface {

    override suspend fun insertWeather(openWeatherJason: OpenWeatherJason) {
        TODO("Not yet implemented")
    }

    override suspend fun getCurrentWeather(timezone: String): LiveData<OpenWeatherJason> {
        TODO("Not yet implemented")
    }

    override suspend fun updateWeather(openWeatherJason: OpenWeatherJason) {
        TODO("Not yet implemented")
    }

}
