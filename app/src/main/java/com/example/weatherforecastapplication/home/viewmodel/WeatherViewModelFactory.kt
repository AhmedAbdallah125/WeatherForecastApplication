package com.example.weatherforecastapplication.home.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.weatherforecastapplication.model.IRepository

class WeatherViewModelFactory(private var myRepository: IRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(WeatherViewModel::class.java)) {
            return WeatherViewModel(myRepository) as T
        }else{
            throw IllegalArgumentException("View Model class Not found")
        }
    }
}