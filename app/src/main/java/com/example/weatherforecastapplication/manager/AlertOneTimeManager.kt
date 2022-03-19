package com.example.weatherforecastapplication.manager

import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.content.ContextCompat
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.weatherforecastapplication.R

class AlertOneTimeManager(private val appContext: Context, params: WorkerParameters) :
    CoroutineWorker(appContext, params) {
    override suspend fun doWork(): Result {
        val inputData = inputData
        // getDescription
        val description = inputData.getString(appContext.getString(R.string.DESC))
        val isAlert = inputData.getBoolean(appContext.getString(R.string.IsAlert), false)
        startAlertService(isAlert, description)
        return Result.success()
    }

    private fun startAlertService(isAlert: Boolean, description: String?) {
        val intent = Intent(appContext, AlertService::class.java).apply {
            putExtra(appContext.getString(R.string.IsAlert), isAlert)
            putExtra(appContext.getString(R.string.DESC),description)
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            ContextCompat.startForegroundService(applicationContext, intent)
        } else {
            applicationContext.startService(intent)
        }
    }
}