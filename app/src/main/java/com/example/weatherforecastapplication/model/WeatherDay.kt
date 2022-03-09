package com.example.weatherforecastapplication.model

data class WeatherDay(
    var day: String,
    var history: String,
    var img: Int,
    var des: String,
    var temp: String
)

data class WeatherHour(
    var time: String,
    var img: Int,
    var temp: String
)
