package com.example.weatherforecastapplication.datasource.local

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.weatherforecastapplication.model.OpenWeatherJason
import com.example.weatherforecastapplication.model.Result

@Dao
interface WeatherDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWeather(openWeatherJason: OpenWeatherJason)

    // for home screen not fav
    @Query("Select * from weather where  timezone =:timezone And isFavourite  =:isFavourite ")
    suspend fun getCurrentWeatherZone(timezone: String, isFavourite: Boolean): OpenWeatherJason

    @Query("Select * from weather where  lat =:lat AND lon =:lon")
    suspend fun getCurrentWeather(lat: Double, lon: Double): OpenWeatherJason
    // for fav

    @Update()
    suspend fun updateWeather(openWeatherJason: OpenWeatherJason)

    @Delete()
    suspend fun deleteWeatherTimeZone(openWeatherJason: OpenWeatherJason)
    // ensure exist or not

    //get Favourites Weathers
    @Query("Select * from weather where   isFavourite  =1 ")
    suspend fun getFavWeathersZone(): List<OpenWeatherJason>
}