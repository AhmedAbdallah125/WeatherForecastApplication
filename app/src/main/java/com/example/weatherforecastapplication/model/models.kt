package com.example.weatherforecastapplication.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "weather")
data class OpenWeatherJason(
    var lat: Double? = null,
    var lon: Double? = null,
    @PrimaryKey
    var timezone: String = "aa",
    var timezoneOffset: Int? = null,
    var current: Current = Current(),
    var hourly: List<Hourly> = arrayListOf(),
    var daily: List<Daily> = arrayListOf(),
    var alerts: List<Alerts> = arrayListOf()
)

data class Weather(
    @SerializedName("id") var id: Int? = null,
    @SerializedName("main") var main: String? = null,
    @SerializedName("description") var description: String? = null,
    @SerializedName("icon") var icon: String? = null
)

data class Current(
    @SerializedName("dt") var dt: Long? = null,
    @SerializedName("sunrise") var sunrise: Int? = null,
    @SerializedName("sunset") var sunset: Int? = null,
    @SerializedName("temp") var temp: Double? = null,
    @SerializedName("feels_like") var feelsLike: Double? = null,
    @SerializedName("pressure") var pressure: Int? = null,
    @SerializedName("humidity") var humidity: Int? = null,
    @SerializedName("dew_point") var dewPoint: Double? = null,
    @SerializedName("uvi") var uvi: Double? = null,
    @SerializedName("clouds") var clouds: Int? = null,
    @SerializedName("visibility") var visibility: Int? = null,
    @SerializedName("wind_speed") var windSpeed: Double? = null,
    @SerializedName("wind_deg") var windDeg: Int? = null,
    @SerializedName("wind_gust") var windGust: Double? = null,
    @SerializedName("weather") var weather: List<Weather> = arrayListOf()

)

data class Hourly(
    @SerializedName("dt") var dt: Long,
    @SerializedName("temp") var temp: Double? = null,
    @SerializedName("feels_like") var feelsLike: Double? = null,
    @SerializedName("pressure") var pressure: Int? = null,
    @SerializedName("humidity") var humidity: Int? = null,
    @SerializedName("dew_point") var dewPoint: Double? = null,
    @SerializedName("uvi") var uvi: Double? = null,
    @SerializedName("clouds") var clouds: Int? = null,
    @SerializedName("visibility") var visibility: Int? = null,
    @SerializedName("wind_speed") var windSpeed: Double? = null,
    @SerializedName("wind_deg") var windDeg: Int? = null,
    @SerializedName("wind_gust") var windGust: Double? = null,
    @SerializedName("weather") var weather: List<Weather> = arrayListOf(),
    @SerializedName("pop") var pop: Double? = null
)


data class Temp(

    @SerializedName("day") var day: Double? = null,
    @SerializedName("min") var min: Double? = null,
    @SerializedName("max") var max: Double? = null,
    @SerializedName("night") var night: Double? = null,
    @SerializedName("eve") var eve: Double? = null,
    @SerializedName("morn") var morn: Double? = null

)

data class FeelsLike(

    @SerializedName("day") var day: Double? = null,
    @SerializedName("night") var night: Double? = null,
    @SerializedName("eve") var eve: Double? = null,
    @SerializedName("morn") var morn: Double? = null

)

data class Daily(

    @SerializedName("dt") var dt: Long? = null,
    @SerializedName("sunrise") var sunrise: Int? = null,
    @SerializedName("sunset") var sunset: Int? = null,
    @SerializedName("moonrise") var moonrise: Int? = null,
    @SerializedName("moonset") var moonset: Int? = null,
    @SerializedName("moon_phase") var moonPhase: Double? = null,
    @SerializedName("temp") var temp: Temp? = Temp(),
    @SerializedName("feels_like") var feelsLike: FeelsLike? = FeelsLike(),
    @SerializedName("pressure") var pressure: Int? = null,
    @SerializedName("humidity") var humidity: Int? = null,
    @SerializedName("dew_point") var dewPoint: Double? = null,
    @SerializedName("wind_speed") var windSpeed: Double? = null,
    @SerializedName("wind_deg") var windDeg: Int? = null,
    @SerializedName("wind_gust") var windGust: Double? = null,
    @SerializedName("weather") var weather: List<Weather> = arrayListOf(),
    @SerializedName("clouds") var clouds: Int? = null,
    @SerializedName("pop") var pop: Double? = null,
    @SerializedName("uvi") var uvi: Double? = null

)

data class Alerts(

    @SerializedName("sender_name") var senderName: String? = null,
    @SerializedName("event") var event: String? = null,
    @SerializedName("start") var start: Int? = null,
    @SerializedName("end") var end: Int? = null,
    @SerializedName("description") var description: String? = null,
    @SerializedName("tags") var tags: List<String> = arrayListOf()

)

enum class Units(var unit: String) {
    IMPERIAL("imperial"),
    METRIC("metric"),

}

enum class Languages(var language: String) {
    ENGLISH("en"),
    ARABIC("ar")
}