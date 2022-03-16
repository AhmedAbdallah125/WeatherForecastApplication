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
        holder.binding.txtFavTimeZone.text = getCityText(
            favWeather[position].lat, favWeather[position].lon
        )
        // handle click
        holder.binding.cardFavView.setOnClickListener {
            // init shared

            initFavSharedPref(fragment.requireContext())
                .edit()
                .apply {
                    putFloat(fragment.getString(R.string.LON), favWeather[position].lon.toFloat())
                    putFloat(fragment.getString(R.string.LAT), favWeather[position].lat.toFloat())
                    putString(fragment.getString(R.string.TIMEZONE), favWeather[position].timezone)
                    putInt(fragment.getString(R.string.FAV_FLAG), 1)
                    apply()
                }
            // make some cond

            Navigation.findNavController(fragment.requireView())
                .navigate(R.id.action_favouriteFragment_to_navigation_home)
        }
    }

    override fun getItemCount(): Int {
        return favWeather.size
    }

    inner class ViewHolder(
        val binding: CardLayoutFavouriteBinding
    ) : RecyclerView.ViewHolder(binding.root) {

    }

    private fun getCityText(lat: Double, lon: Double): String {
        var city = "Unknown!"
        val geocoder =
            Geocoder(fragment.requireContext(), Locale(getCurrentLan(fragment.requireContext())))
        val addresses: List<Address> = geocoder.getFromLocation(lat, lon, 1)
        if (addresses.isNotEmpty()) {
            val state = addresses[0].adminArea
            val country = addresses[0].countryName
            city = "$state, $country"
        }
        return city
    }
}