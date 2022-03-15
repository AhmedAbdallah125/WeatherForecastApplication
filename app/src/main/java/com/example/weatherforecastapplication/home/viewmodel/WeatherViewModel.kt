package com.example.weatherforecastapplication.home.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherforecastapplication.model.*
import com.example.weatherforecastapplication.model.Result.Success
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class WeatherViewModel(private val myRepository: IRepository) : ViewModel() {
    // get from shared the required Data
    var openWeather: MutableLiveData<OpenWeatherJason> = MutableLiveData()
    fun getWeather(
        lat: Double,
        lon: Double,
        lang: String ,
        unit: String,isFavourite :Boolean =false
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            val response = myRepository.getCurrentWeather(lat, lon, lang, unit,isFavourite)
            if (response is Success) {
                openWeather.postValue(response.data!!)
            } else {
                // will be handled
                Log.i("AA", "Failed: ")
            }

        }


    }


}