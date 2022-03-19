package com.example.weatherforecastapplication.home.view

import android.Manifest
import android.content.Context
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherforecastapplication.R
import com.example.weatherforecastapplication.databinding.FragmentHomeBinding
import com.example.weatherforecastapplication.datasource.local.ConcreteLocalSource
import com.example.weatherforecastapplication.datasource.network.RetrofitHelper
import com.example.weatherforecastapplication.home.viewmodel.WeatherViewModel
import com.example.weatherforecastapplication.home.viewmodel.WeatherViewModelFactory
import com.example.weatherforecastapplication.model.*
import com.google.android.gms.location.*
import pub.devrel.easypermissions.EasyPermissions
import java.util.*

class HomeFragment : Fragment(), EasyPermissions.PermissionCallbacks {
    // for permission
    private val permission = arrayOf<String>(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION
    )
    private val requestId = 22
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    //
    private var _binding: FragmentHomeBinding? = null
    private lateinit var weatherDayAdapter: WeatherDayAdapter
    private lateinit var weatherHourAdapter: WeatherHourAdapter
    private lateinit var conditionAdapter: ConditionAdapter
    private lateinit var sharedPreferences: SharedPreferences

    //viewModels
    private val viewModel: WeatherViewModel by viewModels {
        WeatherViewModelFactory(Repository(ConcreteLocalSource(requireContext()), RetrofitHelper))
    }

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentHomeBinding.inflate(inflater, container, false)


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initHourRecycle()
        intDayRecycle()
        initConditions()
        //1 means GPS //2 means MAPS //3 draw Location
        sharedPreferences = initSharedPref(requireContext())
        if (sharedPreferences.getInt(getString(R.string.LOCATION), 3) == 2) {
            // means choose map
            initSharedPref(requireContext()).edit()
                .apply {
                    putInt(getString(R.string.LOCATION), 3)
                    apply()
                }
            Navigation.findNavController(requireView())
                .navigate(R.id.action_navigation_home_to_mapsFragment)
        }


    }

    override fun onResume() {
        super.onResume()
//1 means GPS //2 means MAPS //3 draw Location

        if (!isConnected(requireContext())) {
            Toast.makeText(
                requireContext(),
                getString(R.string.YMCN),
                Toast.LENGTH_SHORT
            ).show()
        }

        // handle if come from map from favourite
        // if come from fav and condition
        val flag = initFavSharedPref(requireContext()).getInt(getString(R.string.FAV_FLAG), 0)
        if (flag == 1 &&
            Navigation.findNavController(requireView()).previousBackStackEntry?.destination?.id == R.id.favouriteFragment
        ) {


            //require get Data
            val lat = initFavSharedPref(requireContext()).getFloat(getString(R.string.LAT), 0f)
            val long = initFavSharedPref(requireContext()).getFloat(getString(R.string.LON), 0f)
            initFavSharedPref(requireContext()).edit().apply {
                putInt(getString(R.string.FAV_FLAG), 0)
                apply()
            }

            viewModel.getWeather(
                lat.toDouble(),
                long.toDouble(),
                getCurrentLan(requireContext()),
                getCurrentUnit(requireContext()), true
            )
            viewModel.openWeather.observe(viewLifecycleOwner) {
                bindViewCurrent(openWeatherJason = it)
                // bind others
                bindCurrentGrid(it.current)
                bindHourlyWeather(it.hourly)
                bindDailyWeather(it.daily)

            }

            //then update value
            initFavSharedPref(requireContext()).edit().apply {
                putInt(getString(R.string.FAV_FLAG), 0)
                apply()
            }

        } else {
            if (checkGPS()) {
                Log.i("GEDO", "commm: ")
                getLocation()
                initSharedPref(requireContext()).edit().apply {
                    putInt(getString(R.string.LOCATION), 3)
                    apply()
                }

            }
            // handle home
            // trying
            val lat = sharedPreferences.getFloat(getString(R.string.LAT), 0f)
            val long = sharedPreferences.getFloat(getString(R.string.LON), 0f)

            if (lat != 0f) {
                viewModel.getWeather(
                    lat.toDouble(),
                    long.toDouble(),
                    getCurrentLan(requireContext()),
                    getCurrentUnit(requireContext()), false
                )
                viewModel.openWeather.observe(viewLifecycleOwner) {
                    bindViewCurrent(openWeatherJason = it)
                    // bind others
                    bindCurrentGrid(it.current)
                    bindHourlyWeather(it.hourly)
                    bindDailyWeather(it.daily)

                }

            }
        }


    }
