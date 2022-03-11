package com.example.weatherforecastapplication.model

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Converters {

    @TypeConverter
    fun fromCurrentToString(current: Current) = Gson().toJson(current)

    @TypeConverter
    fun fromStringToCurrent(currentString: String) =
        Gson().fromJson(currentString, Current::class.java)

    @TypeConverter
    fun fromHourlyToString(hourly: Hourly) = Gson().toJson(hourly)

    @TypeConverter
    fun fromStringToCurrentHourly(hourlyString: String) =
        Gson().fromJson(hourlyString, Hourly::class.java)

    @TypeConverter
    fun fromWeatherListToString(weather: List<Weather>) = Gson().toJson(weather)

    @TypeConverter
    fun fromStringToWeatherList(weatherListString: String): List<Weather> {
        val type = object : TypeToken<List<Weather>>() {}.type
        return Gson().fromJson(weatherListString, type)

    }

    @TypeConverter
    fun fromHourlyListToString(hours: List<Hourly>): String? = Gson().toJson(hours)

    @TypeConverter
    fun fromStringToHourlyList(hoursListString: String): List<Hourly> {
        val type = object : TypeToken<List<Hourly>>() {}.type
        return Gson().fromJson(hoursListString, type)

    }

    @TypeConverter
    fun fromDailyListToString(dailyS: List<Daily>): String? = Gson().toJson(dailyS)

    @TypeConverter
    fun fromStringToDailyList(dailyListString: String): List<Daily> {
        val type = object : TypeToken<List<Daily>>() {}.type
        return Gson().fromJson(dailyListString, type)

    }

    @TypeConverter
    fun fromAlertListToString(alerts: List<Alerts>): String? = Gson().toJson(alerts)

    @TypeConverter
    fun fromStringToAlertList(alertListString: String): List<Alerts> {
        val type = object : TypeToken<List<Alerts>>() {}.type
        return Gson().fromJson(alertListString, type)

    }

}