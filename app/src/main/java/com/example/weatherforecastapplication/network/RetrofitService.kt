package com.example.weatherforecastapplication.network

import com.example.weatherforecastapplication.model.OpenWeatherJason
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

private const val appId = "038b8b1da1c8d411444848dd7a345ad2"
private const val excludeMinutes = "minutely"

interface RetrofitService {
    @GET("onecall")
    suspend fun getCurrentWeather(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("lang") language: String = "en",
        @Query("units") units: String = "imperial",
        @Query("exclude") exclude: String = excludeMinutes,
        @Query("appid") appid: String = appId
    ): Response<OpenWeatherJason>

}

