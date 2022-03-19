package com.example.weatherforecastapplication.home.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherforecastapplication.databinding.CardLayoutDayWeatherBinding
import com.example.weatherforecastapplication.model.*

class WeatherDayAdapter(
    private val fragment: Fragment,

    ) : RecyclerView.Adapter<WeatherDayAdapter.ViewHolder>() {
    private var weatherDays = emptyList<Daily>()
    fun setWeatherDay(weatherDays: List<Daily>) {
        this.weatherDays = weatherDays
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): WeatherDayAdapter.ViewHolder {
        return ViewHolder(
            CardLayoutDayWeatherBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: WeatherDayAdapter.ViewHolder, position: Int) {
        holder.binding.txtOneDay.text =
            convertLongToDay(weatherDays[position + 1].dt, fragment.requireContext())
        holder.binding.txtOneDes.text = weatherDays[position].weather[0].description
        holder.binding.txtOneHistory.text =
            convertToDate(weatherDays[position + 1].dt, fragment.requireContext())
        var temNumber = ""
        temNumber = if (getCurrentLan(fragment.requireContext()) == "ar") {
            convertNumbersToArabic(weatherDays[position + 1].temp!!.day!!)
        } else {
            (weatherDays[position + 1].temp!!.day ?: 0).toString()
        }
        holder.binding.txtOneTemp.text = temNumber.plus(" ")
            .plus(
                getCurrentTemperature(fragment.requireContext())
            )
        holder.binding.imgOne.setImageResource(getIconImage(weatherDays[position + 1].weather[0].icon!!))
    }

    override fun getItemCount(): Int {
        return weatherDays.size - 1
    }

    inner class ViewHolder(
        val binding: CardLayoutDayWeatherBinding
    ) : RecyclerView.ViewHolder(binding.root) {

    }
}
