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
        lat: Double, long: Double, lan: String, unit: String
    ): Result<OpenWeatherJason> {
//        var job = CoroutineScope(ioDispatcher).launch {
        if (isConnected(localSourceInterface.getContext())) {
            try {
                updateCurrentWeatherTimeZone(lat, long, lan, unit)
            } catch (exception: Exception) {
                Log.i("AA", "Ex: " + exception.message)
            }
        }
//        }
//        job.join()

        val sharedPreferences = initSharedPref(localSourceInterface.getContext())
        val timeZone = sharedPreferences.getString(
            localSourceInterface.getContext().getString(R.string.TIMEZONE), ""
        )


        val result = localSourceInterface.getCurrentWeatherZone(timeZone!!)
//        Log.i("AA", "getCurrentWeather: " + result.timezone)
        return if (result == null) {
            Result.Error("this is not exist")
        } else {
            Result.Success(result)
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

    private suspend fun getFromRemoteFirst(lat: Double, long: Double): Response<OpenWeatherJason>? {
        var response: Response<OpenWeatherJason>? = null
        var deferred =
            CoroutineScope(ioDispatcher).async {
                response = remoteSource.getCurrentWeather(lat, long)
            }
        deferred.await()
        return response
    }
}