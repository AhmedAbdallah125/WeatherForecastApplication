package com.example.weatherforecastapplication.home.view

import android.graphics.drawable.GradientDrawable
import android.icu.lang.UCharacter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherforecastapplication.R
import com.example.weatherforecastapplication.databinding.FragmentHomeBinding
import com.example.weatherforecastapplication.home.viewmodel.HomeViewModel
import com.example.weatherforecastapplication.model.WeatherDay
import com.example.weatherforecastapplication.model.WeatherHour

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private lateinit var weatherDayAdapter: WeatherDayAdapter
    private lateinit var weatherHourAdapter: WeatherHourAdapter

    // This property is only valid between onCreateView and
    // onDestroyView.
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
        addDummyData()

    }

    private fun addDummyData() {
        var weatherDays = listOf<WeatherDay>(
            WeatherDay("Tommorow", "22/7", R.drawable.ic_baseline_wb_sunny_24, "clear", "22 C"),
            WeatherDay("Tommorow", "22/7", R.drawable.ic_baseline_wb_sunny_24, "clear", "22 C"),
            WeatherDay("Tommorow", "22/7", R.drawable.ic_baseline_wb_sunny_24, "clear", "22 C"),
            WeatherDay("Tommorow", "22/7", R.drawable.ic_baseline_wb_sunny_24, "clear", "22 C"),
            WeatherDay("Tommorow", "22/7", R.drawable.ic_baseline_wb_sunny_24, "clear", "22 C"),
            WeatherDay("Tommorow", "22/7", R.drawable.ic_baseline_wb_sunny_24, "clear", "22 C"),
        )
        var weatherHour = listOf<WeatherHour>(
            WeatherHour("12AM", R.drawable.ic_baseline_wb_sunny_24, "22C"),
            WeatherHour("12AM", R.drawable.ic_baseline_wb_sunny_24, "22C"),
            WeatherHour("12AM", R.drawable.ic_baseline_wb_sunny_24, "22C"),
            WeatherHour("12AM", R.drawable.ic_baseline_wb_sunny_24, "22C"),
            WeatherHour("12AM", R.drawable.ic_baseline_wb_sunny_24, "22C"),
            WeatherHour("12AM", R.drawable.ic_baseline_wb_sunny_24, "22C"),
            WeatherHour("12AM", R.drawable.ic_baseline_wb_sunny_24, "22C"),
            WeatherHour("12AM", R.drawable.ic_baseline_wb_sunny_24, "22C"),
            WeatherHour("12AM", R.drawable.ic_baseline_wb_sunny_24, "22C"),
            WeatherHour("12AM", R.drawable.ic_baseline_wb_sunny_24, "22C"),
            WeatherHour("12AM", R.drawable.ic_baseline_wb_sunny_24, "22C"),
            WeatherHour("12AM", R.drawable.ic_baseline_wb_sunny_24, "22C"),
            WeatherHour("12AM", R.drawable.ic_baseline_wb_sunny_24, "22C"),
            WeatherHour("12AM", R.drawable.ic_baseline_wb_sunny_24, "22C"),
            WeatherHour("12AM", R.drawable.ic_baseline_wb_sunny_24, "22C"),
            WeatherHour("12AM", R.drawable.ic_baseline_wb_sunny_24, "22C"),
            WeatherHour("12AM", R.drawable.ic_baseline_wb_sunny_24, "22C"),
            WeatherHour("12AM", R.drawable.ic_baseline_wb_sunny_24, "22C"),
            WeatherHour("12AM", R.drawable.ic_baseline_wb_sunny_24, "22C"),
        )
        weatherHourAdapter.setWeatherHours(weatherHour)
        weatherDayAdapter.setWeatherDay(weatherDays)
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