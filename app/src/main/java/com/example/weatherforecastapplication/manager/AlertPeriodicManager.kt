package com.example.weatherforecastapplication.manager

import android.content.Context
import android.util.Log
import androidx.work.*
import com.example.weatherforecastapplication.R
import com.example.weatherforecastapplication.datasource.local.ConcreteLocalSource
import com.example.weatherforecastapplication.datasource.network.RetrofitHelper
import com.example.weatherforecastapplication.model.*
import com.example.weatherforecastapplication.model.Result.Success
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

class AlertPeriodicManager(private val appContext: Context, params: WorkerParameters) :
    CoroutineWorker(appContext, params) {
    var myRepo = Repository(ConcreteLocalSource(appContext), RetrofitHelper)
    override suspend fun doWork(): Result {
        //must have id of alert
        Log.i("ABC", "doWork: ")
        val inputData = inputData
        //get id of weatherCountry - lar long
        val lat = inputData.getFloat(appContext.getString(R.string.LON), 0f)
        val long = inputData.getFloat(appContext.getString(R.string.LON), 0f)
        val id = inputData.getInt(appContext.getString(R.string.ID), 0)
        val weatherAlert = myRepo.getWeatherAlert(id)
        processing(weatherAlert, lat, long, id)




        return Result.success()
    }

    private fun checkDay(startDay: Long, endDay: Long): Boolean {
        val year = Calendar.getInstance().get(Calendar.YEAR)
        val month = Calendar.getInstance().get(Calendar.MONTH)
        val day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH)

        val date = "$day/${month + 1}/$year"
        val dayNow = getDateMillis(date)
        return (dayNow in startDay..endDay)
    }

    private suspend fun processing(weatherAlert: WeatherAlert, lat: Float, long: Float, id: Int) {
        if (checkDay(weatherAlert.startDay,weatherAlert.endDay)) {
            val result = myRepo.getCurrentWeather(
                lat.toDouble(), long.toDouble(), "en", getCurrentUnit(appContext), false
            )
            if (result is Success) {
                Log.i("ABC", "checkDay: ")
                var description = ""
                var isAlert: Boolean
                val openWeatherJason = result.data
                if (openWeatherJason.alerts.isNullOrEmpty()) {
                    isAlert = false
                    description = openWeatherJason.current.weather[0].description.toString()
                } else {
                    isAlert = true
                    description = openWeatherJason.alerts!![0].tags[0]
                }
                setOnTimeWorkManger(isAlert, description, getPeriod(weatherAlert.startTime), id,weatherAlert.endDay)
            }
        } else {
//            myRepo.deleteWeatherAlert(id)
        }
    }

    private fun setOnTimeWorkManger(
        isAlert: Boolean,
        description: String,
        period: Long,
        id: Int,
        endDay: Long
    ) {
        val data = Data.Builder().apply {
            putBoolean(appContext.getString(R.string.IsAlert), isAlert)
            putString(appContext.getString(R.string.DESC), description)
                putInt(appContext.getString(R.string.ID),id)
            putLong(appContext.getString(R.string.ENDDAY),endDay)
        }.build()
        val constraints = Constraints.Builder()
            .setRequiresBatteryNotLow(true)
            .build()
        val oneTimeWorkRequest =
            OneTimeWorkRequest.Builder(AlertOneTimeManager::class.java).setInputData(data)
                .setConstraints(constraints)
                .setInitialDelay(period, TimeUnit.SECONDS)
                .addTag(id.toString())
                .build()
        //
        Log.i("ABC", "setOnTimeWorkManger: will run after${period / 60}")
        WorkManager.getInstance(appContext)
            .enqueueUniqueWork(id.toString(), ExistingWorkPolicy.REPLACE, oneTimeWorkRequest)
    }

    private fun getPeriod(startTime: Long): Long {
        val calendar = Calendar.getInstance()
        val currentHour: Int = calendar.get(Calendar.HOUR_OF_DAY)
        val currentMinute: Int = calendar.get(Calendar.MINUTE)
        var time = (TimeUnit.MINUTES.toSeconds(currentMinute.toLong()) + TimeUnit.HOURS.toSeconds(
            currentHour.toLong())
                )
        time =time.minus(3600L*2)

        Log.i("ABC", "timenow:${time} ")
        Log.i("ABC", "start $startTime: ")
        return (startTime - time)
    }

    private fun getDateMillis(date: String): Long {
        val f = SimpleDateFormat("dd/MM/yyyy", Locale(getCurrentLan((appContext))))
        val d: Date = f.parse(date)
        return (d.time).div(1000)
    }

}

