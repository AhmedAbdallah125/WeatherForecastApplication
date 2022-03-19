package com.example.weatherforecastapplication.favourite.view

import android.app.AlertDialog
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
import com.example.weatherforecastapplication.datasource.local.ConcreteLocalSource
import com.example.weatherforecastapplication.datasource.network.RetrofitHelper
import com.example.weatherforecastapplication.favourite.viewmodel.FavouriteViewModel
import com.example.weatherforecastapplication.favourite.viewmodel.FavouriteViewModelFactory
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
            if (isConnected(requireContext())) {
                initFavSharedPref(requireContext()).edit().apply {
                    putInt(requireContext().getString(R.string.ID), -3)
                    apply()
                }
                // go to map
                // will return lat and long
                Navigation.findNavController(requireView())
                    .navigate(R.id.action_favouriteFragment_to_mapsFragment)
            } else
                Toast.makeText(
                    requireContext(),
                    getString(R.string.YMCN),
                    Toast.LENGTH_SHORT
                ).show()

        }
    }

    override fun onResume() {
        super.onResume()
        // if previous destination is map means is adding
        if ((initFavSharedPref(requireContext()).getInt("SIGN", 0) == 2)) {
            val lat = initFavSharedPref(requireContext()).getFloat(getString(R.string.LAT), 0f)
            val long = initFavSharedPref(requireContext()).getFloat(getString(R.string.LON), 0f)
            //means add
            viewModel.getWeather(
                lat.toDouble(),
                long.toDouble(),
                getCurrentLan(requireContext()),
                getCurrentUnit(requireContext())
            )
            initFavSharedPref(requireContext()).edit().apply {
                putInt("SIGN", 0)
                apply()
            }
            viewModel.favWeather.observe(viewLifecycleOwner) {
                if (it.isEmpty()) {
                    binding.recFavWeathers.visibility = View.GONE
                    binding.imgEmpty.visibility = View.VISIBLE
                    binding.txtEmpty.visibility = View.VISIBLE
                } else {
                    binding.recFavWeathers.visibility = View.VISIBLE
                    binding.imgEmpty.visibility = View.GONE
                    binding.txtEmpty.visibility = View.GONE
                    bindFavWeather(it)
                }

            }
        } else {
            // just retrieve Data
            viewModel.getLocalFavWeathers()
            viewModel.favWeather.observe(viewLifecycleOwner) {
                if (it.isEmpty()) {
                    binding.recFavWeathers.visibility = View.GONE
                    binding.imgEmpty.visibility = View.VISIBLE
                    binding.txtEmpty.visibility = View.VISIBLE
                } else {
                    binding.recFavWeathers.visibility = View.VISIBLE
                    binding.imgEmpty.visibility = View.GONE
                    binding.txtEmpty.visibility = View.GONE
                    bindFavWeather(it)
                }

            }


        }
    }

    private fun initFavWeatherRecycle() {
        favWeatherAdapter = FavouriteAdapter(requireParentFragment(), deleteAction)
        binding.recFavWeathers.apply {
            this.adapter = favWeatherAdapter
            this.layoutManager = LinearLayoutManager(
                requireParentFragment().requireContext(),
                RecyclerView.VERTICAL, false
            )
        }
    }

    private fun bindFavWeather(favWeathers: List<OpenWeatherJason>) {
        favWeatherAdapter.setFavWeather(favWeathers)
    }

    // lambda
    var deleteAction: (Int, String) -> Unit = { id: Int, cityName: String ->
        deleteFavWeather(id, cityName)
    }

    private fun deleteFavWeather(id: Int, cityName: String) {
        val builder = AlertDialog.Builder(requireContext())
        builder.setPositiveButton("Yes") { _, _ ->
            viewModel.deleteFavWeather(id)
            Toast.makeText(requireContext(), getString(R.string.SuccessDeleted), Toast.LENGTH_SHORT)
                .show()
        }
        builder.setNegativeButton("No") { _, _ ->

        }
        builder.setTitle(getString(R.string.Delete).plus(cityName))
        builder.setMessage(getString(R.string.AWTD).plus(cityName))
        builder.create().show()

    }
}