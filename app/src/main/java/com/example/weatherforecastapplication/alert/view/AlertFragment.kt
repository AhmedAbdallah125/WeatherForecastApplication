package com.example.weatherforecastapplication.alert.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherforecastapplication.databinding.FragmentNotificationsBinding
import com.example.weatherforecastapplication.alert.view.viewmodel.AlertViewModel
import com.example.weatherforecastapplication.favourite.view.FavouriteAdapter
import com.example.weatherforecastapplication.model.OpenWeatherJason
import com.example.weatherforecastapplication.model.WeatherAlert

class AlertFragment : Fragment() {

    private var _binding: FragmentNotificationsBinding? = null
    private val viewModel: AlertViewModel by viewModels()
    private lateinit var alertAdapter: AlertAdapter


    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {


        _binding = FragmentNotificationsBinding.inflate(inflater, container, false)
        val root: View = binding.root


        return root
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
    var deleteAction: (Int) -> Unit = { id: Int ->

    }

    private fun bindAlertWeathers(alertWeathers: List<WeatherAlert>) {
        alertAdapter.setWeatherAlerts(alertWeathers)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}