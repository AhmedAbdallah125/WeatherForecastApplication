package com.example.weatherforecastapplication.dialog.alertdialog.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherforecastapplication.model.IRepository
import com.example.weatherforecastapplication.model.WeatherAlert
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AlertTimeDialogViewModel(private val myRepository: IRepository) : ViewModel() {

    fun insertWeatherAlert(weatherAlert: WeatherAlert) {
        viewModelScope.launch(Dispatchers.IO) {
            myRepository.insertWeatherAlert(weatherAlert)
        }
    }

}