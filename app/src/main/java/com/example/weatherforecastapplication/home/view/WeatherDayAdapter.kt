package com.example.weatherforecastapplication.home.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherforecastapplication.databinding.CardLayoutDayWeatherBinding
import com.example.weatherforecastapplication.model.WeatherDay

class WeatherDayAdapter(
    private val fragment: Fragment,

    ) : RecyclerView.Adapter<WeatherDayAdapter.ViewHolder>() {
    private var weatherDays = emptyList<WeatherDay>()
    fun setWeatherDay(weatherDays: List<WeatherDay>) {
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
        holder.binding.txtOneDay.text = weatherDays[position].day
        holder.binding.txtOneDes.text = weatherDays[position].des
        holder.binding.txtOneHistory.text = weatherDays[position].history
        holder.binding.txtOneTemp.text = weatherDays[position].temp
        holder.binding.imgOne.setImageResource(weatherDays[position].img)
    }

    override fun getItemCount(): Int {
        return weatherDays.size
    }

    inner class ViewHolder(
        val binding: CardLayoutDayWeatherBinding
    ) : RecyclerView.ViewHolder(binding.root) {

    }
}
