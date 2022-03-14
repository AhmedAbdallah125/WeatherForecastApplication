package com.example.weatherforecastapplication.home.view

import android.content.DialogInterface
import android.content.SharedPreferences
import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherforecastapplication.R
import com.example.weatherforecastapplication.databinding.FragmentHomeBinding
import com.example.weatherforecastapplication.datasource.local.ConcreteLocalSource
import com.example.weatherforecastapplication.datasource.network.RetrofitHelper
import com.example.weatherforecastapplication.home.viewmodel.HomeActivityViewModel
import com.example.weatherforecastapplication.home.viewmodel.HomeViewModel
import com.example.weatherforecastapplication.home.viewmodel.WeatherViewModel
import com.example.weatherforecastapplication.home.viewmodel.WeatherViewModelFactory
import com.example.weatherforecastapplication.model.*
import java.util.*

class HomeFragment : Fragment() {

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
        //
        sharedPreferences = initSharedPref(requireContext())
        if (sharedPreferences.getInt(getString(R.string.MAP), 0) == 1) {
            // means choose map
            initSharedPref(requireContext()).edit()
                .apply {
                    putInt(getString(R.string.MAP), 1)
                    apply()
                }
            Navigation.findNavController(requireView())
                .navigate(R.id.action_navigation_home_to_mapsFragment)
        }


    }

    override fun onResume() {
        super.onResume()


        // trying
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


    private fun bindViewCurrent(openWeatherJason: OpenWeatherJason) {
        binding.txtCityName.text = getCityText(
            openWeatherJason.lat, openWeatherJason.lon
        )


        binding.txtTodayTemp.text = openWeatherJason.current.temp.toString().plus(
            getCurrentTemperature(requireContext())
        )
        binding.txtTodatDesc.text = openWeatherJason.current.weather[0].description
        binding.imgCurrent.setImageResource(getIconImage(openWeatherJason.current.weather[0].icon!!))
        binding.txtTodayDate.text = (convertToDate(openWeatherJason.current.dt))
    }

    private fun bindCurrentGrid(current: Current) {
        val weatherCondition = listOf<Condition>(
            Condition(
                R.drawable.ic_pressure,
                ("${current.pressure ?: 0} ${getString(R.string.Pascal)}"),
                getString(
                    R.string.Pressure
                )
            ),
            Condition(
                R.drawable.ic_humidity,
                ("${current.humidity ?: 0} %") as String,
                getString(
                    R.string.Humidity
                )
            ),
            Condition(
                R.drawable.ic_cloudy, ("${current.clouds} "), getString(
                    R.string.Cloud
                )
            ),
            Condition(
                R.drawable.ic_sunrise,
                convertToTime(current.sunrise!!.toLong()),
                getString(
                    R.string.Sun_Rise
                )
            ),
            Condition(
                R.drawable.ic_visibility,
                ("${current.visibility ?: 0}"),
                getString(
                    R.string.Visibility
                )
            ),
            Condition(
                R.drawable.ic_windspeed,
                ("${current.windSpeed ?: 0} ${getCurrentSpeed(requireContext())} "),
                getString(
                    R.string.WindSpeed
                )
            )

        )

        conditionAdapter.setConditions(weatherCondition)
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

    private fun getCityText(lat: Double, lon: Double): String {
        var city = "Unknown!"
        val geocoder = Geocoder(requireContext(), Locale("en"))
        val addresses: List<Address> = geocoder.getFromLocation(lat, lon, 1)
        if (addresses.isNotEmpty()) {
            val state = addresses[0].adminArea
            val country = addresses[0].countryName
            city = "$state, $country"
        }
        return city
    }


}