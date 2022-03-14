package com.example.weatherforecastapplication.home.view

import android.Manifest
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.weatherforecastapplication.R
import com.example.weatherforecastapplication.databinding.ActivityHomeBinding
import com.example.weatherforecastapplication.home.viewmodel.HomeActivityViewModel
import com.example.weatherforecastapplication.model.*
import com.google.android.gms.location.*
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.util.*
import kotlin.math.log

class HomeActivity : AppCompatActivity(), DialogInterface.OnDismissListener {
    private lateinit var binding: ActivityHomeBinding
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    // for permission
    private val permission = arrayOf<String>(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION
    )
    private val requestId = 22

    // view Model
    private val viewModel: HomeActivityViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // init shared pref
        sharedPreferences = initSharedPref(this)

        if (isFirst()) {
            // check with shared
            // here init first shared for all
            initUNIT(Units.METRIC.name,this)
            initLan(Locale.getDefault().language,this)
            binding.container.visibility = View.INVISIBLE
            CustomDialog().show(supportFragmentManager, "MyCustomFragment")
        }
        // check if MA
        if (checkGPS()) {
            getLocation()
            initSharedPref(this).edit().apply {
                putInt(getString(R.string.LOCATION), 2)
                apply()
            }

        }


        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_home)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
//        val appBarConfiguration = AppBarConfiguration(
//            setOf(
//                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications
//            )
//        )
//        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }

    private fun checkGPS(): Boolean {
        return initSharedPref(this).getInt(getString(R.string.LOCATION), 0) == 1
    }

    private fun checkLocationPermission(): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            when {
                ContextCompat.checkSelfPermission(
                    applicationContext, Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
                        &&
                        ContextCompat.checkSelfPermission(
                            applicationContext,
                            Manifest.permission.ACCESS_COARSE_LOCATION
                        ) == PackageManager.PERMISSION_GRANTED
                -> {
                    Toast.makeText(
                        applicationContext,
                        "Location Permission Granted",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                    // get location
                    return true
                }
                else -> {
                    return false
                }
                // will show message

            }
        }
        return true
    }

    private fun getLocation() {

        val locationRequest: LocationRequest = LocationRequest.create()
        locationRequest.apply {
            interval = 1000
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }

        if (checkLocationPermission()) {
            if (isLocationProviderEnabled()) {
                fusedLocationProviderClient =
                    LocationServices.getFusedLocationProviderClient(this)
                fusedLocationProviderClient.requestLocationUpdates(
                    locationRequest,
                    locationCallback,
                    Looper.getMainLooper()
                )
            } else {
                enableLocationSetting()
            }
        } else {
            requestPermission()

        }
    }

    private fun requestPermission() {
        ActivityCompat.requestPermissions(this, permission, requestId)
//        if (!checkLocationPermission()) requestPermission()

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == requestId) {

            if (permissions.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                // show some toast to tell user
                Toast.makeText(this, "location permission refused", Toast.LENGTH_SHORT).show()
                // make what You want
            } else {
                Toast.makeText(this, "location permission granted", Toast.LENGTH_SHORT).show()
                getLocation()
                finish()
                startActivity(intent)
            }
        }


    }

    private fun isFirst(): Boolean {
        return initSharedPref(this).getBoolean(getString(R.string.FIRST), true)
    }

    private fun isLocationProviderEnabled(): Boolean {
        val locationManager = getSystemService(LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
    }


    private val locationCallback: LocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            super.onLocationResult(locationResult)
            val location = locationResult.lastLocation
            //store shredPref


            sharedPreferences.edit().apply {
                putFloat(getString(R.string.LAT), location.latitude.toFloat())
                putFloat(getString(R.string.LON), location.longitude.toFloat())
                Log.i("AA", "The Result is: " + location.latitude)
                apply()
            }
            fusedLocationProviderClient.removeLocationUpdates(this)
            Log.i("HOSSAM", "onLocationResult: ")

        }
    }

    private fun enableLocationSetting() {
        val settingIntent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
        startActivity(settingIntent)
    }

    override fun onDismiss(p0: DialogInterface?) {

        finish()
        startActivity(intent)
    }
}