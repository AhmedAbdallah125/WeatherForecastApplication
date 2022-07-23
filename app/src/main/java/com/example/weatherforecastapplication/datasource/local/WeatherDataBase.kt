package com.example.weatherforecastapplication.datasource.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.weatherforecastapplication.model.Converters
import com.example.weatherforecastapplication.model.OpenWeatherJason
import com.example.weatherforecastapplication.model.Weather
import com.example.weatherforecastapplication.model.WeatherAlert

//, exportSchema = false

@Database(entities = [OpenWeatherJason::class, WeatherAlert::class], version = 2, exportSchema = false)
@TypeConverters(Converters::class)
abstract class WeatherDB : RoomDatabase() {
    abstract fun weatherDao(): WeatherDao
}