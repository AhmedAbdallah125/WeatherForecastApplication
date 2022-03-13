package com.example.weatherforecastapplication.model

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.location.Geocoder
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.core.content.ContentProviderCompat.requireContext
import com.example.weatherforecastapplication.R
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

fun getIconImage(icon: String): Int {
    val iconValue: Int
    when (icon) {
        "01d" -> iconValue = R.drawable.a01d
        "01n" -> iconValue = R.drawable.a01n
        "02d" -> iconValue = R.drawable.a02d
        "02n" -> iconValue = R.drawable.a02n
        "03n" -> iconValue = R.drawable.a03n
        "03d" -> iconValue = R.drawable.a03d
        "04d" -> iconValue = R.drawable.a04d
        "04n" -> iconValue = R.drawable.a04n
        "09d" -> iconValue = R.drawable.a09d
        "09n" -> iconValue = R.drawable.a09n
        "10d" -> iconValue = R.drawable.a10d
        "10n" -> iconValue = R.drawable.a10n
        "11d" -> iconValue = R.drawable.a11d
        "11n" -> iconValue = R.drawable.a11n
        "13d" -> iconValue = R.drawable.a13d
        "13n" -> iconValue = R.drawable.a13n
        "50d" -> iconValue = R.drawable.a50d
        "50n" -> iconValue = R.drawable.a50n
        else -> iconValue = R.drawable.ic_baseline_wb_sunny_24
    }
    return iconValue
}

// convert to hours
@SuppressLint("SimpleDateFormat")
fun convertToTime(dt: Long): String {
    val date = Date(dt * 1000)
    val format = SimpleDateFormat("h:mm a")
    return format.format(date)
}

// now Day friday
fun convertToDay(dt: Long): String {
    val calendar = Calendar.getInstance()
    calendar.timeInMillis = dt
    return calendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.ENGLISH)
}

// return now day history
@SuppressLint("SimpleDateFormat")
fun convertToDate(dt: Long): String {
    val date = Date(dt * 1000)
    val format = SimpleDateFormat("d MMM, yyyy")
    return format.format(date)
}

fun isConnected(context: Context): Boolean {
    val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        val capabilities =
            connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        if (capabilities != null) {
            when {
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> {
                    return true
                }
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> {
                    return true
                }
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> {
                    return true
                }
            }
        }
    } else {
        val activeNetworkInfo = connectivityManager.activeNetworkInfo
        if (activeNetworkInfo != null && activeNetworkInfo.isConnected) {
            return true
        }
    }
    return false
}

fun initSharedPref(context: Context): SharedPreferences {
    return context.getSharedPreferences(
        context.getString(R.string.shared_pref),
        Context.MODE_PRIVATE
    )

}

fun checkSharedTimeZone(context: Context): Boolean {
    val sharedPref = initSharedPref(context)
    return sharedPref.getString(context.getString(R.string.TIMEZONE),"null").isNullOrEmpty()
}


fun checkShared(context: Context): Boolean {
    val sharedPref = initSharedPref(context)
    return sharedPref.getFloat(context.getString(R.string.LAT), 0f) == 0f
}


//
//// history of Days
//fun convertLongToDay(time: Long): String {
//    val date = Date(TimeUnit.SECONDS.toMillis(time))
//    val format = SimpleDateFormat("EEE, d MMM yyyy")
//    return format.format(date)
//}

//        ‏