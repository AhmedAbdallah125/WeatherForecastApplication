package com.example.weatherforecastapplication.favourite.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherforecastapplication.model.IRepository
import com.example.weatherforecastapplication.model.OpenWeatherJason
import com.example.weatherforecastapplication.model.Result
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlin.system.measureTimeMillis

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
        runBlocking {
             flow<String> {
                val list = listOf<String>("A", "B", "c")
                for (i in list) {
                    delay(1000)
                    emit(i)
                }
            }.collect {

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

    fun deleteFavWeather(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            myRepository.deleteFavWeather(id)
            getLocalFavWeathers()
        }
    }
}