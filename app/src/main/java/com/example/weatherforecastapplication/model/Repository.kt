package com.example.weatherforecastapplication.model

import android.content.Context
import android.util.Log
import com.example.weatherforecastapplication.R
import com.example.weatherforecastapplication.datasource.local.LocalSourceInterface
import com.example.weatherforecastapplication.datasource.network.RemoteSource
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class Repository  @Inject constructor(
    @ApplicationContext
    private val applicationContext: Context,
    private val localSourceInterface: LocalSourceInterface,
    private val remoteSource: RemoteSource,
) : IRepository {
    override suspend fun getCurrentWeather(
        lat: Double, long: Double, lan: String, unit: String, isFavourite: Boolean
    ): Result<OpenWeatherJason> {
        if (isConnected(applicationContext)) {
            try {
                updateCurrentWeatherID(lat, long, lan, unit)
            } catch (exception: Exception) {
                Log.i("AA", "Ex: " + exception.message)
            }
        }


        val sharedPreferences = initSharedPref(applicationContext)
        val id = sharedPreferences.getInt(
            applicationContext.getString(R.string.ID), -3
        )


        val result = localSourceInterface.getCurrentWeatherZone(id, isFavourite)
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
        if (isConnected(applicationContext)) {
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

    override suspend fun getCurrentFavWeather(
        lat: Double,
        long: Double,
        lan: String,
        unit: String,
        isFavourite: Boolean
    ): Result<OpenWeatherJason> {
        if (isConnected(applicationContext)) {
            try {
                updateCurrentFavWeather(lat, long, lan, unit)
            } catch (exception: Exception) {
                Log.i("AA", "Ex: " + exception.message)
            }
        }


        val id = initFavSharedPref(applicationContext).getInt(
            applicationContext.getString(R.string.ID), -1
        )


        val result = localSourceInterface.getCurrentWeatherZone(id, isFavourite)
//        Log.i("AA", "getCurrentWeather: " + result.timezone)
        return if (result == null) {
            Result.Error("this is not exist")
        } else {
            Result.Success(result)
        }
    }

    override suspend fun deleteWeather(openWeatherJason: OpenWeatherJason) {

    }

    override suspend fun deleteFavWeather(id: Int) {
        return localSourceInterface.deleteFavWeather(id)
    }

    override suspend fun insertWeatherAlert(weatherAlert: WeatherAlert): Int {
        return localSourceInterface.insertWeatherAlert(weatherAlert).toInt()
    }

    override fun getWeatherAlerts(): Flow<List<WeatherAlert>> {
        return localSourceInterface.getWeatherAlerts()
    }

    override suspend fun deleteWeatherAlert(id: Int) {
        localSourceInterface.deleteWeatherAlert(id)
    }

    override suspend fun getWeatherAlert(id: Int): WeatherAlert {
        return localSourceInterface.getWeatherAlert(id)
    }

    private suspend fun updateCurrentFavWeather(
        lat: Double,
        long: Double,
        lan: String,
        unit: String
    ) {
        try {
            val response = remoteSource.getCurrentWeather(lat, long, lan, unit)
            if (response.isSuccessful) {
                val sharedPreferences = initFavSharedPref(applicationContext)
                val idResult = sharedPreferences.getInt(
                    applicationContext.getString(R.string.ID), -3
                )
                val openWeatherJason = response.body()
                openWeatherJason?.isFavourite = true
                if (idResult != -3) {
                    openWeatherJason?.id = idResult
                }


                // must insert
                val id = localSourceInterface.insertWeather(openWeatherJason!!).toInt()
                initFavSharedPref(applicationContext).edit().apply {
                    putInt(
                        applicationContext.getString(R.string.ID), id
                    )
                    apply()
                }

            }
        } catch (exception: Exception) {
            Log.i("AA", "Exception: " + exception.message)
            throw exception
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

    private suspend fun updateCurrentWeatherID(
        lat: Double,
        long: Double,
        lan: String,
        unit: String
    ) {
        try {
            val response = remoteSource.getCurrentWeather(lat, long, lan, unit)
            if (response.isSuccessful) {

                val sharedPreferences = initSharedPref(applicationContext)
                // check if already exist or not
                val idResult = sharedPreferences.getInt(
                    applicationContext.getString(R.string.ID), -3
                )
                // At Beginning
                if (idResult != -3) {
                    //means no id for home yet
                    response.body()?.id = idResult
                }
                val id = localSourceInterface.insertWeather(response.body()!!).toInt()

                sharedPreferences.edit().apply {
                    putInt(
                        applicationContext.getString(R.string.ID),
                        id
                    )
                    putFloat(
                        applicationContext.getString(R.string.LAT),
                        response.body()!!.lat!!.toFloat()
                    )
                    putFloat(
                        applicationContext.getString(R.string.LON),
                        response.body()!!.lon!!.toFloat()
                    )
                    apply()
                }
                Log.i(
                    "AA",
                    "inONline:lat  " + sharedPreferences.getFloat(
                        applicationContext.getString(R.string.LAT), 0f
                    )
                )
                // must insert
            }
        } catch (exception: Exception) {
            Log.i("AA", "Exception: " + exception.message)
            throw exception
        }

    }


}