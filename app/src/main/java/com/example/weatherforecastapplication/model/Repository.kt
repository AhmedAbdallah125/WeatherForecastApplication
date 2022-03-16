package com.example.weatherforecastapplication.model

import android.util.Log
import com.example.weatherforecastapplication.R
import com.example.weatherforecastapplication.datasource.local.LocalSourceInterface
import com.example.weatherforecastapplication.datasource.network.RemoteSource
import kotlinx.coroutines.*
import retrofit2.Response
import  com.example.weatherforecastapplication.model.initSharedPref

class Repository(
    private val localSourceInterface: LocalSourceInterface,
    private val remoteSource: RemoteSource,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : IRepository {
    override suspend fun getCurrentWeather(
        lat: Double, long: Double, lan: String, unit: String, isFavourite: Boolean
    ): Result<OpenWeatherJason> {
        if (isConnected(localSourceInterface.getContext())) {
            try {
                updateCurrentWeatherTimeZone(lat, long, lan, unit)
            } catch (exception: Exception) {
                Log.i("AA", "Ex: " + exception.message)
            }
        }


        val sharedPreferences = initSharedPref(localSourceInterface.getContext())
        val timeZone = sharedPreferences.getString(
            localSourceInterface.getContext().getString(R.string.TIMEZONE), ""
        )


        val result = localSourceInterface.getCurrentWeatherZone(timeZone!!, isFavourite)
//        Log.i("AA", "getCurrentWeather: " + result.timezone)
        return if (result == null) {
            Result.Error("this is not exist")
        } else {
            Result.Success(result)
        }
    }

    override suspend fun getFavWeathers(
        lat: Double,
        long: Double,
        lan: String,
        unit: String,

        ): Result<List<OpenWeatherJason>> {
        if (isConnected(localSourceInterface.getContext())) {
            try {
                updateFavWeathersTimeZone(lat, long, lan, unit)
            } catch (exception: Exception) {
                Log.i("AA", "Ex: " + exception.message)
            }
        }


        val result = localSourceInterface.getFavWeathersZone()
//        Log.i("AA", "getCurrentWeather: " + result.timezone)
        return if (result == null) {
            Result.Error("this is not exist")
        } else {
            Result.Success(result)
        }

    }

    override suspend fun getLocalFavWeathers(): Result<List<OpenWeatherJason>> {
        val result = localSourceInterface.getFavWeathersZone()
//        Log.i("AA", "getCurrentWeather: " + result.timezone)
        return if (result == null) {
            Result.Error("this is not exist")
        } else {
            Result.Success(result)
        }
    }

    private suspend fun updateFavWeathersTimeZone(
        lat: Double,
        long: Double,
        lan: String,
        unit: String
    ) {
        try {
            val response = remoteSource.getCurrentWeather(lat, long, lan, unit)
            if (response.isSuccessful) {
                val openWeatherJason = response.body()
                openWeatherJason?.isFavourite = true

                // must insert
                localSourceInterface.insertWeather(openWeatherJason!!)
            }
        } catch (exception: Exception) {
            Log.i("AA", "Exception: " + exception.message)
            throw exception
        }
    }

    private suspend fun updateCurrentWeatherTimeZone(
        lat: Double,
        long: Double,
        lan: String,
        unit: String
    ) {
        try {
            val response = remoteSource.getCurrentWeather(lat, long, lan, unit)
            if (response.isSuccessful) {
                val sharedPreferences = initSharedPref(localSourceInterface.getContext())
                sharedPreferences.edit().apply {
                    putString(
                        localSourceInterface.getContext().getString(R.string.TIMEZONE),
                        response.body()!!.timezone
                    )
                    putFloat(
                        localSourceInterface.getContext().getString(R.string.LAT),
                        response.body()!!.lat!!.toFloat()
                    )
                    putFloat(
                        localSourceInterface.getContext().getString(R.string.LON),
                        response.body()!!.lon!!.toFloat()
                    )
                    apply()
                }
                Log.i(
                    "AA",
                    "inONline:lat  " + sharedPreferences.getFloat(
                        localSourceInterface.getContext().getString(R.string.LAT), 0f
                    )
                )
                // must insert
                localSourceInterface.insertWeather(response.body()!!)
            }
        } catch (exception: Exception) {
            Log.i("AA", "Exception: " + exception.message)
            throw exception
        }

    }

    private suspend fun deleteWeatherZone(openWeatherJason: OpenWeatherJason) {
//        localSourceInterface.d
    }

    private suspend fun insertWeatherZone(openWeatherJason: OpenWeatherJason) {
        localSourceInterface.insertWeather(openWeatherJason)
    }


}