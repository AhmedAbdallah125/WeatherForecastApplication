package com.example.weatherforecastapplication.favourite.view

import android.location.Address
import android.location.Geocoder
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherforecastapplication.R
import com.example.weatherforecastapplication.databinding.CardLayoutDayWeatherBinding
import com.example.weatherforecastapplication.databinding.CardLayoutFavouriteBinding
import com.example.weatherforecastapplication.home.view.WeatherDayAdapter
import com.example.weatherforecastapplication.model.*
import java.util.*

class FavouriteAdapter(
    private val fragment: Fragment,
    private val onDelete: (Int, String) -> Unit

) : RecyclerView.Adapter<FavouriteAdapter.ViewHolder>() {
    private var favWeather = emptyList<OpenWeatherJason>()
    fun setFavWeather(favWeather: List<OpenWeatherJason>) {
        this.favWeather = favWeather
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): FavouriteAdapter.ViewHolder {
        return ViewHolder(
            CardLayoutFavouriteBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: FavouriteAdapter.ViewHolder, position: Int) {
        // change to city
        var cityName = getCityText(
            favWeather[position].lat, favWeather[position].lon, favWeather[position].timezone
        )
        holder.binding.txtFavTimeZone.text = cityName
        // handle click
        holder.binding.txtFavTimeZone.setOnClickListener {
            // init shared

            initFavSharedPref(fragment.requireContext())
                .edit()
                .apply {
                    putFloat(fragment.getString(R.string.LON), favWeather[position].lon.toFloat())
                    putFloat(fragment.getString(R.string.LAT), favWeather[position].lat.toFloat())

                    putInt(fragment.getString(R.string.ID), favWeather[position].id)
                    putInt(fragment.getString(R.string.FAV_FLAG), 1)
                    apply()
                }
            // make some cond

            Navigation.findNavController(fragment.requireView())
                .navigate(R.id.action_favouriteFragment_to_navigation_home)
        }
        // handle delete
        holder.binding.imgDelete.setOnClickListener {
            onDelete(favWeather[position].id, cityName)
        }
    }

    override fun getItemCount(): Int {
        return favWeather.size
    }

    inner class ViewHolder(
        val binding: CardLayoutFavouriteBinding
    ) : RecyclerView.ViewHolder(binding.root) {

    }

    private fun getCityText(lat: Double, lon: Double, timezone: String): String {
        var city = "city"
        Locale.setDefault(Locale(getCurrentLan(fragment.requireContext()),"eg"))
        val new_locale = Locale.getDefault()
        val geocoder =
            Geocoder(fragment.requireContext(),new_locale)
        val addresses: List<Address> = geocoder.getFromLocation(lat, lon, 1)
        if (addresses.isNotEmpty()) {
            val state = addresses[0].adminArea
            val country = addresses[0].countryName
            city = "$state, $country"
            if (state.isNullOrEmpty()) city = country
        }
        if (city == "city")
            city = timezone
        return city
    }
}