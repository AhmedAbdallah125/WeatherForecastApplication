package com.example.weatherforecastapplication.datasource.local

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.weatherforecastapplication.model.OpenWeatherJason
import com.example.weatherforecastapplication.model.Result

@Dao
interface WeatherDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWeather(openWeatherJason: OpenWeatherJason)

    @Query("Select * from weather where  timezone =:timezone")
    suspend fun getCurrentWeatherZone(timezone: String): OpenWeatherJason

    @Query("Select * from weather where  lat =:lat AND lon =:lon")
    suspend fun getCurrentWeather(lat: Double, lon: Double): OpenWeatherJason

    @Update()
    suspend fun updateWeather(openWeatherJason: OpenWeatherJason)

    @Delete()
    suspend fun deleteWeatherTimeZone(openWeatherJason: OpenWeatherJason)
}