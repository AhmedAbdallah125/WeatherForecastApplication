package com.example.weatherforecastapplication.datasource.network

import com.example.weatherforecastapplication.model.OpenWeatherJason
import retrofit2.Response
import javax.inject.Inject

class ConcreteRemote @Inject constructor(private val retrofitService: RetrofitService) :
    RemoteSource {

    override suspend fun getCurrentWeather(
        lat: Double,
        long: Double,
        lang: String,
        tempUnit: String
    ): Response<OpenWeatherJason> = retrofitService.getCurrentWeather(lat, long, lang, tempUnit)

}