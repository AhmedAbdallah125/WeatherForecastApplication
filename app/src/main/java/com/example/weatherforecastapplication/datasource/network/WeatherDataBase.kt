package com.example.weatherforecastapplication.datasource.network

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.weatherforecastapplication.datasource.local.WeatherDao
import com.example.weatherforecastapplication.model.Weather

@Database(entities = [Weather::class], version = 1, exportSchema = false)
abstract class WeatherDataBase : RoomDatabase() {
    abstract fun weatherDao(): WeatherDao

    companion object {
        // Singleton prevents multiple instances of database opening at the
        // same time.
        @Volatile
        private var INSTANCE: WeatherDataBase? = null
        fun getDataBase(context: Context): WeatherDataBase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    WeatherDataBase::class.java,
                    "weather_db"
                ).build()
                INSTANCE = instance
                instance
            }

        }
    }

}