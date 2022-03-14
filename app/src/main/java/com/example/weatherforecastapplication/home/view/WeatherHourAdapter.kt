package com.example.weatherforecastapplication.home.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherforecastapplication.databinding.CardLayoutDayWeatherBinding
import com.example.weatherforecastapplication.databinding.CardLayoutHoursWeatherBinding
import com.example.weatherforecastapplication.model.*

class WeatherHourAdapter(
    private val fragment: Fragment,

    ) : RecyclerView.Adapter<WeatherHourAdapter.ViewHolder>() {
    private var weatherHours = emptyList<Hourly>()
    fun setWeatherHours(weatherHours: List<Hourly>) {
        this.weatherHours = weatherHours
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): WeatherHourAdapter.ViewHolder {
        return ViewHolder(
            CardLayoutHoursWeatherBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: WeatherHourAdapter.ViewHolder, position: Int) {
        holder.binding.txtWeatHour.text = convertToTime(weatherHours[position + 1].dt)
        holder.binding.txtWeatherTemp.text = weatherHours[position + 1].temp.toString().plus(
            getCurrentTemperature(fragment.requireContext())
        )
        holder.binding.imgWeaHour.setImageResource(getIconImage(weatherHours[position + 1]!!.weather[0]!!.icon!!))
    }

    override fun getItemCount(): Int {
        return weatherHours.size - 1
    }

    inner class ViewHolder(
        val binding: CardLayoutHoursWeatherBinding
    ) : RecyclerView.ViewHolder(binding.root) {

    }
}
