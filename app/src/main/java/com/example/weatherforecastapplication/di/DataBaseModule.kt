package com.example.weatherforecastapplication.di

import android.content.Context
import androidx.room.Room
import com.example.weatherforecastapplication.datasource.local.WeatherDB
import com.example.weatherforecastapplication.datasource.local.WeatherDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object DataBaseModule {
    @Singleton
    @Provides
    fun provideWeatherDB(@ApplicationContext applicationContext: Context): WeatherDB {
        return Room.databaseBuilder(
            applicationContext,
            WeatherDB::class.java,
            "weather_db"
        ).build()
    }

    @Provides
    fun providesWeatherDao(weatherDB: WeatherDB): WeatherDao {
        return weatherDB.weatherDao()
    }

}