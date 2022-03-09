package com.example.weatherforecastapplication.home.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherforecastapplication.databinding.CardLayoutDayWeatherBinding
import com.example.weatherforecastapplication.databinding.CardLayoutHoursWeatherBinding
import com.example.weatherforecastapplication.model.WeatherDay
import com.example.weatherforecastapplication.model.WeatherHour

class WeatherHourAdapter(
    private val fragment: Fragment,

    ) : RecyclerView.Adapter<WeatherHourAdapter.ViewHolder>() {
    private var weatherHours = emptyList<WeatherHour>()
    fun setWeatherHours(weatherHours: List<WeatherHour>) {
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
        holder.binding.txtWeatHour.text = weatherHours[position].time
        holder.binding.txtWeatherTemp.text = weatherHours[position].temp
        holder.binding.imgWeaHour.setImageResource(weatherHours[position].img)
    }

    override fun getItemCount(): Int {
        return weatherHours.size
    }

    inner class ViewHolder(
        val binding: CardLayoutHoursWeatherBinding
    ) : RecyclerView.ViewHolder(binding.root) {

    }
}