//

    private fun checkLocationPermission(): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            when {
                ContextCompat.checkSelfPermission(
                    requireActivity().applicationContext, Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
                        &&
                        ContextCompat.checkSelfPermission(
                            requireActivity().applicationContext,
                            Manifest.permission.ACCESS_COARSE_LOCATION
                        ) == PackageManager.PERMISSION_GRANTED
                -> {
                    Toast.makeText(
                        requireActivity().applicationContext,
                        getString(R.string.LPG),
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

    private fun hasPermissions(context: Context) =
        EasyPermissions.hasPermissions(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )

    private fun getLocation() {
        requestPermission()
        // handle the updated Gps
        val lat = sharedPreferences.getFloat(getString(R.string.LAT), 0f)
        val long = sharedPreferences.getFloat(getString(R.string.LON), 0f)

        if (lat != 0f) {
            viewModel.getWeather(
                lat.toDouble(),
                long.toDouble(),
                getCurrentLan(requireContext()),
                getCurrentUnit(requireContext())
            )
            viewModel.openWeather.observe(viewLifecycleOwner) {
                bindViewCurrent(openWeatherJason = it)
                // bind others
                bindCurrentGrid(it.current)
                bindHourlyWeather(it.hourly)
                bindDailyWeather(it.daily)

            }


        }

    }

    private fun requestPermission() {
        EasyPermissions.requestPermissions(
            this,
            "you need to accept location to get weather correctly",
            requestId,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION

        )
    }

    private fun isLocationProviderEnabled(): Boolean {
        val locationManager =
            requireActivity().getSystemService(AppCompatActivity.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
    }


    private val locationCallback: LocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            super.onLocationResult(locationResult)
            val location = locationResult.lastLocation
            fusedLocationProviderClient.removeLocationUpdates(this)

            //store shredPref
            sharedPreferences.edit().apply {
                putFloat(getString(R.string.LAT), location.latitude.toFloat())
                putFloat(getString(R.string.LON), location.longitude.toFloat())
                Log.i("AA", "The Result is: " + location.latitude)
                apply()
            }
            // handle the updated one
            // handle the updated Gps
            val lat = sharedPreferences.getFloat(getString(R.string.LAT), 0f)
            val long = sharedPreferences.getFloat(getString(R.string.LON), 0f)

            if (lat != 0f) {
                viewModel.getWeather(
                    lat.toDouble(),
                    long.toDouble(),
                    getCurrentLan(requireContext()),
                    getCurrentUnit(requireContext())
                )
                viewModel.openWeather.observe(viewLifecycleOwner) {
                    bindViewCurrent(openWeatherJason = it)
                    // bind others
                    bindCurrentGrid(it.current)
                    bindHourlyWeather(it.hourly)
                    bindDailyWeather(it.daily)

                }
            }

        }

    }



    //
    private fun bindViewCurrent(openWeatherJason: OpenWeatherJason) {
        binding.txtCityName.text = getCityText(
            openWeatherJason.lat, openWeatherJason.lon, openWeatherJason.timezone
        )
        var temNumber = ""
        temNumber = if (getCurrentLan(requireContext()) == "ar") {
            convertNumbersToArabic(openWeatherJason.current.temp!!)
        } else {
            openWeatherJason.current.temp.toString()
        }

        binding.txtTodayTemp.text = temNumber.plus(" ").plus(
            getCurrentTemperature(requireContext())
        )
        binding.txtTodatDesc.text = openWeatherJason.current.weather[0].description
        binding.imgCurrent.setImageResource(getIconImage(openWeatherJason.current.weather[0].icon!!))
        binding.txtTodayDate.text = (convertToDate(openWeatherJason.current.dt, requireContext()))
    }

    private fun bindCurrentGrid(current: Current) {
        var isArabic = getCurrentLan(requireContext()) == "ar"
        var preesure =current.pressure.toString()
        var humidity=current.humidity.toString()
        var cloud=current.clouds.toString()
        var visibilty=current.visibility.toString()
        var wind=current.windSpeed.toString()
        if (isArabic) {
            preesure= convertNumbersToArabic(current.pressure!!)
            wind = convertNumbersToArabic(current.windSpeed!!)
            cloud = convertNumbersToArabic(current.clouds!!)
            visibilty = convertNumbersToArabic(current.visibility!!)
        }

        val weatherCondition = listOf<Condition>(
            Condition(
                R.drawable.ic_pressure,
                ("${preesure} ${getString(R.string.Pascal)}"),
                getString(
                    R.string.Pressure
                )
            ),
            Condition(
                R.drawable.ic_humidity,
                ("${humidity} %") as String,
                getString(
                    R.string.Humidity
                )
            ),
            Condition(
                R.drawable.ic_cloudy, ("${cloud} "), getString(
                    R.string.Cloud
                )
            ),
            Condition(
                R.drawable.ic_sunrise,
                convertToTime(current.sunrise!!.toLong(), requireContext()),
                getString(
                    R.string.Sun_Rise
                )
            ),
            Condition(
                R.drawable.ic_visibility_newdp,
                ("$visibilty"),
                getString(
                    R.string.Visibility
                )
            ),
            Condition(
                R.drawable.ic_windspeed,
                ("$wind ${getCurrentSpeed(requireContext())} "),
                getString(
                    R.string.WindSpeed
                )
            )

        )

        conditionAdapter.setConditions(weatherCondition)
    }

    //1 means GPS //2 means MAPS //3 draw Location
    private fun checkGPS(): Boolean {
        return initSharedPref(requireContext()).getInt(getString(R.string.LOCATION), 3) == 1
    }

    private fun checkMap(): Boolean {
        return initSharedPref(requireContext()).getInt(getString(R.string.LOCATION), 3) == 2

    }

    private fun bindDailyWeather(dailyList: List<Daily>) {
        weatherDayAdapter.setWeatherDay(dailyList)
    }

    private fun bindHourlyWeather(hourlyList: List<Hourly>) {
        weatherHourAdapter.setWeatherHours(hourlyList)
    }

    private fun intDayRecycle() {
        weatherDayAdapter = WeatherDayAdapter(requireParentFragment())
        _binding?.recyViewDayWeather.apply {
            this?.adapter = weatherDayAdapter
            this?.layoutManager = LinearLayoutManager(
                requireParentFragment().requireContext(),
                RecyclerView.VERTICAL, false
            )
        }
    }

    private fun initConditions() {
        conditionAdapter = ConditionAdapter(requireParentFragment())
        _binding?.recyViewConditionDescription.apply {
            this?.adapter = conditionAdapter
            this?.layoutManager = GridLayoutManager(
                requireContext(), 3, RecyclerView.VERTICAL, false
            )
            this?.setHasFixedSize(true)
        }
    }

    private fun initHourRecycle() {
        weatherHourAdapter = WeatherHourAdapter(requireParentFragment())
        _binding?.recyViewHourWeather.apply {
            this?.adapter = weatherHourAdapter
            this?.layoutManager = LinearLayoutManager(
                requireParentFragment().requireContext(),
                RecyclerView.HORIZONTAL, false
            )
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun getCityText(lat: Double, lon: Double, timezone: String): String {
        var city = "city"
        Locale.setDefault(Locale(getCurrentLan(requireContext()), "eg"))
        val new_locale = Locale.getDefault()
        val geocoder =
            Geocoder(requireContext(), new_locale)
        val addresses: List<Address> = geocoder.getFromLocation(lat, lon, 1)
        if (addresses.isNotEmpty()) {
            val state = addresses[0].adminArea
            val country = addresses[0].countryName
            city = "$state, $country"
            if (state.isNullOrEmpty()) city = country
        }
        if (city == "city")
            city = timezone
        return city
    }

    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {
        // handle the result
        // trying
        val locationRequest: LocationRequest = LocationRequest.create()
        locationRequest.apply {
            interval = 1000
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }
        if (checkLocationPermission()) {
            if (isLocationProviderEnabled()) {
                fusedLocationProviderClient =
                    LocationServices.getFusedLocationProviderClient(requireContext())
                fusedLocationProviderClient.requestLocationUpdates(
                    locationRequest,
                    locationCallback,
                    Looper.getMainLooper()
                )
            }
        }


    }

    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {
        // apologize
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)


    }
}