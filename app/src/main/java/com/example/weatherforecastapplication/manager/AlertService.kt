package com.example.weatherforecastapplication.manager

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import android.os.IBinder
import android.provider.Settings
import androidx.core.app.NotificationCompat
import com.example.weatherforecastapplication.R
import com.example.weatherforecastapplication.home.view.HomeActivity

class AlertService : Service() {
    private var alertDescription: String = ""
    private lateinit var notificationManager: NotificationManager
    private var icon = 0

    private var isAlert: Boolean = false
    val CHANNEL_ID = 123
    val FOREGROUND_ID = 10
    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {

        //        name=intent.getStringExtra(TakerOneTmeWorkManager.NAME_TAG);
        createNotificationChannel()
        startForeground(
            FOREGROUND_ID,
            makeNotification()
        )

        alertDescription = intent.getStringExtra(getString(R.string.DESC))!!
        isAlert = intent.getBooleanExtra(getString(R.string.ISALERT), false)

        if (isAlert) {
            icon = R.drawable.ic_warning_com
            alertDescription = "You should be careful".plus(".").plus("\n")
                .plus(getString(R.string.the_weather_is)).plus(alertDescription)

        } else {
            icon = R.drawable.ic_sunrise
            alertDescription = getString(R.string.ther_is_no_alarm).plus(".").plus("\n")
                .plus(getString(R.string.the_weather_is)).plus("  ").plus(alertDescription)
        }
        startForeground(
            FOREGROUND_ID,
            makeNotification()
        )
        if (if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                Settings.canDrawOverlays(this.applicationContext)
            } else {
                TODO("VERSION.SDK_INT < M")
            }
        ) {
            // call window manager
            val myWorkManager = AlertWindowManager(
                this, icon, alertDescription
            )
            myWorkManager.setWindowManager()
        }

        return START_NOT_STICKY

    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    private fun makeNotification(): Notification {
        val intent = Intent(applicationContext, HomeActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        //        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(),
//                0, intent, 0);
        val sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE)
        return NotificationCompat.Builder(
            applicationContext,
            CHANNEL_ID.toString()
        )
            .setSound(sound)
            .setSmallIcon(icon)
            .setContentTitle(getString(R.string.weather_alerts))
            .setContentText(alertDescription)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT) //                .setContentIntent(pendingIntent)
//            .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM))

            .setAutoCancel(true).build()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name: CharSequence = getString(R.string.channel_name)

            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(
                CHANNEL_ID.toString(),
                name, importance
            )
            channel.description = alertDescription
            notificationManager = this.getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }
    }


}