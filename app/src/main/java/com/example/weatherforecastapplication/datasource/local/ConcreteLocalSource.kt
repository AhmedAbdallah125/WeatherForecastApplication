package com.example.weatherforecastapplication.datasource.local

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import com.example.weatherforecastapplication.model.OpenWeatherJason
import com.example.weatherforecastapplication.model.WeatherAlert
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class ConcreteLocalSource(
    private val context: Context,
    private val weatherDao: WeatherDao = WeatherDB.getDataBase(context)
        .weatherDao()
) : LocalSourceInterface {

    override suspend fun insertWeather(openWeatherJason: OpenWeatherJason):Long {
           return weatherDao.insertWeather(openWeatherJason)

    }

    override suspend fun getCurrentWeatherZone(
        id: Int,
        isFavourite: Boolean
    ): OpenWeatherJason {
        return weatherDao.getCurrentWeatherZone(id, isFavourite)
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

    override suspend fun getFavWeathersZone(

    ): List<OpenWeatherJason> {
        return weatherDao.getFavWeathersZone()
    }

    override suspend fun deleteWeather(openWeatherJason: OpenWeatherJason) {
        return weatherDao.deleteWeatherTimeZone(openWeatherJason)
    }

    override suspend fun deleteFavWeather(id: Int) {
        return weatherDao.deleteFavWeather(id)
    }

    // handle Alerts
    override suspend fun insertWeatherAlert(weatherAlert: WeatherAlert) {
        return weatherDao.insertWeatherAlert(weatherAlert)
    }

    override  fun getWeatherAlerts(): Flow<List<WeatherAlert>> {
        return weatherDao.getWeatherAlerts()
    }

    override suspend fun deleteWeatherAlert(id: Int) {
        return weatherDao.deleteWeatherAlert(id)
    }


}
