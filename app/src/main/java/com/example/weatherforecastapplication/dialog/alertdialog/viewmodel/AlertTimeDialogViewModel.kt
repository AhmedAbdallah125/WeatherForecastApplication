package com.example.weatherforecastapplication.dialog.alertdialog.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherforecastapplication.model.IRepository
import com.example.weatherforecastapplication.model.WeatherAlert
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AlertTimeDialogViewModel(private val myRepository: IRepository) : ViewModel() {
    private var _id: MutableLiveData<Int> = MutableLiveData()
    val id = _id
    fun insertWeatherAlert(weatherAlert: WeatherAlert) {
        viewModelScope.launch(Dispatchers.IO) {
            id.postValue(myRepository.insertWeatherAlert(weatherAlert))
        }
    }

}