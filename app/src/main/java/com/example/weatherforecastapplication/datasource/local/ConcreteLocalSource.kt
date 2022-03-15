package com.example.weatherforecastapplication.datasource.local

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import com.example.weatherforecastapplication.model.OpenWeatherJason
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ConcreteLocalSource(
    private val context: Context,
    private val weatherDao: WeatherDao = WeatherDB.getDataBase(context)
        .weatherDao()
) : LocalSourceInterface {

    override suspend fun insertWeather(openWeatherJason: OpenWeatherJason) {
        withContext(Dispatchers.IO) {
            weatherDao.insertWeather(openWeatherJason)
        }
    }

    override suspend fun getCurrentWeatherZone(timezone: String,isFavourite:Boolean): OpenWeatherJason {
        return weatherDao.getCurrentWeatherZone(timezone,isFavourite)
    }

    override suspend fun getWeather(lat: Double, long: Double): OpenWeatherJason {
        return weatherDao.getCurrentWeather(lat, long)
    }

    override suspend fun updateWeather(openWeatherJason: OpenWeatherJason) {
        TODO("Not yet implemented")
    }

    override fun getContext(): Context {
        return context
    }

}
