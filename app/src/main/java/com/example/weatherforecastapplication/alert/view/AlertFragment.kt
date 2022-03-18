package com.example.weatherforecastapplication.alert.view

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherforecastapplication.R
import com.example.weatherforecastapplication.alert.view.viewmodel.AlertViewModel
import com.example.weatherforecastapplication.alert.viewmodel.FactoryAlertViewModel
import com.example.weatherforecastapplication.databinding.FragmentNotificationsBinding
import com.example.weatherforecastapplication.datasource.local.ConcreteLocalSource
import com.example.weatherforecastapplication.datasource.network.RetrofitHelper
import com.example.weatherforecastapplication.dialog.alertdialog.view.AlertTimeDialog
import com.example.weatherforecastapplication.model.Repository
import com.example.weatherforecastapplication.model.WeatherAlert
import kotlinx.coroutines.flow.collect

class AlertFragment : Fragment() {

    private var _binding: FragmentNotificationsBinding? = null
    private val viewModel: AlertViewModel by viewModels {
        FactoryAlertViewModel(
            (Repository(ConcreteLocalSource(requireContext()), RetrofitHelper))
        )
    }
    private lateinit var alertAdapter: AlertAdapter


    // This property is only valid between onCreateView and
    // onDestroyView.
    private
    val binding
        get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNotificationsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initAlertWeatherRecycle()
        binding.addAlert.setOnClickListener{
            //show dilog to insert
            AlertTimeDialog().show(requireFragmentManager(), "MyAlertDialogFragment")

            //then show data from local

        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.getWeatherAlerts()
        lifecycleScope.launchWhenStarted {
            viewModel.weatherAlerts.collect{
                bindAlertWeathers(it)
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
        Toast.makeText(requireContext(), getString(R.string.succ_deleted), Toast.LENGTH_SHORT).show()

    }

    private fun bindAlertWeathers(alertWeathers: List<WeatherAlert>) {
        alertAdapter.setWeatherAlerts(alertWeathers)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}