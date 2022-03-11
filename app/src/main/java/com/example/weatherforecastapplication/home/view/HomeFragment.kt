package com.example.weatherforecastapplication.home.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.coroutineScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.work.ListenableWorker
import com.example.weatherforecastapplication.R
import com.example.weatherforecastapplication.databinding.FragmentHomeBinding
import com.example.weatherforecastapplication.datasource.local.ConcreteLocalSource
import com.example.weatherforecastapplication.datasource.local.WeatherDataBase
import com.example.weatherforecastapplication.datasource.network.RetrofitHelper
import com.example.weatherforecastapplication.home.viewmodel.HomeViewModel
import com.example.weatherforecastapplication.home.viewmodel.WeatherViewModel
import com.example.weatherforecastapplication.home.viewmodel.WeatherViewModelFactory
import com.example.weatherforecastapplication.model.*
import com.example.weatherforecastapplication.model.Result.Success
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private lateinit var weatherDayAdapter: WeatherDayAdapter
    private lateinit var weatherHourAdapter: WeatherHourAdapter
    private lateinit var conditionAdapter: ConditionAdapter

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
        val homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)

//        val textView: TextView = binding.textHome
//        homeViewModel.text.observe(viewLifecycleOwner) {
//            textView.text = it
//        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initHourRecycle()
        intDayRecycle()
        initConditions()
        // trying
        val lat = 31.4175
        val long = 31.8144
        viewModel.getWeather(lat, long)
        viewModel.openWeather.observe(viewLifecycleOwner) {
            bindViewCurrent(openWeatherJason = it)
            // bind others
            bindCurrentGrid(it.current)
            bindHourlyWeather(it.hourly)
            bindDailyWeather(it.daily)
        }


    }


    private fun bindViewCurrent(openWeatherJason: OpenWeatherJason) {
        binding.txtCityName.text = openWeatherJason.timezone
        binding.txtTodayTemp.text = openWeatherJason.current.temp.toString()
        binding.txtTodatDesc.text = openWeatherJason.current.weather[0].description
        binding.imgCurrent.setImageResource(getIconImage(openWeatherJason.current.weather[0].icon!!))
    }

    private fun bindCurrentGrid(current: Current) {
        var weatherCondition = listOf<Condition>(
            Condition(
                R.drawable.ic_pressure,
                ("" + current.pressure ?: 0) as String,
                getString(
                    R.string.Pressure
                )
            ),
            Condition(
                R.drawable.ic_humidity,
                ("" + current.humidity ?: 0) as String,
                getString(
                    R.string.Humidity
                )
            ),
            Condition(
                R.drawable.ic_cloudy, ("" + current.clouds ?: 0) as String, getString(
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
                ("" + current.visibility ?: 0) as String,
                getString(
                    R.string.Visibility
                )
            ),
            Condition(
                R.drawable.ic_windspeed,
                ("" + current.windSpeed ?: 0) as String,
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
}