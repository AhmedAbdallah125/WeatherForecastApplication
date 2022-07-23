package com.example.weatherforecastapplication.home.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherforecastapplication.model.IRepository
import com.example.weatherforecastapplication.model.OpenWeatherJason
import com.example.weatherforecastapplication.model.Result.Success
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor(private val myRepository: IRepository) : ViewModel() {
    // get from shared the required Data
    var openWeather: MutableLiveData<OpenWeatherJason> = MutableLiveData()
    fun getWeather(
        lat: Double,
        lon: Double,
        lang: String,
        unit: String, isFavourite: Boolean = false
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            if (isFavourite) {
                val response = myRepository.getCurrentFavWeather(lat, lon, lang, unit, true)
                if (response is Success) {
                    openWeather.postValue(response.data!!)
                } else {
                    // will be handled
                    Log.i("AA", "Failed: ")
                }
            } else {
                val response = myRepository.getCurrentWeather(lat, lon, lang, unit, isFavourite)
                if (response is Success) {
                    openWeather.postValue(response.data!!)
                } else {
                    // will be handled
                    Log.i("AA", "Failed: ")
                }
            }


        }


    }


}