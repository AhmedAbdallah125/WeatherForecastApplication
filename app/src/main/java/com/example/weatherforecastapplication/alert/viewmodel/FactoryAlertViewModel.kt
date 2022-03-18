package com.example.weatherforecastapplication.alert.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.weatherforecastapplication.alert.view.viewmodel.AlertViewModel
import com.example.weatherforecastapplication.model.IRepository

class FactoryAlertViewModel (private var myRepository: IRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AlertViewModel::class.java)) {
            return AlertViewModel(myRepository) as T
        } else {
            throw IllegalArgumentException("View Model class Not found")
        }
    }
}