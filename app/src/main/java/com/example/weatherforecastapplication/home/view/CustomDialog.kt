package com.example.weatherforecastapplication.home.view

import android.Manifest

import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import com.example.weatherforecastapplication.R
import com.example.weatherforecastapplication.databinding.DialogIntialSetupBinding
import com.example.weatherforecastapplication.home.viewmodel.HomeActivityViewModel


class CustomDialog : DialogFragment() {
    private val viewModel: HomeActivityViewModel by activityViewModels()

    lateinit var binding: DialogIntialSetupBinding
    lateinit var appContext: Context
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DialogIntialSetupBinding.inflate(inflater, container, false)
        dialog!!.window!!.setBackgroundDrawableResource(R.drawable.round_corner)
        isCancelable = false
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        appContext = requireActivity().applicationContext
        binding.setupDialogButton.setOnClickListener {
            if (binding.radioButtonGPS.isChecked) {
//                checkLocationPermission()
                viewModel.selectedLocProvider(0)
            } else if (binding.radioButtonMaps.isChecked) {
//                viewModel.selectedLocProvider(1)
                Navigation.findNavController(view).navigate(R.id.action_customDialog_to_mapsFragment)
            }
            dialog!!.dismiss()
        }
    }


    override fun onStart() {
        super.onStart()
        val width = (resources.displayMetrics.widthPixels * 0.85).toInt()
        dialog!!.window!!.setLayout(width, ViewGroup.LayoutParams.WRAP_CONTENT)
    }


}