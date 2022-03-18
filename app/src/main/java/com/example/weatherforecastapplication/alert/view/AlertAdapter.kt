package com.example.weatherforecastapplication.alert.view

import android.location.Address
import android.location.Geocoder
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherforecastapplication.R
import com.example.weatherforecastapplication.databinding.CardLayoutAlertBinding
import com.example.weatherforecastapplication.databinding.CardLayoutFavouriteBinding
import com.example.weatherforecastapplication.favourite.view.FavouriteAdapter
import com.example.weatherforecastapplication.model.*
import java.util.*

class AlertAdapter(
    private val fragment: Fragment,
    private val onDelete: (Int) -> Unit

) : RecyclerView.Adapter<AlertAdapter.ViewHolder>() {
    private var weatherAlerts = emptyList<WeatherAlert>()
    fun setWeatherAlerts(weatherAlert: List<WeatherAlert>) {
        this.weatherAlerts = weatherAlert
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AlertAdapter.ViewHolder {
        return ViewHolder(
            CardLayoutAlertBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: AlertAdapter.ViewHolder, position: Int) {
        holder.binding.txtFrom.text =
            getText(weatherAlerts[position].startTime, weatherAlerts[position].startDay)
        holder.binding.txtTo.text =
            getText(weatherAlerts[position].endTime, weatherAlerts[position].endDay)


        // handle delete
        holder.binding.imgDelete.setOnClickListener {
            onDelete(weatherAlerts[position].id!!)
        }
    }

    override fun getItemCount(): Int {
        return weatherAlerts.size
    }

    inner class ViewHolder(
        val binding: CardLayoutAlertBinding
    ) : RecyclerView.ViewHolder(binding.root) {

    }

    private fun getText(time: Long, day: Long): String {
        return convertToDate(day, fragment.requireContext()).plus("\n")
            .plus(convertToTime(time, fragment.requireContext()))
    }


}