package com.example.weatherforecastapplication.map

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.example.weatherforecastapplication.R
import com.example.weatherforecastapplication.databinding.FragmentMapsBinding
import com.example.weatherforecastapplication.model.initFavSharedPref
import com.example.weatherforecastapplication.model.initSharedPref
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.MarkerOptions
import java.util.*


class MapsFragment : Fragment() {
    // handle offline
    private lateinit var binding: FragmentMapsBinding
    private val callback = OnMapReadyCallback { googleMap ->


        googleMap.setOnMapClickListener {
//1 means GPS //2 means MAPS //3 draw Location
            // inti marker option
            var lat: Float = 0f
            var long: Float = 0f
            val marker = MarkerOptions().apply {
                position(it)
                lat = it.latitude.toFloat()
                long = it.longitude.toFloat()
                title((lat).plus(long).toString())
                googleMap.clear()
                googleMap.animateCamera(
                    CameraUpdateFactory.newLatLngZoom(
                        it, 10f
                    )
                )
            }
            googleMap.addMarker(marker)
            changeSaveCondition(false)

            binding.btnSave.setOnClickListener {
                handleSaveClickable(lat, long)
            }
        }


    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMapsBinding.inflate(layoutInflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
    }

    private fun changeSaveCondition(visible: Boolean) {
        if (visible) {
            binding.btnSave.visibility = View.INVISIBLE
            binding.btnSave.isClickable = false
        } else {
            binding.btnSave.visibility = View.VISIBLE
            binding.btnSave.isClickable = true
        }
    }

    private fun handleSaveClickable(lat: Float, long: Float) {

        when (Navigation.findNavController(requireView()).previousBackStackEntry?.destination?.id) {
            R.id.favouriteFragment -> {
                Navigation.findNavController(requireView())
                    .navigate(R.id.action_mapsFragment_to_favouriteFragment)
                initFavSharedPref(requireContext())
                    .edit()
                    .apply {
                        putFloat(getString(R.string.LON), long)
                        putFloat(getString(R.string.LAT), lat)
                        apply()
                    }
            }
            else -> {
                Navigation.findNavController(requireView())
                    .navigate(R.id.action_mapsFragment_to_navigation_home)
                initSharedPref(requireContext())
                    .edit()
                    .apply {
                        putFloat(getString(R.string.LON), long)
                        putFloat(getString(R.string.LAT), lat)
                        putInt(getString(R.string.LOCATION), 3)
                        apply()
                    }
            }
        }

        changeSaveCondition(true)
    }
}