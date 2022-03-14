package com.example.weatherforecastapplication

import android.Manifest
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.example.weatherforecastapplication.databinding.FragmentHomeBinding
import com.example.weatherforecastapplication.databinding.FragmentSettingScreenBinding
import com.example.weatherforecastapplication.model.*


class SettingScreen : Fragment() {
    // for permission
    private val permission = arrayOf<String>(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION
    )
    private val requestId = 22
    private var _binding: FragmentSettingScreenBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentSettingScreenBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onResume() {

        super.onResume()
        // check for every setting
        handleRadioButton(requireContext())
        // handle Map
        binding.radioButtonMaps.setOnClickListener {
            // init for GPS
            initSharedPref(requireContext()).edit().apply() {
                //1 meaning GPS
                putInt(getString(R.string.LOCATION), 1)
                apply()
            }
            if (binding.radioButtonMaps.isChecked) {
                Navigation.findNavController(binding.root)
                    .navigate(R.id.action_settingScreen_to_mapsFragment)
            }
        }
        // for location
        binding.radioButtonGPS.setOnClickListener {
            //0 means
            if (binding.radioButtonGPS.isChecked) {
                initSharedPref(requireContext()).edit().apply {
                    putInt(getString(R.string.LOCATION), 1)
                    apply()
                }
                Navigation.findNavController(binding.root).navigate(R.id.action_settingScreen_to_navigation_home)
            }
        }


    }



    private fun handleRadioButton(context: Context) {
        handleUnitRadio(context)
        handleLanRadio(context)
    }

    private fun handleUnitRadio(context: Context) {
        when (getCurrentUnit(requireContext())) {
            Units.METRIC.name -> {
                updateUnit(true)
            }
            Units.IMPERIAL.name -> {
                updateUnit(imperial = true)
            }
            Units.STANDARD.name -> {
                updateUnit(standard = true)
            }
        }
    }

    private fun handleLanRadio(context: Context) {
        // get First
        when (getCurrentLan(context)) {
            Languages.ENGLISH.name -> {
                updateLan(english = true)
            }
            Languages.ARABIC.name -> {
                updateLan(arabic = true)
            }
        }
    }

    private fun updateUnit(
        metric: Boolean = false,
        imperial: Boolean = false,
        standard: Boolean = false
    ) {
        binding.radioButtonTempMetricCelsius.isChecked = metric
        binding.radioButtonTempMetricFahrenheit.isChecked = imperial
        binding.radioButtonTempMetricKelvin.isChecked = standard
    }

    private fun updateLan(english: Boolean = false, arabic: Boolean = false) {
        binding.radioButtonEnglish.isChecked = english
        binding.radioButtonArabic.isChecked = arabic
    }

}