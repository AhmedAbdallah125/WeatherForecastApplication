package com.example.weatherforecastapplication.datasource.local

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import com.example.weatherforecastapplication.model.OpenWeatherJason
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ConcreteLocalSource(
    private val context: Context,
    private val weatherDao: WeatherDao = WeatherDataBase.getDataBase(context)
        .weatherDao()
) : LocalSourceInterface {

    override suspend fun insertWeather(openWeatherJason: OpenWeatherJason) {
        withContext(Dispatchers.IO) {
            Log.i("AA", "The entered is: " + openWeatherJason.toString())
            weatherDao.insertWeather(openWeatherJason)
        }
    }

    override suspend fun getCurrentWeatherZone(timezone: String): OpenWeatherJason {
        return weatherDao.getCurrentWeatherZone(timezone)
    }

    override suspend fun getWeather(lat: Double, long: Double): OpenWeatherJason {
        return weatherDao.getCurrentWeather(lat, long)
    }

    override suspend fun updateWeather(openWeatherJason: OpenWeatherJason) {
        TODO("Not yet implemented")
    }

}
