package com.example.weatherforecastapplication.manager

import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.content.ContextCompat
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.weatherforecastapplication.R
import com.example.weatherforecastapplication.datasource.local.ConcreteLocalSource
import com.example.weatherforecastapplication.datasource.network.RetrofitHelper
import com.example.weatherforecastapplication.model.Repository
import com.example.weatherforecastapplication.model.getCurrentLan
import java.text.SimpleDateFormat
import java.util.*

class AlertOneTimeManager(private val appContext: Context, params: WorkerParameters) :
    CoroutineWorker(appContext, params) {
    var myRepo = Repository(ConcreteLocalSource(appContext), RetrofitHelper)


    override suspend fun doWork(): Result {
        val inputData = inputData
        // getDescription
        val description = inputData.getString(appContext.getString(R.string.DESC))
        val isAlert = inputData.getBoolean(appContext.getString(R.string.IsAlert), false)
        val _id = inputData.getInt(appContext.getString(R.string.ID), 0)
        val _endDay = inputData.getLong(appContext.getString(R.string.ENDDAY), 0L)
        startAlertService(isAlert, description)
        closeWorker(_id, _endDay)
        return Result.success()
    }

    private suspend fun closeWorker(_id: Int, _endDay: Long) {
        if (checkDay(endDay = _endDay)) {
            myRepo.deleteWeatherAlert(_id)
        }
    }

    private fun startAlertService(isAlert: Boolean, description: String?) {
        val intent = Intent(appContext, AlertService::class.java).apply {
            putExtra(appContext.getString(R.string.IsAlert), isAlert)
            putExtra(appContext.getString(R.string.DESC), description)
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            ContextCompat.startForegroundService(applicationContext, intent)
        } else {
            applicationContext.startService(intent)
        }

    }

    private fun checkDay(endDay: Long): Boolean {
        val year = Calendar.getInstance().get(Calendar.YEAR)
        val month = Calendar.getInstance().get(Calendar.MONTH)
        val day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH)

        val date = "$day/${month + 1}/$year"
        val dayNow = getDateMillis(date)
        return (dayNow == endDay)
    }

    private fun getDateMillis(date: String): Long {
        val f = SimpleDateFormat("dd/MM/yyyy", Locale(getCurrentLan((appContext))))
        val d: Date = f.parse(date)
        return (d.time).div(1000)
    }
}