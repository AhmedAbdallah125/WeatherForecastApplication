package com.example.weatherforecastapplication.alert.view

import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.work.WorkManager
import com.example.weatherforecastapplication.R
import com.example.weatherforecastapplication.alert.view.viewmodel.AlertViewModel
import com.example.weatherforecastapplication.databinding.FragmentNotificationsBinding
import com.example.weatherforecastapplication.dialog.alertdialog.view.AlertTimeDialog
import com.example.weatherforecastapplication.model.WeatherAlert
import com.example.weatherforecastapplication.model.initSharedPref
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AlertFragment : Fragment() {

    private var _binding: FragmentNotificationsBinding? = null
    private val viewModel: AlertViewModel by viewModels()
    private lateinit var alertAdapter: AlertAdapter

    private val binding
        get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNotificationsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initAlertWeatherRecycle()
        binding.addAlert.setOnClickListener {
            val once = initSharedPref(requireContext()).getInt("A", 0)
            if (once == 0) {
                checkDrawOverlayPermission()
                initSharedPref(requireContext()).edit().apply {
                    putInt("A", 1)
                    apply()
                }
            } else {
                AlertTimeDialog().show(
                    requireActivity().supportFragmentManager,
                    "MyAlertDialogFragment"
                )
            }

        }
    }

    override fun onResume() {
        super.onResume()

        viewModel.getWeatherAlerts()

        lifecycleScope.launchWhenStarted {
            viewModel.weatherAlerts.collect {
                if(it.isNullOrEmpty()){
                    binding.animationView.visibility=View.VISIBLE
                    binding.recAlertsWeathers.visibility=View.GONE
                }else{
                    binding.animationView.visibility=View.GONE
                    binding.recAlertsWeathers.visibility=View.VISIBLE


                    bindAlertWeathers(it)

                }
            }
        }

    }

    private fun initAlertWeatherRecycle() {
        alertAdapter = AlertAdapter(requireParentFragment(), deleteAction)
        binding.recAlertsWeathers.apply {
            this.adapter = alertAdapter
            this.layoutManager = LinearLayoutManager(
                requireParentFragment().requireContext(),
                RecyclerView.VERTICAL, false
            )
        }
    }

    // lambda
    private var deleteAction: (Int) -> Unit = { id: Int ->
        // will delete in room and delete work manager
        // if this is end day will delete also
        viewModel.deleteAlertWeather(id)
        WorkManager.getInstance().cancelUniqueWork(id.toString())

        Toast.makeText(requireContext(), getString(R.string.succ_deleted), Toast.LENGTH_SHORT)
            .show()

    }

    private fun bindAlertWeathers(alertWeathers: List<WeatherAlert>) {
        alertAdapter.setWeatherAlerts(alertWeathers)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun checkDrawOverlayPermission() {
        // Check if we already  have permission to draw over other apps
        if (
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                !Settings.canDrawOverlays(requireContext())
            } else {
                TODO("VERSION.SDK_INT < M")
            }
        ) {
            // if not construct intent to request permission
            val alertDialogBuilder = MaterialAlertDialogBuilder(requireContext())
            alertDialogBuilder.setTitle(getString(R.string.weather_alerts))
                .setMessage(getString(R.string.to_enjoy_features))
                .setPositiveButton("Let's Go") { dialog: DialogInterface, i: Int ->
                    val intent = Intent(
                        Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                        Uri.parse("package:" + requireContext().applicationContext.packageName)


                    )
                    // request permission via start activity for result
                    startActivityForResult(
                        intent,
                        1
                    ) //It will call onActivityResult Function After you press Yes/No and go Back after giving permission
                    dialog.dismiss()

                }.setNegativeButton(
                    "Cancel"
                ) { dialog: DialogInterface, i: Int ->
                    dialog.dismiss()
                }.show()
        }
    }

}