package com.example.weatherforecastapplication.datasource.network

import com.example.weatherforecastapplication.model.OpenWeatherJason
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitHelper : RemoteSource {
    private val baseUrl = "https://api.openweathermap.org/data/2.5/"

    private val retrofit by lazy {
        Retrofit.Builder().baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    private val retrofitService: RetrofitService by lazy {
        retrofit.create(RetrofitService::class.java)
    }

    override suspend fun getCurrentWeather(
        lat: Double,
        long: Double,
        lang: String,
        tempUnit: String
    ): Response<OpenWeatherJason> = retrofitService.getCurrentWeather(lat, long, lang, tempUnit)

}