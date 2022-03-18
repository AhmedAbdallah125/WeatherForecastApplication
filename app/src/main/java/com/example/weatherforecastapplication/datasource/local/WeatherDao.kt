package com.example.weatherforecastapplication.datasource.local

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.weatherforecastapplication.model.OpenWeatherJason
import com.example.weatherforecastapplication.model.Result
import com.example.weatherforecastapplication.model.WeatherAlert

@Dao
interface WeatherDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWeather(openWeatherJason: OpenWeatherJason):Long

    // for home screen not fav
    @Query("Select * from weather where  id =:id And isFavourite  =:isFavourite ")
    suspend fun getCurrentWeatherZone(id: Int, isFavourite: Boolean): OpenWeatherJason

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

    // delete Favourite Weather
    @Query("Delete from weather where  id =:id  And isFavourite=1 ")
    suspend fun deleteFavWeather(id: Int)

    // for Alerts
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWeatherAlert(alert: WeatherAlert)

    @Query("Select * from Alert ")
    suspend fun getWeatherAlerts(): List<WeatherAlert>

    @Query("Delete from Alert where id=:id")
    suspend fun deleteWeatherAlert(id: Int)
}