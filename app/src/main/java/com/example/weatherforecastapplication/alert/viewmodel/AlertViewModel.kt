package com.example.weatherforecastapplication.alert.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherforecastapplication.model.IRepository
import com.example.weatherforecastapplication.model.OpenWeatherJason
import com.example.weatherforecastapplication.model.WeatherAlert
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class AlertViewModel(private val myRepository: IRepository) : ViewModel() {
    private var _weatherAlerts: MutableStateFlow<List<WeatherAlert>> = MutableStateFlow(emptyList())
    val weatherAlerts = _weatherAlerts

    private val _text = MutableLiveData<String>().apply {
        value = "This is notifications Fragment"
    }

    fun getWeatherAlerts() {
        viewModelScope.launch(Dispatchers.IO) {
            myRepository.getWeatherAlerts().collect {
                weatherAlerts.emit(it)
            }
        }
    }

    fun deleteAlertWeather(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            myRepository.deleteWeatherAlert(id)
        }
    }

}