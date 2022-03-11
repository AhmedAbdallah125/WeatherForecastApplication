package com.example.weatherforecastapplication.model

import android.util.Log
import com.example.weatherforecastapplication.datasource.local.LocalSourceInterface
import com.example.weatherforecastapplication.datasource.network.RemoteSource
import kotlinx.coroutines.*
import retrofit2.Response

class Repository(
    private val localSourceInterface: LocalSourceInterface,
    private val remoteSource: RemoteSource,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : IRepository {
    override suspend fun getCurrentWeather(
        lat: Double, long: Double, lan: String, unit: String
    ): Result<OpenWeatherJason> {
        try {
            updateCurrentWeatherTimeZone(lat, long, lan, unit)
        } catch (exception: Exception) {
            Log.i("AA", "Ex: " + exception.message)
        }
        var result = localSourceInterface.getWeather(lat, long)
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
                Log.i("AA", "EnteredHre: ")

                Log.i("AA", "updateCurrentWeatherTimeZone: " + "Succesuful")
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