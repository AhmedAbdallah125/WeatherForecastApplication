package com.example.weatherforecastapplication.alert.view

import android.location.Address
import android.location.Geocoder
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherforecastapplication.R
import com.example.weatherforecastapplication.databinding.CardLayoutAlertBinding
import com.example.weatherforecastapplication.databinding.CardLayoutFavouriteBinding
import com.example.weatherforecastapplication.favourite.view.FavouriteAdapter
import com.example.weatherforecastapplication.model.OpenWeatherJason
import com.example.weatherforecastapplication.model.WeatherAlert
import com.example.weatherforecastapplication.model.getCurrentLan
import com.example.weatherforecastapplication.model.initFavSharedPref
import java.util.*

class AlertAdapter(
    private val fragment: Fragment,
    private val onDelete: (Int) -> Unit

) : RecyclerView.Adapter<AlertAdapter.ViewHolder>() {
    private var weatherAlerts = emptyList<WeatherAlert>()
    fun weatherAlerts(favWeather: List<WeatherAlert>) {
        this.weatherAlerts = weatherAlerts
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

        // change to city
//        var cityName = getCityText(
//            favWeather[position].lat, favWeather[position].lon, favWeather[position].timezone
//        )
//        holder.binding.txtFavTimeZone.text = cityName
        // handle click
//        holder.binding.txtFavTimeZone.setOnClickListener {
//            // init shared
//
//            initFavSharedPref(fragment.requireContext())
//                .edit()
//                .apply {
//                    putFloat(fragment.getString(R.string.LON), favWeather[position].lon.toFloat())
//                    putFloat(fragment.getString(R.string.LAT), favWeather[position].lat.toFloat())
//                    putString(fragment.getString(R.string.TIMEZONE), favWeather[position].timezone)
//                    putInt(fragment.getString(R.string.FAV_FLAG), 1)
//                    apply()
//                }
//            // make some cond
//
//            Navigation.findNavController(fragment.requireView())
//                .navigate(R.id.action_favouriteFragment_to_navigation_home)
//        }
//        // handle delete
//        holder.binding.imgDelete.setOnClickListener {
//            onDelete(weatherAlerts[position].id)
//        }
    }

    override fun getItemCount(): Int {
        return weatherAlerts.size
    }

    inner class ViewHolder(
        val binding: CardLayoutAlertBinding
    ) : RecyclerView.ViewHolder(binding.root) {

    }

    private fun getCityText(lat: Double, lon: Double, timezone: String): String {
        var city = "city"
        val geocoder =
            Geocoder(fragment.requireContext(), Locale(getCurrentLan(fragment.requireContext())))
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