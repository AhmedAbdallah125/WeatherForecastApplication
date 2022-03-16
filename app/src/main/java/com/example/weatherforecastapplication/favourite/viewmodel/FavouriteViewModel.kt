package com.example.weatherforecastapplication.favourite.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherforecastapplication.model.IRepository
import com.example.weatherforecastapplication.model.OpenWeatherJason
import com.example.weatherforecastapplication.model.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FavouriteViewModel(private val myRepository: IRepository) : ViewModel() {
    // get from shared the required Data
    var favWeather: MutableLiveData<List<OpenWeatherJason>> = MutableLiveData()
    fun getWeather(
        lat: Double,
        lon: Double,
        lang: String,
        unit: String
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            val response = myRepository.getFavWeathers(lat, lon, lang, unit)
            if (response is Result.Success) {
                favWeather.postValue(response.data!!)
            } else {
                // will be handled
                Log.i("AA", "Failed: ")
            }
        }

    }

    fun getLocalFavWeathers() {
        // for getting local Data

        viewModelScope.launch(Dispatchers.IO) {
            val response = myRepository.getLocalFavWeathers()
            if (response is Result.Success) {
                favWeather.postValue(response.data!!)
            } else {
                // will be handled
                Log.i("AA", "Failed: ")
            }
        }
    }

    fun deleteFavWeather(timezone: String) {
        viewModelScope.launch(Dispatchers.IO) {
            myRepository.deleteFavWeather(timezone)
            getLocalFavWeathers()
        }
    }
}