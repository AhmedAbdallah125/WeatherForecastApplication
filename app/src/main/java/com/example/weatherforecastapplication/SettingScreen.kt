package com.example.weatherforecastapplication

import android.Manifest
import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import androidx.navigation.Navigation
import com.example.weatherforecastapplication.databinding.FragmentSettingScreenBinding
import com.example.weatherforecastapplication.model.*
import java.util.*


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
                Navigation.findNavController(binding.root)
                    .navigate(R.id.action_settingScreen_to_navigation_home)
            }
        }
        // handle change in Units
        binding.radioButtonTempMetricCelsius.setOnClickListener {
            onUnitsRadioButtonClicked(it)
        }
        binding.radioButtonTempMetricKelvin.setOnClickListener {
            onUnitsRadioButtonClicked(it)

        }
        binding.radioButtonTempMetricFahrenheit.setOnClickListener {
            onUnitsRadioButtonClicked(it)
        }
        // handle changes in Lan
        binding.radioButtonEnglish.setOnClickListener {
            onLanRadioButtonClicked(it)
        }
        binding.radioButtonArabic.setOnClickListener {
            onLanRadioButtonClicked(it)
        }

    }

    private fun onUnitsRadioButtonClicked(view: View) {
        if (view is RadioButton) {
            // Is the button now checked?
            val checked = view.isChecked

            // Check which radio button was clicked
            when (view.getId()) {
                R.id.radioButton_temp_metric_celsius ->
                    if (checked) {
                        initUNIT(Units.METRIC.name, requireContext())

                    }
                R.id.radioButton_temp_metric_Kelvin ->
                    if (checked) {
                        initUNIT(Units.STANDARD.name, requireContext())

                    }
                R.id.radioButton_temp_metric_Fahrenheit ->
                    if (checked) {
                        initUNIT(Units.IMPERIAL.name, requireContext())
                    }
            }
        }
    }

    private fun onLanRadioButtonClicked(view: View) {
        if (view is RadioButton) {
            // Is the button now checked?
            val checked = view.isChecked

            // Check which radio button was clicked
            when (view.getId()) {
                R.id.radioButton_English ->
                    if (checked) {
                        initLan("en", requireContext())
                        setLan("en")
                    }
                R.id.radio_button_Arabic ->
                    if (checked) {
                        initLan("ar", requireContext())
                        setLan("ar")
                    }
            }
        }
        Navigation.findNavController(requireView()).navigate(R.id.action_settingScreen_to_navigation_home)

    }

    private fun setLan(language: String) {
        var metric = resources.displayMetrics
        var configuration = resources.configuration
        configuration.locale = Locale(language)
        // update configuration
        resources.updateConfiguration(configuration, metric)
        // notify configuration
        onConfigurationChanged(configuration)

    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
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