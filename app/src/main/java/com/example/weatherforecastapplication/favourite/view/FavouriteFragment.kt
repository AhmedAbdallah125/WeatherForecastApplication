package com.example.weatherforecastapplication.favourite.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherforecastapplication.R
import com.example.weatherforecastapplication.databinding.FragmentFavouriteBinding
import com.example.weatherforecastapplication.databinding.FragmentHomeBinding
import com.example.weatherforecastapplication.datasource.local.ConcreteLocalSource
import com.example.weatherforecastapplication.datasource.network.RetrofitHelper
import com.example.weatherforecastapplication.favourite.viewmodel.FavouriteViewModel
import com.example.weatherforecastapplication.favourite.viewmodel.FavouriteViewModelFactory
import com.example.weatherforecastapplication.home.view.WeatherDayAdapter
import com.example.weatherforecastapplication.home.viewmodel.WeatherViewModel
import com.example.weatherforecastapplication.home.viewmodel.WeatherViewModelFactory
import com.example.weatherforecastapplication.model.*


class FavouriteFragment : Fragment() {

    private var _binding: FragmentFavouriteBinding? = null
    private lateinit var favWeatherAdapter: FavouriteAdapter

    //viewModels
    private val viewModel: FavouriteViewModel by viewModels {
        FavouriteViewModelFactory(Repository(ConcreteLocalSource(requireContext()), RetrofitHelper))
    }

    private val binding get() = _binding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentFavouriteBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initFavWeatherRecycle()
        binding.fabAddLocation.setOnClickListener {
            // go to map
            // will return lat and long
            Navigation.findNavController(requireView())
                .navigate(R.id.action_favouriteFragment_to_mapsFragment)
        }
    }

    override fun onResume() {
        super.onResume()
        // if previous destination is map means is adding
        when (Navigation.findNavController(requireView()).previousBackStackEntry?.destination?.id) {
            R.id.mapsFragment -> {
                val lat = initFavSharedPref(requireContext()).getFloat(getString(R.string.LAT), 0f)
                val long = initFavSharedPref(requireContext()).getFloat(getString(R.string.LON), 0f)
                //means add
                viewModel.getWeather(
                    lat.toDouble(),
                    long.toDouble(),
                    getCurrentLan(requireContext()),
                    getCurrentUnit(requireContext())
                )
                viewModel.favWeather.observe(viewLifecycleOwner) {
                    bindFavWeather(it)

                }
            }
            else -> {
                // just retrieve Data
                viewModel.getLocalFavWeathers()
                viewModel.favWeather.observe(viewLifecycleOwner) {
                    bindFavWeather(it)

                }
            }

        }
    }

    private fun initFavWeatherRecycle() {
        favWeatherAdapter = FavouriteAdapter(requireParentFragment())
        binding.recFavWeathers.apply {
            this.adapter = favWeatherAdapter
            this.layoutManager = LinearLayoutManager(
                requireParentFragment().requireContext(),
                RecyclerView.VERTICAL, false
            )
        }
    }

    private fun bindFavWeather(favWeathers: List<OpenWeatherJason>) {
        Toast.makeText(requireContext(), favWeathers[0].toString(), Toast.LENGTH_SHORT).show()

        favWeatherAdapter.setFavWeather(favWeathers)
    }
}