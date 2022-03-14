package com.example.weatherforecastapplication

import android.location.Geocoder
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.example.weatherforecastapplication.model.initSharedPref
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.GoogleMap.OnMarkerDragListener
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import java.io.IOException
import java.util.*


class MapsFragment : Fragment() {

    private val callback = OnMapReadyCallback { googleMap ->

//        val cairo = LatLng(30.044, 31.235)
//        googleMap.moveCamera(CameraUpdateFactory.newLatLng(cairo))
//        // Enable the zoom controls for the map
        //            googleMap.moveCamera(CameraUpdateFactory.newLatLng(LatLng(it.latitude, it.longitude)))
//            googleMap.uiSettings.isZoomControlsEnabled = true
        googleMap.setOnMapClickListener {

            // inti marker option
            val marker =MarkerOptions().apply {
                position(it)
                title((it.latitude).plus(it.longitude).toString())
            }
            googleMap.addMarker(marker)
            initSharedPref(requireContext())
                .edit()
                .apply {
                    putFloat(getString(R.string.LON), it.longitude.toFloat())
                    putFloat(getString(R.string.LAT), it.latitude.toFloat())
                    putInt(getString(R.string.MAP),0)
                    apply()
                }
            Navigation.findNavController(this.requireView())
                .navigate(R.id.action_mapsFragment_to_navigation_home)


            //init shared
            //Navigate to home screen
            // check shared not empty
            // must be online
            // will be dismissed locato
            //lat long
        }


    }
    // must init Shared pref


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_maps, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
    }
}