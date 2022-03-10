package com.example.weatherforecastapplication.datasource.local

import androidx.lifecycle.LiveData
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.weatherforecastapplication.model.OpenWeatherJason

interface WeatherDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertWeather(openWeatherJason: OpenWeatherJason)

    @Query("Select * from weather where  timezone =:timezone")
    suspend fun getCurrentWeather(timezone: String): LiveData<OpenWeatherJason>

    @Update()
    suspend fun updateWeather(openWeatherJason: OpenWeatherJason)
}